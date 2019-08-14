package com.zhixiangmain.api.module.scgl.sckjgl.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.scgl.sckjgl.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 16:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class FoodInspectionDTO implements Serializable {
    private Integer userId;
    private String sdIds;
    private Integer sdId;
    private String siteName;
    private String sitePhoto;
    private String warn;
    private String selectYear;
    private String selectMonth;
    private String selectYearMonth;
}
