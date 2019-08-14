package com.zhixiangmain.api.module.cgcc.ghsjc.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.cgcc.ghsjc.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 14:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SupplierLicenseDTO implements Serializable {
    private Integer userId;
    private String sdIds;
    private Integer sdId;
    private String siteName;
    private String sitePhoto;
}
