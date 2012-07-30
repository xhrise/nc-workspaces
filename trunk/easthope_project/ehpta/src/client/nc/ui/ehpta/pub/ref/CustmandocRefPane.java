package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class CustmandocRefPane extends AbstractRefModel {

	public CustmandocRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("���̵���");
		setRefTitle("���̵���");
		setFieldCode(new String[] {
			"bd_cumandoc.pk_cumandoc",
			"bd_cubasdoc.custcode",
			"bd_cubasdoc.custname",
			"bd_cumandoc.CUSTFLAG"
			
		});
		
		setFieldName(new String[] {
			"����",
			"���̱���",
			"��������",
			"���̱�ʶ"
		});
		
		setPkFieldCode("bd_cumandoc.pk_cumandoc");
		
		setRefCodeField("bd_cubasdoc.custcode");
		
		setRefNameField("bd_cubasdoc.custname");
		
		setTableName(" bd_cumandoc left join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc   ");
		// and bd_cumandoc.pk_corp = '"+getPk_corp()+"'
		setWherePart(" 1 = 1  and (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') " );
		
		setOrderPart(" bd_cumandoc.pk_cumandoc ");
	}
}
