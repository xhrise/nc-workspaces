package nc.ui.om.om_005;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.frame.IHrBillCode;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.hr.pub.PubDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.action.AbstractAction;
import nc.ui.ml.NCLangRes;
import nc.vo.om.om_005.JobVO;
import nc.vo.yto.business.JobdocVO;
import nc.vo.yto.business.PsndocVO;

/**
 * ��λ��Ϣ��ť�¼���ɾ��
 * 
 * @author wangxing
 * 
 */
public class DeleteAction extends AbstractAction {

	private List<JobdocVO> JobdocDel = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocDel2 = new ArrayList<JobdocVO>();
	
	/**
	 * ���캯��
	 * 
	 * @param frameUI1
	 */
	public DeleteAction(FrameUI frameUI1) {
		super(frameUI1);

	}

	/**
	 * �˴����뷽�������� �������ڣ�(2003-6-1 16:03:15)
	 */
	public boolean deleteMain(JobVO jobVO) {
		boolean isSuccess = false;
		String pk_om_job = jobVO.getPk_om_job();
		try {
			int rdcount = ORGDelegator.getIJob().deleteJobByPk(pk_om_job);
			// ���ɾ���˼�¼
			if (rdcount > 0) {
				isSuccess = true;

				// getFrameUI().deleteFromQueryResult(pk_om_job);
				getFrameUI().deleteFromCurrentJobs(pk_om_job);
				// getFrameUI().removeJobShowCache(jobVO);

				getFrameUI().setFinishDelete(false);
				// ɾ����ǰѡ�е���
				getMainPanel().getListPanel().getMainListPanel().delLine();

				getFrameUI().setFinishDelete(true);

				int currentJobListSize = getFrameUI().getCurrentJobListSize();

				// ���ɾ����
				if (currentJobListSize > 0) {
					// ������ʾ��ǰѡ���е�����
					getMainPanel().getListPanel().justLoadRowData(
							getFrameUI().getCurrentJobCursor());
				} else {// ���û��ɾ��
					getFrameUI().setCurrnetJobCursor(-1);
					getMainPanel().getCardPanel().getBillCardPanel()
							.getBillData().clearViewData();
				}// end if

				// ��ʾ��Ϣ
				getFrameUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000219")/* @res "ɾ���ɹ�!" */);
			} else {// ���û��ɾ����¼
				isSuccess = false;
				getFrameUI().showWarningMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000220")/*
														 * @res
														 * "ɾ��ʧ�ܣ������Ǹø�λ�Ѳ����ڣ���ˢ�º����ԣ�"
														 */);
			}// end if

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() == null) {
				getFrameUI().showErrorMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000221")/*
														 * @res "ɾ��ʧ�ܣ���ˢ�º�����!"
														 */);
			} else {
				getFrameUI().showErrorMessage(e.getCause().getMessage());
			}// end if
		}// end try
		return isSuccess;
	}

	/**
	 * ִ��������
	 */
	public void execute() throws Exception {
		JobInfoMainPanel mp = (JobInfoMainPanel) getMainPanel();

		int selRowCount = mp.getListPanel().getMainListPanel().getTable()
				.getSelectedRowCount();
		if (selRowCount <= 0 || selRowCount > 1) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000233")/* @res "����ѡ��һ����λ��" */);
			return;
		}// end if

		JobVO jobVO = getFrameUI().getCurrentSelJobVO();

		// ��ʼ����ж�
		if (jobVO == null) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000233")/* @res "����ѡ��һ����λ��" */);
			return;
		} else if (!getFrameUI().getLogin_Pk_corp().equals(jobVO.getPk_corp())) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPT60050704-000208")/* @res "ֻ��ɾ������˾��λ��" */);
			return;
		}// end if

		boolean isLock = false;
		String pk_om_job = jobVO.getPk_om_job();

		try {
			// ����¼
			if (!getFrameUI().lockRecord(pk_om_job)) {
				getFrameUI()
						.showWarningMessage(
								NCLangRes.getInstance().getStrByID("6005",
										"UPP6005-000022")/* @res"��λ���ڱ������û����������Ժ�����!" */);
				return;
			} else {
				isLock = true;
			}// end if

			// ����ǻ�׼��λ������Ҫ�жϻ�׼��λ�Ƿ�����
			if (getFrameUI().isStdJob()) {
				int count = PubDelegator.getIPersistenceRetrieve()
						.statFieldCount("om_job", null, null, pk_om_job,
								"pk_jobdoc", pk_om_job, "pk_om_job");
				if (count > 0) {
					// UPT60050704-000209=�û�׼��λ�Ѿ������ã�������ɾ����
					getFrameUI()
							.showWarningMessage(
									NCLangRes.getInstance().getStrByID(
											"60050704", "UPT60050704-000209")/* @res"�û�׼��λ�Ѿ������ã�������ɾ����" */);
					return;
				}// end if
			}// end if

			// ����ɾ��
			if (ORGDelegator.getIJob().hasChild(pk_om_job)) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000230")/*
														 * @res
														 * "�ø�λ����������λ������ɾ����"
														 */);
				return;
			}// end if

			// ����ȷ�϶Ի���
			if (getFrameUI().showYesNoMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000231")/*
													 * @res "ɾ�������ݽ����ɻָ���Ҫ������"
													 */) == nc.ui.pub.beans.UIDialog.ID_YES) {
				
				// ɾ����λʱͬ�����м�� add by river for 2011-09-14 Ƭ��1
				IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
				JobdocVO[] jobvos = (JobdocVO[])msg.getGeneralVOs(JobdocVO.class, " pk_om_job = '"+jobVO.getPk_om_job().toString().trim()+"'");
				
				
				// ���������ݿ���ɾ��
				boolean isSuc = deleteMain(jobVO);

				// ���ɾ���ɹ��ˣ���ѵ��ݺ�Ҳ��Ӧ��ɾ��
				if (isSuc) {
					// �ع����ݱ���
					// HRBillCodeUtils.getInstance().returnBillCode(
					// jobVO.getJobcode(),
					// getFrameUI().getPkBillTypeCode(),
					// jobVO.getPk_corp(),
					// getFrameUI().getLogin_Uesrid()
					// );
					IHrBillCode iHrBillCode = ((IHrBillCode) NCLocator
							.getInstance().lookup(IHrBillCode.class.getName()));
					iHrBillCode.returnBillCodes(getFrameUI()
							.getPkBillTypeCode(), jobVO.getPk_corp(),
							getFrameUI().getLogin_Uesrid(), jobVO.getJobcode());

					IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
					IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				
					
					// ɾ����λʱͬ�����м�� add by river for 2011-09-14 Ƭ��2
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml5(jobvos[0], "RequestJob", "job", "del"));
					
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						JobdocDel.add(jobvos[0]);
						new Thread() {
							public void run() {
								
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								try {
									if(true) {
										this.sleep(SleepTime.Time);
									
										for(JobdocVO job : JobdocDel) {
											String retStr = filepost.postFile(Uriread.uriPath() , 
													gener.generateXml5(job, "RequestJob", "job", "del"));
											
											String[] strs = retStr.split("<success>");
											String retMsg = "";
											if (strs.length > 1)
												retMsg = strs[1].substring(0, strs[1].indexOf("<"));
											
											if(retMsg.equals("false") || strs.length <= 1)
												JobdocDel2.add(job);
										
										}
										
										JobdocDel = JobdocDel2;
										
										JobdocDel2 = new ArrayList<JobdocVO>();
										
//										if(JobdocDel.size() == 0)
//											break;
									}
									
									System.out.println("<<<<<<  ��λ����ɾ���߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				
				}// end if
			}// end if
			// ���� --V35 �޸� �Ƶ��������Ĳ�����

		} catch (Exception e) {
			getFrameUI().handleException(e);
		} finally {
			try {
				if (isLock)
					getFrameUI().freeLockRecord(pk_om_job);
			} catch (Exception e) {
				e.printStackTrace();
			}// end if
		}// end try
	}

	/**
	 * �õ�JobInfoUI
	 */
	public JobInfoUI getFrameUI() {
		return (JobInfoUI) super.getFrameUI();
	}

	/**
	 * �õ���Panel
	 */
	public JobInfoLeftPanel getLeftPanel() {
		return (JobInfoLeftPanel) super.getLeftPanel();
	}

	/**
	 * �õ���Panel
	 */
	public JobInfoMainPanel getMainPanel() {
		return (JobInfoMainPanel) super.getMainPanel();
	}

	/**
	 * �õ�����Panel
	 */
	public JobInfoTopPanel getTopPanel() {
		return (JobInfoTopPanel) super.getTopPanel();
	}

}
