package com.zhixiangmain.module.role;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.api.module.role
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-29 13:06
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="role")
public class Role implements Serializable {

    private Integer id;

    private String roleName;

    private String descpt;

    private String code;

    private Integer insertUid;

    private Date insertTime;

    private Date updateTime;

    private Integer sdid;

}
