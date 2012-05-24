package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/** 
 * ˵�������鵵������
 * @author ���� 
 * ʱ�䣺2008��5��7��14:30:46
 */
public class BdDeviceRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public BdDeviceRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "devicode","deviname","pk_device"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "�������","��������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "���鵵��";
	}

	@Override
	public String getTableName() {
		return "eh_bd_device";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_device";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
