package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class PosSwRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public PosSwRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "poscode","posname","pk_pos"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "仓位编码","仓位名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "仓位";
	}

	@Override
	public String getTableName() {
		return "eh_pos";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_pos";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }



}
