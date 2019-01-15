package example.classloader;

/**
 * @author liuhaibo on 2019/01/04
 */
public class ReplaceJvmClassLoader {

    /**
     * <p>param: <code>-Djava.system.class.loader=example.classloader.CustomClassLoader</code>
     * <p>cmd: <code>java -Djava.system.class.loader=example.classloader.CustomClassLoader example.classloader.ReplaceJvmClassLoader</code>
     *
     * Output:
     * without param: This is my ClassLoader: sun.misc.Launcher$AppClassLoader@18b4aac2
     *
     * with param:
     * loading class 'java.lang.InternalError' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'sun.instrument.InstrumentationImpl' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'com.intellij.rt.execution.application.AppMainV2$Agent' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'example.classloader.ReplaceJvmClassLoader' by custom classloader: class example.classloader.CustomClassLoader
     * loading class 'java.lang.Object' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.String' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.System' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.StringBuilder' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.Class' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.io.PrintStream' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * This is my ClassLoader: example.classloader.CustomClassLoader@7ea987ac
     */
    public static void main(String... args) {
        System.out.print("This is my ClassLoader: " + ReplaceJvmClassLoader.class.getClassLoader());
    }
}
