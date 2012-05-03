package nc.itf.hi;

import java.util.Vector;

import nc.vo.bd.b04.DeptdocVO;
import nc.vo.hi.hi_301.CtrlDeptVO;
import nc.vo.hi.hi_301.HRMainVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IHRhiQBS {

	/**
	 * 查询表名为setcode，所在公司为pk_corp的表的类型，如果该公司没有，使用集团类型。
	 * 创建日期：(2004-5-18 10:32:17)
	 * @return int
	 * @param setcode java.lang.String
	 * @param corp java.lang.String
	 */
	public int getSubsetType(String setcode, String pk_corp)
			throws BusinessException;

	/**
	 * 根据公司主键pk_corp，信息集类别setclass和主表标识ismainset 返回 所有的记录
	 * 使用联合查询,用到hi_setdict和hi_flddict表
	 */

	public nc.vo.hr.bd.setdict.SetdictVO[] queryAllSetdictDeptTbl(String pk_corp,String setclass, String setcode) 
			throws BusinessException ;
	/********************************************************************
	 * 根据查询条件，查询符合条件的文本                                 *
	 * 创建日期：(2003-5-7 14:25:09)                                    *
	 * @return 符合查询条件的文件的列表                                 *
	 * @param term 查询条件，查询语法为 “单词1 单词2 单词3 ...”，*
	 * 单词之间或者短语之间用空格分开，表示查询同时包含这些单词的文本。 *
	 ********************************************************************/
	public Vector searchFileByTerm(String term,String pk_corp) throws BusinessException ;
	/**
	 * 得到部门列表，参照使用
	 * 创建日期：(2004-7-27 8:33:02)
	 * @return nc.vo.bd.b04.DeptdocVO[]
	 * @param pk_corp java.lang.String
	 * @param whereport java.lang.String
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public DeptdocVO[] getDeptVOs(String pk_corp, String wherepart) throws BusinessException;
//	/**
//	 * 根据表名及字段名及主键返回字段值
//	 * 创建日期：(2004-7-27 8:33:02)
//	 * @return Object
//	 * @param tableName java.lang.String  表名
//	 * @param pkField java.lang.String 主键字段名
//	 * @param vField java.lang.String 字段名
//	 * @param pk Object  主键值
//	 * @exception java.rmi.RemoteException 异常说明。
//	 */
//	public Object getValue(String tableName, String pkField, String vField,Object pk) throws BusinessException;
	/**
	  * 判断table表中，主键是pkfield，主键为pk，目标字段是destField，目标值是dest，在条件where下是否重复
	  * 如果pk不为空，则不包含自身。
	  * 创建日期：(2004-5-17 10:30:00)
	  * @return boolean
	  * @param table java.lang.String
	  * @param pkfield java.lang.String
	  * @param pk java.lang.String
	  * @param destField java.lang.String
	  * @param dest java.lang.String
	  * @param where java.lang.String
	  */
	 public boolean recordExists(String table, String pkfield, String pk, String destField, String dest, String where) throws BusinessException;
	 /**
	  *  得到公司下与用户关联的所有子公司。
	  * 创建日期：(2004-5-19 11:31:26)
	  * @return java.lang.String[]
	  * @param corp java.lang.String
	  * @param userid java.lang.String
	  * @exception java.sql.SQLException 异常说明。
	  */
	 public Vector getAllChildCorp(String pk_corp, String userid,Boolean isRelaUser) throws BusinessException;
	 public boolean isTurnOverInf(String pk_psncl,String pk_corp) throws  BusinessException;
	 /**
	  * 根据查询条件获得要对应辅助信息。 --解决V30中问题编码为200507111204423395 和 200507210913444548中的同步问题
	  * 创建日期：(2005-7-13 10:21:02)
	  * @return java.lang.String
	  * @param tablename java.lang.String
	  * @param fldcode java.lang.String
	  * @param pk_psndoc java.lang.String
	  * @param chkformula java.lang.String
	  */
	 public String findItemInf(String tablename,String fldcode,String pk_psndoc,String chkformula)throws BusinessException;
	 /**
	  * 通过公司主键和数据采集人,用户自定义字段名字和类型返回所有已转入
	  * 档案标识为N的记录VO数组。
	  *
	  * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。
	  *			如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	  *			sql语句。
	  * 创建日期：(2002-3-19)
	  * @return nc.vo.hi.hi_301.PsndocMainVO[]
	  * @param unitCode int
	  * @exception java.sql.SQLException 异常说明。
	  */
	 public HRMainVO[] queryAllHRMainByWhereScope(nc.vo.hi.hi_301.Global301VarVO gloVar,int psnclscope,String[] defFieldNames,int[] defFieldTypes,boolean indocflag,
				BusinessFuncParser_sql funcParser)	throws BusinessException;
	 
	 /**
	  * 根据pk_psndoc字符串返回翻译过的HRMainVO[]
	  * @param pks
	  * @return HRMainVO[]
	  * @throws BusinessException
	  */
	 public HRMainVO[] queryHRMainNameByPKs(String pk_corp, String pks,
			BusinessFuncParser_sql funcParser) throws BusinessException;
		/**
		 * 根据人员pk,信息集字典vo获得人员信息集信息
		 *
		 * 创建日期：(2002-3-21)
		 * @return nc.vo.hi.hi_401.ChgbillVO[]
		 * @param chgbillVO nc.vo.hi.hi_401.ChgbillVO
		 * @param isAnd boolean 以与条件查询还是以或条件查询
		 * @exception java.sql.SQLException 异常说明。
		 */
		public PsnDataVO[] queryCorpPsndataNotTrans(String pk_psn,
			String tablename, String corpID, int condition)
			throws BusinessException;

	public CtrlDeptVO queryRelatCorps(String userID, String pk_corp,
			boolean isRelate) throws BusinessException;

	public HRMainVO[] queryAllByCorpOperScopeName(
			nc.vo.hi.hi_301.Global301VarVO gloVar, int psnclscode,
			nc.vo.pub.lang.UFBoolean b, nc.vo.pub.query.ConditionVO[] qvos,
			BusinessFuncParser_sql funcParser) throws BusinessException;

	public HRMainVO[] queryAllByCorpOperScopeName(
			nc.vo.hi.hi_301.Global301VarVO gloVar, int psnclscode,
			nc.vo.pub.lang.UFBoolean b, nc.vo.pub.query.ConditionVO[] qvos,
			String DepartId, BusinessFuncParser_sql funcParser)
			throws BusinessException;
	
	/**
	 * 根据新的查询模板，直接按照where条件取得HRMainVO[]
	 * @param istranslate 是否翻译
	 * @param tablenames 取数所需要的关联表
	 * @param wheresql 取数where条件
	 * @param pk_corp  公司主键条件（可以为空）
	 * @param departId  部门主键条件（可以为空）
	 * @param psnclscode  人员类别条件（可以为空）
	 * @return HRMainVO[]
	 * @throws BusinessException
	 * @author dusx
	 */
	public HRMainVO[] queryHRInfoByWhereSQL(boolean istranslate,String[] tablenames,String wheresql,String pk_corp,String departId,Integer psnclscode,
			BusinessFuncParser_sql funcParser)throws BusinessException ;

	/**
	 * @author :张钢
	 * @date:2009-06-01
	 * @description:文件类型描述
	 * @filename:ITrnQBS.java
	 * @typename:ITrnQBS
	 * @version:  V1.0，加入版本控制
	 * @motifydate:修改日期
	 * @motifyname:修改人
	 */
	public boolean checkPsnWorkDate(String pk_corp,String pk_psnbasdoc,UFDate beginDate,UFDate endDate) throws BusinessException;

	
}