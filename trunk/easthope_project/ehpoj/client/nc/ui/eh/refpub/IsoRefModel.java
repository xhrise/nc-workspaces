package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class IsoRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public IsoRefModel() {
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
               "invcode","invname","pk_invbasdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "���ϱ���","��������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "���ϵ���";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_invbasdoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_invbasdoc";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
