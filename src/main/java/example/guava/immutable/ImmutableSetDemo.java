package example.guava.immutable;

import com.google.common.collect.ImmutableSet;

/**
 * @author liuhaibo on 2018/10/23
 */
public class ImmutableSetDemo {

    public static void main(String... args) {
        ImmutableSet<String> popularBrands = ImmutableSet.of(
                "zte",
                "xiaomi",
                "vivo",
                "smartisan",
                "oppo",
                "oneplus",
                "nubia",
                "meitu",
                "meizu",
                "leshi",
                "lenovo",
                "ktouch",
                "huawei",
                "htc",
                "hisense",
                "gionee",
                "coolpad",
                "chinamobile",
                "apple",
                "360");
        System.out.println(popularBrands.contains("xiaomi"));
    }
}
