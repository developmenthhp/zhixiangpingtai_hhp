package com.zhixiangmain.dao.cgcc.sccg;

import com.zhixiangmain.api.module.cgcc.sccg.dto.LibraryPurchaseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.cgcc.sccg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-22 11:05
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryPurchaseMapper {
    List<Map<String,Object>> findMainLPList(@Param("mainId") Integer mainId, @Param("sdId") Integer sdId);

    void updateAuthorizedPriceBySdIdId(@Param("id") String id, @Param("price") String price, @Param("sdId") String sdId, @Param("mainId") String mainId);

    Integer findLibraryPurchasesTotal(LibraryPurchaseDTO libraryPurchaseDTO);

    List<Map<String,Object>> findLibraryPurchases(LibraryPurchaseDTO libraryPurchaseDTO);
}
