package com.zhixiangmain.api.module.work;

import com.alibaba.excel.annotation.ExcelProperty;
import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Brand extends BaseEntity {
    @ExcelProperty(value = {"名称"}, index = 1)
    private String name;   //品牌名称
    @ExcelProperty(value = {"所属公司"}, index =2)
    private String company;//公司


}
