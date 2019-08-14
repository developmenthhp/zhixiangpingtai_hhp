package com.zhixiangmain.api.module.cgcc.scycg;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.cgcc.scycg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-19 16:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="librarypurchasehead")
public class MainLibraryPurchase extends BaseEntity {
    //站点编号
    private String siteNum;
    //站点名称
    private String siteName;
    //单据编号
    private Integer billsNum;
    /*
     * 总站点编号  总站点编号保存在redis（redis现在公用一个）
     *  每个站点在保存总站点编号的时候从redis里取
     *  取出来之后在取出来的值上加一，然后将这个加完一之后的值存入redis 并关闭redis
     *
     *  查询的时候在每个站点就显示单据编号
     *  总站点就显示每个站点的总站点编号
     * */
    private Integer totalStation;
    //制单时间
    private String documentTime;
    //制单人
    private String documentMaker;
    //试点id
    private Integer sdId;
    //提交审核状态 0未提交 1已提交未审核 2审核未通过 3审核通过
    private Integer status;
    //1菜品搭配不合理 2需求数量不合理 3其他原因
    private Integer reason;
}
