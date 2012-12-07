package nc.ui.so.so002;

import nc.ui.pub.linkoperate.ILinkQueryData;

public class LinkQueryData implements ILinkQueryData {

	protected String whereSql = "";
	protected SaleInvoiceUI invUI = null;
	
	public LinkQueryData(String whereSql , SaleInvoiceUI saleInvoiceUI) {
		this.whereSql = whereSql;
		this.invUI = saleInvoiceUI;
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
