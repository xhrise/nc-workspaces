package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class AidcustRefPane extends AbstractRefModel {

	public AidcustRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("�����ͻ�");
		setRefTitle("�����ͻ�");
		setFieldCode(new String[] {
			"pk_custdoc",
			"custcode",
			"custname"
			
		});
		
		setFieldName(new String[] {
			"����",
			"���̱���",
			"��������"
		});
		
		setPkFieldCode("pk_custdoc");
		
		setRefCodeField("pk_custdoc");
		
		setRefNameField("custname");
		
		setTableName(" ( select pk_contract , pk_custdoc , custcode , custname from ehpta_aidcust cust left join bd_cumandoc man on cust.pk_custdoc = man.pk_cumandoc left join bd_cubasdoc bas on man.pk_cubasdoc = bas.pk_cubasdoc where nvl(cust.dr,0)=0 and nvl(man.dr,0)=0 and nvl(bas.dr,0)=0 order by def1 asc )  ");
		
		setWherePart(" 1 = 1 ");
		
	}
}
