package com.zhixiangmain.api.module.jygs.sjzs;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.sjzs
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-07 14:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="displayarea")
public class DisplayArea extends BaseEntity {
    private String area;
    private Integer pid;
    private Integer sdId;
}
