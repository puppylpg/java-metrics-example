package example.cache.ehcache;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import lombok.Getter;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.StatisticsService;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.core.statistics.DefaultStatisticsService;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author liuhaibo on 2020/04/30
 */
public class EhcacheService {

    private static final CacheManager CACHE_MANAGER;
    @Getter
    private static final CacheConfiguration<Integer, Double> CONFIG = CacheConfigurationBuilder.newCacheConfigurationBuilder(
            Integer.class,
            Double.class,
            // size of the cache
            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(1, MemoryUnit.MB)
    ).withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(10))).build();

    private static final Cache<Integer, Double> FFM_CACHE;

    /**
     * https://stackoverflow.com/a/40462767/7676237
     */
    @Deprecated
    private static final StatisticsService STATISTICS_SERVICE = new DefaultStatisticsService();

    static {
        CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder()
                .using(STATISTICS_SERVICE)
                .build();

        CACHE_MANAGER.init();

        FFM_CACHE = CACHE_MANAGER
                .createCache(
                        "ffmCache",
                        CONFIG
                );
    }

    public static Cache<Integer, Double> getFfmCache() {
        return FFM_CACHE;
    }

    private static CacheStatistics ffmCacheStatistics() {
        return STATISTICS_SERVICE.getCacheStatistics("ffmCache");
    }

    public static void main(String... args) {
        MetricRegistry metricRegistry = new MetricRegistry();

        // lets add a Console reporter to the metrics
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);

        // lets add 10K elements
        for (int i = 0; i < 100000; i++) {
            getFfmCache().put(i, 0.1);
        }

//        try {
//            Thread.sleep(50 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println(ffmCacheStatistics().getCacheGets());
        System.out.println(ffmCacheStatistics().getCachePuts());
        System.out.println(ffmCacheStatistics().getCacheEvictions());
        System.out.println(ffmCacheStatistics().getCacheExpirations());
        System.out.println(ffmCacheStatistics().getCacheHits());
        System.out.println(ffmCacheStatistics().getCacheHitPercentage());
        System.out.println(ffmCacheStatistics().getCacheMisses());
        System.out.println(ffmCacheStatistics().getCacheMissPercentage());
        System.out.println(ffmCacheStatistics().getCacheRemovals());
    }
}
