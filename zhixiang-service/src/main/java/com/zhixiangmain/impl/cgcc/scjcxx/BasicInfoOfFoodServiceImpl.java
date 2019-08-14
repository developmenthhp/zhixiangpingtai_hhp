package com.zhixiangmain.impl.cgcc.scjcxx;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.BasicInfoFood;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.api.service.cgcc.BasicInfoOfFoodService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.config.TargetDataSource;
import com.zhixiangmain.dao.cgcc.scck.LibraryOutMapper;
import com.zhixiangmain.dao.cgcc.scrk.WarehouseDetailMapper;
import com.zhixiangmain.dao.qppr.prcz.CookingWeightMapper;
import com.zhixiangmain.dao.qppr.qpcz.CutWeightMapper;
import com.zhixiangmain.dao.qxjp.scfgcz.AirDryingWeightMapper;
import com.zhixiangmain.dao.qxjp.scqxcz.CleanWeightMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.excel.ReadDataBase;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.pagination.MyStartEndUtil;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.scjcxx.BasicInfoOfFoodMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.cgcc.scjcxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-25 15:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = BasicInfoOfFoodService.class)
public class BasicInfoOfFoodServiceImpl implements BasicInfoOfFoodService{
    private static final Logger logger = LoggerFactory
            .getLogger(BasicInfoOfFoodServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private BasicInfoOfFoodMapper basicInfoOfFoodMapper;
    @Autowired
    private WarehouseDetailMapper warehouseDetailMapper;
    @Autowired
    private LibraryOutMapper libraryOutMapper;
    @Autowired
    private CleanWeightMapper cleanWeightMapper;
    @Autowired
    private AirDryingWeightMapper airDryingWeightMapper;
    @Autowired
    private CutWeightMapper cutWeightMapper;
    @Autowired
    private CookingWeightMapper cookingWeightMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public PageDataResult getBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<Map<String,Object>> basicInfoFoods = basicInfoOfFoodMapper.findBIOFoodList(basicInfoFoodDTO);
        // 获取分页查询后的数据
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(basicInfoFoods);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(basicInfoFoods);
        return pdr;
    }

