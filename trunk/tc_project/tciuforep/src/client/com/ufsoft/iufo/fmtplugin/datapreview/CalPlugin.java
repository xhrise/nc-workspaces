package com.ufsoft.iufo.fmtplugin.datapreview;

import java.awt.event.ActionEvent;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.perfwatch.PerfWatch;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.fmtplugin.service.ReportCalcSrv;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 格式态预览的计算插件
 * @author zhaopq
 * @created at 2009-5-6,上午11:14:51
 * @since v56
 */
public class CalPlugin extends AbstractPlugin{
	
	private static final String GROUP = "caculate"; 

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] {
				new CalAction()
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}
	
	private class CalAction extends AbsIUFORptDesignerPluginAction{

		/**
		 * @i18n miufohbbb00202=报表计算[
		 */
		@Override
		public void execute(ActionEvent e) {
			
			
			Context context=(Context)getMainboard().getCurrentView().getContext();
			UfoContextVO contextVO=new UfoContextVO(context);
			
			PerfWatch pw = new PerfWatch(StringResource.getStringResource("miufohbbb00202") + contextVO.getName()  + "(" + contextVO.getReportcode() + ")]");

			IContext mainContext=getMainboard().getContext();
			contextVO.setAttribute(IUfoContextKey.DATA_SOURCE,mainContext.getAttribute(IUfoContextKey.DATA_SOURCE));
			contextVO.setAttribute(IUfoContextKey.TASK_PK, null);
			
			ReportCalcSrv reportCalcSrv = new ReportCalcSrv(contextVO,getCellsModel(),false);
			reportCalcSrv.calcAllFormula(false);
	//		CellsModelOperator.convertDataModelToLWModel(getCellsModel(), true);
			pw.stop();
			
		}

		@Override
		public IPluginActionDescriptor getPluginActionDescriptor() {
			PluginActionDescriptor descriptor = new PluginActionDescriptor();
			descriptor.setGroupPaths(MultiLang.getString("data"), GROUP);//"数据"
			descriptor.setName(MultiLangInput.getString("uiufotableinput0004"));//"计算"
			descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
			return descriptor;
		}
		
	}

}
 