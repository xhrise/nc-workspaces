package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class LPriceRefPane extends AbstractRefModel {

	public LPriceRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("���Ƽ۱�");
		setRefTitle("���Ƽ۱�");
		setFieldCode(new String[] {
			"maindate",
			"listingmny",
			"memo"
		});
		
		setFieldName(new String[] {
			"ά������",
			"���Ƽ�",
			"��ע"
		});
		
		setPkFieldCode("listingmny");

		setRefCodeField("listingmny");
		
		setRefNameField("listingmny");
		
		setTableName(" ehpta_maintain ");
		
		setWherePart(" 1 = 1 ");
		
		setOrderPart(" maindate asc ");
		
	}
}
