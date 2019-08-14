package com.zhixiangmain.api.module.jygs.gszx;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.gszx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-07 13:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="companyinformation")
public class CompanyInformation extends BaseEntity {
    private String companyName;//公司名称
    private String detailedAddress;//详细地址
    private String principal;//负责人
    private String contactWay;//联系方式
    private Integer diningType; //餐饮类型 1，社会餐饮，2其他餐饮
    private String businessHours;//营业时间
    private Integer operatingState;//营业状态:1正在营业，2暂停营业
    private String foodSafetyName;//食品安全员姓名
    private String foodSafetyImg;//食品安全员照片
    private String foodSafetyPhone ;//食品安全员手机
    private String supervisorName ;//监管人员姓名
    private String supervisorPhone ;//监管人员电话
    private String supervisorImg ;//监管人员的照片
    private String commitmentFoodSafetyImg ;//食品安全承诺书
    private Integer sdId;//试点id
    private Integer accountId;//用户的id
}
