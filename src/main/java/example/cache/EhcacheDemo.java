package example.cache;

import example.cache.ehcache.EhcacheService;
import example.cache.ehcache.EhcacheXmlService;

/**
 * @author liuhaibo on 2020/04/30
 */
public class EhcacheDemo {

//    private static EhcacheService ehcacheService = new EhcacheService();

    private static EhcacheXmlService ehcacheService = new EhcacheXmlService();

    public static void main(String... args) {
        for (int i = 0; i < 10; i++) {
            if (ehcacheService.getCache().containsKey(i)) {
                System.out.println("Cache for " + i + ": " + ehcacheService.getCache().get(i));
            } else {
                int value = i * i;
                ehcacheService.getCache().put(i, value);
                System.out.println("Calculate for " + i + ": " + value);
            }
        }

        ehcacheService.getCache().forEach(entry -> {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        });
    }
}
