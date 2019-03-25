package com.jiujun.shows.rank.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.Helper;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.rank.constant.CacheTime;
import com.jiujun.shows.rank.constant.RedisKey;
import com.jiujun.shows.rank.vo.AnchorInfo;
import com.jiujun.shows.rank.vo.Kind;
import com.jiujun.shows.rank.vo.Rank;
import com.jiujun.shows.user.service.IUserAnchorService;

/**
 * 榜单业务处理类
 * @author shao.xiang
 * @date 2017-08-05
 *
 */
@Service("rankBiz")
public class RankBiz {
	
	@Resource
	private IUserAnchorService userAnchorService;
	
	/** 是否需要重新查询数据，默认false，不查询，返回旧的缓存 */
	public static boolean isReGetOtherData = false;
	
	/**
	 * 榜单数据处理
	 * @param rank 榜单相关实体，见文档
	 * @param kind 榜单相关实体，见文档
	 * @return json
	 */
	public JSONObject getRanking(Page page, Rank rank, Kind kind) {
		Result result = new Result(ErrorCode.SUCCESS_0);
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if( null != rank ){
			if( null != rank && 0 != rank.getTypeId() 
					&& page !=null && !StringUtils.isEmpty(page.getPageNum())
					&& !StringUtils.isEmpty(page.getPagelimit())) {
				String startTime = null;
				String endTime = null;
				String toUserId = rank.getToUserId();
				String rankKind =  rank.getKind();
				int ranktypeId = rank.getTypeId();
				if( rankKind.equals("d") ){
					startTime = DateUntil.getFormatDate("yyyyMMdd", new Date());
					endTime = DateUntil.getFormatDate("yyyyMMdd", new Date());
				}else if( rankKind.equals("yesterday") ){ //昨天
					Date nowDate = new Date();
					Date yesterday = DateUntil.addDay(nowDate, -1);
					startTime = DateUntil.getFormatDate("yyyyMMdd", yesterday);
					endTime = DateUntil.getFormatDate("yyyyMMdd", yesterday);
				}else if( rankKind.equals("w") ){
					startTime = DateUntil.getWeek();
					endTime = DateUntil.getFormatDate("yyyyMMdd", new Date());
				}else if( rankKind.equals("m") ){
					startTime = DateUntil.getMonthDatetime();
					endTime = DateUntil.getFormatDate("yyyyMMdd", new Date());
				}else if( rankKind.equals("t") ){
					startTime = "";
					endTime = "";
				} else if(rankKind.equals("lw")) { // 上周
					startTime = DateUntil.getLastWeek();
					endTime = DateUntil.getWeek();
				} else if(rankKind.equals("tw")) { // 本周
					startTime = DateUntil.getWeek();
					endTime = DateUntil.getFormatDate("yyyyMMdd", new Date());
				}
				String sql="";
				if(null == startTime || null == endTime ){
					result.setResultCode(ErrorCode.ERROR_3003.getResultCode());
					result.setResultDescr(ErrorCode.ERROR_3003.getResultDescr());
				}else {
					//int begin = page.getPageNum()<=1?0:(page.getPageNum()-1)*page.getPagelimit();
					int pageNum = Integer.parseInt(page.getPageNum());
					int pageLimit = Integer.parseInt(page.getPagelimit());
					int begin = pageNum<=1?0:(pageNum-1)*pageLimit;
					
					int countRank = 0;
					String listRankSize = "0";
					StringBuffer sbf = new StringBuffer();
					// 缓存中的key用keyPre+rankxxx_+rankKind+ranktypeId+[toUserId]
					//缓存前缀
					String cahceKeyPre = RedisKey.RANK;
					String cacheKeyRankSize = cahceKeyPre+"rankSize_"+rankKind+ranktypeId+pageNum+pageLimit;
					String cacheKeyRankCount = cahceKeyPre+"rankCount_"+rankKind+ranktypeId+pageNum+pageLimit;
					String cacheKeyDataBody = cahceKeyPre+"dataBody_"+rankKind+ranktypeId+pageNum+pageLimit;
					if(!StringUtils.isEmpty(toUserId)){
						cacheKeyRankSize = cacheKeyRankSize +toUserId;
						cacheKeyRankCount = cacheKeyRankCount+toUserId;
						cacheKeyDataBody = cacheKeyDataBody+toUserId;
					}
					Object cacheObjRankSize = RedisUtil.get(cacheKeyRankSize);
					Object cacheObjRankCount = RedisUtil.get(cacheKeyRankCount);
					Object cacheObjDataBody = RedisUtil.get(cacheKeyDataBody);
					int cacheTime = CacheTime.RANK_TIMEOUTSECOND;
					if ( 3 == ranktypeId ){ //房间内手动刷新富豪榜,缓存时间不要太长
						cacheTime = CacheTime.RANK_ATROOM_HANDACTIVE_TIMEOUTSECOND;
					}
					
					// 判断缓存是否有数据,有则从缓存中取 
					if( cacheObjRankSize !=null && cacheObjRankCount!=null && cacheObjDataBody!=null ){
						LogUtil.log.info("###getRanking-cahce,cacheKeyRankSize:"+cacheKeyRankSize);
						countRank = Integer.parseInt(cacheObjRankCount.toString());
						listRankSize = cacheObjRankCount.toString();
						jsonArray = (JSONArray)cacheObjDataBody;
					}else{ //缓存中没有则去数据库查，查完后再放入到缓存
						// 加二级缓存
						// 当缓存失效后，需要重新查询数据
						// 1、先拦截查询线程，并发时，只处理一条真正查询，其他都返回老的缓存
						String oldcacheKeyRankSize = cacheKeyRankSize + "_old"; // 二级缓存
						String oldcacheKeyRankCount = cacheKeyRankCount + "_old"; // 二级缓存
						String oldcacheKeyDataBody = cacheKeyDataBody + "_old"; // 二级缓存
						if(isReGetOtherData) { // 说有其他线程在处理，直接返回旧数据
							Object cacheOldObjRankSize = RedisUtil.get(oldcacheKeyRankSize);
							Object cacheOldObjRankCount = RedisUtil.get(oldcacheKeyRankCount);
							Object cacheOldObjDataBody = RedisUtil.get(oldcacheKeyDataBody);
							if(cacheOldObjRankSize != null && cacheOldObjRankCount != null && cacheOldObjDataBody != null) {
								LogUtil.log.error("###getRanking-一级缓存失效。返回二级缓存，页码：" + pageNum + ",单页条数：pageSize=" + pageLimit);
								countRank = Integer.parseInt(cacheOldObjRankSize.toString());
								listRankSize = cacheOldObjRankCount.toString();
								jsonArray = (JSONArray)cacheOldObjDataBody;
							} else {
								// 如果老的缓存也失效了，则需要重新查询
								isReGetOtherData = false;
							}
						}
						if(!isReGetOtherData) {
							LogUtil.log.error("###getRanking-一级缓存失效，二级缓存也失效，重新查询DB，页码：" + pageNum + ",单页条数：pageSize=" + pageLimit);
							isReGetOtherData = true;
							try {
								if( 1 == ranktypeId ){//魅力榜
									//sql = getMeiliRankSQL(startTime, endTime, toUserId,begin,pageLimit);
									sql = getMeiliRankSQL(startTime, endTime, toUserId,rankKind);
								}else if( 2 == ranktypeId ){//富豪榜
									//sql = getFuhaoRankSQL(startTime, endTime, toUserId,begin,pageLimit);
									sql = getFuhaoRankSQL(startTime, endTime, toUserId,rankKind);
								}else if ( 3 == ranktypeId ) {//房间内手动刷新富豪榜
									sql = getFuhaoRankSQL(toUserId);
								}else if( 4 == ranktypeId ){//人气榜
									sql = getRenqiRankSQL(startTime , endTime,toUserId,rankKind);
								}else if( 5 == ranktypeId ){//幸运屋榜单
									sql = getLuckyGiftHouseSQL(startTime , endTime,toUserId,rankKind);
								}else if( 6 == ranktypeId ){//好运君榜单
									sql = getLuckyGiftJunSQL(startTime , endTime,toUserId,rankKind);
								} else if( 7 == ranktypeId ){//新人王榜单
									sql = getNewStarMeiliRankSQL(startTime , endTime,toUserId,rankKind);
								} 
								// my-todo-service
//								countRank = this.getCountBySQL(sql); 
//								sql = SQLUtil.getLimitSqlByPage(page, sql);
//								List<Map<String,Object>> listRank = this.selectListBySql(sql);
//								listRankSize = listRank.size()+"";
//								for( Map<String,Object> map: listRank){
//									AnchorInfo anchorInfo = Helper.map2Object(map, AnchorInfo.class);
//									// 魅力榜、人气榜、幸运屋, 还要查被关注度人数
//									if(ranktypeId == 1 || ranktypeId == 4 || ranktypeId == 5 || ranktypeId == 7){
//										String qryUserId = map.get("userId").toString();
//										//int fansCount = userAttentionService.findAttentionCounts(qryUserId);
//										int fansCount = 0;
//										try {
//											fansCount = this.userAnchorService.getAnchorFansCount(qryUserId);
//										} catch (Exception e) {
//											LogUtil.log.error(e.getMessage(),e);
//										}
//										
//										anchorInfo.setAttentionCount(fansCount+"");
//									}
//									jsonArray.add(anchorInfo.buildJson());
//								}	
							} catch(Exception e) {
								LogUtil.log.error(e.getMessage(),e);
							} finally {
								isReGetOtherData = false;
							}
							LogUtil.log.error("###getRanking-一级缓存失效，二级缓存也失效，须重新查询DB，页码：" + pageNum + ",单页条数：pageSize=" + pageLimit + ",end!");
						}
						
						// 将查到的数据放回缓存
						RedisUtil.set(cacheKeyRankCount, countRank,cacheTime);
						RedisUtil.set(cacheKeyRankSize, listRankSize,cacheTime);
						RedisUtil.set(cacheKeyDataBody, jsonArray,cacheTime);
						
						int sec_cachetime = CacheTime.RANK_SEC_TIMEOUTSECOND;
						RedisUtil.set(oldcacheKeyRankCount, countRank,sec_cachetime);
						RedisUtil.set(oldcacheKeyRankSize, listRankSize,sec_cachetime);
						RedisUtil.set(oldcacheKeyDataBody, jsonArray,sec_cachetime);
					}
					page.setCount(countRank+"");
					page.setPagelimit(listRankSize+"");
					json.put("page", page.buildJson());
				}
			}else{
				result.setResultCode(ErrorCode.ERROR_3003.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_3003.getResultDescr());
			}
		}else{
			result.setResultCode(ErrorCode.ERROR_2000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_2000.getResultDescr());
		}
		json.put(result.getShortName(), result.buildJson());
		json.put(BaseConstants.DATA_BODY, jsonArray);
		return json;
	}
	
