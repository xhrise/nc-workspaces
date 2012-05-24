package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/*
 * 名称：出库类型
 * @author 张起源
 * 2008-7-24 9:39:11
 * 
 * */

public class OuttypeRefModel extends AbstractRefModel {
	
	public OuttypeRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_outtype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "入库类型编码","入库类型名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "出库类型";
	}

	@Override
	public String getTableName() {
		return "eh_outtype";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_outtype";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }



}
