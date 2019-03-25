package com.lm.live.common.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/***
 * 此类用于分页
 * 
 * @author ADMINI
 * 
 */
@SuppressWarnings("all")
public class Page<T> implements Serializable {
	
	private List<T> list; // 要返回的某一页的记录列表 (数据)
	private int allRow; // 总记录数
	private int totalPage; // 总页数
	private int pageNo; // 当前页码
	private int pageSize; // 每页记录数
	private boolean isFirstPage; // 是否为第一页
	private boolean isLastPage; // 是否为最后一页
	private boolean hasPreviousPage; // 是否有前一页
	private boolean hasNextPage; // 是否有下一页

	public Page(){}
	
	public Page(List<T> list, int allRow, int pageNo, int pageSize) {
		if(list == null) {
			list = Collections.emptyList();
		} else {
			this.list = list;
		}
		if(pageSize == 0) pageSize = 10;
		this.allRow = allRow;
		this.allRow = allRow;
		this.totalPage = allRow % pageSize == 0 ? allRow / pageSize : allRow / pageSize + 1; // 计算总页数
		this.pageNo = pageNo == 0 ? 1 : pageNo;
		this.pageSize = pageSize;
		this.init();
	}

	// 初始化分页信息
	public void init() {
		this.isFirstPage = isFirstPage();
		this.isLastPage = isLastPage();
		this.hasPreviousPage = isHasPreviousPage();
		this.hasNextPage = isHasNextPage();
	}

	// get,set方法
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getAllRow() {
		return allRow;
	}

	public void setAllRow(int allRow) {
		this.allRow = allRow;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// is方法相当于get方法
	public boolean isFirstPage() {
		return pageNo == 1; // 如是当前页是第1页
	}

	public boolean isLastPage() {
		return pageNo == totalPage; // 如果当前页是最后一页
	}

	public boolean isHasPreviousPage() {
		return pageNo != 1; // 只要当前页不是第1页
	}

	public boolean isHasNextPage() {
		return pageNo != totalPage; // 只要当前页不是最后1页
	}

	@Override
	public String toString() {
		return "Page [list=" + list + ", allRow=" + allRow + ", totalPage="
				+ totalPage + ", currentPage=" + pageNo + ", pageSize="
				+ pageSize + ", isFirstPage=" + isFirstPage + ", isLastPage="
				+ isLastPage + ", hasPreviousPage=" + hasPreviousPage
				+ ", hasNextPage=" + hasNextPage + "]";
	}
	
	public void setPage(Integer page){
		this.pageNo = page;
	}
	
	public void setRows(Integer rows) {
		this.pageSize = rows;
	}

}
