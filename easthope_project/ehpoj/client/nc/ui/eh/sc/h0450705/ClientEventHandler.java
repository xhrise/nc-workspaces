package nc.ui.eh.sc.h0450705;

/**
 * MRP运算
 * ZB32
 * @author wangbing
 * 2008-12-20 16:18:55
 */

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.eh.sc.h0450705.ScMrpVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageinfoVO;

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		ScMrpVO hvo = (ScMrpVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		//是否超过产能判断
		ScMrpBVO[] bvos = (ScMrpBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getBodyValueVOs(getUIController().getBillVoName()[2]);
		//add by houcq 2011-11-11 begin
		for (int i=0;i<bvos.length;i++)
		{
			if(bvos[i].getBcamount().toDouble()==0)
			{
				getBillUI().showErrorMessage("本次生产量不能为零!");
				return;
			}
		}
		//add by hucq 2011-11-11 end
		UFDouble maxamount = isMaxCN(hvo,bvos);
		if(maxamount.toDouble()>0){
			getBillUI().showErrorMessage("当日安排生产量大于日产能,超过"+maxamount+"吨,不能保存! 请对本次生产量进行调整!");
			return;
		}
		//原料是否齐套
		ScMrpCVO[] cvos = (ScMrpCVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_c").getBodyValueVOs(getUIController().getBillVoName()[3]);
		String strMessage = isAllamountYL(cvos);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenough_flag").setValue("N");
		if(strMessage!=null&&strMessage.length()>0){
			if (getBillUI().showYesNoMessage("生产当前产品所需原料库存不足,请查看<原料明细>页签.\r\n是否继续保存?") != UIDialog.ID_YES) {
	            return;
	        }
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenough_flag").setValue("Y");
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenoughmsg").setValue(strMessage);
		}
     	super.onBoSave_withBillno();
     	String pk_mrp = hvo.getPk_mrp();
     	StringBuilder sb = new StringBuilder()
     	.append(" select sum(sla)-sum(slb) xl from (")
     	.append(" select count(*) slA,0  slB  from eh_sc_mrp_b where pk_mrp='"+pk_mrp+"' and sc_flag='Y'")
     	.append(" union all")
     	.append(" select 0  slA,count(*) slB  from eh_sc_mrp_b where pk_mrp='"+pk_mrp+"' and nvl(sc_flag,'N')='N')")
     	.append(" having sum(sla)-sum(slb)=0");
     	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object obj = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor());
		if (obj!=null)
		{
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String usql="update eh_sc_mrp set sc_flag='Y' where pk_mrp='"+pk_mrp+"'";
			pubitf.updateSQL(usql);
		}
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void onBoCommit() throws Exception {
		super.onBoCommit();
		ScMrpVO hvo = (ScMrpVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		boolean notenough_flag = hvo.getNotenough_flag()==null?false:hvo.getNotenough_flag().booleanValue();
		if(notenough_flag){			//在提交时将原料不足明细信息发送给 生产经理、品管经理、总经理
			String pk_corp = _getCorp().getPk_corp();
			String strMessage = hvo.getNotenoughmsg();
			StringBuffer sql = new StringBuffer()
			.append("  SELECT a.cuserid")
			.append("  FROM sm_user_role a,sm_role b")
			.append("  WHERE a.pk_role = b.pk_role")
			.append("  AND b.role_name IN ('生产经理','品管经理','总经理')")
			.append("  AND a.pk_corp = '"+pk_corp+"'")
			.append("  AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0");
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				ClientEnvironment ce = ClientEnvironment.getInstance();
				MessageinfoVO[] infos = new MessageinfoVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					MessageinfoVO info = new MessageinfoVO();
					HashMap hm = (HashMap)arr.get(i);
					String cuserid = hm.get("cuserid")==null?null:hm.get("cuserid").toString();
					info.setSenddate(ce.getServerTime());
					info.setSenderman(_getOperator());
					info.setCheckman(cuserid);
					info.setBillno(hvo.getBillno());
					info.setTitle("MRP运算中单号:"+hvo.getBillno()+"中原料不足提醒!");
					info.setContent(strMessage);
					info.setPk_corp(pk_corp);
					info.setPriority(1);
					info.setType(1);
					info.setDr(0);
					info.setState(0);
					
					infos[i] = info;
				}
				IVOPersistence iVO =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				iVO.insertVOArray(infos);
			}
		}
	}
	/***
	 * 是否超过产能判断
	 * wb 2008-12-22 10:16:13
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public UFDouble isMaxCN(ScMrpVO hvo,ScMrpBVO[] bvos) throws Exception{
		UFDouble maxammount = new UFDouble(0);
		//今日已安排生产量
		StringBuffer sql =  new StringBuffer()
		.append(" SELECT SUM(ISNULL(b.bcamount,0)) amount")
		.append(" FROM eh_sc_mrp a,eh_sc_mrp_b b")
		.append(" WHERE a.pk_mrp = b.pk_mrp")
		.append(" AND a.calcdate = '"+_getDate()+"' and a.pk_mrp <> '"+hvo.getPk_mrp()+"'")
		//.append(" --AND a.vbillstatus = 1")
		.append(" AND a.pk_corp = '"+_getCorp().getPk_corp()+"'")
		.append(" AND ISNULL(a.dr,0)=0")
		.append(" AND ISNULL(b.dr,0)=0");
		//日产能
		String cnsql = "SELECT maxamount FROM eh_sc_cn WHERE ISNULL(dr,0)=0 AND pk_corp = '"+_getCorp().getPk_corp()+"'";
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object objamount = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		Object objMaxamount = iUAPQueryBS.executeQuery(cnsql, new ColumnProcessor());
		UFDouble amountIndata = new UFDouble(0);				//今日已安排生产量
		UFDouble currentAmount = new UFDouble(0);				//当前单据生产数量
		UFDouble cnamount = new UFDouble(0);					//日最大产能
		amountIndata = new UFDouble(objamount==null?"0":objamount.toString());
		cnamount = new UFDouble(objMaxamount==null?"0":objMaxamount.toString());
		if(bvos!=null&&bvos.length>0){
			for(int i=0;i<bvos.length;i++){
				ScMrpBVO bvo = bvos[i];
				currentAmount = currentAmount.add(bvo.getBcamount());
			}
		}
		maxammount = amountIndata.add(currentAmount).sub(cnamount);  //已安排生产量+当前单据生产量-日最大产能
		return new UFDouble(maxammount.toString(),2);
	}
    
	/***
	 * 原料齐套判定
	 * @param cvos
	 * @return
	 */
	public String isAllamountYL(ScMrpCVO[] cvos){
		StringBuffer str = new StringBuffer();
		if(cvos!=null&&cvos.length>0){
		 try {
			for(int i=0;i<cvos.length;i++){
				ScMrpCVO cvo = cvos[i];
				UFDouble cy = cvo.getCy();				//差异 库存-需求
				if(cy.toDouble()<0){
					str.append(" 第"+(i+1)+"行差距："+cy+"\r\n");
					break;
				}
			}
		  } catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str.toString();
	}
	
//    @Override
//    protected void onBoPrint() throws Exception {
//        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
//                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
//        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
//                dataSource);
//        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
//                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
//                .getBusinessType(), getBillUI().getNodeKey());
//        print.selectTemplate();
//        print.preview();
//    }
   
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
//    	//在修改时对不需安排生产的数据行 本次生产数量 字段不可以编辑
//    	ScMrpBVO[] bvos = (ScMrpBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getBodyValueVOs(getUIController().getBillVoName()[2]);
//    	if(bvos!=null&&bvos.length>0){
//			for(int i=0;i<bvos.length;i++){
//				ScMrpBVO bvo = bvos[i];
//				boolean sc_flag = bvo.getSc_flag()==null?false:bvo.getSc_flag().booleanValue();
//				if(sc_flag){
//					getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i, "bcamount", false);
//				}
//			}
//    	}
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void onButton_N(ButtonObject bo, BillModel model) {
    	super.onButton_N(bo, model);
    	 String bocode=bo.getCode();
         if(bocode.equals("销售订单")){
           int row=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getRowCount();
           HashMap hm = new PubTools().getInvSafeKC(null);		//安全库存量 
           for(int i=0;i<row;i++){
               getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i,"vinvcode", false);
               String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
               							getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
               UFDouble safekc =  new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
               UFDouble scamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcamount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcamount").toString());		//本次生产数量 = 订单数量-出库数量
               UFDouble truekc = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount").toString());			//实际库存
               UFDouble scrwamount = scamount.sub(truekc).add(safekc);									//生产任务数量=本次生产数量+安全库存量-实际库存量
               //如果生产任务数量<零，生产任务数量取零不安排生产并可修改；如果生产任务数量〉零，生产任务数量取正数安排生产并可修改
               if(scrwamount.toDouble()<0){
            	   	scamount = new UFDouble(0);
               		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("不需要安排生产", i, "memo");
//               		getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt("不需要安排生产", i, "memo");
               		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "sc_flag");
//               		getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i, "bcamount", false);
               }else{
            	   scamount = scrwamount;
               }
               getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(safekc, i, "safekc");
               getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scamount, i, "bcamount");
           }
//           getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//           getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
//           getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
//           getBillUI().updateUI();
         }
    }
    
    
    
    @Override
    protected void onBoLineDel() throws Exception {
    	//在原料需求页签时不允许删行
    	String currentModel = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();
    	if(currentModel.equals("eh_sc_mrp_c")){
    		getBillUI().showErrorMessage("原料明细不允许删行操作!");
    		return;
    	}
    	super.onBoLineDel();
    }
}
