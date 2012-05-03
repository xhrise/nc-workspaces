package nc.itf.hr.u9.psn;

import nc.vo.pub.BusinessException;

/**
 * 人员信息传输服务接口
 * @author fengwei
 *
 */
public interface IPersonADDSV {
	public static final int ADD = 1;
	
	public static final int MODIFY = 2;
	
	public static final int DELETE = 3;
	
	/**
	 * 同步人员信息
	 * @param pk_psndoc 人员管理主键
	 * @param operatetype 操作类型
	 * @param pk_corp 登录公司
	 * @throws BusinessException
	 */
	public abstract void operatePersonInfo(String[] pk_psndoc, int operatetype, String pk_corp) throws BusinessException;
	
	/**
	 * 删除人员同步
	 * @param psncode 人员编码
	 * @param operatetype 
	 * @param pk_corp
	 * @throws BusinessException
	 */
	public abstract void deletePersonInfo(String psncode, int operatetype, String pk_corp) throws BusinessException;

}
