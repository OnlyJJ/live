package com.lm.live.user.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.Page;
import com.lm.live.user.domain.UserAnchor;
/**
 * 主播信息服务
 * @author shao.xiang
 * @date 2017-06-06
 */
public interface IUserAnchorService {

	/**
	 * 通过userId获取主播
	 * @param userId
	 * @return
	 */
	UserAnchor getUserAnchorByUserId(String userId);
	
	
	/**
	 * 从缓存中通过userId获取主播
	 * @param userId
	 * @return
	 */
	UserAnchor getAnchorFromCacheByUserId(String userId);
	
	List<Map<String, Object>> userAnchorListOne(String sql);
	
	/**
	 * 通过roomId获取主播
	 * @param roomid
	 * @return
	 */
	UserAnchor getUserAnchorByRoomId(String roomid) ;
	
	/**
	 * 从缓存中查询主播列表
	 * @return
	 */
	public Page pageFindRecommendFromCache(Page page) throws Exception;
	
	/**
	 * 按自定义规则查询，并且放到缓存<br />
	 * 规则如下:<br />
	 * 1.手动推荐正在直播的主播 <br />
		2，前12位正在直播的主播 （这12位位置随机）:就是所有开播的主播，按当天收到礼物排序，并取前12位，这12位的顺序再打乱（为使这12位顺序不固定），附加到规则1的后面<br />
		3，12位之后正在直播的主播 （这个按顺序）:规则2取出12位后剩下的开播主播，数据再附加到后面<br />
		4，手动推荐的未开播主播<br />
		5. 当前面的步骤查到还少于6个，就查未开播的(按主播登录的时间倒序) <br />
	 * @return
	 */
	// my-todo-service
//	public List<AnchorInfo> queryAndSetRecommendAnchorInCahce() throws Exception;
	
	/**
	 * 清除缓存中的主播列表
	 */
	public void clearAnchorListCache();
	
	/**
	 * 查询所有有效的房间号
	 * @return
	 */
	public List<String> findAllValidRoomIds();
	
	/**
	 * 查询所有主播信息
	 * @return
	 * @throws Exception
	 */
	public List<UserAnchor> findAllAnchorInCahce(int days) throws Exception;
	
	/**
	 * 从缓存中查询，没有再查db，返回分页数据
	 * @param userIdList 过滤的主播
	 * @param page 分页信息
	 * @param rankKind 类别id
	 * @param startWeekTime 周榜开始时间
	 * @param startMounthTime 月榜开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	public Page queryAnchorInCahce(List<String> userIdList,Page page,int kind,String startWeekTime,String startMounthTime, String endTime,String appVersion) throws Exception;
	
	/**
	 * 获取主播总数
	 * @return
	 * @throws Exception
	 */
	public int getAnchorSumForCache() throws Exception;
	
	/**
	 * 改变主播粉丝数
	 * @param anchorUserId
	 * @param changeNum 关注+1; 取消关注 -1
	 */
	public void modifyAnchorFansCount(String anchorUserId,int changeNum) throws Exception;
	
	
	/**
	 * 查询主播粉丝数
	 * @param anchorUserId
	 * @throws Exception
	 */
	public int getAnchorFansCount(String anchorUserId) throws Exception;
	
	/**
	 * 2016-12-26
	 * 新的首页排序规则
	 * @param userIdList
	 * @param page
	 * @param kind
	 * @param startWeekTime
	 * @param startMounthTime
	 * @param endTime
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public Page queryNewAnchorInCahce(List<String> userIdList,Page page,int kind,String startWeekTime,String startMounthTime, String endTime,String appVersion) throws Exception;
	
	/**
	 * 通过roomId获取主播，有缓存
	 * @param roomid
	 * @return
	 */
	UserAnchor getUserAnchorByRoomIdCache(String roomId);
	
	/**
	 * 通过主播userId集合来修改主播是否禁播
	 * @param userIdList
	 */
	public void updateIsForbidden(List<String> userIdList);
	
	/**是否显示主播等级图标*/
	public void isAnchorIconShow();
	
	/**
	 * 通过主播userId集合查询审核通过90天的主播
	 * @param userIdList
	 * @return
	 */
	public List<String> isRecentlyRegist(List<String> userIdList);
	
	/**
	 * 获取分类主播
	 * @param style 1-女神，2-好声音，3-新秀
	 * @param userIdList
	 * @param page
	 * @param startWeekTime
	 * @param startMounthTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	Page queryAnchorStyleFromCahce(Page page, int style) throws Exception;
	
	/**
	 * 获取所有新秀主播
	 * @param userIdList
	 * @param payStarttime
	 * @param payEndTime
	 * @param tartWeekTime
	 * @param startMounthTime
	 * @return
	 */
	// my-todo-service
//	List<AnchorInfo> listAllNewstar(List<String> userIdList);
	
	/**
	 * 获取所有分类类型的主播
	 * @param style
	 * @param userIdList
	 * @param payStarttime
	 * @param payEndTime
	 * @param tartWeekTime
	 * @param startMounthTime
	 * @return
	 */
	// my-todo-service
//	List<AnchorInfo> listAnchorByStyle(int style, List<String> userIdList);
	
	JSONObject listAnchorOthers(Page page, int type) throws Exception;
	
	/**
	 * 获取所有主播，排序规则：开播、有无视频
	 * @param userIdList
	 * @return
	 */
	// my-todo-service
//	List<AnchorInfo> listAllOtherPck(List<String> userIdList) throws Exception;
	
	/**
	 * 获取主播信息（正常使用的）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserAnchor getNormalAnchor(String userId) throws Exception;
}
