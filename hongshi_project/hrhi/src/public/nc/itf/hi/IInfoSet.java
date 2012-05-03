package nc.itf.hi;


import java.util.HashMap;

import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hi.hi_401.SetdictVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IInfoSet {

	/**
	 * 删除最后一条记录 创建日期：(2003-8-5 9:12:16)
	 * 
	 * @param lastvo
	 *            nc.vo.hi.hi_401.PsnDataVO
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void deleteLastRecord(PsnDataVO lastvo)
			throws nc.vo.pub.BusinessException;

	/**
	 * 批量删除信息子集。 创建日期：(2004-8-24 9:57:02)
	 * 
	 * @param psndataVOs
	 *            nc.vo.hi.hi_401.PsnDataVO[]
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void deletePsnDataRecords(PsnDataVO[] psndataVOs)
			throws nc.vo.pub.BusinessException;

	/**
	 * 查询岗位的部门和公司
	 * 
	 * 创建日期：(2002-6-12 9:38:01)
	 * 
	 * @return java.lang.String[]
	 * @param id
	 *            java.lang.String
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String[] findPostdocByID(String id)
			throws nc.vo.pub.BusinessException;

	/**
	 * 是否此员工还存在
	 * 
	 * 创建日期：(2002-6-5 11:20:49)
	 * 
	 * @return boolean
	 * @param pk
	 *            java.lang.String
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public boolean isExistPsn(String pk_psndoc)
			throws nc.vo.pub.BusinessException;

	/**
	 * 把指定序号后的字段前移 创建日期：(2002-4-11 8:58:12)
	 *  
	 */
	public void movePre(String tblName, String pk_psndoc, int num)
			throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryBusiRefValue(String refitempk, String type)
			throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public PsnDataVO[] queryDataArrayByPsn(String psnpk, SetdictVO setdict,
			int condition) throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryDeptByJob(String jobpk)
			throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryDeptByPsnsubpk(String jobpk)
			throws nc.vo.pub.BusinessException;

	/**
	 * V35 add
	 * @param refitemName
	 * @param type
	 * @param hmReftype
	 * @return
	 * @throws nc.vo.pub.BusinessException
	 */
	public String queryDocRefPK(String refitemName, String type,
			HashMap hmReftype) throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryDocRefValue(String refitempk, String type)
			throws nc.vo.pub.BusinessException;
	
	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryDocRefValue(String refitempk, String type,HashMap hmReftype)
			throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public int queryRecordNum(PsnDataVO psndatavo)
			throws nc.vo.pub.BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public String queryRefValue(String refitempk)
			throws nc.vo.pub.BusinessException;

	/**
	 * 跨公司调入处理时将人员的兼职记录修改为新公司 创建日期：(2002-4-11 8:58:12)
	 *  
	 */
	public void updateDeptForCrossCorp(String strNewPsndoc, String pk_corp,
			String pk_psnbasdoc) throws nc.vo.pub.BusinessException;

	/**
	 * 把指定序号的记录的lastflag设置成lastflag 创建日期：(2002-4-11 8:58:12)
	 *  
	 */
	public void updateCtrtContstate(String pk_psndoc, int beginnum, int endnum,
			int istate) throws nc.vo.pub.BusinessException;

	/**
	 * 用一个VO对象的属性更新数据库中的值。 更新时不维护lastflag、enddate内容 创建日期：(2002-3-23)
	 * 
	 * @param psndocGroupdef10
	 *            nc.vo.hi.hi_401.PsndocGroupdef10VO
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void updateData(PsnDataVO data) throws nc.vo.pub.BusinessException;

	/**
	 * 用一个VO对象的属性更新数据库中的值。 更新时不维护lastflag、enddate内容 创建日期：(2002-3-23)
	 * 
	 * @param psndocGroupdef10
	 *            nc.vo.hi.hi_401.PsndocGroupdef10VO
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void updateData(PsnDataVO data, PsnDataVO dataname)
			throws nc.vo.pub.BusinessException;

	/**
	 * 把指定序号的记录的lastflag设置成lastflag 创建日期：(2002-4-11 8:58:12)
	 *  
	 */
	public void updateLastflag(String tblName, String pk_psndoc, int num,
			String lastflag) throws nc.vo.pub.BusinessException;

	/**
	 * V35 add
	 * @param refitempk
	 * @param hmRefType
	 * @return
	 * @throws nc.vo.pub.BusinessException
	 */
	public String queryBusiRefValue(String refitempk, String reftype,
			HashMap hmRefType) throws nc.vo.pub.BusinessException;

	/**
	 * V35 add
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public HashMap getRefTypeHashAll() throws nc.vo.pub.BusinessException;
	public String queryJobByPsnsubpk(String jobpk) throws nc.vo.pub.BusinessException;
	public PsnDataVO queryLastData(PsnDataVO psndatavo, int num) throws nc.vo.pub.BusinessException;
	public String queryDocRefPK(String refitemName, String type) throws nc.vo.pub.BusinessException;
	public String queryRefPK(String refitemName, String PK_type) throws nc.vo.pub.BusinessException;
	/**
     * 删除表中的一条记录 创建日期：(2002-4-11 8:58:12)
     * 
     * @param psndataVO
     *            nc.vo.hi.hi_401.PsnDataVO
     */
    public void deletePsnDataRecord(PsnDataVO psndataVO) throws nc.vo.pub.BusinessException;
    /**
     * 把指定表的历史记录lastflag标志更新成"N" 创建日期：(2002-4-11 8:58:12)
     *  
     */
    public void clearLastflag(String tblName, String pk_psndoc) throws BusinessException;
    /**
     * 把指定序号后的字段后移 创建日期：(2002-4-11 8:58:12)
     *  
     */
    public void moveNext(String tblName, String pk_psndoc, int num) throws BusinessException;
    /**
     * 如果倒数第二条记录的结束日期为空更新enddate 创建日期：(2002-4-11 8:58:12)
     *  
     */
    public void updateLastSecondEnddateNoCheck(String tblName,String pk_psndoc, UFDate date) throws BusinessException;
}