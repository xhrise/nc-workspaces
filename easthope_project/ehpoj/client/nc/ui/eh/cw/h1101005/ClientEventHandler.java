package nc.ui.eh.cw.h1101005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.AccountByCusdocRefModel;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.eh.cw.h11060.SkhxVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：付款单
 * @author 王明
 * 2008-05-28 下午04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	
    public static boolean ishx = false; 
    
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

    @SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
         //对非空验证 
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        UFDouble fkje = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fkje").getValueObject()==null?"0":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fkje").getValueObject().toString());
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(row==0){
        	//modify by houcq 2011-02-09
        	int iRet = getBillUI().showYesNoCancelMessage("        请确认款项类型，运费及预付款请选择是，支付帐余款请选择否,返回请选取消!");
    		System.out.println("TEST"+iRet);
        	if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
    			this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("df_flag", "Y");//功能：如果表体数据为空自动将运费及预付款标记上。时间：2010-01-29作者：张志远
    		}else if (iRet == 8)
    		{
    			this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("df_flag", "N");
    		}else
    		{
    			return;
    		}
        	
        }
        UFDouble sumwfkje = new UFDouble(0);			//未付款总金额
        UFDouble sumbcfkje = new UFDouble(0);			//本次付款总金额表体
        for(int i=0;i<row;i++){
        	UFDouble yfkje = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "yfkje")==null?"0":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "yfkje").toString());//已经付款金额
        	UFDouble price = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "price")==null?"0":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "price").toString());//入库金额
        	UFDouble bcfkje_b = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcfkje")==null?"0":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcfkje").toString());//本次付款金额
        	//add by houcq 2011-02-09 begin
        	if (bcfkje_b.equals(new UFDouble(0)))
        	{
        		getBillUI().showWarningMessage("表体中本次付款金额有空值,请删除后再保存!");
        		return;
        	}
        	//add by houcq 2011-02-09 end
        	sumwfkje = sumwfkje.add(price.sub(yfkje));
        	sumbcfkje = sumbcfkje.add(bcfkje_b);
        	int rowstatus  = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowState(i);
        	if(isAdding())
        		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
        	else{
        		if (rowstatus ==BillModel.ADD)
        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
        		else{
        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        		}
        	}
        }
        if(fkje.compareTo(sumwfkje)>0 && row>0){
            getBillUI().showErrorMessage("本次付款金额不能大于表体未付款金额总额!");
            return;
        }

       
        AggregatedValueObject aggVO = getBillCardPanelWrapper().getBillVOFromUI();
        //对付款方式与收款银行进行判断，当收款方式为非现金收款时，收款银行信息必填 add by zqy 2008-9-5 10:07:13
        ArapFkVO avo = (ArapFkVO) aggVO.getParentVO();
        String pk_sfkfs = avo.getPk_sfkfs()==null?"":avo.getPk_sfkfs().toString();//收付款方式
        String cubasbank = avo.getCubasbank()==null?"":avo.getCubasbank().toString();//收款银行
        if(pk_sfkfs!=null&&pk_sfkfs.length()>0){
	        String SQL = " select iscash from eh_arap_sfkfs where pk_sfkfs='"+pk_sfkfs+"' and isnull(dr,0)=0 ";
	        Vector vector = (Vector) iUAPQueryBS.executeQuery(SQL.toString(), new VectorProcessor());
	        Vector ve =(Vector) vector.get(0);
	        String iscash = ve.get(0)==null?"":ve.get(0).toString();
	        if(!iscash.equals("Y") && cubasbank.length()<1){
	            getBillUI().showErrorMessage("请到客商档案中维护该客商的银行账户与账号,谢谢!");
	            return;
	        }
        }
        if(ishx&&(sumbcfkje.compareTo(fkje)!=0)){
        	getBillUI().showErrorMessage("子表本次付款金额不等于付款金额!表体核销合计必须为:"+fkje.toDouble());
        	return;
        }
        
        //UFDouble money = sumwfkje.sub(sumbcfkje);//未付款金额-本次付款金额.时间2010-01-11
        //功能：根据往来单位PK查询客商余额。时间：2010-02-01作者：张志远
        //放入afterEdit中,edit  by 2010-3-31 10:52:55
