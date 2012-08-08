package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;

import nc.itf.so.so120.IBillInvokeCreditManager;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class N_32_SoBlankOut extends AbstractCompiler2 {

	private Hashtable m_methodReturnHas;
	private Hashtable m_keyHas;

	public N_32_SoBlankOut() {
		m_methodReturnHas = new Hashtable();
		m_keyHas = null;
	}

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			m_tmpVo = vo;
			Object inObject = getVo();
			if (!(inObject instanceof SaleinvoiceVO))
				throw new RemoteException("Remote Call", new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000262")/*@res "������ϣ����������۷�Ʊ���Ͳ�ƥ��"*/));
			if (inObject == null)
				throw new RemoteException("Remote Call", new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000263")/*@res "������ϣ����������۷�Ʊû������"*/));
			SaleinvoiceVO inVO = (SaleinvoiceVO) inObject;
			String pk_bill = ((SaleVO) inVO.getParentVO()).getCsaleid();
			String billtype = ((SaleVO) inVO.getParentVO()).getCreceipttype();
			inObject = null;
			setParameter("INVO", inVO);
			setParameter("PKBILL", pk_bill);
			setParameter("BILLTYPE", billtype);
			Object retObj = null;

			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "nc.vo.pub.AggregatedValueObject:01", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("lockPkForVo", retObj);
			try {
				retObj = runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isBlankOutStatus",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("isBlankOutStatus", retObj);
				retObj = runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillBlankOut",
						"&PKBILL:String,&BILLTYPE:String", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("setBillBlankOut", retObj);
				runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "writeToARSub", "&INVO:nc.vo.so.so002.SaleinvoiceVO",
						vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("writeToARSub", retObj);
				runClass("nc.impl.scm.so.pub.CheckValueValidityImpl", "returnBillNo",
						"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("returnBillNo", retObj);
          //V55 fengjb jdm jianghp ��Ʊ��Ӱ������
//				IBillInvokeCreditManager creditManager = (IBillInvokeCreditManager) nc.bs.framework.common.NCLocator
//						.getInstance().lookup(IBillInvokeCreditManager.class.getName());
//				;
//				setParameter("CreditManager", creditManager);
				inVO.setIAction(ISaleOrderAction.A_BLANKOUT);

				// ɾ��ʱ����д��������Ʊ��־���������Ǵ���
				// modify by river for 2012-08-08
				// start .. 
//				runClass("nc.impl.scm.so.pub.DataControlDMO", "setDecreaseInvoiceNum",
//						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
//				if (retObj != null)
//					m_methodReturnHas.put("setDecreaseInvoiceNum", retObj);
				
				// .. end
				
        //V55 ����Գ��ǣ��������ر�
				retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "clearControlActFlag",
						"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
			} catch (Exception e) {
				if (e instanceof RemoteException)
					throw (RemoteException) e;
				else
					throw new RemoteException(e.getMessage());
			}

			inVO = null;
			pk_bill = null;
			billtype = null;
			return retObj;
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	public String getCodeRemark() {
		return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ#### \n\t//*************��ƽ̨ȡ���ɸö����������ڲ�����*********** \n\tObject inObject  =getVo (); \n\t//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա� \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊ���Ͳ�ƥ��\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊû������\")); \n\t//2,���ݺϷ���������ת���� \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tString pk_bill  = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCsaleid (); \n\tString billtype  = ( (nc.vo.so.so002.SaleVO)inVO.getParentVO ()).getCreceipttype (); \n\tinObject  =null; \n\t//************************************************************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\tsetParameter ( \"PKBILL\",pk_bill); \n\tsetParameter ( \"BILLTYPE\",billtype); \n\t//************************************************************************************************** \n\tObject retObj  =null; \n\t//����˵��:����\n\tObject bFlag=null;\n\tbFlag=runClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n\t//##################################################\n\ttry {\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:���������� \n\t\tretObj  =runClassCom@ \"nc.impl.scm.so.pub.CheckStatusDMO\", \"isBlankOutStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:�������� \n\t\tretObj  =runClassCom@ \"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillBlankOut\", \"&PKBILL:String,&BILLTYPE:String\"@; \n\t\t//################################################## \n\t\t//��д��Ӧ��\n\t\trunClassCom@ \"nc.impl.scm.so.so002.SaleinvoiceDMO\", \"writeToARSub\", \"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:�ͷŵ��ݺ�\n\t\trunClassCom@ \"nc.impl.scm.so.pub.CheckValueValidity\", \"returnBillNo\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n\t\t//##################################################\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��: ��д��Ʊ����\n\t\trunClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"setDecreaseInvoiceNum\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:����������Ʊ״̬���������� \n\t\trunClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"autoSetInvoicetCancelFinish\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof RemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage ());\n\t}\n\tfinally {\n\t\t//��ҵ����\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n\t//*********���ؽ��****************************************************** \n\tinVO  =null; \n\tpk_bill  =null; \n\tbilltype  =null; \n\treturn retObj; \n\t//************************************************************************\n";
	}

	private void setParameter(String key, Object val) {
		if (m_keyHas == null)
			m_keyHas = new Hashtable();
		if (val != null)
			m_keyHas.put(key, val);
	}
}