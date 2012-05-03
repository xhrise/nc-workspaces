package nc.itf.hr.u9.psn;

import nc.vo.hr.u9.psn.BasicDataExpEmpRecVO;
import nc.vo.hr.u9.psn.BasicDataExpPsnbasdocVO;
import nc.vo.hr.u9.psn.BasicDataExpPsnclVO;
import nc.vo.hr.u9.psn.BasicDataExpPsndocVO;
import nc.vo.hr.u9.psn.BasicDataExpDutyVO;
import nc.vo.hr.u9.psn.BasicDataExpJobVO;
import nc.vo.pub.BusinessException;

/**
 * 基础数据导出 接口类
 * @author fengwei
 *
 */
public interface IBasicDataExp {
	/**
	 * 查询岗位信息
	 * @param whereSQL
	 * @return
	 * @throws BusiBeanException
	 */
	public BasicDataExpJobVO[] queryJobBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * 查询职务信息
	 * @param whereSQl
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpDutyVO[] queryDutyBySQL(String whereSQl, String pk_corp) throws BusinessException;
	
	/**
	 * 查询人员信息
	 * @param whereSQL
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsnbasdocVO[] queryPsnbasdocBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * 根据登录公司查询员工的工作记录
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsndocVO[] queryJobRecord(String pk_corp, String whereSQL) throws BusinessException;
	
	/**
	 * 查询人员类别信息
	 * @param whereSQL
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpPsnclVO[] queryPsnclassBySQL(String whereSQL, String pk_corp) throws BusinessException;
	
	/**
	 * 查询员工任职记录
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	public BasicDataExpEmpRecVO[] queryEmployeeRecord(String pk_corp, String whereSQL) throws BusinessException;

}
