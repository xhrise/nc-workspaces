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
                "��λ����","��λ����","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "��λ";
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
