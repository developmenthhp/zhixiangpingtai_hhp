package com.zhixiangmain.service.user;

import com.zhixiangmain.module.user.User;
import com.zhixiangmain.module.user.dto.UserDTO;
import com.zhixiangmain.module.userRole.dto.UserSearchDTO;
import com.zhixiangmain.module.userRole.vo.UserRolesVO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.service.user
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-21 21:55
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserService {

    /**
     * 分页查询用户列表
     * @param page
     * @param limit
     * @return
     */
    ResultBean getUsers(UserSearchDTO userSearch, Integer page, Integer limit);

    /**
     *	设置用户【新增或更新】
     * @param user
     * @param roleIds
     * @return
     */
    String setUser(User user, String roleIds, User adminUser, String siteRoleIds);

    /**
     * 设置用户是否离职
     * @param id
     * @param isJob
     * @param insertUid
     * @return
     */
    String setJobUser(Integer id, Integer isJob, Integer insertUid,
                      Integer version);

    /**
     * 删除用户
     * @param id
     * @param isDel
     * @return
     */
    String setDelUser(Integer id, Integer isDel, Integer insertUid,
                      Integer version);

    /**
     * 查询用户数据
     * @param id
     * @return
     */
    UserRolesVO getUserAndRoles(Integer id);

    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    String sendMsg(UserDTO user);

    /**
     * 根据手机号查询用户数据
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);

    /**
     * 根据手机号发送短信验证码
     * @param mobile
     * @return
     */
    String sendMessage(int userId, String mobile);

    /**
     * 修改用户手机号
     * @param id
     * @param password
     * @return
     */
    int updatePwd(Integer id, String password);

    /**
     * 锁定用户
     * @param id
     * @param isLock  0:解锁；1：锁定
     * @return
     */
    int setUserLockNum(Integer id, int isLock);
}
