package com.zhixiangmain.baseInvokeMethod;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.baseInvokeMethod
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-13 10:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class InvokeMethodEntity {
    //mapper的全类名
    private String mapperName;
    //查询方法名
    private String methodName;
    //查询参数
    private Map<String,Object> map;

    public InvokeMethodEntity(String mapperName, String methodName, Map<String,Object> map){
        this.mapperName = mapperName;
        this.methodName = methodName;
        this.map = map;
    }
}
