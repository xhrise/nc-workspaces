package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/** 
 * 说明：班组档案参照
 * @author 王兵 
 * 时间：2008年5月7日14:30:46
 */
public class TermRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public TermRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "teamcode","teamname","pk_team"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "班组编码","班组名称"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "班组档案";
	}

	@Override
	public String getTableName() {
		return "eh_bd_team";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_team";
	}
	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }

}
