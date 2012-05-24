package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class DiscountTypeRefModel extends AbstractRefModel {
	public DiscountTypeRefModel() {
		super();
		// TODO 自动生成构造函数存根
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
		// TODO 自动生成方法存根
		return new String[]{
               "typecode","typename","pk_discounttype"
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
		return "折扣类型档案";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_discounttype";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_discounttype";
	}

	 
    @Override
	public String getWherePart() {
        return "  isnull(dr,0)=0 ";
    }
    

}
