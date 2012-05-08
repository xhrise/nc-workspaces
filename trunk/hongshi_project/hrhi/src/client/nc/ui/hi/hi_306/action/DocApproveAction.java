package nc.ui.hi.hi_306.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.bs.uap.lock.PKLock;
import nc.impl.mbSyn.MbSynImpl;
import nc.impl.mbSyn.ServiceUtilImpl;
import nc.itf.hi.HIDelegator;
import nc.itf.hr.pub.PubDelegator;
import nc.itf.mbSyn.IMbSys;
import nc.itf.mbSyn.IServiceUtil;
import nc.ui.hi.hi_306.DocApproveMainPanel;
import nc.ui.hi.hi_306.DocApproveUI;
import nc.ui.hi.hi_306.DocBillState;
import nc.ui.hi.hi_306.DocDataModel;
import nc.ui.hr.comp.pf.DirectApproveDialog;
import nc.ui.hr.comp.pf.action.PFApproveAction;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.global.Global;
import nc.ui.pub.beans.UIDialog;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_301.validator.FieldValidator;
import nc.vo.hi.hi_306.DocApplyHVO;
import nc.vo.hi.hi_306.DocPsnCodeVO;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;


public class DocApproveAction extends PFApproveAction {

	private GeneralVO[] intoDocData = null;

	private GeneralVO[] intoPsnList = null;

	private nc.vo.pub.billcodemanage.BillCodeObjValueVO objBillCodeVO = null;

	private final static String PSNCODE_AUTO_GENERATE = "1";

	private String autopsncode = null;// 当人员编码是自动生成时。

	private String isUniquePsncodeInGroup = null;// 是否设置 集团内唯一的参数

	// 人员采集自动编码, 自动生成还是手工输入
	private String psncodeAutoGenerate = null;

	public DocApproveAction(FrameUI frameUI) {
		super(frameUI);
	}

	/**
	 *
	 */
	public boolean validate() throws ValidationException {

		super.validate();

		if(isOver()){
			return false;
		}

		intoPsnList = getDocBusinessMainPanel().getPsnListData(1); // 审批通过的人员

		if ((getPsncodeAutoGenerateParam() != null)
				&& getPsncodeAutoGenerateParam().trim().equals("0")) {
			checkPsnCode(intoPsnList);
		}

		intoDocData = getDocDataModel().getIntoDocData(intoPsnList);

		checkPsnListDept(intoDocData);

		return true;
	}

	/**
	 * 直批操作
	 */
	public boolean doDirectApprove(HRAggVO aggVO, String censorText,int blApproved)throws BusinessException {

		
		super.doDirectApprove(aggVO, censorText, blApproved);
		
		String billpk = headVO.getPrimaryKey();
		DocApplyHVO newHeaderVO = getDocDataModel().getHeadVOFromDB(billpk);
		
		try {
			if (blApproved == DirectApproveDialog.PF_APPROVE_APPROVED) {// 批准	
				
//				del by zhyan 2007-08-16 由于超编但是不严格控制的人员可以入职,后台无法进行提示,所以还是放到前台的审批按钮上进行编制校验
//				if (isExceedWorkout(intoDocData)) {
//					return false;
//				}
				newHeaderVO.setBillstate(blApproved);
				newHeaderVO.setApprovenote(censorText);
			} else if (blApproved == DirectApproveDialog.PF_APPROVE_REJECTED) {// 不批准
				newHeaderVO.setBillstate(blApproved);
				newHeaderVO.setApprovenote(censorText);
			} else if (blApproved == DirectApproveDialog.PF_APPROVE_RETURN) {// 驳回
				newHeaderVO.setBillstate(blApproved);
				newHeaderVO.setApprovenote(censorText);
			}			
			newHeaderVO.setApprovedate(Global.getLogDate());
			newHeaderVO.setStatus(VOStatus.UPDATED);
			aggVO.setParentVO(newHeaderVO);
			getDataModel().onSave(aggVO);

		} catch (Exception e) {
			if (e instanceof BusinessException){
				throw (BusinessException) e;
			}
			else{ 
				getFrameUI().handleException(e);
				e.printStackTrace();
			}
			return false;
		}

		return true;
	}

