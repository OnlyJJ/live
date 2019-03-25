package com.lm.live.user.enums;

/**
 * 聊天用户的信息实体属性的枚举值
 * @author Administrator
 *
 */
public class UserInfoVoEnum {

	/**
	 *  发送者类型  1:主播，2:普通用户，3:房管  4:游客 5官方人员（权限最高） 
	 */
	
	
	/**
	 * 身份类型
	 *userInfoVo.setType("5"); // 发送者类型  ，，   
	 */
	public  enum Type {
		
		/** 1:主播 */
		Anchor("1"),
		
		
		/** 2:普通用户  */
		CommonUser("2"),
		
		/** 3:房管 */
		RoomMgr("3"),
		
		/** 4:游客 */
		Visitor("4"),
		
		/** 5官方人员（权限最高） */
		OfficialUser("5");
		
		
		private final String value;
		
		Type(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
