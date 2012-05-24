package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class CustlevelRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public CustlevelRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "levelcode","levelname","memo","pk_custleve"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "�������","��������","��ע","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "�ͻ�����";
	}

	@Override
	public String getTableName() {
		return "eh_custlevel";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_custleve";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }



}
