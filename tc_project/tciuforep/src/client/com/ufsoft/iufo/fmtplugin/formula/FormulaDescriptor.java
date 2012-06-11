package com.ufsoft.iufo.fmtplugin.formula;


import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
/**
 * 插件Demo。公式节点的描述
 * @author wupeng
 * 2004-8-30
 */
public class FormulaDescriptor extends AbstractPlugDes {

	public FormulaDescriptor(FormulaDefPlugin pi){
		super(pi);
	}

	public String getName() {
		return "formula";
	}

	public String getNote() {
		return "formula";
	}

	public IExtension[] createExtensions() {		
		ICommandExt cellDefExt = new CalculateFmlExt();//单元公式
		ICommandExt fmlTransExt = new FormulaTransExt();//公式套用
		ICommandExt checkFmlExt = new CheckFmlExt(this);//审核公式
		ICommandExt batchFmlExt = new BatchFmlExt(this);//批量公式
		FormulaRendererExt fmlRendererExt = new FormulaRendererExt(this);//公式扩展属性是否渲染

		ICommandExt[] exts = {cellDefExt, checkFmlExt, batchFmlExt, fmlTransExt, 
				fmlRendererExt};
		return exts;
	  
	}
}
