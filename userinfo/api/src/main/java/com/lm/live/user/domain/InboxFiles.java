package com.lm.live.user.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_inbox_files
 * @author shao.xiang
 * @date 2017-06-11
 */
public class InboxFiles extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	/** 主键自增ID */
	private Integer id;
	
	/** 用户ID */
	private String userId;
	
	/** 用户昵称 */
	private String nickName;
	
	/** 标题 */
	private String title;
	
	/** 文字内容  */
	private String content;
	
	/** 跳转地址 */
	private String url;
	
	/**上传后在服务器的文件名,比如'abc123.jpg'*/
	private String fileName;
	
	/** 媒体类型,0:图片,1:文字  */
	private Integer mediaType;
	
	/** 开始时间 */
	private Date beginTime;
	
	/** 结束时间 */
	private Date endTime;
	
	/** 添加时间 */
	private Date addTime;
	
	/** 截图排序号， 最小的排在最前面 */
	private Integer indexs;
	
	/**文件状态, 0停用, 1启用 */
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getIndexs() {
		return indexs;
	}

	public void setIndexs(Integer indexs) {
		this.indexs = indexs;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
