package com.zhixiangmain.web.finalEnum;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web.finalEnum
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-22 15:37
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ImageConfig {
    /** 站点图标在线项目使用 */
    ///home/test001/html 179  /var/www/html 181
    SITE_ICON("//home/test001/html/", "zdIcon", "1002");
    /* 本地使用 */
    //SITE_ICON("/var/www/html/", "zdIcon", "1002");

    private String directory;

    private String key;

    private String code;

    private ImageConfig(String directory, String key, String code) {
        this.directory = directory;
        this.key = key;
        this.code = code;
    }

    public String getDirectory() {
        return directory;
    }

    public String getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }

}
