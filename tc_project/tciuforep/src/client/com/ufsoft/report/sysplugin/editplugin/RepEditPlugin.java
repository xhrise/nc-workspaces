package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;
/**
 * v56�µı༭���
 * @author wangyga
 * @created at 2009-7-17,����02:33:03
 *
 */
public class RepEditPlugin extends AbstractPlugin {

	static{
		CellRenderAndEditor.registRender(BorderPlayRender.class, new BorderPlayRender());//ע����Ⱦ��	
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
