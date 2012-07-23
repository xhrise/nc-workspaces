
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
public class StorDocDefaulteRefModel extends AbstractRefModel {

	public StorDocDefaulteRefModel() {
		super();
		
		init();
		// TODO �Զ����ɹ��캯�����
	}
	public void init() {
		
		setRefTitle("�ֿ⵵��");
		
		setFieldCode(new String[] { "pk_stordoc" , "storcode", "storname", "storaddr" }); //19��
		
		setFieldName(new String[] { "����" , "�ֿ����" , "�ֿ�����" , "�ֿ��ַ"});
		
		setHiddenFieldCode(new String[] { "pk_stordoc" });
		
		setTableName("bd_stordoc");
		
		setPkFieldCode("pk_stordoc");
		
		setRefCodeField("pk_stordoc");
		
		setRefNameField("storname");
		
		String subSqlCalBody_dataPower = getDataPowerSubSql("bd_calbody",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0001825")/*@res "�����֯"*/);
		
		String subSqlCalBoby = "select pk_calbody from bd_calbody where sealflag is null or sealflag='N'";
		
		if (subSqlCalBody_dataPower !=null) {
			if (subSqlCalBody_dataPower.trim().equals(" 1 < 0 ")) 
				subSqlCalBoby = subSqlCalBoby + " and (" + subSqlCalBody_dataPower + ")";
			 else 
				subSqlCalBoby = subSqlCalBoby + " and  pk_calbody in (" + subSqlCalBody_dataPower+")";;

		}
		
		setWherePart(" pk_corp='" + getPk_corp() + "' and pk_calbody in ("+subSqlCalBoby+")");
		
		setOrderPart(" bd_stordoc.pk_stordoc ");
		
	}

}
