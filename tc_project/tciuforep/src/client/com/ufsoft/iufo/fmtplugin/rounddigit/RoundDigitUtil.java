package com.ufsoft.iufo.fmtplugin.rounddigit;

import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaModel;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class RoundDigitUtil {
	public static Boolean isUnRoundDigitArea(CellsModel cellsModel){
		CellPosition[] selPoss = cellsModel.getSelectModel().getSelectedCells();
		boolean isHasUnRoundDigitPos = false;
		boolean isHasRoundDigitPos = false;
		for (int i = 0; i < selPoss.length; i++) {
			if(getRoundDigitAreaModel(cellsModel).isUnRoundDigitPos(selPoss[i])){
				isHasUnRoundDigitPos = true;
			}else{
				isHasRoundDigitPos = true;
			}
			if(isHasUnRoundDigitPos && isHasRoundDigitPos){
				break;
			}
		}	
		if(isHasUnRoundDigitPos && isHasRoundDigitPos){
			return null;
		}else if(isHasUnRoundDigitPos){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}
	
	public static RoundDigitAreaModel getRoundDigitAreaModel(CellsModel cellsModel){
		return RoundDigitAreaModel.getInstance(cellsModel);
	}
	
	/**
	 * 设置选择区域单元是否为舍位区域
	 * @param b
	 */
	public static void setUnRoundDigitArea(boolean b,CellsModel cellsModel) {
		CellPosition[] cellPoss = cellsModel.getSelectModel().getSelectedCells();
		if(b){
			getRoundDigitAreaModel(cellsModel).addUnRoundDigitPos(cellPoss) ;
		}else{
			getRoundDigitAreaModel(cellsModel).removeUnRoundDigitPos(cellPoss);
		}
		cellsModel.setDirty(true);
		cellsModel.fireExtPropChanged(cellsModel.getSelectModel().getSelectedArea());
	}

}
