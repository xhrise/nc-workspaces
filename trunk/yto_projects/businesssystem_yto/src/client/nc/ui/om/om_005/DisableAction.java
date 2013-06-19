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
import nc.ui.om.om_004.DeleteDialog;
import nc.ui.pub.beans.UIDialog;
import nc.vo.om.om_005.JobVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.yto.business.JobdocVO;

/**
 * 岗位信息按钮事件，撤销
 * @author wangxing
 *
 */
public class DisableAction extends AbstractAction {

	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	/**
	 * 构造函数
	 * @param frameUI1
	 */
	public DisableAction(FrameUI frameUI1) {
		super(frameUI1);
		
	}
	
	/**
	 * 
	 * @param jobVO
	 * @return
	 * @throws Exception
	 */
	private DeleteDialog getDisableJobDialog(JobVO jobVO) throws Exception {
		return new DeleteDialog(this.getFrameUI(), "", jobVO);
	}
	
	/**
	 * 此处插入方法描述。 创建日期：(2003-6-2 14:47:04)
	 */
	public boolean disableJob(JobVO jobVO) {
		boolean flag = false;
		try {
			String pk = jobVO.getPk_om_job();
			String buildDate = jobVO.getBuilddate().toString();
			
			int useroperate = getDisableJobDialog(jobVO).showModal();
			if(useroperate != UIDialog.ID_YES
					&& useroperate != UIDialog.ID_OK){
				return false;
			}//end if
			
			String abortDate = getFrameUI().getRetractDate();
			if (abortDate.compareTo(buildDate) < 0) {
				getFrameUI().showWarningMessage(
						NCLangRes.getInstance()
								.getStrByID("60050704",
										"UPP60050704-000278")/*
															  * @res
															  * "撤销日期不能早于建立日期，请重新设置！"
															  */);
				return false;
			}//end if
			
			if (ORGDelegator.getIJob().abortVO(pk, "Y", abortDate) > 0) {
				jobVO.setIsAbort(new UFBoolean(true));
				jobVO.setAbortdate(new UFDate(abortDate));
				flag = true;
				
				// 如果不包含撤销单位，则从列表中删除
				if(!getTopPanel().getCheckbox_isshowcanceled().isSelected()){
					getFrameUI().deleteFromCurrentJobs(pk);
//					getFrameUI().deleteFromQueryResult(pk);
//					getFrameUI().removeJobShowCache(jobVO);
				}//end if
				
				//----
				getFrameUI().showHintMessage(
						NCLangRes.getInstance()
								.getStrByID("60050704",
										"UPP60050704-000215")/* @res "撤销成功!" */);
			} else {
				getFrameUI().showWarningMessage(
						NCLangRes.getInstance()
								.getStrByID("60050704",
										"UPP60050704-000216")/*
															  * @res
															  * "撤销失败，可能是该岗位已不存在，请刷新后再试！"
															  */);
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			if (e.getMessage() != null
					&& e.getMessage().indexOf(
							NCLangRes.getInstance().getStrByID(
									"60050704", "UPP60050704-000217")/*
																	  * @res
																	  * "不能被撤消"
																	  */) >= 0)
				
				getFrameUI().showErrorMessage(e.getMessage());
			else
				getFrameUI().showErrorMessage(
						NCLangRes.getInstance().getStrByID(
								"60050704", "UPP60050704-000218")/*
																  * @res
																  * "撤销失败，请刷新后再试!"
																  */);
		}
		return flag;
	}

	/**
	 * 执行命令体
	 */
	public void execute() throws Exception {
		boolean isSuccess = false;
		boolean isLock = false;
		JobInfoUI ui = getFrameUI();
		JobVO jobVO = ui.getCurrentSelJobVO();
		if(jobVO==null){
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000233")/* @res "请先选中一个岗位！" */);
			return;
		}else if(!ui.getLogin_Pk_corp().equals(jobVO.getPk_corp())){
			ui.showWarningMessage(NCLangRes.getInstance().getStrByID(
					"60050704", "UPT60050704-000204")/* @res "只能修改本公司岗位！" */);
			return;
		}//end if
		
		try {
			//锁记录
			if (!getFrameUI().lockRecord(jobVO.getPk_om_job())) {
				getFrameUI().showWarningMessage(NCLangRes.getInstance().getStrByID(
						"6005", "UPP6005-000022")/* @res"岗位正在被其它用户操作，请稍后再试!" */);
				return;
			} else{
				isLock = true;
			}//end if
			
			// 执行撤销
			isSuccess = disableJob(jobVO);
			
			if(isSuccess) {
	//			 撤销岗位时同步至中间表 add by river for 2011-09-14
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
									
	//								if(JobdocUpdate.size() == 0)
	//									break;
								}
								
								System.out.println("<<<<<<  岗位档案修改线程停止！ >>>>>>");
								System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
								this.stop();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			
			}
			
			// 撤销之后的操作
			int size = getFrameUI().getCurrentJobListSize();
			if(size<=0){
				// 设置按钮状态
				getDataModel().setCurrentState(JobInfoStateReg.USERSTS_VIEW_INIT);
			}else{
				boolean isabort = jobVO.getIsAbort()!=null && jobVO.getIsAbort().booleanValue();
				
				// 重新显示当前行的数据
				if(ui.isShowCanceled() || !isabort){
					getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
				}else{
					int cursor = getFrameUI().getCurrentJobCursor();
					getMainPanel().getListPanel().leftTreeSelectionChanged(getLeftPanel().getSelectedNode());
					int count = getFrameUI().getJobList().length;
					if(count<=0){
						cursor = -1;
					}else if(count==1){
						cursor = 0;
					}else if(count==cursor){
						cursor--;
					}else if(count<cursor){
						cursor = count - 1;
					}//end if
					
					ui.setCurrnetJobCursor(cursor);
					ui.selectJobCursor();
//					getMainPanel().getListPanel().mainListPanelHeadRowChanged(cursor);
				}
				
			}//end if
		} finally {
			if (isLock){
				getFrameUI().freeLockRecord(jobVO.getPk_om_job());
			}//end if
		}//end try
	}

	/**
	 * 得到JobInfoUI
	 */
	public JobInfoUI getFrameUI() {
		return (JobInfoUI)super.getFrameUI();
	}

	/**
	 * 得到左Panel
	 */
	public JobInfoLeftPanel getLeftPanel() {
		return (JobInfoLeftPanel)super.getLeftPanel();
	}

	/**
	 * 得到主Panel
	 */
	public JobInfoMainPanel getMainPanel() {
		return (JobInfoMainPanel)super.getMainPanel();
	}

	/**
	 * 得到顶部Panel
	 */
	public JobInfoTopPanel getTopPanel() {
		return (JobInfoTopPanel)super.getTopPanel();
	}
}
