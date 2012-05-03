package nc.itf.hr.u9.corporg;

import nc.bs.framework.common.NCLocator;

/**
 * 公司部门对照Delegator类
 * @author fengwei
 *
 */
public class CorpOrgDelegator {
	private static ICorpOrg icorpOrg = null;
	
	/**
	 * 接口实现
	 * @return 接口
	 */
	public static ICorpOrg getICorpOrg(){
		if(icorpOrg == null){
			icorpOrg = (ICorpOrg) NCLocator.getInstance().lookup(ICorpOrg.class.getName());
		}
		
		return icorpOrg;
	}

}
