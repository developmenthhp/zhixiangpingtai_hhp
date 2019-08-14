package com.zhixiangmain.api.module.scgl.sckjgl;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.scgl.sckjgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 16:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="msurement")
public class FoodInspection {
    //供应商名称
    private Integer supplierId;
    //食材名称
    private String foodStuffName;
    //检测名称
    private String inspectionName;
    //检测内容
    private String mstContext;
    //直接存储时间
    private String mstDate;
    //异常类型1正常 2.异常
    private String warn;
    //状态 1 未处理  2已处理
    private String status;
    private Integer sdId;//试点id
    private String handler;  //处理的人
    private String handlingTime; //处理的时间
    private String handlerEvidence; //处理证据
}
