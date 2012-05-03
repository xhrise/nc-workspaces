package nc.itf.hi;

import nc.vo.hi.hi_303.PsnLiteInfoVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.pub.carddef.HrReportDataVO;
import nc.vo.hr.pub.carddef.RptAuthVO;
import nc.vo.hr.pub.carddef.RptDefVO;
import nc.vo.pub.BusinessException;

/**
 * HR�������õ���һЩҵ�����ݽӿ�
 * @author wangxing
 *
 */
public interface IHrRpt {
	
	/**
	 * ����������ѯ����Ա������Ϣ
	 * @param pk_corp ѡ�й�˾����
	 * @param pk_userid ��¼�û�����
	 * @param pk_corps Ҫ��ѯ�Ĺ�˾��������
	 * @param indocflag �Ƿ��������Ա����
	 * @param psnclscope ��Ա�������
	 * @param jobtype ��Ա��ְ����
	 * @param whereSQL ��ѯ����
	 * @param ischeckdeptpower �Ƿ��鲿��Ȩ��
	 * @param maxrecords ��󷵻�����
	 * @param tableNames �������漰���ı�������
	 * @param corpID ��¼��˾����
	 * @return
	 * @throws BusinessException
	 */
	PsnLiteInfoVO[] queryPsnLiteInfoVOsByCondition(
			String pk_corp, 
			String pk_userid, 
			String[] pk_corps, 
			boolean indocflag, 
			int psnclscope, 
			int jobtype, 
			String whereSQL, 
			boolean ischeckdeptpower,
			int maxrecords,
			String[] tableNames,
			BusinessFuncParser_sql funcParser,
			String corpID) throws BusinessException;
	
	/**
	 * ���һ�������ڱ���˾���ݿ����Ƿ�Ϸ�
	 * @param pk_corp
	 * @param rpt_code
	 * @param rpt_pk �Ѿ����ڵı���PK�������Լ��õ�
	 * @return
	 * @throws Exception
	 */
	boolean checkReportCodeValid(String pk_corp, String rpt_code, String rpt_pk) throws BusinessException;
	
	/**
	 * �����û������͹�˾�����õ�һ���û���Ȩ�޵ı���Ȩ�޶�������
	 * @param userID
	 * @param corpID
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthRptVOByPk(String userID, String corpID) throws BusinessException;
	
	/**
	 * �����û������͹�˾�����ͱ������͵õ�һ���û���Ȩ�޵ı���Ȩ�޶�������
	 * @param userID
	 * @param corpID
	 * @param rptType
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthRptVOByPk(String userID, String corpID, int rptType) throws BusinessException;
	
	/**
	 * ����������ѯ������֣����飬��������м���ʾ�ֶεĲ���
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	RptDefVO[] queryDefVOByCondition(String condition) throws BusinessException;
	
	/**
	 * ����������ѯ������֣����飬��������м���ʾ�ֶεĲ���
	 * @param condition
	 * @param includeRptData �Ƿ������������
	 * @return
	 * @throws BusinessException
	 */
	RptDefVO[] queryDefVOByCondition(String condition, boolean includeRptData) throws BusinessException;
	
	/**
	 * ����һ����������������Ӧʱ����ֶε�����
	 * @param rptDefVO
	 * @return
	 * @throws BusinessException
	 */
	String insertRptDefVO(RptDefVO rptDefVO) throws BusinessException;
	
	/**
	 * ɾ��һ����������󣬷������������ļ���
	 * �����ļ��е���������û������ļ����µ����б�������ȫ��Ȩ�޲ſ���ɾ��
	 * @param rptDefVO
	 * @throws BusinessException
	 */
	void deleteRptDefVO(RptDefVO rptDefVO, String corpID, String userID) throws BusinessException;
	
	/**
	 * ����������ѯ����Ȩ�ޣ֣����飬��������м���ʾ�ֶεĲ���
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthVOByCondition(int refType, String condition) throws BusinessException;
	
	/**
	 * ���ݱ���Ķ���һ���Բ�ѯ��������Ҫ�����ݵķ���
	 * ���������ڱ�������ʱ�������
	 * @param srcVOArray ������VO����
	 * @return �������ݵı�����֣�����
	 * @throws BusinessException
	 */
	HrReportDataVO[] queryHrReportByVO(HrReportDataVO[] srcVOArray,
			BusinessFuncParser_sql funcParser) throws BusinessException;
}
