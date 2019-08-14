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
 * @Description: 菜谱明细
 * @author: hhp
 * @date: 2019-04-08 10:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="foodmenudetails")
public class FoodMenuDetails extends BaseEntity {
    //外键关系,关联菜谱主表
    private Integer foodMenuId;
    //菜品食品id
    private Integer foodId;
    //因样品管控需要用到，样品管控作用域 1.是一楼 2.二楼 3.三楼 4.四楼
    private String simpleScopes;
}
