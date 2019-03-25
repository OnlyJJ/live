package com.jiujun.shows.dynamic.service.other;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.IMutils;
import com.jiujun.shows.common.utils.JsonUtil;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.SensitiveWordUtil;
import com.jiujun.shows.dynamic.enums.IMBusinessEnum;
import com.jiujun.shows.dynamic.enums.IMBusinessEnum.ImType21007Enum;
import com.jiujun.shows.dynamic.enums.IMBusinessEnum.ImTypeEnum;
import com.jiujun.shows.dynamic.enums.IMBusinessEnum.MsgTypeEnum;
import com.jiujun.shows.dynamic.enums.IMBusinessEnum.SeqID;

/**
 * 发出im消息的spring组件
 *
 */
@Component
public class ImSendComponent {
	
	/**
	 * 发送红包雨消息倒计时开始的消息,通知客户端开始倒计时
	 * @param roomId 房间号
	 * @param countdownSecond 剩余倒计时(秒)
	 */
	public void sendImMsgForHongbaoyuCountdown(String roomId,int countdownSecond) throws Exception{
		LogUtil.log.info(String.format("###begin-红包雨消息sendImMsgForHongbaoyuCountdown,roomId:%s,countdownSecond:%s", roomId,countdownSecond));
		if(StringUtils.isEmpty(roomId) || countdownSecond <= 0) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		//红包雨data.type
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_Hongbaoyu.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = roomId;
		JSONObject dataContent = new JSONObject();
		dataContent.put("type", "countdown"); //红包雨:启用倒计时
		dataContent.put("countdownSecond", countdownSecond);
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", dataContent);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
		LogUtil.log.info(String.format("###end-红包雨消息sendImMsgForHongbaoyuCountdown,roomId:%s,countdownSecond:%s", roomId,countdownSecond));
	}
	
