package com.zhixiangmain.web.responseConfig;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-taotao
 * @Package cn.zhixiangyun.web.responseConfig
 * @Description: 配置提示信息
 * @author: hhp
 * @date: 2018-09-27 14:59
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class InterfaceRsConstant {

    /*********************** 请求集 ***************************/
    /** 请求参数存放位置 */
    public static final String REQUEST_DATA = "reqData";
    /** 设备id */
    public static final String APP_ID = "appId";
    /** 语言类型 zh中文 */
    public static final String LANGUAGE = "language";
    /** 登录人主键 */
    public static final String USERID = "userId";
    /** 临时访问密钥 */
    public static final String TEMKEY = "temKey";
    /** 业务数据 */
    public static final String REQUEST_PARAMS = "params";

    /*********************** 结果集 ***************************/
    /** 结果标识 */
    public static final String RESULT_FLAG = "flag";
    /** 提示信息 */
    public static final String RESULT_MSG = "msg";
    /** 结果数据 */
    public static final String RESULT_DATA = "result";
    /** 0:成功，但无结果 */
    public static final String RETURN_FLAG_0 = "0";
    /** 1:成功，有结果 */
    public static final String RETURN_FLAG_1 = "1";
    /** 2:失败 */
    public static final String RETURN_FLAG_2 = "2";
    /** 4：登录失效 */
    public static final String RETURN_FLAG_4 = "4";
    /** -1：出现异常 */
    public static final String RETURN_FLAG_ERROR = "-1";

    /*********************** 异常 ***************************/
    public static final String RETURN_MSG_UNKNOW_ERROR = "服务器繁忙，请稍后再试^_^";

    /*********************** 日志 ***************************/
    /** 请求时间 */
    public static final String LOG_ACC_TIME = "accTime";
    /** 请求参数 */
    public static final String LOG_REQUEST_PARAM = "reqParam";
    /** 返回数据 */
    public static final String LOG_RESULT_DATA = "resData";

}
