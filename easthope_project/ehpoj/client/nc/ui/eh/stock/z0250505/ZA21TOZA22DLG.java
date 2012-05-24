package nc.ui.eh.stock.z0250505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502515.StockBackBVO;
import nc.vo.eh.stock.z0502515.StockBackVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:退货通知单
 * @author 王明 
 * 2008年11月30日15:49:30
 */
public class ZA21TOZA22DLG extends AbstractBTOBDLG {

	public ZA21TOZA22DLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);
	}

	@Override
	public String[] getBillVos() {
		return new String[] {
				PubBillVO.class.getName(),
                StockBackVO.class.getName(),
                StockBackBVO.class.getName()
				};
	}


	@Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0155005","退货通知单");
			queryCondition.hideNormal();
		}
		return queryCondition;
	}

	@Override
	public String getHeadCondition() {	
		StringBuffer strWherePart = new StringBuffer();
		// 根据业务类型，公司编码，单据状态查询
		strWherePart.append(" pk_corp = '");
		strWherePart.append(getPkCorp());
		strWherePart.append("'");
		strWherePart.append("  and (isnull(lock_flag,'N')='N' or lock_flag='') and  (isnull(rk_flag,'N')='N' or rk_flag='') " +
                " and vbillstatus="+IBillStatus.CHECKPASS+" and isrk='Y' ");
		String strwp = strWherePart.toString();
		return strwp;
	}


	/**
	 * 功能:判断上游多行明细是否来自与同一个客户
	 * 
	 * @author 王明 2007-11-1 下午03:14:05
	 * @throws BusinessException 
	 */
	@Override
	public void onOk(){
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(
			getbillListPanel().getHeadTable().getSelectedRow()));
			retBillVo = getbillListPanel().getSelectedVO(m_billVo,m_billHeadVo, m_billBodyVo);
			retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo,m_billHeadVo, m_billBodyVo);
			//IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//判断是否选择
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String pk_in = retBillVo.getParentVO().getAttributeValue("pk_in")==null?"":retBillVo.getParentVO().getAttributeValue("pk_in").toString();
		   
			StringBuffer sql = new StringBuffer()
			.append("SELECT m.pk_contract FROM eh_stock_in m WHERE m.pk_in = '"+pk_in+"' AND NVL(m.dr,0)=0 ");
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String pk_contract = null;
			try {
				ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new  MapListProcessor());
				if(arr !=null&&arr.size()>0){
					for(int i=0; i<arr.size(); i++){
						HashMap hm = (HashMap)arr.get(i);
						pk_contract = hm.get("pk_contract")==null?"":hm.get("pk_contract").toString();//采购合同号
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			//retBillVo.getParentVO().setAttributeValue("def_1", pk_contract);//将采购合同号用DEF_1传递给退货入库
			StockBackVO sbvo = (StockBackVO)retBillVo.getParentVO();
			sbvo.setDef_1(pk_contract);
			
			ArrayList<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
            for(int i=0;i<retBillVos.length;i++){
            CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
	            for (int j = 0; j < childs.length; j++) {
	                list.add(childs[j]);
	            }
            }
            CircularlyAccessibleValueObject[] child=new CircularlyAccessibleValueObject[list.size()] ;
            child = list.toArray(child);
            retBillVo.setParentVO(sbvo);
            retBillVo.setChildrenVO(child);
			}
			this.getAlignmentX();
			this.closeOK();
	}

	
}