package com.lm.live.tools.tool.service;

/**
 * 工具相关业务逻辑流程
 * */
public interface ToolBusinessService {

	public void buyToolOfUser(String userId , int type , int num )throws Exception;
}
