package com.zhixiangmain.service.siteService;

import com.zhixiangmain.module.site.Site;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.module.siteRole.SiteRole;
import com.zhixiangmain.module.siteRole.dto.SiteRoleDTO;
import com.zhixiangmain.module.siteRole.vo.SiteRoleVO;
import com.zhixiangmain.web.responseConfig.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 11:14
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SiteService {
    ResultBean siteRoleList(SiteRoleDTO siteRoleDTO, Integer page, Integer limit);
    int addSite(Site site);
    List<Site> siteList();

    Site getSite(int id);

    String delSite(int id);

    SiteRoleVO findSiteRoleAndSites(Integer id);

    List<SiteVO> findSites(Integer flag, Integer userId);

    String addSiteRole(SiteRole siteRole, String permIds);

    String updateSiteRole(SiteRole siteRole, String permIds);

    String delSiteRole(int id);

    List<SiteRole> getSiteRoles(Integer sdId);

    List<SiteVO> getUserSites(Integer id);

    int updateSite(Site site);

    SiteVO findSiteBySdId(Integer sdid);

    List<SiteVO> findSitesByPId(Integer id);

    ResultBean getPhotoBySdId(String sdId);

    ResultBean isSiteOverTime(String sdId);
}
