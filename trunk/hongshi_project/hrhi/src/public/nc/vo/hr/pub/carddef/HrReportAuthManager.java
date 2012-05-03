package nc.vo.hr.pub.carddef;

import java.util.HashMap;

import nc.itf.hi.HIDelegator;
import nc.vo.hr.pub.carddef.CommonValue;
import nc.vo.hr.pub.carddef.RptAuthVO;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.pub.BusinessException;

/**
 * 管理报表体系权限的管理器，使用单例模式
 * @author wangxing
 *
 */
public class HrReportAuthManager {
	// 客户端唯一实例
	private static HrReportAuthManager instance = new HrReportAuthManager();
	// 存放各个用户有权限的报表的缓存
	// // 以CorpID+ UserID作为联合Key，存放此用户有权限的MAP，此MAP以报表定义PK为Key，Value为RptAuthVO
	private HashMap<String, HashMap<String, RptAuthVO>> mapMain = new HashMap<String, HashMap<String, RptAuthVO>>();
	
	// 以CorpID+ UserID作为联合Key，存放此用户有权限的
	private HashMap<String, RptAuthVO[]> mapMain2 = new HashMap<String, RptAuthVO[]>();

	/**
	 * 构造函数
	 *
	 */
	private HrReportAuthManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 根据一个报表的主健获得这个报表所有已经分配的权限
	 * @param rptID
	 * @return
	 */
	public RptAuthVO[] getRptAllAuth(String rptID) throws Exception{
		return HIDelegator.getIHrReport().queryAuthVOByCondition(CommonValue.RPT_AUTH_REF_TYPE_ROLE, 
				" a.pk_rpt_def='"+rptID+"' and a.authtype="+CommonValue.RPT_AUTH_REF_TYPE_ROLE);
	}
	
	/**
	 * 根据公司主键和用户主键得到一个用户有权限的
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
				// 首先得到用户的角色
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
	 * 根据公司主键和用户主键得到一个用户有权限的
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
				// 首先得到用户的角色
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
//	 * 断言用户具有一个报表的全部权限
//	 * @param corpid
//	 * @param userid
//	 * @param rptid
//	 * @throws Exception
//	 */
//	public synchronized void assertFullAuth(String corpid, String userid, String rptid) throws Exception{
//		RptAuthVO authVO = getAuth(corpid,userid,rptid);
//		if(authVO==null)
//			throw new HrReportAuthException("无法得到用户权限！");
//		
//		if(authVO.getAuth()==null || authVO.getAuth().intValue()!=CommonValue.RPT_AUTH_FULL){
//			throw new HrReportAuthException("本用户没有足够的权限执行此操作！");
//		}
//	}
	
	/**
	 * 断言用户具有一个报表的浏览权限
	 * @param corpid
	 * @param userid
	 * @param rptid
	 * @throws Exception
	 */
	public synchronized void assertViewAuth(String corpid, String userid, String rptid) throws Exception{
		RptAuthVO authVO = getAuth(corpid,userid,rptid);
		if(authVO==null)
//			throw new HrReportAuthException("无法得到用户权限！");
			throw new HrReportAuthException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"600700", "UPP600700-000432")/* @res "无法得到用户权限！" */);
		
		if(authVO.getAuth()==null || authVO.getAuth().intValue()<CommonValue.RPT_AUTH_VIEW){
//			throw new HrReportAuthException("本用户没有足够的权限执行此操作！");
			throw new HrReportAuthException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"600700", "UPP600700-000433")/* @res "本用户没有足够的权限执行此操作！" */);
		}
	}
	
	/**
	 * 判断一个指定的用户是否对指定的报表有权限
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
	 * 得到指定用户对指定报表的权限
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
	 * 重新加载所有用户的权限
	 * @param corpid
	 * @param userid
	 */
	public synchronized void reloadAllAuth(){
		mapMain.clear();
		mapMain2.clear();
	}
	
	/**
	 * 重新加载指定用户的权限
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
