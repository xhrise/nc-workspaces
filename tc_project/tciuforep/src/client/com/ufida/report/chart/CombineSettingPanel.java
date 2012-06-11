/**
 * 
 */
package com.ufida.report.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.util.NCOptionPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.chart.AbstractCombineChartModel;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropEvent;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropListener;
import com.ufsoft.iufo.fmtplugin.chart.CategoryChartModel;
import com.ufsoft.iufo.fmtplugin.chart.ChartConstants;
import com.ufsoft.iufo.fmtplugin.chart.ChartDesc;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.fmtplugin.chart.CombineChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.chart.IChartModel;

/**
 * @author wangyga
 * @created at 2009-5-27,上午09:24:49
 * 
 */
public class CombineSettingPanel extends ChartSettingPanel implements AxisPropListener{

	private static final long serialVersionUID = 1L;

	private JPanel chartTypePanel = null;

	private JPanel chartDataPanel = null;

	private JList chartTypeList = null;

	private JList detailChartList = null;

	private JPanel chartTypeListPanel = null;

	private JPanel detailChartListPanel = null;

	private JPanel operateBtnPanel = null;

	private JPanel dataTablePanel = null;

	private JButton btnModify = null;

	private JButton btnAdd = null;

	private JButton btnDelete = null;

	private JTable dataTable = null;

	private Vector<CombineChartVo> combineChartVos = new java.util.Vector<CombineChartVo>();;

	private final int[] combineChartType = new int[] {
			ChartConstants.CHART_BAR, ChartConstants.CHART_LINE,
			ChartConstants.CHART_AREA };

	/**
	 * @create by wangyga at 2009-5-27,上午09:26:34
	 * 
	 * @param chartModel
	 */
	public CombineSettingPanel(IChartModel chartModel) {
		super(chartModel);
		try {
			initContentPane();
		} catch (Throwable e) {
			handleException(e);
		}

	}

	private void initContentPane() {
		setBounds(32, 82, 505, 520);
		setLayout(new BorderLayout());
		add(getChartTypePanel(), BorderLayout.NORTH);
		add(getChartDataPanel(), BorderLayout.CENTER);
		
		addListener(this);
		initCombineData();

	}
	
	private void initCombineData() {
		CombineChartModel chartModel = (CombineChartModel)getChartModel();
		ChartModel[] subChartModel = chartModel.getAllChartModel();
		if(subChartModel == null || subChartModel.length ==0){
			ChartTableModel model = (ChartTableModel)getDataTable().getModel();
			if(model.getRowCount() != 0){
				model.removeAll();
			}
			return;
		}
		ArrayList<CombineChartVo> chartVoList = new ArrayList<CombineChartVo>(); 
		for(ChartModel model : subChartModel){
			int iChartType = model.getType();
			ChartDesc chartDesc = ChartConstants.getChartDesc(iChartType);
			CombineChartVo chartVo = new CombineChartVo(chartDesc.getName(),iChartType);
			chartVoList.add(chartVo);
		}
		combineChartVos.addAll(chartVoList);
	}
	
	/**
	 * @i18n iufobi00061=请先选择数据集...
	 * @i18n iufobi00062=没有添加复合图表类型或者复合图表类型至少是两个
	 */
	@Override
	void save() {
		AbstractCombineChartModel combineChartModel = (AbstractCombineChartModel)getChartModel();
		String strDataSetPk = combineChartModel.getDataSetDefPK();
		if(strDataSetPk == null || strDataSetPk.trim().length() ==0){
			throw new RuntimeException(StringResource.getStringResource("iufobi00061"));
		}
		if(combineChartModel.getModelCount() < 2){
			throw new RuntimeException(StringResource.getStringResource("iufobi00062"));
		}
		
		for(ChartModel model : combineChartModel.getAllChartModel()){
			CategoryChartModel subModel = (CategoryChartModel) model;
			ChartDesc desc = ChartConstants.getChartDesc(subModel.getType());
			if (subModel.getCategoryAxis().getFieldId() == null) {
				throw new RuntimeException(desc.getName()+":"+StringResource.getStringResource("miufo00273"));
			}
			
			if (subModel.getDataAxises().length < 1) {
				throw new RuntimeException(desc.getName()+":"+StringResource.getStringResource("miufo00272"));
			}
		}
	}

