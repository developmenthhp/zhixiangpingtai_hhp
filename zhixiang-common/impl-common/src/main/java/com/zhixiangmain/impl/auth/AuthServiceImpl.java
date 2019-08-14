package com.zhixiangmain.impl.auth;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixiangmain.config.TargetDataSource;
import com.zhixiangmain.dao.permission.PermissionMapper;
import com.zhixiangmain.dao.role.RoleMapper;
import com.zhixiangmain.dao.rolePermission.RolePermissionMapper;
import com.zhixiangmain.module.permission.Permission;
import com.zhixiangmain.module.permission.vo.PermissionVO;
import com.zhixiangmain.module.role.Role;
import com.zhixiangmain.module.role.dto.RoleDTO;
import com.zhixiangmain.module.role.vo.RoleVO;
import com.zhixiangmain.module.rolePermission.RolePermissionKey;
import com.zhixiangmain.service.auth.AuthService;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.impl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 13:22
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = AuthService.class)
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory
            .getLogger(AuthServiceImpl.class);
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public int addPermission(Permission permission) {
        return this.permissionMapper.insert(permission);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<Permission> permList() {
        return this.permissionMapper.findAll();
    }

    @Override
    @TargetDataSource(name = "default")
    public int updatePerm(Permission permission) {
        return this.permissionMapper.updateByPrimaryKeySelective(permission);
    }

    @Override
    @TargetDataSource(name = "default")
    public Permission getPermission(int id) {
        return this.permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public String delPermission(int id) {
        //查看该权限是否有子节点，如果有，先删除子节点
        List<Permission> childPerm = this.permissionMapper.findChildPerm(id);
        if(null != childPerm && childPerm.size()>0){
            return "删除失败，请您先删除该权限的子节点";
        }
        if(this.permissionMapper.deleteByPrimaryKey(id)>0){
            return "ok";
        }else{
            return "删除失败，请您稍后再试";
        }
    }

    @Override
    @TargetDataSource(name = "default")
    public ResultBean roleList(RoleDTO roleDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            PageHelper.startPage(page, limit);
            List<Map<String,Object>> roleList = this.roleMapper.findList(roleDTO);
            // 获取分页查询后的数据
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(roleList);
            // 设置获取到的总记录数total：
            resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
            resultBean.setRows(roleList);
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            logger.debug("角色列表查询=roleList:" + roleList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("角色查询异常！", e);
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    @Override
    @TargetDataSource(name = "default")
    public List<PermissionVO> findPerms(Integer flag, Integer userId) {
        List<PermissionVO> permissionVOS = null;
        if(flag==1){
            permissionVOS = this.permissionMapper.getUserPerms(userId);
        }else if(flag==0){
            permissionVOS = this.permissionMapper.findPerms();
        }
        return permissionVOS;
    }

    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String addRole(Role role, String permIds) {
        this.roleMapper.insert(role);
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        setRolePerms(roleId, arrays);
        return "ok";
    }

    @Override
    @TargetDataSource(name = "default")
    public RoleVO findRoleAndPerms(Integer id) {
        return this.roleMapper.findRoleAndPerms(id);
    }

    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String updateRole(Role role, String permIds) {
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        //1，更新角色表数据；
        int num=this.roleMapper.updateByPrimaryKeySelective(role);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        //2，删除原角色权限；
        batchDelRolePerms(roleId);
        //3，添加新的角色权限数据；
        setRolePerms(roleId, arrays);
        return "更新角色成功";
    }



    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String delRole(int id) {
        //1.删除角色对应的权限
        batchDelRolePerms(id);
        //2.删除角色
        int num=this.roleMapper.deleteByPrimaryKey(id);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        return "ok";
    }

    @Override
    @TargetDataSource(name = "default")
    public List<Role> getRoles(Integer sdId) {
        return this.roleMapper.getRoles(sdId);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<Role> getRoleByUser(Integer userId) {
        return this.roleMapper.getRoleByUserId(userId);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<Permission> findPermsByRoleId(Integer id) {
        return this.permissionMapper.findPermsByRole(id);
    }

    @Override
    @TargetDataSource(name = "default")
    public List<PermissionVO> getUserPerms(Integer id) {
        return this.permissionMapper.getUserPerms(id);
    }

    /**
     * 批量删除角色权限中间表数据
     * @param roleId
     */
    @TargetDataSource(name = "default")
    private void batchDelRolePerms(int roleId) {
        List<RolePermissionKey> rpks=this.rolePermissionMapper.findByRole(roleId);
        if(null!=rpks && rpks.size()>0){
            for (RolePermissionKey rpk : rpks) {
                this.rolePermissionMapper.deleteByPrimaryKey(rpk);
            }
        }
    }

    /**
     * 给当前角色设置权限
     * @param roleId
     * @param arrays
     */
    @TargetDataSource(name = "default")
    private void setRolePerms(int roleId, String[] arrays) {
        for (String permid : arrays) {
            RolePermissionKey rpk=new RolePermissionKey();
            rpk.setRoleId(roleId);
            rpk.setPermitId(Integer.valueOf(permid));
            this.rolePermissionMapper.insert(rpk);
        }
    }

}
