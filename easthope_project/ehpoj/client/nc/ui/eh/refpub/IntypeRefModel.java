package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class IntypeRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	
	public IntypeRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_intype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "������ͱ���","�����������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "�������";
	}

	@Override
	public String getTableName() {
		return "eh_intype";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_intype";
	}

	
    @Override
	public String getWherePart() {
        return "  isnull(dr,0)=0 ";
    }



}
