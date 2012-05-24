package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 
功能：采购单价类型
作者：zqy
日期：2008-11-30 下午04:09:23
 */

public class ContracttypeRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public ContracttypeRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{
                "typecode","typename","pk_contracttype"
		};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{
                "类型编码","类型名称","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_contracttype";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "采购单价类型";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "eh_contracttype";
	}
	
	@Override
	public String getWherePart() {
		//修改为集团维护。时间：2010-02-24.作者：张志远
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
		return " isnull(dr,0)=0 ";
    }

	
}
