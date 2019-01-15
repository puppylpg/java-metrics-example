package example.classloader;

/**
 * @author liuhaibo on 2019/01/04
 */
public class ClassLoaderNameSpace {

    /**
     * Every different class loader represents a different namespace.
     *
     * <p>Usage: when implementing frameworks or application servers,
     * where every deployed application should be isolated from others.(Tomcat)
     *
     * Output:
     * loading class 'example.classloader.Foooo' by custom classloader: class example.classloader.CustomClassLoader
     * loading class 'java.lang.Object' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.String' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.System' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.StringBuilder' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.io.PrintStream' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * before change: s=Karrigan
     * after change: s=GuardiaN
     * loading class 'example.classloader.Foooo' by custom classloader: class example.classloader.CustomClassLoader
     * loading class 'java.lang.Object' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.String' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.System' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.StringBuilder' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.io.PrintStream' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * before change: s=Karrigan
     * after change: s=Niko
     *
     * @param args
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        CustomClassLoader loader1 = new CustomClassLoader(ClassLoaderNameSpace.class.getClassLoader());
        Class<?> classFoo1 = loader1.loadClass("example.classloader.Foooo");
        classFoo1.getMethod("changeString", String.class).invoke(null, "GuardiaN");

        CustomClassLoader loader2 = new CustomClassLoader(ClassLoaderNameSpace.class.getClassLoader());
        Class<?> classFoo2 = loader2.loadClass("example.classloader.Foooo");
        classFoo2.getMethod("changeString", String.class).invoke(null, "Niko");
    }
}
