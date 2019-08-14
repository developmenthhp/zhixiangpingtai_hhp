package com.zhixiangmain.api.module.ratGuardBase;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.ratGuardBase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-03 16:37
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "ratplate_ulsonic_base")
public class RatGuardBase extends BaseEntity {
    //存放区域名称
    private String ratPlateArea;
    //传感器编号
    private String ratPlateSensor;
    //可用状态 1 禁用  2.正常 3.删除
    private String status;
    //最小限距离,超过距离将报警
    private String ratPlateRultionsTime;
    //试点id
    private Integer sdId;
}
