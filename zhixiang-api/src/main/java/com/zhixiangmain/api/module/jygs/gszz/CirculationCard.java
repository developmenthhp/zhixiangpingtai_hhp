package com.zhixiangmain.api.module.jygs.gszz;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.gszz
 * @Description: 流通证
 * @author: hhp
 * @date: 2019-04-07 14:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="circulationcard")
public class CirculationCard extends BaseEntity {
    //单位名称
    private String name;
    //法定代表人
    private String representative;
    //试点
    private Integer sdId;
    //有效期限
    private String validTime;
    //预警日期
    private String warningDate;
    //编号
    private String serialNumber;
    //用户的id
    private Integer accountId;
    //餐饮流通证照片
    private String circulationCardImg;
}
