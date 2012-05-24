package nc.ui.eh.cw.h1100505;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.cw.h1100505.ArapFkqsBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：付款申请单
 * @author zqy
 * 2008-05-28 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {

//    UIRefPane ref=null;
//    public static String pk_cubasdoc =null;
    public ClientUI(){
        super();
//        ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_hth").getComponent();
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
		getBillCardPanel().getHeadItem("vbilltype").setValue(getBilltype());	
		getBillCardPanel().setHeadItem("yjfkrq", _getDate());
		super.setDefaultData_withNObillno();
	}
    
	@Override
	protected void initSelfData() {
		//审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     super.initSelfData();
	}
    
	@Override
	@SuppressWarnings("unchecked")
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
       }
        
        //客商带出部门等一些信息 
		if(strKey.equals("pk_cubasdoc")){
//            changeRefModel(pk_cubasdoc);           
     		String pk_cubasdoc=(String) (getBillCardPanel().getHeadItem("pk_cubasdoc")==null?"":getBillCardPanel()
                    .getHeadItem("pk_cubasdoc").getValueObject());//客商PK 
            
     		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            
//             StringBuffer sql = new StringBuffer()
//             .append(" select a.pk_psndoc,a.psnname,b.freecustflag,d.pk_deptdoc,d.deptname,b.bankno,b.bankname from bd_psndoc a,bd_cubasdoc b,eh_custyxdb c,bd_deptdoc d  ")
//             .append(" where a.pk_psndoc=c.pk_psndoc  and a.pk_deptdoc=d.pk_deptdoc ")
//             .append(" and b.pk_cubasdoc=c.pk_cubasdoc ")
//             .append(" and b.pk_cubasdoc ='"+pk_cubasdoc+"' and c.ismain ='Y' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ");
     	    //新版SQL
     		String sql = "  select  bp.pk_psndoc,bp.psnname,bcc.freecustflag,  "+
     					 " bd.pk_deptdoc,bd.deptname,bkb.pk_bankaccbas  "+
     					 " from  bd_cumandoc bc "+
     					 " inner join bd_cubasdoc bcc on bc.pk_cubasdoc=bcc.pk_cubasdoc  "+
     					 " and bc.pk_cumandoc='"+pk_cubasdoc+"' and nvl(bcc.dr,0)=0  "+
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
     					 " and  nvl(bd.dr,0)=0  "; 
     		try {
             	Vector vector =(Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
             	if(vector!=null&&vector.size()!=0){
             		Vector ve =(Vector) vector.get(0);
             		String pk_psndoc=ve.get(0)==null?"":ve.get(0).toString();  //人员PK
             		String vpnsdocname=ve.get(1)==null?"":ve.get(1).toString(); //人员民称
             		String pk_deptdoc=ve.get(3)==null?"":ve.get(3).toString();  //部门PK
                 	String deptname=ve.get(4)==null?"":ve.get(4).toString();    //部门名称   
                    String bankno=ve.get(5)==null?"":ve.get(5).toString();//银行帐号PK
//                    String bankname = ve.get(6)==null?"":ve.get(6).toString();//银行账户
     				getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
     				getBillCardPanel().setHeadItem("vcaiperson", vpnsdocname);
     				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_deptdoc", pk_deptdoc);
					getBillCardWrapper().getBillCardPanel().setHeadItem("vdept", deptname);
//                    getBillCardWrapper().getBillCardPanel().setHeadItem("cubasbank", bankname);
                    getBillCardWrapper().getBillCardPanel().setHeadItem("bankaccount", bankno);
                    getBillCardWrapper().getBillCardPanel().execHeadEditFormulas();
             	}	
     		} catch (BusinessException e1) {
     			e1.printStackTrace();
     		}
             
             StringBuffer SQL = new StringBuffer()
//             .append(" select  a.billno htbillno,c.billno,g.billno fpbillno,d.pk_invbasdoc , b.amount , d.inprice , d.inamount ,d.def_6, d.poundamount , ")
//             .append(" d.deduamount , d.deduprice , d.pk_in_b,c.pk_in,sum(isnull(e.bcfkje,0)) yfkje")
//             .append(" from eh_stock_in c inner join eh_stock_in_b d")
//             .append(" on c.pk_in = d.pk_in")
//             .append(" left join eh_stock_contract a on a.pk_contract = c.pk_contract")
//             .append(" left join eh_stock_contract_b b on a.pk_contract = b.pk_contract ")
//             .append(" left join eh_arap_fk_b e on e.vsourcebillrowid = d.pk_in_b")
//             .append(" LEFT JOIN eh_arap_stockinvoices_b f ON f.vsourcebillid = c.pk_in and isnull ( e.dr , 0 ) = 0")
//             .append(" LEFT JOIN eh_arap_stockinvoice g ON f.pk_stockinvoice = g.pk_stockinvoice")
//             .append(" where c.pk_cubasdoc = '"+pk_cubasdoc+"' and isnull ( a.dr , 0 ) = 0 and isnull ( b.dr , 0 ) = 0 ")
//             .append(" and isnull ( c.dr , 0 ) = 0 ")
//             .append(" and isnull ( d.dr , 0 ) = 0 and isnull ( e.dr , 0 ) = 0  and ( isnull ( d.def_1 , 'N' ) = 'N' or d.def_1 = '' )")
//             .append(" group by a.billno,c.billno,g.billno,d.pk_invbasdoc , b.amount , d.inprice , d.inamount ,d.def_6, d.poundamount,d.deduamount , d.deduprice , d.pk_in_b,c.pk_in");
//             
             .append(" select a.billno htbillno, ")
             .append("        c.billno, ")
             .append("        g.billno fpbillno, ")
             .append("        d.pk_invbasdoc, ")
             .append("        b.amount, ")
             .append("        d.inprice, ")
             .append("        d.inamount, ")
             .append("        d.def_6, ")
             .append("        d.poundamount, ")
             .append("        d.deduamount, ")
             .append("        d.deduprice, ")
             .append("        d.pk_in_b, ")
             .append("        c.pk_in, ")
             .append("        sum(nvl(e.bcfkje, 0)) yfkje ")
             .append("   from eh_stock_in c ")
             .append("  inner join eh_stock_in_b d on c.pk_in = d.pk_in ")
             .append("   left join eh_stock_contract a on a.pk_contract = c.pk_contract  and nvl(a.dr, 0) = 0 ")
             .append("   left join eh_stock_contract_b b on (a.pk_contract = b.pk_contract  and b.pk_invbasdoc = d.pk_invbasdoc and nvl(b.dr, 0) = 0)  ")
             .append("   left join eh_arap_fk_b e on e.vsourcebillrowid = d.pk_in_b and nvl(e.dr, 0) = 0")
             .append("   LEFT JOIN  ")
             .append("   ( ")
             .append("       select b.vsourcebillrowid,max(a.billno) billno from  eh_arap_stockinvoice a  ")
             .append("       inner join eh_arap_stockinvoices_b b  ")
             .append("       ON a.pk_stockinvoice = b.pk_stockinvoice and a.pk_corp = '"+_getCorp().getPrimaryKey()+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 ")
             .append("       group by vsourcebillrowid ")
             .append("   ) g on g.vsourcebillrowid=d.pk_in_b ")
             .append("  where c.pk_cubasdoc = '"+pk_cubasdoc+"' ")
             .append("    and nvl(c.dr, 0) = 0 ")
             .append("    and nvl(d.dr, 0) = 0 ")
             .append("    and (nvl(d.def_1, 'N') = 'N' or d.def_1 = '') and nvl(c.lock_flag,'N')<>'Y'")
             .append("    and c.pk_corp='"+_getCorp().getPrimaryKey()+"' ")
             .append("  group by a.billno, ")
             .append("           c.billno, ")
             .append("           g.billno, ")
             .append("           d.pk_invbasdoc, ")
             .append("           b.amount, ")
             .append("           d.inprice, ")
             .append("           d.inamount, ")
             .append("           d.def_6, ")
             .append("           d.poundamount, ")
             .append("           d.deduamount, ")
             .append("           d.deduprice, ")
             .append("           d.pk_in_b, ")
             .append("           c.pk_in; ");

         ArrayList list = new ArrayList();
         try {
             ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
             if(arr!=null && arr.size()>0){
                 for(int i=0;i<arr.size();i++){
                     ArrayList all = new ArrayList();
                     HashMap hm = (HashMap)arr.get(i);
                     String htbillno=hm.get("htbillno")==null?"":hm.get("htbillno").toString();
                     String billno=hm.get("billno")==null?"":hm.get("billno").toString();
                     String fpbillno = hm.get("fpbillno")==null?"":hm.get("fpbillno").toString();		//发票单据号
                     String pk_invbasdoc=hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
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
                     
                     all.add(htbillno);
                     all.add(billno);
                     all.add(pk_invbasdoc);
                     all.add(amount);
                     all.add(taxinprice);
                     all.add(inamount);
                     all.add(sbamount);
                     all.add(kzamount);
                     all.add(kjprice);
                     all.add(yfkje);
                     all.add(wfkje);
                     all.add(pk_in_b);
                     all.add(rkje);
                     all.add(fpbillno);
                     list.add(all);
                 }
             }
             //清空表体
             int rowcount=getBillCardPanel().getRowCount();
             int[] rowss=new int[rowcount];
             for(int i=rowcount - 1;i>=0;i--){
                 rowss[i]=i;
             }
             getBillCardPanel().getBillModel().delLine(rowss);
             
             SuperVO[] vos=new SuperVO[list.size()];
             for(int i=0;i<list.size();i++){
                 ArapFkqsBVO abvo = new ArapFkqsBVO();
                 ArrayList hmm=(ArrayList) list.get(i);
                 abvo.setHtbillno(hmm.get(0).toString());//合同单号
                 abvo.setVbillno(hmm.get(1).toString());//入库单号
                 abvo.setPk_invbasdoc(hmm.get(2).toString());//物料
                 abvo.setHtmount(new UFDouble(hmm.get(3).toString()));//合同数量
                 abvo.setPricemny(new UFDouble(hmm.get(4).toString()));//入库单价
                 abvo.setRkmount(new UFDouble(hmm.get(5).toString()));//入库数量
                 abvo.setSbamount(new UFDouble(hmm.get(6).toString()));//司磅数量
                 abvo.setKzamount(new UFDouble(hmm.get(7).toString()));//扣重数量
                 abvo.setKjprice(new UFDouble(hmm.get(8).toString()));//扣价价格 
    //             abvo.setZtmount((new UFDouble(hmm.get(2).toString())).sub((new UFDouble(hmm.get(4).toString()))));//在途数量                 
                 abvo.setFkmny(new UFDouble(hmm.get(12).toString()));
                 abvo.setYfkje(new UFDouble(hmm.get(9).toString()));//已付款金额
                 abvo.setWfkje(new UFDouble(hmm.get(10).toString()));//未付款金额
                 abvo.setVsourcebillrowid(hmm.get(11).toString());//入库单子表的PK 
                 abvo.setMemo(hmm.get(13).toString());		//发票单号
                 vos[i]=abvo;
                 getBillCardPanel().getBillModel().addLine();
                 getBillCardPanel().getBillModel().setBodyRowVO(abvo, i);
                 
                 String[] formual=getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//获取编辑公式
                 getBillCardPanel().execBodyFormulas(i,formual);                       
             }                   
         } catch (BusinessException e1) {
             e1.printStackTrace();
         }
            
         }
       
		super.afterEdit(e);
	}

//    private void changeRefModel(String pk_sfkfs) {
//        nc.ui.eh.businessref.ContractRefModel contractrefmodel = new nc.ui.eh.businessref.ContractRefModel();
//        ref.setRefModel(contractrefmodel);
//        ref.setMultiSelectedEnabled(true);
//        ref.setTreeGridNodeMultiSelected(true);
//    }
	

}
