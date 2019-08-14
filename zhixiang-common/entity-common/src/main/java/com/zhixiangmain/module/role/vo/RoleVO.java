package com.zhixiangmain.module.role.vo;
import com.zhixiangmain.module.rolePermission.RolePermissionKey;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.role.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 14:56
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class RoleVO implements Serializable {

    private Integer id;

    private String roleName;

    private String descpt;

    private String code;

    private Integer insertUid;

    private String insertTime;
    private Integer sdId;
    //角色下的权限ids
    private List<RolePermissionKey> rolePerms;

}
