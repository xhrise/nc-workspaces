package nc.itf.hr.u9.psn;

import nc.vo.pub.BusinessException;

/**
 * ��Ա��Ϣ�������ӿ�
 * @author fengwei
 *
 */
public interface IPersonADDSV {
	public static final int ADD = 1;
	
	public static final int MODIFY = 2;
	
	public static final int DELETE = 3;
	
	/**
	 * ͬ����Ա��Ϣ
	 * @param pk_psndoc ��Ա��������
	 * @param operatetype ��������
	 * @param pk_corp ��¼��˾
	 * @throws BusinessException
	 */
	public abstract void operatePersonInfo(String[] pk_psndoc, int operatetype, String pk_corp) throws BusinessException;
	
	/**
	 * ɾ����Աͬ��
	 * @param psncode ��Ա����
	 * @param operatetype 
	 * @param pk_corp
	 * @throws BusinessException
	 */
	public abstract void deletePersonInfo(String psncode, int operatetype, String pk_corp) throws BusinessException;

}
