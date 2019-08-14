package com.zhixiangmain.module.excel.dto;

import com.zhixiangmain.module.excel.ReadDataBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.excel.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-30 14:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class NamePrices implements Serializable {
    private List<ReadDataBase> namePrices;
}
