package nc.ui.trn.records.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.hr.utils.ResHelper;
import nc.impl.mbSyn.MbSynImpl;
import nc.itf.hi.HIDelegator;
import nc.itf.hr.bd.ICorpWorkout;
import nc.itf.hr.cm.IHrcmPsnChanged;
import nc.itf.hr.comp.IParValue;
import nc.itf.hr.trn.TRNDelegator;
import nc.itf.mbSyn.IMbSys;
import nc.itf.mbSyn.IQueryList;
import nc.itf.mbSyn.IServiceUtil;
import nc.itf.uap.bd.psn.IPsncl;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.global.Global;
import nc.ui.hr.global.GlobalTool;
import nc.ui.hrsm.hrsm_301.action.MailMessageRecieverSelectDlg;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.smtm.pub.CommonValue;
import nc.ui.trn.records.BillPreformSelectDlg;
import nc.ui.trn.records.GeneNewPsncodesDlg;
import nc.ui.trn.records.PerformProtertiesDlg;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.wa.pub.WaLinkData;
import nc.vo.bd.b05.PsnclVO;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_301.HRMainVO;
import nc.vo.hr.comp.pf.PFAggVO;
import nc.vo.hr.tools.pub.ArrayUtils;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.hrsm.hrsm_301.StApplyPFConfig;
import nc.vo.hrsm.hrsm_301.StapplybBVO;
import nc.vo.hrsm.hrsm_301.StapplybHHeaderVO;
import nc.vo.hrsm.hrsm_301.StapplybHItemVO;
import nc.vo.hrsm.hrsm_301.StapplybHVO;
import nc.vo.om.om_013.WorkoutResultVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.smtm.pub.PerfromPropVO;
import nc.vo.wa.wa_031.PsnappaproveBVO;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.seeyon.client.PersonServiceStub;
import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;

public class PerformAction extends RdsBaseAction {

	private StapplybHVO[] billVOS;

	// 类别 归属范围对应
	private Map<String, Integer> hashPsnclScope = new HashMap<String, Integer>();

	// 选中的执行单据
	private StapplybHVO[] toPerformBills;

	// 将待执行记录按执行类型分类
	private HashMap<Integer, StapplybHVO[]> toPerformBillsHash = new HashMap<Integer, StapplybHVO[]>();

	// 公司内调配
	private static final Integer RDS_IN_CORP = 0;

	// 调出
	private static final Integer RDS_CROSS_OUT = 1;

	// 调入
	private static final Integer RDS_CROSS_IN = 2;

	public PerformAction(FrameUI frameUI1) {
		super(frameUI1);
	}

