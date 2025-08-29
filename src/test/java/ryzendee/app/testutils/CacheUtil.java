package ryzendee.app.testutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Утилита для тестов, упрощающая взаимодействие с кэшами
 *
 * @author Dmitry Ryazantsev
 */
public class CacheUtil {

    @Autowired
    private Map<String, CacheManager> cacheManagers;

    public void putInCache(String cacheManagerName, String cache, String key, Object value) {
        getCache(cacheManagerName, cache).put(key, value);
    }

    public Cache.ValueWrapper getFromCache(String cacheManagerName, String cache, String key) {
        return getCache(cacheManagerName, cache).get(key);
    }

    public <T> T getFromCache(String cacheManagerName, String cache, String key, Class<T> type) {
        return getCache(cacheManagerName, cache).get(key, type);
    }

    public void clearCache(String cacheManagerName, String cache) {
        getCache(cacheManagerName, cache).clear();
    }

    private Cache getCache(String cacheManagerName, String cache) {
        CacheManager cm = cacheManagers.get(cacheManagerName);
        if (cm == null) {
            throw new IllegalArgumentException("No CacheManager with name: " + cacheManagerName);
        }
        return cm.getCache(cache);
    }

    @TestConfiguration
    public static class Config {
        @Bean
        public CacheUtil cacheUtil() {
            return new CacheUtil();
        }
    }

}
