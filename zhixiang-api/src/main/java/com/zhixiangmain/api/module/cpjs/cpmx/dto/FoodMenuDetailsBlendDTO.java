package com.zhixiangmain.api.module.cpjs.cpmx.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.cpjs.cpmx.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-12 12:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class FoodMenuDetailsBlendDTO implements Serializable {
    private Integer sdId;
    private String addJson;
    private String updateJson;
    private String deleteJson;
}
