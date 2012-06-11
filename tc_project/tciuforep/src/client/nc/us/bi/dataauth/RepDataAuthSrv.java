/*
 * �������� 2006-4-11
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.us.bi.dataauth;


import nc.bs.framework.common.NCLocator;
import nc.itf.bi.dataauth.IBDataAuthQuerySrv;
import nc.itf.bi.dataauth.IBRepDataAuthSrv;
import nc.vo.bi.dataauth.RepDataAuthVO;
import nc.vo.pub.BusinessException;

/**
 * @author zyjun
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class RepDataAuthSrv {
	private static RepDataAuthSrv		s_instance = new RepDataAuthSrv();
	private RepDataAuthSrv(){
		super();
	}
	public static RepDataAuthSrv   getInstance(){
		return s_instance;
	}
	public RepDataAuthVO[] getRepAuthesByMember(String strRepPK,
			String strDimPK, String strDimMemberPK) throws BusinessException {
		return getDataAuthQuerySrvImpl().getRepAuthesByMember(strRepPK, strDimPK, strDimMemberPK);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBDataAuthQuerySrv#getRepAuthByPK(java.lang.String, java.lang.String, java.lang.String)
	 */
	public RepDataAuthVO getRepAuthByPK(String strPK) throws BusinessException {
		return getDataAuthQuerySrvImpl().getRepAuthByPK(strPK);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBDataAuthQuerySrv#getRepAuthesByUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public RepDataAuthVO[] getRepAuthesByUser(String strRepPK, String strDimPK,
			String strUserPK) throws BusinessException {
		return getDataAuthQuerySrvImpl().getRepAuthesByUser(strRepPK, strDimPK, strUserPK); 
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBDataAuthQuerySrv#getAllRepAuthesByDim(java.lang.String, java.lang.String)
	 */
	public RepDataAuthVO[] getAllRepAuthesByDim(String strRepPK, String strDimPK)
			throws BusinessException {
		return getDataAuthQuerySrvImpl().getAllRepAuthesByDim(strRepPK, strDimPK);
	}
	
	public RepDataAuthVO	getRepAuthesByMemberUser(String strRepPK, String strDimPK, String strDimMember, String strUserPK)
		throws BusinessException{
		return getDataAuthQuerySrvImpl().getRepAuthesByMemberUser(strRepPK, strDimPK, strDimMember, strUserPK);
	}
	
	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#createRepDataAuth(nc.vo.bi.dataauth.RepDataAuthVO)
	 */
	public String createRepDataAuth(RepDataAuthVO authVO)
			throws BusinessException {
		return getRepDataAuthBSrvImpl().createRepDataAuth(authVO);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#createRepDataAuthes(nc.vo.bi.dataauth.RepDataAuthVO[])
	 */
	public String[] createRepDataAuthes(RepDataAuthVO[] authVOs)
			throws BusinessException {
		return getRepDataAuthBSrvImpl().createRepDataAuthes(authVOs);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#updateRepDataAuth(nc.vo.bi.dataauth.RepDataAuthVO)
	 */
	public void updateRepDataAuth(RepDataAuthVO authVO)
			throws BusinessException {
		getRepDataAuthBSrvImpl().updateRepDataAuth(authVO);

	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#updateRepDataAuthes(nc.vo.bi.dataauth.RepDataAuthVO[])
	 */
	public void updateRepDataAuthes(RepDataAuthVO[] authVOs)
			throws BusinessException {
		getRepDataAuthBSrvImpl().updateRepDataAuthes(authVOs);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuth(java.lang.String)
	 */
	public void removeRepDataAuth(String strPK) throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuth(strPK);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuthes(java.lang.String[])
	 */
	public void removeRepDataAuthes(String[] strPKs) throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuthes(strPKs);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuthesByDim(java.lang.String)
	 */
	public void removeRepDataAuthesByDim(String strDimPK)
			throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuthesByDim(strDimPK);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuthesByUsers(java.lang.String[])
	 */
	public void removeRepDataAuthesByUsers(String[] strUserPKs)
			throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuthesByUsers(strUserPKs);	
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuthesByMembers(java.lang.String, java.lang.String[])
	 */
	public void removeRepDataAuthesByMembers(String strDimPK,
			String[] strMemberPKs) throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuthesByMembers(strDimPK, strMemberPKs);
	}

	/* ���� Javadoc��
	 * @see nc.itf.bi.dataauth.IBRepDataAuthSrv#removeRepDataAuthesByRep(java.lang.String[])
	 */
	public void removeRepDataAuthesByRep(String[] strRepPKs)
			throws BusinessException {
		getRepDataAuthBSrvImpl().removeRepDataAuthesByRep(strRepPKs);
	}
	
	private IBRepDataAuthSrv getRepDataAuthBSrvImpl(){
		return (IBRepDataAuthSrv)NCLocator.getInstance().lookup(IBRepDataAuthSrv.class.getName());
	}
	private IBDataAuthQuerySrv getDataAuthQuerySrvImpl(){
		return (IBDataAuthQuerySrv)NCLocator.getInstance().lookup(IBDataAuthQuerySrv.class.getName());
	}
}
