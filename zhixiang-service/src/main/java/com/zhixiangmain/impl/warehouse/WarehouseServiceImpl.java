package com.zhixiangmain.impl.warehouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.module.warehouse.Warehouse;
import com.zhixiangmain.api.service.warehouse.WarehouseService;
import com.zhixiangmain.dao.warehouse.WarehouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.warehouse
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-26 17:43
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = WarehouseService.class)
public class WarehouseServiceImpl implements WarehouseService{
    private static final Logger logger = LoggerFactory
            .getLogger(WarehouseServiceImpl.class);
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Override
    public List<Map<String, Object>> getAllWarehouse(Warehouse warehouse) {
        return warehouseMapper.findAllWarehouse(warehouse);
    }
}
