package com.zhixiangmain.dao.cleanJl;

import com.zhixiangmain.api.module.cleanRecord.CleanRecord;
import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.cleanJl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CleanJlMapper {
    CleanRecord findAlertOneCleanJl(@Param("sdId") int sdId);
}
