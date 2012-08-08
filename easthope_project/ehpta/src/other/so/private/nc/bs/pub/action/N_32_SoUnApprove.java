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
 * ��ע�����۷�Ʊ������ ���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 * 
 * �������ڣ�(2005-6-18)
 * 
 * @author��ƽ̨�ű�����
 */
public class N_32_SoUnApprove extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	/**
	 * N_32_SoUnApprove ������ע�⡣
	 */
	public N_32_SoUnApprove() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			// *************��ƽ̨ȡ���ɸö����������ڲ�����***********
			Object inObject = getVo();
			// 1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
			if (!(inObject instanceof nc.vo.so.so002.SaleinvoiceVO))
				throw new java.rmi.RemoteException("Remote Call", new nc.vo.pub.BusinessException("������ϣ����������۷�Ʊ���Ͳ�ƥ��"));
			if (inObject == null)
				throw new java.rmi.RemoteException("Remote Call", new nc.vo.pub.BusinessException("������ϣ����������۷�Ʊû������"));
			// 2,���ݺϷ���������ת����
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
			// ����˵��:����
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
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
				// ����˵��:�������
				runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("checkVoNoChanged", retObj);
				}
				// ##################################################
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
				// ����˵��:����������
				runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isUnApproveStatus",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("isUnApproveStatus", retObj);
				}

				ICreateCorpQueryService icorp = (ICreateCorpQueryService) NCLocator.getInstance().lookup(
						ICreateCorpQueryService.class.getName());
				Hashtable ht = icorp.queryProductEnabled(inVO.getPk_corp(), new String[] { "SO6" });
				boolean bEnableSO6 = ((UFBoolean) ht.get("SO6")).booleanValue();
				/** ��������Ӧ�� begin* */
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
				/** ��������Ӧ�� begin* */

				// ##################################################
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
				// ����˵��:�����ύ����������
				retObj = runClass("nc.impl.scm.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck",
						"&PKBILL:String", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("setAfterInvoiceAbandonCheck", retObj);
				}
				// ##################################################
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
				// ����˵��:������
				runClass("nc.impl.scm.so.pub.CheckExecDMOImpl", "isUnSaleInvoice",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null) {
					m_methodReturnHas.put("isUnSaleInvoice", retObj);
				}
				// ##################################################
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
				// ����˵��:���۷�Ʊ���ɹ����Ƿ������
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
				// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####	
				procUnApproveFlow(vo);
				/*System.out.println("�������������procUnApproveFlow��isFinishToGoing = " + isFinishToGoing);*/
				// ##################################################
				
				if (bEnableSO6) {
					creditManager.renovateARByHidsEnd(para);
				}

				// ����˵��:�������µķ�ƱVO
				retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
						"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
				// ##################################################
				
				
				// ������ʱ���������Ϳ�Ʊ״̬�Ļ�д
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

			// *********���ؽ��******************************************************
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
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ#### \n	//*************��ƽ̨ȡ���ɸö����������ڲ�����*********** \n	Object inObject =getVo (); \n	//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա� \n	if (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊ���Ͳ�ƥ��\")); \n	if (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊû������\")); \n	//2,���ݺϷ���������ת���� \n	nc.vo.so.so002.SaleinvoiceVO inVO = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n	String pk_bill = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCsaleid (); \n	String billtype = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCreceipttype (); \n	inObject =null; \n	//************************************************************************************************** \n	setParameter ( \"INVO\",inVO); \n	setParameter ( \"PKBILL\",pk_bill); \n	setParameter ( \"BILLTYPE\",billtype); \n	//************************************************************************************************** \n	Object retObj =null; \n	//����˵��:����\n	Object bFlag=null;\n	bFlag=runClassCom@\"nc.impl.scm.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	//##################################################\n	try{\n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	              //����˵��:�������\n	              runClassCom@ \"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	              //##################################################\n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n	              //����˵��:���������� \n	               runClassCom@ \"nc.impl.scm.so.pub.CheckStatusDMO\", \"isUnApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n	              //################################################## \n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n	              //����˵��:������ \n	              runClassCom@ \"nc.impl.scm.so.pub.CheckExecDMO\", \"isUnSaleInvoice\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n	              //################################################## \n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	              //����˵��:���۷�Ʊ���ɹ����Ƿ������\n	              runClassCom@\"nc.bs.ps.settle.SettleDMO\",\"isUnauditableForSale\",\"&PKBILL:STRING\"@;\n	              //##################################################\n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n	              //����˵��:������״̬��Ϊ�����ɡ� \n	              runClassCom@ \"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillFree\", \"&PKBILL:String,&BILLTYPE:String\"@; \n	              //################################################## \n	              //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	              //����˵��:�����ύ���������� \n	              retObj =runClassCom@ \"nc.impl.scm.so.so012.SquareInputDMO\", \"setAfterInvoiceAbandonCheck\", \"&PKBILL:String\"@; \n	              //################################################## \n	}\n	catch (Exception e) {\n		if (e instanceof	RemoteException) throw (RemoteException)e;\n		else throw new RemoteException (e.getMessage());\n	}\n	finally {\n		//��ҵ����\n		//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n		//����˵��:����\n		if(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n			runClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n			//##################################################\n		}\n	}\n   \n              //*********���ؽ��****************************************************** \n	inVO =null; \n	pk_bill =null; \n	billtype =null; \n	return retObj; \n	//************************************************************************\n";
	}

	/*
	 * ��ע�����ýű�������HAS
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
