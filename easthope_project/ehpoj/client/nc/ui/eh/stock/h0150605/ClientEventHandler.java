package nc.ui.eh.stock.h0150605;


import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.stock.h0150605.ArapStockinvoicesBVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：采购发票
 * @author 王明
 * 2008-05-29 下午04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	
	public static int flag = 0;
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.SUREMONEY: 
			sureMoney();
			break;
		}
		super.onBoElse(intBtn);
	}

	/*
	 * (non-Javadoc)
	 * @see nc.ui.eh.uibase.AbstractEventHandler#onBoSave()
	 * modify by houcq 2011-05-03
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
   		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		ArapStockinvoicesBVO[] bvos=(ArapStockinvoicesBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		UFDouble sumtaxinmony=new UFDouble(0);
		String tip="";
		for(int i=0;i<bvos.length;i++){
			UFDouble taxinmony=new UFDouble(bvos[i].getTaxinmony()==null?"0":bvos[i].getTaxinmony().toString());
			if (taxinmony.doubleValue()==0)
			{
				tip=tip+"第("+(i+1)+")行含税金额不能为零!\r\n ";
			}
			sumtaxinmony=taxinmony.add(sumtaxinmony);
		}
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("taxinmony",sumtaxinmony);	
		ArapStockinvoicesBVO[] reBVO=(ArapStockinvoicesBVO[]) getBillUI().getVOFromUI().getChildrenVO();		
		StringBuilder pk_in_bs = new StringBuilder("('");	
		StringBuilder pk_ins = new StringBuilder("('");	
		for(int i=0;i<reBVO.length;i++){
			pk_ins.append(reBVO[i].getVsourcebillid().toString()).append("','"); 
			pk_in_bs.append(reBVO[i].getVsourcebillrowid().toString()).append("','"); 
			UFDouble rkmount=reBVO[i].getRkmount();;
			UFDouble kpmount=reBVO[i].getKpmount();
			UFDouble fpmount=reBVO[i].getFpmount();
			//add by houcq 2011-11-08 begin
			if (rkmount.toDouble()>0)
            {            
            	if(fpmount.toDouble()<=0 ||fpmount.toDouble()>rkmount.sub(kpmount).toDouble() )
            	{   
            		getBillUI().showErrorMessage("第("+(i+1)+")行发票数量有误,请检查!");
            		return;
            	}
            }else if (rkmount.toDouble()<0)
	            {
	            	if(fpmount.toDouble()>=0 ||fpmount.toDouble()<rkmount.sub(kpmount).toDouble() )
	            	{   
	            		getBillUI().showErrorMessage("第("+(i+1)+")行发票数量有误,请检查!");
	            		return;
	            	}
	            }else
	            {
	            	getBillUI().showErrorMessage("第("+(i+1)+")行发票数量有误,请检查!");
	   			 	return;
	            }
			//add by houcq 2011-11-08 end
			String vsourcebillrowid = reBVO[i].getVsourcebillrowid().toString();
			String SQL = " select vsourcebillrowid pk_in_b  from eh_stock_in_b where vsourcebillrowid ='"+ vsourcebillrowid+"' and cgfp_flag='Y' ";
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(SQL, new MapListProcessor()); 
			if(arr!=null && arr.size()>0)
			{
				getBillUI().showErrorMessage("第("+(i+1)+")行已经开过发票了!");
   			 	return;
			}
			//end
		}
		pk_in_bs.append("')");
		pk_ins.append("')");		
		int ret= getBillUI().getBillOperate();
		if (ret!=0)
		{
			String insql ="select * from eh_stock_in where pk_in in "+pk_ins.toString()+" and cgfp_flag='Y'";
			ArrayList newarr = (ArrayList)iUAPQueryBS.executeQuery(insql, new MapListProcessor()); 
			if(newarr.size()>0)
			{
				getBillUI().showErrorMessage("该入库单已开过发票了,不允许重复开发票!");
				return;	
			}
			super.onBoSave();
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String updateFlagSql = "update eh_stock_in_b set cgfp_flag='Y' where pk_in_b in"+pk_in_bs.toString();
			pubitf.updateSQL(updateFlagSql);
			/*
			 * 	根据表体来源单据行pk，判断同一个入库单是否已经开完发票，如果都开完，主表打上标记
			 */
			StringBuilder sb =new StringBuilder()
			.append(" update eh_stock_in set cgfp_flag='Y' where pk_in in (")
			.append(" select pk_in from (select pk_in, sum(flagY) flagY, sum(flagN) flagN")
			.append(" from (select pk_in, count(*) flagY, 0 flagN from eh_stock_in_b where pk_in in "+pk_ins.toString())
			.append(" and nvl(cgfp_flag, 'N') = 'Y'   and nvl(dr, 0) = 0  group by pk_in ")
			.append(" union all")
			.append(" select pk_in, 0 flagY, count(*) flagN from eh_stock_in_b")
			.append(" where pk_in  in "+pk_ins.toString())
			.append(" and nvl(dr, 0) = 0 group by pk_in) group by pk_in)")
			.append("  where flagY - flagN = 0)");		
			pubitf.updateSQL(sb.toString());
		}
		else
		{			
			super.onBoSave();
		}
			
		
		
	}
	   @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
		String bocode=bo.getCode();
		super.onButton_N(bo, model);
		if(bocode.equals("入库单")){
			flag = 2;
			 getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
	         //getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
	         getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
	         getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
	         getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);  
	         getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_currency", "00010000000000000001");
	         int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
	         UFDouble rate =  new PubTools().getXSCgRate(1);
	         for(int i=0;i<row;i++){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvcode", false);
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(rate, i, "taxrate");	
                String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("fpmount").getEditFormulas();//获取编辑公式
                getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
			}      
	         String pk_cubasdoc = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject() == null ? "":
	        		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();

	         if(pk_cubasdoc != ""){
	         	String[] formula = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getEditFormulas();
	         	this.getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formula);
	         	this.getBillUI().updateUI();
	         }
		}
		if(bocode.equals("自制单据")){
			  flag = 1;
		}
	}
	   
	   
	@SuppressWarnings("unchecked")
	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
		//删除是修改回写标志
    	ArrayList alPk_in=new ArrayList();
    	ArrayList alPk_in_b=new ArrayList();
    	ArapStockinvoicesBVO[] aggbvo=(ArapStockinvoicesBVO[]) (getBillCardPanelWrapper().getBillVOFromUI()).getChildrenVO();
    	for(int i=0;i<aggbvo.length;i++){
    		alPk_in.add(aggbvo[i].getVsourcebillid()==null?"":aggbvo[i].getVsourcebillid().toString());
    		alPk_in_b.add(aggbvo[i].getVsourcebillrowid()==null?"":aggbvo[i].getVsourcebillrowid().toString());
    	}
    	if(alPk_in!=null&&alPk_in.size()>0){
    		String [] pk_in=(String[]) alPk_in.toArray(new String[alPk_in.size()]);
    		String [] pk_in_b=(String[]) alPk_in_b.toArray(new String [alPk_in_b.size()]);
    		String pk_ins=PubTool.combinArrayToString(pk_in);
    		String pk_in_bs=PubTool.combinArrayToString(pk_in_b);
    		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
    		
    		ArrayList alSQL=new ArrayList();
    		alSQL.add("update eh_stock_in set cgfp_flag='N' where pk_in in "+pk_ins+" ");
    		alSQL.add("update eh_stock_in_b set cgfp_flag='N' where pk_in_b in "+pk_in_bs+" "); 
    		pubitf.UpdateSQLS(alSQL);
    	}
		super.onBoTrueDelete();
		}
	
	
	/**
	 * 功能：保存后，如果再进行单据编辑，进行行操作的时候也应该对选中的行的标记给予回写，实现的效果应该和删除单据的效果一致
	 * 时间：2010年5月21日15:51:16 add by zqy 
	 */
	protected void onBoLineDel() throws Exception {
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		//add by houcq 2010-11-15 begin
		int[] rowids = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		for (int i=0;i<rowids.length;i++)
		{
			String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i],"vsourcebillid")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i], "vsourcebillid").toString();
			String vsourcebillrowid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i],"vsourcebillrowid")==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowids[i], "vsourcebillrowid").toString();
			String sql = " update eh_stock_in set cgfp_flag='N' where pk_in = '"+vsourcebillid+"' ";
			String sql2 = " update eh_stock_in_b set cgfp_flag='N' where pk_in_b = '"+vsourcebillrowid+"' ";
			pubitf.updateSQL(sql);
			pubitf.updateSQL(sql2);
		}
		super.onBoLineDel();
	}
	
	private void sureMoney() throws Exception{
		int vbillstaturs=new Integer(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").getValueObject()
			==null?"88":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").getValueObject().toString()).intValue();
		if(vbillstaturs==IBillStatus.CHECKPASS){
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag","true");
			
			super.onBoSave();
//			setButtonE();    //确认状态功能不使用，删除此功能  add by zqy 2010年6月29日11:43:56
		}else{
			getBillUI().showErrorMessage("此单据没有审批,不能确认!");
			return;
		}
	}
	
	public void setButtonE(){
		String qr_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject().toString();
		String pk_stockinvoice = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_stockinvoice").getValueObject()==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_stockinvoice").getValueObject().toString();
		if(pk_stockinvoice.equals("")){
		}else{
			if(qr_flag.equals("true")){
				getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(false);
				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			}
			else{
				getButtonManager().getButton(IEHButton.SUREMONEY).setEnabled(true);
				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
				getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
			}
		}try {
			getBillUI().updateButtonUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onBoCard() throws Exception {
		super.onBoCard();
	}

	protected void onBoEdit() throws Exception{
		super.onBoEdit();
		getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
        getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
		
	}
}