package nc.ui.eh.stock.z0250505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:上下游单据界面(司磅到入库单)
 * @author 王明 
 * 2008-4-18 下午04:02:28
 */
public class ZA18TOZA22DLG extends AbstractBTOBDLG {

	public ZA18TOZA22DLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);

		BillItem billItem = getbillListPanel().getBillListData().getHeadItem(
				"vbillstatus");
		initComboBox(billItem, IBillStatus.strStateRemark, true);
		BillItem sbtype = getbillListPanel().getBillListData().getHeadItem("sbtype");
	    initComboBox(sbtype, ICombobox.STR_SBBILLTYPE,true);
	}

	@Override
	public String[] getBillVos() {
		return new String[] {
				PubBillVO.class.getName(),
				SbbillVO.class.getName(),
				SbbillBVO.class.getName() 
				};
	}

	@Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0151001","收货通知单");
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
		strWherePart.append("'  and sbtype=1 and isnull(close_flag,'N')<>'Y' and isnull(ycy_flag,'N')='N' and isnull(ischeck,'N')='N' and fullload is not null and emptyload is not null and vbillstatus=1 ");
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
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//判读是不是同一个客户
			
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "请至少选择一行数据!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(retBillVos.length>1){
				JOptionPane.showMessageDialog(null, "不能多张司磅单同时入库!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			
			SbbillVO vo=(SbbillVO) retBillVo.getParentVO();
			String vourcebillrowid=vo.getVsourcebillrowid()==null?"":vo.getVsourcebillrowid().toString();
			String sql="select vsourcebillrowid from eh_stock_receipt_b where pk_receipt_b='"+vourcebillrowid+"' ";
			try {
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(0);
					String vsourcebillrowid=hm.get("vsourcebillrowid")==null?"":hm.get("vsourcebillrowid").toString();
					vo.setVsourcebillid(vsourcebillrowid);
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			
			UFDouble Sumsuttle=new UFDouble(vo.getSumsuttle()==null?"0":vo.getSumsuttle().toString()).div(new UFDouble(1000));//净重
			UFDouble Bzkz=new UFDouble(vo.getBzkz()==null?"":vo.getBzkz().toString()).div(new UFDouble(1000),3);//包装扣重
			vo.setSumsuttle(Sumsuttle);
			vo.setBzkz(Bzkz);
			UFDouble taxinprice=new UFDouble(0);//价格
			String pk_receipt_b=vo.getVsourcebillrowid()==null?"":vo.getVsourcebillrowid().toString();
			String sql2="select taxinprice from eh_stock_receipt_b where pk_receipt_b='"+pk_receipt_b+"'";
			try {
				ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(i);
					taxinprice=new UFDouble(hm.get("taxinprice")==null?"0":hm.get("taxinprice").toString());
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			vo.setDef_6(taxinprice);
			vo.setDef_1(taxinprice.toString());
			vo.setDef_7(new UFDouble(10000));
			
			
			
//			SbbillBVO[] childs=(SbbillBVO[]) retBillVos[0].getChildrenVO();
			
//			 ArrayList list = new ArrayList();
//             for(int i=0;i<retBillVos.length;i++){
//                 CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
//                 for (int j = 0; j < childs.length; j++) {
//                 	 list.add(childs[j]);
//                 }
//             }   
//             SuperVO[] child=new SuperVO[list.size()] ;
//             child = (SuperVO[])list.toArray(child);
//             retBillVo.setChildrenVO(child);
             retBillVo.setParentVO(vo);
         }
		
			this.getAlignmentX();
			this.closeOK();

		}
	}

	
