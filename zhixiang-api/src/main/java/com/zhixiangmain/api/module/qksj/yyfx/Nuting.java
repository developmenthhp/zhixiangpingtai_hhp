package com.zhixiangmain.api.module.qksj.yyfx;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.qksj.yyfx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 11:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "nuting")
public class Nuting extends BaseEntity {
    private String name;//姓名
    private Integer sex;//性别 1男 2女
    private Integer age;//年龄
    private Integer grade;// 年级
    private Integer clas;// 班级
    private String carId;//卡用来标识唯一身份的键
    private String menuDate;//用餐日期
    private Integer status;//摄入量状态 1正常 2偏低 3偏高
    private Integer sdId;//试点id
    //父母亲电话
    private String phone;
}
