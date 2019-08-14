package com.zhixiangmain.api.module.rlzb.tgjc;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.rlzb.tgjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 18:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="pepoletemp")
public class PepoleTemp extends BaseEntity {
    private Integer userId; //体温检测对象id
    private String userName; //体温检测对象名称
    private String date;    //检测时间
    private String sceneImg; //体温检测现场图
    private String somTemp;//温度
    private String recomTemper; //建议温度
    private Integer sdId;//试点id
}
