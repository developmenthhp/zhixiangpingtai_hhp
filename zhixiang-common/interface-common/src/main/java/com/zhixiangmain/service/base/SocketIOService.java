package com.zhixiangmain.service.base;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-01 11:17
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SocketIOService {
    void send();
    String getMsg();
    void setMsg(String msg);
}
