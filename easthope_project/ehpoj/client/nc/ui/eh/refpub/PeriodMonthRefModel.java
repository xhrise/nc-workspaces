package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * ���ܣ�����·ݲ���
 * ����:����
 * ����:2008��5��12��15:28:10
 */
public class PeriodMonthRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public PeriodMonthRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}

    @Override
	public int getDefaultFieldCount(){
        return 3;
    }
    
    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[]{"SUBSTRING(begindate,1,7)","begindate","enddate","pk_period"};
    
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getFieldName()
     */
    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{"���-�·�","��ʼ����","��������","����"};
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getRefTitle()
     */
    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "�·�ѡ��";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getTableName()
     */
    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_period";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getPkFieldCode()
     */
    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_period";
    }

    @Override
	public String getWherePart() {
		return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and nvl(jz_flag,'N')='N' and isnull(dr,0)=0";
	}

}