	/**
	 * 重写父类执行方法
	 */
	public void execute() throws Exception {
		getFrameUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000001")/*@res "审批..."*/);
		if(isInDoc(intoDocData)){
			return;
		}
		//如果超编,不能转入或者用户选择不转入,则取消
		if (getDocBusinessMainPanel().isExceedWorkout(intoDocData,true)) {
			return;
		} else {
			super.execute();
			afterExecute();
			getFrameUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000002")/*@res "已完成审批操作！"*/);
		}

	}

	/**
	 *
	 * @return
	 */
	protected boolean isInDoc(GeneralVO[] psnListTemp)throws ValidationException {
		boolean into = false;
		if (psnListTemp != null) {
			try {
				String sqls[] = new String[psnListTemp.length*2];
				for (int i = 0; i < psnListTemp.length; i++) {
					//效率优化start 减少连接数
					String pk_psndoc = (String) psnListTemp[i].getAttributeValue("pk_psndoc");
					String pk_psnbasdoc = (String) psnListTemp[i].getAttributeValue("pk_psnbasdoc");

					String sql1 = " select 1 from bd_psndoc where pk_psndoc ='"+pk_psndoc+"'and indocflag ='Y'";
					String sql2 = " select 1 from bd_psndoc where psnclscope = 0 and indocflag = 'Y' and pk_psnbasdoc ='"+pk_psnbasdoc+"' and pk_corp <>'"+Global.getCorpPK()+"'";
					sqls[i*2] = sql1;
					sqls[i*2+1] = sql2;
//					into = HIDelegator.getPsnInf().isRecordExist(sql1);
//					if(into){
//						getFrameUI().showErrorMessage(psnname+nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000003")/*@res "已经转入人员档案，不能再次入职!"*/);
//						return into;
//					}
//					String sql2 = " select 1 from bd_psndoc where psnclscope = 0 and indocflag = 'Y' and pk_psnbasdoc ='"+pk_psnbasdoc+"' and pk_corp <>'"+Global.getCorpPK()+"'";
//					boolean existrehire = HIDelegator.getPsnInf().isRecordExist(sql2);
//					if(existrehire){
//						getFrameUI().showErrorMessage(psnname+" 人员已经在其他公司入职，不能在本公司入职！");
//						return existrehire;
//					}	
				}
				boolean[] isexists = HIDelegator.getPsnInf().isRecordExists(sqls);
				for (int i = 0; i < psnListTemp.length; i++) {
					String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
					into =  isexists[2*i];
					boolean existrehire = isexists[2*i+1];
					if(into){
						getFrameUI().showErrorMessage(psnname+nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000003")/*@res "已经转入人员档案，不能再次入职!"*/);
						return into;
					}
					if(existrehire){
//						getFrameUI().showErrorMessage(psnname+" 人员已经在其他公司入职，不能在本公司入职！");
						getFrameUI().showErrorMessage(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
								"UPP600700-000155")/*@res " 人员已经在其他公司入职，不能在本公司入职！"*/);
						return existrehire;
					}	
				}
				//效率优化end
			} catch (Exception e) {
				throw new ValidationException(e.getMessage());
			}
		}

		return into;
	}

