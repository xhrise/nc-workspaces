package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class ContractRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public ContractRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_contracttype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "���ͱ���","��������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "��ͬ����";
	}

	@Override
	public String getTableName() {
		return "eh_contract_type";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_contracttype";
	}

	
    @Override
	public String getWherePart() {
    	//�޸�Ϊ����ά����ʱ�䣺2010-02-24.���ߣ���־Զ
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    	return " isnull(dr,0)=0 ";
    }


}
