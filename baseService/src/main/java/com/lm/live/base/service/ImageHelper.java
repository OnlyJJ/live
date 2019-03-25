package com.lm.live.base.service;

import com.alibaba.fastjson.JSONObject;

/***
 * 图片操作服务
 * @author sx
 * @since 2017-06-2
 * 
 */
public interface ImageHelper {
	
	/**
	 * 图片处理
	 * @param userId
	 * @param diaryInfoId 动态id
	 * @param ratioIndex 图片序号
	 * @param tempFile 临时文件地址
	 */
	public JSONObject creatFile(String userId, long diaryInfoId, int ratioIndex, String tempFile);
	
	/**
	 * 举报图片处理
	 * @param userId
	 * @param aiId
	 * @param ratioIndex
	 * @param tempImg 临时上传图片
	 * @return 返回参数说明：<br>
	 * 		JSONObject，{"img":"xxx"}，img为处理后的图片地址
	 */
	public JSONObject accusationImgDispose(String userId, long aiId, int ratioIndex, String tempImg);
	
	/**
	 * 删除重复举报时上传到临时目录的图片
	 * @param tempImg
	 */
	public void deleteAccusationImg(String tempImg);
}
