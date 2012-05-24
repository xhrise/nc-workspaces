package nc.ui.eh.sc.h0451505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0451005.ScPosmBVO;
import nc.vo.eh.sc.h0451005.ScPosmVO;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:派工单上下游界面(由生产任务单生成)
 * @author 王兵
 * 2008-5-7 19:09:31
 */
public class ZA44TOZA43DLG extends AbstractBTOBDLG {
    
	public static  ScPgdBVO[] pgdBVOs = null;
	public ZA44TOZA43DLG(String pkField, String pkCorp, String operator,
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
				ScPosmVO.class.getName(),
	            ScPosmBVO.class.getName()  
				};
	}

	@Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0451005","生产任务单");
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
		strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(pg_flag,'N')='N' or pg_flag=''  ) " +
                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') ");//增加关闭状态标记
		String strwp = strWherePart.toString();
		return strwp;
	}
	@Override
	public String getBodyCondition() {
		m_whereStr=null;
		StringBuffer strWherePart = new StringBuffer();
		strWherePart.append(" (isnull(pg_flag,'N')='N' or pg_flag='' ) ");
		String strwp = strWherePart.toString();
		return strwp;
	}

	/**
	 * 功能:判断上游多行明细是否来自与同一个客户
	 * 
	 * @author 王明 2007-11-1 下午03:14:05
	 */
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(
			getbillListPanel().getHeadTable().getSelectedRow()));
			retBillVo = getbillListPanel().getSelectedVO(m_billVo,m_billHeadVo, m_billBodyVo);
			retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo,m_billHeadVo, m_billBodyVo);
			//判断是否选择
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "请至少选择一行数据!", "提示",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			HashMap hm=new HashMap();
			ArrayList posmArr = new ArrayList();					//存放生产任务单的主表主键
            ArrayList list = new ArrayList();                                   // 此处由于下游单据是多页签，数据无法直接直接带到下游,故通过 vo 传到 ui 中 
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
           
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                ScPosmVO hvo = (ScPosmVO)retBillVos[i].getParentVO();
                String pk_posm = hvo.getPk_posm();
                posmArr.add(pk_posm);
                String posmsql = "select billno from eh_sc_posm where pk_posm = '"+pk_posm+"'";
                String billno = null;
                try {
					Object billnoObj = iUAPQueryBS.executeQuery(posmsql, new ColumnProcessor());
					billno = billnoObj==null?"":billnoObj.toString();
				} catch (BusinessException e) {
					e.printStackTrace();
				}
                for (int j = 0; j < childs.length; j++) {
                	ScPgdBVO pgdBVO = new ScPgdBVO();
                    ScPosmBVO posmBVO = (ScPosmBVO)childs[j];
                    String pk_posm_b = posmBVO.getPk_posm_b()==null?"":posmBVO.getPk_posm_b();			//生产任务单子表pk
                    pgdBVO.setVsourcebillrowid("'"+pk_posm_b+"'");
                    pgdBVO.setVsourcebillid("'"+pk_posm+"'");
                    pgdBVO.setPk_posm(billno);
                    //判断物料是否相同
                    String pk_invbasdoc= posmBVO.getPk_invbasdoc()==null?"0":posmBVO.getPk_invbasdoc().toString();
                    UFDouble newamount = new UFDouble(posmBVO.getScmount()==null?"0":posmBVO.getScmount().toString());
                    pgdBVO.setPk_invbasdoc(posmBVO.getPk_invbasdoc());
                    pgdBVO.setPk_unit(posmBVO.getPk_unit());
                    pgdBVO.setHjinvbasdoc(posmBVO.getHjinvbasdoc());
                    pgdBVO.setVer(posmBVO.getVer());
                    pgdBVO.setXh(posmBVO.getXh());
                    pgdBVO.setScmount(posmBVO.getScmount());								//生产数量
                    pgdBVO.setYpgamount(posmBVO.getPgamount()==null?new UFDouble(0):posmBVO.getPgamount());	//已派工数量
                    pgdBVO.setPgamount(posmBVO.getScmount().sub(pgdBVO.getYpgamount()));	//本次派工数量
                    pgdBVO.setMemo(posmBVO.getMemo());
                    if(hm.containsKey(pk_invbasdoc)){
                    	ScPgdBVO oldbvo=(ScPgdBVO) hm.get(pk_invbasdoc);
                    	UFDouble oldamount = new UFDouble(oldbvo.getScmount()==null?"0":oldbvo.getScmount().toString());
                    	UFDouble amount=oldamount.add(newamount);
                    	oldbvo.setScmount(amount);													//生产数量
                    	UFDouble ypgamount = oldbvo.getYpgamount().add(posmBVO.getPgamount()==null?new UFDouble(0):posmBVO.getPgamount());		//已派工数量
                    	oldbvo.setYpgamount(ypgamount);									
                    	oldbvo.setPgamount(amount.sub(ypgamount));									//本次派工数量
                    	String pk_posm_bs = "'"+pk_posm_b+"',"+oldbvo.getVsourcebillrowid();
                 		String pk_posms = "'"+pk_posm+"',"+oldbvo.getVsourcebillid();
                 		String billnos = billno+","+oldbvo.getPk_posm();
                 		oldbvo.setVsourcebillid(pk_posms);
                 		oldbvo.setVsourcebillrowid(pk_posm_bs);
                 		oldbvo.setPk_posm(billnos);
                 		if(posmBVO.getVer().intValue()>oldbvo.getVer().intValue()){			//当两个物料相同时取最新的BOM版本
                 			oldbvo.setVer(posmBVO.getVer());
                 		}
//                 		if(posmBVO.getXh().intValue()>oldbvo.getXh().intValue()){			//当两个物料生产顺序一样时
//                 			oldbvo.setXh(posmBVO.getXh());
//                 		}
                    	hm.put(pk_invbasdoc, oldbvo);
                    }else{
                    	hm.put(pk_invbasdoc, pgdBVO);
                    }
                }
            } 
            String[] pk_posms = (String[])posmArr.toArray(new String[posmArr.size()]);
            String pk_posmss = PubTools.combinArrayToString2(pk_posms);
            if(pk_posmss!=null&&pk_posmss.length()>2500){
            	JOptionPane.showMessageDialog(null, "所选生产任务单记录太多,不允许超过100条记录!", "提示",JOptionPane.INFORMATION_MESSAGE);
				return;
            }
            retBillVo.getParentVO().setAttributeValue("pk_posm", pk_posmss);
            
            Object[] keyset=hm.keySet().toArray();
            for(int i=0;i<keyset.length;i++){
            	list.add(hm.get(keyset[i].toString()));
            }
            pgdBVOs=new ScPgdBVO[list.size()] ;
            pgdBVOs = (ScPgdBVO[])list.toArray(pgdBVOs);
            
            
            SuperVO[] child = new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
//            retBillVo.setChildrenVO(child);
			this.getAlignmentX();
			this.closeOK();

		}
	}

}