package com.zhixiangmain.api.module.jcxxpt.cyrygk;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.jcxxpt.cyrygk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-21 13:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="healthtimerecord")
public class HealthCertificate extends BaseEntity {
    //员工用户id
    private Integer mainAccountId;
    //日期核算，到期日 格式:2018-10-01
    private String documentTime;
    //图文存放路径，证件原型， 存放后缀
    private String documentImg;
    //记录生成时间
    private String generateTime;
    //试点id
    private Integer sdId;
    //健康预警日期
    private String expireTime;
}
