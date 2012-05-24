package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class WorkshopRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public WorkshopRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "workshopcode","workshopname","pk_contracttype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "车间编码","车间名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "车间";
	}

	@Override
	public String getTableName() {
		return "eh_workshop";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_contracttype";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and  isnull(dr,0)=0 ";
    }




}
