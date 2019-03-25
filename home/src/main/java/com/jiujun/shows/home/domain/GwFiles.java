package com.jiujun.shows.home.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_gw_files
 * @author shao.xiang
 * @date 2017-06-11
 */
public class GwFiles extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 对应t_common_attachment的groupId */
	private String attaId;
	/** 标题 */
	private String title;
	/** 类别,对应t_common_tree中的id */
	private Integer treeId;
	/** 截图排序号， 最小的排在最前面 */
	private Integer indexs;
	/** 跳转地址 */
	private String url;
	/** 0.000 */
	private Double fileSize;
	/** 0000-00-00 00:00:00 */
	private Date addTime;
	/**判断添加时间，大于或等于 */
	private Date	gtAddTime;
	/**判断添加时间，小于或等于 */
	private Date	ltAddTime;
	/**上传后在服务器的文件名,比如'abc123.jpg'*/
	private String fileName;
	/**文件状态, 0停用, 1启用 */
	private Integer status;
	
	/** app端显示的图片名 */
	private String appShowImg;
	
	/** 标题颜色，16进制或RGB都可  */
	private String titleColor;
	
	/** 媒体类型,0:图片,1:文字  */
	private Integer mediaType;
	
	/** 使用目的,0:banner,1:开机动画  */
	private Integer usePurpose;
	
	/** 文字内容  */
	private String content;
	
	/** 内容颜色，16进制或RGB都可  */
	private String contentColor;
	
	/** 开始时间 */
	private Date beginTime;
	
	/** 结束时间 */
	private Date endTime;
	
	/** banner适用类型：0,公用;1,web;2,APP*/
	private Integer bannerType;
	
	
	/** 在哪个页面显示(当usePurpose=0时用),0:首页,1:充值页(,默认为0) 2.蜜桃播报页 */
	private int showPage;
		
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setAttaId(String attaId){
		this.attaId = attaId;
	}
	
	public String getAttaId() {
		return this.attaId;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTreeId(Integer treeId){
		this.treeId = treeId;
	}
	
	public Integer getTreeId() {
		return this.treeId;
	}
	
	public void setIndexs(Integer indexs){
		this.indexs = indexs;
	}
	
	public Integer getIndexs() {
		return this.indexs;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setFileSize(Double l){
		this.fileSize = l;
	}
	
	public Double getFileSize() {
		return this.fileSize;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}
	
	public void setGtAddTime(Date gtAddTime){
		this.gtAddTime = gtAddTime;
	}
	
	public Date getGtAddTime() {
		return this.gtAddTime;
	}
	
	public void setLtAddTime(Date ltAddTime){
		this.ltAddTime = ltAddTime;
	}
	
	public Date getLtAddTime() {
		return this.ltAddTime;
	}
	
	public Integer getStatus() {
		if(status == null){
			return 0;
		}else{
			return status;
		}
		
	}

	public void setStatus(Integer status) {
		if(status == null){
			this.status = 0;
		}else{
			this.status = status;
		}
	}

	public String getAppShowImg() {
		return appShowImg;
	}

	public void setAppShowImg(String appShowImg) {
		this.appShowImg = appShowImg;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public Integer getUsePurpose() {
		return usePurpose;
	}

	public void setUsePurpose(Integer usePurpose) {
		this.usePurpose = usePurpose;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentColor() {
		return contentColor;
	}

	public void setContentColor(String contentColor) {
		this.contentColor = contentColor;
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

	public Integer getBannerType() {
		return bannerType;
	}

	public void setBannerType(Integer bannerType) {
		this.bannerType = bannerType;
	}

	public int getShowPage() {
		return showPage;
	}

	public void setShowPage(int showPage) {
		this.showPage = showPage;
	}
}