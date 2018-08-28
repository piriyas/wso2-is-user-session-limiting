package org.wso2.carbon.sessionlimit.user.operation.event.listener.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SessionCache {
    private static LoadingCache<String, Integer> sessionCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(7, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, Integer>() {
                        @Override
                        public Integer load(String key) throws Exception {
                            return DataPublisherDbUtil.getActiveSessionCount(key);
                        }
                    }
            );

    public static Integer getActiveSessionCount(String username) throws ExecutionException {
        return sessionCache.get(username);
    }

    public static void updateActiveSessionCount(String username) throws ExecutionException {
            sessionCache.refresh(username);
    }
}
