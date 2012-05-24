package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/** 
 * 说明：设备型号参照
 * @author 王兵 
 * 时间：2009-1-12 15:30:02
 */
public class SBtypeRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public SBtypeRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_sbtype"
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
		return "设备型号档案";
	}

	@Override
	public String getTableName() {
		return "eh_sc_sbtype";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sbtype";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }


}
