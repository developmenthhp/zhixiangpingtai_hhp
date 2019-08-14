package com.zhixiangmain.dao.cgcc.scycg;

import com.zhixiangmain.api.module.cgcc.scycg.MainLibraryPurchase;
import com.zhixiangmain.api.module.cgcc.scycg.dto.MainLibraryPurchaseDTO;
import com.zhixiangmain.api.module.message.vo.MessageVO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.cgcc.scycg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-19 17:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MainLibraryPurchaseMapper {
    List<MessageVO> findAllByStatus(Integer sdId);

    Integer findAllTotalByStatus(Integer sdId);

    List<Map<String,Object>> findMainLPList(MainLibraryPurchaseDTO mainLibraryPurchaseDTO);

    Integer findMainLPListTotal(MainLibraryPurchaseDTO mainLibraryPurchaseDTO);

    Integer updateStatusByIdSdId(MainLibraryPurchase mainLibraryPurchase);
}
