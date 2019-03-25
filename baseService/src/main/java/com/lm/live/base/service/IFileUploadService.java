package com.lm.live.base.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.framework.service.ServiceResult;

/**
 * 文件上传服务
 * @author shao.xiang
 * @date 2017-06-02
 */
public interface IFileUploadService {
	
	/**
	 * F1 上传文件（通用，如头像等）
	 * @param data
	 * @return
	 */
	public ServiceResult<JSONObject> uploadFile(HttpServletRequest request, String userId);
	
	/**
	 * F2-动态图片上传接口
	 * @param data
	 * @return
	 */
	public ServiceResult<JSONObject> uploadFile2(HttpServletRequest request, String userId);
	
	/**
	 * F3-举报图片上传接口
	 * @param data
	 * @return
	 */
	public ServiceResult<JSONObject> uploadImgs(HttpServletRequest request, String userId);
	
}
