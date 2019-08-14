package com.zhixiangmain.api.module.lygl.lygl.dto;

import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.lygl.lygl.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-08 18:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class KeepSampleDTO extends BaseEntityDTO {
    private Integer start;
    private Integer end;
    private String menuOrder;
    private String status;
    private String timeQuantum;
    private String startTime;
    private String endTime;
}
