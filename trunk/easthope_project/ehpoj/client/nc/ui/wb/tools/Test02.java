package nc.ui.wb.tools;

import nc.ui.sm.identityverify.ILoginPretreatment;
import nc.vo.sm.login.LoginSessBean;

public class Test02  implements ILoginPretreatment {
	/**
	 * UI 实现的接口方法 把值给取到 放到SESSION中去，到后台做相应的处理
	 */
	public void pretreatment(LoginSessBean lsb) throws Exception {
		QueryDialog a=new QueryDialog();
		a.showModal();
		String values=a.getValue();
		System.out.println("################### nc.ui.eh.test.Test02"+"   "+values);
		lsb.put("wm","wm");
	}

}
