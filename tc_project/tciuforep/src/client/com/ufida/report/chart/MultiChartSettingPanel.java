package com.ufida.report.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.constenum.DefaultConstEnum;

import com.ufida.dataset.descriptor.AggrItem;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropEvent;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropListener;
import com.ufsoft.iufo.fmtplugin.chart.CategoryChartModel;
import com.ufsoft.iufo.fmtplugin.chart.ChartConstants;
import com.ufsoft.iufo.fmtplugin.chart.ChartDesc;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.fmtplugin.chart.MultipleAxisChartModel;
import com.ufsoft.iufo.fmtplugin.chart.NumberChartAxis;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.chart.IChartModel;

public class MultiChartSettingPanel extends ChartSettingPanel implements
		AxisPropListener {
	private static final long serialVersionUID = 2098981543367246928L;

	private JCheckBox cbItemLabel = new JCheckBox();

	private JCheckBox obItemLabel = new JCheckBox();
	
	private DefaultTableModel tableModel =  new ItemTableModel();;
	
	private JTable table = new UITable(tableModel);

	private DataDetialAxis[] dataAxis = null;

	/**
	 * @i18n miufopublic307=合计
	 * @i18n miufo1001255=计数
	 * @i18n miufo1001258=平均
	 * @i18n miufo5308000005=最大
	 * @i18n miufo1001257=最小
	 */
	public final static DefaultConstEnum[] AGGR_TYPES = new DefaultConstEnum[] {
			new DefaultConstEnum(AggrItem.TYPE_SUM, StringResource.getStringResource("miufopublic307")),
			new DefaultConstEnum(AggrItem.TYPE_COUNT, StringResource.getStringResource("miufo1001255")),
			new DefaultConstEnum(AggrItem.TYPE_AVAGE, StringResource.getStringResource("miufo1001258")),
			new DefaultConstEnum(AggrItem.TYPE_MAX, StringResource.getStringResource("miufo5308000005")),
			new DefaultConstEnum(AggrItem.TYPE_MIN, StringResource.getStringResource("miufo1001257")),

	};

	/**
	 * @i18n uibichart00051=分类轴
	 * @i18n miufo00266=次分类轴
	 */
	public MultiChartSettingPanel(IChartModel chartModel) {

		super(chartModel);
		this.addListener(this);

		setLayout(null);
		final CategoryChartModel model = (CategoryChartModel) chartModel;

		Field[] flds = getFieldsInfo();
		final int height = 58;
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("uibichart00051"), model.getCategoryAxis(),
				flds);
		ap.setBounds(0, 0, 505, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00266"), model.getCategoryAxis2(), flds);
		ap.setBounds(0, 60, 505, height);
		this.add(ap);

		JScrollPane sp = new JScrollPane();
		sp.setBounds(0, 120, 380, 180);
		this.add(sp);
		initDataPanel(model, sp);

		JPanel pnlOption = new JPanel();
		pnlOption.setBounds(385, 120, 120, 180);
		this.add(pnlOption);
		initOptionPnl(model, pnlOption);

	}
	
	/**
	 * @i18n miufo00267=高级选项
	 * @i18n miufo00268=显示值标签
	 * @i18n iufobi00065=显示叠加图
	 */
	private void initOptionPnl(final CategoryChartModel model, JPanel pnlOption) {
		pnlOption.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00267"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		cbItemLabel.setText(StringResource.getStringResource("miufo00268"));
		cbItemLabel.setBounds(39, 42, 118, 26);
		pnlOption.add(cbItemLabel);
		cbItemLabel.setSelected(model.isItemLabelVisible());
		
		obItemLabel.setText(StringResource.getStringResource("iufobi00065"));
		obItemLabel.setBounds(39, 62, 118, 26);
		pnlOption.add(obItemLabel);
		obItemLabel.setSelected(isMultiChart());
		int iParentType = ChartConstants.getParentType(model.getType());
		obItemLabel.setEnabled(iParentType == ChartConstants.CHART_BAR || iParentType == ChartConstants.CHART_LINE || iParentType == ChartConstants.CHART_AREA);
	}

	/**
	 * @i18n miufo00269=数据轴
	 */
	private void initDataPanel(final CategoryChartModel model,
			JScrollPane pnlDataAxis) {
		pnlDataAxis.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo00269"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		pnlDataAxis.setColumnHeaderView(table.getTableHeader());
		pnlDataAxis.setViewportView(table);
		TableColumn column = table.getColumn(COLUMN_NAMES[0]);
		column.setWidth(50);

		column = table.getColumn(COLUMN_NAMES[2]);
		column.setCellEditor(new DefaultCellEditor(new UIComboBox(AGGR_TYPES)));
		
		column = table.getColumn(COLUMN_NAMES[3]);
		column.setCellEditor(new DefaultCellEditor(new UIComboBox(getCombineChartType())));
		initTableData();

		table.updateUI();
	}
	
	private boolean isMultiChart(){
		return getChartModel() instanceof MultipleAxisChartModel;
	}
	
	/**
	 * @i18n miufoinputnew00066=显示
	 * @i18n miufo00270=数据列
	 * @i18n miufo00271=汇总类型
	 * @i18n miufo1000602=图表类型
	 */
	private final static String[] COLUMN_NAMES = { StringResource.getStringResource("miufoinputnew00066"), StringResource.getStringResource("miufo00270"), StringResource.getStringResource("miufo00271"),StringResource.getStringResource("miufo1000602") };

	private final static int[] COMBINE_CHART_TYPE = new int[]{ChartConstants.CHART_BAR,ChartConstants.CHART_LINE,ChartConstants.CHART_AREA};
	
	private void initTableData() {

		Field[] flds = getFieldsInfo();
		if (flds == null) {
			return;
		}
		ArrayList<DataDetialAxis> numberFlds = new ArrayList<DataDetialAxis>();

		CategoryChartModel model = (CategoryChartModel) getChartModel();
		NumberChartAxis[] existDataAxis = model.getDataAxises();

		for (Field f : flds) {
			if (DataTypeConstant.isNumberType(f.getDataType())) {
				DataDetialAxis da = new DataDetialAxis(f);
				int chartType = da.getChartType() == ChartConstants.UNDIFINED
						|| !isMultiChart() ? ((ChartModel) getChartModel())
						.getType() : da.getChartType();
				da.setChartType(chartType);
				for (NumberChartAxis a : existDataAxis) {
					if (a.getFieldId().equals(f.getFldname())) {
						da.setSelected(true);
						da.setAggrType(a.getAggrType());
						chartType = a.getChartType() == ChartConstants.UNDIFINED
								|| !isMultiChart() ? ((ChartModel) getChartModel())
								.getType()
								: a.getChartType();
						da.setChartType(chartType);
					}
				}
				numberFlds.add(da);
			}
		}
		dataAxis = numberFlds.toArray(new DataDetialAxis[0]);
		tableModel.setRowCount(dataAxis.length);
		table.updateUI();
	}

	/**
	 * @i18n miufo00272=未选择数据列或数据集无数值字段。
	 */
	private void saveTableData() {
		ArrayList<NumberChartAxis> axis = new ArrayList<NumberChartAxis>();
		for (DataDetialAxis a : dataAxis) {
			if (a.isSelected()) {
				NumberChartAxis na = new NumberChartAxis();
				na.setFieldId(a.getFld().getFldname());
				na.setLabel(a.getFld().getCaption());
				na.setAggrType(a.getAggrType());
				na.setChartType(a.getChartType());
				axis.add(na);
			}
		}
		CategoryChartModel model = (CategoryChartModel) getChartModel();
		NumberChartAxis[] array = axis.toArray(new NumberChartAxis[0]);
		if (array.length < 1) {
			throw new RuntimeException(StringResource.getStringResource("miufo00272"));
		}
		model.setDataAxises(array);

	}

	private ChartDesc[] getCombineChartType(){
		ArrayList<ChartDesc> descList = new ArrayList<ChartDesc>();
		for(int parentType : COMBINE_CHART_TYPE){
			descList.addAll(Arrays.asList(ChartConstants.getSubs(parentType)));
		}
		return descList.toArray(new ChartDesc[0]);
	}
	
	/**
	 * 表模型
	 */
	private class ItemTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		public ItemTableModel() {
			super(COLUMN_NAMES, 0);
		}

		public Class<?> getColumnClass(int col) {
			switch (col) {
			case 0:
				return Boolean.class;
			case 2: {
				return DefaultConstEnum.class;
			}
			default: {
				return String.class;
			}
			}
		}
 
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			DataDetialAxis item = getItem(rowIndex);
			if(item == null){
				return false;
			}
			if (columnIndex == 1) {
				return false;
			} else if(columnIndex == 0){
				return true;
			} else if(columnIndex == 2){
				return item.isSelected();
			} else if(columnIndex == 3){
				return item.isSelected() && obItemLabel.isSelected();
			}
			return false;
		}

		public Object getValueAt(final int row, final int column) {
			DataDetialAxis item = getItem(row);
			if (item == null) {
				return null;
			}
			switch (column) {
			case 0:
				return item.isSelected();
			case 1:
				return item.getFld().getCaption();
			case 2:
				int aggr = item.getAggrType();
				for(DefaultConstEnum a: AGGR_TYPES){
					if(a.getValue().equals(aggr)){
						return a;
					}
				}
				return null;//item.getFld().getCaption();
			case 3:
				ChartDesc desc = ChartConstants.getChartDesc(item.getChartType());
				
				return desc == null ? "" : desc.getName();
			default:
				break;
			}
			return null;
		}

		public void setValueAt(final Object aValue, final int row,
				final int column) {
			DataDetialAxis item = getItem(row);

			switch (column) {
			case 0:
				boolean b = (Boolean) aValue;
				item.setSelected(b);
				break;
			case 2:
				DefaultConstEnum a = (DefaultConstEnum) aValue;
				item.setAggrType(new Integer(a.getValue().toString()));
				break;
			case 3:
				ChartDesc desc = (ChartDesc)aValue;
				item.setChartType(desc.getType());
			default:
				break;
			}
			fireTableCellUpdated(row, column);
		}

		private DataDetialAxis getItem(final int row) {
			return dataAxis[row];
		}
	}

	// private void initArrPanel(final CategoryChartModel model, JPanel pnlAggr)
	// {
	// pnlAggr.setBorder(BorderFactory.createTitledBorder(null, "汇总类型",
	// TitledBorder.DEFAULT_JUSTIFICATION,
	// TitledBorder.DEFAULT_POSITION,
	// new Font("Dialog", Font.BOLD, 12), Color.blue));
	// ButtonGroup bg = new ButtonGroup();
	// for (int i = 0; i < AGGR_TYPES.length; i++) {
	// final JRadioButton rd = new JRadioButton();
	// bg.add(rd);
	// pnlAggr.add(rd);
	// rd.setText(AGGR_TYPES[i][0]);
	// if (AGGR_TYPES[i][1].equals(model.getAggrType() + "")) {
	// rd.setSelected(true);
	// }
	// rd.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// if (rd.isSelected()) {
	// String text = rd.getText();
	// for (String[] val : AGGR_TYPES) {
	// if (val[0].equals(text)) {
	// int tt = new Integer(val[1]).intValue();
	// model.setAggrType(tt);
	// }
	// }
	// }
	// }
	// });
	//
	// }
	// }

	/**
	 * @i18n miufo00273=分类轴不能为空。
	 */
	@Override
	void save() {

		CategoryChartModel model = (CategoryChartModel) getChartModel();

		if (model.getCategoryAxis().getFieldId() == null) {
			throw new RuntimeException(StringResource.getStringResource("miufo00273"));
		}

		saveTableData();

		if (model.getCategoryAxis2().getFieldId() == null
				&& model.getDataAxises().length <= 1) {
			model.setLegend(false);
		}

		model.setItemLabelVisible(cbItemLabel.isSelected());
		
		if(!isMultiChart() && obItemLabel.isSelected()){//如果是叠加图，转化模型
			MultipleAxisChartModel multiAxisModel = new MultipleAxisChartModel();
			com.ufida.iufo.pub.tools.Toolkit.copyProperties(model.clone(), multiAxisModel);
			setChartModel(multiAxisModel);
		}
		
		if(isMultiChart() && !obItemLabel.isSelected()){
			CategoryChartModel cateModel = new CategoryChartModel();
			com.ufida.iufo.pub.tools.Toolkit.copyProperties(model.clone(), cateModel);
			setChartModel(cateModel);
		}
	}

	public void actionPerformed(AxisPropEvent e) {
		initTableData();

	}

	class DataDetialAxis extends NumberChartAxis {
		private static final long serialVersionUID = -6256871187354076438L;
		private boolean selected = false;
		private Field fld = null;

		DataDetialAxis(Field fld) {
			this.fld = fld;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setFld(Field fld) {
			this.fld = fld;
		}

		public Field getFld() {
			return fld;
		}

	}
}
  