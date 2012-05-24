package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class SwRefModel extends AbstractRefModel {
	
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public SwRefModel() {
		super();
	} 
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "swcode","swname","pk_sw"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "编码","名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "发运方式";
	}

	@Override
	public String getTableName() {
		return "eh_sw";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sw";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }



}
