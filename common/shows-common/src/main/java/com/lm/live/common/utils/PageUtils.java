package com.lm.live.common.utils;

import com.lm.live.common.vo.Page;

public class PageUtils {
	
	public static void setPage(Page page){
		if(null == page.getPageNum()) page.setPageNum("0");
		if(null == page.getPagelimit()) page.setPagelimit("10");
	}
	
	public static int getNum( int count,int limit){
		return count%limit==0?count/limit:count/limit+1;
	}
}
