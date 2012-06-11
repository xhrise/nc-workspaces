package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufida.report.anareport.applet.AnaReportFieldAction;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.edit.EditClearExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

public abstract class AnaEditClearExt extends EditClearExt {
	public AnaEditClearExt(UfoReport report) {
		super(report);
	}

	public boolean isEnabled(Component focusComp) {
		return StateUtil.isAreaSel(getReport(), focusComp);
	}

	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				doAnaClear();
				getAnaRepPlugin().getModel().getFormatModel().clearArea(getClearType(),getSelectedArea());
				if(!isFormatState())
					getAnaRepPlugin().refreshDataModel(true);
			}

		};
	}

	public void doAnaClear() {
	}

	protected void clearAnaField(UfoReport report) {
		AnaReportPlugin anaPlugin = getAnaRepPlugin();
		UFOTable table = report.getTable();
		AnaReportFieldAction.removeFlds(anaPlugin.getModel(), getSelectedCells(),null);
		
		//包装拷贝事件.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				getSelectedArea());
		if (table.checkEvent(event)) {
			table.fireEvent(event);
		}
	}

	private AnaReportPlugin getAnaRepPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private CellPosition[] getSelectedCells(){
		AreaPosition[] areas = getSelectedArea();
		if(areas == null || areas.length == 0)
			return null;
		
		return areas[0].split();
	}
	
	private AreaPosition[] getSelectedArea(){
		AnaReportPlugin anaPlugin = getAnaRepPlugin();
		AreaPosition[] areas = anaPlugin.getCellsModel().getSelectModel().getSelectedAreas();
		AnaReportModel model = anaPlugin.getModel();
		if(!isFormatState()){
			areas = model.getFormatAreas(model.getDataModel(), areas);
		}	
		return areas;
	}
	
	private boolean isFormatState(){
		AnaReportPlugin anaPlugin = getAnaRepPlugin();
		return anaPlugin.getModel().isFormatState();
	}
}

class AnaEditClearAllExt extends AnaEditClearExt {

	public AnaEditClearAllExt(UfoReport report) {
		super(report);
	}

	public void doAnaClear() {
		clearAnaField(getReport());
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("miufo1000362");// "全部";
	}

	protected int getClearType() {
		return CellsModel.CELL_ALL;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo1000362"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"), MultiLang.getString("miufo1000103") });
		uiDes.setGroup(MultiLang.getString("edit"));
		return new ActionUIDes[] { uiDes };
	}

}

class AnaEditClearContentExt extends AnaEditClearExt {

	public AnaEditClearContentExt(UfoReport report) {
		super(report);
	}

	public void doAnaClear() {
		clearAnaField(getReport());
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0001001");// "仅内容";
	}

	/*
	 * @see com.ufsoft.report.sysplugin.EditClearExt#getClearType()
	 */
	protected int getClearType() {
		return CellsModel.CELL_CONTENT;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("uiuforep0001001"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"), MultiLang.getString("miufo1000103") });
		uiDes.setGroup(MultiLang.getString("edit"));
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("miufo1000103"));
		uiDes1.setImageFile("reportcore/clear.gif");
		uiDes1.setToolBar(true);
		uiDes1.setGroup(MultiLang.getString("edit"));
		uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		ActionUIDes uiDes2 = new ActionUIDes();
		uiDes2.setName(MultiLang.getString("miufo1000103"));
		uiDes2.setImageFile("reportcore/clear.gif");
		uiDes2.setPopup(true);
		uiDes2.setGroup(MultiLang.getString("edit"));

		return new ActionUIDes[] { uiDes, uiDes1, uiDes2 };
	}
}

class AnaEditClearFormatExt extends AnaEditClearExt {

	public AnaEditClearFormatExt(UfoReport report) {
		super(report);
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0001002");// "仅格式";
	}

	/*
	 * @see com.ufsoft.report.sysplugin.EditClearExt#getClearType()
	 */
	protected int getClearType() {
		return CellsModel.CELL_FORMAT;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("uiuforep0001002"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"), MultiLang.getString("miufo1000103") });
		uiDes.setGroup(MultiLang.getString("edit"));
		return new ActionUIDes[] { uiDes };
	}
}
