package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;

/**
 * 公式节点(公式定义、公式计算)的描述
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
		ICommandExt fmlDefExt = new AreaFormulaDefExt(pi);// 公式定义
		return new IExtension[] { fmlDefExt };
	}

}
