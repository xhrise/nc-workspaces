package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 出入库类型
 * wb
 * 2008-7-11 14:47:28
 * */
public class CRtypeRefModel extends AbstractRefModel {
	 ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
	    
	    String pk_corp=ce.getCorporation().getPk_corp();;
	    public CRtypeRefModel() {
	        super();
	        // TODO 自动生成构造函数存根
	    }
	    @Override
	    public int getDefaultFieldCount() {
	    	// TODO Auto-generated method stub
	    	return 2;
	    }
	    
	    @Override
		public String[] getFieldCode() {
	        // TODO 自动生成方法存根
	        return new String[]{
	                "typecode","typename","pk_intype"
	        };
	    }

	    @Override
		public String[] getFieldName() {
	        // TODO 自动生成方法存根
	        return new String[]{
	                "类型编码","类型名称","主键"
	            };
	    }

	    
	    @Override
		public String getRefTitle() {
	        // TODO 自动生成方法存根
	        return "业务类型";
	    }

	    @Override
		public String getTableName() {
	        // TODO 自动生成方法存根
	        return "eh_busitype";
	    }

	    @Override
		public String getPkFieldCode() {
	        // TODO 自动生成方法存根
	        return "pk_intype";
	    }
	    
	    @Override
	    public String getAddWherePart() {
	    	return " and pk_corp = '"+getPk_corp()+"' ";
	    }
	    
}
