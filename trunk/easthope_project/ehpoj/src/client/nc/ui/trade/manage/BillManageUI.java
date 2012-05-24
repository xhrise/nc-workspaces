package nc.ui.trade.manage;

import java.awt.CardLayout;
import java.util.List;
import java.util.Observer;

import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillSortListener2;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ITableTreeController;
import nc.ui.trade.billsource.IHYBillSource;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.buffer.BillStateModel;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.BillStatus;

/**
 * ��������List,Card)�� �������ڣ�(2004-1-3 17:34:24)
 *
 * @author�����ھ�
 */
public abstract class BillManageUI extends AbstractBillUI implements
		ICheckRetVO, Observer, BillEditListener, BillEditListener2,
		IHYBillSource, BillTableMouseListener, ILinkApprove,ILinkMaintain {
	//1����ģ����Ƭ����İ�װ��
	private BillListPanelWrapper m_BillListPanelWrapper;

	//2����ģ�忨Ƭ����İ�װ��
	private BillCardPanelWrapper m_BillCardPanelWrapper;

	//3�����Ӧ��Action�����¼���
	private ManageEventHandler m_btnAction = null;

	//4����UI��Ӧ������ģ����
	private BillUIBuffer m_modelData = null;

	//5���������
	private AbstractManageController m_uiCtl = null;

	//
	private nc.ui.pub.beans.UIPanel m_managePanel = null;


	//��ǰ��ʾ��Panel,LIST OR CARD
	protected String m_CurrentPanel = BillTemplateWrapper.LISTPANEL;

	//��ǰ���ݵĲ���״̬
	private int m_billOperate = IBillOperate.OP_NOTEDIT;

	private CardLayout m_cardLayOut = null;

	private java.awt.BorderLayout m_borderLayOut = null;

	//
	private boolean m_isUseBillSource = false;


	//��������
	private IVOTreeData m_tabletreedata = null;
	//������
	private BillTableCreateTreeTableTool m_listtabledata = null;
	private BillTableCreateTreeTableTool m_cardtabledata = null;
	/**
	 * BilListUI ������ע�⡣
	 */
	public BillManageUI() {
		super();
		initialize();
	}

	public BillManageUI(FramePanel fp) {
		setFrame(fp);
		initialize();
	}
	
	/**
	 * BilListUI ������ע�⡣
	 */
	public BillManageUI(Boolean useBillSource) {
		super();
		this.m_isUseBillSource = useBillSource.booleanValue();
		initialize();
	}

	/**
	 * TestBill ������ע�⡣ ���ڵ��������������ʹ��
	 */
	public BillManageUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super();
		setBusinessType(pk_busitype);
		initialize();
		setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		//��������
		try {
			getBufferData().addVOToBuffer(loadHeadData(billId));
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ����ָ�������ݡ� �������ڣ�(2004-1-11 11:23:25)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void addListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListWrapper().addListVo(vo);
			setHeadSpecialData(vo, getBufferData().getCurrentRow());
			//ִ�е�ǰ�й�ʽ
			getBillListWrapper().execLoadHeadRowFormula(
					getBufferData().getCurrentRow());
		}
	}

	/**
	 * �������ѡ������� �������ڣ�(2004-5-18 10:26:51)
	 */
	public void addMouseSelectListener(BillTableMouseListener ml) {
		getBillListPanel().addMouseListener(ml);

	}

	/**
	 * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's observers
	 * notified of the change.
	 *
	 * @param o
	 *            the observable object.
	 * @param arg
	 *            an argument passed to the <code>notifyObservers</code>
	 *            method.
	 */
	public void afterUpdate() {
	}

	/**
	 * �༭ǰ���� �������ڣ�(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's observers
	 * notified of the change.
	 *
	 * @param o
	 *            the observable object.
	 * @param arg
	 *            an argument passed to the <code>notifyObservers</code>
	 *            method.
	 */
	public boolean beforeUpdate() {
		return true;
	}

	/**
	 * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(final nc.ui.pub.bill.BillEditEvent e) {

		if (e.getSource() == getBillListPanel().getParentListPanel().getTable()) {
			if (isUseBillSource() && !isMultiChildSource()) {
				System.out.println("�����ϣ��з����任��" + e.getOldRow() + "��"
						+ e.getRow());
				int iOldRow = e.getOldRow();
				int iNewRow = e.getRow();
				System.out.println("����ģ���У��з����任��" + iOldRow + "��" + iNewRow);
				getBillListPanel().setBodyModelDataCopy(iOldRow);
				if (!getBillListPanel().setBodyModelData(iNewRow)) {
					getBufferData().setCurrentRow(e.getRow());
					getBillListPanel().setBodyModelDataCopy(iNewRow);
				} else
					getBillListPanel().repaint();
			} else {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getBufferData().setCurrentRow(e.getRow());
					}
				});
			}
		}

	}

	/**
	 * ��ձ������ݡ� �������ڣ�(2004-5-18 10:51:09)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void clearBody() throws java.lang.Exception {
		getBillListPanel().setBodyValueVO(null);
	}

	/**
	 * ����BillCardPanelWrapper�ķ���,����������Ҫ���ش˷��� �������ڣ�(2004-2-3 14:08:28)
	 *
	 * @return nc.ui.trade.bill.BillCardPanelWrapper
	 */
	protected BillCardPanelWrapper createBillCardPanelWrapper()
			throws Exception {
		String funcode = null;
		try
		{
			funcode = getModuleCode();
		}
		catch (RuntimeException e)
		{
			
		}
		return new BillCardPanelWrapper(getClientEnvironment(), getUIControl(),
		                                getBusinessType(), getNodeKey(), getBillDef(getUIControl()
		                                                                            .getHeadZYXKey(), getUIControl().getBodyZYXKey()),funcode);

	}

	/**
	 * ����BillListPanelWrapper�ķ���,���ܻ�����Ҫ���ش˷��� �������ڣ�(2004-2-3 14:08:28)
	 *
	 * @return nc.ui.trade.bill.BillCardPanelWrapper
	 */
	protected BillListPanelWrapper createBillListPanelWrapper()
			throws Exception {
		String funcode = null;
		try
		{
			funcode = getModuleCode();
		}
		catch (RuntimeException e)
		{
			
		}		
		return new BillListPanelWrapper(getClientEnvironment(), getUIControl(),
		                				getBusinessType(), getNodeKey(), getBillDef(getUIControl()
		                						.getHeadZYXKey(), getUIControl().getBodyZYXKey()),funcode);


	}

	/**
	 * ʵ���������ʼ������ �������ڣ�(2004-1-3 18:13:36)
	 */
	protected abstract AbstractManageController createController();

	/**
	 * ʵ����ǰ̨�����ҵ��ί����
	 * ��������¼�������Ҫ���ظ÷���
	 * �������ڣ�(2004-1-3 18:13:36)
	 */
	protected BusinessDelegator createBusinessDelegator() {
		if(getUIControl().getBusinessActionType() == IBusinessActionType.BD)
			return new BDBusinessDelegator();
		else
			return new BusinessDelegator();
	}

	/**
	 * ʵ��������༭ǰ���¼�����, ��������¼�������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected ManageEventHandler createEventHandler() {
		return new ManageEventHandler(this, getUIControl());
	}

	/**
	 * ���ؿ�Ƭģ�档 �������ڣ�(2002-12-23 9:44:25)
	 */
	public final BillCardPanel getBillCardPanel() {
		return m_BillCardPanelWrapper.getBillCardPanel();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 16:19:19)
	 *
	 * @return nc.ui.trade.pub.BillCardPanelWrapper
	 */
	public final BillCardPanelWrapper getBillCardWrapper() {
		return this.m_BillCardPanelWrapper;
	}

	/**
	 * ���ؿ�Ƭģ�档 �������ڣ�(2002-12-23 9:44:25)
	 */
	public final BillListPanel getBillListPanel() {
		return m_BillListPanelWrapper.getBillListPanel();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 16:19:19)
	 *
	 * @return nc.ui.trade.pub.BillListPanelWrapper
	 */
	public final BillListPanelWrapper getBillListWrapper() {
		return this.m_BillListPanelWrapper;
	}

	/**
	 * ��õ��ݱ�ţ�������Ҫ��õ��ݱ�ŵĵ��ݱ������ش˷����� �������ڣ�(2003-6-28 10:03:55)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected String getBillNo() throws java.lang.Exception {
		return null;
	}

	/**
	 * ��õ��ݵĲ���״̬�� �������ڣ�(2003-7-21 14:47:32)
	 *
	 * @return int
	 */
	public final int getBillOperate() {
		return m_billOperate;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-12-25 12:45:35)
	 *
	 * @return java.lang.String
	 * @param intState
	 *            int
	 */
	protected String getBillState(int intState) {

		String name = null;
		if (intState < 0)
			name = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000101")/*@res "����̬"*/;
		else
			name = BillStateModel.strStateRemark[intState];
		return name;
	}

	/**
	 * ��ȡǰ̨���ݻ��������ģ�� �������ڣ�(2003-6-2 10:48:39)
	 *
	 * @return nc.vo.pub.SuperVO[]
	 */
	public final BillUIBuffer getBufferData() {
		if (m_modelData == null)
			m_modelData = createBillUIBuffer();
		return m_modelData;
	}

	protected BillUIBuffer createBillUIBuffer() {
		return new BillUIBuffer(getUIControl(), getBusiDelegator());
	}

	/**
	 * ��ý���仯����VO�� �������ڣ�(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public AggregatedValueObject getChangedVOFromUI()
			throws java.lang.Exception {
		return this.m_BillCardPanelWrapper.getChangedVOFromUI();
	}

	/**
	 * ���ؿ�Ƭ�����ά���飬��һάΪ����Ϊ2�� �ڶ�ά���ޣ� ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}} ע��:
	 * 1.���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ�� 2.�����ڸ÷����ڽ���ʵ����,�÷����ڹ����е��ã� ����δʵ����
	 * �������ڣ�(2001-12-25 10:19:03)
	 *
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	/**
	 * ���ؿ�Ƭ�ӱ��ά���飬��һάΪ����Ϊ2�� �ڶ�ά���ޣ� ��һ��Ϊ�ֶ����ԣ��ڶ���Ϊ��ʾ��λ����
	 * {{"����A","����B","����C","����D"},{"3","4","5","6"}} ע��:
	 * 1.���뱣֤���еĳ�����ȡ�����ϵͳ��Ĭ��ֵ2ȡ�� 2.�����ڸ÷����ڽ���ʵ����,�÷����ڹ����е��ã� ����δʵ����
	 * �������ڣ�(2001-12-25 10:19:03)
	 *
	 * @return java.lang.String[][]
	 */
	public String[][] getItemShowNum() {
		return null;
	}

	/**
	 * ��õ�ǰUI��Ӧ�Ĳ��ֹ������� �������UI�ɸ���������з�������, Ĭ��ʵ��ΪBorderLayout �������ڣ�(2004-1-3
	 * 21:57:38)
	 *
	 * @return java.awt.LayoutManager
	 */
	protected java.awt.BorderLayout getLayOutManager() {
		if (m_borderLayOut == null)
			m_borderLayOut = new java.awt.BorderLayout();
		return m_borderLayOut;
	}

	/**
	 * ���ص�ǰUI��Ӧ�Ŀ�����ʵ���� �������ڣ�(2004-01-06 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	protected final ManageEventHandler getManageEventHandler() {
		if (m_btnAction == null)
			m_btnAction = createEventHandler();
		return m_btnAction;
	}

	/**
	 * ��õ�ǰUI��Ӧ�Ĳ��ֹ������� �������UI�ɸ���������з�������, Ĭ��ʵ��ΪBorderLayout �������ڣ�(2004-1-3
	 * 21:57:38)
	 *
	 * @return java.awt.LayoutManager
	 */
	protected java.awt.CardLayout getManageLayOutManager() {
		if (m_cardLayOut == null)
			m_cardLayOut = new java.awt.CardLayout();
		return m_cardLayOut;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-02-03 16:42:29)
	 *
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	public nc.ui.pub.beans.UIPanel getManagePane() {
		if (m_managePanel == null) {
			m_managePanel = new nc.ui.pub.beans.UIPanel();
			m_managePanel.setLayout(getManageLayOutManager());
		}
		return m_managePanel;
	}

	/**
	 * �� �������ڣ�(2004-1-3 15:20:15)
	 */
	public String getRefBillType() {
		return null;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 *
	 * @version (00-6-6 13:33:25)
	 *
	 * @return java.lang.String
	 */
	public String getTitle() {
		return m_BillCardPanelWrapper.getBillCardPanel().getBillData()
				.getTitle();
	}

	/**
	 * ���ص�ǰUI��Ӧ�Ŀ�����ʵ���� Ӧ����ʵ��������һ��Control��ʵ��m_uiCtl private XXXCtl m_uiCtl;
	 * �÷���Ӧ��ʵ��Ϊ { if(m_uiCtl==null) m_uiCtl=new XXXCtl(); return m_uiCtl; }
	 * �������ڣ�(2003-5-27 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	public final AbstractManageController getUIControl() {
		if (m_uiCtl == null)
			m_uiCtl = createController();
		return m_uiCtl;
	}

	/**
	 * ��������VO�� �������ڣ�(2001-12-18 16:59:11)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public final AggregatedValueObject getVo() throws Exception {
		return getBufferData().getCurrentVO();
	}

	/**
	 * ��ý���ȫ��������VO�� �������ڣ�(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public  AggregatedValueObject getVOFromUI() throws java.lang.Exception {
		return this.m_BillCardPanelWrapper.getBillVOFromUI();

	}

	/**
	 * ��ʼ����ť�� �������ڣ�(2004-1-11 16:17:58)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void initButton() throws java.lang.Exception {
		getButtonManager().getButtonAry(getUIControl().getListButtonAry());
		getButtonManager().getButtonAry(getUIControl().getCardButtonAry());
	}

	/**
	 * �¼��ļ��������� �������ڣ�(2003-9-15 11:03:30)
	 */
	protected void initEventListener() {
		m_BillCardPanelWrapper.addEditListener(this);
		m_BillCardPanelWrapper.addBodyEditListener2(this);
		m_BillListPanelWrapper.getBillListPanel().addEditListener(this);
		//�б�״̬����������
		initTableSoftEventListener();
	
	}

	/**
	 * ��ʼ�������� �������ڣ�(2004-1-3 17:36:11)
	 */
	private void initialize() {
		try {
			this.setLayout(getLayOutManager());

			if (!isUseBillSource()) {
				//��ʼ����ť
				initButton();

				//����NodeKey��ť
				getManageEventHandler().initNodeKeyButton();

				//����������ť
				getManageEventHandler().initActionButton();
			}
			this.add(getManagePane(), java.awt.BorderLayout.CENTER);

			//��ʼ��UI
			initUI();

			if (!isUseBillSource())
				addMouseSelectListener(this);

			//���ö�model�ļ�����observerģʽ
			getBufferData().addObserver(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000118")/*@res "�����쳣��BillManageUI�����ʼ������"*/);
		}
	}

	/**
	 * �б������¼��ļ��������� �������ڣ�(2003-9-15 11:03:30)
	 */
	protected void initTableSoftEventListener() {
		//�б�״̬����������
//		javax.swing.table.JTableHeader jth = (javax.swing.table.JTableHeader) getBillListWrapper()
//				.getBillListPanel().getHeadTable().getTableHeader();
//		
//		
//		jth.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseClicked(java.awt.event.MouseEvent e) {
//				synVOsSequence();
//			}
//		});
		
		//�����BillUIBuffer���Listʵ��ֱ�ӱ�¶������������billuibufferҲʵ����IBillRelaSortListener�ӿ�
		getBillListPanel().getHeadBillModel().addSortRelaObjectListener(new IBillRelaSortListener() {
			public List getRelaSortObject() {
				return getBufferData().getRelaSortObject();
			}
		
		});
		//�������Ҫ���赱ǰ��
		getBillListPanel().getHeadBillModel().addBillSortListener2(new BillSortListener2() {
		
			public void currentRowChange(int row) {
				getBufferData().setCurrentRowWithOutTriggerEvent(row);
		
			}
		
		});
			
		
	}

	/**
	 * ��ʼ�����õ�ǰUI, ��Ӧ�ĸ�����UI����ʵ�ָ÷���(BillCardUI,BillManageUI) �������ڣ�(2004-2-19
	 * 13:20:37)
	 */
	public final void initUI() throws Exception {
		getManagePane().removeAll();

		//Add Card
		this.m_BillCardPanelWrapper = createBillCardPanelWrapper();

		getManagePane().add(m_BillCardPanelWrapper.getBillCardPanel(),
				BillTemplateWrapper.CARDPANEL);

		//����List
		this.m_BillListPanelWrapper = createBillListPanelWrapper();

		getManagePane().add(m_BillListPanelWrapper.getBillListPanel(),
				BillTemplateWrapper.LISTPANEL);

		setCurrentPanel(BillTemplateWrapper.LISTPANEL);

		//��ʼ���Լ�UI����ģ������
		initSelfData();

		//��ʼ���¼�
		initEventListener();

		//����С��λ��
		getBillListWrapper().setListDecimalDigits(getHeadShowNum(),
				getItemShowNum());
		getBillCardWrapper().setCardDecimalDigits(getHeadShowNum(),
				getItemShowNum());

		//��ʼ������״̬
		if (getUIControl().isExistBillStatus() && !getBillCardPanel().getBillData().isMeataDataTemplate()) {
			getBillCardWrapper().initHeadComboBox(
					getBillField().getField_BillStatus(),
					new BillStatus().strStateRemark, true);
			getBillListWrapper().initHeadComboBox(
					getBillField().getField_BillStatus(),
					new BillStatus().strStateRemark, true);
		}

		//���ó�ʼ״̬
		setBillOperate(IBillOperate.OP_INIT);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-12 15:03:31)
	 *
	 * @return java.lang.String
	 */
	public boolean isListPanelSelected() {
		return m_CurrentPanel.equals(BillTemplateWrapper.LISTPANEL);
	}

	/**
	 * �����Ƿ���ӱ� �������ڣ�(2004-5-20 15:55:06)
	 *
	 * @return boolean
	 */
	protected boolean isMultiChildSource() {
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo != null && vo instanceof nc.vo.trade.pub.IExAggVO)
			return true;
		else
			return false;
	}

	/**
	 * �Ƿ�������״̬�� �������ڣ�(2004-4-1 8:47:36)
	 *
	 * @return boolean
	 */
	protected boolean isSetRowNormalState() {
		return true;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-5-19 12:13:29)
	 *
	 * @return boolean
	 */
	protected boolean isUseBillSource() {
		return this.m_isUseBillSource;
	}

	/**
	 * ��ѯ��������VO����(���ڵ�������͹�����ʹ�ã� �������ڣ�(2003-7-16 15:48:52)
	 *
	 * @param key
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected AggregatedValueObject loadHeadData(String key)
			throws java.lang.Exception {
		AggregatedValueObject retVo = (AggregatedValueObject) Class.forName(
				getUIControl().getBillVoName()[0]).newInstance();
		//��ѯ����
		//��Ϊ�еĵ��ݵı�ͷVO�������Զ�����Ŀ��������Ʒ��ָ�꣬
		//����ͨ�����ݶ�Ӧ��DMO����ѯ
		SuperVO tmpvo = (SuperVO) Class.forName(
				getUIControl().getBillVoName()[1]).newInstance();
		retVo.setParentVO(getBusiDelegator().queryByPrimaryKey(
				tmpvo.getClass(), key));
		//�ӱ�����
		//setChcekManAndDate(retVo);
		return retVo;
	}

	/**
	 * �����������ݲ�ѯ�� �������ڣ�(2004-5-18 10:39:44)
	 *
	 * @param strWhere
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void loadListHeadData(String strWhere,
			IBillReferQuery qryCondition) throws java.lang.Exception {
		SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
				Class.forName(getUIControl().getBillVoName()[1]),
				getUIControl().getBillType(), strWhere);

		//��ջ�������
		getBufferData().clear();
		if (queryVos != null && queryVos.length != 0) {
			for (int i = 0; i < queryVos.length; i++) {
				AggregatedValueObject aVo = (AggregatedValueObject) Class
						.forName(getUIControl().getBillVoName()[0])
						.newInstance();
				aVo.setParentVO(queryVos[i]);
				getBufferData().addVOToBuffer(aVo);
			}
			setListHeadData(queryVos);
			getBufferData().setCurrentRow(0);
		} else {
			setListHeadData(queryVos);
			//getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
		}
	}

	/**
	 * ���˫���¼��� �������ڣ�(2001-5-9 8:52:00)
	 *
	 * @param row
	 *            int
	 */
	public void mouse_doubleclick(BillMouseEnent e) {
		if (isListPanelSelected() && e.getPos() == IBillItem.HEAD) {
			try {
				getManageEventHandler().onBoCard();
			} catch (Exception ex) {
				System.out.println("��Ƭ�л�����::" + ex.getMessage());
			}
		}
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 *
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo
	 *            ButtonObject
	 */
	public final void onButtonClicked(ButtonObject bo) {
		getManageEventHandler().onButton(bo);
	}

	/**
	 * ���ָ���е��б��ȫ����ͷ���� �������ڣ�(2004-1-10 15:12:13)
	 *
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public void removeListHeadData(int intRow) throws Exception {
		m_BillListPanelWrapper.getBillListPanel().addEditListener(null);
		getBillListWrapper().removeListVo(intRow);
		m_BillListPanelWrapper.getBillListPanel().addEditListener(this);
	}

	/**
	 * ���õ��ݱ�š� �������ڣ�(2003-6-28 10:03:55)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void setBillNo() throws java.lang.Exception {
		BillItem noItem = getBillCardPanel().getHeadItem(
				getBillField().getField_BillNo());
		if (noItem == null)
			return;
		noItem.setValue(getBillNo());
		noItem.setEnabled(getBusiDelegator().getParaBillNoEditable()
				.booleanValue());
		noItem.setEdit(getBusiDelegator().getParaBillNoEditable()
				.booleanValue());
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-7-21 14:47:32)
	 *
	 * @param newBillOperate
	 *            int
	 */
	public final void setBillOperate(int newBillOperate) throws Exception {
		m_billOperate = newBillOperate;
		setTotalUIState(newBillOperate);
	}

	/**
	 * ���ñ����ѯ������ ������ս�����ӱ����ݸ��ݲ�ѯ���������趨�� ��ýӿڷ��ؿ������е��ӱ��ѯ������ example
	 * :Control.setBodycondition(strBodyCond); �������ڣ�(2004-5-18 14:56:52)
	 *
	 * @param strBodyCond
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void setBodyCondition(String strBodyCond) throws java.lang.Exception {

	}

	/**
	 * ���ñ��帴�����ݡ� ��Ҫ���н���ת��������,���Զ�����յ����� �������ڣ�(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public abstract void setBodySpecialData(
			CircularlyAccessibleValueObject[] vos) throws Exception;

	/**
	 * ��ȡǰ̨���ݻ��������ģ�� �������ڣ�(2003-6-2 10:48:39)
	 *
	 * @return nc.vo.pub.SuperVO[]
	 */
	public final void setBufferData(BillUIBuffer newBufferData) {
		m_modelData = newBufferData;
	}

	/**
	 * ����UI��������ݡ� �������ڣ�(2004-2-25 19:05:24)
	 *
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public void setCardUIData(AggregatedValueObject vo) throws Exception {
		getBillCardWrapper().setCardData(vo);
		if (vo == null)
			return;
		if (vo.getParentVO() != null)
			setHeadSpecialData(vo.getParentVO(), -1);
		if (vo.getChildrenVO() != null)
			setBodySpecialData(vo.getChildrenVO());
	}

	/**
	 * ���õ���UI״̬ΪCARD,���ظ���ķ��� �������ڣ�(2004-1-11 21:07:42)
	 */
	public final void setCardUIState() {
		if (isListPanelSelected())
			setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	}

	/**
	 * ���ý����л��� �������ڣ�(2003-9-12 15:03:31)
	 *
	 * @param newCurrentPanel
	 *            java.lang.String
	 */
	public void setCurrentPanel(java.lang.String newCurrentPanel) {
		m_CurrentPanel = newCurrentPanel;
		getManageLayOutManager().show(this.getManagePane(), m_CurrentPanel);
		if (isListPanelSelected()) {
			setButtons(getButtonManager().getButtonAry(
					getUIControl().getListButtonAry()));
			try {
				if (getFrame() != null)
					setTitleText(getTitle());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			setButtons(getButtonManager().getButtonAry(
					getUIControl().getCardButtonAry()));
			if (getFrame() != null)
				setTitleText(getTitle());
		}
		updateButtons();
	}

	/**
	 * ����ָ���б�ͷ�������ݡ� ��Ҫ���н���ת��������,���Զ�����յ����� �������ڣ�(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param intRow
	 *            int
	 */
	protected abstract void setHeadSpecialData(
			CircularlyAccessibleValueObject vo, int intRow) throws Exception;

	/**
	 * �����б����ݡ� �������ڣ�(2004-1-10 15:56:35)
	 */
	protected void setListBodyData() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			this.m_BillListPanelWrapper.setListBodyData(getBufferData()
					.getCurrentRow(), null);
		else {
			this.m_BillListPanelWrapper.setListBodyData(getBufferData()
					.getCurrentRow(), getBufferData().getCurrentVO());
			//���ñ��帴������
			if (getBufferData().getCurrentVO() != null)
				setBodySpecialData(getBufferData().getCurrentVO()
						.getChildrenVO());
		}
	}

	/**
	 * �����б��ȫ����ͷ����,���ظ��෽�� �������ڣ�(2004-1-10 15:12:13)
	 *
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public void setListHeadData(CircularlyAccessibleValueObject[] headVOs)
			throws Exception {
		getBillListWrapper().setListHeadData(headVOs);
		setTotalHeadSpecialData(headVOs);
	}

	/**
	 * ���ñ�����б�����,���ظ��෽�� �������ڣ�(2004-1-11 21:21:27)
	 */
	protected final void setSaveListData(boolean isAdding) throws Exception {
		if (isAdding)
			addListVo();
		else
			updateListVo();
	}

	/**
	 * ����������ͷ�������ݡ� ��Ҫ���н���ת��������,���Զ�����յ����� �������ڣ�(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param intRow
	 *            int
	 */
	protected abstract void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] vos) throws Exception;

	/**
	 * ���õ��ݰ�ť״̬�����ݴ����VO���ݽ����жϣ�����Ϊ�ա� �������ڣ�(2002-12-31 11:19:21)
	 *
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	protected void setTotalUIState(int intOpType) throws Exception {

		//���ð�ť״̬
		getButtonManager().setButtonByOperate(intOpType);
		updateButtons();
		//���ݲ�����������UI״̬
		switch (intOpType) {
		case OP_ADD: {
			getBillCardPanel().setEnabled(true);
			getBillCardPanel().addNew();
			setDefaultData();
			setBillNo();
//			getBillCardPanel().requestFocus();
//			getBillCardPanel().requestFocusToFirstEditItem();
			break;
		}
		case OP_EDIT: {
			getBillCardPanel().setEnabled(true);
			getBillCardPanel().transferFocusToFirstEditItem();
			if (isSetRowNormalState())
				m_BillCardPanelWrapper.setRowStateToNormal();
			break;
		}
		case OP_REFADD: {
			getBillCardPanel().setEnabled(true);
			break;
		}
		case OP_INIT: {
			this.m_BillCardPanelWrapper.setCardData(null);
		}
		case OP_NOTEDIT: {
			getBillCardPanel().setEnabled(false);
			getBillListPanel().setEnabled(false);
			break;
		}
		default: {
			break;
		}
		}
		
	}

//	/**
//	 * �÷������б�����ͷ����ʱͬ�������ϵĵ���˳���VOBuffer��VO��˳�� �������ڣ�(2003-9-15 14:40:04)
//	 */
//	private final void synVOsSequence() {
//		try {
//
//			ArrayList pks = new ArrayList();
//			for (int i = 0; i < getBillListWrapper().getBillListPanel()
//					.getHeadTable().getRowCount(); i++) {
//				pks.add(getBillListWrapper().getBillListPanel()
//						.getHeadBillModel().getValueAt(i,
//								getUIControl().getPkField()));
//			}
//			getBufferData().synVOsSequence(
//					(String[]) pks.toArray(new String[0]));
//		} catch (Exception e) {
//			e.printStackTrace();
//			showErrorMessage(e.getMessage());
//		}
//	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's observers
	 * notified of the change.
	 *
	 * @param o
	 *            the observable object.
	 * @param arg
	 *            an argument passed to the <code>notifyObservers</code>
	 *            method.
	 */
	public void update(java.util.Observable o, java.lang.Object arg) {
		if (beforeUpdate()) {
			try {
				if (isListPanelSelected()){
					//edit by stl2006-08-21 set the selected state of the current row 
					setListBodyData();
					int nrow=getBufferData().getCurrentRow();
					int maxSelIndex=getBillListPanel().getHeadTable().getSelectionModel().getMaxSelectionIndex();
					int minSelIndex=getBillListPanel().getHeadTable().getSelectionModel().getMinSelectionIndex();
					if(nrow>=0&&maxSelIndex==minSelIndex){
						getBillListPanel().addEditListener(null);
						getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(nrow,nrow);
						getBillListPanel().addEditListener(this);
					}
				}
				else
					setCardUIData(getBufferData().getCurrentVO());
			
				this.getBillCardPanel().updateValue();
				//���õ���״̬
				updateBtnStateByCurrentVO();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
			if(getUIControl() instanceof ITableTreeController){
			    setTableToTreeTable();
			}
		}

		afterUpdate();

	}

	//���µ���״̬
	protected final void updateBtnStateByCurrentVO() throws Exception {
		//���õ���״̬
		if (getUIControl().isExistBillStatus())
			getButtonManager().setButtonByBillStatus(getBufferData(),
					getUIControl().isEditInGoing());
		//������չ״̬
		getButtonManager().setButtonByextendStatus(
				getExtendStatus(getBufferData().getCurrentVO()));
		//����ҳ״̬
		getButtonManager().setPageButtonState(getBufferData());
		updateButtons();
	}

	/**
	 * ����ָ�������ݡ� �������ڣ�(2004-1-11 11:23:25)
	 *
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListWrapper().updateListVo(vo,
					getBufferData().getCurrentRow());

			//ִ�й�ʽ
		}
	}
	/**
	 * ����������������
	 * �������ڣ�(2004-2-1 18:45:58)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected BillTableCreateTreeTableTool getBillListTableTreeTool() {

		if(m_listtabledata == null)
			m_listtabledata = new BillTableCreateTreeTableTool(getBillListPanel());

		return m_listtabledata;
	}
	/**
	 * ����������������
	 * �������ڣ�(2004-2-1 18:45:58)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected BillTableCreateTreeTableTool getBillCardTableTreeTool() {

		if(m_cardtabledata == null)
			m_cardtabledata = new BillTableCreateTreeTableTool(getBillCardPanel());

		return m_cardtabledata;
	}
	/**
	 * ��ʼ��������
	 * �������ڣ�(2004-1-3 18:13:36)
	 */
	protected nc.ui.trade.pub.IVOTreeData getCreateTableTreeData(){
		if(m_tabletreedata == null)
			m_tabletreedata = ((ITableTreeController)getUIControl()).getTableTreeData();

		return m_tabletreedata;
	}
	/**
	 * ���ñ�����
	 * �������ڣ�(2004-2-1 18:45:58)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected void setTableToTreeTable() {
		if(getCreateTableTreeData() != null){

			BillTableCreateTreeTableTool tabletreetool = null;

			if(isListPanelSelected())
				tabletreetool = getBillListTableTreeTool();
			else
				tabletreetool = getBillCardTableTreeTool();

			if(getCreateTableTreeData() instanceof IVOTreeDataByID){
				IVOTreeDataByID idtree = (IVOTreeDataByID)getCreateTableTreeData();
				tabletreetool.changeToTreeTable(idtree.getIDFieldName(),idtree.getParentIDFieldName(),idtree.getShowFieldName());
			}else{
				IVOTreeDataByCode idtree = (IVOTreeDataByCode)getCreateTableTreeData();
				tabletreetool.changeToTreeTableByCode(idtree.getCodeFieldName(),idtree.getCodeRule(),idtree.getShowFieldName());
			}
			tabletreetool.expandToLevel(-1);
		}
	}
	
	public void doApproveAction(ILinkApproveData approvedata)
	{
	
//			setBusinessType(approvedata.pk_busitype);
			setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			//��������
			try {
				getBufferData().addVOToBuffer(loadHeadData(approvedata.getBillID()));
				setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
	}

	public void doMaintainAction(ILinkMaintainData maintaindata) {
		setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		//��������
		try {
			getBufferData().addVOToBuffer(loadHeadData(maintaindata.getBillID()));
			setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	protected void saveOnClosing() throws Exception {
		getManageEventHandler().onBoSave();

		
	}
	
	
	
}