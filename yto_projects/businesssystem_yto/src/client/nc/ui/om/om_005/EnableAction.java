package nc.ui.om.om_005;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.action.AbstractAction;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.om.om_005.JobVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.yto.business.JobdocVO;

/**
 * ��λ��Ϣ��ť�¼���ȡ������
 * @author wangxing
 *
 */
public class EnableAction extends AbstractAction {

	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	/**
	 * ���캯��
	 * @param frameUI1
	 */
	public EnableAction(FrameUI frameUI1) {
		super(frameUI1);
		
	}

	/**
	 * �õ��������Ի���
	 * @return
	 * @throws Exception
	 */
	private CancleDelDlg getEnableDialog(JobVO jobVO) throws Exception {
		return new CancleDelDlg(getFrameUI(), jobVO,
				NCLangRes.getInstance().getStrByID("60050704",
						"UPP60050704-000070")/*
											  * @res "������"
											  */);
	}
	
	/**
	 * ִ��������
	 */
	public void execute() throws Exception {
		boolean isSuccess = false;
		JobVO jobVO = getFrameUI().getCurrentSelJobVO();
		if(jobVO==null){
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000233")/* @res "����ѡ��һ����λ��" */);
			return;
		}else if(!getFrameUI().getLogin_Pk_corp().equals(jobVO.getPk_corp())){
			getFrameUI().showWarningMessage(NCLangRes.getInstance().getStrByID(
					"60050704", "UPT60050704-000204")/* @res "ֻ���޸ı���˾��λ��" */);
			return;
		}//end if
		
		try {
			//У���ϼ��Ƿ�����������ϼ���λ�ǳ��������÷�����
			String suporior = jobVO.getSuporior();
			if (StringUtils.hasText(suporior)) {
				JobVO jobvoFather = ORGDelegator.getIJob().findJobByPK(suporior);
				if (jobvoFather != null
						 && jobvoFather.getIsAbort()!=null && jobvoFather.getIsAbort().booleanValue()) {
//					UPT60050704-000210=�ø�λ���ϼ���λ[{0}]�Ѿ��������ø�λ���ܷ�������
					String msg = NCLangRes.getInstance().getStrByID(
							"60050704", "UPT60050704-000210", null,new String[]{jobvoFather.getJobname()})/* @res "ֻ���޸ı���˾��λ��" */;
					getFrameUI().showWarningMessage(msg);
					return;
				}//end if
			}//end if

			DeptdocVO belongDeptVO = ORGDelegator.getIJob().getDeptdocVOByJob(jobVO.getPk_om_job());//wangkf add
			
			if (belongDeptVO != null
					&& belongDeptVO.getHrcanceled().booleanValue()) {
//				UPT60050704-000211=�ø�λ��������[{0}]�Ѿ��������ø�λ���ܷ�������
				String msg = NCLangRes.getInstance().getStrByID(
						"60050704", "UPT60050704-000211", null,new String[]{belongDeptVO.getDeptname()})/* @res "ֻ���޸ı���˾��λ��" */; 
				getFrameUI().showWarningMessage(msg);
			} else {
				
				// ������
				if (!getFrameUI().lockRecord(jobVO.getPk_om_job())) {
					getFrameUI().showWarningMessage(NCLangRes.getInstance().getStrByID(
							"6005", "UPP6005-000022")/* @res"��λ���ڱ������û����������Ժ�����!" */);
					return;
				}//end if
				
				//-------------------
				int useroperate = getEnableDialog(jobVO).showModal();
				if(useroperate != UIDialog.ID_YES
						&& useroperate != UIDialog.ID_OK){
					return;
				}//end if
				
				// ����������ɹ�
				if (ORGDelegator.getIJob().abortVO(jobVO.getPk_om_job(), "N", getFrameUI().getEffectDate()) > 0) {
					jobVO.setIsAbort(new UFBoolean(false));
					isSuccess = true;
					getFrameUI().freeLockRecord(jobVO.getPk_om_job());//wangkf add
					getFrameUI().showHintMessage(
							NCLangRes.getInstance()
									.getStrByID("60050704",
											"UPP60050704-000273")/*
																  * @res
																  * "�������ɹ�!"
																  */);
					
					// ����������ɹ�����
					// ������ʾ��ǰ�е�����
					getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
				
					// ������λʱͬ�����м�� add by river for 2011-09-14
			    	IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			    	IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
					IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				
			    	JobdocVO[] jobvos = (JobdocVO[])msg.getGeneralVOs(JobdocVO.class, " pk_om_job in ('"+jobVO.getPk_om_job()+"')");
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml5(jobvos[0], "RequestJob", "job", "update"));
				
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						JobdocUpdate.add(jobvos[0]);
						new Thread() {
							public void run() {
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								
								try {
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
									}
									System.out.println("<<<<<<  ��λ�����޸��߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					
				} else {// ���ʧ��
					isSuccess = false;
					getFrameUI().freeLockRecord(jobVO.getPk_om_job());//wangkf add
					getFrameUI().showWarningMessage(
							NCLangRes.getInstance()
									.getStrByID("60050704",
											"UPP60050704-000274")/*
																  * @res
																  * "������ʧ�ܣ���ˢ�º����ԣ�"
																  */);
				}//end if
				

			}
		} catch (Exception e) {
			e.printStackTrace();
			
			getFrameUI().showErrorMessage(
					NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000275")/*
															  * @res "������ʧ��!"
															  */);
		}finally{
			getFrameUI().freeLockRecord(jobVO.getPk_om_job());
		}//end try
	}
	
	/**
	 * �õ�JobInfoUI
	 */
	public JobInfoUI getFrameUI() {
		return (JobInfoUI)super.getFrameUI();
	}

	/**
	 * �õ���Panel
	 */
	public JobInfoLeftPanel getLeftPanel() {
		return (JobInfoLeftPanel)super.getLeftPanel();
	}

	/**
	 * �õ���Panel
	 */
	public JobInfoMainPanel getMainPanel() {
		return (JobInfoMainPanel)super.getMainPanel();
	}

	/**
	 * �õ�����Panel
	 */
	public JobInfoTopPanel getTopPanel() {
		return (JobInfoTopPanel)super.getTopPanel();
	}

}
