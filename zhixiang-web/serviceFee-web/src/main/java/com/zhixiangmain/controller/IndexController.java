package com.zhixiangmain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @项目名称：lyd-channel
 * @包名：com.lyd.channel.web
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-11-28 18:52
 * @version：V1.0
 */
@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger logger = LoggerFactory
			.getLogger(IndexController.class);
	@RequestMapping("/") public String transIndex() {
		return "home";
	}
	@RequestMapping("/index") public String index() {
		return "home";
	}
	@RequestMapping("/home") public String toHome() {
		return "index";
	}
	@RequestMapping("/login")
	public String toLogin() {
		return "login";
	}
	@RequestMapping("/toLogin")
	public String toTrueLogin() {
		return "toLogin";
	}
	@RequestMapping("/errorPage")
	public String toError(HttpServletRequest request) {
		/*Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		} else {
			try {
				return HttpStatus.valueOf(statusCode);
			} catch (Exception var4) {
				return HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
*/
		return "error";
	}
}
