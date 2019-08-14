package com.zhixiangmain.dao.rolePermission;

import com.zhixiangmain.module.rolePermission.RolePermissionKey;

import java.util.List;

public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermissionKey key);

    int insert(RolePermissionKey record);

    int insertSelective(RolePermissionKey record);

	List<RolePermissionKey> findByRole(int roleId);
}