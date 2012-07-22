package nc.ui.om.om_102;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.hr.global.Global;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.om.om_101.DepthistoryVO;
import nc.vo.om.om_102.MergJobVO;
import nc.vo.om.om_102.MergPsnVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.yto.business.JobdocVO;
import nc.vo.yto.business.OperationMsg;
import nc.vo.yto.business.PsndocVO;

/**
 * 此处插入类型描述。
 * 创建日期：(2005-12-9 15:19:32)
 * @author：zhyan
 */
public class DeptMergPsnPan extends nc.ui.hr.base.HRMainPanel implements BillEditListener,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	 DeptMergMainPan mainpanel = null;
	private BillScrollPane ivjJScrollPane_PsnList = null;
/**
 * DeptMergPsnPan 构造子注解。
 */
public DeptMergPsnPan() {
	super();
	initialize();
}
/**
 * DeptMergPsnPan 构造子注解。
 * @param p0 java.awt.LayoutManager
 */
public DeptMergPsnPan(java.awt.LayoutManager p0) {
	super(p0);
}
/**
 * DeptMergPsnPan 构造子注解。
 * @param p0 java.awt.LayoutManager
 * @param p1 boolean
 */
public DeptMergPsnPan(java.awt.LayoutManager p0, boolean p1) {
	super(p0, p1);
}
/**
 * DeptMergPsnPan 构造子注解。
 * @param parentpnl nc.ui.hr.base.HRToftPanel
 */
public DeptMergPsnPan(nc.ui.hr.base.HRToftPanel parentpnl) {
	super(parentpnl);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2005-12-9 15:45:24)
 * @param mainpanel nc.ui.om.om_102.DeptMergMainPan
 */
public DeptMergPsnPan(DeptMergMainPan mainpanel) {
	super();
	this.mainpanel = mainpanel;
	initialize();
	}
/**
 * DeptMergPsnPan 构造子注解。
 * @param p0 boolean
 */
public DeptMergPsnPan(boolean p0) {
	super(p0);
}

/**
 * 
 * @return
 */
public DeptMergMainPan getMainPanel(){
    return mainpanel;
    
}
/**
 * 将界面上的数值组织到结构中。
 * 创建日期：(2003-3-27 20:21:10)
 * @return nc.vo.pub.ValueObject[]
 */
public nc.vo.pub.ValueObject[] displayToVO() throws Exception {
	return null;
}
/**
 * 返回 JScrollPane1 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
public BillScrollPane getUIScrollPane_PsnList() {
	if (ivjJScrollPane_PsnList == null) {
		try {
			ivjJScrollPane_PsnList = new BillScrollPane();
			ivjJScrollPane_PsnList.setName("JScrollPane_PsnList");
			ivjJScrollPane_PsnList.setBounds(20, 30, 630, 320);
			ivjJScrollPane_PsnList.setBBodyMenuShow(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane_PsnList;
}

/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// nc.bs.logging.Logger.error("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DeptMergPsnPan");
//		setToolTipText("部门合并-人员调配");
		setLayout(null);
		setSize(600, 500);
		add(getUIScrollPane_PsnList(), getUIScrollPane_PsnList().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	initTable();
	// user code end
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-6-10 10:53:24)
 */
