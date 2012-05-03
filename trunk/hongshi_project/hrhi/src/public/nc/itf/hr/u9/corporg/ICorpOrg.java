package nc.itf.hr.u9.corporg;

import nc.vo.hr.u9.corporg.CorpContrastVO;
import nc.vo.hr.u9.corporg.CorpOrgNodeVO;
import nc.vo.hr.u9.corporg.DeptContrastVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 公司部门对照接口
 * @author fengwei
 *
 */
public interface ICorpOrg {
	
	/**
	 * 得到登录公司的所有关联子公司，以登录公司为根
	 * @param pk_corp 登录公司或者选中公司主键
	 * @param userID 登录用户主键
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpOrgNodeVO[] queryCorps(String pk_corp, String userID) throws BusinessException; 
	
	/**
	 * 查询公司的所有部门
	 * @param pk_corp 登录公司或者选中公司主键
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpOrgNodeVO[] queryDepts(String pk_corp) throws BusinessException;
	
	/**
	 * 根据登录公司及where条件查询公司及子公司
	 * @param userID
	 * @param pk_corp
	 * @param strWhereSQL
	 * @return
	 * @throws BusinessException
	 */
	public abstract CorpContrastVO[] queryCorpsByWhereSql(String userID, String pk_corp, String strWhereSQL) throws BusinessException;
	
	/**
	 * 根据公司及where条件查询公司的部门
	 * @param pk_corp
	 * @param strWhereSQL
	 * @return
	 * @throws BusinessException
	 */
	public abstract DeptContrastVO[] queryDeptsByWhereSql(String pk_corp, String strWhereSQL) throws BusinessException;
	
	/**
	 * 校验公司对照中组织编码是否重复
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkOrgcode(SuperVO[] vos) throws BusinessException;
	
	/**
	 * 校验部门对照中组织编码和部门编码是否重复
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkDeptcode(SuperVO vos) throws BusinessException;

}