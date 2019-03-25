package com.jiun.shows.others.push.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 推送消息配置
* @entity
* @table t_push_content_conf
* @author shao.xiang
* @date 2017-06-15
*/
public class PushContentConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 状态，0-停用，1-启用
	 */
	private int useStatus;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	/**
	 * 消息类型：0-自定义，用户点击打开到首页，1-开播提醒，打开到房间，2-活动消息，打开到活动页面
	 */
	private int msgType;
	/**
	 * 发送类型，0-所有用户
	 */
	private int sendType;
	/**
	 * 活动链接
	 */
	private String url;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public int getUsestatus() {
		return this.useStatus;
	}
	
	public void setUsestatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public Date getModifytime() {
		return this.modifyTime;
	}
	
	public void setModifytime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
