package nc.itf.hi;


import nc.vo.hi.hi_301.BDFormuleVO;
import nc.vo.hi.hi_401.PsnDataVO;
import nc.vo.pub.BusinessException;

public interface IHRhiBS {

	/** --���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	 * �����ӱ�һ����¼
	 * �������ڣ�(2002-4-5 14:50:05)
	 * @return java.lang.String
	 * @param psndataVO nc.vo.hi.hi_401.PsnDataVO
	 */
	public PsnDataVO insertPsnData(PsnDataVO psndataVO,
			BDFormuleVO[] tongbuFields, int iSetType) throws BusinessException;

	/** --���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	 * ��VO���������ֵ�������ݿ⡣
	 * �������ڣ�(2002-3-27)
	 * @param psndocMain nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public void updatePsnData(PsnDataVO psndataVO, BDFormuleVO[] tongbuFields,
			int itype) throws BusinessException;

	/**
	 * ��VO���������ֵ�������ݿ⡣ --���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	 *
	 * �������ڣ�(2002-3-27)
	 * @param psndocMain nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public void updatePsnData(PsnDataVO psndataVO, BDFormuleVO[] tongbuFields,
			int itype, PsnDataVO psndataVOname) throws BusinessException;

	/**
	 * ͬ��������Ϣ��----���V30���������Ϊ200507111204423395 �� 200507210913444548�е�ͬ������
	 * �������ڣ�(2005-7-15 11:21:48)
	 * @param psnDataVO nc.vo.hi.hi_401.PsnDataVO
	 * @param tongbuFields nc.vo.hi.hi_301.BDFormuleVO[]
	 */
	public void updateBDAcc(PsnDataVO psnDataVO, BDFormuleVO[] tongbuFields)
			throws BusinessException;
	public void updateTimecard(String pk_psndoc,String value) throws BusinessException;
	public int getPsnclscopeByPK(String pk_psndoc) throws BusinessException;
	/**
	 * ���䵽�¹�˾�����bd_psnbasdoc��
	 *
	 * �������ڣ�(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException �쳣˵����
	 */
	public void updateBDPsnBas(nc.vo.hi.hi_301.HRMainVO hrMainVO)throws BusinessException;
	/**
	 * ����hi_psndoc_deptchg��VO����bd_psndoc��
	 *
	 * �������ڣ�(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException �쳣˵����
	 */
	public String updateBDForDeptchg(nc.vo.hi.hi_401.PsnDataVO psndocMain,nc.vo.hi.hi_401.PsnDataVO namevo)throws BusinessException;
	/**
	 * ����ָ�������ݵ�VO����bd_accpsndoc��
	 *
	 * �������ڣ�(2002-3-27)
	 * @param node nc.vo.hi.hi_301.PsndocMainVO
	 * @exception java.sql.SQLException �쳣˵����
	 */
	public String updateBDbasdoc(nc.vo.hi.hi_401.PsnDataVO psndocMain,BDFormuleVO[] vos)throws BusinessException;
}