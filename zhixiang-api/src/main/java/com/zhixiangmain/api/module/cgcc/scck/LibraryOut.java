package com.zhixiangmain.api.module.cgcc.scck;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.cgcc.scck
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 17:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="libraryout")
public class LibraryOut extends BaseEntity {
    //出库日期
    private String outTime;
    //出库人
    private String outPeople;
    //出库人id
    private Integer outPeopleId;
    //出库状态 1未审核,2已审核,3.已出库,4.已作废
    private String outStatus;
    //食材基础表id
    private Integer ingredientBaseId;
    //出库数量
    private Double outCount;
    private Double uPrice;
    //计量单位(或可不用)
    private String outUnit;
    //领取的货品食材采购的批次
    private String batch;
    //出库货品的生产日期
    private String outDientBaseTime;
    //现场条码照片,app 出库有， 其他出库没有
    private String barCodeImg;
    private Integer sdId;
}
