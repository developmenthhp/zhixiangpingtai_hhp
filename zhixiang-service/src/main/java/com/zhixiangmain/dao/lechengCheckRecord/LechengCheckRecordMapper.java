package com.zhixiangmain.dao.lechengCheckRecord;

import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.module.base.dto.EmploymentViolationDTO;
import com.zhixiangmain.api.module.lechengCheckRecord.LechengCheckRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.lechengCheckRecord
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LechengCheckRecordMapper {
    LechengCheckRecord findTopOne(@Param("sdId") int sdId, @Param("type") String type);

    Integer findTotalEmploymentViolation(EmploymentViolationDTO employmentViolationDTO);

    List<Map<String,Object>> findAbnormalSnapshot(AbnormalSnapshotDTO abnormalSnapshotDTO);
}
