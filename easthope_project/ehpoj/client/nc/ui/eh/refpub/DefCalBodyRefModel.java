package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class DefCalBodyRefModel extends AbstractRefModel {
   
    
	public DefCalBodyRefModel() {
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
               "doccode","docname","pk_defdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "�����֯����","�����֯����","����"
			};
	}
    
	     
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�Զ�������֯";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_defcalbody";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_defdoc";
	}

}
