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
 * ��λ��Ϣ��ť�¼�������
 * @author wangxing
 *
 */
public class SaveAction extends AbstractAction {
	
	// ��Ҫʹ�õ�һЩ˽�г���
	
	 /*
	  * @res "����ָ��"
	  */
	private final String INDEX_NATURE = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000039");
	
	 /*
	  * @res "����ָ��"
	  */
	private final String INDEX_QUALITY = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000038");
	
	/*
	 * @res "�ǽ׶���"
	 */
	private final String PERIOD_NOT = NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000040");
	
	/*
	 * @res "���"
	 */
	private final String PERIOD_YEAR = NCLangRes.getInstance().getStrByID("common","UC000-0001802");
	
	/*
	 * @res "����"
	 */
	private final String PERIOD_HALFYEAR = NCLangRes.getInstance().getStrByID("common","UC000-0000725");
	
	/*
	 * @res "����"
	 */
	private final String PERIOD_SEASON = NCLangRes.getInstance().getStrByID("common","UC000-0001492");

	/*
	 * @res "�·�"
	 */
	private final String PERIOD_MONTH = NCLangRes.getInstance().getStrByID("common","UC000-0002495");
	/**
	 * ���캯��
	 * @param frameUI1
	 */
	public SaveAction(FrameUI frameUI1) {
		super(frameUI1);
		
	}
	
	/**
	 * ��д���෽��
	 * @return
	 */
	public JobInfoUI getFrameUI(){
		return (JobInfoUI)super.getFrameUI();
	}
	
