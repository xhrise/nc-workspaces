package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class KZKJRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public KZKJRefModel() {
		super();
	}
	
    /**
     * 默认显示字段中的显示字段数----表示显示前几个字段
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
                "扣重扣价方式","公式","主键"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		return "扣重扣价公式";
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
