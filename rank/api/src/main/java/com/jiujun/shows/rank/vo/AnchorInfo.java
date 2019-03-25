package com.jiujun.shows.rank.vo;

import com.jiujun.shows.common.vo.BaseAnchorInfo;

public class AnchorInfo extends BaseAnchorInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6907709631475175002L;

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

}