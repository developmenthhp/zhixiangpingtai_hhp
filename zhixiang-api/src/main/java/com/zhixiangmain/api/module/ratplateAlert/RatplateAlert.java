package com.zhixiangmain.api.module.ratplateAlert;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.ratplateAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "ratplate_alert")
public class RatplateAlert implements Serializable {
    private Integer id;
    //存放区域名称
    private String ratplateArea;
    //传感器编号
    private String ratplateSensor;
    //开始发生时间
    private String ratplateStartTime;
    //结束时间
    private String ratplateEndTime;
    //1.警报中  2.已处理  3.已作废
    private String ratplateStatus;
    //描述
    private String ratplateDescription;
    //改为抓拍图片
    private String ratplateRultionsTime;
    //试点id
    private Integer sdId;
}
