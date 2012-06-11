package com.ufsoft.report.sysplugin.editplugin;

import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;

import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellSelection;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.BorderPlayRender;

public abstract class EditAction extends AbsEditAction{

	abstract protected int getClipType();
	
	abstract protected int getEditType();
	
	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled())//��ֹ��ݼ�
			return;
		BorderPlayRender.controlPlay(getCellsPane());// ��������
		copy(getClipType(), getEditType());
		
	}

	protected void copy(int clipType, int editType) {
		copy(clipType, editType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	private void copy(int clipType, int editType, boolean dispatch) {
		
		//liuyy+�� ��ѡ���ܡ�
		AreaPosition[] areas = getSelectedAreas();		
		
		AreaPosition area = null;
		if(areas!=null&&areas.length > 0){
			area = areas[0];
		}
		
		if(area == null){
			return;
		}
		
		CellsModel cellsModel = getCellsModel();
		Cell[][] selectedCells = cellsModel.getCells(area);
		
		EditParameter editParam = new EditParameter(clipType, editType, area,selectedCells);
		editParam.setAreaInfo(EditParameter.COMBINED_CELL, getCombinedAreaModel().getCombineCells(area));//������ϵ�Ԫ�����ڱ����֮�临��
		
		//��װ�༭����
		CellSelection sel = new CellSelection(editParam,getExcelContent(area));
				
		// ��װ�����¼�.
		UserUIEvent event = new UserUIEvent(this, editType, sel,
				editParam);
		
		UFOTable table = getTable();
		if(table == null){
			return;
		}
		// �������¼�.
		if (dispatch) {//��ʽˢʱ����Ҫ�ɷ��¼�
			if(table.checkEvent(event)){
				table.fireEvent(event);
			}else{
				BorderPlayRender.stopPlay(table.getCells());
				return;
			}			
		}

		Clipboard m_Clipboard = table.getClipboard();// ��ü��а�
		if (m_Clipboard == null) {
			return;
		}
		
		m_Clipboard.setContents(sel, null);
	}
	
	private CombinedAreaModel getCombinedAreaModel(){
		return CombinedAreaModel.getInstance(getCellsModel());
	}
	
	private String getExcelContent(AreaPosition area){
		CellsModel cellsModel = getCellsModel();
		Cell[][] cells = cellsModel.getCells(area);
		StringBuffer strContent = new StringBuffer();
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				Cell[] cLine = cells[i];
				if (cLine != null) {
					for (int j = 0; j < cLine.length; j++) {
						if (cLine[j] != null) {
							Object value = cLine[j].getValue();
							strContent.append(value == null ? "" : value
									.toString());

							if (j != cLine.length - 1) {
								strContent.append("\t");
							}
						}
					}
				}

				if (i != cells.length - 1) {
					strContent.append("\n");
				}
			}
		}
		return strContent.toString();
	}

}
