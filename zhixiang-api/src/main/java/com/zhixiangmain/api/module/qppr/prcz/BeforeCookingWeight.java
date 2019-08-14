package com.zhixiangmain.api.module.qppr.prcz;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.qppr.prcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 13:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="cookingweight")
public class BeforeCookingWeight extends BaseEntity {
    private Integer foodId;//食材id
    private String weightTime;//称重时间
    private Double foodWeight;//食材重量
    private Integer dishesId;//菜品id
    private Integer sdId;//试点id
}
