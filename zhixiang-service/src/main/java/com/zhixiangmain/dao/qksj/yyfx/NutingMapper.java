package com.zhixiangmain.dao.qksj.yyfx;

import com.zhixiangmain.api.module.qksj.yyfx.dto.NutingDTO;
import com.zhixiangmain.api.module.qksj.yyfx.vo.NutingVO;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.dao.qksj.yyfx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 11:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface NutingMapper {
    Integer findPaginationDataTotal(NutingDTO nutingDTO);

    List<NutingVO> findRankingListBySdId(NutingDTO nutingDTO);
}
