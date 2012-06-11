package nc.ui.segrep.segdef;

import nc.ui.iufo.function.IFuncOrderFlag;

public interface IFuncFlagSegDefMng {


	public static final String[] FUNCORDERS_DIR = new String[] {
		IFuncOrderFlag.HBBB_SEGREP_DIR_NEW,
		IFuncOrderFlag.HBBB_SEGREP_DIR_MODIFY,
		IFuncOrderFlag.HBBB_SEGREP_DIR_REMOVE,
		IFuncOrderFlag.HBBB_SEGREP_DIR_MOVE
	};

	public static final String[] FUNCORDERS_FILE = new String[] {
		IFuncOrderFlag.HBBB_SEGREP_SEGREP_NEW,
		IFuncOrderFlag.HBBB_SEGREP_SEGREP_MODIFY,
		IFuncOrderFlag.HBBB_SEGREP_SEGREP_REMOVE,
		IFuncOrderFlag.HBBB_SEGREP_SEGREP_MOVE
	};

	public static final String[] FUNCORDERS_AUTH = new String[] {
		IFuncOrderFlag.HBBB_SEGREP_AUTH_RES
	};

	public static final String[] FUNCORDERS_TOOL = new String[] {
		IFuncOrderFlag.HBBB_SEGREP_TOOL_EXEC,
		IFuncOrderFlag.HBBB_SEGREP_TOOL_SHOWDATA,
		IFuncOrderFlag.HBBB_SEGREP_TOOL_RELEASE
	};
}