//        String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"0":
//            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
//        PubTools pub = new PubTools();
//        UFDouble money = pub.getCustOverage(pk_cubasdoc, _getCorp().getPk_corp(), _getDate().toString());
//        this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("canusemoney", money);
        
//        if(isEditing()&&aggVO!=null&&aggVO.getChildrenVO()!=null){
//        	int rowstatus  = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowState(i);
//        	if(lb_add )
//        		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//        	else{
//        		if (rowstatus ==BillModel.ADD)
//        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//        		else
//        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
//        	}
//        }
        //把本次付款金额写到HXJE
        ArapFkVO vo=(ArapFkVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        UFDouble sumprice=new UFDouble(vo.getFkje()==null?"0":vo.getFkje().toString());
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("hxje", sumprice);
        super.onBoSave2_whitBillno();
        
        
//        // 关闭入库单  edit by wb 2009-11-17 15:17:23
//		if(row>0||ishx){
//	        String[] pk_in_bs = new String[row];
//			for (int i = 0; i < row; i++) {	
//	        	String pk_rkd_b =  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?null:
//                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();//入库单子表PK 
//	        	if(pk_rkd_b!=null){
//		        	pk_in_bs[i] = pk_rkd_b;
//		        	StringBuffer hxsql = new StringBuffer()
//			        .append(" select sum(isnull(def_6,0)) rkje,'A' flag from eh_stock_in_b where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0 ")  // 入库金额
//			        .append(" union all")
//			        .append(" select sum(isnull(bcfkje,0)) rkje,'B' flag from eh_arap_fk_b where vsourcebillrowid = '"+pk_rkd_b+"'   and isnull(dr,0)=0 ");  //已付金额
//			        ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
//			        if(arr!=null&&arr.size()>0){
//	    				HashMap hma = new HashMap();
//	    				for(int j=0; j<arr.size(); j++){
//	    					HashMap hm=(HashMap)arr.get(j);
//	    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
//	    					UFDouble je = new UFDouble(hm.get("rkje")==null?"0":hm.get("rkje").toString());
//	    					hma.put(flag, je);
//	    				}
//	    				String updateSQL1 = null;
//	    				if(hma.get("A").equals(hma.get("B"))){  //全部付款将入库子表关闭
//	    					updateSQL1 = "update eh_stock_in_b set def_1 = 'Y' where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0";
//	    				}
//	    				else{
//	    					updateSQL1 = "update eh_stock_in_b set def_1 = 'N' where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0";
//	    				}
//	    				pubitf.updateSQL(updateSQL1);
//	    			}
//	        	}
//	        }
//        }
		ishx = false;
		setBoEnabled();
	}

	protected void setBoEnabled() throws Exception {
		 super.setBoEnabled();
        ArapFkVO hvo = (ArapFkVO) getBillUI().getVOFromUI().getParentVO();
		if(hvo!=null&&hvo.getPrimaryKey()!=null){
			String qr_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject().toString();
			if(qr_flag.equals("true")){
				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			}
			int vbillstatus = hvo.getVbillstatus();
			boolean df_flag = hvo.getDf_flag()==null?false:hvo.getDf_flag().booleanValue();		//垫付
			boolean hx_flag = hvo.getHx_flag()==null?true:hvo.getHx_flag().booleanValue();		//核销
			if(vbillstatus==IBillStatus.CHECKPASS&&df_flag&&!hx_flag){			//为垫付未核销的
				getButtonManager().getButton(IEHButton.CREATEVOUCHER).setEnabled(true);
			}else{
				getButtonManager().getButton(IEHButton.CREATEVOUCHER).setEnabled(false);
			}
		    getBillUI().updateButtonUI();
		}
	   }

	 protected void onBoCard() throws Exception {
		 super.onBoCard();
		 setBoEnabled();
	 }
     
     public void onButton_N(ButtonObject bo, BillModel model) {
            super.onButton_N(bo, model);
            String bocode=bo.getCode()==null?"":bo.getCode().toString();
            if(bocode.equals("付款申请单")){
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sqje").setEnabled(false);
              UFDouble sqje = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sqje").getValueObject()==null?"0":
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sqje").getValueObject().toString());		//申请金额
              UFDouble yfje = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
            	  							getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());		//已付金额
              getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yfje", yfje);
              getBillCardPanelWrapper().getBillCardPanel().setHeadItem("wfje", sqje.sub(yfje));
            }
            if(bocode.equals("自制单据")){
            	AccountByCusdocRefModel.pk_cubasdoc = "";
            }
     }

    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
         PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
         String[] pk_in_bs = new String[row]; 
         for(int i=0;i<row;i++){
             String vsourcebillrowid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?"":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();//入库单子表PK 
             pk_in_bs[i] = vsourcebillrowid;
             //对入库单的回写
             String Sql = " update eh_stock_in_b set def_1='N' where pk_in_b='"+vsourcebillrowid+"' and isnull(dr,0)=0 ";
             pubitf.updateSQL(Sql);
         }     
         super.onBoTrueDelete();
    }
    //add by houcq 2011-02-09
    protected void onBoLineDel() throws Exception{
    	 PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
         String[] pk_in_bs = new String[row]; 
         for(int i=0;i<row;i++){
             String vsourcebillrowid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?"":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();//入库单子表PK 
             pk_in_bs[i] = vsourcebillrowid;
             //对入库单的回写
             String Sql = " update eh_stock_in_b set def_1='N' where pk_in_b='"+vsourcebillrowid+"' and isnull(dr,0)=0 ";
             pubitf.updateSQL(Sql);
         } 
         super.onBoLineDel();
    }
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn){
            case IEHButton.CREATEVOUCHER:    //垫付运费核销
            	yfhx();
                break;
        } 
        super.onBoElse(intBtn);
    }
    
    @Override
    protected void onBoCancel() throws Exception {
    	super.onBoCancel();
    	ishx = false;
    }
    
    /***
     * 运费核销
     * @throws Exception
     */
    public void yfhx() throws Exception{
		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
	    ArapFkVO hvo = (ArapFkVO) aggvo.getParentVO();
	    String pk_cubasdoc = hvo.getPk_cubasdoc();							//客户
	    UFDouble fkje = hvo.getFkje();										//付款金额
	    UFDouble yfcyje = fkje;												//运费剩余金额
//    	StringBuffer SQL = new StringBuffer()
//        .append(" select  a.billno htbillno,c.billno,d.pk_invbasdoc , f.invcode,f.invname,f.invspec,f.invtype,f.colour,g.brandname,h.measname,b.amount , d.inprice , d.inamount ,d.def_6, d.poundamount , ")
//        .append(" d.deduamount , d.deduprice , d.pk_in_b,c.pk_in,sum(isnull(e.bcfkje,0)) yfkje")
//        .append(" from eh_stock_in c  join eh_stock_in_b d")
//        .append(" on c.pk_in = d.pk_in")
//        .append(" left join eh_stock_contract a on a.pk_contract = c.pk_contract")
//        .append(" left join eh_stock_contract_b b on a.pk_contract = b.pk_contract AND b.pk_invbasdoc = d.pk_invbasdoc")
//        .append(" left join eh_arap_fk_b e on e.vsourcebillrowid = d.pk_in_b")
//        .append(" join eh_invbasdoc f on d.pk_invbasdoc = f.pk_invbasdoc")
//        .append(" left join eh_brand g on f.brand = g.pk_brand")
//        .append(" join bd_measdoc h on h.pk_measdoc = f.pk_measdoc")
//        .append(" where c.pk_cubasdoc = '"+pk_cubasdoc+"' and isnull ( a.dr , 0 ) = 0 and isnull ( b.dr , 0 ) = 0 ")
//        .append(" and isnull ( c.dr , 0 ) = 0 ")
//        .append(" and isnull ( d.dr , 0 ) = 0 and isnull ( e.dr , 0 ) = 0  and ( isnull ( d.def_1 , 'N' ) = 'N' or d.def_1 = '' )")
//        .append(" group by a.billno,c.billno,d.pk_invbasdoc ,f.invcode,f.invname,f.invspec,f.invtype,f.colour,g.brandname,h.measname, b.amount , d.inprice , d.inamount ,d.def_6, d.poundamount,d.deduamount , d.deduprice , d.pk_in_b,c.pk_in");
    	//新版SQL
	    try {
	    	String sql = ((ClientUI)getBillUI()).getRkinfoByCust(pk_cubasdoc, _getCorp().getPk_corp());
    		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null && arr.size()>0){
    			ishx = true;
    			getBillUI().setBillOperate(IBillOperate.OP_EDIT);
    			for(int i=0;i<arr.size();i++){
	                HashMap hm = (HashMap)arr.get(i);
	                String htbillno=hm.get("htbillno")==null?"":hm.get("htbillno").toString();
	                String billno=hm.get("billno")==null?"":hm.get("billno").toString();
	                String fpbillno = hm.get("fpbillno")==null?"":hm.get("fpbillno").toString();		//发票单据号
	                String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
	                String invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
	                String invname = hm.get("invname")==null?"":hm.get("invname").toString();
	                String invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
	                String invtype = hm.get("invtype")==null?"":hm.get("invtype").toString();
	                String colour = hm.get("colour")==null?"":hm.get("colour").toString();
	                String brand = hm.get("brandname")==null?"":hm.get("brandname").toString();
	                String measname = hm.get("measname")==null?"":hm.get("measname").toString();
	                UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
	                UFDouble taxinprice=new UFDouble(hm.get("inprice")==null?"0":hm.get("inprice").toString());
	                UFDouble inamount=new UFDouble(hm.get("inamount")==null?"0":hm.get("inamount").toString());
	                UFDouble rkje =new UFDouble(hm.get("def_6")==null?"0":hm.get("def_6").toString());
	                UFDouble sbamount=new UFDouble(hm.get("poundamount")==null?"0":hm.get("poundamount").toString());
	                UFDouble kzamount=new UFDouble(hm.get("deduamount")==null?"0":hm.get("deduamount").toString());
	                UFDouble kjprice=new UFDouble(hm.get("deduprice")==null?"0":hm.get("deduprice").toString());
	                UFDouble yfkje = new UFDouble(hm.get("yfkje")==null?"0":hm.get("yfkje").toString());
	                String pk_in_b=hm.get("pk_in_b")==null?"":hm.get("pk_in_b").toString();
                
	                //未付款金额
	                UFDouble wfkje = (rkje).sub(yfkje);
	                UFDouble bcfkje = null;
	                //本次付款金额 
	                if(yfcyje.toDouble()>0&&yfcyje.sub(wfkje).toDouble()<0){
	                	bcfkje = yfcyje;
	                }
	                yfcyje = yfcyje.sub(wfkje);
	                if(yfcyje.toDouble()>0){
	                	bcfkje = wfkje;
	                }
	                getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invcode, i, "vinvcode");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invname, i, "vinvname");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invspec, i, "vinvguige");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invtype, i, "vinvtype");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(colour, i, "vinvcolor");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(brand, i, "vinvbrabd");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(measname, i, "vinvunit");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
	                
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(billno, i, "rkbillno");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(htbillno, i, "htbillno");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(fpbillno, i, "fpbillno");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amount, i, "htamount");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(taxinprice, i, "pricemny");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(inamount, i, "rkmount");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(sbamount, i, "sbamount");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kzamount, i, "kzamount");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kjprice, i, "kjprice");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(rkje, i, "price");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(yfkje, i, "yfkje");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(wfkje, i, "wfkje");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcfkje, i, "bcfkje");
	                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_in_b, i, "vsourcebillrowid");
	                //单据行状态的修改。时间2010-01-12作者：张志远
	                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i,BillModel.ADD);
	                
    			}
    			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("hx_flag").setValue("Y");
    			BillItem[] headitems = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
                for(int i=0;i<headitems.length;i++){
                	if(!headitems[i].getKey().equals("memo")){
                		headitems[i].setEnabled(false);
                	}
                }
    		}else{
    			getBillUI().showWarningMessage("没有可核销的入库单!");
    		}
	    }catch (BusinessException e1) {
	        e1.printStackTrace();
	    }
