package com.ufida.report.anareport.applet;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.header.Header;

public class AnaFormatModelListener implements HeaderModelListener, UserActionListner {
	AnaReportPlugin m_plugin = null;

	AnaFormatModelListener(AnaReportPlugin plugin) {
		m_plugin = plugin;
	}

	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void headerCountChanged(HeaderEvent e) {
		CellsModel model = m_plugin.getModel().getFormatModel();
		if (e.isHeaderAdd()) {
			AreaPosition aimArea = null;
			if (e.isRow()) {
				int width = model.getColNum();
				aimArea = AreaPosition.getInstance(e.getStartIndex(), 0, width, e.getCount());
			} else {
				int height = model.getRowNum();
				aimArea = AreaPosition.getInstance(0, e.getStartIndex(), e.getCount(), height);
			}
			for (int i = 0; i < aimArea.getHeigth(); i++) {
				for (int j = 0; j < aimArea.getWidth(); j++) {
					CellPosition pos = aimArea.getStart().getMoveArea(i, j);
					Cell cell = model.getCell(pos);
					if (cell != null)
						cell.removeExtFmt(AnaRepField.EXKEY_FIELDINFO);
				}
			}
		}

		//处理交叉区域的头事件
		ExAreaModel exAreaModel = ExAreaModel.getInstance(model);
		ExAreaCell[] exAreaCells = exAreaModel.getExAreaCells();
		if(exAreaCells == null || exAreaCells.length == 0)
			return;
		for(ExAreaCell exAreaCell:exAreaCells){
			AreaDataModel areaDataModel = (AreaDataModel)exAreaCell.getModel();
			if(areaDataModel == null)
				continue;
			AnaCrossTableSet crossSet = areaDataModel.getCrossSet();
			if(crossSet != null){
				crossSet.moveAnaCrossSet(e.isHeaderAdd(),e.isRow(),e.getStartIndex(),e.getCount());
			}
		}
	}

	/**
	 * modify by wangyga 处理分析报表数据态改变行高列宽
	 */
	public void headerPropertyChanged(PropertyChangeEvent e) {
		if(m_plugin.getModel().isFormatState())
			return;
		if(e.getPropertyName().equals(Header.HEADER_SIZE_PROPERTY)){
			Object source = e.getSource();
			Object dragObject = e.getNewValue();
			if(dragObject == null)
				return;
			int dragIndex = 0;
			try {
				dragIndex = Integer.parseInt(dragObject.toString());
			} catch (Exception ex) {
				AppDebug.debug(ex);
				throw new IllegalArgumentException(ex);
			}
			
			if(source instanceof Header[]){
				Header[] headers = (Header[])source;
				if(headers == null || headers.length == 0)
					return;
				Header header = headers[0];
				if(header.getType() == Header.ROW){
					CellPosition pos = CellPosition.getInstance(dragIndex, 0);
					Cell cell = m_plugin.getModel().getFormatCell(m_plugin.getCellsModel(), pos);
					if(cell == null)
						return;
					getCellsModel().getRowHeaderModel().setSize(cell.getRow(), header.getSize());
				}else{
					CellPosition pos = CellPosition.getInstance(0, dragIndex);
					Cell cell = m_plugin.getModel().getFormatCell(m_plugin.getCellsModel(), pos);
					if(cell == null)
						return;
					getCellsModel().getColumnHeaderModel().setSize(cell.getCol(), header.getSize());					
				}				
				m_plugin.refreshDataModel(false);
			}
		}

	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

	public void userActionPerformed(UserUIEvent e) {
		// 处理事件。
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			processPasteEvent(e);
			break;
		}
	}

	/**
	 * modify by wangyga 2008-9-26 处理粘贴事件:此处只处理个业务插件，cell已经处理
	 *  1:处理了交叉区域
	 *  
	 * @param e
	 */
	private void processPasteEvent(UserUIEvent e) {
		if (e == null)
			return;
		Object object = e.getNewValue();
		if(!(object instanceof EditParameter))
			return;
		EditParameter parameter = (EditParameter) object;;
		if(parameter == null)
			return;
		
		int iEditType = parameter.getEditType();
		boolean isTransfer = parameter.isTransfer();
		
		AreaPosition[] newAreas = parameter.getPasteAreas();				
		AreaPosition oldArea = parameter.getCopyArea();
		
		pasteCrossArea(oldArea,newAreas,iEditType,isTransfer);
		
		//考虑复制时，选择区域在扩展区域中，但自身不包含可扩展区域
		pasteExAreaField(oldArea,newAreas,iEditType,isTransfer);
	
	}

