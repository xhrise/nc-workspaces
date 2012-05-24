
package nc.ui.eh.trade.z0207010;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass;


public class ClientBackCHK implements Serializable, IBDGetCheckClass {

	public String getCheckClass() {
		return "nc.bs.eh.trade.z02030.ClientBusiCheck";
	}

}
