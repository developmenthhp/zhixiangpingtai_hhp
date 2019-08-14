package com.zhixiangmain.api.module.fqwgl.fqwgl;
import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.fqwgl.fqwgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 12:17
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "wasteoil")
public class WasteOil extends BaseEntity {
    //处理时间
    private String handlingTime;
    //地点
    //private String oiladdress;
    //重量
    private Double oilQuantity;
    //经办人
    //private String agent;
    //称重人
    private Integer chargePersion;
    //拖货人
    private String oilPhotos;
    //签发人
    //private String signer;
    //拖货图片
    private String photo;
    //试点id
    private Integer sdId;
    private  String unitName;	//单位名称
    private  String address	;//地址
    private  String phone;//联系电话
    private  String recycleUse;//回收用途
    private Integer haveOrNotAptitude;//有无资质 1.有 2.无
}
