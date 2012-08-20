package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ehpta.pub.convert.ConvertFunc;

public class TempletItemRefPane extends AbstractRefModel {

	public TempletItemRefPane() throws Exception {
		super();
		
		init();
	}
	
	private final void init() throws Exception { 
		setRefNodeName("����");
		setRefTitle("����");
		setFieldCode(new String[] {
			"pk_billtemplet_b",
			"itemkey",
			"defaultshowname"
			
		});
		
		setFieldName(new String[] {
			"����",
			"����",
			"����"
		});
		
		setPkFieldCode("pk_billtemplet_b");
		
		setHiddenFieldCode(new String[]{"pk_billtemplet_b"});
		
		setRefCodeField("itemkey");
		
		setRefNameField("defaultshowname");
		
		String sqlField = ConvertFunc.change(new String[]{
				"templetb.pk_billtemplet_b" , 
				"templetb.itemkey" , 
				"templetb.defaultshowname" , 
				"templetb.showorder" , 
				"templetb.pos" , 
				"templetb.SHOWFLAG", 
				"templet.PK_BILLTYPECODE",
				"templetb.editflag",
		});
		
		setTableName(" ( select "+sqlField+" from pub_billtemplet_b templetb left join pub_billtemplet templet on templet.pk_billtemplet = templetb.pk_billtemplet ) pub_billtemplet_b ");
		
		setWherePart(" 1 = 0 "); // beforeEdit�е���ʱ����WHERE����
		
		setOrderPart(" showorder asc ");
		
	}
}
