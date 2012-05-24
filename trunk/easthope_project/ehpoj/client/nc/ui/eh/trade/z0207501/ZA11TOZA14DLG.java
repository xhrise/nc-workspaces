package nc.ui.eh.trade.z0207501;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ���
 * @author ����
 * 2007-9-29 ����04:02:28
 */
public class ZA11TOZA14DLG extends AbstractBTOBDLG {
	
      
    public ZA11TOZA14DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		IcoutVO.class.getName(),
        		IcoutBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0255001","���ⵥ");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' ");
        strWherePart.append(" and vbillstatus=1");
        strWherePart.append(" and isnull(used_flag,'N')='N'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author honghai
     * 2007-11-1 ����03:14:05
     */
    
	@Override
	@SuppressWarnings("deprecation")
	public void onOk() {

        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            IcoutVO icout=new IcoutVO();
            icout=(IcoutVO) retBillVo.getParentVO();
            icout.setCarnumber("ZA11");
            
            try {
            	icout.setBillno(BillcodeRuleBO_Client.getBillCode(ClientUI.billTyped,ClientUI.corp, null,ClientUI.objVOd));
			} catch (Exception e) {
				e.printStackTrace();
			}
            
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }    
            
//            if(retBillVos.length>1||retBillVos.length==0){
//            	 JOptionPane.showMessageDialog(null,"ֻ����һ�ų��ⵥ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
//                 return;          	
//            }
            
            
            String [] pk_icouts=new String [retBillVos.length];
            HashMap hmbillno=new HashMap();//���ݺ�
            HashMap hm=new HashMap();
            for(int i=0;i<retBillVos.length;i++){
            	IcoutVO vo=(IcoutVO) retBillVos[i].getParentVO();
            	String pk_cubasdoc=vo.getPk_cubasdoc()==null?"":vo.getPk_cubasdoc().toString();
            	String pk_icout=vo.getPk_icout()==null?"":vo.getPk_icout().toString();
            	String billno=vo.getBillno()==null?"":vo.getBillno().toString();
            	hmbillno.put(pk_icout, billno);
            	pk_icouts[i]=pk_icout;
            	hm.put(pk_cubasdoc, pk_cubasdoc);
            }
            if(hm.size()>=2){
            	 JOptionPane.showMessageDialog(null,"��ѡ��ͬһ�ͻ�����Ʊ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
            	 return;          	
            }
            String pk_icout=PubTool.combinArrayToString(pk_icouts); 
            
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {               	
                    IcoutBVO icVO = (IcoutBVO)childs[j];
                    String pk_icoutb=icVO.getPk_icout()==null?"":icVO.getPk_icout().toString();
                    String billno=hmbillno.get(pk_icoutb)==null?"":hmbillno.get(pk_icoutb).toString();
                    icVO.setDef_4(billno);
                	list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
            
            IcoutVO VO=(IcoutVO) retBillVo.getParentVO();
            VO.setPk_icout(pk_icout);
            retBillVo.setParentVO(VO);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}