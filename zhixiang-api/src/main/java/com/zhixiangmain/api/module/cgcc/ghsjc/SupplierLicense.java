package com.zhixiangmain.api.module.cgcc.ghsjc;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.cgcc.ghsjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 14:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="supplierlicense")
public class SupplierLicense extends BaseEntity {
    //单位名称
    private String name;
    //法定代表人
    private String representative;
    //试点
    private Integer sdId;
    //有效期限
    private String validTime;
    //预警日期
    private String warningDate;
    //编号
    private String serialNumber;
    //用户的id
    private Integer supplierId;
    //餐饮许可证照片
    private String licenceImg;
}
