/*
 * 创建日期 2006-1-18
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bd.deptdoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDException;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.b04.DeptOpEvent;
import nc.ui.bd.b04.DeptOpListner;
import nc.ui.bd.b04.DeptdocBO_Client;
import nc.ui.bd.ddreader.DataReaderConstEnumFactoryAdapter;
import nc.ui.bd.ddreader.QzsmIntToIntDataTypeConvert;
import nc.ui.bd.ddreader.QzsmIntToStrDataTypeConvert;
import nc.ui.bd.def.ListDefShowUtil;
import nc.ui.bd.pub.BDDocManageDlg;
import nc.ui.bd.pub.BillExportUtil;
import nc.ui.bd.util.CopyDlg;
import nc.ui.bd.util.ICopy;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.DefaultCheckIsDef;
import nc.ui.trade.pub.DefaultGetDefCodeOrName;
import nc.ui.trade.pub.IGetDefCodeOrName;
import nc.ui.trade.pub.PrtDefDealedDecorator;
import nc.ui.trade.pub.SingleListHeadPRTS;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.bd.service.InterfaceexecVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.filemanage.BDAssociateFileUtil;
import nc.vo.pub.filemanage.IBDFileManageConst;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.util.tree.IOPolicy;
import nc.vo.util.tree.MethodGroup;
import nc.vo.util.tree.TreeDetail;

/**
 * @author xuchao
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DeptDocEHD extends TreeCardEventHandler implements ICopy {
	private final int treeCardType = 0;

	private final int listType = 1;

	private int panelState = treeCardType;// 树卡

	private BillListPanelWrapper listPanel = null;

	private boolean isHrUse = false;

	protected DeptOpListner m_HROpListner = null; // HR事件处理监听器

	private CopyDlg copyDlg;

	private ButtonVO[] hrFatherVO = null;

	private boolean isDefInitialed = false;

	public DeptDocEHD(BillTreeCardUI billUI, ICardController control,
			boolean isHrUse, ButtonVO[] hrFatherVO) {
		super(billUI, control);
		this.isHrUse = isHrUse;
		this.hrFatherVO = hrFatherVO;
	}

	private List<nc.vo.yto.business.DeptdocVO> deptdocAdd = new ArrayList<nc.vo.yto.business.DeptdocVO>();

	private List<nc.vo.yto.business.DeptdocVO> deptdocAdd2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();

	private List<nc.vo.yto.business.DeptdocVO> deptdocUpdate = new ArrayList<nc.vo.yto.business.DeptdocVO>();

	private List<nc.vo.yto.business.DeptdocVO> deptdocUpdate2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();

	// private Thread th1 = null;
	// private Thread th2 = null;

	protected void onBoSave() throws Exception {
		IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
		IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
	
		
		this.getBillCardPanelWrapper().getBillCardPanel().getBillData()
				.dataNotNullValidate();
		AggregatedValueObject aggVO = getBillCardPanelWrapper()
				.getBillVOFromUI();
		if (aggVO == null)
			return;
		DeptdocVO headvo = (DeptdocVO) aggVO.getParentVO();
		if (headvo == null)
			return;

		boolean isSave = checkBeforeSave(headvo);

		if (isSave == false)
			return;

		// 部门档案修改或新增时同步至中间表 add by river for 2011-09-13
		IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(
				IReadmsg.class.getName());
		boolean check = msg.checkExist(nc.vo.yto.business.DeptdocVO.class,
				" pk_deptdoc = '" + headvo.getPk_deptdoc() + "'");
		if (check) {

			super.onBoSave();

			onBoRefresh();
			setSelectionPath(headvo);
			getDeptdocUI().getShowSealdataCbx().setEnabled(true);
			nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[]) msg
					.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class,
							" deptcode = '"
									+ headvo.getAttributeValue("deptcode")
									+ "' and pk_corp = '"+headvo.getAttributeValue("pk_corp")+"'"))[0];

				
			String retStr = filepost.postFile(Uriread
					.uriPath(), gener.generateXml3(deptdocvo, "RequestDeptdoc",
					"dept", "add"));

			String[] strs = retStr.split("<success>");
			String retMsg = "";
			if (strs.length > 1)
				retMsg = strs[1].substring(0, strs[1].indexOf("<"));

			if (retMsg.equals("false") || strs.length <= 1) {
				deptdocAdd.add(deptdocvo);

				// if(th1 != null) {
				// th1.stop();
				// th1 = null;
				// }

				// th1 =
				Thread th1 = new Thread() {
					public void run() {
						
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
						
						try {
							if (true) {
								this.sleep(SleepTime.Time);

								for (nc.vo.yto.business.DeptdocVO dept : deptdocAdd) {
									String retStr = filepost
											.postFile(Uriread.uriPath(), gener.generateXml3(dept,
															"RequestDeptdoc",
															"dept", "update"));

									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1]
												.indexOf("<"));

									if (retMsg.equals("false")
											|| strs.length <= 1)
										deptdocAdd2.add(dept);
								}

								deptdocAdd = deptdocAdd2;

								deptdocAdd2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();

//								if (deptdocAdd.size() == 0)
//									break;

							}

							System.out.println("<<<<<<  部门档案新增线程停止！ >>>>>>");
							System.out.println("<<<<<<  线程号: " + this.getId()
									+ " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};

				th1.start();
			}

			return;
		} else {
			super.onBoSave();

			onBoRefresh();
			setSelectionPath(headvo);
			getDeptdocUI().getShowSealdataCbx().setEnabled(true);

			// IReadmsg msg = (IReadmsg)
			// NCLocator.getInstance().lookup(IReadmsg.class.getName());
			nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[]) msg
					.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class,
							" pk_deptdoc = '"
									+ headvo.getAttributeValue("pk_deptdoc")
									+ "' and pk_corp = '"+headvo.getAttributeValue("pk_corp")+"'"))[0];

			String retStr = filepost.postFile(Uriread
					.uriPath(), gener.generateXml3(deptdocvo, "RequestDeptdoc",
					"dept", "update"));

			String[] strs = retStr.split("<success>");
			String retMsg = "";
			if (strs.length > 1)
				retMsg = strs[1].substring(0, strs[1].indexOf("<"));

			if (retMsg.equals("false") || strs.length <= 1) {
				deptdocUpdate.add(deptdocvo);
				Thread th2 = new Thread() {
					public void run() {
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						try {
							if (true) {
								this.sleep(SleepTime.Time);
								
								for (nc.vo.yto.business.DeptdocVO dept : deptdocUpdate) {
									String retStr = filepost
											.postFile(Uriread.uriPath(), gener
													.generateXml3(dept,
															"RequestDeptdoc",
															"dept", "add"));

									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1]
												.indexOf("<"));

									if (retMsg.equals("false")
											|| strs.length <= 1)
										deptdocUpdate2.add(dept);

								}

								deptdocUpdate = deptdocUpdate2;

								deptdocAdd2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();

								if (deptdocUpdate.size() == 0
										|| deptdocUpdate == null) {
									deptdocUpdate = new ArrayList<nc.vo.yto.business.DeptdocVO>();

//									break;
								}
							}
							System.out.println("<<<<<<  部门档案修改线程停止！ >>>>>>");
							System.out.println("<<<<<<  线程号: " + this.getId()
									+ " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};

				th2.start();
			}

			return;
		}

		// super.onBoSave();
		//
		// onBoRefresh();
		// setSelectionPath(headvo);
		// getDeptdocUI().getShowSealdataCbx().setEnabled(true);

	}

	/**
	 * @param headvo
	 */
	@SuppressWarnings("unchecked")
	public void setSelectionPath(DeptdocVO headvo) {
		TreePath selectTreePath = null;
		DefaultTreeModel treeModel = (DefaultTreeModel) getDeptdocUI()
				.getUITree().getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();

		Enumeration en = root.preorderEnumeration();
		while (en.hasMoreElements()) {
			TableTreeNode treeNode = (TableTreeNode) en.nextElement();
			if ("root".equalsIgnoreCase(treeNode.getNodeID().toString().trim()))
				continue;
			VOTreeNode voTreeNode = (VOTreeNode) treeNode;
			CircularlyAccessibleValueObject deptdocVO = voTreeNode.getData();
			if (headvo.getDeptcode().equalsIgnoreCase(
					deptdocVO.getAttributeValue("deptcode").toString())) {
				TreeNode[] treeNodes = treeModel.getPathToRoot(voTreeNode);
				selectTreePath = new TreePath(treeNodes);
				break;
			}
		}

		if (selectTreePath != null) {
			getDeptdocUI().getUITree().expandPath(selectTreePath);
			getDeptdocUI().getUITree().setSelectionPath(selectTreePath);
		}
	}

	/**
	 * @throws Exception
	 */
	private boolean checkBeforeSave(DeptdocVO headVo) throws Exception {

		boolean returnflag = true;
		if (headVo.getPk_fathedept() != null
				&& headVo.getPk_fathedept().length() != 0) {
			DeptdocVO fatherDeptVo = getFatherDeptdocVO(headVo
					.getPk_fathedept());
			if (fatherDeptVo == null) {
				throw new BDException("上级部门不存在！");
			}
			if (fatherDeptVo.getCanceled() != null
					&& fatherDeptVo.getCanceled().booleanValue()) {
				showSealErroInfo();
				returnflag = false;
			}
		}
		setDefauletCancelDate(headVo);
		ICreateCorpQueryService iICreateCorpQueryService = (ICreateCorpQueryService) NCLocator
				.getInstance().lookup(ICreateCorpQueryService.class.getName());
		if (!iICreateCorpQueryService.isEnabled(headVo.getPk_corp(), "HRJF"))
			returnflag = deptCancelHint(headVo, returnflag);
		if (headVo.getDeptattr().equals("5")
				&& (headVo.getPk_calbody() == null || headVo.getPk_calbody()
						.equals(""))) {
			getBillUI().showErrorMessage(
					nc.vo.bd.MultiLangTrans.getTransStr("MC3",
							new String[] { nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10080404", "UC000-0001825") /*
																				 * @res
																				 * "库存组织"
																				 */})); // 库存组织不能为空
			returnflag = false;
		}

		if (headVo.getMemo() != null
				&& headVo.getMemo().getBytes().length > 100) {
			getBillUI().showErrorMessage(
					nc.vo.bd.MultiLangTrans.getTransStr("MC2", new String[] {
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080404", "UPP10080404-000017")/*
																		 * @res
																		 * "备注长度"
																		 */, "100" })); // 备注长度不能大于100
			returnflag = false;
		}
		if (headVo.getCreateDate() == null
				|| headVo.getCreateDate().toString().length() == 0) {
			getBillUI().showErrorMessage(
					nc.vo.bd.MultiLangTrans.getTransStr("MC3",
							new String[] { nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10080404",
											"UPP10080404-000018") /*
																	 * @res
																	 * "创建日期"
																	 */})); // 创建日期不能为空
			returnflag = false;
		}
		if (headVo.getPk_fathedept() != null
				&& headVo.getPk_fathedept().length() > 0) {
			DeptdocVO fatherVO = getFatherDeptdocVO(headVo.getPk_fathedept());
			if (fatherVO != null && fatherVO.getCreateDate() != null
					&& headVo.getCreateDate().before(fatherVO.getCreateDate())) {
				getBillUI().showErrorMessage(
						nc.vo.bd.MultiLangTrans.getTransStr("MC13",
								new String[] {
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("10080404",
														"UPP10080404-000019")/*
																				 * @res
																				 * "下级创建时间"
																				 */,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("10080404",
														"UPP10080404-000020") /*
																				 * @res
																				 * "上级创建时间"
																				 */})); // 下级创建时间不能早于上级创建时间
				returnflag = false;
			}
		}

		if (headVo.getPrimaryKey() == null
				|| headVo.getPrimaryKey().length() == 0) {
			if (headVo.getCanceled().booleanValue()) {
				getBillUI().showErrorMessage(
						nc.vo.bd.BDMsg.MSG_NEW_NOT_SEALED()/* 新增档案不能封存! */);
				return false;
			}
		} else {
			if (headVo.getPk_fathedept() != null
					&& headVo.getPk_fathedept().equals(headVo.getPrimaryKey())) {
				getBillUI().showErrorMessage(
						nc.vo.bd.MultiLangTrans.getTransStr("MP1",
								new String[] { nc.ui.ml.NCLangRes.getInstance()
										.getStrByID("10080404",
												"UPP10080404-000001") /*
																		 * @res
																		 * "将自身或下级设置为上级部门"
																		 */})); // 不能将自身或下级设置为上级部门
				returnflag = false;
			}
			DeptdocVO[] childs = getChildDeptdocVOs(headVo.getPrimaryKey());
			if (childs != null && childs.length != 0) {
				for (int i = 0; i < childs.length; i++) {
					// 若下级部门的创建日期为空，则不比较
					if (childs[i].getCreateDate() == null
							|| childs[i].getCreateDate().toString().trim()
									.length() == 0) {
						continue;
					}
					if (childs[i] != null
							&& headVo.getCreateDate() != null
							&& headVo.getCreateDate().after(
									childs[i].getCreateDate())) {
						getBillUI()
								.showErrorMessage(
										nc.vo.bd.MultiLangTrans
												.getTransStr(
														"MC13",
														new String[] {
																nc.ui.ml.NCLangRes
																		.getInstance()
																		.getStrByID(
																				"10080404",
																				"UPP10080404-000019")/*
																										 * @res
																										 * "下级创建时间"
																										 */,
																nc.ui.ml.NCLangRes
																		.getInstance()
																		.getStrByID(
																				"10080404",
																				"UPP10080404-000020") /*
																										 * @res
																										 * "上级创建时间"
																										 */})); // 下级创建时间不能早于上级创建时间
						returnflag = false;
					}
				}
			}
		}
		if (headVo.getCancelDate() != null
				&& headVo.getCreateDate().after(headVo.getCancelDate())) {
			getBillUI().showErrorMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10080404",
							"UPP10080404-000033"));// 部门封存时间不能早于部门创立时间
			return false;
		}
		return returnflag;
	}

	/**
	 * @param headVo
	 * @param returnflag
	 * @return
	 * @throws BusinessException
	 */
	private boolean deptCancelHint(DeptdocVO headVo, boolean returnflag)
			throws BusinessException {
		AggregatedValueObject aggBufVo = (AggregatedValueObject) getBillUI()
				.getBufferData().getCurrentVO();
		if (aggBufVo == null)
			return true;
		DeptdocVO deptBufVo = (DeptdocVO) aggBufVo.getParentVO();
		if (headVo.getPrimaryKey() != null
				&& headVo.getPrimaryKey().length() != 0
				&& existPsn(headVo.getPrimaryKey())
				&& (headVo.getCanceled() != null && headVo.getCanceled()
						.booleanValue() == true)
				&& (deptBufVo.getCanceled() == null || deptBufVo.getCanceled()
						.booleanValue() == false)) {
			int dialogReturnFlag = getBillUI().showYesNoMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10080404",
							"UPP10080404-000031"));// 该部门下存在人员档案，是否确定封存
			if (dialogReturnFlag == UIDialog.ID_NO)
				returnflag = false;

		}
		return returnflag;
	}

	/**
	 * @param headVo
	 */
	private void setDefauletCancelDate(DeptdocVO headVo) {
		if (headVo.getCanceled() == null
				|| !headVo.getCanceled().booleanValue())
			return;
		if (headVo.getCancelDate() == null
				|| headVo.getCancelDate().toString() == null
				|| headVo.getCancelDate().toString().length() == 0)
			headVo.setCancelDate(ClientEnvironment.getInstance().getDate());
	}

	public void onBoAdd(ButtonObject bo) throws Exception {
		getDeptdocUI().getShowSealdataCbx().setEnabled(false);
		DeptdocVO deptdocVO = getCurrSelectVO();
		if (deptdocVO != null && deptdocVO.getCanceled() != null
				&& deptdocVO.getCanceled().booleanValue()) {
			showSealErroInfo();
			return;
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("canceldate")
				.setEdit(false);
		super.onBoAdd(bo);
		if (deptdocVO == null)
			return;
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
				"pk_fathedept", deptdocVO.getPrimaryKey());
		if (deptdocVO.getDeptcode() != null
				&& deptdocVO.getDeptcode().length() != 0)
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
					"deptcode", deptdocVO.getDeptcode());

	}

	/**
	 * 
	 */
	private void showSealErroInfo() {
		getBillUI().showErrorMessage(
				nc.vo.bd.MultiLangTrans.getTransStr("MO4", new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10080404",
								"UPP10080404-000003")/*
														 * @res "封存"
														 */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10080404",
								"UC001-0000002") /*
													 * @res "增加"
													 */})); // 上级已封存,不能增加下级
	}

	/**
	 * @return
	 */

	private DeptdocVO getCurrSelectVO() {
		if (getBillTreeCardUI().getBillTreeSelectNode() == null)
			return null;
		DeptdocVO deptdocVO = (DeptdocVO) getBillTreeCardUI()
				.getBillTreeSelectNode().getData();
		return deptdocVO;
	}

	/**
	 * @param fatherPk
	 * @return
	 * @throws Exception
	 */
	private DeptdocVO getFatherDeptdocVO(String fatherPk) throws Exception {
		CircularlyAccessibleValueObject[] headVos = getBufferData()
				.getAllHeadVOsFromBuffer();
		if (headVos == null || headVos.length == 0 || fatherPk == null)
			return null;
		for (int i = 0; i < headVos.length; i++) {
			if (headVos[i].getPrimaryKey().equals(fatherPk)) {
				return (DeptdocVO) headVos[i];
			}
		}
		return null;
	}

	/**
	 * @param deptPk
	 * @return
	 * @throws Exception
	 */
	private DeptdocVO[] getChildDeptdocVOs(String deptPk) throws Exception {
		CircularlyAccessibleValueObject[] headVos = getBufferData()
				.getAllHeadVOsFromBuffer();
		ArrayList<DeptdocVO> childList = new ArrayList<DeptdocVO>();
		if (headVos == null || headVos.length == 0 || deptPk == null)
			return null;
		for (int i = 0; i < headVos.length; i++) {
			if (deptPk.equals(((DeptdocVO) headVos[i]).getPk_fathedept())) {
				childList.add((DeptdocVO) headVos[i]);
			}
		}
		return (DeptdocVO[]) childList.toArray(new DeptdocVO[0]);
	}

	protected void onBoEdit() throws Exception {
		if (getCurrSelectVO() == null)
			return;
		getDeptdocUI().getShowSealdataCbx().setEnabled(false);
		setCancelDateEditable();
		DeptdocVO faterVo = getFatherDeptdocVO(getCurrSelectVO()
				.getPk_fathedept());
		if (faterVo != null && faterVo.getCanceled() != null
				&& faterVo.getCanceled().booleanValue()) {
			getBillUI().showErrorMessage(
					nc.vo.bd.MultiLangTrans.getTransStr("MO4", new String[] {
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080404", "UPP10080404-000003")/*
																		 * @res
																		 * "封存"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10080404", "UC001-0000045") /*
																	 * @res "修改"
																	 */}));// 上级已封存,不能修改下级
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"canceldate").setEdit(false);
			return;
		}
		super.onBoEdit();
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void setCancelDateEditable() {
		if (getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("canceled").getValue().equals("false"))
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"canceldate").setEdit(false);
		else
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"canceldate").setEdit(true);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
	 */
	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == DeptDocSwitchBtnVO.BTN_NO) {
			onSwitch();
		} else if (intBtn == DeptDocCopyBtnVO.BTN_NO) {
			onCopy();
		} else if (intBtn == DeptDocExportBtnVO.BTN_NO) {
			onExport();
		} else if (intBtn == DeptDocManBtnVO.BTN_NO) {
			onDocMan();
		}
		if (hrFatherVO != null && isHrChildBtnNo(intBtn)) {
			AggregatedValueObject aggVO = getBillCardPanelWrapper()
					.getBillVOFromUI();
			DeptdocVO headvo = aggVO == null ? null : (DeptdocVO) aggVO
					.getParentVO();
			onHrChanged(intBtn);
			if (headvo != null && headvo.getPrimaryKey() != null
					&& headvo.getPrimaryKey().trim().length() > 0)
				setSelectionPath(headvo);
		}
	}

	private void onDocMan() {
		DeptdocVO currentVO = getCurrSelectVO();
		if (currentVO == null) {
			MessageDialog.showErrorDlg(getBillUI(), null, nc.vo.bd.BDMsg
					.MSG_CHOOSE_DATA());
			return;
		}
		BDAssociateFileUtil deptdocAssociateFileUtil = new BDAssociateFileUtil(
				IBDFileManageConst.DEPT_FILEMANAGE_PATH);
		String showname = currentVO.getDeptcode() + currentVO.getDeptname();
		String dirname = deptdocAssociateFileUtil.getFileDir(currentVO);

		BDDocManageDlg.showFileManageDlg(getBillUI(), null,
				new String[] { dirname }, new String[] { showname }, dirname);
	}

	private boolean isHrChildBtnNo(int intBtn) {
		if (hrFatherVO == null || hrFatherVO.length == 0)
			return false;
		for (int i = 0; i < hrFatherVO.length; i++) {
			ButtonVO fatherBtn = hrFatherVO[i];
			if (intBtn == fatherBtn.getBtnNo())
				return true;
			int[] childAry = fatherBtn.getChildAry();
			if (childAry != null && childAry.length > 0) {
				for (int j = 0; j < childAry.length; j++) {
					if (intBtn == childAry[j])
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void onSwitch() throws Exception {
		getBillUI().removeAll();

		if (panelState == treeCardType) {
			getDeptdocUI().addSealdataPanel();
			getBillUI().add(getDeptTablePanel().getBillListPanel());

			getDeptTablePanel().getBillListPanel().updateUI();
			panelState = listType;

			getDeptTablePanel().initHeadComboBox(
					"deptattr",
					new DataReaderConstEnumFactoryAdapter("bd_deptdoc",
							"deptattr", new QzsmIntToStrDataTypeConvert())
							.getAllConstEnums(), false);
			getDeptTablePanel().initHeadComboBox(
					"depttype",
					new DataReaderConstEnumFactoryAdapter("bd_deptdoc",
							"depttype", new QzsmIntToIntDataTypeConvert())
							.getAllConstEnums(), false);
			if (!isDefInitialed) {
				showListDef();
				isDefInitialed = true;
			}
			// 初始化列表界面的数据
			initTablePanel();
			ButtonObject[] listButtons = getButtonManager().getButtonAry(
					new int[] { IBillButton.Card, IBillButton.Print });

			getDeptdocUI().setButtons(listButtons);
			getDeptTablePanel().getBillListPanel().getHeadBillModel()
					.execLoadFormula();
			if (getBufferData().getVOBufferSize() > 0) {
				getButtonManager().getButton(IBillButton.Print)
						.setEnabled(true);
				getBillUI().updateButtonUI();

				DeptdocVO selectedVO = (DeptdocVO) getBufferData()
						.getCurrentVO().getParentVO();
				CircularlyAccessibleValueObject[] allVOs = getDeptTablePanel()
						.getBillListPanel().getHeadBillModel().getBodyValueVOs(
								DeptdocVO.class.getName());
				int currentRow = -1;
				for (int i = 0; allVOs != null && i < allVOs.length; i++) {
					DeptdocVO currentVO = (DeptdocVO) allVOs[i];
					if (selectedVO.getPrimaryKey().equals(
							currentVO.getPrimaryKey())) {
						currentRow = i;
						break;
					}
				}
				listPanel.getBillListPanel().getHeadTable().getSelectionModel()
						.setSelectionInterval(currentRow, currentRow);

			}
		} else if (panelState == listType) {
			ButtonObject[] cardButtons = getButtonManager().getButtonAry(
					((DeptDocCTL) getUIController()).getCardButtonAry());
			getBillUI().removeAll();
			getDeptdocUI().addSealdataPanel();
			getBillUI().add(getDeptdocUI().getSplitPane());
			getDeptdocUI().getSplitPane().updateUI();
			onBoRefresh();
			panelState = treeCardType;
			getDeptdocUI().setButtons(cardButtons);

			// 设置当前选中的节点
			int newCurrentRow = listPanel.getBillListPanel().getHeadTable()
					.getSelectedRow();
			HYBillVO vo = null;
			if (newCurrentRow != -1) {
				vo = (HYBillVO) listPanel.getBillListPanel().getBillValueVO(
						newCurrentRow, HYBillVO.class.getName(),
						DeptdocVO.class.getName(), DeptdocVO.class.getName());
			}

			DeptdocVO selectedVO = vo == null ? null : (DeptdocVO) vo
					.getParentVO();
			if (selectedVO != null) {
				TableTreeNode root = (TableTreeNode) ((DeptDocUI) getBillUI())
						.getBillTree().getModel().getRoot();
				Enumeration<TableTreeNode> enumeration = root
						.preorderEnumeration();
				enumeration.nextElement();
				TableTreeNode selectedNode = null;
				while (enumeration.hasMoreElements()) {
					TableTreeNode currentNode = enumeration.nextElement();
					if (selectedVO.getPrimaryKey().equals(
							currentNode.getNodeID())) {
						selectedNode = currentNode;
						break;
					}
				}
				((DeptDocUI) getBillUI()).getBillTree().setSelectionPath(
						new TreePath(selectedNode.getPath()));
			}
		}

	}

	/**
	 * @throws Exception
	 */
	private void showListDef() throws Exception {
		String[] strDefObjs = new String[] { "部门档案" };
		String[] strPrefix = new String[] { "def" };
		ListDefShowUtil defShowUtil = new ListDefShowUtil(getDeptTablePanel()
				.getBillListPanel());
		defShowUtil.showDefWhenRef(strDefObjs, strPrefix, true);
	}

	private void onCopy() {

		try {
			CircularlyAccessibleValueObject[] deptDocCircurlarAccessVOs = getBufferData()
					.getAllHeadVOsFromBuffer();

			int len = (deptDocCircurlarAccessVOs == null ? 0
					: deptDocCircurlarAccessVOs.length);
			if (len == 0) {
				getBillUI().showErrorMessage(
						nc.vo.bd.MultiLangTrans.getTransStr("MC3",
								new String[] { nc.ui.ml.NCLangRes.getInstance()
										.getStrByID("10080404",
												"UPP10080404-000014") /*
																		 * @res
																		 * "部门档案记录"
																		 */})); // 部门档案记录不能为空
				return;
			}
			getCopyDlg().setTotalData(deptDocCircurlarAccessVOs);
			getCopyDlg().clearUnitData();
			getCopyDlg().showModal();
			copyDlg = null;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			getBillUI().showErrorMessage(e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	public BillListPanelWrapper getDeptTablePanel() throws Exception {
		if (listPanel == null) {
			listPanel = new BillListPanelWrapper(getBillUI().getEnvironment(),
					new DeptTableListCTL(), null, "10080404");
			listPanel.getBillListPanel().getHeadBillModel()
					.addSortRelaObjectListener(new IBillRelaSortListener() {

						public List getRelaSortObject() {
							try {
								return Arrays.asList(getBufferData()
										.getAllHeadVOsFromBuffer());
							} catch (Exception e) {
								Logger.error(e.getMessage(), e);
								e.printStackTrace();
							}
							return null;
						}

					});
		}
		// initTablePanel();

		return listPanel;
	}

	private DeptDocUI getDeptdocUI() {
		return ((DeptDocUI) getBillUI());
	}

	/**
	 * @throws Exception
	 */
	private void initTablePanel() throws Exception {
		if (getBufferData().getAllHeadVOsFromBuffer() == null)
			return;
		listPanel.getBillListPanel().setHeaderValueVO(
				getBufferData().getAllHeadVOsFromBuffer());
	}

	/**
	 * 取HR事件处理类
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected DeptOpListner getHROpListner() {
		if (!isHrUse)
			return null;
		if (m_HROpListner == null) {
			InterfaceexecVO[] vos = null;
			try {

				IUAPQueryBS iIUAPQueryBS = (IUAPQueryBS) NCLocator
						.getInstance().lookup(IUAPQueryBS.class.getName());
				Collection c = iIUAPQueryBS.retrieveByClause(
						InterfaceexecVO.class,
						"funcode = '10080404' and opertype = 32");
				if (c.size() == 0)
					return null;
				vos = (InterfaceexecVO[]) c.toArray(new InterfaceexecVO[c
						.size()]);
				// vos =
				// (InterfaceexecVO[])UAPSuperBO_Client.queryByWhereClause(InterfaceexecVO.class,"funcode
				// = '10080404' and opertype = 32");
				if (vos != null && vos.length > 0) {
					String className = vos[0].getInterfaceclassname();
					Class cs = Class.forName(className);
					if (DeptOpListner.class.isAssignableFrom(cs)) {
						m_HROpListner = (DeptOpListner) cs.newInstance();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("取HR事件处理类时发生错误");
			}
		}
		return m_HROpListner;
	}

	/**
	 * HR变更事件处理
	 * 
	 */
	private void onHrChanged(int btnNo) {
		// 如果当前VO有效,则生成DeptOpEvent事件,通知HROpListner进行处理
		nc.vo.pub.AggregatedValueObject currVO = getBufferData().getCurrentVO();
		if (currVO == null)
			return;

		getHROpListner().PorcessHROp(
				new DeptOpEvent(
						getBillUI().getButtonManager().getButton(btnNo),
						(DeptdocVO) currVO.getParentVO()));

		// HR变更事件后执行一次刷新操作
		try {
			onBoRefresh();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			getBillUI().showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 获取复制对话框
	 * 
	 * @return
	 */
	private CopyDlg getCopyDlg() {
		if (copyDlg == null) {
			copyDlg = new CopyDlg(getBillUI(), nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10080404", "UC001-0000043")/* @res "复制" */);

			TreeDetail md = new TreeDetail();
			MethodGroup mg = new MethodGroup();
			try {
				mg.setKeyField(DeptdocVO.class.getMethod("getPk_deptdoc"));
				mg.setAssKeyField(DeptdocVO.class.getMethod("getPk_fathedept"));
				mg.setNameField(DeptdocVO.class.getMethod("getDeptname"));
				mg.setSortCodeFiled(DeptdocVO.class.getMethod("getDeptcode"));
				mg.setHowDisplay(new boolean[] { false, true, true });
				mg.setAimClass(DeptdocVO.class);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
			// 必要
			md.setMg(new MethodGroup[] { mg });
			// 可选
			md.setRootname(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080404", "UPP10080404-000011")/* @res "部门档案" */);

			md.setPolicy(IOPolicy.Policy_Anomaly);
			copyDlg.setTreeDetail(md);
			copyDlg.setIsFullPath(false);
			copyDlg.setMarjorClass(DeptdocVO.class);
			copyDlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080404", "UPP10080404-000011")/* @res "部门档案" */);
			copyDlg.setUser(this);
			copyDlg.setRefDestUnitsModel("公司目录");
			filterSelfCorp(copyDlg);
			copyDlg.setUserDefCbx1(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10080404", "UPP10080404-000013")/* @res "是否同时复制岗位信息" */);
			copyDlg.setUserDefCbx1Selected(false);
		}
		return copyDlg;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bd.util.ICopy#getValue(java.lang.Object[])
	 */
	public Object[] getValue(Object[] param) throws Exception {

		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bd.util.ICopy#checkBeforeSelect(java.lang.String[],
	 *      java.lang.Object[])
	 */
	public void checkBeforeSelect(String[] units, Object[] values)
			throws Exception {
		if (values == null || values.length == 0) {
			throw new Exception(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA()/* 请选中要处理的数据! */);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bd.util.ICopy#doCopy(java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public String doCopy(String[] units, Object[] values) throws Exception {
		String strRtn = null;
		Vector v = new Vector();
		DeptdocVO[] vos = null;

		try {
			boolean isCopyJobs = getCopyDlg().isUserDef1Selected();
			for (int i = 0; i < values.length; i++) {
				if (values[i] instanceof DeptdocVO) {
					DeptdocVO vo = (DeptdocVO) values[i];
					vo.setPk_psndoc(null);
					vo.setPk_psndoc2(null);
					vo.setPk_psndoc3(null);
					v.add(vo);
				}
			}
			vos = new DeptdocVO[v.size()];
			v.copyInto(vos);
			strRtn = DeptdocBO_Client.doCopy(units, vos, isCopyJobs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage() == null ? "error type "
					+ e.getClass() : e.getMessage());
		}

		return strRtn;
	}

	/**
	 * 在复制时的目标单位中过滤本公司
	 * 
	 * @param dlg
	 *            复制对话框
	 */
	private void filterSelfCorp(CopyDlg dlg) {
		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		String strWhere = dlg.geRefDestUnitsWherepart();
		strWhere = (strWhere == null || strWhere.trim().length() == 0 ? ""
				: strWhere);
		// 过滤公司,使其仅包含当前用户拥有"部门档案"操作权限的公司,且不包含当前公司
		strWhere = "(" + strWhere + ") and pk_corp <> '" + pk_corp + "' ";

		dlg.setRefDestUnitsWherepart(strWhere);
	}

	private boolean existPsn(String pkDeptdoc) throws BusinessException {

		String sql = "select count(*) from bd_psndoc where "/*
															 * psnclscope = 0
															 * and
															 */
				+ "  pk_deptdoc = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(pkDeptdoc);

		IUAPQueryBS iIUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		Integer count = (Integer) iIUAPQueryBS.executeQuery(sql, para,
				new ColumnProcessor());
		return count.intValue() > 0;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.trade.bill.BillEventHandler#onBoPrint()
	 */
	protected void onBoPrint() throws Exception {

		nc.ui.pub.print.IDataSource dataSource = new SingleListHeadPRTS(
				getBillUI()._getModuleCode(), getDeptTablePanel()
						.getBillListPanel());

		IGetDefCodeOrName getdefcodeorname = new DefaultGetDefCodeOrName(
				dataSource);
		dataSource = new PrtDefDealedDecorator(new DefaultCheckIsDef(),
				getdefcodeorname, dataSource);
		dataSource = new DeptPrtDefShowNameDecorator(dataSource,
				getDeptTablePanel().getBillListPanel());

		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
				dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
				._getModuleCode(), getBillUI()._getOperator(), getBillUI()
				.getBusinessType(), null);
		print.selectTemplate();
		getDeptTablePanel().getBillListPanel().getHeadBillModel()
				.execLoadFormula();
		print.preview();
	}

	private void onExport() {

		try {
			BillExportUtil.output(getBufferData().getAllHeadVOsFromBuffer(),
					"bsdept", this.getBillUI());
		} catch (Exception e) {

			getBillUI().showErrorMessage(e.getMessage());
		}
	}

	@Override
	protected void onBoCard() throws Exception {
		onSwitch();
	}

	@Override
	protected void onBoReturn() throws Exception {
		// TODO Auto-generated method stub
		onSwitch();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoCancel()
	 */
	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		getDeptdocUI().getShowSealdataCbx().setEnabled(true);
	}

	protected void addNewForImport() {
		try {
			super.onBoAdd(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPanelState() {
		return panelState;
	}
}