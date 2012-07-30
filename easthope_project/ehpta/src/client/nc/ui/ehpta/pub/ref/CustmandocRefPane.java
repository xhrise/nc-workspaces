package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class CustmandocRefPane extends AbstractRefModel {

	public CustmandocRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("客商档案");
		setRefTitle("客商档案");
		setFieldCode(new String[] {
			"bd_cumandoc.pk_cumandoc",
			"bd_cubasdoc.custcode",
			"bd_cubasdoc.custname",
			"bd_cumandoc.CUSTFLAG"
			
		});
		
		setFieldName(new String[] {
			"主键",
			"客商编码",
			"客商名称",
			"客商标识"
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
