package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;


/**
 * ���㷽ʽ
 * <p>
 * <strong>�ṩ�ߣ�</strong>UAP
 * <p>
 * <strong>ʹ���ߣ�</strong>
 * 
 * <p>
 * <strong>���״̬��</strong>��ϸ���
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class BalanceTypeRefModel extends AbstractRefModel {

	public BalanceTypeRefModel() {
		init();
		// TODO �Զ����ɹ��캯�����
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bd.ref.DefaultRefModel#setRefNodeName(java.lang.String)
	 */
	public void init() {
		setRefTitle("���㷽ʽ");
		setFieldCode(new String[] { "balancode", "balanname" }); // 7��
		setHiddenFieldCode(new String[] { "pk_balatype" });
		setTableName("bd_balatype");
		setPkFieldCode("pk_balatype");
		setWherePart(" (pk_corp='" + getPk_corp() + "' or pk_corp= '" + getGroupCode() + "' or pk_corp is null) and pk_balatype in ('0001A810000000000JBQ', '0001A810000000000JBW', '0001A810000000000JBY' , '0001A810000000000JC0') ");
		setSealedWherePart("sealflag is null or sealflag <>'Y'");
		resetFieldName();
	}

}
