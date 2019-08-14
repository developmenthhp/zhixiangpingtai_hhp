package com.zhixiangmain.api.module.hjjc.wsbjxx;
import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.hjjc.wsbjxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 17:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="gasaramr")
public class Gasaramr extends BaseEntity {
    //区域
    private String aramArea;
    //时间
    private String aramTime;
    //描述信息
    private String descriptionInfo;
    //1报警中 2已处理 3作废
    private String handleStatus;
    //
    private Integer sdId;
}
