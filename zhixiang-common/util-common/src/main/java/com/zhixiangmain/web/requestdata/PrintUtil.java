/**  
* Title: PrintUtil.java
* Description:
* Copyright: Copyright (c) 2018  
* Company: http://www.zhixiangyun.net
* @author hhp  
* @date 2018年6月27日  
* @version 1.0  
*/
package com.zhixiangmain.web.requestdata;

/**  
* Title: PrintUtil
* Description: 输出json格式参数
* @author hhp  
* @date 2018年6月27日  
*/
public class PrintUtil {

	private static boolean DEBUG_FLAG = true;

    public static void out(Object content) {
        if (DEBUG_FLAG) {
            System.out.print(content);
        }
    }

    public static void outln(Object content) {
        if (DEBUG_FLAG) {
            System.out.println(content);
        }
    }

    public static void out(String TAG, Object content) {
        if (DEBUG_FLAG) {
            System.out.print("===" + TAG + ":" + content);
        }
    }

    public static void outln(String TAG, Object content) {
        if (DEBUG_FLAG) {
            System.out.println("===" + TAG + ":" + content);
        }
    }
	
}
