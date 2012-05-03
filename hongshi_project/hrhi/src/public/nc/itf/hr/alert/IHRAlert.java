package nc.itf.hr.alert;

import java.util.Vector;
import nc.vo.hr.alert.POIDataVO;
import nc.vo.hr.alert.Pk_psnAndDeptmanaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pa.P2PAlartMessage;
import nc.vo.pub.pa.P2PItem;

public interface IHRAlert {

	/**
	 * �˴����뷽�������� �������ڣ�(2004-7-28 17:26:21)
	 * 
	 * @return nc.vo.hr.alert.Pk_psnAndDeptmanaVO[]
	 * @param param
	 *            nc.vo.hr.alert.Pk_psnAndDeptmanaVO
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public abstract Vector[] dealwithDeptmanaVOsOrdByPsn(
			nc.vo.hr.alert.UserAndClerkVO[] deptmanauserVOs)
			throws BusinessException;

	/**
	 * /** * ���������Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� *
	 * 
	 * @param ����˵�� *
	 * @return ����ֵ *
	 * @exception �쳣���� *
	 * @see ��Ҫ�μ����������� *
	 * @since �������һ���汾���˷�������ӽ���������ѡ�� *
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ�� *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public abstract Pk_psnAndDeptmanaVO[] findDeptMngrByPsnPK(
			String[] pk_psndocs) throws BusinessException;

	/**
	 * ����Ԥ��������
	 * 
	 * �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public abstract POIDataVO[] queryBirthdayData(Vector v)
			throws BusinessException;

	/**
	 * /** * ���������Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� *
	 * 
	 * @param ����˵�� *
	 * @return ����ֵ *
	 * @exception �쳣���� *
	 * @see ��Ҫ�μ����������� *
	 * @since �������һ���汾���˷�������ӽ���������ѡ�� *
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ�� *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public abstract nc.vo.hr.alert.UserAndClerkVO[] queryByClerkId(
			String[] pk_psndocs) throws BusinessException;

	/**
	 * /** * ���������Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� *
	 * 
	 * @param ����˵�� *
	 * @return ����ֵ *
	 * @exception �쳣���� *
	 * @see ��Ҫ�μ����������� *
	 * @since �������һ���汾���˷�������ӽ���������ѡ�� *
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ�� *-/
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public abstract nc.vo.hr.alert.UserAndClerkVO[] queryByManaClerkId(
			nc.vo.hr.alert.Pk_psnAndDeptmanaVO[] deptmanaVOs)
			throws BusinessException;

	/**
	 * ��ͬ����Ԥ��������
	 * 
	 * �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public abstract POIDataVO[] queryHetongData(Vector v)
			throws BusinessException;

	/**
	 * ת��Ԥ��������
	 * 
	 * �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public abstract POIDataVO[] queryZhuanzhengData(Vector v)
			throws BusinessException;

	/**
	 * �˴����뷽�������� �������ڣ�(2004-4-5 9:05:40)
	 */
	public abstract P2PAlartMessage sendMsgtoDeptMana(POIDataVO[] vos,
			String[] colNames, String[] strArrName, String strpk_psn,
			String csfilename, nc.vo.pub.pa.P2PAlartMessage p2pmesg)
			throws BusinessException;

	/**
	 * �˴����뷽�������� �������ڣ�(2004-4-5 8:44:02)
	 * 
	 * @return nc.vo.pub.pa.P2PItem
	 * @param param
	 *            java.lang.String[]
	 * @param contents
	 *            java.lang.String[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public abstract nc.vo.pub.pa.P2PAlartMessage sendMsgtoPsn(
			String[] pk_psndocs, String[] contents, P2PAlartMessage p2pmesg,
			String csfilename) throws BusinessException;

	/**
	 * �˴����뷽�������� �������ڣ�(2004-4-5 9:05:40)
	 */
	public abstract P2PAlartMessage sendMsgtoSpecifiedpsn(POIDataVO[] vos,
			String[] colNames, String[] strArrName, String csfilename,
			nc.vo.pub.pa.P2PAlartMessage p2pmesg) throws BusinessException;

	/**
	 * �˴����뷽�������� �������ڣ�(2004-7-28 14:24:37)
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
	 * ǩ����ͬԤ��������
	 * 
	 * �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hr.alert.POIDataVO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public abstract POIDataVO[] queryNotSignCtrtData(Vector v)
			throws BusinessException;
}