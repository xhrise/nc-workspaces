package nc.ui.om.om_005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.hr.pub.PubDelegator;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.action.AbstractAction;
import nc.ui.ml.NCLangRes;
import nc.ui.om.pub.Validate;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.vo.bd.defref.DefdocVO;
import nc.vo.hi.hi_301.HRSubVO;
import nc.vo.hi.pub.CommonValue;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.om.om_005.JobAndDesc;
import nc.vo.om.om_005.JobVO;
import nc.vo.om.om_005.JobdescVO;
import nc.vo.om.om_011.OrgNodeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.yto.business.JobdocVO;

/**
 * 岗位信息按钮事件，保存
 * @author wangxing
 *
 */
public class SaveAction extends AbstractAction {
	
	// 需要使用的一些私有常量
	
	 /*
	  * @res "定性指标"
	  */
	private final String INDEX_NATURE = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000039");
	
	 /*
	  * @res "定量指标"
	  */
	private final String INDEX_QUALITY = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000038");
	
	/*
	 * @res "非阶段性"
	 */
	private final String PERIOD_NOT = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000040");
	
	/*
	 * @res "年度"
	 */
	private final String PERIOD_YEAR = NCLangRes.getInstance().getStrByID("common","UC000-0001802");
	
	/*
	 * @res "半年"
	 */
	private final String PERIOD_HALFYEAR = NCLangRes.getInstance().getStrByID("common","UC000-0000725");
	
	/*
	 * @res "季度"
	 */
	private final String PERIOD_SEASON = NCLangRes.getInstance().getStrByID("common","UC000-0001492");

	/*
	 * @res "月份"
	 */
	private final String PERIOD_MONTH = NCLangRes.getInstance().getStrByID("common","UC000-0002495");
	/**
	 * 构造函数
	 * @param frameUI1
	 */
	public SaveAction(FrameUI frameUI1) {
		super(frameUI1);
		
	}
	
	/**
	 * 重写基类方法
	 * @return
	 */
	public JobInfoUI getFrameUI(){
		return (JobInfoUI)super.getFrameUI();
	}
	
