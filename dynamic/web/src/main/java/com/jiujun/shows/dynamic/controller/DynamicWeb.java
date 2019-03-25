package com.jiujun.shows.dynamic.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.IpUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoService;
import com.jiujun.shows.dynamic.vo.DataRequest;
import com.jiujun.shows.dynamic.vo.PublishVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态业务控制层
 * @author shao.xiang
 * @date 2017年6月2日
 *
 */
@Controller("DynamicWeb")
public class DynamicWeb {
	
	@Resource
	private IDiaryInfoService diaryInfoService;
	
	/**
	 * D1
	 * 获取动态首页信息
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月2日
	 */
	public JSONObject getDynamicInfosData(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getPage() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			Page page = data.getPage();
			ServiceResult<JSONObject> srt = diaryInfoService.getDynamicInfos(page);
			if(srt.isSucceed()) {
				jsonRes.put("diaryInfo", srt.getData());
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_8000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * D2
	 * 发布动态接口
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月2日
	 */
	public JSONObject publishDynamicInfo(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getPublishVo() == null
					|| data.getAnchorInfo() == null
					|| data.getUserBaseInfo().getUserId() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String pubIp = IpUtils.getClientIp(data.getRequest());
			int appType = 0;
			if(data.getDeviceProperties() != null) {
				appType = data.getDeviceProperties().getAppType();
			}
			String userId = data.getUserBaseInfo().getUserId();
			String isAnchor = data.getAnchorInfo().getIsAnchor();
			PublishVo vo = data.getPublishVo();
			ServiceResult<Boolean> srt = diaryInfoService.publishDynamic(userId, isAnchor,pubIp, appType,vo);
			if(!srt.isSucceed()) {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_8000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * D6
	 * 获取个人所有动态信息
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月5日
	 */
	public JSONObject getUserDiaryInfoData(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getAnchorInfo() == null
					|| data.getAnchorInfo().getUserId() == null
					|| data.getPage() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			DeviceProperties pros = data.getDeviceProperties();
			Page page = data.getPage();
			String userId = data.getAnchorInfo().getUserId();
			ServiceResult<JSONObject> srt = diaryInfoService.getUserAllDiaryInfos(userId, page);
			if(srt.isSucceed()) {
				jsonRes.put("list", srt.getData());
			}
		}catch (SystemDefinitionException e) {
			LogUtil.log.error(e.getMessage() ,e);
			result = new Result(e.getErrorCode());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_8000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * D7
	 * 删除动态
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年6月6日
	 */
	public JSONObject deleteDiaryInfo(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null
					|| data.getDynamicInfoVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			long diaryInfoId = data.getDynamicInfoVo().getDynamicId();
			ServiceResult<Boolean> srt = diaryInfoService.deleteDiaryInfo(diaryInfoId, userId);
			if(!srt.isSucceed()) {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_8000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}

	/**
	 * D12
	 * 用户发布动态前，权限校验
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月6日
	 */
	public JSONObject publishDiaryAccess(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			diaryInfoService.publishDiaryAccess(userId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_8000.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		return jsonRes; 
	}
}
