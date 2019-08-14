package com.zhixiangmain.module.userRole.vo;

import com.zhixiangmain.module.userRole.UserRoleKey;
import com.zhixiangmain.module.userSiteRole.UserSiteRoleKey;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.userRole.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 14:59
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserRolesVO implements Serializable {

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

    private List<UserRoleKey> userRoles;

    private List<UserSiteRoleKey> userSiteRoleKeys;

    private Integer version;

    private boolean isZx;

    private Integer sdid;

}
