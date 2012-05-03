package nc.itf.hi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.vo.bd.b04.DeptdocVO;
import nc.vo.hi.hi_301.CtrlDeptVO;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_306.DocApplyHVO;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.msg.UserNameObject;
import nc.vo.pub.query.ConditionVO;
import nc.vo.sm.UserVO;
import nc.vo.sm.user.UserAndClerkVO;
import nc.vo.uap.rbac.RoleVO;



public interface IPsnInf {

	/**
	 * ���������û���ɫ
	 * @param selectVOs
	 * @throws BusinessException
	 */
	public abstract void batchInsertUserRoles(HashMap<String,RoleVO[]> userRoleMap,UserAndClerkVO[] userAndClerks) throws BusinessException;
	
	/**
	 * ���������û�
	 * @param selectVOs
	 * @throws BusinessException
	 */
	public abstract String[] batchInsertUsers(UserVO[] userVOs) throws BusinessException;

	/**
	 * ���������������Ϣ�� 
	 * �������ڣ�(2004-5-19 16:36:40)
	 * 
	 * @param psndoc
	 *            nc.vo.hi.hi_301.GeneralVO
	 * @param accpsndocVO
	 *            nc.vo.hi.hi_301.GeneralVO
	 * @throws nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public abstract void updateMain(GeneralVO psndoc, GeneralVO accpsndocVO,
			GeneralVO mainaddpsndocVO) throws BusinessException;
	
	/**
	 * �޸ķ�ҵ���Ӽ���recordnum
	 * v50
	 * @param pk_psnbasdoc
	 * @param Subsetdatas
	 */
	public abstract void updateSubsetRecordnum(String pk_psnbasdoc, String tablecode,
			CircularlyAccessibleValueObject[] Subsetdatas)
			throws BusinessException ;
	/**
	 * ����������Ա������Ϣ�û�
	 * @param pk_corp
	 * @param userids
	 * @param sendtype
	 * @return String ����hr_message��¼������
	 * @throws BusinessException
	 */
	public abstract String insertRecievers(String pk_corp, String userids,int sendtype)
			throws BusinessException;
			
			
	/**
	 * V35 add ��չ��updateAccpsndoc(GeneralVO accpsndocVO)����,
	 * ר������������Ϣ�Ӽ��������bd_psndoc bd_psnbasdoc������Ϣ
	 * @param hmRelVo 
	 * key:��ŵ��Ǳ���
	 * value:��ŵ���
	 * GeneralVO���󣺱��Ӧ�������ֶ�ֵ
	 * @throws nc.vo.pub.BusinessException
	 */
	public void updateRelTable(HashMap hmRelVo)
			throws BusinessException; ;
	
	/**
	 * ���¸�����Ա��Ϣ�� 
	 * �������ڣ�(2004-5-19 14:53:19)
	 * 
	 * @param accpsndocVO
	 *            nc.vo.hi.hi_301.GeneralVO
	 * @throws nc.vo.pub.BusinessException
	 */
	public void updateAccpsndoc(GeneralVO accpsndocVO,String pk_psndoc)
			throws BusinessException;
	
	/**
	 * �޸��ӱ��¼�� �������ڣ�(2004-8-6 9:28:24)
	 * @param tableCode
	 * @param pk_psndoc
	 * @param vo
	 * @throws BusinessException
	 */
	public void updateChild(String tableCode, String pk_psndoc, GeneralVO vo)
			throws BusinessException;
	/**
	 * ���������ɾ����¼��־
	 * @param pk_psndoc
	 * 				java.lang.String
	 * @param ifdelete
	 * 				boolean
	 * @throws nc.vo.pub.BusinessException
	 */
	public void updateDeleteFlag(String pk_psndoc,boolean ifdelete)
			throws BusinessException;
	
	
	/**
	 * ���¹����ĸ�����Ա��Ϣ��
	 * ���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	 * @param fields
	 * @param map
	 * @param tableCode
	 * @param accpsndocVO
	 * @return GeneralVO
	 * @throws BusinessException
	 */
	public GeneralVO updateAccRel(String[] fields, Hashtable map,
			String tableCode, GeneralVO accpsndocVO)
			throws BusinessException;
	
