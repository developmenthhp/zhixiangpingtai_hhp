package com.zhixiangmain.dao.hjjc.mqkgbjxx;

import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.hjjc.mqkgbjxx.dto.GasSwitchAlertDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.hjjc.mqkgbjxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 17:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface GasSwitchAlertMapper {
    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    List<Map<String,Object>> findMonthBySdId(GasSwitchAlertDTO gasSwitchAlertDTO);
}
