package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * ���������
 * wb
 * 2008-7-11 14:47:28
 * */
public class CRtypeRefModel extends AbstractRefModel {
	 ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
	    
	    String pk_corp=ce.getCorporation().getPk_corp();;
	    public CRtypeRefModel() {
	        super();
	        // TODO �Զ����ɹ��캯�����
	    }
	    @Override
	    public int getDefaultFieldCount() {
	    	// TODO Auto-generated method stub
	    	return 2;
	    }
	    
	    @Override
		public String[] getFieldCode() {
	        // TODO �Զ����ɷ������
	        return new String[]{
	                "typecode","typename","pk_intype"
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
	        return "ҵ������";
	    }

	    @Override
		public String getTableName() {
	        // TODO �Զ����ɷ������
	        return "eh_busitype";
	    }

	    @Override
		public String getPkFieldCode() {
	        // TODO �Զ����ɷ������
	        return "pk_intype";
	    }
	    
	    @Override
	    public String getAddWherePart() {
	    	return " and pk_corp = '"+getPk_corp()+"' ";
	    }
	    
}
