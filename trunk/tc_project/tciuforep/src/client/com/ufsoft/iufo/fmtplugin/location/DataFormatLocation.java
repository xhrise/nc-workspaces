package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  定位：数据格式（数值，字符，日期，表扬）
 * @author 王宇光
 * @version 
 * Create on 2008-5-20
 */
public class DataFormatLocation extends AbsLocation{
	/** 数据类型：定位的二级选项 */
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
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//输入参数不允许为空
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
