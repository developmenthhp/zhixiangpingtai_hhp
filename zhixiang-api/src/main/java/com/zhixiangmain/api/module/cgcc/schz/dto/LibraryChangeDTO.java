package com.zhixiangmain.api.module.cgcc.schz.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.cgcc.schz.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-16 10:26
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryChangeDTO implements Serializable {
    private Integer sdId;
    private Integer userId;
    private String sdIds;
    private Integer start;
    private Integer count;
    private String startTime;
    private String endTime;
    private String reservation;
    private String ingredientBaseIdSdId;
    private Integer ingredientBaseId;
    private String changeMode;
    private Integer id;
    private String siteName;
    private String sitePhoto;
}
