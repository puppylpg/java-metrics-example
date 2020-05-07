package example.cache.jcache;

import example.cache.ehcache.EhcacheService;
import org.apache.commons.lang3.RandomUtils;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.management.CacheStatisticsMXBean;
import javax.cache.spi.CachingProvider;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 * JCache with ehcache implementation. MBeans for JCache is registered by default.
 * <p>
 * http://www.ehcache.org/documentation/3.7/107.html
 *
 * @author liuhaibo on 2020/05/06
 */
public class JCacheDemo {
    
    private static Logger console = LoggerFactory.getLogger("console");

    public static void main(String... args) throws InterruptedException {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        Cache<Integer, Double> cache = cacheManager.createCache("jCache", Eh107Configuration.fromEhcacheCacheConfiguration(EhcacheService.getCONFIG()));



//        CacheStatisticsMXBean cacheStatBean = getCacheStatisticsMXBean("jCache");
//        if (cacheStatBean != null) {
//            console.info("Cache hits #{} misses #{}", cacheStatBean.getCacheHits(), cacheStatBean.getCacheMisses());
//            console.info("Cache hits %{} misses %{}", cacheStatBean.getCacheHitPercentage(), cacheStatBean.getCacheMissPercentage());
//            console.info("Cache gets #{}, puts #{}, removal #{}", cacheStatBean.getCacheGets(), cacheStatBean.getCachePuts(), cacheStatBean.getCacheRemovals());
//            console.info("Cache evictions #{}", cacheStatBean.getCacheEvictions());
//            console.info("Cache average get time {} milliseconds", cacheStatBean.getAverageGetTime());
//        }

        // lets add 10K elements
        for (int i = 0; i < 100000 * 100; i++) {
            Thread.sleep(3);
            int index = RandomUtils.nextInt(0, 100000);
            Double result = cache.get(index);
            if (result == null) {
                cache.put(index, 0.1);
            }
        }

//        try {
//            Thread.sleep(500 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

//    private static CacheStatisticsMXBean getCacheStatisticsMXBean(final String cacheName) {
//        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
//        ObjectName name = null;
//        try {
//            name = new ObjectName("wtf:a=b");
//        } catch (MalformedObjectNameException ex) {
//            console.error("Someting wrong with ObjectName {}", ex);
//        }
//        Set<ObjectName> beans = mbeanServer.queryNames(name, null);
//        if (beans.isEmpty()) {
//            console.info("Cache Statistics Bean not found");
//            return null;
//        }
//        ObjectName[] objArray = beans.toArray(new ObjectName[beans.size()]);
//        return JMX.newMBeanProxy(mbeanServer, objArray[0], CacheStatisticsMXBean.class);
//    }

}
