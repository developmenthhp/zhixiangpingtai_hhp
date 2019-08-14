package com.zhixiangmain.dao.siteRole;

import com.zhixiangmain.module.siteRole.SiteRole;
import com.zhixiangmain.module.siteRole.dto.SiteRoleDTO;
import com.zhixiangmain.module.siteRole.vo.SiteRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.siteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 11:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SiteRoleMapper {
    List<SiteRole> findSiteRoleList(SiteRoleDTO siteRoleDTO);

    int insert(SiteRole siteRole);

    int updateByPrimaryKeySelective(SiteRole siteRole);

    SiteRoleVO findSiteRoleAndSites(@Param("id") Integer id);

    List<SiteRole> findSiteRoles(@Param("sdId") Integer sdId);

    int deleteByPrimaryKey(int id);
}
