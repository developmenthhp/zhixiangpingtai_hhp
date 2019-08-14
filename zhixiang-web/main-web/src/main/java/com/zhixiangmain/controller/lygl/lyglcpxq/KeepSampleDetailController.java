package com.zhixiangmain.controller.lygl.lyglcpxq;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.lygl.lyglxq.dto.KeepSampleDetailDTO;
import com.zhixiangmain.api.service.lygl.lyglxq.KeepSampleDetailService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.overallParam.OpslabConfig;
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
 * @Package com.zhixiangmain.controller.lygl.lyglcpxq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-10 16:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/keepSampleDetail")
public class KeepSampleDetailController {
    private static final Logger logger = LoggerFactory
            .getLogger(KeepSampleDetailController.class);
    @Reference(version = "1.0.0")
    private KeepSampleDetailService keepSampleDetailService;

    /**
     * 留样菜品详情列表
     * @return ModelAndView
     */
    @PostMapping( "/getDetailByIdSdId")
    @ResponseBody
    public ResultBean getDetailByIdSdId(KeepSampleDetailDTO keepSampleDetailDTO) {
        logger.debug("查询留样菜品详情信息列表！搜索条件：keepSampleDetailDTO：" + keepSampleDetailDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            keepSampleDetailDTO.setUserId(existUser.getId());
            String path = KeepSampleDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = keepSampleDetailService.getDetailByIdSdId(keepSampleDetailDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取留样菜品详情信息列表!=keepSampleDetailDTO:" + keepSampleDetailDTO);

        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取留样菜品详情信息列表异常！", e);
            resultBean.setMsg("获取留样菜品详情信息列表异常！");
        }
        return resultBean;
    }
}
