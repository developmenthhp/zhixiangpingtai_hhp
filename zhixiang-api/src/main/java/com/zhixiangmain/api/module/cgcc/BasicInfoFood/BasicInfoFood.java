package com.zhixiangmain.api.module.cgcc.BasicInfoFood;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cgcc.BasicInfoFood
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-24 10:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="ingredientbase")
public class BasicInfoFood extends BaseEntity {

    //食材药材配料等信息名称
    private String ingredientName;
    //计量单位名称
    private String meteringName;
    //大类类别关联id
    private Integer mainCategoryId;
    //小类类别关联id
    private Integer smallCategoryId;
    //单一图文路径
    private String scspimgPath;
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
    /**
     * 所属boom的分类(使用位运算)  这里设计成 油、盐、糖、配料只能单选，主料辅料可多选
     * 1(000001)主料 2(000010)辅料
     * 3(000011)主料和辅料  4(000100)配料
     * 5(000101)主料和配料  6(000110)配料和辅料
     * 7(000111) 主料辅料和配料  8(001000)油
     * 9(001001)主料和油  10(001010)辅料和油
     * 11(001011)主料辅料和油
     * 12(001100)配料和油 13(001101)主料配料和油
     * 14(001110)辅料配料和油 15(001111)主料辅料配料和油
     * 16(010000)盐 17(010001)主料和盐
     * 18(010010) 辅料和盐 19(010011)
     * 主料辅料和盐 20(010100)配料和盐
     32(100000) 糖
     * */
    private Integer boomType;

    //大类编号 1:sc:蔬菜  2:scp:水产品  3:dp:冻品 4:gt:干调 5:hc:荤菜 6:sg:水果
    private Integer bigCategory;
    //食材等级 1:a 2:b 3:c 4:d
    private Integer level;
    //食材编码
    private Integer foodCode;
}
