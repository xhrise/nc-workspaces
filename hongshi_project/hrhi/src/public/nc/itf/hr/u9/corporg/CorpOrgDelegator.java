package nc.itf.hr.u9.corporg;

import nc.bs.framework.common.NCLocator;

/**
 * ��˾���Ŷ���Delegator��
 * @author fengwei
 *
 */
public class CorpOrgDelegator {
	private static ICorpOrg icorpOrg = null;
	
	/**
	 * �ӿ�ʵ��
	 * @return �ӿ�
	 */
	public static ICorpOrg getICorpOrg(){
		if(icorpOrg == null){
			icorpOrg = (ICorpOrg) NCLocator.getInstance().lookup(ICorpOrg.class.getName());
		}
		
		return icorpOrg;
	}

}
