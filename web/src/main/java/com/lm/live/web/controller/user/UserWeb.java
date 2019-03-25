package com.lm.live.web.controller.user;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.service.IIpStoreService;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.vo.DeviceProperties;
import com.lm.live.common.vo.Page;
import com.lm.live.common.vo.Result;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.user.enums.ErrorCode;
import com.lm.live.user.exception.UserBizException;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;
import com.lm.live.web.vo.DataRequest;

/**
 * 用户服务
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
@Controller("UserWeb")
public class UserWeb extends BaseController {
	
	@Resource
	private IIpStoreService ipStoreService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	/**
	 * U1
	 * 用户个人资料（需登录）
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月7日
	 */
	@RequestMapping(value = {"U1/{q}"} , method= {RequestMethod.POST})
	public void getUserInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			String userId = userbase.getUserId();
			UserInfo vo = userInfoService.getUserDetailInfo(userId);
			jsonRes.put(vo.getShortName(), vo.buildJson());
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * U2
	 * 获取用户在房间的基本信息，无需登录
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	@RequestMapping(value = {"U2/{q}"} , method= {RequestMethod.POST})
	public void getUserBaseInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			String userId = userbase.getUserId();
			UserInfo vo = userInfoService.getUserInfo(userId);
			if(vo != null) {
				jsonRes.put(vo.getShortName(), vo.buildJson());
			}
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}

	/**
	 * 获取关注列表
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	@RequestMapping(value = {"U3/{q}"} , method= {RequestMethod.POST})
	public void getAttentionData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(DeviceProperties.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(Page.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(UserBaseInfo.class.getSimpleName().toLowerCase())) {
				throw new UserBizException(ErrorCode.ERROR_101);
			}
			DeviceProperties dv = new DeviceProperties();
			dv.parseJson(data.getData().getJSONObject(dv.getShortName()));
			UserBaseInfo userbase = new UserBaseInfo();
			userbase.parseJson(data.getData().getJSONObject(userbase.getShortName()));
			Page page = new Page();
			page.parseJson(data.getData().getJSONObject(page.getShortName()));
			String userId = userbase.getUserId();
			UserInfo vo = userInfoService.getUserInfo(userId);
			if(vo != null) {
				jsonRes.put(vo.getShortName(), vo.buildJson());
			}
		} catch(UserBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
}