	/**
	 * ��ѯѡ����Ա�ĵ�ǰ�Ӽ�����
	 * ���ص� key = pk_psnbasdoc value = GeneralVO[] ��HashMap
	 * @param vPkPsnbasdocs
	 * 			java.util.Vector
	 * @param tablecode
	 * 			java.lang.String
	 * @return HashMap
	 * @throws nc.vo.pub.BusinessException
	 */
	public HashMap querySubInfo(Vector vPkPsnbasdocs,String tablecode)
			throws BusinessException;
	
	/**
	 * ��ѯ�û���ָ����˾��Ȩ�޵Ĳ��š�
	 * �������ڣ�(2004-10-19 9:08:23)
	 * 
	 * @return nc.vo.hi.hi_301.CtrlDeptVO
	 * @param userID
	 *            java.lang.String
	 * @param pk_corp
	 *            java.lang.String
	 * @param isRelate
	 *            boolean
	 * @exception nc.vo.pub.BusinessException
	 *                �쳣˵����
	 */
	public CtrlDeptVO queryRelatedDepts(String userID, String pk_corp,
			boolean isRelate) 
			throws BusinessException;
	/**
	 *  
	 * @param strParam
	 * @param type
	 * @param power
	 * @return nc.vo.hi.hi_301.GeneralVO[]
	 * @throws BusinessException
	 */
	public nc.vo.hi.hi_301.GeneralVO[] queryRefEmployees(String strParam,Integer type,String power) 
			throws BusinessException;
	
	/**
	 * �ɼ��ڵ��ѯ��Ա��¼��(����)
	 * 
	 * @param indocflag
	 * @param pk_corps
	 * @param conditions
	 * @param userid
	 * @param normalwheresql
	 * @return int
	 * @throws BusinessException
	 */
	public int queryRecordCountByCondition(boolean indocflag,
			String[] pk_corps, ConditionVO[] conditions, String loginCorp,
			String userid, String normalwheresql) throws BusinessException;
	
	
	/**
	 * ��ý�����
	 * @param pk_corp
	 * @return UserNameObject[]
	 * @throws BusinessException
	 */
	public UserNameObject[] getRecievers(String pk_corp,int usertype) throws BusinessException ;
	
	/**
	 * ά���ڵ��ѯ��Ա��Ϣ��¼������
	 * @param indocflag
	 * @param pk_corps
	 * @param conditions
	 * @param scope
	 * @param loginCorp
	 * @param userid
	 * @param jobtype
	 * @param normalwheresql
	 * @return int
	 * @throws BusinessException
	 */
	public int queryRecordCountByCondition(boolean indocflag,
			String[] pk_corps, ConditionVO[] conditions, int scope,
			String loginCorp, String userid, int jobtype, String normalwheresql)
			throws BusinessException;
	/**
	 * ͬ��λ������ѯ���ȣ���ְ�ȣ�
	 * @param table
	 * @param pk
	 * @param pk_corp
	 * @return int
	 * @throws BusinessException
	 */
	public int queryPsnCountByPk(String table,String pk,String pk_corp)
			throws BusinessException;
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param table
	 * @param isTraceTable
	 * @param islookhistory	
	 * @param loginPkcorp
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryPersonInfo(String pk_corp, String pk_psnbasdoc,String table,boolean isTraceTable,
										boolean islookhistory,String pk_psndoc,String loginPkcorp) throws BusinessException;
	/**
	 * ��ѯָ����ԱPK���Ӽ�����ļ�¼
	 * @param pk
	 * @param table
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryPersonInfo(String pk, String table)
			throws BusinessException; 

	/**
	 * ������Ա������������ѯ��Ա������������Ϣ
	 * @param pk_psndoc
	 * @return
	 * @throws BusinessException
	 */
	public GeneralVO[] queryPersonMainInfo(String[] pk_psndocs,String pk_corp) throws BusinessException; 
	/**
	 * ����������id��psnname��ѯ��Ա��Ϣ
	 * @param pk_psndoc
	 * @param id
	 * @param psnname
	 * @return GeneralVO
	 * @throws BusinessException
	 */
	public GeneralVO queryPsnInfo(String pk_psndoc, String id, String psnname)
			throws BusinessException;
	
