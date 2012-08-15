package nc.ui.pf.changedir;

import nc.ui.pf.afterclass.AFTD2TOHQ09;
import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGD2TOHQ09 extends VOConversionUI {

	public String getAfterClassName() {

		return AFTD2TOHQ09.class.getName();
	}

	public String getOtherClassName() {
		return AFTD2TOHQ09.class.getName();
	}

	// 在afterClass中进行处理转换
	public String[] getField() {
		return new String[] {

		};

	}

	public String[] getAssign() {
		return new String[] {

		};

	}

	public String[] getFormulas() {
		return new String[] {

		};

	}

	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
