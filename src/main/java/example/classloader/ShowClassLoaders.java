package example.classloader;

/**
 * Created by lgl on 上午12:00 18-6-1.
 */
public class ShowClassLoaders {
    /**
     * This main method shows which class loaders are used for loading classes
     * like Integer, BlowfishCipher (lib/ext) and ShowClassLoaders.
     *
     * Output:
     * class loader for Integer: null
     * class loader for BlowfishCipher: sun.misc.Launcher$ExtClassLoader@4617c264
     * class loader for this class: sun.misc.Launcher$AppClassLoader@18b4aac2
     */
    public static void main(String[] args) {
        // In most implementations getClassLoader() method returns null for the bootstrap class loader
        System.out.println("class loader for Integer: " + Integer.class.getClassLoader());
        // com.sun.crypto.provider.BlowfishCipher class is stored inside the <JAVA_HOME>/lib/ext/sunjce_provider.jar
        // so it was loaded with the extensions class loader
        System.out.println("class loader for BlowfishCipher: " + com.sun.crypto.provider.BlowfishCipher.class.getClassLoader());
        // Classes implemented by us, like the class with the main method, was loaded by system class loader
        System.out.println("class loader for this class: " + ShowClassLoaders.class.getClassLoader());
    }
}
