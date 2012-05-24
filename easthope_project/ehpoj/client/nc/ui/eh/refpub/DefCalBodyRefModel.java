package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class DefCalBodyRefModel extends AbstractRefModel {
   
    
	public DefCalBodyRefModel() {
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
               "doccode","docname","pk_defdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "库存组织编码","库存组织名称","主键"
			};
	}
    
	     
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "自定义库存组织";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_defcalbody";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_defdoc";
	}

}
