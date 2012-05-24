package nc.ui.eh.stock.z0250505;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0151001.StockReceiptBVO;
import nc.vo.eh.stock.z0151001.StockReceiptVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:�����ε��ݽ���(�ջ�֪ͨ������ⵥ)
 * @author ���� 
 * 2008-4-18 ����04:02:28
 */
public class ZA20TOZA22DLG extends AbstractBTOBDLG {

	public ZA20TOZA22DLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);

		BillItem billItem = getbillListPanel().getBillListData().getHeadItem(
				"vbillstatus");
		initComboBox(billItem, IBillStatus.strStateRemark, true);
	}

	@Override
	public String[] getBillVos() {
		return new String[] {
				PubBillVO.class.getName(),
				StockReceiptVO.class.getName(),
				StockReceiptBVO.class.getName() 
				};
	}

	@Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0151001","�ջ�֪ͨ��");
			queryCondition.hideNormal();
		}
		return queryCondition;
	}

	@Override
	public String getHeadCondition() {	
		StringBuffer strWherePart = new StringBuffer();
		// ����ҵ�����ͣ���˾���룬����״̬��ѯ
		strWherePart.append(" pk_corp = '");
		strWherePart.append(getPkCorp());
		strWherePart.append("'  and vbillstatus="+IBillStatus.CHECKPASS+"  and (isnull(lock_flag,'N')='N' or lock_flag='') and  (isnull(rk2_flag,'N')='N' or rk2_flag='') and (allcheck=1 or allcheck=2) and (yjsb_flag=1 or yjsb_flag=2) and (isnull(isallx,'Y')='Y' )");
		String strwp = strWherePart.toString();
		return strwp;
	}
	@Override
	public String getBodyCondition() {
		m_whereStr=null;
		StringBuffer strWherePart = new StringBuffer();
		// ����ҵ�����ͣ���˾���룬����״̬��ѯ
		strWherePart.append(" isnull(allcheck,'N')='N' and isnull(issb,'N')='N' and isnull(rk_flag,'N')='N' ");
		String strwp = strWherePart.toString();
		return strwp;
	}
	

	/**
	 * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
	 * 
	 * @author ���� 2007-11-1 ����03:14:05
	 * @throws BusinessException 
	 */
	@Override
	public void onOk(){
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(
			getbillListPanel().getHeadTable().getSelectedRow()));
			retBillVo = getbillListPanel().getSelectedVO(m_billVo,m_billHeadVo, m_billBodyVo);
			retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo,m_billHeadVo, m_billBodyVo);
		//�ж��ǲ���ͬһ���ͻ�
			
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "������ѡ��һ������!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(retBillVos.length>1){
				JOptionPane.showMessageDialog(null, "���ܶ����ջ�֪ͨ��ͬʱ���!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			 ArrayList list = new ArrayList();
             for(int i=0;i<retBillVos.length;i++){
                 CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                 for (int j = 0; j < childs.length; j++) {
                	 StockReceiptBVO bvo = (StockReceiptBVO)childs[j];
                	 UFDouble inamount = new UFDouble(bvo.getInamount()==null?"":bvo.getInamount().toString());
                	 UFDouble taxinprice=new UFDouble(bvo.getTaxinprice()==null?"":bvo.getTaxinprice().toString());
                	 UFDouble sumpackagweight=new UFDouble(bvo.getSumpackagweight()==null?"":bvo.getSumpackagweight().toString()).div(new UFDouble(1000),3);
                	 UFDouble suultamount=inamount.sub(sumpackagweight);
                	 UFDouble mon=suultamount.multiply(taxinprice);
                	 bvo.setInamount(suultamount);
                	 bvo.setDef_6(mon);
                	 bvo.setSumpackagweight(sumpackagweight);
                 	 list.add(childs[j]);
                 }
             }   
             SuperVO[] child=new SuperVO[list.size()] ;
             child = (SuperVO[])list.toArray(child);
             retBillVo.setChildrenVO(child);
         }
		
			this.getAlignmentX();
			this.closeOK();

		}
	}

	
