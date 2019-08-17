package com.zhixiangmain.module.site.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.module.site.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/16 14:41
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SiteDTO implements Serializable {
    private Integer page;
    private Integer limit;
    private Integer sdId;
}
