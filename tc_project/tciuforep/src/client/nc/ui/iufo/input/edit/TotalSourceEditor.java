package nc.ui.iufo.input.edit;

import nc.ui.iufo.input.control.RepDataControler;

import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.inputplugin.biz.UfoTotalSourcePlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoExcelExpPlugin;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;

public class TotalSourceEditor extends ReportDesigner {
	private static final long serialVersionUID = 2072403680434984844L;

	public boolean isDirty() {
		return false;
	}

	public String[] createPluginList() {
		return new String[]{PrintPlugin.class.getName(),UfoExcelExpPlugin.class.getName(),
				HelpPlugin.class.getName(),MonitorPlugin.class.getName(),UfoTotalSourcePlugin.class.getName()};
	}

	protected void shutdown() {
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		RepDataEditor editor=controler.getLastActiveRepDataEditor();
		//关闭汇总结果溯源窗口时，需要打开最近一次打开的报表数据窗口
		if (editor!=null){
			getMainboard().openView(RepDataEditor.class.getName(), editor.getId());
		}		
	}

	protected boolean save() {
		return false;
	}
}
