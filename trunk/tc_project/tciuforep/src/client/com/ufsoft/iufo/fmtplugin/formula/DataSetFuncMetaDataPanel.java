package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.dsmanager.MetaDataTableModel;
import nc.ui.pub.dsmanager.SelectMetaDataDlg;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.metadata.Field;
import com.ufida.dataset.metadata.MetaData;
import com.ufsoft.iufo.resource.StringResource;

/*
 * 查询字段定义面板.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncMetaDataPanel extends BasicWizardStepPanel {
	private static final long serialVersionUID = 1L;
	private UIButton ivjbtnAdd = null;

	private UIButton ivjbtnRemove = null;

	private UIButton ivjbtnMoveUp = null;

	private UIButton ivjbtnMoveDown = null;

	private UIPanel ivjPnEast = null;

	private UITablePane ivjTablePn = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;
	
	private DataSetFuncDesignWizardListPn m_dswlp = null;
	
	//当前选择数据集(数据集切换时需更新该标识)
	private String strSelDataSet = null;
	protected MetaData allFields;
	private MetaData selectedFields;
	
	/**
	 * 返回 btnAdd 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * @i18n miufo00615=新增
	 */
	private UIButton getbtnAdd() {
		if (ivjbtnAdd == null) {
			try {
				ivjbtnAdd = new UIButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setText(StringResource.getStringResource("miufo00615"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private nc.ui.pub.beans.UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(10, 5));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private nc.ui.pub.beans.UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new nc.ui.pub.beans.UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new Dimension(10, 5));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * 返回 btnMoveUp 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1001298=上移
	 */
	private UIButton getbtnMoveUp() {
		if (ivjbtnMoveUp == null) {
			try {
				ivjbtnMoveUp = new UIButton();
				ivjbtnMoveUp.setName("btnMoveUp");
				ivjbtnMoveUp.setText(StringResource.getStringResource("miufo1001298"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnMoveUp;
	}

	/**
	 * 返回 btnMoveDown 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1001290=下移
	 */
	private UIButton getbtnMoveDown() {
		if (ivjbtnMoveDown == null) {
			try {
				ivjbtnMoveDown = new UIButton();
				ivjbtnMoveDown.setName("btnMoveDown");
				ivjbtnMoveDown.setText(StringResource.getStringResource("miufo1001290"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnMoveDown;
	}

	/**
	 * 返回 btnRemove 特性值。
	 * 
	 * @return UIButton
	 * @i18n ubichart00006=删除
	 */
	private UIButton getbtnRemove() {
		if (ivjbtnRemove == null) {
			try {
				ivjbtnRemove = new UIButton();
				ivjbtnRemove.setName("btnRemove");
				ivjbtnRemove.setText(StringResource.getStringResource("ubichart00006"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnRemove;
	}

	/**
	 * 返回 PnEast 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getPnEast() {
		if (ivjPnEast == null) {
			try {
				ivjPnEast = new UIPanel();
				ivjPnEast.setName("PnEast");
				ivjPnEast.setPreferredSize(new Dimension(160, 0));
				ivjPnEast.setLayout(new GridBagLayout());

				GridBagConstraints constraintsbtnAdd = new GridBagConstraints();
				constraintsbtnAdd.gridx = 1;
				constraintsbtnAdd.gridy = 1;
				constraintsbtnAdd.ipadx = 50;
				constraintsbtnAdd.insets = new Insets(52, 20, 26, 20);
				getPnEast().add(getbtnAdd(), constraintsbtnAdd);

				GridBagConstraints constraintsbtnRemove = new GridBagConstraints();
				constraintsbtnRemove.gridx = 1;
				constraintsbtnRemove.gridy = 2;
				constraintsbtnRemove.ipadx = 50;
				constraintsbtnRemove.insets = new Insets(26, 20, 26, 20);
				getPnEast().add(getbtnRemove(), constraintsbtnRemove);

				GridBagConstraints constraintsbtnMoveUp = new GridBagConstraints();
				constraintsbtnMoveUp.gridx = 1;
				constraintsbtnMoveUp.gridy = 3;
				constraintsbtnMoveUp.ipadx = 50;
				constraintsbtnMoveUp.insets = new Insets(26, 20, 26, 20);
				getPnEast().add(getbtnMoveUp(), constraintsbtnMoveUp);

				GridBagConstraints constraintsbtnMoveDown = new GridBagConstraints();
				constraintsbtnMoveDown.gridx = 1;
				constraintsbtnMoveDown.gridy = 4;
				constraintsbtnMoveDown.ipadx = 50;
				constraintsbtnMoveDown.insets = new Insets(26, 20, 26, 20);
				getPnEast().add(getbtnMoveDown(), constraintsbtnMoveDown);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	private void initialize() {
		try {
			setName("MetaDataPanel");
			setLayout(new BorderLayout());
			setSize(720, 480);
			add(getPnEast(), "East");
			add(getTablePn(), "Center");
			add(getPnNorth(), "North");
			add(getPnSouth(), "South");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}

	/**
	 * 获得表格
	 */
	public UITable getTable() {
		return getTablePn().getTable();
	}

	public MetaDataTableModel getTableModel() {
		return (MetaDataTableModel) getTable().getModel();
	}

	/**
	 * DataSetFuncMetaDataPanel 工具化构造子注解
	 */
	public DataSetFuncMetaDataPanel(WizardShareObject wso, AbstractWizardListPanel awlp) {
		super(wso);
		m_dswlp = (DataSetFuncDesignWizardListPn)awlp;
		initialize();
		initTable();
		initEvents();
	}

	public void stopTableEdit() {
		if (getTable().getCellEditor() != null) {
			getTable().getCellEditor().stopCellEditing();
		}
	}

	public boolean canFinish() {
		return false;
	}
	
	public boolean completeStep() {
		stopTableEdit();
		if (selectedFields != null) {
			selectedFields.clear();
			selectedFields.addField(getTableModel().getFields());
		}
		return true;
	}

	/**
	 * @i18n miufo01052=请选择数据集函数查询字段
	 */
	@Override
	public String check() {
		DataSetFuncDesignObject objDataSetSharedObject = (DataSetFuncDesignObject)getWizardShareObject();
		if(objDataSetSharedObject != null && 
				objDataSetSharedObject.getCurDataSetDef().getDataSetDef().getMetaData().getFieldNum() == 0){
			return StringResource.getStringResource("miufo01052");
		}
		return null;
	}

	/**
	 * @i18n mbiadhoc00003=字段
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("mbiadhoc00003");
	}

	public void initStep() {
		// 根据对象填充界面
		DataSetDefVO vo = getSharedObject().getCurDataSetDef();
		load(vo);
	}

	public void initEvents() {
		getbtnAdd().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				addField();
			}
		});

		getbtnMoveUp().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				MetaDataTableModel tm = (MetaDataTableModel) getTable()
						.getModel();
				int[] rows = getTable().getSelectedRows();
				// 如果第一个选中行不在第一行
				if (rows.length > 0 && rows[0] > 0) {
					getTable().getSelectionModel().clearSelection();
					for (int rowid : rows) {
						tm.moveField(rowid, rowid - 1);
						getTable().getSelectionModel().addSelectionInterval(
								rowid - 1, rowid - 1);
					}
					clearFieldSharedObject();
					addFieldsSharedObject(tm.getFields());
				}

				getTable().repaint();
			}
		});

		getbtnMoveDown().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				MetaDataTableModel tm = (MetaDataTableModel) getTable()
						.getModel();
				int[] rows = getTable().getSelectedRows();
				// 如果最后一个选中行不在最后一行
				if (rows.length > 0
						&& rows[rows.length - 1] < getTable().getRowCount() - 1) {
					getTable().getSelectionModel().clearSelection();
					for (int rowid : rows) {
						tm.moveField(rowid, rowid + 1);
						getTable().getSelectionModel().addSelectionInterval(
								rowid + 1, rowid + 1);
					}
					clearFieldSharedObject();
					addFieldsSharedObject(tm.getFields());
				}
				getTable().repaint();
			}
		});

		getbtnRemove().addActionListener(new ActionListener() {
			/**
			 * @i18n miufo1000775=确认
			 * @i18n miufo01053=你确认要删除所选的字段信息吗？
			 */
			public void actionPerformed(final ActionEvent arg0) {
				if (getTable().getSelectedRow() < 0) {
					return;
				}
				if (MessageDialog.showYesNoDlg(getParent(), StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo01053")) == MessageDialog.ID_YES) {
					MetaDataTableModel tm = (MetaDataTableModel) getTable()
							.getModel();
					int[] rows = getTable().getSelectedRows();
					for (int i = rows.length - 1; i >= 0; i--) {
						String fieldName = (String) tm.getValueAt(rows[i],
								MetaDataTableModel.COLUMN_FIELD_NAME);
						DataSetFuncMetaDataPanel.this.selectedFields.dropField(fieldName);
						DataSetFuncMetaDataPanel.this.dropFieldSharedObject(fieldName);
						tm.removeRow(rows[i]);
					}
				}
			}
		});
	}

	public void loadMetaData(MetaData selectedFields, MetaData allFields) {
		this.selectedFields = selectedFields;
		this.allFields = allFields;
		MetaDataTableModel mtd = new DataSetMetaDataTableModel(selectedFields
				.getFields());
		getTable().setModel(mtd);
	}

	private void addField() {
		SelectMetaDataDlg dlg = new SelectMetaDataDlg(this,
				this.selectedFields, this.allFields);
		if (dlg.showModal() == UIDialog.ID_OK) {
			this.selectedFields.addField(dlg.getSelectedFields());
			this.addFieldsSharedObject(dlg.getSelectedFields());
			MetaDataTableModel mtd = (MetaDataTableModel) getTable().getModel();
			mtd.addFields(dlg.getSelectedFields());
		}
	}

	public void load(DataSetDefVO datasetVo) {
		if ((datasetVo != null) && (datasetVo.getPk_datasetdef() != null)) {
			if(strSelDataSet == null || !strSelDataSet.equals(datasetVo.getPk_datasetdef())){
				datasetVo.getDataSetDef().getMetaData().clear();
				strSelDataSet = datasetVo.getPk_datasetdef();
			}
			DataSet bds = m_dswlp.getDataSet(datasetVo.getPk_datasetdef());
			MetaData allFields = bds.getMetaData();
			checkFields(allFields);
			if ((datasetVo == null) || (datasetVo.getDataSetDef() == null)
					|| (datasetVo.getDataSetDef().getMetaData() == null)) {
				return;
			}
			loadMetaData(datasetVo.getDataSetDef().getMetaData(), allFields);
		}
	}

	/**
	 * 过滤掉不显示的字段
	 * @param allFields
	 */
	private void checkFields(MetaData allFields){
		if(allFields == null)
			return;
		Field[] allDataField = allFields.getFields();
		if(allDataField == null || allDataField.length ==0)
			return;
		for(Field field : allDataField){
			if(!field.isEdit()){
				allFields.dropField(field.getFldname());
			}
		}
	}
	
	private void initTable() {
		UITable table = getTable();
		// 设置表属性
		table.getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		table.getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);

		MetaDataTableModel mtd = new MetaDataTableModel(null);
		table.setModel(mtd);
	}

	private class DataSetMetaDataTableModel extends MetaDataTableModel{
		private static final long serialVersionUID = 1L;

		public DataSetMetaDataTableModel(Field[] fields){
			super(fields);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	public void clearFieldSharedObject(){
		getSharedObject().getCurDataSetDef().getDataSetDef().getMetaData().clear();
	}
	
	public void addFieldSharedObject(Field field){
		getSharedObject().getCurDataSetDef().getDataSetDef().getMetaData().addField(field);
	}
	
	public void addFieldsSharedObject(Field[] fields){
		getSharedObject().getCurDataSetDef().getDataSetDef().getMetaData().addField(fields);
	}
	
	public void dropFieldSharedObject(String fieldName){
		getSharedObject().getCurDataSetDef().getDataSetDef().getMetaData().dropField(fieldName);
	}

	public DataSetFuncDesignObject getSharedObject() {
		return (DataSetFuncDesignObject) getWizardShareObject();
	}
	
	public String getCurSelDataSetPK() {
		return strSelDataSet;
	}

	public void setCurSelDataSetPK(String selDataSetPK) {
		strSelDataSet = selDataSetPK;
	}
	
}
