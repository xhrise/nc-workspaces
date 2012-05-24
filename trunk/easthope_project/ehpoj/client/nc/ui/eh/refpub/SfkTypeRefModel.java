package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * �ո�������
 */
public class SfkTypeRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    String pk_corp=ce.getCorporation().getPk_corp();
    String Corporation=ce.getUser().getPrimaryKey();
    
	public SfkTypeRefModel() {
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
               "typecode","typename","pk_sfktype"
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
		return "�ո�������";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_arap_sfktype";
	} 

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_sfktype";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";// and pk_corp='"+pk_corp+"' ";//�ո������͵���Ϊ����ά����ʱ�䣺2010-02-24.���ߣ���־Զ
    }
    

}
