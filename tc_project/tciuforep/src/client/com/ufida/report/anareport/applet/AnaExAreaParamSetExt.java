package com.ufida.report.anareport.applet;

import java.awt.Component;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.Parameter;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaParameter;
import com.ufida.report.anareport.model.ReportParameter;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;


public class AnaExAreaParamSetExt extends AbsActionExt {
	private AnaReportPlugin m_plugin = null;
	private ExAreaModel model;
	
	public AnaExAreaParamSetExt(AnaReportPlugin plugin){
		this.m_plugin=plugin;
	}
	
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		ExAreaCell selExArea = getSelectedEx();
		if (selExArea != null && selExArea.getModel() instanceof AreaDataModel) {
			AreaDataModel areaData = (AreaDataModel) selExArea.getModel();

			AreaParameter paramsDef = areaData.getAreaParams();
			if (paramsDef != null) {
				AnaExAreaParamSetDlg paramDlg = new AnaExAreaParamSetDlg(container, selExArea,areaData,paramsDef);
				paramDlg.show();
				if (paramDlg.getResult() == UfoDialog.ID_OK) {
					if (paramDlg.getParamSetPanel().updateInfo()) {
						ReportParameter rptPrms = m_plugin.getModel()
								.getReportParams(false);
						if (rptPrms == null) {
							rptPrms = m_plugin.getModel().getReportParams(true);
						}
						m_plugin.getModel().updateAreaParams(paramDlg.getParamSetPanel().getParames());
						m_plugin.refreshDataModel(true);
					}
				}
			}

		}
		return null;
	}

	/**
	 * @i18n uiufo20043=²ÎÊý
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes mainMenu = new ActionUIDes();
		mainMenu.setName(StringResource.getStringResource("uiufo20043"));
		mainMenu.setPaths(new String[] {MultiLang.getString("data")});
		ActionUIDes uiDes2 = new ActionUIDes();
		uiDes2.setToolBar(true);
		uiDes2.setGroup(StringResource.getStringResource("miufo00178"));
		uiDes2.setPaths(new String[] {});
		uiDes2.setName(StringResource.getStringResource("uiufo20043"));
		uiDes2.setTooltip(StringResource.getStringResource("uiufo20043"));
		uiDes2.setImageFile("reportcore/param.gif");
		return new ActionUIDes[] {mainMenu,uiDes2};
	}
    
	
	@Override
	public boolean isEnabled(Component focusComp) {
		boolean hasParam = false;
		ExAreaCell selExArea = getSelectedEx();
		if (selExArea != null && selExArea.getModel() instanceof AreaDataModel) {
			AreaDataModel areaData = (AreaDataModel) selExArea.getModel();
			if (areaData.getDSTool() != null) {
				DataSet ds = areaData.getDSTool().getDSDef();
				if (ds != null) {
//					if (ds
//							.supportDescriptorFunc(DescriptorType.FilterDescriptor)) {
						Parameter[] params = ds.getUsedParameters();
						if (params != null && params.length > 0) {
							hasParam = true;
						}
//					}
				}
			}
		}
		return hasParam;
	}

	private ExAreaCell getSelectedEx() {
		ExAreaCell cell = null;
		AnaReportModel anaModel = m_plugin.getModel();
		CellsModel model = m_plugin.getCellsModel();
		AreaPosition[] selCells =model.getSelectModel().getSelectedAreas();
		if (!anaModel.isFormatState()) {
			CellPosition[] anaCells=anaModel.getFormatPoses(model, selCells);
			if(anaCells!=null&&anaCells.length>0&&anaCells[0]!=null){
				selCells[0]=AreaPosition.getInstance(anaCells[0], anaCells[0]);
			}else{
				selCells[0]=null;
			}
		}
		if (selCells[0] != null) {
			ExAreaCell[] cells = getExAreaModel().getExAreaCells();
			for (int i = 0; i < cells.length; i++) {
				AreaPosition area = cells[i].getArea();
				if (selCells[0].intersection(area)) {
					cell = cells[i];
					break;
				}
			}
		}
		return cell;
	}
    
	public ExAreaModel getExAreaModel() {
		if (model == null) {
			model = ExAreaModel.getInstance(m_plugin.getModel().getFormatModel());
		}
		return model;
	}
}
 