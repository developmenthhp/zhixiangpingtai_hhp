package com.zhixiangmain.api.module.lechengAptureRecord;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.lechengAptureRecord
 * @Description: 乐橙抓拍实体
 * @author: hhp
 * @date: 2019-01-17 17:38
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "lecheng_apture_record")
public class LechengAptureRecord implements Serializable {
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
    //'检测识别状态(1.正常 ，2.告警.3已处理)'
    private String detectionStatus;
}
