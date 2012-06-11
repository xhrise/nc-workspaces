/*
 * �������� 2006-4-11
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.us.bi.dataauth;


import nc.bs.framework.common.NCLocator;
import nc.itf.bi.dataauth.IBDataAuthQuerySrv;
import nc.itf.bi.dataauth.IBDataPolicySrv;
import nc.vo.bi.dataauth.DataPolicyVO;
import nc.vo.pub.BusinessException;

/**
 * @author zyjun
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class DataPolicySrv {
	private static DataPolicySrv   s_srv = new DataPolicySrv();
	private DataPolicySrv(){
		
	}
	public static DataPolicySrv getInstance(){
		return s_srv;
	}
public DataPolicyVO[] getDataPolicyByDim(String strDimPK)
	throws BusinessException {
		return getDataAuthBSrvImpl().getDataPolicyByDim(strDimPK);
}


public DataPolicyVO getDataPolicyByPK(String strPK)
	throws BusinessException {
	return getDataAuthBSrvImpl().getDataPolicyByPK(strPK);
}

public DataPolicyVO[] getDataPolicyByRole(String strDimPK, String strRolePK)
	throws BusinessException {
	return getDataAuthBSrvImpl().getDataPolicyByRole(strDimPK, strRolePK);
}

public DataPolicyVO  getByRoleAndField(String strDimPK, String strRolePK, String strDimField)
	throws BusinessException{
	return getDataAuthBSrvImpl().getByRoleAndField(strDimPK, strRolePK, strDimField);
}


public String createDataPolicy(DataPolicyVO policyVO)
		throws BusinessException {
	return getDataPolicyBSrvImpl().createDataPolicy(policyVO);
}

public String[] createDataPolicys(DataPolicyVO[] policyVOs)
		throws BusinessException {
	return getDataPolicyBSrvImpl().createDataPolicys(policyVOs);
}

public void updateDataPolicy(DataPolicyVO policyVO)
		throws BusinessException {
	getDataPolicyBSrvImpl().updateDataPolicy(policyVO);

}

public void updateDataPolicys(DataPolicyVO[] policyVOs)
		throws BusinessException {
	getDataPolicyBSrvImpl().updateDataPolicys(policyVOs);
}

public void removeDataPolicy(String strPK)
		throws BusinessException {
	getDataPolicyBSrvImpl().removeDataPolicy(strPK);
}

public void removeDataPolicys(String[] strPKs)
		throws BusinessException {
	getDataPolicyBSrvImpl().removeDataPolicys(strPKs);
}

public void removeDataPolicysByDims(String[] strDimPKs)
		throws BusinessException {
	getDataPolicyBSrvImpl().removeDataPolicysByDims(strDimPKs);
}

public void removeDataPolicysByRole(String[] strRolePKs)
		throws BusinessException {
	getDataPolicyBSrvImpl().removeDataPolicysByRole(strRolePKs);
}

private IBDataAuthQuerySrv  getDataAuthBSrvImpl(){
	return (IBDataAuthQuerySrv)NCLocator.getInstance().lookup(IBDataAuthQuerySrv.class.getName());
}
private IBDataPolicySrv	getDataPolicyBSrvImpl(){
	return (IBDataPolicySrv)NCLocator.getInstance().lookup(IBDataPolicySrv.class.getName());
}
}
