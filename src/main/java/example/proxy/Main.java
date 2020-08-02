package example.proxy;

import java.lang.reflect.*;

/**
 * interface
 *  (jvm) -> proxy class object
 *  (new an object) -> proxy instance
 *
 * jvm首先使用ClassLoader和Interface构建一个该Interface的实现类（代理类），
 * 该代理类添加了一个以InvocationHandler为参数的构造函数
 * 然后传入InvocationHandler，构建一个代理类的实例：代理对象
 *
 * 我们调用代理对象的方法（接口中声明的方法），代理对象的方法实现很一致：调用handler
 * handler一般会使用被代理的对象执行方法，然后可以修改方法执行结果，或者在方法执行前后加一些其他操作
 *
 * 所以现在的效果是：所有对接口方法的调用，都统一放到了代理对象内部的handler里进行处理
 *
 * 可以查看生成的TestProxyGen，jvm给代理对象生成的执行逻辑。
 *
 * @author liuhaibo on 2018/04/18
 */
public class Main {

    public static void main(String ... args) {
        // true coder, 需要被代理
        Coder coder = new JavaCoder("Tom");

        staticProxy(coder);
        dynamicProxy(coder);
    }

    private static void staticProxy(Coder coder) {
        System.out.println("===== static proxy =====");
        // static proxy
        Coder staticProxy = new StaticProxy(coder);

        staticProxy.estimateTime("Hello, world");
        staticProxy.implementDemands("Send an ad");
        staticProxy.implementDemands("illegal");
    }

    /**
     * {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}实际上就是
     * 根据classLoader和interface，搞出这个代理类（Proxy0），并得到它的构造函数，然后生成这么一个代理类。
     * 其实现具体代码如下：
     * //com.sun.proxy.$Proxy0
     * Class<?> cl = getProxyClass0(loader, intfs);
     *
     * // public com.sun.proxy.$Proxy0(java.lang.reflect.InvocationHandler)
     * cl.getConstructors()
     *
     * // parameter types of a proxy class constructor
     * private static final Class<?>[] constructorParams = { InvocationHandler.class };
     *
     * // public com.sun.proxy.$Proxy0(java.lang.reflect.InvocationHandler)
     * final Constructor<?> cons = cl.getConstructor(constructorParams);
     */
    private static void dynamicProxy(Coder coder) {
        System.out.println("\n===== dynamic proxy =====");
        // 代理对象调用方法时，会回调的handler
        InvocationHandler handler = new BibiAroundInvocationHandler(coder);

        // 被代理类的classLoader & interfaces，要用他们来创建一个代理类
        Class<?> coderCls = coder.getClass();
        ClassLoader classLoader = coderCls.getClassLoader();
        Class<?>[] interfaces = coderCls.getInterfaces();

        // 获取代理类
        Class<?> proxyClass = Proxy.getProxyClass(classLoader, interfaces);

        // 展示代理类的所有方法
        System.out.println("\n=> method =>");
        for (Method method : proxyClass.getMethods()) {
            System.out.println(method.getName() + " : ");
            for (Parameter parameter : method.getParameters()) {
                System.out.println("\t" + parameter.getName() + " -> " + parameter.getType() );
            }
        }

        // 展示代理类的所有构造方法：jvm给生成的代理类新加了“一个以InvocationHandler为参数的构造方法”
        System.out.println("\n=> constructor =>");
        for (Constructor<?> constructor : proxyClass.getConstructors()) {
            System.out.println(constructor.getName() + " : ");
            for (Parameter parameter : constructor.getParameters()) {
                System.out.println("\t" + parameter.getName() + " -> " + parameter.getType() );
            }
        }

        System.out.println("\n=> invoke method =>");

        // 直接生成一个代理对象实例（实际上也是两步：先在jvm中生成一个代理类Class的对象，再使用该Class对象生成代理对象）
        Coder proxy = (Coder) Proxy.newProxyInstance(coder.getClass().getClassLoader(), coder.getClass().getInterfaces(), handler);

        proxy.estimateTime("Hello, world");
        proxy.implementDemands("Send an ad");

        System.out.println(proxy.comment());
    }
}
