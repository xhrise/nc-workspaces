/*
 * 创建日期 2005-7-7
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

/**
 * 表格控件处理IUFO业务的操作接口
 * @author liulp
 *
 */
public interface IInputBizOper {
	/**
	 * 处理菜单任务
	 * @param nBizType
	 * @return
	 */
	public Object performBizTask(int nBizType);
}
