package com.zhixiangmain.api.module.cpjs.zdcpbom;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cpjs.zdcpbom
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 10:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="foodbase")
public class FoodBase extends BaseEntity {
    //适用菜谱类型:1早餐 2午餐 3晚餐    // 4早餐午餐 5午餐晚餐 6早餐晚餐 7早餐晚餐中餐
    private String menuType;
    //食品名称
    private String foodName;
    //计量单位名称
    private String meteringName;
    //重量
    private Double weight;
    //图文信息路径,不携带前缀信息
    private String scspImgPath;
    //创建时间
    private String gmtDate;
    //试点id
    private Integer sdId;
    //删除标识1正常0 已删除
    private String deleteStatus;
    //价格
    private Double price;
    //检测状态 1检测2不检测
    private String checkStatus;
    //食品菜品图文明细描述
    private String foodContext;
    //作用域
    private String simpleScopes;

    private String[] menuTypeSelect;
}