	@Override
	public void execute() throws Exception {
		// 单据编码
		String[] billNOs = null;
		// 选择待执行单据
		UFDate dproeffectdate = null;
		if (billVOS.length > 1) {
			StapplybHVO[] billforsel = billVOS;
			BillPreformSelectDlg dlg = new BillPreformSelectDlg(
					getParentUI(),
					(getParentUI().getModuleCode().equals(
							CommonValue.STAFFING_RECORDS) ? CommonValue.PROJECT_TYPE_STAFFING
							: CommonValue.PROJECT_TYPE_TURNOVER), billforsel);
			dlg.showModal();
			if (dlg.getResult() == UIDialog.ID_CANCEL)
				return;
			// 获得选中单据信息
			billNOs = dlg.getBillNOs();
			dproeffectdate = dlg.getFirstSelectDate();// 第一个选中的单据的计划调配
			toPerformBills = getSelectedBills(billforsel, billNOs);
		} else {
			StapplybHHeaderVO parentvo = (StapplybHHeaderVO) billVOS[0]
					.getParentVO();
			billNOs = new String[] { parentvo.getVbillno() };
			dproeffectdate = parentvo.getDproeffectdate();// 第一个选中的单据的计划调配
			toPerformBills = billVOS;
		}
		if (toPerformBills == null || toPerformBills.length < 1) {
			throw new BusinessException(ResHelper.getString("60090713",
					"UPP60090713-000059")/*
											 * @res "无满足条件的申请单可以执行!"
											 */);
		}
		/* 确认执行 */
		if (getParentUI()
				.showOkCancelMessage(
						ResHelper.getString("60090713", "UPP60090713-000060")/* "您确定要执行吗？" */) == UIDialog.ID_CANCEL) {
			return;
		}
		if (getParentUI().getModuleCode().equalsIgnoreCase(
				CommonValue.STAFFING_RECORDS)) {
			// 公司内调配/调出/调入，将单据分类
			filtBillsByPerformType();
			// 跨公司调配调出时 检查是否有未解除合同,提示是否继续执行
			if (!checkUpContract(filterStafOutBills(toPerformBillsHash
					.get(RDS_CROSS_OUT)))) {
				return;
			}
		} else {
			// 离职执行时 检查是否有未解除合同,提示是否继续执行
			if (!checkUpContract(toPerformBills)) {
				return;
			}
		}
		int type = (getParentUI().getModuleCode()
				.equalsIgnoreCase(CommonValue.STAFFING_RECORDS)) ? PerformProtertiesDlg.TYPE_BJ
				: PerformProtertiesDlg.TYPE_BK;
		// 执行设置 begin
		PerformProtertiesDlg proDlg = new PerformProtertiesDlg(getParentUI(),
				type);
		// 设入执行日期默认值
		if (dproeffectdate != null)
			proDlg.setTrndate(dproeffectdate);
		proDlg.showModal();
		if (proDlg.getResult() == UIDialog.ID_CANCEL)
			return;
		PerfromPropVO prop = proDlg.getResultVO();
		// 得到用户输入的日期，默认为系统登录日期
		UFDate userDate = proDlg.getTrndate() != null ? proDlg.getTrndate()
				: new UFDate(nc.ui.hr.global.Global.getLogDate().toString());
		prop.setEffectdate(userDate);
		prop.setIfGeneWaBill(false);
		if (prop.isAddInBlack() && prop.getAddreason() == null) {
			throw new BusinessException(ResHelper.getString("60091013",
					"UPT60091013-000021")/* "必须填写加入黑名单原因！" */);
		}

		// -----------人员调配时点击执行，更新OA用户信息 add by river ------------ //
		StapplybHHeaderVO parentvo = (StapplybHHeaderVO) billVOS[0]
				.getParentVO();
		PersonServiceStub.PersonInfoParam_All person = null;
		IQueryList queryList = (IQueryList) NCLocator.getInstance().lookup(
				IQueryList.class.getName());
		// List list = queryList.getPerson(this.getDatasourse(),
		// billVOS[0].getPk_psndocs()[0]);
		// if(list.size() > 0){
		// person = (PersonInfoParam_All) list.get(0);
		// }

		boolean sucess = false;

		String datasource = "nc56true";
	

		if (CommonValue.STAFFING_RECORDS.equals(getFrameUI().getModuleCode())) {
			sucess = performStaffing(prop);

			if (sucess) {
				// ----------- add by river ------------ //

				if (getParentUI().getModuleCode().equalsIgnoreCase(
						CommonValue.STAFFING_RECORDS)) {
					// 公司内调配/调出/调入，将单据分类
					// PersonServiceStub.PersonInfoParam_All person2 =
					// queryList.getPerson(this.getDatasourse(),
					// billVOS[0].getPk_psndocs()[0]).get(0);

					try {
						IServiceUtil util = (IServiceUtil) NCLocator
								.getInstance().lookup(
										IServiceUtil.class.getName());
						Long accid = util.getAccountId("");
					} catch (Exception e1) {
						JOptionPane
								.showMessageDialog(
										null,
										"连接A8服务器失败，调配操作继续执行，本次同步A8操作被截断！恢复该人员信息并连接上服务器后再次进行此操作即可同步信息至A8！",
										"提示", JOptionPane.OK_OPTION);

						afterExecute();

						return;
					}

					for (String pk_psndoc : billVOS[0].getPk_psndocs()) {
						
						List<PersonInfoParam_All> personList = queryList.getPerson1("nc56true",
								pk_psndoc);
						if (personList.size() == 0) {
							System.out.println("人员列表:NULL");
							//return;
						} else
							person = personList.get(0);
						
						try {
							Long pk_oauser = null;
							List list = queryList.getPersonbypkpsn(datasource,
									pk_psndoc);
							if (list.size() == 0) {
								System.out.println("人员信息：OA name is NULL");
								return;
							} else
								pk_oauser = (Long) list.get(0);
							IMbSys mbSyn = new MbSynImpl();
							
							try {
								String[] defs = queryList.getPersonDefVal(pk_psndoc, datasource);
								queryList.updateDelPerson2(person.getLoginName(), datasource);
								queryList.updateNewPerson(pk_psndoc, defs, datasource);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(person.getLoginName() != null) {
								nc.itf.uap.IUAPQueryBS querybs = (nc.itf.uap.IUAPQueryBS)NCLocator.getInstance().lookup(nc.itf.uap.IUAPQueryBS.class.getName());
								String sql = "select pk_psndoc from bd_psndoc where pk_psnbasdoc = (select pk_psnbasdoc from bd_psndoc where pk_psndoc = '"+pk_psndoc+"') and psnclscope = 0";
								pk_psndoc = querybs.executeQuery(sql, new ColumnProcessor("pk_psndoc")) == null ? "" : querybs.executeQuery(sql ,new ColumnProcessor("pk_psndoc")).toString();
								
								mbSyn.update(pk_psndoc, parentvo.getPk_aimcorp(),
										pk_oauser, datasource, true, false);
								
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				}
			}
		} else {
			// boolean addCheck =
			// queryList.addPsnTurnOver(billVOS[0].getPk_psndocs()[0],
			// datasource);
			// System.out.println("添加到人员离职表：" + addCheck);
			
			int confNum = 0;
			try {
				IServiceUtil util = (IServiceUtil) NCLocator.getInstance()
						.lookup(IServiceUtil.class.getName());
				Long accid = util.getAccountId("");
			} catch (Exception e1) {
				confNum = JOptionPane
						.showConfirmDialog(
								null,
								"连接A8服务器失败，本次同步A8操作被截断！是否继续执行离职操作！\n (注：若无A8账号，请继续执行离职操作)",
								"提示", JOptionPane.OK_OPTION);
//
//				afterExecute();
//
//				return;
			}
			
			if(confNum != 1)
				sucess = performTurnOver(prop);

			if (!getParentUI().getModuleCode().equalsIgnoreCase(
					CommonValue.STAFFING_RECORDS)) {
				// 离职执行时 检查是否有未解除合同,提示是否继续执行
				
				// PersonServiceStub.PersonInfoParam_All person2 =
				// queryList.getPerson(this.getDatasourse(),
				// billVOS[0].getPk_psndocs()[0]).get(0);

//				try {
//					IServiceUtil util = (IServiceUtil) NCLocator.getInstance()
//							.lookup(IServiceUtil.class.getName());
//					Long accid = util.getAccountId("");
//				} catch (Exception e1) {
//					JOptionPane
//							.showMessageDialog(
//									null,
//									"连接A8服务器失败，离职操作继续执行，本次同步A8操作被截断！恢复该人员信息并连接上服务器后再次进行此操作即可同步信息至A8！",
//									"提示", JOptionPane.OK_OPTION);
//
//					afterExecute();
//
//					return;
//				}

				for (String pk_psndoc : billVOS[0].getPk_psndocs()) {
					try {
						Long pk_oauser = null;
						List list = queryList.getPersonbypkpsn(datasource,
								pk_psndoc);
						if (list.size() == 0) {
							System.out.println("人员信息：OA name is NULL");
							return;
						} else
							pk_oauser = (Long) list.get(0);
						IMbSys mbSyn = new MbSynImpl();
						// mbSyn.update(billVOS[0].getPk_psndocs()[0], parentvo
						// .getPk_aimcorp(), pk_oauser, this.getDatasourse(),
						// false);
						// mbSyn.delete(psndoc, datasource);
						mbSyn.enable(pk_oauser, false);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

			}
		}

		if (sucess) {
			afterExecute();
		}

	}

	public String getDatasourse() {
		String ncFileName = "Ufida/datasource/";
		String xmlfile = "Ufida/datasource/source.properties";

		File myFilePath = new File(ncFileName);
		if (!(myFilePath.exists())) {
			myFilePath.mkdirs();
		}

		try {
			File xmlList = new File(xmlfile);

			// 如果xmlList不存在则创建
			Boolean bl = xmlList.exists();

			if (!bl) {
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
			} else {
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

	private void afterExecute() throws Exception {
		for (int i = 0; i < toPerformBills.length; i++) {
			getLeftTreePanel().removeNode(toPerformBills[i]);
			// 去掉model中缓存的记录
			String idValue = toPerformBills[i].getParentVO().getPrimaryKey();
			if (!org.apache.commons.lang.ArrayUtils.isEmpty(getDataMdl()
					.getBillInfoVOS())) {
				Vector<StapplybHVO> temp = new Vector<StapplybHVO>();
				for (StapplybHVO o : getDataMdl().getBillInfoVOS()) {
					if (!o.getParentVO().getPrimaryKey().equals(idValue))
						temp.add(o);
				}
				getDataMdl().setBillInfoVOS(temp.toArray(new StapplybHVO[0]));
			}
		}
		toPerformBillsHash.clear();
	}

	private void checkDimissionDate(String[] billpks, UFDate userDate)
			throws BusinessException {
		Map psndocMap = TRNDelegator.getStapplybH().performTurnOverValidate(
				billpks, userDate, corpPK);
		if (psndocMap != null && psndocMap.size() > 0) {
			StringBuffer sbOut = new StringBuffer();
			for (Iterator it = psndocMap.keySet().iterator(); it.hasNext();) {
				String key = it.next().toString();
				List list = (List) psndocMap.get(key);
				StringBuffer values = new StringBuffer();
				int i = 0;
				for (Iterator itt = list.iterator(); itt.hasNext();) {
					if (i != 0)
						values.append(",");
					values.append(itt.next().toString());
				}
				sbOut.append(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"hr_trn_pub", "UPPhr_trn_pub-000059")/*
																		 * @res
																		 * "单据号为:"
																		 */).append(key).append(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"hr_trn_pub", "UPPhr_trn_pub-000060")/*
																		 * @res "
																		 * 中的人员"
																		 */).append(values.toString());
			}

			throw new BusinessException(sbOut
					+ ResHelper.getString("60090713", "UPP60090713-000158")/*
																			 * @res
																			 * "离职日期不能小于有效任职记录的开始日期!"
																			 */);

		}

	}

	private void autoGeneratedWABill(Map<String, String> resultLs)
			throws Exception {
		WaLinkData linkData = new WaLinkData();
		List<PsnappaproveBVO> wabvoLs = new ArrayList<PsnappaproveBVO>();
		int i = 0;
		PsnappaproveBVO wabvo = null;
		StapplybHItemVO[] itemVOs = null;
		for (StapplybHVO o : toPerformBills) {
			for (String s : resultLs.keySet()) {
				if (ArrayUtils.isExistInArray(o.getPk_psndocs(), s)) {
					itemVOs = o.getItemVOsByPK(s);
					wabvo = new PsnappaproveBVO();
					wabvo.setPk_psndoc(resultLs.get(s));
					wabvo.setDeptName(getValueBy(itemVOs, "newpk_deptdoc"));
					wabvo.setPsnname(itemVOs[0].getPsnname());
					wabvo.setPsncode(itemVOs[0].getPsncode());
					wabvoLs.add(wabvo);
				}
			}
		}
		linkData.setPsnappaproveBVO(WaLinkData.getPsnappaproveDeptName(wabvoLs
				.toArray(new PsnappaproveBVO[0])));
		boolean succ = false;
		IFuncWindow window = SFClientUtil.findOpenedFuncWindow("60130702");
		if (window != null) {
			succ = window.closeWindow();
		} else {
			succ = true;
		}
		if (succ) {
			SFClientUtil.openLinkedADDDialog("60130702", getParentUI(),
					linkData);
		} else {
			getParentUI().showWarningMessage(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"hr_trn_pub", "UPPhr_trn_pub-000058")/*
																	 * @res
																	 * "窗口已经打开,没有正确关闭!"
																	 */);
		}

	}

	private void filtBillsByPerformType() {
		Vector<StapplybHVO> incorpvec = new Vector<StapplybHVO>();
		Vector<StapplybHVO> crossOutvec = new Vector<StapplybHVO>();
		Vector<StapplybHVO> crossInvec = new Vector<StapplybHVO>();
		StapplybHHeaderVO header = null;
		for (StapplybHVO o : toPerformBills) {
			header = (StapplybHHeaderVO) o.getParentVO();
			if (header.getPk_currcorp().equals(header.getPk_aimcorp())) {
				incorpvec.add(o);
			} else if (getDataMdl().getPk_corp()
					.equals(header.getPk_currcorp())) {
				crossOutvec.add(o);
			} else if (getDataMdl().getPk_corp().equals(header.getPk_aimcorp())) {
				crossInvec.add(o);
			}
		}
		if (!incorpvec.isEmpty()) {
			toPerformBillsHash.put(RDS_IN_CORP, incorpvec
					.toArray(new StapplybHVO[0]));
		}
		if (!crossOutvec.isEmpty()) {
			toPerformBillsHash.put(RDS_CROSS_OUT, crossOutvec
					.toArray(new StapplybHVO[0]));
		}
		if (!crossInvec.isEmpty()) {
			toPerformBillsHash.put(RDS_CROSS_IN, crossInvec
					.toArray(new StapplybHVO[0]));
		}
	}

	/**
	 * @author chexz
	 */
	public void fendMailToUser(MailMessageRecieverSelectDlg userSelectDlg,
			Map<String, String> performRs, String isBillType, UFDate userDate) {

		try {
			userSelectDlg.setPk_corp(Global.getCorpPK());

			userSelectDlg.refreshContent();

			userSelectDlg.showModal();
			String recievers = getDataMdl().getUserpks();
			if (userSelectDlg.getResult() == UIDialog.ID_OK) {
				// 调配
				if (isBillType.equals(StApplyPFConfig.PROJECT_TYPE_STAFFING))
					HIDelegator.getPsnInf().insertRecievers(Global.getCorpPK(),
							recievers, 3);// 保存消息接收人
				else
					// 离职
					HIDelegator.getPsnInf().insertRecievers(Global.getCorpPK(),
							recievers, 4);// 保存消息接收人
			} else {
				userSelectDlg.closeCancel();
				return;
			}
			if (recievers.length() > 0) {
				String subject;
				if (isBillType.equals(StApplyPFConfig.PROJECT_TYPE_STAFFING))
					subject = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000031")/*
																				 * @res
																				 * "调配通知"
																				 */;
				else
					subject = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000032")/*
																				 * @res
																				 * "离职通知"
																				 */;
				String content = " ";
				String psninfo = " ";

				String content2 = " ";
				String psninfo2 = " ";
				StapplybHItemVO[] bodyVOs = null;
				StapplybHHeaderVO headvo = null;
				Map<String, List<String>> typeKeyLsHash = new HashMap<String, List<String>>();
				Map<String, List<StapplybHItemVO[]>> approveHash = new HashMap<String, List<StapplybHItemVO[]>>();
				boolean flag = false;
				for (StapplybHVO o : toPerformBills) {
					headvo = (StapplybHHeaderVO) o.getParentVO();
					for (String pkspndoc : performRs.keySet()) {
						if (ArrayUtils.isExistInArray(o.getPk_psndocs(),
								pkspndoc)) {
							if (!approveHash.containsKey(headvo
									.getPk_hi_stapplyb_h())) {
								approveHash.put(headvo.getPk_hi_stapplyb_h(),
										new ArrayList<StapplybHItemVO[]>());
							}
							approveHash.get(headvo.getPk_hi_stapplyb_h()).add(
									o.getItemVOsByPK(pkspndoc));
							bodyVOs = o.getItemVOsByPK(pkspndoc);
							if (!typeKeyLsHash.containsKey("pk_deptdoc")) {
								typeKeyLsHash.put("pk_deptdoc",
										new ArrayList<String>());
							}
							typeKeyLsHash.get("pk_deptdoc").add(
									getValueBy(bodyVOs, "oldpk_deptdoc"));
							typeKeyLsHash.get("pk_deptdoc").add(
									getValueBy(bodyVOs, "newpk_deptdoc"));
							if (!typeKeyLsHash.containsKey("oldpk_om_job")) {
								typeKeyLsHash.put("oldpk_om_job",
										new ArrayList<String>());
							}
							typeKeyLsHash.get("oldpk_om_job").add(
									getValueBy(bodyVOs, "oldpk_om_job"));
							if (!typeKeyLsHash.containsKey("newpk_om_job")) {
								typeKeyLsHash.put("newpk_om_job",
										new ArrayList<String>());
							}
							typeKeyLsHash.get("newpk_om_job").add(
									getValueBy(bodyVOs, "newpk_om_job"));
							flag = true;
						}
					}
					if (flag) {
						if (!typeKeyLsHash.containsKey("corp")) {
							typeKeyLsHash.put("corp", new ArrayList<String>());
						}
						typeKeyLsHash.get("corp").add(headvo.getPk_currcorp());
						typeKeyLsHash.get("corp").add(headvo.getPk_aimcorp());
						if (!typeKeyLsHash.containsKey("bd_defdoc")) {
							typeKeyLsHash.put("bd_defdoc",
									new ArrayList<String>());
						}
						typeKeyLsHash.get("bd_defdoc").add(
								headvo.getPk_sttype());
						flag = false;
					}
				}
				Map<String, String> nameKeyHash = TRNDelegator.getStapplybH()
						.getTranslateName(typeKeyLsHash);
				for (StapplybHVO o : toPerformBills) {
					headvo = (StapplybHHeaderVO) o.getParentVO();
					for (String pk : approveHash.keySet()) {
						if (!headvo.getPrimaryKey().equals(pk))
							continue;
						if (isBillType
								.equals(StApplyPFConfig.PROJECT_TYPE_STAFFING)) {

							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000033")/*
																	 * @res
																	 * "调配时间:"
																	 */
									+ nullFormalu(userDate != null ? userDate
											.toString()
											: nc.ui.hr.global.Global
													.getLogDate().toString())
									+ ",";
							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000034")/*
																	 * @res
																	 * "调配业务类型:"
																	 */
									+ nullFormalu(nameKeyHash.get(headvo
											.getPk_sttype())) + ",";

						} else {

							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000035")/*
																	 * @res
																	 * "离职时间:"
																	 */
									+ nullFormalu(userDate != null
											|| !userDate.toString().equals("") ? userDate
											.toString()
											: nc.ui.hr.global.Global
													.getLogDate().toString())
									+ ",";
							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000036")/*
																	 * @res
																	 * "离职业务类型:"
																	 */
									+ nullFormalu(nameKeyHash.get(headvo
											.getPk_sttype())) + ",";

						}
						for (StapplybHItemVO[] bodys : approveHash.get(pk)) {

							psninfo = forMulaInformation(headvo, bodys,
									isBillType, nameKeyHash, psninfo, userDate,
									false);
							psninfo2 += forMulaInformation(headvo, bodys,
									isBillType, nameKeyHash, psninfo, userDate,
									true);
						}
						if (isBillType
								.equals(StApplyPFConfig.PROJECT_TYPE_STAFFING)) {
							// 去掉末尾的逗号
							if (psninfo2.endsWith(","))
								psninfo2 = psninfo2.substring(0, psninfo2
										.length() - 1);
							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000062")/*
																	 * @res
																	 * "的人员调配!\n"
																	 */;

						} else {
							// 去掉末尾的逗号
							if (psninfo2.endsWith(","))
								psninfo2 = psninfo2.substring(0, psninfo2
										.length() - 1);
							psninfo2 += nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000063")/*
																	 * @res
																	 * "的人员离职!\n"
																	 */;

						}

					}
				}
				content = subject + "\n" + psninfo;
				content2 = subject + "\n" + psninfo2;

				GeneralVO[] recieverEmails = HIDelegator.getPsnInf()
						.getRecieverEmails(recievers);
				Vector<String> v = new Vector<java.lang.String>();
				String nomailname = "";
				int msgnum = 0;
				if (recieverEmails != null && recieverEmails.length > 0) {
					msgnum = recieverEmails.length;
					for (int i = 0; i < recieverEmails.length; i++) {
						Object email = recieverEmails[i]
								.getAttributeValue("email");
						if (email == null) {
							String name = (String) recieverEmails[i]
									.getAttributeValue("username");
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

				sendMailMsg(subject, content2, getDataMdl().getUserRecievers());

				if (emailAddress != null && emailAddress.length > 0) {
					HIDelegator.getPsnInf().sendMail(subject, content,
							emailAddress);
					mailnum = emailAddress.length;
				}
				String msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000015")/*
																			 * @res
																			 * "发送通知成功!共发送"
																			 */
						+ msgnum
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("hr_trn_pub",
										"UPPhr_trn_pub-000016")/* @res "条消息" */
						+ mailnum
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("hr_trn_pub",
										"UPPhr_trn_pub-000017")/* @res "个邮件" */;
				if (nomailname.trim().length() > 1) {
					nomailname = nomailname.substring(0,
							nomailname.length() - 1);
					msg += nomailname
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("hr_trn_pub",
											"UPPhr_trn_pub-000018")/*
																	 * @res
																	 * "没有Email地址"
																	 */;
				}
				getParentUI().showHintMessage(msg);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param title
	 * @param msg
	 * @param userRecievers
	 */
	public void sendMailMsg(String title, String msg,
			nc.vo.pub.msg.UserNameObject[] userRecievers) {
		try {
			nc.vo.pub.msg.CommonMessageVO msgVo = new nc.vo.pub.msg.CommonMessageVO();
			msgVo.setTitle(title);
			msgVo.setActionType("MSN");
			msgVo.setMessageContent(msg);
			msgVo.setReceiver(userRecievers);
			msgVo.setSendDataTime(Global.getServerTime());
			msgVo.setSender(Global.getUserID());
			HIDelegator.getPFMessage().insertCommonMsg(msgVo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String forMulaInformation(StapplybHHeaderVO headvo,
			StapplybHItemVO[] itBodyVO, String isBillType,
			Map<String, String> nameKeyHash, String psninfo, UFDate userDate,
			boolean bl) throws Exception {
		String psninfo2 = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("60090713", "UPP60090713-000212")/* @res "人员姓名: " */;
		if (isBillType.equals(StApplyPFConfig.PROJECT_TYPE_STAFFING)) {

			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000008")/* @res "单据编号:" */
					+ nullFormalu(headvo.getVbillno()) + ",";
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000033")/* @res "调配时间:" */
					+ nullFormalu(userDate != null
							|| !userDate.toString().trim().equals("") ? userDate
							.toString()
							: nc.ui.hr.global.Global.getLogDate().toString())
					+ ",";
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000034")/* @res "调配业务类型:" */
					+ nullFormalu(nameKeyHash.get(headvo.getPk_sttype())) + ",";
			if (!headvo.getPk_aimcorp().trim().equalsIgnoreCase(
					headvo.getPk_currcorp().trim())) {
				psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000041")/*
																			 * @res
																			 * "原公司:"
																			 */
						+ nullFormalu(nameKeyHash.get(headvo.getPk_currcorp()))
						+ ",";
				psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000042")/*
																			 * @res
																			 * "新公司:"
																			 */
						+ nullFormalu(nameKeyHash.get(headvo.getPk_aimcorp()))
						+ ",";
			}

			for (int i = 0; i < itBodyVO.length; i++) {
				if (itBodyVO[i].getPk_stitem().toString().equals("psnname")) {
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000064")/*
																				 * @res
																				 * "人员姓名:"
																				 */
							+ nullFormalu(itBodyVO[i].getVresult()) + ",";
					psninfo2 += nullFormalu(itBodyVO[i].getVresult()) + ",";
				} else if (itBodyVO[i].getPk_stitem().toString().equals(
						"psncode"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000043")/*
																				 * @res
																				 * "人员编码:"
																				 */
							+ nullFormalu(itBodyVO[i].getVresult()) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"oldpk_deptdoc"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000044")/*
																				 * @res
																				 * "原部门:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"oldpk_om_job"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000045")/*
																				 * @res
																				 * "原岗位:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"newpk_deptdoc"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000046")/*
																				 * @res
																				 * "新部门:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"newpk_om_job"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000047")/*
																				 * @res
																				 * "新岗位:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";

			}

			// 去掉末尾的逗号
			if (psninfo.endsWith(","))
				psninfo = psninfo.substring(0, psninfo.length() - 1);
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000062")/* @res "的人员调配!\n" */;

		} else {

			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000008")/* @res "单据编号:" */
					+ nullFormalu(headvo.getVbillno()) + ",";
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000035")/* @res "离职时间:" */
					+ nullFormalu(userDate != null
							|| !userDate.toString().equals("") ? userDate
							.toString() : nc.ui.hr.global.Global.getLogDate()
							.toString()) + ",";
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000036")/* @res "离职业务类型:" */
					+ nullFormalu(nameKeyHash.get(headvo.getPk_sttype())) + ",";

			for (int i = 0; i < itBodyVO.length; i++) {
				if (itBodyVO[i].getPk_stitem().toString().equals("psnname")) {
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000064")/*
																				 * @res
																				 * "人员姓名:"
																				 */
							+ nullFormalu(itBodyVO[i].getVresult()) + ",";
					psninfo2 += nullFormalu(itBodyVO[i].getVresult()) + ",";
				} else if (itBodyVO[i].getPk_stitem().toString().equals(
						"psncode"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000043")/*
																				 * @res
																				 * "人员编码:"
																				 */
							+ nullFormalu(itBodyVO[i].getVresult()) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"oldpk_deptdoc"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000048")/*
																				 * @res
																				 * "部门:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";
				else if (itBodyVO[i].getPk_stitem().toString().equals(
						"oldpk_om_job"))
					psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("hr_trn_pub", "UPPhr_trn_pub-000049")/*
																				 * @res
																				 * "岗位:"
																				 */
							+ nullFormalu(nameKeyHash.get(itBodyVO[i]
									.getVresult())) + ",";

			}

			// 去掉末尾的逗号
			if (psninfo.endsWith(","))
				psninfo = psninfo.substring(0, psninfo.length() - 1);
			psninfo += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"hr_trn_pub", "UPPhr_trn_pub-000063")/* @res "的人员离职!\n" */;

		}

		if (bl)
			return psninfo2;

		return psninfo;
	}

	/**
	 * @author chexz 取翻译
	 * @return
	 * @throws Exception
	 */
	public String getTranslateName(String pk, String value, String type)
			throws Exception {
		return TRNDelegator.getStapplybH().getTranslateName(pk, value, type);
	}

	public String nullFormalu(String str) {
		if (str == null || str.trim().equals(""))
			return " ";
		else
			return str;
	}

	/**
	 * 获得需要执行的申请单
	 * 
	 * @param pk_psndoc
	 * @return
	 */
	private StapplybHVO[] getSelectedBills(StapplybHVO[] billforsel,
			String[] billNOs) throws BusinessException {
		Vector<StapplybHVO> bills = new Vector<StapplybHVO>();
		// 执行选中的单据
		if (billNOs != null) {
			String s;
			for (StapplybHVO o : billforsel) {
				s = ((StapplybHHeaderVO) o.getParentVO()).getVbillno();
				if (ArrayUtils.isExistInArray(billNOs, s)) {
					bills.add(o);
				}
			}
		} else {
			return null;
		}
		return bills.toArray(new StapplybHVO[0]);
	}

	/**
	 * 获取调出单据
	 * 
	 * @param bills
	 * @return
	 */
	private StapplybHVO[] filterStafOutBills(StapplybHVO[] bills) {
		if (bills == null || bills.length < 1)
			return null;
		Vector<StapplybHVO> vtemp = new Vector<StapplybHVO>();
		StapplybHHeaderVO header = null;
		for (StapplybHVO o : bills) {
			header = (StapplybHHeaderVO) o.getParentVO();
			if (header.getPk_currcorp()
					.equalsIgnoreCase(header.getPk_aimcorp())) {
				continue;
			}
			vtemp.add(o);
		}
		return vtemp.toArray(new StapplybHVO[0]);
	}

	/**
	 * 检查是否有生效合同存在
	 * 
	 * @param applyBills
	 * @param bl
	 * @return
	 * @throws Exception
	 */
	private boolean checkUpContract(StapplybHVO[] checkvos) throws Exception {
		if (!getDataMdl().getInstalledModule().get("HRCM").booleanValue())
			return true;
		if (checkvos == null || checkvos.length < 1)
			return true;
		try {
			IHrcmPsnChanged iHrcmPsnChanged = (IHrcmPsnChanged) NCLocator
					.getInstance().lookup(IHrcmPsnChanged.class.getName());
			String[] nameArr = null;
			String[] persons = new String[0];
			Map<String, Vector<String>> corpPsnHash = new HashMap<String, Vector<String>>();
			for (int i = 0; i < checkvos.length; i++) {
				StapplybHHeaderVO headVo = (StapplybHHeaderVO) checkvos[i]
						.getParentVO();
				Vector<String> vapprovedpsn = filterApprovedPerson(checkvos[i]);
				if (!corpPsnHash.containsKey(headVo.getPk_currcorp()))
					corpPsnHash.put(headVo.getPk_currcorp(),
							new Vector<String>());
				corpPsnHash.get(headVo.getPk_currcorp()).addAll(vapprovedpsn);
			}
			nameArr = iHrcmPsnChanged.queryContForPsn(corpPsnHash,
					IHrcmPsnChanged.NOT_FINISH_EFFECT, false);
			if (!org.apache.commons.lang.ArrayUtils.isEmpty(nameArr)) {
				persons = (String[]) org.apache.commons.lang.ArrayUtils.addAll(
						persons, nameArr);
			}
			String message = "";
			String sliper = ",";
			if (persons.length > 0) {
				for (String name : persons) {
					message += name + sliper;
				}
				message = MessageFormat.format(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("60090713",
								"UPP60090713-000213")/*
														 * @res
														 * "下列人员有未生效或已生效但尚未结束的合同/协议,是否继续执行?"
														 */
						+ "\n" + "{0}", message.substring(0, message
						.lastIndexOf(sliper)));
				if (getParentUI().showYesNoMessage(message) == UIDialog.ID_NO) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 检查是否有相同人员存在于多个单据。 创建日期：(2004-08-30 下午 09:44:31)
	 * 
	 * @return boolean
	 * @param vos
	 *            StapplybHVO[]
	 */
	private boolean validatePsnInMultiBill() throws Exception {
		StapplybHVO[] vos = toPerformBills;

		if (vos.length < 2)
			return true;
		String psnname = "";
		String[] currentPKs = null;
		String[] nextPKs = null;
		List<String> intersection = null;
		List<String> ls1 = null;
		List<String> ls2 = null;

		int i = 0;
		for (; i < vos.length; i++) {
			currentPKs = vos[i].getPk_psndocs();
			if (i == vos.length - 1)
				break;
			nextPKs = vos[i + 1].getPk_psndocs();
			ls1 = new ArrayList<String>();
			ls2 = new ArrayList<String>();
			Collections.addAll(ls1, currentPKs);
			Collections.addAll(ls2, nextPKs);
			// 取交集
			intersection = ListUtils.intersection(ls1, ls2);
			if (intersection != null && intersection.size() > 0) {
				break;
			}
		}
		String msg1 = ResHelper.getString("60090713", "UPP60090713-000062");/*
																			 * @res
																			 * "人员 '"
																			 */
		String msg2 = ResHelper.getString("60090713", "UPP60090713-000080");/*
																			 * @res
																			 * "':
																			 * 单据"
																			 */
		String prop = ResHelper.getString("60090713", "UPP60090713-000081");/*
																			 * @res
																			 * "、"
																			 */
		String msg3 = ResHelper.getString("60090713", "UPP60090713-000082");/*
																			 * @res "
																			 * 中都存在，请单张执行!"
																			 */

		if (intersection != null && intersection.size() > 0) {
			for (String s : intersection.toArray(new String[0])) {
				StapplybHItemVO[] itemVOs1 = vos[i].getItemVOsByPK(s);
				StapplybHItemVO[] itemVOs2 = vos[i + 1].getItemVOsByPK(s);
				if (isApproved(itemVOs1) && isApproved(itemVOs2)) {
					getParentUI()
							.showErrorMessage(
									msg1
											+ psnname
											+ msg2
											+ ((StapplybHHeaderVO) vos[i]
													.getParentVO())
													.getVbillno()
											+ prop
											+ ((StapplybHHeaderVO) vos[i + 1]
													.getParentVO())
													.getVbillno() + msg3);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 校验任职记录开始日期是否为空 校验调配/离职日期与有效工作履历开始日期之前的关系
	 * 
	 * @param pkpsndocs
	 */
	private boolean checkDateForSub(PerfromPropVO prop)
			throws BusinessException {
		Map<UFDate, String[]> datepsnhash = new HashMap<UFDate, String[]>();
		if (CommonValue.TURNOVER__RECORDS.equals(getFrameUI().getModuleCode())) {
			datepsnhash.put(prop.getEffectdate(), filterApprovedPerson(
					toPerformBills).toArray(new String[0]));
		} else {
			if (toPerformBillsHash.containsKey(RDS_CROSS_IN)) {
				UFDate date = prop.isIfSyncDate() ? prop.getEffectdate()
						: nc.ui.hr.global.Global.getLogDate();
				datepsnhash.put(date, filterApprovedPerson(
						toPerformBillsHash.get(RDS_CROSS_IN)).toArray(
						new String[0]));
			} else if (prop.isInPhase()) {
				StapplybHVO[] temp = (StapplybHVO[]) org.apache.commons.lang.ArrayUtils
						.addAll(toPerformBillsHash.get(RDS_IN_CORP),
								toPerformBillsHash.get(RDS_CROSS_OUT));
				if (datepsnhash.containsKey(prop.getEffectdate())) {
					datepsnhash.put(prop.getEffectdate(),
							(String[]) org.apache.commons.lang.ArrayUtils
									.addAll(filterApprovedPerson(temp).toArray(
											new String[0]), datepsnhash
											.get(prop.getEffectdate())));
				} else {
					datepsnhash.put(prop.getEffectdate(), filterApprovedPerson(
							temp).toArray(new String[0]));
				}
			}
		}

		String names = TRNDelegator.getTrnPub().isValidDateForUpWork(
				datepsnhash, true);
		if (names != null && names.length() > 0) {
			String msg = "";
			if (getFrameUI().getModuleCode().equals(
					CommonValue.STAFFING_RECORDS)) {
				msg += ResHelper.getString("common", "UC000-0003801")/* "调配日期" */;
			} else {
				msg += ResHelper.getString("common", "UC000-0003061")/* "离职日期" */;
			}
			msg += ResHelper.getString("60090713", "UPP60090713-000200")/* "应晚于工作履历的开始日期,您要继续执行吗?" */;
			return (getFrameUI().showYesNoMessage(msg) == UIDialog.ID_YES);
		}
		return true;
	}

	/**
	 * 校验人员编码是否重复
	 * 
	 * @param psndocs
	 * @return
	 */
	private boolean checkRepeatPsncodes(String[] psndocs, Vector vPsncodes,
			Hashtable<String, String> hashPsncode,
			GeneNewPsncodesDlg geneNewPsncodesDlg) throws BusinessException {
		HRMainVO[] psnDataVOs = null;
		try {
			psnDataVOs = TRNDelegator.getStapplybH().isExitRepeatedPsncode(
					psndocs, getDataMdl().getPk_corp());
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes()
					.getStrByID("60090713", "UPP60090713-000214")/*
																	 * @res
																	 * "校验人员编码是否重复出错!"
																	 */);
		}

		String strReturn = null;
		if (psnDataVOs != null) {
			HRMainVO[] newPsnDataVOs = null;
			geneNewPsncodesDlg = new GeneNewPsncodesDlg(getParentUI(),
					psnDataVOs, vPsncodes);
			geneNewPsncodesDlg.showModal();
			if (geneNewPsncodesDlg.getResult() == UIDialog.ID_CANCEL)
				return false;
			else {
				newPsnDataVOs = geneNewPsncodesDlg.getBillDatas();
				// 人员编码有用户输入时需要检查编码是否有效
				if (!geneNewPsncodesDlg.isAutoPsncode()) {
					// 校验用户输入的编码是否有重复的
					try {
						strReturn = TRNDelegator.getTrnPub().existPsncode(
								newPsnDataVOs, getDataMdl().getPk_corp());
					} catch (Exception e) {
						Logger.error(e.getMessage());
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("60090713",
										"UPP60090713-000215")/*
																 * @res
																 * "查询人员编码是否重复出错!"
																 */);
					}
					while (strReturn != null) {
						// 显示出错信息
						strReturn = strReturn
								+ ResHelper.getString("60090713",
										"UPP60090713-000169")/*
																 * @res
																 * "的新公司人员编码已经存在，请重新输入！"
																 */;
						getParentUI().showWarningMessage(strReturn);
						geneNewPsncodesDlg = new GeneNewPsncodesDlg(
								getParentUI(), psnDataVOs, vPsncodes);
						geneNewPsncodesDlg.showModal();
						if (geneNewPsncodesDlg.getResult() == UIDialog.ID_CANCEL)
							return false;
						else
							newPsnDataVOs = geneNewPsncodesDlg.getBillDatas();
						try {
							strReturn = TRNDelegator.getTrnPub().existPsncode(
									newPsnDataVOs, getDataMdl().getPk_corp());
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new BusinessException(
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("60090713",
													"UPP60090713-000215")/*
																			 * @res
																			 * "查询人员编码是否重复出错!"
																			 */);
						}
					}
				}
			}
			// 获得新公司人员编码
			hashPsncode = getHashPsncode(newPsnDataVOs);
			return true;
		}
		return true;
	}

	/**
	 * @author chexz
	 * @param bodyVOs
	 * @return
	 * @throws BusinessException
	 */
	protected static HRAggVO filterSingleTable(HRAggVO bodyVOs,
			Map<String, String> resultLs) throws BusinessException {
		if (bodyVOs == null || bodyVOs.equals("")) {
			return null;
		}
		HRAggVO itemVOs = bodyVOs;
		StapplybBVO[] bodys = (StapplybBVO[]) bodyVOs.getChildrenVO();
		String s = null;
		for (StapplybBVO body : bodys) {
			if (resultLs.containsKey(s = body.getPk_psndoc())) {
				body.setPk_psndoc(resultLs.get(s));
			}
		}
		itemVOs.setChildrenVO(null);
		itemVOs.setChildrenVO(bodys);
		return itemVOs;
	}

	/**
	 * 查询值。 创建日期：(2005-1-18 14:36:28)
	 * 
	 * @return java.lang.String
	 */
	public String getValueBy(StapplybHItemVO[] itemVOs, String itemname) {
		if (itemVOs == null || itemVOs.length == 0)
			return null;
		String value = null;
		for (int i = 0, n = itemVOs.length; i < n; i++) {
			if (itemVOs[i].getPk_stitem().equals(itemname)
					&& itemVOs[i].getVresult() != null) {
				value = itemVOs[i].getVresult().toString();
				break;
			}
		}
		return value;
	}

	/**
	 * 超编检查
	 * 
	 * 创建日期：(2002-6-7 12:23:10)
	 * 
	 * @return boolean
	 * @param StapplybHVO[]
	 */
	public boolean checkifExceedWorkout(StapplybHVO[] applyBills,
			boolean isCrossCorp) throws BusinessException {
		// 申请单中在职人员
		Hashtable<String, StapplybHItemVO[]> hashChildWorkvos = new Hashtable<String, StapplybHItemVO[]>();
		if (applyBills != null && applyBills.length > 0) {
			for (int i = 0; i < applyBills.length; i++) {
				String[] pk_psndocs = applyBills[i].getPk_psndocs();
				if (pk_psndocs == null || pk_psndocs.length < 1)
					break;
				for (int j = 0; j < pk_psndocs.length; j++) {
					StapplybHItemVO[] itemVOs = applyBills[i]
							.getItemVOsByPK(pk_psndocs[j]);
					if (isApproved(itemVOs)) {
						hashChildWorkvos.put(pk_psndocs[j], itemVOs);
					}
				}
			}
		}
		// 对在职人员检查是否超编
		return isExceedWorkout(hashChildWorkvos, isCrossCorp);

	}

	/**
	 * 是否超编
	 * 
	 * @return
	 */
	private boolean isExceedWorkout(Hashtable hashChildvos, boolean isCrossCorp)
			throws BusinessException {
		// 如果子表中没有值就不必在判断
		if (hashChildvos != null
				&& (hashChildvos.isEmpty() || hashChildvos.size() < 1))
			return false;

		boolean isExceedWorkout = false;
		// 1校验公司编制
		String msg = "";
		// 当前校验行
		GeneralVO[] oldPsnVOs;
		if (isCrossCorp)
			oldPsnVOs = getWorkoutDataForOld(hashChildvos);
		else
			oldPsnVOs = getWorkoutDataForOld(hashChildvos);
		// 获得新数据
		GeneralVO[] newPsnVOs = getWorkoutDataForNew(hashChildvos);

		// 检查是否超编
		WorkoutResultVO resultVO = ((ICorpWorkout) NCLocator.getInstance()
				.lookup(ICorpWorkout.class.getName())).checkWorkout(
				getDataMdl().getPk_corp(), Global.getUserID(), isCrossCorp,
				Global.getLogYear(), oldPsnVOs, newPsnVOs);
		if (!resultVO.isSyncLock()) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("hr_trn_pub",
							"UPPhr_trn_pub-000021")/* @res "编制管理正有其他用户操作，请稍后再试" */);
		} else {
			if (resultVO.isPassTest()) {
				if (StringUtils.isNotBlank(resultVO.getMessage())) {
					getParentUI().showWarningMessage(
							resultVO.getMessage().trim() + "\n");
				}
			} else {// 超编且强制不通过
				throw new BusinessException(resultVO.getMessage());
			}
		}
		return isExceedWorkout;
	}

	/**
	 * 检查人员在申请单中是否同意。
	 */
	private boolean isApproved(StapplybHItemVO[] itemVOs) {
		// 检查人员在申请单中是否同意
		boolean bapprove = true;
		if (itemVOs == null)
			return false;
		for (int k = 0, n = itemVOs.length; k < n; k++) {
			if ("bapprove".equals(itemVOs[k].getPk_stitem())) {
				if (!UFBoolean.valueOf(itemVOs[k].getVresult().trim())
						.booleanValue()
						|| itemVOs[k].getVresult().equals("N"))
					bapprove = false;
				break;
			}
		}
		return bapprove;
	}

	/**
	 * 检查当前人员是否已经加入黑名单
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	public void isExistPsndocBads(String[] psndocs) throws Exception {

		String strReturn = TRNDelegator.getStapplybH().isExistPsndocBads(
				psndocs);
		if (!StringUtils.isBlank(strReturn)) {
			String msg = ResHelper.getString("60090713", "UPP60090713-000161")/*
																				 * @res
																				 * "待执行单据中有人员："
																				 */

					+ " " + strReturn + " "
					+ ResHelper.getString("60090713", "UPP60090713-000162");/*
																			 * @res
																			 * "存在于黑名单中，这些人员不能执行调配,您要继续执行吗？"
																			 */
			throw new ValidationException(msg);
		}
	}

	private Vector<String> filterApprovedPerson(StapplybHVO... os)
			throws BusinessException {
		Vector<String> pks = new Vector<String>();
		if (os == null || os.length < 1) {
			return pks;
		}
		for (StapplybHVO o : os) {
			if (org.apache.commons.lang.ArrayUtils.isEmpty(o.getPk_psndocs())) {
				throw new BusinessException(MessageFormat.format(
						"单据{0}中人员的工作信息已被删除，不能执行!", o.getParentVO()
								.getAttributeValue("vbillno").toString()));
			}
			for (String s : o.getPk_psndocs()) {
				if (isApproved(o.getItemVOsByPK(s))) {
					pks.add(s);
				}
			}
		}
		return pks;
	}

	public Map<String, Integer> getHashPsnclScope() {
		if (hashPsnclScope.size() == 0) {
			try {
				IPsncl publicPsncl = (IPsncl) NCLocator.getInstance().lookup(
						IPsncl.class.getName());
				PsnclVO[] psnclVOs = publicPsncl.queryAllPsnclVOs(corpPK);
				if (psnclVOs != null && psnclVOs.length > 0) {
					for (int i = 0; i < psnclVOs.length; i++) {
						hashPsnclScope.put(psnclVOs[i].getPk_psncl(),
								psnclVOs[i].getPsnclscope());
					}
				}
			} catch (Exception e) {
				getParentUI().handleException(e);
			}
		}

		return hashPsnclScope;
	}

	public boolean isAutoGeneratedWaBill() throws Exception {
		if (getDataMdl().getInstalledModule().get("WA").booleanValue()) {
			// 取系统参数：
			if (getParentUI().getModuleCode().equals(
					CommonValue.STAFFING_RECORDS)) {
				return ifGeneWABill(getDataMdl().getPk_corp(), "HRSMWA01");
			}
		}
		return false;
	}

	/**
	 * 是否生成定调级申请单。 创建日期：(2003-6-22 20:15:54)
	 */

	public boolean ifGeneWABill(String pk_corp, String paraCode)
			throws Exception {
		IParValue parValue = (IParValue) NCLocator.getInstance().lookup(
				IParValue.class.getName());
		nc.vo.pub.lang.UFBoolean ifGeneWaBill = parValue.getParaBoolean(
				pk_corp, paraCode);
		if (ifGeneWaBill == null) {
			return false;
		} else {
			return ifGeneWaBill.booleanValue();
		}
	}

	/**
	 * 获得新公司人员编码
	 */
	private Hashtable<String, String> getHashPsncode(HRMainVO[] psnDataVOs) {
		if (psnDataVOs != null) {
			Hashtable<String, String> hashPsncode = new Hashtable<String, String>();
			for (int i = 0, len = psnDataVOs.length; i < len; i++) {
				hashPsncode.put(psnDataVOs[i].getAttributeValue("pk_psndoc")
						.toString(), psnDataVOs[i].getAttributeValue(
						"newpsncode").toString());
			}
			return hashPsncode;
		} else
			return null;
	}

	/**
	 * 得到需要编制判断的数据 V53 add
	 * 
	 * @param row
	 * @return
	 */
	private GeneralVO[] getWorkoutDataForOld(Hashtable hashChildvos) {
		Enumeration enumChilds = hashChildvos.elements();
		GeneralVO[] oldPsnVOs = null;
		Vector<GeneralVO> vGeneralVO = new Vector<GeneralVO>();
		while (enumChilds.hasMoreElements()) {
			StapplybHItemVO[] itemVOs = (StapplybHItemVO[]) enumChilds
					.nextElement();
			GeneralVO oldPsnVO = new GeneralVO();
			// 人员主键
			oldPsnVO.setAttributeValue("pk_psndoc", itemVOs[0].getPk_psndoc());
			// 人员类别
			oldPsnVO.setAttributeValue("pk_psncl", getValueBy(itemVOs,
					"oldpk_psncl"));
			// 人员部门
			oldPsnVO.setAttributeValue("pk_deptdoc", getValueBy(itemVOs,
					"oldpk_deptdoc"));
			// 人员岗位
			if (getValueBy(itemVOs, "oldpk_om_job") != null) {
				oldPsnVO.setAttributeValue("pk_om_job", getValueBy(itemVOs,
						"oldpk_om_job"));
			} else {
				oldPsnVO.setAttributeValue("pk_om_job",
						ICorpWorkout.GENERAL_NULLVALUE);
			}

			vGeneralVO.add(oldPsnVO);
		}
		if (vGeneralVO.size() > 0) {
			oldPsnVOs = new GeneralVO[vGeneralVO.size()];
			vGeneralVO.copyInto(oldPsnVOs);
		}
		return oldPsnVOs;
	}

	/**
	 * 得到需要编制判断的数据 V53 add
	 * 
	 * @param row
	 * @return
	 */
	private GeneralVO[] getWorkoutDataForNew(Hashtable hashChildvos) {
		Enumeration enumChilds = hashChildvos.elements();
		GeneralVO[] newPsnVOs = null;
		Vector<GeneralVO> vGeneralVO = new Vector<GeneralVO>();
		while (enumChilds.hasMoreElements()) {
			StapplybHItemVO[] itemVOs = (StapplybHItemVO[]) enumChilds
					.nextElement();
			GeneralVO newPsnVO = new GeneralVO();
			// 人员岗位
			for (int i = 0, n = itemVOs.length; i < n; i++) {
				if (itemVOs[i].getPk_stitem().toLowerCase().startsWith("new")) {
					if (itemVOs[i].getVresult() != null) {
						newPsnVO.setAttributeValue(itemVOs[i].getPk_stitem()
								.substring(3,
										itemVOs[i].getPk_stitem().length()),
								itemVOs[i].getVresult());
					} else {
						newPsnVO.setAttributeValue(itemVOs[i].getPk_stitem()
								.substring(3,
										itemVOs[i].getPk_stitem().length()),
								ICorpWorkout.GENERAL_NULLVALUE);
					}
				}
			}
			vGeneralVO.add(newPsnVO);

		}
		if (vGeneralVO.size() > 0) {
			newPsnVOs = new GeneralVO[vGeneralVO.size()];
			vGeneralVO.copyInto(newPsnVOs);
		}
		return newPsnVOs;
	}

	@Override
	public boolean validate() throws ValidationException {
		billVOS = getDataMdl().getBillInfoVOS();
		int queryType = getDataMdl().getQueryType();

		if (org.apache.commons.lang.ArrayUtils.isEmpty(billVOS)
				|| queryType == CommonValue.PERFORM_DEALED) {
			throw new ValidationException(ResHelper.getString("60090713",
					"UPP60090713-000059")/*
											 * @res "无满足条件的申请单可以执行!"
											 */);
		}
		DefaultMutableTreeNode node = getDataMdl().getSelectedNode();
		if (node.isLeaf()) {
			billVOS = new StapplybHVO[] { (StapplybHVO) getDataMdl()
					.getSelectedNode().getUserObject() };
		}
		return true;
	}

	private boolean performStaffing(PerfromPropVO prop) throws Exception {

		Vector<String> vperformbillPKs = new Vector<String>();
		Hashtable<String, String> hashPsncode = new Hashtable<String, String>();
		// 公司内调配检查是否超编
		checkifExceedWorkout(toPerformBillsHash.get(RDS_IN_CORP), false);
		// 调入执行检查是否超编
		checkifExceedWorkout(toPerformBillsHash.get(RDS_CROSS_IN), true);
		// 校验执行日期与有效工作履历开始日期之前的关系
		if (!checkDateForSub(prop)) {
			return false;
		}
		// 调入检查人员编码是否重复
		GeneNewPsncodesDlg geneNewPsncodesDlg = null;
		if (toPerformBillsHash.containsKey(RDS_CROSS_IN)
				&& !checkRepeatPsncodes(geneNewPsncodesDlg, toPerformBillsHash
						.get(RDS_CROSS_IN), hashPsncode)) {
			return false;
		}
		for (StapplybHVO o : toPerformBills) {
			vperformbillPKs.add(o.getParentVO().getPrimaryKey());
		}
		try {
			// 记录执行，更新薪资信息
			Map<String, String> resultLs = TRNDelegator.getStapplybH()
					.perfromStaff(getDataMdl().getPk_corp(),
							nc.ui.hr.global.Global.getLogDate(),
							vperformbillPKs.toArray(new String[0]),
							hashPsncode, prop,
							GlobalTool.getFuncParserWithoutWa());
			// 推薪资单据
			pushWa(resultLs);
			// 发送通知
			if (prop.isSendMail()) {
				sendMessages(CommonValue.PROJECT_TYPE_STAFFING, resultLs, prop
						.getEffectdate());
			}
			// 打开定调资单据
			openLinkWA(resultLs);
		} catch (Exception e) {
			if (geneNewPsncodesDlg != null)
				geneNewPsncodesDlg.returnBillcode();
			Logger.error(e.getMessage(), e);
			throw e;
		}
		return true;
	}

	/**
	 * 推定调资单据 弹出窗口
	 * 
	 * @param resultLs
	 * @throws Exception
	 */
	private void openLinkWA(Map<String, String> resultLs) throws Exception {
		if (resultLs.isEmpty())
			return;
		if (isAutoGeneratedWaBill()) {
			autoGeneratedWABill(resultLs);
		}
	}

	/**
	 * 发送通知
	 * 
	 * @param type
	 * @param userdate
	 */
	private void sendMessages(String type, Map<String, String> performRs,
			UFDate userdate) {
		if (performRs == null || performRs.isEmpty()) {
			getParentUI().showHintMessage(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"hr_trn_pub", "UPPhr_trn_pub-000061")/*
																	 * @res
																	 * "没有同意的人员,不需要发送通知!"
																	 */);
			return;
		}
		MailMessageRecieverSelectDlg userSelectDlg = new nc.ui.hrsm.hrsm_301.action.MailMessageRecieverSelectDlg(
				getParentUI(), Global.getCorpPK(), getFrameUI().getModuleCode());
		fendMailToUser(userSelectDlg, performRs, type, userdate);
	}

	/**
	 * 推薪资单据，参数为需要推薪资单据的新旧人员主键map
	 * 
	 * @param resultLs
	 */
	private void pushWa(Map<String, String> resultLs) throws BusinessException {
		if (resultLs.isEmpty()
				|| !getDataMdl().getInstalledModule().get("WA").booleanValue())
			return;
		StapplybHHeaderVO head = null;
		Map<String, String> billWhereHash = new HashMap<String, String>();
		String sql = null;
		String dis = "','";
		for (StapplybHVO o : toPerformBills) {
			sql = " hi_stapplyb_b.pk_psndoc in ('";
			head = (StapplybHHeaderVO) o.getParentVO();
			for (String s : resultLs.keySet()) {
				if (ArrayUtils.isExistInArray(o.getPk_psndocs(), s)) {
					sql += s + dis;
				}
			}
			if (sql.indexOf(dis) > 0) {
				sql = sql.subSequence(0, sql.lastIndexOf(",")) + ")";
				billWhereHash.put(head.getPrimaryKey(), sql);
			}
		}
		try {
			HRAggVO[] aggvos = TRNDelegator.getStapplybH()
					.queryPersitenceDelegatorHeadByBodyVOs(billWhereHash);
			for (HRAggVO o : aggvos) {
				o = filterSingleTable(o, resultLs);
			}
			List<PFAggVO> pfaggvos = new ArrayList<PFAggVO>();
			for (HRAggVO o : aggvos) {
				pfaggvos.add(new PFAggVO(o, new StApplyPFConfig(
						CommonValue.PROJECT_TYPE_STAFFING)));
			}
			HashMap<String, String> hashPara = new HashMap<String, String>();
			hashPara.put(PfUtilBaseTools.PARAM_NOAPPROVE,
					PfUtilBaseTools.PARAM_NOAPPROVE);
			PfUtilClient.runBatch(null, "PUSH" + Global.getUserID(),
					CommonValue.PROJECT_TYPE_STAFFING, Global.getLogDate()
							.toString(), pfaggvos.toArray(new PFAggVO[0]),
					null, null, null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes()
					.getStrByID("60090713", "UPP60090713-000216")/*
																	 * @res
																	 * "推定调资单据出错!"
																	 */);
		}

	}

	/**
	 * 调入检查人员编码是否重复，获取新人员编码
	 * 
	 * @param geneNewPsncodesDlg
	 * @param stapplybHVOs
	 * @param hashPsncode
	 */
	private boolean checkRepeatPsncodes(GeneNewPsncodesDlg geneNewPsncodesDlg,
			StapplybHVO[] stapplybHVOs, Hashtable<String, String> hashPsncode)
			throws BusinessException {
		Vector<String> vPsndocs = new Vector<String>();
		Vector<String> vPsncodes = new Vector<String>();
		for (StapplybHVO o : toPerformBillsHash.get(RDS_CROSS_IN)) {
			// 审批通过人员
			String[] appedpsndocs = filterApprovedPerson(o).toArray(
					new String[0]);
			if (appedpsndocs == null || appedpsndocs.length < 1)
				break;
			for (String s : appedpsndocs) {
				StapplybHItemVO[] itemVOs = o.getItemVOsByPK(s);
				vPsndocs.addElement(s);
				vPsncodes.addElement(getValueBy(itemVOs, "psncode"));
			}
		}
		try {
			if (!vPsndocs.isEmpty()
					&& !checkRepeatPsncodes(vPsndocs.toArray(new String[0]),
							vPsncodes, hashPsncode, geneNewPsncodesDlg)) {
				return false;
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
		return true;

	}

	private boolean performTurnOver(PerfromPropVO prop) throws Exception {
		Vector<String> vperformbillPKs = new Vector<String>();
		// 离职校验
		// 加入执行离职时人员对当前任职记录的开始时间的效验
		// checkDimissionDate(vperformbillPKs.toArray(new
		// String[0]),prop.getEffectdate());
		// 校验任职记录开始日期是否为空
		// 校验调配/离职日期与有效工作履历开始日期之前的关系
		// 校验离职日期与有效工作履历开始日期之前的关系
		if (!checkDateForSub(prop)) {
			return false;
		}
		// 离职执行
		for (StapplybHVO o : toPerformBills) {
			vperformbillPKs.add(o.getParentVO().getPrimaryKey());
		}
		// 是否增加到黑名单
		boolean bAddToBlackNames = prop.isAddInBlack();
		String cause = prop.getAddreason();
		Map<String, String> resultLs = TRNDelegator
				.getStapplybH()
				.performTurnOver(nc.ui.hr.global.Global.getCorpPK(),
						nc.ui.hr.global.Global.getLogDate(),
						prop.getEffectdate(), new Boolean(prop.isIfSyncDate()),
						new Boolean(bAddToBlackNames),
						vperformbillPKs.toArray(new String[0]), cause,
						Global.getUserID(), GlobalTool.getFuncParserWithoutWa());

		if (prop.isSendMail()) {
			sendMessages(CommonValue.PROJECT_TYPE_TURNOVER, resultLs, prop
					.getEffectdate());
		}

		return true;
	}

}