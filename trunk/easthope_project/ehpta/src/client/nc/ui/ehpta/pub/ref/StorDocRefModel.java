
package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * �ֿ⵵��
 * <p>
 * <strong>�ṩ�ߣ�</strong>UAP
 * <p>
 * <strong>ʹ���ߣ�</strong>
 * 
 * <p>
 * <strong>���״̬��</strong>��ϸ��� 
 * <p>
 * @version 	NC5.0
 * @author 		sxj
 */
public class StorDocRefModel extends AbstractRefModel {

	public StorDocRefModel() {
		super();
		
		init();
		// TODO �Զ����ɹ��캯�����
	}
	public void init() {
		
		setRefTitle("�ִ���ͬ");
		
		setFieldCode(new String[] { "pk_storagedoc" , "pk_stordoc" , "custname" , "storcode", "storname", "storaddr" }); //19��
		
		setFieldName(new String[] { "�ִ���ͬ����" , "�ֿ�����" , "ǩԼ��λ����" , "�ֿ����" , "�ֿ�����" , "�ֿ��ַ"});
		
		setHiddenFieldCode(new String[] { "pk_storagedoc" });
		
		setTableName(" (select storcont.pk_corp , storcont.pk_storagedoc , stordoc.pk_stordoc , cubas.custcode , cubas.custname , stordoc.storcode , stordoc.storname , stordoc.storaddr from ehpta_storcontract storcont left join bd_stordoc stordoc on stordoc.pk_stordoc = storcont.pk_stordoc left join bd_cubasdoc cubas on cubas.pk_cubasdoc = storcont.signcompany where storcont.vbillstatus = 1 and nvl(stordoc.dr , 0) = 0 and nvl(storcont.dr , 0) = 0) bd_stordoc ");
		
		setPkFieldCode("pk_storagedoc");
		
		setRefCodeField("pk_storagedoc");
		
		setRefNameField("storname");
		
		setWherePart(" pk_corp= '" + getPk_corp() + "' ");
		
		setOrderPart(" bd_stordoc.pk_storagedoc ");
		
	}

}
