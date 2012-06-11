package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.UfoReport;
/**
 * ����������˷�������Ĺ�ʽ׷��ҵ��ӿ�
 * @author liulp
 *
 */
public interface IFormulaTraceBizLink {
	/**
	 * ���㹫ʽ�����ֵ
	 * @param ufoReport
	 * @param formulaParsedDataItem
	 * @return
	 */
	public Object getCalulatedValue(UfoReport ufoReport,IFormulaParsedDataItem formulaParsedDataItem);
	/**
	 * �õ���ʽ����������ֵ��Ϣ
	 * @param ufoReport
	 * @param formulaParsedDataItem
	 * @return
	 */
	public IFormulaTraceValueItem[] getTraceMultiValues(UfoReport ufoReport,IFormulaParsedDataItem formulaParsedDataItem);
	/**
	 * ׷�ٵ�ĳ�����е�Ŀ�걨�������ǰ�û�������Ȩ����򿪣�����û������ʾ������Ȩ��
	 * @param ufoReport
	 * @param formulaTraceValueItem
	 */
	public void doTraceValue(UfoReport ufoReport,IFormulaTraceValueItem formulaTraceValueItem);

}
