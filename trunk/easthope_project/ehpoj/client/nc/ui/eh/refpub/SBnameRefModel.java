package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/** 
 * 说明：设备名称参照
 * @author 王兵 
 * 时间：2009-1-12 15:30:02
 */
public class SBnameRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public SBnameRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_sbname"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "编码","名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "设备名称档案";
	}

	@Override
	public String getTableName() {
		return "eh_sc_sbname";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sbname";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }


}
