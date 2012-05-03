package nc.vo.hr.pub.carddef;

import java.util.HashMap;

import nc.itf.hi.HIDelegator;
import nc.vo.hr.pub.carddef.CommonValue;
import nc.vo.hr.pub.carddef.RptAuthVO;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.pub.BusinessException;

/**
 * ��������ϵȨ�޵Ĺ�������ʹ�õ���ģʽ
 * @author wangxing
 *
 */
public class HrReportAuthManager {
	// �ͻ���Ψһʵ��
	private static HrReportAuthManager instance = new HrReportAuthManager();
	// ��Ÿ����û���Ȩ�޵ı���Ļ���
	// // ��CorpID+ UserID��Ϊ����Key����Ŵ��û���Ȩ�޵�MAP����MAP�Ա�����PKΪKey��ValueΪRptAuthVO
	private HashMap<String, HashMap<String, RptAuthVO>> mapMain = new HashMap<String, HashMap<String, RptAuthVO>>();
	
	// ��CorpID+ UserID��Ϊ����Key����Ŵ��û���Ȩ�޵�
	private HashMap<String, RptAuthVO[]> mapMain2 = new HashMap<String, RptAuthVO[]>();

	/**
	 * ���캯��
	 *
	 */
	private HrReportAuthManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ����һ��������������������������Ѿ������Ȩ��
	 * @param rptID
	 * @return
	 */
	public RptAuthVO[] getRptAllAuth(String rptID) throws Exception{
		return HIDelegator.getIHrReport().queryAuthVOByCondition(CommonValue.RPT_AUTH_REF_TYPE_ROLE, 
				" a.pk_rpt_def='"+rptID+"' and a.authtype="+CommonValue.RPT_AUTH_REF_TYPE_ROLE);
	}
	
