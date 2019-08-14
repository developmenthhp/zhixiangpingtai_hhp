package com.zhixiangmain.web.finalEnum;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web.finalEnum
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-22 15:24
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public enum SysConf {

    /* 后台登录的session的key */
    LOGIN_SESSION_KEY("admin");

    private String code;

    private SysConf(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
