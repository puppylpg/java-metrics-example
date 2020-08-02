/**
 * RPC其实就是通过动态代理的手段，获取到protocol对象，并当做本地对象在调用。
 *
 * 这里的protocol其实就是{@link example.proxy.Coder}。
 *
 * 唯一的区别是，这里，获取完{@link example.proxy.Coder}动态代理对象，
 * 当调用对象方法时，在{@link example.proxy.BibiAroundInvocationHandler#invoke(Object, Method, Object[])}里直接用反射就可以了，获取到其返回值，
 * 因为它已经包含有{@link example.proxy.JavaCoder}的对象了，可以直接反射调用其方法；
 *
 * 当rpc通信时，在{@link example.proxy.BibiAroundInvocationHandler#invoke(Object, Method, Object[])}里必须得是通过通信，获取到方法的返回值，
 * 因为它明显不包含{@link example.proxy.JavaCoder}对象（由服务器端实现，并提供服务），所以得通信去取得其结果。
 * 这些通信就可以借助mina等已有的框架。
 *
 * 所以本质区别不在于获取动态代理对象，而在于获取完这个对象之后，“调用其方法”这个过程如何实现，
 * 体现在{@link java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}里。
 * 或者说{@link example.proxy.BibiAroundInvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}里。
 *
 * 另：获取动态对象都使用{@link java.lang.reflect.Proxy#newProxyInstance(java.lang.ClassLoader, java.lang.Class[], java.lang.reflect.InvocationHandler)}。
 */
package example.proxy;

import java.lang.reflect.Method;
