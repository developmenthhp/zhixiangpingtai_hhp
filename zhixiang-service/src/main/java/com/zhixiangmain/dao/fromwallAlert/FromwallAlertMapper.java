package com.zhixiangmain.dao.fromwallAlert;

import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.fromwallAlert.FromwallAlert;
import com.zhixiangmain.api.module.fromwallAlert.dto.FromwallAlertDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.fromwallAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FromwallAlertMapper {
    FromwallAlert findAll(@Param("sdId") int sdId);

    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    Integer findWarningTotal(FromwallAlertDTO fromwallAlertDTO);

    List<Map<String,Object>> findMonthBySdId(FromwallAlertDTO fromwallAlertDTO);
}
