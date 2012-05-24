package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * �ɱ��������
 * @author wangbing
 * 
 */
public class CostobjRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();

	public CostobjRefModel() {
		super();
	}

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[] { "costobjcode", "costobjname", "pk_costobj" };
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[] { "�ɱ��������", "�ɱ���������", "����" };
	}

	@Override
	public String getRefTitle() {
		return "�ɱ�����";
	}

	@Override
	public String getTableName() {
		return "eh_arap_costobj";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_costobj";
	}

	@Override
	public String getWherePart() {
		return "pk_corp = '" + ce.getCorporation().getPk_corp()
				+ "' and isnull(dr,0)=0";
	}

}