	/**
	 * ���ݹ�˾�������û������õ�һ���û���Ȩ�޵�
	 * @param corpid
	 * @param userid
	 * @return
	 */
	public synchronized void loadUserRptAuthVOs(String corpid, String userid){
		if(!StringUtils.hasText(corpid) || !StringUtils.hasText(userid)){
			return;
		}//end if
		
		RptAuthVO[] allvos = null;
		if(!mapMain2.containsKey(corpid+userid) || 
				!mapMain.containsKey(corpid+userid)){
			try{
				// ���ȵõ��û��Ľ�ɫ
				allvos = HIDelegator.getIHrReport().queryAuthRptVOByPk(userid, corpid);
				if(allvos!=null && allvos.length>0){
					mapMain2.put(corpid+userid, allvos);
					HashMap<String, RptAuthVO> subMap = new HashMap<String, RptAuthVO>();
					for(int i=0 ;i<allvos.length; i++){
						subMap.put(allvos[i].getPk_rpt_def(), allvos[i]);
					}
					mapMain.put(corpid+userid, subMap);
				}
			}catch(BusinessException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ���ݹ�˾�������û������õ�һ���û���Ȩ�޵�
	 * @param corpid
	 * @param userid
	 * @return
	 */
	public synchronized RptAuthVO[] getUserRptAuthVOs(String corpid, String userid){
		if(!StringUtils.hasText(corpid) || !StringUtils.hasText(userid)){
			return null;
		}//end if
		
		RptAuthVO[] allvos = null;
		if(mapMain2.containsKey(corpid+userid)){
			allvos = mapMain2.get(corpid+userid);
		}else{
			try{
				// ���ȵõ��û��Ľ�ɫ
				allvos = HIDelegator.getIHrReport().queryAuthRptVOByPk(userid, corpid);
				if(allvos!=null && allvos.length>0){
					mapMain2.put(corpid+userid, allvos);
					HashMap<String, RptAuthVO> subMap = new HashMap<String, RptAuthVO>();
					for(int i=0 ;i<allvos.length; i++){
						subMap.put(allvos[i].getPk_rpt_def(), allvos[i]);
					}
					mapMain.put(corpid+userid, subMap);
				}
			}catch(BusinessException e){
				e.printStackTrace();
			}
		}
		
		
		return allvos;
	}
	
//	/**
//	 * �����û�����һ�������ȫ��Ȩ��
//	 * @param corpid
//	 * @param userid
//	 * @param rptid
//	 * @throws Exception
//	 */
//	public synchronized void assertFullAuth(String corpid, String userid, String rptid) throws Exception{
//		RptAuthVO authVO = getAuth(corpid,userid,rptid);
//		if(authVO==null)
//			throw new HrReportAuthException("�޷��õ��û�Ȩ�ޣ�");
//		
//		if(authVO.getAuth()==null || authVO.getAuth().intValue()!=CommonValue.RPT_AUTH_FULL){
//			throw new HrReportAuthException("���û�û���㹻��Ȩ��ִ�д˲�����");
//		}
//	}
	
	/**
	 * �����û�����һ����������Ȩ��
	 * @param corpid
	 * @param userid
	 * @param rptid
	 * @throws Exception
	 */
	public synchronized void assertViewAuth(String corpid, String userid, String rptid) throws Exception{
		RptAuthVO authVO = getAuth(corpid,userid,rptid);
		if(authVO==null)
//			throw new HrReportAuthException("�޷��õ��û�Ȩ�ޣ�");
			throw new HrReportAuthException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"600700", "UPP600700-000432")/* @res "�޷��õ��û�Ȩ�ޣ�" */);
		
		if(authVO.getAuth()==null || authVO.getAuth().intValue()<CommonValue.RPT_AUTH_VIEW){
//			throw new HrReportAuthException("���û�û���㹻��Ȩ��ִ�д˲�����");
			throw new HrReportAuthException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"600700", "UPP600700-000433")/* @res "���û�û���㹻��Ȩ��ִ�д˲�����" */);
		}
	}
	
	/**
	 * �ж�һ��ָ�����û��Ƿ��ָ���ı�����Ȩ��
	 * @param userid
	 * @param rptid
	 * @return
	 */
	public synchronized boolean isHasAuth(String corpid, String userid, String rptid){
		if(!StringUtils.hasText(corpid) || !StringUtils.hasText(userid) ||
				!StringUtils.hasText(rptid)){
			return false;
		}//end if
		
		loadUserRptAuthVOs(corpid,userid);
		HashMap<String, RptAuthVO> map = mapMain.get(corpid+userid);
		
		return map==null?false:map.containsKey(rptid);
	}
	
	/**
	 * �õ�ָ���û���ָ�������Ȩ��
	 * @param corpid
	 * @param userid
	 * @param rptid
	 * @return
	 */
	public synchronized RptAuthVO getAuth(String corpid, String userid, String rptid){
		if(!StringUtils.hasText(corpid) || !StringUtils.hasText(userid) ||
				!StringUtils.hasText(rptid)){
			return null;
		}//end if
		loadUserRptAuthVOs(corpid,userid);
		HashMap<String, RptAuthVO> map = mapMain.get(corpid+userid);
		
		return map==null?null:map.get(rptid);
	}
	
	/**
	 * ���¼��������û���Ȩ��
	 * @param corpid
	 * @param userid
	 */
	public synchronized void reloadAllAuth(){
		mapMain.clear();
		mapMain2.clear();
	}
	
	/**
	 * ���¼���ָ���û���Ȩ��
	 * @param corpid
	 * @param userid
	 */
	public synchronized void reloadhAuth(String corpid, String userid){
		if(!StringUtils.hasText(corpid) || !StringUtils.hasText(userid)){
			return;
		}//end if
		
		mapMain.remove(corpid+userid);
		mapMain2.remove(corpid+userid);
		loadUserRptAuthVOs(corpid,userid);
	}

	/**
	 * @return the instance
	 */
	public synchronized static HrReportAuthManager getInstance() {
		return instance;
	}
}