	/**
	 * 此方法只处理粘贴时对交叉区域的处理，其他属性都有编辑插件和各个业务插件处理
	 * @param oldArea
	 * @param newAreas
	 * @param iEditType
	 */
	private void pasteCrossArea(AreaPosition oldArea,AreaPosition[] newAreas,int iEditType,boolean isTransfer){
		if(oldArea == null || newAreas == null || newAreas.length == 0)
			return;
		AnaReportModel model = m_plugin.getModel();
		CellsModel formatModel = model.getFormatModel();
		
		ExAreaModel exAreaModel = ExAreaModel.getInstance(formatModel);

		ExAreaCell[] oldExAreaCells = exAreaModel.getContainExCells(oldArea);
		if(oldExAreaCells == null || oldExAreaCells.length == 0)
			return;
	
		for (AreaPosition newArea : newAreas) {//可扩展区域里只有一个交叉区域
			ExAreaCell[] newExAreaCells = exAreaModel.getContainExCells(newArea);
			for (int i = 0; i < oldExAreaCells.length; i++) {
				ExAreaCell oldExAreaCell = oldExAreaCells[i];				
				if(oldExAreaCell == null)
					continue;
				AreaDataModel oldAreaDataModel = null;
				if (oldExAreaCell.getModel() instanceof AreaDataModel) {
					oldAreaDataModel = (AreaDataModel) oldExAreaCell.getModel();
				}
				if (oldAreaDataModel == null)
					continue;

				if (!oldAreaDataModel.isCross())// 处理交叉区域，没有交叉区域不再处理
					continue;
				AnaCrossTableSet oldCrossSet = oldAreaDataModel.getCrossSet();
				
				if (oldCrossSet == null)
					continue;
				ExAreaCell newExAreaCell = newExAreaCells[i];
				
				AreaDataModel newAreaDataModel = (AreaDataModel)newExAreaCell.getModel();
				if(newAreaDataModel == null)
					continue;						
				if(iEditType == EditParameter.COPY || iEditType == EditParameter.BRUSH){
					AnaCrossTableSet newCrossSet = (AnaCrossTableSet)oldCrossSet.clone();
					newCrossSet.crossMoveArea(newCrossSet,newExAreaCell.getArea(),oldExAreaCell.getArea());              
					newAreaDataModel.setCrossInfo(newCrossSet);					
				} 
				formatModel.setDirty(true);	
			}				
		}	
	}
	
	private void pasteExAreaField(AreaPosition oldArea,AreaPosition[] newAreas,int iEditType,boolean isTransfer){
		if(oldArea == null || newAreas == null || newAreas.length == 0)
			return;
		ExAreaModel exAreaModel = getExAreaModel();
		ExAreaCell[] copyExcells = exAreaModel.getContainExCells(oldArea);
    	if (copyExcells == null || copyExcells.length == 0) {// 考虑复制时，选择区域在扩展区域中，但自身不包含可扩展区域

			ExAreaCell oldExAreaCell = exAreaModel.getExArea(oldArea);
			if (oldExAreaCell == null)
				return;
			AreaDataModel oldAreaDataModel = (AreaDataModel) oldExAreaCell
					.getModel();
			AnaReportModel anaRepModel = m_plugin.getModel();
			for (AreaPosition area : newAreas) {
				ExAreaCell exCell = exAreaModel.getExArea(area);
				if (exCell != null)
					return;
				if(isTransfer){
					area = AreaPosition.getInstance(area.getStart().getRow(), area.getStart().getColumn(), area.getHeigth(), area.getWidth());
				}
				exCell = exAreaModel.addExArea(area);
				String areaPK = exCell.getExAreaPK();
				AreaDataModel areaData = anaRepModel.getAreaData(areaPK);
				if (areaData.getDSPK() == null) {// 新增区域
					exCell.setExMode(ExAreaCell.EX_MODE_Y);// 默认设置成纵向扩展
					if(oldAreaDataModel != null){//对于扩展区域内没有字段时，此模型是空的
						areaData.setDSPK(oldAreaDataModel.getDSPK());
					}					
				}
				exCell.setModel(areaData);

			}
    		
    	}
	}
	
	private ExAreaModel getExAreaModel() {
		return ExAreaModel.getInstance(getCellsModel());
	}
	
	private CellsModel getCellsModel(){
		return m_plugin.getModel().getFormatModel();
	}
}
