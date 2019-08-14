package com.zhixiangmain.module.user.dto;

import lombok.Data;
import lombok.ToString;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.user.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 15:01
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserDTO implements Serializable {

    private Integer id;
    @NotNull(message = "用户名不能为空，请您先填写用户名")
    private String username;
    @NotNull(message = "手机号不能为空，请您先填写手机号")
    private String mobile;

    private String email;
    @NotNull(message = "密码不能为空，请您先填写手机号")
    @MatchPattern(pattern = "^[0-9_a-zA-Z]{6,20}$", message = "用户名或密码有误，请您重新填写")
    private String password;
    /*@NotNull(message = "图片验证码不能为空，请您先填写验证码")
    @MatchPattern(pattern = "\\w{4}$", message = "图片验证码有误，请您重新填写")
    private String code;*/

    @ValidateWithMethod(methodName = "isValidateSmsCode", message = "验证码有误，请您重新填写", parameterType = String.class)
    private String smsCode;

    private static final Pattern CODE = Pattern.compile("[0-9]{6}$");

    /**
     *	是否为6位短信验证码
     * @param smsCode
     * @return
     */
    private boolean isValidateSmsCode(String smsCode){
        if(StringUtils.isNotBlank(smsCode)){
            if (!CODE.matcher(smsCode).matches()) {
                return false;
            }
        }
        return true;
    }

}
