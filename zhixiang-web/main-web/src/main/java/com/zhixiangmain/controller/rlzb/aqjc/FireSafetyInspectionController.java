package com.zhixiangmain.controller.rlzb.aqjc;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.rlzb.aqjc.dto.FireSafetyInspectionDTO;
import com.zhixiangmain.api.service.rlzb.aqjc.FireSafetyInspectionService;
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
 * @Package com.zhixiangyun.controller.rlzb.aqjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-03 13:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/fireSafetyInspection")
public class FireSafetyInspectionController {
    private static final Logger logger = LoggerFactory
            .getLogger(FireSafetyInspectionController.class);
    @Reference(version = "1.0.0")
    private FireSafetyInspectionService fireSafetyInspectionService;

    /**
     * 查询本月安全检查记录
     * @return ModelAndView
     */
    @PostMapping( "/getByMonth")
    @ResponseBody
    public ResultBean getByMonth(FireSafetyInspectionDTO fireSafetyInspectionDTO) {
        logger.debug("查询本月安全检查记录！搜索条件：fireSafetyInspectionDTO：" + fireSafetyInspectionDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            fireSafetyInspectionDTO.setUserId(existUser.getId());
            String path = FireSafetyInspectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = fireSafetyInspectionService.getByMonth(fireSafetyInspectionDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取本月安全检查记录异常！", e);
        }
        return resultBean;
    }
}
