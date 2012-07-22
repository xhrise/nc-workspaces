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
 * 岗位信息按钮事件，删除
 * 
 * @author wangxing
 * 
 */
public class DeleteAction extends AbstractAction {

	private List<JobdocVO> JobdocDel = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocDel2 = new ArrayList<JobdocVO>();
	
	/**
	 * 构造函数
	 * 
	 * @param frameUI1
	 */
	public DeleteAction(FrameUI frameUI1) {
		super(frameUI1);

	}

	/**
	 * 此处插入方法描述。 创建日期：(2003-6-1 16:03:15)
	 */
	public boolean deleteMain(JobVO jobVO) {
		boolean isSuccess = false;
		String pk_om_job = jobVO.getPk_om_job();
		try {
			int rdcount = ORGDelegator.getIJob().deleteJobByPk(pk_om_job);
			// 如果删除了记录
			if (rdcount > 0) {
				isSuccess = true;

				// getFrameUI().deleteFromQueryResult(pk_om_job);
				getFrameUI().deleteFromCurrentJobs(pk_om_job);
				// getFrameUI().removeJobShowCache(jobVO);

				getFrameUI().setFinishDelete(false);
				// 删除当前选中的行
				getMainPanel().getListPanel().getMainListPanel().delLine();

				getFrameUI().setFinishDelete(true);

				int currentJobListSize = getFrameUI().getCurrentJobListSize();

				// 如果删空了
				if (currentJobListSize > 0) {
					// 重新显示当前选中行的数据
					getMainPanel().getListPanel().justLoadRowData(
							getFrameUI().getCurrentJobCursor());
				} else {// 如果没有删空
					getFrameUI().setCurrnetJobCursor(-1);
					getMainPanel().getCardPanel().getBillCardPanel()
							.getBillData().clearViewData();
				}// end if

				// 提示信息
				getFrameUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000219")/* @res "删除成功!" */);
			} else {// 如果没有删除记录
				isSuccess = false;
				getFrameUI().showWarningMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000220")/*
														 * @res
														 * "删除失败，可能是该岗位已不存在，请刷新后再试！"
														 */);
			}// end if

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() == null) {
				getFrameUI().showErrorMessage(
						NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000221")/*
														 * @res "删除失败，请刷新后再试!"
														 */);
			} else {
				getFrameUI().showErrorMessage(e.getCause().getMessage());
			}// end if
		}// end try
		return isSuccess;
	}

	/**
	 * 执行命令体
	 */
	public void execute() throws Exception {
		JobInfoMainPanel mp = (JobInfoMainPanel) getMainPanel();

		int selRowCount = mp.getListPanel().getMainListPanel().getTable()
				.getSelectedRowCount();
		if (selRowCount <= 0 || selRowCount > 1) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000233")/* @res "请先选中一个岗位！" */);
			return;
		}// end if

		JobVO jobVO = getFrameUI().getCurrentSelJobVO();

		// 初始情况判断
		if (jobVO == null) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000233")/* @res "请先选中一个岗位！" */);
			return;
		} else if (!getFrameUI().getLogin_Pk_corp().equals(jobVO.getPk_corp())) {
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPT60050704-000208")/* @res "只能删除本公司岗位！" */);
			return;
		}// end if

		boolean isLock = false;
		String pk_om_job = jobVO.getPk_om_job();

		try {
			// 锁记录
			if (!getFrameUI().lockRecord(pk_om_job)) {
				getFrameUI()
						.showWarningMessage(
								NCLangRes.getInstance().getStrByID("6005",
										"UPP6005-000022")/* @res"岗位正在被其它用户操作，请稍后再试!" */);
				return;
			} else {
				isLock = true;
			}// end if

			// 如果是基准岗位，还需要判断基准岗位是否被引用
			if (getFrameUI().isStdJob()) {
				int count = PubDelegator.getIPersistenceRetrieve()
						.statFieldCount("om_job", null, null, pk_om_job,
								"pk_jobdoc", pk_om_job, "pk_om_job");
				if (count > 0) {
					// UPT60050704-000209=该基准岗位已经被引用，不可以删除！
					getFrameUI()
							.showWarningMessage(
									NCLangRes.getInstance().getStrByID(
											"60050704", "UPT60050704-000209")/* @res"该基准岗位已经被引用，不可以删除！" */);
					return;
				}// end if
			}// end if

			// 物理删除
			if (ORGDelegator.getIJob().hasChild(pk_om_job)) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704",
								"UPP60050704-000230")/*
														 * @res
														 * "该岗位下有下属岗位，不能删除！"
														 */);
				return;
			}// end if

			// 弹出确认对话框
			if (getFrameUI().showYesNoMessage(
					NCLangRes.getInstance().getStrByID("60050704",
							"UPP60050704-000231")/*
													 * @res "删除后数据将不可恢复，要继续吗？"
													 */) == nc.ui.pub.beans.UIDialog.ID_YES) {
				
				// 删除岗位时同步至中间表 add by river for 2011-09-14 片段1
				IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
				JobdocVO[] jobvos = (JobdocVO[])msg.getGeneralVOs(JobdocVO.class, " pk_om_job = '"+jobVO.getPk_om_job().toString().trim()+"'");
				
				
				// 真正从数据库中删除
				boolean isSuc = deleteMain(jobVO);

				// 如果删除成功了，则把单据号也响应的删除
				if (isSuc) {
					// 回滚单据编码
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
				
					
					// 删除岗位时同步至中间表 add by river for 2011-09-14 片段2
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
									
									System.out.println("<<<<<<  岗位档案删除线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				
				}// end if
			}// end if
			// 撤消 --V35 修改 移到撤消做的操作中

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
	 * 得到JobInfoUI
	 */
	public JobInfoUI getFrameUI() {
		return (JobInfoUI) super.getFrameUI();
	}

	/**
	 * 得到左Panel
	 */
	public JobInfoLeftPanel getLeftPanel() {
		return (JobInfoLeftPanel) super.getLeftPanel();
	}

	/**
	 * 得到主Panel
	 */
	public JobInfoMainPanel getMainPanel() {
		return (JobInfoMainPanel) super.getMainPanel();
	}

	/**
	 * 得到顶部Panel
	 */
	public JobInfoTopPanel getTopPanel() {
		return (JobInfoTopPanel) super.getTopPanel();
	}

}
