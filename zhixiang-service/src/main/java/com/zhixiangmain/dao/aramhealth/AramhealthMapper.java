package com.zhixiangmain.dao.aramhealth;

import com.zhixiangmain.api.module.aramhealth.Aramhealth;
import com.zhixiangmain.api.module.message.dto.NoticeCementDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.aramhealth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface AramhealthMapper {
    Aramhealth findAll(@Param("aramhealthg") Aramhealth aramhealthg);

    List<Map<String,Object>> findTopAramHealths(NoticeCementDTO noticeCementDTO);
}
