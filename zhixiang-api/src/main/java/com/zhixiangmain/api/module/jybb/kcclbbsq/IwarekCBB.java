package com.zhixiangmain.api.module.jybb.kcclbbsq;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.jybb.kcclbbsq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 17:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="iwarekcbb")
public class IwarekCBB extends BaseEntity {
    //入库明细id集合，
    private String wareRecordIds;
    //报表制单时间
    private String reportTime;
    //报表制单人姓名,直接填写用户姓名
    private String reportPersion;
    //报表制单人id
    private Integer reportPersionId;
    //报表状态 1.审签中  2.已作废  3.审签完成
    private String reportStatus;
    //隐藏签收状态  1 未审签  2 第一审通过 3第二审通过 3第三审通过
    private String hideStatus;
    private Integer sdId;//试点id
}
