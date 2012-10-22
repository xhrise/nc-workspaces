package nc.ui.ic.ic211;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.pub.RefMsg;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.pf.QryInBillDlg;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.qc.inter.CertService;
import nc.ui.scm.pub.redunmulti.ISourceRedunUI;
import nc.ui.scm.pub.redunmulti.PubRedunMultiDlg;
import nc.ui.scm.pub.redunmulti.PubTransBillPaneVO;
import nc.vo.bd.b15.GroupInventoryVO;
import nc.vo.ic.ic700.IntList;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ProductCode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.so.RushLinkQueryVO;

/**
 * 销售出库单 创建日期：(2001-11-23 15:39:43)
 * 
 * @author：张欣
 */
@SuppressWarnings({ "restriction", "serial", "rawtypes", "unchecked",
		"deprecation", "unused" })
public class ExtClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// 形成代管对话框
	private FormMemoDlg ivjFormMemoDlg1 = null;

	private nc.ui.ic.auditdlg.ClientUIInAndOut m_dlgInOut = null;

	// 保存借转业务类型id
	private ArrayList m_alBrwLendBusitype = null;

	// 业务类型itemkey
	private final String m_sBusiTypeItemKey = "cbiztype";

	// 是否起用了质量检验产品
	private boolean m_isQCstartup = false;

	// 是否已经校验质量产品起用
	private boolean m_isCheckQCstartup = false;

	private ICBcurrArithUI clsCurrArith;

	private IButtonManager m_buttonManager;

	/**
	 * ClientUI2 构造子注解。
	 */
	public ExtClientUI() {
		super();
		initialize();
		
	}

	/**
	 * ClientUI 构造子注解。 add by liuzy 2007-12-18 根据节点编码初始化单据模版
	 */
	public ExtClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI 构造子注解。 nc 2.2 提供的单据联查功能构造子。
	 */
	public ExtClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	/**
	 * 创建者：王乃军 功能：单据编辑后处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	@Override
	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		// nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
	}

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	@Override
	protected void afterBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

	}

	/**
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */

	public void afterInvEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// 字段itemkey
			String sItemKey = e.getKey();
			int row = e.getRow();

			// 如果清除存货编码则清掉此行,并掉表尾显示
			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
				// 表尾
				setTailValue(null);
			} else {

				String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent()).getRefPK();
				if (sTempID1 != null && sTempID1.trim().length() != 0) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
						sTempID2 = getBillCardPanel().getHeadItem(
								IItemKey.WAREHOUSE).getValue();
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(getEnvironment().getUserID());
					alIDs.add(getEnvironment().getCorpID());
					// 查询存货信息
					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(QryInfoConst.INV), alIDs);

					// invokeClient(
					// "queryInfo",
					// new Class[] { Integer.class, Object.class },
					// new Object[] { new Integer(QryInfoConst.INV), alIDs });
					if (getM_voBill() != null) {

						getM_voBill().setItemInv(row, voInv);
						getM_voBill().setItemValue(
								row,
								IItemKey.NAME_HEADID,
								getM_voBill().getHeaderValue(
										IItemKey.NAME_HEADID));
					}
					// 表体
					setBodyInvValue(row, voInv);
					// 表尾
					// setTailValue1(row);
					// 清批次/自由项等数据
					clearRowData(0, row, sItemKey);
					//
					execEditFomulas(0, "cinventorycode");
					//
					// 以下的信息需要优化，如果批次号未显示，则无需显示。
					// showHintMessage("存货修改，请重新确认批次、数量。");
				}
			}
			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* 表明该存货没有替换件，则将存货管理主键重新置回单据的存货编码单元 **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// 设置序列号是否可用
			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void afterInvEditforBarCode(String sItemKey, int row) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// 字段itemkey
			// String sItemKey = e.getKey();
			// int row = e.getRow();

			// 如果清除存货编码则清掉此行,并掉表尾显示

			String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem(sItemKey).getComponent()).getRefPK();
			if (sTempID1 != null && sTempID1.trim().length() != 0) {
				String sTempID2 = null;
				if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
					sTempID2 = getBillCardPanel().getHeadItem(
							IItemKey.WAREHOUSE).getValue();
				ArrayList alIDs = new ArrayList();
				alIDs.add(sTempID2);
				alIDs.add(sTempID1);
				alIDs.add(getEnvironment().getUserID());
				alIDs.add(getEnvironment().getCorpID());
				// 查询存货信息
				InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(new Integer(
						QryInfoConst.INV), alIDs);
				if (getM_voBill() != null)
					getM_voBill().setItemInv(row, voInv);
				// 表体
				setBodyInvValue(row, voInv);
				// 表尾
				// setTailValue1(row);
				// 清批次/自由项等数据
				clearRowData(0, row, sItemKey);
				// 以下的信息需要优化，如果批次号未显示，则无需显示。
				// showHintMessage("存货修改，请重新确认批次、数量。");
			}

			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* 表明该存货没有替换件，则将存货管理主键重新置回单据的存货编码单元 **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// 设置序列号是否可用
			// setBtnStatusSN(e.getRow());
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}
	
	/**
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void afterInvEditforBarCode(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.SCMEnv.out("inv chg");
		try {
			// 字段itemkey
			String sItemKey = e.getKey();
			int row = e.getRow();

			// 如果清除存货编码则清掉此行,并掉表尾显示
			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
				// 表尾
				setTailValue(null);
			} else {

				String sTempID1 = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent()).getRefPK();
				if (sTempID1 != null && sTempID1.trim().length() != 0) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null)
						sTempID2 = getBillCardPanel().getHeadItem(
								IItemKey.WAREHOUSE).getValue();
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(getEnvironment().getUserID());
					alIDs.add(getEnvironment().getCorpID());
					// 查询存货信息
					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(QryInfoConst.INV), alIDs);
					if (getM_voBill() != null)
						getM_voBill().setItemInv(row, voInv);
					// 表体
					setBodyInvValue(row, voInv);
					// 表尾
					// setTailValue1(row);
					// 清批次/自由项等数据
					clearRowData(0, row, sItemKey);
					// 以下的信息需要优化，如果批次号未显示，则无需显示。
					// showHintMessage("存货修改，请重新确认批次、数量。");
				}
			}
			// by zhx 0311 not use
			// if (getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			// && e.getValue().toString().trim().length() == 0) {
			// /* 表明该存货没有替换件，则将存货管理主键重新置回单据的存货编码单元 **/
			// getBillCardPanel().setBodyValueAt(
			// m_voBill.getItemValue(e.getRow(), "cinventorycode"),
			// e.getRow(),
			// "cinventorycode");
			// }

			// 2002-07-26 use it to clear should number when no source id
			if (getSourBillTypeCode() == null
					|| getSourBillTypeCode().trim().length() == 0) {
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(row,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, row,
							getEnvironment().getShouldAssistNumItemKey());
			}

			// 设置序列号是否可用
			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,before sel");

	}

	/**
	 * 创建者：王乃军 功能：抽象方法：保存前的VO检查 参数：待保存单据 返回： 例外： 日期：(2001-5-24 下午 5:17)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		try {
			VOCheck.checkOutRetVO(voBill);
		} catch (Exception e) {
			GenMethod.handleException(this, null, e);
			return false;
		}
		return this.checkVO();
	}

	/**
	 * 程起伍 功能：重载父类的方法.执行特殊公式:承运商. 参数： 返回： 例外： 日期：(2004-7-20 17:27:02)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param alListData
	 *            java.util.ArrayList
	 */
	protected void execExtendFormula(ArrayList alListData) {
		if (alListData == null || alListData.get(0) == null)
			return;
		int iLen = alListData.size();
		CircularlyAccessibleValueObject[] headVO = new CircularlyAccessibleValueObject[iLen];
		for (int i = 0; i < iLen; i++) {
			headVO[i] = ((AggregatedValueObject) alListData.get(i))
					.getParentVO();
		}

		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(headVO,
				new String[] { "pk_cubasdoctran" }, "dm_trancust",
				"pk_trancust", new String[] { "pkcusmandoc" },
				"cwastewarehouseid");

		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(headVO,
				new String[] { "vcustname" }, "bd_cubasdoc", "pk_cubasdoc",
				new String[] { "custname" }, "pk_cubasdoctran");
	}

	/**
	 * 创建者：余大英 功能：得到当前单据vo,包含货位/序列号和界面上所有的数据,不包括删除的行 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */

	public GeneralBillVO getBillVO() {
		GeneralBillVO billVO = super.getBillVO();
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null)
			billVO.setHeaderValue("vdiliveraddress",
					((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem("vdiliveraddress").getComponent())
							.getText());
		return billVO;

	}

	/**
	 * 创建者：王乃军 功能：得到查询对话框 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	// protected QueryConditionDlgForBill getConditionDlg() {
	// if (ivjQueryConditionDlg == null) {
	// ivjQueryConditionDlg = super.getConditionDlg();
	// ivjQueryConditionDlg.setCombox("freplenishflag", new String[][] {
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPP4008busi-000368")
	// /*
	// * @res "出库"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPT40080602-000014")
	// /*
	// * @res "退库"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLALL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	// "UPPSCMCommon-000217")
	// /*
	// * @res "全部"
	// */}
	// });
	// // zhy2005-04-23销售出库单需要过滤客户权限
	// // 比基类多一个客户
	// ivjQueryConditionDlg.setCorpRefs("head.pk_corp", new String[] {
	// "head.ccustomerid"
	// });
	// }
	// return ivjQueryConditionDlg;
	// }

	/**
	 * 返回 ReturnDlg1 特性值。
	 * 
	 * @return nc.ui.ic.auditdlg.ClientUIInAndOut
	 */
	/* 警告：此方法将重新生成。 */
	protected nc.ui.ic.auditdlg.ClientUIInAndOut getDispenseDlg(String sTitle,
			ArrayList alInVO, ArrayList alOutVO) {
		if (m_dlgInOut == null) {
			try {
				// user code begin {1}
				m_dlgInOut = new ClientUIInAndOut(this, sTitle);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		// if (m_voBill == null)
		setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData().get(
				getM_iLastSelListHeadRow())).clone());
		m_dlgInOut.setVO(getM_voBill(), alInVO, alOutVO, getBillType(),
				getM_voBill().getPrimaryKey().trim(), getEnvironment()
						.getCorpID(), getEnvironment().getUserID());
		m_dlgInOut.setName("BillDlg");
		// m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		return m_dlgInOut;
	}

	/**
	 * 返回 FormMemoDlg1 特性值。
	 * 
	 * @return nc.ui.ic.ic211.FormMemoDlg
	 */
	/* 警告：此方法将重新生成。 */
	private FormMemoDlg getFormMemoDlg1() {
		if (ivjFormMemoDlg1 == null) {
			try {
				ivjFormMemoDlg1 = new nc.ui.ic.ic211.FormMemoDlg(this);
				ivjFormMemoDlg1.setName("FormMemoDlg1");
				// ivjFormMemoDlg1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				// user code begin {1}
				// ivjFormMemoDlg1.setParent(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFormMemoDlg1;
	}

	/**
	 * 类型说明： 创建日期：(2002-11-28 14:12:13) 作者：张欣IC 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void initialize() {

		// 必须重载基类的initialize方法
		super.initialize();

		try {
			// 可开票数量
			BillItem item = getBillCardPanel().getBodyItem("navlinvoicenum");
			if (item != null) {
				item.setShow(false);
				getBillCardPanel().getBodyPanel()
						.hideTableCol("navlinvoicenum");
			}
			getBillCardPanel()
					.getBodyPanel()
					.getTable()
					.setSelectionMode(
							javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			getBillListPanel()
					.getChildListPanel()
					.getTable()
					.setSelectionMode(
							javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e.getMessage());
		} finally {
			getBillCardPanel().addBillEditListenerHeadTail(this);
		}

	}

	/**
	 * 创建者：王乃军 功能：初始化系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void initPanel() {
		// 需要单据参照
		super.setNeedBillRef(true);

		// “形成代管”按钮
		getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
				.setEnabled(false);
		// “配套”按钮
		getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
				.setEnabled(true);
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_saleOut;
	}

	public String getFunctionNode() {
		return "40080802";
	}

	public int getInOutFlag() {
		return InOutFlag.OUT;
	}

	/**
	 * 创建者：王乃军 功能：是否借转类型 第一次需要读库。 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;
			// 业务类型
			String sBusitypeid = null;
			if (getM_iCurPanel() == BillMode.List) { // 列表形式下
				if (getM_alListData() != null
						&& getM_iLastSelListHeadRow() >= 0
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					voMyBill = ((GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow()));
					sBusitypeid = (String) voMyBill
							.getHeaderValue(m_sBusiTypeItemKey);
				}
			} else { // 表单
				if (getBillCardPanel().getHeadItem(m_sBusiTypeItemKey) != null
						&& getBillCardPanel().getHeadItem(m_sBusiTypeItemKey)
								.getComponent() != null) {
					nc.ui.pub.beans.UIRefPane ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(m_sBusiTypeItemKey).getComponent());
					// 置pk
					sBusitypeid = ref.getRefPK();
				}
			}
			// 业务类型不为空时
			// 第一次需要读库。
			if (sBusitypeid != null && m_alBrwLendBusitype == null) {
				ArrayList alParam = new ArrayList();
				alParam.add(getEnvironment().getCorpID());
				m_alBrwLendBusitype = (ArrayList) GeneralBillHelper
						.queryInfo(new Integer(
								QryInfoConst.QRY_BRW_LEND_BIZTYPE), alParam);
				// 如果返回空，初始化之，标志已经读过了，但没有借转类型啊!
				if (m_alBrwLendBusitype == null)
					m_alBrwLendBusitype = new ArrayList();
			}
			// 是借转类型的，返回“是”
			if (sBusitypeid != null && m_alBrwLendBusitype != null
					&& m_alBrwLendBusitype.contains(sBusitypeid))
				return true;
		} catch (Exception e) {
			SCMEnv.error(e);
		}
		return false;
	}

	/**
	 * 创建者：根据发货出库 v55 add 修改日期，修改人，修改原因，注释标志：
	 */
	protected void onAdd4331() {
		try {

			PubTransBillPaneVO[] sourecVO = new PubTransBillPaneVO[1];
			sourecVO[0] = new PubTransBillPaneVO(
					"4331",
					"发货单",
					(ISourceRedunUI) Class
							.forName(
									"nc.ui.so.soreceive.redun.ReceiveTo4CRedunSourceCtrl")
							.newInstance(), null, "SO", null, null);

			PubTransBillPaneVO[] curVO = new PubTransBillPaneVO[1];
			curVO[0] = new PubTransBillPaneVO("4C", "销售出库单", null, null, "IC",
					null, null);

			HashMap<String, String> haparam = new HashMap<String, String>();
			haparam.put("cbiztype", getSelBusiType());

			PubRedunMultiDlg refdlg = new PubRedunMultiDlg(this, sourecVO,
					curVO, true, haparam);
			if (refdlg.showModal() != UIDialog.ID_OK)
				return;
			AggregatedValueObject[] sourcebillvos = refdlg.getRetBillVos();
			if (sourcebillvos == null || sourcebillvos.length <= 0)
				return;

			AggregatedValueObject[] vos = null;
			try {
				// sourcebillvos =
				// nc.vo.ic.pub.GenMethod.splitSourceVOs(sourcebillvos, "4331",
				// getBillType());
				vos = PfChangeBO_Client.pfChangeBillToBillArray(sourcebillvos,
						"4331", getBillType());
				vos = nc.vo.ic.pub.GenMethod.splitTargetVOs(vos, "4331",
						getBillType());
				if (vos != null && vos.length > 0)
					// 检查单据是否来源于新的参照界面
					if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
							.getAttributeValue(ICConst.IsFromNewRef))) {
						// 按库存默认方式分单
						vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
								(GeneralBillVO[]) vos, getBillType(),
								getBillListPanel().getHeadBillModel()
										.getFormulaParse());
						// 将外来单据的单位转换为库存默认单位.
						nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
								(GeneralBillVO[]) vos, GenMethod.getIntance());
					}
			} catch (Exception e) {
				GenMethod.handleException(this, null, e);
				nc.vo.scm.pub.SCMEnv.out(e);
				return;
			}

			GeneralBillUICtl.procOrdEndAfterRefAdd(
					(GeneralBillItemVO[]) SmartVOUtilExt.getBodyVOs(vos),
					getBillCardPanel(), getBillType());

			// v5 lj 支持多张单据参照生成
			if (vos != null && vos.length == 1) {
				setRefBillsFlag(false);
				setBillRefResultVO(((GeneralBillVO) vos[0]).getHeaderVO()
						.getCbiztypeid(), vos);
			} else {
				setRefBillsFlag(true);// 是参照生成多张
				setBillRefMultiVOs(((GeneralBillVO) vos[0]).getHeaderVO()
						.getCbiztypeid(), (GeneralBillVO[]) vos);
			}
			GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD,
					IItemKey.freplenishflag, false);
			// end v5

		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000297")/* @res "发生错误:" */
					+ e.getMessage());
		}
	}

	protected class ButtonManager211 extends
			nc.ui.ic.pub.bill.GeneralButtonManager {

		public ButtonManager211(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		/**
		 * 子类实现该方法，响应按钮事件。
		 * 
		 * @version (00-6-1 10:32:59)
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			showHintMessage(bo.getName());
			if (bo.getTag() != null && bo.getTag().indexOf(":") > 0) {
				String billtype = bo.getTag().substring(0,
						bo.getTag().indexOf(":"));
				if (billtype.equals("4331")) {
					onAdd4331();
					return;
				}
			}
			if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_XCDG))
				// 处理"形成待管"
				onFormMemo();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_DISPENSE))
				onDispense();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_PRINT_SUM_BATCH))
				onPrintLot();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_PRINT_CERTIFICATION))
				onPrintCert();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_ASSEMBLY))
				onAdd4L();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_REFER_IN))
				onRefInBill();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_RUSH_QUERY))
				onRushQuery();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_OUT_RETURN))
				onOutReturn();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_REFER_DIRECTIN))
				onRefDirectInBill();
			else if (bo == getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_COOP_45))
				onCoop45Save();
			else {
				
				String boName = bo.getName();
				Boolean check = true;
				// 修改针对取消签字按钮的BEFORE操作
				// modify by river for 2012-09-19
				try {
					if("取消签字".equals(bo.getName())) {
						check = beforeOnCancleSign();
						
					}
				} catch(Exception e ) {
					Logger.error(e);
				}
				
				if(check)
					super.onButtonClicked(bo);
				
				if("卡片显示".equals(boName) || "首页".equals(boName) || "上页".equals(boName) || "下页".equals(boName) || "末页".equals(boName) || "取消".equals(boName)) {
					getBillCardPanel().execHeadFormulas(new String[]{
							"contracttype->getColValue(ic_general_h , contracttype , cgeneralhid , cgeneralhid)",
							"storprice->getColValue(ic_general_h , pk_storcontract_b , cgeneralhid , cgeneralhid)",
							"transprice->getColValue(ic_general_h , pk_transport_b , cgeneralhid , cgeneralhid)",
							
						});
					
				}
					
			}

		}
		
		/**
		 * 功能 ： 取消签字前操作
		 * 
		 * @author river
		 * 
		 * Create Date : 2012-09-19
		 * 
		 */
		protected final Boolean beforeOnCancleSign() throws Exception {
			
			GeneralBillVO billVO = getCurVO();
			Boolean check = true;
			Map retMap = (Map) UAPQueryBS.getInstance().executeQuery("select vuserdef3 , vuserdef4 from ic_general_h where cgeneralhid = '"+billVO.getPrimaryKey()+"'", new MapProcessor());
			
			if(retMap != null) {
				if("Y".equals(retMap.get("vuserdef3"))) {
					showErrorMessage("下游运费已经结算，不能进行弃审操作！");
					check = false;
				}
				
				if("Y".equals(retMap.get("vuserdef4"))) {
					showErrorMessage("仓储费及装卸费已经结算，不能进行弃审操作！");
					check = false;
				}
					
			}
			
			return check;
			
		}

		/**
		 * 创建者：王乃军 功能：新增处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
		 */
		public void onAdd() {
			super.onAdd();
			// 取得服务器时间戳
			// try {
			// m_dTime = RecordTimeHelper.getTimeStamp();
			// }
			// catch (Exception e) {
			// SCMEnv.error(e);
			// }
		}

		/**
		 * 创建者：王乃军 功能：查询处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
		 * 韩卫 2003-06-24 注释： 由于已经在
		 * nc.ui.ic.pub.bill.GeneralBillClientUI.setListHeadData
		 * (GeneralBillHeaderVO
		 * nc.ui.ic.pub.bill.GeneralBillClientUI.setAlistDataByFormula(int,
		 * ArrayList) 执行了表头、表体公式，所以切不可在代码中重复执行以下代码：
		 * getBillListPanel().getHeadBillModel().execLoadFormula();
		 * getBillListPanel().getBodyBillModel().execLoadFormula();
		 * 
		 */
		public void onQuery() {
			try {
				nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
				boolean isFrash = !(getClientUI().isBQuery() || !getClientUI()
						.getQryDlgHelp().isBEverQry());
				int cardOrList = getM_iCurPanel();
				Object[] ret = getQryDlgHelp().queryData(isFrash);
				if (ret == null || ret[0] == null
						|| !((UFBoolean) ret[0]).booleanValue())
					return;
				ArrayList<GeneralBillVO> alListData = (ArrayList<GeneralBillVO>) ret[1];
				if (!isFrash) {
					// 刷新按钮 “曾经查询过”
					setButtonStatus(true);
				}

				// m_sBnoutnumnull = null;
				//
				// timer.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				// "UPP4008busi-000277")/* @res "@@查询开始：" */);
				//
				// // DUYONG 增加“刷新”的功能
				// // 将原单据类型（卡片/列表）纪录下来，如果在卡片界面执行了刷新，则无须切换到列表界面
				// int cardOrList = getM_iCurPanel();
				// // 如果是[(1)进行查询(2)从来没有进行查询过但是点击了刷新按钮]，则显示查询模板进行查询
				// // 如果曾经查询过，并且点击了刷新按钮，则跳过此段代码
				// QryConditionVO voCond = null;
				// //修改人：刘家清 修改日期：2007-9-24上午09:52:28 修改原因：刷新时作用存储的查询VO
				// if (m_bQuery || !m_bEverQry) {
				// getConditionDlg().showModal();
				// timer.showExecuteTime("@@getConditionDlg().showModal()：");/*-=notranslate=-*/
				//
				// if (getConditionDlg().getResult() !=
				// nc.ui.pub.beans.UIDialog.ID_OK)
				// // 取消返回
				// return;
				//
				// // 如果是进行查询，则将“曾经查询过”的标识设置成true（这样，才能够进行“刷新”的操作）
				// m_bEverQry = true;
				// // 刷新按钮
				// setButtonStatus(true);
				//
				// // 获得qrycontionVO的构造
				// voCond = getQryConditionVO();
				//
				// // 记录查询条件备用
				// m_voLastQryCond = voCond;
				// }else
				// voCond = m_voLastQryCond;
				//
				// //addied by liuzy 2008-03-31 修正单据日期“从”、“到”字段名
				// String str_where = voCond.getQryCond();
				// if(null != str_where && !"".equals(str_where)){
				// if(str_where.indexOf("head.dbilldate.from") > -1)
				// str_where = str_where.replace("head.dbilldate.from",
				// "head.dbilldate");
				// if(str_where.indexOf("head.dbilldate.end") > -1)
				// str_where = str_where.replace("head.dbilldate.end",
				// "head.dbilldate");
				// voCond.setQryCond(str_where);
				// }
				//
				// // 如果使用公式则必须传voaCond 到后台. 修改 zhangxing 2003-03-05
				// nc.vo.pub.query.ConditionVO[] voaCond = getConditionDlg()
				// .getConditionVO();
				// // /处理voaCond:当退库标志的值是"全部"时,需要替换掉.
				//
				// // voaCond = dealCondVO(voaCond); //父类已经处理
				//
				// voCond.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);
				//
				// voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
				// if (m_sBnoutnumnull != null) {
				// // 是否存在实发数量
				// voCond.setParam(33, m_sBnoutnumnull);
				// }
				//
				// voCond.setParam(QryConditionVO.QRY_LOGCORPID,
				// getEnvironment().getCorpID());
				// voCond.setParam(QryConditionVO.QRY_LOGUSERID,
				// getEnvironment().getUserID());
				//
				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				// "UPP4008busi-000250")/* @res "正在查询，请稍候..." */);
				// timer.showExecuteTime("Before 查询：：");/*-=notranslate=-*/
				// ArrayList alListData = (ArrayList)
				// GeneralBillHelper.queryBills(
				// getBillType(), voCond);

				timer.showExecuteTime("查询时间：");/* -=notranslate=- */

				// 公式情况 第一条表体记录公式查询补充数据 修改 hanwei 2003-03-05
				try {

					setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM,
							alListData);
					timer.showExecuteTime("@@setAlistDataByFormula公式解析时间：");/*
																			 * -=
																			 * notranslate
																			 * =
																			 * -
																			 */
					nc.vo.scm.pub.SCMEnv.out("0存货公式解析成功！");

				} catch (Exception e) {
				}
				execExtendFormula(alListData);
				if (alListData != null && alListData.size() > 0) {
					
					alListData = setContractManager(alListData);
					
					setScaleOfListData(alListData);
					setM_alListData(alListData);
					setListHeadData();
					// 当前是表单形式时，首先切换到列表形式
					if (BillMode.Card == getM_iCurPanel())
						onSwitch();

					// 缺省表头指向第一张单据
					selectListBill(0);
					// 设置当前的单据数量/序号，用于按钮控制
					setM_iLastSelListHeadRow(0);
					// 初始化当前单据序号，切换时用到！！！不宜主动设置表单的数据。
					m_iCurDispBillNum = -1;
					// 当前单据数
					m_iBillQty = getM_alListData().size();

					if (m_iBillQty > 0)
						showHintMessage(nc.ui.ml.NCLangRes
								.getInstance()
								.getStrByID(
										"4008busi",
										"UPP4008busi-000290",
										null,
										new String[] { (new Integer(m_iBillQty))
												.toString() })/* @res "共查到{0}张单据！" */);
					else
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000243")/*
																			 * @res
																			 * "未查到符合条件的单据。"
																			 */);
					// 控制有来源单据的按钮、菜单等的状态。
					ctrlSourceBillUI(false);
					timer.showExecuteTime("@@将数据显示到页面时间：");/* -=notranslate=- */

				} else {
					dealNoData();
				}

				setButtonStatus(true);

				// DUYONG 当执行刷新操作，并且当前界面为卡片类型时，不应该切换到列表类型的界面中
				if (!m_bQuery && getM_iCurPanel() != cardOrList) {
					onSwitch();
				}
			} catch (Exception e) {
				handleException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000251")/* @res "查询出错。" */);
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000252")/* @res "查询出错：" */
						+ e.getMessage());
			}
			
			getBillCardPanel().execHeadFormulas(new String[]{
				"contracttype->getColValue(ic_general_h , contracttype , cgeneralhid , cgeneralhid)",
				"storprice->getColValue(ic_general_h , pk_storcontract_b , cgeneralhid , cgeneralhid)",
				"transprice->getColValue(ic_general_h , pk_transport_b , cgeneralhid , cgeneralhid)",
				
			});
		}
		
		/**
		 * 功能 : 设置PTA销售订单参照过来的相关合同字段的值
		 * 
		 * Author ： river
		 * 
		 * Create ： 2012-09-13
		 * 
		 * @param alListData
		 * @throws Exception
		 */
		protected final ArrayList<GeneralBillVO> setContractManager(ArrayList<GeneralBillVO> alListData) throws Exception {
			
			List<String> pkList = new ArrayList<String>();
			for(GeneralBillVO alData : alListData) {
				pkList.add("'" + alData.getPrimaryKey() + "'");
			}
			
			String[] fieldArr = new String[]{
					"cgeneralhid" ,
					"concode" ,
					"pk_transport" ,
					"transprice" ,
					"storprice" ,
					"contracttype" ,
					"salecode" ,
					"pk_storcontract_b" ,
					"pk_transport_b" ,
					"pk_contract" ,
			};
			
			String sqlField = ConvertFunc.change(fieldArr);
			
			List<Map> retList = (List<Map>) UAPQueryBS.getInstance().executeQuery("select "+sqlField+" from ic_general_h where cgeneralhid in ("+ConvertFunc.change(pkList.toArray(new String[0]))+")", new MapListProcessor());
			
			if(retList != null && retList.size() > 0) {
				for(GeneralBillVO alData : alListData) 
					for(Map map : retList) 
						if(alData.getPrimaryKey().equals(map.get("cgeneralhid"))) 
							for(String filed : fieldArr)
								alData.getParentVO().setAttributeValue(filed, map.get(filed));
			}
			
			
			return alListData;
			
		}

		/**
		 * 创建者：王乃军 功能：保存，如果是借转类型的，清货位、序列号。 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
		 * 修改日期，修改人，修改原因，注释标志：
		 */
		public boolean onSave() {
			if (isBrwLendBiztype()) {
				// 保存前清货位数据
				m_alLocatorDataBackup = getM_alLocatorData();
				setM_alLocatorData(null);
				// 保存前清序列号数据
				m_alSerialDataBackup = getM_alSerialData();
				setM_alSerialData(null);
			}
			return super.onSave();
		}

		/**
		 * 创建者：王乃军 功能：序列号分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
		 * 修改日期，修改人，修改原因，注释标志：
		 */
		public void onSNAssign() {
			// 非浏览模式下，如果是借转类型的，不需要此操作。
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
														 * @res
														 * "特殊业务类型，不需要执行此操作。请在浏览状态下查看。"
														 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																 * @res
																 * "没有对应的借入/出单，无法查询到货位、序列号数据。请检查单据来源！"
																 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																 * @res
																 * "请察看相关借入/借出单的数据。"
																 */);
					}
					return;
				}
			}
			super.onSNAssign();
		}

		/**
		 * 创建者：王乃军 功能：货位分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
		 */
		public void onSpaceAssign() {
			// 非浏览模式下，如果是借转类型的，不需要此操作。
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
														 * @res
														 * "特殊业务类型，不需要执行此操作。请在浏览状态下查看。"
														 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																 * @res
																 * "没有对应的借入/出单，无法查询到货位、序列号数据。请检查单据来源！"
																 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																 * @res
																 * "请察看相关借入/借出单的数据。"
																 */);
					}
					return;
				}
			}
			super.onSpaceAssign();
		}

		/**
		 * 此处插入方法说明。 创建日期：(2001-9-10 11:34:31)
		 */
		private void onFormMemo() {
			if (getM_iLastSelListHeadRow() >= 0
					&& getM_alListData() != null
					&& getM_alListData().size() > getM_iLastSelListHeadRow()
					&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
				if (((GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow())).getChildrenVO().length == 0) {

					return;
				}

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();
				/** 将当前的操作员ID置入voBill, 传入形成代管对话框 */
				voBill.setHeaderValue("coperatorid", getEnvironment()
						.getUserID());
				getFormMemoDlg1().setBillVO(voBill);
				getFormMemoDlg1().showModal();
			}
		}

		/**
		 * 配套功能按钮方法。 功能： 参数： 返回： 例外： 日期：(2002-04-18 10:43:46)
		 * 修改日期，修改人，修改原因，注释标志：
		 */
		private void onDispense() {
			if (BillMode.Browse == getM_iMode() && isSigned() != SIGNED
			// && getSourBillTypeCode() != null
			// && !getSourBillTypeCode().startsWith("4")
			) {

			} else
				return;

			if (getBillCardPanel().getBillTable().getSelectedRows().length >= 1) {

				if (nc.ui.pub.beans.UIDialog.ID_CANCEL == nc.ui.pub.beans.MessageDialog
						.showOkCancelDlg(
								getClientUI(),
								null,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008busi", "UPP4008busi-000268")/*
																		 * @res
																		 * "将对本单据的所有成套件，自动配套处理，生成其它出、入库单据?"
																		 */)) {
					return;

				}

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "4008busi", "UPT40080602-000007")/* @res "配套" */);

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();

				try {
					// 如果是退回、退货操作检查表体数量是否有负数，有的话报错
					VOCheck.checkOutRetAssemblyVO(voBill);
				} catch (Exception e) {
					// 日志异常
					nc.vo.scm.pub.SCMEnv.out(e);
					showHintMessage(e.getMessage());
					return;
				}

				GeneralBillVO voBillclone = (GeneralBillVO) voBill.clone();

				ArrayList alOutGeneralVO = new ArrayList();
				ArrayList alInGeneralVO = new ArrayList();

				ArrayList aloutitem = new ArrayList();
				ArrayList alinitem = new ArrayList();
				int[] rownums = getBillCardPanel().getBillTable()
						.getSelectedRows();

				for (int i = 0; i < rownums.length; i++) {

					if (!isSetInv(voBill, rownums[i])
							|| isDispensedBill(voBill, rownums[i]))
						continue;

					// 表体行
					GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];// searchInvKit(voBill.getItemVOs()[rownums[i]]);
					UFDouble ufSetNum = null;

					ufSetNum = voParts.getNoutnum();
					voParts.setAttributeValue("nshouldinnum",
							voParts.getNoutnum());
					voParts.setAttributeValue("nneedinassistnum",
							voParts.getNoutassistnum());
					voParts.setAttributeValue("ninnum", voParts.getNoutnum());
					voParts.setAttributeValue("ninassistnum",
							voParts.getNoutassistnum());
					// after set null to noutnum and noutassistnum zhx 030616
					voParts.setAttributeValue("noutnum", null);
					voParts.setAttributeValue("noutassistnum", null);
					voParts.setAttributeValue("nshouldoutnum", null);
					voParts.setAttributeValue("nshouldoutassistnum", null);
					// soucebill
					voParts.setAttributeValue("csourcetype", voBill
							.getHeaderVO().getCbilltypecode());
					voParts.setAttributeValue("csourcebillhid", voBill
							.getHeaderVO().getPrimaryKey());
					voParts.setAttributeValue("csourcebillbid",
							voBill.getItemVOs()[rownums[i]].getPrimaryKey());
					voParts.setAttributeValue("vsourcebillcode", voBill
							.getHeaderVO().getVbillcode());
					voParts.setCgeneralbid(null);
					voParts.setCgeneralbb3(null);
					voParts.setCsourceheadts(null);
					voParts.setCsourcebodyts(null);
					voParts.setDbizdate(new nc.vo.pub.lang.UFDate(
							getEnvironment().getLogDate()));

					alinitem.add(voParts);
					// alOutGeneralVO.add(gbvoOUT);

					// 置其它入库VO，应是采购入库单据的子项存货。

					voParts.setLocator(null);// zhy
					GeneralBillItemVO[] tempItemVO = splitInvKit(voParts,
							voBillclone.getHeaderVO(), ufSetNum);
					if (tempItemVO != null && tempItemVO.length > 0) {
						for (int j = 0; j < tempItemVO.length; j++) {
							aloutitem.add(tempItemVO[j]);

						}

					} else {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000270")/*
																			 * @res
																			 * "配套中套件没有定义配件！请定义配件后再进行配套！"
																			 */);
						return;
					}
				}

				if (aloutitem.size() == 0 || alinitem.size() == 0)

					return;

				GeneralBillVO gbvoIn = new GeneralBillVO();
				voBill.getHeaderVO().setCoperatorid(
						getEnvironment().getUserID());
				voBill.getHeaderVO()
						.setDbilldate(
								new nc.vo.pub.lang.UFDate(getEnvironment()
										.getLogDate()));
				gbvoIn.setParentVO(voBill.getParentVO());
				gbvoIn.getHeaderVO().setPrimaryKey(null);
				gbvoIn.getHeaderVO().setVbillcode(null);
				gbvoIn.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherIn);
				gbvoIn.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem
						.size()];
				alinitem.toArray(inbodys);
				gbvoIn.setChildrenVO(inbodys);
				alInGeneralVO.add(gbvoIn);

				// 置出库VO

				GeneralBillVO gbvoOut = new GeneralBillVO();
				gbvoOut.setParentVO(voBillclone.getParentVO());
				gbvoOut.getHeaderVO().setPrimaryKey(null);
				gbvoOut.getHeaderVO().setVbillcode(null);
				gbvoOut.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherOut);
				gbvoOut.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoOut.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem
						.size()];
				aloutitem.toArray(outbodys);

				gbvoOut.setChildrenVO(outbodys);

				// 销售出库生成的配件出库单需要重置单据号
				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoOut,
						nc.vo.ic.pub.BillTypeConst.m_otherOut, "crowno");

				alOutGeneralVO.add(gbvoOut);

				getDispenseDlg(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000269")/*
													 * @res "配套生成：其它入/其它出库单"
													 */, alInGeneralVO,
						alOutGeneralVO).showModal();
				if (m_dlgInOut.isOK()) {
					try { // 更新表尾
						// setAuditBillFlag();
						filterNullLine();

						setDispenseFlag(
								(GeneralBillVO) ((GeneralBillVO) getM_alListData()
										.get(getM_iLastSelListHeadRow())),
								rownums);
						setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow())).clone());
						super.setButtonStatus(false);
						// 配套成功后，需要重置单据的按钮（删除，修改，复制按钮不可用，）

						// setBillState();
						// can not dispense the inv more over, after create
						// the other in and out bill!
						// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);
						ctrlSourceBillButtons(true);

					} catch (Exception e) {
						handleException(e);
						nc.ui.pub.beans.MessageDialog.showErrorDlg(
								getClientUI(), null, e.getMessage());
					}
				}
			}
		}

		/**
		 * 创建者：张欣 功能：批次汇总打印，对外提供补丁的方法 参数： 返回： 例外： 日期：(2003-12-18 下午 4:16)
		 * 修改日期，修改人，修改原因，注释标志： 2005-02-18 谢正南、王乃军重新添加之。
		 */
		public void onPrintLot() {

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000248")/* @res "正在打印，请稍候..." */);
			nc.vo.scm.pub.SCMEnv.out("打印批次汇总开始!\n");
			try {
				// 调出打印窗口
				// 依当前是列表还是表单而定打印内容
				if (getM_iMode() == BillMode.Browse
						&& getM_iCurPanel() == BillMode.Card) { // 浏览
					nc.vo.scm.pub.SCMEnv.out("打印批次汇总开始!表单打印!\n");
					// 准备数据
					GeneralBillVO vo = null;

					if (getM_iLastSelListHeadRow() != -1
							&& null != getM_alListData()
							&& getM_alListData().size() != 0) {
						vo = (GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow());
						if (getBillCardPanel().getHeadItem("vcustname") != null)
							vo.setHeaderValue("vcustname", getBillCardPanel()
									.getHeadItem("vcustname").getValue());
					}

					if (null == vo) {
						vo = new GeneralBillVO();
					}
					if (null == vo.getParentVO()) {
						vo.setParentVO(new GeneralBillHeaderVO());
					}
					if ((null == vo.getChildrenVO())
							|| (vo.getChildrenVO().length == 0)
							|| (vo.getChildrenVO()[0] == null)) {
						GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
						ivo[0] = new GeneralBillItemVO();
						vo.setChildrenVO(ivo);
					}

					if (getPrintEntry().selectTemplate() < 0)
						return;
					GeneralBillVO gvobak = (GeneralBillVO) vo.clone();
					// /
					nc.vo.scm.merge.DefaultVOMerger dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
					dvomerger.setGroupingAttr(new String[] { "cinventoryid",
							"castunitid" });
					dvomerger.setSummingAttr(new String[] { "nshouldoutnum",
							"nshouldoutassistnum", "noutnum", "noutassistnum",
							"nmny" });
					nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger
							.mergeByGroup(gvobak.getItemVOs());
					if (itemvosnew != null) {
						UFDouble udNum = null;
						UFDouble udMny = null;
						for (int k = 0; k < itemvosnew.length; k++) {
							udNum = itemvosnew[k].getNoutnum();
							udMny = itemvosnew[k].getNmny();
							if (udNum != null && udMny != null) {
								itemvosnew[k].setNprice(udMny.div(udNum));
							}
							nc.vo.scm.pub.SCMEnv.out("cinventoryid:"
									+ itemvosnew[k].getCinventoryid() + "\n");
							nc.vo.scm.pub.SCMEnv.out("castunitid:"
									+ itemvosnew[k].getCastunitid() + "\n");
							nc.vo.scm.pub.SCMEnv.out("Vbatchcode:"
									+ itemvosnew[k].getVbatchcode() + "\n");
							nc.vo.scm.pub.SCMEnv.out("noutnum:" + udNum + "\n");

						}

					}
					gvobak.setChildrenVO(itemvosnew);

					// /
					getDataSource().setVO(gvobak);

					// 向打印置入数据源，进行打印
					getPrintEntry().setDataSource(getDataSource());
					nc.vo.scm.pub.SCMEnv.out("打印批次汇总开始!表单打印结束!\n");
					getPrintEntry().preview();

				} else if (getM_iCurPanel() == BillMode.List) {
					// 列表

					nc.vo.scm.pub.SCMEnv.out("列表打印开始!\n");
					if (null == getM_alListData()
							|| getM_alListData().size() == 0) {
						return;
					}
					if (getPrintEntry().selectTemplate() < 0)
						return;
					ArrayList alBill = getSelectedBills();
					// 置小数精度
					setScaleOfListData(alBill);
					nc.vo.scm.pub.SCMEnv.out("列表打印:得到选中的单据并设置数量精度!\n");
					if (alBill == null)
						return;
					nc.vo.scm.merge.DefaultVOMerger dvomerger = null;
					for (int i = 0; i < alBill.size(); i++) {
						nc.vo.scm.pub.SCMEnv.out("列表打印:开始合并表体行!\n");
						GeneralBillVO gvobak = (GeneralBillVO) alBill.get(i);
						// /
						dvomerger = new nc.vo.scm.merge.DefaultVOMerger();
						dvomerger.setGroupingAttr(new String[] {
								"cinventoryid", "castunitid" });
						dvomerger.setSummingAttr(new String[] {
								"nshouldoutnum", "nshouldoutassistnum",
								"noutnum", "noutassistnum", "nmny" });
						nc.vo.ic.pub.bill.GeneralBillItemVO[] itemvosnew = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) dvomerger
								.mergeByGroup(gvobak.getItemVOs());
						nc.vo.scm.pub.SCMEnv.out("列表打印:得到合并后的表体行!\n");
						if (itemvosnew != null) {
							UFDouble udNum = null;
							UFDouble udMny = null;
							for (int k = 0; k < itemvosnew.length; k++) {
								udNum = itemvosnew[k].getNoutnum();
								udMny = itemvosnew[k].getNmny();
								if (udNum != null && udMny != null) {
									itemvosnew[k].setNprice(udMny.div(udNum));
								}
								nc.vo.scm.pub.SCMEnv.out("cinventoryid:"
										+ itemvosnew[k].getCinventoryid()
										+ "\n");
								nc.vo.scm.pub.SCMEnv.out("castunitid:"
										+ itemvosnew[k].getCastunitid() + "\n");
								nc.vo.scm.pub.SCMEnv.out("Vbatchcode:"
										+ itemvosnew[k].getVbatchcode() + "\n");
								nc.vo.scm.pub.SCMEnv.out("noutnum:" + udNum
										+ "\n");

							}
							gvobak.setChildrenVO(itemvosnew);
							alBill.set(i, gvobak);
						}

					}
					//
					nc.vo.scm.pub.SCMEnv.out("列表打印:得到合并后的单据!\n");
					getDataSource().setListVOs(alBill);
					getDataSource().setTotalLinesInOnePage(
							getPrintEntry().getBreakPos());
					// 向打印置入数据源，进行打印
					getPrintEntry().setDataSource(getDataSource());
					getPrintEntry().preview();

				} else
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000249")/*
																		 * @res
																		 * "只能在浏览状态下打印"
																		 */);
			} catch (Exception e) {
				SCMEnv.error(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPPSCMCommon-000061")/* @res "打印出错" */
						+ e.getMessage());
			}
		}

		/**
		 * 打印质证书
		 */
		private void onPrintCert() {
			Object obj = null;
			try {
				if (!m_isCheckQCstartup) {
					m_isQCstartup = nc.ui.ic.pub.tools.GenMethod
							.isProductEnabled(getEnvironment().getCorpID(),
									nc.vo.pub.ProductCode.PROD_QC);
					m_isCheckQCstartup = true;
				}
				if (!m_isQCstartup) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000492")/*
																			 * @res
																			 * "质量管理没有启用！"
																			 */);
					return;
				}
				// obj = InterServUI.getInterInstance(ProductCode.PROD_QC,
				// InterRegister.QC0002);
				// obj =
				// (CertService)NCLocator.getInstance().lookup(CertService.class.getName());

				// obj = (ICertInter)
				// NewObjectService.newInstance("qc",
				// "nc.ui.qc.inter.CertService.printCert");
				Class cl = Class.forName("nc.ui.qc.inter.CertService");
				obj = cl.newInstance();

			} catch (Exception e) {
				SCMEnv.error(e);
			}
			if (obj == null)
				return;
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			ClientLink client = new ClientLink(ce);
			GeneralBillVO voBill = null;
			if (getM_voBill() != null && getM_iCurPanel() == BillMode.Card) {
				// 在浏览状态下打印当前单据的质证书
				voBill = (GeneralBillVO) getM_voBill().clone();
				// ((IQcToIc_CertService) obj).printCert(this, voBill, client);
				((CertService) obj).printCert(getClientUI(), voBill, client);

			} else if (getM_iLastSelListHeadRow() != -1
					&& null != getM_alListData()
					&& getM_alListData().size() != 0) {
				voBill = (GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow());
				// ((IQcToIc_CertService) obj).printCert(this, voBill, client);
				((CertService) obj).printCert(getClientUI(), voBill, client);
			}
		}

		/**
		 * 方法功能描述：组装
		 * <p>
		 * <b>examples:</b>
		 * <p>
		 * 使用示例
		 * <p>
		 * <b>参数说明</b>
		 * <p>
		 * 
		 * @author liuzy
		 * @time 2007-7-5 下午01:25:44
		 */
		private void onAdd4L() {
			showHintMessage("");
			GeneralBillVO billVO4C = null;
			GeneralBillItemVO item4C = null;
			String sInvbasID = null;
			UFBoolean sSetpartsflag = null;
			try {
				if (getM_iCurPanel() == BillMode.List) {
					// 列表界面
					int seleHeadRow = getBillListPanel().getHeadTable()
							.getSelectedRow();
					if (seleHeadRow < 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000310")/*
																			 * @res
																			 * "请先选择一张单据和要生成组装单的成套件"
																			 */);
						return;
					}
					/*
					 * billVO4C = (GeneralBillVO)
					 * getBillListPanel().getSelectedVO(
					 * nc.vo.ic.pub.bill.GeneralBillVO.class.getName(),
					 * nc.vo.ic.pub.bill.GeneralBillHeaderVO.class.getName(),
					 * nc.vo.ic.pub.bill.GeneralBillItemVO.class.getName());
					 */

					billVO4C = (GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow());

					// 表体选择的行数
					int seleBodyRowCount = getBillListPanel().getBodyTable()
							.getSelectedRowCount();
					// 不是单行则返回
					if (seleBodyRowCount != 1)
						return;
					// 获得所选行号
					int seleBodyRow = getBillListPanel().getBodyTable()
							.getSelectedRow();
					if (seleBodyRow > -1) {
						// 根据行号获得所选行VO
						if (null != billVO4C.getChildrenVO()
								&& billVO4C.getChildrenVO().length > 0)
							item4C = (GeneralBillItemVO) billVO4C
									.getChildrenVO()[seleBodyRow];
					}
				} else {
					// billVO4C = (GeneralBillVO) m_voBill.clone();
					billVO4C = new GeneralBillVO();
					getBillCardPanel().getBillValueVO(billVO4C);
					if (null == billVO4C.getHeaderValue("cwarehousename")) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000318")/*
																			 * @res
																			 * "请先选择表头仓库"
																			 */);
						return;
					}
					int seleBodyRow = getBillCardPanel().getBodyPanel()
							.getTable().getSelectedRow();
					if (seleBodyRow > -1) {
						CircularlyAccessibleValueObject[] caVO = getM_voBill()
								.getChildrenVO();
						if (null != caVO && caVO.length > 0) {
							item4C = (GeneralBillItemVO) caVO[seleBodyRow];
						}
					} else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000312")/*
																			 * @res
																			 * "请选择存货为成套件的行"
																			 */);
						return;
					}
				}

				if (null != item4C)
					// 获得存货基本档案ID
					sInvbasID = item4C.getCinvbasid();
				if (null != sInvbasID && !"".equals(sInvbasID.trim())) {

					nc.itf.uap.bd.inv.IInventoryQry invQry = (nc.itf.uap.bd.inv.IInventoryQry) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									nc.itf.uap.bd.inv.IInventoryQry.class
											.getName());
					if (null != invQry) {
						GroupInventoryVO gInvVo = (GroupInventoryVO) invQry
								.findGroupInvHeaderByPK(sInvbasID);
						if (null == gInvVo)
							return;
						// 取成套件标志
						sSetpartsflag = gInvVo.getSetpartsflag();
						if (null != sSetpartsflag
								&& !sSetpartsflag.booleanValue()) {
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("40080802",
											"UPT40080802-000314")/*
																 * @res
																 * "选定的存货不是成套件"
																 */);
							// 如果不是成套件则返回
							return;
						}
						if (null != billVO4C) {
							// 准备数据
							billVO4C.setChildrenVO(new GeneralBillItemVO[] { item4C });
							try {
								// 如果是退回、退货操作检查表体数量是否有负数，有的话报错
								VOCheck.checkOutRetAssemblyVO(billVO4C);
							} catch (Exception e) {
								// 日志异常
								nc.vo.scm.pub.SCMEnv.out(e);
								showHintMessage(e.getMessage());
								return;
							}
							// VO数据交换
							SpecialBillVO specBillVO4L = (SpecialBillVO) nc.ui.pub.change.PfChangeBO_Client
									.pfChangeBillToBill(billVO4C, "4C", "4L");
							if (null == specBillVO4L) {
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("40080802",
												"UPT40080802-000316")/*
																	 * @res
																	 * "数据交换失败"
																	 */);
								return;
							}

							RefMsg msg = new RefMsg(this);
							msg.setBillVos(new SpecialBillVO[] { specBillVO4L });
							msg.setIBillOperate(ILinkType.LINK_TYPE_ADD);
							// this.setVisible(false);
							nc.ui.ic.ic231.ClientUI.openNodeAsDlg(
									getClientUI(), msg);
						}
					}
				}
			} catch (BusinessException e) {
				// 日志异常
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000320")/* @res "显示组装单界面出错：" */
						+ e.getMessage());
			}

		}

		protected void onRefInBill() {
			try {
				QryInBillDlg dlgBill = new QryInBillDlg("cgeneralhid",
						getEnvironment().getCorpID(), getEnvironment()
								.getUserID(), "40089907", "1=1", "45", null,
						null, "4C", getClientUI());

				if (dlgBill == null)
					return;

				nc.ui.ic.pub.pf.ICBillQuery dlgQry = new nc.ui.ic.pub.pf.ICBillQuery(
						getClientUI());
				// 加载源查询模版
				// dlgQry.setParent(this);
				dlgQry.setTempletID(getEnvironment().getCorpID(), "40080608",
						getEnvironment().getUserID(), null, "40089907");
				dlgQry.initData(getEnvironment().getCorpID(), getEnvironment()
						.getUserID(), "40089907", null, "4C", "45", null);
				// 修改人：刘家清 修改时间：2008-9-22 下午04:59:35 修改原因：业务类型按非直运过滤。
				dlgQry.setRefInitWhereClause("head.cbiztype", "业务类型",
						" verifyrule != 'Z' and (pk_corp='"
								+ getEnvironment().getCorpID()
								+ "' or pk_corp='@@@@') ", null);
				if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

					// 需要注册
					nc.vo.pub.query.ConditionVO[] voCons = dlgQry
							.getConditionVO();

					// 获取查询条件
					StringBuffer sWhere = new StringBuffer(" 1=1 ");
					if (voCons != null && voCons.length > 0
							&& voCons[0] != null) {
						sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
					}
					sWhere.append(" and head.cbiztype in (select pk_busitype from bd_busitype where verifyrule != 'Z' and (pk_corp='"
							+ getEnvironment().getCorpID()
							+ "' or pk_corp='@@@@')) ");

					// 加载源参照对话框
					dlgBill.initVar("cgeneralhid",
							getEnvironment().getCorpID(), getEnvironment()
									.getUserID(), null, sWhere.toString(),
							"45", null, null, "4C", null, this);

					dlgBill.setStrWhere(sWhere.toString());
					dlgBill.getBillVO();
					dlgBill.loadHeadData();
					dlgBill.addBillUI();
					dlgBill.setQueyDlg(dlgQry);

					nc.vo.scm.pub.ctrl.GenMsgCtrl
							.printHint("will load qrybilldlg");
					if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg closeok");
						// 获取所选VO
						nc.vo.pub.AggregatedValueObject[] vos = dlgBill
								.getRetVos();
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos");

						if (vos == null) {
							nc.vo.scm.pub.ctrl.GenMsgCtrl
									.printHint("qrybilldlg getRetVos null");
							return;
						}

						// //
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos is not null");
						nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
								.pfChangeBillToBillArray(vos, "45", "4C");
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");

						// 控制界面
						String cbiztype = null;
						if (voRetvos != null && voRetvos.length > 0)
							cbiztype = (String) voRetvos[0].getParentVO()
									.getAttributeValue("cbiztype");
						setBillRefResultVO(cbiztype, voRetvos);
						if (getM_voBill().getItemVOs().length > 0
								&& getM_voBill().getItemVOs()[0] != null
								&& getM_voBill().getItemVOs()[0].getNoutnum() != null) {
							setM_alSerialData(getM_voBill().getSNs());
							setM_alLocatorData(getM_voBill().getLocators());
						}

						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");

					}
				}
			} catch (Exception e) {

				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * 修改人：刘家清 修改时间：2008-6-26 下午08:03:02 修改原因：参照直运采购入库单生成销售出库单并自动关联直运销售订单。
		 * 
		 * 
		 */
		protected void onRefDirectInBill() {
			try {
				QryInBillDlg dlgBill = new QryInBillDlg("cgeneralhid",
						getEnvironment().getCorpID(), getEnvironment()
								.getUserID(), "40089907", "1=1", "45", null,
						null, "4C", getClientUI());

				if (dlgBill == null)
					return;

				nc.ui.ic.pub.pf.ICBillQuery dlgQry = new nc.ui.ic.pub.pf.ICBillQuery(
						getClientUI());
				// 加载源查询模版
				// dlgQry.setParent(this);
				dlgQry.setTempletID(getEnvironment().getCorpID(), "40080608",
						getEnvironment().getUserID(), null, "40089907");
				dlgQry.initData(getEnvironment().getCorpID(), getEnvironment()
						.getUserID(), "40089907", null, "4C", "45", null);

				// 修改人：刘家清 修改时间：2008-9-22 下午04:59:35 修改原因：业务类型按直运过滤。
				dlgQry.setRefInitWhereClause("head.cbiztype", "业务类型",
						" verifyrule = 'Z' and (pk_corp='"
								+ getEnvironment().getCorpID()
								+ "' or pk_corp='@@@@') ", null);

				if (dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

					// 需要注册
					nc.vo.pub.query.ConditionVO[] voCons = dlgQry
							.getConditionVO();

					// 获取查询条件
					StringBuffer sWhere = new StringBuffer(" 1=1 ");
					if (voCons != null && voCons.length > 0
							&& voCons[0] != null) {
						sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
					}
					sWhere.append(" and head.cbiztype in (select pk_busitype from bd_busitype where verifyrule = 'Z' and (pk_corp='"
							+ getEnvironment().getCorpID()
							+ "' or pk_corp='@@@@')) ");

					// 加载源参照对话框
					dlgBill.initVar("cgeneralhid",
							getEnvironment().getCorpID(), getEnvironment()
									.getUserID(), null, sWhere.toString(),
							"45", null, null, "4C", null, this);

					dlgBill.setStrWhere(sWhere.toString());
					dlgBill.getBillVO();
					dlgBill.loadHeadData();
					dlgBill.addBillUI();
					dlgBill.setQueyDlg(dlgQry);

					nc.vo.scm.pub.ctrl.GenMsgCtrl
							.printHint("will load qrybilldlg");
					if (dlgBill.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg closeok");
						// 获取所选VO
						nc.vo.pub.AggregatedValueObject[] vos = dlgBill
								.getRetVos();
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos");

						if (vos == null || 0 == vos.length
								|| null == vos[0].getChildrenVO()) {
							nc.vo.scm.pub.ctrl.GenMsgCtrl
									.printHint("qrybilldlg getRetVos null");
							return;
						}

						// //
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos is not null");
						nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
								.pfChangeBillToBillArray(vos, "45", "4C");
						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");
						// 关联直运销售订单，并且获取相关信息到销售出库单。
						voRetvos = (GeneralBillVO[]) GenMethod.callICService(
								"nc.bs.ic.ic211.GeneralHBO",
								"fillDirectSaleOrderInfo",
								new Class[] { voRetvos.getClass() },
								new Object[] { (GeneralBillVO[]) voRetvos });
						for (GeneralBillVO voRetvo : (GeneralBillVO[]) voRetvos)
							voRetvo.getHeaderVO().setBdirecttranflag(
									UFBoolean.TRUE);
						// 控制界面
						String cbiztype = null;
						if (voRetvos != null && voRetvos.length > 0)
							cbiztype = (String) voRetvos[0].getParentVO()
									.getAttributeValue("cbiztype");
						setBillRefResultVO(cbiztype, voRetvos);
						if (getM_voBill().getItemVOs().length > 0
								&& getM_voBill().getItemVOs()[0] != null
								&& getM_voBill().getItemVOs()[0].getNoutnum() != null) {
							setM_alSerialData(getM_voBill().getSNs());
							setM_alLocatorData(getM_voBill().getLocators());
						}

						nc.vo.scm.pub.ctrl.GenMsgCtrl
								.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");

					}
				}
			} catch (Exception e) {

				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * 销售出库对冲联查 v52。 功能： 参数： 返回： 例外： 日期：(2002-04-18 10:43:46)
		 * 修改日期，修改人，修改原因，注释标志：
		 */
		private void onRushQuery() {
			try {
				if (!GenMethod.isProductEnabled(getEnvironment().getCorpID(),
						ProductCode.PROD_SO)) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008busi", "UPP4008busi-000069")/*
																		 * @res
																		 * "销售产品没有启用"
																		 */);
					return;
				}
				if (BillMode.New == getM_iMode()
						|| BillMode.Update == getM_iMode()) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000506")/*
																			 * @res
																			 * "请单据保存之后再查询！"
																			 */);
					return;
				}
				String cgeneralhid = null;
				if (BillMode.List == getM_iCurPanel()) {
					if (getBillListPanel().getHeadTable().getSelectedRowCount() <= 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																				 * @
																				 * res
																				 * "没有选择单据"
																				 */);
						return;
					}
					cgeneralhid = (String) getBillListPanel()
							.getHeadBillModel().getValueAt(
									getBillListPanel().getHeadTable()
											.getSelectedRow(),
									IItemKey.CGENERALHID);
				} else {
					if (getM_voBill() != null
							&& getM_voBill().getHeaderVO() != null)
						cgeneralhid = getM_voBill().getHeaderVO()
								.getCgeneralhid();
				}
				if (nc.vo.ic.pub.GenMethod.isSEmptyOrNull(cgeneralhid)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																			 * @res
																			 * "没有选择单据"
																			 */);
					return;
				}

				RushLinkQueryVO[] rushvos = (RushLinkQueryVO[]) GenMethod
						.callICEJBService("nc.bs.ic.ic211.GeneralHBO",
								"queryRushLinkQueryVO", new Class[] {
										String.class, String.class },
								new Object[] { cgeneralhid,
										getEnvironment().getCorpID() });

				UIDialog uidlg = (UIDialog) Class
						.forName("nc.ui.scm.so.RushLinkQueryDlg")
						.getConstructor(
								new Class[] { Container.class,
										RushLinkQueryVO[].class })
						.newInstance(new Object[] { getClientUI(), rushvos });
				uidlg.showModal();

			} catch (Exception e) {
				GenMethod.handleException(getClientUI(), null, e);
			}
		}

		/**
		 * 方法功能描述：出库退回 v52add
		 * <p>
		 * <b>examples:</b>
		 * <p>
		 * 使用示例
		 * <p>
		 * <b>参数说明</b>
		 * <p>
		 * 
		 * @author yangb
		 * @time 2007-7-5 下午01:25:44
		 */
		private void onOutReturn() {

			try {

				if (getM_iMode() != BillMode.Browse) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000522")/*
																			 * @res
																			 * "未保存的单据,不能退回"
																			 */);
					return;
				}

				GeneralBillVO vo = getCurVO();
				if (vo == null) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmcommon", "UPPSCMCommon-000167")/*
																			 * @res
																			 * "没有选择单据"
																			 */);
					return;
				}

				int[] selrows = null;
				if (BillMode.List == getM_iCurPanel())
					selrows = getBillListPanel().getChildListPanel().getTable()
							.getSelectedRows();
				else
					selrows = getBillCardPanel().getBodyPanel().getTable()
							.getSelectedRows();
				if (selrows == null || selrows.length <= 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008other", "UPP4008other-000515")/*
																			 * @res
																			 * "没有选中的表体行。"
																			 */);
					return;
				}
				if (vo.getItemValue(0, IItemKey.CSOURCETYPE) != null
						&& vo.getItemValue(0, IItemKey.CSOURCETYPE).toString()
								.trim().equals(BillTypeConst.m_allocationOut)) {// startsWith("3")){
					showHintMessage(NCLangRes.getInstance().getStrByID(
							"40080802", "ClientUI-000000")/* 直运业务形成的销售出库单不能退回 */);
					return;

				}

				GeneralBillUICtl.getNaccumoutbacknumFromBB3(vo);

				vo = (GeneralBillVO) vo.clone();

				GeneralBillItemVO[] itemvos = vo.getItemVOs();
				GeneralBillItemVO[] selitemvos = new GeneralBillItemVO[selrows.length];
				for (int i = 0; i < selrows.length; i++)
					selitemvos[i] = itemvos[selrows[i]];
				vo.setChildrenVO(selitemvos);

				VOCheck.checkCanOutRet(vo);

				GeneralBillUICtl.processBillVOFor(vo,
						GeneralBillUICtl.Action.RETURN, m_bIsInitBill);

				// 当前是列表形式时，首先切换到表单形式
				if (BillMode.List == getM_iCurPanel())
					onSwitch();

				setM_voBill(vo);

				// 设置当前的条码框的条码 韩卫 2004-04-05
				if (getM_utfBarCode() != null)
					getM_utfBarCode().setCurBillItem(null);

				getBillCardPanel().getBillModel().setNeedCalculate(false);

				// 新增
				getBillCardPanel().addNew();
				setBillVO(getM_voBill());

				GeneralBillUICtl.processUIWhenRetOut(getBillCardPanel());

				for (int row = 0; row < getBillCardPanel().getBillModel()
						.getRowCount(); row++) {
					// 设置行状态为新增
					getBillCardPanel().getBillModel().setRowState(row,
							nc.ui.pub.bill.BillModel.ADD);
					getBillCardPanel().getBillModel().setValueAt(null, row,
							IItemKey.NAME_BODYID);
				}
				// 设置新增单据的初始数据，如日期，制单人等。
				setNewBillInitData();
				getBillCardPanel().setEnabled(true);
				setM_iMode(BillMode.New);

				GeneralBillUICtl.procFlagAndCalcNordcanoutnumAfterRefAdd(
						getM_voBill(), getBillCardPanel(), getBillType());

				// 设置单据号是否可编辑
				if (getBillCardPanel().getHeadItem("vbillcode") != null)
					getBillCardPanel().getHeadItem("vbillcode").setEnabled(
							m_bIsEditBillCode);

				setButtonStatus(true);

			} catch (Exception e) {
				GenMethod.handleException(getClientUI(), null, e);
			} finally {
				getBillCardPanel().getBillModel().setNeedCalculate(true);
			}

		}

	}

	public IButtonManager getButtonManager() {
		if (m_buttonManager == null) {
			try {
				m_buttonManager = new ButtonManager211(this);
			} catch (BusinessException e) {
				// 日志异常
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}

	/**
	 * 从单据表体行，查找出，是成套件的存货，并重构一个新的表体行VO[] 功能： 参数： 返回： 例外： 日期：(2002-04-18
	 * 11:29:23) 修改日期，修改人，修改原因，注释标志：
	 */
	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		ArrayList alInvKit = null;

		if (cvos != null) {
			if (cvos.getIsSet() != null && cvos.getIsSet().intValue() == 1)
				return cvos;
		}
		return null;
	}

	/**
	 * 创建者：王乃军 功能：在列表方式下选择一张单据 参数： 单据在alListData中的索引 返回：无 例外： 日期：(2001-11-23
	 * 18:11:18) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * 创建者：王乃军 功能：根据当前单据的待审状态决定签字/取消签字那个可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setBtnStatusSign() {
		setBtnStatusSign(true);
	}

	/**
	 * 创建者：王乃军 功能：根据当前单据的待审状态决定签字/取消签字那个可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {
		// 只在浏览状态下并且界面上有单据时控制
		if (BillMode.Browse != getM_iMode() || getM_iLastSelListHeadRow() < 0
				|| m_iBillQty <= 0) {
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			return;
		}
		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// 已签字，所以设置按钮状态,签字不可用，取消签字可用
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(true);
			// 不可删、改
			getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
					.setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
			getButtonManager()
					.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
					.setEnabled(false);

		} else if (NOTSIGNED == iSignFlag) {
			// 未签字，所以设置按钮状态,签字可用，取消签字不可用
			// 判断是否已填了数量，因为数量是完整的，所以只要检查第一行就行了。

			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					true);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// 可删、改
			if (isCurrentTypeBill()) {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
			// if (isSetInv(m_voBill, m_iFirstSelectRow) &&
			// !isDispensedBill(null))
			if (BillMode.Card == getM_iCurPanel()) {
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			}
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);

		} else { // 不可签字操作
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// 可删、改
			if (isCurrentTypeBill()) {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
		}
		// 使设置生效
		if (bUpdateButtons)
			updateButtons();

	}

	/**
	 * 创建者：王乃军 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setButtonsStatus(int iBillMode) {

		// 浏览模式下，有单据并且已经签字才可用
		if (getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG) != null) {
			if (iBillMode == BillMode.Browse && m_iBillQty > 0
					&& isSigned() == SIGNED)
				getButtonManager()
						.getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
						.setEnabled(true);
			else
				getButtonManager()
						.getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG)
						.setEnabled(false);
		}
		if (getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45) != null) {
			if (iBillMode == BillMode.Browse
					&& m_iBillQty > 0
					&& isSigned() == SIGNED
					&& null != getCurVO()
					&& (null == getCurVO().getHeaderVO().getBsalecooppur() || (null != getCurVO()
							.getHeaderVO().getBsalecooppur() && !getCurVO()
							.getHeaderVO().getBsalecooppur().booleanValue()))) {
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45)
						.setEnabled(true);
			} else
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_COOP_45)
						.setEnabled(false);
		}
		// in card browser status, can use dispense button.
		if (getButtonManager()
				.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE) != null) {
			if (getM_iCurPanel() == BillMode.Card
					&& iBillMode == BillMode.Browse && m_iBillQty > 0
					&& isSigned() != SIGNED)
				// if (isSetInv(m_voBill, m_iFirstSelectRow)
				// && !isDispensedBill(null))
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(
						false);
		}
		//
		// if (m_boVerify != null) {
		// if (iBillMode == BillMode.Browse
		// && m_iBillQty > 0)
		// m_boVerify.setEnabled(false);
		// else
		// m_boVerify.setEnabled(false);
		// }
		if (getButtonManager()
				.getButton(ICButtonConst.BTN_ASSIST_FUNC_REFER_IN) != null) {
			if (iBillMode == BillMode.Browse)
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_REFER_IN)
						.setEnabled(true);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_REFER_IN).setEnabled(
						false);

		}
		updateButtons();
		// 需要重新设置按钮以刷新子类按钮的状态。
		// super.initButtonsData();
		// m_vTopMenu.addElement(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_XCDG));
		// m_vBillMngMenu.addElement(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE));
		// m_vBillMngMenu.addElement(m_boVerify);
		// super.setButtons();
	}

	/**
	 * 单据配套之后，需要将已配套的标志置回VO中 功能： 参数： 返回： 例外： 日期：(2002-06-03 14:39:46)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();

			for (int i = 0; i < rownums.length; i++) {
				if (!isSetInv(gvo, rownums[i]))
					continue;
				resultvos[rownums[i]].setFbillrowflag(new Integer(
						nc.vo.ic.pub.BillRowType.afterConvert));
				alBid.add(resultvos[rownums[i]].getPrimaryKey());

			}

		}

	}

	/**
	 * 创建者：zhx 功能：成套件拆分配件的处理方法。 参数：成套件的存货管理ID，成套件数量（用于计算配件数量） 返回： 例外：
	 * 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 */
	public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo,
			GeneralBillHeaderVO headervo, UFDouble nsetnum) {

		if (itemvo == null)
			return null;
		String sInvSetID = itemvo.getCinventoryid();

		if (sInvSetID != null) {
			ArrayList alInvvo = new ArrayList();
			try {
				alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
			if (alInvvo == null) {
				nc.vo.scm.pub.SCMEnv.out("该成套件没有配件，请检查数据库...");
				return null;
			}
			int rowcount = alInvvo.size();
			GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
			nc.vo.pub.lang.UFDate db = new nc.vo.pub.lang.UFDate(
					getEnvironment().getLogDate());
			for (int i = 0; i < rowcount; i++) {
				voParts[i] = new GeneralBillItemVO();
				voParts[i].setInv((InvVO) alInvvo.get(i));
				voParts[i].setDbizdate(db);
				UFDouble nchildnum = ((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum") == null ? new UFDouble(
						0) : new UFDouble(((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum").toString());
				UFDouble ntotalnum = null;
				if (nsetnum != null)
					ntotalnum = nchildnum.multiply(nsetnum);
				else
					ntotalnum = nchildnum;
				UFDouble hsl = ((InvVO) alInvvo.get(i))
						.getAttributeValue("hsl") == null ? null
						: new UFDouble(((InvVO) alInvvo.get(i))
								.getAttributeValue("hsl").toString());
				UFDouble ntotalastnum = null;
				if (hsl != null && hsl.doubleValue() != 0) {
					ntotalastnum = ntotalnum.div(hsl);
				}

				voParts[i].setAttributeValue("nshouldoutnum", ntotalnum);
				voParts[i].setAttributeValue("nshouldoutassistnum",
						ntotalastnum);
				voParts[i].setAttributeValue("noutnum", ntotalnum);
				voParts[i].setAttributeValue("noutassistnum", ntotalastnum);
				voParts[i].setCsourceheadts(null);
				voParts[i].setCsourcebodyts(null);
				//
				voParts[i].setAttributeValue("csourcetype",
						headervo.getCbilltypecode());
				voParts[i].setAttributeValue("csourcebillhid",
						headervo.getPrimaryKey());
				voParts[i].setAttributeValue("csourcebillbid",
						itemvo.getPrimaryKey());
				voParts[i].setAttributeValue("vsourcebillcode",
						headervo.getVbillcode());
				voParts[i].setAttributeValue("creceieveid",
						itemvo.getCreceieveid());
				voParts[i].setAttributeValue("cprojectid",
						itemvo.getCprojectid());
				String s = "vuserdef";
				String ss = "pk_defdoc";
				for (int j = 0; j < 20; j++) {

					voParts[i]
							.setAttributeValue(
									s + String.valueOf(j + 1),
									itemvo.getAttributeValue(s
											+ String.valueOf(j + 1)));
					voParts[i]
							.setAttributeValue(
									ss + String.valueOf(j + 1),
									itemvo.getAttributeValue(ss
											+ String.valueOf(j + 1)));
				}
				voParts[i].setCgeneralhid(null);
				voParts[i].setCgeneralbid(null);
				voParts[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}
			return voParts;
		}
		return null;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-26 13:55:23)
	 * 
	 * @return nc.ui.bd.b21.CurrArith
	 * @param pk_corp
	 *            java.lang.String
	 */
	public ICBcurrArithUI getCurrArith() {

		if (clsCurrArith == null) {
			try {
				clsCurrArith = new ICBcurrArithUI(ClientEnvironment
						.getInstance().getCorporation().getPrimaryKey());
			} catch (Exception e) {
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null, e);
			}
		}

		return clsCurrArith;
	}

	/**
	 * 创建者：王乃军 功能：选中列表形式下的第sn张单据 参数：sn 单据序号 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void setListBodyData(GeneralBillItemVO voi[]) {
		getBB3Info(voi, getBillListPanel().getBodyBillModel());
		if (voi != null && voi.length > 0) {
			setCurrDigit(BillMode.List,
					(String) voi[0].getAttributeValue("cquotecurrency"));
		}
		super.setListBodyData(voi);
	}

	/**
	 * 创建者：王乃军 功能：在表单设置显示VO 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {
		if (bvo != null && bvo.getChildrenVO() != null
				&& bvo.getChildrenVO().length > 0) {
			getBB3Info(bvo.getItemVOs(), getBillCardPanel().getBillModel());
			setCurrDigit(BillMode.Card,
					(String) bvo.getChildrenVO()[0]
							.getAttributeValue("cquotecurrency"));
		}
		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-26 13:55:23)
	 * 
	 * @return nc.ui.bd.b21.CurrArith
	 * @param pk_corp
	 *            java.lang.String
	 */
	public void setCurrDigit(int ipanelsatus, String cquotecurrency) {

		if (cquotecurrency == null || cquotecurrency.trim().length() <= 0)
			return;

		int iDigit = 2;
		try {
			ICBcurrArithUI currtype = getCurrArith();

			Integer Digit = currtype.getBusiCurrDigit(cquotecurrency);

			if (Digit != null)
				iDigit = Digit.intValue();

			if (ipanelsatus == BillMode.Card) {
				BillItem item = getBillCardPanel().getBodyItem("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			} else {
				BillItem item = getBillListPanel().getBodyBillModel()
						.getItemByKey("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			}

		} catch (Exception e) {
			SCMEnv.error(e);
		}

	}

	/**
	 * UAP提供的编辑前控制
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 */
	public boolean isCellEditable(
			boolean value/* BillModel的isCellEditable的返回值 */,
			int row/* 界面行序号 */, String itemkey/* 当前列的itemkey */) {

		if (getM_iMode() == BillMode.Browse)
			return false;

		if (itemkey != null) {
			if (itemkey.equals(getEnvironment().getShouldAssistNumItemKey())) {
				InvVO voInv = getM_voBill().getItemInv(row);
				if (voInv != null && voInv.getIsAstUOMmgt() != null
						&& voInv.getIsAstUOMmgt().intValue() == 1)
					return true;
				else
					return false;
			}
			if (itemkey.equals(getEnvironment().getShouldNumItemKey()))
				return true;
		}
		return super.isCellEditable(value, row, itemkey);

	}

	/**
	 * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	public void getBB3Info(GeneralBillItemVO[] voi, BillModel md) {
		if (voi == null || voi.length <= 0 || md == null)
			return;
		UFBoolean bget_naccumoutbacknum = CheckTools.toUFBoolean(voi[0]
				.getAttributeValue(IItemKey.bget_naccumoutbacknum));
		if (bget_naccumoutbacknum != null
				&& bget_naccumoutbacknum.booleanValue())
			return;
		ArrayList<String> bb3field = new ArrayList<String>(4);
		ArrayList<String> vofield = new ArrayList<String>(4);
		IntList type = new IntList(4);
		String[] uikeys = new String[] { "naccinvoicenum", "naccountnum1",
				"nrushnum", IItemKey.naccumoutbacknum };
		String[] vofieldkeys = new String[] { "nsignnum", "naccountnum1",
				"nrushnum", IItemKey.naccumoutbacknum };
		BillItem item = md.getItemByKey(IItemKey.naccumoutbacknum);
		for (int i = 0; i < uikeys.length; i++) {
			item = md.getItemByKey(uikeys[i]);
			if (item != null && item.isShow()) {
				vofield.add(uikeys[i]);
				bb3field.add(vofieldkeys[i]);
				type.add(SmartFieldMeta.JAVATYPE_UFDOUBLE);
			}
		}
		if (vofield.size() > 0) {
			try {
				GeneralBillUICtl.fillBodyVOFromBB3(voi,
						vofield.toArray(new String[vofield.size()]),
						bb3field.toArray(new String[bb3field.size()]),
						type.toIntArray());
				for (int i = 0; i < voi.length; i++) {
					voi[i].setAttributeValue(IItemKey.bget_naccumoutbacknum,
							UFBoolean.TRUE);
				}
			} catch (Exception e) {
				GenMethod.handleException(null, null, e);
			}
		}
	}

	/**
	 * 过滤单据参照 创建者：张欣 功能：初始化参照过滤 参数： 返回： 例外： 日期：(2001-7-17 10:33:20)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void filterRef(String sCorpID) {
		try {
			super.filterRef(sCorpID);
			// 过滤仓库参照
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			RefFilter.filtWh(bi, sCorpID, null);
			// if(getM_iMode()==BillMode.Browse){
			// RefFilter.filtWh(bi, sCorpID,null);
			// }else{
			// RefFilter.filtWh(bi, sCorpID,
			// new String[] { "and isdirectstore = 'N'" });
			// }
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	}

	@Override
	public QueryDlgHelp getQryDlgHelp() {
		if (m_qryDlgHelp == null) {
			m_qryDlgHelp = new QueryDlgHelp(this);
		}
		return (QueryDlgHelp) m_qryDlgHelp;
	}

	public void onCoop45Save() {
		showHintMessage("");
		GeneralBillVO currBillVO = getCurVO();

		if (null == currBillVO || null == currBillVO.getItemVOs()
				|| currBillVO.getItemVOs().length == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40080802", "UPT40080802-000321"));// 请选择一张单据
			return;
		}
		String pk_cusmandoc = (String) currBillVO
				.getHeaderValue(IItemKey.ccustomerid);
		if (null == pk_cusmandoc) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40080802", "UPT40080802-000322"));// "表头客户为空，不能协同")
			return;
		}
		// 修改人：刘家清 修改时间：2008-10-14 下午08:15:21 修改原因：非协同客户也能协同 --对对对！ by liuzy
		// Object[][] objResult =
		// nc.ui.scm.pub.cache.CacheTool.getMultiColValue(
		// "bd_cumandoc", "pk_cumandoc", new String[] {
		// "cooperateflag"
		// }, new String[] {
		// pk_cusmandoc
		// });
		// if (null != objResult && objResult.length > 0 && null != objResult[0]
		// && objResult.length > 0) {
		// UFBoolean cooperateflag = nc.vo.scm.ic.bill.SwitchObject
		// .switchObjToUFBoolean(objResult[0][0]);
		// if (!cooperateflag.booleanValue()) {
		// showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","UPT40080802-000323"));//"客户非协同客户，不能协同");
		// return;
		// }
		// }
		currBillVO.getHeaderVO().setAttributeValue("coperatoridnow",
				getEnvironment().getUserID());
		ArrayList<GeneralBillVO> al_splitBills = filterAndSplitVOs((GeneralBillVO) currBillVO
				.clone());// 克隆一个对象，否则协同未成功，标志也被置上了
		if (null == al_splitBills)
			return;
		if (null != al_splitBills && al_splitBills.size() > 0) {
			GeneralBillVO[] vos = new GeneralBillVO[al_splitBills.size()];
			al_splitBills.toArray(vos);
			// 给最后一个VO表头置协同标志
			((GeneralBillHeaderVO) (vos[vos.length - 1].getParentVO()))
					.setBsalecooppur(UFBoolean.TRUE);
			try {
				Object[] obj = nc.ui.pub.pf.PfUtilClient.runBatch(this,
						"COOPPO", ScmConst.m_saleOut, getEnvironment()
								.getLogDate(), vos, vos, null, null);
				if (null != obj && obj.length > 0) {
					int count = obj.length;
					ArrayList<String> al_billcodes = new ArrayList<String>();
					for (int i = 0; i < obj.length; i++) {
						al_billcodes.add((String) obj[i]);
					}
					if (count > 0 && al_billcodes.size() > 0) {
						String msg = nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40080802", "UPT40080802-000324",
										null,
										new String[] { String.valueOf(count) });// "协同生成"
																				// +
																				// count
																				// +
																				// "张采购入库单，单据号如下：";
						for (int i = 0; i < al_billcodes.size(); i++) {
							if (i % 2 == 0)
								msg += "\n";
							msg += al_billcodes.get(i) + ",";
						}
						getCurVO().getHeaderVO()
								.setBsalecooppur(UFBoolean.TRUE);
						if (BillMode.List == getM_iCurPanel()) {
							int selrow = getBillListPanel().getHeadTable()
									.getSelectedRow();
							getBillListPanel().getHeadBillModel().setValueAt(
									UFBoolean.TRUE, selrow,
									IItemKey.bsalecooppur);
						} else
							getBillCardPanel().getHeadItem(
									IItemKey.bsalecooppur).setValue(
									UFBoolean.TRUE);
						// showYesNoMessage(msg);
						nc.ui.pub.beans.MessageDialog
								.showHintDlg(
										this,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("SCMCOMMON",
														"UPPSCMCommon-000270")/*
																			 * @res
																			 * "提示"
																			 */,
										msg);
						setButtonsStatus(getM_iMode());
					}
				}
			} catch (Exception e) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000329")
						+ "\n" + e.getMessage());
				SCMEnv.out(e.getMessage());
			}
		}
	}

	private ArrayList<GeneralBillVO> filterAndSplitVOs(GeneralBillVO currBillVO) {
		// 过滤不符合条件的表体，以及重新组织单据，把来源单据表体ID相同的行重新组织到一个单据VO中
		GeneralBillItemVO[] items = currBillVO.getItemVOs();
		ArrayList<String> al_30bids = new ArrayList<String>();
		ArrayList<GeneralBillItemVO> al_repeatitems = new ArrayList<GeneralBillItemVO>();
		ArrayList<GeneralBillItemVO> al_items = new ArrayList<GeneralBillItemVO>();
		ArrayList<GeneralBillVO> al_splitBills = new ArrayList<GeneralBillVO>();

		do {
			if (al_repeatitems.size() > 0) {
				items = new GeneralBillItemVO[al_repeatitems.size()];
				al_repeatitems.toArray(items);
				al_repeatitems.clear();
				al_30bids.clear();
				al_items.clear();
			}
			int i = 0;
			// modified by liuzy 2008-10-31 上午09:05:29
			// 销售订单与销售出库单之间可能会有其它单据，如：发货单、销售发票
			// 与需求谢阳、开发经理杨波确认，一般情况下可认为销售出库单的源头单据是销售订单
			String sCfirstbillbid = null;
			for (GeneralBillItemVO item : items) {
				sCfirstbillbid = item.getCfirstbillbid();
				if (!ScmConst.SO_Order.equals(item.getCfirsttype())
						|| null == sCfirstbillbid || "".equals(sCfirstbillbid)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40080802", "UPT40080802-000325"));// "该单据不符合协同生成采购入库单的条件：来源单据必须是销售订单");
					return null;
				}

				if (!al_30bids.contains(sCfirstbillbid)) {
					al_30bids.add(sCfirstbillbid);
					al_items.add(item);
				} else
					al_repeatitems.add(item);
				i++;
			}
			GeneralBillVO vo = new GeneralBillVO();
			GeneralBillItemVO[] itemvos = new GeneralBillItemVO[al_items.size()];
			itemvos = al_items.toArray(itemvos);
			vo.setParentVO(currBillVO.getParentVO());
			vo.setChildrenVO(itemvos);
			al_splitBills.add(vo);

		} while (al_repeatitems.size() > 0);
		return al_splitBills;
	}
	
	@Override
	public boolean beforeEdit(BillItemEvent e) {
		
		if("storprice".equals(e.getItem().getKey())) {
			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();
			//　0001A810000000000JB9　-> 自提
			if("0001A810000000000JB9".equals(getBillCardPanel().getHeadItem("cdilivertypeid").getValueObject()))
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 0 ");
			else 
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 1 and pk_stordoc = '"+cwarehouseid+"' ");
		} else if("transprice".equals(e.getItem().getKey())) {
			// 0001A810000000000JB9 -> 自提
			if("0001A810000000000JB9".equals(getBillCardPanel().getHeadItem("cdilivertypeid").getValueObject()))
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 0 ");
			else 
				((UIRefPane)e.getItem().getComponent()).setWhereString(" 1 = 1 and nvl(dr,0)=0 and pk_corp = '"+getCorpPrimaryKey()+"' and vbillstatus = 1 and pk_transport = '"+getBillCardPanel().getHeadItem("pk_transport").getValueObject()+"'");
		}
		
		return super.beforeEdit(e);
	}
	
	/**
	 * 重写表头编辑后处理
	 */
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
			if("transprice".equals(e.getKey()))
				afterTransprice(e);
		
			else if("storprice".equals(e.getKey()))
				afterStorprice(e);
			
			else if("cdilivertypeid".equals(e.getKey()))
				afterCdilivertypeid(e);
				
		} catch(Exception e1) {
			Logger.error(e1.getMessage());
		}
			
	}
	
	protected final void afterTransprice(BillEditEvent e) throws Exception {
		getBillCardPanel().getHeadItem("pk_transport_b").setValue(((UIRefPane)e.getSource()).getRefPK());
		
	}
	
	protected final void afterStorprice(BillEditEvent e) throws Exception {
		getBillCardPanel().getHeadItem("pk_storcontract_b").setValue(((UIRefPane)e.getSource()).getRefPK());
		
	}
	
	protected final void afterCdilivertypeid(BillEditEvent e) throws Exception {
		
		if("自提".equals(((UIRefPane)e.getSource()).getRefName())) {
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setPK(null);
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setValue(null);
			getBillCardPanel().getHeadItem("pk_storcontract_b").setValue(null);
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setEnabled(false);
			
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setPK(null);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setValue(null);
			getBillCardPanel().getHeadItem("pk_transport_b").setValue(null);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setEnabled(false);
		} else {
			((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).setEnabled(true);
		}
		
	}
	
	/**
	 * 重写保存方法，保存自定义添加的几个字段的值
	 * 
	 * Overrider ： river
	 * 
	 * Date ： 2012-09-13
	 * 
	 */
	@Override
	public boolean onSave() {
		boolean retType = false;
		
		GeneralBillVO voInputBill = getBillVO();
		
		/* 在此添加验证 ： 如果运输方式为自提时运输单价及装卸费不需要填写 ， 否则这2个字段为必填  by river */
		if(voInputBill != null && voInputBill.getParentVO() != null) {
			
			// 读取销售订单中的 [ 是否自提 ] ，以判断运输单价是否必填。
			CircularlyAccessibleValueObject[] bodyVOs = voInputBill.getChildrenVO();
			if(bodyVOs != null && bodyVOs.length > 0) {
				Object csourcebillhid = bodyVOs[0].getAttributeValue("csourcebillhid");
				Object issince = null;
				try { issince = UAPQueryBS.getInstance().executeQuery("select issince from so_sale where csaleid = '"+csourcebillhid+"' ", new ColumnProcessor()); } catch(Exception e) { Logger.error(e.getMessage(), e, this.getClass(), "onSave"); }
				if(!(issince != null && "Y".equals(issince))) {
					if(((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).getRefName() == null) {
						showErrorMessage("表头 [ 运输单价 ] 不能为空。");
						return false;
					}
				}
			
			}
			
		}
		
		retType = super.onSave();
		
		afterOnSave(voInputBill , retType);
		
		return retType;
	}
	
	/**
	 * 功能 ： 保存后续操作
	 * 
	 * @author river
	 * 
	 * Create date : 2012-09-19
	 * 
	 * @param voInputBill
	 * @param retType
	 */
	protected final void afterOnSave(GeneralBillVO voInputBill , boolean retType) {
		
		GeneralBillVO newBill = getBillVO();
		
		if(retType && newBill != null && newBill.getPrimaryKey() != null) {
			
			Object concode = voInputBill.getParentVO().getAttributeValue("concode");
			Object pk_transport = voInputBill.getParentVO().getAttributeValue("pk_transport");
			Object transprice = ((UIRefPane)getBillCardPanel().getHeadItem("transprice").getComponent()).getRefName();
			Object storprice = ((UIRefPane)getBillCardPanel().getHeadItem("storprice").getComponent()).getRefName();
			Object contracttype = voInputBill.getParentVO().getAttributeValue("contracttype");
			Object salecode = voInputBill.getParentVO().getAttributeValue("salecode");
			Object pk_storcontract_b = getBillCardPanel().getHeadItem("pk_storcontract_b").getValueObject();
			Object pk_transport_b = getBillCardPanel().getHeadItem("pk_transport_b").getValueObject();
			Object pk_contract = voInputBill.getParentVO().getAttributeValue("pk_contract");
			
			newBill.getParentVO().setAttributeValue("transprice", transprice);
			newBill.getParentVO().setAttributeValue("storprice", storprice);
			newBill.getParentVO().setAttributeValue("pk_storcontract_b", pk_storcontract_b);
			newBill.getParentVO().setAttributeValue("pk_transport_b", pk_transport_b);
			
			try { 
				
				String sqlField = ConvertFunc.change(new String[]{
						" concode = '" + (concode == null ? "" : concode) + "' " ,
						" pk_transport = '" + (pk_transport == null ? "" : pk_transport) + "' " ,
						" transprice = '" + (transprice == null ? "" : transprice) + "' " ,
						" storprice = '" + (storprice == null ? "" : storprice) + "' " ,
						" contracttype = '" + (contracttype == null ? "" : contracttype) + "' " ,
						" salecode = '" + (salecode == null ? "" : salecode) + "' " ,
						" pk_storcontract_b = '" + (pk_storcontract_b == null ? "" : pk_storcontract_b) + "' " ,
						" pk_transport_b = '" + (pk_transport_b == null ? "" : pk_transport_b) + "' " ,
						" pk_contract = '"+(pk_contract == null ? "" : pk_contract)+"' " ,
				});
				
				UAPQueryBS.getInstance().executeQuery("update ic_general_h set "+sqlField+" where cgeneralhid = '"+newBill.getPrimaryKey()+"'", null); 
				
			} catch(Exception ex) { }
			
			// 更新数据后对TS字段重新取值，解决后续签字提示被修改、删除
			try {
				Object ts = UAPQueryBS.getInstance().executeQuery("select ts from ic_general_h where cgeneralhid = '"+newBill.getPrimaryKey()+"'", new ColumnProcessor());
				newBill.getParentVO().setAttributeValue("ts", ts);
				
				updateBillToList(newBill);
				
			} catch(Exception e) {}
		}
		
	}
	
}