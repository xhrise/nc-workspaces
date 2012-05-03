package nc.itf.hi;

import java.util.HashMap;

import nc.vo.bd.b06.PsndocVO;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_307.KeyPersonGrpVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;

public interface IKeyPerson {


	/**
	 * 封存关键人员 参数说明： String[] pk_psndoc 人员主键数组 需要封存的人员管理档案主键数组 UFDate sealdata
	 * 封存日期 pk_corp 封存人员公司
	 */
	public abstract String sealKeyPsns(String[] pk_psndoc, UFDate sealdata,
			String pk_corp, String pk_keypsn_group, Boolean isNeedChk)
			throws BusinessException; 
	
	
	/**
	 * 删除关键人员
	 * 参数说明：
	 * String[] pk_psndoc 人员主键数组  需要封存的人员管理档案主键数组
	 * pk_corp 封存人员公司
	 */
	public abstract void delKeyPsns(String[] pk_psndoc,String pk_corp,String pk_keypsn_group)throws BusinessException; 


	/**
	 * 查询关键人员组
	 * 	参数说明：
	 * 	pk_corp：公司主键
	 * 	bSeal：是否包含封存组
	 */
	public abstract KeyPersonGrpVO[] queryKeyPsnGroupVOs(String pk_corp,Boolean bSeal)throws BusinessException;


	/**
	 * 查询关键人员
	 * 	参数说明：
	 * 	pk_keypsngroup：关键人员组主键
	 * 	bSeal：是否包含已结束人员
	 * 
	 */
	public abstract GeneralVO[] queryKeyPsnVOsByCondition(String pk_keypsngroup,Boolean bSeal,String pk_corp,String[] corps,
			ConditionVO[] conditions, String listfield, String normalwheresql,Boolean bSealGroup)throws BusinessException;
	
	/**
	 * 增加关键人员组
	 * @param keypersongroup
	 * @return
	 */
	public abstract String insertKeyPersonGrp(KeyPersonGrpVO keypersongroup)throws BusinessException;
	
	/**
	 * 删除关键人员组
	 * @param pk_keypsngroup
	 * @throws BusinessException
	 */
	public abstract void deleteKeyPsnGroup(String pk_keypsngroup)throws BusinessException;
	/**
	 * 修改关键人员组
	 * @param keypersongroup
	 * @return
	 * @throws BusinessException
	 */
	public abstract String updateKeyPsnGroup(KeyPersonGrpVO keypersongroup)throws BusinessException;
	
	/**
	 * 增加人员组和关键人员关联,历史信息
	 * @param vos
	 * @param pk_keypsngroup
	 * @return
	 * @throws BusinessException
	 */
	public abstract  String[] insertKeyPsnHistory(GeneralVO[] vos,String pk_keypsngroup)throws BusinessException;
	/**
	 *  修改关键人员组时如果封存该人员组,则封存其下所有未封存的关键人员
	 * @param keypersongroup
	 * @param sealdate
	 * @throws BusinessException
	 */
	public abstract  void sealPsnGroupToKeyPsn(KeyPersonGrpVO keypersongroup,UFDate sealdate )throws BusinessException;
	
	/**
	 * 更新关键人员的显示顺序字段 
	 * @param pk_psndoc_subs
	 * @param keypsnshoworder
	 * @throws BusinessException
	 */
	public abstract  void updateKeyPsnShoworder(String[] pk_psndoc_subs, HashMap keypsnshoworder)throws BusinessException;
	
	/**
	 * 查询关键人员历史记录
	 * @param pk_psndoc
	 * @param pk_corp
	 * @param pk_psngroup
	 * @param psngroup
	 * @return
	 * @throws BusinessException
	 */
	public  abstract GeneralVO[] queryKeyPersonInfo(String pk_psndoc, String pk_corp,String pk_psngroup,boolean psngroup)throws BusinessException ;
	/**
	 * 查询参照人员
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public PsndocVO[] querybyCondition(String condition)throws BusinessException ;
	/**
	 * 查询该人员组下所有未封存的关键人员记录的开始日期
	 * @param pk_keypsn_group
	 * @return
	 * @throws BusinessException
	 */
	public String[] queryAllKeyPsnBegindate(String pk_keypsn_group)throws BusinessException;
	
	/**
	 * 查询关键人员
	 * 	参数说明：
	 * 	pk_keypsngroup：关键人员组主键
	 * 	bSeal：是否包含已结束人员
	 *
	 */
	public GeneralVO[] queryKeyPsnVOsByCondition_NewDLG(String pk_keypsngroup,
			Boolean bSeal, String pk_corp, String[] corps, String DLGwheresql,
			String[] DLGtables, String listfield, String normalwheresql,
			Boolean bSealGroup, BusinessFuncParser_sql funcParser,String orderbysql)
			throws BusinessException;
	
}
