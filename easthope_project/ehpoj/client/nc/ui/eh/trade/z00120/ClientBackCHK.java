package nc.ui.eh.trade.z00120;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;

public class ClientBackCHK implements Serializable, IBDGetCheckClass {

	/* ���� Javadoc��
	 * @see nc.vo.trade.pub.IBDGetCheckClass#getCheckClass()
	 */
	public String getCheckClass() {
		// TODO �Զ����ɷ������
		return "nc.bs.eh.trade.z00120.ClientBusiCheck";
	}

}