	/**
	 * 红包雨潇潇,通知客户端取消倒计时 <br />
	 * 如:停播
	 * @param roomId 房间号
	 */
	public void sendImMsgForHongbaoyuCancelCountdown(String roomId) throws Exception{
		LogUtil.log.info(String.format("###begin-红包雨消息sendImMsgForHongbaoyuCancelCountdown,roomId:%s", roomId));
		if(StringUtils.isEmpty(roomId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		//红包雨data.type
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_Hongbaoyu.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = roomId;
		JSONObject dataContent = new JSONObject();
		dataContent.put("type", "cancelCountdown"); //红包雨:取消倒计时
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", dataContent);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
		LogUtil.log.info(String.format("###end-红包雨消息sendImMsgForHongbaoyuCancelCountdown,roomId:%s", roomId));
	}

	/**
	 * 发送大喇叭消息
	 * @param dalabaMsg
	 * @param dalabaMsgColor
	 * @param senderUserId
	 * @param userLevel
	 * @param sourceRoomId
	 * @param anchorId
	 * @param anchorLevel
	 * @param attentionCount
	 * @param anchorNickname
	 * @param isAutomatic //是否自动(系统)发送：y/n
	 * @throws Exception
	 */
	public void sendDalaba(String dalabaMsg, String dalabaMsgColor, String senderUserId, String userLevel, String sourceRoomId, 
			String anchorId, String anchorLevel, String attentionCount, String anchorNickname, String isAutomatic) throws Exception{
		if (StringUtils.isEmpty(dalabaMsg) || StringUtils.isEmpty(senderUserId) || StringUtils.isEmpty(sourceRoomId)
				|| StringUtils.isEmpty(anchorId) || StringUtils.isEmpty(anchorNickname)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		String wholeSiteNoticeRoomId = BaseConstants.WHOLE_SITE_NOTICE_ROOMID;
		JSONObject imAllDataBodyJson = new JSONObject();
		imAllDataBodyJson.put("funID", IMBusinessEnum.FunID.FUN_11001.getValue());
		imAllDataBodyJson.put("seqID", IMBusinessEnum.SeqID.SEQ_1.getValue());
		JSONObject imMsgJsonData = new JSONObject() ;
		imMsgJsonData.put("msgtype", 2);
		imMsgJsonData.put("targetid", wholeSiteNoticeRoomId);
		imMsgJsonData.put("type", IMBusinessEnum.ImTypeEnum.IM_11001_dalaba.getValue());
		JSONObject content = new JSONObject() ;
		content.put("roomId", sourceRoomId); // 发喇叭房间
		content.put("anchorId", anchorId);
		content.put("attentionCount",attentionCount);
		content.put("nickname", anchorNickname);
		content.put("userLevel", userLevel);
		content.put("anchorLevel", anchorLevel);
		content.put("msg", SensitiveWordUtil.replaceSensitiveWord(dalabaMsg));
		content.put("msgColor", dalabaMsgColor);
		content.put("isAutomatic", isAutomatic);
		imMsgJsonData.put("content", content);
		imAllDataBodyJson.put("data", imMsgJsonData);
		LogUtil.log.info(String.format("###begin-发送大喇叭senderUserId:%s,消息体:%s", senderUserId,JsonUtil.beanToJsonString(imAllDataBodyJson))) ;
		IMutils.sendMsg2IM(imAllDataBodyJson, senderUserId);
		LogUtil.log.info(String.format("###end-发送大喇叭senderUserId:%s,消息体:%s", senderUserId,JsonUtil.beanToJsonString(imAllDataBodyJson))) ;

	}
	
	/**
	 * 发送暖冬计划消息201701
	 * @param roomId 通知房间号
	 * @param content 
	 */
	public void sendImMsgForNuandongjihua2017(String roomId,JSONObject content) throws Exception{
		if(StringUtils.isEmpty(roomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		LogUtil.log.info(String.format("###begin-暖冬计划消息sendImMsgForNuandongjihua2017,roomId:%s,content:%s", roomId,JsonUtil.beanToJsonString(content)));
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		//暖冬计划data.type
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_ALLROOMMSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = roomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
		LogUtil.log.info(String.format("###end-暖冬计划消息sendImMsgForNuandongjihua2017,roomId:%s,jsonIm:%s", roomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	/**
	 * 发送暖冬计划改变背景消息
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendImMsgForNuandongjihua2017ChangeBackGroud(String roomId,JSONObject content) throws Exception{
		if(StringUtils.isEmpty(roomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		LogUtil.log.info(String.format("###begin-暖冬计划消息sendImMsgForNuandongjihua2017ChangeBackGroud,roomId:%s,content:%s", roomId,JsonUtil.beanToJsonString(content)));
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		//暖冬计划data.type
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_Nuandongjihua2017.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = roomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
		LogUtil.log.info(String.format("###end-暖冬计划消息sendImMsgForNuandongjihua2017ChangeBackGroud,roomId:%s,jsonIm:%s", roomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	
	/**
	 * 2017年2月情人节活动-发送im通知
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendImMsgFor201702Qingrenjie(String notifyRoomId,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-2017年2月情人节活动-发送im通知,roomId:[%s],content:[%s]", notifyRoomId,JsonUtil.beanToJsonString(content)));
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_ALLROOMMSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送情人节活动im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
			// throw e;
		}
		LogUtil.log.info(String.format("###end-2017年2月情人节活动-发送im通知,roomId:[%s],jsonIm:[%s]", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	/**
	 * 2017年2月情人节活动-表白成功,触发全站守护特效（下花瓣）
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendImMsgFor201702QingrenjieBiaobaiSuccessTexiao(String notifyRoomId,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-2017年2月情人节活动-表白成功,触发全站守护特效（下花瓣）,roomId:[%s],content:[%s]", notifyRoomId,JsonUtil.beanToJsonString(content)));
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_201702Qingrenjie.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送情人节活动表白成功im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
			//throw e;
		}
		LogUtil.log.info(String.format("###end-2017年2月情人节活动-表白成功,触发全站守护特效（下花瓣）,roomId:[%s],jsonIm:[%s]", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	/**
	 * 发送im消息（11001.聊天消息）
	 * @param seqID 
	 * @param msgTypeEnum 单聊/群聊
	 * @param targetid 目标用户ID/房间号
	 * @param imTypeEnum 消息类型 ，1：文本，2：图片，3：声音，4：视频，5.礼物,6禁言,7踢人 8.红包 9.赠送坐驾 10.蜜桃礼物……
	 * @param content 发送内容
	 * @throws Exception
	 */
	public void sendFun11001Msg2Im(SeqID seqID,MsgTypeEnum msgTypeEnum,String targetid,ImTypeEnum imTypeEnum,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-发送11001聊天消息,seqID:[%s],msgTypeEnum:[%s],imTypeEnum:%s,targetid:%s,content:%s", seqID.getValue(),msgTypeEnum.getValue(),imTypeEnum.getValue(),targetid,JsonUtil.beanToJsonString(content)));
		
		if(StringUtils.isEmpty(targetid) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		
		int seqIDInt = seqID.getValue() ;
		
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		
		JSONObject data = new JSONObject();
		//群聊 or 单聊
		int dataMsgType = msgTypeEnum.getValue();
		//消息类型
		int dataType = imTypeEnum.getValue();
		//房间id
		String dataTargetid = targetid;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqIDInt);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###-发送11001聊天消息发生异常,seqID:[%s],msgTypeEnum:[%s],imTypeEnum:%s,targetid:%s,jsonIm:%s", seqID.getValue(),msgTypeEnum.getValue(),imTypeEnum.getValue(),targetid,JsonUtil.beanToJsonString(jsonIm)));
			throw e;
		}
		LogUtil.log.info(String.format("###end-发送11001聊天消息,seqID:[%s],msgTypeEnum:[%s],imTypeEnum:%s,targetid:%s,jsonIm:%s", seqID.getValue(),msgTypeEnum.getValue(),imTypeEnum.getValue(),targetid,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	/**
	 * 十里桃花活动，特效通知
	 * @param notifyRoomId
	 * @param content
	 * @throws Exception
	 */
	public void sendTaoHuaActivityMsg(String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_Taohua.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送十里桃花活动im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 发送type：26，活动通知（非私聊）
	 * @param notifyRoomId 目标房间或全站房间
	 * @param content type：26的消息主体内容，详见IM文档
	 * @throws Exception
	 */
	public void sendActivityMsgByType26(String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_ALLROOMMSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送活动im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 活动特效通知
	 * 说明：effectId：1-风精灵,2-彩带雨，3-蛋糕雨，4-，5-雨滴，6-守护进阶特效，7-天神下凡，8-荣耀驾临
	 * @param notifyRoomId
	 * @param content
	 * @throws Exception
	 */
	public void sendSpecialEffectForActivity(String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SecialEffect.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送活动特效im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 发送特效通知
	 * @param notifyRoomId
	 * @param toUserId 目标房间的主播信息
	 * @param content
	 * @throws Exception
	 */
	public void sendSpecialEffectForActivity(String notifyRoomId, String toUserId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_SecialEffect.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		if(StringUtils.isNotEmpty(toUserId)) {
			data.put("to", toUserId);
		}
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送活动特效im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 2017年3月奇幻赤道之旅,im消息
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendImMsgFor201703QihuanChidaoZhilv(String notifyRoomId,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-2017年3月奇幻赤道之旅,im消息,roomId:[%s],content:[%s]", notifyRoomId,JsonUtil.beanToJsonString(content)));
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_201703Qihuanchidaozhilv.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送2017年3月奇幻赤道之旅,im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
			//throw e;
		}
		LogUtil.log.info(String.format("###end-2017年3月奇幻赤道之旅,im消息,roomId:[%s],jsonIm:[%s]", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	
	/**
	 * 用户礼物包裹变更,im消息
	 * @param roomId 通知房间号
	 * @param content content
	 */
	public void sendImMsgForUserGiftPackageChange(String notifyRoomId,JSONObject content) throws Exception{
		LogUtil.log.info(String.format("###begin-用户礼物包裹变更,im消息,roomId:[%s],content:[%s]", notifyRoomId,JsonUtil.beanToJsonString(content)));
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_userGiftPackageChange.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送用户礼物包裹变更,im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
			//throw e;
		}
		LogUtil.log.info(String.format("###end-用户礼物包裹变更,im消息,roomId:[%s],jsonIm:[%s]", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
	}
	
	/**
	 * 发送动态评论消息，全站
	 * @param content 包含接收者id:userId
	 * @throws Exception
	 */
	public void sendMsgForDiary(JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		String notifyRoomId = BaseConstants.WHOLE_SITE_NOTICE_ROOMID; // 全站通知
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM; // 系统身份
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_DIARY_COMMENT.getValue();
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue(); //群聊
		data.put("msgtype", dataMsgType);
		data.put("targetid", notifyRoomId);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error("###发送动态评论通知失败，content=" + content.toString());
		}
	}
	
	/**
	 * 客服管理后台推送及时弹窗消息
	 * @param seqID
	 * @param msgTypeEnum
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendMsg2AnchorByCustom(SeqID seqID, MsgTypeEnum msgTypeEnum, String targetid, JSONObject content) throws Exception{
		LogUtil.log.info("###客服管理后台发送消息["+ JsonUtil.beanToJsonString(content) +"]到主播端");
		JSONObject data = new JSONObject();
		JSONObject jsonIm = new JSONObject();
		if(StringUtils.isEmpty(targetid) || null == content){
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		int seqIDInt = seqID.getValue();
		// 系统消息还是聊天消息
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int dataMsgType = msgTypeEnum.getValue();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_Popup_ByTime.getValue();
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqIDInt);
		jsonIm.put("data", data);
		LogUtil.log.info("推送IM内容："+jsonIm.toString());
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		IMutils.sendMsg2IM(jsonIm, senderUserId);
	}
	
	/**
	 * 主播pk，发送相关消息
	 * @param notifyRoomId 目标房间
	 * @param dataType 消息类型
	 * @param content
	 * @throws Exception
	 */
	public boolean sendAnchorPKMSG(String notifyRoomId, int dataType, JSONObject content) throws Exception {
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		boolean ret = false;
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			ret = IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送pk请求im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
		return ret;
	}
	
	/**
	 * 发送全站滚屏通知（通用，根据需要，可选择传不同参数）
	 * @param notifyRoomId
	 * @param dataType
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public boolean sendRunwayMSG(String notifyRoomId, JSONObject content) throws Exception {
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		boolean ret = false;
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		int dataType = ImTypeEnum.IM_11001_RUNWAY.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			ret = IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送滚屏通知请求im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
		return ret;
	}
	
	/**
	 * 发用户邮箱-系统通知
	 * @param notifyUId
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public boolean sendMailBoxMSG(String notifyUId, String msg) throws Exception {
		if(StringUtils.isEmpty(notifyUId) || StringUtils.isEmpty(msg)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		boolean ret = false;
		JSONObject imDataAnchor = new JSONObject();
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		int imType = ImType21007Enum.shoujianxiang.getValue();
		int funID = 21007;
		int msgtype = IMBusinessEnum.MsgTypeEnum.SingleChat.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		imDataAnchor.put("msgtype", msgtype);
		imDataAnchor.put("type", imType);
		imDataAnchor.put("content", msg);
		imDataAnchor.put("targetid", notifyUId);
		try {
			ret = IMutils.sendMsg2IM(funID, seqID, imDataAnchor,senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return ret;
	}

	/**
	 *  幸运礼物发放勋章
	 * @param notifyRoomId
	 * @param content
	 */
	public void sendMsgForLuckyGift(String notifyRoomId, JSONObject content) {
		// 发房间通知
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		int imType = IMBusinessEnum.ImTypeEnum.IM_11001_SYS_ALLROOMMSG.getValue();
		content.put("isShowHorn", false);
		JSONObject imData = new JSONObject();
		imData.put("msgtype", 2);
		imData.put("targetid", notifyRoomId);
		imData.put("type", imType);
		imData.put("content", content);
		
		int funID = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		
		// 发送IM消息
		try {
			LogUtil.log.info("####begin 用户幸运礼物送勋章-发送通知：msg=" + content);
			IMutils.sendMsg2IM(funID, seqID, imData,senderUserId);
			LogUtil.log.info("####end 用户幸运礼物送勋-发送通知：msg=" + content);
		} catch (Exception e) {
			LogUtil.log.error("####end 用户幸运礼物送勋-发送通知发生异常：msg=" + content);
			LogUtil.log.error(e.getMessage(), e);
		}
	}
	/**
	 * 发送给主播端使用的消息
	 * @param notifyRoomId
	 * @param content
	 * @throws Exception
	 */
	public void sendMsgToAnchor(String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId) || content == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_MSGTOANCHOR.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送活动im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 通知客户端更新礼物栏
	 * @param notifyRoomId
	 * @param content
	 * @throws Exception
	 */
	public void sendUpdateGiftMsg(String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_11001_UPDATE_GIFT.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送活动im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 用户间送礼后的消息
	 * @param sendUser 发送者消息者
	 * @param notifyRoomId 目标
	 * @param content
	 * @throws Exception
	 */
	public void sendUserGiftMsg(String sendUser, String notifyRoomId, JSONObject content) throws Exception{
		if(StringUtils.isEmpty(notifyRoomId)) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_SENDGIFT_TO_USER.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = notifyRoomId;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
		data.put("to", sendUser);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,通知房间:[%s],content:%s", notifyRoomId,JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 发送头条消息
	 * @param content
	 * @throws Exception
	 */
	public void sendHeadlineMsg(JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_HEADLINE_MSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		String dataTargetid = BaseConstants.WHOLE_SITE_NOTICE_ROOMID;		
		data.put("msgtype", dataMsgType);
		data.put("targetid", dataTargetid);
		data.put("type", dataType);
		data.put("content", content);
//		data.put("to", sendUser);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 通知客户端抢宝箱相关信息
	 * @param content
	 * @throws Exception
	 */
	public void sendGrabBoxMsg(String notifyRoomId, JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_GRABBOX_MSG.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		data.put("msgtype", dataMsgType);
		data.put("targetid", notifyRoomId);
		data.put("type", dataType);
		data.put("content", content);
//		data.put("to", sendUser);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 服务端发送传送门使用
	 * 由服务端拼接的传送门消息，简单的文字，不处理用户勋章，等级等图片
	 * @param msgContent 消息主体内容
	 * @param urlMsg 跳转链接说明内容
	 * @throws Exception
	 */
	// my-todo，这里的UserInfovo应该改造为其他的，如Map，解耦
//	public void sendDalaba(UserInfoVo anchor,String msgContent, String urlMsg, String roomId) throws Exception{
//		if(StringUtils.isEmpty(msgContent)) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
//			throw e;
//		}
//		String wholeSiteNoticeRoomId = Constants.WHOLE_SITE_NOTICE_ROOMID;
//		JSONObject imAllDataBodyJson = new JSONObject();
//		imAllDataBodyJson.put("funID", IMBusinessEnum.FunID.FUN_11001.getValue());
//		imAllDataBodyJson.put("seqID", IMBusinessEnum.SeqID.SEQ_1.getValue());
//		JSONObject imMsgJsonData = new JSONObject() ;
//		imMsgJsonData.put("msgtype", 2);
//		imMsgJsonData.put("targetid", wholeSiteNoticeRoomId);
//		imMsgJsonData.put("type", IMBusinessEnum.ImTypeEnum.IM_1001_DALABA.getValue());
//		JSONObject content = new JSONObject() ;
//		content.put("roomId", roomId); // 跳转到的房间
//		content.put("anchorId", anchor.getUid()); 
//		content.put("nickname", anchor.getNickname());
//		content.put("userLevel", anchor.getUserLevel());
//		content.put("anchorLevel", anchor.getAnchorLevel());
//		content.put("avatar", anchor.getAvatar());
//		content.put("msg", msgContent); // 消息内容
//		content.put("uriMsg", urlMsg); // 链接内容说明
//		imMsgJsonData.put("content", content);
//		imAllDataBodyJson.put("data", imMsgJsonData);
//		String senderUserId = Constants.SYSTEM_USERID_OF_IM;
//		try {
//			IMutils.sendMsg2IM(imAllDataBodyJson, senderUserId);
//		} catch (Exception e) {
//			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(imAllDataBodyJson)));
//		}
//	}
	
	/**
	 * 通知客户的刷新夺宝礼物配置
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendUpdateGiftCon(String targetid, JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_INDIANA.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 私聊失败提示通知
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendPrivateChatFailMsg(String targetid, JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_PRIVATE_CHAT_FAIL.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
	/**
	 * 用户分享信息IM通知
	 * @param targetid
	 * @param content
	 * @throws Exception
	 */
	public void sendUserShareMsg(String targetid, JSONObject content) throws Exception{
		JSONObject jsonIm = new JSONObject();
		// 聊天消息 
		int funIDChatMsg = IMBusinessEnum.FunID.FUN_11001.getValue();
		int seqID = IMBusinessEnum.SeqID.SEQ_1.getValue();
		JSONObject data = new JSONObject();
		int dataType = IMBusinessEnum.ImTypeEnum.IM_1001_USER_SHARE.getValue();
		//群聊
		int dataMsgType = IMBusinessEnum.MsgTypeEnum.GroupChat.getValue();
		//房间id
		data.put("msgtype", dataMsgType);
		data.put("targetid", targetid);
		data.put("type", dataType);
		data.put("content", content);
		jsonIm.put("funID", funIDChatMsg);
		jsonIm.put("seqID", seqID);
		jsonIm.put("data",data );
		String senderUserId = BaseConstants.SYSTEM_USERID_OF_IM;
		try {
			IMutils.sendMsg2IM(jsonIm, senderUserId);
		} catch (Exception e) {
			LogUtil.log.error(String.format("###发送im消息发生异常,content:%s",JsonUtil.beanToJsonString(jsonIm)));
		}
	}
	
}