	/**
	 * 生成"榜单"查询语句
	 * @param startTime
	 * @param endTime
	 * @param toUserID
	 * @param rankKind
	 * @return
	 */
	private String getMeiliRankSQL(String startTime,String endTime,String toUserID,String rankKind){
		//  魅力榜查询语句
		StringBuffer sb = new StringBuffer();
		
		// 条件
		String conSqlString = "";
		if(!StringUtils.isEmpty(startTime)){
			conSqlString += " and fuhao.timeframe >= '"+startTime+"' ";
		}
		if(!StringUtils.isEmpty(endTime)){
			conSqlString += " and fuhao.timeframe <= '"+endTime+"' ";
		}
		// 直播页的排行榜
		// and toUserId=主播ID
		if(!StringUtils.isEmpty(toUserID)) {
			conSqlString += " and fuhao.toUserId='"+toUserID+"' ";
		}
		
		//sb.append("SELECT c.anchorPoint sum, a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,SUM(a.diamond) diamond,b.icon,b.remark,attention.attentionCount");
//		sb.append("SELECT c.anchorPoint sum, a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,SUM(a.diamond) diamond,b.icon,b.remark");
//		sb.append(" FROM t_stat_meili a " );
//		sb.append("LEFT JOIN t_user_info b ON (a.userId=b.userId) " );
//		sb.append(" LEFT JOIN t_user_account c ON (a.userId=c.userId)" );
//		sb.append(" LEFT JOIN t_user_anchor d ON (a.userId= d.userId)" );
//		//sb.append(" LEFT JOIN (SELECT COUNT(*) attentionCount ,toUserId FROM t_user_attention group by toUserId").append(") attention on attention.toUserId = b.userId ");
//		sb.append(" WHERE 1=1 ").append(conSqlString) ;
//		sb.append(" AND  d.anchorStatus=0  AND (d.isForbidden = 0 or d.isForbidden is null) ");//anchorStatus:主播状态，0-正常; 1-停用; 2-待审核; 3-审核不通过    isForbidden:是否已被禁播:0-未被禁播，1-已被禁播
//		sb.append(" GROUP BY a.userId ");
//		if("t".equals(rankKind)){
//			sb.append(" ORDER BY c.anchorPoint DESC");
//		}else{
//			sb.append(" ORDER BY SUM(a.diamond) DESC");
//		}
		if("t".equals(rankKind)) {
			sb.append("SELECT c.anchorPoint sum, a.toUserId userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,a.diamond diamond,b.icon,b.remark")
			.append(" FROM ( ")
			.append(" SELECT toUserId,SUM(gold) AS diamond FROM t_stat_fuhao fuhao ")
			.append(" WHERE fuhao.userId != '104573' AND fuhao.userId != '102136' ")
			.append(" GROUP BY 1 ")
			.append(" ) a ")
			.append(" LEFT JOIN t_user_info b ON (a.toUserId=b.userId) ")
			.append(" LEFT JOIN t_user_account c ON (a.toUserId=c.userId) ")
			.append(" LEFT JOIN t_user_anchor d ON (a.toUserId= d.userId) ")
			.append(" WHERE d.anchorStatus=0  AND (d.isForbidden = 0 OR d.isForbidden IS NULL)	 ")
			.append(" ORDER BY sum DESC ");
		} else {
			sb.append("SELECT a.diamond sum, a.toUserId userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,a.diamond diamond,b.icon,b.remark")
			.append(" FROM ( ")
			.append(" SELECT toUserId,SUM(gold) AS diamond FROM t_stat_fuhao fuhao ")
			.append(" WHERE 1=1 ").append(conSqlString)
			.append(" AND fuhao.userId != '104573' AND fuhao.userId != '102136' ")
			.append(" GROUP BY 1 ")
			.append(" ) a ")
			.append(" LEFT JOIN t_user_info b ON (a.toUserId=b.userId) ")
			.append(" LEFT JOIN t_user_account c ON (a.toUserId=c.userId) ")
			.append(" LEFT JOIN t_user_anchor d ON (a.toUserId= d.userId) ")
			.append(" WHERE d.anchorStatus=0  AND (d.isForbidden = 0 OR d.isForbidden IS NULL)	 ")
			.append(" ORDER BY diamond DESC ");
		}
		//LogUtil.log.info("sql:"+sb.toString());
		return sb.toString();
	}
	private String getFuhaoRankSQL(String startTime,String endTime,String toUserID,String rankKind){
		//  富豪榜查询语句
		StringBuffer sb = new StringBuffer();
		
		// 条件
		String conSqlString = "";
		if(startTime.length() > 0){
			conSqlString += " and fuhao.timeframe >= '"+startTime+"' ";
		}
		if(endTime.length() > 0){
			conSqlString += " and fuhao.timeframe <= '"+endTime+"' ";
		}
		// 直播页的排行榜
		// and toUserId=主播ID
		boolean isRoom = false;
		if(!StringUtils.isEmpty(toUserID)) {
			isRoom = true;
			conSqlString += " and fuhao.toUserId='"+toUserID+"' ";
		}
		/*sb.append(" SELECT c.userPoint sum,a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,SUM(a.gold) gold,b.icon,b.remark  FROM " );
		sb.append(" t_stat_fuhao a" );
		sb.append(" LEFT JOIN t_user_info b ON (a.userId=b.userId)" );
		sb.append(" LEFT JOIN t_user_account c ON (a.userId=c.userId)" );
		//104573 是百万的号
		sb.append(" WHERE 1=1 AND a.userId != '104573' AND a.userId != '102136' " + conSqlString );//根据id过滤测试用户的数据（用户可能修改）
		sb.append(" GROUP BY a.userId");
		sb.append(" ORDER BY SUM(a.gold) DESC");*/
		if(rankKind.equals("t")) {
			if(isRoom) {
				sb.append(" SELECT a.gold sum,a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,a.gold,b.icon,b.remark  ")
				.append(" from (" )
				.append(	"select userId,sum(gold) gold from t_stat_fuhao fuhao   ")
				.append(" where userId != '104573' AND userId != '102136' ");
				if(!StringUtils.isEmpty(conSqlString)){
					sb.append(conSqlString)  ;
				}
				sb.append("  group by 1 ")
				.append(    " order by 2 desc " )
				.append("  ) a")
				.append("  LEFT JOIN t_user_info b ON (a.userId=b.userId)")
				.append("  LEFT JOIN t_user_account c ON (a.userId=c.userId)");
			} else {
				sb.append(" SELECT c.userPoint sum,a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,c.userPoint AS gold,b.icon,b.remark   ")
				.append(" FROM ( " )
				.append(" SELECT userId  FROM t_stat_fuhao fuhao  ")
				.append(" WHERE userId != '104573' AND userId != '102136'  ")
				.append(" GROUP BY 1 ")
				.append("  ) a ")
				.append("  LEFT JOIN t_user_info b ON (a.userId=b.userId) ")
				.append("  LEFT JOIN t_user_account c ON (a.userId=c.userId) ")
				.append(" ORDER BY c.userPoint DESC ");
			}
		} else {
			sb.append(" SELECT a.gold sum,a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,a.gold,b.icon,b.remark  ")
			.append(" from (" )
			.append(	"select userId,sum(gold) gold from t_stat_fuhao fuhao   ")
			.append(" where userId != '104573' AND userId != '102136' ");
			if(!StringUtils.isEmpty(conSqlString)){
				sb.append(conSqlString)  ;
			}
			sb.append("  group by 1 ")
			.append(    " order by 2 desc " )
			.append("  ) a")
			.append("  LEFT JOIN t_user_info b ON (a.userId=b.userId)")
			.append("  LEFT JOIN t_user_account c ON (a.userId=c.userId)");
		}
		//LogUtil.log.info("sql:"+sb.toString());
		return sb.toString();
	}
	private String getFuhaoRankSQL(String toUserID){
		//  手动富豪榜查询语句
		StringBuffer sb = new StringBuffer();
		
		// 条件
		String conSqlString = "";
		
		// 直播页的排行榜
		// and toUserId=主播ID
		if(!StringUtils.isEmpty(toUserID)) {
			conSqlString += " and a.toUserId='"+toUserID+"' ";
		}
		sb.append("SELECT a.userId,b.nickName nickName,c.userLevel,c.anchorLevel," );
		sb.append("SUM(a.price) gold,b.icon,b.remark" );
		sb.append(" FROM t_pay_gift_out a" );
		sb.append(" LEFT JOIN t_user_info b ON (a.userId = b.userId)");
		sb.append(" LEFT JOIN t_user_account c ON (a.userId = c.userId)");
		sb.append("	WHERE 1=1 " + conSqlString );
		sb.append(" AND resultTime >= DATE_FORMAT(now(), '%Y-%m-%d') GROUP BY userId ORDER BY SUM(a.price) DESC");	
		
		//LogUtil.log.info("sql:"+sb.toString());
		return sb.toString();
	}
	private String getRenqiRankSQL(String startTime, String endTime,
			String toUserId, String rankKind) {
		//  人气榜查询语句
		StringBuffer sb = new StringBuffer();
		
		// 条件
		String conSqlString = "";
		if(startTime.length() > 0){
			conSqlString += " and a.timeframe >= '"+startTime+"' ";
		}
		if(endTime.length() > 0){
			conSqlString += " and a.timeframe <= '"+endTime+"' ";
		}
		if(toUserId != null && !"".equals(toUserId)){
			conSqlString +=" and a.userId = '" + toUserId + "' " ; 
		}
		
		//sb.append(" SELECT  a.userId ,b.`nickName` , b.`remark` , b.`icon` , d.`anchorLevel` , e.`anchorSum` AS sum,c.roomId as roomId,attention.attentionCount,d.userLevel as userLevel ");
		sb.append(" SELECT  a.userId ,b.`nickName` , b.`remark` , b.`icon` , d.`anchorLevel` , e.`anchorSum` AS sum,c.roomId as roomId,d.userLevel as userLevel ");
		if("t".equals(rankKind) || rankKind == "t"){
			sb.append(",e.`anchorSum` AS renqi") ;
		}else{
			sb.append(", SUM(a.`renqi`) AS renqi ");
		}
		sb.append(" FROM t_stat_renqi a  ")
		.append(" LEFT JOIN t_user_info b ON b.`userId` = a.`userId` ")
		.append(" LEFT JOIN t_user_anchor c ON c.`userId` = a.`userId` ")
		.append(" LEFT JOIN t_user_account d ON d.`userId` = a.`userId` ")
		.append(" LEFT JOIN t_gift_freetype_record e ON e.`userId` = a.`userId` ")
		//.append(" LEFT JOIN (SELECT COUNT(*) attentionCount ,toUserId FROM t_user_attention group by toUserId").append(") attention on attention.toUserId = b.userId ")
		.append(" WHERE 1=1  ")
		.append( conSqlString )
		.append(" AND (c.`isForbidden` = 0 OR c.`isForbidden` IS NULL) AND c.`anchorStatus` = 0 ")
		.append(" AND a.`userId` != '104732' ")
		.append(" GROUP BY a.`userId`  ");
		if("t".equals(rankKind) || rankKind == "t"){
			sb.append(" ORDER BY e.`anchorSum` DESC  ");
		}else{
			sb.append(" ORDER BY SUM(a.`renqi`) DESC  ");
		}
		return sb.toString();
	}
	/**
	 * 幸运屋sql
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param toUserId 用户id
	 * @param rankKind 类别
	 * @return sql
	 */
	private String getLuckyGiftHouseSQL(String startTime, String endTime,String toUserId, String rankKind) {
		//幸运屋查询语句
		StringBuffer sb = new StringBuffer();
		// 条件
		String conSqlString = "";
		if(!StringUtils.isEmpty(startTime)){
			conSqlString += " and a.timeframe >= '"+startTime+"' ";
		}
		if(!StringUtils.isEmpty(endTime)){
			conSqlString += " and a.timeframe <= '"+endTime+"' ";
		}
		
		/*sb.append("SELECT a.`userId`,a.`nickName`,a.icon,a.`remark`,acc.anchorLevel, acc.userLevel,");
		sb.append("  b.`roomId`,SUM(c.`prizeGold`) prizeGold ");
		sb.append("FROM t_user_info a  ");
		sb.append("  JOIN t_user_anchor b  ON (a.`userId` = b.`userId`)  ");
		sb.append("  JOIN t_user_account acc ON (a.userId = acc.userId) ");
		sb.append("  JOIN `t_activities_luckygift_prize_his` c ON(a.`userId`=c.`anchorUserId`) ");
		sb.append("  WHERE 1=1 "+conSqlString);
		sb.append(" GROUP BY c.`anchorUserId` ");
		sb.append(" ORDER BY prizeGold DESC ");*/
		
		sb.append("SELECT a.userId,b.`nickName`,b.icon,");
		sb.append("  b.`remark`,c.anchorLevel, c.userLevel,d.`roomId`,SUM(a.`prizeGold`) prizeGold ");
		sb.append(" FROM `t_stat_xingyun` a");
		sb.append(" JOIN t_user_info b ON (a.userId = b.`userId`)   ");
		sb.append(" JOIN t_user_account c ON(a.userId=c.`userId`) ");
		sb.append(" JOIN t_user_anchor d ON(a.userId=d.`userId`) ");
		sb.append(" WHERE a.totalType = 0 "+conSqlString);
		sb.append(" GROUP BY a.`userId` ");
		sb.append(" ORDER BY prizeGold DESC ");
		
		return sb.toString();
	}
	/**
	 * 幸运君sql
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param toUserId 用户id
	 * @param rankKind 类别
	 * @return sql
	 */
	private String getLuckyGiftJunSQL(String startTime, String endTime,String toUserId, String rankKind) {
		//幸运君查询语句
		StringBuffer sb = new StringBuffer();
		// 条件
		String conSqlString = "";
		if(!StringUtils.isEmpty(startTime)){
			conSqlString += " and a.timeframe >= '"+startTime+"' ";
		}
		if(!StringUtils.isEmpty(endTime)){
			conSqlString += " and a.timeframe <= '"+endTime+"' ";
		}
		
		/*sb.append("SELECT a.`userId`,a.`nickName`,a.icon,a.`remark`,acc.anchorLevel, acc.userLevel,");
		sb.append("  b.`roomId`,SUM(c.`prizeGold`) prizeGold ");
		sb.append(" FROM t_user_info a  ");
		sb.append("  LEFT JOIN t_user_anchor b  ON (a.`userId` = b.`userId`)  ");
		sb.append("  JOIN t_user_account acc ON (a.userId = acc.userId) ");
		sb.append("  JOIN `t_activities_luckygift_prize_his` c ON(a.`userId`=c.`userId`) ");
		sb.append("  WHERE 1=1 "+conSqlString);
		sb.append(" GROUP BY c.`userId` ");
		sb.append(" ORDER BY prizeGold DESC ");*/
		
		sb.append("SELECT a.userId,b.`nickName`,b.icon,");
		sb.append("  b.`remark`,c.anchorLevel, c.userLevel,SUM(a.`prizeGold`) prizeGold ");
		sb.append(" FROM `t_stat_xingyun` a");
		sb.append(" JOIN t_user_info b ON (a.userId = b.`userId`)   ");
		sb.append(" JOIN t_user_account c ON(a.userId=c.`userId`) ");
		sb.append(" WHERE a.totalType = 1 "+conSqlString);
		sb.append(" GROUP BY a.`userId` ");
		sb.append(" ORDER BY prizeGold DESC ");
		
		return sb.toString();
	}
	
