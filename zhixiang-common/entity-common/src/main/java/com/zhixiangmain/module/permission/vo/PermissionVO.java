package com.zhixiangmain.module.permission.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.permission.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 14:58
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = -2783081162690878303L;
    private String id;

    private String name;

    private String pId;

    private String istype;

    private String code;

    private String page;

    private String icon;

    private String zindex;

    private boolean checked;

    private boolean open;

}
