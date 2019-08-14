package com.zhixiangmain.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixiangmain.module.base.mail.vo.MailBeanVO;
import com.zhixiangmain.service.base.EmailService;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.file.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.controller.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 17:47
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/email")
public class EmailController {
    private static final Logger logger = LoggerFactory
            .getLogger(EmailController.class);
    @Reference(version = "1.0.0")
    private EmailService emailService;
    @PostMapping( "/sendMailAttachment")
    @ResponseBody/*MailBeanVO mailBeanVO, */
    public ResultBean sendMailAttachment(MailBeanVO mailBeanVO, @RequestParam("emailFile")MultipartFile emailFile) {
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        String[] fileType = {};
        ResultBean resultBean = fileUploadUtil.uploadMyFile(emailFile,fileType);
        try {
            resultBean = emailService.sendMailAttachment(mailBeanVO,resultBean);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg(WebMassage.EMAIL_SEND_ERROR);
            logger.error("发送邮件异常！", e);
        }
        return resultBean;
    }
}
