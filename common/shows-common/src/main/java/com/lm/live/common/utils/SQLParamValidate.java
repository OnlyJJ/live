package com.lm.live.common.utils;

public class SQLParamValidate {
	public final static String validate(String str) {
		if (str == null) return "";
		str = str.replaceAll("'", "''");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		return str;
	}
}
