package nc.ui.eh.iso.z0502005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.iso.z0501015.IsoBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyBVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（成品到检测报告单） 
 * Edit: 张起源 
 * Date:2008-5-20 19:09:34
 */

public class ZA28TOZA30DLG extends AbstractBTOBDLG {
    public static ProcheckapplyBVO[] stockbvo = null;
    static ArrayList brr = null;
    static ArrayList crr = null;
	//getDatebase Link
	static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA28TOZA30DLG(String pkField, String pkCorp, String operator, String funNode,
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
                ProcheckapplyVO.class.getName(),
                ProcheckapplyBVO.class.getName()               
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0502505","成品检测申请");
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
        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+" and (NVL(def_5,'N')='N' or def_5='') " +
                "and (NVL(yjjc_flag,'N')='N' or yjjc_flag='') " +
                "and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='')"); //加个关闭状态标记
        String strwp = strWherePart.toString();
        return strwp;
     
    }
    
    @Override
	public String getBodyCondition() {
    	m_whereStr=null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" (NVL(def_5,'N')='N' or def_5='') and NVL(dr,0)=0 "); //def_5 为成品质量标准单上的回写标记
        String strwp = strWherePart.toString();
        return strwp;
    }
   
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 时间：2008-5-20 19:12:03
     */
    @Override
	@SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
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
            
//            if(retBillVos.length>1||retBillVos.length==0){
//                JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                return;             
//           }
//            
//            ProcheckapplyBVO[] Bvo = (ProcheckapplyBVO[]) retBillVo.getChildrenVO();
//                if(Bvo==null || Bvo.length ==0 || Bvo.length >1){
//                    JOptionPane.showMessageDialog(null,"表体只能选择一条数据或至少选择一条数据！","提示",JOptionPane.INFORMATION_MESSAGE); 
//                    return;             
//                }
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            //add by zqy 2008-5-21 10:13:31
            ArrayList list = new ArrayList();
            //把上游子表物料PK取出来
//            String pk_invbasdoc = retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc")==null?"":
//                        retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc").toString();
            
