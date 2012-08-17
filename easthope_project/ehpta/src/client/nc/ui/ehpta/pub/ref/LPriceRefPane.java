package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class LPriceRefPane extends AbstractRefModel {

	public LPriceRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("挂牌价表");
		setRefTitle("挂牌价表");
		setFieldCode(new String[] {
			"maindate",
			"listingmny",
			"memo"
		});
		
		setFieldName(new String[] {
			"维护日期",
			"挂牌价",
			"备注"
		});
		
		setPkFieldCode("listingmny");

		setRefCodeField("listingmny");
		
		setRefNameField("listingmny");
		
		setTableName(" ehpta_maintain ");
		
		setWherePart(" 1 = 1 ");
		
		setOrderPart(" maindate asc ");
		
	}
}
