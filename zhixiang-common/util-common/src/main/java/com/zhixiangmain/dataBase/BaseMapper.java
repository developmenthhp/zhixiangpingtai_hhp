package com.zhixiangmain.dataBase;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-taotao
 * @Package cn.zhixiangyun.dataBase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2018-10-03 12:29
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BaseMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
