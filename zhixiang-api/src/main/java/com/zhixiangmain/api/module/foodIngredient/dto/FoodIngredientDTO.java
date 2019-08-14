package com.zhixiangmain.api.module.foodIngredient.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.foodIngredient.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-09 18:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class FoodIngredientDTO implements Serializable {
    private String foodId;

    //修改时已存在的主料食材id
    private String updMainIngredientIds;
    //标记的已删除的主料食材id
    private String delMainIngredientIds;
    //主料食材id
    private String[] ingredientid;
    //主料食材用量
    private String[] useLevelZL;
    //主料食材单价
    private String[] unitPriceZL;

    //修改时已存在的辅料食材id
    private String updIngredientIds;
    //标记的已删除的辅料食材id
    private String delIngredientIds;
    //辅料食材id
    private String[] ingredientidFL;
    //辅料食材用量
    private String[] useLevelFL;
    //辅料食材单价
    private String[] unitPriceFL;

    //修改时已存在的配料食材id
    private String updPLIngredientIds;
    //标记的已删除的配料食材id
    private String delPLIngredientIds;
    //配料食材id
    private String[] ingredientidPL;
    //配料食材用量
    private String[] useLevelPL;
    //配料食材单价
    private String[] unitPricePL;

    //修改时已存在的调味品食材id
    private String updTWPIngredientIds;
    //标记的已删除的调味品食材id
    private String delTWPIngredientIds;
    //调味品食材id
    private String[] ingredientidTWP;
    //配料食材用量
    private String[] useLevelTWP;
    //配料食材单价
    private String[] unitPriceTWP;

    private Integer sdId;
}
