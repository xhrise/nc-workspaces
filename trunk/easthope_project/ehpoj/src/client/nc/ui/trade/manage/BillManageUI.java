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
 * 管理（包含List,Card)。 创建日期：(2004-1-3 17:34:24)
 *
 * @author：樊冠军
 */
public abstract class BillManageUI extends AbstractBillUI implements
		ICheckRetVO, Observer, BillEditListener, BillEditListener2,
		IHYBillSource, BillTableMouseListener, ILinkApprove,ILinkMaintain {
	//1单据模板列片基类的包装类
	private BillListPanelWrapper m_BillListPanelWrapper;

	//2单据模板卡片基类的包装类
	private BillCardPanelWrapper m_BillCardPanelWrapper;

	//3界面对应的Action处理事件类
	private ManageEventHandler m_btnAction = null;

	//4单据UI对应的数据模型类
	private BillUIBuffer m_modelData = null;

	//5界面控制类
	private AbstractManageController m_uiCtl = null;

	//
	private nc.ui.pub.beans.UIPanel m_managePanel = null;


	//当前显示的Panel,LIST OR CARD
	protected String m_CurrentPanel = BillTemplateWrapper.LISTPANEL;

	//当前单据的操作状态
	private int m_billOperate = IBillOperate.OP_NOTEDIT;

	private CardLayout m_cardLayOut = null;

	private java.awt.BorderLayout m_borderLayOut = null;

	//
	private boolean m_isUseBillSource = false;


	//表树数据
	private IVOTreeData m_tabletreedata = null;
	//树表工具
	private BillTableCreateTreeTableTool m_listtabledata = null;
	private BillTableCreateTreeTableTool m_cardtabledata = null;
	/**
	 * BilListUI 构造子注解。
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
	 * BilListUI 构造子注解。
	 */
	public BillManageUI(Boolean useBillSource) {
		super();
		this.m_isUseBillSource = useBillSource.booleanValue();
		initialize();
	}

	/**
	 * TestBill 构造子注解。 用于单据联查和审批流使用
	 */
	public BillManageUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super();
		setBusinessType(pk_busitype);
		initialize();
		setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		//加载数据
		try {
			getBufferData().addVOToBuffer(loadHeadData(billId));
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 增加指定行数据。 创建日期：(2004-1-11 11:23:25)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void addListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListWrapper().addListVo(vo);
			setHeadSpecialData(vo, getBufferData().getCurrentRow());
			//执行当前行公式
			getBillListWrapper().execLoadHeadRowFormula(
					getBufferData().getCurrentRow());
		}
	}

	/**
	 * 增加鼠标选择监听。 创建日期：(2004-5-18 10:26:51)
	 */
	public void addMouseSelectListener(BillTableMouseListener ml) {
		getBillListPanel().addMouseListener(ml);

	}

	/**
	 * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
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
	 * 编辑前处理。 创建日期：(2001-3-23 2:02:27)
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
	 * 行改变事件。 创建日期：(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(final nc.ui.pub.bill.BillEditEvent e) {

		if (e.getSource() == getBillListPanel().getParentListPanel().getTable()) {
			if (isUseBillSource() && !isMultiChildSource()) {
				System.out.println("界面上：行发生变换由" + e.getOldRow() + "到"
						+ e.getRow());
				int iOldRow = e.getOldRow();
				int iNewRow = e.getRow();
				System.out.println("数据模型中：行发生变换由" + iOldRow + "到" + iNewRow);
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
	 * 清空表体数据。 创建日期：(2004-5-18 10:51:09)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void clearBody() throws java.lang.Exception {
		getBillListPanel().setBodyValueVO(null);
	}

	/**
	 * 创建BillCardPanelWrapper的方法,可能子类需要重载此方法 创建日期：(2004-2-3 14:08:28)
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
	 * 创建BillListPanelWrapper的方法,可能基类需要重载此方法 创建日期：(2004-2-3 14:08:28)
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
	 * 实例化界面初始控制器 创建日期：(2004-1-3 18:13:36)
	 */
	protected abstract AbstractManageController createController();

	/**
	 * 实例化前台界面的业务委托类
	 * 如果进行事件处理需要重载该方法
	 * 创建日期：(2004-1-3 18:13:36)
	 */
	protected BusinessDelegator createBusinessDelegator() {
		if(getUIControl().getBusinessActionType() == IBusinessActionType.BD)
			return new BDBusinessDelegator();
		else
			return new BusinessDelegator();
	}

	/**
	 * 实例化界面编辑前后事件处理, 如果进行事件处理需要重载该方法 创建日期：(2004-1-3 18:13:36)
	 */
	protected ManageEventHandler createEventHandler() {
		return new ManageEventHandler(this, getUIControl());
	}

	/**
	 * 返回卡片模版。 创建日期：(2002-12-23 9:44:25)
	 */
	public final BillCardPanel getBillCardPanel() {
		return m_BillCardPanelWrapper.getBillCardPanel();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-8 16:19:19)
	 *
	 * @return nc.ui.trade.pub.BillCardPanelWrapper
	 */
	public final BillCardPanelWrapper getBillCardWrapper() {
		return this.m_BillCardPanelWrapper;
	}

	/**
	 * 返回卡片模版。 创建日期：(2002-12-23 9:44:25)
	 */
	public final BillListPanel getBillListPanel() {
		return m_BillListPanelWrapper.getBillListPanel();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-8 16:19:19)
	 *
	 * @return nc.ui.trade.pub.BillListPanelWrapper
	 */
	public final BillListPanelWrapper getBillListWrapper() {
		return this.m_BillListPanelWrapper;
	}

	/**
	 * 获得单据编号，对于需要获得单据编号的单据必须重载此方法。 创建日期：(2003-6-28 10:03:55)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected String getBillNo() throws java.lang.Exception {
		return null;
	}

	/**
	 * 获得单据的操作状态。 创建日期：(2003-7-21 14:47:32)
	 *
	 * @return int
	 */
	public final int getBillOperate() {
		return m_billOperate;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-25 12:45:35)
	 *
	 * @return java.lang.String
	 * @param intState
	 *            int
	 */
	protected String getBillState(int intState) {

		String name = null;
		if (intState < 0)
			name = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000101")/*@res "自由态"*/;
		else
			name = BillStateModel.strStateRemark[intState];
		return name;
	}

	/**
	 * 获取前台单据缓存的数据模型 创建日期：(2003-6-2 10:48:39)
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
	 * 获得界面变化数据VO。 创建日期：(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public AggregatedValueObject getChangedVOFromUI()
			throws java.lang.Exception {
		return this.m_BillCardPanelWrapper.getChangedVOFromUI();
	}

	/**
	 * 返回卡片主表二维数组，第一维为必须为2。 第二维不限， 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}} 注意:
	 * 1.必须保证二行的长度相等。否则系统按默认值2取。 2.必须在该方法内进行实例化,该方法在构造中调用， 本类未实例化
	 * 创建日期：(2001-12-25 10:19:03)
	 *
	 * @return java.lang.String[][]
	 */
	public String[][] getHeadShowNum() {
		return null;
	}

	/**
	 * 返回卡片子表二维数组，第一维为必须为2。 第二维不限， 第一行为字段属性，第二行为显示的位数。
	 * {{"属性A","属性B","属性C","属性D"},{"3","4","5","6"}} 注意:
	 * 1.必须保证二行的长度相等。否则系统按默认值2取。 2.必须在该方法内进行实例化,该方法在构造中调用， 本类未实例化
	 * 创建日期：(2001-12-25 10:19:03)
	 *
	 * @return java.lang.String[][]
	 */
	public String[][] getItemShowNum() {
		return null;
	}

	/**
	 * 获得当前UI对应的布局管理器。 各具体的UI可根据情况进行方法重载, 默认实现为BorderLayout 创建日期：(2004-1-3
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
	 * 返回当前UI对应的控制类实例。 创建日期：(2004-01-06 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	protected final ManageEventHandler getManageEventHandler() {
		if (m_btnAction == null)
			m_btnAction = createEventHandler();
		return m_btnAction;
	}

	/**
	 * 获得当前UI对应的布局管理器。 各具体的UI可根据情况进行方法重载, 默认实现为BorderLayout 创建日期：(2004-1-3
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
	 * 此处插入方法说明。 创建日期：(2004-02-03 16:42:29)
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
	 * 。 创建日期：(2004-1-3 15:20:15)
	 */
	public String getRefBillType() {
		return null;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
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
	 * 返回当前UI对应的控制类实例。 应该在实例里声明一个Control的实例m_uiCtl private XXXCtl m_uiCtl;
	 * 该方法应该实现为 { if(m_uiCtl==null) m_uiCtl=new XXXCtl(); return m_uiCtl; }
	 * 创建日期：(2003-5-27 15:46:35)
	 *
	 * @return nc.ui.tm.pub.ControlBase
	 */
	public final AbstractManageController getUIControl() {
		if (m_uiCtl == null)
			m_uiCtl = createController();
		return m_uiCtl;
	}

	/**
	 * 返回数据VO。 创建日期：(2001-12-18 16:59:11)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public final AggregatedValueObject getVo() throws Exception {
		return getBufferData().getCurrentVO();
	}

	/**
	 * 获得界面全部的数据VO。 创建日期：(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public  AggregatedValueObject getVOFromUI() throws java.lang.Exception {
		return this.m_BillCardPanelWrapper.getBillVOFromUI();

	}

	/**
	 * 初始化按钮。 创建日期：(2004-1-11 16:17:58)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private void initButton() throws java.lang.Exception {
		getButtonManager().getButtonAry(getUIControl().getListButtonAry());
		getButtonManager().getButtonAry(getUIControl().getCardButtonAry());
	}

	/**
	 * 事件的监听方法。 创建日期：(2003-9-15 11:03:30)
	 */
	protected void initEventListener() {
		m_BillCardPanelWrapper.addEditListener(this);
		m_BillCardPanelWrapper.addBodyEditListener2(this);
		m_BillListPanelWrapper.getBillListPanel().addEditListener(this);
		//列表状态排序动作监听
		initTableSoftEventListener();
	
	}

	/**
	 * 初始化方法。 创建日期：(2004-1-3 17:36:11)
	 */
	private void initialize() {
		try {
			this.setLayout(getLayOutManager());

			if (!isUseBillSource()) {
				//初始化按钮
				initButton();

				//设置NodeKey按钮
				getManageEventHandler().initNodeKeyButton();

				//设置其它按钮
				getManageEventHandler().initActionButton();
			}
			this.add(getManagePane(), java.awt.BorderLayout.CENTER);

			//初始化UI
			initUI();

			if (!isUseBillSource())
				addMouseSelectListener(this);

			//设置对model的监听，observer模式
			getBufferData().addObserver(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000118")/*@res "发生异常，BillManageUI界面初始化错误"*/);
		}
	}

	/**
	 * 列表排序事件的监听方法。 创建日期：(2003-9-15 11:03:30)
	 */
	protected void initTableSoftEventListener() {
		//列表状态排序动作监听
//		javax.swing.table.JTableHeader jth = (javax.swing.table.JTableHeader) getBillListWrapper()
//				.getBillListPanel().getHeadTable().getTableHeader();
//		
//		
//		jth.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseClicked(java.awt.event.MouseEvent e) {
//				synVOsSequence();
//			}
//		});
		
		//不想把BillUIBuffer里的List实例直接暴露出来，所以让billuibuffer也实现了IBillRelaSortListener接口
		getBillListPanel().getHeadBillModel().addSortRelaObjectListener(new IBillRelaSortListener() {
			public List getRelaSortObject() {
				return getBufferData().getRelaSortObject();
			}
		
		});
		//排序后需要重设当前行
		getBillListPanel().getHeadBillModel().addBillSortListener2(new BillSortListener2() {
		
			public void currentRowChange(int row) {
				getBufferData().setCurrentRowWithOutTriggerEvent(row);
		
			}
		
		});
			
		
	}

	/**
	 * 初始化设置当前UI, 对应的各抽象UI必须实现该方法(BillCardUI,BillManageUI) 创建日期：(2004-2-19
	 * 13:20:37)
	 */
	public final void initUI() throws Exception {
		getManagePane().removeAll();

		//Add Card
		this.m_BillCardPanelWrapper = createBillCardPanelWrapper();

		getManagePane().add(m_BillCardPanelWrapper.getBillCardPanel(),
				BillTemplateWrapper.CARDPANEL);

		//增加List
		this.m_BillListPanelWrapper = createBillListPanelWrapper();

		getManagePane().add(m_BillListPanelWrapper.getBillListPanel(),
				BillTemplateWrapper.LISTPANEL);

		setCurrentPanel(BillTemplateWrapper.LISTPANEL);

		//初始化自己UI单据模版数据
		initSelfData();

		//初始化事件
		initEventListener();

		//设置小数位数
		getBillListWrapper().setListDecimalDigits(getHeadShowNum(),
				getItemShowNum());
		getBillCardWrapper().setCardDecimalDigits(getHeadShowNum(),
				getItemShowNum());

		//初始化单据状态
		if (getUIControl().isExistBillStatus() && !getBillCardPanel().getBillData().isMeataDataTemplate()) {
			getBillCardWrapper().initHeadComboBox(
					getBillField().getField_BillStatus(),
					new BillStatus().strStateRemark, true);
			getBillListWrapper().initHeadComboBox(
					getBillField().getField_BillStatus(),
					new BillStatus().strStateRemark, true);
		}

		//设置初始状态
		setBillOperate(IBillOperate.OP_INIT);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-12 15:03:31)
	 *
	 * @return java.lang.String
	 */
	public boolean isListPanelSelected() {
		return m_CurrentPanel.equals(BillTemplateWrapper.LISTPANEL);
	}

	/**
	 * 表体是否多子表。 创建日期：(2004-5-20 15:55:06)
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
	 * 是否设置行状态。 创建日期：(2004-4-1 8:47:36)
	 *
	 * @return boolean
	 */
	protected boolean isSetRowNormalState() {
		return true;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-19 12:13:29)
	 *
	 * @return boolean
	 */
	protected boolean isUseBillSource() {
		return this.m_isUseBillSource;
	}

	/**
	 * 查询整个单据VO数据(用于单据联查和工作流使用） 创建日期：(2003-7-16 15:48:52)
	 *
	 * @param key
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected AggregatedValueObject loadHeadData(String key)
			throws java.lang.Exception {
		AggregatedValueObject retVo = (AggregatedValueObject) Class.forName(
				getUIControl().getBillVoName()[0]).newInstance();
		//查询主表
		//因为有的单据的表头VO中以有自定义项目，比如新品牌指标，
		//必须通过单据对应的DMO来查询
		SuperVO tmpvo = (SuperVO) Class.forName(
				getUIControl().getBillVoName()[1]).newInstance();
		retVo.setParentVO(getBusiDelegator().queryByPrimaryKey(
				tmpvo.getClass(), key));
		//子表数据
		//setChcekManAndDate(retVo);
		return retVo;
	}

	/**
	 * 进行主表数据查询。 创建日期：(2004-5-18 10:39:44)
	 *
	 * @param strWhere
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void loadListHeadData(String strWhere,
			IBillReferQuery qryCondition) throws java.lang.Exception {
		SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
				Class.forName(getUIControl().getBillVoName()[1]),
				getUIControl().getBillType(), strWhere);

		//清空缓冲数据
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
	 * 鼠标双击事件。 创建日期：(2001-5-9 8:52:00)
	 *
	 * @param row
	 *            int
	 */
	public void mouse_doubleclick(BillMouseEnent e) {
		if (isListPanelSelected() && e.getPos() == IBillItem.HEAD) {
			try {
				getManageEventHandler().onBoCard();
			} catch (Exception ex) {
				System.out.println("卡片切换错误::" + ex.getMessage());
			}
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
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
	 * 清除指定行的列表的全部表头数据 创建日期：(2004-1-10 15:12:13)
	 *
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public void removeListHeadData(int intRow) throws Exception {
		m_BillListPanelWrapper.getBillListPanel().addEditListener(null);
		getBillListWrapper().removeListVo(intRow);
		m_BillListPanelWrapper.getBillListPanel().addEditListener(this);
	}

	/**
	 * 设置单据编号。 创建日期：(2003-6-28 10:03:55)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
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
	 * 此处插入方法说明。 创建日期：(2003-7-21 14:47:32)
	 *
	 * @param newBillOperate
	 *            int
	 */
	public final void setBillOperate(int newBillOperate) throws Exception {
		m_billOperate = newBillOperate;
		setTotalUIState(newBillOperate);
	}

	/**
	 * 设置标题查询条件。 如果参照界面的子表数据根据查询条件进行设定， 则该接口返回控制器中的子表查询条件， example
	 * :Control.setBodycondition(strBodyCond); 创建日期：(2004-5-18 14:56:52)
	 *
	 * @param strBodyCond
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public void setBodyCondition(String strBodyCond) throws java.lang.Exception {

	}

	/**
	 * 设置表体复杂数据。 需要进行界面转换的数据,如自定义参照的数据 创建日期：(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public abstract void setBodySpecialData(
			CircularlyAccessibleValueObject[] vos) throws Exception;

	/**
	 * 获取前台单据缓存的数据模型 创建日期：(2003-6-2 10:48:39)
	 *
	 * @return nc.vo.pub.SuperVO[]
	 */
	public final void setBufferData(BillUIBuffer newBufferData) {
		m_modelData = newBufferData;
	}

	/**
	 * 设置UI层界面数据。 创建日期：(2004-2-25 19:05:24)
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
	 * 设置单据UI状态为CARD,重载父类的方法 创建日期：(2004-1-11 21:07:42)
	 */
	public final void setCardUIState() {
		if (isListPanelSelected())
			setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	}

	/**
	 * 设置界面切换。 创建日期：(2003-9-12 15:03:31)
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
	 * 设置指定行表头复杂数据。 需要进行界面转换的数据,如自定义参照的数据 创建日期：(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param intRow
	 *            int
	 */
	protected abstract void setHeadSpecialData(
			CircularlyAccessibleValueObject vo, int intRow) throws Exception;

	/**
	 * 设置列表数据。 创建日期：(2004-1-10 15:56:35)
	 */
	protected void setListBodyData() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			this.m_BillListPanelWrapper.setListBodyData(getBufferData()
					.getCurrentRow(), null);
		else {
			this.m_BillListPanelWrapper.setListBodyData(getBufferData()
					.getCurrentRow(), getBufferData().getCurrentVO());
			//设置表体复杂数据
			if (getBufferData().getCurrentVO() != null)
				setBodySpecialData(getBufferData().getCurrentVO()
						.getChildrenVO());
		}
	}

	/**
	 * 设置列表的全部表头数据,重载父类方法 创建日期：(2004-1-10 15:12:13)
	 *
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public void setListHeadData(CircularlyAccessibleValueObject[] headVOs)
			throws Exception {
		getBillListWrapper().setListHeadData(headVOs);
		setTotalHeadSpecialData(headVOs);
	}

	/**
	 * 设置保存后列表数据,重载父类方法 创建日期：(2004-1-11 21:21:27)
	 */
	protected final void setSaveListData(boolean isAdding) throws Exception {
		if (isAdding)
			addListVo();
		else
			updateListVo();
	}

	/**
	 * 设置整个表头复杂数据。 需要进行界面转换的数据,如自定义参照的数据 创建日期：(2003-1-6 14:56:37)
	 *
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param intRow
	 *            int
	 */
	protected abstract void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] vos) throws Exception;

	/**
	 * 设置单据按钮状态，根据串入的VO数据进行判断，可能为空。 创建日期：(2002-12-31 11:19:21)
	 *
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	protected void setTotalUIState(int intOpType) throws Exception {

		//设置按钮状态
		getButtonManager().setButtonByOperate(intOpType);
		updateButtons();
		//根据操作类型设置UI状态
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
//	 * 该方法在列表界面表头排序时同步界面上的单据顺序和VOBuffer中VO的顺序 创建日期：(2003-9-15 14:40:04)
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
				//设置单据状态
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

	//更新单据状态
	protected final void updateBtnStateByCurrentVO() throws Exception {
		//设置单据状态
		if (getUIControl().isExistBillStatus())
			getButtonManager().setButtonByBillStatus(getBufferData(),
					getUIControl().isEditInGoing());
		//设置扩展状态
		getButtonManager().setButtonByextendStatus(
				getExtendStatus(getBufferData().getCurrentVO()));
		//设置页状态
		getButtonManager().setPageButtonState(getBufferData());
		updateButtons();
	}

	/**
	 * 更新指定行数据。 创建日期：(2004-1-11 11:23:25)
	 *
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListWrapper().updateListVo(vo,
					getBufferData().getCurrentRow());

			//执行公式
		}
	}
	/**
	 * 创建表树处理器。
	 * 创建日期：(2004-2-1 18:45:58)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected BillTableCreateTreeTableTool getBillListTableTreeTool() {

		if(m_listtabledata == null)
			m_listtabledata = new BillTableCreateTreeTableTool(getBillListPanel());

		return m_listtabledata;
	}
	/**
	 * 创建表树处理器。
	 * 创建日期：(2004-2-1 18:45:58)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected BillTableCreateTreeTableTool getBillCardTableTreeTool() {

		if(m_cardtabledata == null)
			m_cardtabledata = new BillTableCreateTreeTableTool(getBillCardPanel());

		return m_cardtabledata;
	}
	/**
	 * 初始化树数据
	 * 创建日期：(2004-1-3 18:13:36)
	 */
	protected nc.ui.trade.pub.IVOTreeData getCreateTableTreeData(){
		if(m_tabletreedata == null)
			m_tabletreedata = ((ITableTreeController)getUIControl()).getTableTreeData();

		return m_tabletreedata;
	}
	/**
	 * 设置表到树表。
	 * 创建日期：(2004-2-1 18:45:58)
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
			//加载数据
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
		//加载数据
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