	/**
	 * ��ѯָ�����ź͹�˾PK�Ĳ�����
	 * @param pk_deptdoc
	 * @param pk_corp
	 * @return CtrlDeptVO
	 * @throws BusinessException
	 */
	public CtrlDeptVO queryMgrDepts(String pk_deptdoc, String pk_corp,boolean includehrcanceled)
			throws BusinessException;
	
	/**
	 * ��ѯ��Ŀ�����б�
	 * @param pk_corp
	 * @param funcode
	 * @param queryScope
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryListItem(String pk_corp, String funcode,
			String queryScope) 
			throws BusinessException;
	
	/**
	 * ��ѯ�ܹ����ƵĲ���
	 * @param userID
	 * @param corpVO
	 * @param useDeptPower
	 * @return nc.vo.hi.hi_301.CtrlDeptVO
	 * @throws BusinessException
	 */
	public nc.vo.hi.hi_301.CtrlDeptVO queryCorpCtrlDepts(String userID,
			CtrlDeptVO corpVO, boolean useDeptPower,boolean includeHrCanceld)
			throws BusinessException;
	
	/**
	 * V31SP1 ��Ƭ��������
	 * @param pk_corp
	 * @param psnPK
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByPsnPK(String pk_corp, String psnPK)
			throws BusinessException;  
	
	/**
	 * ����������ѯ��Ա��Ϣ
	 * @param indocflag
	 * @param pk_corp
	 * @param conditions
	 * @param scope
	 * @param powerSql
	 * @param jobtype
	 * @param listfield
	 * @param normalwheresql
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByConditionDepts(boolean indocflag, String pk_corp,
			ConditionVO[] conditions, int scope, String powerSql, int jobtype,
			String listfield,String normalwheresql) 
			throws BusinessException; 
	
	/**
	 * �ɼ��ڵ㱣������Ա���ѯ��Ա��Ϣ
	 * @param pk_corp
	 * @param conditions
	 * @param powerSql
	 * @param listfield
	 * @param normalwheresql
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition(String pk_corp,
			ConditionVO[] conditions, String powerSql, String listfield,String normalwheresql)
			throws BusinessException; 
	
	/**
	 * �ɼ��ڵ��ѯ��Ա��Ϣ(����)
	 * @deprecated
	 * @param indocflag
	 * @param pk_corps
	 * @param conditions
	 * @param loginCorp
	 * @param userid
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition(boolean indocflag, String[] pk_corps,
			ConditionVO[] conditions, String loginCorp, String userid,
			String listfield, String normalwheresql, int recordcount)
			throws BusinessException;

	/**
	 * ά���ڵ��ѯ��Ա��Ϣ
	 * @param indocflag
	 * @param pk_corp
	 * @param conditions
	 * @param scope
	 * @param loginCorp
	 * @param userid
	 * @param jobtype
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition(boolean indocflag, String[] pk_corp,
			ConditionVO[] conditions, int scope, String loginCorp,
			String userid, int jobtype, String listfield,
			String normalwheresql, int recordcount) throws BusinessException;
	
	/**
	 * ά���ڵ��ѯ��Ա��Ϣ
	 * @param indocflag
	 * @param pk_corp
	 * @param conditions
	 * @param scope
	 * @param loginCorp
	 * @param userid
	 * @param jobtype
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @param isCheckDeptPower
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition(boolean indocflag, String[] pk_corp,
			ConditionVO[] conditions, int scope, String loginCorp,
			String userid, int jobtype, String listfield,
			String normalwheresql, int recordcount, boolean isCheckDeptPower)
			throws BusinessException;
	
	/**
	 * ��hi_flddict��hi_setdic���в�ѯbd_accpsndoc���ֶ�acc_fldcode�Ĺ����ֶ�
	 * @param corppk
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryAllRelatedTableField(String corppk)
			throws BusinessException; 
	
	/**
	 * �����õ���Աת�뵽��������
	 * @param psnvos
	 * @return int
	 * @throws BusinessException
	 */
	public int addRefEmployees(nc.vo.hi.hi_301.GeneralVO[] psnvos) 
			throws BusinessException; 
	
