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
			"carrier",
			"memo"
			
		});
		
		setFieldName(new String[] {
			"����",
			"��ͬ����",
			"������",
			"��ע"
		});
		
//		setHiddenFieldCode(new String[]{"pk_transport"});
		
		setPkFieldCode("pk_transport");
		
		setRefCodeField("pk_transport");
		
		setRefNameField("vbillno");
		
		setTableName(" (select transcont.pk_transport , transcont.vbillno , bas.custname carrier , transcont.memo , transcont.pk_corp , transcont.dr , transcont.vbillstatus from ehpta_transport_contract transcont left join bd_cumandoc man on man.pk_cumandoc = transcont.pk_carrier left join bd_cubasdoc bas on bas.pk_cubasdoc = man.pk_cubasdoc where transcont.transtype = 'under') ehpta_transport_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getPk_corp()+"' and vbillstatus = 1 " );
		
	}
}
