package com.hsbc.trade.infrastructure.gateway.impl;

import com.hsbc.trade.domain.gateway.CacheRepository;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.AccountDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.AccountMapper;
import com.hsbc.trade.infrastructure.gateway.impl.redis.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository("cacheRepository")
public class CacheRepositoryImpl implements CacheRepository, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(CacheRepositoryImpl.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisLock redisLock;

    @Resource
    private AccountMapper accountMapper;


    @Value("${account.lock.timeout}")
    private int timeout;

    private static String TRANS_SCRIPT = "local sourceAccount = KEYS[1]\n" +
            "local destAccount = KEYS[2]\n" +
            "local amount = tonumber(KEYS[3])\n" +
            "local sourceBalance = tonumber(redis.call('get', sourceAccount))\n" +
            "local destBalance = tonumber(redis.call('get', destAccount))\n" +
            "if sourceBalance and destBalance and sourceBalance >= amount then\n" +
            "    if redis.call('set', sourceAccount, sourceBalance - amount) then\n" +
            "        if redis.call('set', destAccount, destBalance + amount) then\n" +
            "            return 1\n" +
            "        else\n" +
            "            redis.call('set', sourceAccount, sourceBalance)\n" +
            "            return 2\n" +
            "        end\n" +
            "    else\n" +
            "        return 3\n" +
            "    end\n" +
            "else\n" +
            "    return 4\n" +
            "end";
    private static DefaultRedisScript<Object> TRANSFER_SCRIPT = new DefaultRedisScript<>();

    public boolean transfer(String sourceAccount, String destAccount, double amount) {
        TRANSFER_SCRIPT.setScriptText(TRANS_SCRIPT);
        TRANSFER_SCRIPT.setResultType(Object.class);
        List<String> keys = new ArrayList<>();
        keys.add(sourceAccount);
        keys.add(destAccount);
        keys.add(String.valueOf(amount));
        Object result = redisTemplate.execute(TRANSFER_SCRIPT, keys);
        if (result instanceof List) {
            List<Long> ls = (List<Long>) result;
            if (ls != null && ls.size() > 0) {
                Long resultCode = ls.get(0);
                if (resultCode == 1) {
                    log.info("transaction transfer success,source account:{},dest account:{},amount:{}", sourceAccount, destAccount, amount);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String get(String key) {
        Object res = redisTemplate.opsForValue().get(key);
        if (res == null) {
            return null;
        }
        return (String) res;
    }

    @Override
    public boolean lock(String key, String lockValue) {
        return redisLock.lock(key, lockValue, timeout, TimeUnit.MINUTES);
    }

    @Override
    public boolean unlock(String key, String value) {
        return redisLock.unlock(key, value);
    }

    @Override
    public void set(String k, String v) {
        redisTemplate.opsForValue().set(k, v);
    }

    /**
     * 工程启动时，加载热点数据到redis中（比如交易最频繁的2000个客户的账户数据）
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        loadHotAccountInfo2Cache();
    }

    public void loadHotAccountInfo2Cache() {
        try {
            List<String> accounts = getHotAccounts();
            //todo 根据情况决定是否使用多线程方式加载
            for (String account : accounts) {
                loadAccountInfo2Cache(account);
            }
        } catch (Exception e) {
            log.error("loadHotAccountInfo2Cache error", e);
        }
    }

    /**
     * todo 可以通过配置的方式加载热点账户数据
     *
     * @return
     */
    public List<String> getHotAccounts() {
        String hsbc = "c684fa083ca14fe8ad8332b1f1d7c4aa";
        String colin = "3da6111a917b4aa09a0cce02e133c85a";
        List<String> accounts = Arrays.asList(hsbc, colin);
        return accounts;
    }

    public void loadAccountInfo2Cache(String accountId) {
        try {
            //todo 可以使用批量查询的方式
            AccountDO accountDO = accountMapper.getAccount(accountId);
            if (accountDO != null) {
                redisTemplate.opsForValue().set(accountId, String.valueOf(accountDO.getAmount()));
            }
        } catch (Exception e) {
            log.error("loadAccountInfo2Cache error", e);
        }
    }
}
