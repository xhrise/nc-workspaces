package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class StoreTypeRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public StoreTypeRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_storetype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "�ֿ����ͱ���","�ֿ���������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "�ֿ�����";
	}

	@Override
	public String getTableName() {
		return "eh_store_type";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_storetype";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }



}
