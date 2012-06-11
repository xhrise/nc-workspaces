/*
 * �������� 2006-4-11
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.us.bi.dataauth;


import nc.bs.framework.common.NCLocator;
import nc.itf.bi.dataauth.IBDataAuthQuerySrv;
import nc.itf.bi.dataauth.IBDataAuthSrv;
import nc.vo.bi.dataauth.*;
import nc.vo.pub.BusinessException;


/**
 * @author zyjun
 *
 * ά������Ȩ�޷�����
 */
public class DataAuthSrv {
	private	static DataAuthSrv		s_srv = new DataAuthSrv();
	
	private DataAuthSrv(){
		super();
	}
	public static DataAuthSrv	getInstance(){
		return s_srv;
	}
	public String createDataAuth(DataAuthVO authVO) throws BusinessException {
		return getDataAuthBSrvImpl().createDataAuth(authVO);
	}
	public String[] createDataAuthes(DataAuthVO[] authVOs) throws BusinessException {
		return getDataAuthBSrvImpl().createDataAuthes(authVOs);
	}
	public void removeDataAuth(String strPK)
			throws BusinessException {
		getDataAuthBSrvImpl().removeDataAuth(strPK);

	}
	public void removeDataAuthes(String[] strPKs)
			throws BusinessException {
		getDataAuthBSrvImpl().removeDataAuthes(strPKs);
	}
	public void removeDataAuthesByDim(String strDimPK) throws BusinessException {
		getDataAuthBSrvImpl().removeDataAuthesByDim(strDimPK);
	}
	public void removeDataAuthesByMember(String strMemberPK)
			throws BusinessException {
		getDataAuthBSrvImpl().removeDataAuthesByMember(strMemberPK);

	}
	public void removeDataAuthesByUser(String strUserPK)
			throws BusinessException {
		getDataAuthBSrvImpl().removeDataAuthesByUser(strUserPK);

	}
	public void updateDataAuth(DataAuthVO authVO) throws BusinessException {
		getDataAuthBSrvImpl().updateDataAuth(authVO);
	}
	public void updateDataAuthes(DataAuthVO[] authVOs) throws BusinessException {
		getDataAuthBSrvImpl().updateDataAuthes(authVOs);
	}
	
	public DataAuthVO[] getDimAuthesByMember(String strDimPK,
			String strDimMemberPK) throws BusinessException {
		return getDataAuthQueryBSrvImpl().getDimAuthesByMember(strDimPK, strDimMemberPK);
	}

	public DataAuthVO getDimAuthByPK(String strPK)
			throws BusinessException {
		return getDataAuthQueryBSrvImpl().getDimAuthByPK(strPK);
		
	}

	public DataAuthVO[] getDimAuthesByUser(String strDimPK, String strUserPK)
			throws BusinessException {
		return getDataAuthQueryBSrvImpl().getDimAuthesByUser(strDimPK,strUserPK);
	}

	public DataAuthVO getDimAuthesByMemberUser(String strDimPK, String strDimMember, String strUserPKs)
		throws BusinessException{
		return getDataAuthQueryBSrvImpl().getDimAuthesByMemberUser(strDimPK, strDimMember, strUserPKs);
	}
	public DataAuthVO[] getAllDimAuthesByDim(String strDimPK)
			throws BusinessException {
		return getDataAuthQueryBSrvImpl().getAllDimAuthesByDim(strDimPK);
	}

	private  IBDataAuthSrv  getDataAuthBSrvImpl(){
		return (IBDataAuthSrv)NCLocator.getInstance().lookup(IBDataAuthSrv.class.getName());
	}
	private IBDataAuthQuerySrv getDataAuthQueryBSrvImpl(){
		return (IBDataAuthQuerySrv)NCLocator.getInstance().lookup(IBDataAuthQuerySrv.class.getName());
	}

}
