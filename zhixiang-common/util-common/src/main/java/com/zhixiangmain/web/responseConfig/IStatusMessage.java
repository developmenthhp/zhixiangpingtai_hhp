package com.zhixiangmain.web.responseConfig;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web.responseConfig
 * @Description: 响应状态信息
 * @author: hhp
 * @date: 2018-11-23 15:21
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IStatusMessage {

    String getCode();

    String getMessage();

    public enum SystemStatus implements IStatusMessage {

        SUCCESS("1000","SUCCESS"), //请求成功
        ERROR("1001","ERROR"),	   //请求失败
        PARAM_ERROR("1002","PARAM_ERROR"), //请求参数有误
        SUCCESS_MATCH("1003","SUCCESS_MATCH"), //表示成功匹配
        NO_LOGIN("1100","NO_LOGIN"), //未登录
        MANY_LOGINS("1101","MANY_LOGINS"), //多用户在线（踢出用户）
        UPDATE("1102","UPDATE"), //用户信息或权限已更新（退出重新登录）
        LOCK("1111","LOCK"), //用户已锁定
        LOGIC_ERROR("1004","LOCK"); //参数逻辑错误
        private String code;
        private String message;

        private SystemStatus(String code,String message){
            this.code = code;
            this.message = message;
        }

        public String getCode(){
            return this.code;
        }

        public String getMessage(){
            return this.message;
        }
    }

    public enum LogicStatus implements IStatusMessage {

        LOGIC_ERROR("1527","LOGIC_ERROR"),//逻辑错误
        NAME_EXISTS("1528","NAME_EXISTS"),//名称已存在
        NAME_NOT_EXISTS("1529","NAME_NOT_EXISTS"),//名称不存在
        IMPORT_BASE_PRICE_DATA_MATCHING("1530","IMPORT_BASE_PRICE_DATA_MATCHING"),//导入基础食材价格食材名未匹配
        UPLOAD_FILE_TYPE_ERROR("1531","UPLOAD_FILE_TYPE_ERROR"),//不支持该文件类型
        SITE_IS_NOT_SET_WARNING_OVER_TIME("1532","SITE_IS_NOT_SET_WARNING_OVER_TIME");//不支持该文件类型
        private String code;
        private String message;

        private LogicStatus(String code,String message){
            this.code = code;
            this.message = message;
        }

        public String getCode(){
            return this.code;
        }

        public String getMessage(){
            return this.message;
        }
    }

}
