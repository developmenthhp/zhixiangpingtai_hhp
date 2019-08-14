package com.zhixiangmain.api.module.message.vo;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.message.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-03 12:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class MessageVO extends BaseEntity {
    private String msgTimestamp;
    private String fromUserName;
    private String fromUserIcon;
    private String content;
    private Integer sdId;
    private Integer totalStation;
}
