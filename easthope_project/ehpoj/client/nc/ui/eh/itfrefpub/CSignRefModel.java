package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * ƾ֤������
 * wb
 * 2008-7-11 14:47:28
 * */
public class CSignRefModel extends AbstractRefModel {
	public CSignRefModel() {
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
               "typecode","typename","pk_vouchertype"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "������","�������","����"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "ƾ֤���";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_arap_vouchertype";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_vouchertype";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+getPk_corp()+"' and isnull(dr,0)=0 ";
    }
    

}