    @Override
    public ResultBean checkName(BasicInfoFood basicInfoFood) {
        ResultBean resultBean = new ResultBean();
        Object id = basicInfoOfFoodMapper.checkNameTotal(basicInfoFood);
        if(IsEmptyUtils.isEmpty(id)){
            resultBean.setCode(IStatusMessage.LogicStatus.NAME_NOT_EXISTS.getCode());
        }else{
            if(Integer.parseInt(String.valueOf(id))==basicInfoFood.getId()){
                resultBean.setCode(IStatusMessage.LogicStatus.NAME_NOT_EXISTS.getCode());
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.NAME_EXISTS.getCode());
                resultBean.setMsg(WebMassage.INGREDIENT_HAS_SAME_NAME);
            }
        }
        resultBean.setFlag(true);
        return resultBean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean updateBasicInfoFood(BasicInfoFood basicInfoFood) {
        ResultBean resultBean = new ResultBean();
        basicInfoOfFoodMapper.updateBasicInfoFood(basicInfoFood);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(WebMassage.UPDATE_INGREDIENT_SUCCESS);
        return resultBean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean addBasicInfoFood(BasicInfoFood basicInfoFood) {
        ResultBean resultBean = new ResultBean();
        basicInfoFood.setDeleteStatus("1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        basicInfoFood.setCreateDate(simpleDateFormat.format(new Date()));

        String bigCategory = "";

        if(!IsEmptyUtils.isEmpty(basicInfoFood.getBigCategory())){
            switch (basicInfoFood.getBigCategory()){
                case 1:{
                    bigCategory = "SC";
                }
                break;
                case 2:{
                    bigCategory = "SCP";
                }
                break;
                case 3:{
                    bigCategory = "DP";
                }
                break;
                case 4:{
                    bigCategory = "GT";
                }
                break;
                case 5:{
                    bigCategory = "HC";
                }
                break;
                case 6:{
                    bigCategory = "SG";
                }
                break;
            }
        }

        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        if(!IsEmptyUtils.isEmpty(vo.get(bigCategory+"-23"))){
            Integer curFoodCode = Integer.parseInt(String.valueOf(vo.get(bigCategory+"-23")))+1;
            vo.set(bigCategory+"-23",curFoodCode);
            basicInfoFood.setFoodCode(curFoodCode);
        }else{
            vo.set(bigCategory+"-23",1);
            basicInfoFood.setFoodCode(1);
        }

        basicInfoOfFoodMapper.addBasicInfoFood(basicInfoFood);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(WebMassage.INSERT_INGREDIENT_SUCCESS);
        return resultBean;
    }

    @Override
    public ResultBean getBasicInfoFoodById(int id, Integer sdId) {
        ResultBean resultBean = new ResultBean();
        Map<String,Object> basicInfoOfFood = basicInfoOfFoodMapper.findBasicInfoFoodById(id,sdId);
        resultBean.setData(basicInfoOfFood);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean delBasicInfoFoodById(int id, Integer sdId) {
        ResultBean resultBean = new ResultBean();
        Integer total = basicInfoOfFoodMapper.delBasicInfoFoodById(id,sdId);
        resultBean.setFlag(true);
        resultBean.setMsg(WebMassage.DELETE_INGREDIENT_SUCCESS);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean doAddFood(){

        List<String> priceList = new ArrayList<String>();

        try {

            File file = new File("/food/食材导入.xlsx");

            //判断文件是否存在
            String[] split = file.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
            Workbook wb = null;
            //根据文件后缀（xls/xlsx）进行判断
            if ( "xls".equals(split[1])){
                FileInputStream fis = new FileInputStream(file);   //文件流对象
                wb = new HSSFWorkbook(fis);
            }else if ("xlsx".equals(split[1])){
                wb = new XSSFWorkbook(file);
            }else {
                System.out.println("文件类型错误!");
            }
            //开始解析
            //读取sheet 0
            Sheet sheet = wb.getSheetAt(0);

            //第一行是列名，所以不读
            int firstRowIndex = sheet.getFirstRowNum()+2;
            int lastRowIndex = sheet.getLastRowNum();

            //遍历行
            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {

                String ingredientNamePrice="";

                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    int firstCellIndex = row.getFirstCellNum()+1;
                    int lastCellIndex = row.getLastCellNum();
                    //遍历列
                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
                            //System.out.println(cell.toString());
                            if(cIndex==firstCellIndex){
                                ingredientNamePrice = cell.toString();
                            }else{
                                ingredientNamePrice = ingredientNamePrice+","+cell.toString();
                            }

                        }
                    }
                }

                priceList.add(ingredientNamePrice);

            }
            for(String s:priceList){
                String[] codeAndName = s.split(",");
                String code = codeAndName[0];
                String[] codes = code.split("-");

                BasicInfoFood basicInfoFood = new BasicInfoFood();

                ValueOperations<String, Object> vo = redisTemplate.opsForValue();
                if(!IsEmptyUtils.isEmpty(vo.get(codes[1]+"-23"))){
                    Integer curFoodCode = Integer.parseInt(String.valueOf(vo.get(codes[1]+"-23")))+1;
                    vo.set(codes[1]+"-23",curFoodCode);
                    basicInfoFood.setFoodCode(curFoodCode);
                }else{
                    vo.set(codes[1]+"-23",1);
                    basicInfoFood.setFoodCode(1);
                }

                if(codes[1].equals("SC")){
                    basicInfoFood.setBigCategory(1);
                }else if(codes[1].equals("SCP")){
                    basicInfoFood.setBigCategory(2);
                }else if(codes[1].equals("DP")){
                    basicInfoFood.setBigCategory(3);
                }else if(codes[1].equals("GT")){
                    basicInfoFood.setBigCategory(4);
                }else if(codes[1].equals("HC")){
                    basicInfoFood.setBigCategory(5);
                }else if(codes[1].equals("SG")){
                    basicInfoFood.setBigCategory(6);
                }


                if(codes[2].equals("A")){
                    basicInfoFood.setLevel(1);
                }else if(codes[2].equals("B")){
                    basicInfoFood.setLevel(2);
                }else if(codes[2].equals("C")){
                    basicInfoFood.setLevel(3);
                }else if(codes[2].equals("D")){
                    basicInfoFood.setLevel(4);
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                basicInfoFood.setCreateDate(simpleDateFormat.format(new Date()));
                basicInfoFood.setDeleteStatus("1");
                basicInfoFood.setCheckStatus("1");
                basicInfoFood.setIngredientName(codeAndName[1]);
                basicInfoFood.setSdId(22);


                basicInfoOfFoodMapper.addBasicInfoFood(basicInfoFood);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultBean();
    }

    @Override
    public List<Map<String, Object>> getBasicInfoFoodByIds(String ids) {
        List<Map<String,Object>> basicInfoFoods = new ArrayList<Map<String,Object>>();
        for(String id:ids.split(",")){
            Map<String,Object> basicInfoOfFood = basicInfoOfFoodMapper.getBasicInfoFoodByIds(id);
            basicInfoFoods.add(basicInfoOfFood);
        }
        return basicInfoFoods;
    }

    @Override
    public PageDataResult getInsideBIOFoodList(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<Map<String,Object>> basicInfoFoods = basicInfoOfFoodMapper.findInsideBIOFoodList(basicInfoFoodDTO);
        // 获取分页查询后的数据
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(basicInfoFoods);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(basicInfoFoods);
        return pdr;
    }

    @Override
    public ResultBean getAll(Integer sdId) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> basicInfoOfFoods = basicInfoOfFoodMapper.findAll(sdId);
        resultBean.setFlag(true);
        resultBean.setMsg(WebMassage.DELETE_INGREDIENT_SUCCESS);
        resultBean.setRows(basicInfoOfFoods);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    @TargetDataSource(name = "default")//使用默认数据库（总库）
    public ResultBean importBasePrice(Integer sdId, List<ReadDataBase> namePrices){
        ResultBean resultBean = new ResultBean();
        Integer updateTotal = 0;
        for(ReadDataBase readDataBase:namePrices){
            readDataBase.setSdId(sdId);
            updateTotal = updateTotal+basicInfoOfFoodMapper.updateBasePriceBySdIdName(readDataBase);
        }
        resultBean.setFlag(true);
        String msg = "";
        String code = "";
        if(updateTotal==0){
            msg = WebMassage.IMPORT_BASE_PRICE_DATA_MATCHING;
            code = IStatusMessage.LogicStatus.IMPORT_BASE_PRICE_DATA_MATCHING.getCode();
        }else{
            msg = "已为"+namePrices.size()+"个食材成功导入"+updateTotal+"条单价";
            code = IStatusMessage.SystemStatus.SUCCESS.getCode();
        }
        resultBean.setMsg(msg);
        resultBean.setCode(code);
        resultBean.setTotal(updateTotal);
        return resultBean;
    }

    @Override
    public ResultBean getBaseInfoOfFoodBySdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getSdIdStr())){
            String[] sdIds = basicInfoFoodDTO.getSdIdStr().split(",");
            for(String sdId:sdIds){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取采购单
                    basicInfoFoodDTO.setSdId(Integer.parseInt(sdId));
                    /*if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getIdSdId())){
                        foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                        foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                        foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                    }*/
                    List<Map<String,Object>> categoryBases = basicInfoOfFoodMapper.findBaseInfoOfFoodBySdId(basicInfoFoodDTO);
                    mainLPLs.addAll(categoryBases);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(basicInfoFoodDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
                    if(!IsEmptyUtils.isEmpty(jobj.get(siteVO.getSdId().toString()))){
                        dataSourceName = jobj.get(siteVO.getSdId().toString()).toString();
                    }
                    //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                    if(!IsEmptyUtils.isEmpty(dataSourceName)){
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        basicInfoFoodDTO.setSdId(siteVO.getSdId());
                        /*if(!IsEmptyUtils.isEmpty(foodBaseDTO.getIdSdId())){
                            foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                            foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                            foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                        }*/
                        List<Map<String,Object>> categoryBases = basicInfoOfFoodMapper.findBaseInfoOfFoodBySdId(basicInfoFoodDTO);
                        mainLPLs.addAll(categoryBases);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }

        resultBean.setRows(mainLPLs);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getAllIngBBySdId(BasicInfoFoodDTO basicInfoFoodDTO, Integer page, Integer limit, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<String> ingBaseList = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        //所有食材名
        if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getSdIdStr())){
            String[] sdIds = basicInfoFoodDTO.getSdIdStr().split(",");
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(sdId);
                    }else{
                        stringBuffer.append(","+sdId);
                    }
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = basicInfoOfFoodMapper.findPaginationDataTotal(basicInfoFoodDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                basicInfoFoodDTO.setStart(startEndNums.get(i).get(0));
                basicInfoFoodDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                basicInfoFoodDTO.setSdId(Integer.parseInt(sourceNames[i]));
                //获取分页数据
                List<String> namgs  = basicInfoOfFoodMapper.findAllIngBNameBySdId(basicInfoFoodDTO);
                ingBaseList = getOneNameDataList(ingBaseList,namgs);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(basicInfoFoodDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(siteVO.getSdId());
                    }else{
                        stringBuffer.append(","+siteVO.getSdId());
                    }
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = basicInfoOfFoodMapper.findPaginationDataTotal(basicInfoFoodDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询 未传试点id：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                basicInfoFoodDTO.setStart(startEndNums.get(i).get(0));
                basicInfoFoodDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                //获取分页数据
                List<String> namgs  = basicInfoOfFoodMapper.findAllIngBNameBySdId(basicInfoFoodDTO);
                ingBaseList = getOneNameDataList(ingBaseList,namgs);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(ingBaseList);
        resultBean.setTotal(total);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    /**
     * @param basicInfoFoodDTO 参数
     * @param jobj 站点配置
     * 查找该名称食材所有环节损耗 进出库到烹饪
     * */
    @Override
    public ResultBean getIngBUseBySdIdName(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Double whDetailWeightAll = 0.0;
        Double loWeightAll = 0.0;
        Map<String,Object> cleanWeightMapAll = null;
        Map<String,Object> adWeightMapAll = null;
        Map<String,Object> cutWeightMapAll = null;
        Map<String,Object> cookWeightMapAll = null;
        List<Map<String,Object>> slopsList = Lists.newArrayList();

        if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getStartEnd())){
            String[] startEnd = basicInfoFoodDTO.getStartEnd().split(" - ");
            basicInfoFoodDTO.setStartTime(startEnd[0]+" 00:00:00");
            basicInfoFoodDTO.setEndTime(startEnd[1]+" 23:59:59");
        }

        if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getSdIdStr())){
            String[] sdIds = basicInfoFoodDTO.getSdIdStr().split(",");
            for(String sdId:sdIds){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    basicInfoFoodDTO.setSdId(Integer.parseInt(sdId));
                    /* 这里全部统一用公斤 */
                    //查找食材id
                    Map<String,Object> basicFoodMap = basicInfoOfFoodMapper.findByName(basicInfoFoodDTO);
                    if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                        if(!IsEmptyUtils.isEmpty(basicFoodMap.get("id"))){
                            basicInfoFoodDTO.setIngredientId(Integer.parseInt(basicFoodMap.get("id").toString()));
                        }else{
                            basicInfoFoodDTO.setIngredientId(-1);
                        }
                    }
                    //入库重量
                    Double whDetailWeight  = warehouseDetailMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    //出库重量
                    Double loWeight = libraryOutMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                        if(!IsEmptyUtils.isEmpty(basicFoodMap.get("meteringName"))){
                            if(basicFoodMap.get("meteringName").equals("g")||basicFoodMap.get("meteringName").equals("g(克)")
                                    ||basicFoodMap.get("meteringName").equals("克")||basicFoodMap.get("meteringName").equals("克(g)")
                                    ||basicFoodMap.get("meteringName").equals("克（g）")||basicFoodMap.get("meteringName").equals("g（克）")){
                                //克转公斤
                                whDetailWeight = whDetailWeight*0.001;
                                loWeight = loWeight*0.001;
                            }else if(basicFoodMap.get("meteringName").equals("斤")){
                                //斤转公斤
                                whDetailWeight = whDetailWeight*0.5;
                                loWeight = loWeight*0.5;
                            }else if(basicFoodMap.get("meteringName").equals("kg")||basicFoodMap.get("meteringName").equals("kg(公斤)")
                                    ||basicFoodMap.get("meteringName").equals("公斤")||basicFoodMap.get("meteringName").equals("公斤(kg)")
                                    ||basicFoodMap.get("meteringName").equals("公斤（kg）")||basicFoodMap.get("meteringName").equals("kg（公斤）")
                                    ||basicFoodMap.get("meteringName").equals("千克")||basicFoodMap.get("meteringName").equals("千克(kg)")||basicFoodMap.get("meteringName").equals("千克(公斤)")
                                    ||basicFoodMap.get("meteringName").equals("kg(千克)")||basicFoodMap.get("meteringName").equals("公斤(千克)")||basicFoodMap.get("meteringName").equals("千克（kg）")||basicFoodMap.get("meteringName").equals("千克（公斤）")
                                    ||basicFoodMap.get("meteringName").equals("kg（千克）")||basicFoodMap.get("meteringName").equals("公斤（千克）")){

                            }else{
                                if(!IsEmptyUtils.isEmpty(basicFoodMap.get("ggxhdw"))){
                                    //其他使用规格型号换算成公斤
                                    if(basicFoodMap.get("ggxhdw").equals("g")||basicFoodMap.get("ggxhdw").equals("克")){
                                        whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                                        loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                                    }else if(basicFoodMap.get("ggxhdw").equals("斤")){
                                        whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                                        loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                                    }else if(basicFoodMap.get("ggxhdw").equals("公斤")||basicFoodMap.get("ggxhdw").equals("kg")){
                                        whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString());
                                        loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString());
                                    }
                                }
                            }
                        }
                    }
                    //入库重量
                    whDetailWeightAll = whDetailWeightAll + whDetailWeight;
                    //出库重量
                    loWeightAll = loWeightAll + loWeight;
                    //食材清洗
                    Map<String,Object> cleanWeightMap = cleanWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    cleanWeightMapAll = getMixMap(cleanWeightMapAll,cleanWeightMap,basicFoodMap);
                    //食材风干
                    Map<String,Object> adWeightMap = airDryingWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    adWeightMapAll = getMixMap(adWeightMapAll,adWeightMap,basicFoodMap);
                    //食材切配
                    Map<String,Object> cutWeightMap = cutWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    cutWeightMapAll = getMixMap(cutWeightMapAll,cutWeightMap,basicFoodMap);
                    //食材烹饪
                    Map<String,Object> cookWeightMap = cookingWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                    cookWeightMapAll = getMixMap(cookWeightMapAll,cookWeightMap,basicFoodMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(basicInfoFoodDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
                    String sdIdStr = siteVO.getSdId().toString();
                    SiteData siteData = null;
                    if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                        siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                    }
                    if(!IsEmptyUtils.isEmpty(siteData)){
                        dataSourceName = siteData.getDateSourceName();
                    }
                    //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                    if(!IsEmptyUtils.isEmpty(dataSourceName)){
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        basicInfoFoodDTO.setSdId(siteVO.getSdId());
                        /* 这里全部统一用公斤 */
                        //查找食材id
                        Map<String,Object> basicFoodMap = basicInfoOfFoodMapper.findByName(basicInfoFoodDTO);
                        if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                            if(!IsEmptyUtils.isEmpty(basicFoodMap.get("id"))){
                                basicInfoFoodDTO.setIngredientId(Integer.parseInt(basicFoodMap.get("id").toString()));
                            }else{
                                basicInfoFoodDTO.setIngredientId(-1);
                            }
                        }
                        //入库重量
                        Double whDetailWeight  = warehouseDetailMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        //出库重量
                        Double loWeight = libraryOutMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                            if(!IsEmptyUtils.isEmpty(basicFoodMap.get("meteringName"))){
                                if(basicFoodMap.get("meteringName").equals("g")||basicFoodMap.get("meteringName").equals("g(克)")
                                        ||basicFoodMap.get("meteringName").equals("克")||basicFoodMap.get("meteringName").equals("克(g)")
                                        ||basicFoodMap.get("meteringName").equals("克（g）")||basicFoodMap.get("meteringName").equals("g（克）")){
                                    //克转公斤
                                    whDetailWeight = whDetailWeight*0.001;
                                    loWeight = loWeight*0.001;
                                }else if(basicFoodMap.get("meteringName").equals("斤")){
                                    //斤转公斤
                                    whDetailWeight = whDetailWeight*0.5;
                                    loWeight = loWeight*0.5;
                                }else if(basicFoodMap.get("meteringName").equals("kg")||basicFoodMap.get("meteringName").equals("kg(公斤)")
                                        ||basicFoodMap.get("meteringName").equals("公斤")||basicFoodMap.get("meteringName").equals("公斤(kg)")
                                        ||basicFoodMap.get("meteringName").equals("公斤（kg）")||basicFoodMap.get("meteringName").equals("kg（公斤）")
                                        ||basicFoodMap.get("meteringName").equals("千克")||basicFoodMap.get("meteringName").equals("千克(kg)")||basicFoodMap.get("meteringName").equals("千克(公斤)")
                                        ||basicFoodMap.get("meteringName").equals("kg(千克)")||basicFoodMap.get("meteringName").equals("公斤(千克)")||basicFoodMap.get("meteringName").equals("千克（kg）")||basicFoodMap.get("meteringName").equals("千克（公斤）")
                                        ||basicFoodMap.get("meteringName").equals("kg（千克）")||basicFoodMap.get("meteringName").equals("公斤（千克）")){

                                }else{
                                    if(!IsEmptyUtils.isEmpty(basicFoodMap.get("ggxhdw"))){
                                        //其他使用规格型号换算成公斤
                                        if(basicFoodMap.get("ggxhdw").equals("g")||basicFoodMap.get("ggxhdw").equals("克")){
                                            whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                                            loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                                        }else if(basicFoodMap.get("ggxhdw").equals("斤")){
                                            whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                                            loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                                        }else if(basicFoodMap.get("ggxhdw").equals("公斤")||basicFoodMap.get("ggxhdw").equals("kg")){
                                            whDetailWeight = whDetailWeight*Double.valueOf(basicFoodMap.get("ggxh").toString());
                                            loWeight = loWeight*Double.valueOf(basicFoodMap.get("ggxh").toString());
                                        }
                                    }
                                }
                            }
                        }
                        //入库重量
                        whDetailWeightAll = whDetailWeightAll + whDetailWeight;
                        //出库重量
                        loWeightAll = loWeightAll + loWeight;
                        //食材清洗
                        Map<String,Object> cleanWeightMap = cleanWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        cleanWeightMapAll = getMixMap(cleanWeightMapAll,cleanWeightMap,basicFoodMap);
                        //食材风干
                        Map<String,Object> adWeightMap = airDryingWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        adWeightMapAll = getMixMap(adWeightMapAll,adWeightMap,basicFoodMap);
                        //食材切配
                        Map<String,Object> cutWeightMap = cutWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        cutWeightMapAll = getMixMap(cutWeightMapAll,cutWeightMap,basicFoodMap);
                        //食材烹饪
                        Map<String,Object> cookWeightMap = cookingWeightMapper.findWeightByIngBaseId(basicInfoFoodDTO);
                        cookWeightMapAll = getMixMap(cookWeightMapAll,cookWeightMap,basicFoodMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        Map<String,Object> whdWeightMap = new HashMap<>();
        whdWeightMap.put("foodWeight",whDetailWeightAll);
        Map<String,Object> loWeightMap = new HashMap<>();
        loWeightMap.put("foodWeight",loWeightAll);
        slopsList.add(whdWeightMap);
        slopsList.add(loWeightMap);
        slopsList.add(cleanWeightMapAll);
        slopsList.add(adWeightMapAll);
        slopsList.add(cutWeightMapAll);
        slopsList.add(cookWeightMapAll);
        resultBean.setRows(slopsList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    public List<String> getOneNameDataList(List<String> allList,List<String> paramList){
        if(!IsEmptyUtils.isEmpty(allList)){
            for(String ingBaseName:paramList){
                if(!allList.contains(ingBaseName)){
                    //旧list不包含，加入list
                    allList.add(ingBaseName);
                    break;
                }
            }
        }else{
            allList.addAll(paramList);
        }
        return allList;
    }

    public Map<String,Object> getMixMap(Map<String,Object> allMap,Map<String,Object> paramMap,Map<String,Object> basicFoodMap){
        if(!IsEmptyUtils.isEmpty(allMap)){
            Double weight = Double.valueOf(allMap.get("foodWeight").toString());
            Double weightParam = Double.valueOf(paramMap.get("foodWeight").toString());
            if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                if(!IsEmptyUtils.isEmpty(basicFoodMap.get("meteringName"))){
                    //单位转换
                    if(basicFoodMap.get("meteringName").equals("g")||basicFoodMap.get("meteringName").equals("g(克)")
                            ||basicFoodMap.get("meteringName").equals("克")||basicFoodMap.get("meteringName").equals("克(g)")
                            ||basicFoodMap.get("meteringName").equals("克（g）")||basicFoodMap.get("meteringName").equals("g（克）")){
                        //克转公斤
                        weightParam = weightParam*0.001;
                    }else if(basicFoodMap.get("meteringName").equals("斤")){
                        //斤转公斤
                        weightParam = weightParam*0.5;
                    }else if(basicFoodMap.get("meteringName").equals("kg")||basicFoodMap.get("meteringName").equals("kg(公斤)")
                            ||basicFoodMap.get("meteringName").equals("公斤")||basicFoodMap.get("meteringName").equals("公斤(kg)")
                            ||basicFoodMap.get("meteringName").equals("公斤（kg）")||basicFoodMap.get("meteringName").equals("kg（公斤）")
                            ||basicFoodMap.get("meteringName").equals("千克")||basicFoodMap.get("meteringName").equals("千克(kg)")||basicFoodMap.get("meteringName").equals("千克(公斤)")
                            ||basicFoodMap.get("meteringName").equals("kg(千克)")||basicFoodMap.get("meteringName").equals("公斤(千克)")||basicFoodMap.get("meteringName").equals("千克（kg）")||basicFoodMap.get("meteringName").equals("千克（公斤）")
                            ||basicFoodMap.get("meteringName").equals("kg（千克）")||basicFoodMap.get("meteringName").equals("公斤（千克）")){

                    }else{
                        if(!IsEmptyUtils.isEmpty(basicFoodMap.get("ggxhdw"))){
                            //其他使用规格型号换算成公斤
                            if(basicFoodMap.get("ggxhdw").equals("g")||basicFoodMap.get("ggxhdw").equals("克")){
                                weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                            }else if(basicFoodMap.get("ggxhdw").equals("斤")){
                                weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                            }else if(basicFoodMap.get("ggxhdw").equals("公斤")||basicFoodMap.get("ggxhdw").equals("kg")){
                                weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString());
                            }
                        }
                    }
                }
            }
            allMap.put("foodWeight",weight+weightParam);
            DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
            Double standard = Double.valueOf(allMap.get("standard").toString());
            Double standardParam = Double.valueOf(paramMap.get("standard").toString());
            allMap.put("standard",df.format((standard+standardParam)/2));
        }else{
            Double weightParam = Double.valueOf(paramMap.get("foodWeight").toString());
            if(!IsEmptyUtils.isEmpty(basicFoodMap)){
                if(!IsEmptyUtils.isEmpty(basicFoodMap.get("meteringName"))){
                    if(basicFoodMap.get("meteringName").equals("g")||basicFoodMap.get("meteringName").equals("g(克)")
                            ||basicFoodMap.get("meteringName").equals("克")||basicFoodMap.get("meteringName").equals("克(g)")
                            ||basicFoodMap.get("meteringName").equals("克（g）")||basicFoodMap.get("meteringName").equals("g（克）")){
                        //克转公斤
                        weightParam = weightParam*0.001;
                    }else if(basicFoodMap.get("meteringName").equals("斤")){
                        //斤转公斤
                        weightParam = weightParam*0.5;
                    }else if(basicFoodMap.get("meteringName").equals("kg")||basicFoodMap.get("meteringName").equals("kg(公斤)")
                            ||basicFoodMap.get("meteringName").equals("公斤")||basicFoodMap.get("meteringName").equals("公斤(kg)")
                            ||basicFoodMap.get("meteringName").equals("公斤（kg）")||basicFoodMap.get("meteringName").equals("kg（公斤）")
                            ||basicFoodMap.get("meteringName").equals("千克")||basicFoodMap.get("meteringName").equals("千克(kg)")||basicFoodMap.get("meteringName").equals("千克(公斤)")
                            ||basicFoodMap.get("meteringName").equals("kg(千克)")||basicFoodMap.get("meteringName").equals("公斤(千克)")||basicFoodMap.get("meteringName").equals("千克（kg）")||basicFoodMap.get("meteringName").equals("千克（公斤）")
                            ||basicFoodMap.get("meteringName").equals("kg（千克）")||basicFoodMap.get("meteringName").equals("公斤（千克）")){

                    }else{
                        //其他使用规格型号换算成公斤
                        if(basicFoodMap.get("ggxhdw").equals("g")||basicFoodMap.get("ggxhdw").equals("克")){
                            weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.001;
                        }else if(basicFoodMap.get("ggxhdw").equals("斤")){
                            weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString())*0.5;
                        }else if(basicFoodMap.get("ggxhdw").equals("公斤")||basicFoodMap.get("ggxhdw").equals("kg")){
                            weightParam = weightParam*Double.valueOf(basicFoodMap.get("ggxh").toString());
                        }
                    }
                }
            }
            paramMap.put("foodWeight",weightParam);
            allMap = paramMap;
        }
        return allMap;
    }

    public static void main(String args[]){
        BasicInfoOfFoodServiceImpl basicInfoOfFoodService = new BasicInfoOfFoodServiceImpl();
        basicInfoOfFoodService.doAddFood();
    }


}