//	    setBoEnabled();
//	    getBillUI().updateUI();
    }
    
    //核销方法
    public void Fphx() throws Exception{
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String fpbillno=calcDialog.billno==null?"":calcDialog.billno;
		if(fpbillno==null || fpbillno.length()<12){
			getBillUI().showErrorMessage("你输入的销售发票单据号不合法！");
			return;
		}
		//核销业务处理
		//1 核销(每次核销到对应的表中的找新最新的数据)
		String sql="select hxje,pk_invoice from eh_invoice where billno='"+fpbillno+"'";
		ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		UFDouble hxje=new UFDouble(0);
		String pk_invoice=null;
		for(int i=0;i<al.size();i++){
			HashMap hm=(HashMap) al.get(i);
			hxje=new UFDouble(hm.get("hxje")==null?"0":hm.get("hxje").toString());
			pk_invoice=hm.get("pk_invoice")==null?"":hm.get("pk_invoice").toString();
		}
		if(hxje.toDouble()==0){
			getBillUI().showErrorMessage("发票已经核销，无需在核销！");
			return;
		}
		ArapFkVO vo=(ArapFkVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		String pk_fk=vo.getPk_fk()==null?"":vo.getPk_fk().toString();
		if(pk_fk==null || pk_fk.length()<0){
			getBillUI().showErrorMessage("此收款单没有保存，无法核销！");
			return;
		}
		String sql2="select hxje from eh_arap_fk where pk_fk='"+pk_fk+"'";
		ArrayList al2=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
		UFDouble hxjefk=new UFDouble(0);
		for(int i=0;i<al2.size();i++){
			HashMap hm=(HashMap) al2.get(i);
			hxjefk=new UFDouble(hm.get("hxje")==null?"":hm.get("hxje").toString());
		}
		if(hxjefk.toDouble()==0){
			getBillUI().showErrorMessage("此收款单核销金额为零，无法核销！");
			return;
		}
		UFDouble je=hxjefk.sub(hxje);
		SkhxVO cvo=new SkhxVO();
		String updateSQL1=null;
		String updateSQL2=null;
		if(je.toDouble()>0){
			updateSQL1="update eh_arap_fk set hxje="+je+" where pk_fk='"+pk_fk+"'";
			updateSQL2="update eh_invoice set hxje=0 where billno='"+fpbillno+"'";
			cvo.setPk_sk(pk_fk);//收款的PK
			cvo.setPk_invoice(pk_invoice);//发票PK
			cvo.setHxje(hxje);//核销的金额
			cvo.setHxrq(_getDate());//核销日期
			
			
		}else if(je.toDouble()==0){
			updateSQL1="update eh_arap_fk set hxje=0 where pk_fk='"+pk_fk+"'";
			updateSQL2="update eh_invoice set hxje=0 where billno='"+fpbillno+"'";
			cvo.setPk_sk(pk_fk);//收款的PK
			cvo.setPk_invoice(pk_invoice);//发票PK
			cvo.setHxje(hxje);//核销的金额
			cvo.setHxrq(_getDate());//核销日期
			
		}else if(je.toDouble()<0){
			updateSQL1="update eh_arap_fk set hxje=0 where pk_fk='"+pk_fk+"'";
			je=new UFDouble(0).sub(je);
			updateSQL2="update eh_invoice set hxje="+je+" where billno='"+fpbillno+"'";
			cvo.setPk_sk(pk_fk);//收款的PK
			cvo.setPk_invoice(pk_invoice);//发票PK
			cvo.setHxje(hxjefk);//核销的金额
			cvo.setHxrq(_getDate());//核销日期
		}
		ArrayList alU=new ArrayList();
		alU.add(updateSQL1);
		alU.add(updateSQL2);
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		IVOPersistence IVOPersistence =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
		IVOPersistence.insertVO(cvo);   //增加到报表中
		pubitf.UpdateSQLS(alU);
    }
     
    @Override
    public String addCondtion() {
    	return  " vbilltype = '" + IBillType.eh_h1101005 + "' ";
    }
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	//以下代码由houcq于2010-11-01年注释掉
//    	getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//    	getBillUI().updateButtonUI();
    }

    /**
     * 功能：对入库单的回写
     * 
     * 
     * 
     */
	public void onBoCommit() throws Exception {
		
		 // 关闭入库单  edit by wb 2009-11-17 15:17:23
		int row = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		HashMap pkHm = new HashMap();
		StringBuffer sqlwhere = new StringBuffer();
		String[] ary = new String[row];
		for(int i=0;i<row;i++){
			String pk_rkd_b =  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?"":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();//入库单子表PK 
			ary[i]=pk_rkd_b;
		}
		sqlwhere.append(PubTools.combinArrayToString(ary));
		if(sqlwhere!=null){
			StringBuffer str = new StringBuffer()
			.append(" SELECT b.pk_in_b,b.def_1 FROM eh_stock_in_b b WHERE NVL(b.dr,0)=0 AND b.pk_in_b in "+sqlwhere.toString()+"");
			IUAPQueryBS iq = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList arrl = (ArrayList) iq.executeQuery(str.toString(), new MapListProcessor());
        	if(arrl!=null&&arrl.size()>0){
        		for(int i=0; i<arrl.size();i++){
        			HashMap hm = (HashMap) arrl.get(i);
        			String pk_in_b = hm.get("pk_in_b")==null?"":hm.get("pk_in_b").toString();
        			String def_1 = hm.get("def_1")==null?"N":hm.get("def_1").toString();
        			pkHm.put(pk_in_b,def_1);
        		}
        		
        		StringBuffer mess = new StringBuffer().append("行");
        		for(int i=0;i<row;i++){
        			//def_6保存的是出库单的子表PK
        			String pk_in_b = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();
        			if(pkHm.containsKey(pk_in_b)){
        				String def_1 = pkHm.get(pk_in_b).toString();
            			if("Y".equals(def_1)){
            				mess.append(" "+(i+1)+"");
            			}
        			}
        		}
        		if(!mess.toString().equals("行")){
        			mess.append(" 入库单已付款！请重新选择！");
        			this.getBillUI().showErrorMessage(mess.toString());
        			return;
        		}
		}
		super.onBoCommit();
		if(row>0){
	        String[] pk_in_bs = new String[row];
	        
			for (int i = 0; i < row; i++) {	
	        	String pk_rkd_b =  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid")==null?null:
                    getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillrowid").toString();//入库单子表PK 
	        	if(pk_rkd_b!=null){
		        	pk_in_bs[i] = pk_rkd_b;
		        	StringBuffer hxsql = new StringBuffer()
			        .append(" select sum(isnull(def_6,0)) rkje,'A' flag from eh_stock_in_b where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0 ")  // 入库金额
			        .append(" union all")
			        .append(" select sum(isnull(bcfkje,0)) rkje,'B' flag from eh_arap_fk_b where vsourcebillrowid = '"+pk_rkd_b+"'   and isnull(dr,0)=0 ");  //已付金额
		        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		        	ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(hxsql.toString(),new MapListProcessor());
			        if(arr!=null&&arr.size()>0){
	    				HashMap hma = new HashMap();
	    				for(int j=0; j<arr.size(); j++){
	    					HashMap hm=(HashMap)arr.get(j);
	    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
	    					UFDouble je = new UFDouble(hm.get("rkje")==null?"0":hm.get("rkje").toString());
	    					hma.put(flag, je);
	    				}
	    				String updateSQL1 = null;
	    				if(hma.get("A").equals(hma.get("B"))){  //全部付款将入库子表关闭
	    					updateSQL1 = "update eh_stock_in_b set def_1 = 'Y' where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0";
	    				}
	    				else{
	    					updateSQL1 = "update eh_stock_in_b set def_1 = 'N' where pk_in_b = '"+pk_rkd_b+"' and isnull(dr,0)=0";
	    				}
	    				PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	    				pubitf.updateSQL(updateSQL1);
	    			}
	        	}
	        }
        }
	}
	}
	}
