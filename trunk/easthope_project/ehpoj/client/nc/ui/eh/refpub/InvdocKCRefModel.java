/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 功能：物料库存(成品检测自制)
 * @author wb
 * @date 2008-12-2 15:32:56
 */
public class InvdocKCRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	 private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public InvdocKCRefModel() {
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
               "invcode","invmnecode","invname","invspec","invtype","def1","brandname","amount","pk_invbasdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "物料编码","助记码","物料名称","规格","型号","颜色","品牌","库存","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "物料库存";
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_kcinvbasdoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_invbasdoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" isnull(is_flag,0) = 1 and pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
    	return sql.toString();
    }

}
