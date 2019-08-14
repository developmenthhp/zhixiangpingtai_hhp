package com.zhixiangmain.api.module.inventoryAlert;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.inventoryalert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "inventoryalert")
public class InventoryAlert extends BaseEntity {
    //异常描述
    private String expconText;
    //发生时间
    private String occurTime;
    //承担者昵称
    private String underTaker;
    //承担者id
    private String underTakerId;
    //确认者名cheng
    private String affirmant;
    //1.异常告警中 2.已处理
    private String alertStatus;
    //处理时间
    private String handleTime;
    //试点id
    private Integer sdId;
    //类型, 1.库存过期   2.即将过期  3.库存不足
    private String alertType;
}
