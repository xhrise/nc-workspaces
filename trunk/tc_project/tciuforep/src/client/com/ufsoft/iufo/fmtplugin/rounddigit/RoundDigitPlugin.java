package com.ufsoft.iufo.fmtplugin.rounddigit;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaRender;
import com.ufsoft.table.re.CellRenderAndEditor;
/**
 * 舍位区域插件
 * @created at 2009-4-23,下午02:28:32
 * @since v56
 */
public class RoundDigitPlugin extends AbstractPlugin{
	
	public static final String GROUP = "rounddigit";
	
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new RoundDigitAreaRender());
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] {
				new RoundDigitSetAction(),//设置舍位区
				new RoundDigitCancelAction(),//设置非舍位区
				new RoundDigitDisplayAction()//显示非舍位区
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

}
