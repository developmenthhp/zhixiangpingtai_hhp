package com.zhixiangmain.dao.hjjc.wsbjxx;

import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.hjjc.wsbjxx.dto.GasaramrDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.hjjc.wsbjxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 17:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface GasaramrMapper {
    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    List<Map<String,Object>> findMonthBySdId(GasaramrDTO gasaramrDTO);
}
