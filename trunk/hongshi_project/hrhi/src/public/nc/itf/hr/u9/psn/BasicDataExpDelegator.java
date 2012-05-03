package nc.itf.hr.u9.psn;

import nc.bs.framework.common.NCLocator;

/**
 * 岗位、职务、人员对照 Delegator类
 * @author fengwei
 *
 */
public class BasicDataExpDelegator {
	
	private static IBasicDataExp iPostDutyPsn = null;
	private static IPersonADDSV iPersonADDSV = null;

	public static IBasicDataExp getIPostDutyPsn() {
		if(iPostDutyPsn == null){
			iPostDutyPsn = (IBasicDataExp) NCLocator.getInstance().lookup(IBasicDataExp.class.getName());
		}
		
		return iPostDutyPsn;
	}
	
	public static IPersonADDSV getIPersonADDSV(){
		if(iPersonADDSV == null){
			iPersonADDSV = (IPersonADDSV) NCLocator.getInstance().lookup(IPersonADDSV.class.getName());
		}
		
		return iPersonADDSV;
	}

}
