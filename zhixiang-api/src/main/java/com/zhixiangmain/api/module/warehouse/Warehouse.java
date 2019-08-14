package com.zhixiangmain.api.module.warehouse;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.warehouse
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-26 17:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="ingredientwhouse")
public class Warehouse extends BaseEntity {
    //仓库基础信息名称
    private String whName;
    //仓库状态信息 1正常使用  2删除禁用状态
    private String whStatus;
    //运作主体id
    private Integer sdId;
}
