package example.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author liuhaibo on 2020/04/30
 */
public class EhcacheService {

    private CacheManager cacheManager;
    private Cache<Integer, Integer> cache;

    public EhcacheService() {
        cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().build();
        cacheManager.init();

        cache = cacheManager
                .createCache(
                        "simpleCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Integer.class,
                                Integer.class,
                                ResourcePoolsBuilder.heap(5)
                        )
                );
    }

    public Cache<Integer, Integer> getCache() {
        return cacheManager.getCache("simpleCache", Integer.class, Integer.class);
    }
}
