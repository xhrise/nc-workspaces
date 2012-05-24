package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class MarketareaRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	
	public MarketareaRefModel() {
		super();
	}
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "areacode","areaname","memo","pk_marketarea"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "区域编码","区域名称","备注","主键"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "市场区域";
	}

	@Override
	public String getTableName() {
		return "eh_marketarea";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_marketarea";
	}

	
    @Override
	public String getWherePart() {
        //return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
        return "(pk_corp = '"+ce.getCorporation().getPk_corp()+"' or pk_corp='0001') and isnull(dr,0)=0 ";//modify by houcq 2010-12-24
    }


}


