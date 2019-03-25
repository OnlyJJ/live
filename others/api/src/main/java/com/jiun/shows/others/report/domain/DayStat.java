package com.jiun.shows.others.report.domain;



import com.jiujun.shows.common.vo.BaseVo;

/**
* DayStat20170822
 * 每日统计数据表（考虑到安全问题，程序未做删除表处理，需手动删除过期的表，已节省空间）
 * @entity
 * @table t_day_stat_xxx
 * @author shao.xiang
 * @date 2017-06-15
*/
public class DayStat extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6145392929842105702L;
	private int id;
	/**
	 * 客户端类型,0-android,1-IOS
	 */
	private int clientType;
	/**
	 * 平台id
	 */
	private int platformId;
	/**
	 * 添加时间，yyyyMMddHH
	 */
	private String addtime;
	/**
	 * 手机卡标识
	 */
	private String uuid;
	/**
	 * 登录帐号
	 */
	private String userAccount;
	/**
	 * 渠道id
	 */
	private String channelId;
	/**
	 * 产品id
	 */
	private int productId;
	/**
	 * SDK版本
	 */
	private String version;
	/**
	 * 是否登录，1-是; 0-否
	 */
	private int isLogin;
	/**
	 * 是否安装，1-是; 0-否
	 */
	private int isAdd;
	/**
	 * 是否注册，1-是; 0-否
	 */
	private int isReg;
	/**
	 * 是否激活，1-是; 0-否
	 */
	private int isAct;
	/**
	 * 留存天数，例如: 1、3、7
	 */
	private int retention;
	/**
	 * 是否有效 用户，1是,0否
	 */
	private int isVaild;
	/**
	 * 有效用户留存天数如1，3，7，
	 */
	private int retentionVaild;
	/**
	 * douid
	 */
	private String douId;
	
	private String tableName; // 数据库表名
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClienttype() {
		return this.clientType;
	}
	
	public void setClienttype(int clientType) {
		this.clientType = clientType;
	}
	public int getPlatformid() {
		return this.platformId;
	}
	
	public void setPlatformid(int platformId) {
		this.platformId = platformId;
	}
	public String getAddtime() {
		return this.addtime;
	}
	
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUseraccount() {
		return this.userAccount;
	}
	
	public void setUseraccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getChannelid() {
		return this.channelId;
	}
	
	public void setChannelid(String channelId) {
		this.channelId = channelId;
	}
	public int getProductid() {
		return this.productId;
	}
	
	public void setProductid(int productId) {
		this.productId = productId;
	}
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	public int getIslogin() {
		return this.isLogin;
	}
	
	public void setIslogin(int isLogin) {
		this.isLogin = isLogin;
	}
	public int getIsadd() {
		return this.isAdd;
	}
	
	public void setIsadd(int isAdd) {
		this.isAdd = isAdd;
	}
	public int getIsreg() {
		return this.isReg;
	}
	
	public void setIsreg(int isReg) {
		this.isReg = isReg;
	}
	public int getIsact() {
		return this.isAct;
	}
	
	public void setIsact(int isAct) {
		this.isAct = isAct;
	}
	public int getRetention() {
		return this.retention;
	}
	
	public void setRetention(int retention) {
		this.retention = retention;
	}
	public int getIsvaild() {
		return this.isVaild;
	}
	
	public void setIsvaild(int isVaild) {
		this.isVaild = isVaild;
	}
	public int getRetentionvaild() {
		return this.retentionVaild;
	}
	
	public void setRetentionvaild(int retentionVaild) {
		this.retentionVaild = retentionVaild;
	}
	public String getDouid() {
		return this.douId;
	}
	
	public void setDouid(String douId) {
		this.douId = douId;
	}

	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