public void initTable() {
	try {
	    //初始TableModel
		BillModel billmodel = getMainPanel().getController().initPsnTableCol();
		getUIScrollPane_PsnList().setTableModel(billmodel);
		getUIScrollPane_PsnList().getTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		getUIScrollPane_PsnList().addEditListener(this);
		getUIScrollPane_PsnList().getTableModel().setBodyDataVO(null);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		DeptMergPsnPan aDeptMergPsnPan;
		aDeptMergPsnPan = new DeptMergPsnPan();
		frame.setContentPane(aDeptMergPsnPan);
		frame.setSize(aDeptMergPsnPan.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.hr.base.HRMainPanel 的 main() 中发生异常");
		exception.printStackTrace(System.out);
	}
}
/**
 * 根据当前界面状态设定界面控件的编辑状态。
 * 创建日期：(2003-3-27 20:08:38)
 * @param stat int
 */
public void setPanelStat(int stat) {}
/**
 * 将数据显示到界面上去。
 * 用返回类型来标识方法是否执行成功
 * 创建日期：(2003-3-27 20:17:32)
 * @param vo nc.vo.pub.ValueObject
 */
public boolean voToDisplay(nc.vo.pub.ValueObject[] vos) throws Exception {
	return false;
}
/**
 * 返回需要调配的人员
 * @return
 */
 public MergPsnVO[] getMergPsns(){
     MergPsnVO[] mergpsns = (MergPsnVO[])getUIScrollPane_PsnList().getTableModel().getBodyValueChangeVOs("nc.vo.om.om_102.MergPsnVO");
     return mergpsns;
 }
 /**
  * 删除已经复制的岗位
  * 
  * @return
  */
    public boolean delCopyedJobs() throws Exception {
        boolean del = false;
        Hashtable ht_jobs = getMainPanel().getController().getDataModel()
                .getHash_MergedJobsToMergJobs();
        MergJobVO[] copyedjobs = getMainPanel().getController().getDataModel()
                .getTopsnMergJobs();

        if (copyedjobs == null || copyedjobs.length == 0) {
            del = true;
        } else {
            try {
                del = ORGDelegator.getIDeptMerg().delCopyedJobs(copyedjobs, ht_jobs);
            } catch (Exception e) {
                throw e;
            }
        }
        return del;
    }
    
    private List<nc.vo.yto.business.DeptdocVO> DeptdocUpdate = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	private List<nc.vo.yto.business.DeptdocVO> DeptdocUpdate2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	
	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	private List<PsndocVO> PsndocUpdate = new ArrayList<PsndocVO>();
	private List<PsndocVO> PsndocUpdate2 = new ArrayList<PsndocVO>();
    
    
    /**
     *依次执行人员调配，增加任职记录，同步工作履历，
     *根据岗位更新管理档案，根据任职类型，更新兼职记录
     *岗位撤销，撤销被合并部门下所有岗位
     *部门撤销操作，撤销部门－包括撤销所有被合并部门的岗位和虚拟组织
     * @return
     */
    public boolean doOk()throws Exception {
    	IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
		IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
	
        
       MergPsnVO[] mergpsns = getMergPsns(); // 合并部门后的人员信息
       String[] mergeddeptpks = getMainPanel().getController().getDataModel().getPk_Deptdoc_Merged(); // 被合并的部门PK
       DepthistoryVO depthistoryvo = getMainPanel().getController().getDataModel().getDepthistoryvo(); //　接收部门历史VO
       boolean save = false;
       boolean record = true;
       //del by zhyan liwh 要求去掉提示,默认记录历史 2006-11-29
//       int result = showYesNoCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404","UPP60050404-000136")/*@res "是否记录部门变更历史？"*/);
       
//       add by lum
       UFDate canceleddate = null;
       boolean canceled = false;
//       通过datamodel得到boolean的值
       canceled = getMainPanel().getController().getDataModel().isCanceled();
       
//       if (result == UIDialog.ID_YES) {
//           record = true;
//       }
//       else if(result == UIDialog.ID_NO){
//           record = false;           
//       }
//       else if (result == UIDialog.ID_CANCEL){
//           return false;
//       }
       try {
           save = ORGDelegator.getIDeptMerg().saveDeptmerg(mergpsns,mergeddeptpks,depthistoryvo,record,Global.getLogDate());
           
           
           // 部门合并操作时，同步人员、岗位、部门等信息至中间表 add by river for 2011-09-13
           
           if(save) {
        	   String pk_corp = Global.getCorpPK();
        	   String pk_psndocs = "";
//        	   String pk_om_jobs = "";
//        	   String pk_deptdocs = "";
        	   String pk_deptdocs2 = "";
        	   
        	   for(int i = 0 ; i < mergpsns.length ; i ++) {
        		   pk_psndocs += "'" + mergpsns[i].getAttributeValue("pk_psndoc").toString() + "',";
//        		   pk_om_jobs += "'" + mergpsns[i].getAttributeValue("oldpk_om_job").toString() + "',";
//        		   pk_deptdocs += "'" + mergpsns[i].getAttributeValue("oldpk_deptdoc").toString() + "',";
        	   }
        	   
        	   for(String str : mergeddeptpks) {
        		   pk_deptdocs2 += "'" + str + "',";
        	   }
        	   
//        	   pk_deptdocs += pk_deptdocs2;
        	   
        	   
        	   
        	   

//        	   if(pk_deptdocs.length() > 0) 
//        		   pk_deptdocs = pk_deptdocs.substring(0 , pk_deptdocs.length() - 1);
        	   
//        	   if(pk_om_jobs.length() > 0) 
//        		   pk_om_jobs = pk_om_jobs.substring(0 , pk_om_jobs.length() - 1);
        	   
        	   IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
        	   PsndocVO[] psnvo = new PsndocVO[0];
        	   if(pk_psndocs.length() > 0) {
        		   pk_psndocs = pk_psndocs.substring(0 , pk_psndocs.length() - 1);
        		   
        		   psnvo = (PsndocVO[])msg.getGeneralVOs(PsndocVO.class, " pk_psndoc in ("+pk_psndocs+")");
        	   }
        	   DeptdocVO[] deptvos = new DeptdocVO[0];
        	   JobdocVO[] jobvos2 = new JobdocVO[0];
        	   if(pk_deptdocs2.length() > 0) {
        		   pk_deptdocs2 = pk_deptdocs2.substring(0 , pk_deptdocs2.length() - 1);
        		   deptvos = (DeptdocVO[]) msg.getGeneralVOs(DeptdocVO.class, " pk_deptdoc in ("+pk_deptdocs2+")");
        		   
        		   //JobdocVO[] jobvos = (JobdocVO[]) msg.getGeneralVOs(JobdocVO.class, " pk_om_job in ("+pk_om_jobs+")");
        		   jobvos2 = (JobdocVO[]) msg.getGeneralVOs(JobdocVO.class, " pk_deptdoc in ( "+pk_deptdocs2+" ) and pk_corp = '"+pk_corp+"'");
        	   
        	   }
        	   
        	   OperationMsg opmsg = new OperationMsg();
				opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
				opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
				opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
				opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
        	   
        	   for(int i = 0 ; i < psnvo.length ; i ++) {
	        	   String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml4(psnvo[i], "RequestPsndoc", "psn", "update" , opmsg));
        	   
	        	   String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						PsndocUpdate.add(psnvo[i]);
					}
        	   }
        	   
        	   if(PsndocUpdate.size() > 0 ) {
					Thread th1 = new Thread() {
						public void run() {
							IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
							IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							OperationMsg opmsg = new OperationMsg();
							opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
							opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
							opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
							opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
							try {
								if(true) {
									this.sleep(SleepTime.Time);
								
									for(PsndocVO psn : PsndocUpdate) {
										String retStr = filepost.postFile(Uriread.uriPath() , 
												gener.generateXml4(psn, "RequestPsndoc", "psn", "add" , opmsg));
										
										String[] strs = retStr.split("<success>");
										String retMsg = "";
										if (strs.length > 1)
											retMsg = strs[1].substring(0, strs[1].indexOf("<"));
									
										if(retMsg.equals("false") || strs.length <= 1)
											PsndocUpdate2.add(psn);
									}
									
									PsndocUpdate = PsndocUpdate2;
									
									PsndocUpdate2 = new ArrayList<PsndocVO>();
									
//									if(PsndocUpdate.size() == 0)
//										break;
									
									
								}
								System.out.println("<<<<<<  人员档案修改线程停止！ >>>>>>");
								System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
								this.stop();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
					
					th1.start();
				}
        	   
//        	   for(int i = 0 ; i < jobvos.length ; i ++) {
//        		   String retStr = nc.bs.yto.client.FilePost.postFile(Uriread.uriPath() , 
//							Gener.generateXml5(jobvos[i], "RequestJob", "job", "update"));
//        	   }
        	   
        	   for(int i = 0 ; i < jobvos2.length ; i ++) {
        		   String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml5(jobvos2[i], "RequestJob", "job", "update"));
        	   
        		   String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						JobdocUpdate.add(jobvos2[i]);
					}
        	   }
        	   
        	   if(JobdocUpdate.size() > 0 ) {
					Thread th2 = new Thread() {
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
									
//									if(JobdocUpdate.size() == 0)
//										break;
									
									
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
        	   
        	   for(int i = 0 ; i < deptvos.length ; i ++) {
        		   
//        		   IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
					nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[])msg.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class, " pk_deptdoc = '"+deptvos[i].getAttributeValue("pk_deptdoc")+"'"))[0];
					
        		   
        		   String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml3(deptdocvo, "RequestDeptdoc", "dept", "update"));
        	   
        		   String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
						DeptdocUpdate.add(deptdocvo);
					}
        	   }
        	   
        	   if(DeptdocUpdate.size() > 0 ) {
					Thread th3 = new Thread() {
						public void run() {
							IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
							IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
						
							try {
								if(true) {
									this.sleep(SleepTime.Time);
								
									for(nc.vo.yto.business.DeptdocVO Dept : DeptdocUpdate) {
										String retStr = filepost.postFile(Uriread.uriPath() , 
												gener.generateXml3(Dept, "RequestDeptdoc", "dept", "add"));
										
										String[] strs = retStr.split("<success>");
										String retMsg = "";
										if (strs.length > 1)
											retMsg = strs[1].substring(0, strs[1].indexOf("<"));
									
										if(retMsg.equals("false") || strs.length <= 1)
											DeptdocUpdate2.add(Dept);
									}
									
									DeptdocUpdate = DeptdocUpdate2;
									
									DeptdocUpdate2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
									
//									if(DeptdocUpdate.size() == 0)
//										break;
									
									
								}
								System.out.println("<<<<<<  部门档案修改线程停止！ >>>>>>");
								System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
								this.stop();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
					
					th3.start();
				}
           }
           
//         add by lum
           if (canceled) {
               DeptdocVO[] deptdocvos = getMainPanel().getdeptpan().getMergedDeptVOs();
               canceleddate = deptdocvos[0].getCancelDate();
               ORGDelegator.getIDeptMerg().cancelOper(deptdocvos, canceleddate);                 	   
           }          
       } catch (Exception e) {
           return false;
       }
       return save;
    }
    /**
     * 显示错误信息
     * @param err java.lang.String
     */
    public int showOkCancelMessage(String msg) {
    	return MessageDialog.showOkCancelDlg(this, null, msg);
    }
    /**
     * 显示错误信息
     * @param err java.lang.String
     */
    public int showYesNoCancelMessage(String msg) {
    	return MessageDialog.showYesNoCancelDlg(this, null, msg);
    }
    
    
	public void actionPerformed(ActionEvent arg0) {
	   
	    
	}
	/**
	 * 点击参照选择岗位后处理事件
	 */
    public void afterEdit(BillEditEvent e) {
        if(e.getKey().equals("newjobcode")){
            nc.ui.pub.beans.UIRefPane jobref = (nc.ui.pub.beans.UIRefPane)getUIScrollPane_PsnList().getTableModel().getItemByKey("newjobcode").getComponent();
            String pk_om_job = jobref.getRefPK();
            String jobcode = jobref.getRefCode();
            String jobname = jobref.getRefName();
            int i = e.getRow();
            getUIScrollPane_PsnList().getTableModel().setValueAt(pk_om_job,i,"newpk_om_job");
            getUIScrollPane_PsnList().getTableModel().setValueAt(jobcode,i,"newjobcode");
            getUIScrollPane_PsnList().getTableModel().setValueAt(jobname,i,"newjobname");
        }

        
    }

    public void bodyRowChange(BillEditEvent e) {

        
    }
}
