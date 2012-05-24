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
                "仓库类型编码","仓库类型名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "仓库类型";
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
