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
                "入库类型编码","入库类型名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "入库类型";
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
