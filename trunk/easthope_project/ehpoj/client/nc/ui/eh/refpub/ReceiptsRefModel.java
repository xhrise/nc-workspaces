package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 
 * 功能:收货通知单多选参照
 * 作者:wb
 * 日期:2009-11-12 10:53:09
 */
public class ReceiptsRefModel extends AbstractRefModel {
	
	public ReceiptsRefModel() {
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
        return new String[]{"billno","dmakedate","custname","pk_receipt"};
    
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getFieldName()
     */
    @Override
	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{"单据号","制单日期","客户","主键"};
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getRefTitle()
     */
    @Override
	public String getRefTitle() {
        // TODO Auto-generated method stub
        return "收货通知单";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getTableName()
     */
    @Override
	public String getTableName() {
        // TODO Auto-generated method stub
        return "eh_view_receipt";
    }

    /* (non-Javadoc)
     * @see nc.ui.bd.ref.IRefModel#getPkFieldCode()
     */
    @Override
	public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "pk_receipt";
    }

    @Override
	public String getWherePart() {
		return " pk_corp = '"+getPk_corp()+"'";
	}

}