	private String getNewStarMeiliRankSQL(String startTime,String endTime,String toUserID,String rankKind){
		StringBuffer sb = new StringBuffer();
		
		// 本周：周日到当前
		// 上周：上周日到本周六
		String conSqlString = "";
		if(!StringUtils.isEmpty(startTime)){
			conSqlString += " and fuhao.timeframe >= '"+startTime+"' ";
		}
		if(!StringUtils.isEmpty(endTime)){
			conSqlString += " and fuhao.timeframe <= '"+endTime+"' ";
		}
//		sb.append("SELECT c.anchorPoint sum, a.userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,SUM(a.diamond) diamond,b.icon,b.remark");
//		sb.append(" FROM t_stat_meili a " );
//		sb.append("LEFT JOIN t_user_info b ON (a.userId=b.userId) " );
//		sb.append(" LEFT JOIN t_user_account c ON (a.userId=c.userId)" );
//		sb.append(" LEFT JOIN t_user_anchor d ON (a.userId= d.userId)" );
//		sb.append(" WHERE 1=1 ").append(conSqlString) ;
//		sb.append(" AND  d.anchorStatus=0  AND (d.isForbidden = 0 or d.isForbidden is null) ");//anchorStatus:主播状态，0-正常; 1-停用; 2-待审核; 3-审核不通过    isForbidden:是否已被禁播:0-未被禁播，1-已被禁播
//		sb.append(" GROUP BY a.userId ");
//		if("t".equals(rankKind)){
//			sb.append(" ORDER BY c.anchorPoint DESC");
//		}else{
//			sb.append(" ORDER BY SUM(a.diamond) DESC");
//		}
		sb.append("SELECT a.diamond sum, a.toUserId userId,b.nickname nickName,c.userLevel,c.anchorLevel,d.roomId,a.diamond diamond,b.icon,b.remark")
		.append(" FROM ( ")
		.append(" SELECT toUserId,SUM(gold) AS diamond FROM t_stat_fuhao fuhao ")
		.append(" WHERE 1=1 ").append(conSqlString)
		.append(" AND fuhao.userId != '104573' AND fuhao.userId != '102136' ")
		.append(" GROUP BY 1 ")
		.append(" ) a ") ;
		String time = DateUntil.format2Str(new Date(), "yyyyMMdd");
		if("lw".equals(rankKind)) {
			time = startTime;
		}
		sb.append(" INNER JOIN t_user_anchor d ON d.userId=a.toUserId AND d.actTime > ")
		.append(" DATE_SUB( ").append(time).append(" ,INTERVAL 1 MONTH) ")
		.append(" LEFT JOIN t_user_info b ON (a.toUserId=b.userId) ")
		.append(" LEFT JOIN t_user_account c ON (a.toUserId=c.userId) ")
		.append(" WHERE d.anchorStatus=0  AND (d.isForbidden = 0 OR d.isForbidden IS NULL)	 ")
		.append(" ORDER BY diamond DESC ");
		return sb.toString();
	}
}
