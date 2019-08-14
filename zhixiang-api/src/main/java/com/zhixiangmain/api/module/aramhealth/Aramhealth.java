package com.zhixiangmain.api.module.aramhealth;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.aramhealth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "aramhealth")
public class Aramhealth extends BaseEntity {
    //类型, 1人员健康  2供应异常
    private String aramType;
    //相关信息描述
    private String aramconText;
    //发生时间
    private String aramTime;
    //处理情况 1已处理  2未处理
    private String aramHandle;
    //相关违规结构id, 供应的就取供应id
    private Integer aramAccountId;
    //运作主体试点id
    private Integer sdId;
}
