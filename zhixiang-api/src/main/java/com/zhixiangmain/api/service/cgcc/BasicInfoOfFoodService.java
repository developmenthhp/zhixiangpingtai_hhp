package com.zhixiangmain.api.service.cgcc;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.BasicInfoFood;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.module.excel.ReadDataBase;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.cgcc
 * @Description: 食材基础信息业务interface
 * @author: hhp
 * @date: 2019-02-24 10:26
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BasicInfoOfFoodService {
    PageDataResult getBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit);

    ResultBean checkName(BasicInfoFood basicInfoFood);

    ResultBean updateBasicInfoFood(BasicInfoFood basicInfoFood);

    ResultBean addBasicInfoFood(BasicInfoFood basicInfoFood);

    ResultBean getBasicInfoFoodById(int id, Integer sdid);

    ResultBean delBasicInfoFoodById(int id, Integer sdid);

    ResultBean doAddFood();

    List<Map<String,Object>> getBasicInfoFoodByIds(String ids);

    PageDataResult getInsideBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit);

    ResultBean getAll(Integer sdid);

    ResultBean importBasePrice(Integer sdid, List<ReadDataBase> namePrices);

    ResultBean getBaseInfoOfFoodBySdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj);

    ResultBean getAllIngBBySdId(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit, JSONObject jobj);

    ResultBean getIngBUseBySdIdName(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj);
}
