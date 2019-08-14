package com.zhixiangmain.api.module.jygs.gsry;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.gsry
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-03 13:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="companyhonor")
public class CompanyGlories extends BaseEntity {
    private String certificateImg;//证书图片
    private String zhengPaiImg;//证牌图片
    private Integer accountId;//用户的id
    private Integer sdId;//试点的id
}
