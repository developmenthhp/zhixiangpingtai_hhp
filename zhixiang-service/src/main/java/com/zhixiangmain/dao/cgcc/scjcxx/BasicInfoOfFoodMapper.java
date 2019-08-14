package com.zhixiangmain.dao.cgcc.scjcxx;

import com.zhixiangmain.api.module.cgcc.BasicInfoFood.BasicInfoFood;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.module.excel.ReadDataBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.base
 * @Description: 食材分类
 * @author: hhp
 * @date: 2019-02-25 10:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BasicInfoOfFoodMapper {
    List<Map<String,Object>> findBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO);

    Object checkNameTotal(BasicInfoFood basicInfoFood);

    int updateBasicInfoFood(BasicInfoFood basicInfoFood);

    int addBasicInfoFood(BasicInfoFood basicInfoFood);

    Map<String,Object> findBasicInfoFoodById(@Param("id") int id, @Param("sdId") Integer sdId);

    Integer delBasicInfoFoodById(@Param("id") int id, @Param("sdId") Integer sdId);

    Map<String,Object> getBasicInfoFoodByIds(@Param("id") String id);

    List<Map<String,Object>> findInsideBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO);

    List<Map<String,Object>> findAll(@Param("sdId") Integer sdId);

    Integer updateBasePriceBySdIdName(ReadDataBase readDataBase);

    List<Map<String,Object>> findBaseInfoOfFoodBySdId(BasicInfoFoodDTO basicInfoFoodDTO);

    List<Map<String,Object>> findMonthBySdId(BasicInfoFoodDTO basicInfoFoodDTO);

    List<String> findAllIngBNameBySdId(BasicInfoFoodDTO basicInfoFoodDTO);

    Integer findPaginationDataTotal(BasicInfoFoodDTO basicInfoFoodDTO);

    Map<String,Object> findByName(BasicInfoFoodDTO basicInfoFoodDTO);
}
