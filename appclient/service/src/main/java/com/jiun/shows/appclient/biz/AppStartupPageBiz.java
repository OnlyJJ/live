package com.jiun.shows.appclient.biz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.user.domain.UserAnchor;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserAnchorService;
import com.jiujun.shows.user.service.IUserCacheInfoService;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiujun.shows.user.vo.UserInfoVo;
import com.jiun.shows.appclient.dao.AppStartupPageMapper;
import com.jiun.shows.appclient.domain.AppStartupPage;
import com.jiun.shows.appclient.enums.AppStartupPageTableEnum;
import com.jiun.shows.appclient.enums.AppStartupPageTableEnum.ThemeType;
import com.jiun.shows.appclient.vo.AnchorInfo;
import com.jiun.shows.appclient.vo.AppStartupPageVo;


/**
 * app开机页业务
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appStartupPageBiz")
public class AppStartupPageBiz {

	
	@Resource(name="appStartupPageDao")
	private AppStartupPageMapper appStartupPageMapper;
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	
	public AppStartupPageVo getAppStartupPage() throws Exception {
		AppStartupPageVo appStartupPageVo = new AppStartupPageVo();
		// 获取使用启用的且在使用时间段内开机配置
		AppStartupPage appStartupPage = appStartupPageMapper.getInuseConf();
		LogUtil.log.info("###db中开机页面配置:"+JsonUtil.beanToJsonString(appStartupPage));
		if(appStartupPage != null){
			int themeType = appStartupPage.getThemeType();
			int jumpType = appStartupPage.getJumpType();
			String jumpTarget = appStartupPage.getJumpTarget();
			
			// 返回的图片地址
			String imgArrStr = null;
			int startupPageId = appStartupPage.getId();
			// 依据逗号切分图片配置字符串成图片数组
			List<String> mediaUrls = appStartupPageMapper.findStartupPageMedia(startupPageId);
			if(mediaUrls != null && mediaUrls.size() > 0){
				List<String> retMediaUrls = new ArrayList<String>();
				// themeType是单张图片的,只取第一张
				if(themeType == ThemeType.SingleImg_Auto_Marquee.getValue() || themeType == ThemeType.SingleImg_No_Animation.getValue()){
					retMediaUrls.add(mediaUrls.get(0));
				}else{
					retMediaUrls = mediaUrls;
				}
				imgArrStr = JsonUtil.arrayToJsonString(retMediaUrls);
			}
			appStartupPageVo.setThemeType(themeType);
			appStartupPageVo.setJumpType(jumpType);
			appStartupPageVo.setJumpTarget(jumpTarget);
			appStartupPageVo.setImgArr(imgArrStr);
			
			// 房间跳转: 返回主播相关信息
			if(jumpType == AppStartupPageTableEnum.JumpType.Jump_Room.getValue()){
				AnchorInfo  anchorInfo =  new AnchorInfo();
				String roomId = jumpTarget;
				UserAnchor anchor = userAnchorService.getUserAnchorByRoomId(roomId);
				if(anchor != null){
					String userId = anchor.getUserId();
					UserInfo userInfo = userInfoService.getUserByUserId(userId);
					UserInfoVo userAcc = userCacheInfoService.getInfoFromCache(userId, null);
					anchorInfo.setRoomId(roomId);
					anchorInfo.setUserId(userId);
					anchorInfo.setAttentionCount(anchor.getFansCount()+"");
					anchorInfo.setIcon(userInfo.getIcon());
					anchorInfo.setNickName(userInfo.getNickName()) ;
					anchorInfo.setUserLevel(userAcc.getUserLevel());
					anchorInfo.setAnchorLevel(userAcc.getAnchorLevel());
					JSONObject json = anchorInfo.buildJson();
					appStartupPageVo.setAnchorInfoJsonStr(JsonUtil.beanToJsonString(json));
				}
				
			}
		}else{
			LogUtil.log.info("###没有启用中的开机配置...");
		}
		return appStartupPageVo;			
	}

}
