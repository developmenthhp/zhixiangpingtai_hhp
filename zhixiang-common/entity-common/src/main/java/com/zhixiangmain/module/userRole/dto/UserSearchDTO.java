package com.zhixiangmain.module.userRole.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.userRole.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 15:03
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserSearchDTO implements Serializable {

    private Integer page;

    private Integer limit;

    private String uname;

    private String umobile;

    private String insertTimeStart;

    private String insertTimeEnd;

    private Integer sdid;

}
