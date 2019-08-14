package com.zhixiangmain.dao.lechengAptureRecord;

import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.module.lechengAptureRecord.LechengAptureRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 17:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LechengAptureRecordMapper {
    LechengAptureRecord findTopOne(@Param("sdId") int sdId, @Param("type") String type);

    Integer findTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO);

    List<Map<String,Object>> findAbnormalSnapshot(AbnormalSnapshotDTO abnormalSnapshotDTO);
}
