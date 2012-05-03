package nc.itf.hi;

import nc.vo.hi.hi_303.PsnLiteInfoVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.pub.carddef.HrReportDataVO;
import nc.vo.hr.pub.carddef.RptAuthVO;
import nc.vo.hr.pub.carddef.RptDefVO;
import nc.vo.pub.BusinessException;

/**
 * HR报表所用到的一些业务数据接口
 * @author wangxing
 *
 */
public interface IHrRpt {
	
	/**
	 * 根据条件查询出人员简明信息
	 * @param pk_corp 选中公司主键
	 * @param pk_userid 登录用户主键
	 * @param pk_corps 要查询的公司主键数组
	 * @param indocflag 是否进入了人员档案
	 * @param psnclscope 人员归属类别
	 * @param jobtype 人员任职类型
	 * @param whereSQL 查询条件
	 * @param ischeckdeptpower 是否检查部门权限
	 * @param maxrecords 最大返回行数
	 * @param tableNames 条件中涉及到的表名数组
	 * @param corpID 登录公司主键
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
	 * 检查一个编码在本公司数据库中是否合法
	 * @param pk_corp
	 * @param rpt_code
	 * @param rpt_pk 已经存在的报表PK，过滤自己用的
	 * @return
	 * @throws Exception
	 */
	boolean checkReportCodeValid(String pk_corp, String rpt_code, String rpt_pk) throws BusinessException;
	
	/**
	 * 根据用户主键和公司主键得到一个用户有权限的报表权限对象数组
	 * @param userID
	 * @param corpID
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthRptVOByPk(String userID, String corpID) throws BusinessException;
	
	/**
	 * 根据用户主键和公司主键和报表类型得到一个用户有权限的报表权限对象数组
	 * @param userID
	 * @param corpID
	 * @param rptType
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthRptVOByPk(String userID, String corpID, int rptType) throws BusinessException;
	
	/**
	 * 根据条件查询报表定义ＶＯ数组，并且完成中间显示字段的补冲
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	RptDefVO[] queryDefVOByCondition(String condition) throws BusinessException;
	
	/**
	 * 根据条件查询报表定义ＶＯ数组，并且完成中间显示字段的补冲
	 * @param condition
	 * @param includeRptData 是否包含报表数据
	 * @return
	 * @throws BusinessException
	 */
	RptDefVO[] queryDefVOByCondition(String condition, boolean includeRptData) throws BusinessException;
	
	/**
	 * 插入一个报表定义对象，完成相应时间戳字段的设置
	 * @param rptDefVO
	 * @return
	 * @throws BusinessException
	 */
	String insertRptDefVO(RptDefVO rptDefVO) throws BusinessException;
	
	/**
	 * 删除一个报表定义对象，分情况处理报表和文件夹
	 * 对于文件夹的情况必须用户对于文件夹下的所有报表都具有全部权限才可以删除
	 * @param rptDefVO
	 * @throws BusinessException
	 */
	void deleteRptDefVO(RptDefVO rptDefVO, String corpID, String userID) throws BusinessException;
	
	/**
	 * 根据条件查询报表权限ＶＯ数组，并且完成中间显示字段的补冲
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	RptAuthVO[] queryAuthVOByCondition(int refType, String condition) throws BusinessException;
	
	/**
	 * 根据报表的定义一次性查询报表所需要的数据的方法
	 * 本方法用于报表运行时获得数据
	 * @param srcVOArray 报表定义VO数组
	 * @return 存有数据的报表定义ＶＯ数组
	 * @throws BusinessException
	 */
	HrReportDataVO[] queryHrReportByVO(HrReportDataVO[] srcVOArray,
			BusinessFuncParser_sql funcParser) throws BusinessException;
}
