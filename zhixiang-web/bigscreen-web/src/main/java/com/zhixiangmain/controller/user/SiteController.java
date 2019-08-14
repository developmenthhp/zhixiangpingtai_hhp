package com.zhixiangmain.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.module.site.Site;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.module.siteRole.SiteRole;
import com.zhixiangmain.module.siteRole.dto.SiteRoleDTO;
import com.zhixiangmain.module.siteRole.vo.SiteRoleVO;
import com.zhixiangmain.module.siteRolePermission.SiteRolePermissionKey;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.service.siteService.SiteService;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.overallParam.OpslabConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.web.user
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-12-20 15:42
 * @version：V1.0
 */
@Controller
@RequestMapping("/site")
public class SiteController {
	private static final Logger logger = LoggerFactory
			.getLogger(SiteController.class);
	@Reference(version = "1.0.0")
    private SiteService siteService;

	/**
	 * 平台map显示所有站点
	 * @return ok/fail
	 */
	@RequestMapping(value = "/getMapPage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getMapPage() {
		logger.debug("站点列表！");
		ModelAndView mav = new ModelAndView("/jcxxpt/zdjcxx/zdBase");
		try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
			mav.addObject("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("站点查询异常！", e);
		}
		return mav;
	}

	/**
	 * 添加权限【test】
	 * @param site
	 * @return ok/fail
	 */
	@RequestMapping(value = "/addSite", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView toPage(Site site) {
		logger.debug("新增站点--site-" + site);
		ModelAndView mav = new ModelAndView("/home");
		try {
			if (null != site) {
				site.setInsertTime(new Date());

				siteService.addSite(site);
				logger.debug("新增站点成功！-site-" + site);
				mav.addObject("msg", "ok");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "fail");
			logger.error("新增站点异常！", e);
		}
		return mav;
	}

	/**
	 * 站点列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/siteList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView permList() {
		logger.debug("站点列表！");
		ModelAndView mav = new ModelAndView("/auth/siteList");
		try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
			mav.addObject("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("站点查询异常！", e);
		}
		return mav;
	}

	/**
	 * 权限列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/siteListBfe", method = RequestMethod.GET)
	public ModelAndView permListCge() {
		return new ModelAndView("/auth/permList");
	}

	/**
	 * 权限列表
	 * @return ok/fail
	 */
	@PostMapping(value = "/getSiteList")
	@ResponseBody
	public ResultBean getPermList() {
		logger.debug("站点列表！");
		ResultBean rb = new ResultBean();
		try {
			List<Site> siteList = siteService.siteList();
			logger.debug("站点列表查询=siteList:" + siteList);
			rb.setFlag(true);
			rb.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			rb.setData(siteList);
			rb.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("站点查询异常！", e);
			rb.setFlag(false);
			rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
		}
		return rb;
	}

	/**
	 * 添加权限
	 * @param type [0：编辑；1：新增子节点权限]
	 * @param site
	 * @return ModelAndView ok/fail
	 */
	@RequestMapping(value = "/setSite", method = RequestMethod.POST)
	@ResponseBody
    public ResultBean setPerm(
            @RequestParam("type") int type, Site site) {
		logger.debug("设置权限--区分type-" + type + "【0：编辑；1：新增子节点站点】，权限--site-"
				+ site);
        ResultBean rb = new ResultBean();
		try {
			if (null != site) {
				Date date = new Date();
				if (0 == type) {
					site.setUpdateTime(date);
					rb.setMsg("更新站点成功");
					//编辑权限
					this.siteService.updateSite(site);
				} else if (1 == type) {
					site.setInsertTime(date);
					//增加子节点权限
                    rb.setMsg("新增成功");
					this.siteService.addSite(site);
				}
				logger.debug("设置站点成功！-site-" + site);
                rb.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设置站点异常！", e);
			rb.setMsg("设置站点出错，请您稍后再试");
			rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
		}
		return rb;
	}

	/**
	 * 获取权限
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/getSite")
	@ResponseBody
	public Site getPerm(
			@RequestParam("id") int id) {
		logger.debug("获取站点--id-" + id);
		try {
			if (id > 0) {
				Site site = this.siteService.getSite(id);
				logger.debug("获取站点成功！-site-" + site);
				return site;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取站点异常！", e);
		}
		return null;
	}

	/**
	 * 删除权限
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/del")
	@ResponseBody
	public String del(
			@RequestParam("id") int id) {
		logger.debug("删除站点--id-" + id);
		try {
			if (id > 0) {
				return this.siteService.delSite(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除站点异常！", e);
		}
		return "删除站点出错，请您稍后再试";
	}

	/**
	 * 跳转到角色列表
	 * @return
	 */
	@RequestMapping("/siteRoleManage")
	public ModelAndView toPage() {
		return new ModelAndView("/auth/siteRoleManage");
	}

	/**
	 * 角色列表
	 * @return ok/fail
	 */
	@PostMapping(value = "/getSiteRoleList")
	@ResponseBody
	public ResultBean getRoleList(@RequestParam("page") Integer page,
								  @RequestParam("limit") Integer limit, SiteRoleDTO siteRoleDTO) {
		logger.debug("站点角色列表！");
		User existUser= (User) SecurityUtils.getSubject().getPrincipal();
		if(existUser.getIsZx()==true||existUser.getSdid()!=22){
			//所有
			siteRoleDTO.setSdId(existUser.getSdid());
		}
		return siteService.siteRoleList(siteRoleDTO,page,limit);
	}

	/**
	 * 查询权限树数据
	 * @return PermTreeDTO
	 */
	@PostMapping(value = "/findSites")
	@ResponseBody
	public List<SiteVO> findPerms() {
		logger.debug("站点权限树列表！");
		List<SiteVO> svo = null;
		try {

			User existUser= (User) SecurityUtils.getSubject().getPrincipal();
			Integer flag = 1;
			if(existUser.getIsZx()==false&&existUser.getSdid()==22){
				flag = 0;
			}/*else{
				SiteVO siteVo = siteService.findSiteBySdId(existUser.getSdid());
				svo = siteService.findSitesByPId(siteVo.getId());
				svo.add(siteVo);
			}*/
            //如果不是智飨查询该角色下的所有站点
            svo = siteService.findSites(flag,existUser.getId());

			//生成页面需要的json格式
			logger.debug("站点权限树列表查询=svo:" + svo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("站点权限树列表查询异常！", e);
		}
		return svo;
	}

	/**
	 * 添加角色并授权
	 * @return PermTreeDTO
	 */
	@PostMapping(value = "/addSiteRole")
	@ResponseBody
	public ResultBean addSiteRole(@RequestParam("roleSiteRole") String permIds, SiteRole siteRole) {
		logger.debug("添加站点角色并授权！站点角色数据siteRole："+siteRole+"，站点权限数据permIds："+permIds);
		ResultBean rb = new ResultBean();
		try {
			if(StringUtils.isEmpty(permIds)){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("未授权，请您给该站点角色授权");
			}
			if(null == siteRole){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("请您填写完整的站点角色数据");
			}else if(IsEmptyUtils.isEmpty(siteRole.getRoleName())||IsEmptyUtils.isEmpty(siteRole.getCode())){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("请您填写完整的站点角色数据");
			}

			siteRole.setInsertTime(new Date());

			User existUser= (User) SecurityUtils.getSubject().getPrincipal();
			if(!IsEmptyUtils.isEmpty(siteRole.getSdId())){
				if(existUser.getIsZx()==true||existUser.getSdid()!=22){
					//不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
					siteRole.setSdId(existUser.getSdid());
				}
			}else{
				siteRole.setSdId(existUser.getSdid());
			}

			String msg = siteService.addSiteRole(siteRole,permIds);

			if(IsEmptyUtils.isEmpty(msg)||!msg.equals("ok")){
				msg = "新增站点角色并授权权限失败";
				rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			}else{
				msg = "新增站点角色并授权权限成功";
				rb.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			}
			rb.setMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加站点角色并授权！异常！", e);
			rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			rb.setMsg("未授权，请您给该站点角色授权");
		}
		return rb;
	}
	/**
	 * 根据id查询角色
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/updateRole/{id}", method = RequestMethod.GET)
	//@ResponseBody
	public ModelAndView updateRole(@PathVariable("id") Integer id) {
		logger.debug("根据id查询角色id："+id);
		ModelAndView mv=new ModelAndView("/auth/roleManage");
		try {
			if(null==id){
				mv.addObject("msg","请求参数有误，请您稍后再试");
				return mv;
			}
			mv.addObject("flag","updateRole");
			SiteRoleVO srvo=this.siteService.findSiteRoleAndSites(id);
			//角色下的权限
			List<SiteRolePermissionKey> rpks=srvo.getSiteRolePerms();
			//获取全部权限数据
			User existUser= (User) SecurityUtils.getSubject().getPrincipal();
			Integer flag = 1;
			if(existUser.getIsZx()==false&&existUser.getSdid()==22){
				flag = 0;
			}
			List<SiteVO> pvos = siteService.findSites(flag,existUser.getId());
			for (SiteRolePermissionKey srpk : rpks) {
				//设置角色下的权限checked状态为：true
				for (SiteVO pvo : pvos) {
					if(String.valueOf(srpk.getSiteId()).equals(String.valueOf(pvo.getId()))){
						pvo.setChecked(true);
					}
				}
			}
			mv.addObject("sites", pvos.toArray());
			//角色详情
			mv.addObject("roleDetail",srvo);
			logger.debug("根据id查询角色数据："+mv);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加角色并授权！异常！", e);
			mv.addObject("msg","请求异常，请您稍后再试");
		}
		return mv;
	}

	/**
	 * 根据id查询角色
	 * @return PermTreeDTO
	 */
	@PostMapping(value = "/getUpdSiteRoles")
	@ResponseBody
	public ResultBean getUpdRoles(@RequestParam("id") Integer id) {
		logger.debug("根据id查询角色id："+id);
		ResultBean rb = new ResultBean();
		try {
			if(null==id){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("请求参数有误，请您稍后再试");
			}

			SiteRoleVO rvo=this.siteService.findSiteRoleAndSites(id);
			//角色下的权限
			/*List<RolePermissionKey> rpks=rvo.getRolePerms();
			//获取全部权限数据
			List<PermissionVO> pvos = authService.findPerms();
			for (RolePermissionKey rpk : rpks) {
				//设置角色下的权限checked状态为：true
				for (PermissionVO pvo : pvos) {
					if(String.valueOf(rpk.getPermitId()).equals(String.valueOf(pvo.getId()))){
						pvo.setChecked(true);
					}
				}
			}*/

			rb.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			//rb.setObj(pvos.toArray());
			//角色详情
			rb.setData(rvo);

			logger.debug("根据id查询角色数据："+rb);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加角色并授权！异常！", e);
			rb.setMsg("请求异常，请您稍后再试");
			rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
		}
		return rb;
	}


	/**
	 * 更新角色并授权
	 * @return PermTreeDTO
	 */
	@PostMapping(value = "/setSiteRole")
	@ResponseBody
	public ResultBean setRole(@RequestParam("roleSiteRole") String roleSiteRole, SiteRole siteRole) {
		logger.debug("更新站点角色并授权！站点角色数据siteSole："+siteRole+"，站点数据roleSiteRole："+roleSiteRole);
		ResultBean rb = new ResultBean();
		try {
			if(StringUtils.isEmpty(roleSiteRole)){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("未授权，请您给该角色授权");
			}
			if(null == siteRole){
				rb.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
				rb.setMsg("请您填写完整的角色数据");
			}
			siteRole.setUpdateTime(new Date());

			User existUser= (User) SecurityUtils.getSubject().getPrincipal();
			if(!IsEmptyUtils.isEmpty(siteRole.getSdId())){
				if(existUser.getIsZx()==true||existUser.getSdid()!=22){
					//不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
					siteRole.setSdId(existUser.getSdid());
				}
			}else{
				siteRole.setSdId(existUser.getSdid());
			}

			String msg = siteService.updateSiteRole(siteRole,roleSiteRole);
			if(IsEmptyUtils.isEmpty(msg)||msg.equals("操作失败")){
				rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			}else{
				rb.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			}
			rb.setMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新角色并授权！异常！", e);
			rb.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			rb.setMsg("更新角色并授权！异常！");
		}
		return rb;
	}

	/**
	 * 删除角色以及它对应的权限
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/delRole")
	@ResponseBody
	public String delRole(
			@RequestParam("id") int id) {
		logger.debug("删除角色以及它对应的权限--id-" + id);
		try {
			if (id > 0) {
				return this.siteService.delSiteRole(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除角色异常！", e);
		}
		return "删除角色出错，请您稍后再试";
	}

	/**
	 * 查找所有角色
	 * @return
	 */
	@PostMapping(value = "/getSiteRoles")
	@ResponseBody
	public List<SiteRole> getRoles() {
		logger.debug("查找所有角色!");
		List<SiteRole> siteRoles=null;
		try {
            Integer sdId = null;
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(existUser.getIsZx()==true||existUser.getSdid()!=22){
                //不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
                sdId = existUser.getSdid();
            }
			return this.siteService.getSiteRoles(sdId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查找所有角色异常！", e);
		}
		return siteRoles;
	}

	/**
	 * 根据用户id查询站点权限树数据
	 * @return PermTreeDTO
	 */
	@PostMapping(value = "/getUserSites")
	@ResponseBody
	public ResultBean getUserPerms() {
		logger.debug("根据用户id查询站点！");
		ResultBean resultBean = new ResultBean();
		User existUser= (User) SecurityUtils.getSubject().getPrincipal();
		if(null==existUser){
			logger.debug("根据用户id查询站点 用户未登录");
			return resultBean;
		}
		try {
			List<SiteVO> pvo = siteService.getUserSites(existUser.getId());
			resultBean.setRows(pvo);
			resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
			//生成页面需要的json格式
			logger.debug("根据用户id查询站点查询=pvo:" + pvo);
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			resultBean.setMsg("根据用户id查询站点查询异常！");
			logger.error("根据用户id查询站点查询异常！", e);
		}
		return resultBean;
	}

	/**
	 *  得到该站点的站点图标
	 * @param sdId 试点
	 * @return
	 */
	@PostMapping(value = "getPhotoBySdId")
	@ResponseBody
	public ResultBean getPhotoBySdId(@RequestParam(value = "sdId") String sdId) {
		logger.debug("得到该站点的站点图标！sdId:"+sdId);
		ResultBean resultBean = new ResultBean();
		try {
			resultBean = siteService.getPhotoBySdId(sdId);
		} catch (Exception e) {
			resultBean.setFlag(false);
			e.printStackTrace();
			resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			System.out.println(e.getMessage()+"-->error Message");
			logger.error("获取站点图标异常！", e);
		}
		resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
		return resultBean;
	}

}
