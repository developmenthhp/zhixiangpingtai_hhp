package com.zhixiangmain.web.responseConfig;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.web.responseConfig
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 17:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class EarlyWarningResult implements Serializable {
    /** 成功，失败标识 */
    private boolean flag;
    /** 其他参数 */
    private Object code;
    /** 消息提示 */
    private String msg;
    /** 结果数据对象集合 */
    private List<?> msurementList;
    private List<?> cleanRecordList;
    private List<?> disinfectionList;
    private List<?> slipperyAlertList;
    private List<?> gasaramrList;
    private List<?> ratplateAlertList;
    private List<?> fromwallAlertList;
    private List<?> gasSwitchAlertList;
    private List<?> pepoleTempList;
    private List<?> lechengCheckRecordList;
    private List<?> lechengAptureRecordList;

    public EarlyWarningResult() {}
}