	/**
	 * �����޸�
	 * @param pk_psndocs
	 * @param tableCode
	 * @param fieldCode
	 * @param value
	 * @throws BusinessException
	 */
	public void batchUpdate(String[] pk_psndocs, String tableCode,
			String fieldCode, Object value,String modulcode,String pk_corp) 
			throws BusinessException; 
	/**
	 * ���֤��Ϊ�ռ���Ƿ��Ѿ�����������������֤��������
	 * @param accpsndocVO
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean checkBadlist(GeneralVO accpsndocVO)
			throws BusinessException; 

	/**
	 * ����Ƿ���ڼ�¼
	 * @param sql
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean isRecordExist(String sql) throws BusinessException;
	
	/**
	 * ����Ƿ���ڼ�¼
	 * @param sql
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean[] isRecordExists(String[] sqls) throws BusinessException;
	/**
	 * �жϴ�Ա���ڱ���˾�Ƿ����û���
	 * @param pk_psndoc
	 * @param pkCorp
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean checkUserClerk(String pk_psndoc, String pkCorp) 
			throws BusinessException;
	/**
	 * �õ�ָ����˾PK����Ա��Ϣ��У����Ϣ
	 * @param pk_corp
	 * @param conitmUniqueFields
	 * @param psnbasDocVO
	 * @return String
	 * @throws BusinessException
	 */
	public String dataUniqueValidate(String pk_corp,nc.vo.bd.psndoc.PsndocConsItmVO[] conitmUniqueFields,CircularlyAccessibleValueObject psnbasDocVO)
			throws BusinessException; 
	/**
	 * ɾ��ָ����������Ա������ͨ����Ա��ϢGeneralVO�ļ�¼
	 * @param tableCode
	 * @param pk_psndoc
	 * @param vo
	 * @throws BusinessException
	 */
	public void deleteChild(String tableCode, String pk_psndoc, GeneralVO vo)
			throws BusinessException; 
	
	/**
	 * �õ���ɾ����Ա������ϢPK��Ӧ����Ա����
	 * @param pk_psnbasdoc
	 * @return int
	 * @throws BusinessException
	 */
	public int deletePsnValidate(String pk_psnbasdoc)
			throws BusinessException;//throws RemoteException
	
	/**
	 * 
	 * @param tableCode
	 * @param pk_psndoc
	 * @throws BusinessException
	 */
	public void deletePsnData(String tableCode, String pk_psndoc)
			throws BusinessException;
	
	/**
	 * ɾ��ά���ڵ���Ա��¼
	 * @param psnvo
	 * @param pk_corp
	 * @throws BusinessException
	 */
	public void deletePersonDoc(GeneralVO psnvo, String pk_corp)
			throws BusinessException;

	/**
	 * ɾ���ɼ��ڵ���Ա�� 
	 * �������ڣ�(2004-12-3 16:23:41) 
	 * @param psnvo GeneralVO
	 * @param pk_corp  java.lang.String
	 * @exception BusinessException  �쳣˵����
	 */
	public void deletePersonnotinDoc(GeneralVO psnvo, String pk_corp)
			throws BusinessException;
	
	/**
	 * ɾ����Ƹ��Ա�� 
	 * �������ڣ�(2004-12-3 16:23:41) 
	 * @param psnvo GeneralVO
	 * @param pk_corp  java.lang.String
	 * @param hiretype  int 0 ��Ƹ��1 ��Ƹ
	 * @exception BusinessException  �쳣˵����
	 */
	public void deletePersonRehire(GeneralVO psnvo, String pk_corp,int hiretype)
			throws BusinessException;
	
	/**
	 * ��ѯ����pk_deptdoc�������Ӳ��ź�������
	 * @param pk_deptdoc
	 * @return DeptdocVO[]
	 * @throws BusinessException
	 */
	public DeptdocVO[] deptChildren(String pk_deptdoc,boolean includehrcanceled)
			throws BusinessException;
	/**
	 * ���˵���ģ�壬��ȥ���ɼ�ҳǩ������׷����Ϣ��
	 * @param corp
	 * @param newTempletVO
	 * @return Object[]
	 * @throws BusinessException
	 */
	public Object[] filter(String corp, BillTempletVO newTempletVO)
			throws BusinessException; 
	
	/**
	 * ���Զ��嵵����������VO
	 * @param pk_defdoclist
	 * @return nc.vo.bd.def.DefdoclistVO
	 * @throws BusinessException
	 */
	public nc.vo.bd.def.DefdoclistVO findDefdoclistByPk(String pk_defdoclist)
			throws BusinessException; 
	
