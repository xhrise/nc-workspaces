/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.stock.h0150305;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能:五金采购申请
 * ZB22
 * @author WB
 * 2009-1-5 15:33:07
 *
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI{
	
	public ClientUI() {
        super();
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO 自动生成方法存根
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
    	getBillCardPanel().setHeadItem("applydate", _getDate());
    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	String sql = "select * from bd_psndoc where pk_psnbasdoc = (select pk_psndoc FROM sm_userandclerk su WHERE su.userid = '"+_getOperator()+"')";
    	Object obj = iUAPQueryBS.executeQuery(sql, new BeanProcessor(PsndocVO.class));
    	PsndocVO psnvo = obj==null?new PsndocVO():(PsndocVO)obj;  
    	getBillCardPanel().setHeadItem("pk_deptdoc",psnvo.getPk_deptdoc());		//申请部门
    	getBillCardPanel().setHeadItem("pk_psndoc",psnvo.getPk_psndoc());		//申请人
    	getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());//add by houcq 2011-04-01
        super.setDefaultData_withNObillno();
    }
   
 
    @Override
    public void afterEdit(BillEditEvent e) {
    	super.afterEdit(e);
    	String strKey = e.getKey();
    	if(strKey.equals("vinvcode")){
    		String pk_invbasdoc = getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc")==null?null:
				getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc").toString();
    		try {
	    		//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "0", _getCorp().getPk_corp());
	    		//UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());	//当前库存;
	    		//modify by houcq 2011-06-20修改取库存方法
    			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
	    		getBillCardPanel().setBodyValueAt(kcamount, e.getRow(), "kcamount");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    		
    	}
    }
}

   
    

