package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * ���ܣ�����ȡ������ȵ���--�����������
 * ����:honghai
 * ����:2007-3-13
 */
public class PeriodYearRefModel extends AbstractRefModel {
	
	public PeriodYearRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}

    @Override
	public int getDefaultFieldCount(){
        return 1;
    }
    
    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[]{"periodyear","periodyear as p"};
    
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getFieldName()
     */
    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{"�������","�������"};
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getRefTitle()
     */
    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "���ѡ��";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getTableName()
     */
    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "bd_accperiod";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getPkFieldCode()
     */
    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "periodyear";
    }

    @Override
	public String getWherePart() {
		return "isnull(dr,0)=0 ";
	}

}
