package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class SkRefModel extends AbstractRefModel {
	
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public SkRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "billno","pk_cubasdoc","zje"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "编号","客商编码","金额"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "收款单";
	}

	@Override
	public String getTableName() {
		return "eh_arap_sk";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_sk";
	}

	
    @Override
	public String getWherePart() {
//        return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
    	return null;
    }



}
