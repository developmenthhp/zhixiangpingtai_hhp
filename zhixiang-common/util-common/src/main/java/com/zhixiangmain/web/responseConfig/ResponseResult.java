package com.zhixiangmain.web.responseConfig;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web.responseConfig
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-12-01 16:03
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 7285065610386199394L;

    private String code;
    private String message;
    private Object obj;

    public ResponseResult() {
        this.code = IStatusMessage.SystemStatus.SUCCESS.getCode();
        this.message = IStatusMessage.SystemStatus.SUCCESS.getMessage();
    }

    public ResponseResult(IStatusMessage statusMessage){
        this.code = statusMessage.getCode();
        this.message = statusMessage.getMessage();

    }

}
