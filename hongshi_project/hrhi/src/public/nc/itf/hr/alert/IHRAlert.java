package nc.itf.hr.alert;

import java.util.Vector;
import nc.vo.hr.alert.POIDataVO;
import nc.vo.hr.alert.Pk_psnAndDeptmanaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pa.P2PAlartMessage;
import nc.vo.pub.pa.P2PItem;

public interface IHRAlert {

	/**
	 * 此处插入方法描述。 创建日期：(2004-7-28 17:26:21)
	 * 
	 * @return nc.vo.hr.alert.Pk_psnAndDeptmanaVO[]
	 * @param param
	 *            nc.vo.hr.alert.Pk_psnAndDeptmanaVO
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public abstract Vector[] dealwithDeptmanaVOsOrdByPsn(
			nc.vo.hr.alert.UserAndClerkVO[] deptmanauserVOs)
			throws BusinessException;

	/**
	 * /** * 描述函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 *
	 * 
	 * @param 参数说明 *
	 * @return 返回值 *
	 * @exception 异常描述 *
	 * @see 需要参见的其它内容 *
	 * @since 从类的那一个版本，此方法被添加进来。（可选） *
	 * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选） *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public abstract Pk_psnAndDeptmanaVO[] findDeptMngrByPsnPK(
			String[] pk_psndocs) throws BusinessException;

	/**
	 * 生日预警方法。
	 * 
	 * 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public abstract POIDataVO[] queryBirthdayData(Vector v)
			throws BusinessException;

	/**
	 * /** * 描述函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 *
	 * 
	 * @param 参数说明 *
	 * @return 返回值 *
	 * @exception 异常描述 *
	 * @see 需要参见的其它内容 *
	 * @since 从类的那一个版本，此方法被添加进来。（可选） *
	 * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选） *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public abstract nc.vo.hr.alert.UserAndClerkVO[] queryByClerkId(
			String[] pk_psndocs) throws BusinessException;

	/**
	 * /** * 描述函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 *
	 * 
	 * @param 参数说明 *
	 * @return 返回值 *
	 * @exception 异常描述 *
	 * @see 需要参见的其它内容 *
	 * @since 从类的那一个版本，此方法被添加进来。（可选） *
	 * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选） *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public abstract nc.vo.hr.alert.UserAndClerkVO[] queryByManaClerkId(
			nc.vo.hr.alert.Pk_psnAndDeptmanaVO[] deptmanaVOs)
			throws BusinessException;

	/**
	 * 合同到期预警方法。
	 * 
	 * 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public abstract POIDataVO[] queryHetongData(Vector v)
			throws BusinessException;

	/**
	 * 转正预警方法。
	 * 
	 * 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public abstract POIDataVO[] queryZhuanzhengData(Vector v)
			throws BusinessException;

	/**
	 * 此处插入方法描述。 创建日期：(2004-4-5 9:05:40)
	 */
	public abstract P2PAlartMessage sendMsgtoDeptMana(POIDataVO[] vos,
			String[] colNames, String[] strArrName, String strpk_psn,
			String csfilename, nc.vo.pub.pa.P2PAlartMessage p2pmesg)
			throws BusinessException;

	/**
	 * 此处插入方法描述。 创建日期：(2004-4-5 8:44:02)
	 * 
	 * @return nc.vo.pub.pa.P2PItem
	 * @param param
	 *            java.lang.String[]
	 * @param contents
	 *            java.lang.String[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public abstract nc.vo.pub.pa.P2PAlartMessage sendMsgtoPsn(
			String[] pk_psndocs, String[] contents, P2PAlartMessage p2pmesg,
			String csfilename) throws BusinessException;

	/**
	 * 此处插入方法描述。 创建日期：(2004-4-5 9:05:40)
	 */
	public abstract P2PAlartMessage sendMsgtoSpecifiedpsn(POIDataVO[] vos,
			String[] colNames, String[] strArrName, String csfilename,
			nc.vo.pub.pa.P2PAlartMessage p2pmesg) throws BusinessException;

	/**
	 * 此处插入方法描述。 创建日期：(2004-7-28 14:24:37)
	 * 
	 * @return nc.vo.pub.pa.P2PItem
	 * @param param1
	 *            int
	 * @param col
	 *            java.lang.String[][]
	 * @param p2pitem
	 *            nc.vo.pub.pa.P2PItem[][]
	 */
	public abstract P2PItem setP2PItem(POIDataVO[] vos, String[] colNames,
			int isubvosllength, String[][] colValues,
			nc.vo.hr.alert.UserAndClerkVO deptmanauserVO, String csfilename,
			P2PItem p2pitem) throws BusinessException;
	/**
	 * 签订合同预警方法。
	 * 
	 * 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hr.alert.POIDataVO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public abstract POIDataVO[] queryNotSignCtrtData(Vector v)
			throws BusinessException;
}