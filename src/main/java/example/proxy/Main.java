package example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author liuhaibo on 2018/04/18
 */
public class Main {

    public static void main(String ... args) {
        // true coder, 需要被代理
        ICoder coder = new JavaCoder("Tom");

        staticProxy(coder);
        dynamicProxy(coder);
    }

    private static void staticProxy(ICoder coder) {
        System.out.println("===== static proxy =====");
        // static proxy
        ICoder staticProxy = new StaticProxy(coder);

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
    private static void dynamicProxy(ICoder coder) {
        System.out.println("===== dynamic proxy =====");
        // 创建中介类实例
        InvocationHandler handler = new DynamicProxy(coder);
        // 被代理类的classLoader & interfaces
        // runtime类，此时指的是JavaCoder，而非ICoder
        Class coderCls = coder.getClass();
        ClassLoader classLoader = coderCls.getClassLoader();
        Class<?>[] interfaces = coderCls.getInterfaces();
        // 动态生成一个代理类
        // example.proxy.JavaCoder@1bc6a36e
        ICoder proxy = (ICoder) Proxy.newProxyInstance(classLoader, interfaces, handler);

        proxy.estimateTime("Hello, world");
        proxy.implementDemands("Send an ad");
        proxy.implementDemands("illegal");
    }
}
