package com.zhixiangmain.module.base.metering;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.base.metering
 * @Description: 基础计量单位
 * @author: hhp
 * @date: 2019-02-26 16:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="meteringunit")
public class MeteringUnit extends BaseEntity {
    private String meteringName;
    private String deleteStatus;
}
