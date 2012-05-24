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
	        return new String[] {"�������","��������", "����","����"};
	    }

	    @Override
		public String getPkFieldCode() {
	        return "pk_areacl";
	    }

	    @Override
		public String getRefTitle() {
	        return "��������";
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
