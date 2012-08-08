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
						.getStrByID("sopub", "UPPsopub-000262")/*@res "错误：您希望保存的销售发票类型不匹配"*/));
			if (null == inObject)
				throw new RemoteException("Remote Call", new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000263")/*@res "错误：您希望保存的销售发票没有数据"*/));
			SaleinvoiceVO inVO = (SaleinvoiceVO) inObject;
			inObject = null;
			setParameter("INVO", inVO);
			setParameter("BillType", "32");
			setParameter("NOKey", "crowno");
			Object retObj = null;
      //判断是否出库汇总开票
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
      //20090209 guyan、fangchan、fengjb 未做过合并开票的发票在保存时需要进行尾差处理
			}else if((null == inVO.getHeadVO().getNstrikemny() 
          || inVO.getHeadVO().getNstrikemny().compareTo(new UFDouble(0)) == 0)
          && inVO.getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_NORMAL){
        runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "dealMny",
            "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
        if (retObj != null)
          m_methodReturnHas.put("dealMny", retObj);
      }
      //判断是否需要自动合并开票
      if(inVO.getIsAutoUnit()){
			inVO = (nc.vo.so.so002.SaleinvoiceVO) runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "autoUniteInvoice",
					"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
			setParameter("INVO", inVO);
      }
      //加锁
			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo",
					"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("lockPkForVo", retObj);
			try {
        //校验时间戳
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

				// 在保存时不进行数量的回写及开票状态的回写
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
		return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值#### \n\t//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的销售订单。*********** \n\tObject inObject  =getVo (); \n\t//1,首先检查传入参数类型是否合法，是否为空。 \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票类型不匹配\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票没有数据\")); \n\t//2,数据合法，把数据转换为销售发票。 \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tinObject  =null; \n\t//************************************************************************************************** \n\t//************************把销售发票置入参数表。************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\tsetParameter ( \"BillType\",\"32\"); \n\tsetParameter ( \"NOKey\",\"crowno\"); \n\t//************************************************************************************************** \n\t//*******************执行销售发票保存前的业务处理********************** \n\tObject retObj  =null;\n\t//方法说明:判断是否为出库汇总开票\n\tUFBoolean isGather = ((nc.vo.so.so002.SaleVO) inVO.getParentVO()).getIsGather();\n\tif (isGather != null && isGather.booleanValue()) {\n\t\tinVO =(nc.vo.so.so002.SaleinvoiceVO) runClass(\"nc.bs.so.so002.SaleinvoiceDMO\",\"getGather\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n\t\tsetParameter(\"INVO\", inVO);\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:获得行号\n\t\trunClassCom@\"nc.bs.scm.pub.BillRowNoDMO\",\"setVORowNoByRule\",\"&INVO:nc.vo.pub.AggregatedValueObject,&BillType:STRING,&NOKey:STRING\"@;\n\t\t//##################################################\n\t}\n\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t//方法说明:加锁\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.bs.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:并发检查\n\t\trunClassCom@ \"nc.bs.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t//##################################################\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:保存关联检查\n\t\trunClassCom@ \"nc.bs.so.pub.CheckRelationDMO\", \"isInvoiceRelation\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t//##################################################\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t\t//方法说明:检查开票合计数量是否超过订购数量 \n\t\t//去掉检查,用后面保存后的约束, 谢高兴 2003/10/20\n\t\t//runClassCom@ \"nc.bs.so.pub.CheckApproveDMO\", \"isInvoiceAppRequst\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//****************************执行销售发票保存************************************************************ \n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\tretObj  =runClassCom@ \"nc.bs.so.so002.SaleinvoiceDMO\", \"insert\", \"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t\t//##################################################\n\t\tString strID = ((java.util.ArrayList)retObj).get(0).toString();\n\t\tinVO.getParentVO().setPrimaryKey(strID);\n\t\tinVO.setAllinvoicevo(inVO);\n\t\t//回写冲应收\n\t\trunClassCom@ \"nc.bs.so.so002.SaleinvoiceDMO\", \"writeToARSub\", \"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t\t//方法说明:回写开票数量 \n\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"setTotalInvoiceNum\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t\t//方法说明:结束订单开票状态 \n\t\tSaleVO hvo = (SaleVO)inVO.getParentVO();\n  if (hvo.getFcounteractflag()!=null  && hvo.getFcounteractflag().intValue()==2	)\n runClass(\"nc.bs.so.pub.DataControlDMO\", \"autoSetInvoicetCancelFinish\", \"&INVO:nc.vo.pub.AggregatedValueObject\", vo, m_keyHas, m_methodReturnHas);\n                else	runClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"autoSetInvoicetFinish\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t\t//方法说明:检查订单上开票数量是否大于订单数量 \n\t\trunClassCom@ \"nc.bs.so.pub.CheckValueValidity\", \"checkSaleOrderTInvnu\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t\t//################################################## \n\t}\n\tcatch (Exception e) {\n\t\t//方法说明:退单号\n\t\tnc.vo.so.so002.SaleVO  hvo =(nc.vo.so.so002.SaleVO) inVO.getParentVO();\n\t\tif(hvo.getVreceiptcode()!=null && hvo.getVreceiptcode().length()>0){\n\t\t                 try{\n\t\t\thvo.setVoldreceiptcode(hvo.getVreceiptcode()); \n\t\t\t retObj =runClassCom@ \"nc.bs.so.pub.CheckValueValidity\", \"returnBillNo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t                 }catch(Exception ex){\n\t\t\tthrow new RemoteException (ex.getMessage ());\n\t\t                 }\n\t\t}\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//解业务锁\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:解锁\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n\t//*******************执行销售发票保存后的业务处理********************** \n\t//*********返回结果****************************************************** \n\tinVO  =null; \n\treturn retObj;\n";
	}

	private void setParameter(String key, Object val) {
		if (m_keyHas == null)
			m_keyHas = new Hashtable<String, Object>();
		if (val != null)
			m_keyHas.put(key, val);
	}
}