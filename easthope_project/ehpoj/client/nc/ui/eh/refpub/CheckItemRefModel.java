package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class CheckItemRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public CheckItemRefModel() {
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
               "itemname","itemcode","pk_checkitem"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "�������","������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�����Ŀ";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_bd_checkitem";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_checkitem";
	}

	
    @Override
	public String getWherePart() {
        return " pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
