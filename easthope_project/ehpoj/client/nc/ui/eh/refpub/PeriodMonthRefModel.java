package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * 功能：会计月份参照
 * 作者:王兵
 * 日期:2008年5月12日15:28:10
 */
public class PeriodMonthRefModel extends AbstractRefModel {
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public PeriodMonthRefModel() {
		super();
		// TODO 自动生成构造函数存根
	}

    @Override
	public int getDefaultFieldCount(){
        return 3;
    }
    
    @Override
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[]{"SUBSTRING(begindate,1,7)","begindate","enddate","pk_period"};
    
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getFieldName()
     */
    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{"年度-月份","开始日期","结束日期","主键"};
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getRefTitle()
     */
    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "月份选择";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getTableName()
     */
    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_period";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getPkFieldCode()
     */
    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_period";
    }

    @Override
	public String getWherePart() {
		return "pk_corp = '"+ce.getCorporation().getPk_corp()+"' and nvl(jz_flag,'N')='N' and isnull(dr,0)=0";
	}

}
