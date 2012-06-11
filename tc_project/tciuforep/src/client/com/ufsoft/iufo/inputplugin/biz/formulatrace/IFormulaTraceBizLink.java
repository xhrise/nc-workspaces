package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.UfoReport;
/**
 * 需向服务器端发出请求的公式追踪业务接口
 * @author liulp
 *
 */
public interface IFormulaTraceBizLink {
	/**
	 * 计算公式子项的值
	 * @param ufoReport
	 * @param formulaParsedDataItem
	 * @return
	 */
	public Object getCalulatedValue(UfoReport ufoReport,IFormulaParsedDataItem formulaParsedDataItem);
	/**
	 * 得到公式子项的联查多值信息
	 * @param ufoReport
	 * @param formulaParsedDataItem
	 * @return
	 */
	public IFormulaTraceValueItem[] getTraceMultiValues(UfoReport ufoReport,IFormulaParsedDataItem formulaParsedDataItem);
	/**
	 * 追踪到某任务中的目标报表，如果当前用户有数据权限则打开，不过没有则提示无数据权限
	 * @param ufoReport
	 * @param formulaTraceValueItem
	 */
	public void doTraceValue(UfoReport ufoReport,IFormulaTraceValueItem formulaTraceValueItem);

}
