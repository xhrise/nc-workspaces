package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;

import nc.pub.iufo.cache.RepFormatModelCache;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.DefaultSheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

public class AreaPosRefRightPanelSample extends AreaPosRefRightPanel {
	private static final long serialVersionUID = 3834704046365332194L;

	public AreaPosRefRightPanelSample(AreaPosRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeCellPosList) {
		super(parentDlg, isContainsCurrentReport, currentKeyGroupVO, excludeCellPosList);
		setLayout(new BorderLayout());
	}

	/**
	 * @i18n uiiufofmt00013=无匹配指标！
	 */
	protected void changeReportImpl(ReportVO reportVO) {
		RepFormatModelCache repFormatModelCache = CacheProxy.getSingleton().getRepFormatCache();
		CellsModel formatModel = repFormatModelCache.getUfoTableFormatModel(reportVO.getReportPK());
		formatModel = (CellsModel) formatModel.clone();
		UFOTable table = UFOTable.createTableByCellsModel(formatModel);
		initReportRenderAndEditor(table.getReanderAndEditor());
		removeAll();
		add(table,BorderLayout.CENTER);
		((JComponent)getParent()).revalidate();
	}

	private void initReportRenderAndEditor(CellRenderAndEditor renderAndEditor) {
		//指标显示为"指标名称"。
		renderAndEditor.registExtSheetRenderer(new SheetCellRenderer(){
			JLabel label = new nc.ui.pub.beans.UILabel();
			public Component getCellRendererComponent(CellsPane cellsPane,Object value,  boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
				CellsModel cellsModel = cellsPane.getDataModel();
				MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
				MeasureVO measureVO = measureModel.getMeasureVOByPos(CellPosition.getInstance(row,column));
				if(measureVO != null){
					String meausreName = measureVO.getName();	
					label.setText("<"+meausreName+">");
					return label;
				}else{
					return null;
				}
				
			}			
		});
		//编辑器判断当前为有效单元编号时，返回当前选择单元
		renderAndEditor.registExtSheetEditor(new DefaultSheetCellEditor(new nc.ui.pub.beans.UILabel()){
		    public Component getTableCellEditorComponent(CellsPane table, Object value,
		            boolean isSelected, int row, int column) {		    	
		    	if(isAutoClose()){
		    		CellPosition selVO = getSelectedCellPosition();
		    		if(selVO != null){
		    			closeWithOKResult();
		    		}
		    	}
		    	return null;
		    }
	        public int getEditorPRI() {
	        	return 10;
	        }
	    	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
				return true;
			}
		});
		
	}

	protected CellPosition getSelectedCellPosition() {
		if(getComponentCount() == 0){
			return null;
		}
		
		Component component = getComponent(0);
		if(!(component instanceof UFOTable)){
			return null;
		}
		
		UFOTable table = (UFOTable) component;
		CellsModel cellsModel = table.getCellsModel();
		CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
		return anchorPos;
	}

}
