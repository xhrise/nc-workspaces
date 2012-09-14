package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class StorcontRefPane extends AbstractRefModel {

	public StorcontRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("仓储合同");
		setRefTitle("仓储合同");
		setFieldCode(new String[] {
			"feetype",
			"signprice",
			
			
		});
		
		setFieldName(new String[] {
			"仓储类型",
			"单价",
		});
		
		setHiddenFieldCode(new String[]{"pk_storcontract_b"});
		
		setPkFieldCode("pk_storcontract_b");
		
		setRefCodeField("signprice");
		
		setRefNameField("signprice");
		
		StringBuilder builder = new StringBuilder();
		builder.append(" (select storcontb.pk_storcontract_b , storcont.pk_stordoc , ");
		builder.append(" decode(storcontb.feetype , 2 , '直驳费' , 3 , '船-库-船' , 4 , '船-库-车' , 5 , '直驳费（船-船）' , 6 , '直驳费（船-车）' , '仓库费') feetype  ,  ");
		builder.append(" storcontb.signprice from ehpta_storcontract storcont ");
		builder.append(" left join ehpta_storcontract_b storcontb on storcontb.pk_storagedoc = storcont.pk_storagedoc ");
		builder.append(" where nvl(storcontb.feetype,1) <> 1 and storcont.vbillstatus = 1 and nvl(storcont.dr , 0) = 0 and nvl(storcontb.dr,0) = 0 ");
		builder.append(" and nvl(ty_flag,'N') = 'N') ");
		
		setTableName(builder.toString() + " ehpta_storcontract ");
		
		setWherePart(" 1 = 1 " );
		
		setOrderPart(" pk_storcontract_b asc ");
		
	}
}
