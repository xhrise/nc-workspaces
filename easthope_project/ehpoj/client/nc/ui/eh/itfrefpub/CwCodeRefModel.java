package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * ��ƿ�Ŀ����
 * wb
 * 2008-7-11 14:47:28
 * */
public class CwCodeRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
	public CwCodeRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}
	
    /**
     * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
     */
    @Override
	public int getDefaultFieldCount() {
        return 2;
    }
    
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "ccode","ccode_name","pk_code"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "��Ŀ����","��Ŀ����","����"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "��ƿ�Ŀ";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_code";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_code";
	}

	
    @Override
	public String getWherePart() {
        return null;
    }
    

}
