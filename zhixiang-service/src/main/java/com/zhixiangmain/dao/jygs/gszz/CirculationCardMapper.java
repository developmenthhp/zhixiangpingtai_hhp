package com.zhixiangmain.dao.jygs.gszz;

import com.zhixiangmain.module.base.dto.BaseEntityDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.jygs.gszz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-07 15:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CirculationCardMapper {
    Integer findTotal(BaseEntityDTO baseEntityDTO);

    List<Map<String,Object>> findCirInfoBySdId(BaseEntityDTO baseEntityDTO);
}
