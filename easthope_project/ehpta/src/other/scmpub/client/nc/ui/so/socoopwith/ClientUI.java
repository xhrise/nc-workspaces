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
	 * ��ť��״̬���� 0-->��̬ 1-->���Ӱ�ť������״̬ 2-->�б���ʾ 3-->update 4-->��Ƭ��ʾ
	 * 5-->����6-->�༭7-->����
	 * 
	 */
	private int btnStatus = 0;

	/*
	 * �Ƿ��ڱ���״̬
	 */
	private boolean isSave = true;

	private int preid = 0;

	private String strWhere = null;

	private final String stockOrder = "21";

	private final String sellOrder = "30";

	private BillCardPanel m_cardpanel = null;

	private BillListPanel m_listpanel = null;

	private QueryDlg queryClient = null;

	private ButtonObject bo_Add = null; // ����

	private ButtonObject bo_Save = null;// ����

	private ButtonObject bo_Modify = null;// ά��

	private ButtonObject bo_Search = null;// ��ѯ

	private ButtonObject bo_Look = null;// ���

	private ButtonObject bo_CardShow = null;// ��Ƭ

	// ������ά����ť���Ӱ�ť
	private ButtonObject ch_Modify = null;// �޸�

	private ButtonObject ch_Cancel = null;// ȡ��

	private ButtonObject ch_Delete = null;// ɾ��

	private ButtonObject ch_Copy = null;// ����

	// �����������ť���Ӱ�ť
	private ButtonObject ch_Refresh = null;// ˢ��

	private ButtonObject ch_FirstPage = null;// ��ҳ

	private ButtonObject ch_PrePage = null;// ��ҳ

	private ButtonObject ch_NextPage = null;// ��ҳ

	private ButtonObject ch_LastPage = null;// ĩҳ

	// ��ť����
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
		btnStatus = 0;// ��ť���ڳ�ʼ̬
		changeStatus();
		cardbutton_Arry = new ButtonObject[] { bo_Add, bo_Save, bo_Modify,
				bo_Search, bo_Look, bo_CardShow };
		listbutton_Arry = new ButtonObject[] { bo_Add, bo_Save, bo_Modify,
				bo_Search, bo_Look, bo_CardShow };
	}

	@SuppressWarnings( { "deprecation", "deprecation", "deprecation",
			"deprecation" })
	private void initButtons() {
		// TODO huzy1 ��ť����ʹ��ButtonTreeģʽ
		ButtonTree btnTree = null;
		try {
			btnTree = new ButtonTree("400106");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
		}
		bo_Add = btnTree.getButton("����");// ����
		bo_Save = btnTree.getButton("����");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000002"));// ����
		bo_Modify = btnTree.getButton("ά��");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000003"));// ά��
		bo_Search = btnTree.getButton("��ѯ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000004"));// ��ѯ
		bo_Look = btnTree.getButton("���");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000005"));// ���
		bo_CardShow = btnTree.getButton("��Ƭ��ʾ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000006"));
		// ������ά����ť���Ӱ�ť
		ch_Modify = btnTree.getButton("�޸�");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000008"));// �޸�
		ch_Cancel = btnTree.getButton("ȡ��");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000009"));// ȡ��
		ch_Delete = btnTree.getButton("ɾ��");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000010"));// ɾ��
		ch_Copy = btnTree.getButton("����");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000011"));// ����
		// �����������ť���Ӱ�ťa
		ch_Refresh = btnTree.getButton("ˢ��");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000012"));// ˢ��
		ch_FirstPage = btnTree.getButton("��ҳ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000013"));// ��ҳ
		ch_PrePage = btnTree.getButton("��ҳ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000014"));// ��ҳ
		ch_NextPage = btnTree.getButton("��ҳ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000015"));// ��ҳ
		ch_LastPage = btnTree.getButton("ĩҳ");// NCLangRes.getInstance().getStrByID(
		// "400106", "Coopwith400106-000016"));// ĩҳ
		// Ϊ��ť����Ӱ�ť
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
			// ����ģ������
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
			// TODO huzy1 ������������ģ�� by liuzy
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
		// // ��������ֶ�
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
		if (bo == bo_Add) { // ���� ��ť����¼�����Ӧ
			onAdd();
		} else if (bo == bo_Save) { // ���� ��ť����¼�����Ӧ
			onSave();

		} else if (bo == bo_Search) { // ��ѯ ��ť����¼�����Ӧ
			onQuery();
		} else if (bo == bo_CardShow
				&& bo_CardShow.getName().trim().equals(
						NCLangRes.getInstance().getStrByID("400106",
								"ClientUI-000032")/* "��Ƭ��ʾ" */)) {// ��Ƭ��ʾ
			// ��ť����¼�����Ӧ
			onCardShow();
		} else if (bo == bo_CardShow
				&& bo_CardShow.getName().trim().equals(
						NCLangRes.getInstance().getStrByID("400106",
								"ClientUI-000033")/* "�б���ʾ" */)) {// �б�
			// ��ť����¼�����Ӧ
			onListShow();
		} else if (bo == ch_Modify) {// �޸� ��ť����¼�����Ӧ
			onEdite();
		} else if (bo == ch_Cancel) {// ȡ�� ��ť����¼�����Ӧ
			onCancel();
		} else if (bo == ch_Delete) {// ɾ�� ��ť����¼�����Ӧ
			onDelete();
		} else if (bo == ch_Copy) {// ���� ��ť����¼�����Ӧ
			onCopy();
		} else if (bo == ch_Refresh) {// ˢ�� ��ť����¼�����Ӧ
			onRefresh();
		} else if (bo == ch_FirstPage) {// ��ҳ ��ť����¼�����Ӧ
			onFirstPage();
		} else if (bo == ch_PrePage) {// ��ҳ ��ť����¼�����Ӧ
			onPrePage();
		} else if (bo == ch_NextPage) {// ��һҳ ��ť����¼�����Ӧ
			onNextPage();
		} else if (bo == ch_LastPage) {// ĩҳ ��ť����¼�����Ӧ
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
		 * ��ť��״̬���� 0-->��̬ 1-->���Ӱ�ť������״̬ 2-->�б���ʾ 3-->update 4-->��Ƭ��ʾ
		 * 5-->����6-->�༭7-->����
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
			SCMEnv.error("����У��ʧ��", e1);
			showErrorMessage(e1.getMessage());
			return;
		}
		// ������ݵĺϷ���
		if (!chechValidation(vo))
			return;
		if (btnStatus == 3) {
			try {
				if (CoopWith_Help.update(pk, vo))
					showWarningMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000000")/* ���³ɹ� */);
				else {
					showWarningMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000002")/* ��Ϣ���ظ� */);
					return;
				}
				isSave = true;
				preid = btnStatus;
				btnStatus = 5;
				changeStatus();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000001")/* ����ʧ�� */);
				showHintMessage(e.getMessage());
			}
			return;
		}
		try {
			String key = CoopWith_Help.insert(vo);
			if (key == null) {
				MessageDialog
						.showErrorDlg(this, "��������", NCLangRes.getInstance()
								.getStrByID("400106", "ClientUI-000002")/* �������Ϣ���ظ� */);
			} else {
				showHintMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000003")/* ����ɹ� */);
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
																				 * ����ʧ�ܣ�
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

	// ��Ƭ��ʾ
	public boolean onCardShow() {
		// this.remove(m_cardpanel);
		int selectRow = -1;
		selectRow = getListPanel().getHeadTable().getSelectedRow();
		if (selectRow < 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000005")/* ��ѡ��һ������ */);
			return false;
		}
		isList = false;
		CoopwithHeaderVO header = new CoopwithHeaderVO();

		if (getListPanel().getHeadTable().getValueAt(selectRow, 0).toString()
				.equals("���۶���")) {
			header.setCsourcereceipttype("30");
			header.setCtargetreceipttype("21");
			getCardPanel().getHeadItem("csourceorgid").setRefType("������֯");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("�ɹ���֯");
		} else {
			header.setCsourcereceipttype("21");
			header.setCtargetreceipttype("30");
			getCardPanel().getHeadItem("csourceorgid").setRefType("�ɹ���֯");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("������֯");

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
		// ����Эͬ���õ�vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// ����ⵥЭͬ���õ�vo
		Vector<CoopwithItemVO> v2 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items2 = null;
		for (CoopwithItemVO vo : bodyvos) {
			// trim����Ҫ
			if (vo.getCsourcefieldnameid().trim().equals("45"))
				v2.add(vo);
			else
				v1.add(vo);
		}
		// ���vo����
		if (v1.size() > 0) {
			items1 = new CoopwithItemVO[v1.size()];
			v1.copyInto(items1);
		}
		if (v2.size() > 0) {
			items2 = new CoopwithItemVO[v2.size()];
			v2.copyInto(items2);
		}

		// �Ա�ͷ��Դ���������ֶν�������
		if (header.getCsourcereceipttype().equals("30"))
			((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
					.getComponent()).setSelectedItem("���۶���");
		else
			((UIComboBox) getCardPanel().getHeadItem("csourcereceipttype")
					.getComponent()).setSelectedItem("�ɹ�����");

		// ��ʼ����Ƭ����
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
		// ��صİ�ť����
		preid = btnStatus;
		btnStatus = 4;
		changeStatus();
		setButtons(cardbutton_Arry);
		getCardPanel().setEnabled(false);
		updateUI();
		return true;
	}

	// �б���ʾ
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

	// ��ѯ-----����
	public void onQuery() {
		// ���ز���ʾ��ѯģ��
		if (getQueryDlg().showModal() == MessageDialog.ID_OK) {
			onListShow();
			strWhere = queryClient.getWhereSQL();
			try {
				CoopwithHeaderVO[] headervos = CoopWith_Help
						.getHeaderVOS(strWhere);
				// ��headervos�Ա�������
				getListPanel().setHeaderValueVO(headervos);
				int rows = getListPanel().getHeadTable().getRowCount();
				for (int i = 0; i < rows; i++) {
					String type = getListPanel().getHeadTable()
							.getValueAt(i, 0).toString();
					if (type.trim().equals("���۶���")) {
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

	// ɾ����ť----����
	public void onDelete() {
		if (isList) {
			int selectRow = -1;
			selectRow = getListPanel().getHeadTable().getSelectedRow();
			if (selectRow < 0) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000005")/* ��ѡ��һ������ */);
				return;
			}
			int ireturn = MessageDialog.showYesNoCancelDlg(this, "��ʾ",
					NCLangRes.getInstance().getStrByID("400106",
							"ClientUI-000006")/* �Ƿ�ɾ���ü�¼ */);
			if (ireturn != MessageDialog.ID_YES) {
				return;
			}
			CoopwithHeaderVO header = new CoopwithHeaderVO();

			String sourceReceipt = getListPanel().getHeadTable().getValueAt(
					selectRow, 0).toString();
			if (sourceReceipt.trim().equals("���۶���")) {
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
						"ClientUI-000007")/* ɾ���ɹ� */);
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
					"ClientUI-000008")/* ɾ��ʧ�� */);
			// TODO Auto-generated catch block

		}
		if (flag)
			showWarningMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000007")/* ɾ���ɹ� */);
	}

	// �༭��ť����¼��Ĵ���
	public void onEdite() {
		if (isList)
			if (!onCardShow())
				return;
		isSave = false;
		getCardPanel().setEnabled(true);
		// ���ñ�ͷ�Ƿ�ɱ༭
		enableHeader();
		// ����ctargetfieldnameid���ɱ༭
		getCardPanel().getBodyItem("so_coopwith_b", "ctargetfieldnameid")
				.setEnabled(false);
		getCardPanel().getBodyItem("in_out_set", "ctargetfieldnameid")
				.setEnabled(false);
		preid = btnStatus;
		btnStatus = 3;
		changeStatus();
		setButtons(cardbutton_Arry);
		// ���޸�
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
		 * ��������۶��� ������ctargetreceipttypeΪ������֯ ctargetreceipttypeΪ�ɹ���֯
		 */
		if (csource.equals(sellOrder)) {
			((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
					.getComponent()).setSelectedItem("�ɹ�����");
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
					.getComponent()).setSelectedItem("���۶���");
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
		// in_out_set�ӱ�����
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

	// ���ư�ť�Ĵ���
	/*
	 * ����ѡ�е��У��õ����е�vo Ȼ�����һ�� �������е�ֵ��vo ����ʼ��
	 */
	public void onCopy() {

		if (isList) {
			int rowIndex = -1;
			rowIndex = getListPanel().getHeadTable().getSelectedRow();
			if (rowIndex < 0) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
						"ClientUI-000009")/* ��ѡ��Ҫ���Ƽ�¼�� */);
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
				if (type.trim().equals("���۶���")) {
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
		if (btnStatus == 0) {// ��ʼ��״̬
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
					"ClientUI-000032")/* ��Ƭ��ʾ */);
		} else if (btnStatus == 1) {// ��Ӱ�ť������״̬
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
					"ClientUI-000033")/* "�б���ʾ" */);
			bo_CardShow.setEnabled(false);

		} else if (btnStatus == 2) {// bo_List��ť������״̬
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
					"ClientUI-000032")/* "��Ƭ��ʾ" */);
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
					"ClientUI-000033")/* "�б���ʾ" */);
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
					"ClientUI-000010")/* ԭ�������Ͳ���Ϊ��\n */);
		if (head.getCcustomerid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000011")/* ԭ��˾����Ϊ��\n */);
		if (head.getCbusitypeid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000012")/* ԭҵ�����Ͳ���Ϊ��\n */);
		if (head.getCsourceorgid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000013")/* ԭ������֯����Ϊ��\n */);
		if (head.getCtargetreceipttype() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000014")/* Ŀ�ĵ������Ͳ���Ϊ��\n */);
		if (head.getCmaincustomerid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000015")/* Ŀ�Ĺ�˾����Ϊ��\n */);
		if (head.getCtargetbusitypeid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000016")/* Ŀ��ҵ�����Ͳ���Ϊ��\n */);
		if (head.getCtargetorgid() == null)
			errorMessage.append(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000017")/* Ŀ�Ĺ�����֯����Ϊ�� */);
		if (!errorMessage.toString().equals("")) {
			showErrorMessage(errorMessage.toString());
			return false;
		}
		return checkBodyValidate(vo);
	}

	/*
	 * EditListener������ʵ�ֵķ��� ��Ҫ��ʹ���ڱ�ͷ���� ����ĵ�Ԫ��༭ǰ�������¼� ��������Ҫ�ǽ��б�ͷBillItem�Ĳ�������
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
			if (valueModule.trim().equals("ϵͳֵ")) {
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
			 * ��������۶��� ������ctargetreceipttypeΪ������֯ ctargetreceipttypeΪ�ɹ���֯
			 */
			if (csource.equals(sellOrder)) {
				((UIComboBox) getCardPanel().getHeadItem("ctargetreceipttype")
						.getComponent()).setSelectedItem("�ɹ�����");
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
						.getComponent()).setSelectedItem("���۶���");
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
	 * EditListener2������ʵ�ֵķ��� ��Ҫ��ʹ���ڱ��岿�� ����ĵ�Ԫ��༭ǰ�������¼� ��������Ҫ�ǽ��е�Ԫ��������͵����
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.pub.bill.BillEditListener2#beforeEdit(nc.ui.pub.bill.BillEditEvent)
	 */
	@SuppressWarnings("deprecation")
	public boolean beforeEdit(BillEditEvent e) {
		int rowIndex = e.getRow();
		// �ӱ�so_coopwith_b�Ĵ���------����Эͬ����
		if (e.getTableCode().equals("so_coopwith_b")) {
			if (e.getKey().equals("vvaluename")) {// ȡֵ���ݸ���ȡֵģʽ������Ӧ������

				String source_receipt = getCardPanel().getHeadItem(
						"csourcereceipttype").getValue();
				String templetPk = "30";
				String title = "���۶���VO����";
				if (source_receipt == null) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000018")/* Դ�������Ͳ���Ϊ�� */);
					return true;
				}
				if (source_receipt.trim().equals("21")) {
					templetPk = "21";
					title = "�ɹ�����VO����";
				}

				Object valueModule = getCardPanel().getBillModel(
						"so_coopwith_b").getValueAt(rowIndex, "fvaluemodule");
				if (valueModule == null || valueModule.toString().equals("")) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000019")/* ����ѡ��ȡֵ��ʽ */);
					return true;
				}
				/*
				 * ����vvalue������Ϊ�Զ���Ĳ��� ��ʾ���ֶ��У��ֶα��룬��������������/�ӱ�
				 */
				if (valueModule.equals("��Ӧֵ")) {
					parallelValueHandler(templetPk, title, rowIndex);
				} else if (valueModule.equals("�̶�ֵ")) {
					fixValueHandler("so_coopwith_b");
				} else if (valueModule.equals("ϵͳֵ")) {
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
		// �ӱ�in_out_set�Ĵ���--------����ⵥЭͬ����
		else if (e.getTableCode().equals("in_out_set")) {
			if (e.getKey().equals("vvaluename")) {
				String valueModule = getCardPanel().getBillModel("in_out_set")
						.getValueAt(rowIndex, "fvaluemodule").toString();
				if (valueModule == null || valueModule.equals("")) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000020")/* ��ѡ��ȡֵģʽ */);
					return true;
				}
				if (valueModule.equals("��Ӧֵ")) {
					String templetPk = "4C";// ���۳��ⵥ
					String title = "���۳��ⵥ��VO";
					parallelValueHandler(templetPk, title, rowIndex);
				} else if (valueModule.equals("�̶�ֵ")) {
					fixValueHandler("in_out_set");
				} else if (valueModule.equals("ϵͳֵ")) {
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
	 * ʹ��ͷ���ɱ༭ �˷�����һ�����߷��� ���ú�--����ͷ���ɱ༭
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
	 * ��ʼ�������� ��Ҫ������������ �ֱ���EditListener2��EditListener
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
		// ע��ֵ�ı��¼� ��ʹֵ�����ı�ʱ��֮������ص������
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
								.getRefType().equals("������֯"))
					getCardPanel().getHeadItem("csourceorgid").setRefType(
							"������֯");
				((UIRefPane) getCardPanel().getHeadItem("csourceorgid")
						.getComponent()).getRefModel().setWherePart(
						"belongcorp='" + sourceCompanyID.trim() + "'");
			} else if (sourceRecepit.trim().equals(stockOrder)) {
				if (getCardPanel().getHeadItem("csourceorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("csourceorgid")
								.getRefType().equals("�ɹ���֯"))
					getCardPanel().getHeadItem("csourceorgid").setRefType(
							"�ɹ���֯");
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
								.getRefType().equals("������֯"))
					getCardPanel().getHeadItem("ctargetorgid").setRefType(
							"������֯");
				((UIRefPane) getCardPanel().getHeadItem("ctargetorgid")
						.getComponent()).getRefModel().setWherePart(
						"belongcorp='" + targetCompanyID.trim() + "'");
			} else if (targetRecepit.trim().equals(stockOrder)) {
				if (getCardPanel().getHeadItem("ctargetorgid").getRefType() == null
						|| !getCardPanel().getHeadItem("ctargetorgid")
								.getRefType().equals("�ɹ���֯"))
					getCardPanel().getHeadItem("ctargetorgid").setRefType(
							"�ɹ���֯");
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
							"UPPSCMCommon-000270")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH001")/* @res "�Ƿ񱣴����޸ĵ����ݣ���" */);
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
			ref.setRefNodeName("����");
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
						ref.setRefNodeName("�Զ������");
						ref.setRefModel(refModel);
					} catch (Exception e) {
						Logger.error(e.getMessage());
					}
				} else
					ref.setRefNodeName(reftype);
			} else {
				ref.setIsCustomDefined(true);
				ref.setRefNodeName("�Զ������");
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
				vo.setFvaluemodule("��Ӧֵ");
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
							"400106", "ClientUI-000024")/* ȡֵģʽ����Ϊ�� */);
					return false;
				}
			}
		}
		for (CoopwithItemVO item : items2) {
			if (item.getVvalue() != null) {
				length++;
				if (item.getFvaluemodule() == null) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"400106", "ClientUI-000024")/* ȡֵģʽ����Ϊ�� */);
					return false;
				}
			}
		}
		if (length == 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("400106",
					"ClientUI-000025")/* ���岻��Ϊ�� */);
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
					"ClientUI-000026")/* Դ��˾��Ŀ�Ĺ�˾����Ϊͬһ��˾ */);
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
		// ����Эͬ���õ�vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// ����ⵥЭͬ���õ�vo
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
			getCardPanel().getHeadItem("csourceorgid").setRefType("������֯");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("�ɹ���֯");
		} else {
			getCardPanel().getHeadItem("csourceorgid").setRefType("�ɹ���֯");
			getCardPanel().getHeadItem("ctargetorgid").setRefType("������֯");
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
//		 ����Эͬ���õ�vo
		Vector<CoopwithItemVO> v1 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items1 = null;
		// ����ⵥЭͬ���õ�vo
		Vector<CoopwithItemVO> v2 = new Vector<CoopwithItemVO>();
		CoopwithItemVO[] items2 = null;
		for (CoopwithItemVO vo : bodyvos) {
			// trim����Ҫ
			if (vo.getCsourcefieldnameid().trim().equals("45"))
				v2.add(vo);
			else
				v1.add(vo);
		}
		// ���vo����
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
