package example.proxy.utils;


import example.proxy.ICoder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public final class TestProxyGen extends Proxy implements ICoder {

    private static Method m1;
    // 看下面的初始化，m3是estimateTime方法
    private static Method m3;
    private static Method m4;
    private static Method m2;
    private static Method m0;


    public TestProxyGen(InvocationHandler var1)  {
        super(var1);
    }

    public final boolean equals(Object var1)  {
        try {
            return ((Boolean)super.h.invoke(this, m1, new Object[]{var1})).booleanValue();
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    /**
     * 所以这里，当生成的proxy对象去调用estimateTime的时候，其实是去调用
     * {@link example.proxy.DynamicProxy#invoke(Object, Method, Object[])}
     *
     * @param var1
     */
    public final void estimateTime(String var1)  {
        try {
            // h是Proxy类保有的InvocationHandler
            super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void implementDemands(String var1)  {
        try {
            super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString()  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode()  {
        try {
            return ((Integer)super.h.invoke(this, m0, (Object[])null)).intValue();
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[]{Class.forName("java.lang.Object")});
            // m3是estimateTime方法
            m3 = Class.forName("example.proxy.ICoder").getMethod("estimateTime", new Class[]{Class.forName("java.lang.String")});
            m4 = Class.forName("example.proxy.ICoder").getMethod("implementDemands", new Class[]{Class.forName("java.lang.String")});
            m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
            m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
