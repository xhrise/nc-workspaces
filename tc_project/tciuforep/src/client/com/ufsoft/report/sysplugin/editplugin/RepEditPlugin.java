package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;
/**
 * v56ÐÂµÄ±à¼­²å¼þ
 * @author wangyga
 * @created at 2009-7-17,ÏÂÎç02:33:03
 *
 */
public class RepEditPlugin extends AbstractPlugin {

	static{
		CellRenderAndEditor.registRender(BorderPlayRender.class, new BorderPlayRender());//×¢²áäÖÈ¾Æ÷	
	}
	
	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { 				
				new CutAllAction(),
				new CopyAllAction(), 
				new PasteAction(),
				new ClearAllAction(), new ClearContentAction(),
				new ClearFormatAction(), new FormatBrushAction(this)
				};
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub

	}

}
