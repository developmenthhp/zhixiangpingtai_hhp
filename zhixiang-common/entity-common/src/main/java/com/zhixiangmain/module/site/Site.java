package com.zhixiangmain.module.site;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.module.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-21 10:41
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="site")
public class Site implements Serializable {
    private Integer id;
    private Integer pid;//父id
    private Integer zindex;//优先级
    private Integer sdId;//试点id
    private String name;//站点名称
    private String address;//站点地址描述
    private String photo;//站点图标
    private Date insertTime;//添加数据时间
    private Date updateTime;//更新数据时间
    private Double lng;//经度
    private Double lat;//纬度
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date warningTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date overTime;
}
