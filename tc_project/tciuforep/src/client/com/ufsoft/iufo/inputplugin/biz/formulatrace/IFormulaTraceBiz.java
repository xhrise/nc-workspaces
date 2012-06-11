package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufida.dataset.Context;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.ContextVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public interface IFormulaTraceBiz {
	/**
	 * exist formula in the CellsModel
	 * @param cellModel
	 * @param cell
	 * @return
	 */
	public  boolean existFormula(CellsModel cellModel,CellPosition cell);
	/**
	 * parse formula on the cell position in the CellsModel
	 * @param contextVO
	 * @param cellModel
	 * @param cell
	 * @return
	 */
	public  FormulaParsedData parseFormula(Context contextVO,CellsModel cellModel,CellPosition cell);
	/**
	 * calculate formulaTraceValueItem in the CellsModel
	 * @param contextVO
	 * @param cellModel
	 * @param cell
	 * @return
	 */
	public  IFormulaTraceValueItem calFormulaTraceValueItem(ContextVO contextVO,CellsModel cellModel,IFormulaParsedDataItem formulaParsedDataItem,CellPosition cell);
}
