package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  ��λ�����ݸ�ʽ����ֵ���ַ������ڣ����
 * @author �����
 * @version 
 * Create on 2008-5-20
 */
public class DataFormatLocation extends AbsLocation{
	/** �������ͣ���λ�Ķ���ѡ�� */
	private int dataType = 0;
	
	public DataFormatLocation(UfoReport rep,int dataType){
		super(rep);
		this.dataType = dataType;
	}
	@Override
	protected int getConditionType() {
		// TODO Auto-generated method stub
		return AbsLocation.DATA_FORMAT;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition,CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null){
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		Cell cell = cellsModel.getCell(cellPosition);
		if (cell == null) {
			return;
		}
		IufoFormat format = (IufoFormat)cell.getFormat();
		if(format == null){
			return;
		}
		int cellType = format.getCellType();
		if(cellType == getDataType()){
			getCellsPositionList().add(cellPosition);
		}
	}

	private int getDataType(){
		return this.dataType;
	}
}
