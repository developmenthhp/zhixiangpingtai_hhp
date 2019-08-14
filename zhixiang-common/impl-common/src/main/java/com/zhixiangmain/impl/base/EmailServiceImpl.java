package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.component.Sender;
import com.zhixiangmain.module.base.mail.MailBean;
import com.zhixiangmain.module.base.mail.vo.MailBeanVO;
import com.zhixiangmain.service.base.EmailService;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 17:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = EmailService.class)
public class EmailServiceImpl implements EmailService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(EmailServiceImpl.class);
    @Autowired
    private Sender sender;

    @Override
    public ResultBean sendMailAttachment(MailBeanVO mailBeanVO, ResultBean resultBean1) {
        ResultBean resultBean = new ResultBean();
        MailBean mailBean = MailBean.getMailBean();
        if(resultBean1.getCode().toString().equals("1527")){

        }else if(resultBean1.getCode().toString().equals("1531")){
            return resultBean;
        }else if(resultBean1.getCode().toString().equals("1001")){
            return resultBean;
        }else{
                /*File file = new File(resultBean1.getMsg());
                mailBean.setFile(new FileSystemResource(file));*/
            mailBean.setUploadFilePath(resultBean1.getMsg());
        }
        mailBean.setAttachmentFilename(mailBeanVO.getAttachmentFilename());
        mailBean.setContentId(mailBeanVO.getContentId());
        mailBean.setReceiver(mailBeanVO.getReceiver());
        mailBean.setSubject(mailBeanVO.getSubject());
        mailBean.setText(mailBeanVO.getText());
        //TimeoutException
        sender.send(mailBean);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(WebMassage.EMAIL_HAS_BEEN_SEND);
        return resultBean;
    }
}
