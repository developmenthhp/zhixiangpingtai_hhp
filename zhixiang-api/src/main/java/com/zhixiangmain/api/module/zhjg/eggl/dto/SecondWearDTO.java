package com.zhixiangmain.api.module.zhjg.eggl.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.zhjg.eggl.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-29 16:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SecondWearDTO implements Serializable {
    private Integer sdId;
    private Integer userId;
    private String sdIds;
    private String siteName;
    private String sitePhoto;
    private Double somtempStart;
}
