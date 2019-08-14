package com.zhixiangmain.module.base;

import lombok.Data;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-taotao
 * @Package cn.zhixiangyun.api.model.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-09-26 16:48
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键ID **/
    private Integer id;

    /** 版本号 **/
    //private Integer version = 0;

    /** 创建时间 **/
    //private Date createTime;
}
