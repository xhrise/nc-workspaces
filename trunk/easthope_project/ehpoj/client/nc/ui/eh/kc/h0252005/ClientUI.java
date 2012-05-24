package nc.ui.eh.kc.h0252005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.PCRefModel;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;



/**
 * 功能说明：材料调拨单
 * @author 王明
 * 2008-05-07 下午04:03:18
 */ 
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
	
	UIRefPane refpc=null;
	private String pc = "";
	public ClientUI() {
	    super();
		refpc=(UIRefPane) getBillCardPanel().getBodyItem("dcpc").getComponent();
		refpc.setMultiSelectedEnabled(true);
		refpc.setProcessFocusLost(false);
		refpc.setButtonFireEvent(true);
		refpc.setTreeGridNodeMultiSelected(true);		
		
	 }

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		 getBillCardPanel().getHeadItem("vbillstatus").setValue(
	                Integer.toString(IBillStatus.FREE));
		 getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());	 
		 //日期
		 getBillCardPanel().setHeadItem("dbdate", _getDate());
		 getBillCardPanel().setHeadItem("dcdate",_getDate());
		 getBillCardPanel().setHeadItem("drdate",_getDate());
		 
		 pc = PubTools.getPC("eh_sc_dbd", _getDate());// 批次
		 //设置表体批次
		 BillModel mode = getBillCardPanel().getBillModel();
			for(int i = 0;i < mode.getRowCount();i ++){
			    mode.setValueAt(pc, i, "drpc");
			}
		super.setDefaultData();
	}
	@Override
	protected void initSelfData() {
		  getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
		//审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     //调拨类型 成品、原料
	     getBillCardWrapper().initHeadComboBox("dbtype",ICombobox.STR_DBTYPE, true);
	     getBillListWrapper().initHeadComboBox("dbtype",ICombobox.STR_DBTYPE, true);
	    
	     super.initSelfData();
	}
//	@Override
	@SuppressWarnings("unchecked")
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		int row=e.getRow();	
		
		 if(e.getPos()==HEAD){
	            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
	            getBillCardPanel().execHeadFormulas(formual);
	        }else if (e.getPos()==BODY){
	            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
	            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
	        }else{
	            getBillCardPanel().execTailEditFormulas();
	        }
		 
