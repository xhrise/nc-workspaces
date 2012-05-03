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
	 * ��ѯ����Ϊsetcode�����ڹ�˾Ϊpk_corp�ı�����ͣ�����ù�˾û�У�ʹ�ü������͡�
	 * �������ڣ�(2004-5-18 10:32:17)
	 * @return int
	 * @param setcode java.lang.String
	 * @param corp java.lang.String
	 */
	public int getSubsetType(String setcode, String pk_corp)
			throws BusinessException;

	/**
	 * ���ݹ�˾����pk_corp����Ϣ�����setclass�������ʶismainset ���� ���еļ�¼
	 * ʹ�����ϲ�ѯ,�õ�hi_setdict��hi_flddict��
	 */

	public nc.vo.hr.bd.setdict.SetdictVO[] queryAllSetdictDeptTbl(String pk_corp,String setclass, String setcode) 
			throws BusinessException ;
	/********************************************************************
	 * ���ݲ�ѯ��������ѯ�����������ı�                                 *
	 * �������ڣ�(2003-5-7 14:25:09)                                    *
	 * @return ���ϲ�ѯ�������ļ����б�                                 *
	 * @param term ��ѯ��������ѯ�﷨Ϊ ������1 ����2 ����3 ...����*
	 * ����֮����߶���֮���ÿո�ֿ�����ʾ��ѯͬʱ������Щ���ʵ��ı��� *
	 ********************************************************************/
	public Vector searchFileByTerm(String term,String pk_corp) throws BusinessException ;
	/**
	 * �õ������б�����ʹ��
	 * �������ڣ�(2004-7-27 8:33:02)
	 * @return nc.vo.bd.b04.DeptdocVO[]
	 * @param pk_corp java.lang.String
	 * @param whereport java.lang.String
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public DeptdocVO[] getDeptVOs(String pk_corp, String wherepart) throws BusinessException;
//	/**
//	 * ���ݱ������ֶ��������������ֶ�ֵ
//	 * �������ڣ�(2004-7-27 8:33:02)
//	 * @return Object
//	 * @param tableName java.lang.String  ����
//	 * @param pkField java.lang.String �����ֶ���
//	 * @param vField java.lang.String �ֶ���
//	 * @param pk Object  ����ֵ
//	 * @exception java.rmi.RemoteException �쳣˵����
//	 */
//	public Object getValue(String tableName, String pkField, String vField,Object pk) throws BusinessException;
	/**
	  * �ж�table���У�������pkfield������Ϊpk��Ŀ���ֶ���destField��Ŀ��ֵ��dest��������where���Ƿ��ظ�
	  * ���pk��Ϊ�գ��򲻰�������
	  * �������ڣ�(2004-5-17 10:30:00)
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
	  *  �õ���˾�����û������������ӹ�˾��
	  * �������ڣ�(2004-5-19 11:31:26)
	  * @return java.lang.String[]
	  * @param corp java.lang.String
	  * @param userid java.lang.String
	  * @exception java.sql.SQLException �쳣˵����
	  */
	 public Vector getAllChildCorp(String pk_corp, String userid,Boolean isRelaUser) throws BusinessException;
	 public boolean isTurnOverInf(String pk_psncl,String pk_corp) throws  BusinessException;
	 /**
	  * ���ݲ�ѯ�������Ҫ��Ӧ������Ϣ�� --���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	  * �������ڣ�(2005-7-13 10:21:02)
	  * @return java.lang.String
	  * @param tablename java.lang.String
	  * @param fldcode java.lang.String
	  * @param pk_psndoc java.lang.String
	  * @param chkformula java.lang.String
	  */
	 public String findItemInf(String tablename,String fldcode,String pk_psndoc,String chkformula)throws BusinessException;
	 /**
	  * ͨ����˾���������ݲɼ���,�û��Զ����ֶ����ֺ����ͷ���������ת��
	  * ������ʶΪN�ļ�¼VO���顣
	  *
	  * ��֪���⣺��ע�����ɵ�sql��䣺where�Ӿ��м��蹫˾�����ֶ�Ϊpk_corp��
	  *			�����Ҫ��Թ�˾���в�ѯ����ôӦ�������ʵ���ֶ������ֹ��޸�
	  *			sql��䡣
	  * �������ڣ�(2002-3-19)
	  * @return nc.vo.hi.hi_301.PsndocMainVO[]
	  * @param unitCode int
	  * @exception java.sql.SQLException �쳣˵����
	  */
	 public HRMainVO[] queryAllHRMainByWhereScope(nc.vo.hi.hi_301.Global301VarVO gloVar,int psnclscope,String[] defFieldNames,int[] defFieldTypes,boolean indocflag,
				BusinessFuncParser_sql funcParser)	throws BusinessException;
	 
	 /**
	  * ����pk_psndoc�ַ������ط������HRMainVO[]
	  * @param pks
	  * @return HRMainVO[]
	  * @throws BusinessException
	  */
	 public HRMainVO[] queryHRMainNameByPKs(String pk_corp, String pks,
			BusinessFuncParser_sql funcParser) throws BusinessException;
		/**
		 * ������Աpk,��Ϣ���ֵ�vo�����Ա��Ϣ����Ϣ
		 *
		 * �������ڣ�(2002-3-21)
		 * @return nc.vo.hi.hi_401.ChgbillVO[]
		 * @param chgbillVO nc.vo.hi.hi_401.ChgbillVO
		 * @param isAnd boolean ����������ѯ�����Ի�������ѯ
		 * @exception java.sql.SQLException �쳣˵����
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
	 * �����µĲ�ѯģ�壬ֱ�Ӱ���where����ȡ��HRMainVO[]
	 * @param istranslate �Ƿ���
	 * @param tablenames ȡ������Ҫ�Ĺ�����
	 * @param wheresql ȡ��where����
	 * @param pk_corp  ��˾��������������Ϊ�գ�
	 * @param departId  ������������������Ϊ�գ�
	 * @param psnclscode  ��Ա�������������Ϊ�գ�
	 * @return HRMainVO[]
	 * @throws BusinessException
	 * @author dusx
	 */
	public HRMainVO[] queryHRInfoByWhereSQL(boolean istranslate,String[] tablenames,String wheresql,String pk_corp,String departId,Integer psnclscode,
			BusinessFuncParser_sql funcParser)throws BusinessException ;

	/**
	 * @author :�Ÿ�
	 * @date:2009-06-01
	 * @description:�ļ���������
	 * @filename:ITrnQBS.java
	 * @typename:ITrnQBS
	 * @version:  V1.0������汾����
	 * @motifydate:�޸�����
	 * @motifyname:�޸���
	 */
	public boolean checkPsnWorkDate(String pk_corp,String pk_psnbasdoc,UFDate beginDate,UFDate endDate) throws BusinessException;

	
}