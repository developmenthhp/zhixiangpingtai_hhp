package com.zhixiangmain.api.module.alert;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.alert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 17:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class Alert implements Comparable<Alert>,Serializable {

    private String type;//类型  1橙色  2黄色
    private String context;// 内容
    private Integer id;// 编号
    private String time;// 时间
    private String imgUrl; //图片地址
    private String category;// 类别   违规告警类型
    private String username;// 违规者名称
    private Integer sdId;

    @Override
    public int compareTo(Alert obj) {
        if(obj == null){
            return 0;
        }else{

            return obj.getTime().compareTo(this.time);
        }
    }
}
