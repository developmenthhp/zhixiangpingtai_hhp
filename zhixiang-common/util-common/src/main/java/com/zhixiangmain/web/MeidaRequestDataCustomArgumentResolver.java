package com.zhixiangmain.web;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.web
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-11-22 11:24
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public enum MeidaRequestDataCustomArgumentResolver {

    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");//记住要用分号结束

    private String desc;//中文描述

    /**
     * 私有构造,防止被外部调用
     * @param desc
     */
    private MeidaRequestDataCustomArgumentResolver(String desc){ this.desc=desc; }
    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     * @return
     */
    public String getDesc(){
        return desc;
    }
    public static void main(String[] args){
        for (MeidaRequestDataCustomArgumentResolver day: MeidaRequestDataCustomArgumentResolver.values()) {
            System.out.println("name:"+day.name()+ ",desc:"+day.getDesc());
        }
    }


}
