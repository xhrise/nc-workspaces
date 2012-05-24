package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class KZKJRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public KZKJRefModel() {
		super();
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
		return new String[]{
               "kzkjname","formulae","pk_kzkj"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "���ؿۼ۷�ʽ","��ʽ","����"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		return "���ؿۼ۹�ʽ";
	}
	
	@Override
	public String getTableName() {
		return "eh_kzkj";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_kzkj";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and NVL(dr,0)=0 ";
    }
    

}
