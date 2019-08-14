package com.zhixiangmain.dao.cgcc.scrk;

import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.api.module.cgcc.scrk.dto.WarehouseDetailDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.cgcc.scrk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-09 14:14
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface WarehouseDetailMapper {
    List<String> findBasicInfoFoodById(@Param("id") int id, @Param("sdId") Integer sdId);

    List<Map<String,Object>> findWarehouseDetails(WarehouseDetailDTO warehouseDetailDTO);

    Map<String,Object> findWarehouseDetailByIdSdId(WarehouseDetailDTO warehouseDetailDTO);

    List<Map<String,Object>> findIBMMonthBySdId(WarehouseDetailDTO warehouseDetailDTO);

    Double findWeightByIngBaseId(BasicInfoFoodDTO basicInfoFoodDTO);
}
