package com.ufsoft.iufo.inputplugin.formula;

import com.ufsoft.report.fmtplugin.formula.AreaFormulaToolBarAction;

/**
 * IUFO ��ʽ�˵�������չ�����Ϊ��ʽ�˵�����������¼��̬��ʽ���<code>FormulaInputPlugin</code>�ṩ���뷽ʽ��
 * 
 * @author zhaopq
 * @created at 2009-4-15,����03:05:08
 * @since v5.6
 */
public class FormulaInputToolBarAction extends AreaFormulaToolBarAction {
	@Override
	protected FormulaInputToolBar createToolBar() {
		return new FormulaInputToolBar(getMainboard());
	}
}