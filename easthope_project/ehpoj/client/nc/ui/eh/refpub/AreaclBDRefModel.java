package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefTreeModel;

public class AreaclBDRefModel extends AbstractRefTreeModel {
	 
	 @Override
	public String getChildField() {
	        return "pk_areacl";
	    }

	    @Override
		public String getCodingRule() {
	        return null;
	    }

	    @Override
		public String getEndField() {
	        return null;
	    }

	    @Override
		public String getFatherField() {
	        return "pk_fatherarea";
	    }

	    @Override
		public String[] getFieldCode() {
	        return new String[] {"areaclcode","areaclname", "pk_areacl","pk_fatherarea"};
	    }

	    @Override
		public String[] getFieldName() {
	        return new String[] {"地区编号","地区名称", "主键","父键"};
	    }

	    @Override
		public String getPkFieldCode() {
	        return "pk_areacl";
	    }

	    @Override
		public String getRefTitle() {
	        return "地区参照";
	    }

	    @Override
		public String getTableName() {
	        return "bd_areacl";
	    }
        
	    @Override
		public String getWherePart() {
	        return " isnull(dr,0)=0";
	    }

}
