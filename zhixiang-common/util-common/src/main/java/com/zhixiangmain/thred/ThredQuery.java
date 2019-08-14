package com.zhixiangmain.thred;

import com.zhixiangmain.baseInvokeMethod.BaseInvokeMethod;
import com.zhixiangmain.baseInvokeMethod.InvokeMethodEntity;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.thred
 * @Description: 线程查询数据类
 * @author: hhp
 * @date: 2019-03-13 9:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ThredQuery implements Callable<List<Map<String, Object>>> {
    private List<InvokeMethodEntity> mapperMethods;//需要通过够早方法把对应的业务service传进来 实际用的时候把类型变为对应的类型
    private SqlSession sqlSession;
    private Integer start;
    private Integer count;
    /**
     * 重新构造方法
     * @param mapperMethods
     * @param sqlSession
     * @param start
     * @param count
     */
    public ThredQuery(List<InvokeMethodEntity> mapperMethods, SqlSession sqlSession, Integer start, Integer count){
        this.mapperMethods=mapperMethods;
        this.sqlSession = sqlSession;
        this.start = start;
        this.count = count;
    }

    @Override
    public List<Map<String, Object>> call() throws Exception {
        //通过service查询得到对应结果
        List<Map<String, Object>>  list  =new ArrayList<>(); //myService.queryBySex(sex,bindex,num);
        for(InvokeMethodEntity invokeMethodEntity:mapperMethods){
            Class interfaceImpl = Class.forName(invokeMethodEntity.getMapperName());//这里要写全类名
            Object instance = Proxy.newProxyInstance(
                    interfaceImpl.getClassLoader(),
                    new Class[]{interfaceImpl},
                    new BaseInvokeMethod(sqlSession.getMapper(interfaceImpl))
            );
            Method method = instance.getClass().getMethod(invokeMethodEntity.getMethodName(), Map.class);
            Map<String,Object> map = invokeMethodEntity.getMap();
            map.put("start",start);
            System.out.println(map.get("start")+"||||||||||||||||||||||||||"+start);
            map.put("countNum",count);
            list.addAll((List<Map<String, Object>>)method.invoke(instance,map));
        }
        return list;
    }

}
