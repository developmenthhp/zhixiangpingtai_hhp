package com.zhixiangmain.service.auth;

import com.zhixiangmain.module.permission.Permission;
import com.zhixiangmain.module.permission.vo.PermissionVO;
import com.zhixiangmain.module.role.Role;
import com.zhixiangmain.module.role.dto.RoleDTO;
import com.zhixiangmain.module.role.vo.RoleVO;
import com.zhixiangmain.web.responseConfig.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.service.auth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 13:05
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface AuthService {

    int addPermission(Permission permission);

    List<Permission> permList();

    int updatePerm(Permission permission);

    Permission getPermission(int id);

    String delPermission(int id);

    /**
     * 查询所有角色
     * @return
     */
    ResultBean roleList(RoleDTO roleDTO, Integer page, Integer limit);

    /**
     * 关联查询权限树列表
     * @return
     */
    List<PermissionVO> findPerms(Integer flag, Integer userId);

    /**
     * 添加角色
     * @param role
     * @param permIds
     * @return
     */
    String addRole(Role role, String permIds);

    RoleVO findRoleAndPerms(Integer id);

    /**
     * 更新角色并授权
     * @param role
     * @param permIds
     * @return
     */
    String updateRole(Role role, String permIds);

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    String delRole(int id);

    /**
     * 查找所有角色
     * @return
     */
    List<Role> getRoles(Integer sdId);

    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleByUser(Integer userId);

    /**
     * 根据角色id获取权限数据
     * @param id
     * @return
     */
    List<Permission> findPermsByRoleId(Integer id);

    /**
     * 根据用户id获取权限数据
     * @param id
     * @return
     */
    List<PermissionVO> getUserPerms(Integer id);

}
