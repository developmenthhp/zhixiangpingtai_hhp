package com.zhixiangmain.thred;

import com.zhixiangmain.baseInvokeMethod.BaseInvokeMethod;
import com.zhixiangmain.baseInvokeMethod.InvokeMethodEntity;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.thred
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 15:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class ThreadTotalQuery implements Callable<Integer> {
    private List<InvokeMethodEntity> mapperMethods;//需要通过够早方法把对应的业务service传进来 实际用的时候把类型变为对应的类型
    private SqlSession sqlSession;
    /**
     * 重新构造方法
     * @param mapperMethods
     * @param sqlSession
     */
    public ThreadTotalQuery(List<InvokeMethodEntity> mapperMethods, SqlSession sqlSession){
        this.mapperMethods=mapperMethods;
        this.sqlSession = sqlSession;
    }

    @Override
    public Integer call() throws Exception {
        //通过service查询得到对应结果
        Integer total = 0;
        for(InvokeMethodEntity invokeMethodEntity:mapperMethods){
            Class interfaceImpl = Class.forName(invokeMethodEntity.getMapperName());//这里要写全类名
            Object instance = Proxy.newProxyInstance(
                    interfaceImpl.getClassLoader(),
                    new Class[]{interfaceImpl},
                    new BaseInvokeMethod(sqlSession.getMapper(interfaceImpl))
            );
            Method method = instance.getClass().getMethod(invokeMethodEntity.getMethodName(), Map.class);
            total = total + (Integer)method.invoke(instance,invokeMethodEntity.getMap());
        }
        return total;
    }
}
