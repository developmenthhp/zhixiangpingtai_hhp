package com.zhixiangmain.api.module.jygs.gszz;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.jygs.gszz
 * @Description: 营业执照实体
 * @author: hhp
 * @date: 2019-04-07 14:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="business")
public class Business extends BaseEntity {
    private String registration;//注册号
    private String legalRepresentative;//法定代表人
    private String registeredCapital;//注册资本
    private String companyName;//公司名称
    private String address;//地址
    private String deadline; //营业期限
    private String businessScope;//经营范围
    private String mainTypes;//主体类型
    private String path;//营业执照上传图片
    private Integer accountId;//用户的id
    private Integer sdId ;//试点的id
    private String earlyWarningTime;//预警时间
    private String licenceImg;//餐饮许可证
    private String circulationCardImg;//餐饮流通证
}
