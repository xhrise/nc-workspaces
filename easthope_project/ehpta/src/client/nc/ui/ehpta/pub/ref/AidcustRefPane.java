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
			"pk_cumandoc",
			"custcode",
			"custname"
			
		});
		
		setFieldName(new String[] {
			"����",
			"���̱���",
			"��������"
		});
		
		setPkFieldCode("pk_cumandoc");
		
		setRefCodeField("custcode");
		
		setRefNameField("custname");
		
		setTableName(" ( select cust.pk_contract, cust.pk_custdoc pk_cumandoc, bas.custcode, bas.custname , man.frozenflag , man.sealflag from ehpta_aidcust cust left join bd_cumandoc man on cust.pk_custdoc = man.pk_cumandoc left join bd_cubasdoc bas on man.pk_cubasdoc = bas.pk_cubasdoc where nvl(cust.dr,0)=0 and nvl(man.dr,0)=0 and nvl(bas.dr,0)=0 order by cust.def1 asc ) bd_cumandoc ");
		
		setWherePart(" 1 = 1 ");
		
	}
}
