package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class IsoRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public IsoRefModel() {
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
               "invcode","invname","pk_invbasdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "物料编码","物料名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "物料档案";
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_invbasdoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_invbasdoc";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
