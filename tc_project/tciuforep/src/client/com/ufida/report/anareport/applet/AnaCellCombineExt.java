package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.ufida.report.anareport.model.AnaCellCombine;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

/**
 * 分析表中单元的自动合并属性的设置
 * 
 * @author ll
 * 
 */
public class AnaCellCombineExt extends AbsActionExt {
	/**
	 * @i18n miufo00176=自动合并
	 */
	private static final String RESID_AUTO_COMBINE = "miufo00176";
	/**
	 * @i18n miufo00177=取消自动合并
	 */
	private static final String RESID_CANCEL_COMBINE = "miufo00177";
	/**
	 * @i18n miufo00178=字段设置
	 */
	private static final String RESID_SETFIELD = "miufo00178";
	
	private String autoCombineImage = "reportcore/auto_combine.gif";
	private String cancelCombine = "reportcore/cancel_combine.gif";
	private AnaReportPlugin m_plugin = null;
	private boolean setCombine = true;

	public AnaCellCombineExt(AnaReportPlugin plugin, boolean isSetCombine) {
		super();
		m_plugin = plugin;
		setCombine(isSetCombine);
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				if (params == null)
					return;
				setCombineType(m_plugin.getModel().getFormatModel());					
			}
		};
	}

	
	@Override
	public void initListenerByComp(Component stateChangeComp) {
		if(!(stateChangeComp instanceof JButton))
			return;
		final JButton toolButton = (JButton)stateChangeComp;
		m_plugin.getModel().getCellsModel().getSelectModel().addSelectModelListener(
				new SelectListener(){
					public void selectedChanged(SelectEvent e) {
						AnaReportModel model = m_plugin.getModel();
						CellsModel cellsModel = model.getFormatModel();
						CellPosition cellPos = cellsModel.getSelectModel().getAnchorCell();
						if(!model.isFormatState()){
							CellsModel dataModel = model.getDataModel();
							CellPosition[] selectCells = model.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
							if(selectCells != null && selectCells.length >0)
								cellPos = selectCells[0];
						}						
						Cell cell = cellsModel.getCell(cellPos);
						if (cell == null)
							return;
						Object autoCombine = cell.getExtFmt(AnaCellCombine.KEY);							
						if(autoCombine != null){
							setCombine(false);
							toolButton.setName(StringResource.getStringResource(RESID_CANCEL_COMBINE));
							toolButton.setToolTipText(StringResource.getStringResource(RESID_CANCEL_COMBINE));
							toolButton.setIcon(ResConst.getImageIcon(cancelCombine));
						} else{
							setCombine(true);
							toolButton.setName(StringResource.getStringResource(RESID_AUTO_COMBINE));
							toolButton.setToolTipText(StringResource.getStringResource(RESID_AUTO_COMBINE));
							toolButton.setIcon(ResConst.getImageIcon(autoCombineImage));
						}	
					}
					
				});
		toolButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(isSetCombine()){
					setCombine(false);
					toolButton.setName(StringResource.getStringResource(RESID_CANCEL_COMBINE));
					toolButton.setToolTipText(StringResource.getStringResource(RESID_CANCEL_COMBINE));
					toolButton.setIcon(ResConst.getImageIcon(cancelCombine));
				} else{
					setCombine(true);
					toolButton.setName(StringResource.getStringResource(RESID_AUTO_COMBINE));
					toolButton.setToolTipText(StringResource.getStringResource(RESID_AUTO_COMBINE));
					toolButton.setIcon(ResConst.getImageIcon(autoCombineImage));
				}
				
				if(!m_plugin.getModel().isFormatState())
					m_plugin.refreshDataModel(false);
			}
			
		});
		
	}

	@Override
	public Object[] getParams(UfoReport container) {
		// TODO Auto-generated method stub
		return new Object[] { container };
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		if(!StateUtil.isAreaSel(m_plugin.getReport(), focusComp))
			return false;
		AnaReportModel model = m_plugin.getModel();
		CellPosition anchorCellPos = model.getCellsModel().getSelectModel().getAnchorCell();
		if(!model.isFormatState()){
			CellPosition[] dataStateCells = model.getFormatPoses(model.getDataModel(), new AreaPosition[]{AreaPosition.getInstance(anchorCellPos, anchorCellPos)});
			if(dataStateCells != null && dataStateCells.length > 0)
				anchorCellPos = dataStateCells[0];
		}
			
		Cell cell = model.getFormatModel().getCell(anchorCellPos);
		if(cell == null)
			return false;
		if(cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null)
			return true;
		else
			return false;	
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes.setPaths(new String[] {});
		if (isSetCombine()) {
			uiDes.setName(StringResource.getStringResource(RESID_AUTO_COMBINE));
			uiDes.setTooltip(StringResource.getStringResource(RESID_AUTO_COMBINE));
			uiDes.setImageFile(autoCombineImage);
		} else {
			uiDes.setName(StringResource.getStringResource(RESID_CANCEL_COMBINE));
			uiDes.setTooltip(StringResource.getStringResource(RESID_CANCEL_COMBINE));
			uiDes.setImageFile(cancelCombine);
		}
		return new ActionUIDes[] { uiDes };

	}

	// 执行区域字段的属性设置
	private void setCombineType(CellsModel model) {
		CellPosition[] area = getSelectedCells();
		if (area == null || area.length == 0)
			return;

		for (CellPosition pos : area) {
			Cell cell = model.getCell(pos);
			if (cell != null) {
				if (isSetCombine())
					cell.addExtFmt(AnaCellCombine.KEY, new AnaCellCombine());
				else
					cell.removeExtFmt(AnaCellCombine.KEY);
			}
		}
	}

	private CellPosition[] getSelectedCells(){
		AnaReportModel model = m_plugin.getModel();
		CellsModel formatModel = model.getFormatModel();
		CellsModel dataModel = model.getDataModel();
		CellPosition[] selectCells = formatModel.getSelectModel().getSelectedCells();
		if (!model.isFormatState()){
			selectCells = model.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}	
		return selectCells;
	}
	
	private boolean isSetCombine() {
		return setCombine;
	}

	private void setCombine(boolean setCombine) {
		this.setCombine = setCombine;
	}
}
 