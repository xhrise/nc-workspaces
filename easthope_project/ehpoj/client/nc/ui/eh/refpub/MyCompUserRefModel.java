package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class MyCompUserRefModel extends AbstractRefModel {
    
	public MyCompUserRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
				 "user_code", "user_name", "cuserid"
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
		return "人员";
	}

	@Override
	public String getTableName() {
		return "sm_user";
	}

	@Override
	public String getPkFieldCode() {
		return "cuserid";
	}

	
    @Override
	public String getWherePart() {
        return "  cuserid in (select userid from sm_userandclerk where  substr(pk_psndoc,1,4) = '"+getPk_corp()+"') and nvl(dr,0)=0";
    }


}
