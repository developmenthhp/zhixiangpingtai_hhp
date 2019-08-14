package com.zhixiangmain.module.userSiteRole.vo;

import com.zhixiangmain.module.userSiteRole.UserSiteRoleKey;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.userSiteRole.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 10:26
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserSiteRolesVO implements Serializable {
    private Integer id;
    private String username;
    private String mobile;
    private String email;
    private String password;
    private Integer insertUid;
    private String insertTime;
    private String updateTime;
    private boolean isDel;
    private boolean isJob;
    private List<UserSiteRoleKey> userSiteRoles;
    private Integer version;
    private boolean isZx;
    private Integer sdId;
}
