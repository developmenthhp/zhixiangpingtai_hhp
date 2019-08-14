package com.zhixiangmain.service.base;

import com.zhixiangmain.module.base.mail.vo.MailBeanVO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.service.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 17:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface EmailService {
    ResultBean sendMailAttachment(MailBeanVO mailBeanVO, ResultBean resultBean);
}
