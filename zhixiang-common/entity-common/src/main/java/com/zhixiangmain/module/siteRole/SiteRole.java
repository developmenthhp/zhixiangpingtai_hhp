package com.zhixiangmain.module.siteRole;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.siteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 10:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="siteRole")
public class SiteRole implements Serializable {
    private Integer id;
    private String roleName;//角色名
    private String descpt;//角色描述
    private String code;//角色编号
    private Integer insertUid;//操作用户编号
    private Date insertTime;//添加数据时间
    private Date updateTime;//更新时间
    private Integer sdId;//试点id
}
