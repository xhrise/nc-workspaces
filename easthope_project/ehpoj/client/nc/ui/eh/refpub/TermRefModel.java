package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/** 
 * ˵�������鵵������
 * @author ���� 
 * ʱ�䣺2008��5��7��14:30:46
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
                "�������","��������"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "���鵵��";
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
