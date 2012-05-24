package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class LadingbillRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public LadingbillRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}

    /**
     * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
     */
    @Override
	public int getDefaultFieldCount() {
        return 3;
    }
    
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "billno","billno","pk_ladingbill"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "�������","�������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "���֪ͨ��";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_ladingbill";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_ladingbill";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
