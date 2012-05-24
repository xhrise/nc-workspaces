package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * U8模板字段参照
 * wb
 * 2008-7-11 14:47:28
 * */
public class TColumRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    public static String pk_billtype = null;
    public static String tablename_h = null;
    public static String tablename_b = null;
    public TColumRefModel() {
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
               "defaultshowname","itemkey","case pos when 0 then '表头' when 1 then '表体' when 2 then '表尾' end","pk_billtemplet_b"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
				"名称","字段","模板位置","主键"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "凭证模板字段参照";
	}
	
	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "pub_billtemplet_b";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_billtemplet_b";
	}

	
    @Override
	public String getWherePart() {
        StringBuffer whereSQL = new StringBuffer()
//        .append(" pk_billtemplet in (select pk_billtemplet from pub_billtemplet where pk_billtypecode = '"+pk_billtype+"') ")
//        .append(" and itemkey in (select b.name from sys.tables a , sys.all_columns b,sys.types c ")
//        .append(" where a.object_id = b.object_id and c.system_type_id = b.system_type_id")
//        .append(" and c.name = 'numeric' and a.name in( '"+tablename_h+"','"+tablename_b+"'))")
//        .append(" and isnull(dr,0)=0 ");
        .append(" table_code in('"+tablename_h+"','"+tablename_b+"') and isnull(dr,0)=0 ");
    	return whereSQL.toString();// and itemkey not like 'v%'
    }
    

}
