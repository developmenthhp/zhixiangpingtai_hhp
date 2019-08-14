package com.zhixiangmain.service.system;

import com.zhixiangmain.module.system.RequestLog;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.system
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-12-27 12:46
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RequestLogService {

    public RequestLog save(RequestLog requestLog);

    public RequestLog getById(Long id);

    public List<RequestLog> getAll();

    public void delById(Long id);
}
