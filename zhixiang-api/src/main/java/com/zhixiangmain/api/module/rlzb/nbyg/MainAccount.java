package com.zhixiangmain.api.module.rlzb.nbyg;

import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.rlzb.nbyg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-18 13:34
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="mainaccount")
public class MainAccount extends BaseEntity {
    //系统账户,用于登录验证
    private String accountInfo;
    //系统账户密码,用于验证
    private String accountPWD;
    //系统账户分类-暂时不使用
    private String classify;
    //试点主体id
    private Integer sdId;
    //生成时间
    private String accountTime;
    //在线状态  1在线   2离线
    private String onlineStatus;
    //员工姓名-全称
    private String fullName;
    //职能工位
    private String duties;
    //性别, M女  F男
    private String gender;
    //年龄
    private String accountAge;
    //员工入职日期
    private String entryTime;
    //离职日期
    private String leaveTime;
    //身份证编号
    private String idCardNum;
    //紧急联系人电话
    private String pressingTel;
    //紧急联系人尊称
    private String pressingPerson;
    //户籍
    private String houseReg;
    private String youTuKouImgs;//口罩照片,多张 用于人脸识别
    private String menJinImgs;//门禁照片
    private String youTuImgs;//人脸照片,多张 用于人脸识别
    private String qianMingIimgs;//签名照片
    //1 正常在职使用，2 离职状态(不能登录)  3.禁用(不能登录)
    private String zhStatus;
    //1 正常使用 2 删除
    private String deleteStatus;
    private String loginTime;//登录时间
    private String qsType;//审签人类型
    private String userTel;//用户电话
    private Integer issAmple;//是否留样1,不是专属人员留样，2是专属人员留样
}
