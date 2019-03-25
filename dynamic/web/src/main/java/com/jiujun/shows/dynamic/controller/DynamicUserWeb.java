package com.jiujun.shows.dynamic.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.IpUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.service.home.IDiaryInfoService;
import com.jiujun.shows.dynamic.service.user.IDiaryCommentService;
import com.jiujun.shows.dynamic.service.user.IDiaryFavourateService;
import com.jiujun.shows.dynamic.vo.AccusationVo;
import com.jiujun.shows.dynamic.vo.CommentVo;
import com.jiujun.shows.dynamic.vo.DataRequest;
import com.jiujun.shows.dynamic.vo.DynamicInfoVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态业务控制层
 * @author shao.xiang
 * @date 2017年6月2日
 *
 */
@Controller("DynamicUserWeb")
public class DynamicUserWeb {
	
	@Resource
	private IDiaryFavourateService diaryFavourateService;
	
	@Resource
	private IDiaryCommentService diaryCommentService;
	
	@Resource
	private IDiaryInfoService diaryInfoService;
	
	/**
	 * D3
	 * 用户点赞或踩相关业务处理方法
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-08
	 * @return
	 */
	public JSONObject handleUserLoveOrHate(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null
					|| data.getDynamicInfoVo() == null
					|| data.getFavourateVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			int type = data.getFavourateVo().getType();
			long diaryInfoId = data.getDynamicInfoVo().getDynamicId();
			ServiceResult<Boolean> srt = diaryFavourateService.handleUserLoveOrHate(diaryInfoId, userId, type);
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
	 * D4
	 * 发表评论
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-08
	 * @return
	 */
	public JSONObject publishComment(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null
					|| data.getDynamicInfoVo() == null
					|| data.getCommentVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String pubIp = IpUtils.getClientIp(data.getRequest());
			int appType = 0;
			if(data.getDeviceProperties() != null) {
				appType = data.getDeviceProperties().getAppType();
			}
			String userId = data.getUserBaseInfo().getUserId();
			DynamicInfoVo dynamicInfoVo = data.getDynamicInfoVo();
			long diaryInfoId = dynamicInfoVo.getDynamicId();
			CommentVo commentVo = data.getCommentVo();
			String content = commentVo.getContent();
			long toCommentId = commentVo.getToCommentId();
			int commentType = commentVo.getCommentType();
			ServiceResult<Boolean> srt = diaryCommentService.publishComments(diaryInfoId, userId, content, appType, pubIp, toCommentId,commentType);
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
	 * D5
	 * 获取某一动态的所有评论
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-09
	 * @return
	 */
	public JSONObject getDiaryCommentData(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getPage() == null
					|| data.getDynamicInfoVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			DeviceProperties pros = data.getDeviceProperties();
			Page page = data.getPage();
			long diaryInfoId = data.getDynamicInfoVo().getDynamicId();
			// 从缓存中获取动态所有评论，每次评论完后，追加至该缓存，并更新
			ServiceResult<JSONObject> srt = diaryCommentService.getDiaryCommentByPage(diaryInfoId, page);
			if(srt.isSucceed()) {
				jsonRes.put("comment", srt.getData());
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
	 * D8
	 * 删除评论
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-09
	 * @return
	 */
	public JSONObject deleteDiaryCommet(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null
					|| data.getDynamicInfoVo() == null
					|| data.getCommentVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			long diaryInfoId = data.getDynamicInfoVo().getDynamicId();
			long commentId = data.getCommentVo().getCommentId();
			ServiceResult<Boolean> srt = diaryCommentService.deleteDiaryCommet(diaryInfoId, commentId, userId);
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
	 * D9
	 * 举报动态或评论
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-09
	 * @return
	 */
	public JSONObject reportDiaryInfoOrCommet(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null
					|| data.getAccusationVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			DeviceProperties pros = data.getDeviceProperties();
			String ip = IpUtils.getClientIp(data.getRequest());
			String userId = data.getUserBaseInfo().getUserId();
			AccusationVo vo = data.getAccusationVo();
			int type = vo.getType();
			long diaryInfoId = vo.getDiaryInfoId();
			String content = "";
			if(vo.getContent() != null) {
				content = vo.getContent();
			}
			ServiceResult<Boolean> srt = null;
			if(type == 1) { // 举报动态
				srt = diaryInfoService.reportDiaryInfo(diaryInfoId, userId, content, ip);
			} else if(type == 2) { // 举报动态评论
				long commentId = vo.getCommentId();
				srt = diaryCommentService.reportDiaryComment(diaryInfoId, commentId, userId, content);
			}
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
	 * D10
	 * 获取用户消息，评论和回复评论消息
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-10
	 * @return
	 */
	public JSONObject getUserDiaryMSG(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getPage() == null
					|| data.getUserBaseInfo().getUserId() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			Page page = data.getPage();
			ServiceResult<JSONObject> srt = diaryCommentService.getUserDiaryMSG(userId, page);
			if(srt.isSucceed()) {
				jsonRes.put("usermsg", srt.getData());
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
	 * D11
	 * 情空个人所有消息，即设置readFlag为2
	 * @param data
	 * @author shao.xiang
	 * @date 2017-07-11
	 * @return
	 */
	public JSONObject deleteUserDiaryAllMSG(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getUserBaseInfo().getUserId() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			String userId = data.getUserBaseInfo().getUserId();
			ServiceResult<Boolean> srt = diaryCommentService.deleteUserDiaryAllMSG(userId);
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
	 * D14
	 * 获取某一动态点赞用户信息，分页数据
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public JSONObject getDiaryPrizeData(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getPage() == null
					|| data.getDynamicInfoVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			DeviceProperties pros = data.getDeviceProperties();
			Page page = data.getPage();
			long diaryInfoId = data.getDynamicInfoVo().getDynamicId();
			// 从缓存中获取点赞用户，每次点赞后，追加该缓存
			ServiceResult<JSONObject> srt = diaryFavourateService.getDiaryFavourateData(diaryInfoId, page);
			if(srt.isSucceed()) {
				jsonRes.put("users", srt.getData());
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
	 * D15
	 * 处理用户动态消息列表中，某条消息是否已读标记
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年7月12日
	 */
	public JSONObject handleUserDiaryMsgReadFlag(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data == null || data.getUserBaseInfo() == null
					|| data.getDynamicInfoVo() == null) {
				result.setResultCode(ErrorCode.ERROR_8015.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_8015.getResultDescr());
			}
			DeviceProperties pros = data.getDeviceProperties();
			String userId = data.getUserBaseInfo().getUserId();
			long diaryInfoId= data.getDynamicInfoVo().getDynamicId();
//			long commentId = data.getCommentVo().getCommentId();
			ServiceResult<Boolean> srt = diaryCommentService.updateCommentMsgFlag(userId, diaryInfoId);
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
}
