package com.zhixiangmain.api.module.cpjs.cpmx;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-08 10:36
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="foodmenu")
public class FoodMenu extends BaseEntity {
    //菜谱日期
    private String menuDate;
    //菜谱顺序 1.早 , 2.中 , 3.晚  4,夜宵
    private String menuOrder;
    //状态:1已发布 2已作废
    private String menuStatus;
    //菜品总数
    private Integer foodCount;
    //经营点表id,与之关联
    private Integer sdId;
    //门号
    private Integer doorNumber;
}
