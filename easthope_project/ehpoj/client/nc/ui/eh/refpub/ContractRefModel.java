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
                "类型编码","类型名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "合同类型";
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
    	//修改为集团维护。时间：2010-02-24.作者：张志远
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    	return " isnull(dr,0)=0 ";
    }


}
