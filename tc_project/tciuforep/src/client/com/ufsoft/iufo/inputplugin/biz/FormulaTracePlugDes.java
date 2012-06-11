package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
/**
 * ��ʽ׷�ٵĲ������
 * @author liulp
 *
 */
public class FormulaTracePlugDes  extends AbstractPlugDes{

	public FormulaTracePlugDes(IPlugIn plugin) {
		super(plugin);
	}

	@Override
	protected IExtension[] createExtensions() {
		//��ʽ׷�ٲ˵�����չ
		IExtension formulaTraceExt = new FormulaTraceExt(getReport());
		//��ʽ׷�ٵ���������չ
		IExtension formulaTraceNavExt = new FormulaTraceNavExt(getReport());
		
		return new IExtension[]{formulaTraceExt,formulaTraceNavExt};
	}

}
