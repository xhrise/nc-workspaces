package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * @author houcq 
 *
 */
public class PrizeRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public PrizeRefModel(){
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
				"prizecode","prizename","integral","pk_prize"
		};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{
				"奖品编码","奖品名称","兑换积分","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_prize";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "奖品参照";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "eh_prize";
	}
	
	@Override
	public String getWherePart() {
        
		//return " pk_corp='"+getPk_corp()+"' and nvl(dr,0)=0 and nvl(isqy,'N')='Y'";
		String str =" nvl(dr,0)=0 and nvl(isqy,'N')='Y'";
		return str;
    }

	
}
