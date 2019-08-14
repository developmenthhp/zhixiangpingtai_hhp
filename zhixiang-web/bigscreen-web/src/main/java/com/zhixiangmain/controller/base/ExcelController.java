package com.zhixiangmain.controller.base;

import com.zhixiangmain.module.excel.ExportInfo;
import com.zhixiangmain.module.excel.ImportAuthorizedPrice;
import com.zhixiangmain.module.excel.ImportInfo;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.excel.ExcelUtil;
import com.zhixiangmain.overallParam.OpslabConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.controller.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-29 13:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {
    private static final Logger logger = LoggerFactory
            .getLogger(ExcelController.class);
    /** * 文件上传 * @param file 接收前端的formdata * @return 包含上传信息的json */

    @PostMapping(value = "/getPriceExcel")
    @ResponseBody
    public ResultBean singleFileUpload(@RequestParam("excelFile")MultipartFile excelFile){
        ResultBean resultBean = new ResultBean();
        if (excelFile.isEmpty()) {
            resultBean.setCode(IStatusMessage.LogicStatus.LOGIC_ERROR.getCode());
            resultBean.setMsg(WebMassage.FILE_UPLOAD_MISS);
            return resultBean;
        }
        try {
            List<Object> datas = ExcelUtil.readExcel(excelFile,new ImportAuthorizedPrice());
            resultBean.setData(datas);
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        } catch (Exception e) {
            resultBean.setCode(OpslabConfig.get("")+IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(WebMassage.ANALYTICAL_EXCEL_ANOMALY_ERROR);
            e.printStackTrace();
        }
        return resultBean;
    }

    /**
     * 读取 Excel（允许多个 sheet）
     */
    @RequestMapping(value = "/readExcelWithSheets", method = RequestMethod.POST)
    public Object readExcelWithSheets(MultipartFile excel) {
        return ExcelUtil.readExcel(excel, new ImportInfo());
    }

    /**
     * 读取 Excel（指定某个 sheet）
     */
    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
    public Object readExcel(MultipartFile excel, int sheetNo,
                            @RequestParam(defaultValue = "1") int headLineNum) {
        return ExcelUtil.readExcel(excel, new ImportInfo(), sheetNo, headLineNum);
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "/writeExcel", method = RequestMethod.GET)
    public void writeExcel(HttpServletResponse response) throws IOException {
        List<ExportInfo> list = getList();
        String fileName = "一个 Excel 文件";
        String sheetName = "第一个 sheet";

        ExcelUtil.writeExcel(response, list, fileName, sheetName, new ExportInfo());
    }

    /**
     * 导出 Excel（多个 sheet）
     */
    @RequestMapping(value = "/writeExcelWithSheets", method = RequestMethod.GET)
    public void writeExcelWithSheets(HttpServletResponse response) throws IOException {
        List<ExportInfo> list = getList();
        String fileName = "一个 Excel 文件";
        String sheetName1 = "第一个 sheet";
        String sheetName2 = "第二个 sheet";
        String sheetName3 = "第三个 sheet";

        ExcelUtil.writeExcelWithSheets(response, list, fileName, sheetName1, new ExportInfo())
                .write(list, sheetName2, new ExportInfo())
                .write(list, sheetName3, new ExportInfo())
                .finish();
    }

    private List<ExportInfo> getList() {
        List<ExportInfo> list = new ArrayList<>();
        ExportInfo model1 = new ExportInfo();
        model1.setName("howie");
        model1.setAge("19");
        model1.setAddress("123456789");
        model1.setEmail("123456789@gmail.com");
        list.add(model1);
        ExportInfo model2 = new ExportInfo();
        model2.setName("harry");
        model2.setAge("20");
        model2.setAddress("198752233");
        model2.setEmail("198752233@gmail.com");
        list.add(model2);
        return list;
    }
}
