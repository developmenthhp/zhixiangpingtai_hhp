package com.zhixiangmain.module.base.mail.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.base.mail.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 18:03
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class MailBeanVO implements Serializable {
    //收件人
    private String receiver;
    //邮件主题
    private String subject;
    //邮件内容
    private String text;
    //附件
    private String fileResource;
    //附件名称
    private String attachmentFilename;
    //内容ID，用于发送静态资源邮件时使用
    private String contentId;
}
