package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 房间-角色的信息
 * @author Administrator
 *
 */
public class RoomRoleRelCacheInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2434572526679822359L;

	/**
	 * 房间id
	 */
	private String roomId;
	
	/**
	 * 主播id
	 */
	private String anchorUserId;
	
	/**
	 * 房管id列表
	 */
	private List<String> roomAdminUserIds;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getAnchorUserId() {
		return anchorUserId;
	}

	public void setAnchorUserId(String anchorUserId) {
		this.anchorUserId = anchorUserId;
	}

	public List<String> getRoomAdminUserIds() {
		return roomAdminUserIds;
	}

	public void setRoomAdminUserIds(List<String> roomAdminUserIds) {
		this.roomAdminUserIds = roomAdminUserIds;
	}
	
	

}
