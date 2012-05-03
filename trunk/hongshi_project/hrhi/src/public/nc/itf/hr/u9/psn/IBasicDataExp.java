package nc.itf.hr.u9.psn;

import nc.vo.hr.u9.psn.BasicDataExpEmpRecVO;
import nc.vo.hr.u9.psn.BasicDataExpPsnbasdocVO;
import nc.vo.hr.u9.psn.BasicDataExpPsnclVO;
import nc.vo.hr.u9.psn.BasicDataExpPsndocVO;
import nc.vo.hr.u9.psn.BasicDataExpDutyVO;
import nc.vo.hr.u9.psn.BasicDataExpJobVO;
import nc.vo.pub.BusinessException;

/**
 * �������ݵ��� �ӿ���
 * @author fengwei
 *
 */
public interface IBasicDataExp {
	/**
	 * ��ѯ��λ��Ϣ
	 * @param whereSQL
	 * @return
	 * @throws BusiBeanException
	 */
	public BasicDataExpJobVO[] queryJobBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * ��ѯְ����Ϣ
	 * @param whereSQl
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpDutyVO[] queryDutyBySQL(String whereSQl, String pk_corp) throws BusinessException;
	
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param whereSQL
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsnbasdocVO[] queryPsnbasdocBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * ���ݵ�¼��˾��ѯԱ���Ĺ�����¼
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsndocVO[] queryJobRecord(String pk_corp, String whereSQL) throws BusinessException;
	
	/**
	 * ��ѯ��Ա�����Ϣ
	 * @param whereSQL
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsnclVO[] queryPsnclassBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * ��ѯԱ����ְ��¼
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpEmpRecVO[] queryEmployeeRecord(String pk_corp, String whereSQL) throws BusinessException;

}
