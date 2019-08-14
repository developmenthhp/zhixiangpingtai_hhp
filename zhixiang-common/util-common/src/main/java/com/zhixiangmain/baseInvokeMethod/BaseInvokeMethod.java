package com.zhixiangmain.baseInvokeMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.BaseInvokeMethod
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-13 10:34
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class BaseInvokeMethod implements InvocationHandler {
    private Object target;
    public BaseInvokeMethod(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }

    /**
     * 执行某对象方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @return 方法返回值
     * @throws Exception
     */
    /*public Object invokeMethod(Object owner, String methodName, Object[] args) {
        String res = null;
        Method method;
        Object obj = null;
        try {
            Class ownerClass = owner.getClass();

            if (args == null) {
                method = ownerClass.getDeclaredMethod(methodName);
                method.setAccessible(true);
                //res = (String) method.invoke(owner);
                obj = method.invoke(owner);
            } else {
                Class[] argsClass = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if(args[i]!=null) {
                        System.out.println("invokeMethod: i ="+i);
                        argsClass[i] = args[i].getClass();
                    }
                }

                method = ownerClass.getDeclaredMethod(methodName, argsClass);
                method.setAccessible(true);
                //res = (String) method.invoke(owner, args);
                obj = method.invoke(owner, args);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("error:"+e.getMessage());
        }
        return obj;
        //return res;
    }*/
}
