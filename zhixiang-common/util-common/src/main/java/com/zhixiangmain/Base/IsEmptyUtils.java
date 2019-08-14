/**  
* Title: IsEmptyUtils.java
* Description:
* Copyright: Copyright (c) 2018  
* Company: http://www.zhixiangyun.net
* @author hhp  
* @date 2018年6月27日  
* @version 1.0  
*/
package com.zhixiangmain.Base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**  
* Title: IsEmptyUtils
* Description: 判断对象是否为空
* @author hhp  
* @date 2018年6月27日  
*/
public class IsEmptyUtils {

	public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection<?>) obj).isEmpty();
        else if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
    
    public static void main(String args[]) {
    	int[] code = {};
    	System.out.println(isEmpty(code));
    }
	
}