	/**
	 *
	 * @return
	 */
	protected boolean isOver() throws ValidationException {
		boolean over = false;
		String billpk = headVO.getPrimaryKey();
		try {
			String sql = " select 1 from hi_docapply_h  where pk_docapply_h ='"
					+ billpk + "' and billstate in (0,1)";
			over = HIDelegator.getPsnInf().isRecordExist(sql);
			if (over) {
				getFrameUI().showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000004")/*@res "申请单已经审批结束，请刷新后再试!"*/);
				return over;
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
		return over;
	}

	/**
	 * 审批通过后转入人员档案等操作
	 *
	 * @throws Exception
	 */
	public void afterExecute() throws Exception {
		String billpk = headVO.getPrimaryKey();
		String note = (String)headVO.getAttributeValue(getDocDataModel().getPFConfig().getApproveNoteFieldCode());
		DocApplyHVO newHeaderVO = getDocDataModel().getHeadVOFromDB(billpk);
		
		if (newHeaderVO != null) {
			// 批准状态下数据的修改
			if (newHeaderVO.getBillstate().intValue() == DocBillState.BILLSTATE_PASSED) {
				//version5.6 dusx修改于2009/5/4（由于原代码审批和插入人员在不同的事务上，可能会导致数据不一致，现将转入人员档案放入审批校验类DocFlowService里,和审批使用同一事务进行处理）
//				// 转入之前先设置人员编码
//				DocPsnCodeVO[] psncodeVOS = null;
//				if (intoPsnList != null) {
//					psncodeVOS = getPsnCodeVOS(intoPsnList);
//				}
//				setPsnCode(psncodeVOS);
//				// 转入人员档案,同时把 任职子集 的数据同步到 工作履历表。和50系统结合
//				intoDoc(intoDocData);
//				// 处理不通过人员
//				UpdatePsnstate(2);
				
				if (intoDocData == null || intoDocData.length < 1) {
					// getFrameUI().showWarningMessage("该单据没有人员入职,不需要发送通知!");
					return;
				} else {
					getUserSelectDlg().setPk_corp(Global.getCorpPK());
					getUserSelectDlg().refreshContent();
					getUserSelectDlg().setLocationRelativeTo(getFrameUI().getMainPanel());
					getUserSelectDlg().showModal();
					String recievers = ((DocDataModel) getDataModel()).getUserpks();
					
					boolean check = false;
					try {
						IServiceUtil util = new ServiceUtilImpl();
						Long accid = util.getAccountId("");
						check = true;
					} catch(Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage() , "提示" , JOptionPane.OK_OPTION);
						
					}
					
					if (userSelectDlg.getResult() == UIDialog.ID_OK) {
						HIDelegator.getPsnInf().insertRecievers(Global.getCorpPK(),recievers, 2);// 保存消息接收人
					
						if (recievers.length() > 0) {
	
							String subject = nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000005")/*@res "入职通知"*/;
							StringBuffer msgcontent = new StringBuffer();
							StringBuffer mailcontent = new StringBuffer();
							StringBuffer psninfo = new StringBuffer();
							StringBuffer msgpsninfo = new StringBuffer();
							
							for(int i=0; i<intoDocData.length;i++){
								psninfo.append("\n");
								if(intoDocData[i].getAttributeValue("psncode")!=null && !"".equals(intoDocData[i].getAttributeValue("psncode"))){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000006")/*@res "人员编码为:"*/+intoDocData[i].getAttributeValue("psncode")+",");
								}
								//在审批时已经保证了都有psncode
//								else{
//									String psncode = "";
//									for(int j =0 ;j<psncodeVOS.length ;j++){
//										if(psncodeVOS[j].getPk_psndoc().equalsIgnoreCase((String)intoDocData[i].getAttributeValue("pk_psndoc"))){
//											psncode = psncodeVOS[j].getPsncode();
//										}
//									}
//									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000006")/*@res "人员编码为:"*/+psncode+",");
//
//								}
								if(intoDocData[i].getAttributeValue("psnname")!=null){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000007")/*@res "人员姓名为:"*/+intoDocData[i].getAttributeValue("psnname")+",");
									msgpsninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000007")/*@res "人员姓名为:"*/+intoDocData[i].getAttributeValue("psnname")+",");
								}
								if(intoDocData[i].getAttributeValue("indutydate")!=null){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000008")/*@res "到职日期为:"*/+intoDocData[i].getAttributeValue("indutydate")+",");
									msgpsninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000008")/*@res "到职日期为:"*/+intoDocData[i].getAttributeValue("indutydate")+",");
								}
								if(intoDocData[i].getAttributeValue("unitname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000009")/*@res "公司为:"*/+intoDocData[i].getAttributeValue("unitname")+",");
								if(intoDocData[i].getAttributeValue("deptname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000010")/*@res "部门为:"*/+intoDocData[i].getAttributeValue("deptname")+",");
								if(intoDocData[i].getAttributeValue("jobname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000011")/*@res "岗位为:"*/+intoDocData[i].getAttributeValue("jobname"));
								psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000012")/*@res "的人员入职!"*/);
								
								// ---------- 添加同步OA方法 add by river ------------ //
								
								String datasource = "nc56true";
								
								if(check) {
									IMbSys mbSyn = new MbSynImpl();
									String msg = mbSyn.create(datasource,intoDocData[i].getAttributeValue("pk_psndoc").toString() , "Y");
									System.out.println(msg);
								
								}
								
								
							}
							msgcontent.append(subject+"\n");
							msgcontent.append(msgpsninfo);
							((DocApproveUI) getFrameUI()).sendMailMsg(subject,msgcontent.toString(), ((DocDataModel) getDataModel()).getUserRecievers());

							mailcontent.append(subject+"\n");
							mailcontent.append(psninfo);
							GeneralVO[] recieverEmails = HIDelegator.getPsnInf().getRecieverEmails(recievers);
							Vector v = new Vector();
							String nomailname = "";
							int msgnum = 0;
							if (recieverEmails != null && recieverEmails.length > 0) {
								msgnum = recieverEmails.length;
								for (int i = 0; i < recieverEmails.length; i++) {
									Object email = recieverEmails[i].getAttributeValue("email");
									if (email == null) {
										String name = (String) recieverEmails[i].getAttributeValue("username");
										nomailname += name + ",";
									} else {
										v.add((String) email);
									}
								}
							}
							String[] emailAddress = new String[v.size()];
							if (v.size() > 0) {
								v.copyInto(emailAddress);
							}
							int mailnum = 0;

							if (emailAddress != null && emailAddress.length > 0) {
								HIDelegator.getPsnInf().sendMail(subject, mailcontent.toString(),emailAddress);
								mailnum = emailAddress.length;
							}
							String msg = nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000013")/*@res "发送通知成功!共发送"*/ + msgnum + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000014")/*@res "条消息"*/ + mailnum+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000015")/*@res "个邮件"*/;
							if (nomailname.trim().length() > 1) {
								nomailname = nomailname.substring(0, nomailname.length() - 1);
								msg += nomailname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000016")/*@res "没有Email地址"*/;
							}
							getFrameUI().showHintMessage(msg);
						}
					} else {
						userSelectDlg.closeCancel();
						
//						---------- 添加同步OA方法 add by river ------------ //
						
						String datasource = "nc56true";
						
						if( check ) {
							for(int i=0; i<intoDocData.length;i++){
								IMbSys mbSyn = new MbSynImpl();
								String msg = mbSyn.create(datasource,intoDocData[i].getAttributeValue("pk_psndoc").toString() , "Y");
								System.out.println(msg);
							}
						}
					}
				}

				newHeaderVO.setApprovedate(Global.getLogDate());
				newHeaderVO.setStatus(VOStatus.UPDATED);
				newHeaderVO.setApprovenote(note);
				HRAggVO aggVO = new HRAggVO();
				aggVO.setParentVO(newHeaderVO);
				getDataModel().onSave(aggVO);
				
				// -------------------------- //
			}
			// 修改表头数据
			if (newHeaderVO.getBillstate().intValue() == DocBillState.BILLSTATE_NO_PASS) {
				UpdatePsnstate(0);//单据不通过,则所有人员都未未批准
				newHeaderVO.setApprovedate(Global.getLogDate());
				newHeaderVO.setStatus(VOStatus.UPDATED);
				newHeaderVO.setApprovenote(note);
				HRAggVO aggVO = new HRAggVO();
				aggVO.setParentVO(newHeaderVO);
				getDataModel().onSave(aggVO);
			}
			displayData();

		}
	}

