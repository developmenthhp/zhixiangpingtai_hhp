package com.zhixiangmain.dao.lygl.lyglxq;

import com.zhixiangmain.api.module.lygl.lyglxq.dto.KeepSampleDetailDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.dao.lygl.lyglxq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-10 17:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface KeepSampleServiceDetailMapper {
    List<Map<String,Object>> findDetailByIdSdId(KeepSampleDetailDTO keepSampleDetailDTO);
}
