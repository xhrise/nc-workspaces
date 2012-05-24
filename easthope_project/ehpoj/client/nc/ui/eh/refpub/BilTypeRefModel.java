package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class BilTypeRefModel extends AbstractRefModel {
    
	public BilTypeRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "pk_billtypecode","billtypename"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "类型编码","类型名称"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "单据类型";
	}

	@Override
	public String getTableName() {
		return "bd_billtype";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_billtypecode";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 and pk_billtypecode like 'Z%' ";
    }


}
