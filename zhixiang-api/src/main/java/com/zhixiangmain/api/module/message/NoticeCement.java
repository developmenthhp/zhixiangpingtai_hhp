package com.zhixiangmain.api.module.message;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.message.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-20 17:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="notice_cement")
public class NoticeCement extends BaseEntity {
    //通知时间
    private String noticeTime;
    //发布者身份类型
    private String noticeType;
    //接收者 试点id , 逗号分割，只存储试点id
    private String noticeReceive;
    //封面图片路径, 逗号隔开
    private String noticeImgs;
    //公告标题
    private String noticeTitle;
    //内容
    private String noticeContent;
    //状态  1已发布  2撤销
    private String noticeStatus;
    //发布者id,和上面类型匹配
    private String noticeReleaseId;
}
