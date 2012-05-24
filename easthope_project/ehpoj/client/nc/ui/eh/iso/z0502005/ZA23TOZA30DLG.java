package nc.ui.eh.iso.z0502005;

import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.iso.z0501505.StockSampleVO;
import nc.vo.eh.iso.z0501505.StockCheckreportBVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照（抽样单到检测报告单） 
 * Edit: 张起源 
 * Date:2008-04-14
 */
public class ZA23TOZA30DLG extends AbstractBTOBDLG {
    static nc.vo.eh.iso.z0501505.StockCheckreportBVO[] stbvo = null;
    static ArrayList arr = null;
    public ZA23TOZA30DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
//        BillItem result = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
//        initComboBox(result, ICombobox.STR_RESULE,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { PubBillVO.class.getName(),
        		StockSampleVO.class.getName(),
	            StockCheckreportBVO.class.getName() 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"z0501505","抽样单");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (NVL(def_4,'N')='N' or def_4='') " +
                "and NVL(dr,0)=0 and (NVL(lock_flag,'N')='N' or lock_flag='') ");//增加关闭状态标记
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
	public String getBodyCondition() {
    	m_whereStr=null;
    	String strWherePart = null;
        return strWherePart;
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
            
            ArrayList list = new ArrayList();
            arr=new ArrayList();
            int length = retBillVos.length;
            for(int k=0;k<length;k++){
                StockSampleVO svo = (StockSampleVO) retBillVos[k].getParentVO();
                String pk_invbasdoc = svo.getPk_invbasdoc()==null?"":svo.getPk_invbasdoc().toString();
                String pk_sample = svo.getPk_sample()==null?"":svo.getPk_sample().toString();
                arr.add(pk_sample);
                StockCheckreportBVO[] sbvo = (StockCheckreportBVO[]) retBillVos[k].getChildrenVO(); 
                for(int i=0;i<sbvo.length;i++){
                    String pk_project = sbvo[i].getPk_project()==null?"":sbvo[i].getPk_project().toString();
                    String method = sbvo[i].getDef_2()==null?"":sbvo[i].getDef_2().toString();
                    //<修改>理论指标和收货指标类型修改.日期:2009-8-14.作者:张志远
                    UFDouble ll_ceil= new UFDouble(sbvo[i].getLl_ceil()==null?"":sbvo[i].getLl_ceil().toString());
                    UFDouble ll_limit=new UFDouble(sbvo[i].getLl_limit()==null?"":sbvo[i].getLl_limit().toString());
                    UFDouble rece_ceil = new UFDouble(sbvo[i].getRece_ceil()==null?"":sbvo[i].getRece_ceil().toString());
                    UFDouble rece_limit=new UFDouble(sbvo[i].getRece_limit()==null?"":sbvo[i].getRece_limit().toString());
                    
                    nc.vo.eh.iso.z0501505.StockCheckreportBVO bvo =new nc.vo.eh.iso.z0501505.StockCheckreportBVO();
                    bvo.setPk_project(pk_project);
                    bvo.setDef_2(method);
                    bvo.setLl_ceil(ll_ceil);
                    bvo.setLl_limit(ll_limit);
                    bvo.setRece_ceil(rece_ceil);
                    bvo.setRece_limit(rece_limit);
                    
                    list.add(bvo);
                }
            }
            
            if(list!=null && list.size()>0){
                stbvo=(nc.vo.eh.iso.z0501505.StockCheckreportBVO[]) 
                        list.toArray(new nc.vo.eh.iso.z0501505.StockCheckreportBVO[list.size()]);
            }          
              
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
            this.getAlignmentX();
            this.closeOK();
        }
    }   
}