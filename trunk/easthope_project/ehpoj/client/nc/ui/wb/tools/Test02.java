package nc.ui.wb.tools;

import nc.ui.sm.identityverify.ILoginPretreatment;
import nc.vo.sm.login.LoginSessBean;

public class Test02  implements ILoginPretreatment {
	/**
	 * UI ʵ�ֵĽӿڷ��� ��ֵ��ȡ�� �ŵ�SESSION��ȥ������̨����Ӧ�Ĵ���
	 */
	public void pretreatment(LoginSessBean lsb) throws Exception {
		QueryDialog a=new QueryDialog();
		a.showModal();
		String values=a.getValue();
		System.out.println("################### nc.ui.eh.test.Test02"+"   "+values);
		lsb.put("wm","wm");
	}

}
