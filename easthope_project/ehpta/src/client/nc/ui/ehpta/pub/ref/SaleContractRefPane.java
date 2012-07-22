package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class SaleContractRefPane extends AbstractRefModel {

	public SaleContractRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("PTA���ۺ�ͬ");
		setRefTitle("PTA���ۺ�ͬ");
		setFieldCode(new String[] {
			"pk_contract",
			"vbillno",
			"connamed",
			"contype",
			"memo"
			
		});
		
		setFieldName(new String[] {
			"����",
			"��ͬ����",
			"��ͬ����",
			"��ͬ����",
			"��ע"
		});
		
		setPkFieldCode("pk_contract");
		
		setRefCodeField("pk_contract");
		
		setRefNameField("vbillno");
		
		setTableName(" ehpta_sale_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '" + getPk_corp() + "'");
		
		setOrderPart(" dmakedate asc ");
		
	}
}
