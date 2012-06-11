package nc.ui.bi.integration.dimension;

import nc.ui.iufo.function.IFuncOrderFlag;

public interface IFuncFlagDimensionMng {

	public static final String[] FUNCORDERS_DIR = new String[] {
		IFuncOrderFlag.BI_DIMENTION_DIR_NEW,
		IFuncOrderFlag.BI_DIMENTION_DIR_MODIFY,
		IFuncOrderFlag.BI_DIMENTION_DIR_REMOVE,
		IFuncOrderFlag.BI_DIMENTION_DIR_MOVE
	};

	public static final String[] FUNCORDERS_FILE = new String[] {
		IFuncOrderFlag.BI_DIMENTION_DIM_NEW,
		IFuncOrderFlag.BI_DIMENTION_DIM_MODIFY,
		IFuncOrderFlag.BI_DIMENTION_DIM_REMOVE,
		IFuncOrderFlag.BI_DIMENTION_DIM_COPY,
		IFuncOrderFlag.BI_DIMENTION_DIM_MOVE
	};
	
	public static final String[] FUNCORDERS_INFO = new String[] {
		IFuncOrderFlag.BI_DIMENTION_INFO_MEMBERMNG,
		IFuncOrderFlag.BI_DIMENTION_INFO_EXPROPMNG
	};
	
	public static final String[] FUNCORDERS_AUTH = new String[] {
		IFuncOrderFlag.BI_DIMENTION_AUTH_RES,
		IFuncOrderFlag.BI_DIMENTION_AUTH_DATA,
		IFuncOrderFlag.BI_DIMENTION_AUTH_DATAPOLICY
	};

	public static final String[] FUNCORDERS_TOOL = new String[] {
		IFuncOrderFlag.BI_DIMENTION_TOOL_IMPORTSET,
		IFuncOrderFlag.BI_DIMENTION_TOOL_IMPORT
	};
}
