package com.zhixiangmain.pagination;

import java.util.ArrayList;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.pagination
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 15:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyStartEndUtil {
    public static ArrayList<ArrayList<Integer>> getStartEndArray(ArrayList<Integer> totalAll,int startNum,int endNum){
        ArrayList<ArrayList<Integer>> startEndNums = new ArrayList<>();
        int count1=0;
        for(Integer totalSingle:totalAll){
            count1=count1+totalSingle;
            if(count1>startNum){
                ArrayList<Integer> startEndNum = new ArrayList<>();
                int startNumIndbNo=totalSingle-(count1-startNum);//算出从每个库中第几个开始取
                if(startNumIndbNo<0){
                    startNumIndbNo = 0;
                }
                startEndNum.add(startNumIndbNo);
                int endNumIndbNo = 0;
                if(endNum>count1){
                    endNumIndbNo=totalSingle;//算出从每个库中取的最后一个值是第几个。
                    startEndNum.add(endNumIndbNo);
                    startEndNums.add(startEndNum);
                }else{
                    endNumIndbNo=totalSingle-(count1-endNum);//算出从每个库中取的最后一个值是第几个
                    startEndNum.add(endNumIndbNo);
                    startEndNums.add(startEndNum);
                    break;//这里需要退出，因为取的值已经够了
                }

            }else{
                ArrayList<Integer> startEndNum = new ArrayList<>();
                startEndNum.add(0);
                startEndNum.add(0);
                startEndNums.add(startEndNum);
            }
        }
        System.out.println(startEndNums+"------------------------------|||||||||||||||||||||||");
        return startEndNums;
    }
}
