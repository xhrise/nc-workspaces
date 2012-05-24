package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * ���ܣ��ڼ����
 * ����:zqy
 * ����:2008-9-11 10:46:31
 */
public class PeriodRefModel extends AbstractRefModel {
	
	public PeriodRefModel() {
		super();
	}

    @Override
	public int getDefaultFieldCount(){
        return 1;
    }
    
    @Override
	public String[] getFieldCode() {
        return new String[]{"nmonth","nyear","pk_period"};
    }

    @Override
	public String[] getFieldName() {
        return new String[]{"��","��","����"};
    }

    @Override
	public String getRefTitle() {
        return "�ڼ�ѡ��";
    }

    @Override
	public String getTableName() {
        return "eh_period";
    }

    @Override
	public String getPkFieldCode() {
        return "pk_period";
    }

    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    @Override
	public String getWherePart() {
		return "isnull(dr,0)=0 and pk_corp = '"+ce.getCorporation().getPk_corp()+"' ";
	}

}
