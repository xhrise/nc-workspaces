package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.yto.blacklist.IblkQueryFunc;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;
import nc.vo.yto.blacklist.MyBillVO;

/**
 * ��ע��ActionVo.getBillTypeName()������ �Ķ�ִ̬���ࡣ
 * 
 * �������ڣ�(2011-8-26)
 * 
 * @author ƽ̨�ű�����
 */
public class N_blk_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_blk_APPROVE ������ע�⡣
	 */
	public N_blk_APPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####�����Ϊ����������������ʼ...���ܽ����޸�####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}

			// ####�����Ϊ�������������������...���ܽ����޸�####
			Object retObj = runClass("nc.bs.trade.comstatus.BillApprove",
					"approveBill", "nc.vo.pub.AggregatedValueObject:01", vo,
					m_keyHas, m_methodReturnHas);
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####�����Ϊ����������������ʼ...���ܽ����޸�####\n	procActionFlow@@;\n	//####�����Ϊ�������������������...���ܽ����޸�####\n	Object retObj  =runClassCom@ \"nc.bs.trade.comstatus.BillApprove\", \"approveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@; \n	return retObj;\n";
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
