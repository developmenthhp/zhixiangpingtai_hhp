package com.zhixiangmain.dao.hjjc.dmjsjb;

import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.hjjc.dmjsjb.SlipperyAlert;
import com.zhixiangmain.api.module.hjjc.dmjsjb.dto.SlipperyAlertDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.hjjc.dmjsjb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 11:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SlipperyAlertMapper {
    SlipperyAlert findAll(@Param("sdId") int sdId);

    //List<Map<String,Object>> findSlipperyAlerts(SlipperyAlertDTO slipperyAlertDTO);
    List<Map<String,Object>> findSlipperyAlerts(SlipperyAlertDTO slipperyAlertDTO);

    Integer findSlipperyAlertTotal(SlipperyAlertDTO slipperyAlertDTO);

    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    List<Map<String,Object>> findMonthBySdId(SlipperyAlertDTO slipperyAlertDTO);
}
