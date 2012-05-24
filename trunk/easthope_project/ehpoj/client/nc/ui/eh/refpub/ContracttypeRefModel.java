package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 
���ܣ��ɹ���������
���ߣ�zqy
���ڣ�2008-11-30 ����04:09:23
 */

public class ContracttypeRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public ContracttypeRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{
                "typecode","typename","pk_contracttype"
		};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{
                "���ͱ���","��������","����"
		};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_contracttype";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "�ɹ���������";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "eh_contracttype";
	}
	
	@Override
	public String getWherePart() {
		//�޸�Ϊ����ά����ʱ�䣺2010-02-24.���ߣ���־Զ
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
		return " isnull(dr,0)=0 ";
    }

	
}
