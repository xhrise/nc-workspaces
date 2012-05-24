package nc.ui.eh.cw.h1102005;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;

/**
 * 说明：查款单 
 * @author 张起源
 * 时间：2008-5-28 10:30:26
 */
public class ClientUI extends AbstractClientUI {
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	@Override
	protected void initSelfData() {      
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
        //初始化差款日期 
        getBillCardPanel().setHeadItem("qr_rq", _getDate());
        getBillCardPanel().setHeadItem("pk_cubasdoc", null);
		super.setDefaultData();
	}
    
    @Override
    public void afterEdit(BillEditEvent e) {
        String strKey = e.getKey();
        //选择客户把营销代表带出来
        String pk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        if(strKey.equals("pk_cubasdoc")){
            IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            StringBuffer sql = new StringBuffer()
            .append(" select pk_psndoc from eh_custyxdb where pk_cubasdoc = '"+pk_cubasdoc+"' and ismain ='Y' and isnull(dr,0)=0 ");
            
            try {
                Vector al = (Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                if(al!=null && al.size()>0){
                    Vector all = (Vector) al.get(0);
                    getBillCardPanel().setHeadItem("pk_psndoc", all.get(0));
                    String[] formual = getBillCardPanel().getHeadItem("pk_psndoc").getEditFormulas();
                    getBillCardPanel().execHeadFormulas(formual);  //获取编辑公式
                }               
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }            
        }
        
        super.afterEdit(e);
    }
    
    @Override
	protected void initPrivateButton() {
        super.initPrivateButton();
    }

	
}