
package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 单元定位的命令。
 * @author 王宇光
 * 2008-5-19
 */
public class GotoCommand extends UfoCommand {

	/**
	 * 报表
	 */
	private UfoReport m_Report ;


	 /**
	  * @param rep
	  * 
	  */
	 public GotoCommand(UfoReport rep) {
		 super();
		 m_Report = rep;
	 }
	 
	/**
	 * 定位单个单元格
	 * 如果单元位置字符串非法或者目标单元超出当前报表中实际有的单元，则认为引用无效 
	 * @param params[0] : 状态对象执行器
	 * 
	 */
	public void execute(Object[] params) {
		if (params == null || params.length == 0) {
			return;
		}
		AbsLocation absLocation = (AbsLocation) params[0];
		if (absLocation != null) {
			absLocation.location();
		}    
	}	

    /**
     * @return 返回 UfoReport。
     */
    public UfoReport getReport() {
        return m_Report;
    }
}
