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
                "���ͱ���","��������"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "��������";
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