//            ProcheckapplyVO scpgdVO = (ProcheckapplyVO) retBillVo.getParentVO();
//            scpgdVO.setDef_1(pk_invbasdoc); //把上游子表物料PK放到派工单def_1中通过上下游带到下游
            
            //把上游选择的多个成品检测申请单的PK放到pk_procheckapplys中带到下游的pk_procheckapplys中
            String pk_procheckapply = null;
            if(retBillVos.length>0){
                int length = retBillVos.length;
                String[] pk_procheckapplys = new String[length];
                ProcheckapplyVO avo = new ProcheckapplyVO();
                
                for(int i=0;i<length;i++){
                    avo = (ProcheckapplyVO) retBillVos[i].getParentVO();
                    pk_procheckapply = avo.getPk_procheckapply();
                    pk_procheckapplys[i] = pk_procheckapply;
                }
                pk_procheckapply=PubTools.combinArrayToString2(pk_procheckapplys);
                retBillVo.getParentVO().setAttributeValue("pk_procheckapplys", pk_procheckapply);
            }           
            
            ArrayList arr = new ArrayList();
            brr = new ArrayList();
            crr = new ArrayList();
            int length = retBillVos.length;  
            for(int i=0;i<length;i++){
                ProcheckapplyBVO[] prbvo = (ProcheckapplyBVO[]) retBillVos[i].getChildrenVO();                
                for(int j=0;j<prbvo.length;j++){
                   StockCheckreportCVO cvo = new StockCheckreportCVO();
                   String pk_invbasdoc = prbvo[j].getPk_invbasdoc()==null?"":prbvo[j].getPk_invbasdoc().toString();
                   String pk_procheckapply_b =prbvo[j].getPk_procheckapply_b()==null?"":prbvo[j].getPk_procheckapply_b().toString();
//                   UFDouble amount = prbvo[j].getCheckamount();
                   UFDouble amount = prbvo[j].getAmount();
                   
                   arr.add(pk_invbasdoc);
                   brr.add(pk_procheckapply_b);
                   cvo.setPk_invbasdoc(pk_invbasdoc);
                   cvo.setVsourcebillid(pk_procheckapply_b);
                   cvo.setAmount(amount);
                   crr.add(cvo);
                }                           
            }
            
            
            StringBuffer alsql = new StringBuffer();
            for (int i = 0; i < arr.size(); i++) {
                alsql.append("'");
                alsql.append(arr.get(i));
                alsql.append("'");
                if ((i + 1) < arr.size()) {
                    alsql.append(",");
                } else {
                    alsql.append("");
                }
            }          
          
            
            //根据物料去查询该物料的质量标准单，def_1为最新标记
            String sql = "select b.* from eh_iso a,eh_iso_b b where a.pk_iso=b.pk_iso and a.pk_invbasdoc in ("+alsql+")" +
                    " and vbillstatus="+IBillStatus.CHECKPASS+" " +
                    " and NVL(a.def_1,'N')='Y' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 " +
                    " and (NVL(lock_flag,'N')='N' or lock_flag='') "; //增加关闭状态标记            
            try {
                ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql, new BeanListProcessor(IsoBVO.class));
                HashMap hm = new HashMap();               
                if(al!=null && al.size()>0){
                    for(int i=0;i<al.size();i++){                     
                        String pk_project = ((IsoBVO)al.get(i)).getPk_project()==null?"":((IsoBVO)al.get(i)).getPk_project().toString();
                        String anaimethod = ((IsoBVO)al.get(i)).getAnaimethod()==null?"":((IsoBVO)al.get(i)).getAnaimethod().toString();
                        String ll_ceil = ((IsoBVO)al.get(i)).getLl_ceil()==null?"":((IsoBVO)al.get(i)).getLl_ceil().toString();
                        String ll_limit = ((IsoBVO)al.get(i)).getLl_limit()==null?"":((IsoBVO)al.get(i)).getLl_limit().toString();
                        String rece_ceil = ((IsoBVO)al.get(i)).getRece_ceil()==null?"":((IsoBVO)al.get(i)).getRece_ceil().toString();
                        String rece_limit = ((IsoBVO)al.get(i)).getRece_limit()==null?"":((IsoBVO)al.get(i)).getRece_limit().toString(); 
                        //Eidt by wm 2008年8月4日11:30:27 成品不需要这个收获方式 去除
//                        Integer treatype = new Integer(((IsoBVO)al.get(i)).getTreatype()==null?"C":((IsoBVO)al.get(i)).getTreatype().toString());
                        
                        if(hm.containsKey(pk_project)){
                            ProcheckapplyBVO pbvo = (ProcheckapplyBVO) hm.get(pk_project);
                            pk_project = pbvo.getDef_1();
                            pbvo.setDef_1(pk_project);
                            
                            hm.put(pk_project, pbvo);                        
                        }else{
                            ProcheckapplyBVO pbvo = new ProcheckapplyBVO();
                            pbvo.setDef_1(pk_project);
                            pbvo.setDef_2(anaimethod);
                            pbvo.setDef_3(ll_ceil);
                            pbvo.setDef_4(ll_limit);
                            pbvo.setDef_5(rece_ceil);
                            pbvo.setDef_11(rece_limit); 
//                          Eidt by wm 2008年8月4日11:30:27 成品不需要这个收获方式 去除
//                            pbvo.setDr(treatype);
                            hm.put(pk_project, pbvo);
                            
                            list.add(pbvo);
                        }                    
                    }
                }
//                else{
//                    JOptionPane.showMessageDialog(null,"此物料没有在成品标准单中维护或此物料单据已经关闭，请核实!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                    return;
//                }                
            } catch (BusinessException e) {
                e.printStackTrace();
            }           
            if(list!=null && list.size()>0){
               stockbvo=(ProcheckapplyBVO[]) list.toArray(new ProcheckapplyBVO[list.size()]);
            }
            
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }

}