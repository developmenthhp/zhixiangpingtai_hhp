package com.zhixiangmain.controller.cpjs.cpmx;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsBlendDTO;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsDTO;
import com.zhixiangmain.api.service.cpjs.cpmx.FoodMenuDetailService;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.overallParam.OpslabConfig;
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
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.controller.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-09 13:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/foodMenuDetail")
public class FoodMenuDetailController {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodMenuDetailController.class);
    @Reference(version = "1.0.0")
    private FoodMenuDetailService foodMenuDetailService;
    /**
     * 试点id菜单id查找该菜单下的所有菜品
     * @return ModelAndView
     */
    @PostMapping( "/getBySdIdFMId")
    @ResponseBody
    public ResultBean getBySdIdFMId(FoodMenuDetailsDTO foodMenuDetailsDTO) {
        logger.debug("根据试点id菜单id查找该菜单下的所有菜品！搜索条件：foodMenuDetailsDTO：" + foodMenuDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            String path = FoodMenuDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodMenuDetailService.getBySdIdFMId(foodMenuDetailsDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("根据试点id菜单id查找该菜单下的所有菜品异常！", e);
        }
        return resultBean;
    }

    /**
     * 更新，添加，修改，三合一
     * @return ModelAndView
     */
    @PostMapping( "/updateBlend")
    @ResponseBody
    public ResultBean updateBlend(FoodMenuDetailsBlendDTO foodMenuDetailsBlendDTO) {
        logger.debug("更新，添加，修改，三合一更新菜谱明细！条件：foodMenuDetailsBlendDTO：" + foodMenuDetailsBlendDTO);
        ResultBean resultBean = new ResultBean();
        try {
            String path = FoodMenuDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodMenuDetailService.updateBlend(foodMenuDetailsBlendDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("根据试点id菜单id查找该菜单下的所有菜品异常！", e);
        }
        return resultBean;
    }
}
