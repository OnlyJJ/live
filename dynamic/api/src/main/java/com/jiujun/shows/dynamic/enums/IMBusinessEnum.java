package com.jiujun.shows.dynamic.enums;

/**
 * im相关的业务枚举
 * @author Administrator
 *
 */
public class IMBusinessEnum {
	

	/**
	 * 功能号
	 */
	public  enum FunID {
		
		/** 聊天消息: 11001 */
		FUN_11001(11001),
		
		/** 系统通知:21007 */
		FUN_21007(21007);
		
		private final int value;
		
		FunID(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 序列号
	 */
	public  enum SeqID {
		
		SEQ_1(1);
		
		private final int value;
		
		SeqID(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 消息公开程度
	 * @author Administrator
	 *
	 */
	public  enum MsgTypeEnum {
		
		/** 单聊:1 */
		SingleChat(1),
		
		
		/** 群聊:2 */
		GroupChat(2);
		
		
		private final int value;
		
		MsgTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * IM发送消息类型枚举类，以后增加的新类型需要放入到这里
	 * @author shaoix
	 *
	 */
	public enum ImTypeEnum {
		/** 文本*/
		IM_11001_text(1),
		
		/** 礼物 */
		IM_11001_liwu(5),
		
		/** 禁言 */
		IM_11001_forbidSpeak(6),
		
		/** 踢人 */
		IM_11001_forceOUt(7),
		
		/** 红包 */
		IM_11001_hongbao(8),
		
		/** 赠送座驾 */
		IM_11001_songzuojia(9),
		
		/** 用户摘蜜桃礼物 */
		IM_11001_mitao(10),
		
		/** 抢车位 */
		IM_11001_qiangchewei(11),
		
		/** 大喇叭 */
		IM_11001_dalaba(12),
		
		/** 宝箱 */
		IM_11001_baoxiang(13),
		
		/** 主播摘蜜桃礼物 */
		IM_11001_anchorPeachGift(14),
		
		/** 砸蛋消息通知 */
		IM_11001_zadan(15),
		
		/** 2016七夕礼物  */
		IM_11001_2016qixi(16),
		
		/** 蜜桃成熟消息   */
		IM_11001_peachRipe(17),
		
		/** 201609恋爱季活动 */
		IM_11001_2016September(18),
		
		/** 运营活动 */
		IM_11001_YunYingHuoDong(19),		

		/** 解除禁言 */
		IM_11001_unForbidSpeak(20),
		
		/** 用作礼物跑道滚屏消息(原来用作:守护送礼满足条件后发的喊话) */
		IM_11001_hanhuasmg(21),
		
		/** 用来发特殊消息，提供给客户端刷新守护列表使用 */
		IM_11001_specialForSH(22),
		
		/** 玩游戏,消息通知*/
		IM_11001_Game(23),
		
		/** 幸运礼物:奖励金币,发送聊天消息  */
		IM_11001_Luckygift(24),
		
		/** 用户等级提升消息  */
		IM_11001_Upgrade(25),
		
		/** 系统发送全站消息,用于发送全站消息且在公屏上显示 一句话  */
		IM_11001_SYS_ALLROOMMSG(26),
		
		/** 红包雨  */
		IM_11001_SYS_Hongbaoyu(27),
		
		/** 用户关注后，发送给主播的消息 */
		IM_11001_Attention(28),
		
		/** 暖冬计划  */
		IM_11001_Nuandongjihua2017(29),
		
		/** 201702情人节  */
		IM_11001_201702Qingrenjie(30),
		
		/** 刷新房间成员  */
		IM_11001_RefreshRoomOnlineMembers(31),
		
		/** 刷新房间-勋章墙数据  */
		IM_11001_RefreshRoomDecorateWalls(32),
		
		/** 十里桃花活动，特效通知 */
		IM_11001_Taohua(33),
		/** 获得特效通知，详见im文档 */
		IM_11001_SecialEffect(34),
		
		/** 201703奇幻赤道之旅相关im消息   */
		IM_11001_201703Qihuanchidaozhilv(35),
		
		/** 用户礼物包裹通用变更   */
		IM_11001_userGiftPackageChange(36),
		
		/** 动态评论通知 */
		IM_11001_DIARY_COMMENT(37),
		
		/** 及时弹窗消息 */
		IM_11001_Popup_ByTime(38),
		
		/** 主播pk，发起者向被pk者发送pk请求消息 */
		IM_11001_PK_ToBeChallenger(39),
		
		/** pk接受者接受请求后，反馈给发起者的消息 */
		IM_11001_PK_BackToChallenger(40),
		
		/** pk结束后，发送给主播的消息 */
		IM_11001_PK_End(41),
		
		/** 收到礼物，向pk主播发送消息，更新收礼金币 */
		IM_11001_PK_ReceivesGift_Msg(42),
		
		/** 滚屏通知 */
		IM_11001_RUNWAY(43),
		
		/** 发送给主播端使用的消息体 */
		IM_11001_MSGTOANCHOR(44),
		
		/** 通知客户端更新礼物栏 */
		IM_11001_UPDATE_GIFT(45),
		
		/** 夺宝消息通知 */
		IM_11001_TREATURE(46),
		
		/** 用户间送礼消息 */
		IM_1001_SENDGIFT_TO_USER(47),
		
		/** 头条消息 */
		IM_1001_HEADLINE_MSG(48),
		
		/** 抢宝箱相关消息 */
		IM_1001_GRABBOX_MSG(49),
		
		/** 系统发送的传送门，用于全站通知，可跳转至目标房间 */
		IM_1001_DALABA(50),
		
		/** 夺宝礼品配置刷新消息 */
		IM_1001_INDIANA(51),
		
		/** 私聊失败提示消息 */
		IM_1001_PRIVATE_CHAT_FAIL(52),
		
		/** 用户分享IM消息 */
		IM_1001_USER_SHARE(53);
		
		private final int value;
		
		ImTypeEnum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	
	/**
	 * IM发送funID=21007消息类型枚举类，以后增加的新类型需要放入到这里
	 * @author shaoix
	 *
	 */
	public enum ImType21007Enum {
		
		/** 文本通知 */
		text(0),
		
		/** 进入房间  */
		inRoom(1),
		
		/** 退出房间   */
		outRoom(2),
		
		/** 关注主播  */
		attention(3),
		
		/**  取消关注主播  */
		cancelAttention(4),
		
		/** 系统公告 */
		sysNitice(5),
		
		/** 用户收件箱   */
		shoujianxiang(6);
		
		
		private final int value;
		
		ImType21007Enum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	/**
	 * IM发送消息(content.contentTpye)枚举类，以后增加的新类型需要放入到这里
	 * @author shaoix
	 *
	 */
	public enum ImContentTpyeEnum {
		/** 聊天消息:游戏:摇钱树*/
		IM_11001_23_moneTree(1);		
		
		private final int value;
		
		ImContentTpyeEnum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}
	
	/**
	 * IM发送消息(type=26的消息的content.type)枚举类，以后增加的新类型需要放入到这里
	 * @author shaoix
	 *
	 */
	public enum ImType26_Content_TypeEnum {
		/** 2016圣诞树*/
		IM_11001_26_2016chrimasTree(1),	
		
		/** 201702情人节通知 */
		IM_11001_26_201702Qingrenjie(5),
		
		/** 十里桃花活动通知*/
		IM_11001_26_taohua(6),
		
		/** 201703奇幻赤道之旅*/
		IM_11001_26_201703Qhcdzl(7);	
		
		private final int value;
		
		ImType26_Content_TypeEnum(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		
	}

}
