package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.data.CheckRenderer;
import com.ufsoft.iufo.inputplugin.biz.data.CheckResultExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceRenderer;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;

import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.re.CellRenderAndEditor;

public class InputCheckPlugIn  extends AbstractPlugIn {

	private CheckResultExt checkResultExt=null;
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new FormulaTraceRenderer(null));
	}
	
	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			 protected IExtension[] createExtensions() {
					 return new IExtension[]{getExt()};
			 }
		};
	}
	private CheckResultExt getExt(){
		if(checkResultExt==null)
			checkResultExt=new CheckResultExt(getReport());
		return checkResultExt;
	}
	public void startup(){
		if (getReport().getTable()!=null && getReport().getTable().getCells()!=null && getExt()!=null)
			getReport().getTable().getCells().registExtSheetRenderer(new CheckRenderer(getExt().getResultPanel()));
	}
}
