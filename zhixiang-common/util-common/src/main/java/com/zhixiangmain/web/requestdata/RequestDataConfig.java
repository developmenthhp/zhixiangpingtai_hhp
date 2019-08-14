/**  
* Title: RequestDataConfig.java
* Description:
* Copyright: Copyright (c) 2018  
* Company: http://www.zhixiangyun.net
* @author hhp  
* @date 2018年6月27日  
* @version 1.0  
*/
package com.zhixiangmain.web.requestdata;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**  
* Title: RequestDataConfig
* Description: 请求参数配置
* @author hhp  
* @date 2018年6月27日  
*/
public class RequestDataConfig {

	public static final String PRINT_STR = "requestData";

    /**  
     * Title: getWebRequestData
     * Description: 将请求参数配置成map数组
     * @param request
     * @return Map<String,String> 返回一个键值对map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getWebRequestData(HttpServletRequest request) {
        Map<String,String> map=new HashMap<>();
        try {
            Enumeration<String> names = request.getParameterNames();
            if (names != null) {
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    String value = request.getParameter(name);
                    value = java.net.URLDecoder.decode(value, "UTF-8");
                    map.put(name, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PrintUtil.outln(PRINT_STR, map.toString());
        return map;
    }
	
}
