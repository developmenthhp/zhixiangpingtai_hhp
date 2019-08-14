package com.zhixiangmain.api.module.hjjc.mqkgbjxx;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.hjjc.mqkgbjxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 17:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="gasswitchalert")
public class GasSwitchAlert extends BaseEntity {
    //区域名称
    private String area;
    //传感器编号
    private String sensor;
    //开始发生时间
    private String startTime;
    //结束时间
    private String endTime;
    //状态 1.警报中 2.已处理 3.已作废 4.正常
    private Integer status;
    //开启的乐橙抓拍图片
    private String openUrlImg;
    //关闭的乐橙抓拍图片
    private String offUrlImg;
    //描述
    private String description;
    //试点id
    private Integer sdId;
}
