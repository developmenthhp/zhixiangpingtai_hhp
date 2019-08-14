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
 * @date: 2019-06-11 12:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "surplusfood")
public class SurplusFood extends BaseEntity {
    //回收时间
    private String recoveryTime;
    //称重人
    private Integer recyclingPersion;
    //回收重量
    private Double recyclingQuantity;
    //拖货人
    private Integer dragPersion;
    //图片
    private String photo;
    //经营点表id,与之关联
    private Integer sdId;
    //称重人名
    private String recName;
    //称重人门禁图片
    private String recPhoto;
    //拖货人名
    private String draName;
    //拖货人门禁图片
    private String draPhoto;
}