//		 if("vdrcode".equals(strKey)){			
//             getBillCardPanel().setBodyValueAt(pc, row, "drpc");
//             //add by houcq 2011-4-02
//             String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_dr_inv")==null?"":
//         		getBillCardPanel().getBodyValueAt(row, "pk_dr_inv").toString();
//     		PCRefModel.pk_invbasdoc=pk_invbasdoc;      		
//     		HashMap drinvprcehm = getInvPriceByDate(_getCorp().getPk_corp(), _getDate());
//     		Object drob =drinvprcehm.get(pk_invbasdoc);
//     		getBillCardPanel().setBodyValueAt(drob, row, "drprice");//add by houcq 2011-03-29
//		 }
		if("vdccode".equals(strKey)){
    		String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_dc_inv")==null?"":
        		getBillCardPanel().getBodyValueAt(row, "pk_dc_inv").toString();
    		PCRefModel.pk_invbasdoc=pk_invbasdoc;
    		//add by houcq 2011-06-24
    		String pk_dc_store = getBillCardPanel().getHeadItem("pk_dc_store").getValueObject()==null?"":
				getBillCardPanel().getHeadItem("pk_dc_store").getValueObject().toString();	
    		HashMap hm = new PubTools().getInvKcAmountByStore( _getCorp().getPk_corp(),_getDate(), pk_dc_store);
    		getBillCardPanel().setBodyValueAt(hm.get(pk_invbasdoc), row, "pcamount");
    		//end
    		HashMap currenthm = getInvPrice(_getCorp().getPk_corp(), _getDate());    		
    		Object dcob =currenthm.get(pk_invbasdoc);
    		if (dcob==null)
    		{
    			HashMap dcinvprcehm = getInvPriceByDate(_getCorp().getPk_corp(), _getDate());
    			dcob=dcinvprcehm.get(pk_invbasdoc);
    		}    		
    		getBillCardPanel().setBodyValueAt(dcob, row, "dcprice");//add by houcq 2011-03-29
    		getBillCardPanel().setBodyValueAt(dcob, row, "drprice");//add by houcq 2011-03-29
    		
        }
		if("pk_dc_store".equals(strKey)){    		
    		
    		int rows=getBillCardPanel().getBillModel().getRowCount();
    		if (rows>0)
    		{
        		String pk_dc_store = getBillCardPanel().getHeadItem("pk_dc_store").getValueObject()==null?"":
    				getBillCardPanel().getHeadItem("pk_dc_store").getValueObject().toString();	
        		HashMap hm = new PubTools().getInvKcAmountByStore( _getCorp().getPk_corp(),_getDate(), pk_dc_store);
        		for (int i=0;i<rows;i++)
        		{
        			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i, "pk_dc_inv")==null?"":
                		getBillCardPanel().getBodyValueAt(i, "pk_dc_inv").toString();
        			getBillCardPanel().setBodyValueAt(hm.get(pk_invbasdoc), i, "pcamount");  
        		}
    		}
    		
        }
		if("dcpc".equals(strKey)){
        	String pk_cprk_b=refpc.getRefPK();
        	String[] pc=refpc.getRefCodes();
        	String sql= "select sum(isnull(rkmount,0))-sum(isnull(ckamount,0)) wckamount from eh_sc_cprkd_b where  pk_rkd_b in ("+pk_cprk_b+")";
			UFDouble wckamount = new UFDouble(0);
			IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				Object wcamountstr = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				wckamount = wcamountstr==null?new UFDouble(0):new UFDouble(wcamountstr.toString());			//所选批次未出库数量
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	String  c ="";
        	if (pc != null)
      		  c = PubTools.combinArrayToString3(pc);
        	getBillCardPanel().setBodyValueAt(pk_cprk_b, row, "pk_cprk_b");
        	getBillCardPanel().setBodyValueAt(c, row, "dcpc");
        	getBillCardPanel().setBodyValueAt(wckamount, row, "pcamount");
        }
		
		/**选择仓库后，判断是否是末级仓库(花召滨要求) add by zqy 2010年11月23日10:38:19**/
        if(e.getKey().equals("pk_intype") && e.getPos()==HEAD){
        	String pk_intype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_intype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级入库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_intype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        
        if(strKey.equals("pk_outtype") && e.getPos()==HEAD){
        	String pk_outtype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_outtype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级出库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_outtype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        	
        }
		
		
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillEditEvent e) {
		String strKey=e.getKey();
		//add by houcq 2011-06-24 begin
    	if(strKey.equals("vdccode")){
    		String pk_dc_store = getBillCardPanel().getHeadItem("pk_dc_store").getValueObject()==null?null:
					getBillCardPanel().getHeadItem("pk_dc_store").getValueObject().toString();								//仓库
    		if(pk_dc_store==null){
    			showErrorMessage("请先选择调出仓库!");
    		}
    	}
    	//end 
		@SuppressWarnings("unused")
		int row=e.getRow();	
		if("dcpc".equals(strKey)){
			refpc.getRef().getRefModel().reloadData();//刷新参照模板的数据
		}
		
		return super.beforeEdit(e);
	}
	/*
	 * 获得上月物料价格
	 */
	@SuppressWarnings("unchecked")
	private HashMap getInvPriceByDate(String pk_corp,UFDate date)
	{		
		int year =date.getYear();
		int month = date.getMonth();
		if (month==1)
		{
			month=12;
			year=year-1;
		}
		else
		{
			month=month-1;
		}
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap hs = new HashMap();
		StringBuilder sb = new StringBuilder()
		.append("select b.pk_costobj_b pk_invbasdoc,round(sumfy/def_6,2) unitprice from eh_arap_cosths a,eh_arap_cosths_b b ")
		.append(" where a.pk_cosths = b.pk_cosths ")	
		.append(" and nvl(a.dr,0)=0")
		.append(" and a.pk_corp='")
		.append(pk_corp)
		.append("' and nvl(b.dr,0)=0")
		.append(" and a.nyear=")
		.append(year)
		.append(" and a.nmonth=")
		.append(month);
		ArrayList arr;
		try {
			arr = (ArrayList)iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
			for (int i=0;i<arr.size();i++)
			{
				HashMap hm = (HashMap) arr.get(i);
				hs.put(hm.get("pk_invbasdoc"), hm.get("unitprice"));
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hs;
	}
	/*
	 * 获得当月物料价格
	 */
	@SuppressWarnings("unchecked")
	private HashMap getInvPrice(String pk_corp,UFDate date)
	{		
		int year =date.getYear();
		int month = date.getMonth();		
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap hs = new HashMap();
		StringBuilder sb = new StringBuilder()
		.append("select b.pk_costobj_b pk_invbasdoc,round(sumfy/def_6,2) unitprice from eh_arap_cosths a,eh_arap_cosths_b b ")
		.append(" where a.pk_cosths = b.pk_cosths ")	
		.append(" and nvl(a.dr,0)=0")
		.append(" and a.pk_corp='")
		.append(pk_corp)
		.append("' and nvl(b.dr,0)=0")
		.append(" and a.nyear=")
		.append(year)
		.append(" and a.nmonth=")
		.append(month);
		ArrayList arr;
		try {
			arr = (ArrayList)iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
			for (int i=0;i<arr.size();i++)
			{
				HashMap hm = (HashMap) arr.get(i);
				hs.put(hm.get("pk_invbasdoc"), hm.get("unitprice"));
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hs;
	}

}
