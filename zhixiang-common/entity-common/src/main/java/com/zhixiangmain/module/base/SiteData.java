package com.zhixiangmain.module.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-22 9:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SiteData implements Serializable {
    private String dateSourceName;
    private String name;
    private String photo;
}
