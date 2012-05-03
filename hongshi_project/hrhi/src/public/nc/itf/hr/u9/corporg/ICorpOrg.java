package nc.itf.hr.u9.corporg;

import nc.vo.hr.u9.corporg.CorpContrastVO;
import nc.vo.hr.u9.corporg.CorpOrgNodeVO;
import nc.vo.hr.u9.corporg.DeptContrastVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * ��˾���Ŷ��սӿ�
 * @author fengwei
 *
 */
public interface ICorpOrg {
	
	/**
	 * �õ���¼��˾�����й����ӹ�˾���Ե�¼��˾Ϊ��
	 * @param pk_corp ��¼��˾����ѡ�й�˾����
	 * @param userID ��¼�û�����
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpOrgNodeVO[] queryCorps(String pk_corp, String userID) throws BusinessException; 
	
	/**
	 * ��ѯ��˾�����в���
	 * @param pk_corp ��¼��˾����ѡ�й�˾����
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpOrgNodeVO[] queryDepts(String pk_corp) throws BusinessException;
	
	/**
	 * ���ݵ�¼��˾��where������ѯ��˾���ӹ�˾
	 * @param userID
	 * @param pk_corp
	 * @param strWhereSQL
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpContrastVO[] queryCorpsByWhereSql(String userID, String pk_corp, String strWhereSQL) throws BusinessException;
	
	/**
	 * ���ݹ�˾��where������ѯ��˾�Ĳ���
	 * @param pk_corp
	 * @param strWhereSQL
	 * @return
	 * @throws BusinessException
	 */
	public abstract DeptContrastVO[] queryDeptsByWhereSql(String pk_corp, String strWhereSQL) throws BusinessException;
	
	/**
	 * У�鹫˾��������֯�����Ƿ��ظ�
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkOrgcode(SuperVO[] vos) throws BusinessException;
	
	/**
	 * У�鲿�Ŷ�������֯����Ͳ��ű����Ƿ��ظ�
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkDeptcode(SuperVO vos) throws BusinessException;

}