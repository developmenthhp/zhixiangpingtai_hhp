package com.zhixiangmain.controller.zjbb.zjbb;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.jybb.kcpdbbsq.dto.IwarekCPDDTO;
import com.zhixiangmain.api.service.jybb.kcpdbbsq.IwarekCPDService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
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
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.controller.zjbb.zjbb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-13 15:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/iwarekCPD")
public class IwarekCPDController {
    private static final Logger logger = LoggerFactory
            .getLogger(IwareReportController.class);
    @Reference(version = "1.0.0")
    private IwarekCPDService iwarekCPDService;

    /**
     * 获取某些站点货品库存盘点管理
     * @return ModelAndView
     */
    @PostMapping( "/getTotalBySdIdStatus")
    @ResponseBody
    public ResultBean getTotalBySdIdStatus(IwarekCPDDTO iwarekCPDDTO) {
        logger.debug("平台对传过来的站点进行货品库存盘点分析！查询条件：iwarekCPDDTO：" + iwarekCPDDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        iwarekCPDDTO.setUserId(existUser.getId());
        String path = IwarekCPDController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = iwarekCPDService.getTotalBySdIdStatus(iwarekCPDDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("货品库存盘点分析异常");
            logger.error("货品库存盘点分析异常！", e);
        }
        return resultBean;
    }
}
