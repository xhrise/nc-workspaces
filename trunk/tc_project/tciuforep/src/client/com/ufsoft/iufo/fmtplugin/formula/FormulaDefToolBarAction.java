package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.inputplugin.formula.FormulaInputToolBarAction;
/**
 * IUFO ��ʽ�˵�������չ�����Ϊ��ʽ�˵������빫ʽ����̬���<code>FormulaPlugin</code>�ṩ���뷽ʽ��
 * @author zhaopq
 * @created at 2009-4-9,����03:05:08
 * @since v5.6
 */
public class FormulaDefToolBarAction extends FormulaInputToolBarAction {

	@Override
	protected FormulaDefToolBar createToolBar() {
		return new FormulaDefToolBar(getMainboard());
	}
}
