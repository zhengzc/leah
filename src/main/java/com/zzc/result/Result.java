package com.zzc.result;


/**
 * 统一的值返回接口
 * @author ying
 *
 */
public interface Result {
	/**
	 * 获取token
	 * @return
	 */
	public String getToken();
	
	/**
	 * 获取返回值
	 * @return
	 */
	public Object getResult();
}
