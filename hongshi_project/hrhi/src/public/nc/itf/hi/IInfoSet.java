package nc.itf.hi;


import java.util.HashMap;

import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.hi.hi_401.SetdictVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IInfoSet {

	/**
	 * ɾ�����һ����¼ �������ڣ�(2003-8-5 9:12:16)
	 * 
	 * @param lastvo
	 *            nc.vo.hi.hi_401.PsnDataVO
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public void deleteLastRecord(PsnDataVO lastvo)
			throws nc.vo.pub.BusinessException;

	/**
	 * ����ɾ����Ϣ�Ӽ��� �������ڣ�(2004-8-24 9:57:02)
	 * 
	 * @param psndataVOs
	 *            nc.vo.hi.hi_401.PsnDataVO[]
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public void deletePsnDataRecords(PsnDataVO[] psndataVOs)
			throws nc.vo.pub.BusinessException;

	/**
	 * ��ѯ��λ�Ĳ��ź͹�˾
	 * 
	 * �������ڣ�(2002-6-12 9:38:01)
	 * 
	 * @return java.lang.String[]
	 * @param id
	 *            java.lang.String
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String[] findPostdocByID(String id)
			throws nc.vo.pub.BusinessException;

	/**
	 * �Ƿ��Ա��������
	 * 
	 * �������ڣ�(2002-6-5 11:20:49)
	 * 
	 * @return boolean
	 * @param pk
	 *            java.lang.String
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public boolean isExistPsn(String pk_psndoc)
			throws nc.vo.pub.BusinessException;

	/**
	 * ��ָ����ź���ֶ�ǰ�� �������ڣ�(2002-4-11 8:58:12)
	 *  
	 */
	public void movePre(String tblName, String pk_psndoc, int num)
			throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String queryBusiRefValue(String refitempk, String type)
			throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public PsnDataVO[] queryDataArrayByPsn(String psnpk, SetdictVO setdict,
			int condition) throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String queryDeptByJob(String jobpk)
			throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
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
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String queryDocRefValue(String refitempk, String type)
			throws nc.vo.pub.BusinessException;
	
	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String queryDocRefValue(String refitempk, String type,HashMap hmReftype)
			throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public int queryRecordNum(PsnDataVO psndatavo)
			throws nc.vo.pub.BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 * 
	 * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp�� �����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	 * sql��䡣 �������ڣ�(2002-3-23)
	 * 
	 * @return nc.vo.hi.hi_401.PsndocCorpdef10VO[]
	 * @param unitCode
	 *            int
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public String queryRefValue(String refitempk)
			throws nc.vo.pub.BusinessException;

	/**
	 * �繫˾���봦��ʱ����Ա�ļ�ְ��¼�޸�Ϊ�¹�˾ �������ڣ�(2002-4-11 8:58:12)
	 *  
	 */
	public void updateDeptForCrossCorp(String strNewPsndoc, String pk_corp,
			String pk_psnbasdoc) throws nc.vo.pub.BusinessException;

	/**
	 * ��ָ����ŵļ�¼��lastflag���ó�lastflag �������ڣ�(2002-4-11 8:58:12)
	 *  
	 */
	public void updateCtrtContstate(String pk_psndoc, int beginnum, int endnum,
			int istate) throws nc.vo.pub.BusinessException;

	/**
	 * ��һ��VO��������Ը������ݿ��е�ֵ�� ����ʱ��ά��lastflag��enddate���� �������ڣ�(2002-3-23)
	 * 
	 * @param psndocGroupdef10
	 *            nc.vo.hi.hi_401.PsndocGroupdef10VO
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public void updateData(PsnDataVO data) throws nc.vo.pub.BusinessException;

	/**
	 * ��һ��VO��������Ը������ݿ��е�ֵ�� ����ʱ��ά��lastflag��enddate���� �������ڣ�(2002-3-23)
	 * 
	 * @param psndocGroupdef10
	 *            nc.vo.hi.hi_401.PsndocGroupdef10VO
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public void updateData(PsnDataVO data, PsnDataVO dataname)
			throws nc.vo.pub.BusinessException;

	/**
	 * ��ָ����ŵļ�¼��lastflag���ó�lastflag �������ڣ�(2002-4-11 8:58:12)
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
     * ɾ�����е�һ����¼ �������ڣ�(2002-4-11 8:58:12)
     * 
     * @param psndataVO
     *            nc.vo.hi.hi_401.PsnDataVO
     */
    public void deletePsnDataRecord(PsnDataVO psndataVO) throws nc.vo.pub.BusinessException;
    /**
     * ��ָ�������ʷ��¼lastflag��־���³�"N" �������ڣ�(2002-4-11 8:58:12)
     *  
     */
    public void clearLastflag(String tblName, String pk_psndoc) throws BusinessException;
    /**
     * ��ָ����ź���ֶκ��� �������ڣ�(2002-4-11 8:58:12)
     *  
     */
    public void moveNext(String tblName, String pk_psndoc, int num) throws BusinessException;
    /**
     * ��������ڶ�����¼�Ľ�������Ϊ�ո���enddate �������ڣ�(2002-4-11 8:58:12)
     *  
     */
    public void updateLastSecondEnddateNoCheck(String tblName,String pk_psndoc, UFDate date) throws BusinessException;
}