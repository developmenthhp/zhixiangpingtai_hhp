package com.zhixiangmain.api.module.zhck.slgl;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.zhck.slgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-01 17:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="fromwall_base")
public class ThreeLeaveBase extends BaseEntity {
    //区域名称
    private String fromWallArea;
    //传感器编hoa
    private String fromWallSensor;
    //最小限距离,超过距离将报警
    private String maximumDistance;
    //可用状态 1 禁用  2.正常 3.删除
    private String status;
    //试点id
    private Integer sdId;
}
