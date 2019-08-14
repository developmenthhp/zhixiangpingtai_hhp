package com.zhixiangmain.api.module.rlzb.aqjc;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.module.rlzb.aqjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-03 13:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="firesafetyinspection")
public class FireSafetyInspection extends BaseEntity {
    //试点
    private Integer sdId;
    private String checkCategories;	//检查类别   1.消防安全 2.食品安全 3.人员安全
    private String checkForm;  //检查的形式 1.例行检查  2.不定期抽查 3.领导检查 4.主管单位检查
    //检查的人员/单位
    private String checkPerson;
    //检查时间
    private String checkTime;
    //检查的区域
    private Integer areaId;
    //检查区域一级分类
    private Integer mainCategoryId;
    //检查区域二级分类
    private Integer smallCategoryId;
    //清洁卫生
    private String cleanIsQualified;// 1.合格 ，2.不合格
    //清洁卫生图片
    private String cleanIsQualifiedImg;
    //摆放整齐
    private String isPlaceNeatly; // 1.合格 ，2.不合格
    //摆放整齐图片
    private String placeNeatlyImg;
    //消防通道
    private String fireEngineAccess;	// 1.合格 ，2.不合格
    //消防通道图片
    private String fireEngineAccessImg;
    //消防器具有效期
    private String fireApplianceSarevalId;// 1.合格 ，2.不合格
    //消防器具有效期图片
    private String fireApplianceSarevalIdImg;
    //灭火毯
    private String fireBlanket;	// 1.合格 ，2.不合格
    //灭火毯图片
    private String fireBlanketImg;
    //瓦斯开关
    private String theGasSwitch;// 1.合格 ，2.不合格
    //瓦斯开关图片
    private String theGasSwitchImg;
    //瓦斯监测
    private String gasMonitor;	// 1.合格 ，2.不合格
    //瓦斯监测图片
    private String gasMonitorImg;
    //备注
    private String remark;
}
