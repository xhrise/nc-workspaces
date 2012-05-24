/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 功能：根据客商得到上级客商(在二次折扣中)
 * @author wb
 * @date 2008-5-5 18:52:00
 */
public class CubasBySubcuRefModel extends AbstractRefModel {
	public static String pk_subcubasdoc = null;
	public CubasBySubcuRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 2;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO 自动生成方法存根
		return new String[]{
				"mnecode","custname","custcode","custshortname","taxpayerid","conaddr","phone2","pk_cumandoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
				"助记码","客户名称","客户编码","客户简称","纳税人登记号","通信地址","电话","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "客户档案";
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_view_cumandoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_cumandoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and pk_cubasdoc in ")
        .append(" (")
        .append(" select pk_cubasdoc1 from bd_cubasdoc a,bd_cumandoc b")
        .append(" where a.pk_cubasdoc = b.pk_cubasdoc and pk_cumandoc = '"+pk_subcubasdoc+"'")
        .append(" and nvl(a.dr, 0) = 0 and nvl(b.dr,0)=0")
        .append(" union all")
        .append(" select a.pk_cubasdoc from bd_cubasdoc a,bd_cumandoc b")
        .append(" where a.pk_cubasdoc = b.pk_cubasdoc and pk_cumandoc = '"+pk_subcubasdoc+"'")
        .append(" and nvl(a.dr, 0) = 0 and nvl(b.dr,0)=0 ")
        .append(" ) ");
    	return sql.toString();
    }

}
