package com.zhixiangmain.api.module.foodIngredient;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.foodIngredient
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-08 15:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="foodingredient")
public class FoodIngredient extends BaseEntity {
    //组成成分名称-选择食材货品名称
    private String ingredientName;
    //食材id
    private Integer ingredientId;
    //计量信息
    private Double metering;
    //计量单位-自动获取食材货品信息里面绑定的
    private String meterUnit;
    //食品菜品id，多对一关系
    private Integer foodId;
    //食材单价
    private String unitPrice;
    //类型
    private Integer type;
    //试点id
    private Integer sdId;
}
