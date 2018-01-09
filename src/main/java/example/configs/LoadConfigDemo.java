package example.configs;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author liuhaibo on 2018/01/04
 */
public class LoadConfigDemo {

    private static final String FILE_NAME = "config/example.properties";
//    private static final String FILE_NAME = "configInResources/example.properties";
    private static  Properties config = new Properties();
    private static final Splitter LIST_SPLITTER = Splitter.on(",");
    private static final Splitter.MapSplitter MAP_SPLITTER = Splitter.on(",").withKeyValueSeparator(":");

    private static Transformer STRING_TO_INT_TRANSFORMER = new Transformer() {
        @Override
        public Object transform(Object input) {
            return Integer.parseInt(input.toString());
        }
    };

    public static void main(String[] args) {
        try {
            config.load(Files.newBufferedReader(Paths.get(FILE_NAME)));
        } catch (IOException e) {
            System.out.println(String.format("File %s doesn't exist!", FILE_NAME));
        }

        List<String> list = Lists.newArrayList(LIST_SPLITTER.split(config.getProperty("example.list", "a,b,c")));

        Map<String, String> tmpMap = new HashMap<>(MAP_SPLITTER.split(config.getProperty("example.map", "a:1,b:1,c:3")));
        Map<String, Integer> map = TransformedMap.decorateTransform(tmpMap, null, STRING_TO_INT_TRANSFORMER);

        System.out.println(System.getProperty("java.class.path"));
        System.out.println(list);
        System.out.println(map);
    }

}