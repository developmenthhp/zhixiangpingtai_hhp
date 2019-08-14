package com.zhixiangmain.api.module.qksj.yyfx.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.module.qksj.yyfx.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 13:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class NutingVO implements Serializable,Comparable<NutingVO> {
    private Double calorie;//卡路里
    private String name;//学生，员工 姓名
    private String siteName;
    private String sitePhoto;
    public NutingVO(Double calorie, String name, String siteName, String sitePhoto){
        this.calorie = calorie;
        this.name = name;
        this.siteName = siteName;
        this.sitePhoto = sitePhoto;
    }
    @Override
    public int compareTo(NutingVO nutingVO) {
        if (this.calorie < nutingVO.getCalorie()){
            return 1;
        } if (this.calorie == nutingVO.getCalorie()){
            return 0;
        }
        return -1;
    }

    public static void main(String[] args) {
        List<NutingVO> list = new ArrayList<NutingVO>();
        list.add(new NutingVO(22.5,"张三", "a","b"));
        list.add(new NutingVO(13.2,"李四",  "a","b"));
        list.add(new NutingVO(55.1, "王五", "a","b"));
        list.add(new NutingVO(24.5,"陈十七", "a","b"));
        Collections.sort(list);
        System.out.println(list.toString());
    }
}
