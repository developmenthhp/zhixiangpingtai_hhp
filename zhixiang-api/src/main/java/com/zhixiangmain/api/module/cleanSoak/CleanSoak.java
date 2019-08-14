package com.zhixiangmain.api.module.cleanSoak;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.cleanSoak
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:26
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "clean_soak")
public class CleanSoak implements Serializable {
    //id主键
    private Integer id;

    //清洗类别 蔬菜,猪肉,牛肉,腌制品
    private String cleanName;

    //清洗时间统一用分钟
    private String cleanTime;

    //注意事项
    private String matters;

    //清洗浸泡水位 统一用厘米
    private String waterlevel;

    //试点
    private Integer sdid;


    private String cleanimg;//清洗类别图片一张
}
