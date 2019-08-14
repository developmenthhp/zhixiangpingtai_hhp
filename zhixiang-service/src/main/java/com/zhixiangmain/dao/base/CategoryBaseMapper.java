package com.zhixiangmain.dao.base;

import com.zhixiangmain.api.module.base.CategoryBase;
import com.zhixiangmain.api.module.base.dto.CategoryBaseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.base
 * @Description: 食材分类
 * @author: hhp
 * @date: 2019-02-25 10:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CategoryBaseMapper {
    List<CategoryBase> findCategoryBaseByPid(@Param("sdId") Integer sdId, @Param("pid") Integer pid);

    List<Map<String,Object>> findCategoryBaseByPidSdId(CategoryBaseDTO categoryBaseDTO);
}
