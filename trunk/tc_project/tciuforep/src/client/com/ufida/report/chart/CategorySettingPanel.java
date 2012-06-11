package com.ufida.report.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

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
import com.ufsoft.iufo.fmtplugin.chart.NumberChartAxis;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.chart.IChartModel;

public class CategorySettingPanel extends ChartSettingPanel implements
		AxisPropListener {
	private static final long serialVersionUID = 2098981543367246928L;

	private JCheckBox cbItemLabel = new JCheckBox();

	private ItemTableModel tableModel = new ItemTableModel();
	private JTable table = new UITable(tableModel);

	private DataDetialAxis[] dataAxis = null;


	/**
	 * @i18n miufopublic307=合计
	 * @i18n miufo1001255=计数
	 * @i18n miufo1001258=平均
	 * @i18n miufo5308000005=最大
	 * @i18n miufo1001257=最小
	 */
	private final static DefaultConstEnum[] AGGR_TYPES = new DefaultConstEnum[] {
			new DefaultConstEnum(AggrItem.TYPE_SUM, StringResource.getStringResource("miufopublic307")),
			new DefaultConstEnum(AggrItem.TYPE_COUNT, StringResource.getStringResource("miufo1001255")),
			new DefaultConstEnum(AggrItem.TYPE_AVAGE, StringResource.getStringResource("miufo1001258")),
			new DefaultConstEnum(AggrItem.TYPE_MAX, StringResource.getStringResource

("miufo5308000005")),
			new DefaultConstEnum(AggrItem.TYPE_MIN, StringResource.getStringResource("miufo1001257")),

	};

	/**
	 * @i18n uibichart00051=分类轴
	 * @i18n miufo00266=次分类轴
	 */
	public CategorySettingPanel(IChartModel chartModel) {

		super(chartModel);
		this.addListener(this);

		setLayout(null);
		final CategoryChartModel model = (CategoryChartModel) chartModel;

		Field[] flds = getFieldsInfo();
		final int height = 58;
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("uibichart00051"),model.getCategoryAxis(), flds);
		ap.setBounds(0, 0, 505, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00266"),
				model.getCategoryAxis2(),
				flds);
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
	 */
	private void initOptionPnl(final CategoryChartModel model, JPanel pnlOption) {
		pnlOption.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource
        ("miufo00267"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		cbItemLabel.setText(StringResource.getStringResource("miufo00268"));
		cbItemLabel.setBounds(39, 42, 118, 26);
		pnlOption.add(cbItemLabel);
		cbItemLabel.setSelected(model.isItemLabelVisible());

	}

	/**
	 * @i18n miufo00269=数据轴
	 */
	private void initDataPanel(final CategoryChartModel model,
			JScrollPane pnlDataAxis) {
		pnlDataAxis.setBorder(BorderFactory.createTitledBorder(null, StringResource.getStringResource
        ("miufo00269"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		pnlDataAxis.setColumnHeaderView(table.getTableHeader());
		pnlDataAxis.setViewportView(table);
		TableColumn column = table.getColumn(COLUMN_NAMES[0]);
		column.setWidth(50);

		column = table.getColumn(COLUMN_NAMES[2]);
		column.setCellEditor(new DefaultCellEditor(new UIComboBox(AGGR_TYPES)));

		initTableData();

		table.updateUI();

	}

	/**
	 * @i18n miufoinputnew00066=显示
	 * @i18n miufo00270=数据列
	 * @i18n miufo00271=汇总类型
	 */
	private final static String[] COLUMN_NAMES = { StringResource.getStringResource("miufoinputnew00066"), StringResource.getStringResource("miufo00270"), StringResource.getStringResource("miufo00271") };

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
				for (NumberChartAxis a : existDataAxis) {
					if (a.getFieldId().equals(f.getFldname())) {
						da.setSelected(true);
						da.setAggrType(a.getAggrType());
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

	/**
	 * 表模型
	 */
	private class ItemTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		public ItemTableModel() {
			super(COLUMN_NAMES, 0);
		}

		public Class getColumnClass(int col) {
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
			if (columnIndex == 0 || columnIndex == 2) {
				return true;
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



			default:
				break;
			}
			fireTableCellUpdated(row, column);
		}

		private DataDetialAxis getItem(final int row) {
			return dataAxis[row];
		}
	}

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