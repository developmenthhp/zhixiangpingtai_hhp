package com.zhixiangmain.module.excel;

import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.module.excel
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-30 10:37
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ReadDataBase implements Serializable {
    private String name;
    private Double price;
    private Map<Integer, CellStyle> cellStyleMap;
    private Integer sdId;
}
