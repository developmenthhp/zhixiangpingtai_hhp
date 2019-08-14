package com.zhixiangmain.web.responseConfig;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web.responseConfig
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-12-01 16:08
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class PageDataResult implements Serializable {

    //总记录数量
    private Integer totals;
    //当前页数据列表
    private List<?> list;

    private Integer code;
    private String imgPath;

    private String coreName;

    public PageDataResult() {
    }

    public PageDataResult( Integer totals,
                           List<?> list) {
        this.totals = totals;
        this.list = list;
    }

}
