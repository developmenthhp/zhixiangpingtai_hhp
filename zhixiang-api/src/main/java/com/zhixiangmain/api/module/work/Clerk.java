package com.zhixiangmain.api.module.work;

import com.alibaba.excel.annotation.ExcelProperty;
import com.zhixiangmain.module.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Clerk extends BaseEntity {

    private int state = 0;
    @ExcelProperty(value = {"姓名"}, index = 1)
    private String name;
    @ExcelProperty(value = {"电话号码"}, index = 2)
    private String phone;
    @ExcelProperty(value = {"性别"}, index = 3)
    private String gender;
    @ExcelProperty(value = {"状态"}, index = 4)
    private String status;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "store_id")
    private Store store;

}
