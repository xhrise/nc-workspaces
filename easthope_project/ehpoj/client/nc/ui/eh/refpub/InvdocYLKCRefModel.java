/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 功能：原料库存(五金出库)
 * @author houcq
 * @date 2011-02-24
 */
public class InvdocYLKCRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	 private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public InvdocYLKCRefModel() {
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
		return "原料库存";
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_ylkcinvbasdoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_invbasdoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
    	return sql.toString();
    }

}
