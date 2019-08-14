package com.zhixiangmain.api.module.cgcc.sccg.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.cgcc.sccg.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-15 17:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryPurchaseDTO implements Serializable {
    private Integer userId;
    private String mainCategoryIdSdId;
    private Integer mainCategoryId;
    private String smallCategoryIdSdId;
    private Integer smallCategoryId;
    private String sdIds;
    private Integer start;
    private Integer count;
    private String startTime;
    private String endTime;
    private String reservation;
    private String siteName;
    private String sitePhoto;
}
