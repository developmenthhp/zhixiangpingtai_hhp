package com.zhixiangmain.api.module.lygl.lyglxq;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.lygl.lyglxq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-10 16:34
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "lyitems")
public class KeepSampleDetail extends BaseEntity {
    //样品管控主表外键id
    private Integer lyMainId;
    //食品id,与之外键
    private Integer lyFoodBaseId;
    //食品名称
    private String lyFoodName;
    //样品标签-必须生成单次记录中的唯一
    private String lyItemLabel;
    //存入类别状态 1未存样品, 2.识别存样 3.应急存样
    private String lyTypes;
    //识别存入时间
    private String lyTime;
    //标签识别图文地址,目前为路径
    private String lyLabelImg;
    //出样时间
    private String cyTime;
    //出样标签识别图文, 目前存储全路径
    private String cyLabelImg;
    //出样类别状态 1.未出样品 2.识别出样 3.应急出样
    private String cyTypes;
    //试点id
    private Integer sdId;
    //是否打印过(批次留样打印)
    private Integer isPrint;
}
