package example.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 正常情况下，一个遵守规范的classloader，应该在父类找不到某个class之后再去由自己找。
 * 所以只需要覆写{@link ClassLoader#findClass(String)}就行了（找到class，define class，返回找到的class）。
 * <p>
 * 但是，像Tomcat的WebAppClassLoader，后于bootstrap的Classloader（防止jdk的类被替换），
 * 但是优先于父类（system、common）的classloader去加载WEB-INF/classes和WEB-INF/lib/*下的类（但是），
 * 从而实现了每个web应用有自己的一套依赖。（当然可配置为优先使用common，最后使用自己的）
 * <p>
 * https://tomcat.apache.org/tomcat-7.0-doc/class-loader-howto.html
 * <p>
 * {@link CustomClassLoader}就没有按照规范去搞，所以override了{@link ClassLoader#loadClass(String)}。
 *
 * @author liuhaibo on 2019/01/15
 */
public class CustomClassLoaderSuitsNorm extends ClassLoader {

    public CustomClassLoaderSuitsNorm(ClassLoader parent) {
        super(parent);
    }

    /**
     * 将Foo改为Foooo，然后尝试从文件load。
     */
    @Override
    public Class findClass(String name) {
        System.out.println("Load class finally by custom class loader which suits the norm: " + name);
        name = name.replace("Foo", "Foooo");
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    /**
     * 从文件中去load一个class的byte流。
     */
    private byte[] loadClassFromFile(String fileName) {
//        getClass().getClassLoader().loadClass(fileName);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName.replace('.', File.separatorChar) + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoaderSuitsNorm customClassLoader = new CustomClassLoaderSuitsNorm(CustomClassLoader.class.getClassLoader());
        Class<?> classFoo = customClassLoader.loadClass("example.classloader.Foo");

        // invoke instance method
        Object instance = classFoo.newInstance();
        classFoo.getMethod("bar").invoke(instance);
        // invoke class method
        classFoo.getMethod("staticBar").invoke(null);
    }
}
