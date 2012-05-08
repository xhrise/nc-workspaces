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

	private String autopsncode = null;// ����Ա�������Զ�����ʱ��

	private String isUniquePsncodeInGroup = null;// �Ƿ����� ������Ψһ�Ĳ���

	// ��Ա�ɼ��Զ�����, �Զ����ɻ����ֹ�����
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

		intoPsnList = getDocBusinessMainPanel().getPsnListData(1); // ����ͨ������Ա

		if ((getPsncodeAutoGenerateParam() != null)
				&& getPsncodeAutoGenerateParam().trim().equals("0")) {
			checkPsnCode(intoPsnList);
		}

		intoDocData = getDocDataModel().getIntoDocData(intoPsnList);

		checkPsnListDept(intoDocData);

		return true;
	}

	/**
	 * ֱ������
	 */
	public boolean doDirectApprove(HRAggVO aggVO, String censorText,int blApproved)throws BusinessException {

		
		super.doDirectApprove(aggVO, censorText, blApproved);
		
		String billpk = headVO.getPrimaryKey();
		DocApplyHVO newHeaderVO = getDocDataModel().getHeadVOFromDB(billpk);
		
		try {
			if (blApproved == DirectApproveDialog.PF_APPROVE_APPROVED) {// ��׼	
				
//				del by zhyan 2007-08-16 ���ڳ��൫�ǲ��ϸ���Ƶ���Ա������ְ,��̨�޷�������ʾ,���Ի��Ƿŵ�ǰ̨��������ť�Ͻ��б���У��
//				if (isExceedWorkout(intoDocData)) {
//					return false;
//				}
				newHeaderVO.setBillstate(blApproved);
				newHeaderVO.setApprovenote(censorText);
			} else if (blApproved == DirectApproveDialog.PF_APPROVE_REJECTED) {// ����׼
				newHeaderVO.setBillstate(blApproved);
				newHeaderVO.setApprovenote(censorText);
			} else if (blApproved == DirectApproveDialog.PF_APPROVE_RETURN) {// ����
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
	 * ��д����ִ�з���
	 */
	public void execute() throws Exception {
		getFrameUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000001")/*@res "����..."*/);
		if(isInDoc(intoDocData)){
			return;
		}
		//�������,����ת������û�ѡ��ת��,��ȡ��
		if (getDocBusinessMainPanel().isExceedWorkout(intoDocData,true)) {
			return;
		} else {
			super.execute();
			afterExecute();
			getFrameUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000002")/*@res "���������������"*/);
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
					//Ч���Ż�start ����������
					String pk_psndoc = (String) psnListTemp[i].getAttributeValue("pk_psndoc");
					String pk_psnbasdoc = (String) psnListTemp[i].getAttributeValue("pk_psnbasdoc");

					String sql1 = " select 1 from bd_psndoc where pk_psndoc ='"+pk_psndoc+"'and indocflag ='Y'";
					String sql2 = " select 1 from bd_psndoc where psnclscope = 0 and indocflag = 'Y' and pk_psnbasdoc ='"+pk_psnbasdoc+"' and pk_corp <>'"+Global.getCorpPK()+"'";
					sqls[i*2] = sql1;
					sqls[i*2+1] = sql2;
//					into = HIDelegator.getPsnInf().isRecordExist(sql1);
//					if(into){
//						getFrameUI().showErrorMessage(psnname+nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000003")/*@res "�Ѿ�ת����Ա�����������ٴ���ְ!"*/);
//						return into;
//					}
//					String sql2 = " select 1 from bd_psndoc where psnclscope = 0 and indocflag = 'Y' and pk_psnbasdoc ='"+pk_psnbasdoc+"' and pk_corp <>'"+Global.getCorpPK()+"'";
//					boolean existrehire = HIDelegator.getPsnInf().isRecordExist(sql2);
//					if(existrehire){
//						getFrameUI().showErrorMessage(psnname+" ��Ա�Ѿ���������˾��ְ�������ڱ���˾��ְ��");
//						return existrehire;
//					}	
				}
				boolean[] isexists = HIDelegator.getPsnInf().isRecordExists(sqls);
				for (int i = 0; i < psnListTemp.length; i++) {
					String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
					into =  isexists[2*i];
					boolean existrehire = isexists[2*i+1];
					if(into){
						getFrameUI().showErrorMessage(psnname+nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000003")/*@res "�Ѿ�ת����Ա�����������ٴ���ְ!"*/);
						return into;
					}
					if(existrehire){
//						getFrameUI().showErrorMessage(psnname+" ��Ա�Ѿ���������˾��ְ�������ڱ���˾��ְ��");
						getFrameUI().showErrorMessage(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
								"UPP600700-000155")/*@res " ��Ա�Ѿ���������˾��ְ�������ڱ���˾��ְ��"*/);
						return existrehire;
					}	
				}
				//Ч���Ż�end
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
				getFrameUI().showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000004")/*@res "���뵥�Ѿ�������������ˢ�º�����!"*/);
				return over;
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
		return over;
	}

	/**
	 * ����ͨ����ת����Ա�����Ȳ���
	 *
	 * @throws Exception
	 */
	public void afterExecute() throws Exception {
		String billpk = headVO.getPrimaryKey();
		String note = (String)headVO.getAttributeValue(getDocDataModel().getPFConfig().getApproveNoteFieldCode());
		DocApplyHVO newHeaderVO = getDocDataModel().getHeadVOFromDB(billpk);
		
		if (newHeaderVO != null) {
			// ��׼״̬�����ݵ��޸�
			if (newHeaderVO.getBillstate().intValue() == DocBillState.BILLSTATE_PASSED) {
				//version5.6 dusx�޸���2009/5/4������ԭ���������Ͳ�����Ա�ڲ�ͬ�������ϣ����ܻᵼ�����ݲ�һ�£��ֽ�ת����Ա������������У����DocFlowService��,������ʹ��ͬһ������д���
//				// ת��֮ǰ��������Ա����
//				DocPsnCodeVO[] psncodeVOS = null;
//				if (intoPsnList != null) {
//					psncodeVOS = getPsnCodeVOS(intoPsnList);
//				}
//				setPsnCode(psncodeVOS);
//				// ת����Ա����,ͬʱ�� ��ְ�Ӽ� ������ͬ���� ������������50ϵͳ���
//				intoDoc(intoDocData);
//				// ����ͨ����Ա
//				UpdatePsnstate(2);
				
				if (intoDocData == null || intoDocData.length < 1) {
					// getFrameUI().showWarningMessage("�õ���û����Ա��ְ,����Ҫ����֪ͨ!");
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
						JOptionPane.showMessageDialog(null, e.getMessage() , "��ʾ" , JOptionPane.OK_OPTION);
						
					}
					
					if (userSelectDlg.getResult() == UIDialog.ID_OK) {
						HIDelegator.getPsnInf().insertRecievers(Global.getCorpPK(),recievers, 2);// ������Ϣ������
					
						if (recievers.length() > 0) {
	
							String subject = nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000005")/*@res "��ְ֪ͨ"*/;
							StringBuffer msgcontent = new StringBuffer();
							StringBuffer mailcontent = new StringBuffer();
							StringBuffer psninfo = new StringBuffer();
							StringBuffer msgpsninfo = new StringBuffer();
							
							for(int i=0; i<intoDocData.length;i++){
								psninfo.append("\n");
								if(intoDocData[i].getAttributeValue("psncode")!=null && !"".equals(intoDocData[i].getAttributeValue("psncode"))){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000006")/*@res "��Ա����Ϊ:"*/+intoDocData[i].getAttributeValue("psncode")+",");
								}
								//������ʱ�Ѿ���֤�˶���psncode
//								else{
//									String psncode = "";
//									for(int j =0 ;j<psncodeVOS.length ;j++){
//										if(psncodeVOS[j].getPk_psndoc().equalsIgnoreCase((String)intoDocData[i].getAttributeValue("pk_psndoc"))){
//											psncode = psncodeVOS[j].getPsncode();
//										}
//									}
//									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000006")/*@res "��Ա����Ϊ:"*/+psncode+",");
//
//								}
								if(intoDocData[i].getAttributeValue("psnname")!=null){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000007")/*@res "��Ա����Ϊ:"*/+intoDocData[i].getAttributeValue("psnname")+",");
									msgpsninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000007")/*@res "��Ա����Ϊ:"*/+intoDocData[i].getAttributeValue("psnname")+",");
								}
								if(intoDocData[i].getAttributeValue("indutydate")!=null){
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000008")/*@res "��ְ����Ϊ:"*/+intoDocData[i].getAttributeValue("indutydate")+",");
									msgpsninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000008")/*@res "��ְ����Ϊ:"*/+intoDocData[i].getAttributeValue("indutydate")+",");
								}
								if(intoDocData[i].getAttributeValue("unitname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000009")/*@res "��˾Ϊ:"*/+intoDocData[i].getAttributeValue("unitname")+",");
								if(intoDocData[i].getAttributeValue("deptname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000010")/*@res "����Ϊ:"*/+intoDocData[i].getAttributeValue("deptname")+",");
								if(intoDocData[i].getAttributeValue("jobname")!=null)
									psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000011")/*@res "��λΪ:"*/+intoDocData[i].getAttributeValue("jobname"));
								psninfo.append(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000012")/*@res "����Ա��ְ!"*/);
								
								// ---------- ���ͬ��OA���� add by river ------------ //
								
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
							String msg = nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000013")/*@res "����֪ͨ�ɹ�!������"*/ + msgnum + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000014")/*@res "����Ϣ"*/ + mailnum+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000015")/*@res "���ʼ�"*/;
							if (nomailname.trim().length() > 1) {
								nomailname = nomailname.substring(0, nomailname.length() - 1);
								msg += nomailname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000016")/*@res "û��Email��ַ"*/;
							}
							getFrameUI().showHintMessage(msg);
						}
					} else {
						userSelectDlg.closeCancel();
						
//						---------- ���ͬ��OA���� add by river ------------ //
						
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
			// �޸ı�ͷ����
			if (newHeaderVO.getBillstate().intValue() == DocBillState.BILLSTATE_NO_PASS) {
				UpdatePsnstate(0);//���ݲ�ͨ��,��������Ա��δδ��׼
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
	// �õ�MODEL
	public DocDataModel getDocDataModel() {
		return (DocDataModel) getDataModel();
	}

	// �õ�MAINPANEL
	public DocApproveMainPanel getDocBusinessMainPanel() {
		return (DocApproveMainPanel) getMainPanel();
	}

	/**
	 * ת����Ա�������˱����Ѿ��в��ţ���顣
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
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
																				 * "�Ĳ���"
																				 */,
									"notnull", pk_deptdoc);
				} catch (Exception e) {
					throw new ValidationException(e.getMessage());
				}

			}
		}
	}

	/**
	 * ת����Ա�������˱����Ѿ�����Ա���룬��顣
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void checkPsnCode(GeneralVO[] psnListTemp)
			throws ValidationException {
		if (psnListTemp != null) {
			for (int i = 0; i < psnListTemp.length; i++) {
				String psncode = (String) psnListTemp[i].getAttributeValue("psncode");
				String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
				try {
					FieldValidator.validate(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600705","UPP600705-000017")/*@res "����Ա����"*/, "notnull",psncode);
				} catch (Exception e) {
					throw new ValidationException(e.getMessage());
				}
			}
		}
	}

	

	/**
	 * �õ���Ҫ�����жϵ�����
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
						.getAttributeValue("psnclscope");// ֻ�ж���Ա����Ϊ��ְ
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
	 * ����ְ��Ա���ո�λ�����ţ���˾����
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
		//Ч���Ż���ǰ̨������Ϊ��̨����
		Vector vPkPsn = new Vector();
		
			for (int i = 0; i < intoDocData.length; i++) {
				String pk = (String) intoDocData[i].getFieldValue("pk_psndoc");
//				if (!lockPsn(pk)) {
//					this.getFrameUI().showWarningMessage(
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"600704", "UPP600704-000082")/*
//																	 * @res
//																	 * "��ǰ�б��е���Ա:"
//																	 */
//									+ intoDocData[i].getAttributeValue(
//											"psnname").toString()
//									+ nc.ui.ml.NCLangRes.getInstance()
//											.getStrByID("600704",
//													"UPP600704-000083")/*
//																		 * @res
//																		 * "���ڱ������û�����������ʧ��"
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
	 * �޸����뵥����Ա��״̬δδ��׼
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
																	 * "��ǰ�б��е���Ա:"
																	 */
									+ intoDocData[i].getAttributeValue(
											"psnname").toString()
									+ nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("600704",
													"UPP600704-000083")/*
																		 * @res
																		 * "���ڱ������û�����������ʧ��"
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
			//�޸��ⲿ���˵�״̬δδ��׼
			HIDelegator.getPsnInf().updatePsnState(pk_psndocs,2);

		} finally {
			for (int i = 0; i < v.size(); i++) {
				freeLockPsn((String) v.elementAt(i));
			}
		}
	}

	/**
	 * ����¼
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
	 * ������¼�� �������ڣ�(2004-7-14 19:12:37)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
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
	 * ���vo���������Ա��������Ա����
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
				if ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) // ����Ǽ�����Ψһ���򰴼��Ų�����Ա����
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
	 * �õ��Ƿ��Զ��������
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
	 * ��Ա�ɼ��Զ����� :��õ��ݱ��VO�� �������ڣ�(2004-5-19 16:48:20)
	 */
	public nc.vo.pub.billcodemanage.BillCodeObjValueVO getObjVO() {
		if (objBillCodeVO == null) {
			objBillCodeVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
			objBillCodeVO.setAttributeValue("��˾", Global.getCorpPK());
			objBillCodeVO.setAttributeValue("����Ա", Global.getUserID());
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

		// ���xmlList.xml�������򴴽�
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
