package com.zhixiangmain.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.base.dto.CertificateWarningDTO;
import com.zhixiangmain.api.service.base.CertificateWarningService;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.overallParam.OpslabConfig;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.controller.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-22 15:41
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/certificateWarning")
public class CertificateWarningController {
    private static final Logger logger = LoggerFactory
            .getLogger(CertificateWarningController.class);
    //@Reference(version = "1.0.0", timeout = 1200000, retries = 3)
    @Reference(version = "1.0.0")
    private CertificateWarningService certificateWarningService;

    /**
     * 统计健康证，公司营业执照到期预警
     * @return ModelAndView
     */
    @PostMapping( "/getTopCertificateWarning")
    @ResponseBody
    public ResultBean getTopCertificateWarning(CertificateWarningDTO certificateWarningDTO) {
        logger.debug("查询食材核准信息列表！搜索条件：certificateWarningDTO：" + certificateWarningDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            certificateWarningDTO.setUserId(existUser.getId());
            String path = CertificateWarningController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = certificateWarningService.getTopCertificateWarning(certificateWarningDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取查询食材核准信息列表异常！", e);
        }
        return resultBean;
    }
}
