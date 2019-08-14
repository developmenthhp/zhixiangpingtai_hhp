package com.zhixiangmain.dao.ratplateAlert;

import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.ratplateAlert.RatplateAlert;
import com.zhixiangmain.api.module.ratplateAlert.dto.RatplateAlertDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.ratplateAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RatplateAlertMapper {
    RatplateAlert findAll(@Param("sdId") int sdId);

    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    Integer findWarRatTotal(RatplateAlertDTO ratplateAlertDTO);

    List<Map<String,Object>> findMonthBySdId(RatplateAlertDTO ratplateAlertDTO);
}
