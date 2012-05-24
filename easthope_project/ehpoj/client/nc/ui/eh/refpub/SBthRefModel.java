package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 退回司磅单的参照
 * wb 2008-7-17 9:30:42
 * */
public class SBthRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public static String pk_cubasdoc = null;
	
	public SBthRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
				"carnumber","billno","pk_sbbill"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
				"车号","司磅单号","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "退货司磅";
	}

	@Override
	public String getTableName() {
		return "eh_sbbill ";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sbbill";
	}
	
	@Override
	public void setQuerySql(String arg0) {
		// TODO Auto-generated method stub
		super.setQuerySql(arg0);
	}
    @Override
	public String getWherePart() {
        return " sbtype = 2 and pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 and (isnull(def_6,0)=0 or isnull(def_7,0)=0) ";//and a.pk_cubasdoc = '"+pk_cubasdoc+"' 
    }
}
