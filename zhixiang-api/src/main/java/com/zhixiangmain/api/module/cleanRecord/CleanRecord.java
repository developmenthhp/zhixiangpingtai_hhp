package com.zhixiangmain.api.module.cleanRecord;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cleanRecord
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "clean_record")
public class CleanRecord implements Serializable {
    private Integer id;//ID
    private Integer cleanType;//清洗浸泡类别
    private String czrName; //操作人名称
    private Integer czrId;   //操作人id
    private String sjTime;//实际耗时
    private String gdTime; //规定耗时
    //操作开始时间
    private String startTime;
    //操作结束时间
    private String endTime;
    //图像拍摄信息
    private String imgs;
    // 1.正在清洗   2.清洗结束  3.强制终止   5准备清洗
    private String status;
    //规定水位
    private String gdWater;
    //传感器编号
    private String cgqBh;
    //结束原因
    private String stopYy;
    //试点id
    private Integer sdId;
    //实时水位
    private String realWater;

}
