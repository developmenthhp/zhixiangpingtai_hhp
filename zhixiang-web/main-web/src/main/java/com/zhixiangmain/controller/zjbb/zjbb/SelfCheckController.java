package com.zhixiangmain.controller.zjbb.zjbb;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.service.zjbb.zjbb.SelfCheckService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import net.sf.ehcache.search.impl.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.controller.zjbb.zjbb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-12 14:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/selfCheck")
public class SelfCheckController {
    private static final Logger logger = LoggerFactory
            .getLogger(SelfCheckController.class);
    @Reference(version = "1.0.0")
    private SelfCheckService selfCheckService;

    /**
     * 预警列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getModularPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回预警页面");
        ModelAndView mav = new ModelAndView("/zjbb/zjbb/zjbb");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回预警页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取某些站点报表管理
     * @return ModelAndView
     */
    @PostMapping( "/getTotalBySdId")
    @ResponseBody
    public ResultBean getTotalBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行报表分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = SelfCheckController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = selfCheckService.getTotalBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("报表分析异常");
            logger.error("报表分析异常！", e);
        }
        return resultBean;
    }
}
