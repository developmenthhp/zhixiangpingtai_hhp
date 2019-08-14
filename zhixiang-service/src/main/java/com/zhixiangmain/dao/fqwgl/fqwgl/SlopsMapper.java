package com.zhixiangmain.dao.fqwgl.fqwgl;

import com.zhixiangmain.api.module.fqwgl.fqwgl.dto.WasteDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.dao.fqwgl.fqwgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 12:30
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SlopsMapper {
    List<Map<String,Object>> findWasteMonthBySdId(WasteDTO wasteDTO);
}
