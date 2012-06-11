package com.ufsoft.iufo.fmtplugin.formula;

import com.ufida.dataset.IContext;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.table.CellsModel;

public class ParamFilterExprFmlExecutor extends RowFilterExprFmlExecutor{

	public ParamFilterExprFmlExecutor(IContext contextVO, CellsModel cellModel){
		super(contextVO,cellModel);
	}

	@Override
	protected int[] getFuncIds() {
		return new int[]{UfoFuncList.DATEFUNC,UfoFuncList.MATHFUNC,UfoFuncList.STRFUNC
				,UfoFuncList.IFFFUNC};
	}
}
