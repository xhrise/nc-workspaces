/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 功能：根据客商得到物料(销售订单)
 * @author wb
 * @date 2008-5-5 18:52:00
 */
public class InvdocByCusdocRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	public InvdocByCusdocRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 3;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO 自动生成方法存根
		return new String[]{
               "invcode","invmnecode","invname","invspec","invtype","colour","brandname","pk_invmandoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "物料编码","助记码","物料名称","规格","型号","颜色","品牌","主键"
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
		return "eh_view_invmandoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_invmandoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" pk_invmandoc in")
        .append(" (")
        .append(" select pk_invbasdoc from eh_custkxl")
        .append(" where pk_cubasdoc like '"+pk_cubasdoc+"'")
        .append(" and nvl(dr,0)=0")
        .append(" )");
    	return sql.toString();
    }

}
