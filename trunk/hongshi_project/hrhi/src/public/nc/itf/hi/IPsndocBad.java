package nc.itf.hi;

import nc.vo.hi.hi_312.PsndocBadHeaderVO;
import nc.vo.pub.BusinessException;

public interface IPsndocBad {

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public abstract void checkOutPsn(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * 向数据库中插入一个VO对象。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public abstract void deleteHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * 通过主键获得VO对象。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @return nc.vo.hi.hi_312.PsndocBadVO
	 * @param key
	 *            String
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public PsndocBadHeaderVO[] findVosByCorp(String key, boolean isCheck,
			String DLGwheresql,String normalwheresql) throws BusinessException;

	/**
	 * 向数据库中插入一个VO对象。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public abstract String insertHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

	/**
	 * 用VO对象的属性值更新数据库。
	 * 
	 * 创建日期：(2003-9-18)
	 * 
	 * @param psndocBad
	 *            nc.vo.hi.hi_312.PsndocBadVO
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public abstract void updateHeader(PsndocBadHeaderVO psndocBadhead)
			throws BusinessException;

}