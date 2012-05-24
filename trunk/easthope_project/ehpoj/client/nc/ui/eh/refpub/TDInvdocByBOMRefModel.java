/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 功能：根据成品到BOM中查询替代物料(材料出库)
 * @author wb
 * @date 2008-5-5 18:52:00
 */
public class TDInvdocByBOMRefModel extends AbstractRefModel {
	public static String pk_cpinvbasdocs;
	public static String pk_ylinvbaddoc;    //当前的物料
	public TDInvdocByBOMRefModel() {
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
               "invcode","invmnecode","invname","invspec","invtype","pk_invbasdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "物料编码","助记码","物料名称","规格","型号","主键"
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
    	StringBuffer sql = new StringBuffer()
        .append(" isnull(dr,0)=0 and pk_invbasdoc in")
        .append(" (")
        .append(" select distinct b.pk_altinvbasdoc from eh_bom a,eh_bom_b b ")
        .append(" where a.pk_bom = b.pk_bom and a.pk_invbasdoc in "+pk_cpinvbasdocs+" and b.pk_invbasdoc ='"+pk_ylinvbaddoc+"'")
        .append(" and isnull(a.new_flag,'Y')='Y' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
        .append(" )");
    	return sql.toString();
    }

}
