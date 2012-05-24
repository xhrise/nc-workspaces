package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * 收付款类型
 */
public class SfkTypeRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    String pk_corp=ce.getCorporation().getPk_corp();
    String Corporation=ce.getUser().getPrimaryKey();
    
	public SfkTypeRefModel() {
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
               "typecode","typename","pk_sfktype"
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
		return "收付款类型";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_arap_sfktype";
	} 

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_sfktype";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";// and pk_corp='"+pk_corp+"' ";//收付款类型调整为集团维护。时间：2010-02-24.作者：张志远
    }
    

}
