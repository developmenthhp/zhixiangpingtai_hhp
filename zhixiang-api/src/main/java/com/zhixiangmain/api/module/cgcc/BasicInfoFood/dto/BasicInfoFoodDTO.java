package com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cgcc.BasicInfoFood.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-25 15:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class BasicInfoFoodDTO implements Serializable {
    //试点id
    private Integer sdId;
    private Integer mainCategoryId;
    private Integer smallCategoryId;
    private String ingredientName;
    private List<Integer> sdIds;
    private Integer levelDTO;
    private Integer bigCategoryDTO;
    private Integer userId;
    private String sdIdStr;
    private Integer start;
    private Integer end;
    private Integer ingredientId;
    private String startEnd;
    private String startTime;
    private String endTime;
}
