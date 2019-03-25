package com.lm.live.login.domain;

import com.lm.live.common.vo.BaseVo;

/**
* CodeRandom
*/
public class CodeRandom extends BaseVo {
	private static final long serialVersionUID = -5909508847479149156L;
	
	private int id;
	/**
	 * 随机号
	 */
	private String code;
	/**
	 * userId是否使用,0:未使用,1:已使用
	 */
	private int isUserUse;
	/**
	 * roomId是否使用,0:未使用,1:已使用
	 */
	private int isRoomUse;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	public int getIsuseruse() {
		return this.isUserUse;
	}
	
	public void setIsuseruse(int isUserUse) {
		this.isUserUse = isUserUse;
	}
	public int getIsroomuse() {
		return this.isRoomUse;
	}
	
	public void setIsroomuse(int isRoomUse) {
		this.isRoomUse = isRoomUse;
	}
}
