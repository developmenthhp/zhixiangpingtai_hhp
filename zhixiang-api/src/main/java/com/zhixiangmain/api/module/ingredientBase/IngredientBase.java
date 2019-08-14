package com.zhixiangmain.api.module.ingredientBase;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.ingredientBase
 * @Description: 食材基础信息表
 * @author: hhp
 * @date: 2019-01-12 17:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="ingredientbase")
public class IngredientBase extends BaseEntity {
    //食材药材配料等信息名称
    private String ingredientName;
    //计量单位名称
    private String meteringName;
    //大类类别关联名称
    private String mainName;
    //小类类别关联名称
    private String smallName;
    //单一图文路径
    private String scspImgPath;
    //额定常见存储日期
    private Integer ratedTerm;
    private String ratedTermDW;
    //创建日期
    private String createDate;
    //检测状态 1检测2不检测
    private String checkStatus;
    //计算核实单位
    private String unit;
    //基础价格
    private Double basePrice;
    //存储警戒
    private Double inventoryLimit;
    //试点id
    private Integer sdId;
    //物理标识 1正常 0已删除
    private String deleteStatus;
    private String ggxh;
    //规格型号单位
    private String ggxhdw;
    private Integer suppId;//供应商id
    private Integer whouseId;//仓库id
    private String suppName;//供应商名称
    private String whouseName;//仓库名称
}
