package com.zhixiangmain.api.module.base;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.base.FoodClassification
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-24 10:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="categorybase")
public class CategoryBase extends BaseEntity {
    //食品h货品药品类别信息
    private String categoryName;
    //试点id绑定
    private Integer sdId;
    //标识1否  0 是
    private String deleteStatus;
    private Integer pid;//

}
