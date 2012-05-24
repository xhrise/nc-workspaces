package nc.ui.eh.stock.z0150515;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;

public class ClientBackCHK implements Serializable, IBDGetCheckClass {

	/* （非 Javadoc）
	 * @see nc.vo.trade.pub.IBDGetCheckClass#getCheckClass()
	 */
	public String getCheckClass() {
		// TODO 自动生成方法存根
		return "nc.bs.eh.stock.z0150515.ClientBusiCheck";
	}

}
