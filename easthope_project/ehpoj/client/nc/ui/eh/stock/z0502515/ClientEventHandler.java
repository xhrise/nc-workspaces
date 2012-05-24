package nc.ui.eh.stock.z0502515;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z0502515.StockBackBVO;
import nc.vo.eh.stock.z0502515.StockBackVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：原料退货通知单
 * @author 王兵
 * 2008-7-24 10:35:03
 */

public class ClientEventHandler extends AbstractEventHandler {
	
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
    public void onBoSave() throws Exception {
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		// 对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();

          //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("退货物料已经存在，不允许操作！");
            return;
        }  
        AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
        StockBackVO svo = (StockBackVO) aggvo.getParentVO();
        StockBackBVO[] sbvo = (StockBackBVO[]) aggvo.getChildrenVO();
        
        String pk_back=svo.getPk_back()==null?"":svo.getPk_back().toString();
        UFDouble amount3=new UFDouble(sbvo[0].getWeight()==null?"0":sbvo[0].getWeight().toString());//界面重量
        String pk_in=svo.getPk_in()==null?"":svo.getPk_in().toString();
        String pk_invbasdoc=sbvo[0].getPk_invbasdoc()==null?"":sbvo[0].getPk_invbasdoc().toString();
        ArrayList al3=new ArrayList();
        ArrayList al1=new ArrayList();
        UFDouble amount4=new UFDouble();//退货总量
        UFDouble amount1=new UFDouble();//入库的数量
        UFDouble sub=new UFDouble(-1);
        String sql1="select inamount from eh_stock_in_b where pk_in='"+pk_in+"' and pk_invbasdoc='"+pk_invbasdoc+"' ";
		String sql3="select sum(weight) amount  from eh_stock_back a, eh_stock_back_b b where a.pk_back=b.pk_back and " +
		" NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_in='"+pk_in+"' and b.pk_invbasdoc='"+pk_invbasdoc+"' group by a.pk_in";
		try {
			al3=(ArrayList) iUAPQueryBS.executeQuery(sql3, new MapListProcessor());
			al1=(ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(al3 != null && al3.size()>0){
			HashMap hm=(HashMap) al3.get(0);
			amount4=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		}
		if(al1 != null && al1.size()>0){
			HashMap hm=(HashMap) al1.get(0);
			amount1=new UFDouble(hm.get("inamount")==null?"0":hm.get("inamount").toString());
		}
        if(!pk_back.equals("")){ //修改状态
        	sub=amount1.sub(amount1);
        }else{//第一次保存状态
        	sub=amount1.sub((amount4.add(amount3)));
        }
        if(sub.toDouble()==0){
        	 PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	 String sql="update eh_stock_in set th_flag='Y' where pk_in ='"+pk_in+"'";
        	 pubitf.updateSQL(sql);
        }
        
        //如果是自制单据不需要做大于入库CHECK
        if(svo.getVsourcebilltype() != null && svo.getVsourcebilltype().equals(IBillType.eh_z0502005)){
        	if(sub.toDouble()<0){
            	getBillUI().showErrorMessage("你的退货数量大于入库数量，请检查！");
            	return;
            }
        }
    
        String vsourcebilltype  = svo.getVsourcebilltype();
        if(vsourcebilltype != null && vsourcebilltype.equals(IBillType.eh_z0502005)){
            String pk_checkreport = svo.getVsourcebillid()==null?"":svo.getVsourcebillid().toString();
            if(pk_checkreport!=null || pk_checkreport.equals("")){
                String SQL = " update eh_stock_checkreport set def_4 ='N' where pk_checkreport='"+pk_checkreport+"' and NVL(dr,0)=0 ";
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                pubitf.updateSQL(SQL);
            }           
        }
        StockBackBVO[] bvo=(StockBackBVO[]) aggvo.getChildrenVO();
        String sb=null;
        for(int i=0;i<bvo.length;i++){
        	sb=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "issb")==null?"":
        		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "issb").toString();
        }
        if(sb!=null && sb.equals("true")){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("isrk", "N");
        }else{
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("isrk", "Y");
        }
        
        
		super.onBoSave();
	}


    protected void onBoDelete() throws Exception {
        AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
        StockBackVO svo = (StockBackVO) aggvo.getParentVO();
        String vsourcebilltype  = svo.getVsourcebilltype();
        if(vsourcebilltype.equals(IBillType.eh_z0502005)){
            String pk_checkreport = svo.getVsourcebillid()==null?"":svo.getVsourcebillid().toString();
            if(pk_checkreport!=null || pk_checkreport.equals("")){
            	String pk_in=svo.getPk_in()==null?"":svo.getPk_in().toString();
                String SQL = " update eh_stock_checkreport set def_4 ='Y' where pk_checkreport='"+pk_checkreport+"' and NVL(dr,0)=0 ";
                String SQL2 = "update eh_stock_in set th_flag='N' where pk_in ='"+pk_in+"'";
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                pubitf.updateSQL(SQL);
                pubitf.updateSQL(SQL2);
            }           
        }
        super.onBoDelete();
    }
    

     public void onButton_N(ButtonObject bo, BillModel model) {
            super.onButton_N(bo, model);
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            String bocode = bo.getCode();
            if (bocode.equals("检测报告")) {
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
                int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
                for (int i = 0; i < row; i++) {
                    getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vinvcode", false);
                }
                  getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
                  getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
                  try {
					StockBackVO vo= (StockBackVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
					String pk_cubasdoc=vo.getPk_cubasdoc()==null?"":vo.getPk_cubasdoc().toString();
					String sql="select freecustflag from bd_cubasdoc where pk_cubasdoc ='"+pk_cubasdoc+"'";
					ArrayList al= (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
					String freecustflag="";
					for(int i=0;i<al.size();i++){
						HashMap hm=(HashMap) al.get(i);
						freecustflag=hm.get("freecustflag")==null?"":hm.get("freecustflag").toString();
					}
					if(freecustflag.equals("Y")){
						getBillCardPanelWrapper().getBillCardPanel().getHeadItem("retailinfo").setEnabled(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				String pk_in=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_in").getValueObject()==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_in").getValueObject().toString();//入库单
				String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "pk_invbasdoc")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "pk_invbasdoc").toString();//物料
				UFDouble inamount=new UFDouble(0);
				UFDouble inprice=new UFDouble(0);
				String sql1="select inamount,inprice from eh_stock_in_b where pk_in='"+pk_in+"' and pk_invbasdoc='"+pk_invbasdoc+"' ";
				try {
					ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
					for(int i=0;i<al.size();i++){
						HashMap hm=(HashMap) al.get(i);
						inamount=new UFDouble(hm.get("inamount")==null?"0":hm.get("inamount").toString());
						inprice=new UFDouble(hm.get("inprice")==null?"0":hm.get("inprice").toString());
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				
				String pk_cgpsn=null;
				String pk_deptodc=null;
				String sql2="select pk_cgpsn,pk_deptodc from eh_stock_in where pk_in='"+pk_in+"' ";
				try {
					ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
					for(int i=0;i<al.size();i++){
						HashMap hm=(HashMap) al.get(i);
						pk_cgpsn=hm.get("pk_cgpsn")==null?"":hm.get("pk_cgpsn").toString();
						pk_deptodc=hm.get("pk_deptodc")==null?"":hm.get("pk_deptodc").toString();
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if(pk_cgpsn==null){
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(true);
				}else{
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
				}
				
				ArrayList al3=new ArrayList();
				UFDouble amount3=new UFDouble(0);
				String sql3="select sum(weight) amount  from eh_stock_back a, eh_stock_back_b b where a.pk_back=b.pk_back and " +
				" NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_in='"+pk_in+"' and b.pk_invbasdoc='"+pk_invbasdoc+"' group by a.pk_in";
				try {
					al3=(ArrayList) iUAPQueryBS.executeQuery(sql3, new MapListProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if(al3 != null && al3.size()>0){
					HashMap hm=(HashMap) al3.get(0);
					amount3=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
					inamount=inamount.sub(amount3);
				}
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_psndoc", pk_cgpsn);
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptodc);
				
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(inamount, 0, "weight");
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(inamount, 0, "amount");
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(inprice, 0, "taxinprice");
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(inamount.multiply(inprice), 0, "taxmoney");
            }
            getBillUI().updateUI();
     }
     
     protected void onBoElse(int intBtn) throws Exception {
         switch (intBtn)
         {
             case IEHButton.LOCKBILL:    //关闭单据
                 onBoLockBill();
                 break;
             case IEHButton.Prev:    //上一页 下一页
                 onBoBrows(intBtn);
                 break;
             case IEHButton.Next:    //上一页 下一页
                 onBoBrows(intBtn);
                 break;
         }   
     }
     
     private void onBoBrows(int intBtn) throws java.lang.Exception {
         // 动作执行前处理
         buttonActionBefore(getBillUI(), intBtn);
         switch (intBtn) {
         case IEHButton.Prev: {
             getBufferData().prev();
             break;
         }
         case IEHButton.Next: {
             getBufferData().next();
             break;
         }
         }
         // 动作执行后处理
         buttonActionAfter(getBillUI(), intBtn);
         getBillUI().showHintMessage(
                 nc.ui.ml.NCLangRes.getInstance()
                         .getStrByID(
                                 "uifactory",
                                 "UPPuifactory-000503",
                                 null,
                                 new String[] { nc.vo.format.Format
                                         .indexFormat(getBufferData()
                                                 .getCurrentRow()+1) })/*
                                                                      * @res
                                                                      * "转换第:" +
                                                                      * getBufferData().getCurrentRow() +
                                                                      * "页完成)"
                                                                      */
                         );
           setBoEnabled();
     }
      
      protected void onBoLockBill() throws Exception{
//        SuperVO parentvo = (SuperVO)getBillUI().getChangedVOFromUI().getParentVO();
//        String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
//             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        StockBackVO ivo = (StockBackVO) aggvo.getParentVO();
        String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
        String primaryKey = ivo.getPrimaryKey();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该单据已经关闭!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("已经关闭成功");
                onBoRefresh();
            } 
            else{
                return;
            }
        }
        setBoEnabled();
        
    }
      
    
}