	/**
	 * У�鷽����У���������ְ�ʸ��������Ϣ
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
							"UPP60050704-000209")/* @res "ѧ��Ҫ���ַ�����!" */);
			return false;
		}
		if (vo.getReqexp() != null
				&& vo.getReqexp().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000213")/* @res "��������Ҫ���ַ�����!" */);
			return false;
		}
		if (vo.getReqpro() != null
				&& vo.getReqpro().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000212")/* @res "רҵ����Ҫ���ַ�����!" */);
			return false;
		}
		if (vo.getReqsex() != null
				&& vo.getReqsex().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000210")/* @res "�Ա�Ҫ���ַ�����!" */);
			return false;
		}
		if (vo.getReqworktime() != null
				&& vo.getReqworktime().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000214")/* @res "����ʱ���ַ�����!" */);
			return false;
		}
		if (vo.getReqyold() != null
				&& vo.getReqyold().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000211")/* @res "����Ҫ���ַ�����!" */);
			return false;
		}
		
		if (vo.getReqother() != null
				&& vo.getReqother().toString().trim().length() > 512) {
			MessageDialog.showErrorDlg(getFrameUI(), getFrameUI().getTitle(),
					NCLangRes.getInstance().getStrByID("60050704", "UPP60050704-000279"))/* @res "����Ҫ���ַ�����!" */;
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
	 * У��һ���ӱ�����
	 * @param vos
	 * @return boolean �Ƿ�ɹ�
	 * @throws Exception �쳣��Ϣ�а���������Ϣ
	 */
	public boolean checkChildData(HRSubVO[] vos) throws Exception {
		if(vos==null || vos.length<=0){
			return true;
		}
		JobInfoUI ui = (JobInfoUI)getFrameUI();
		BillCardPanel bcp = ui.getMainPanel().getCardPanel().getBillCardPanel();
		String curTableCode = vos[0].getTablename();
		BillItem[] items = bcp.getBillData().getBodyItemsForTable(curTableCode);
		
		//�����ǿ�У��
		try {
			Validate validate = new Validate();
			for(HRSubVO aVO : vos){
				validate.dataNotNullValidate(items, aVO);
			}//end for
		} catch (Exception e) {
			ui.showWarningMessage(e.getMessage());
			return false;
		}//end if
		
		// ����У���ظ���¼�Ļ���
		Map<String, String> map = new HashMap<String, String>();
		String tmpPk = null;
	// ѭ��У��ÿһ��VO
	for(HRSubVO vo : vos){
		//����ָ��
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
																	  * "����ָ�������Ƕ���ָ��ʱ������Ŀ�겻�ܴ���12λ!"
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
																  * "����ָ�������Ƕ���ָ��ʱ������Ŀ��ӦΪ����!"
																  */);
					return false;
				}//end try
			}//end if

		}//end if
		
		//�ڸ���ѵ
		if ("om_jobtraining".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_trianitem");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(NCLangRes.getInstance().getStrByID(
						"60050704", "UPP60050704-000185")/*
														  * @res
														  * "��ѵ��Ŀ�����ظ���"
														  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��ǰ��ѵ
		if ("om_jobbeftrain".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_trianitem");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(NCLangRes.getInstance().getStrByID(
						"60050704", "UPP60050704-000185")/*
														  * @res
														  * "��ѵ��Ŀ�����ظ���"
														  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λ�칫�豸
		if ("om_jobequipment".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("equipmentno");
			
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000188")/*
																  * @res
																  * "�豸��Ų����ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λ����Ŀ��
		if ("om_jobgoal".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("vcode");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000190")/*
																  * @res
																  * "���벻���ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λְ��
		if ("om_jobresp".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_resptype");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000192")/*
																  * @res
																  * "ְ�����Ͳ����ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
			
		}//end if
		
		//��λ�ල
		if ("om_jobsuperv".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_supervtype");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000194")/*
																  * @res
																  * "�ල���Ͳ����ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λ�Ӵ����
		if ("om_jobcont".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("pk_contactobj");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000196")/*
																  * @res
																  * "�Ӵ��������ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λ����Ȩ��
		if ("om_jobpower".equals(curTableCode)) {
			tmpPk = curTableCode+vo.getAttributeValue("vpowername");
			if(map.containsKey(tmpPk)){
				ui.showWarningMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000198")/*
																  * @res
																  * "Ȩ�����Ʋ����ظ���"
																  */);
				return false;
			}else{
				map.put(tmpPk, tmpPk);
			}//end if
		}//end if
		
		//��λ����ָ��
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
													  * "��ͬ��������ָ�����Ʋ����ظ���"
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
	 * У�鷽����У������Ļ�����Ϣ
	 * 
	 * @return boolean
	 */
	public boolean checkData(JobVO jobvo) throws Exception {
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		int sts = getDataModel().getCurrentState();
		if (getFrameUI().isLoginGroup()) {//����У��
			if (jobvo.getJobcode() == null
					|| jobvo.getJobcode().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000201")/* @res "��λ���벻��Ϊ��" */);
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
																	  * "��λ�ϼ�����ѡ�Լ�"
																	  */);
					return false;
				}//end if
				
				//add by zhyan 2005-11-17 ֱ���ϼ�������ֱ���¼����߼���¼�
				if (ORGDelegator.getIJob().isCircle(Pk_om_job, Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000277")/*
																	  * @res
																	  * "��λֱ���ϼ�������ֱ���¼����߼���¼�"
																	  */);
					return false;
				}//end if
			}//end if
			
			if (jobvo.getJobname() == null
					|| jobvo.getJobname().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000203")/* @res "��λ���Ʋ���Ϊ��" */);
				return false;
			}//end if
			
			if (jobvo.getBuilddate() == null
					|| jobvo.getBuilddate().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000205")/* @res "�������ڲ���Ϊ��" */);
				return false;
			}//end if
			
			if (jobvo.getJobseries() == null
					|| jobvo.getJobseries().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000206")/* @res "��λ���в���Ϊ��" */);
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
															  * "��λ���в���Ϊ��"
															  */);
						return false;
					}//end if
				} catch (Exception e1) {
					e1.printStackTrace();
				}//end try

			}//end if

			// added by zhangdd 2009.7.13 modify for ���������������� �� bug���NCdp200910711
			if(!getFrameUI().isStdJob()){// ������ǻ�׼��λ��Ҫ�жϸ�λ�ȼ��Ƿ�Ϊ��
				if (jobvo.getJobrank() == null
						|| jobvo.getJobrank().toString().trim().length() < 1) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000207")/* @res "��λ�ȼ�����Ϊ��" */);
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
															  * "��ѡְ��ĸ�λ������ø�λ��ѡ��λ���в���!"
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
			
			// У���Զ�����ˮ��, added by walkfire V5.02 2007-12-19
			// ֻ��������״̬��У�鵥�ݺ�
			if( !getFrameUI().isAutoJobCode() && sts==JobInfoStateReg.USERSTS_ADD_MAIN ){
				try{
					getFrameUI().checkCustomBillCode(getFrameUI().getPkBillTypeCode(), jobvo.getJobcode());
				}catch(Exception e){
					e.printStackTrace();
				}//end try
			}//end if
			
			return true;
		} else {//��˾У��
			if (jobvo.getJobcode() == null
					|| jobvo.getJobcode().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000201")/* @res "��λ���벻��Ϊ��" */);
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
																	  * "��λ�ϼ�����ѡ�Լ�"
																	  */);
					return false;
				}//end if
				
				//add by zhyan 2005-11-17 ֱ���ϼ�������ֱ���¼����߼���¼�
				if (ORGDelegator.getIJob().isCircle(Pk_om_job, Suporior)) {
					getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000277")/*
																	  * @res
																	  * "��λֱ���ϼ�������ֱ���¼����߼���¼�"
																	  */);
					return false;
				}//end if
			}//end if
			
			if (jobvo.getJobname() == null
					|| jobvo.getJobname().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000203")/* @res "��λ���Ʋ���Ϊ��" */);
				return false;
			}//end if
			
			if (jobvo.getPk_deptdoc() == null
					|| jobvo.getPk_deptdoc().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000204")/* @res "�������Ų���Ϊ��" */);
				return false;
			}//end if
			
			//wangkf add
			IDeptdocQry ideptqury = ((IDeptdocQry)NCLocator.getInstance().lookup(IDeptdocQry.class.getName()));
			nc.vo.bd.b04.DeptdocVO deptvo = ideptqury.findDeptdocVOByPK(jobvo.getPk_deptdoc());
			if (deptvo.getHrcanceled() != null && deptvo.getHrcanceled().booleanValue() ) {			
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPT60050704-000171")/* @res "�������Ų���Ϊ��" */);
				return false;
			}//end if
			
			if (jobvo.getBuilddate() == null
					|| jobvo.getBuilddate().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000205")/* @res "�������ڲ���Ϊ��" */);
				return false;
			}//end if
			
			if (jobvo.getJobseries() == null
					|| jobvo.getJobseries().toString().trim().length() < 1) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000206")/* @res "��λ���в���Ϊ��" */);
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
																	  * "��λ���в���Ϊ��"
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
								"60050704", "UPP60050704-000207")/* @res "��λ�ȼ�����Ϊ��" */);
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
																	  * "��ѡְ��ĸ�λ������ø�λ��ѡ��λ���в���!"
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
			
			// У���Զ�����ˮ��, added by walkfire V5.02 2007-12-19
			// ֻ��������״̬��У�鵥�ݺ�
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
	 * �õ���ǰ��ͷ�Ļ�����ϢVO
	 * 
	 * @return nc.vo.om.om_005.JobVO
	 */
	protected JobAndDesc getCurrentMainVO() throws Exception {
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		JobAndDesc headvo = getFrameUI().createNewMainVO();

		bcp.getBillData().getHeaderValueVO(headvo);
		
		// �����Զ���Ĳ����ֶ�
		JobVO jobVO = headvo.getJobVO();
		if(!jobVO.isHasUserDefineFields()){
			return headvo;
		}//end if
		
		// ��ʼ�����Զ����ֶ�,groupdef
		BillItem[] items = bcp.getHeadItems();
		if(items==null || items.length<=0){
			return headvo;
		}//end if
		
		for(BillItem item : items){
			// ������Զ�������ֶ�
			if(item.isShow() && StringUtils.hasText(item.getKey()) && item.getKey().startsWith("groupdef") && item.getDataType()==BillItem.UFREF){
				UIRefPane refP = (UIRefPane)item.getComponent();
				String refValue = refP.getText();
				jobVO.setUserDefineFieldShowValue(item.getKey(), refValue);
			}//end if
		}//end for
		
		return headvo;

	}

