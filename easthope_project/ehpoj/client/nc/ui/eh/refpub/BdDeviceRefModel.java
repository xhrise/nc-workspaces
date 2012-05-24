package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/** 
 * 说明：机组档案参照
 * @author 王兵 
 * 时间：2008年5月7日14:30:46
 */
public class BdDeviceRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public BdDeviceRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "devicode","deviname","pk_device"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "机组编码","机组名称","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "机组档案";
	}

	@Override
	public String getTableName() {
		return "eh_bd_device";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_device";
	}

	
    @Override
	public String getWherePart() {
        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    }


}
