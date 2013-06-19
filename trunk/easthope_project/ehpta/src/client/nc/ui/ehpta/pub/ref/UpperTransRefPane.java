package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class UpperTransRefPane extends AbstractRefModel {

	public UpperTransRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("�����ͬ");
		setRefTitle("�����ͬ");
		setFieldCode(new String[] {
			"pk_transport_b",
			"vbillno",
			"transtype",
			"carrier",
			"addrname",
			"sendname",
			"price"
			
		});
		
		setFieldName(new String[] {
			"����",
			"��ͬ����",
			"��ͬ����",
			"������",
			"��վ��",
			"���䷽ʽ",
			"����۸�"
		});
		
//		setHiddenFieldCode(new String[]{"pk_transport"});
		
		setPkFieldCode("pk_transport_b");
		
		setRefCodeField("price");
		
		setRefNameField("price");
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(" (select transcontb.pk_transport_b, ");
		builder.append(" transcont.vbillno, ");
		builder.append(" case when transcont.transtype = 'upper' then '���������ͬ' when transcont.transtype = 'under' then '���������ͬ' else '' end transtype, ");
		builder.append(" bas.custname carrier, ");
		builder.append(" transcont.memo, ");
		builder.append(" transcont.pk_corp, ");
		builder.append(" transcont.dr, "); 
		builder.append(" transcont.vbillstatus, ");
		builder.append(" send.sendname, ");
		builder.append(" case when transcont.transtype = 'upper' then transcontb.shipprice when transcont.transtype = 'under' then transcontb.transprice else 0 end price , ");
		builder.append(" transcont.pk_transport , address.addrname ");
		builder.append(" from ehpta_transport_contract transcont ");
		builder.append(" left join bd_cumandoc man on man.pk_cumandoc = transcont.pk_carrier ");
		builder.append(" left join bd_cubasdoc bas on bas.pk_cubasdoc = man.pk_cubasdoc ");
		builder.append(" left join ehpta_transport_contract_b transcontb on transcontb.pk_transport = transcont.pk_transport ");
		builder.append(" left join bd_sendtype send on send.pk_sendtype = transcontb.pk_sendtype ");
		builder.append(" left join bd_address address on address.pk_address = transcontb.estoraddr) ");
		
		setTableName(builder.toString() + " ehpta_transport_contract ");
		
		setWherePart(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getPk_corp()+"' and vbillstatus = 1 and transtype = '���������ͬ' " );
		
	}
}