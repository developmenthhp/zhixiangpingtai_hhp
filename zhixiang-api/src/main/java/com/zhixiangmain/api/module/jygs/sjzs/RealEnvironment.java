package com.zhixiangmain.api.module.jygs.sjzs;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.sjzs
 * @Description: 区域展示
 * @author: hhp
 * @date: 2019-04-07 14:47
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="liveshow")
public class RealEnvironment extends BaseEntity {
    private Integer areaId;//区域id
    private String img;//拍照图片
    private Integer sdId;//试点id
    private Integer  accountId;//用户的id
}