	/**
	 * 校验方法，校验主表的任职资格等描述信息
	 * 
	 * @return boolean
	 * @param jobdescvo
	 *            nc.vo.om.om_005.JobdescVO
	 */
	public boolean checkDescData(JobdescVO vo) {
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		if (vo.getReqedu() != null
				&& vo.getReqedu().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000209")/* @res "学历要求字符超长!" */);
			return false;
		}
		if (vo.getReqexp() != null
				&& vo.getReqexp().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000213")/* @res "工作经验要求字符超长!" */);
			return false;
		}
		if (vo.getReqpro() != null
				&& vo.getReqpro().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000212")/* @res "专业背景要求字符超长!" */);
			return false;
		}
		if (vo.getReqsex() != null
				&& vo.getReqsex().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000210")/* @res "性别要求字符超长!" */);
			return false;
		}
		if (vo.getReqworktime() != null
				&& vo.getReqworktime().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000214")/* @res "工作时间字符超长!" */);
			return false;
		}
		if (vo.getReqyold() != null
				&& vo.getReqyold().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000211")/* @res "年龄要求字符超长!" */);
			return false;
		}
		
		if (vo.getReqother() != null
				&& vo.getReqother().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704", "UPP60050704-000279"))/* @res "其他要求字符超长!" */;
			return false;
		}
		
		try {
			Validate validate = new Validate();
			validate.dataNotNullValidate(bcp.getHeadShowItems("om_jobdesc"), vo);

		} catch (Exception e) {
			getFrameUI().handleException(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 校验一组子表数据
	 * @param vos
	 * @return boolean 是否成功
	 * @throws Exception 异常信息中包含错误信息
	 */
	public boolean checkChildData(HRSubVO[] vos) throws Exception {
		if(vos==null || vos.length<=0){
			return true;
		}
		JobInfoUI ui = (JobInfoUI)getFrameUI();
		BillCardPanel bcp = ui.getMainPanel().getCardPanel().getBillCardPanel();
		String curTableCode = vos[0].getTablename();
		BillItem[] items = bcp.getBillData().getBodyItemsForTable(curTableCode);
		
		//先做非空校验
		try {
			Validate validate = new Validate();
			for(HRSubVO aVO : vos){
				validate.dataNotNullValidate(items, aVO);
			}//end for
		} catch (Exception e) {
			ui.showWarningMessage(e.getMessage());
			return false;
		}//end if
		
		// 用来校验重复记录的缓存
		Map<String, String> map = new HashMap<String, String>();
		String tmpPk = null;
	// 循环校验每一个VO
	for(HRSubVO vo : vos){
		//考核指标
		if ("om_jobmeasure".equals(curTableCode)) {
			String ideterminate = (String) vo.getAttributeValue("ideterminate");
			if (ideterminate.trim().equals("0")) {
				String vexamgoal = (String) vo.getAttributeValue("vexamgoal");
				if (vexamgoal != null) {
					if (vexamgoal.length() > 12) {
						ui.showErrorMessage(
								NCLangRes.getInstance()
										.getStrByID("60050704",
												"UPP60050704-000179")/*
																	  * @res
																	  * "考核指标性质是定量指标时，考核目标不能大于12位!"
																	  */);
						return false;
					}//end if
				}//end if
				try {
					new UFDouble(vexamgoal);
				} catch (NumberFormatException e) {
					ui.showErrorMessage(
							NCLangRes.getInstance()
									.getStrByID("60050704",
											"UPP60050704-000180")/*
																  * @res
																  * "考核指标性质是定量指标时，考核目标应为数字!"
																  */);
					return false;
				}//end try
			}//end if

		}//end if
		
		//在岗培训
		if ("om_jobtraining".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_trianitem");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(NCLangRes.getInstance().getStrByID(
						"60050704", "UPP60050704-000185")/*
														  * @res
														  * "培训项目不能重复！"
														  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗前培训
		if ("om_jobbeftrain".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_trianitem");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(NCLangRes.getInstance().getStrByID(
						"60050704", "UPP60050704-000185")/*
														  * @res
														  * "培训项目不能重复！"
														  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位办公设备
		if ("om_jobequipment".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("equipmentno");
			
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000188")/*
																  * @res
																  * "设备编号不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位工作目标
		if ("om_jobgoal".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("vcode");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000190")/*
																  * @res
																  * "编码不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位职责
		if ("om_jobresp".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_resptype");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000192")/*
																  * @res
																  * "职责类型不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
			
		}//end if
		
		//岗位监督
		if ("om_jobsuperv".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_supervtype");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000194")/*
																  * @res
																  * "监督类型不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位接触情况
		if ("om_jobcont".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_contactobj");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000196")/*
																  * @res
																  * "接触对象不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位工作权限
		if ("om_jobpower".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("vpowername");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000198")/*
																  * @res
																  * "权限名称不能重复！"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//岗位素质指标
		if ("om_jobneed".equals(curTableCode)) {
			String pk_posttype = (String) vo.getAttributeValue("pk_posttype");
			String pk_postrequire_h = (String) vo.getAttributeValue("pk_postrequire_h");
			tmpPk = curTableCode+pk_posttype+pk_postrequire_h;
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance()
						.getStrByID("60050704",
								"UPP60050704-000200")/*
													  * @res
													  * "相同类别的素质指标名称不能重复！"
													  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
	}//end for
	
		return true;
	}
	
	/**
	 * 校验方法，校验主表的基本信息
	 * 
	 * @return boolean
	 */
	public boolean checkData(JobVO jobvo) throws Exception {
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		int sts = getDataModel().getCurrentState();
		if (getFrameUI().isLoginGroup()) {//集团校验
			if (jobvo.getJobcode() == null
					|| jobvo.getJobcode().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000201")/* @res "岗位编码不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getSuporior() != null
					&& jobvo.getSuporior().toString().trim().length() > 0
					&& jobvo.getPk_om_job() != null) {
				String Pk_om_job = jobvo.getPk_om_job().toString().trim();
				String Suporior = jobvo.getSuporior().toString().trim();
				if (Pk_om_job.equals(Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000202")/*
																	  * @res
																	  * "岗位上级不能选自己"
																	  */);
					return false;
				}//end if
				
				//add by zhyan 2005-11-17 直接上级不能是直接下级或者间接下级
				if (ORGDelegator.getIJob().isCircle(Pk_om_job, Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000277")/*
																	  * @res
																	  * "岗位直接上级不能是直接下级或者间接下级"
																	  */);
					return false;
				}//end if
			}//end if
			
			if (jobvo.getJobname() == null
					|| jobvo.getJobname().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000203")/* @res "岗位名称不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getBuilddate() == null
					|| jobvo.getBuilddate().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000205")/* @res "成立日期不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getJobseries() == null
					|| jobvo.getJobseries().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000206")/* @res "岗位序列不能为空" */);
				return false;
			}else if (jobvo.getJobseries() != null) {
				// add by zhyan 2005-11-14
				String pk_series = jobvo.getJobseries();
				boolean exit;
				try {
					exit = ORGDelegator.getIDuty().checkRefExit(pk_series);
					if (!exit) {
						getFrameUI().showWarningMessage(
								nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("60050704",
										"UPP60050704-000206")/*
															  * @res
															  * "岗位序列不能为空"
															  */);
						return false;
					}//end if
				} catch (Exception e1) {
					e1.printStackTrace();
				}//end try

			}//end if

			// added by zhangdd 2009.7.13 modify for 深圳领跑特殊需求 。 bug编号NCdp200910711
			if(!getFrameUI().isStdJob()){// 如果不是基准岗位，要判断岗位等级是否为空
				if (jobvo.getJobrank() == null
						|| jobvo.getJobrank().toString().trim().length() < 1) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000207")/* @res "岗位等级不能为空" */);
					return false;
				}//end if
			}
			
			if (getFrameUI().getDutyType().equals("1")) {
				Object series = ((UIRefPane) bcp.getHeadItem("pk_om_duty").getComponent()).getRefValue("series");
				Object jobseries = ((UIRefPane) bcp.getHeadItem("jobseries").getComponent()).getRefPK();
				if (series != null && jobseries != null)
					if (!series.equals(jobseries)) {
						getFrameUI().showWarningMessage(
								nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("60050704",
										"UPP60050704-000021")/*
															  * @res
															  * "所选职务的岗位序列与该岗位所选岗位序列不符!"
															  */);
						return false;
					}//end if
			}//end if
			
			try {
				Validate validate = new Validate();
				validate.dataNotNullValidate(bcp.getHeadShowItems("om_job"), jobvo);

			} catch (Exception e) {
				getFrameUI().showWarningMessage(e.getMessage());
				return false;
			}//end try
			
			// 校验自定义流水号, added by walkfire V5.02 2007-12-19
			// 只有在新增状态才校验单据号
			if( !getFrameUI().isAutoJobCode() && sts==JobInfoStateReg.USERSTS_ADD_MAIN ){
				try{
					getFrameUI().checkCustomBillCode(getFrameUI().getPkBillTypeCode(), jobvo.getJobcode());
				}catch(Exception e){
					e.printStackTrace();
				}//end try
			}//end if
			
			return true;
		} else {//公司校验
			if (jobvo.getJobcode() == null
					|| jobvo.getJobcode().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000201")/* @res "岗位编码不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getSuporior() != null
					&& jobvo.getSuporior().toString().trim().length() > 0
					&& jobvo.getPk_om_job() != null) {
				String Pk_om_job = jobvo.getPk_om_job().toString().trim();
				String Suporior = jobvo.getSuporior().toString().trim();
				if (Pk_om_job.equals(Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000202")/*
																	  * @res
																	  * "岗位上级不能选自己"
																	  */);
					return false;
				}//end if
				
				//add by zhyan 2005-11-17 直接上级不能是直接下级或者间接下级
				if (ORGDelegator.getIJob().isCircle(Pk_om_job, Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000277")/*
																	  * @res
																	  * "岗位直接上级不能是直接下级或者间接下级"
																	  */);
					return false;
				}//end if
			}//end if
			
			if (jobvo.getJobname() == null
					|| jobvo.getJobname().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000203")/* @res "岗位名称不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getPk_deptdoc() == null
					|| jobvo.getPk_deptdoc().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000204")/* @res "所属部门不能为空" */);
				return false;
			}//end if
			
			//wangkf add
			IDeptdocQry ideptqury = ((IDeptdocQry)NCLocator.getInstance().lookup(IDeptdocQry.class.getName()));
			nc.vo.bd.b04.DeptdocVO deptvo = ideptqury.findDeptdocVOByPK(jobvo.getPk_deptdoc());
			if (deptvo.getHrcanceled() != null && deptvo.getHrcanceled().booleanValue() ) {			
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPT60050704-000171")/* @res "所属部门不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getBuilddate() == null
					|| jobvo.getBuilddate().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000205")/* @res "成立日期不能为空" */);
				return false;
			}//end if
			
			if (jobvo.getJobseries() == null
					|| jobvo.getJobseries().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000206")/* @res "岗位序列不能为空" */);
				return false;
			}else if (jobvo.getJobseries() != null) {
				// add by zhyan 2005-11-14
				String pk_series = jobvo.getJobseries();
				boolean exit;
				try {
					exit = ORGDelegator.getIDuty().checkRefExit(pk_series);
					if (!exit) {
						getFrameUI().showWarningMessage(
								nc.ui.ml.NCLangRes.getInstance()
										.getStrByID("60050704",
												"UPP60050704-000206")/*
																	  * @res
																	  * "岗位序列不能为空"
																	  */);
						return false;
					}//end if
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}//end try

			}//end if

			if (jobvo.getJobrank() == null
					|| jobvo.getJobrank().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000207")/* @res "岗位等级不能为空" */);
				return false;
			}//end if


			if (getFrameUI().getDutyType().equals("1")) {
				Object series = ((UIRefPane) bcp.getHeadItem("pk_om_duty").getComponent()).getRefValue("series");
				Object jobseries = ((UIRefPane) bcp.getHeadItem("jobseries").getComponent()).getRefPK();
				if (series != null && jobseries != null)
					if (!series.equals(jobseries)) {
						getFrameUI().showWarningMessage(
								nc.ui.ml.NCLangRes.getInstance()
										.getStrByID("60050704",
												"UPP60050704-000021")/*
																	  * @res
																	  * "所选职务的岗位序列与该岗位所选岗位序列不符!"
																	  */);
						return false;
					}//end if
			}//end if
			
			try {
				Validate validate = new Validate();
				validate.dataNotNullValidate(bcp.getHeadShowItems("om_job"), jobvo);

			} catch (Exception e) {
				getFrameUI().showWarningMessage(e.getMessage());
				return false;
			}//end try
			
			// 校验自定义流水号, added by walkfire V5.02 2007-12-19
			// 只有在新增状态才校验单据号
			if( !getFrameUI().isAutoJobCode() && sts==JobInfoStateReg.USERSTS_ADD_MAIN ){
				try{
					getFrameUI().checkCustomBillCode(getFrameUI().getPkBillTypeCode(), jobvo.getJobcode());
				}catch(Exception e){
					e.printStackTrace();
				}//end try
			}//end if
			
			return true;
		}//end if
	}
	
	/**
	 * 得到当前表头的基本信息VO
	 * 
	 * @return nc.vo.om.om_005.JobVO
	 */
	protected JobAndDesc getCurrentMainVO() throws Exception {
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		JobAndDesc headvo = getFrameUI().createNewMainVO();

		bcp.getBillData().getHeaderValueVO(headvo);
		
		// 处理自定义的参照字段
		JobVO jobVO = headvo.getJobVO();
		if(!jobVO.isHasUserDefineFields()){
			return headvo;
		}//end if
		
		// 开始处理自定义字段,groupdef
		BillItem[] items = bcp.getHeadItems();
		if(items==null || items.length<=0){
			return headvo;
		}//end if
		
		for(BillItem item : items){
			// 如果是自定义参照字段
			if(item.isShow() && StringUtils.hasText(item.getKey()) && item.getKey().startsWith("groupdef") && item.getDataType()==BillItem.UFREF){
				UIRefPane refP = (UIRefPane)item.getComponent();
				String refValue = refP.getText();
				jobVO.setUserDefineFieldShowValue(item.getKey(), refValue);
			}//end if
		}//end for
		
		return headvo;

	}

//	/**
//	 * 得到当前表头的描述信息VO
//	 * 
//	 * @return nc.vo.om.om_005.JobVO
//	 */
//	protected JobdescVO getHeadJobdescDate() throws Exception {
//		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
//		JobVO jobvo = new JobVO();
//		JobdescVO jobdescvo = new JobdescVO();
//		JobAndDesc headvo = new JobAndDesc();
//		headvo.setjobdescvo(jobdescvo);
//		headvo.setJobVO(jobvo);
//		bcp.getBillData().getHeaderValueVO(headvo);
////		getBillCardPan().getBillData().getHeaderValueVO(headvo);
//		jobdescvo = headvo.getJobdescVO();
//		return jobdescvo;
//
//	}
	
	/**
	 * 得到当前子表的元数据VO
	 * @return
	 */
	private HRSubVO getCurrentSubTabMetaVO(){
		HRSubVO vo = new HRSubVO();
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		String currentSubCode = bcp.getCurrentBodyTableCode();
		vo.setTablename(currentSubCode);
		vo.setPksubname("pk_job_sub");
		vo.setPkname("pk_om_job");
		
		// 得到所有的BillItems
		BillItem[] items = bcp.getBillModel(currentSubCode).getBodyItems();
		if(items==null || items.length<=0){
			return vo;
		}//end if
		
		boolean isExistLastflag = false;
		boolean isExistRecordnum = false;
		Vector<String> vFldNames = new Vector<String>();
		Vector<Integer> vFldTypes = new Vector<Integer>();
		
		for(int i=0; i<items.length; i++){
			// 显示用的字段不在保存范围之内
			if(items[i].getKey().endsWith(HRSubVO.REF_SHOW_NAME)){
				continue;
			}else if("recordnum".equals(items[i].getKey())){
				isExistRecordnum = true;
			}else if("lastfalg".equals(items[i].getKey())){
				isExistLastflag = true;
			}//end if
			vFldNames.addElement(items[i].getKey());
			vFldTypes.addElement(new Integer(items[i].getDataType()));
		}//end for
		
		// 如果不存在 recordnum 字段
		if(!isExistRecordnum){
			vFldNames.addElement("recordnum");
			vFldTypes.addElement(new Integer(IBillItem.INTEGER));
		}//end if
		
		// 如果不存在 lastfalg 字段
		if(!isExistLastflag){
			vFldNames.addElement("lastfalg");
			vFldTypes.addElement(new Integer(IBillItem.BOOLEAN));
		}//end if
		
		vo.setFieldNames(vFldNames.toArray(new String[0]));
		vo.setFieldTypes(vFldTypes.toArray(new Integer[0]));
		vo.setFieldValues(new Object[vFldNames.size()]);
		return vo;
	}
	
	/**
	 * 把VO中的显示值翻译为数据库对应值的特殊处理
	 * @param srcVOs
	 * @return
	 */
	private HRSubVO[] convertHRSubVOShow2DB(HRSubVO[] srcVOs){
		if(srcVOs==null || srcVOs.length<=0){
			return srcVOs;
		}//end if
		
		JobVO jobVO = getFrameUI().getCurrentSelJobVO();
		String tmpValue = null;
		Integer[] dataTypes = null;
		String[] names = null;
		HRSubVO aVO = null;
		for(int i=0; i<srcVOs.length; i++){
			aVO = srcVOs[i];
			// 首先是一个特殊表
			if("om_jobmeasure".equals(aVO.getTablename())){
				// 首先修改 ideterminate 指标类型
				tmpValue = (String)aVO.getAttributeValue("om_jobmeasure");
				if(INDEX_NATURE.equals(tmpValue)){
					aVO.setAttributeValue("om_jobmeasure", "1");
				}else if(INDEX_QUALITY.equals(tmpValue)){
					aVO.setAttributeValue("om_jobmeasure", "0");
				}//end if
				
				// 修改 iperiodtype
				tmpValue = (String)aVO.getAttributeValue("iperiodtype");
				if(PERIOD_NOT.equals(tmpValue)){
					aVO.setAttributeValue("iperiodtype", "0");
				}else if(PERIOD_YEAR.equals(tmpValue)){
					aVO.setAttributeValue("iperiodtype", "1");
				}else if(PERIOD_HALFYEAR.equals(tmpValue)){
					aVO.setAttributeValue("iperiodtype", "2");
				}else if(PERIOD_SEASON.equals(tmpValue)){
					aVO.setAttributeValue("iperiodtype", "3");
				}else if(PERIOD_MONTH.equals(tmpValue)){
					aVO.setAttributeValue("iperiodtype", "4");
				}//end if
				
				// 修改 chalfy
				
				// 修改 cquarter
				
				// 修改 cmonth
			}//end if om_jobmeasure
			
			
			// 统一处理 UFBoolean类型
			dataTypes = aVO.getFieldTypes();
			names = aVO.getFieldnames();
			if(dataTypes==null || dataTypes.length<=0){
				continue;
			}//end if
			
			for(int t=0; t<dataTypes.length; t++){
				if(CommonValue.DATATYPE_BOOLEAN == dataTypes[t].intValue()){
					tmpValue = (String)aVO.getAttributeValue(names[t]);
					
					if("true".equalsIgnoreCase(tmpValue)){
						aVO.setAttributeValue(names[t], "Y");
					}else if("false".equalsIgnoreCase(tmpValue)){
						aVO.setAttributeValue(names[t], "N");
					}// end if
				}//end if
			}//end for
			
			// 统一处理主键
			aVO.setPk_main(jobVO.getPk_om_job());
			
			// 处理recordnum和lastfalg
			aVO.setAttributeValue("recordnum", ""+(srcVOs.length-1-i));
			if(!"om_jobhistory".equalsIgnoreCase(aVO.getTablename())){
				aVO.setAttributeValue("lastfalg", "Y");
			}else if(i==(srcVOs.length-1)){
				aVO.setAttributeValue("lastfalg", "Y");
			}else{
				aVO.setAttributeValue("lastfalg", "N");
			}//end if
		}//end for
		
		return srcVOs;
	}
	
	/**
	 * 得到当前要保存的子表VO
	 * 
	 * @return nc.vo.hi.hi_301.HRSubVO
	 */
	public HRSubVO[] getCurSubSavingVOs() {
		HRSubVO curMetaVO = getCurrentSubTabMetaVO();
		
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		String currentSubCode = bcp.getCurrentBodyTableCode();
		int subLength = bcp.getBillTable(currentSubCode).getRowCount();
		if(subLength<=0){
			return null;
		}
		HRSubVO[] savingVOs = HRSubVO.createVOArray(curMetaVO, subLength, true);
		
		bcp.getBillModel(currentSubCode).getBodyValueVOs(savingVOs);
		savingVOs = convertHRSubVOShow2DB(savingVOs);
		
		return savingVOs;
	}
	
	/**
	 * 子表保存
	 * @return
	 */
	public boolean subTableSave() throws Exception{
		BillCardPanel billcardpan = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		String tbcode = billcardpan.getCurrentBodyTableCode();
		if (billcardpan.getBillTable(tbcode) != null
				&& billcardpan.getBillTable(tbcode).getCellEditor() != null) {
			billcardpan.getBillTable(tbcode).getCellEditor()
					.stopCellEditing();
		}
		//编辑的是子集
		HRSubVO[] savingVOs = getCurSubSavingVOs();
		// 校验子表信息
		if(!this.checkChildData(savingVOs)){
			return false;
		}//end if
		
		// 真正保存到数据库中
		// 如果清空了子表，则也只是在数据库中清空对应的子表信息，清除可能的垃圾数据
		if(savingVOs==null || savingVOs.length<=0){
			PubDelegator.getIPersistenceUpdate().executeSQLs(new String[]{
					"delete from "+tbcode+" where pk_om_job='"+getFrameUI().getCurrentSelJobVO().getPk_om_job()+"' "
			});
		}else{
			// 添加岗位信息主键
			for(HRSubVO subVO : savingVOs){
				subVO.setPk_main(getFrameUI().getCurrentSelJobVO().getPk_om_job());
			}//end for
			
			// 保存实际的VO，保存也是覆盖式保存，会先清空历史数据，然后重新覆盖保存
			String[] pks = ORGDelegator.getISub().saveSubsByVOStatus(savingVOs);
		}//end if
		
		// 解除子表的数据锁
		JobInfoUI ui = (JobInfoUI)getFrameUI();
		getFrameUI().freeLockRecord(ui.getCurrentSelJobVO().getPk_om_job()+"_"+tbcode);
		
		getFrameUI().clearCurrentJobSubCahce();
		return true;
		//返回的主键
		
//		vo.setAttributeValue("lastfalg", "N");
//		if (checkChildData(vo)) {
//			if (InsertType != UI_Update) {
//				//赋部门主键
//				vo.setAttributeValue("pk_om_job", getCurSelectJob());
//				vo.setAttributeValue("pk_corp", pk_corp);
//				if (InsertType == UI_Insert) {
//					vo.setAttributeValue("recordnum", new Integer(
//							billcardpan.getBillTable(tbcode)
//									.getRowCount()
//									- 1 - iEditRow).toString());
//					Key = controlChildAdd(vo, true);
//					//处理缓存
//					setVOsToHash(vo, true, iEditRow);
//				} else if (InsertType == UI_Add) {
//					vo.setAttributeValue("recordnum", "0");
//					Key = controlChildAdd(vo, false);
//					//处理缓存
//					setVOsToHash(vo, false, iEditRow);
//
//				}
//				//把返回的主键值赋给VO
//				vo.setAttributeValue("pk_job_sub", Key);
//				//把VO赋给BillCardPanel
//				billcardpan.getBillModel().setBodyRowVO(vo, iEditRow);
//
//			} else {
//				//				vo = getCurSubVO();
//				vo.setAttributeValue("recordnum", new Integer(
//						billcardpan.getBillTable(tbcode).getRowCount()
//								- 1 - iEditRow).toString());
//				controlChildUpdate(vo);
//				//处理缓存
//				setVOsToHash(vo, false, iEditRow);
//				freeLockRecord(vo.getPrimaryKey()
//						+ tbcode);//wangkf add
//			}
//			setChildStat(UI_Browse);
//			nc.bs.logging.Logger.error("当前编辑的行为第" + iEditRow + "行。");
//			if (billcardpan.getBillTable().getRowCount() > 0) {
//				billcardpan.getBillTable().setRowSelectionInterval(
//						iEditRow, iEditRow);
//			}
//			//清空
//			BillItem[] billitems = billcardpan.getBillModel().getBodyItems();
//			for (int i = 0; i < billitems.length; i++) {
//				if (billitems[i].getDataType() == BillItem.UFREF) {
//					((UIRefPane) billitems[i].getComponent()).setPK(null);
//					((UIRefPane) billitems[i].getComponent()).setText(null);
//				}
//			}
//			bool = true;
//		} else {
//			bool = false;
//			freeLockRecord(vo.getPrimaryKey()+ tbcode);//wangkf add
//		}
	
	}
	
	/**
	 * 判断是否需要同步的方法
	 * @param oldVO
	 * @param newVO
	 * @return
	 */
	private boolean isNeedSyncCorpJob(JobAndDesc oldVO, JobAndDesc newVO){
		// 如果不是基准岗位节点，则不需要同步
		if(!getFrameUI().isStdJob()){
			return false;
		}//end if
		
		// 如果数据出现错误，则不需要同步
		if(oldVO==null || newVO==null){
			return false;
		}//end if
		
		JobVO oldJobVO = oldVO.getJobVO();
		JobdescVO oldJobDescVO = oldVO.getJobdescVO();
		
		JobVO newJobVO = newVO.getJobVO();
		JobdescVO newJobDescVO = newVO.getJobdescVO();
		
		return !StringUtils.isStringsSame(oldJobVO.getJobseries(), newJobVO.getJobseries()) || 
		!StringUtils.isStringsSame(oldJobVO.getJobrank(), newJobVO.getJobrank()) || 
		!StringUtils.isStringsSame(oldJobVO.getPk_om_duty(), newJobVO.getPk_om_duty()) || 
		!StringUtils.isObjectsSame(oldJobVO.getIsdeptrespon(), newJobVO.getIsdeptrespon()) || 
		!StringUtils.isStringsSame(oldJobVO.getWorksumm(), newJobVO.getWorksumm()) || 
//		!StringUtils.isStringsSame(oldJobVO.getOutcontact(), newJobVO.getOutcontact()) || 
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef1"), newJobVO.getAttributeValue("groupdef1")) || 
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef2"), newJobVO.getAttributeValue("groupdef2")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef3"), newJobVO.getAttributeValue("groupdef3")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef4"), newJobVO.getAttributeValue("groupdef4")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef5"), newJobVO.getAttributeValue("groupdef5")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef6"), newJobVO.getAttributeValue("groupdef6")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef7"), newJobVO.getAttributeValue("groupdef7")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef8"), newJobVO.getAttributeValue("groupdef8")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef9"), newJobVO.getAttributeValue("groupdef9")) ||
		!StringUtils.isObjectsSame(oldJobVO.getAttributeValue("groupdef10"), newJobVO.getAttributeValue("groupdef10")) ||
		!StringUtils.isStringsSame(oldJobDescVO.getReqedu(), newJobDescVO.getReqedu()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqexp(), newJobDescVO.getReqexp()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqpro(), newJobDescVO.getReqpro()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqsex(), newJobDescVO.getReqsex()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqworktime(), newJobDescVO.getReqworktime()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqother(), newJobDescVO.getReqother()) || 
		!StringUtils.isStringsSame(oldJobDescVO.getReqyold(), newJobDescVO.getReqyold());
	}
	
	/**
	 * 主表更新
	 */
	public boolean mainTableUpdate() throws Exception {
		JobAndDesc aMainJobVO = null;
		try {
			aMainJobVO = getCurrentMainVO();

			// 当岗位序列，岗位等级或者职务发生变化时，同步任职表中的记录
			String old_jobseries = null;
			String old_jobrank = null;
			String old_duty = null;
			JobAndDesc oldMainVO = (JobAndDesc) getFrameUI().getCurrentCardVO().getParentVO();
			if (oldMainVO != null) {
				
				aMainJobVO.getJobVO().setPk_corp(oldMainVO.getJobVO().getPk_corp());
				aMainJobVO.getJobVO().setCreatecorp(oldMainVO.getJobVO().getCreatecorp());
				
				// 得到旧的岗位序列，岗位等级，和职务
				old_jobseries = oldMainVO.getJobVO().getJobseries();
				old_jobrank = oldMainVO.getJobVO().getJobrank();
				old_duty = oldMainVO.getJobVO().getPk_om_duty();
				
				// 处理没有岗位任职资格的情况
				if(oldMainVO.getJobdescVO()==null){
					JobdescVO descVO = new JobdescVO();
					descVO.setPk_om_job(oldMainVO.getJobVO().getPk_om_job());
					String pkdesc = ORGDelegator.getIJob().insertJobdesc(descVO);
					descVO.setPk_job_sub(pkdesc);
					oldMainVO.setjobdescvo(descVO);
					aMainJobVO.getJobdescVO().setPk_job_sub(pkdesc);
				}//end if
			}
			if (!checkData(aMainJobVO.getJobVO()))
				return false;
			
			if (!checkDescData(aMainJobVO.getJobdescVO()))
				return false;
			if (aMainJobVO.getJobVO().getIsAbort() == null) {
				aMainJobVO.getJobVO().setIsAbort(new UFBoolean("N"));
			}
			
			if (aMainJobVO.getJobVO().getAttributeValue("pk_corp") == null
					|| aMainJobVO.getJobVO().getAttributeValue("pk_corp").toString().equals("")) {
				aMainJobVO.getJobVO().setPk_corp(getFrameUI().getLogin_Pk_corp());
			}//end if
			
			// 是否需要同步公司岗位
			boolean isNeedSyncCorpJob = isNeedSyncCorpJob(oldMainVO, aMainJobVO);
			if(isNeedSyncCorpJob){
				int resDlg = MessageDialog.showYesNoDlg(getFrameUI(), 
						NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "确认对话框" */, 
//						UPT60050704-000288=是否同步更新公司引用岗位中的信息？
						NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000288"));
				isNeedSyncCorpJob = resDlg==MessageDialog.ID_YES;
			}//end if
			// 是否同步人员任职表
			// modified by zhangdd 2009.7.13 modify for 深圳领跑特殊需求 。 bug编号NCdp200910711
			boolean isSyncPsn = !aMainJobVO.getJobVO().getJobseries().equals(old_jobseries) || 
				(StringUtils.hasText(old_duty) && !old_duty.equals(aMainJobVO.getJobVO().getPk_om_duty()));
			
			String jobRank = aMainJobVO.getJobVO().getJobrank();
			isSyncPsn = isSyncPsn || (jobRank!=null&&old_jobrank!=null&&!aMainJobVO.getJobVO().getJobrank().equals(old_jobrank));
			
			// 真正更新数据库
			aMainJobVO.getJobVO().setUseShowValue(false);
			ORGDelegator.getIJob().updateJobMainData(aMainJobVO, getFrameUI().getDutyType(), isNeedSyncCorpJob, isSyncPsn);
			aMainJobVO.getJobVO().setUseShowValue(true);

			// 如果成功保存了主表信息，则尝试保存引用的子表信息，如果确实引用了基准岗位的子集数据
			if (getFrameUI().getStdJobSubRefDatas()!=null) {
				// 插入子表数据
				ORGDelegator.getIJob().insertAllGroupSub(
						getFrameUI().getStdJobSubRefDatas(), 
						getFrameUI().getStdJobSubTableCodes(), 
						aMainJobVO.getJobVO().getPk_om_job());
				// 显示代码统一处理
				getFrameUI().setStdJobSubRefDatas(null);
				getFrameUI().setStdJobSubTableCodes(null);
			}//end if
			
			JobInfoMainPanel mp = (JobInfoMainPanel)getMainPanel();
			mp.getListPanel().getMainListPanel().getTableModel().setBodyRowVO(aMainJobVO.getJobVO(), getFrameUI().getCurrentJobCursor());
			mp.getListPanel().getMainListPanel().getTableModel().execLoadFormulaByRow(getFrameUI().getCurrentJobCursor());
			
			getFrameUI().clearLastJobCahce();
//			getFrameUI().changeFromQueryResult(aMainJobVO.getJobVO().getPk_om_job(), aMainJobVO.getJobVO());
			getFrameUI().changeFromCurrentJobs(aMainJobVO.getJobVO().getPk_om_job(), aMainJobVO.getJobVO());
			getFrameUI().setCurrentMainVO(aMainJobVO);
			
//			if (&& getFrameUI().getDutyType().equals("1"))
//				ORGDelegator.getIJob().updateJobbyduty(aNewJobVO, true);
//			else {
//				if (getFrameUI().isLoginGroup()) {
//					ORGDelegator.getIJob().updateGroup(aNewJobVO);
//				} else {
//					ORGDelegator.getIJob().updateJob(aNewJobVO);
//				}			
//			}
//				
//			//******************************************************
//			if (jobdescvo != null) {
//				jobdescvo.setPk_om_job(aNewJobVO.getPk_om_job());
//				if (vo.getJobdescVO() != null
//						&& vo.getJobdescVO().getPrimaryKey() != null
//						&& vo.getJobdescVO().getPrimaryKey().toString().length() > 0) {
//					jobdescvo.setPk_job_sub(vo.getJobdescVO().getPrimaryKey());
//					//岗位描述
//					ORGDelegator.getIJob().updateJobdesc(jobdescvo);
//				} else {
//					//	      岗位描述
//					String descpk = ORGDelegator.getIJob().insertJobdesc(jobdescvo);
//					jobdescvo.setPk_job_sub(descpk);
//				}
//
//				jobanddesc.setjobdescvo(jobdescvo);
//			}
//			jobanddesc.setJobVO(aNewJobVO);
//			//若标准岗位带入子集则保存
//			if (SubVOS != null) {
//				ORGDelegator.getIJob()
//						.insertAllSub(SubVOS, tablist, aNewJobVO.getPk_om_job());
//				//		得到-岗位子集TableCode
//				if (aNewJobVO.getPk_om_job() != null) {
//					String tableCode = getBillCardPan().getCurrentBodyTableCode();
//					JobCurVO = getChildData(tableCode, aNewJobVO.getPk_om_job());
//					getBillCardPan().getBillData().setBodyValueVO(tableCode,
//							JobCurVO);
//				}
//				SubVOS = null;
//			}
			getFrameUI().freeLockRecord(aMainJobVO.getJobVO().getPrimaryKey());
			getFrameUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000223")/* @res "修改成功!" */);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getCause().getMessage());
		}//end if
	}
	
	/**
	 * 主表新增
	 */
	public boolean mainTableInsert() throws Exception {
		JobAndDesc aMainJobVO = getCurrentMainVO();
		if (!checkData(aMainJobVO.getJobVO()))
			return false;
		aMainJobVO.getJobVO().setPk_corp(getFrameUI().getLogin_Pk_corp());
		aMainJobVO.getJobVO().setCreatecorp(getFrameUI().getLogin_Pk_corp());

		if (!checkDescData(aMainJobVO.getJobdescVO()))
			return false;
		if (aMainJobVO.getJobVO().getIsAbort() == null) {
			aMainJobVO.getJobVO().setIsAbort(new UFBoolean("N"));
		}
		
		// 保存主表基本信息
		aMainJobVO.getJobVO().setUseShowValue(false);
		if(getFrameUI().isRefDuty()){
			aMainJobVO = ORGDelegator.getIJob().insertJobMainData(aMainJobVO, true, false);
			getFrameUI().setRefDuty(false);
		}else{
			aMainJobVO = ORGDelegator.getIJob().insertJobMainData(aMainJobVO, false, false);
		}//end if
		aMainJobVO.getJobVO().setUseShowValue(true);
		
		// 如果成功保存了主表信息，则尝试保存引用的子表信息，如果确实引用了基准岗位的子集数据
		if (getFrameUI().getStdJobSubRefDatas()!=null) {
			// 插入子表数据
			ORGDelegator.getIJob().insertAllGroupSub(
					getFrameUI().getStdJobSubRefDatas(), 
					getFrameUI().getStdJobSubTableCodes(), 
					aMainJobVO.getJobVO().getPk_om_job());
			// 显示代码统一处理
			getFrameUI().setStdJobSubRefDatas(null);
			getFrameUI().setStdJobSubTableCodes(null);
		}//end if
		
//		aNewJobVO.setPk_om_job(newPk[0]);
//		jobdescvo.setPk_om_job(aNewJobVO.getPk_om_job());
//		//如果引入的职务，把职务素质指标带入
//		if (getFrameUI().isRefDuty()) {
//			String pk_om_duty = aNewJobVO.getPk_om_duty();
//			String pk_om_job = aNewJobVO.getPk_om_job();
//			ORGDelegator.getIJob().jobNeedInserts(getFrameUI().getLogin_Pk_corp(), pk_om_duty, pk_om_job);
//			getFrameUI().setRefDuty(false);
//		}
//		jobanddesc.setjobdescvo(jobdescvo);
//		jobanddesc.setJobVO(aNewJobVO);
//		//岗位描述
//		String subpk = ORGDelegator.getIJob().insertJobdesc(jobdescvo);
//		jobdescvo.setPk_job_sub(subpk);

		//V35 add----begin--
		
		// 如果主表插入成功，则不进行单据号回滚，权益之计，应该统一在一个事务中进行
		getFrameUI().setCurrentJobCode(null);
		
		getFrameUI().clearLastJobCahce();
		
//		// 添加当前查询缓存
//		getFrameUI().addJobToQueryResult(aMainJobVO.getJobVO());
//		
//		// 清空当前JobVO关联的一切缓存
//		getFrameUI().removeJobShowCache(aMainJobVO.getJobVO());
		
		// 根据情况决定是否加入当前显示界面的缓存
		DefaultMutableTreeNode treeNode = getFrameUI().getLeftPanel().getSelectedNode();
		if(treeNode==null){
			// 如果没选中任何节点
			getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
		}else if(treeNode.getUserObject()==null || treeNode.getUserObject() instanceof String){
			// 选中的节点没有业务数据，比如根节点
			getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
		}else if(treeNode.getUserObject() instanceof OrgNodeVO){
			OrgNodeVO nodeVO = (OrgNodeVO)treeNode.getUserObject();
			
			if(nodeVO.getNodeType()==OrgNodeVO.NODETYPE_CORP && nodeVO.getPk_corp().equals(aMainJobVO.getJobVO().getPk_corp())){
				// 如果是公司节点，且和当前JobVO是一个公司
				getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
			}else if(nodeVO.getNodeType()==OrgNodeVO.NODETYPE_DEPT && nodeVO.getPk_node().equals(aMainJobVO.getJobVO().getPk_deptdoc())){
				// 如果是部门节点，且和当前JobVO是一个部门
				getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
			}//end if
		}else if(treeNode.getUserObject() instanceof DefdocVO){
			DefdocVO defVO = (DefdocVO)treeNode.getUserObject();
			if(defVO.getPrimaryKey().equals(aMainJobVO.getJobVO().getJobseries())){
				// 如果是岗位序列节点，且和当前JobVO是一个岗位序列
				getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
			}//end if
		}//end if
		
		getFrameUI().setCurrentMainVO(aMainJobVO);
		JobInfoMainPanel mp = (JobInfoMainPanel)getMainPanel();
		
		JobInfoLeftPanel lp = (JobInfoLeftPanel)getLeftPanel();
		lp.expendTree();
		lp.selectTreeNodeByPk(aMainJobVO.getJobVO().getPk_deptdoc());
	
		JobVO[] jobList = getFrameUI().getJobList();
		mp.getListPanel().getMainListPanel().getTableModel().setBodyDataVO(jobList);
		mp.getListPanel().getMainListPanel().getTableModel().execLoadFormula();
		
		int cursor = mp.getListPanel().getMainListPanel().getTableModel().getRowCount() -1;
		getFrameUI().setCurrnetJobCursor(cursor);
		//----end----
		//若标准岗位带入子集则保存
//			if (SubVOS != null) {
//				ORGDelegator.getIJob()
//						.insertAllSub(SubVOS, tablist, aNewJobVO.getPk_om_job());
//				//		得到-岗位子集TableCode
//				if (aNewJobVO.getPk_om_job() != null) {
//					String tableCode = getBillCardPan().getCurrentBodyTableCode();
//					JobCurVO = getChildData(tableCode, aNewJobVO.getPk_om_job());
//					getBillCardPan().getBillData().setBodyValueVO(tableCode,
//							JobCurVO);
//
//					billVo.addTableVO(tableCode, "", JobCurVO);//V35 add
//				}
//
//				SubVOS = null;
//			}
//			

//			
//			newAddJob = aNewJobVO;//V35 add
//			if(listSelectRow==-1){
//				
//			}
		
		getFrameUI().showHintMessage(
				NCLangRes.getInstance().getStrByID(
						"60050704", "UPP60050704-000222")/* @res "增加成功!" */);
		return true;

	}
	
	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	private List<JobdocVO> JobdocAdd = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocAdd2 = new ArrayList<JobdocVO>();

	/**
	 * 执行命令体
	 */
	public void execute() throws Exception {
		
		boolean isSuc = false;
		int status = getDataModel().getCurrentState();
		// 首先结束编辑
		getFrameUI().getMainPanel().getCardPanel().getBillCardPanel().stopEditing();
		// 按照状态分情况处理
		switch(status){
			case JobInfoStateReg.USERSTS_ADD_MAIN:{
				isSuc = this.mainTableInsert();
				break;
			}//end case
			
			case JobInfoStateReg.USERSTS_EDIT_MAIN:{
				JobVO jobVO = getFrameUI().getCurrentSelJobVO();
				if(jobVO==null){
					return;
				}//end if
				isSuc = this.mainTableUpdate();
				break;
			}//end case
			
			case JobInfoStateReg.USERSTS_EDIT_SUB:{
				JobVO jobVO = getFrameUI().getCurrentSelJobVO();
				if(jobVO==null){
					return;
				}//end if
				isSuc = this.subTableSave();
				break;
			}//end case
		}//end if
		
		// 结果判断
		if(!isSuc){ // 如果失败了
			return;
		}else{ // 如果成功了
			if(status==JobInfoStateReg.USERSTS_ADD_MAIN){
				getFrameUI().getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
			}else{
				getFrameUI().getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
			}//end if		
			
			getFrameUI().getMainPanel().getListPanel().mainListPanelDoubleClicked(getFrameUI().getCurrentJobCursor(), IBillItem.HEAD);
			
			
			// 岗位信息更新或新增时同步至中间表 add by river for 2011-09-14
			
			IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
			IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
		
			JobdocVO[] jobvos = (JobdocVO[])msg.getGeneralVOs(JobdocVO.class, " pk_om_job = '"+getCurrentMainVO().getJobVO().getPk_om_job().toString().trim()+"'");
			switch(status){
				case JobInfoStateReg.USERSTS_ADD_MAIN:{
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml5(jobvos[0], "RequestJob", "job", "add"));
					
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						JobdocAdd.add(jobvos[0]);
						Thread th1 = new Thread() {
							public void run() {
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								try {
									if(true) {
										this.sleep(SleepTime.Time);
									
										for(JobdocVO job : JobdocAdd) {
											String retStr = filepost.postFile(Uriread.uriPath() , 
													gener.generateXml5(job, "RequestJob", "job", "update"));
											
											String[] strs = retStr.split("<success>");
											String retMsg = "";
											if (strs.length > 1)
												retMsg = strs[1].substring(0, strs[1].indexOf("<"));
											
											if(retMsg.equals("false") || strs.length <= 1)
												JobdocAdd2.add(job);
										
										}
										
										JobdocAdd = JobdocAdd2;
										
										JobdocAdd2 = new ArrayList<JobdocVO>();
										
//										if(JobdocAdd.size() == 0)
//											break;
									}
									System.out.println("<<<<<<  岗位档案新增线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						};
						
						th1.start();
					}
					
					break;
				}//end case
				
				case JobInfoStateReg.USERSTS_EDIT_MAIN:{
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml5(jobvos[0], "RequestJob", "job", "update"));
					
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						JobdocUpdate.add(jobvos[0]);
						Thread th2 = new Thread() {
							public void run() {
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								try {
//									int i = 0;
									if(true) {
										this.sleep(SleepTime.Time);
									
										for(JobdocVO job : JobdocUpdate) {
											String retStr = filepost.postFile(Uriread.uriPath() , 
													gener.generateXml5(job, "RequestJob", "job", "add"));
											
											String[] strs = retStr.split("<success>");
											String retMsg = "";
											if (strs.length > 1)
												retMsg = strs[1].substring(0, strs[1].indexOf("<"));
											
											if(retMsg.equals("false") || strs.length <= 1)
												JobdocUpdate2.add(job);
										
										}
										
										JobdocUpdate = JobdocUpdate2;
										
										JobdocUpdate2 = new ArrayList<JobdocVO>();
										
//										if(JobdocUpdate.size() == 0)
//											break;
//										
//										i++;
//										if(i > 3)
//											break;
									}
									System.out.println("<<<<<<  岗位档案修改线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						};
						
						th2.start();
					}
					
					break;
				}//end case
			}
			
		}//end if
	}

}
