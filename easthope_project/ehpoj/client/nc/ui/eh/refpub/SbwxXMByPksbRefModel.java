/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * 功能：根据设备得到该设备的维修项目
 * @author wb
 * @date 2008-12-20 18:27:25
 */
public class SbwxXMByPksbRefModel extends AbstractRefModel {
	public static String pk_sb;
	public SbwxXMByPksbRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 4;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO 自动生成方法存根
		return new String[]{
               "project","project projecta","period","lastdate","pk_sb_b"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
                "维护项目","维护项目","维护周期","最后维护时间","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "设备维修项目";
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "eh_sc_sbbasdoc_b";
	}

	@Override
	public String getPkFieldCode() {
		// TODO 自动生成方法存根
		return "pk_sb_b";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" isnull(dr,0)=0 and pk_sb = '"+pk_sb+"'");
    	return sql.toString();
    }

}