	/**
	 * �õ�ָ����˾�������û�����
	 * @param corpPK
	 * @return String[]
	 * @throws BusinessException
	 */
	public String[] getAllUserCode(String corpPK)
			throws BusinessException; 
	
	
	
	/**
	 * ��ѯָ������ģ�������
	 * @param pk_corp
	 * @param userid
	 * @param nodecode
	 * @return String TempletID
	 * @throws BusinessException
	 */
	public String getBillTempletID(String pk_corp, String userid,
			String nodecode)
			throws BusinessException;//throws ...
	/**
	 * �õ���ָ����˾��ְ����Ա���ڵĹ�˾
	 * @param sql
	 * @return String[]
	 * @throws BusinessException
	 */
	public String[] queryDeptPowerBySql(String sql) 
			throws BusinessException; 
	/**
	 * ��������,��ѯ��ӡ����
	 * @param conditions
	 * @param hmPower
	 * @param corps
	 * @param indocflag
	 * @param scope
	 * @param jobtype
	 * @param normalwheresql
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] getExtendedData(ConditionVO[] conditions,
			HashMap hmPower, String[] corps, boolean indocflag, int scope,int jobtype,String listfield,
			String normalwheresql) throws BusinessException;
//	/**
//	 * �õ�����
//	 * @param cls
//	 * @param unitcode
//	 * @param initcode
//	 * @return
//	 * @throws BusinessException
//	 */
//	public Object getParam(Class cls,String unitcode,String initcode) throws BusinessException;
	/**
	 * ��ѯָ����˾PK�ͽڵ㹦�ܵ���Ȩ�޵��û�����
	 * @param pk_corp
	 * @param funcode
	 * @return nc.vo.pub.msg.UserNameObject[]
	 * @throws BusinessException
	 */
	public nc.vo.pub.msg.UserNameObject[] getPowerUserid(String pk_corp,String funcode)
			throws BusinessException; 
	
	/**
	 * �õ��û�����
	 * @param userids
	 * @return nc.vo.pub.msg.UserNameObject[]
	 * @throws BusinessException
	 */
	public nc.vo.pub.msg.UserNameObject[] getUserObj(String[] userids)
			throws BusinessException; 
	
	/**
	 * ����pk�õ�ĳ������ֶε�ֵ
	 * @param tableName
	 * @param pkField
	 * @param vField
	 * @param pk
	 * @return Object ֵ
	 * @throws BusinessException
	 */
	public Object getValue(String tableName, String pkField, String vField,
			Object pk) throws BusinessException; 
	
	/**
	 * ����ָ����ԱPK��ĳ�Ӽ���¼
	 * @param tableCode
	 * @param pk_psndoc
	 * @param vo
	 * @return String �����ֱ���γɵ�����pk
	 * @throws BusinessException
	 */
	public String insertChild(String tableCode, String pk_psndoc, GeneralVO vo)
			throws BusinessException;//
	/**
	 * ����������Ա�Ĺ�����Ϣ (hi_psndoc_ref)
	 * @param psndocVO
	 * @return String�����ļ�¼��pk
	 * @throws BusinessException
	 */
	public String insertHiRef(GeneralVO psndocVO,boolean isNeedAFirm) 
			throws BusinessException; 
 
	/**
	 * �������������Ա�Ĺ�����Ϣ (hi_psndoc_ref)
	 * @param psndocVOs
	 * @param isNeedAFirm
	 * @param billcodevo
	 * @return String
	 * @throws BusinessException
	 */
	public String insertHiRefs(GeneralVO[] psndocVOs,boolean isNeedAFirm,nc.vo.pub.billcodemanage.BillCodeObjValueVO billcodevo) throws BusinessException;
	/**
	 * ��������Ϣ��
	 * @param psndocVO
	 * @param accpsndocVO
	 * @param mainaddpsndocVO
	 * @param billcodevo
	 * @return String
	 * @throws BusinessException
	 */
	public String insertMain(GeneralVO psndocVO, GeneralVO accpsndocVO,
			GeneralVO mainaddpsndocVO,nc.vo.pub.billcodemanage.BillCodeObjValueVO billcodevo) 
			throws BusinessException; 
	/**
	 * V35 add ������������
	 * @param data
	 * @param tableCode
	 * @return String[]�����¼��pk����
	 * @throws BusinessExcepiotn
	 */
	public String[] insertTable(
			nc.vo.pub.CircularlyAccessibleValueObject[] data, String tableCode)
			throws BusinessException; 
	
