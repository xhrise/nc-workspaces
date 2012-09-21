package nc.ui.so.so002;

import nc.ui.pub.linkoperate.ILinkQueryData;

public class LinkQueryData implements ILinkQueryData {

	protected String whereSql = "";
	protected ExtSaleInvoiceUI invUI = null;
	
	public LinkQueryData(String whereSql , ExtSaleInvoiceUI invUI) {
		this.whereSql = whereSql;
		this.invUI = invUI;
	}
	
	
	public String getBillID() {
		return null;
	}

	public String getBillType() {
		return null;
	}

	public String getPkOrg() {
		return null;
	}

	public Object getUserObject() {
		return new Object[]{whereSql,invUI};
	}

}
