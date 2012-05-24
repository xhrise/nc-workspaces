/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.stock.h0150325;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.pub.BusinessException;

/**
 * 功能:五金采购入库
 * ZB24
 * @author WB
 * 2009-1-8 13:48:06
 *
 */
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
    	getBillCardPanel().setHeadItem("indate",_getDate());
		String pc = PubTools.getPC("eh_stock_in", _getDate());// 批次
		getBillCardPanel().setHeadItem("instalment",pc);     // 批次
//    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//    	String sql = "select * from bd_psndoc where pk_psnbasdoc = (select pk_psndoc FROM sm_userandclerk su WHERE su.userid = '"+_getOperator()+"')";
//    	PsndocVO psnvo = new PsndocVO();  
//    	psnvo = (PsndocVO)iUAPQueryBS.executeQuery(sql, new BeanProcessor(PsndocVO.class));
//    	getBillCardPanel().setHeadItem("pk_deptdoc",psnvo.getPk_deptdoc());		//申请部门
//    	getBillCardPanel().setHeadItem("pk_psndoc",psnvo.getPk_psndoc());		//申请人
		getBillCardPanel().setHeadItem("pk_intype", "0001A8100000000002H6");
        super.setDefaultData_withNObillno();
    }
   
 
    @Override
    public void afterEdit(BillEditEvent e) {
    	super.afterEdit(e);
    	String strKey = e.getKey();
    	if(strKey.equals("pk_cubasdoc")){
    		String pk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
    										getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
    		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    		try {
    			StringBuffer str = new StringBuffer()
    			.append("SELECT * FROM bd_cubasdoc cubas WHERE cubas.pk_cubasdoc IN(SELECT cuman.pk_cubasdoc FROM bd_cumandoc cuman WHERE cuman.pk_cumandoc = '"+pk_cubasdoc+"')");    			
				CubasdocVO cuvo = (CubasdocVO)iUAPQueryBS.executeQuery(str.toString(), new BeanProcessor(CubasdocVO.class));
				boolean sf_flag = cuvo.getFreecustflag()==null?false:cuvo.getFreecustflag().booleanValue();   //散户标记
				getBillCardPanel().getHeadItem("psninfo").setEnabled(sf_flag);
    		} catch (BusinessException e1) {
				e1.printStackTrace();
			}
    		
    		
    	}
    	/**对入库类型是否是末级进行控制 add by zqy 2010年11月23日11:02:06**/
    	if(strKey.equals("pk_intype") && e.getPos()==HEAD){
    		String pk_intype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject()==null?"":
    			getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject().toString();
    		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    		String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_intype+"' and nvl(dr,0)=0 ";
    		try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql,new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级入库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_intype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
    	}
    	
    }
    
    
    
}

   
    