	/**
	 * ת����Ա����,ͬʱ�� ��ְ�Ӽ� ������ͬ���� ����������
	 * @param psnList
	 * @param pk_psndocs
	 * @throws BusinessException
	 */
	public void intoDoc(GeneralVO[] psnList, String[] pk_psndocs,String userid)
			throws BusinessException; 
	/**
	 * ԭ�ӿ�
	 * ת����Ա����,ͬʱ�� ��ְ�Ӽ� ������ͬ���� ����������
	 * @param psnList
	 * @param pk_psndocs
	 * @throws BusinessException
	 */
	public void intoDoc(GeneralVO[] psnList, String[] pk_psndocs)
			throws BusinessException; 
	/**
	 * �����������ʾ�ֶ�
	 * @param pk_psndocs
	 * @throws BusinessException
	 */
	public void updateShoworder(String[] pk_psndocs,HashMap psnshoworder) 
			throws BusinessException;
	
	/**
	 * ���ҵ���Ӽ��Ƿ�����鿴��ʷ
	 * @param tablename
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean isTraceTableLookHistory(String tablename) throws BusinessException;
	
	/**
	 * �Ƿ������Ա�����ظ�����Ա
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean hasPerson() throws BusinessException;
	
	/**
	 * ������Ա�޸�����ʱ����н�����
	 * @param psndoc
	 * @param accpsndocVO
	 * @param mainaddpsndocVO
	 * @param logdate
	 * @throws BusinessException
	 */
	public void updateRefPersonMain(GeneralVO psndoc, GeneralVO accpsndocVO,GeneralVO mainaddpsndocVO,UFDate logdate) throws BusinessException;
	/**
	 * �ж���Ա����Ƿ�Ϊ����ְ
	 * @param pk_psncl
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean isOutdutyPsncl(String pk_psncl)throws BusinessException;

	
	/**
	 * ȡ���û���email��ַ
	 * @param recievers
	 * @return GeneralVO[]
	 */
	public GeneralVO[] getRecieverEmails(String recievers)throws BusinessException ;
	/**
	 * ����mail
	 * @param subject
	 * @param content
	 * @param emailAddress
	 */
	public void sendMail(String subject, String content, String[] emailAddress)throws BusinessException;

	/**
	 * �����޸���Ա����
	 * @param psnvos
	 * @throws BusinessException
	 */
	public void batchUpdatePsnCode(GeneralVO[] psnvos) throws BusinessException;
	
	/**
	 * ����������������ѯ��ְ���뵥
	 * @param table
	 * @param Where
	 * @return DocApplyHVO[] ��ְ���뵥����
	 * @throws BusinessException
	 */
	public  abstract  DocApplyHVO[] queryDocApplyBillByCon(String tableandWhere)throws BusinessException;
	
	/**
	 * ���鷵Ƹ��Ƹ��Ա
	 * @param pk_corp
	 * @param conitmUniqueFields
	 * @param psnbasDocVO
	 * @return GeneralVO
	 * @throws BusinessException
	 */
	public abstract GeneralVO checkRehirePerson(String pk_corp,
			nc.vo.bd.psndoc.PsndocConsItmVO[] conitmUniqueFields,
			CircularlyAccessibleValueObject psnbasDocVO,int returntype) throws BusinessException;
	

	/**
	 * �޸���Ա״̬
	 * @param pk_psndocs
	 * @throws BusinessException
	 */
	public abstract void updatePsnState(String[] pk_psndocs ,int state)throws BusinessException;

	/**
	 * У�����뵥����ʱ����Ա��Ϣ
	 * @param pk_psndocs
	 * @param bodypkMap
	 * @return String
	 * @throws BusinessException
	 */
	public String checkApplyPsn(String[] pk_psndocs,HashMap bodypkMap)throws BusinessException;
	/**
	 * ��ѯ���й�����˾
	 * @param userID
	 * @param pk_corp
	 * @param isRelate
	 * @return CtrlDeptVO[]
	 * @throws BusinessException
	 */
	public CtrlDeptVO[] queryAllRelatedCorps(String userID, String pk_corp,
			boolean isRelate) throws BusinessException;
	
