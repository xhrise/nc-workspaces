package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 凭证类别参照
 * wb
 * 2008-7-11 14:47:28
 * */
public class CSignRefModel extends AbstractRefModel {
	public CSignRefModel() {
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
               "typecode","typename","pk_vouchertype"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "类别编码","类别名称","主键"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "凭证类别";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_arap_vouchertype";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_vouchertype";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+getPk_corp()+"' and isnull(dr,0)=0 ";
    }
    

}
