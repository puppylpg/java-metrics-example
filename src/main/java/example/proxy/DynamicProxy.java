package example.proxy;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类并不需要在Java代码中定义，而是在运行时动态生成的。
 * 相比于静态代理，动态代理的优势在于可以很方便的对代理类的函数进行统一的处理，而不用修改每个代理类的函数。
 * 比如在每个方法前后都添加一些操作。
 *
 * @author liuhaibo on 2018/04/19
 */
@AllArgsConstructor
public class DynamicProxy implements InvocationHandler {

    private ICoder coder;

    /**
     * 当代理类调用类中的方法的时候就是在调用这个方法，
     * 比如：ICoder proxy是生出来的动态代理，proxy.implementDemands("Send an ad");
     * 此时参数值如下：
     * proxy = example.proxy.JavaCoder@1bc6a36e
     * method = public abstract void example.proxy.ICoder.implementDemands(java.lang.String)
     * args = Send an ad
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Do pre-work before a job");
        // 就是通过反射，由函数名、对象、函数参数，去调用此函数。
        Object result = method.invoke(coder, args);
        System.out.println("Do post-work before a job");
        return result;
    }
}
