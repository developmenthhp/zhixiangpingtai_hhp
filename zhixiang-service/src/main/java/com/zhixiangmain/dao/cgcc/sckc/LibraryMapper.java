package com.zhixiangmain.dao.cgcc.sckc;

import com.zhixiangmain.api.module.cgcc.StockOMaterials.dto.LibrarySearchDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.cgcc.scjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-01 8:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryMapper {
    Integer findLibrariesTotal(LibrarySearchDTO librarySearchDTO);

    List<Map<String,Object>> findLibraries(LibrarySearchDTO librarySearchDTO);
}
