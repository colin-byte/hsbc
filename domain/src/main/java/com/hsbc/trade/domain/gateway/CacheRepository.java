package com.hsbc.trade.domain.gateway;

public interface CacheRepository {

    String get(String key);

    boolean lock(String key, String lockValue);

    boolean unlock(String sourceAccount, String lockValue);

    void set(String accountId, String valueOf);
}