	/**
	 * ����ҵ���Ӽ���¼����Ҫ����recordnum��lastflag 
	 * �÷������ڲ������ݼ�¼����ã��ýӿڸ�����¼�¼��recordnum��lastflag 
	 * @param tablecode  ҵ���Ӽ�����
	 * @param pk_psndoc_subs  �����Ӽ���¼��������
	 * @throws BusinessException
	 */
	public void afterInsertChild(String tablecode,String[] pk_psndoc_subs) throws BusinessException;
	/**
	 * ɾ��ҵ���Ӽ���¼  
	 * @param tablecode  ҵ���Ӽ�����
	 * @param pk_psndoc_sub  �Ӽ���¼����
	 * @throws BusinessException
	 */
	public void deleteChildSet(String tablecode,String[] pk_psndoc_sub) throws BusinessException;
	
	/**
	 * ��ѯ��ְ��Աҵ���Ӽ���Ϣ
	 * @param pk_psndoc
	 * @param pk_corp
	 * @param table
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryPartPersonChildInfo(String pk_psndoc, String pk_corp,String table)
		throws BusinessException;
	/**
	 * ������Ա�������������͹�˾������ѯ��Աbd_psndoc��Ϣ��ļ�¼
	 * @param pk
	 * @param table
	 * @param pk_corp
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryPersonInfo(String pk, String table,String pk_corp)
			throws BusinessException ;
	/**
	 * v55 add ���������û�
	 * @param vos
	 * @return Hashtable<��Ա�������Ƿ��Ÿ�����> 
	 * @throws BusinessException
	 */
	public Hashtable isNeedSelfUser(GeneralVO[] vos) throws BusinessException;

	/**
	 * ����ȡ����Ա����
	 * @param vos
	 * @throws BusinessException
	 */
	public void batchCancelRefPsn(GeneralVO[] vos) throws BusinessException;

	/**
	 * ��������Ӽ���¼��Ϣ
	 * @param tableCode
	 * @param pk_psndoc
	 * @param vos
	 * @param delPkPsndocSubs
	 * @throws BusinessException
	 */
	public void saveSubSetInfos(String tableCode, String pk_psndoc,
			GeneralVO[] vos,String[] delPkPsndocSubs) throws BusinessException;
	
	/**
	 * ��Ա��Ϣ�ɼ��ڵ�ʹ�õĲ�ѯ��Ա����
	 * @param indocflag
	 * @param pk_corp
	 * @param DLGwheresql
	 * @param DLGtables
	 * @param loginCorp
	 * @param userid
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @param funcParser
	 * @param ordrebyclause
	 * @param includesubcorp
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition_Collect(boolean indocflag,
			String pk_corp, String DLGwheresql, String[] DLGtables,
			String loginCorp, String userid, String listfield,
			String normalwheresql, int recordcount,
			BusinessFuncParser_sql funcParser,String ordrebyclause,boolean includesubcorp) throws BusinessException;

	/**
	 * ��Ա��Ϣ�ɼ��ڵ�ʹ�õĲ�ѯ��Ա��������
	 * @param indocflag
	 * @param pk_corp
	 * @param DLGwheresql
	 * @param DLGtables
	 * @param loginCorp
	 * @param userid
	 * @param normalwheresql
	 * @param funcParser
	 * @param includesubcorp
	 * @return int ��Ա����
	 * @throws BusinessException
	 */
	public int queryRecordCountByCondition_Collect(boolean indocflag,
			String pk_corp, String DLGwheresql, String[] DLGtables,
			String loginCorp, String userid, String normalwheresql,
			BusinessFuncParser_sql funcParser,boolean includesubcorp) throws BusinessException;

	/**
	 * ��Ա��Ϣά���ڵ�ʹ�õĲ�ѯ��Ա��������
	 * @param indocflag
	 * @param pk_corp
	 * @param DLGwheresql
	 * @param DLGtables
	 * @param scope
	 * @param loginCorp
	 * @param userid
	 * @param jobtype
	 * @param normalwheresql
	 * @param funcParser
	 * @param includesubcorp
	 * @return int ��Ա����
	 * @throws BusinessException
	 */
	public int queryRecordCountByCondition_Maintain(boolean indocflag,
			String pk_corp, String DLGwheresql, String[] DLGtables,
			int scope, String loginCorp, String userid, int jobtype,
			String normalwheresql, BusinessFuncParser_sql funcParser,boolean includesubcorp)
			throws BusinessException;

