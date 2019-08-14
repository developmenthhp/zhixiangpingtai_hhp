package com.zhixiangmain.module.userSiteRole;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.userRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 14:54
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="siteRole")
public class UserSiteRoleKey implements Serializable {

    private Integer userId;

    private Integer siteRoleId;

}
