package nc.ui.ehpta.hq010201;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends nc.ui.trade.manage.BillManageUI implements
		ILinkQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4315846814431299564L;
	
	private final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);

	private AggregatedValueObject nowAggVO = null;
	
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}

	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if (listButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		if (hasCommit) {
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}

		if (hasAudit) {
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}

		if (hasCancelAudit) {
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);
		}
		
		// 添加停用按钮
		addPrivateButton(DefaultBillButton.getDisabledButtonVO());
		addPrivateButton(DefaultBillButton.getEnabledButtonVO());
		
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected ManageEventHandler createEventHandler() {
		return new EventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {

		getBillListPanel().setParentMultiSelect(true);

		getButtonManager().getButton(DefaultBillButton.ENABLED).setEnabled(false);
		getButtonManager().getButton(DefaultBillButton.DISABLED).setEnabled(false);
		
		getBillListPanel().setMultiSelect(true);
		getBillCardPanel().setBodyMultiSelect(true);
		

	}
	
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() , "dmakedate" };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() , _getDate() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
		
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
			if("pk_stordoc".equals(e.getKey())) {
				
				UIRefPane storRef = (UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent();
				Object storaddr = iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc = '" + storRef.getRefPK()
						+ "'" , new ColumnProcessor());
				getBillCardPanel().getHeadItem("storaddr").setValue(storaddr);
			}
		} catch(Exception ex) {
			AppDebug.debug(ex);
		}
	}
	
	@Override
	public void afterUpdate() {
		super.afterUpdate();
		
		updateButtonState();
	}
	
	private void updateButtonState() {
		
		if(nowAggVO == null || nowAggVO.getParentVO() == null)
			return ;
		
		UFBoolean ty_flag = (UFBoolean) nowAggVO.getParentVO().getAttributeValue("ty_flag");
		
		try {
			if(ty_flag.booleanValue()) {
				getButtonManager().getButton(DefaultBillButton.DISABLED).setEnabled(false);
				getButtonManager().getButton(DefaultBillButton.ENABLED).setEnabled(true);
			} else {
				getButtonManager().getButton(DefaultBillButton.ENABLED).setEnabled(false);
				getButtonManager().getButton(DefaultBillButton.DISABLED).setEnabled(true);
			}
		} catch(Exception e ) {
			AppDebug.debug(e);
		}
		
		updateButtons();
	
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		nowAggVO = vo;
		
		TableColumn column = null;
		for (int i = 0; i < getBillListPanel().getHeadTable().getColumnCount(); i++) {
			column = getBillListPanel().getHeadTable().getColumn(getBillListPanel().getHeadTable().getColumnName(i));
			
			if("停用标志".equals(column.getHeaderValue()))
					continue;
			
			column.setCellRenderer(new RowRenderer(getBufferData()));
			
		}
		
		return super.getExtendStatus(vo);
	}
	
	class RowRenderer extends javax.swing.table.DefaultTableCellRenderer {

		private static final long serialVersionUID = -7116902044700587819L;

		private BillUIBuffer buffData = null;
		
		public RowRenderer() {
			super();
		}
		
		public RowRenderer(BillUIBuffer _buffData) {
			super();
			
			buffData = _buffData;
		}

		public Color colorDark = new Color(236, 244, 244);

		public Color colorLight = Color.white;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

//			if (row % 2 == 0)
//				setBackground(colorLight);
//			else
//				setBackground(colorDark);

			try {
				AggregatedValueObject aggVO = buffData.getVOByRowNo(row);
				if(Integer.valueOf(aggVO.getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.CHECKPASS) 
					setBackground(colorLight);
				else
					setBackground(colorDark);
				
			} catch(Exception e) {
				Logger.debug(e);
			}
			
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	
}