	private JPanel getChartTypePanel() {
		if (chartTypePanel == null) {
			chartTypePanel = new UIPanel();
			chartTypePanel.setPreferredSize(new Dimension(505, 170));
			chartTypePanel.setLayout(new BorderLayout(30, 2));
			chartTypePanel.add(getChartTypeListPanel(), BorderLayout.WEST);
			chartTypePanel.add(getDetailChartListPanel(), BorderLayout.CENTER);
		}
		return chartTypePanel;
	}

	/**
	 * @i18n iufobi00063=复合图表
	 */
	private JPanel getChartDataPanel() {
		if (chartDataPanel == null) {
			chartDataPanel = new UIPanel();
			chartDataPanel.setLayout(new BorderLayout());
			chartDataPanel.setBorder(BorderFactory.createTitledBorder(null,
					StringResource.getStringResource("iufobi00063"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			chartDataPanel.add(getDataTablePanel(), BorderLayout.CENTER);
			chartDataPanel.add(getOperateBtnPanel(), BorderLayout.EAST);
		}
		return chartDataPanel;
	}

	/**
	 * @i18n miufo1000602=图表类型
	 */
	private JPanel getChartTypeListPanel() {
		if (chartTypeListPanel == null) {
			chartTypeListPanel = new UIPanel();
			chartTypeListPanel.setLayout(new BorderLayout());
			chartTypeListPanel.setBorder(BorderFactory.createTitledBorder(null,
					StringResource.getStringResource("miufo1000602"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			chartTypeListPanel.add(new JScrollPane(getChartTypeList()),
					BorderLayout.CENTER);
		}
		return chartTypeListPanel;
	}

	/**
	 * @i18n miufo1000623=子图表类型
	 */
	private JPanel getDetailChartListPanel() {
		if (detailChartListPanel == null) {
			detailChartListPanel = new UIPanel();
			detailChartListPanel.setLayout(new BorderLayout());
			detailChartListPanel.setBorder(BorderFactory.createTitledBorder(
					null, StringResource.getStringResource("miufo1000623"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			detailChartListPanel.add(new JScrollPane(getDetailChartTypeList()),
					BorderLayout.CENTER);

		}
		return detailChartListPanel;
	}

	private JPanel getDataTablePanel() {
		if (dataTablePanel == null) {
			dataTablePanel = new UIPanel();
			dataTablePanel.setLayout(new BorderLayout());
			dataTablePanel.add(new JScrollPane(getDataTable()),
					BorderLayout.CENTER);
		}
		return dataTablePanel;
	}

	private JPanel getOperateBtnPanel() {
		if (operateBtnPanel == null) {
			operateBtnPanel = new UIPanel();
			operateBtnPanel.setLayout(new BoxLayout(operateBtnPanel,
					BoxLayout.Y_AXIS));
			operateBtnPanel.add(Box.createRigidArea(new Dimension(20, 8)));
			operateBtnPanel.add(getBtnAdd());
			operateBtnPanel.add(Box.createRigidArea(new Dimension(20, 6)));
			operateBtnPanel.add(getBtnModify());
			operateBtnPanel.add(Box.createRigidArea(new Dimension(20, 6)));
			operateBtnPanel.add(getBtnDelete());

		}
		return operateBtnPanel;
	}

	private JList getChartTypeList() {
		if (chartTypeList == null) {

			ArrayList<ChartDesc> descList = new ArrayList<ChartDesc>();
			for (int type : combineChartType) {
				descList.add(ChartConstants.getChartDesc(type));
			}
			chartTypeList = new JList(descList.toArray(new ChartDesc[0]));
			chartTypeList.setPreferredSize(new Dimension(150, 275));
			chartTypeList.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			
			chartTypeList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					ChartDesc cd = (ChartDesc) chartTypeList.getSelectedValue();
					ChartDesc[] subs = ChartConstants.getSubs(cd.getType());
					DefaultListModel md = (DefaultListModel) detailChartList
							.getModel();
					md.removeAllElements();
					for (ChartDesc c : subs) {
						md.addElement(c);
					}
					if(md.getSize() > 0){
						detailChartList.setSelectedIndex(0);
					}
				}

			});
			
		}
		return chartTypeList;
	}

	private JList getDetailChartTypeList() {
		if (detailChartList == null) {
			detailChartList = new JList();
			detailChartList.setModel(new DefaultListModel());
			detailChartList.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			detailChartList.setCellRenderer(new ChartItemRenderer());
			if(chartTypeList.getModel().getSize() > 0){
				chartTypeList.setSelectedIndex(0);
			}
		}
		return detailChartList;
	}

	private JTable getDataTable() {
		if (dataTable == null) {
			dataTable = new UITable();
			dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			dataTable.setModel(new ChartTableModel());
			dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			dataTable.setAutoCreateColumnsFromModel(false);
			dataTable.getColumnModel().getColumn(0).setPreferredWidth(80);
			dataTable.getColumnModel().getColumn(1).setPreferredWidth(320);
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			dataTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
			dataTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		}
		return dataTable;
	}

	/**
	 * @i18n miufo1001141=添加
	 */
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new UIButton();
			btnAdd.setSize(new Dimension(75, 20));
			btnAdd.setPreferredSize(new Dimension(75, 20));
			btnAdd.setText(StringResource.getStringResource("miufo1000950"));
			btnAdd.addActionListener(new ActionListener() {

				/**
				 * @i18n iufobi00061=请先选择数据集...
				 */
				public void actionPerformed(ActionEvent e) {
					
					String datasetPk = ((ChartModel)getChartModel()).getDataSetDefPK();
					if(datasetPk == null || datasetPk.trim().length() ==0){
						 NCOptionPane.showMessageDialog(CombineSettingPanel.this, StringResource.getStringResource("iufobi00061"), MultiLang.getString("miufo1000925"),NCOptionPane.WARNING_MESSAGE);
						 return;
					}
					
					if (getItemVo() == null) {
						return;
					}

					ChartModel newModel = createChartModel();
					
					CombineChartParamDlg paramDlg = new CombineChartParamDlg(CombineSettingPanel.this,newModel);
					paramDlg.showModal();
					
					if(paramDlg.getResult() == UfoDialog.ID_CANCEL){
						return;
					}
					
					((ChartTableModel) getDataTable().getModel())
					.addRow(getItemVo());
					
					((CombineChartModel)getChartModel()).addChartModel(newModel);
				}

				

			});
		}
		return btnAdd;
	}

	private CombineChartVo getItemVo() {
		ChartDesc chartDesc = (ChartDesc) getDetailChartTypeList()
				.getSelectedValue();
		if (chartDesc == null) {
			return null;
		}
		CombineChartVo vo = new CombineChartVo(chartDesc.getName(),
				chartDesc.getType());

		return vo;
	}
	
	private ChartModel createChartModel() {
		ChartDesc chartDesc = (ChartDesc) getDetailChartTypeList()
				.getSelectedValue();
		if (chartDesc == null) {
			return null;
		}

		ChartModel newModel = null;
		Class<? extends ChartModel> newClz = chartDesc.getModelClz();
		if (newClz == null) {
			newClz = CategoryChartModel.class;
		}

		if (newModel == null) {
			try {
				newModel = newClz.newInstance();
			} catch (Throwable ee) {
				throw new RuntimeException(ee.getMessage());
			}
		}
		newModel.setType(getItemVo().getChartType());
		newModel.setTitle(getItemVo().getChartName());

		newModel.setDataSetDefPK(((ChartModel) getChartModel())
				.getDataSetDefPK());
		return newModel;
	}
	
	/**
	 * @i18n miufo1001396=修改
	 */
	private JButton getBtnModify() {
		if (btnModify == null) {
			btnModify = new UIButton();
			btnModify.setText(StringResource.getStringResource("miufo1001396"));
			getDataTable().getModel().addTableModelListener(
					new TableModelListener() {
						public void tableChanged(TableModelEvent e) {
							if (e.getType() == TableModelEvent.UPDATE) {
								int selectIndex = getDataTable()
										.getSelectedRow();
								CombineChartVo vo = ((ChartTableModel) getDataTable()
										.getModel()).getRow(selectIndex);
								if(vo == null){
									return ;
								}
								if (vo.isSameX() && vo.isSameY()) {
									btnModify.setEnabled(false);
								} else {
									btnModify.setEnabled(true);
								}
							}
						}
					});
			btnModify.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					int selectIndex = getDataTable().getSelectedRow();
					ChartModel model = ((CombineChartModel)getChartModel()).getChartModel(selectIndex);
					CombineChartParamDlg paramDlg = new CombineChartParamDlg(CombineSettingPanel.this,model);
					paramDlg.showModal();
					
				}
				
			});
		}
		return btnModify;
	}

	/**
	 * @i18n ubichart00006=删除
	 */
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new UIButton();
			btnDelete.setSize(new Dimension(75, 20));
			btnDelete.setPreferredSize(new Dimension(75, 20));
			btnDelete.setText(StringResource.getStringResource("ubichart00006"));
			btnDelete.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int selectIndex = getDataTable().getSelectedRow();
					if (selectIndex < 0) {
						return;
					}
					((ChartTableModel) getDataTable().getModel())
							.removeRow(selectIndex);
					((CombineChartModel)getChartModel()).removeChartModel(selectIndex);
				}

			});
		}
		return btnDelete;
	}

	private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);
	}

	/**
	 * @i18n miufopublic359=序号
	 * @i18n miufo1000602=图表类型
	 */
	private static final String[] COLUMN_NAMES = new String[] { StringResource.getStringResource("miufopublic359"), StringResource.getStringResource("miufo1000602"),
			};

	private class ChartTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		public int getRowCount() {
			if (combineChartVos == null) {
				return 0;
			}
			return combineChartVos.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			CombineChartVo vo = getRow(rowIndex);;
			if(vo == null){
				return null;
			}
			
			switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return vo.getChartName();
			
			default:
				break;
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			CombineChartVo vo = getRow(row);;
			if(vo == null){
				return ;
			}
			switch (column) {
			case 0:
			case 1:
				vo.setChartName("");
				break;
			default:
				break;
			}
			fireTableCellUpdated(row, column);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return Integer.class;
			case 1: {
				return String.class;
			}
			default: {
				return String.class;
			}
			}
		}

		public void addRow(CombineChartVo vo) {
			if(vo == null){
				return;
			}
			combineChartVos.add(vo);
			int nLastRow = combineChartVos.size() - 1;
			fireTableRowsInserted(nLastRow, nLastRow);
		}

		public void removeRow(int rowIndex) {
			if (rowIndex < 0 || rowIndex > combineChartVos.size() - 1) {
				return;
			}
			combineChartVos.remove(rowIndex);
			int nLastRow = combineChartVos.size();
			fireTableRowsInserted(nLastRow, nLastRow);
		}

		public void removeAll(){
			combineChartVos.removeAllElements();
			fireTableDataChanged();
		}
		
		@Override
		public String getColumnName(int column) {
			if(column < 0){
				throw new IllegalArgumentException("column < 0");
			}
			return COLUMN_NAMES[column];
		}

		private CombineChartVo getRow(final int row) {
			if (row < 0 || row > combineChartVos.size() -1 ) {
				return null;
			}
			return combineChartVos.get(row);
		}

	}

	private class CombineChartVo {

		private String strChartName = null;

		private int iChartType = 0;

		private boolean isSameX = true;

		private boolean isSameY = true;

		String getChartName() {
			return strChartName;
		}

		public CombineChartVo(String strName, int iType) {
			strChartName = strName;
			iChartType = iType;
		}

		void setChartName(String strChartName) {
			this.strChartName = strChartName;
		}

		int getChartType() {
			return iChartType;
		}

		void setChartType(int chartType) {
			iChartType = chartType;
		}

		boolean isSameX() {
			return isSameX;
		}

		void setSameX(boolean isSameX) {
			this.isSameX = isSameX;
		}

		boolean isSameY() {
			return isSameY;
		}

		void setSameY(boolean isSameY) {
			this.isSameY = isSameY;
		}

	}

	public void actionPerformed(AxisPropEvent e) {
		CombineChartModel chartModel = (CombineChartModel)getChartModel();
		chartModel.removeAllModel();
		initCombineData();
	}

}
 