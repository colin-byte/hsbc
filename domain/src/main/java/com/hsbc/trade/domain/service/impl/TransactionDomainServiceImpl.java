package com.hsbc.trade.domain.service.impl;

import com.hsbc.trade.client.dto.data.ErrorCode;
import com.hsbc.trade.domain.entity.AccountEntity;
import com.hsbc.trade.domain.exception.AccountNotFoundException;
import com.hsbc.trade.domain.exception.AwaitToRetryException;
import com.hsbc.trade.domain.exception.ParamValidException;
import com.hsbc.trade.domain.gateway.AccountRepository;
import com.hsbc.trade.domain.gateway.CacheRepository;
import com.hsbc.trade.domain.gateway.TransactionRepository;
import com.hsbc.trade.domain.service.TransactionDomainService;
import com.hsbc.trade.domain.entity.TransactionEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionDomainServiceImpl implements TransactionDomainService {

    private static final Logger log = LoggerFactory.getLogger(TransactionDomainServiceImpl.class);
    @Resource
    AccountRepository accountRepository;

    @Resource
    TransactionRepository transactionRepository;

    @Resource
    CacheRepository cacheRepository;

    /**
     * 依赖mysql的事务实现转账功能
     *
     * @param params
     * @return
     */
    @Override
    @Retryable(include = {Exception.class}, exclude = {ParamValidException.class, AccountNotFoundException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public TransactionEntity modify(TransactionEntity params) {
        //查询账户信息
        AccountEntity source = accountRepository.getAccountById(params.getSourceAccount());
        //判断source的数量是否足够、用户是否存在
        if (source == null || source.getAmount().compareTo(params.getAmount()) < 0) {
            log.error("process transaction failed,account info is null");
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        //判断dest用户是否存在
        AccountEntity dest = accountRepository.getAccountById(params.getDestAccount());
        if (dest == null) {
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        String transId = UUID.randomUUID().toString().replace("-", "");
        TransactionEntity newTransEntity = new TransactionEntity();
        newTransEntity.setTransactionId(transId);
        newTransEntity.setSourceAccount(params.getSourceAccount());
        newTransEntity.setDestAccount(params.getDestAccount());
        newTransEntity.setAmount(params.getAmount());
        newTransEntity.setTimestamp(System.currentTimeMillis());
        //更新source和dest账户的金额
        source.setAmount(params.getAmount());
        dest.setAmount(params.getAmount());
        transactionRepository.processTransaction(newTransEntity, source, dest);
        return params;
    }

    /**
     * 依赖redis实现转账功能
     *
     * @param entity
     * @return
     */
    @Override
    @Retryable(include = {Exception.class}, exclude = {ParamValidException.class, AccountNotFoundException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public TransactionEntity process(TransactionEntity entity) {
        //前缀数量校验
        beforeExec(entity);
        boolean success = transactionRepository.processTransaction(entity);
        if (!success) {
            log.error("process transaction error,entity:{}", entity);
            throw new RuntimeException("转账失败");
        } else {
            //todo 通过MQ的方式发送写交易流水的消息,MQ的消费者处理交易明细消息写数据库，保证最终一致性
            log.info("process transaction success,send transaction task to MQ,entity:{}", entity);
            transactionRepository.sendTransMsg2RecordTransaction(entity);
        }
        return entity;
    }

    /**
     * 1.检查redis中是否已经有源账户、目标账户数据，如果有，则校验数量
     * 2.如果没有账户数据，则从mysql中将数据设置到redis中，缓存起来（第一次交易需要查询db，同步数据，后面直接用缓存中的数据）
     * 3.当前线程如果获取锁失败，则抛出AwaitToRetryException异常，利用retry机制，过一会重试执行
     *
     * @param entity
     */

    private void beforeExec(TransactionEntity entity) {
        try {
            String sourceAccountId = entity.getSourceAccount();
            String destAccountId = entity.getDestAccount();
            String sourceBalanceStr = cacheRepository.get(sourceAccountId);
            BigDecimal sourceBalance = null;
            if (sourceBalanceStr == null) {
                Pair<Boolean, BigDecimal> pair = setAccountInfo4Cache(sourceAccountId);
                boolean locked = pair.getLeft();
                if (!locked) {
                    //未获取到锁的线程，抛出异常，然后重试
                    throw new AwaitToRetryException(ErrorCode.AWAIT_TO_RETRY);
                }
                sourceBalance = pair.getRight();
            }
            if (sourceBalance == null || sourceBalance.compareTo(entity.getAmount()) < 0) {
                throw new ParamValidException(ErrorCode.PARAMETER_VALUE_INVALID);
            }
            String destBalance = cacheRepository.get(destAccountId);
            if (destBalance == null) {
                Pair<Boolean, BigDecimal> pair = setAccountInfo4Cache(destAccountId);
                boolean locked = pair.getLeft();
                if (!locked) {
                    throw new AwaitToRetryException(ErrorCode.AWAIT_TO_RETRY);
                }
            }
        } catch (Exception e) {
            log.error("before exec error:" + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 如果cache中没有account账户信息(key=accountId,value=account balance amount)
     * 则需要通过redis加锁的方式，同一个账户只有一个线程去做数据同步处理
     * 获取到mysql中的账户数据，同步设置到redis后，释放锁资源
     * 未获取到锁资源的线程继续往下执行代码逻辑，在执行lua脚本进行数量更新时也会判断源、目的账户信息，如果异常，则会报错，重试转账（3次）
     *
     * @param key
     */
    private Pair<Boolean, BigDecimal> setAccountInfo4Cache(String key) {
        String lockValue = UUID.randomUUID().toString().replace("-", "");
        String lockKey = key + "_l";
        //获取锁，获取到了才能更新redis中数据
        try {
            if (cacheRepository.lock(lockKey, lockValue)) {
                AccountEntity accountEntity = accountRepository.getAccountById(key);
                if (accountEntity == null || StringUtils.isEmpty(accountEntity.getAccountId())) {
                    throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
                }
                cacheRepository.set(accountEntity.getAccountId(), String.valueOf(accountEntity.getAmount()));
                return Pair.of(true, accountEntity.getAmount());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            boolean res = cacheRepository.unlock(lockValue, lockValue);
            log.info("unlock key,res:{}", !res);
        }
        return Pair.of(false, null);
    }


    @Recover
    private TransactionEntity transactionErrorRecover(Exception e, TransactionEntity entity) {
        //todo 处理最终失败的情况
        // 可以记录日志,或者是投递MQ,采用最终一致性的方式处理
        log.info("交易异常重复执行失败,message:{},entity:{}", e.getMessage(), entity);
        throw new RuntimeException("交易异常,error:" + e.getMessage());
    }
}
