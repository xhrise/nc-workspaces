package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class UnderTransRefPane extends AbstractRefModel {

	public UnderTransRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("运输合同");
		setRefTitle("运输合同");
		setFieldCode(new String[] {
			"pk_transport_b",
			"vbillno",
			"transtype",
			"carrier",
			"estoraddr",
			"sendname",
			"price"
			
		});
		
		setFieldName(new String[] {
			"主键",
			"合同编码",
			"合同类型",
			"承运商",
			"到站地",
			"运输方式",
			"运输价格"
		});
		
//		setHiddenFieldCode(new String[]{"pk_transport"});
		
		setPkFieldCode("pk_transport_b");
		
		setRefCodeField("pk_transport_b");
		
		setRefNameField("price");
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(" (select transcontb.pk_transport_b, ");
		builder.append(" transcont.vbillno, ");
		builder.append(" case when transcont.transtype = 'upper' then '上游运输合同' when transcont.transtype = 'under' then '下游运输合同' else '' end transtype, ");
		builder.append(" bas.custname carrier, ");
		builder.append(" transcont.memo, ");
		builder.append(" transcont.pk_corp, ");
		builder.append(" transcont.dr, "); 
		builder.append(" transcont.vbillstatus, ");
		builder.append(" send.sendname, (select max(addrname) from bd_address where pk_address = transcontb.estoraddr) estoraddr , ");
		builder.append(" case when transcont.transtype = 'upper' then transcontb.shipprice when transcont.transtype = 'under' then transcontb.transprice else 0 end price , ");
		builder.append(" transcont.pk_transport ");
		builder.append(" , transcont.pk_carrier pk_cumandoc , transcontb.pk_sstordoc pk_stordoc , transcontb.estoraddr pk_address , transcontb.pk_sendtype pk_sendtype ");
		builder.append(" from ehpta_transport_contract transcont ");
		builder.append(" left join bd_cumandoc man on man.pk_cumandoc = transcont.pk_carrier ");
		builder.append(" left join bd_cubasdoc bas on bas.pk_cubasdoc = man.pk_cubasdoc ");
		builder.append(" left join ehpta_transport_contract_b transcontb on transcontb.pk_transport = transcont.pk_transport ");
		builder.append(" left join bd_sendtype send on send.pk_sendtype = transcontb.pk_sendtype) ");
		
		setTableName(builder.toString() + " ehpta_transport_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getPk_corp()+"' and vbillstatus = 1 and transtype = '下游运输合同'  " );
		
	}
}
