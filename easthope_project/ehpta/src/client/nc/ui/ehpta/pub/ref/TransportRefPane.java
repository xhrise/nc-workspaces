package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TransportRefPane extends AbstractRefModel {

	public TransportRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("运输合同");
		setRefTitle("运输合同");
		setFieldCode(new String[] {
			"pk_transport",
			"vbillno",
			"transname",
			"memo"
			
		});
		
		setFieldName(new String[] {
			"主键",
			"合同编码",
			"合同名称",
			"备注"
		});
		
//		setHiddenFieldCode(new String[]{"pk_transport"});
		
		setPkFieldCode("pk_transport");
		
		setRefCodeField("pk_transport");
		
		setRefNameField("transname");
		
		setTableName(" ehpta_transport_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getPk_corp()+"' and vbillstatus = 1 " );
		
	}
}
