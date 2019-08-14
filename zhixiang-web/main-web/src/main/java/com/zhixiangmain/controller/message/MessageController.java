package com.zhixiangmain.controller.message;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.api.module.message.vo.MessageVO;
import com.zhixiangmain.api.service.message.MessageService;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.controller.message
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-03 12:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    private static final Logger logger = LoggerFactory
            .getLogger(MessageController.class);
    @Reference(version = "1.0.0")
    private MessageService messageService;

    /**
     * 发送短信验证码
     * @param sdId 试点
     * @return
     */
    @PostMapping(value = "getMessages")
    @ResponseBody
    public ResultBean sendMessage(@RequestParam("sdId") String sdId) {
        logger.debug("获取消息！sdId:" + sdId);
        ResultBean resultBean = new ResultBean();
        try {
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg("获取消息成功");
            List<MessageVO> messages = Lists.newArrayList();
            for(int i=0;i<10;i++){

                if(i==1||i==3||i==5){
                    MessageVO messageVO = new MessageVO();
                    messageVO.setContent("支付宝到账:5元");
                    messageVO.setFromUserIcon("../images/Lv60chaungguan.png");
                    messageVO.setFromUserName("hhp");
                    messages.add(messageVO);
                }else if(i==2){
                    MessageVO messageVO = new MessageVO();
                    messageVO.setContent("您有一笔新的交易款项:5元");
                    messageVO.setFromUserIcon("../images/blackvip6.png");
                    messageVO.setFromUserName("智飨科技");
                    messages.add(messageVO);
                }else if(i==7){
                    MessageVO messageVO = new MessageVO();
                    messageVO.setContent("您的快递已签收:门卫");
                    messageVO.setFromUserIcon("../images/vip6.png");
                    messageVO.setFromUserName("hg");
                    messages.add(messageVO);
                }else{
                    MessageVO messageVO = new MessageVO();
                    messageVO.setContent("新的消息提示");
                    messageVO.setFromUserIcon("../images/hx.png");
                    messageVO.setFromUserName("智飨科技");
                    messages.add(messageVO);
                }

            }

            resultBean.setData(messages);
            logger.error("获取消息成功！");
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg("获取消息失败，请您稍后再试");
            logger.error("获取消息异常！", e);
        }
        logger.debug("获取消息，结果=resultBean:" + resultBean);
        return resultBean;
    }

    /**
     *  暂时只获取提交审核的预采购订单信息
     * @param sdId 试点
     * @return
     */
    @PostMapping(value = "getAllShowMSG")
    @ResponseBody
    public ResultBean getAllShowMSG(@RequestParam(value = "sdId",required = false) String sdId) {
        logger.debug("获取信息！");
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        String path = MessageController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = messageService.getAllShowMSG(existUser.getId(),sdId,jobj);
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            logger.error("获取提交的未审核的采购单异常！", e);
        }
        return resultBean;
    }
}
