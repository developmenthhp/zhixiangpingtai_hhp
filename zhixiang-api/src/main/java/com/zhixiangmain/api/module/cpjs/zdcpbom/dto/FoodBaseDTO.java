package com.zhixiangmain.api.module.cpjs.zdcpbom.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cpjs.zdcpbom.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 10:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class FoodBaseDTO implements Serializable {
    private Integer sdId;
    private String menuTypeDto;
    private String foodNameDto;
    private String[] menuTypeDtoSel;
    private String sdIds;
    private Integer userId;
    private String idSdId;
    private Integer id;
    private String mainCategoryIdSdId;
    private Integer mainCategoryId;
    private String smallCategoryIdSdId;
    private Integer smallCategoryId;
    private Integer start;
    private Integer end;
    private String foodName;
    private String siteName;
    private String sitePhoto;
}
