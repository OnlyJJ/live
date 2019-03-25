package com.lm.live.common.utils;

import com.lm.live.common.vo.Page;

public class SQLUtil {
	
	public static String getCountBySQL(String sql){
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(*) count from ("+sql+") z");
		//LogUtil.log.info(" countSql:"+countSql.toString());
		return countSql.toString();
	}
	
	public static String getLimitSqlByPage(Page page,String sql){
		StringBuffer limitSql = new StringBuffer();
		limitSql.append(sql);
		if( null != page.getPageNum()&&Integer.parseInt(page.getPageNum())>=2){
			int limit = Integer.parseInt(null != page.getPagelimit()?page.getPagelimit():"10");
			int start = (Integer.parseInt(page.getPageNum())-1)*limit;
			//int end = Integer.parseInt(page.getPageNum())*limit;
			//limitSql.append(" limit "+start+","+end+"");
			limitSql.append(" limit "+start+","+limit+"");
		}else{
			limitSql.append(" limit 0,"+( null != page.getPagelimit()?page.getPagelimit():10));
		}
		//LogUtil.log.info(" limitSql:"+limitSql.toString());
		return limitSql.toString();
	}
}
