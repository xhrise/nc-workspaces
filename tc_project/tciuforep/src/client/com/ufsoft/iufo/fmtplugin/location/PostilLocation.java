package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.cellpostil.CellPostilDefPlugin;
import com.ufsoft.report.sysplugin.postil.PostilVO;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  定位：批注
 * @author 王宇光
 * @version 
 * Create on 2008-5-19
 */
public class PostilLocation extends AbsLocation {

	public PostilLocation(UfoReport rep) {
		super(rep);

	}

	@Override
	protected int getConditionType() {
		return AbsLocation.POSTIL;
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
		PostilVO pvo = (PostilVO) cell.getExtFmt(CellPostilDefPlugin.EXT_FMT_POSTIL);
		if (pvo == null) {
			return;
		}

		getCellsPositionList().add(cellPosition);
		
	}

}
