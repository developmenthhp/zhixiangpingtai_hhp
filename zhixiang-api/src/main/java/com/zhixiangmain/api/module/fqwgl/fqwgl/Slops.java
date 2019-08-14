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
 * @date: 2019-06-11 11:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "slops")
public class Slops extends BaseEntity {
    //回收时间
    private String recoveryTime;
    //称重人
    private Integer recyclingPersion;
    //回收重量
    private Double recyclingQuantity;
    //食材照片 多张
    private String foodPhotos;
    //图片
    private String photo;
    //经营点表id,与之关联
    private Integer sdId;
    private  String unitName;	//单位名称
    private  String address	;//地址
    private  String phone;//联系电话
    private  String recycleUse;//回收用途
    private Integer haveOrNotAptitude;//有无资质 1.有 2.无
}
