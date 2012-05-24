package nc.ui.eh.cw.h1101005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.cw.h1100505.ArapFkqsBVO;
import nc.vo.eh.cw.h1100505.ArapFkqsVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（付款申请单到付款单）
 * Edit: 王明 
 * Date:2008-05-31
 */

public class ZA56TOZA57DLG extends AbstractBTOBDLG {
      
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    
    public ZA56TOZA57DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { 
        		 PubBillVO.class.getName(),
                 ArapFkqsVO.class.getName(),
                 ArapFkqsBVO.class.getName()  
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H1100505","付款申请单");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (allfk_flag='N' or allfk_flag is null) and (lock_flag='N' or lock_flag is null) " +
                " and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
   
    @Override
	public String getBodyCondition() {
    	m_whereStr = null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" (fk_flag='N' or fk_flag is null) and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    	//当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            if(retBillVos.length>1||retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
            
//            HashMap hmpk_contract=new HashMap();//key=pk_contract
//            for(int i=0;i<retBillVos.length;i++){
//            	ArapFkqsVO invos=(ArapFkqsVO) retBillVos[i].getParentVO();
//            	
//            	String pk_contract=invos.getPk_hth()==null?"":invos.getPk_hth().toString();
//            	if(!(hmpk_contract.containsKey(pk_contract)) && hmpk_contract.size()!=0 ){
//            		JOptionPane.showMessageDialog(null,"请选择同一张合同!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                    return;
//            	}
//            	else{
//            		hmpk_contract.put(pk_contract, pk_contract);
//            	}
//            }
          
            ArrayList list=new ArrayList();
            ArapFkqsVO avo = (ArapFkqsVO) retBillVo.getParentVO();
            String pk_fkqs = avo.getPk_fkqs()==null?"":avo.getPk_fkqs().toString();
//            String fksql = "select sum(isnull(fkje,0)) fkje from eh_arap_fk where vsourcebillid = '"+pk_fkqs+"' and vbillstatus="+IBillStatus.CHECKPASS+" and isnull(dr,0)=0 ";
            String sql =" select b.pk_invbasdoc,b.htbillno,b.vbillno,b.sbamount,b.kzamount,b.kjprice,b.htmount," +
                    " b.pricemny,b.rkmount,b.fkmny,b.wfkje,b.vsourcebillrowid,b.pk_fkqs_b from eh_arap_fkqs a,eh_arap_fkqs_b b " +
                    " where a.pk_fkqs=b.pk_fkqs and a.pk_fkqs='"+pk_fkqs+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 " +
                    " and a.vbillstatus="+IBillStatus.CHECKPASS+" and (b.fk_flag='N' or b.fk_flag is null) ";
            
           String sqyfsql = "select sum(isnull(fkje,0)) yfje  from  eh_arap_fk  where  vsourcebillid = '"+pk_fkqs+"' and isnull(dr,0)=0 and vbillstatus="+IBillStatus.CHECKPASS+" ";
           
            try {
            ArrayList all =(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            ArrayList yfall =(ArrayList)iUAPQueryBS.executeQuery(sqyfsql.toString(), new MapListProcessor());
            if(yfall!=null&&yfall.size()>0){
            	HashMap hm = (HashMap)yfall.get(0);
//            	UFDouble sqje=new UFDouble(hm.get("fkje")==null?"0":hm.get("fkje").toString());
            	UFDouble yfje=new UFDouble(hm.get("yfje")==null?"0":hm.get("yfje").toString());
            	avo.setDef_6(yfje);
            }
            if(all!=null && all.size()>0){
                for(int i=0;i<all.size();i++){
                    HashMap hm = (HashMap)all.get(i);
                    String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                    String htbillno=hm.get("htbillno")==null?"":hm.get("htbillno").toString();
                    String vbillno=hm.get("vbillno")==null?"":hm.get("vbillno").toString();
                    UFDouble sbamount=new UFDouble(hm.get("sbamount")==null?"0":hm.get("sbamount").toString());
                    UFDouble kzamount=new UFDouble(hm.get("kzamount")==null?"0":hm.get("kzamount").toString());
                    UFDouble kjprice=new UFDouble(hm.get("kjprice")==null?"0":hm.get("kjprice").toString());
                    UFDouble htmount=new UFDouble(hm.get("htmount")==null?"0":hm.get("htmount").toString());
                    UFDouble pricemny=new UFDouble(hm.get("pricemny")==null?"0":hm.get("pricemny").toString());
                    UFDouble rkmount=new UFDouble(hm.get("rkmount")==null?"0":hm.get("rkmount").toString());
                    UFDouble fkmny=new UFDouble(hm.get("fkmny")==null?"0":hm.get("fkmny").toString());
                    UFDouble wfkje=new UFDouble(hm.get("wfkje")==null?"0":hm.get("wfkje").toString());
                    String vsourcebillrowid=hm.get("vsourcebillrowid")==null?"0":hm.get("vsourcebillrowid").toString();
                    String pk_fkqs_b=hm.get("pk_fkqs_b")==null?"":hm.get("pk_fkqs_b").toString();
                    
                    String Sql=" select sum(isnull(b.bcfkje,0)) yfkje from eh_arap_fk a,eh_arap_fk_b b " +
                    " where a.pk_fk=b.pk_fk " +
                    " and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and a.vbillstatus="+IBillStatus.CHECKPASS+" " +
                            " and b.vsourcebillrowid='"+vsourcebillrowid+"' ";
                    
                    UFDouble yfkje =null;
                    ArrayList ar =(ArrayList)iUAPQueryBS.executeQuery(Sql.toString(), new MapListProcessor());
                    if(ar!=null && ar.size()>0){
                        for(int j=0;j<ar.size();j++){
                            HashMap HH =(HashMap)ar.get(j);
                            yfkje=new UFDouble(HH.get("yfkje")==null?"0":HH.get("yfkje").toString());
                        }
                    }else{
                        yfkje=new UFDouble(0);
                    }
                    
                    UFDouble wfkje2 = fkmny.sub(yfkje);
                    
                    ArapFkqsBVO abvo = new ArapFkqsBVO();
                    abvo.setVbillno(vbillno);
                    abvo.setHtbillno(htbillno);
                    abvo.setPk_invbasdoc(pk_invbasdoc);
                    abvo.setKzamount(kzamount);
                    abvo.setSbamount(sbamount);
                    abvo.setKjprice(kjprice);
                    abvo.setHtmount(htmount);
                    abvo.setPricemny(pricemny);
                    abvo.setRkmount(rkmount);
                    abvo.setFkmny(fkmny);
                    abvo.setYfkje(yfkje);
                    abvo.setWfkje(wfkje2);
                    abvo.setVsourcebillrowid(vsourcebillrowid);
                    abvo.setVsourcebillid(pk_fkqs_b);
                    
                    list.add(abvo);                    
                }               
            }            
        } catch (BusinessException e) {
            e.printStackTrace();
        }
         
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
            retBillVo.setParentVO(avo);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}