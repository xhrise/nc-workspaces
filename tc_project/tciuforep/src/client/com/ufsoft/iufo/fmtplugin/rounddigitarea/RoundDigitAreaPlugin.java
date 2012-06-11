package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * ÉáÎ»²å¼þ
 * @author guogang
 *
 */
public class RoundDigitAreaPlugin extends AbstractPlugIn {
	
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new RoundDigitAreaRender());
	}
	
	@Override
	public void startup() {
//		getReport().getTable().getCells().registExtSheetRenderer(new RoundDigitAreaRender());
	}

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new RoundDigitAreaExt(getReport()),
						new RoundDigitAreaExt1(getReport()),
						new RoundDigitAreaDisExt(getReport())};
			}
			
		};
	}

	
}