	/**
	 * ��Ա��Ϣά���ڵ�ʹ�õĲ�ѯ��Ա����
	 * @param indocflag
	 * @param pk_corp
	 * @param DLGwheresql
	 * @param DLGtables
	 * @param scope
	 * @param loginCorp
	 * @param userid
	 * @param jobtype
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @param funcParser
	 * @param orderbyclause
	 * @param includesubcorp
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition_Maintain(boolean indocflag,
			String pk_corp, String DLGwheresql, String[] DLGtables,
			int scope, String loginCorp, String userid, int jobtype,
			String listfield, String normalwheresql, int recordcount,
			BusinessFuncParser_sql funcParser,String orderbyclause,boolean includesubcorp) throws BusinessException;
	/**
	 * ��ѵģ����ʹ�õĲ�ѯ��
	 * @param indocflag
	 * @param pk_corps
	 * @param DLGwheresql
	 * @param DLGtables
	 * @param loginCorp
	 * @param userid
	 * @param listfield
	 * @param normalwheresql
	 * @param recordcount
	 * @param funcParser
	 * @param ordrebyclause
	 * @param isDeptPower
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryByCondition_Train(boolean indocflag,
			String[] pk_corps, String DLGwheresql, String[] DLGtables,
			String loginCorp, String userid, String listfield,
			String normalwheresql, int recordcount,
			BusinessFuncParser_sql funcParser,String ordrebyclause,boolean isDeptPower)
			throws BusinessException;

	/**
	 * �ؼ���Ա����ѡ������ʱ�Ĳ�ѯ������
	 */
	public GeneralVO[] queryByCondition_KeyPerson(boolean indocflag,
			String pk_corp, String DLGwheresql, String[] DLGtables,
			int scope, String loginCorp, String userid, int jobtype,
			String listfield, String normalwheresql, int recordcount,
			BusinessFuncParser_sql funcParser,String orderbyclause,boolean includesubcorp)
			throws BusinessException ;
	/**
	 * ��ѯָ����ԱPK���Ӽ�����ļ�¼
	 * @param pk
	 * @param pk_corp
	 * @param table
	 * @param funcParser
	 * @return GeneralVO[]
	 * @throws BusinessException
	 */
	public GeneralVO[] queryMainPersonInfo(String pk, String pk_corp,
			String table,
			BusinessFuncParser_sql funcParser) throws BusinessException; 
	
	/**
	 * ͨ��pkȡ��om_dumorg��Ϣ
	 * @param pk_om_dumorg
	 * @return GeneralVO
	 * @throws BusinessException
	 */
	public GeneralVO queryDetailForDumorg(String pk_om_dumorg)
			throws BusinessException;

	/**
	 * У��PsndocInf
	 * @param psninfVO
	 * @param pk_corp
	 * @return GeneralVO
	 * @throws BusinessException
	 */
	public GeneralVO validatePsndocInf(GeneralVO psninfVO, String pk_corp)
			throws BusinessException; 
	
	/**
	 * �õ���Ӧ��˾��Ȩ��sql,
	 * 2008.11.24���������ǲ鿴�¼���λ�����ܲ���Ȩ�޿��ƣ�����λ����¼��λ���ܲ���Ȩ������
	 * @param depttable
	 * @param userid
	 * @param pk_corp
	 * @param loginCorp
	 * @param includesubcorp
	 * @return String powersql
	 * @throws BusinessException
	 */
	public String getDeptPowerSqls(String depttable, String userid,
			String pk_corp,String loginCorp,boolean includesubcorp) throws BusinessException;
	/**
	 * ����Ƿ������δ��˵ģ���Ա��Ϣ��������ݡ�
	 * @param pk_psndocs
	 */
	public void checkIfExistsUnAuditBillofPSN(String[] pk_psndocs,String setcode) throws BusinessException;

	/**
	 * �������ͨ������Ա������GeneralVO
	 * @return
	 * @throws BusinessException 
	 * @throws BusinessException 
	 */
	public GeneralVO[] getIntoDocData(GeneralVO[] psnlistVOS )throws BusinessException;
}
