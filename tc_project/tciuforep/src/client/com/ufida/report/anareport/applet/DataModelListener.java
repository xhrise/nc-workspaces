package com.ufida.report.anareport.applet;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.crosstable.DimInfo;
import com.ufida.report.crosstable.DimValueSet;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.IArea;
import com.ufsoft.table.exarea.ExAreaCell;

public class DataModelListener implements CellsModelListener {
	private AnaReportPlugin m_plugin = null;

	public DataModelListener(AnaReportPlugin plugin) {
		m_plugin = plugin;
	}

	public void cellsChanged(CellsEvent event) {
		if (m_plugin.getModel().isFormatState())
			return;
		CellsModel cells = m_plugin.getModel().getDataModel();
		if (cells == null) {// �������ᱻת�Ƶ���ʽģ���ϣ������л�������̬�����У�����ڴ�������
			return;
		}
		boolean cOnlyFormat = false;// ֻ�ǵ�Ԫ��ʽ�ı仯
		if (event.getMessage() == CellsEvent.VALUE_CHANGED) {

		} else if (event.getMessage() == CellsEvent.FORMAT_CHANGED) {
			cOnlyFormat = true;
		} else
			// �����¼������ݲ�����
			return;
        boolean isChange=false;
		IArea area = event.getArea();
		ArrayList<CellPosition> al = cells.getSeperateCellPos(area);
		Cell dataCell = null;
		Cell fCell=null;
		CellPosition fPos=null;
		AnaRepField anaFld = null;
		DimInfo dimInfo = null;
		DimValueSet dimset = null;
		Hashtable<CellPosition, Object> table = AreaDataModel.getModelExProp(cells, AnaRepField.EXKEY_DATAVALUE);
		Hashtable<CellPosition, CellPosition> cpRef=AreaDataModel.getModelExProp(cells, ExAreaCell.class);
		for (CellPosition pos : al) {
			fPos=cpRef.get(pos);
			if (fPos == null)// δ�ҵ���Ӧ�ĸ�ʽ��Ԫ
				continue;
			fCell=m_plugin.getModel().getFormatModel().getCell(fPos);
			dataCell = cells.getCell(pos);
			if(fCell==null||dataCell==null){
				continue;
			}
			if (fCell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) instanceof AnaRepField) {
				anaFld = (AnaRepField) fCell
						.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			} else {
				anaFld = null;
			}
			Object dimvalue = null;
			
			if (cOnlyFormat){
				isChange=true;
			}else {
				if (anaFld != null) {// �޸�γ��ֵ��Ա����ʾ��
					
					if (dataCell != null) {
						dimvalue = table.get(pos);
					}
					if (dataCell.getValue() != null && dimvalue != null
							&& !dataCell.getValue().equals("")
							) {
						dimInfo = anaFld.getDimInfo();
						dimset = new DimValueSet(new String[] { anaFld
								.getField().getFldname() },
								new Object[] { dimvalue });
						if(!dataCell.getValue().equals(dimvalue)){
							dimInfo.setDimValueShowName(dimset, dataCell.getValue()
									.toString());
						}else{
							dimInfo.setDimValueShowName(dimset, null);
						}
						isChange=true;
					}

				} else {
						fCell.setValue(dataCell.getValue());//�޸ĸ�ʽ̬û���ֶεĵ�Ԫ
						isChange=true;
				}

			}
            //�޸�����̬
			 if (isChange) {// ���������¼�
				Enumeration<CellPosition> dPoses = cpRef.keys();
				CellPosition tPos = null;
				CellPosition hPos = null;
				while (dPoses.hasMoreElements()) {
					tPos = dPoses.nextElement();
					hPos = cpRef.get(tPos);
					if (hPos.equals(fPos)) {
						if (cOnlyFormat) {
							cells.getCell(tPos).setFormat(dataCell.getFormat());
						} else {
							if (dimvalue != null
									&& dimvalue.equals(table.get(tPos))) {
								cells.getCell(tPos).setValue(
										dataCell.getValue());
							}else if(dimvalue==null){//��ͨ�ֶ�
								cells.getCell(tPos).setValue(
										dataCell.getValue());
							}

						}

					}
				}
				m_plugin.getReport().repaint();
			}
			 
		}
      
	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

}
