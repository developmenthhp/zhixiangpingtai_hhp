package com.zhixiangmain.dao.scgl.spszgl;

import com.zhixiangmain.api.module.scgl.spszgl.dto.WarehousDetailsDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.dao.scgl.spszgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 10:53
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CableTicketCardMapper {
    List<Map<String,Object>> findCTCBySdId(WarehousDetailsDTO warehousDetailsDTO);

    Integer findCTByDateSdId(WarehousDetailsDTO warehousDetailsDTO1);
}
