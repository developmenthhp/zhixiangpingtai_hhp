package com.zhixiangmain.module.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.excel
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-29 13:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ImportAuthorizedPrice extends BaseRowModel implements Serializable {
    //注解中 value 属性指定字段名，index属性指定字段排序
    @ExcelProperty(value ="食材名",index = 0)
    private String name;
    @ExcelProperty(value ="单价",index = 1)
    private Double price;
}
