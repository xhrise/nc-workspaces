package com.ufsoft.report.plugin;

/**
 * 扩展可以继承这个接口，来表示插件的一些特殊特性。
 * 
 * @author zzl 2005-4-11
 */
public interface IValidate {

	/**
	 * 用于插件初始化时，根据场景确定是否支持。
	 * 
	 * @return boolean
	 */
	public boolean isSupport();
}
