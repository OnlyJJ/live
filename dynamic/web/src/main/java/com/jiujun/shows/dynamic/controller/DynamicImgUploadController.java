package com.jiujun.shows.dynamic.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.base.service.IFileUploadService;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.vo.DataRequest;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态图片上传入口
 * @author shao.xiang
 * @date 2017-06-03
 *
 */
@Controller("DynamicImgUploadController")
public class DynamicImgUploadController {
	
	@Resource
	private IFileUploadService FileUploadService;
	
	/**
	 * F2-动态图片上传接口
	 * @param data
	 * @return
	 */
	public JSONObject uploadFile2(DataRequest data) {
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		if(data != null && data.getUserBaseInfo() != null 
				&& data.getUserBaseInfo().getUserId() != null) {
			HttpServletRequest request = data.getRequest();
			String userId = data.getUserBaseInfo().getUserId();
			ServiceResult<JSONObject> ret = FileUploadService.uploadFile2(request, userId);
			if(ret.isSucceed()) {
				json = ret.getData();
			} else {
				result.setResultCode(ret.getResultCode());
				result.setResultDescr(ret.getResultMsg());
				
			}
		}
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
	/**
	 * F3-举报图片上传接口
	 * @param data
	 * @return
	 */
	public JSONObject uploadImgs(DataRequest data) {
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		if(data != null && data.getUserBaseInfo() != null 
				&& data.getUserBaseInfo().getUserId() != null) {
			HttpServletRequest request = data.getRequest();
			String userId = data.getUserBaseInfo().getUserId();
			ServiceResult<JSONObject> ret = FileUploadService.uploadImgs(request, userId);
			if(ret.isSucceed()) {
				json = ret.getData();
			} else {
				result.setResultCode(ret.getResultCode());
				result.setResultDescr(ret.getResultMsg());
			}
		}
		json.put(result.getShortName(), result.buildJson());
		return json;
	}
	
}
