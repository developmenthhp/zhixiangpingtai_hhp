package com.zhixiangmain.impl.foodIngredient;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.foodIngredient.FoodIngredient;
import com.zhixiangmain.api.module.foodIngredient.dto.FoodIngredientDTO;
import com.zhixiangmain.api.service.foodIngredient.FoodIngredientService;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.foodIngredient.FoodIngredientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.foodIngredient
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-08 15:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FoodIngredientService.class)
public class FoodIngredientServiceImpl implements FoodIngredientService {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodIngredientServiceImpl.class);
    @Autowired
    private FoodIngredientMapper foodIngredientMapper;
    @Override
    public ResultBean getByFoodId(int foodId, Integer sdId) {
        ResultBean resultBean = new ResultBean();
        String sort = "asc";
        List<Map<String,Object>> foodIngredients = foodIngredientMapper.findByFoodId(foodId,sdId,sort);
        resultBean.setRows(foodIngredients);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setFlag(true);
        return resultBean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean setFoodIngredient(FoodIngredientDTO foodIngredientDTO) {
        ResultBean resultBean = new ResultBean();
        /* String foodId = foodIngredientDTO.getFoodId();*/

        //修改时已存在的主料食材id
        //String updateMainIngredientIds = foodIngredientDTO.getUpdMainIngredientIds();

        String[] updMainIngIds = null;
        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUpdMainIngredientIds())){
            updMainIngIds = foodIngredientDTO.getUpdMainIngredientIds().split(",");
        }

        //标记的已删除的主料食材id
        //String deleteMainIngredientIds = foodIngredientDTO.getDelMainIngredientIds();

        //主料食材id
        //String[] mainIngredientIds = foodIngredientDTO.getIngredientid();

        //添加主料
        for(int i=0;i<foodIngredientDTO.getIngredientid().length;i++){

            FoodIngredient foodIngredient = new FoodIngredient();

            if(updMainIngIds!=null){
                if(i < updMainIngIds.length){
                    foodIngredient.setId(Integer.parseInt(updMainIngIds[i]));
                }
            }

            //添加
            if(foodIngredientDTO.getSdId()>0){

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getFoodId())){
                    foodIngredient.setFoodId(Integer.parseInt(foodIngredientDTO.getFoodId()));
                }

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getIngredientid()[i])){

                    String[] idAndName = foodIngredientDTO.getIngredientid()[i].split("-");

                    foodIngredient.setIngredientId(Integer.parseInt(idAndName[0]));
                    foodIngredient.setIngredientName(idAndName[1]);

                    //主料食材用量
                    /*String[] mainIngredientCount= foodIngredientDTO.getUseLevelZL();*/
                    foodIngredient.setMetering(Double.valueOf(foodIngredientDTO.getUseLevelZL()[i]));

                    //主料食材计量单位
                    foodIngredient.setMeterUnit("克");

                    //主料食材单价
                    /*String[] mainIngredientUnitPrice = foodIngredientDTO.getUnitPriceZL();*/

                    if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUnitPriceZL())){
                        foodIngredient.setUnitPrice(foodIngredientDTO.getUnitPriceZL()[i]);
                    }

                    //设置类型 1主料 2辅料 3配料 4油 5盐 6糖
                    foodIngredient.setType(1);

                    //设置试点id
                    foodIngredient.setSdId(foodIngredientDTO.getSdId());

                    //底层是修改或添加语句

                    if(!IsEmptyUtils.isEmpty(foodIngredient.getId())){
                        //修改
                        foodIngredientMapper.updateFoodIngredient(foodIngredient);
                    }else{
                        //添加
                        foodIngredientMapper.addFoodIngredient(foodIngredient);
                    }
                }
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.LOGIC_ERROR.getCode());
            }
        }

        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getDelMainIngredientIds())){
            String[] delMainIngIds = foodIngredientDTO.getDelMainIngredientIds().split(",");
            for(String id:delMainIngIds){
                foodIngredientMapper.delFoodIngredientById(Integer.parseInt(id),foodIngredientDTO.getSdId());
            }
        }
        //修改时已存在的辅料食材id
        /*String updateIngredientIds = foodIngredientDTO.getUpdIngredientIds();*/
        String[] updIngIds = null;
        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUpdIngredientIds())){
            updIngIds = foodIngredientDTO.getUpdIngredientIds().split(",");
        }

        //标记的已删除的辅料食材id
        /*String deleteIngredientIds = foodIngredientDTO.getDelIngredientIds();*/
        //辅料食材id
        /*String[] ingredients = foodIngredientDTO.getIngredientidFL();*/

        //添加辅料
        for(int i=0;i<foodIngredientDTO.getIngredientidFL().length;i++){

            FoodIngredient foodIngredient = new FoodIngredient();

            if(updIngIds!=null){
                if(i < updIngIds.length){
                    foodIngredient.setId(Integer.parseInt(updIngIds[i]));
                }
            }

            //添加
            if(foodIngredientDTO.getSdId()>0){

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getFoodId())){

                    foodIngredient.setFoodId(Integer.parseInt(foodIngredientDTO.getFoodId()));

                }

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getIngredientidFL()[i])){

                    String[] idAndName = foodIngredientDTO.getIngredientidFL()[i].split("-");

                    foodIngredient.setIngredientId(Integer.parseInt(idAndName[0]));
                    foodIngredient.setIngredientName(idAndName[1]);

                    //辅料食材用量
                    /*String[] ingredientCount= foodIngredientDTO.getUseLevelFL();*/
                    foodIngredient.setMetering(Double.valueOf(foodIngredientDTO.getUseLevelFL()[i]));

                    //辅料食材计量单位
                    foodIngredient.setMeterUnit("克");

                    //辅料食材单价
                    /*String[] ingredientUnitPrice = foodIngredientDTO.getUnitPriceFL();*/
                    if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUnitPriceFL())){
                        foodIngredient.setUnitPrice(foodIngredientDTO.getUnitPriceFL()[i]);
                    }


                    //设置类型 1主料 2辅料 3配料 4油 5盐 6糖
                    foodIngredient.setType(2);

                    //设置试点id
                    foodIngredient.setSdId(foodIngredientDTO.getSdId());

                    //底层是修改或添加语句
                    if(!IsEmptyUtils.isEmpty(foodIngredient.getId())){
                        //修改
                        foodIngredientMapper.updateFoodIngredient(foodIngredient);
                    }else{
                        //添加
                        foodIngredientMapper.addFoodIngredient(foodIngredient);
                    }

                }
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.LOGIC_ERROR.getCode());
            }
        }

        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getDelIngredientIds())){
            String[] delIngIds = foodIngredientDTO.getDelIngredientIds().split(",");
            for(String id:delIngIds){
                foodIngredientMapper.delFoodIngredientById(Integer.parseInt(id),foodIngredientDTO.getSdId());
            }
        }


        //修改时已存在的配料食材id
        /*String updatePLIngredientIds = foodIngredientDTO.getUpdPLIngredientIds();*/
        String[] updPLIngIds = null;
        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUpdPLIngredientIds())){

            updPLIngIds = foodIngredientDTO.getUpdPLIngredientIds().split(",");

        }
        //标记的已删除的配料食材id
        /*String deletePLIngredientIds = foodIngredientDTO.getDelPLIngredientIds();*/

        //配料食材id
        /*String[] accessoryIds = foodIngredientDTO.getIngredientidPL();*/
        //添加配料
        for(int i=0;i<foodIngredientDTO.getIngredientidPL().length;i++){

            FoodIngredient foodIngredient = new FoodIngredient();

            if(updPLIngIds!=null){

                if(i < updPLIngIds.length){

                    foodIngredient.setId(Integer.parseInt(updPLIngIds[i]));

                }

            }

            //添加
            if(foodIngredientDTO.getSdId()>0){

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getFoodId())){

                    foodIngredient.setFoodId(Integer.parseInt(foodIngredientDTO.getFoodId()));

                }

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getIngredientidPL()[i])){

                    String[] idAndName = foodIngredientDTO.getIngredientidPL()[i].split("-");

                    foodIngredient.setIngredientId(Integer.parseInt(idAndName[0]));
                    foodIngredient.setIngredientName(idAndName[1]);

                    //配料食材用量
                    /*String[] accessoryCount= foodIngredientDTO.getUseLevelPL();*/
                    foodIngredient.setMetering(Double.valueOf(foodIngredientDTO.getUseLevelPL()[i]));

                    //配料食材计量单位
                    foodIngredient.setMeterUnit("克");

                    //配料食材单价
                    /*String[] accessoryUnitPrice = foodIngredientDTO.getUnitPricePL();*/
                    if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUnitPricePL())){
                        foodIngredient.setUnitPrice(foodIngredientDTO.getUnitPricePL()[i]);
                    }

                    //设置类型 1主料 2辅料 3配料 4油 5盐 6糖
                    foodIngredient.setType(3);

                    //设置试点id
                    foodIngredient.setSdId(foodIngredientDTO.getSdId());

                    //底层是修改或添加语句
                    if(!IsEmptyUtils.isEmpty(foodIngredient.getId())){
                        //修改
                        foodIngredientMapper.updateFoodIngredient(foodIngredient);
                    }else{
                        //添加
                        foodIngredientMapper.addFoodIngredient(foodIngredient);
                    }
                }
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.LOGIC_ERROR.getCode());
            }


        }

        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getDelPLIngredientIds())){

            String[] delPLIngIds = foodIngredientDTO.getDelPLIngredientIds().split(",");

            for(String id:delPLIngIds){
                foodIngredientMapper.delFoodIngredientById(Integer.parseInt(id),foodIngredientDTO.getSdId());
            }

        }


        //修改时已存在的调味品食材id
        /*String updTWPIngredientIds = foodIngredientDTO.getUpdTWPIngredientIds();*/
        String[] updTWPIngIds = null;
        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUpdTWPIngredientIds())){

            updTWPIngIds = foodIngredientDTO.getUpdTWPIngredientIds().split(",");

        }
        //标记的已删除的调味品食材id
        /*String delTWPIngredientIds = foodIngredientDTO.getDelTWPIngredientIds();*/

        //调味品食材id
        /*String[] accessoryTWIds = foodIngredientDTO.getIngredientidTWP();*/

        //添加调味品
        for(int i=0;i<foodIngredientDTO.getIngredientidTWP().length;i++){

            FoodIngredient foodIngredient = new FoodIngredient();

            if(updTWPIngIds!=null){

                if(i < updTWPIngIds.length){

                    foodIngredient.setId(Integer.parseInt(updTWPIngIds[i]));

                }

            }

            //添加
            if(foodIngredientDTO.getSdId()>0){

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getFoodId())){

                    foodIngredient.setFoodId(Integer.parseInt(foodIngredientDTO.getFoodId()));

                }

                if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getIngredientidTWP()[i])){

                    String[] idAndName = foodIngredientDTO.getIngredientidTWP()[i].split("-");

                    foodIngredient.setIngredientId(Integer.parseInt(idAndName[0]));
                    foodIngredient.setIngredientName(idAndName[1]);

                    //配料食材用量
                    /* String[] accessoryCount= foodIngredientDTO.getUseLevelTWP();*/
                    foodIngredient.setMetering(Double.valueOf(foodIngredientDTO.getUseLevelTWP()[i]));

                    //配料食材计量单位
                    foodIngredient.setMeterUnit("克");

                    //配料食材单价
                    if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getUnitPriceTWP())){
                        String[] accessoryUnitPrice = foodIngredientDTO.getUnitPriceTWP();
                        foodIngredient.setUnitPrice(accessoryUnitPrice[i]);
                    }


                    //设置类型 1主料 2辅料 3配料 4调味品   油 5盐 6糖
                    foodIngredient.setType(4);

                    //设置试点id
                    foodIngredient.setSdId(foodIngredientDTO.getSdId());

                    //底层是修改或添加语句
                    //底层是修改或添加语句
                    if(!IsEmptyUtils.isEmpty(foodIngredient.getId())){
                        //修改
                        foodIngredientMapper.updateFoodIngredient(foodIngredient);
                    }else{
                        //添加
                        foodIngredientMapper.addFoodIngredient(foodIngredient);
                    }

                }

            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.LOGIC_ERROR.getCode());
            }


        }

        if(!IsEmptyUtils.isEmpty(foodIngredientDTO.getDelTWPIngredientIds())){

            String[] delPLIngIds = foodIngredientDTO.getDelTWPIngredientIds().split(",");

            for(String id:delPLIngIds){
                foodIngredientMapper.delFoodIngredientById(Integer.parseInt(id),foodIngredientDTO.getSdId());
            }

        }

        resultBean.setMsg("操作成功!");
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getByByOutFoodId(int foodId) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> foodIngredients = foodIngredientMapper.findByOutFoodId(foodId);
        resultBean.setRows(foodIngredients);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setFlag(true);
        return resultBean;
    }
}
