package nc.ui.eh.cw.h1101005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.AccountByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：付款单
 * @author 王明
 * 2008-05-28 下午04:03:18
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
    
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}
	
	@Override
	protected void initPrivateButton() {
   	 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CREATEVOUCHER,"预付款核销","预付款核销");
   	 	btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
   	 	addPrivateButton(btn1);
   	 	super.initPrivateButton();
   }

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));	
		getBillCardPanel().setHeadItem("fkrq", _getDate());     //付款日期
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {
		//审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     super.initSelfData();
	}
	
	
  
	@SuppressWarnings("unchecked")
	@Override
	public void afterEdit(BillEditEvent e) {
        String strkey = e.getKey();
        if(strkey.equals("fkje")){
//        	UFDouble sqje = new UFDouble(getBillCardPanel().getHeadItem("sqje").getValueObject()==null?"0":
//                getBillCardPanel().getHeadItem("sqje").getValueObject().toString());           
        	UFDouble fkje = new UFDouble(getBillCardPanel().getHeadItem("fkje").getValueObject()==null?"0":
                getBillCardPanel().getHeadItem("fkje").getValueObject().toString());           
//            UFDouble yfje = new UFDouble(getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
//                getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
//            UFDouble yfje2 =  fkje.add(yfje);										//已付金额 = 本次付款金额+已付款金额
//            UFDouble wfjehead = sqje.sub(yfje2);									//表头未付金额 = 申请金额-已付金额
            
            int row = getBillCardPanel().getBillTable().getRowCount();
            UFDouble camount = fkje;
            for(int i=0;i<row;i++){
            	UFDouble yfkje = new UFDouble(getBillCardPanel().getBodyValueAt(i, "yfkje")==null?"0":
                    							getBillCardPanel().getBodyValueAt(i, "yfkje").toString());//已经付款金额
            	UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(i, "price")==null?"0":
                    							getBillCardPanel().getBodyValueAt(i, "price").toString());//入库金额
            	UFDouble wfkje = price.sub(yfkje);
            	UFDouble wfje = new UFDouble(0);
                if(camount.compareTo(wfkje)>0){
                    getBillCardPanel().setBodyValueAt(wfkje, i, "bcfkje");
                    wfje = price.sub(yfkje).sub(wfkje);
                	getBillCardPanel().setBodyValueAt(wfje, i, "wfkje");
                }
                if(camount.compareTo(wfkje)<=0){
                    getBillCardPanel().setBodyValueAt(camount, i, "bcfkje");
                    wfje = price.sub(yfkje).sub(camount);
                	getBillCardPanel().setBodyValueAt(wfje, i, "wfkje");
                }
                camount=camount.sub(wfkje).toDouble()<0?new UFDouble("0"):camount.sub(wfkje);   
            }   
            int k=0;
            if (camount.toDouble()!=0)
            {
            	for(int j=0;j<row;j++){
                	UFDouble wfkje = new UFDouble(getBillCardPanel().getBodyValueAt(j, "wfkje")==null?"0":
                        							getBillCardPanel().getBodyValueAt(j, "wfkje").toString());
                	if (wfkje.toDouble()==0)
                	{
                		continue;
                	}
                	else
                	{
                		k=j;
                		break;
                	}
                }   
            }
            UFDouble[] bcfkjearr = new UFDouble[row-k];
            UFDouble[] wfkjearr = new UFDouble[row-k];
            for (int m=0;m<row-k;m++)
            {
            	UFDouble bcfkje = new UFDouble(getBillCardPanel().getBodyValueAt(k+m, "bcfkje")==null?"0":
					getBillCardPanel().getBodyValueAt(k+m, "bcfkje").toString());            	
            	UFDouble wfkje = new UFDouble(getBillCardPanel().getBodyValueAt(k+m, "wfkje")==null?"0":
					getBillCardPanel().getBodyValueAt(k+m, "wfkje").toString());
            	bcfkjearr[m] = bcfkje;
            	wfkjearr[m] = wfkje;
            }
            UFDouble btye =camount;
            if (btye.toDouble()>0)
            {
            	for(int i=0;i<row-k;i++)
                {
                	UFDouble bcfkje = bcfkjearr[i];
                	UFDouble wfkje = wfkjearr[i];
                	if (wfkje.toDouble()==0)
                	{
                		continue;
                	}  
              
                    if(btye.sub(wfkje).toDouble()>=0){
                        getBillCardPanel().setBodyValueAt(bcfkje.add(wfkje), i+k, "bcfkje");
                    	getBillCardPanel().setBodyValueAt(0, i+k, "wfkje");
                    	btye=btye.sub(wfkje).toDouble()<0?new UFDouble("0"): btye.sub(wfkje);                	
                    	continue;
                    }
                    else
                    {
                        getBillCardPanel().setBodyValueAt(btye.add(bcfkje), i+k, "bcfkje");
                    	getBillCardPanel().setBodyValueAt(wfkje.sub(btye.add(bcfkje)), i+k, "wfkje");
                    	btye=btye.sub(wfkje).toDouble()<0?new UFDouble("0"): btye.sub(wfkje);
                    }
                }
            }
        }
        
        if(strkey.equals("bcfkje")&&(!ClientEventHandler.ishx)){
            int row2 = getBillCardPanel().getBillTable().getSelectedRow();//所选的行
            UFDouble bcfkje = new UFDouble(getBillCardPanel().getBodyValueAt(row2, "bcfkje")==null?"0":
                getBillCardPanel().getBodyValueAt(row2, "bcfkje").toString());
            UFDouble yfkje = new UFDouble(getBillCardPanel().getBodyValueAt(row2, "yfkje")==null?"0":
				getBillCardPanel().getBodyValueAt(row2, "yfkje").toString());//已经付款金额
            UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(row2, "price")==null?"0":
				getBillCardPanel().getBodyValueAt(row2, "price").toString());//入库金额
            UFDouble wfkje = price.sub(yfkje);
            if(bcfkje.compareTo(wfkje)>0){
                this.showErrorMessage("第("+(row2+1)+"行)本次付款金额不能大于未付款金额!");
                getBillCardPanel().setBodyValueAt(null, row2, "bcfkje");
                getBillCardPanel().setBodyValueAt(null, row2, "yfkje");
                return;
            }
            
            if(wfkje.compareTo(new UFDouble(0))==0){
                this.showErrorMessage("第("+(row2+1)+"行)物料已经完全付款,请手动删除!");
                return;
            }
            UFDouble wfje = price.sub(yfkje).sub(bcfkje);
        	getBillCardPanel().setBodyValueAt(wfje, row2, "wfkje");
            int row = getBillCardPanel().getBillTable().getRowCount();//表体行总数
            UFDouble amount = new UFDouble();
            
            for(int i=0;i<row;i++){
                UFDouble bcfkje2 = new UFDouble(getBillCardPanel().getBodyValueAt(i, "bcfkje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "bcfkje").toString());
                amount=amount.add(bcfkje2);
            }
            UFDouble sqje = new UFDouble(getBillCardPanel().getHeadItem("sqje").getValueObject()==null?"0":
                getBillCardPanel().getHeadItem("sqje").getValueObject().toString());           
            UFDouble yfje = new UFDouble(getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
                getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
            UFDouble yfje2 =  amount.add(yfje);										//已付金额 = 本次付款金额+已付款金额
            UFDouble wfjehead = sqje.sub(yfje2);									//表头未付金额 = 申请金额-已付金额
            getBillCardPanel().setHeadItem("fkje", amount);    
            getBillCardPanel().setHeadItem("wfje", wfjehead);
        }
        
        if(strkey.equals("pk_cubasdoc")){
            String pk_cubasdoc=(String) (getBillCardPanel().getHeadItem("pk_cubasdoc")==null?"":getBillCardPanel()
                    .getHeadItem("pk_cubasdoc").getValueObject());//客商PK 
           
            if(pk_cubasdoc!=null&&pk_cubasdoc.length()>0){
            	AccountByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;//客商账户参照用
            	String custcode = null;			//选择的客商编码
            	UIRefPane cubasdocPane = (UIRefPane)getBillCardPanel().getHeadItem("pk_cubasdoc").getComponent(); 
     			Vector vc = cubasdocPane.getSelectedData();
     			if(vc!=null&&vc.size()>0){
     				Vector vcc = (Vector)vc.get(0);
     				if(vcc!=null&&vcc.size()>0){
     					custcode = vcc.get(0).toString();
     				}
     			}
     			//add by houcq 2011-02-09 begin
     			/*增加核销检查的功能。在做付款单，录入供应商后，系统检该供应商名下的所有付款单，
     			 * 如果有预付款标记为Y且核销标记为N时，提示“该客户尚有单据未核销，无法付款，请核销后再做付款！”，
     			 * 如果预付款标记为N或者NULL且核销标记为Y或null时，不做操作。
     			*/
     			//String tmp="select * from  eh_arap_fk where nvl(hx_flag,'N')='N' and nvl(df_flag,'N')='Y' and pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and pk_cubasdoc='"+pk_cubasdoc+"'";
     			
     			
     			//add by houcq 2011-02-09 end
//            	String pk_corp = null;
//            	StringBuffer isql = new StringBuffer()
//            	.append(" SELECT pk_corp from bd_corp where unitname in ")
//            	.append(" (select b.custname from bd_cumandoc a,bd_cubasdoc b where pk_cumandoc = '"+pk_cubasdoc+"' ")
//            	.append(" and b.custprop = '2' and a.pk_cubasdoc = b.pk_cubasdoc) ");//custprop：客商类型（2：内部客商）
//            	IUAPQueryBS isqlUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());  
//            	try {
//					ArrayList arr = (ArrayList) isqlUAPQueryBS.executeQuery(isql.toString(), new MapListProcessor());
//					if(arr!=null&&arr.size()>0){
//						HashMap hs = (HashMap) arr.get(0);
//						pk_corp = hs.get("pk_corp")==null?"":hs.get("pk_corp").toString();
//						AccountByCusdocRefModel.pk_corp = pk_corp;//内部客商需用此PK_CORP做判断。时间：2009-01-19
//					}
//				
					 /**根据客户带入客商余额 add by wb 2010-3-31 10:49:04***/
					   
					try {
						UFDouble overage = new PubTools().getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());
						 getBillCardWrapper().getBillCardPanel().setHeadItem("canusemoney", overage);
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}		//查找客户余额
					  
					   /****************** end 2010-3-31 10:49:22******************************/
//            	} catch (Exception e2) {
//					e2.printStackTrace();
//				}
             IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());             
//             StringBuffer sql = new StringBuffer()
//             .append(" select a.pk_psndoc,a.psnname,b.freecustflag,d.pk_deptdoc,d.deptname,b.bankname,b.bankno from bd_psndoc a,bd_cubasdoc b,eh_custyxdb c,bd_deptdoc d  ")
//             .append(" where a.pk_psndoc=c.pk_psndoc  and a.pk_deptdoc=d.pk_deptdoc ")
//             .append(" and b.pk_cubasdoc=c.pk_cubasdoc ")
//             .append(" and b.pk_cubasdoc ='"+pk_cubasdoc+"' and c.ismain ='Y' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ");
             //新版SQL
      		String sql = "  select  bp.pk_psndoc,bp.psnname,bcc.freecustflag,  "+
      					 " bd.pk_deptdoc,bd.deptname,bkb.pk_bankaccbas  "+
      					 " from  bd_cumandoc bc "+
      					 " inner join bd_cubasdoc bcc on bc.pk_cubasdoc=bcc.pk_cubasdoc  "+
      					 " and (bc.pk_cumandoc='"+pk_cubasdoc+"' or bcc.custcode = '"+custcode+"') and nvl(bcc.dr,0)=0  "+
      					 " and bc.pk_corp='"+_getCorp().getPrimaryKey()+"' "+
      					 " left join bd_custbank bcb on bcb.pk_cubasdoc=bcc.pk_cubasdoc  "+
      					 " and nvl(bcb.dr,0)=0  and bcb.defflag='Y'  "+
      					 " left join bd_bankaccbas bkb on bkb.pk_bankaccbas=bcb.pk_accbank "+
      					 " and nvl(bkb.dr,0)=0 "+
      					 " left join eh_custyxdb ec on ec.pk_cubasdoc=bc.pk_cumandoc   "+
      					 " and ec.ismain ='Y' and nvl(ec.dr,0)=0  "+
      					 " left join bd_psndoc bp on bp.pk_psndoc=ec.pk_psndoc "+
      					 " and  nvl(bp.dr,0)=0  "+
      					 " left join bd_deptdoc bd on bd.pk_deptdoc=bp.pk_deptdoc   "+
      					 " and  nvl(bd.dr,0)=0   and (bc.custflag = '0' OR bc.custflag = '1' OR bc.custflag = '2')"; 
             try {
                Vector vector =(Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                if(vector!=null&&vector.size()!=0){
                    Vector ve =(Vector) vector.get(0);
                    String pk_psndoc=ve.get(0)==null?"":ve.get(0).toString();  //人员PK
                    String vpnsdocname=ve.get(1)==null?"":ve.get(1).toString(); //人员民称
                    String pk_deptdoc=ve.get(3)==null?"":ve.get(3).toString();  //部门PK
                    String deptname=ve.get(4)==null?"":ve.get(4).toString();    //部门名称   
                    String bankname=ve.get(5)==null?"":ve.get(5).toString();//开户账户PK
//                    String bankno=ve.get(6)==null?"":ve.get(6).toString();//开户账号
                    getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
                    getBillCardPanel().setHeadItem("vuser", vpnsdocname);
                    getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
                    getBillCardWrapper().getBillCardPanel().setHeadItem("vwldept", deptname);
//                    getBillCardWrapper().getBillCardPanel().setHeadItem("cubasbank", bankname);
                    getBillCardWrapper().getBillCardPanel().setHeadItem("bankaccount", bankname);
                    getBillCardWrapper().getBillCardPanel().execHeadEditFormulas();
                }   
                
                /**选择客商后带入需要付款的信息 add by wb 2009-11-17 9:48:32***/
                //清空表体
                int rowcount=getBillCardPanel().getRowCount();
                int[] rowss=new int[rowcount];
                for(int i=rowcount - 1;i>=0;i--){
                    rowss[i]=i;
                }
                getBillCardPanel().getBillModel().delLine(rowss);
                String rksql = getRkinfoByCust(pk_cubasdoc, _getCorp().getPrimaryKey());
                ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(rksql, new MapListProcessor());
                
                //modify by houcq 2011-02-17 原料零星客商，五金零星客商，零星客商不做核销判断
                String pk_cubasdoc1 = new PubTools().getPk_cubasdoc(pk_cubasdoc);
                if (!("0001B81000000007B6E8".equals(pk_cubasdoc1) ||"0001A8100000000D1MJ6".equals(pk_cubasdoc1) ||"0001A8100000000D2BK1".equals(pk_cubasdoc1)))
                {
                	//add by houcq 2011-02-15
                	String pk_corp2=ClientEnvironment.getInstance().getCorporation().getPk_corp();
                    StringBuilder tmp = new StringBuilder();
         			tmp.append("  select * from  eh_arap_fk where nvl(hx_flag,'N')='N' and nvl(df_flag,'N')='Y'")
         			.append(" and vbillstatus = 1 ")
         			.append(" and pk_corp = '")
         			.append(pk_corp2)
         			.append("' and pk_cubasdoc = '")
         			.append(pk_cubasdoc)
         			.append("' ");
                    ArrayList arrs = (ArrayList) iUAPQueryBS.executeQuery(tmp.toString(), new MapListProcessor());
    				if (arrs!=null&&arrs.size()>0&&arr!=null &&arr.size()>0)
         			{		
    					String pk_invbasdocnew = new PubTools().getPk_cubasdoc(pk_cubasdoc);
    					
    					StringBuilder sb = new StringBuilder();
    					sb.append("select sum(whxje)-(sum(rkje)-sum(yfkje)) zhxje from ( select sum(fkje) whxje,0 rkje, 0 yfkje")
    					.append(" from eh_arap_fk a,bd_cumandoc c, bd_cubasdoc d ")
    					.append("  where nvl(a.hx_flag, 'N') = 'N'")
    					.append("  and nvl(a.df_flag, 'N') = 'Y'")
    					.append("  and a.vbillstatus = 1")
    					.append("   and a.pk_cubasdoc=c.pk_cumandoc")
    					.append("   and c.pk_cubasdoc=d.pk_cubasdoc")
    					.append("   and a.pk_corp = '")
    					.append(pk_corp2)
    					.append("'   and d.pk_cubasdoc = '")
    					.append(pk_invbasdocnew)
    					.append("' ")
    					.append("union all")
    					.append("  select 0 whxje, 0 rkje, sum(nvl(d.bcfkje, 0)) yfkje from eh_stock_in  a,  eh_stock_in_b b, eh_arap_fk_b  d, bd_cumandoc e, bd_cubasdoc  f")
    					.append("  where a.pk_in = b.pk_in")
    					.append("  and b.pk_in_b = d.vsourcebillrowid(+)")
    					.append("  and a.pk_cubasdoc = e.pk_cumandoc")
    					.append("  and e.pk_cubasdoc = f.pk_cubasdoc")
    					.append("  and a.pk_corp = '")
    					.append(pk_corp2)
    					.append("'  and a.vbillstatus = 1")
    					.append("  and nvl(a.dr, 0) = 0")
    					.append("  and nvl(b.dr, 0) = 0")
    					.append("  and nvl(d.dr(+), 0) = 0")
    					.append("  and nvl(b.def_1, 'N') <> 'Y'")
    					.append("  and f.pk_cubasdoc = '")
    					.append(pk_invbasdocnew)
    					.append("' ")
    					.append("union all    ")
    					.append("select 0 whxje,sum(nvl(b.def_6, 0)) rkje,0 yfkje from eh_stock_in   a,")
    					.append("  eh_stock_in_b b,")
    					.append("  bd_cumandoc   e,")
    					.append("  bd_cubasdoc   f")
    					.append("  where a.pk_in = b.pk_in")
    					.append("  and a.pk_cubasdoc = e.pk_cumandoc")
    					.append("  and e.pk_cubasdoc = f.pk_cubasdoc")
    					.append("  and a.pk_corp = '")
    					.append(pk_corp2)
    					.append("' and a.vbillstatus = 1")
    					.append("  and nvl(a.dr, 0) = 0")
    					.append("  and nvl(b.dr, 0) = 0")
    					.append("  and nvl(b.def_1, 'N') <> 'Y'")
    					.append("  and f.pk_cubasdoc = '")
    					.append(pk_invbasdocnew)
    					.append("') ");
    					
    					Object ob = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor());
    					if (ob!=null)
    					{
    						Double whxje=Double.parseDouble(ob.toString());
    						if (whxje<0)
    						{
    							this.showErrorMessage("        该客户尚有单据未核销，无法付款，请核销后再做付款！");
    		     				getBillCardPanel().setHeadItem("pk_cubasdoc", "");
    		     				return;
    						}	     				
    					}
         			}
    				//add by houcq end
                }
                
        		if(arr!=null && arr.size()>0){
        			for(int i=0;i<arr.size();i++){
    	                HashMap hm = (HashMap)arr.get(i);
    	                String htbillno=hm.get("htbillno")==null?"":hm.get("htbillno").toString();
    	                String billno=hm.get("billno")==null?"":hm.get("billno").toString();
//    	                String fpbillno = hm.get("fpbillno")==null?"":hm.get("fpbillno").toString();		//发票单据号
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
    	                
    	                //获得发票号
    	                String SQL = " select distinct c.fpnumber from eh_stock_in  a, eh_stock_in_b b,eh_arap_stockinvoice c,eh_arap_stockinvoices_b g" +
    	                		" where a.pk_in = b.pk_in and a.pk_in = g.vsourcebillid and c.pk_stockinvoice = g.pk_stockinvoice(+)" +
    	                		" and a.pk_corp = '"+_getCorp().getPrimaryKey()+"' and b.pk_in_b = '"+pk_in_b+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 and nvl(g.dr,0)=0 ";
    	                Vector VE = (Vector)iUAPQueryBS.executeQuery(SQL,new VectorProcessor());
    	                String fpbillno = "";
    	                if(VE!=null && VE.size()>0){
    	                	Vector fpvc = (Vector)VE.get(0);
    	                	fpbillno = fpvc.get(0)==null?"":fpvc.get(0).toString();
    	                }
    	                
    	                //未付款金额
                        UFDouble wfkje = (rkje).sub(yfkje);
                        
    	                getBillCardPanel().getBillModel().addLine();
    	                getBillCardPanel().setBodyValueAt(invcode, i, "vinvcode");
    	                getBillCardPanel().setBodyValueAt(invname, i, "vinvname");
    	                getBillCardPanel().setBodyValueAt(invspec, i, "vinvguige");
    	                getBillCardPanel().setBodyValueAt(invtype, i, "vinvtype");
    	                getBillCardPanel().setBodyValueAt(colour, i, "vinvcolor");
    	                getBillCardPanel().setBodyValueAt(brand, i, "vinvbrabd");
    	                getBillCardPanel().setBodyValueAt(measname, i, "vinvunit");
    	                getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
    	                
    	                getBillCardPanel().setBodyValueAt(billno, i, "rkbillno");
    	                getBillCardPanel().setBodyValueAt(htbillno, i, "htbillno");
    	                getBillCardPanel().setBodyValueAt(fpbillno, i, "fpbillno");
    	                getBillCardPanel().setBodyValueAt(amount, i, "htamount");
    	                getBillCardPanel().setBodyValueAt(taxinprice, i, "pricemny");
    	                getBillCardPanel().setBodyValueAt(inamount, i, "rkmount");
    	                getBillCardPanel().setBodyValueAt(sbamount, i, "sbamount");
    	                getBillCardPanel().setBodyValueAt(kzamount, i, "kzamount");
    	                getBillCardPanel().setBodyValueAt(kjprice, i, "kjprice");
    	                getBillCardPanel().setBodyValueAt(rkje, i, "price");
    	                getBillCardPanel().setBodyValueAt(yfkje, i, "yfkje");
    	                getBillCardPanel().setBodyValueAt(wfkje, i, "wfkje");
    	                getBillCardPanel().setBodyValueAt(pk_in_b, i, "vsourcebillrowid");
        			}
        		}else{
        			showWarningMessage("此单位未关联入库单!");
        		}
            } catch (Exception e1) {
                e1.printStackTrace();
            }
           }else{
        	   AccountByCusdocRefModel.pk_cubasdoc = "";
        	   this.getBillCardPanel().setHeadItem("bankaccount", "");	//收款账户
        	   this.getBillCardPanel().setHeadItem("cubasbank", "");	//收款账号
        	   getBillCardPanel().setHeadItem("canusemoney", null);		//余额
           }
        }
        if(strkey.equals("pk_psndoc")){
        	
        	String pk_psndoc=(String) (getBillCardPanel().getHeadItem("pk_psndoc")==null?"":getBillCardPanel()
                    .getHeadItem("pk_psndoc").getValueObject());//业务员PK 
        	
        	StringBuffer sql = new StringBuffer()
        	.append(" select bd.pk_deptdoc, bd.deptname from bd_psndoc bp , bd_deptdoc bd ")
        	.append(" where  bd.pk_deptdoc = bp.pk_deptdoc and bp.pk_psndoc = '"+pk_psndoc+"' ")
        	.append(" and nvl(bd.dr, 0) = 0 and nvl(bp.dr, 0) = 0 ");
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());             
        	ArrayList arr;
			try {
				arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
				if(arr!=null && arr.size()>0){
					String pk_deptdoc = null;
					String deptname = null;
	    			for(int i=0;i<arr.size();i++){
	    				HashMap hm = (HashMap) arr.get(i);
	    				pk_deptdoc = hm.get("pk_deptdoc")==null?"":hm.get("pk_deptdoc").toString();
	    				deptname = hm.get("deptname")==null?"":hm.get("deptname").toString();
	    			}
	    			getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
                    getBillCardWrapper().getBillCardPanel().setHeadItem("vwldept", deptname);
	    		}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
        	
        }
        
	    super.afterEdit(e);
	}
	
	/***
	 * 根据客商找到需要付款的入库单信息
	 * @param pk_cumandoc
	 * @param pk_corp
	 * @return
	 * wb 2009-11-17 9:46:54
	 * @throws Exception 
	 */
	public String getRkinfoByCust(String pk_cumandoc,String pk_corp) throws Exception{
		StringBuffer sql = new StringBuffer()
		//.append("  select b.htbillno htbillno,  a.billno, ")
		.append("  select distinct b.htbillno htbillno,  a.billno, ")//modify by houcq 2011-03-30
		.append("     a.pk_invbasdoc, d.invcode,d.invname,d.invspec,d.invtype,d.def1 colour,e.brandname,f.measname,  ")
		.append("     b.amount, a.inprice, a.inamount, a.def_6, a.poundamount,a.deduamount,a.deduprice,")
		.append("     a.pk_in_b, a.pk_in, a.yfkje")
		.append("  from ")
		.append("  (select a.billno ")
		//.append("  ,c.fpnumber fpbillno ")
		.append(" ,a.pk_contract,b.pk_invbasdoc, ")
		.append("         b.inprice, b.inamount,b.def_6, b.poundamount, b.deduamount, b.deduprice, b.pk_in_b, a.pk_in, ")
		.append("         sum(nvl(d.bcfkje, 0)) yfkje ")
		.append("  from eh_stock_in a,eh_stock_in_b b,eh_arap_fk_b d,bd_cumandoc e,bd_cubasdoc f ")
		.append("  where a.pk_in = b.pk_in")
		//.append("  and a.pk_in = c.vsourcebillid(+)")
		//.append(" and a.pk_in = g.vsourcebillid(+) ")
		//.append(" and  g.pk_stockinvoice = c.pk_stockinvoice(+) ")
		.append("  and b.pk_in_b = d.vsourcebillrowid(+)")
		.append("  and a.pk_cubasdoc = e.pk_cumandoc")
		.append("  and e.pk_cubasdoc = f.pk_cubasdoc")
		.append("  and a.pk_corp = '"+pk_corp+"'")
		.append("  and a.vbillstatus = 1 ")	 //增加入库单审批通过控制(审批通过且被关闭的单据也会被过滤掉) add by zqy 2010年11月17日9:45:23
		.append("  and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(d.dr(+), 0) = 0")//如果付款单被删除应该还可以关联到入库子表
//		.append("  and nvl(a.lock_flag, 'N') <> 'Y'")	//去除掉关闭控制条件  add by zqy 2010年11月17日9:45:23
		.append("  and nvl(b.def_1, 'N')<>'Y' ")
		.append("  and f.pk_cubasdoc = '"+new PubTools().getPk_cubasdoc(pk_cumandoc)+"' ")
		.append("  group by a.billno ,b.pk_invbasdoc,a.pk_contract,")
		.append("  b.inprice, b.inamount,b.def_6, b.poundamount, b.deduamount, b.deduprice, b.pk_in_b, a.pk_in")
		.append("  ) a,")
		.append("  (select e.billno htbillno,b.pk_in_b,b.pk_invbasdoc, f.amount")
		.append("  from eh_stock_in a,eh_stock_in_b b,eh_stock_contract e,eh_stock_contract_b f,bd_cumandoc g,bd_cubasdoc h")
		.append("  where a.pk_in = b.pk_in")
		.append("  and a.pk_contract = e.pk_contract")
		.append("  and e.pk_contract = f.pk_contract")
		.append("  and b.pk_invbasdoc = f.pk_invbasdoc")
		.append("  and a.pk_cubasdoc = g.pk_cumandoc")
		.append("  and g.pk_cubasdoc = h.pk_cubasdoc")
		.append("  and a.pk_corp = '"+pk_corp+"'")
		.append("  and a.vbillstatus = 1 ")  //增加入库单审批通过控制(审批通过且被关闭的单据也会被过滤掉) add by zqy 2010年11月17日9:45:23
		.append("  and nvl(a.dr,0)=0 and nvl(b.dr,0)=0")
//		.append("  and nvl(a.lock_flag, 'N') <> 'Y'")		//去除掉关闭控制条件  add by zqy 2010年11月17日9:45:23
		.append("  and nvl(b.def_1, 'N')<>'Y'")
		.append("  and h.pk_cubasdoc = '"+new PubTools().getPk_cubasdoc(pk_cumandoc)+"' ")
		.append("  ) b ,bd_invmandoc c , bd_invbasdoc d ,eh_brand e,bd_measdoc f")
		.append("  where a.pk_in_b = b.pk_in_b(+) ")
		.append("  and c.pk_invmandoc = a.pk_invbasdoc")
		.append("  and c.pk_invbasdoc = d.pk_invbasdoc")
		.append("  and d.invpinpai = e.pk_brand(+)")
		.append("  and d.pk_measdoc = f.pk_measdoc and a.def_6 <> a.yfkje ")		//排除 入库金额 = 已付款金额的数据 edit by wb 2010-4-1 10:15:16
		.append("  order by a.billno");
		return sql.toString();
	}

}
