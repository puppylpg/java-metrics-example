package example.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * @author liuhaibo on 2020/04/30
 */
public class EhcacheXmlService {

    private URL myUrl = getClass().getClassLoader().getResource("config/ehcache.xml");
    private XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
    private CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);

    public Cache<Integer, Integer> getCache() {
        return cacheManager.getCache("simpleCache", Integer.class, Integer.class);
    }
}
