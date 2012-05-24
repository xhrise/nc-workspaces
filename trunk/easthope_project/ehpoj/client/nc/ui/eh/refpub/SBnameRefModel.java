package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/** 
 * ˵�����豸���Ʋ���
 * @author ���� 
 * ʱ�䣺2009-1-12 15:30:02
 */
public class SBnameRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public SBnameRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_sbname"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "����","����","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "�豸���Ƶ���";
	}

	@Override
	public String getTableName() {
		return "eh_sc_sbname";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sbname";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }


}
