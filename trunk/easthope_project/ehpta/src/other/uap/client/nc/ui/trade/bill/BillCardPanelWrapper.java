package nc.ui.trade.bill;

import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.bd.def.DefVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

/**
 * 单据卡片模版的包装。 创建日期：(2004-1-3 16:02:23)
 * 
 * @author：樊冠军1
 */

public class BillCardPanelWrapper extends BillTemplateWrapper implements
		BillBodyMenuListener {

	public BillCardPanel getBillCardPanel() {
		return this.m_BillCardPanel;
	}

	/**
	 * 更改单据卡片模板数值显示位数（根据产品的返回） 创建日期：(2001-12-17 14:40:29)
	 */
	protected void setCardDecimalDigits(int intHeadOrItem, BillData billDataVo, String[][] strShow) throws Exception {
		if (strShow.length < 2) {
			return;
		}
		if (strShow[0].length != strShow[1].length) {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000059")/*
																												 * @res
																												 * "显示位数组第一、二行不匹配"
																												 */);
		}
		for (int i = 0; i < strShow[0].length; i++) {
			String attrName = strShow[0][i];
			Integer attrDigit = new Integer(strShow[1][i]);
			BillItem tmpItem = null;
			switch (intHeadOrItem) {
			case HEAD: {
				tmpItem = billDataVo.getHeadItem(attrName);
				if (tmpItem == null)
					billDataVo.getTailItem(attrName);
				break;
			}
			case BODY: {
				tmpItem = billDataVo.getBodyItem(billDataVo.getTableCodes(BillData.BODY)[0], attrName);
				break;
			}
			}

			if (tmpItem != null) {
				tmpItem.setDecimalDigits(attrDigit.intValue());
			}
		}
	}

	protected void hideBodyColumn() {
		// 进行主子隐藏列的判断
		String[] tmpHideAry = null;
		// tmpHideAry = getCardHeadHideCol();
		// if (tmpHideAry != null) {
		// for (int i = 0; i < tmpHideAry.length; i++) {
		// m_BillCardPanel.hideHeadTableCol(tmpHideAry[i]);
		// }
		// }
		tmpHideAry = m_ctl.getCardBodyHideCol();
		if (tmpHideAry != null) {
			for (int i = 0; i < tmpHideAry.length; i++) {
				m_BillCardPanel.hideBodyTableCol(tmpHideAry[i]);
			}
		}
	}
	
	//为支持节点复制功能，需要能模板加载能真正按照节点号加载
	// 
	// 此种改动方法，最早是cy在 江西电信的5.01上所做，现在适配到5.5
	// 需要支持节点复制功能的产品，需要修改其对应的createBillCardPanelWrapper方法
	// 传入节点号
	
	private String m_sModuleCode = "";
	
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-2-3 14:39:01)
	 * //2007.05.25 cy 为江西电信改动 以前是根据单据类型找到对应的节点号的,复制的节点无法支持自定义单据模版 开始
	 */
	public void setModuleCode(String sModuleCode) {
		this.m_sModuleCode = sModuleCode;

		}

	protected void initCardPanel() {
		try {
			m_BillCardPanel = null;
			m_BillCardPanel = new BillCardPanel();
			m_BillCardPanel.setName(CARDPANEL);
			// 单据类型
			m_BillCardPanel.setBillType(getUIControl().getBillType());
			// 公司
			m_BillCardPanel.setCorp(getClientEnvironment().getCorporation().getPrimaryKey());
			// 操作员
			m_BillCardPanel.setOperator(getClientEnvironment().getUser().getPrimaryKey());

			// 业务类型
			m_BillCardPanel.setBusiType(getBusinessType());
			// 设置第三关键字
			m_BillCardPanel.setNodeKey(getNodeKey());

			// 设置自定义项
			updateUserDefItem();

			m_BillCardPanel.setBillData(getBillData());

			closeBodyMenuShow();

			hideBodyColumn();

			initTotalSumRow();

			initUserFormula();

			nc.vo.pub.bill.BillRendererVO voCell = new nc.vo.pub.bill.BillRendererVO();
			voCell.setShowThMark(true);
			voCell.setShowZeroLikeNull(true);
			m_BillCardPanel.setBodyShowFlags(voCell);

			m_BillCardPanel.addBodyMenuListener(this);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("加载单据卡片模板错误::TestBillBase(getBillCardPanel)!!");
		}

	}

	protected BillCardPanel m_BillCardPanel;

	private BillData m_billData = null;

	// 自定义项的引用
	private ArrayList m_billDef = null;

	// = new ArrayList();

	private Class m_BodyVOClass;

	private CircularlyAccessibleValueObject[] m_CopyedBodyVOs;

	protected ICardController m_ctl = null;

	private boolean m_isShowWhenDefIsUsed;

	public BillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		initCardPanel();
	}

	public BillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, ArrayList defAry) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billDef = defAry;
		initCardPanel();
	}

	public BillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		initCardPanel();
	}

	public BillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData, ArrayList defList) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		m_billDef = defList;
		initCardPanel();
	}

	public BillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData, ArrayList defList, boolean isShowWhenDefIsUsed) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		m_billDef = defList;
		this.m_isShowWhenDefIsUsed = isShowWhenDefIsUsed;
		initCardPanel();

	}
	/**
	 * 通过这个函数可以真正按照节点号加载模板，而不是通过单据类型来关联查找
	 * @param ce
	 * @param ctl
	 * @param pk_busiType
	 * @param nodeKey
	 * @param moduleCode  功能节点号
	 * @param defAry
	 * @throws Exception
	 */
	public BillCardPanelWrapper(
			ClientEnvironment ce,
			ICardController ctl,
			String pk_busiType,
			String nodeKey,
			ArrayList defAry,
			String moduleCode
			)
			throws Exception {
			super(ce, ctl, pk_busiType, nodeKey);
			m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
			m_ctl = ctl;
			m_billDef = defAry;
			setModuleCode(moduleCode);
			initCardPanel();
		}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
	}

	/**
	 * 增行
	 */
	public void addLine() throws Exception {

		getBillCardPanel().addLine();

	}

	/**
	 * 关闭表体菜单 创建日期：(2001-12-17 14:40:29)
	 */
	public void closeBodyMenuShow() {
		String[] tablecodes = getBillCardPanel().getBillData().getBodyTableCodes();

		if (tablecodes != null) {
			for (int i = 0; i < tablecodes.length; i++) {
				getBillCardPanel().setBodyMenuShow(tablecodes[i], false);
			}
		}
	}

	/**
	 * 复制当前所选择的行
	 */
	public void copySelectedLines() {
		int selectedRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (selectedRow != -1) {
			CircularlyAccessibleValueObject[] vos = getSelectedBodyVOs();
			// getBillCardPanel().getBillData().getBillModel().getBodySelectedVOs(
			// m_BodyVOClass.getName());
			setCopyedBodyVOs(vos);

		}
	}

	/**
	 * @return 返回单据模板表体当前被选中的VO，如果没有被选择的行那么返回null
	 */
	public CircularlyAccessibleValueObject[] getSelectedBodyVOs() {
		int[] rows = getBillCardPanel().getBillTable().getSelectedRows();

		if (rows == null || rows.length == 0)
			return null;

		CircularlyAccessibleValueObject[] vos = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(m_BodyVOClass, rows.length);
		for (int i = 0; i < vos.length; i++) {
			vos[i] = getBillCardPanel().getBillModel().getBodyValueRowVO(rows[i], m_BodyVOClass.getName());
		}
		return vos;
	}

	/**
	 * 删除当前所选择的行
	 */
	public void deleteSelectedLines() {
		getBillCardPanel().stopEditing();
		if (getBillCardPanel().getBillTable().getSelectedRow() > -1) {
			// int[] aryRows =
			// getBillCardPanel().getBillTable().getSelectedRows();
			getBillCardPanel().delLine();
		}
	}

	/**
	 * 执行表体公式加载数据。 创建日期：(2004-2-18 22:55:25)
	 * 
	 * @param intRow
	 *            int
	 */
	private void execLoadBodyRowFormula(int intRow) {
		try {
			if (getBillCardPanel().getBillModel() == null)
				return;

			BillItem[] items = getBillCardPanel().getBillModel().getBodyItems();

			String[] formulas = getExecLoadFormula(items);

			if (formulas != null)
				getBillCardPanel().getBillModel().execFormula(intRow, formulas);
		} catch (Exception ex) {
			System.out.println("BillListWrapper:执行行公式加载数据错误");
			ex.printStackTrace();
		}
	}

	/**
	 * 执行显示公式 创建日期：(2003-9-12 16:10:25)
	 */
	protected void execLoadFormula() {
		if (getUIControl().isLoadCardFormula()) {
			BillItem[] billItems = getBillCardPanel().getHeadItems();
			if (billItems != null) {
				for (int i = 0; i < billItems.length; i++) {
					BillItem tmpItem = billItems[i];
					String[] strLoadFormula = tmpItem.getLoadFormula();
					getBillCardPanel().execHeadFormulas(strLoadFormula);
				}
			}
		}
		if (getBillCardPanel().getBillModel() != null) {
			getBillCardPanel().getBillModel().execLoadFormula();
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-02-03 8:39:08)
	 * 
	 * @return nc.ui.pub.bill.BillData
	 */
	protected BillData getBillData() throws Exception {
		if (m_billData == null)
		{
			nc.vo.pub.bill.BillTempletVO vo = null;
			//装载模板
			//2007.05.25 cy 为江西电信改动 以前是根据单据类型找到对应的节点号的,复制的节点无法支持自定义单据模版 开始
			//此种改动方法，最早是cy在5.011上所做，现在适配到5.02
			if (m_sModuleCode == null || m_sModuleCode.trim().length() == 0)
			{
				vo = m_BillCardPanel.getDefaultTemplet(
					getUIControl().getBillType(),
					getBusinessType(),
					m_BillCardPanel.getOperator(),
					m_BillCardPanel.getCorp(),
					m_BillCardPanel.getNodeKey());
			}
			//2007.05.25 cy 为江西电信改动 以前是根据单据类型找到对应的节点号的,复制的节点无法支持自定义单据模版 结束
			else
			{	
				try
				{
					vo = m_BillCardPanel.getDefaultTemplet(
							m_sModuleCode,
						getBusinessType(),
						m_BillCardPanel.getOperator(),
						m_BillCardPanel.getCorp(),
						m_BillCardPanel.getNodeKey());
				}
				catch(Exception e)//未指定默认模板的节点，再利用单据类型去查找一下 
				{
					vo = m_BillCardPanel.getDefaultTemplet(
							getUIControl().getBillType(),
							getBusinessType(),
							m_BillCardPanel.getOperator(),
							m_BillCardPanel.getCorp(),
							m_BillCardPanel.getNodeKey());
				}
			}


			m_billData = new BillData(vo);

		}
		return m_billData;
	}

	/**
	 * 从界面上得到VO。 创建日期：(2004-1-7 10:10:26)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getBillVOFromUI() throws Exception {
		getBillCardPanel().stopEditing();

		AggregatedValueObject billVO = null;

		if (getBillCardPanel().getBillData().isMeataDataTemplate()) {
			Object o = getBillCardPanel().getBillData().getBillObjectByMetaData();
			billVO = creatAggregatedValueObject(o);
		} else
			billVO = getBillCardPanel().getBillValueVO(getUIControl().getBillVoName()[0], getUIControl().getBillVoName()[1], getUIControl().getBillVoName()[2]);
		return billVO;

	}

	private AggregatedValueObject creatAggregatedValueObject(Object o) {
		AggregatedValueObject billVO = null;
		if(o != null) {
			if (getUIControl() instanceof ISingleController) {
				if (o instanceof AggregatedValueObject)
					billVO = (AggregatedValueObject) o;
				else if (((ISingleController) getUIControl()).isSingleDetail()) { // 单表体
					billVO = new HYBillVO();
					billVO.setChildrenVO((CircularlyAccessibleValueObject[]) o);
				} else { // 单表头
					billVO = new HYBillVO();
					billVO.setParentVO((CircularlyAccessibleValueObject) o);
				}
			} else
				billVO = (AggregatedValueObject) o;
		}
		
		return billVO;
	}
	

	/**
	 * 从单据卡片上得到被改变过的vo。 创建日期：(2004-1-7 10:13:37)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		getBillCardPanel().stopEditing();
		AggregatedValueObject billVO = null;

		if (getBillCardPanel().getBillData().isMeataDataTemplate()) {
			Object o = getBillCardPanel().getBillData().getChangeBillObjectByMetaData();
			billVO = creatAggregatedValueObject(o);
		}else
			billVO = getBillCardPanel().getBillValueChangeVO(getUIControl().getBillVoName()[0], getUIControl().getBillVoName()[1], getUIControl().getBillVoName()[2]);
		return billVO;
	}

	/**
	 * 复制被拷贝的VO 创建日期：(2004-1-8 15:33:32)
	 */
	public CircularlyAccessibleValueObject[] getCopyedBodyVOs() {
		return m_CopyedBodyVOs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-3 16:13:15)
	 * 
	 * @return nc.ui.trade.controller.ICardController
	 */
	protected final ICardController getUIControl() {
		return m_ctl;
	}

	/**
	 * 设置表体下拉框 创建日期：(2001-12-17 14:40:29)
	 */
	public void initBodyComboBox(String key, Object[] values, boolean isWhithIndex) {

		if (key == null || values == null)
			return;

		BillItem billItem = getBillCardPanel().getBodyItem(key);

		initComboBox(billItem, values, isWhithIndex);
	}

	/**
	 * 设置表体下拉框 创建日期：(2001-12-17 14:40:29)
	 */
	public void initBodyComboBox(String tablecode, String key, Object[] values, boolean isWhithIndex) {

		if (key == null || values == null)
			return;

		BillItem billItem = getBillCardPanel().getBodyItem(tablecode, key);

		initComboBox(billItem, values, isWhithIndex);
	}

	/**
	 * 设置下拉框 创建日期：(2001-12-17 14:40:29)
	 */
	private void initComboBox(BillItem billItem, Object[] values, boolean isWhithIndex) {

		if (billItem != null && billItem.getDataType() == BillItem.COMBO) {

			nc.ui.pub.beans.UIComboBox cmb = (nc.ui.pub.beans.UIComboBox) billItem.getComponent();

			cmb.removeAllItems();

			for (int i = 0; i < values.length; i++) {
				cmb.addItem(values[i]);
			}

			billItem.setWithIndex(isWhithIndex);
		}

	}

	/**
	 * 设置表头下拉框 创建日期：(2001-12-17 14:40:29)
	 */
	public void initHeadComboBox(String key, Object[] values, boolean isWhithIndex) {

		if (key == null || values == null)
			return;

		BillItem billItem = getBillCardPanel().getHeadItem(key);

		initComboBox(billItem, values, isWhithIndex);
	}

	/**
	 * 初始化汇总合计行,子类可重载此方法。 创建日期：(2004-2-3 15:13:07)
	 */
	protected void initTotalSumRow() {

		// 设置是否显示合计行
		m_BillCardPanel.setTatolRowShow(getUIControl().isShowCardTotal());
		// 设置是否显示行号
		m_BillCardPanel.setRowNOShow(getUIControl().isShowCardRowNo());
	}

	/**
	 * 初试化自定义公式 创建日期：(2004-2-3 15:13:07)
	 */
	protected void initUserFormula() {

		nc.ui.pub.formulaparse.FormulaParse formula = getBillCardPanel().getFormulaParse();

		String classname = "nc.ui.trade.business.HYPubBO_Client";
		String methodname = "findColValue";

		Class returnClass = Object.class;

		Class[] argType = new Class[] { String.class, String.class,
				String.class };

		formula.setSelfMethod(classname, methodname, returnClass, argType, null);

	}

	/**
	 * 增行
	 */
	public void insertLine() throws Exception {

		getBillCardPanel().stopEditing();
		getBillCardPanel().insertLine();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-27 11:09:34)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		try {
			BillScrollPane bsp = getBillCardPanel().getBodyPanel();

			UIMenuItem item = (UIMenuItem) e.getSource();

			if (item == bsp.getMiInsertLine()) {
				insertLine();
			} else if (item == bsp.getMiAddLine()) {
				addLine();
			} else if (item == bsp.getMiDelLine()) {
				deleteSelectedLines();
			} else if (item == bsp.getMiCopyLine()) {
				copySelectedLines();
			} else if (item == bsp.getMiPasteLine()) {
				pasteLines();
			} else if (item == bsp.getMiPasteLineToTail()) {
				pasteLines();
			}
		} catch (Exception ex) {
			System.out.println("line error!!!!");
		}
	}

	/**
	 * 粘贴当前所选择的行
	 */
	public void pasteLines() {
		if (getCopyedBodyVOs() != null)
			for (int i = 0; i < getCopyedBodyVOs().length; i++) {
				getBillCardPanel().stopEditing();
				getBillCardPanel().insertLine();
				int selectedRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().getBillModel().setBodyRowVO(getCopyedBodyVOs()[i], selectedRow);
				execLoadBodyRowFormula(selectedRow);
			}
	}

	
	public void pasteLinesToTail() {
		if (getCopyedBodyVOs() != null)
			for (int i = 0; i < getCopyedBodyVOs().length; i++) {
				getBillCardPanel().stopEditing();
				getBillCardPanel().addLine();
				int lastrow = getBillCardPanel().getBillTable().getRowCount()-1;
				getBillCardPanel().getBillModel().setBodyRowVO(getCopyedBodyVOs()[i], lastrow);
				execLoadBodyRowFormula(lastrow);
			}
		
	}
	
	/**
	 * 此处插入方法说明。 创建日期：(2004-2-3 14:39:01)
	 */
	protected void setBillData(BillData billData) {
		this.m_billData = billData;

	}

	/**
	 * 设置单据卡片界面的数据 创建日期：(2003-9-12 15:50:15)
	 * 
	 * @param billVO
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public void setCardData(AggregatedValueObject billVO) {
		if (billVO == null)
			getBillCardPanel().getBillData().clearViewData();
		else {
			getBillCardPanel().setBillValueVO(billVO);
			execLoadFormula();
		}

	}

	/**
	 * 更改单据卡片模板数值显示位数（根据产品的返回） 创建日期：(2001-12-17 14:40:29)
	 */
	public final void setCardDecimalDigits(String[][] headAry, String[][] itemAry) throws Exception {

		// 更改主表的显示位数
		if (headAry != null) {
			setCardDecimalDigits(HEAD, getBillData(), headAry);
		}
		// 更改子表的显示位数
		if (itemAry != null) {
			setCardDecimalDigits(BODY, getBillData(), itemAry);
		}

	}

	/**
	 * 复制被拷贝的VO 创建日期：(2004-1-8 15:33:32)
	 */
	public void setCopyedBodyVOs(CircularlyAccessibleValueObject[] vos) {
		m_CopyedBodyVOs = vos;
		// m_CopyedBodyVOs.addAll(java.util.Arrays.asList(vos));
	}

	/**
	 * 设置行状态为Normal。 创建日期：(2003-9-19 10:21:43)
	 */
	public void setRowStateToNormal() {
		if (getBillCardPanel().getBillModel() != null) {
			for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
				getBillCardPanel().getBillModel().setRowState(i, BillModel.NORMAL);
			}
		}
	}

	/**
	 * 设置自定义项。 创建日期：(2003-8-15 14:26:05)
	 */
	private final void updateUserDefItem() throws Exception {
		DefVO[] headDef = null, bodyDef = null;
		if (this.m_billDef == null || this.m_billDef.size() != 2)
			return;

		headDef = (DefVO[]) this.m_billDef.get(0);
		bodyDef = (DefVO[]) this.m_billDef.get(1);

		// 查询自定义项设置
		if (headDef != null && headDef.length != 0) {
			getBillData().updateItemByDef(headDef, "vdef", true);
			// added by zhouhai 20050422
			getBillData().updateItemByDef(headDef, "def", true);
			if (m_isShowWhenDefIsUsed) {
				SetDefVisuabled.getInstance().setDefVisuabled(getBillData(), headDef, "vdef", true);
				SetDefVisuabled.getInstance().setDefVisuabled(getBillData(), headDef, "def", true);
			}

		}
		// 表体
		// 查询自定义项设置
		if (bodyDef != null && bodyDef.length != 0) {
			getBillData().updateItemByDef(bodyDef, "vdef", false);
			// added by zhouhai 20050422
			getBillData().updateItemByDef(bodyDef, "def", false);
			if (m_isShowWhenDefIsUsed) {
				SetDefVisuabled.getInstance().setDefVisuabled(getBillData(), bodyDef, "vdef", false);
				SetDefVisuabled.getInstance().setDefVisuabled(getBillData(), bodyDef, "def", false);
			}

		}

	}

	/**
	 * 添加表体附加编辑监听。 创建日期：(2001-3-23 2:20:34)
	 * 
	 * @param ml
	 *            BillEditListener2
	 */
	public void addBodyEditListener2(BillEditListener2 el) {
		getBillCardPanel().addBodyEditListener2(el);

	}

	/**
	 * 添加编辑监听。 创建日期：(2001-3-23 2:20:34)
	 * 
	 * @param ml
	 *            BillEditListener
	 */
	public void addEditListener(BillEditListener el) {
		getBillCardPanel().addEditListener(el);

	}
}