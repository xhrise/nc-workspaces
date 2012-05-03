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
	 * ���ؼ���Ա ����˵���� String[] pk_psndoc ��Ա�������� ��Ҫ������Ա�������������� UFDate sealdata
	 * ������� pk_corp �����Ա��˾
	 */
	public abstract String sealKeyPsns(String[] pk_psndoc, UFDate sealdata,
			String pk_corp, String pk_keypsn_group, Boolean isNeedChk)
			throws BusinessException; 
	
	
	/**
	 * ɾ���ؼ���Ա
	 * ����˵����
	 * String[] pk_psndoc ��Ա��������  ��Ҫ������Ա��������������
	 * pk_corp �����Ա��˾
	 */
	public abstract void delKeyPsns(String[] pk_psndoc,String pk_corp,String pk_keypsn_group)throws BusinessException; 


	/**
	 * ��ѯ�ؼ���Ա��
	 * 	����˵����
	 * 	pk_corp����˾����
	 * 	bSeal���Ƿ���������
	 */
	public abstract KeyPersonGrpVO[] queryKeyPsnGroupVOs(String pk_corp,Boolean bSeal)throws BusinessException;


	/**
	 * ��ѯ�ؼ���Ա
	 * 	����˵����
	 * 	pk_keypsngroup���ؼ���Ա������
	 * 	bSeal���Ƿ�����ѽ�����Ա
	 * 
	 */
	public abstract GeneralVO[] queryKeyPsnVOsByCondition(String pk_keypsngroup,Boolean bSeal,String pk_corp,String[] corps,
			ConditionVO[] conditions, String listfield, String normalwheresql,Boolean bSealGroup)throws BusinessException;
	
	/**
	 * ���ӹؼ���Ա��
	 * @param keypersongroup
	 * @return
	 */
	public abstract String insertKeyPersonGrp(KeyPersonGrpVO keypersongroup)throws BusinessException;
	
	/**
	 * ɾ���ؼ���Ա��
	 * @param pk_keypsngroup
	 * @throws BusinessException
	 */
	public abstract void deleteKeyPsnGroup(String pk_keypsngroup)throws BusinessException;
	/**
	 * �޸Ĺؼ���Ա��
	 * @param keypersongroup
	 * @return
	 * @throws BusinessException
	 */
	public abstract String updateKeyPsnGroup(KeyPersonGrpVO keypersongroup)throws BusinessException;
	
	/**
	 * ������Ա��͹ؼ���Ա����,��ʷ��Ϣ
	 * @param vos
	 * @param pk_keypsngroup
	 * @return
	 * @throws BusinessException
	 */
	public abstract  String[] insertKeyPsnHistory(GeneralVO[] vos,String pk_keypsngroup)throws BusinessException;
	/**
	 *  �޸Ĺؼ���Ա��ʱ���������Ա��,������������δ���Ĺؼ���Ա
	 * @param keypersongroup
	 * @param sealdate
	 * @throws BusinessException
	 */
	public abstract  void sealPsnGroupToKeyPsn(KeyPersonGrpVO keypersongroup,UFDate sealdate )throws BusinessException;
	
	/**
	 * ���¹ؼ���Ա����ʾ˳���ֶ� 
	 * @param pk_psndoc_subs
	 * @param keypsnshoworder
	 * @throws BusinessException
	 */
	public abstract  void updateKeyPsnShoworder(String[] pk_psndoc_subs, HashMap keypsnshoworder)throws BusinessException;
	
	/**
	 * ��ѯ�ؼ���Ա��ʷ��¼
	 * @param pk_psndoc
	 * @param pk_corp
	 * @param pk_psngroup
	 * @param psngroup
	 * @return
	 * @throws BusinessException
	 */
	public  abstract GeneralVO[] queryKeyPersonInfo(String pk_psndoc, String pk_corp,String pk_psngroup,boolean psngroup)throws BusinessException ;
	/**
	 * ��ѯ������Ա
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public PsndocVO[] querybyCondition(String condition)throws BusinessException ;
	/**
	 * ��ѯ����Ա��������δ���Ĺؼ���Ա��¼�Ŀ�ʼ����
	 * @param pk_keypsn_group
	 * @return
	 * @throws BusinessException
	 */
	public String[] queryAllKeyPsnBegindate(String pk_keypsn_group)throws BusinessException;
	
	/**
	 * ��ѯ�ؼ���Ա
	 * 	����˵����
	 * 	pk_keypsngroup���ؼ���Ա������
	 * 	bSeal���Ƿ�����ѽ�����Ա
	 *
	 */
	public GeneralVO[] queryKeyPsnVOsByCondition_NewDLG(String pk_keypsngroup,
			Boolean bSeal, String pk_corp, String[] corps, String DLGwheresql,
			String[] DLGtables, String listfield, String normalwheresql,
			Boolean bSealGroup, BusinessFuncParser_sql funcParser,String orderbysql)
			throws BusinessException;
	
}
