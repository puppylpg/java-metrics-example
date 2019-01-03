package example.classloader;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * All classes in {@link #USER_LOADED_PACKAGE_PREFIX} will be loaded by this class loader.
 *
 * Created by lgl on 上午12:16 18-6-1.
 */
public class CustomClassLoader extends ClassLoader {

    private static final String USER_LOADED_PACKAGE_PREFIX = "example.classloader";

    /**
     * Parent ClassLoader passed to this constructor
     * will be used if this ClassLoader can not resolve a
     * particular class.
     *
     * @param parent Parent ClassLoader
     *               (may be from getClass().getClassLoader())
     */
    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * Every request for a class passes through this method.
     * If the requested class is in {@link #USER_LOADED_PACKAGE_PREFIX} package,
     * it will load it using the
     * {@link CustomClassLoader#getClass()} method.
     * If not, it will use the super.loadClass() method
     * which in turn will pass the request to the parent.
     *
     * @param name Full class name
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith(USER_LOADED_PACKAGE_PREFIX)) {
            System.out.println("loading class '" + name + "' by defined classloader: " + CustomClassLoader.class);
            return getClass(name);
        }
        System.out.println("loading class '" + name + "' by papa classloader: " + CustomClassLoader.class.getClassLoader());
        return super.loadClass(name);
    }

    /**
     * Loads a given class from .class file just like
     * the default ClassLoader. This method could be
     * changed to load the class over network from some
     * other server or from the database.
     *
     * @param name Full class name
     */
    private Class<?> getClass(String name) {
        // We are getting a name that looks like
        // a.b.SomeClass and we have to convert it
        // into the .class file name like a/b/SomeClass.class
        String file = name.replace('.', File.separatorChar) + ".class";
        byte[] b;
        try {
            // This loads the byte code data from the file
            b = loadClassData(file);
            // defineClass is inherited from the ClassLoader class
            // and converts the byte array into a Class
            Class<?> c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a given file (presumably .class) into a byte array.
     * The file should be accessible as a resource, for example
     * it could be located on the classpath.
     *
     * @param name File name to load
     * @return Byte array read from the file
     * @throws IOException thrown when there was problem reading the file
     */
    private byte[] loadClassData(String name) throws IOException {
        // Opening the file
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        // Reading the binary data
        in.readFully(buff);
        in.close();
        return buff;
    }

    /**
     * 在加载{@link Foo}的时候，{@link Foo}所依赖的类也将由我们自定义的类加载器加载。
     * 具体来说，在调用{@link ClassLoader#defineClass(String, byte[], int, int)}的时候，
     * 又把{@link Foo}里面用到的类load了一遍，所以有了{@link Object}、{@link System}和{@link java.io.PrintStream}：
     *
     * loading class 'example.classloader.Foo' by defined classloader: class example.classloader.CustomClassLoader
     * loading class 'java.lang.Object' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.lang.System' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * loading class 'java.io.PrintStream' by papa classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
     * bar
     * static bar
     *
     * <p>
     * Please note, that if you were about to write a real-world class loader, you would probably extend the URLClassLoader,
     * because the part of loading a class from a file is there already implemented.
     * Also, real class loaders normally ask their parent to load a class BEFORE trying to load it themselves.
     * In our example, for the classes in {@link #USER_LOADED_PACKAGE_PREFIX} package, we do load them without asking the parent.
     */
    public static void main(String[] args) throws Exception {
        CustomClassLoader customClassLoader = new CustomClassLoader(Foo.class.getClassLoader());
        Class<?> classFoo = customClassLoader.loadClass("example.classloader.Foo");

        // invoke instance method
        Object instance = classFoo.newInstance();
        classFoo.getMethod("bar").invoke(instance);
        // invoke class method
        classFoo.getMethod("staticBar").invoke(null);
    }
}
