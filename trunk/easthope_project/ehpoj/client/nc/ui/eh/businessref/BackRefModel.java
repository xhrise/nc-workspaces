package nc.ui.eh.businessref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
  
/**
 * 功能：退货入库
 * @author wm
 * @date  2008-6-27 13:36:23
 */

public class BackRefModel  extends AbstractRefModel{
     
	
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public BackRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 2;
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{
				"billno","custname","pk_back"
		};
	}

	@Override
	public String[] getFieldName() {
		return new String[]{
				"单据号","客商名称","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_back";
	}

	@Override
	public String getRefTitle() {
		return "原料退货司磅";
	}

	@Override
	public String getTableName() {
		//return "eh_stock_back a ,  bd_cubasdoc b,bd_cumandoc bb ";
		return "EH_VIEW_STOCK_BACK ";
	}
//	vbilltype='ZA53' and
	@Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
    }
}

