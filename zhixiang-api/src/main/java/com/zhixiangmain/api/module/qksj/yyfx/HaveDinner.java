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
 * @date: 2019-06-18 11:04
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name = "havedinner")
public class HaveDinner extends BaseEntity {
    private String food;//食品名
    private Double calorie;//卡路里
    private Integer menuOrder;//餐次 1早餐 2午餐 3晚餐 4夜宵
    private Integer nutId;
    private Integer sdId;//试点id
}
