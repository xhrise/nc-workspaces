package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.Hashtable;

import nc.bs.logging.Logger;
import nc.bs.pub.compiler.AbstractCompiler2;

import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceVO;

@SuppressWarnings({"unused"})
public class N_32_PreKeep extends AbstractCompiler2 {

	private Hashtable<String, Object> m_methodReturnHas;
	private Hashtable<String, Object> m_keyHas;

	public N_32_PreKeep() {
		m_methodReturnHas = new Hashtable<String, Object>();
		m_keyHas = null;
	}

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			m_tmpVo = vo;
			Object inObject = getVo();
			if (!(inObject instanceof SaleinvoiceVO))
				throw new RemoteException("Remote Call", new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000262")/*@res "������ϣ����������۷�Ʊ���Ͳ�ƥ��"*/));
			if (null == inObject)
				throw new RemoteException("Remote Call", new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000263")/*@res "������ϣ����������۷�Ʊû������"*/));
			SaleinvoiceVO inVO = (SaleinvoiceVO) inObject;
			inObject = null;
			setParameter("INVO", inVO);
			setParameter("BillType", "32");
			setParameter("NOKey", "crowno");
			Object retObj = null;
      //�ж��Ƿ������ܿ�Ʊ
			UFBoolean isGather = inVO.getHeadVO().getIsGather();
			if (null != isGather && isGather.booleanValue()) {
				inVO = (SaleinvoiceVO) runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getGather",
						"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
				setParameter("INVO", inVO);
				runClass("nc.bs.scm.pub.BillRowNoDMO", "setVORowNoByRule",
						"&INVO:nc.vo.pub.AggregatedValueObject,&BillType:STRING,&NOKey:STRING", vo, m_keyHas,
						m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("setVORowNoByRule", retObj);
      //20090209 guyan��fangchan��fengjb δ�����ϲ���Ʊ�ķ�Ʊ�ڱ���ʱ��Ҫ����β���
			}else if((null == inVO.getHeadVO().getNstrikemny() 
          || inVO.getHeadVO().getNstrikemny().compareTo(new UFDouble(0)) == 0)
          && inVO.getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_NORMAL){
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "dealMny",
            "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
        if (retObj != null)
          m_methodReturnHas.put("dealMny", retObj);
      }
      //�ж��Ƿ���Ҫ�Զ��ϲ���Ʊ
      if(inVO.getIsAutoUnit()){
			inVO = (nc.vo.so.so002.SaleinvoiceVO) runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "autoUniteInvoice",
					"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
			setParameter("INVO", inVO);
      }
      //����
			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo",
					"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("lockPkForVo", retObj);
			try {
        //У��ʱ���
				runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("checkVoNoChanged", retObj);
				runClass("nc.impl.scm.so.pub.CheckRelationDMO", "isInvoiceRelation",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("isInvoiceRelation", retObj);
				if (retObj != null)
					m_methodReturnHas.put("isInvoiceAppRequst", retObj);
				retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "insert",
						"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("insert", retObj);
//				String strID = ((ArrayList) retObj).get(0).toString();
        String strID = ((SaleinvoiceVO)retObj).getHeadVO().getCsaleid();
				inVO.getParentVO().setPrimaryKey(strID);
				inVO.setAllinvoicevo(inVO);

				runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "writeToARSub", "&INVO:nc.vo.so.so002.SaleinvoiceVO",
						vo, m_keyHas, m_methodReturnHas);

				// �ڱ���ʱ�����������Ļ�д����Ʊ״̬�Ļ�д
				// modify by river for 2012-08-07
				// start ..
//				runClass("nc.impl.scm.so.pub.DataControlDMO", "setTotalInvoiceNum",
//						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
//				if (retObj != null)
//					m_methodReturnHas.put("setTotalInvoiceNum", retObj);
				
				// .. end 
				
				runClass("nc.impl.scm.so.pub.CheckValueValidityImpl", "checkSaleOrderTInvnu",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("checkSaleOrderTInvnu", retObj);
			} catch (Exception e) {
				SaleVO hvo = (SaleVO) inVO.getParentVO();
				if (hvo.getVreceiptcode() != null && hvo.getVreceiptcode().length() > 0)
					try {
						hvo.setVoldreceiptcode(hvo.getVreceiptcode());
						retObj = runClass("nc.impl.scm.so.pub.CheckValueValidityImpl", "returnBillNo",
								"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
						if (retObj != null)
							m_methodReturnHas.put("returnBillNo", retObj);
					} catch (Exception ex) {
						throw new RemoteException(ex.getMessage());
					}
		
					throw e;
			} 
			inVO = null;
			return retObj;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new BusinessException(ex.getMessage(), ex);
		}
	}

	public String getCodeRemark() {
		return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ#### \n\t//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ��������۶�����*********** \n\tObject inObject  =getVo (); \n\t//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա� \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊ���Ͳ�ƥ��\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊû������\")); \n\t//2,���ݺϷ���������ת��Ϊ���۷�Ʊ�� \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tinObject  =null; \n\t//************************************************************************************************** \n\t//************************�����۷�Ʊ���������************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\tsetParameter ( \"BillType\",\"32\"); \n\tsetParameter ( \"NOKey\",\"crowno\"); \n\t//************************************************************************************************** \n\t//*******************ִ�����۷�Ʊ����ǰ��ҵ����********************** \n\tObject retObj  =null;\n\t//����˵��:�ж��Ƿ�Ϊ������ܿ�Ʊ\n\tUFBoolean isGather = ((nc.vo.so.so002.SaleVO) inVO.getParentVO()).getIsGather();\n\tif (isGather != null && isGather.booleanValue()) {\n\t\tinVO =(nc.vo.so.so002.SaleinvoiceVO) runClass(\"nc.bs.so.so002.SaleinvoiceDMO\",\"getGather\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n\t\tsetParameter(\"INVO\", inVO);\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����к�\n\t\trunClassCom@\"nc.bs.scm.pub.BillRowNoDMO\",\"setVORowNoByRule\",\"&INVO:nc.vo.pub.AggregatedValueObject,&BillType:STRING,&NOKey:STRING\"@;\n\t\t//##################################################\n\t}\n\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t//����˵��:����\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.bs.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:�������\n\t\trunClassCom@ \"nc.bs.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t//##################################################\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����������\n\t\trunClassCom@ \"nc.bs.so.pub.CheckRelationDMO\", \"isInvoiceRelation\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t//##################################################\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:��鿪Ʊ�ϼ������Ƿ񳬹��������� \n\t\t//ȥ�����,�ú��汣����Լ��, л���� 2003/10/20\n\t\t//runClassCom@ \"nc.bs.so.pub.CheckApproveDMO\", \"isInvoiceAppRequst\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//****************************ִ�����۷�Ʊ����************************************************************ \n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\tretObj  =runClassCom@ \"nc.bs.so.so002.SaleinvoiceDMO\", \"insert\", \"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t\t//##################################################\n\t\tString strID = ((java.util.ArrayList)retObj).get(0).toString();\n\t\tinVO.getParentVO().setPrimaryKey(strID);\n\t\tinVO.setAllinvoicevo(inVO);\n\t\t//��д��Ӧ��\n\t\trunClassCom@ \"nc.bs.so.so002.SaleinvoiceDMO\", \"writeToARSub\", \"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:��д��Ʊ���� \n\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"setTotalInvoiceNum\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:����������Ʊ״̬ \n\t\tSaleVO hvo = (SaleVO)inVO.getParentVO();\n  if (hvo.getFcounteractflag()!=null  && hvo.getFcounteractflag().intValue()==2	)\n runClass(\"nc.bs.so.pub.DataControlDMO\", \"autoSetInvoicetCancelFinish\", \"&INVO:nc.vo.pub.AggregatedValueObject\", vo, m_keyHas, m_methodReturnHas);\n                else	runClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"autoSetInvoicetFinish\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t\t//����˵��:��鶩���Ͽ�Ʊ�����Ƿ���ڶ������� \n\t\trunClassCom@ \"nc.bs.so.pub.CheckValueValidity\", \"checkSaleOrderTInvnu\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t}\n\tcatch (Exception e) {\n\t\t//����˵��:�˵���\n\t\tnc.vo.so.so002.SaleVO  hvo =(nc.vo.so.so002.SaleVO) inVO.getParentVO();\n\t\tif(hvo.getVreceiptcode()!=null && hvo.getVreceiptcode().length()>0){\n\t\t                 try{\n\t\t\thvo.setVoldreceiptcode(hvo.getVreceiptcode()); \n\t\t\t retObj =runClassCom@ \"nc.bs.so.pub.CheckValueValidity\", \"returnBillNo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t                 }catch(Exception ex){\n\t\t\tthrow new RemoteException (ex.getMessage ());\n\t\t                 }\n\t\t}\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//��ҵ����\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n\t//*******************ִ�����۷�Ʊ������ҵ����********************** \n\t//*********���ؽ��****************************************************** \n\tinVO  =null; \n\treturn retObj;\n";
	}

	private void setParameter(String key, Object val) {
		if (m_keyHas == null)
			m_keyHas = new Hashtable<String, Object>();
		if (val != null)
			m_keyHas.put(key, val);
	}
}