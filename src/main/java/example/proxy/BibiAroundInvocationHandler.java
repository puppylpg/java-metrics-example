package example.proxy;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 负责在代理对象的方法调用前后bibi一番.
 *
 * @author liuhaibo on 2018/04/19
 */
@AllArgsConstructor
public class BibiAroundInvocationHandler implements InvocationHandler {

    /**
     * 被代理的对象。之所以要用它，是因为毕竟要调用原始方法。
     * 如果用不到被代理对象，就不用把它放进来了
     */
    private Coder coder;

    /**
     * 代理对象在被调用方法时，都会交给该handler（回调该handle的invoke方法）
     *
     * @param proxy jvm生成的代理对象，如果对生成的代理对象不关系，可以不用该参数
     * @param method 要调用的代理对象的方法
     * @param args 要调用的代理对象的方法的参数。method + args才能确定下来要调用哪个方法
     * @return 如果调用的方法会返回值，一般将返回值原封不动的返回回去，当然也可以改改再返回回去
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println(">>> Before: " + LocalDateTime.now());
        // 就是通过反射，由函数名、对象、函数参数，去调用此函数。
        Object result = method.invoke(coder, args);
        System.out.println("<<< After: " + LocalDateTime.now());
        return "(Proxy) " + result;
    }
}
