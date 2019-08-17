package com.zhixiangmain.impl.site;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.config.TargetDataSource;
import com.zhixiangmain.dao.roleSiteRole.RoleSiteRoleMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.dao.siteRole.SiteRoleMapper;
import com.zhixiangmain.date.DateUtils;
import com.zhixiangmain.module.site.Site;
import com.zhixiangmain.module.site.dto.SiteDTO;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.module.siteRole.SiteRole;
import com.zhixiangmain.module.siteRole.dto.SiteRoleDTO;
import com.zhixiangmain.module.siteRole.vo.SiteRoleVO;
import com.zhixiangmain.module.siteRolePermission.SiteRolePermissionKey;
import com.zhixiangmain.service.siteService.SiteService;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 11:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SiteService.class)
public class SiteServiceImpl implements SiteService {

    private static final Logger logger = LoggerFactory
            .getLogger(SiteServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private SiteRoleMapper siteRoleMapper;
    @Autowired
    private RoleSiteRoleMapper roleSiteRoleMapper;

    @Override
    @TargetDataSource(name = "default")
    public ResultBean siteRoleList(SiteRoleDTO siteRoleDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            PageHelper.startPage(page, limit);
            List<SiteRole> roleList = this.siteRoleMapper.findSiteRoleList(siteRoleDTO);
            // 获取分页查询后的数据
            PageInfo<SiteRole> pageInfo = new PageInfo<>(roleList);
            resultBean.setRows(roleList);
            // 设置获取到的总记录数total：
            resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            logger.debug("站点角色列表查询=roleList:"+roleList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点角色查询异常！", e);
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    @Override
    @TargetDataSource(name = "default")
    public int addSite(Site site) {
        return this.siteMapper.insert(site);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<Site> siteList() {
        return this.siteMapper.findAll();
    }

    @Override
    @TargetDataSource(name = "default")
    public Site getSite(int id) {
        return this.siteMapper.selectByPrimaryKey(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public String delSite(int id) {
        //查看该权限是否有子节点，如果有，先删除子节点
        List<Site> childPerm = this.siteMapper.findChildSite(id);
        if(null != childPerm && childPerm.size()>0){
            return "删除失败，请您先删除该站点的子站点";
        }
        if(this.siteMapper.deleteByPrimaryKey(id)>0){
            return "ok";
        }else{
            return "删除失败，请您稍后再试";
        }
    }

    @Override
    @TargetDataSource(name = "default")
    public SiteRoleVO findSiteRoleAndSites(Integer id) {
        return this.siteRoleMapper.findSiteRoleAndSites(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<SiteVO> findSites(Integer flag, Integer userId) {
        List<SiteVO> siteVOS = null;
        if(flag==0){
            siteVOS = this.siteMapper.findSites();
        }else if(flag==1){
            siteVOS = this.siteMapper.findUserSites(userId);
        }
        return siteVOS;
    }

    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String addSiteRole(SiteRole siteRole, String permIds) {
        this.siteRoleMapper.insert(siteRole);
        int roleId=siteRole.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        setRoleSiteRoles(roleId, arrays);
        return "ok";
    }

    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String updateSiteRole(SiteRole siteRole, String permIds) {
        int roleId=siteRole.getId();
        String[] arrays=permIds.split(",");
        logger.debug("站点id =arrays="+arrays.toString());
        //1，更新角色表数据；
        int num=this.siteRoleMapper.updateByPrimaryKeySelective(siteRole);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        //2，删除原角色权限；
        batchDelRolePerms(roleId);
        //3，添加新的角色权限数据；
        setRolePerms(roleId, arrays);
        return "更新站点角色成功";
    }

    /**
     * 给当前角色设置权限
     * @param roleId
     * @param arrays
     */
    @TargetDataSource(name = "default")
    private void setRolePerms(int roleId, String[] arrays) {
        for (String permid : arrays) {
            SiteRolePermissionKey srpk=new SiteRolePermissionKey();
            srpk.setSiteRoleId(roleId);
            srpk.setSiteId(Integer.valueOf(permid));
            this.roleSiteRoleMapper.insert(srpk);
        }
    }

    /**
     * 批量删除角色权限中间表数据
     * @param roleId
     */
    @TargetDataSource(name = "default")
    private void batchDelRolePerms(int roleId) {
        List<SiteRolePermissionKey> rpks=this.roleSiteRoleMapper.findByRole(roleId);
        if(null!=rpks && rpks.size()>0){
            for (SiteRolePermissionKey rpk : rpks) {
                this.roleSiteRoleMapper.deleteByPrimaryKey(rpk);
            }
        }
    }
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String delSiteRole(int id) {
        //1.删除角色对应的权限
        batchDelRolePerms(id);
        //2.删除角色
        int num=this.siteRoleMapper.deleteByPrimaryKey(id);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        return "ok";
    }

    @Override
    public List<SiteRole> getSiteRoles(Integer sdId) {
        return this.siteRoleMapper.findSiteRoles(sdId);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<SiteVO> getUserSites(Integer id) {
        return this.siteMapper.findUserSites(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public int updateSite(Site site) {
        return this.siteMapper.updateByPrimaryKeySelective(site);
    }

    @Override
    @TargetDataSource(name = "default")
    public SiteVO findSiteBySdId(Integer sdId) {
        return this.siteMapper.findSiteBySdId(sdId);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<SiteVO> findSitesByPId(Integer id) {
        return this.siteMapper.findSitesByPId(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public ResultBean getPhotoBySdId(String sdId) {
        ResultBean resultBean = new ResultBean();
        String photo = this.siteMapper.findPhotoBySdId(sdId);
        resultBean.setData(photo);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description: 查询系统是否到期
     * @author: hhp
     * @param:  * @param sdId 试点
     * @date: 2019/8/13 19:13
     */
    @Override
    public ResultBean isSiteOverTime(String sdId){
        ResultBean resultBean = new ResultBean();
        try {
            Map<String,Object> timesMap = this.siteMapper.findOverTimeSiteId(sdId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = new Date();
            if(!IsEmptyUtils.isEmpty(timesMap)){
                if(!IsEmptyUtils.isEmpty(timesMap.get("overTime"))){
                    resultBean.setObj(timesMap.get("overTime"));
                    int bigCompare = currentDate.compareTo(sdf.parse(timesMap.get("overTime").toString()));
                    switch (bigCompare){
                        case -1:
                            //比较临期时间
                            if(!IsEmptyUtils.isEmpty(timesMap.get("advanceTime"))){
                                int advanceCompare = currentDate.compareTo(sdf.parse(timesMap.get("advanceTime").toString()));
                                switch (advanceCompare){
                                    case -1:
                                        resultBean.setCode(200);
                                        resultBean.setData(0);
                                        break;
                                    case 0:
                                        //两个的逻辑一样，故直接放行执行接下来的代码
                                        resultBean.setCode(200);
                                        resultBean.setData(1);
                                        break;
                                    case 1:
                                        System.out.println(timesMap.get("overTime")+",22222222222222222,,"+timesMap.get("advanceTime"));
                                        resultBean.setCode(200);
                                        resultBean.setData(1);
                                        break;
                                }
                            }
                            break;
                        case 0:
                            //两个的逻辑一样，故直接放行执行接下来的代码
                            resultBean.setCode(200);
                            resultBean.setData(2);
                            break;
                        case 1:
                            resultBean.setCode(200);
                            resultBean.setData(2);
                            break;
                    }

                }else{
                    if(!IsEmptyUtils.isEmpty(timesMap.get("advanceTime"))){
                        int advanceCompare = currentDate.compareTo(sdf.parse(timesMap.get("advanceTime").toString()));
                        switch (advanceCompare){
                            case -1:
                                resultBean.setCode(200);
                                resultBean.setData(0);
                                break;
                            case 0:
                                //两个的逻辑一样，故直接放行执行接下来的代码
                                resultBean.setCode(200);
                                resultBean.setData(1);
                                break;
                            case 1:
                                resultBean.setCode(200);
                                resultBean.setData(1);
                                break;
                        }
                    }else{
                        resultBean.setCode(IStatusMessage.LogicStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getCode());
                        resultBean.setMsg("No pilot expiration date has been set Please remind administrators to set the time");
                    }
                }
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getCode());
                resultBean.setMsg("No pilot expiration date has been set Please remind administrators to set the time");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg("No pilot expiration date has been set Please remind administrators to set the time");
        }
        return resultBean;
    }

    @Override
    public ResultBean getPageSiteList(SiteDTO siteDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        siteDTO.setPage(page);
        siteDTO.setLimit(limit);
        // 时间处理
        /*if (null != site) {

            if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
                if (userSearch.getInsertTimeEnd().compareTo(
                        userSearch.getInsertTimeStart()) < 0) {
                    String temp = userSearch.getInsertTimeStart();
                    userSearch
                            .setInsertTimeStart(userSearch.getInsertTimeEnd());
                    userSearch.setInsertTimeEnd(temp);
                }
            }
        }*/

        PageHelper.startPage(page, limit);
        List<Site> siteList = siteMapper.findPageSiteList(siteDTO);
        // 获取分页查询后的数据
        PageInfo<Site> pageInfo = new PageInfo<>(siteList);
        // 设置获取到的总记录数total：
        resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        resultBean.setData(siteList);
        return resultBean;
    }

    /**
     * 给当前角色设置权限
     * @param roleId
     * @param arrays
     */
    @TargetDataSource(name = "default")
    private void setRoleSiteRoles(int roleId, String[] arrays) {
        for (String permid : arrays) {
            SiteRolePermissionKey srpk=new SiteRolePermissionKey();
            srpk.setSiteRoleId(roleId);
            srpk.setSiteId(Integer.valueOf(permid));
            this.roleSiteRoleMapper.insert(srpk);
        }
    }
}
