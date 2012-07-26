package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class TransportRefPane extends AbstractRefModel {

	public TransportRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("�����ͬ");
		setRefTitle("�����ͬ");
		setFieldCode(new String[] {
			"pk_transport",
			"vbillno",
			"transname",
			"memo"
			
		});
		
		setFieldName(new String[] {
			"����",
			"��ͬ����",
			"��ͬ����",
			"��ע"
		});
		
//		setHiddenFieldCode(new String[]{"pk_transport"});
		
		setPkFieldCode("pk_transport");
		
		setRefCodeField("pk_transport");
		
		setRefNameField("transname");
		
		setTableName(" ehpta_transport_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getPk_corp()+"' and vbillstatus = 1 " );
		
	}
}
