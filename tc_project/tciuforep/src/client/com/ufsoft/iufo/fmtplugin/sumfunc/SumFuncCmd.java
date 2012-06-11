package com.ufsoft.iufo.fmtplugin.sumfunc;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formula.IufoFormulalUtil;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UFOTable;

public class SumFuncCmd extends UfoCommand {
	/**
	 * true 向下求和，false 向右求和。
	 */
	private boolean _downwardNotRightward;

	public SumFuncCmd(boolean downwardNotRightward) {
		_downwardNotRightward = downwardNotRightward;
	}

	public void execute(Object[] params) {
		CellsModel cellsModel = (CellsModel) params[0];
		AreaPosition areaPos = (AreaPosition) params[1];
		UfoFmlExecutor fmlExecutor = (UfoFmlExecutor) params[2];
		if(areaPos == null) return;
		if(_downwardNotRightward){
			for(int col=areaPos.getStart().getColumn();col <= areaPos.getEnd().getColumn();col++){
				int contentStartRow = areaPos.getStart().getRow();
				int contentEndRow = areaPos.getEnd().getRow() - 1;
				if(contentEndRow >= contentStartRow){
					AreaPosition contentArea = AreaPosition.getInstance(contentStartRow,col,1,contentEndRow-contentStartRow+1);
					CellPosition funcPos = CellPosition.getInstance(areaPos.getEnd().getRow(),col);
					addFormulaImpl(funcPos,contentArea,cellsModel,fmlExecutor);
				}					
			}
		}else{
			for(int row=areaPos.getStart().getRow();row<=areaPos.getEnd().getRow();row++){
				int contentStartCol = areaPos.getStart().getColumn();
				int contentEndCol = areaPos.getEnd().getColumn() - 1;
				if(contentEndCol >= contentStartCol){
					AreaPosition contentArea = AreaPosition.getInstance(row,contentStartCol,contentEndCol-contentStartCol+1,1);
					CellPosition funcPos = CellPosition.getInstance(row,areaPos.getEnd().getColumn());
					addFormulaImpl(funcPos,contentArea, cellsModel, fmlExecutor);
				}
			}
		}
		
	}
	private void addFormulaImpl(CellPosition funcPos, AreaPosition contentArea, CellsModel cellsModel, UfoFmlExecutor fmlExecutor) {
		try {
			StringBuffer showErrMessage = new StringBuffer();
			boolean addToPublicFormula = IufoFormulalUtil.isCreateUnit(fmlExecutor.getContextVO());
			fmlExecutor.addUserDefFormula(showErrMessage,funcPos,getFuncContent(contentArea),true,addToPublicFormula);
			cellsModel.clearArea(UFOTable.CELL_CONTENT,new IArea[]{funcPos});
		} catch (ParseException e) {
			AppDebug.debug(e);
		}
	}

	private String getFuncContent(AreaPosition contentArea){
		return "PTOTAL("+contentArea.toString()+")";
	}

}
