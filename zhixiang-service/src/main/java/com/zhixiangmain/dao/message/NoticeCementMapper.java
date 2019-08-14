package com.zhixiangmain.dao.message;

import com.zhixiangmain.api.module.message.dto.NoticeCementDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.message
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-20 17:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface NoticeCementMapper {
    Map<String,Object> findTopNoticeCements(NoticeCementDTO noticeCementDTO);
}
