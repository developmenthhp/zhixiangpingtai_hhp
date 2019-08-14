package com.zhixiangmain.api.module.fromwallAlert;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.fromwallAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "fromwall_alert")
public class FromwallAlert implements Serializable {
    private Integer id;
    //存放区域名称
    private String fromwallArea;
    //传感器编号
    private String fromwallSensor;
    //发生时间
    private String ratplateStartTime;
    //1.警报中  2.已处理  3.已作废
    private String ratplateStatus;
    //描述
    private String ratplateDescription;
    //最大限距离,超过距离将报警
    private String ratplateRultionsTime;
    //试点id
    private Integer sdId;
}
