package com.zhixiangmain.module.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.service
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-21 21:52
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */

@Data
@ToString
@Table(name="user")
public class User extends BaseEntity {

    @NotBlank(message = "用户名不能为空")
    @Length(min=5, max=20, message="用户名长度必须在5-20之间")
    @Pattern(regexp = "^[a-zA-Z_]\\w{4,19}$", message = "用户名必须以字母下划线开头，可由字母数字下划线组成")
    private String username;/* 用户名 */

    @Length(max=40, message="昵称长度必须小或等于40位")
    private String nickName;/* 昵称 */

    @Length(max=40, message="签名长度必须小或等于80位")
    private String signature;/* 个性签名 */

    @NotBlank(message = "手机号不能为空")
    @Length(min=11, max=11, message="请输入11位的手机号")
    private String mobile;/* 手机号 */

    private String email;/* 邮箱 */

    @NotBlank(message = "密码不能为空")
    @Length(min=6, max=40, message="密码长度必须在6-40之间")
    private String password;/* 密码 */

    private String headImg; /* 头像 */

    @NotBlank(message = "请选择性别")
    @Length(min=1, max=1, message="请输入合法数据")
    private Integer sex;/* 性别 1男 2女  默认1 */

    // 创建时间
    /*@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date insertTime;

    private String token;/* 登录标识 */

    private Integer version;

    private Integer insertUid;

    private Date updateTime;

    private Boolean isDel;

    private Boolean isJob;

    private String mcode;

    private Date sendTime;

    private Boolean isZx;

    private Integer sdid;
}
