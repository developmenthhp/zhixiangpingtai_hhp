package com.zhixiangmain.dao.roleSiteRole;

import com.zhixiangmain.module.siteRolePermission.SiteRolePermissionKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.roleSiteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 11:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RoleSiteRoleMapper {
    int insert(SiteRolePermissionKey rpk);

    List<SiteRolePermissionKey> findByRole(@Param("siteRoleId") int siteRoleId);

    int deleteByPrimaryKey(SiteRolePermissionKey rpk);
}
