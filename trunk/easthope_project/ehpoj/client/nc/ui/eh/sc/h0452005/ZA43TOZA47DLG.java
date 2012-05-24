package nc.ui.eh.sc.h0452005;

import java.awt.Container;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.eh.sc.h0451505.ScPgdBillVO;
import nc.vo.eh.sc.h0451505.ScPgdPsnVO;
import nc.vo.eh.sc.h0451505.ScPgdVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * 说明:上下游单据界面
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
public class ZA43TOZA47DLG extends AbstractBTOBDLG {
	
      
    public ZA43TOZA47DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		ScPgdBillVO.class.getName(),
        		ScPgdVO.class.getName(),
        		ScPgdBVO.class.getName(),
        		ScPgdPsnVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0451505","派工单");
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
        strWherePart.append("'  and (isnull(rk_flag,'N')='N' or rk_flag='') and (isnull(lock_flag,'N')='N' or lock_flag='')  and  isnull(xdflag,'Y')='Y'  and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;

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
            if(retBillVos.length>22){
            	JOptionPane.showMessageDialog(null,"表头最多只可以选22条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            //把派工单相同的物料累加到一起
            HashMap hm=new HashMap();
            String [] scpg=new String[retBillVos.length];
            String [] scbillno = new String[retBillVos.length];//单据BILLNO。2010-01-11
            for(int i=0;i<retBillVos.length;i++){
            	ScPgdBVO[] scpgds=(ScPgdBVO[]) retBillVos[i].getChildrenVO();
            	
            	ScPgdVO scpgdps=(ScPgdVO) retBillVos[i].getParentVO();
            	scpg[i]=scpgdps.getPk_pgd()==null?"":scpgdps.getPk_pgd().toString();
            	scbillno[i] = scpgdps.getBillno()==null?"":scpgdps.getBillno().toString();//单据BILLNO
            	
            	for(int j=0;j<scpgds.length;j++){
            		ScPgdBVO scpgd=scpgds[j];
            		String pk_invbasdoc=scpgd.getPk_invbasdoc();
            		UFDouble amount=scpgd.getPgamount()==null?new UFDouble():scpgd.getPgamount();
            		if(hm.containsKey(pk_invbasdoc)){
            			ScPgdBVO vo=(ScPgdBVO) hm.get(pk_invbasdoc);
            			UFDouble oldamount=vo.getPgamount()==null?new UFDouble(0):vo.getPgamount();
            			UFDouble newamount=oldamount.add(amount);
            			vo.setPgamount(newamount);
            			hm.put(pk_invbasdoc, vo);
            		}else{
            			hm.put(pk_invbasdoc, scpgd);
            		}
            	}
            }
            String pk_pgd=Toolkits.combinArrayToString(scpg);
            String billno = getStr(scbillno);//2010-01-11
        	ScPgdBVO[] bvo=new ScPgdBVO[hm.size()];
        	Object[] keyset=hm.keySet().toArray();
        	for(int j=0;j<hm.size();j++){
        		bvo[j]=(ScPgdBVO) hm.get(keyset[j]);
        	}
        	retBillVo.setChildrenVO(bvo);
        	ScPgdVO vo=(ScPgdVO) retBillVo.getParentVO();
        	vo.setPk_pgd(pk_pgd);
        	vo.setFrombillno(billno);//2010-01-11
        	retBillVo.setParentVO(vo);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
	
	public String getStr(String[] arr){
		int length = arr.length;
		String bill = null;
		for(int i=0;i<length;i++){
			
			if(i>0){
				bill = bill+","+arr[i];
			}else{
				bill = arr[i];
			}
		}
		return bill;
	}
}