//	/**
//	 * �õ���ǰ��ͷ��������ϢVO
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
	 * �õ���ǰ�ӱ��Ԫ����VO
	 * @return
	 */
	private HRSubVO getCurrentSubTabMetaVO(){
		HRSubVO vo = new HRSubVO();
		BillCardPanel bcp = getFrameUI().getMainPanel().getCardPanel().getBillCardPanel();
		String currentSubCode = bcp.getCurrentBodyTableCode();
		vo.setTablename(currentSubCode);
		vo.setPksubname("pk_job_sub");
		vo.setPkname("pk_om_job");
		
		// �õ����е�BillItems
		BillItem[] items = bcp.getBillModel(currentSubCode).getBodyItems();
		if(items==null || items.length<=0){
			return vo;
		}//end if
		
		boolean isExistLastflag = false;
		boolean isExistRecordnum = false;
		Vector<String> vFldNames = new Vector<String>();
		Vector<Integer> vFldTypes = new Vector<Integer>();
		
		for(int i=0; i<items.length; i++){
			// ��ʾ�õ��ֶβ��ڱ��淶Χ֮��
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
		
		// ��������� recordnum �ֶ�
		if(!isExistRecordnum){
			vFldNames.addElement("recordnum");
			vFldTypes.addElement(new Integer(IBillItem.INTEGER));
		}//end if
		
		// ��������� lastfalg �ֶ�
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
	 * ��VO�е���ʾֵ����Ϊ���ݿ��Ӧֵ�����⴦��
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
			// ������һ�������
			if("om_jobmeasure".equals(aVO.getTablename())){
				// �����޸� ideterminate ָ������
				tmpValue = (String)aVO.getAttributeValue("om_jobmeasure");
				if(INDEX_NATURE.equals(tmpValue)){
					aVO.setAttributeValue("om_jobmeasure", "1");
				}else if(INDEX_QUALITY.equals(tmpValue)){
					aVO.setAttributeValue("om_jobmeasure", "0");
				}//end if
				
				// �޸� iperiodtype
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
				
				// �޸� chalfy
				
				// �޸� cquarter
				
				// �޸� cmonth
			}//end if om_jobmeasure
			
			
			// ͳһ���� UFBoolean����
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
			
			// ͳһ��������
			aVO.setPk_main(jobVO.getPk_om_job());
			
			// ����recordnum��lastfalg
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
	 * �õ���ǰҪ������ӱ�VO
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
	 * �ӱ���
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
		//�༭�����Ӽ�
		HRSubVO[] savingVOs = getCurSubSavingVOs();
		// У���ӱ���Ϣ
		if(!this.checkChildData(savingVOs)){
			return false;
		}//end if
		
		// �������浽���ݿ���
		// ���������ӱ���Ҳֻ�������ݿ�����ն�Ӧ���ӱ���Ϣ��������ܵ���������
		if(savingVOs==null || savingVOs.length<=0){
			PubDelegator.getIPersistenceUpdate().executeSQLs(new String[]{
					"delete from "+tbcode+" where pk_om_job='"+getFrameUI().getCurrentSelJobVO().getPk_om_job()+"' "
			});
		}else{
			// ��Ӹ�λ��Ϣ����
			for(HRSubVO subVO : savingVOs){
				subVO.setPk_main(getFrameUI().getCurrentSelJobVO().getPk_om_job());
			}//end for
			
			// ����ʵ�ʵ�VO������Ҳ�Ǹ���ʽ���棬���������ʷ���ݣ�Ȼ�����¸��Ǳ���
			String[] pks = ORGDelegator.getISub().saveSubsByVOStatus(savingVOs);
		}//end if
		
		// ����ӱ��������
		JobInfoUI ui = (JobInfoUI)getFrameUI();
		getFrameUI().freeLockRecord(ui.getCurrentSelJobVO().getPk_om_job()+"_"+tbcode);
		
		getFrameUI().clearCurrentJobSubCahce();
		return true;
		//���ص�����
		
//		vo.setAttributeValue("lastfalg", "N");
//		if (checkChildData(vo)) {
//			if (InsertType != UI_Update) {
//				//����������
//				vo.setAttributeValue("pk_om_job", getCurSelectJob());
//				vo.setAttributeValue("pk_corp", pk_corp);
//				if (InsertType == UI_Insert) {
//					vo.setAttributeValue("recordnum", new Integer(
//							billcardpan.getBillTable(tbcode)
//									.getRowCount()
//									- 1 - iEditRow).toString());
//					Key = controlChildAdd(vo, true);
//					//������
//					setVOsToHash(vo, true, iEditRow);
//				} else if (InsertType == UI_Add) {
//					vo.setAttributeValue("recordnum", "0");
//					Key = controlChildAdd(vo, false);
//					//������
//					setVOsToHash(vo, false, iEditRow);
//
//				}
//				//�ѷ��ص�����ֵ����VO
//				vo.setAttributeValue("pk_job_sub", Key);
//				//��VO����BillCardPanel
//				billcardpan.getBillModel().setBodyRowVO(vo, iEditRow);
//
//			} else {
//				//				vo = getCurSubVO();
//				vo.setAttributeValue("recordnum", new Integer(
//						billcardpan.getBillTable(tbcode).getRowCount()
//								- 1 - iEditRow).toString());
//				controlChildUpdate(vo);
//				//������
//				setVOsToHash(vo, false, iEditRow);
//				freeLockRecord(vo.getPrimaryKey()
//						+ tbcode);//wangkf add
//			}
//			setChildStat(UI_Browse);
//			nc.bs.logging.Logger.error("��ǰ�༭����Ϊ��" + iEditRow + "�С�");
//			if (billcardpan.getBillTable().getRowCount() > 0) {
//				billcardpan.getBillTable().setRowSelectionInterval(
//						iEditRow, iEditRow);
//			}
//			//���
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
	 * �ж��Ƿ���Ҫͬ���ķ���
	 * @param oldVO
	 * @param newVO
	 * @return
	 */
	private boolean isNeedSyncCorpJob(JobAndDesc oldVO, JobAndDesc newVO){
		// ������ǻ�׼��λ�ڵ㣬����Ҫͬ��
		if(!getFrameUI().isStdJob()){
			return false;
		}//end if
		
		// ������ݳ��ִ�������Ҫͬ��
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
	 * �������
	 */
	public boolean mainTableUpdate() throws Exception {
		JobAndDesc aMainJobVO = null;
		try {
			aMainJobVO = getCurrentMainVO();

			// ����λ���У���λ�ȼ�����ְ�����仯ʱ��ͬ����ְ���еļ�¼
			String old_jobseries = null;
			String old_jobrank = null;
			String old_duty = null;
			JobAndDesc oldMainVO = (JobAndDesc) getFrameUI().getCurrentCardVO().getParentVO();
			if (oldMainVO != null) {
				
				aMainJobVO.getJobVO().setPk_corp(oldMainVO.getJobVO().getPk_corp());
				aMainJobVO.getJobVO().setCreatecorp(oldMainVO.getJobVO().getCreatecorp());
				
				// �õ��ɵĸ�λ���У���λ�ȼ�����ְ��
				old_jobseries = oldMainVO.getJobVO().getJobseries();
				old_jobrank = oldMainVO.getJobVO().getJobrank();
				old_duty = oldMainVO.getJobVO().getPk_om_duty();
				
				// ����û�и�λ��ְ�ʸ�����
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
			
			// �Ƿ���Ҫͬ����˾��λ
			boolean isNeedSyncCorpJob = isNeedSyncCorpJob(oldMainVO, aMainJobVO);
			if(isNeedSyncCorpJob){
				int resDlg = MessageDialog.showYesNoDlg(getFrameUI(), 
						NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "ȷ�϶Ի���" */, 
//						UPT60050704-000288=�Ƿ�ͬ�����¹�˾���ø�λ�е���Ϣ��
						NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000288"));
				isNeedSyncCorpJob = resDlg==MessageDialog.ID_YES;
			}//end if
			// �Ƿ�ͬ����Ա��ְ��
			// modified by zhangdd 2009.7.13 modify for ���������������� �� bug���NCdp200910711
			boolean isSyncPsn = !aMainJobVO.getJobVO().getJobseries().equals(old_jobseries) || 
				(StringUtils.hasText(old_duty) && !old_duty.equals(aMainJobVO.getJobVO().getPk_om_duty()));
			
			String jobRank = aMainJobVO.getJobVO().getJobrank();
			isSyncPsn = isSyncPsn || (jobRank!=null&&old_jobrank!=null&&!aMainJobVO.getJobVO().getJobrank().equals(old_jobrank));
			
			// �����������ݿ�
			aMainJobVO.getJobVO().setUseShowValue(false);
			ORGDelegator.getIJob().updateJobMainData(aMainJobVO, getFrameUI().getDutyType(), isNeedSyncCorpJob, isSyncPsn);
			aMainJobVO.getJobVO().setUseShowValue(true);

			// ����ɹ�������������Ϣ�����Ա������õ��ӱ���Ϣ�����ȷʵ�����˻�׼��λ���Ӽ�����
			if (getFrameUI().getStdJobSubRefDatas()!=null) {
				// �����ӱ�����
				ORGDelegator.getIJob().insertAllGroupSub(
						getFrameUI().getStdJobSubRefDatas(), 
						getFrameUI().getStdJobSubTableCodes(), 
						aMainJobVO.getJobVO().getPk_om_job());
				// ��ʾ����ͳһ����
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
//					//��λ����
//					ORGDelegator.getIJob().updateJobdesc(jobdescvo);
//				} else {
//					//	      ��λ����
//					String descpk = ORGDelegator.getIJob().insertJobdesc(jobdescvo);
//					jobdescvo.setPk_job_sub(descpk);
//				}
//
//				jobanddesc.setjobdescvo(jobdescvo);
//			}
//			jobanddesc.setJobVO(aNewJobVO);
//			//����׼��λ�����Ӽ��򱣴�
//			if (SubVOS != null) {
//				ORGDelegator.getIJob()
//						.insertAllSub(SubVOS, tablist, aNewJobVO.getPk_om_job());
//				//		�õ�-��λ�Ӽ�TableCode
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
							"60050704", "UPP60050704-000223")/* @res "�޸ĳɹ�!" */);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getCause().getMessage());
		}//end if
	}
	
	/**
	 * ��������
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
		
		// �������������Ϣ
		aMainJobVO.getJobVO().setUseShowValue(false);
		if(getFrameUI().isRefDuty()){
			aMainJobVO = ORGDelegator.getIJob().insertJobMainData(aMainJobVO, true, false);
			getFrameUI().setRefDuty(false);
		}else{
			aMainJobVO = ORGDelegator.getIJob().insertJobMainData(aMainJobVO, false, false);
		}//end if
		aMainJobVO.getJobVO().setUseShowValue(true);
		
		// ����ɹ�������������Ϣ�����Ա������õ��ӱ���Ϣ�����ȷʵ�����˻�׼��λ���Ӽ�����
		if (getFrameUI().getStdJobSubRefDatas()!=null) {
			// �����ӱ�����
			ORGDelegator.getIJob().insertAllGroupSub(
					getFrameUI().getStdJobSubRefDatas(), 
					getFrameUI().getStdJobSubTableCodes(), 
					aMainJobVO.getJobVO().getPk_om_job());
			// ��ʾ����ͳһ����
			getFrameUI().setStdJobSubRefDatas(null);
			getFrameUI().setStdJobSubTableCodes(null);
		}//end if
		
//		aNewJobVO.setPk_om_job(newPk[0]);
//		jobdescvo.setPk_om_job(aNewJobVO.getPk_om_job());
//		//��������ְ�񣬰�ְ������ָ�����
//		if (getFrameUI().isRefDuty()) {
//			String pk_om_duty = aNewJobVO.getPk_om_duty();
//			String pk_om_job = aNewJobVO.getPk_om_job();
//			ORGDelegator.getIJob().jobNeedInserts(getFrameUI().getLogin_Pk_corp(), pk_om_duty, pk_om_job);
//			getFrameUI().setRefDuty(false);
//		}
//		jobanddesc.setjobdescvo(jobdescvo);
//		jobanddesc.setJobVO(aNewJobVO);
//		//��λ����
//		String subpk = ORGDelegator.getIJob().insertJobdesc(jobdescvo);
//		jobdescvo.setPk_job_sub(subpk);

		//V35 add----begin--
		
		// ����������ɹ����򲻽��е��ݺŻع���Ȩ��֮�ƣ�Ӧ��ͳһ��һ�������н���
		getFrameUI().setCurrentJobCode(null);
		
		getFrameUI().clearLastJobCahce();
		
//		// ��ӵ�ǰ��ѯ����
//		getFrameUI().addJobToQueryResult(aMainJobVO.getJobVO());
//		
//		// ��յ�ǰJobVO������һ�л���
//		getFrameUI().removeJobShowCache(aMainJobVO.getJobVO());
		
		// ������������Ƿ���뵱ǰ��ʾ����Ļ���
		DefaultMutableTreeNode treeNode = getFrameUI().getLeftPanel().getSelectedNode();
		if(treeNode==null){
			// ���ûѡ���κνڵ�
			getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
		}else if(treeNode.getUserObject()==null || treeNode.getUserObject() instanceof String){
			// ѡ�еĽڵ�û��ҵ�����ݣ�������ڵ�
			getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
		}else if(treeNode.getUserObject() instanceof OrgNodeVO){
			OrgNodeVO nodeVO = (OrgNodeVO)treeNode.getUserObject();
			
			if(nodeVO.getNodeType()==OrgNodeVO.NODETYPE_CORP && nodeVO.getPk_corp().equals(aMainJobVO.getJobVO().getPk_corp())){
				// ����ǹ�˾�ڵ㣬�Һ͵�ǰJobVO��һ����˾
				getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
			}else if(nodeVO.getNodeType()==OrgNodeVO.NODETYPE_DEPT && nodeVO.getPk_node().equals(aMainJobVO.getJobVO().getPk_deptdoc())){
				// ����ǲ��Žڵ㣬�Һ͵�ǰJobVO��һ������
				getFrameUI().addJobToJobList(aMainJobVO.getJobVO());
			}//end if
		}else if(treeNode.getUserObject() instanceof DefdocVO){
			DefdocVO defVO = (DefdocVO)treeNode.getUserObject();
			if(defVO.getPrimaryKey().equals(aMainJobVO.getJobVO().getJobseries())){
				// ����Ǹ�λ���нڵ㣬�Һ͵�ǰJobVO��һ����λ����
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
		//����׼��λ�����Ӽ��򱣴�
//			if (SubVOS != null) {
//				ORGDelegator.getIJob()
//						.insertAllSub(SubVOS, tablist, aNewJobVO.getPk_om_job());
//				//		�õ�-��λ�Ӽ�TableCode
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
						"60050704", "UPP60050704-000222")/* @res "���ӳɹ�!" */);
		return true;

	}
	
	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	private List<JobdocVO> JobdocAdd = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocAdd2 = new ArrayList<JobdocVO>();

	/**
	 * ִ��������
	 */
	public void execute() throws Exception {
		
		boolean isSuc = false;
		int status = getDataModel().getCurrentState();
		// ���Ƚ����༭
		getFrameUI().getMainPanel().getCardPanel().getBillCardPanel().stopEditing();
		// ����״̬���������
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
		
		// ����ж�
		if(!isSuc){ // ���ʧ����
			return;
		}else{ // ����ɹ���
			if(status==JobInfoStateReg.USERSTS_ADD_MAIN){
				getFrameUI().getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
			}else{
				getFrameUI().getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
			}//end if		
			
			getFrameUI().getMainPanel().getListPanel().mainListPanelDoubleClicked(getFrameUI().getCurrentJobCursor(), IBillItem.HEAD);
			
			
			// ��λ��Ϣ���»�����ʱͬ�����м�� add by river for 2011-09-14
			
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
									System.out.println("<<<<<<  ��λ���������߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
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
									System.out.println("<<<<<<  ��λ�����޸��߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
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
