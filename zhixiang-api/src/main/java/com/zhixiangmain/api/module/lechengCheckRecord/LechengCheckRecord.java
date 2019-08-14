package com.zhixiangmain.api.module.lechengCheckRecord;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.lechengCheckRecord
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "lecheng_check_record")
public class LechengCheckRecord implements Serializable {
    private Integer id;
    //区域名称
    private String areaName;
    //设备标示号
    private String deviseNumber;
    //试点id
    private Integer sdId;
    //时间
    private String createTime;
    //图片地址
    private String imgUrl;
    //类型 :1 规范礼仪抓拍，2.不明身份
    private String type;
    //检测状态(1.正常，2.告警 , 3.已处理)',
    private String detectionStatus;
    private String violationName;
}