	private MailMessageRecieverSelectDlg userSelectDlg = null;
	/**
	 *
	 * @return
	 */
	public MailMessageRecieverSelectDlg getUserSelectDlg() {
		if (userSelectDlg == null) {
			userSelectDlg = new MailMessageRecieverSelectDlg(getFrameUI(),Global.getCorpPK(),"600706");
		}
		return userSelectDlg;
	}
	// 得到MODEL
	public DocDataModel getDocDataModel() {
		return (DocDataModel) getDataModel();
	}

	// 得到MAINPANEL
	public DocApproveMainPanel getDocBusinessMainPanel() {
		return (DocApproveMainPanel) getMainPanel();
	}

	/**
	 * 转入人员档案的人必须已经有部门，检查。
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected void checkPsnListDept(GeneralVO[] psnListTemp)
			throws ValidationException {
		if (psnListTemp != null) {
			for (int i = 0; i < psnListTemp.length; i++) {
				String pk_deptdoc = (String) psnListTemp[i]
						.getAttributeValue("pk_deptdoc");
				String psnname = (String) psnListTemp[i]
						.getAttributeValue("psnname");
				try {
					FieldValidator
							.validate(
									psnname
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("600704",
															"UPP600704-000067")/*
																				 * @res
																				 * "的部门"
																				 */,
									"notnull", pk_deptdoc);
				} catch (Exception e) {
					throw new ValidationException(e.getMessage());
				}

			}
		}
	}

	/**
	 * 转入人员档案的人必须已经有人员编码，检查。
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected void checkPsnCode(GeneralVO[] psnListTemp)
			throws ValidationException {
		if (psnListTemp != null) {
			for (int i = 0; i < psnListTemp.length; i++) {
				String psncode = (String) psnListTemp[i].getAttributeValue("psncode");
				String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
				try {
					FieldValidator.validate(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000017")/*@res "的人员编码"*/, "notnull",psncode);
				} catch (Exception e) {
					throw new ValidationException(e.getMessage());
				}
			}
		}
	}

	

	/**
	 * 得到需要编制判断的数据
	 *
	 * @param intoDocData
	 * @return
	 */
	private GeneralVO[] getWorkoutData(GeneralVO[] intoDocData) {
		GeneralVO[] vos = null;
		Vector v = new Vector();
		if (intoDocData != null) {
			for (int i = 0; i < intoDocData.length; i++) {
				Integer intPsnclscope = (Integer) intoDocData[i]
						.getAttributeValue("psnclscope");// 只判断人员归属为在职
				if (intPsnclscope != null && intPsnclscope.intValue() == 0) {
					v.addElement(intoDocData[i]);
				}
			}
		}
		if (v.size() > 0) {
			vos = new GeneralVO[v.size()];
			v.copyInto(vos);
		}
		return vos;
	}

	/**
	 * 对在职人员按照岗位，部门，公司分组
	 *
	 * @param srcData
	 * @param groupPk
	 * @return
	 */
	private HashMap groupByPk(GeneralVO[] srcData, String groupPk) {
		HashMap hm = new HashMap();
		if (srcData != null) {
			Vector vPool = new Vector();
			for (int i = 0; i < srcData.length; i++) {
				vPool.addElement(srcData[i]);
			}
			Vector vSameGroup = new Vector();
			while (vPool.size() > 0) {
				GeneralVO firstVO = (GeneralVO) vPool.elementAt(0);
				String firstKey = (String) firstVO.getAttributeValue(groupPk);
				vSameGroup.addElement(firstVO);
				for (int i = 1; i < vPool.size(); i++) {
					GeneralVO nextVO = (GeneralVO) vPool.elementAt(i);
					String nextKey = (String) nextVO.getAttributeValue(groupPk);
					if (firstKey != null && nextKey != null
							&& firstKey.equalsIgnoreCase(nextKey)) {
						vSameGroup.addElement(nextVO);
						vPool.remove(nextVO);
						i--;
					}
				}
				vPool.remove(firstVO);
				if (vSameGroup.size() > 0) {
					GeneralVO[] vos = new GeneralVO[vSameGroup.size()];
					vSameGroup.copyInto(vos);
					hm.put(firstKey, vos);
					vSameGroup.removeAllElements();
				}
			}
		}
		return hm;
	}

	public Object getListBodyValueAt(int row, String key) {

		Object ss = null;

		return ss;
	}

	

	/**
	 *
	 * @param intoDocData
	 * @throws java.lang.Exception
	 */
	protected void intoDoc(GeneralVO[] intoDocData) throws java.lang.Exception {
		//效率优化，前台加锁改为后台加锁
		Vector vPkPsn = new Vector();
		
			for (int i = 0; i < intoDocData.length; i++) {
				String pk = (String) intoDocData[i].getFieldValue("pk_psndoc");
//				if (!lockPsn(pk)) {
//					this.getFrameUI().showWarningMessage(
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"600704", "UPP600704-000082")/*
//																	 * @res
//																	 * "当前列表中的人员:"
//																	 */
//									+ intoDocData[i].getAttributeValue(
//											"psnname").toString()
//									+ nc.ui.ml.NCLangRes.getInstance()
//											.getStrByID("600704",
//													"UPP600704-000083")/*
//																		 * @res
//																		 * "正在被其他用户操作，操作失败"
//																		 */);
//					for (int k = 0; k < v.size(); k++) {
//						freeLockPsn((String) v.elementAt(k));
//					}
//					return;
//				}
//				v.addElement(pk);
				if (!vPkPsn.contains(pk)) {
					vPkPsn.addElement(pk);
				}
			}
			String[] pk_psndocs = null;
			if (vPkPsn.size() > 0) {
				pk_psndocs = new String[vPkPsn.size()];
				vPkPsn.copyInto(pk_psndocs);
			}
			HIDelegator.getPsnInf().intoDoc(intoDocData, pk_psndocs,Global.getUserID());

		
	}
	
	/**
	 * 修改申请单中人员的状态未未批准
	 * @throws java.lang.Exception
	 */
	private void UpdatePsnstate(int state) throws java.lang.Exception {
		java.util.Vector v = new java.util.Vector();
		Vector vPkPsn = new Vector();
		try {
			GeneralVO[] notintoDocData = getDocBusinessMainPanel().getPsnListData(state);
			if(notintoDocData==null||notintoDocData.length==0){
				return;
			}
			for (int i = 0; i < notintoDocData.length; i++) {
				String pk = (String) notintoDocData[i].getFieldValue("pk_psndoc");
				if (!lockPsn(pk)) {
					this.getFrameUI().showWarningMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"600704", "UPP600704-000082")/*
																	 * @res
																	 * "当前列表中的人员:"
																	 */
									+ intoDocData[i].getAttributeValue(
											"psnname").toString()
									+ nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("600704",
													"UPP600704-000083")/*
																		 * @res
																		 * "正在被其他用户操作，操作失败"
																		 */);
					for (int k = 0; k < v.size(); k++) {
						freeLockPsn((String) v.elementAt(k));
					}
					return;
				}
				v.addElement(pk);
				if (!vPkPsn.contains(pk)) {
					vPkPsn.addElement(pk);
				}
			}
			String[] pk_psndocs = null;
			if (vPkPsn.size() > 0) {
				pk_psndocs = new String[vPkPsn.size()];
				vPkPsn.copyInto(pk_psndocs);
			}
			//修改这部分人的状态未未批准
			HIDelegator.getPsnInf().updatePsnState(pk_psndocs,2);

		} finally {
			for (int i = 0; i < v.size(); i++) {
				freeLockPsn((String) v.elementAt(i));
			}
		}
	}

	/**
	 * 锁记录
	 *
	 * @param psnpk
	 * @return
	 * @throws java.lang.Exception
	 */
	public boolean lockPsn(String psnpk) throws java.lang.Exception {
		try {
			return PKLock.getInstance().acquireLock(psnpk,
					nc.ui.hr.global.Global.getUserID(), null);
		} catch (Exception e) {
			this.getFrameUI().handleException(e);
			throw e;
		}
	}

	/**
	 * 解锁记录。 创建日期：(2004-7-14 19:12:37)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void freeLockPsn(String psnpk) throws java.lang.Exception {
		try {
			PKLock.getInstance().releaseLock(psnpk,
					nc.ui.hr.global.Global.getUserID(), null);
		} catch (Exception e) {
			this.getFrameUI().handleException(e);
			throw e;
		}
	}

	/**
	 * 获得vo数组包括人员主键和人员编码
	 */
	public DocPsnCodeVO[] getPsnCodeVOS(GeneralVO[] psnlistVOS)throws Exception {
		if (psnlistVOS == null) {
			return null;
		}
		DocPsnCodeVO[] psncodeVOS = null;
		retrivePsncodeInGroup();
		Vector v = new Vector();
		for(int i =0;i<psnlistVOS.length;i++){
			if(psnlistVOS[i].getAttributeValue("psncode")==null || "".equals(psnlistVOS[i].getAttributeValue("psncode"))){
				DocPsnCodeVO tempvo = new DocPsnCodeVO();
				tempvo.setPk_psndoc((String) psnlistVOS[i].getAttributeValue("pk_psndoc"));
				v.addElement(tempvo);
			}
		}
		if(v.size()>0){
			psncodeVOS = new DocPsnCodeVO[v.size()];
			v.copyInto(psncodeVOS);
			for (int i = 0; i < psncodeVOS.length; i++) {
				if ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) // 如果是集团内唯一，则按集团产生人员编码
				{
					autopsncode = HIDelegator
							.getBillcodeRule()
							.getBillCode_RequiresNew("BS", "0001", null, getObjVO());
				} else {
					autopsncode = HIDelegator.getBillcodeRule()
							.getBillCode_RequiresNew("BS", Global.getCorpPK(),
									null, getObjVO());
				}
				psncodeVOS[i].setPsncode(autopsncode);
			}
		}
		return psncodeVOS;

	}

	/**
	 *
	 * @param psncodeVOS
	 * @throws Exception
	 */
	public void setPsnCode(DocPsnCodeVO[] psncodeVOS) throws Exception {
		if (psncodeVOS == null) {
			return;
		}
		if ((getPsncodeAutoGenerateParam() != null)
				&& getPsncodeAutoGenerateParam().trim().equals("1")) {
			getDocDataModel().inserPsnCodeToBD(psncodeVOS);
		}
	}

	/**
	 *
	 * @throws BusinessException
	 */
	private void retrivePsncodeInGroup() throws BusinessException {
		isUniquePsncodeInGroup = PubDelegator.getIParValue().getParaString("0001", "HI_CODEUNIQUE");
	}

	/**
	 * 得到是否自动编码参数
	 *
	 * @throws ValidationException
	 */
	protected String getPsncodeAutoGenerateParam() throws ValidationException {
		try {
			if (psncodeAutoGenerate == null) {
				psncodeAutoGenerate = PubDelegator.getIParValue().getParaString("0001", "HI_CODECRTTYPE");
				if (psncodeAutoGenerate == null) {
					psncodeAutoGenerate = PSNCODE_AUTO_GENERATE;
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
		return psncodeAutoGenerate;
	}

	/**
	 * 人员采集自动编码 :获得单据编号VO。 创建日期：(2004-5-19 16:48:20)
	 */
	public nc.vo.pub.billcodemanage.BillCodeObjValueVO getObjVO() {
		if (objBillCodeVO == null) {
			objBillCodeVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
			objBillCodeVO.setAttributeValue("公司", Global.getCorpPK());
			objBillCodeVO.setAttributeValue("操作员", Global.getUserID());
		}
		return objBillCodeVO;
	}

	public GeneralVO[] getIntoDocData() {
		return intoDocData;
	}

	public void setIntoDocData(GeneralVO[] intoDocData) {
		this.intoDocData = intoDocData;
	}

	public GeneralVO[] getIntoPsnList() {
		return intoPsnList;
	}

	public void setIntoPsnList(GeneralVO[] intoPsnList) {
		this.intoPsnList = intoPsnList;
	}
	
	public String getDatasourse() {
	String ncFileName = "Ufida/source/";
	String xmlfile = "Ufida/source/source.properties";

	File myFilePath = new File(ncFileName);
	if (!(myFilePath.exists())) {
		myFilePath.mkdirs();
	}
	
	
	try {
		File xmlList = new File(xmlfile);

		// 如果xmlList.xml不存在则创建
		Boolean bl = xmlList.exists();

		if (!bl) {
//			String path = QueryList.class.getClass().getResource("/").getPath();
			xmlList = new File(xmlfile);
			xmlList.createNewFile();
			InputStream is = new FileInputStream(xmlfile);
			Properties prop = new Properties();
			prop.load(is);
			is.close();
			if (prop.keySet().size() == 0) {
				OutputStream fos = new FileOutputStream(xmlfile);
				prop.setProperty("sources", "nc56true");
				prop.store(fos, " ");
				fos.close();
			}

			return prop.getProperty("sources");
		}else {
			InputStream is = new FileInputStream(xmlfile);

			Properties prop = new Properties();
			prop.load(is);
			is.close();
			return prop.getProperty("sources");
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

}
