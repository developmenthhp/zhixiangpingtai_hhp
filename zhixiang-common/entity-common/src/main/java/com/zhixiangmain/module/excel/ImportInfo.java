package com.zhixiangmain.module.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 导入 Excel 时使用的映射实体类，Excel 模型
 * @Date 2018-06-05
 * @Time 21:41
 */
@Data
@ToString
public class ImportInfo extends BaseRowModel {
    @ExcelProperty(index = 0)
    private String name;
    @ExcelProperty(index = 1)
    private String age;
    @ExcelProperty(index = 2)
    private String email;
}
