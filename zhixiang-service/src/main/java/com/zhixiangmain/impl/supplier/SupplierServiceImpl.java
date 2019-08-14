package com.zhixiangmain.impl.supplier;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.module.supplier.Supplier;
import com.zhixiangmain.api.service.supplier.SupplierService;
import com.zhixiangmain.dao.supplier.SupplierMapper;
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
 * @Package com.zhixiangyun.impl.supplier
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-26 17:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SupplierService.class)
public class SupplierServiceImpl implements SupplierService{
    private static final Logger logger = LoggerFactory
            .getLogger(SupplierServiceImpl.class);
    @Autowired
    private SupplierMapper supplierMapper;
    @Override
    public List<Map<String, Object>> getAllSupplier(Supplier supplier) {
        supplier.setDeleteStatus("1");
        return supplierMapper.findAllSupplier(supplier);
    }
}
