package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 会计科目参照
 * wb
 * 2008-7-11 14:47:28
 * */
public class CwCodeRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
	public CwCodeRefModel() {
		super();
		// TODO 自动生成构造函数存根
	}
	
    /**
     * 默认显示字段中的显示字段数----表示显示前几个字段
     */
    @Override
	public int getDefaultFieldCount() {
        return 2;
    }
    
	@Override
	public String[] getFieldCode() {
		// TODO 自动生成方法存根
		return new String[]{
               "ccode","ccode_name","pk_code"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "科目编码","科目名称","主键"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "会计科目";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_code";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_code";
	}

	
    @Override
	public String getWherePart() {
        return null;
    }
    

}
