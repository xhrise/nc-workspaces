package nc.ui.eh.stock.h0150325;

import java.awt.Container;
import java.util.HashSet;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.stock.h0150315.StockWjDecisionBillVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * 说明:五金采购决策->五金入库
 * @author wb
 * 2009-6-11 9:57:26
 */
@SuppressWarnings("serial")
public class ZB23TOZB24DLG extends AbstractBTOBDLG {
	
   
    public ZB23TOZB24DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		StockWjDecisionBillVO.class.getName(),
        		StockWjdecisionVO.class.getName(),
        		StockWjdecisionCVO.class.getName(),
        		StockWjdecisionBVO.class.getName(),        		
        		StockWjdecisionDVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150315","五金采购决策");
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
        strWherePart.append("' and vbillstatus = 1 and isnull(rk_flag,'N')<>'Y'  and isnull(lock_flag,'N')<>'Y'   and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;

    }
    @Override
    public String getBodyCondition()
    {
    	StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" isnull(rk_flag,'N')<>'Y' and isnull(dr,0)=0 ");       
		return strWherePart.toString();
    	
    }
	@Override
	@SuppressWarnings("deprecation")
	public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            //多表头的处理
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            if(retBillVos.length>1){
            	JOptionPane.showMessageDialog(null,"只能选择一个采购决策!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            StockWjdecisionVO vo =(StockWjdecisionVO) retBillVos[0].getParentVO();
            HashSet<String> hsysy = new HashSet<String>(); 
            HashSet<String> psninfo = new HashSet<String>();  
            for(int i=0;i<retBillVos.length;i++){
            	StockWjdecisionBVO[] cvos = (StockWjdecisionBVO[])retBillVos[i].getChildrenVO();
            	for (int j=0;j<cvos.length;j++){
            		StockWjdecisionBVO cvo = cvos[j];
            		String pk_cubasdoc=cvo.getPk_cubasdoc();
            		hsysy.add(pk_cubasdoc);
            		psninfo.add(cvo.getPsninfo());
            	}
            }
            if (hsysy.size()>1)
            {
            	JOptionPane.showMessageDialog(null,"只能选择同一个供应商!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            else
            {
            	Object [] hss =hsysy.toArray();                
                vo.setPk_cubasdoc((String) hss[0]);
            	if (psninfo.size()>1)
            	{
            		JOptionPane.showMessageDialog(null,"只能选择同一个散户!","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;
            	}
            	else
            	{
            		Object [] hss1 =psninfo.toArray();
            		 vo.setPsninfo((String) hss1[0]);
            	}
            }
            
        }
        
        this.getAlignmentX();
        this.closeOK();

    } 	
}