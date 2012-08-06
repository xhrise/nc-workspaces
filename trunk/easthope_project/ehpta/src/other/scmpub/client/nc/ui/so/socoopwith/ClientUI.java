package nc.ui.so.socoopwith;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPasswordField;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIRefPaneTextField;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillItemNumberFormat;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.UITextAreaScrollPane;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.CompositeMetaVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.socoopwith.CoopwithHeaderVO;
import nc.vo.scm.socoopwith.CoopwithItemVO;
import nc.vo.scm.socoopwith.CoopwithVO;

public class ClientUI extends ToftPanel implements BillEditListener,
		BillEditListener2, BillCardBeforeEditListener, ItemListener,
		BillTableMouseListener, ValueChangedListener {
	private static final long serialVersionUID = 3328186023217215126L;

	private boolean isList = true;

	private String pk = null;

	private String[] pks = null;

	private CoopwithHeaderVO[] headervos = null;
	private CoopwithHeaderVO preheadervo = null;

	private int curPage = 0;

	private CompositeMetaVO composevo = new CompositeMetaVO();

	/*
	 * 按钮的状态编码 0-->常态 1-->增加按钮点击后的状态 2-->列表显示 3-->update 4-->卡片显示
	 * 5-->保存6-->编辑7-->复制
	 * 
	 */
	private int btnStatus = 0;

	/*
	 * 是否处于保存状态
	 */
	private boolean isSave = true;

	private int preid = 0;

	private String strWhere = null;

	private final String stockOrder = "21";

	private final String sellOrder = "30";

	private BillCardPanel m_cardpanel = null;

	private BillListPanel m_listpanel = null;

	private QueryDlg queryClient = null;

	private ButtonObject bo_Add = null; // 增加

	private ButtonObject bo_Save = null;// 保存

	private ButtonObject bo_Modify = null;// 维护

	private ButtonObject bo_Search = null;// 查询

	private ButtonObject bo_Look = null;// 浏览

	private ButtonObject bo_CardShow = null;// 卡片

	// 以下是维护按钮的子按钮
	private ButtonObject ch_Modify = null;// 修改

	private ButtonObject ch_Cancel = null;// 取消

	private ButtonObject ch_Delete = null;// 删除

	private ButtonObject ch_Copy = null;// 复制

	// 以下是浏览按钮的子按钮
	private ButtonObject ch_Refresh = null;// 刷新

	private ButtonObject ch_FirstPage = null;// 首页

	private ButtonObject ch_PrePage = null;// 上页

	private ButtonObject ch_NextPage = null;// 下页

	private ButtonObject ch_LastPage = null;// 末页

	// 按钮数组
	private ButtonObject[] cardbutton_Arry = null;

	private ButtonObject[] listbutton_Arry = null;

	public ClientUI() {
		super();
		init();
		initListener();
		onListShow();
	}

	private void init() {
		initButtons();
		preid = btnStatus;
		btnStatus = 0;// 按钮处于初始态
		changeStatus();
		cardbutton_Arry = new ButtonObject[] { bo_Add, bo_Save, bo_Modify,
				bo_Search, bo_Look, bo_CardShow };
		listbutton_Arry = new ButtonObject[] { bo_Add, bo_Save, bo_Modify,
				bo_Search, bo_Look, bo_CardShow };
	}

	@SuppressWarnings( { "deprecation", "deprecation", "deprecation",
			"deprecation" })
	private void initButtons() {
		// TODO huzy1 按钮加载使用ButtonTree模式
		ButtonTree btnTree = null;
		try {
			btnTree = new ButtonTree("400106");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
		}
		bo_Add = btnTree.getButton("增加");// 增加
		bo_Save = btnTree.getButton("保存");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000002"));// 保存
		bo_Modify = btnTree.getButton("维护");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000003"));// 维护
		bo_Search = btnTree.getButton("查询");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000004"));// 查询
		bo_Look = btnTree.getButton("浏览");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000005"));// 浏览
		bo_CardShow = btnTree.getButton("卡片显示");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000006"));
		// 以下是维护按钮的子按钮
		ch_Modify = btnTree.getButton("修改");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000008"));// 修改
		ch_Cancel = btnTree.getButton("取消");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000009"));// 取消
		ch_Delete = btnTree.getButton("删除");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000010"));// 删除
		ch_Copy = btnTree.getButton("复制");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000011"));// 复制
		// 以下是浏览按钮的子按钮a
		ch_Refresh = btnTree.getButton("刷新");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000012"));// 刷新
		ch_FirstPage = btnTree.getButton("首页");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000013"));// 首页
		ch_PrePage = btnTree.getButton("上页");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000014"));// 上页
		ch_NextPage = btnTree.getButton("下页");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000015"));// 下页
		ch_LastPage = btnTree.getButton("末页");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000016"));// 末页
		// 为按钮添加子按钮
		// @SuppressWarnings
		bo_Modify.addChileButtons(new ButtonObject[] { ch_Modify, ch_Cancel,
				ch_Delete, ch_Copy });
		bo_Look.addChileButtons(new ButtonObject[] { ch_Refresh, ch_FirstPage,
				ch_PrePage, ch_NextPage, ch_LastPage });
	}

	private BillCardPanel getCardPanel() {
		if (m_cardpanel == null) {
			m_cardpanel = new BillCardPanel();
			m_cardpanel.loadTemplet("0001AA1000000000J49Z");
			// 加在模板数据
			m_cardpanel.getBillTable("so_coopwith_b").removeSortListener();
			m_cardpanel.getBillTable("in_out_set").removeSortListener();
			getCardPanel().getHeadItem("cbusitypeid").setEdit(false);
			getCardPanel().getHeadItem("ctargetbusitypeid").setEdit(false);
			getCardPanel().getHeadItem("csourceorgid").setEdit(false);
			getCardPanel().getHeadItem("ctargetorgid").setEdit(false);
		}
		return m_cardpanel;
	}

	private BillListPanel getListPanel() {
		if (m_listpanel == null) {
			m_listpanel = new BillListPanel();
			// TODO huzy1 不能这样加载模板 by liuzy
			m_listpanel.loadTemplet("0001AA1000000000J49Z");
		}
		return m_listpanel;
	}

	private QueryDlg getQueryDlg() {
		if (queryClient == null) {
			queryClient = new QueryDlg();
		}
		return queryClient;
		// if (queryClient == null) {
		// nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
		// .getInstance();
		// String m_sUserID = ce.getUser().getPrimaryKey();
		// String m_sLoginCorpID = ce.getCorporation().getPrimaryKey();
		// TemplateInfo tempinfo = new TemplateInfo();
		// tempinfo.setPk_Org(m_sLoginCorpID);
		// tempinfo.setCurrentCorpPk(m_sLoginCorpID);
		// tempinfo.setFunNode(getModuleCode());
		// tempinfo.setUserid(m_sUserID);
		//
		// queryClient = new QueryDialog(this, tempinfo);
		//
		// // 设置组合字段
		//			 
		// composevo.setActiveFieldCode("ccustomerid");
		// composevo.setPassiveFieldCode("cbusitypeid");
		// tempinfo.addComposeMetaVO(composevo);
		// queryClient.registerFilterEditorFactory(new IFilterEditorFactory() {
		// public IFilterEditor createFilterEditor(IFilterMeta meta) {
		// if ("ccustomerid".equals(meta.getFieldCode())) {
		// FilterMeta activeMeta = (FilterMeta) meta;
		// List<CompositeMetaVO> listvos = queryClient.getQueryContext()
		// .getTempInfo().getComposeVos();
		// if (listvos != null) {
		// for (CompositeMetaVO cpvo : listvos) {
		// if (meta.getFieldCode().equals(
		// cpvo.getPassiveFieldCode())) {
		// activeMeta = composevo.getActiveFiltermeta();
		// UIRefPane refPane = new
		// UIRefpaneCreator(queryClient.getQueryContext()).createUIRefPane(activeMeta);
		// String pk_corp=refPane.getPk_corp();
		// UIRefPane refPane1 = new
		// UIRefpaneCreator(queryClient.getQueryContext()).createUIRefPane((FilterMeta)meta);
		//									
		// StringBuilder wherePart = new StringBuilder();
		// wherePart
		// .append(" (bd_busitype.pk_busitype in (select
		// pub_billbusiness.pk_businesstype from bd_busitype inner join
		// pub_billbusiness on pub_billbusiness.pk_businesstype
		// =bd_busitype.pk_busitype where pub_billbusiness.pk_corp in ('");
		// wherePart.append(pk_corp.trim()).append("' ,'@@@@') and
		// pk_billType='30'))");
		// refPane1.setWhereString(wherePart.toString());
		// break;
		// }
		// }
		// }
		// return new DefaultCompositeFilterEditor(
		// queryClient.getQueryContext(), activeMeta, meta);
		// }
		// return null;
		// }
		// });

		// queryClient
		// .registerFieldValueEelementEditorFactory(new
		// IFieldValueElementEditorFactory() {
		// public IFieldValueElementEditor createFieldValueElementEditor(
		// FilterMeta meta) {
		// if ("cbusitypeid".equals(meta
		// .getFieldCode())) {
		// StringBuilder wherePart = new StringBuilder();
		// wherePart
		// .append(" (bd_busitype.pk_busitype in (select
		// pub_billbusiness.pk_businesstype from bd_busitype inner join
		// pub_billbusiness
		// on pub_billbusiness.pk_businesstype =bd_busitype.pk_busitype
		// where
		// pub_billbusiness.pk_corp in ('");
		// wherePart.append(getCorpPrimaryKey().trim()).append(
		// "' ,'@@@@') and pk_billType='30'))");
		// UIRefPane refPane = new UIRefpaneCreator(
		// queryClient.getQueryContext())
		// .createUIRefPane(meta);
		// refPane.setWhereString(wherePart.toString().trim());
		// return new RefElementEditor(refPane, meta
		// .getReturnType());
		// }
		// return null;
		// }
		// });
		// }
		// return queryClient;
	}

	private String getBillType() {
		return "scmcoop";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "coopwith settings";
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if (bo == bo_Add) { // 增加 按钮点击事件的响应
			onAdd();
		} else if (bo == bo_Save) { // 保存 按钮点击事件的响应
			onSave();

		} else if (bo == bo_Search) { // 查询 按钮点击事件的响应
			onQuery();
		} else if (bo == bo_CardShow
				&& bo_CardShow.getName().trim().equals(
						NCLangRes.getInstance().getStrByID("400106",
								"ClientUI-000032")/* "卡片显示" */)) {// 卡片显示
			// 按钮点击事件的响应
			onCardShow();
		} else if (bo == bo_CardShow
				&& bo_CardShow.getName().trim().equals(
						NCLangRes.getInstance().getStrByID("400106",
								"ClientUI-000033")/* "列表显示" */)) {// 列表
			// 按钮点击事件的响应
			onListShow();
		} else if (bo == ch_Modify) {// 修改 按钮点击事件的响应
			onEdite();
		} else if (bo == ch_Cancel) {// 取消 按钮点击事件的响应
			onCancel();
		} else if (bo == ch_Delete) {// 删除 按钮点击事件的响应
			onDelete();
		} else if (bo == ch_Copy) {// 复制 按钮点击事件的响应
			onCopy();
		} else if (bo == ch_Refresh) {// 刷新 按钮点击事件的响应
			onRefresh();
		} else if (bo == ch_FirstPage) {// 首页 按钮点击事件的响应
			onFirstPage();
		} else if (bo == ch_PrePage) {// 上页 按钮点击事件的响应
			onPrePage();
		} else if (bo == ch_NextPage) {// 下一页 按钮点击事件的响应
			onNextPage();
		} else if (bo == ch_LastPage) {// 末页 按钮点击事件的响应
			onLastPage();
		}
	}

	private void onLastPage() {
		// TODO Auto-generated method stub
		if (pks != null && pks.length > 0)
			curPage = pks.length - 1;
		else
			return;
		showData();
	}

	private void onNextPage() {
		// TODO Auto-generated method stub
		if (null == pks || pks.length == 0)
			return;
		if (curPage < pks.length - 1)
			curPage++;
		showData();
	}

	private void onPrePage() {
		// TODO Auto-generated method stub
		if (null == pks || pks.length == 0)
			return;
		if (curPage > 0)
			curPage--;
		showData();
	}

	private void onFirstPage() {
		// TODO Auto-generated method stub
		curPage = 0;
		showData();
	}

	public void onCancel() {
		/*
		 * 按钮的状态编码 0-->常态 1-->增加按钮点击后的状态 2-->列表显示 3-->update 4-->卡片显示
		 * 5-->保存6-->编辑7-->复制
		 */
		if (!isList) {
			if (btnStatus == 6 && preid != 2) {
				getCardPanel().getBillData().clearViewData();
				getCardPanel().setEnabled(false);
				bo_Add.setEnabled(true);
				bo_Save.setEnabled(false);
				bo_Modify.setEnabled(false);
				bo_CardShow.setEnabled(true);
				setButtons(cardbutton_Arry);
				updateUI();
			} else if (btnStatus == 1 && preid == 2) {
				onListShow();
			} else if (preid == 2) {
				onListShow();
			} else if (preid == 6) {
				getCardPanel().getBillData().clearViewData();
				getCardPanel().setEnabled(false);
				bo_Add.setEnabled(true);
				bo_Save.setEnabled(false);
				bo_Modify.setEnabled(false);
				setButtons(cardbutton_Arry);
				updateUI();
			} else if (preid == 3 && btnStatus == 7) {
				bo_CardShow.setEnabled(true);
				onListShow();
			} else if (btnStatus == 3) {
				bo_Add.setEnabled(true);
				bo_Modify.setEnabled(true);
				bo_Save.setEnabled(false);
				ch_Cancel.setEnabled(false);
				ch_Copy.setEnabled(true);
				ch_Delete.setEnabled(true);
				ch_Modify.setEnabled(true);
				bo_CardShow.setEnabled(true);
				setButtons(cardbutton_Arry);
				preCardView();
				getCardPanel().setEnabled(false);
			} else if (btnStatus == 1 && preid == 5) {
				bo_CardShow.setEnabled(true);
				onListShow();
			} else if (btnStatus == 1 && preid == 3) {
				getCardPanel().getBillData().clearViewData();
				getCardPanel().setEnabled(false);
				bo_Add.setEnabled(true);
				bo_Save.setEnabled(false);
				bo_Modify.setEnabled(false);
				bo_Search.setEnabled(true);
				bo_Look.setEnabled(false);
				bo_CardShow.setEnabled(true);
			} else if (btnStatus == 1 && preid == 4) {
				onListShow();
			}
		}
	}

	public void onSave() {
		CoopwithVO vo = (CoopwithVO) getCardPanel().getBillValueVOExtended(
				CoopwithVO.class.getName(),
				CoopwithHeaderVO.class.getName(),
				new String[] { CoopwithItemVO.class.getName(),
						CoopwithItemVO.class.getName() });
		try {
			vo.getParentVO().validate();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			SCMEnv.error("数据校验失败", e1);
			showErrorMessage(e1.getMessage());
			return;
		}
		// 检查数据的合法性
		if (!chechValidation(vo))
			return;
		if (btnStatus == 3) {
			try {
				if (CoopWith_Help.update(pk, vo))
					showWarningMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000000")/* 更新成功 */);
				else {
					showWarningMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000002")/* 信息有重复 */);
					return;
				}
				isSave = true;
				preid = btnStatus;
				btnStatus = 5;
				changeStatus();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000001")/* 更新失败 */);
				showHintMessage(e.getMessage());
			}
			return;
		}
		try {
			String key = CoopWith_Help.insert(vo);
			if (key == null) {
				MessageDialog
						.showErrorDlg(this, "输入有误", NCLangRes.getInstance()
								.getStrByID("400106", "ClientUI-000002")/* 输入的信息有重复 */);
			} else {
				showHintMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000003")/* 保存成功 */);
				// save the current receipt's pk
				pk = key;
				isSave = true;
				preid = btnStatus;
				btnStatus = 5;
				changeStatus();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showHintMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000004", null, new String[] { e.getMessage() })/*
																				 * 保存失败：
																				 * {0}
																				 */);
		}
	}

	public void onAdd() {
		removeAll();
		preid = btnStatus;
		getCardPanel().getHeadItem("ccustomerid").setEdit(true);
		getCardPanel().getHeadItem("cmaincustomerid").setEdit(true);
		getCardPanel().getHeadItem("csourcereceipttype").setEdit(true);
		
		getCardPanel().getHeadItem("csourceorgid").setEdit(false);
		getCardPanel().getHeadItem("ctargetorgid").setEdit(false);
		getCardPanel().getHeadItem("cbusitypeid").setEdit(false);
		getCardPanel().getHeadItem("ctargetbusitypeid").setEdit(false);
		getCardPanel().getBillData().clearViewData();
		add(getCardPanel(), BorderLayout.CENTER);
		getCardPanel().setEnabled(true);
		isList = false;
		isSave = false;
		// getCardPanel().getHeadItem("ccustomerid").setRefType("nc.ui.so.socoopwith.VOFieldsRefModel");
		btnStatus = 1;
		changeStatus();
		setButtons(cardbutton_Arry);
		updateUI();
	}

	// 卡片显示
	public boolean onCardShow() {
		// this.remove(m_cardpanel);
		int selectRow = -1;
		selectRow = getListPanel().getHeadTable().getSelectedRow();
		if (selectRow < 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000005")/* 请选择一条单据 */);
			return false;
		}
		isList = false;
		CoopwithHeaderVO header = new CoopwithHeaderVO();

		if (getListPanel().getHeadTable().getValueAt(selectRow, 0).toString()
				.equals("销售订单")) {
			header.setCsourcereceipttype("30");
			header.setCtargetreceipttype("21");
			getCardPanel().getHeadItem("csourceorgid").setRefType("销售组织");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("采购组织");
		} else {
			header.setCsourcereceipttype("21");
			header.setCtargetreceipttype("30");
			getCardPanel().getHeadItem("csourceorgid").setRefType("采购组织");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("销售组织");

		}
		getCardPanel().getBillData().clearViewData();
		CoopwithHeaderVO voo = (CoopwithHeaderVO) getListPanel()
				.getHeadBillModel().getBodyValueRowVO(selectRow,
						CoopwithHeaderVO.class.getName());
		CoopwithItemVO[] bodyvos = null;
		try {
			pk = CoopWith_Help.getPk(voo);
			if (pks != null && pks.length > 0)
				for (int i = 0; i < pks.length; i++) {
					if (pks[i].equals(pk)) {
						curPage = i;
						break;
					}

				}
			bodyvos = CoopWith_Help.getBodyVOS(pk);
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(e.getMessage());
			return false;
		}
		if (bodyvos == null)
			return false;
		// 订单协同设置的vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// 出入库单协同设置的vo
		Vector<CoopwithItemVO> v2 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items2 = null;
		for (CoopwithItemVO vo : bodyvos) {
			// trim很重要
			if (vo.getCsourcefieldnameid().trim().equals("45"))
				v2.add(vo);
			else
				v1.add(vo);
		}
		// 填充vo数组
		if (v1.size() > 0) {
			items1 = new CoopwithItemVO[v1.size()];
			v1.copyInto(items1);
		}
		if (v2.size() > 0) {
			items2 = new CoopwithItemVO[v2.size()];
			v2.copyInto(items2);
		}

		// 对表头的源单据类型字段进行设置
		if (header.getCsourcereceipttype().equals("30"))
			((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
					.getComponent()).setSelectedItem("销售订单");
		else
			((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
					.getComponent()).setSelectedItem("采购订单");

		// 初始化卡片界面
		remove(getListPanel());
		add(getCardPanel(), BorderLayout.CENTER);
		preheadervo=voo;
		getCardPanel().getBillData().setHeaderValueVO(voo);
		getCardPanel().getHeadItem("csourcereceipttype").setValue(
				header.getCsourcereceipttype());
		// getCardPanel().getBillData().setBodyValueVO(bodyvos);
		if (items1 != null && items1.length > 0) {
			getCardPanel().getBillData()
					.setBodyValueVO("so_coopwith_b", items1);
		}
		if (items2 != null && items2.length > 0) {
			getCardPanel().getBillData().setBodyValueVO("in_out_set", items2);
		}
		// 相关的按钮设置
		preid = btnStatus;
		btnStatus = 4;
		changeStatus();
		setButtons(cardbutton_Arry);
		getCardPanel().setEnabled(false);
		updateUI();
		return true;
	}

	// 列表显示
	public void onListShow() {
		// this.remove(m_listpanel);
		isList = true;
		remove(getCardPanel());
		add(getListPanel(), BorderLayout.CENTER);
		preid = btnStatus;
		btnStatus = 2;
		changeStatus();
		setButtons(listbutton_Arry);
		onRefresh();
		updateUI();
	}

	// 查询-----处理
	public void onQuery() {
		// 加载并显示查询模板
		if (getQueryDlg().showModal() == MessageDialog.ID_OK) {
			onListShow();
			strWhere = queryClient.getWhereSQL();
			try {
				CoopwithHeaderVO[] headervos = CoopWith_Help
						.getHeaderVOS(strWhere);
				// 用headervos对表进行填充
				getListPanel().setHeaderValueVO(headervos);
				int rows = getListPanel().getHeadTable().getRowCount();
				for (int i = 0; i < rows; i++) {
					String type = getListPanel().getHeadTable()
							.getValueAt(i, 0).toString();
					if (type.trim().equals("销售订单")) {
						getListPanel()
								.getHeadBillModel()
								.execFormulas(
										i,
										new String[] {
												"sourceOrgName->getColValue(bd_salestru, vsalestruname,csalestruid , csourceorgid)",
												"targetOrgName->getColValue(bd_purorg, name, pk_purorg, ctargetorgid)" });
					} else {
						getListPanel()
								.getHeadBillModel()
								.execFormulas(
										i,
										new String[] {
												"sourceOrgName->getColValue(bd_purorg, name,pk_purorg , csourceorgid)",
												"targetOrgName->getColValue(bd_salestru, vsalestruname, csalestruid, ctargetorgid)" });
					}
					getListPanel()
							.getHeadBillModel()
							.execFormulas(
									i,
									new String[] {
											"sourceCompanyName->getColValue(bd_corp, unitname,pk_corp , ccustomerid)",
											"targetCompanyName->getColValue(bd_corp, unitname,pk_corp , cmaincustomerid)" });
				}
				getListPanel().getHeadBillModel().execLoadFormula();
				updateUI();
			} catch (Exception e) {
				showHintMessage(e.getMessage());
			}
		}
	}

	// 删除按钮----处理
	public void onDelete() {
		if (isList) {
			int selectRow = -1;
			selectRow = getListPanel().getHeadTable().getSelectedRow();
			if (selectRow < 0) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000005")/* 请选择一条单据 */);
				return;
			}
			int ireturn = MessageDialog.showYesNoCancelDlg(this, "提示",
					NCLangRes.getInstance().getStrByID("400106",
							"ClientUI-000006")/* 是否删除该记录 */);
			if (ireturn != MessageDialog.ID_YES) {
				return;
			}
			CoopwithHeaderVO header = new CoopwithHeaderVO();

			String sourceReceipt = getListPanel().getHeadTable().getValueAt(
					selectRow, 0).toString();
			if (sourceReceipt.trim().equals("销售订单")) {
				header.setCsourcereceipttype("30");
				header.setCtargetreceipttype("21");
			} else {
				header.setCsourcereceipttype("21");
				header.setCtargetreceipttype("30");
			}
			header.setCcustomerid(getListPanel().getHeadBillModel().getValueAt(
					selectRow, "ccustomerid").toString());
			header.setCbusitypeid(getListPanel().getHeadBillModel().getValueAt(
					selectRow, "cbusitypeid").toString());
			header.setCmaincustomerid(getListPanel().getHeadBillModel()
					.getValueAt(selectRow, "cmaincustomerid").toString());
			header.setCtargetbusitypeid(getListPanel().getHeadBillModel()
					.getValueAt(selectRow, "ctargetbusitypeid").toString());
			header.setCsourceorgid(getListPanel().getHeadBillModel()
					.getValueAt(selectRow, "csourceorgid").toString());
			header.setCtargetorgid(getListPanel().getHeadBillModel()
					.getValueAt(selectRow, "ctargetorgid").toString());

			boolean flag = false;
			try {
				String pk_templet = CoopWith_Help.getPk(header);

				flag = CoopWith_Help.delete(pk_templet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				SCMEnv.out(e);
			}
			if (flag) {
				showWarningMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000007")/* 删除成功 */);
				onRefresh();
				updateUI();
			}

			return;
		}

		boolean flag = false;
		try {
			flag = CoopWith_Help.delete(pk);
			onListShow();
		} catch (Exception e) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000008")/* 删除失败 */);
			// TODO Auto-generated catch block

		}
		if (flag)
			showWarningMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000007")/* 删除成功 */);
	}

	// 编辑按钮点击事件的处理
	public void onEdite() {
		if (isList)
			if (!onCardShow())
				return;
		isSave = false;
		getCardPanel().setEnabled(true);
		// 设置表头是否可编辑
		enableHeader();
		// 设置ctargetfieldnameid不可编辑
		getCardPanel().getBodyItem("so_coopwith_b", "ctargetfieldnameid")
				.setEnabled(false);
		getCardPanel().getBodyItem("in_out_set", "ctargetfieldnameid")
				.setEnabled(false);
		preid = btnStatus;
		btnStatus = 3;
		changeStatus();
		setButtons(cardbutton_Arry);
		// 待修改
		CoopwithVO vo = (CoopwithVO) getCardPanel().getBillValueVOExtended(
				CoopwithVO.class.getName(),
				CoopwithHeaderVO.class.getName(),
				new String[] { CoopwithItemVO.class.getName(),
						CoopwithItemVO.class.getName() });
		CoopwithItemVO[] bodyItems1 = (CoopwithItemVO[]) vo
				.getTableVO("so_coopwith_b");
		CoopwithItemVO[] bodyItems2 = (CoopwithItemVO[]) vo
				.getTableVO("in_out_set");
		String csource = getCardPanel().getHeadItem("csourcereceipttype")
				.getValue();
		/*
		 * 如果是销售订单 则设置ctargetreceipttype为销售组织 ctargetreceipttype为采购组织
		 */
		if (csource.equals(sellOrder)) {
			((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
					.getComponent()).setSelectedItem("采购订单");
			// -=------------------------------------------------------------//
			try {
				CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("21");
				for (CoopwithItemVO voo : voBodys) {
					voo.setVvalue("");
					voo.setVvaluename("");
					String target = voo.getCtargetfieldnameid().trim();
					String hh = voo.getFmain().trim();
					for (CoopwithItemVO vo1 : bodyItems1) {
						if (vo1.getCtargetfieldnameid().trim().equals(target)
								&& vo1.getFmain().trim().equals(hh)
								&& vo1.getVvalue() != null) {
							voo.setFvaluemodule(vo1.getFvaluemodule());
							voo.setVvalue(vo1.getVvalue());
							voo.setVvaluename(vo1.getVvaluename());
							voo.setDatatype(vo1.getDatatype());
							voo.setReftype(vo1.getReftype());
						}
					}
				}
				getCardPanel().getBillData().setBodyValueVO("so_coopwith_b",
						voBodys);

			} catch (Exception ee) {
				SCMEnv.out(ee);
				showHintMessage(ee.getMessage());
			}
			// -==------------------------------------------------------------------//

		} else if (csource.equals(stockOrder)) {
			((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
					.getComponent()).setSelectedItem("销售订单");
			// -=-------------------------------------------------------------------//
			try {
				CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("30");
				for (CoopwithItemVO voo : voBodys) {
					String target = voo.getCtargetfieldnameid().trim();
					String hh = voo.getFmain().trim();
					voo.setVvalue("");
					voo.setVvaluename("");
					for (CoopwithItemVO vo1 : bodyItems1) {
						if (vo1.getCtargetfieldnameid().trim().equals(target)
								&& vo1.getFmain().trim().equals(hh)
								&& vo1.getVvalue() != null) {
							voo.setFvaluemodule(vo1.getFvaluemodule());
							voo.setVvalue(vo1.getVvalue());
							voo.setVvaluename(vo1.getVvaluename());
							voo.setDatatype(vo1.getDatatype());
							voo.setReftype(vo1.getReftype());
						}
					}
				}
				getCardPanel().getBillData().setBodyValueVO("so_coopwith_b",
						voBodys);
			} catch (Exception ee) {
				SCMEnv.out(ee);
				showHintMessage(ee.getMessage());
			}
			// -==------------------------------------------------------------------//

		}
		// in_out_set子表设置
		try {
			CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("45");
			if (voBodys != null)
				for (CoopwithItemVO voo : voBodys) {
					String target = voo.getCtargetfieldnameid().trim();
					String hh = voo.getFmain().trim();
					voo.setVvalue("");
					voo.setVvaluename("");
					for (CoopwithItemVO vo2 : bodyItems2) {
						if (vo2.getCtargetfieldnameid().trim().equals(target)
								&& vo2.getFmain().trim().equals(hh)
								&& vo2.getVvalue() != null) {
							voo.setFvaluemodule(vo2.getFvaluemodule());
							voo.setVvalue(vo2.getVvalue());
							voo.setVvaluename(vo2.getVvaluename());
							voo.setDatatype(vo2.getDatatype());
							voo.setReftype(vo2.getReftype());
						}
					}
				}
			getCardPanel().getBillData().setBodyValueVO("in_out_set", voBodys);
		} catch (Exception ee) {
			SCMEnv.out(ee);
			showHintMessage(ee.getMessage());
		}

		updateUI();
	}

	// 复制按钮的处理
	/*
	 * 根据选中的行，得到该行的vo 然后添加一行 并对这行的值用vo 来初始化
	 */
	public void onCopy() {

		if (isList) {
			int rowIndex = -1;
			rowIndex = getListPanel().getHeadTable().getSelectedRow();
			if (rowIndex < 0) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000009")/* 请选择要复制记录行 */);
				return;
			}
			onCardShow();
			onEdite();
			isSave = false;
			preid = btnStatus;
			btnStatus = 7;
			changeStatus();
			getCardPanel().getHeadItem("csourcereceipttype").setEnabled(true);
			getCardPanel().getHeadItem("ctargetreceipttype").setEnabled(true);
			return;
		}
		onEdite();
		preid = btnStatus;
		btnStatus = 7;
		changeStatus();
		isSave = false;
		getCardPanel().setEnabled(true);
	}

	public void onRefresh() {
		try {
			headervos = CoopWith_Help.getHeaderVOS("(1=1)");
			if (headervos.length > 0) {
				pks = new String[headervos.length];
				for (int i = 0; i < headervos.length; i++) {
					pks[i] = headervos[i].getCcoopwithid();
				}
			}
			// fill the table with headervos
			getListPanel().setHeaderValueVO(headervos);
			int rows = getListPanel().getHeadTable().getRowCount();
			for (int i = 0; i < rows; i++) {
				String type = getListPanel().getHeadTable().getValueAt(i, 0)
						.toString();
				if (type.trim().equals("销售订单")) {
					getListPanel()
							.getHeadBillModel()
							.execFormulas(
									i,
									new String[] {
											"sourceOrgName->getColValue(bd_salestru, vsalestruname,csalestruid , csourceorgid)",
											"targetOrgName->getColValue(bd_purorg, name, pk_purorg, ctargetorgid)" });
				} else {
					getListPanel()
							.getHeadBillModel()
							.execFormulas(
									i,
									new String[] {
											"sourceOrgName->getColValue(bd_purorg, name,pk_purorg , csourceorgid)",
											"targetOrgName->getColValue(bd_salestru, vsalestruname, csalestruid, ctargetorgid)" });
				}
			}
			getListPanel().getHeadBillModel().execLoadFormula();
			updateUI();
		} catch (Exception e) {
			showHintMessage(e.getMessage());
		}
	}

	/*
	 * set buttons status by the variable btnStatus
	 */
	private void changeStatus() {
		if (btnStatus == 0) {// 初始化状态
			getCardPanel().setEnabled(false);
			((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
					.getComponent()).setSelectedIndex(-1);
			bo_Save.setEnabled(false);
			bo_Modify.setEnabled(true);
			ch_Copy.setEnabled(true);
			ch_Delete.setEnabled(false);
			ch_Cancel.setEnabled(true);
			ch_Modify.setEnabled(false);
			bo_Look.setEnabled(true);
			bo_CardShow.setName(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000032")/* 卡片显示 */);
		} else if (btnStatus == 1) {// 添加按钮点击后的状态
			bo_Save.setEnabled(true);
			// line_Oper.setEnabled(true);
			bo_Add.setEnabled(false);
			bo_Modify.setEnabled(true);
			ch_Modify.setEnabled(false);
			ch_Cancel.setEnabled(true);
			ch_Delete.setEnabled(false);
			ch_Copy.setEnabled(false);
			bo_Look.setEnabled(false);
			bo_CardShow.setName(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000033")/* "列表显示" */);
			bo_CardShow.setEnabled(false);

		} else if (btnStatus == 2) {// bo_List按钮点击后的状态
			String corpKey = this.getCorpPrimaryKey();
			bo_Add.setEnabled(true);
			bo_Look.setEnabled(true);
			bo_Search.setEnabled(true);
			ch_FirstPage.setEnabled(false);
			ch_PrePage.setEnabled(false);
			ch_NextPage.setEnabled(false);
			ch_Refresh.setEnabled(true);
			ch_LastPage.setEnabled(false);
			bo_Modify.setEnabled(true);
			ch_Delete.setEnabled(true);
			ch_Copy.setEnabled(true);
			ch_Cancel.setEnabled(false);
			bo_Save.setEnabled(false);
			ch_Modify.setEnabled(true);
			bo_CardShow.setEnabled(true);
			bo_CardShow.setName(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000032")/* "卡片显示" */);
			if (!corpKey.equals("0001")) {
				bo_Add.setEnabled(false);
				bo_Modify.setEnabled(false);
			}
		} else if (btnStatus == 3) {// update
			bo_Save.setEnabled(true);
			ch_Cancel.setEnabled(true);
			ch_Delete.setEnabled(false);
			bo_CardShow.setEnabled(false);
			ch_Modify.setEnabled(false);
			bo_Add.setEnabled(false);
			ch_Copy.setEnabled(false);
			bo_Look.setEnabled(false);
		} else if (btnStatus == 4) {// card show
			String corpKey = this.getCorpPrimaryKey();
			bo_Look.setEnabled(true);
			ch_FirstPage.setEnabled(true);
			ch_LastPage.setEnabled(true);
			ch_PrePage.setEnabled(true);
			ch_NextPage.setEnabled(true);
			if (pks != null && pks.length > 0) {
				if (curPage == 0)
					ch_PrePage.setEnabled(false);
				if (curPage == pks.length - 1)
					ch_NextPage.setEnabled(false);
			}
			ch_Refresh.setEnabled(false);
			bo_Modify.setEnabled(true);
			ch_Copy.setEnabled(false);
			ch_Modify.setEnabled(true);
			bo_Search.setEnabled(false);
			bo_CardShow.setName(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000033")/* "列表显示" */);
			ch_Copy.setEnabled(true);
			bo_CardShow.setEnabled(true);
			if (!corpKey.equals("0001")) {
				bo_Add.setEnabled(false);
				bo_Modify.setEnabled(false);
			}
		} else if (btnStatus == 5) {// save
			getCardPanel().setEnabled(false);
			bo_Save.setEnabled(false);
			ch_Cancel.setEnabled(false);
			bo_Add.setEnabled(true);
			bo_Look.setEnabled(false);
			bo_Modify.setEnabled(true);
			ch_Modify.setEnabled(true);
			ch_Copy.setEnabled(true);
			ch_Delete.setEnabled(true);
			bo_CardShow.setEnabled(true);
			bo_Search.setEnabled(true);
		} else if (btnStatus == 7) {
			bo_Look.setEnabled(false);
			bo_Add.setEnabled(false);
			bo_CardShow.setEnabled(false);
			ch_Modify.setEnabled(false);
			ch_Copy.setEnabled(false);
			bo_CardShow.setEnabled(false);
		}
		updateButtons();
	}

	/*
	 * none header item can be null
	 */
	private boolean chechValidation(CoopwithVO vo) {
		CoopwithHeaderVO head = (CoopwithHeaderVO) vo.getParentVO();
		StringBuffer errorMessage = new StringBuffer();
		if (head.getCsourcereceipttype() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000010")/* 原单据类型不能为空\n */);
		if (head.getCcustomerid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000011")/* 原公司不能为空\n */);
		if (head.getCbusitypeid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000012")/* 原业务类型不能为空\n */);
		if (head.getCsourceorgid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000013")/* 原购销组织不能为空\n */);
		if (head.getCtargetreceipttype() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000014")/* 目的单据类型不能为空\n */);
		if (head.getCmaincustomerid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000015")/* 目的公司不能为空\n */);
		if (head.getCtargetbusitypeid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000016")/* 目的业务类型不能为空\n */);
		if (head.getCtargetorgid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000017")/* 目的购销组织不能为空 */);
		if (!errorMessage.toString().equals("")) {
			showErrorMessage(errorMessage.toString());
			return false;
		}
		return checkBodyValidate(vo);
	}

	/*
	 * EditListener所必需实现的方法 主要是使用在表头部分 表体的单元格编辑前触发该事件 在这里主要是进行表头BillItem的参照设置
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.pub.bill.BillEditListener#afterEdit(nc.ui.pub.bill.BillEditEvent)
	 */
	@SuppressWarnings("deprecation")
	public void afterEdit(BillEditEvent e) {

		String sourceCompanyID = getCardPanel().getHeadItem("ccustomerid")
				.getValue();
		String sourceRecepit = getCardPanel().getHeadItem("csourcereceipttype")
				.getValue();

		if (null != sourceRecepit) {
			getCardPanel().getHeadItem("cbusitypeid").setEdit(true);
			getCardPanel().getHeadItem("ctargetbusitypeid").setEdit(true);
		} else {
			getCardPanel().getHeadItem("cbusitypeid").setEdit(false);
			getCardPanel().getHeadItem("ctargetbusitypeid").setEdit(false);
		}
		if (null != sourceRecepit&&null != sourceCompanyID) {
			getCardPanel().getHeadItem("csourceorgid").setEdit(true);
		} else {
			getCardPanel().getHeadItem("csourceorgid").setEdit(false);
		}
		String targetCompanyID = getCardPanel().getHeadItem("cmaincustomerid")
				.getValue();
		String targetRecepit = getCardPanel().getHeadItem("ctargetreceipttype")
				.getValue();
		if (null != sourceRecepit&&null!=targetCompanyID) {
			getCardPanel().getHeadItem("ctargetorgid").setEdit(true);
		} else {
			getCardPanel().getHeadItem("ctargetorgid").setEdit(false);
		}
		if (e.getKey().trim().equals("vvaluename")) {
			// the editing line
			int selectRow = getCardPanel().getBillTable().getSelectedRow();
			CoopwithItemVO vo = (CoopwithItemVO) getCardPanel().getBillModel(
					e.getTableCode()).getBodyValueRowVO(selectRow,
					CoopwithItemVO.class.getName());
			BillCellEditor cell = (BillCellEditor) getCardPanel()
					.getBillTable().getCellEditor(selectRow, 2);
			UIRefPane ref = null;
			try {
				ref = (UIRefPane) cell.getComponent();
			} catch (Exception ee) {

			}
			String valueModule = vo.getFvaluemodule();
			// the default display of the reffering of system is the name not
			// the value
			// so should deal with it here to get the value by the name.
			if (valueModule.trim().equals("系统值")) {
				String sysFunName = (String) ref.getRefValue("sysname");
				vo.setVvaluename(ref == null ? vo.getVvalue() : sysFunName);// ref.getRefValues("SysFuncName").toString());
			}
			vo.setVvalue(ref == null ? vo.getVvalue() : ref.getRefPK());
			getCardPanel().getBillModel(e.getTableCode()).setBodyRowVO(vo,
					selectRow);
		}
		if (e.getKey().trim().equals("csourcereceipttype")) {
			preid = btnStatus;
			btnStatus = 6;
			String csource = getCardPanel().getHeadItem("csourcereceipttype")
					.getValue();
			/*
			 * 如果是销售订单 则设置ctargetreceipttype为销售组织 ctargetreceipttype为采购组织
			 */
			if (csource.equals(sellOrder)) {
				((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
						.getComponent()).setSelectedItem("采购订单");
				try {
					CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("21");
					getCardPanel().getBillData().setBodyValueVO(
							"so_coopwith_b", voBodys);
					defaultValueHandler("so_coopwith_b");
				} catch (Exception ee) {
					SCMEnv.out(ee);
					showHintMessage(ee.getMessage());
				}
				// -==------------------------------------------------------------------//

			} else if (csource.equals(stockOrder)) {
				((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
						.getComponent()).setSelectedItem("销售订单");
				try {
					CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("30");
					getCardPanel().getBillData().setBodyValueVO(
							"so_coopwith_b", voBodys);
					defaultValueHandler("so_coopwith_b");
				} catch (Exception ee) {
					SCMEnv.out(ee);
					showHintMessage(ee.getMessage());
				}
			}
			try {
				CoopwithItemVO[] voBodys = CoopWith_Help.getVOFields("45");
				getCardPanel().getBillData().setBodyValueVO("in_out_set",
						voBodys);
				defaultValueHandler("in_out_set");
			} catch (Exception ee) {
				SCMEnv.out(ee);
				showHintMessage(ee.getMessage());
			}
		}
		/*
		 * the source and the target company can't be the same
		 */
		if (e.getKey().trim().equals("cmaincustomerid")
				|| e.getKey().trim().equals("ccustomerid")) {
			checkIsCompanySame(e);
		}
		updateUI();
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * EditListener2所必需实现的方法 主要是使用在表体部分 表体的单元格编辑前触发该事件 在这里主要是进行单元格参照类型的设计
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.pub.bill.BillEditListener2#beforeEdit(nc.ui.pub.bill.BillEditEvent)
	 */
	@SuppressWarnings("deprecation")
	public boolean beforeEdit(BillEditEvent e) {
		int rowIndex = e.getRow();
		// 子表so_coopwith_b的处理------订单协同设置
		if (e.getTableCode().equals("so_coopwith_b")) {
			if (e.getKey().equals("vvaluename")) {// 取值内容根据取值模式进行相应的设置

				String source_receipt = getCardPanel().getHeadItem(
						"csourcereceipttype").getValue();
				String templetPk = "30";
				String title = "销售订单VO对照";
				if (source_receipt == null) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000018")/* 源单据类型不能为空 */);
					return true;
				}
				if (source_receipt.trim().equals("21")) {
					templetPk = "21";
					title = "采购订单VO对照";
				}

				Object valueModule = getCardPanel().getBillModel(
						"so_coopwith_b").getValueAt(rowIndex, "fvaluemodule");
				if (valueModule == null || valueModule.toString().equals("")) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000019")/* 请先选择取值方式 */);
					return true;
				}
				/*
				 * 设置vvalue的类型为自定义的参照 显示的字段有：字段编码，中文描述，主表/子表
				 */
				if (valueModule.equals("对应值")) {
					parallelValueHandler(templetPk, title, rowIndex);
				} else if (valueModule.equals("固定值")) {
					fixValueHandler("so_coopwith_b");
				} else if (valueModule.equals("系统值")) {
					systemValueHandler("so_coopwith_b");
				}
				CoopwithItemVO vo = (CoopwithItemVO) getCardPanel()
						.getBillModel(e.getTableCode()).getBodyValueRowVO(
								rowIndex, CoopwithItemVO.class.getName());
				if (vo.getVvalue() != null) {
					BillCellEditor cel = (BillCellEditor) getCardPanel()
							.getBillTable().getCellEditor(rowIndex, 2);
					Object c = cel.getComponent();
					UIRefPane sRef = null;
					if (c instanceof UIRefPane) {
						sRef = (UIRefPane) c;
						sRef.setPK(vo.getVvalue());
					}
				}
			}
		}
		// 子表in_out_set的处理--------出入库单协同设置
		else if (e.getTableCode().equals("in_out_set")) {
			if (e.getKey().equals("vvaluename")) {
				String valueModule = getCardPanel().getBillModel("in_out_set")
						.getValueAt(rowIndex, "fvaluemodule").toString();
				if (valueModule == null || valueModule.equals("")) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000020")/* 请选择取值模式 */);
					return true;
				}
				if (valueModule.equals("对应值")) {
					String templetPk = "4C";// 销售出库单
					String title = "销售出库单的VO";
					parallelValueHandler(templetPk, title, rowIndex);
				} else if (valueModule.equals("固定值")) {
					fixValueHandler("in_out_set");
				} else if (valueModule.equals("系统值")) {
					systemValueHandler("in_out_set");
				}
				CoopwithItemVO vo = (CoopwithItemVO) getCardPanel()
						.getBillModel(e.getTableCode()).getBodyValueRowVO(
								rowIndex, CoopwithItemVO.class.getName());
				if (vo.getVvalue() != null) {
					BillCellEditor cel = (BillCellEditor) getCardPanel()
							.getBillTable().getCellEditor(rowIndex, 2);
					Object c = cel.getComponent();
					UIRefPane sRef = null;
					if (c instanceof UIRefPane) {
						sRef = (UIRefPane) c;
						sRef.setPK(vo.getVvalue());
					}
				}
			}

		}
		return true;
	}

	/*
	 * 使表头不可编辑 此方法是一个工具方法 调用后--〉表头不可编辑
	 */
	private void enableHeader() {
		getCardPanel().getHeadItem("csourcereceipttype").setEnabled(false);
		getCardPanel().getHeadItem("ctargetreceipttype").setEnabled(false);
		getCardPanel().getHeadItem("ccustomerid").setEdit(false);
		getCardPanel().getHeadItem("cmaincustomerid").setEdit(false);
		getCardPanel().getHeadItem("cbusitypeid").setEdit(false);
		getCardPanel().getHeadItem("csourceorgid").setEdit(false);
		getCardPanel().getHeadItem("ctargetorgid").setEdit(true);
		getCardPanel().getHeadItem("ctargetbusitypeid").setEdit(true);
		
	}

	/*
	 * 初始化监听器 主要有俩个监听器 分别是EditListener2和EditListener
	 * 
	 */
	private void initListener() {
		getCardPanel().addEditListener(this);
		getCardPanel().addEditListener("so_coopwith_b", this);
		getCardPanel().addEditListener("in_out_set", this);
		getCardPanel().addBodyEditListener2("so_coopwith_b", this);
		getCardPanel().addBodyEditListener2("in_out_set", this);
		getListPanel().addMouseListener(this);
		getCardPanel().setBillBeforeEditListenerHeadTail(this);
		((UIComboBox) getCardPanel().getBodyItem("so_coopwith_b",
				"fvaluemodule").getComponent()).addItemListener(this);
		((UIComboBox) getCardPanel().getBodyItem("in_out_set", "fvaluemodule")
				.getComponent()).addItemListener(this);
		// 注册值改变事件 以使值发生改变时与之利益相关的项清空
		((UIRefPane) getCardPanel().getHeadItem("ccustomerid").getComponent())
				.addValueChangedListener(this);
		((UIRefPane) getCardPanel().getHeadItem("cmaincustomerid")
				.getComponent()).addValueChangedListener(this);
		((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
				.getComponent()).addItemListener(this);

	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		/*
		 * reffering business type. set fillter condition with the company and
		 * receipt type.
		 * 
		 */
		if (e.getItem() == getCardPanel().getHeadItem("cbusitypeid")) {
			StringBuffer strMessage = new StringBuffer();
			String sourceCompanyID = getCardPanel().getHeadItem("ccustomerid")
					.getValue();
			String sourceRecepit = getCardPanel().getHeadItem(
					"csourcereceipttype").getValue();

			if (null == sourceRecepit) {
				return false;
			}
			StringBuilder wherePart = new StringBuilder();
			wherePart
					.append("   (bd_busitype.pk_busitype in (select pub_billbusiness.pk_businesstype  from bd_busitype inner join pub_billbusiness on pub_billbusiness.pk_businesstype =bd_busitype.pk_busitype where pub_billbusiness.pk_corp in (");
			if(null!=sourceCompanyID)
			wherePart.append("'"+sourceCompanyID.trim()+"',");
			wherePart.append("'0001','@@@@') and pk_billType='" + sourceRecepit.trim()
							+ "')) and pk_corp <> '0001' ");
			((UIRefPane) getCardPanel().getHeadItem("cbusitypeid")
					.getComponent()).getRefModel().setWherePart(
					wherePart.toString());
			if (null == sourceCompanyID) {
				return false;
			}
			// "(pk_corp='" + sourceCompanyID.trim()
			// + "' or pk_corp='@@@@') and busiprop = " + cc);
			/*
			 * the same as above
			 */
		} else if (e.getItem() == getCardPanel().getHeadItem(
				"ctargetbusitypeid")) {
			StringBuffer strMessage = new StringBuffer();
			String targetCompanyID = getCardPanel().getHeadItem(
					"cmaincustomerid").getValue();
			String targetRecepit = getCardPanel().getHeadItem(
					"ctargetreceipttype").getValue();

			if (null == targetRecepit) {
				return false;
			}
			
			StringBuilder wherePart = new StringBuilder();
			wherePart
					.append("   (bd_busitype.pk_busitype in (select pub_billbusiness.pk_businesstype  from bd_busitype inner join pub_billbusiness on pub_billbusiness.pk_businesstype =bd_busitype.pk_busitype where pub_billbusiness.pk_corp in (");
			if(null!=targetCompanyID)
			wherePart.append("'"+targetCompanyID.trim()+"',");
			wherePart.append("'0001','@@@@') and pk_billType='" + targetRecepit.trim()
							+ "')) and pk_corp <> '0001' ");
			((UIRefPane) getCardPanel().getHeadItem("ctargetbusitypeid")
					.getComponent()).getRefModel().setWherePart(
					wherePart.toString());
			if (null == targetCompanyID) {
				return false;
			}
			// "(pk_corp='" + targetCompanyID.trim()
			// + "' or pk_corp='@@@@') and busiprop = " + cc);
			/*
			 * reffering the source company and the receipt type you select.
			 * 
			 */
		} else if (e.getItem() == getCardPanel().getHeadItem("csourceorgid")) {
			StringBuffer strMessage = new StringBuffer();
			String sourceCompanyID = getCardPanel().getHeadItem("ccustomerid")
					.getValue();
			String sourceRecepit = getCardPanel().getHeadItem(
					"csourcereceipttype").getValue();

			if (null == sourceCompanyID || null == sourceRecepit)
				return false;

			if (sourceRecepit.trim().equals(sellOrder)) {
				if (getCardPanel().getHeadItem("csourceorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("csourceorgid")
								.getRefType().equals("销售组织"))
					getCardPanel().getHeadItem("csourceorgid").setRefType(
							"销售组织");
				((UIRefPane) getCardPanel().getHeadItem("csourceorgid")
						.getComponent()).getRefModel().setWherePart(
						"belongcorp='" + sourceCompanyID.trim() + "'");
			} else if (sourceRecepit.trim().equals(stockOrder)) {
				if (getCardPanel().getHeadItem("csourceorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("csourceorgid")
								.getRefType().equals("采购组织"))
					getCardPanel().getHeadItem("csourceorgid").setRefType(
							"采购组织");
				((UIRefPane) getCardPanel().getHeadItem("csourceorgid")
						.getComponent()).getRefModel().setWherePart(
						"ownercorp='" + sourceCompanyID.trim() + "'");

			}
			/*
			 * refering the target company and the type of the source receipt
			 * selected by end user.
			 * 
			 */
		} else if (e.getItem() == getCardPanel().getHeadItem("ctargetorgid")) {
			StringBuffer strMessage = new StringBuffer();
			String targetCompanyID = getCardPanel().getHeadItem(
					"cmaincustomerid").getValue();
			String targetRecepit = getCardPanel().getHeadItem(
					"ctargetreceipttype").getValue();
			if (targetRecepit.trim().equals(sellOrder)) {
				if (getCardPanel().getHeadItem("ctargetorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("ctargetorgid")
								.getRefType().equals("销售组织"))
					getCardPanel().getHeadItem("ctargetorgid").setRefType(
							"销售组织");
				((UIRefPane) getCardPanel().getHeadItem("ctargetorgid")
						.getComponent()).getRefModel().setWherePart(
						"belongcorp='" + targetCompanyID.trim() + "'");
			} else if (targetRecepit.trim().equals(stockOrder)) {
				if (getCardPanel().getHeadItem("ctargetorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("ctargetorgid")
								.getRefType().equals("采购组织"))
					getCardPanel().getHeadItem("ctargetorgid").setRefType(
							"采购组织");
				((UIRefPane) getCardPanel().getHeadItem("ctargetorgid")
						.getComponent()).getRefModel().setWherePart(
						"ownercorp='" + targetCompanyID.trim() + "'");
			}
		}
		return true;
	}

	@Override
	public boolean onClosing() {
		// TODO Auto-generated method stub
		if (!isSave
				&& getCardPanel().getHeadItem("csourcereceipttype").getValue() != null) {
			int ireturn = MessageDialog.showYesNoCancelDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH001")/* @res "是否保存已修改的数据？（" */);
			if (ireturn == MessageDialog.ID_YES) {
				onSave();
				return true;
			} else if (ireturn == MessageDialog.ID_NO)
				return true;
			else
				return false;
		}
		return true;
	}

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			UIComboBox valuemodulebox = (UIComboBox) (getCardPanel()
					.getBodyItem(getCardPanel().getCurrentBodyTableCode(),
							"fvaluemodule").getComponent());
			UIComboBox sourcereceiptbox = (UIComboBox) (getCardPanel()
					.getHeadItem("csourcereceipttype").getComponent());
			if (e.getSource().equals(valuemodulebox)) {
				// clear history data
				clearPreData();
				if (valuemodulebox.getSelectedIndex() == 0) {
					getCardPanel().getBillTable().getColumnModel().getColumn(2)
							.setCellEditor(
									new BillCellEditor(new UITextField()));
				} else if (valuemodulebox.getSelectedIndex() == 1) {
					getCardPanel().getBodyItem(
							getCardPanel().getCurrentBodyTableCode(),
							"vvaluename").setEnabled(true);
				} else if (valuemodulebox.getSelectedIndex() == 2) {
					getCardPanel().getBillTable().getColumnModel().getColumn(2)
							.setCellEditor(
									new BillCellEditor(new UITextField()));
					getCardPanel().getBodyItem(
							getCardPanel().getCurrentBodyTableCode(), "vvalue")
							.setEnabled(true);
				}
				updateUI();
			} else if (e.getSource().equals(sourcereceiptbox)) {
				getCardPanel().getHeadItem("cbusitypeid").setValue("");
				getCardPanel().getHeadItem("csourceorgid").setValue("");
				getCardPanel().getHeadItem("ctargetbusitypeid").setValue("");
				getCardPanel().getHeadItem("ctargetorgid").setValue("");
			}
		}
	}

	public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
		onCardShow();
	}

	/* create component with the data type and the reffering type */
	protected JComponent createEditComponent(int datatype, String reftype) {

		switch (datatype) {
		case BillItem.INTEGER: {
			UIRefPane ref = new UIRefPane();
			ref.setButtonVisible(false);
			ref.setTextType(UITextType.TextInt);
			return ref;
		}
		case BillItem.DECIMAL: {
			UIRefPane ref = new UIRefPane();
			ref.setButtonVisible(false);
			ref.setTextType(UITextType.TextDbl);
			ref.setNumPoint(new BillItemNumberFormat().getDecimalDigits());
			return ref;
		}
		case BillItem.UNSET:
		case BillItem.EMAILADDRESS:
		case BillItem.USERDEFITEM:
		case BillItem.STRING: {
			UIRefPane ref = new UIRefPane();
			ref.setButtonVisible(false);
			ref.setTextType(UITextType.TextStr);
			return ref;
		}
		case BillItem.DATE: {
			UIRefPane ref = new UIRefPane();
			ref.setRefNodeName("日历");
			ref.setTextType(UITextType.TextDate);
			return ref;
		}
		case BillItem.DATETIME: {
			UIRefPane ref = new UIRefPane();
			ref.setTextType(UITextType.TextDateTime);
			ref.setButtonVisible(false);
			return ref;
		}
		case BillItem.UFREF: {
			UIRefPane ref = new UIRefPane();
			((UIRefPaneTextField) (ref.getUITextField()))
					.setIsAutoAdjustLength(true);
			if (reftype != null) {
				if (reftype.startsWith("<")) {
					String type = reftype;
					type = type.replace('<', ' ').trim();
					type = type.replace('>', ' ').trim();
					try {
						ref.setIsCustomDefined(true);
						AbstractRefModel refModel = (AbstractRefModel) Class
								.forName(type).newInstance();
						ref.setRefNodeName("自定义参照");
						ref.setRefModel(refModel);
					} catch (Exception e) {
						Logger.error(e.getMessage());
					}
				} else
					ref.setRefNodeName(reftype);
			} else {
				ref.setIsCustomDefined(true);
				ref.setRefNodeName("自定义参照");
			}
			ref.setTextType(UITextType.TextStr);
			return ref;
		}
		case BillItem.BOOLEAN: {
			UICheckBox centeredCheckBox = new UICheckBox();
			centeredCheckBox.setHorizontalAlignment(UICheckBox.CENTER);
			centeredCheckBox.setBackground(java.awt.Color.white);
			return centeredCheckBox;
		}
		case BillItem.COMBO: {
			UIComboBox cboComboBox = new UIComboBox();
			cboComboBox
					.setBorder(javax.swing.BorderFactory.createEmptyBorder());

			return cboComboBox;
		}
		case BillItem.USERDEF: {
			UIRefPane ref = null;
			if (reftype != null) {
				reftype = reftype.trim();
				if (reftype.startsWith("{") && reftype.endsWith("}")) {
					reftype = reftype.substring(1, reftype.length() - 1);
					if (reftype.length() > 0) {
						try {
							nc.vo.bd.access.BdinfoVO infoVO = nc.vo.bd.access.BdinfoManager
									.getBdInfoVObyName(reftype);
							if (infoVO != null)
								ref = nc.ui.bd.ref.RefCall.getUIRefPane(infoVO
										.getPk_bdinfo());
						} catch (Exception e) {
						}
					}
				} else
					ref = nc.ui.bd.ref.RefCall.getUIRefPane(reftype);
			}
			if (ref == null)
				ref = new UIRefPane();
			((nc.ui.pub.beans.UIRefPaneTextField) (ref.getUITextField()))
					.setIsAutoAdjustLength(true);
			ref.setTextType(UITextType.TextStr);
			ref.setReturnCode(false);
			ref.setEditable(false);
			return ref;
		}
		case BillItem.TIME: {
			UIRefPane ref = new UIRefPane();
			((nc.ui.pub.beans.UIRefPaneTextField) (ref.getUITextField()))
					.setIsAutoAdjustLength(true);
			ref.setButtonVisible(false);
			ref.setTextType(UITextType.TextTime);
			return ref;
		}
		case BillItem.TEXTAREA: {
			UITextAreaScrollPane ref = new UITextAreaScrollPane();
			// ref.setSize(getImageSize());
			// ref.getUITextArea().setMaxLength(4096);
			// ref.getUITextArea().setMaxLength(getLength());
			return ref;
		}

		}

		return new UIRefPane();
	}

	// process fixed value.
	private void fixValueHandler(String tablecode) {
		int selectRow = getCardPanel().getBillTable(tablecode).getSelectedRow();
		CoopwithItemVO vo = (CoopwithItemVO) getCardPanel().getBillModel()
				.getBodyValueRowVO(selectRow, CoopwithItemVO.class.getName());
		CoopwithHeaderVO header = (CoopwithHeaderVO) getCardPanel()
				.getBillData().getHeaderValueVO(
						CoopwithHeaderVO.class.getName());
		String corpPk = header.getCmaincustomerid();
		int dataType = vo.getDatatype();
		String refType = vo.getReftype();
		JComponent editcom = createEditComponent(dataType, refType);

		if (editcom instanceof UIRefPane) {
			((UIRefPane) editcom).setPk_corp(corpPk);
			getCardPanel().getBillTable(tablecode).getColumnModel()
					.getColumn(2).setCellEditor(
							new BillCellEditor((UIRefPane) editcom));
		} else if (editcom instanceof UICheckBox)
			getCardPanel().getBillTable(tablecode).getColumnModel()
					.getColumn(2).setCellEditor(
							new BillCellEditor((UICheckBox) editcom));
		else if (editcom instanceof UIComboBox)
			getCardPanel().getBillTable(tablecode).getColumnModel()
					.getColumn(2).setCellEditor(
							new BillCellEditor((UIComboBox) editcom));
		else if (editcom instanceof UIPasswordField)
			getCardPanel().getBillTable(tablecode).getColumnModel()
					.getColumn(2).setCellEditor(
							new BillCellEditor((UIPasswordField) editcom));
		else if (editcom instanceof UITextField)
			getCardPanel().getBillTable(tablecode).getColumnModel()
					.getColumn(2).setCellEditor(
							new BillCellEditor((UITextField) editcom));
	}

	private void clearPreData() {
		int selectRow = getCardPanel().getBillTable().getSelectedRow();
		CoopwithItemVO vo = (CoopwithItemVO) getCardPanel().getBillModel(
				getCardPanel().getCurrentBodyTableCode()).getBodyValueRowVO(
				selectRow, CoopwithItemVO.class.getName());
		vo.setVvalue("");
		vo.setVvaluename("");
		getCardPanel().getBillModel().setBodyRowVO(vo, selectRow);
	}

	// handler system value.
	private void systemValueHandler(String tablecode) {
		UIRefPane reff = new UIRefPane();
		((UIRefPaneTextField) (reff.getUITextField()))
				.setIsAutoAdjustLength(true);

		SystemValueRef systemRef = new SystemValueRef(ClientEnvironment
				.getInstance().getDesktopApplet());

		reff.setRefModel(systemRef.getRefModel());
		getCardPanel().getBillTable(tablecode).getColumnModel().getColumn(2)
				.setCellEditor(new BillCellEditor(reff));
	}

	/*
	 * deal with paralle value
	 * 
	 */
	private void parallelValueHandler(String templetPk, String title,
			int rowIndex) {
		UIRefPane reff = new UIRefPane();
		((UIRefPaneTextField) (reff.getUITextField()))
				.setIsAutoAdjustLength(true);

		VOFieldsRef ref = new VOFieldsRef(ClientEnvironment.getInstance()
				.getDesktopApplet(), templetPk, title);

		reff.setRefModel(ref.getRefModel());

		// if(getCardPanel().getBillTable().getCellEditor(rowIndex,
		// 2).getCellEditorValue()==null||getCardPanel().getBillTable().getCellEditor(rowIndex,
		// 2).getCellEditorValue().toString().equals(""))
		getCardPanel().getBillTable().getColumnModel().getColumn(2)
				.setCellEditor(new BillCellEditor(reff));
	}

	/*
	 * deal with default valie
	 */
	private void defaultValueHandler(String tablecode) {
		int rows = getCardPanel().getBillTable(tablecode).getRowCount();
		for (int i = 0; i < rows; i++) {
			CoopwithItemVO vo = (CoopwithItemVO) getCardPanel().getBillModel(
					tablecode).getBodyValueRowVO(i,
					CoopwithItemVO.class.getName());
			if (vo.getVvaluename() != null && vo.getVvalue() != null) {
				vo.setFvaluemodule("对应值");
				getCardPanel().getBillModel(tablecode).setBodyRowVO(vo, i);
			}
		}
	}

	private boolean checkBodyValidate(CoopwithVO vo) {
		CoopwithItemVO[] items1 = (CoopwithItemVO[]) vo
				.getTableVO("in_out_set");
		CoopwithItemVO[] items2 = (CoopwithItemVO[]) vo
				.getTableVO("in_out_set");
		int length = 0;
		for (CoopwithItemVO item : items1) {
			if (item.getVvalue() != null) {
				length++;
				if (item.getFvaluemodule() == null) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000024")/* 取值模式不能为空 */);
					return false;
				}
			}
		}
		for (CoopwithItemVO item : items2) {
			if (item.getVvalue() != null) {
				length++;
				if (item.getFvaluemodule() == null) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000024")/* 取值模式不能为空 */);
					return false;
				}
			}
		}
		if (length == 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000025")/* 表体不能为空 */);
			return false;
		}
		return true;
	}

	// the target and source company can't be the same
	private void checkIsCompanySame(BillEditEvent e) {
		UIRefPane sref = (UIRefPane) getCardPanel().getHeadItem("ccustomerid")
				.getComponent();
		UIRefPane tref = (UIRefPane) getCardPanel().getHeadItem(
				"cmaincustomerid").getComponent();
		if (null == sref || null == tref)
			return;
		if (null != sref && null != sref.getRefPK()
				&& sref.getRefPK().equals(tref.getRefPK())) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000026")/* 源公司和目的公司不能为同一公司 */);
			tref.setText("");
		}
	}

	public void valueChanged(ValueChangedEvent event) {
		// TODO Auto-generated method stub
		int sourceHash = event.getSource().hashCode();
		int customeridHash = getCardPanel().getHeadItem("ccustomerid")
				.getComponent().hashCode();
		int maincustomeridHash = getCardPanel().getHeadItem("cmaincustomerid")
				.getComponent().hashCode();
		if (sourceHash == customeridHash) {
			getCardPanel().getHeadItem("cbusitypeid").setValue("");
			getCardPanel().getHeadItem("csourceorgid").setValue("");
		} else if (sourceHash == maincustomeridHash) {
			getCardPanel().getHeadItem("ctargetbusitypeid").setValue("");
			getCardPanel().getHeadItem("ctargetorgid").setValue("");
		}
	}

	private void showData() {
		CoopwithItemVO[] bodyvos = null;
		try {
			bodyvos = CoopWith_Help.getBodyVOS(pks[curPage]);
			pk = pks[curPage];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
		}
		// 订单协同设置的vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// 出入库单协同设置的vo
		Vector<CoopwithItemVO> v2 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items2 = null;
		for (CoopwithItemVO vo : bodyvos) {
			if (vo.getCsourcefieldnameid().trim().equals("45"))
				v2.add(vo);
			else
				v1.add(vo);
		}
		// fill vo array
		if (v1.size() > 0) {
			items1 = new CoopwithItemVO[v1.size()];
			v1.copyInto(items1);
		}
		if (v2.size() > 0) {
			items2 = new CoopwithItemVO[v2.size()];
			v2.copyInto(items2);
		}

		// init card panel
		getCardPanel().getBillData().clearViewData();
		add(getCardPanel(), BorderLayout.CENTER);
		getCardPanel().getBillData().setHeaderValueVO(headervos[curPage]);
		if (headervos[curPage].getCsourcereceipttype().equals("30")) {
			getCardPanel().getHeadItem("csourceorgid").setRefType("销售组织");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("采购组织");
		} else {
			getCardPanel().getHeadItem("csourceorgid").setRefType("采购组织");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("销售组织");
		}
		if (items1 != null && items1.length > 0) {
			getCardPanel().getBillData()
					.setBodyValueVO("so_coopwith_b", items1);
		}
		if (items2 != null && items2.length > 0) {
			getCardPanel().getBillData().setBodyValueVO("in_out_set", items2);
		}
		// button status setting
		preid = btnStatus;
		btnStatus = 4;
		changeStatus();
		setButtons(cardbutton_Arry);
		getCardPanel().setEnabled(false);
		updateUI();
	}
	public void preCardView(){
		CoopwithItemVO[] bodyvos = null;
		try {
			bodyvos = CoopWith_Help.getBodyVOS(pk);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 订单协同设置的vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// 出入库单协同设置的vo
		Vector<CoopwithItemVO> v2 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items2 = null;
		for (CoopwithItemVO vo : bodyvos) {
			// trim很重要
			if (vo.getCsourcefieldnameid().trim().equals("45"))
				v2.add(vo);
			else
				v1.add(vo);
		}
		// 填充vo数组
		if (v1.size() > 0) {
			items1 = new CoopwithItemVO[v1.size()];
			v1.copyInto(items1);
		}
		if (v2.size() > 0) {
			items2 = new CoopwithItemVO[v2.size()];
			v2.copyInto(items2);
		}
		getCardPanel().getBillData().setHeaderValueVO(preheadervo);
		if (items1 != null && items1.length > 0) {
			getCardPanel().getBillData()
					.setBodyValueVO("so_coopwith_b", items1);
		}
		if (items2 != null && items2.length > 0) {
			getCardPanel().getBillData().setBodyValueVO("in_out_set", items2);
		}
		 
	}
}
