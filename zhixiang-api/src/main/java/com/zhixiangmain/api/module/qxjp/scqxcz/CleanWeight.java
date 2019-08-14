package com.zhixiangmain.api.module.qxjp.scqxcz;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.qxjp.scqxcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 12:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="cleanweight")
public class CleanWeight extends BaseEntity {
    private Integer foodId;//食材id
    private String weightTime;//称重时间
    private Double foodWeight;//食材重量
    private Integer isNormal;//是否正常 1正常 2不正常
    private Integer sdId;
    private String standard;//食材标准
    private String shuldeBeWeight;//核定完成重量
    private String foodName;//食材名称
}
