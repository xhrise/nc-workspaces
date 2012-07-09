package nc.ui.ehpta.hq010301;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

import com.ufida.iufo.pub.tools.AppDebug;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *ISingleController
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
	 
	private AggregatedValueObject nowAggVO = null;
	 
	protected IUAPQueryBS iUAPQueryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
       
    protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	@Override
	protected void initSelfData() {

		getBillListPanel().setParentMultiSelect(true);
		getBillListPanel().setMultiSelect(true);
		getBillCardPanel().setBodyMultiSelect(true);

	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String[] itemkeys = new String[]{
				fileDef.getField_Corp(),
				fileDef.getField_Operator(),
				fileDef.getField_Billtype(),
				fileDef.getField_BillStatus(),
				"singledate"
				};
		Object[] values = new Object[]{
				pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype,
				new Integer(IBillStatus.FREE).toString(),
				_getDate(),
				
				};
		
		for(int i = 0; i < itemkeys.length; i++){
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if(item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if(item != null)
				item.setValue(values[i]);
		}
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		try{
			
			if("maintenancedate".equals(e.getKey())){
				String weihu = ((UIRefPane)getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefName();
				getBillCardPanel().getHeadItem("weihudate").setValue(weihu);
			
				String pjfenlei = (String) ((UIComboBox) getBillCardPanel().getHeadItem("priceclassification").getComponent()).getSelectdItemValue();
				
				if(pjfenlei.equals("结算价")){
					StringBuilder builder = new StringBuilder();
					builder.append("select count(1) from ehpta_hangingprice where");
					builder.append(" maintenancedate = '"+getBillCardPanel().getHeadItem("maintenancedate").getValueObject()+"'");
					builder.append(" and priceclassification = '结算价' and pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr,0) = 0 ");
					
					int i = (Integer) iUAPQueryBS.executeQuery(builder.toString() , new ColumnProcessor());
					if(i>0){
						showErrorMessage("当前期间：" + ((UIRefPane)getBillCardPanel().getHeadItem("maintenancedate").getComponent()).getRefName() + " , 已存在结算价记录！");
						getBillCardPanel().getHeadItem("maintenancedate").setValue(null);
						return ;
					}
				} 
				
			} else if("priceclassification".equals(e.getKey())){
				
				String pjfenlei = (String) ((UIComboBox) getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getSelectdItemValue();
				
				if(pjfenlei.equals("结算价")){
					
					StringBuilder builder = new StringBuilder();
					builder.append("select count(1) from ehpta_hangingprice where");
					builder.append(" maintenancedate = '"+getBillCardPanel().getHeadItem("maintenancedate").getValueObject()+"'");
					builder.append(" and priceclassification = '结算价' and pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr,0) = 0 ");
					
					int i = (Integer) iUAPQueryBS.executeQuery(builder.toString() , new ColumnProcessor());
					if(i>0){
						showErrorMessage("当前期间：" + ((UIRefPane)getBillCardPanel().getHeadItem("maintenancedate").getComponent()).getRefName() + " , 已存在结算价记录！");
						((UIComboBox) getBillCardPanel().getHeadItem(e.getKey()).getComponent()).setSelectedIndex(-1);
						return ;
					}
					
					getBillCardPanel().getHeadItem("jiesuanprice").getComponent().setEnabled(true);
					getBillCardPanel().getHeadItem("guapaiprice").getComponent().setEnabled(false);
					getBillCardPanel().getHeadItem("guapaiprice").setValue(null);
					
				} else{
					getBillCardPanel().getHeadItem("guapaiprice").getComponent().setEnabled(true);
					getBillCardPanel().getHeadItem("jiesuanprice").getComponent().setEnabled(false);
					getBillCardPanel().getHeadItem("jiesuanprice").setValue(null);
				}
			}
			
			
		}catch(Exception e2){
			AppDebug.debug();
		}
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		nowAggVO = vo;
		TableColumn tableColumn = null;
		for(int i = 0; i<(getBillListPanel().getHeadTable().getColumnCount()); i++){
			tableColumn = getBillListPanel().getHeadTable().getColumn(getBillListPanel().getHeadTable().getColumnName(i));
		
			if("审核状态".equals(tableColumn.getHeaderValue())){
				continue;
			}
				
			tableColumn.setCellRenderer(new RowRenderer(getBufferData()));
		}
		return super.getExtendStatus(vo);
	}
	
	class RowRenderer extends DefaultTableCellRenderer{
		
		private BillUIBuffer buffer = null;
		
		public RowRenderer(){
			super();
		}
		
		public RowRenderer(BillUIBuffer _buffer){
			super();
			buffer = _buffer;
		}
		
		public Color colora = new Color(236, 244, 244);
		
		public Color colorb = Color.white;
		
		public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row ,int column){
			try{
				AggregatedValueObject aggVO = buffer.getVOByRowNo(row);
				if(Integer.valueOf(aggVO.getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.CHECKPASS){
					setBackground(colora);
				}else{
					setBackground(colorb);
				}
			}catch(Exception e){
				Logger.debug(e);
			}
				
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

	}
}
