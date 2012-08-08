package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;

import nc.itf.ps.settle.ISettle;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.uap.sf.ICreateCorpQueryService;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.CreditConst;
import nc.vo.so.credit.SOCreditPara;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;

/**
 * 备注：销售发票的弃审 单据动作执行中的动态执行类的动态执行类。
 * 
 * 创建日期：(2005-6-18)
 * 
 * @author：平台脚本生成
 */
public class N_32_SoUnApprove extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	/**
	 * N_32_SoUnApprove 构造子注解。
	 */
	public N_32_SoUnApprove() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			// *************从平台取得由该动作传入的入口参数。***********
			Object inObject = getVo();
			// 1,首先检查传入参数类型是否合法，是否为空。
			if (!(inObject instanceof nc.vo.so.so002.SaleinvoiceVO))
				throw new java.rmi.RemoteException("Remote Call", new nc.vo.pub.BusinessException("错误：您希望保存的销售发票类型不匹配"));
			if (inObject == null)
				throw new java.rmi.RemoteException("Remote Call", new nc.vo.pub.BusinessException("错误：您希望保存的销售发票没有数据"));
			// 2,数据合法，把数据转换。
			nc.vo.so.so002.SaleinvoiceVO inVO = (nc.vo.so.so002.SaleinvoiceVO) inObject;
			SaleinvoiceBVO[] salebody = (SaleinvoiceBVO[]) inVO.getChildrenVO();
			int ilength = salebody.length;
			String pk_bill = ((nc.vo.so.so002.SaleVO) inVO.getParentVO()).getCsaleid();
			String billtype = ((nc.vo.so.so002.SaleVO) inVO.getParentVO()).getCreceipttype();
			inObject = null;
			// **************************************************************************************************
			setParameter("INVO", inVO);
			setParameter("PKBILL", pk_bill);
			setParameter("BILLTYPE", billtype);
			// **************************************************************************************************
			Object retObj = null;
			// 方法说明:加锁
			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null) {
				m_methodReturnHas.put("lockPkForVo", retObj);
			}
      inVO = (nc.vo.so.so002.SaleinvoiceVO) runClass("nc.impl.scm.so.so002.SaleinvoiceDMO",
          "fillOrignInvoice", "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
      setParameter("INVO", inVO);
			// ##################################################
			try {
				// ####重要说明：生成的业务组件方法尽量不要进行修改####
				// 方法说明:并发检查
				runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("checkVoNoChanged", retObj);
				}
				// ##################################################
				// ####重要说明：生成的业务组件方法尽量不要进行修改####
				// 方法说明:并发互斥检查
				runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isUnApproveStatus",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("isUnApproveStatus", retObj);
				}

				ICreateCorpQueryService icorp = (ICreateCorpQueryService) NCLocator.getInstance().lookup(
						ICreateCorpQueryService.class.getName());
				Hashtable ht = icorp.queryProductEnabled(inVO.getPk_corp(), new String[] { "SO6" });
				boolean bEnableSO6 = ((UFBoolean) ht.get("SO6")).booleanValue();
				/** 更新三个应收 begin* */
				IBillInvokeCreditManager creditManager = null;
				SOCreditPara para = null;
				if (bEnableSO6) {
					String[] sourcehid = new String[ilength];
					String[] biztype = new String[ilength];
					String curbiztype = ((SaleVO) inVO.getParentVO()).getCbiztype();
					String[] sourcebids = new String[ilength];
					for (int i = 0; i < ilength; i++) {
						sourcehid[i] = salebody[i].getCupsourcebillid();
						biztype[i] = curbiztype;
						sourcebids[i] = salebody[i].getCupsourcebillbodyid();
					}
					String sourcebilltype = inVO.getItemVOs()[0].getCupreceipttype();
					int iSourceBillType = -1;
					if ("30".equals(sourcebilltype)) {
						iSourceBillType = CreditConst.ICREDIT_BILL_ORDER;
					} else if ("4C".equalsIgnoreCase(sourcebilltype)) {
						iSourceBillType = CreditConst.ICREDIT_BILL_OUTGENERAL;
					}
					creditManager = (IBillInvokeCreditManager) nc.bs.framework.common.NCLocator.getInstance().lookup(
							IBillInvokeCreditManager.class.getName());
					para = new SOCreditPara(null, sourcebids, new String[] { inVO.getPrimaryKey() }, null,
							CreditConst.ICREDIT_ACT_ARSUBCHGBYINVOICEUNAUDIT, new String[] { inVO.getHeadVO()
									.getCbiztype() }, inVO.getPk_corp(), CreditConst.ICREDIT_BILL_INVOICE,
							iSourceBillType, creditManager);
					creditManager.renovateARByHidsBegin(para);
				}
				/** 更新三个应收 begin* */

				// ##################################################
				// ####重要说明：生成的业务组件方法尽量不要进行修改####
				// 方法说明:结算提交（反操作）
				retObj = runClass("nc.impl.scm.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck",
						"&PKBILL:String", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("setAfterInvoiceAbandonCheck", retObj);
				}
				// ##################################################
				// ####重要说明：生成的业务组件方法尽量不要进行修改####
				// 方法说明:弃审检查
				runClass("nc.impl.scm.so.pub.CheckExecDMOImpl", "isUnSaleInvoice",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("isUnSaleInvoice", retObj);
				}
				// ##################################################
				// ####重要说明：生成的业务组件方法尽量不要进行修改####
				// 方法说明:销售发票传采购后是否可弃审
				// runClass("nc.itf.ps.settle.ISettle","isUnauditableForSale","&PKBILL:STRING",vo,m_keyHas,m_methodReturnHas);
				ICreateCorpQueryService myService = (ICreateCorpQueryService) nc.bs.framework.common.NCLocator
						.getInstance().lookup(ICreateCorpQueryService.class.getName());
				boolean bPOStartUp = myService.isEnabled(((SaleVO) inVO.getParentVO()).getPk_corp(), "PO");
				if (bPOStartUp) {
					ISettle bo = (ISettle) nc.bs.framework.common.NCLocator.getInstance().lookup(
							ISettle.class.getName());
					bo.isUnauditableForSale(pk_bill);
				}

				if (retObj != null) {
					m_methodReturnHas.put("isUnauditableForSale", retObj);
				}
				// ##################################################
				// ####重要说明：生成的业务组件方法尽量不要进行修改####	
				procUnApproveFlow(vo);
				/*System.out.println("工作流处理结束procUnApproveFlow：isFinishToGoing = " + isFinishToGoing);*/
				// ##################################################
				
				if (bEnableSO6) {
					creditManager.renovateARByHidsEnd(para);
				}

				// 方法说明:返回最新的发票VO
				retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
						"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
				// ##################################################
				
				
				// 在弃审时进行数量和开票状态的回写
				// add by river for 2012-08-07
				// start ..
				runClass("nc.impl.scm.so.pub.DataControlDMO", "setDecreaseInvoiceNum",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("setDecreaseInvoiceNum", retObj);
				
				// .. end
				
			} catch (Exception e) {
				SCMEnv.out(e);
				throw e;
			}

			// *********返回结果******************************************************
			inVO = null;
			pk_bill = null;
			billtype = null;
			return retObj;
			// ************************************************************************
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new BusinessException(ex);
		}
	}

	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值#### \n	//*************从平台取得由该动作传入的入口参数。*********** \n	Object inObject =getVo (); \n	//1,首先检查传入参数类型是否合法，是否为空。 \n	if (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票类型不匹配\")); \n	if (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票没有数据\")); \n	//2,数据合法，把数据转换。 \n	nc.vo.so.so002.SaleinvoiceVO inVO = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n	String pk_bill = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCsaleid (); \n	String billtype = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCreceipttype (); \n	inObject =null; \n	//************************************************************************************************** \n	setParameter ( \"INVO\",inVO); \n	setParameter ( \"PKBILL\",pk_bill); \n	setParameter ( \"BILLTYPE\",billtype); \n	//************************************************************************************************** \n	Object retObj =null; \n	//方法说明:加锁\n	Object bFlag=null;\n	bFlag=runClassCom@\"nc.impl.scm.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	//##################################################\n	try{\n	              //####重要说明：生成的业务组件方法尽量不要进行修改####\n	              //方法说明:并发检查\n	              runClassCom@ \"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	              //##################################################\n	              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n	              //方法说明:并发互斥检查 \n	               runClassCom@ \"nc.impl.scm.so.pub.CheckStatusDMO\", \"isUnApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n	              //################################################## \n	              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n	              //方法说明:弃审检查 \n	              runClassCom@ \"nc.impl.scm.so.pub.CheckExecDMO\", \"isUnSaleInvoice\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n	              //################################################## \n	              //####重要说明：生成的业务组件方法尽量不要进行修改####\n	              //方法说明:销售发票传采购后是否可弃审\n	              runClassCom@\"nc.bs.ps.settle.SettleDMO\",\"isUnauditableForSale\",\"&PKBILL:STRING\"@;\n	              //##################################################\n	              //####重要说明：生成的业务组件方法尽量不要进行修改#### \n	              //方法说明:将单据状态改为“自由” \n	              runClassCom@ \"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillFree\", \"&PKBILL:String,&BILLTYPE:String\"@; \n	              //################################################## \n	              //####重要说明：生成的业务组件方法尽量不要进行修改####\n	              //方法说明:结算提交（反操作） \n	              retObj =runClassCom@ \"nc.impl.scm.so.so012.SquareInputDMO\", \"setAfterInvoiceAbandonCheck\", \"&PKBILL:String\"@; \n	              //################################################## \n	}\n	catch (Exception e) {\n		if (e instanceof	RemoteException) throw (RemoteException)e;\n		else throw new RemoteException (e.getMessage());\n	}\n	finally {\n		//解业务锁\n		//####重要说明：生成的业务组件方法尽量不要进行修改####\n		//方法说明:解锁\n		if(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n			runClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n			//##################################################\n		}\n	}\n   \n              //*********返回结果****************************************************** \n	inVO =null; \n	pk_bill =null; \n	billtype =null; \n	return retObj; \n	//************************************************************************\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
