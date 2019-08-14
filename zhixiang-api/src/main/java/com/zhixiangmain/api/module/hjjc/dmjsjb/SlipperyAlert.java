package com.zhixiangmain.api.module.hjjc.dmjsjb;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.hjjc.dmjsjb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 10:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "slippery_alert")
public class SlipperyAlert extends BaseEntity {
    //存放区域名称
    private String ratplateArea;
    //传感器编号
    private String ratplateSensor;
    //1.警报中  2.已处理  3.已作废
    private String ratplateStatus;
    //描述
    private String ratplateDescription;
    //实际积水水位
    private String ratplateRultionsTime;
    //试点id
    private Integer sdId;
    private String alertTime;//警报时间
}
