package com.ufsoft.iufo.fmtplugin.formula;

import com.ufida.dataset.IContext;
import com.ufsoft.script.extfunc.LoginInfoFuncDriver;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.extfunc.OtherFuncDriver;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellsModel;

public class RowFilterExprFmlExecutor extends BaseExprExecutor{

	public RowFilterExprFmlExecutor(IContext contextVO, CellsModel cellModel){
		super(contextVO,cellModel);
	}
	
	@Override
	protected int[] getFuncIds() {
		return new int[]{UfoFuncList.DATEFUNC,UfoFuncList.MATHFUNC,UfoFuncList.STRFUNC
				,UfoFuncList.AREAFUNC,UfoFuncList.IFFFUNC};
	}

	@Override
	protected void registeDataDriver() {
		getExecutorEnv().registerFuncDriver(new KFuncFuncDriver(getExecutorEnv()));
		getExecutorEnv().registerFuncDriver(new LoginInfoFuncDriver());
		getExecutorEnv().registerFuncDriver(new OtherFuncDriver());
	}
	
	/**
	 * ���ʽʹ�õ�K����������
	 */
	private class KFuncFuncDriver extends MeasFuncDriver {
		
		private static final long serialVersionUID = 4342611816103409900L;
		//uiufofunc101=�ؼ��ֺ���
		//uiufofunc102=ʱ��ؼ������Ժ���
		private final String[] CATNAMES = {"uiufofunc101","uiufofunc102"};

		public KFuncFuncDriver(UfoCalcEnv env){
			super(env);
		}
		
		@Override
		public String[] getCategoryList() {
			return CATNAMES;
		}		
	}
}
