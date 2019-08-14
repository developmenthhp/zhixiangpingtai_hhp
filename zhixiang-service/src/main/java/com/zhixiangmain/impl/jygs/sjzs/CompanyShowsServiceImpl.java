package com.zhixiangmain.impl.jygs.sjzs;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.jygs.sjzs.CompanyShowsDTO;
import com.zhixiangmain.api.service.jygs.sjzs.CompanyShowsService;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.jygs.sjzs.CompanyShowsMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.jygs.sjzs
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-03 12:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CompanyShowsService.class)
public class CompanyShowsServiceImpl implements CompanyShowsService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(CompanyShowsServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CompanyShowsMapper companyShowsMapper;

    @Override
    public ResultBean getCompanyShows(CompanyShowsDTO companyShowsDTO, Integer page, Integer limit, JSONObject jobj) {
        return null;
    }
}
