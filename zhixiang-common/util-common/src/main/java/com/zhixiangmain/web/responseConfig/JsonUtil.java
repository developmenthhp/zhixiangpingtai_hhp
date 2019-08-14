package com.zhixiangmain.web.responseConfig;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.ObjectUtil;
import com.zhixiangmain.web.requestdata.PrintUtil;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-taotao
 * @Package cn.zhixiangyun.web.responseConfig
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-09-26 18:26
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class JsonUtil {

    public static final String PRINT_STR = "resultData";

    /**
     * 返回至客户端的信息
     *
     * @author James
     * @param flag
     *            结果标志
     * @param msg
     *            提示信息
     * @param result
     *            结果数据
     * @return
     * @date 2016年1月21日 下午6:14:28
     *
     */
    public static String getMsg(String flag, String msg, Map<String, Object> result) {
        JSONObject json = new JSONObject();
        json.put(InterfaceRsConstant.RESULT_FLAG, flag);
        if (msg != null && !msg.equals("")) {
            json.put(InterfaceRsConstant.RESULT_MSG, msg);
        }
        json.put(InterfaceRsConstant.RESULT_DATA, result);
        PrintUtil.outln(PRINT_STR, json.toString());
        return json.toString();
    }

    /**
     * 返回至客户端的信息
     *
     * @author James
     * @param flag
     *            结果标志
     * @param msg
     *            提示信息
     * @param result
     *            结果数据
     * @return
     * @date 2016年1月21日 下午6:14:28
     *
     */
    public static String getMsg(String flag, String msg, JSONObject result) {
        JSONObject json = new JSONObject();
        json.put(InterfaceRsConstant.RESULT_FLAG, flag);
        if (msg != null && !msg.equals("")) {
            json.put(InterfaceRsConstant.RESULT_MSG, msg);
        }
        json.put(InterfaceRsConstant.RESULT_DATA, result);
        PrintUtil.outln(PRINT_STR, json.toString());
        return json.toString();
    }

    /**
     * 返回至客户端的数据
     *
     * @param flag
     *            结果标志
     * @param msg
     *            提示信息
     * @return
     * @date 2016.05.05 pm
     */
    public static String getMsg(String flag, String msg) {
        JSONObject json = new JSONObject();
        json.put(InterfaceRsConstant.RESULT_FLAG, flag);
        if (msg != null && !msg.equals("")) {
            json.put(InterfaceRsConstant.RESULT_MSG, msg);
        }
        PrintUtil.outln(PRINT_STR, json.toString());
        return json.toString();
    }

    /**
     * 返回至客户端的信息
     *
     * @author James
     * @param flag
     *            结果标志
     * @param msg
     *            提示信息
     * @param result
     *            结果数据
     * @return
     * @date 2016年1月21日 下午6:14:28
     *
     */
    public static String getMsg(String flag, String msg, Object result) {
        JSONObject json = new JSONObject();
        json.put(InterfaceRsConstant.RESULT_FLAG, flag);
        if (msg != null && !msg.equals("")) {
            json.put(InterfaceRsConstant.RESULT_MSG, msg);
        }
        if (!ObjectUtil.isNull(result)) {
            json.put(InterfaceRsConstant.RESULT_DATA, result);
        }
        PrintUtil.outln(PRINT_STR, json.toString());
        return json.toString();
    }

    public static String toJson(Object object) {
        JSONObject json = new JSONObject();
        json = (JSONObject) JSONObject.toJSON(object);
        PrintUtil.outln(PRINT_STR, json.toString());
        return json.toString();
    }

    public static String arrayToJson(Object object) {
        PrintUtil.outln(PRINT_STR, JSONArray.toJSONString(object));
        return JSONArray.toJSONString(object);
    }

    public static Object jsonToBean(JSONObject json, Class<?> cls) {
        return JSONObject.toJavaObject(json, cls);
    }
}
