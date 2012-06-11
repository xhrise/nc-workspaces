package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;

/**
 * ��ʽ�ڵ�(��ʽ���塢��ʽ����)������
 * 
 * @author chxw 2008-4-16
 */
public class AreaFormulaDescriptor extends AbstractPlugDes {
	
	private AreaFormulaPlugin pi = null;

	public AreaFormulaDescriptor(AreaFormulaPlugin pi) {
		super(pi);
		this.pi = pi;
	}

	public String getName() {
		return "formula";
	}

	public IExtension[] createExtensions() {
		ICommandExt fmlDefExt = new AreaFormulaDefExt(pi);// ��ʽ����
		return new IExtension[] { fmlDefExt };
	}

}
