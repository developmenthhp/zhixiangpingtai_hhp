package com.zhixiangmain.api.module.lygl.lygl;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.lygl.lygl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-08 18:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "lymain")
public class KeepSample extends BaseEntity {
    //菜谱餐次 1 早餐；2 午餐 ；3 晚餐；
    private String menuOrder;
    //数据记录状态;1 制样;2 存样;3 出样;4 作废此次
    private String status;
    //存样时间-管控日期
    private String depositTime;
    //存样员工
    private String depositOperator;
    //存样现场照
    private String depositImg;
    //出样日期时间
    private String cyTime;
    //出样操作人
    private String cyOperator;
    //出样现场图文
    private String cyOperatorImg;
    //试点
    private Integer sdId;
    private String  simpleScopes;//样品管控域
    //门号
    private Integer doorNumber;
}
