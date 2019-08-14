package com.zhixiangmain.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.base.dto.EmploymentViolationDTO;
import com.zhixiangmain.api.service.base.EmploymentViolationService;
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
 * @Description: 所有违规处理 清洗违规，清晰称重违规 ，风干称重违规，切配称重违规 ，烹饪称重违规 ，消毒违规，不戴口罩
 * @author: hhp
 * @date: 2019-04-24 10:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/employmentViolation")
public class EmploymentViolationController {
    private static final Logger logger = LoggerFactory
            .getLogger(EmploymentViolationController.class);
    //@Reference(version = "1.0.0", timeout = 1200000, retries = 3)
    @Reference(version = "1.0.0")
    private EmploymentViolationService employmentViolationService;

    /**
     * 获取所有待审签单总数
     * @return ModelAndView
     */
    @PostMapping( "/getTotalEmploymentViolation")
    @ResponseBody
    public ResultBean getTotalEmploymentViolation(EmploymentViolationDTO employmentViolationDTO) {
        logger.debug("获取所有待审签单总数！搜索条件：employmentViolationDTO：" + employmentViolationDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            employmentViolationDTO.setUserId(existUser.getId());
            String path = TrialSigningController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = employmentViolationService.getTotalEmploymentViolation(employmentViolationDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取所有待审签单总数异常！", e);
        }
        return resultBean;
    }
}
