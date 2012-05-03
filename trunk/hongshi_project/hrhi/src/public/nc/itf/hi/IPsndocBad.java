package nc.itf.hi;

import nc.vo.hi.hi_312.PsndocBadHeaderVO;
import nc.vo.pub.BusinessException;

public interface IPsndocBad {

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public abstract void checkOutPsn(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * �����ݿ��в���һ��VO����
	 * 
	 * �������ڣ�(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @return java.lang.String ������VO����������ַ�����
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public abstract void deleteHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * ͨ���������VO����
	 * 
	 * �������ڣ�(2003-9-18)
	 * 
	 * @return nc.vo.hi.hi_312.PsndocBadVO
	 * @param key
	 *            String
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public PsndocBadHeaderVO[] findVosByCorp(String key, boolean isCheck,
			String DLGwheresql,String normalwheresql) throws BusinessException;

	/**
	 * �����ݿ��в���һ��VO����
	 * 
	 * �������ڣ�(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @return java.lang.String ������VO����������ַ�����
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public abstract String insertHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣
	 * 
	 * �������ڣ�(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public abstract void updateHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

}