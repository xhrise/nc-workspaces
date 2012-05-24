package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class StorRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    String pk_corp=ce.getCorporation().getPk_corp();
    String Corporation=ce.getUser().getPrimaryKey();
    
	public StorRefModel() {
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
               "storcode","storname","pk_stordoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "仓库编码","仓库名称","主键"
			};
	}
    
	     
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "仓库档案";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "bd_stordoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_stordoc";
	}

	
//    @Override
//	public String getWherePart() {
//        return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and pk_stordoc in (" +
//        		"select pk_bdstordoc from  eh_stordoc where (pk_stordoc in (select pk_stordoc from eh_stordoc_b   where cuserid ='"+Corporation+"' ) or pk_psndoc = '"+Corporation+"') and nvl(dr,0)=0) ";
//    }
    
	/**按花召滨要求去除掉仓库权限控制条件 add by zqy 2010年11月23日10:51:22**/
	public String getWherePart() {
        //return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and storcode not like 'z%'";
        return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and storcode not like 'z%' and pk_stordoc in (select pk_bdstordoc from eh_stordoc where pk_corp='"+pk_corp+"' and nvl(dr,0)=0)";
    }

}
