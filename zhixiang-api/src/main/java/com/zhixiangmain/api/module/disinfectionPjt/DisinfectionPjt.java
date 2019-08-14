package com.zhixiangmain.api.module.disinfectionPjt;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.disinfectionPjt
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "disinfection_pjt")
public class DisinfectionPjt implements Serializable {
    //
    private Integer id;

    //消毒类别名称, 碗筷 等
    private String disinfectionName;

    //规定消毒时间 统一用分钟
    private String disinfectionTime;

    //试点id,与之关联
    private Integer sdid;

    //删除标识
    private Integer del=1;

    //消毒方法和描述
    private String disinfectionMethod;

    private String disinfectiontemperature;//规定温度
    private String disinfectionimg;//图片
}
