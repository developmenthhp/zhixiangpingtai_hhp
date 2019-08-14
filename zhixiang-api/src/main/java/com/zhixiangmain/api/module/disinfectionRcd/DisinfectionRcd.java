package com.zhixiangmain.api.module.disinfectionRcd;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.disinfectionRcd
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:05
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "disinfection_rcd")
public class DisinfectionRcd implements Serializable {
    private Integer id;
    //操作结束时间
    private String disinEndTime;
    //消毒开始时间日期,时间窜
    private String disinStartTime;
    //消毒操作人
    private String disinPerson;
    //实际耗费时间
    private String disinActualTime;
    //抓拍人图片
    private String disinPersonImg;
    //试点id
    private Integer sdId;
    //消毒操作人id
    private Integer disinPersonId;
    //规定耗费时间
    private String disinRegulaTime;
    //传感器编码
    private String disinSensorNum;
    //状态1.正在消毒2.消毒结束 3作废  4.准备消毒
    private String disinStatus;
    // 实时温度
    private String disinTyp;
    //结束原因
    private String disinReason;
    //规定温度
    private String disinTemperature;
    private Integer disinPjtId;
}
