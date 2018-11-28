package example.proxy.monitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 这是一个汇聚了业务代码和横切代码的聚合物。
 *
 * @author puppylpg on 2018/11/27
 */
public class TimeMonitorHandler implements InvocationHandler {

    private Object target;

    TimeMonitorHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long begin = System.currentTimeMillis();
        System.out.println("\nStart monitor method: " + method.getName());
        Object result = method.invoke(target, args);
        long end = System.currentTimeMillis();
        System.out.println("End monitor. Time used = " + (end - begin));
        return result;
    }
}
