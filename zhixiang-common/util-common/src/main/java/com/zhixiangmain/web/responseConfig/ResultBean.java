package com.zhixiangmain.web.responseConfig;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-taotao
 * @Package cn.zhixiangyun.web.responseConfig
 * @Description: 返回结果配置
 * @author: hhp
 * @date: 2018-09-26 18:23
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class ResultBean implements Serializable{

    /** 成功，失败标识 */
    private boolean flag;
    /** 提示信息 */
    private String msg;
    /** 查询总条数 */
    private long total;
    /**总页数*/
    private long totalPage;
    /** 结果数据对象 */
    private Object data;
    /** 其他参数 */
    private Object code;
    /** 结果数据对象集合 */
    private List<?> rows;
    private Object obj;

    public ResultBean() {

    }

    /**
     *
     * @param flag
     *            true成功，false失败
     * @param msg
     *            提示信息
     */
    public ResultBean(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    /**
     *
     * @param flag
     *            true成功，false失败
     * @param msg
     *            提示信息
     * @param total
     *            返回查询结果总数
     * @param data
     *            封装查询结果数据
     * @param rows
     *            封装查询结果数据
     */
    public ResultBean(boolean flag, String msg, long total, Object data, List<?> rows) {
        this.flag = flag;
        this.msg = msg;
        this.total = total;
        this.data = data;
        this.rows = rows;
    }

    public ResultBean(boolean flag, String msg, Object obj, Object data, List<?> rows) {
        this.flag = flag;
        this.msg = msg;
        this.obj = obj;
        this.data = data;
        this.rows = rows;
    }

    public ResultBean(boolean flag, String msg, long total, long totalPage, Object data, List<?> rows) {
        this.flag = flag;
        this.msg = msg;
        this.total = total;
        this.totalPage=totalPage;
        this.data = data;
        this.rows = rows;
    }

    public ResultBean(boolean flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }
    public ResultBean(boolean flag, String msg, Object data,Object code) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public ResultBean(boolean flag, String msg, long total, List<?> rows) {
        this.flag = flag;
        this.msg = msg;
        this.total = total;
        this.rows = rows;
    }

    public ResultBean(boolean flag, String msg, List<?> rows) {
        this.flag = flag;
        this.msg = msg;
        this.rows = rows;
    }


}
