package nc.ui.eh.stock.z0502515;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
public class ZA30TOZA21DLG extends AbstractBTOBDLG {
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA30TOZA21DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
        BillItem result = getbillListPanel().getBillListData().getHeadItem("resulst");
        BillItem resultbody = getbillListPanel().getBillListData().getBodyItem("result");
        initComboBox(result, ICombobox.STR_RESULE,true);
        initComboBox(resultbody, ICombobox.STR_PASS_FLAG,true);
    }

    public String[] getBillVos(){
        return new String[] { 
                PubBillVO.class.getName(),
                StockCheckreportVO.class.getName(),
                StockCheckreportBVO.class.getName()  
               };
    }
    
    protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0502005","检测报告");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and resulst = '1' " +
                "and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='') and (NVL(def_4,'Y')='Y' or def_4='') " +
                " and vsourcebilltype ='"+IBillType.eh_z0501505 +"' and NVL(tc_flag,'N')='N' and th_flag in ('R','S')");		//不合格并且不是特采 eidt by wb at 2008-10-31 11:53:02 R/S 从入库单做退货，或者从司磅单，收货通知单
        String strwp = strWherePart.toString();
        return strwp;
    }

    @SuppressWarnings("deprecation")
    public void onOk() {
        //当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVo.getParentVO().setAttributeValue("vbilltype", IBillType.eh_z0501505);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            if(retBillVos.length>1||retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
            
            ArrayList list=new ArrayList();
            
            String billno = retBillVo.getParentVO().getAttributeValue("billno")==null?"":
                            retBillVo.getParentVO().getAttributeValue("billno").toString(); //上游单据号    
            String pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("def_2")==null?"":
                retBillVo.getParentVO().getAttributeValue("def_2").toString(); //上游客户管理档案主键
            String pk_checkreport = retBillVo.getParentVO().getAttributeValue("pk_checkreport")==null?"":
                retBillVo.getParentVO().getAttributeValue("pk_checkreport").toString(); //上游单据的主键
            String vsourcebillid = retBillVo.getParentVO().getAttributeValue("vsourcebillid")==null?"":
                retBillVo.getParentVO().getAttributeValue("vsourcebillid").toString(); //上游单据的来源主键
            
            String Sql = " select vsourcebillid from eh_stock_sample where pk_sample='"+vsourcebillid+"' " +
                    " and (NVL(lock_flag,'N')='N' or lock_flag='' ) and NVL(dr,0)=0 ";
            String pk_receipt = null;
            try {
                ArrayList alll = (ArrayList) iUAPQueryBS.executeQuery(Sql.toString(), new MapListProcessor());
                if(alll!=null && alll.size()>0){
                    for(int i=0;i<alll.size();i++){
                        HashMap hm = (HashMap) alll.get(i);
                        pk_receipt = hm.get("vsourcebillid")==null?"":hm.get("vsourcebillid").toString();
                    }
                }
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }
            
            StringBuffer SQL2 = new StringBuffer()
            .append(" select pk_sbbills,pk_receipt_b from eh_stock_checkreport where pk_checkreport='"+pk_checkreport+"'" +
                    " and NVL(dr,0)=0 and vsourcebilltype ='"+IBillType.eh_z0501505 +"' ");
            String pk_sbbills = null;
            String pk_receipt_b = null;
            try {
                ArrayList al2 = (ArrayList) iUAPQueryBS.executeQuery(SQL2.toString(), new MapListProcessor());
                if(al2!=null && al2.size()>0){
                    for(int i=0;i<al2.size();i++){
                        HashMap hm = (HashMap) al2.get(i);
                        pk_sbbills = hm.get("pk_sbbills")==null?"":hm.get("pk_sbbills").toString();
                        pk_receipt_b = hm.get("pk_receipt_b")==null?"":hm.get("pk_receipt_b").toString();
                    }
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }    
    
            
            UFDouble sumsuttle = null;
            UFDouble emptyload = null;
            UFDouble fullload = null;
            if(!pk_sbbills.equals("") && !pk_receipt_b.equals("")){
                String SQL3 = " select sum(NVL(emptyload,0)) emptyload ,sum(NVL(fullload,0)) fullload,sum(NVL(sumsuttle,0)) sumsuttle from eh_sbbill where pk_sbbill in ("+pk_sbbills+") " +
                        " and (NVL(close_flag,'N')='N'  or close_flag='' ) ";               
                try {
                    ArrayList al3 = (ArrayList)iUAPQueryBS.executeQuery(SQL3.toString(), new MapListProcessor());
                    if(al3!=null && al3.size()>0){
                        for(int i=0;i<al3.size();i++){
                            HashMap hm2 = (HashMap)al3.get(i);
                            sumsuttle = (new UFDouble(hm2.get("sumsuttle")==null?"":hm2.get("sumsuttle").toString())).div(1000);
                            emptyload = (new UFDouble(hm2.get("emptyload")==null?"":hm2.get("emptyload").toString())).div(1000);
                            fullload = (new UFDouble(hm2.get("fullload")==null?"":hm2.get("fullload").toString())).div(1000);
                            if(sumsuttle==null||sumsuttle.equals(new UFDouble(0))||emptyload.equals(new UFDouble(0))||fullload.equals(new UFDouble(0))){
                            	JOptionPane.showMessageDialog(null,"司磅单没有净重,不能入库!","提示",JOptionPane.INFORMATION_MESSAGE); 
                                return; 
                            }
                        }
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
            else{
                if(!pk_receipt_b.equals("")&&pk_sbbills.equals("")){
                    String SQL4 = " select rkamount sumsuttle from eh_stock_checkreport where pk_checkreport='"+pk_checkreport+"' " +
                            " and (NVL(lock_flag,'N')='N' or lock_flag='') and NVL(dr,0) =0 ";
                    try {
                        ArrayList al4 = (ArrayList)iUAPQueryBS.executeQuery(SQL4.toString(), new MapListProcessor());
                        if(al4!=null && al4.size()>0){
                            for(int i=0;i<al4.size();i++){
                                HashMap hm3 = (HashMap)al4.get(i);
                                sumsuttle = new UFDouble(hm3.get("sumsuttle")==null?"":hm3.get("sumsuttle").toString());
                            }
                        }        
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            
            String sql = " select pk_invbasdoc from eh_stock_checkreport where billno='"+billno+"' and NVL(dr,0)= 0 " +
                    " and (NVL(lock_flag,'N')='N' or lock_flag='')";
            try {
                ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                if(all!=null && all.size()>0){
                    for(int i=0;i<all.size();i++){
                        HashMap hm = (HashMap) all.get(i);
                        String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                        String sql1=" select taxinprice from eh_stock_receipt_b where pk_receipt_b='"+pk_receipt_b+"'  and NVL(dr,0)=0 ";
                        ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql1.toString(), new MapListProcessor());
                        UFDouble price = null;
                        if(al!=null && al.size()>0){
                            for(int j=0;j<al.size();j++){
                                HashMap hm2 = (HashMap) al.get(j);
                                price = new UFDouble(hm2.get("taxinprice")==null?"0":hm2.get("taxinprice").toString());
                            }
                        }
                        
                        StockCheckreportBVO sbvo = new StockCheckreportBVO();
                        sbvo.setDef_1(pk_invbasdoc);  
                        sbvo.setDef_10(price);
                        sbvo.setDef_11(new UFDouble(0.17));
                        sbvo.setDef_6(sumsuttle);
                        list.add(sbvo);
                    }                    
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            String pk_psndoc = null; //采购员
            String carnumber = null; //车号
            String tranno = null; //车皮号
            try {
                String SQL1 = " select pk_psndoc ,carnumber ,tranno from eh_stock_receipt where pk_cubasdoc='"+pk_cubasdoc+"'" +
                        " and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='') ";
                ArrayList all2 = (ArrayList) iUAPQueryBS.executeQuery(SQL1.toString(), new MapListProcessor()); 
                if(all2!=null && all2.size()>0){
                    for(int L=0;L<all2.size();L++){
                        HashMap hm3 = (HashMap)all2.get(L);
                        pk_psndoc = hm3.get("pk_psndoc")==null?"":hm3.get("pk_psndoc").toString();
                        carnumber = hm3.get("carnumber")==null?"":hm3.get("carnumber").toString();
                        tranno = hm3.get("tranno")==null?"":hm3.get("tranno").toString();
                    }
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            StockCheckreportVO svo = (StockCheckreportVO) retBillVo.getParentVO();
            svo.setDef_1(pk_cubasdoc);
            svo.setDef_2(pk_psndoc);
//            svo.setDef_3(carnumber);
//            svo.setDef_4(tranno);
            svo.setDef_5("ZA30");
            svo.setDef_7(pk_checkreport);
            
  
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setParentVO(svo);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    } 
}