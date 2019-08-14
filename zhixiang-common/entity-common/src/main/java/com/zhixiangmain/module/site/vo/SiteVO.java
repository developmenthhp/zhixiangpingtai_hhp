package com.zhixiangmain.module.site.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.permission.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 14:58
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SiteVO implements Serializable {
    private static final long serialVersionUID = -2783081162690878303L;
    private Integer id;
    private Integer pid;//父id
    private Integer zindex;//优先级
    private String name;//站点名成
    private String address;//站点地址描述
    private String photo;//站点图标
    private Double lng;//经度
    private Double lat;//纬度
    private Integer sdId;
    private boolean checked;
    private boolean open;
}
