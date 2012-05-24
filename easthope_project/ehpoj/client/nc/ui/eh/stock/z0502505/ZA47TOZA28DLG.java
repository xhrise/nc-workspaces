package nc.ui.eh.stock.z0502505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0452005.ScCprkdBVO;
import nc.vo.eh.sc.h0452005.ScCprkdVO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面(成品入库单到成品检测申请)
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
public class ZA47TOZA28DLG extends AbstractBTOBDLG {
      
    public ZA47TOZA28DLG(String pkField, String pkCorp, String operator, String funNode,
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
                ScCprkdVO.class.getName(),
                ScCprkdBVO.class.getName()  
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0452005","成品入库单");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (NVL(jc_falg,'N')='N' or jc_falg='') " +
                "and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='') ");//增加关闭状态标记
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr=null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" NVL(jcflag,'N')<>'Y' and  NVL(jcflag,'N')<>'C' and NVL(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
  
	@Override
	public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            } 
            
//            if(retBillVos.length>=2){
//            	JOptionPane.showMessageDialog(null,"不能选择多表头","提示",JOptionPane.INFORMATION_MESSAGE); 
//                return;	
//            }          
            
            String[] pk_cprkds = new String[retBillVos.length];
            HashMap hm = new HashMap();
            String pk_rkd = null;//入库单的PK
            if(retBillVos.length>0){
                for(int i=0;i<retBillVos.length;i++){
                    ScCprkdVO scvo = (ScCprkdVO) retBillVos[i].getParentVO();
                    pk_rkd=scvo.getPk_rkd()==null?"":scvo.getPk_rkd().toString();
                    pk_cprkds[i]=pk_rkd;
                    String billno = scvo.getBillno();
                    hm.put(pk_rkd, billno);                    
                }
                pk_rkd=PubTools.combinArrayToString2(pk_cprkds);
                retBillVo.getParentVO().setAttributeValue("pk_cprkds", pk_rkd);//将多条入库单PK存放到pk_cprkds中带到下游pk_cprkds中
            }          
           
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                ScCprkdBVO[] sbvo = (ScCprkdBVO[]) retBillVos[i].getChildrenVO(); 
                for(int j=0;j<sbvo.length;j++){
                    ScCprkdBVO svo =sbvo[j];
                    String pk_crkd=svo.getPk_rkd()==null?"":svo.getPk_rkd().toString();
                    String pk_rkd_b = svo.getPk_rkd_b()==null?"":svo.getPk_rkd_b().toString();
                    String billno = hm.get(pk_crkd)==null?"":hm.get(pk_crkd).toString();
                    svo.setVsourcebillid(pk_rkd_b);
                    svo.setDef_1(billno);
                    list.add(sbvo[j]);
                }            
            }            
            
//            for(int i=0;i<retBillVos.length;i++){
//                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
//                for (int j = 0; j < childs.length; j++) {               	
//                    list.add(childs[j]);
//                }
//            }   
            
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);  
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}