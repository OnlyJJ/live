package com.jiujun.shows.dynamic.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.dynamic.service.banner.IDiaryBannerService;
import com.jiujun.shows.dynamic.vo.DataRequest;
import com.jiujun.shows.dynamic.vo.DiaryBannerVo;
import com.jiujun.shows.framework.service.ServiceResult;

/**
 * 动态banner控制层
 * @author shao.xiang
 * @date 2017年6月21日
 *
 */
@Controller("DiaryBannerWeb")
public class DiaryBannerWeb {
	@Resource
	private IDiaryBannerService diaryBannerService;
	
	/**
	 * 获取动态banner
	 * @param data
	 * @return
	 */
	public JSONObject getSysConf(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0);
		JSONObject jsonRes = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			ServiceResult<List<DiaryBannerVo>> srt = diaryBannerService.getDiaryBannerList();
			if(srt.isSucceed()) {
				List<DiaryBannerVo> list = srt.getData();
				if(list != null && list.size() >0) {
					for(DiaryBannerVo vo : list) {
						array.add(vo.buildJson());
					}
					jsonRes.put("list",array.toString());
				}
			} else {
				result.setResultCode(srt.getResultCode());
				result.setResultDescr(srt.getResultMsg());
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result = new Result(ErrorCode.ERROR_2008);
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
}
