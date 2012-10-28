package nc.ui.ehpta.pub;

public interface IAdjustType {
	
	/**
	 * 收款 = 1
	 */
	public final String Receivables = "1";
	
	/**
	 * 贴息 = 2
	 */
	public final String Discount = "2";
	
	/**
	 * 挂结价差额 = 3
	 */
	public final String LSSubPrice = "3";
	
	/**
	 * 返利 = 4
	 */
	public final String Rebates = "4";
	
	/**
	 * 运补 = 5
	 */
	public final String Subsidies = "5";
	
	/**
	 * 仓储费 = 6
	 */
	public final String Storfee = "6";
	
	/**
	 * 装卸费 = 7
	 */
	public final String Handlingfee = "7";
	
	/**
	 *  已提货金额 / 累计开票额 = 11
	 */
	public final String Otherfee = "11";
}
