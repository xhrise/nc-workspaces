package com.ufida.report.anareport.edit;

import java.awt.Component;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

public class AnaReportOrderMngExt extends AbsActionExt {

	private AnaReportPlugin m_plugin = null;
	
	public AnaReportOrderMngExt(AnaReportPlugin plugin){
		super();
		m_plugin = plugin;
	}
	
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		AnaOrderManageDlg dlg=new AnaOrderManageDlg(container,m_plugin);
		dlg.show();
		return null;
	}

	/**
	 * @i18n iufobi00027=ÅÅÐò¹ÜÀí
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes mainMenu = new ActionUIDes();
		mainMenu.setName(StringResource.getStringResource("iufobi00027"));
		mainMenu.setPaths(new String[] { MultiLang.getString("data")});
		ActionUIDes uiDes1=(ActionUIDes)mainMenu.clone();
		uiDes1.setPopup(true);
		uiDes1.setPaths(new String[]{});
		uiDes1.setGroup(StringResource.getStringResource("miufo00178"));
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup(StringResource.getStringResource("miufo00178"));
		uiDes3.setPaths(new String[] {});
		uiDes3.setName(StringResource.getStringResource("iufobi00027"));
		uiDes3.setTooltip(StringResource.getStringResource("iufobi00027"));
		uiDes3.setImageFile("reportcore/SortManager.gif");
		return new ActionUIDes[] { mainMenu,uiDes1,uiDes3 };
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		ExAreaCell[] exCells=ExAreaModel.getInstance(m_plugin.getModel().getFormatModel()).getExAreaCells();
		if(exCells!=null&&exCells.length>0){
			AreaDataModel areaData=null;
			for(int i=0;i<exCells.length;i++){
				if(exCells[i].getModel() instanceof AreaDataModel){
					areaData=(AreaDataModel)exCells[i].getModel();
					if (!areaData.isCross()
							&& exCells[i].getExAreaType() != ExAreaCell.EX_TYPE_CHART
							&& areaData.getDSTool() != null
							&& areaData.getDSTool().isSupport(
									DescriptorType.SortDescriptor)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	
}
 