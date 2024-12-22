package com.hsbc.trade.infrastructure.gateway.impl;

import com.hsbc.trade.domain.entity.AccountEntity;
import com.hsbc.trade.domain.entity.TransactionEntity;
import com.hsbc.trade.domain.gateway.TransactionRepository;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.AccountDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.TransactionDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.AccountMapper;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.TransactionMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Repository("transactionRepository")
public class TransactionRepositoryImpl implements TransactionRepository {
    private static final Logger log = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private AccountMapper accountMapper;


    @Resource
    CacheRepositoryImpl redisManager;

    @Resource
    ThreadPoolExecutor threadPoolExecutor;

    @Resource
    TransactionProxy transactionProxy;

    @Transactional(rollbackFor = Throwable.class)
    public void processTransaction(TransactionEntity transactionEntity, AccountEntity sourceAccount, AccountEntity destAccount) {
        AccountDO sourceDo = new AccountDO();
        sourceDo.setAccountId(sourceAccount.getAccountId());
        sourceDo.setId(sourceAccount.getId());
        sourceDo.setAmount(sourceAccount.getAmount());
        sourceDo.setAccountId(sourceDo.getAccountId());
        int row = accountMapper.decreaseBalance(sourceDo);
        if (row < 1) {
            throw new PersistenceException("更新源账户异常");
        }
        AccountDO destDo = new AccountDO();
        destDo.setAccountId(destAccount.getAccountId());
        destDo.setId(destAccount.getId());
        destDo.setAmount(destAccount.getAmount());
        destDo.setAccountId(destAccount.getAccountId());
        row = accountMapper.increaseBalance(destDo);
        if (row < 1) {
            throw new PersistenceException("更新目标账户异常");
        }
        TransactionDO transactionDO = new TransactionDO();
        transactionDO.setTransactionId(transactionEntity.getTransactionId());
        transactionDO.setSourceAccount(transactionEntity.getSourceAccount());
        transactionDO.setDestAccount(transactionEntity.getDestAccount());
        transactionDO.setAmount(transactionEntity.getAmount());
        transactionDO.setTimestamp(transactionEntity.getTimestamp());
        row = transactionMapper.insert(transactionDO);
        if (row < 1) {
            throw new PersistenceException("更新交易异常");
        }
    }

    @Override
    public boolean processTransaction(TransactionEntity entity) {
        return redisManager.transfer(entity.getSourceAccount(), entity.getDestAccount(), entity.getAmount().doubleValue());
    }


    /**
     * todo 这里为简单起见，使用异步线程来代替MQ，写交易流水记录到表中
     *
     * @param entity
     */
    @Override
    public void sendTransMsg2RecordTransaction(TransactionEntity entity) {
        threadPoolExecutor.execute(() -> transactionProxy.processTransaction(entity));
    }
}
