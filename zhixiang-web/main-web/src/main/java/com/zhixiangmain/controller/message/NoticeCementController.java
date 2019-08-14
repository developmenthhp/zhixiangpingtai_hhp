package com.zhixiangmain.controller.message;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.message.dto.NoticeCementDTO;
import com.zhixiangmain.api.service.message.NoticeCementService;
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
 * @Package com.zhixiangyun.controller.message
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-20 16:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/noticeCement")
public class NoticeCementController {
    private static final Logger logger = LoggerFactory
            .getLogger(NoticeCementController.class);
    //设置超时时间timeout  设置超时重连次数retries
    @Reference(version = "1.0.0")
    private NoticeCementService noticeCementService;

    /**
     * 食材核准信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getTopNoticeCements")
    @ResponseBody
    public ResultBean getTopNoticeCements(NoticeCementDTO noticeCementDTO) {
        logger.debug("查询食材核准信息列表！搜索条件：noticeCementDTO：" + noticeCementDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            noticeCementDTO.setUserId(existUser.getId());
            String path = NoticeCementController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = noticeCementService.getTopNoticeCements(noticeCementDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取查询食材核准信息列表异常！", e);
        }
        return resultBean;
    }
}
