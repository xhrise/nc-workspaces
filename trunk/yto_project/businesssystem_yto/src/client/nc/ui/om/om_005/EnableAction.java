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
 * 岗位信息按钮事件，取消撤销
 * @author wangxing
 *
 */
public class EnableAction extends AbstractAction {

	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	/**
	 * 构造函数
	 * @param frameUI1
	 */
	public EnableAction(FrameUI frameUI1) {
		super(frameUI1);
		
	}

	/**
	 * 得到反撤销对话框
	 * @return
	 * @throws Exception
	 */
	private CancleDelDlg getEnableDialog(JobVO jobVO) throws Exception {
		return new CancleDelDlg(getFrameUI(), jobVO,
				NCLangRes.getInstance().getStrByID("60050704",
						"UPP60050704-000070")/*
											  * @res "反撤销"
											  */);
	}
	
	/**
	 * 执行命令体
	 */
	public void execute() throws Exception {
		boolean isSuccess = false;
		JobVO jobVO = getFrameUI().getCurrentSelJobVO();
		if(jobVO==null){
			getFrameUI().showWarningMessage(
					NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000233")/* @res "请先选中一个岗位！" */);
			return;
		}else if(!getFrameUI().getLogin_Pk_corp().equals(jobVO.getPk_corp())){
			getFrameUI().showWarningMessage(NCLangRes.getInstance().getStrByID(
					"60050704", "UPT60050704-000204")/* @res "只能修改本公司岗位！" */);
			return;
		}//end if
		
		try {
			//校验上级是否撤销，如果是上级岗位是撤销，则不让反撤销
			String suporior = jobVO.getSuporior();
			if (StringUtils.hasText(suporior)) {
				JobVO jobvoFather = ORGDelegator.getIJob().findJobByPK(suporior);
				if (jobvoFather != null
						 && jobvoFather.getIsAbort()!=null && jobvoFather.getIsAbort().booleanValue()) {
//					UPT60050704-000210=该岗位的上级岗位[{0}]已经撤销，该岗位不能反撤销！
					String msg = NCLangRes.getInstance().getStrByID(
							"60050704", "UPT60050704-000210", null,new String[]{jobvoFather.getJobname()})/* @res "只能修改本公司岗位！" */;
					getFrameUI().showWarningMessage(msg);
					return;
				}//end if
			}//end if

			DeptdocVO belongDeptVO = ORGDelegator.getIJob().getDeptdocVOByJob(jobVO.getPk_om_job());//wangkf add
			
			if (belongDeptVO != null
					&& belongDeptVO.getHrcanceled().booleanValue()) {
//				UPT60050704-000211=该岗位所属部门[{0}]已经撤销，该岗位不能反撤销！
				String msg = NCLangRes.getInstance().getStrByID(
						"60050704", "UPT60050704-000211", null,new String[]{belongDeptVO.getDeptname()})/* @res "只能修改本公司岗位！" */; 
				getFrameUI().showWarningMessage(msg);
			} else {
				
				// 反撤销
				if (!getFrameUI().lockRecord(jobVO.getPk_om_job())) {
					getFrameUI().showWarningMessage(NCLangRes.getInstance().getStrByID(
							"6005", "UPP6005-000022")/* @res"岗位正在被其它用户操作，请稍后再试!" */);
					return;
				}//end if
				
				//-------------------
				int useroperate = getEnableDialog(jobVO).showModal();
				if(useroperate != UIDialog.ID_YES
						&& useroperate != UIDialog.ID_OK){
					return;
				}//end if
				
				// 如果反撤销成功
				if (ORGDelegator.getIJob().abortVO(jobVO.getPk_om_job(), "N", getFrameUI().getEffectDate()) > 0) {
					jobVO.setIsAbort(new UFBoolean(false));
					isSuccess = true;
					getFrameUI().freeLockRecord(jobVO.getPk_om_job());//wangkf add
					getFrameUI().showHintMessage(
							NCLangRes.getInstance()
									.getStrByID("60050704",
											"UPP60050704-000273")/*
																  * @res
																  * "反撤销成功!"
																  */);
					
					// 如果反撤销成功，则
					// 重新显示当前行的数据
					getMainPanel().getListPanel().mainListPanelHeadRowChanged(getFrameUI().getCurrentJobCursor());
				
					// 撤销岗位时同步至中间表 add by river for 2011-09-14
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
									System.out.println("<<<<<<  岗位档案修改线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					
				} else {// 如果失败
					isSuccess = false;
					getFrameUI().freeLockRecord(jobVO.getPk_om_job());//wangkf add
					getFrameUI().showWarningMessage(
							NCLangRes.getInstance()
									.getStrByID("60050704",
											"UPP60050704-000274")/*
																  * @res
																  * "反撤销失败，请刷新后再试！"
																  */);
				}//end if
				

			}
		} catch (Exception e) {
			e.printStackTrace();
			
			getFrameUI().showErrorMessage(
					NCLangRes.getInstance().getStrByID(
							"60050704", "UPP60050704-000275")/*
															  * @res "反撤销失败!"
															  */);
		}finally{
			getFrameUI().freeLockRecord(jobVO.getPk_om_job());
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
