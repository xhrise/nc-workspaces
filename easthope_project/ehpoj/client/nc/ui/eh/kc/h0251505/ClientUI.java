
package nc.ui.eh.kc.h0251505;

import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;

/**
 * 说明：生产备料单 
 * @author 张起源
 * 时间：2008-5-07
 */
public class ClientUI extends AbstractClientUI {
	public ClientUI() {
	     super();
	 }
   
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
		getBillCardPanel().setHeadItem("lldate", _getDate());
        super.setDefaultData();
	}

    
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
	    btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	    addPrivateButton(btn1);
	    super.initPrivateButton();
	}
	
    
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		if("pk_psndoc".equals(strKey)){
			String pk_psndoc = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject()==null?"":
				getBillCardWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject().toString();
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			StringBuffer sql = new StringBuffer()
			.append(" select pk_deptdoc from bd_deptdoc ")
			.append(" where pk_psndoc ='"+pk_psndoc+"'")
			.append(" and isnull(dr,0)=0 ");
			try{
				Vector al = (Vector) iUAPQueryBS.executeQuery(sql.toString(),new VectorProcessor());
				if(al!=null&&al.size()>0){
					Vector hm = (Vector) al.get(0);
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", hm.get(0));
				}
			}catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
        
        if("vinvcode".equals(strKey)){
            String vck = getBillCardWrapper().getBillCardPanel().getBodyItem("vck").getValueObject()==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyItem("vck").getValueObject().toString();
            IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            StringBuffer sql = new StringBuffer()
            .append(" select pk_psndoc from eh_stordoc ")
            .append(" where storname = '"+vck+"'");
            try{
                Vector al = (Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                int row = getBillCardWrapper().getBillCardPanel().getRowCount();
                for(int i=0;i<row;i++){
                   if(al!=null && al.size()>0){
                       Vector hm = (Vector) al.get(0);
                       getBillCardWrapper().getBillCardPanel().setBodyValueAt(hm.get(0), i, "pk_manage");
                   }
               }               
            }catch (Exception e1) {
                e1.printStackTrace();
            }
        }		
		super.afterEdit(e);
	}
    
    
}