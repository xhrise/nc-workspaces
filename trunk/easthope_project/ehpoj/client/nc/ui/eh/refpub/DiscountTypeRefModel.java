package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class DiscountTypeRefModel extends AbstractRefModel {
	public DiscountTypeRefModel() {
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
               "typecode","typename","pk_discounttype"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "���ͱ���","��������","����"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�ۿ����͵���";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_discounttype";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_discounttype";
	}

	 
    @Override
	public String getWherePart() {
        return "  isnull(dr,0)=0 ";
    }
    

}
