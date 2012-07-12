package nc.ui.ehpta.pub.gen;

import nc.bs.pub.billcodemanage.BillcodeGenerater;

public final class GeneraterBillNO {

	private static GeneraterBillNO gen = new GeneraterBillNO();
	
	private GeneraterBillNO() { }
	
	private BillcodeGenerater genFunc =	new BillcodeGenerater ();
	
	public static final GeneraterBillNO getInstanse() {
		return gen;
	}
	
	/**
	 * 
	 * @param billType 单据类型
	 * 
	 * @param pk_corp 公司
	 * 
	 * @return BillcodeGenerater
	 */
	public final String build(String billType , String pk_corp) throws Exception {
		if(genFunc == null)
			genFunc = new BillcodeGenerater ();
		
		return genFunc.getBillCode(billType, pk_corp, null, null);
	}
}
