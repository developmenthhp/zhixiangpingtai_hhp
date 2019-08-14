package com.zhixiangmain.api.module.cgcc.StockOMaterials;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cgcc.StockOMaterials
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-28 18:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="librarys")
public class Library {
    //编号
    private Integer id;
    //食材id
    private Integer ingredientBaseId;
    //库存结余,库存批次变更需要对该字段进行更新
    private Double librarySurplus;
    //批次数量过期总数
    private Double libraryExpired;
    //补货状态 1.生产进货单(手动)   2.正常
    private String replenishmentStatus;
    //库存状态 1.正常  2.过期  3.即将过期
    private String libraryStatus;
    private Integer sdId;
}
