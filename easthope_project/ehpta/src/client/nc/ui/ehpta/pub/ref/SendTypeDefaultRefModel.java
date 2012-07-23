
package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * ���˷�ʽ
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
public class SendTypeDefaultRefModel extends AbstractRefModel {

	public SendTypeDefaultRefModel() {
		super();
		init();
		
	}
	public void init() {

		setFieldCode(new String[] { "pk_sendtype" , "sendcode", "sendname" }); //4��
		
		setHiddenFieldCode(new String[] { "pk_sendtype" });
		
		setTableName("bd_sendtype");
		
		setPkFieldCode("pk_sendtype");
		
		setRefTitle("���䷽ʽ");
		
		setRefCodeField("pk_sendtype");
		
		setRefNameField("sendname");
		
		setWherePart(" pk_corp='" + getPk_corp() + "' or pk_corp= '" + getGroupCode() + "' or pk_corp is null");

		setOrderPart("  ");
		
		resetFieldName();

	}

}
