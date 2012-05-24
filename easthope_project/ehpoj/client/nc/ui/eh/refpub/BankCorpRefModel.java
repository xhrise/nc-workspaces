package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 公司开户银行
 * @author 
 * wb 2009-7-3 10:32:36
 */
public class BankCorpRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public BankCorpRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{
				"bankacc","bankname","remcode","pk_accbank"
		};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{
				"银行账号","开户银行名称","助记码","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_accbank";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "开户银行";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_accbank";
	}
	
	@Override
	public String getWherePart() {
        StringBuffer addSQL = new StringBuffer()
        .append("pk_accbank IN ( ")
        .append(" SELECT pk_accbank FROM bd_accbank_auth WHERE pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0")
        .append(" ) and isnull(dr,0)=0 ");
        return addSQL.toString();
	}
	
}
