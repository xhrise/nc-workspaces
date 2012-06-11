package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.editor.NumberDocument;
import nc.vo.bi.base.util.StringUtil;
import nc.vo.pub.querymodel.QueryConst;

import org.jfree.ui.IntegerDocument;

import com.ufida.dataset.Condition;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.IContext;
import com.ufida.dataset.Parameter;
import com.ufida.dataset.descriptor.FilterItem;
import com.ufida.dataset.descriptor.SortDescriptor;
import com.ufida.dataset.descriptor.SortItem;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.fmtplugin.formula.FmlRefTextField;
import com.ufsoft.script.extfunc.LimitRowNumber;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.CellsPane;

/*
 * 数据集函数行条件过滤定义面板.
 * Creation date: (2008-07-07 15:39:08)
 * @author: chxw
 */
public class RowCordFilterPanel extends UIPanel{
	private static final long serialVersionUID = 1L;
	/**字符类型字段比较操作符,第一个符号为默认符号*/    
    public static final String[] STR_FILTER_OPERATION = {"=", "<>", "LIKE"};
    /**数值类型字段比较操作符,第一个符号为默认符号*/    
    public static final String[] NUM_FILTER_OPERATION = {"=", ">", ">=", "<", "<=",  "<>", "in"};
    
	/**
	 * @i18n mbiadhoc00003=字段
	 * @i18n miufo00612=值类型
	 */
	private final static String[] COLUMN_NAMES = {
		StringResource.getStringResource("uibimultical005"),// 0
		StringResource.getStringResource("mbiadhoc00003"), StringResource.getStringResource("uibimultical002"),// 2
		StringResource.getStringResource("miufo00612"), StringResource.getStringResource("miufo1000751"),// 4
	};

	private final static DefaultConstEnum[] VALUE_TYPES = new DefaultConstEnum[] {
		new DefaultConstEnum(FilterItem.TYPE_CONST, FilterItem
				.getValueTypeCaption(FilterItem.TYPE_CONST)),
		new DefaultConstEnum(FilterItem.TYPE_EXP, FilterItem
				.getValueTypeCaption(FilterItem.TYPE_EXP))};

	private UfoFmlExecutor rowFilterExprFmlExecutor = null;
	
	private UIPanel ivjPnEast = null;
	
	private UIPanel ivjPnNorth = null;
	
	private ParamCordFilterPanel ivjPnSouth = null;
	
	private UIButton ivjbtnAdd = null;
	
	private UIButton ivjbtnDel = null;

	private UITablePane ivjTablePn = null;
	
	private Vector<FilterItem> fieldFilterItems = new Vector<FilterItem>();
	
	private DataSet m_dataSet = null;
	
	private DataSetFuncDesignObject m_funcDesignObject = null;
	
    private IContext contextVo = null;
	
	private CellsPane cellsPane = null;
	
	public RowCordFilterPanel(IContext context,CellsPane cellsPane) {
		this.cellsPane = cellsPane;
		this.contextVo = context;
		initialize();
		initModel();
		initEvent();
	}
	
	private void initEvent() {
		getbtnAdd().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				stopTableEdit();
				((FilterTableModel)getTM()).addRow(new FilterItem());
			}

		});

		getbtnDel().addActionListener(new ActionListener() {
			/**
			 * @i18n miufo00613=删除确认
			 * @i18n miufo00614=确定删除该筛选条件吗？
			 */
			public void actionPerformed(final ActionEvent e) {
				int row = getTable().getSelectedRow();
				if (row < 0 || row >= fieldFilterItems.size()) {
					return;
				}
				if (MessageDialog.showOkCancelDlg(getTable(), StringResource.getStringResource("miufo00613"), StringResource.getStringResource("miufo00614"), JOptionPane.YES_NO_OPTION) == 0) {
					return;
				}
				((FilterTableModel)getTM()).reMoveRow(row);
				
				int selectCount = getTable().getRowCount() -1;
				if(selectCount >= 0)
					getTable().getSelectionModel().setSelectionInterval(selectCount, selectCount);
					
			}
		});
	}

	private void initModel() {
		UITable table = getTable();
		table.setModel(new FilterTableModel());
		// 设置表属性
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		table.getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		table.getColumn(COLUMN_NAMES[0]).setCellEditor(
				new DefaultCellEditor(new UIComboBox(new String[]{"AND"})));
		table.getColumn(COLUMN_NAMES[1]).setCellEditor(
				new FieldValueEditor());
		table.getColumn(COLUMN_NAMES[2]).setCellEditor(
				new DefaultCellEditor(new UIComboBox(NUM_FILTER_OPERATION)));		
		table.getColumn(COLUMN_NAMES[3]).setCellEditor(
				new DefaultCellEditor(new UIComboBox(VALUE_TYPES)));
		table.getColumn(COLUMN_NAMES[4]).setCellEditor(new FilterValueEditor());
	}
	
	/**
	 * 初始化类
	 */
	private void initialize() {
		try {
			setName("RowCordFilterPanel");
			setLayout(new BorderLayout());
			setSize(560, 350);
			add(getPnNorth(), BorderLayout.NORTH);
			add(getPnEast(), BorderLayout.EAST);
			add(getTablePn(), BorderLayout.CENTER);
			add(getPnSouth(), "South");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * 返回 btnAdd 特性值。
	 * 
	 * @return UIButton
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
	 * @return UIPanel
	 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new LimitRowPanel();
				ivjPnNorth.setName("PnNorth");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return UIPanel
	 */
	private ParamCordFilterPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new ParamCordFilterPanel(cellsPane);
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new Dimension(100, 100));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * 返回 btnDel 特性值。
	 * 
	 * @return UIButton
	 * @i18n ubichart00006=删除
	 */
	private UIButton getbtnDel() {
		if (ivjbtnDel == null) {
			try {
				ivjbtnDel = new UIButton();
				ivjbtnDel.setName("btnDel");
				ivjbtnDel.setText(StringResource.getStringResource("ubichart00006"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnDel;
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
				constraintsbtnAdd.insets = new Insets(98, 20, 49, 20);
				getPnEast().add(getbtnAdd(), constraintsbtnAdd);

				GridBagConstraints constraintsbtnDel = new GridBagConstraints();
				constraintsbtnDel.gridx = 1;
				constraintsbtnDel.gridy = 2;
				constraintsbtnDel.ipadx = 50;
				constraintsbtnDel.insets = new Insets(49, 20, 100, 20);
				getPnEast().add(getbtnDel(), constraintsbtnDel);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}

	/**
	 * 获得表格
	 */
	public UITable getTable() {
		return getTablePn().getTable();
	}

	/**
	 * 获得表格模型
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
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

	public void stopTableEdit() {
		if (getTable().getCellEditor() != null) {
			getTable().getCellEditor().stopCellEditing();
		}
		if(ivjPnSouth != null && ivjPnSouth.getTable().getCellEditor() != null){
			ivjPnSouth.getTable().getCellEditor().stopCellEditing();
		}
	}

	public DataSet getDataSet(){
		return m_dataSet;
	}
	
	public void setDataSet(DataSet dataSet) {
		this.m_dataSet = dataSet;
		initData();
	}
	
	private void initData(){
		//设置限制行列条件初始值
		if(m_funcDesignObject.getLimitRowNumber() != null){
			LimitRowPanel pnlLimitRow = (LimitRowPanel)getPnNorth();
			pnlLimitRow.setLimitRow(m_funcDesignObject.getLimitRowNumber());
		}
		//设置行条件初始值
		Hashtable<String, Condition[]> rowCondFilters = m_funcDesignObject.getRowConds();
		((FilterTableModel)getTable().getModel()).refreshData(rowCondFilters);
		Hashtable<String, String> paramCondFilters = m_funcDesignObject.getParamConds();
		getPnSouth().setParamCondFilters(paramCondFilters);
//		ivjPnSouth.setDataset(getDataSet(), paramCondFilters);
		ivjPnSouth.setDataSet(getDataSet());
	}
	
	public DataSetFuncDesignObject getSharedObject() {
		return m_funcDesignObject;
	}
	
	public void setSharedObject(DataSetFuncDesignObject funcDesignObject) {
		m_funcDesignObject = funcDesignObject;
	}
	
	/**
	 * 表模型
	 */
	private class FilterTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public FilterTableModel() {
			super(COLUMN_NAMES, 0);
		}

		public void refreshData(Hashtable<String, Condition[]> rowCondFilters) {
			fieldFilterItems.removeAllElements();
			if(rowCondFilters == null || rowCondFilters.size() == 0){
				return;
			}
			
			int i =0;
			Enumeration<String> fields = rowCondFilters.keys();
			while(fields.hasMoreElements()){
				String field = fields.nextElement();
				Field f = getDataSet().getField(field);
				Condition[] conds = rowCondFilters.get(field);
				Object[] fieldVals = new Object[5];
				fieldVals[0] = "AND";
				fieldVals[1] = f;
				if(conds[0].getOperator() == Condition.Operator.Eq){//=
					fieldVals[2] = "=";
				} else if(conds[0].getOperator() ==  Condition.Operator.Ge){//>=
					fieldVals[2] = ">=";
				} else if(conds[0].getOperator() ==  Condition.Operator.Le){//<=
					fieldVals[2] = "<=";
				} else if(conds[0].getOperator() ==  Condition.Operator.Gt){//>
					fieldVals[2] = ">";
				} else if(conds[0].getOperator() ==  Condition.Operator.Lt){//<
					fieldVals[2] = "<";
				} else if(conds[0].getOperator() ==  Condition.Operator.Neq){//<>
					fieldVals[2] = "<>";
				} else if(conds[0].getOperator() ==  Condition.Operator.In){
					fieldVals[2] = "IN";
				}
				
				StringBuffer sbCondVars = new StringBuffer();
				String strDisplay = conds[0].getDisplay();
				fieldVals[3] = getValueType(strDisplay);
				
				sbCondVars.append(conds[0].getDisplay());
				fieldVals[4] = toDelQuotes(sbCondVars.toString());
				
				addRow(new FilterItem());
				this.setValueAt(fieldVals[0], i, 0);
				this.setValueAt(fieldVals[1], i, 1);
				this.setValueAt(fieldVals[2], i, 2);
				this.setValueAt(fieldVals[3], i, 3);
				this.setValueAt(fieldVals[4], i, 4);
				i++;
			}
		}

		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return super.isCellEditable(rowIndex, columnIndex);
		}

		public Object getValueAt(final int row, final int column) {
			FilterItem item = getItem(row);
			if (item == null) {
				return null;
			}
			switch (column) {
			case 0:
				return item.getLink();
			case 1:
				String fldName = item.getFieldName();
				if(StringUtil.isNull(fldName)){
					Field[] fields = getDataSetFields();
					if(fields.length > 0){
						item.setFieldInfo(fields[0]);
						return fields[0];
					}						
				}					
				Field fld = getDataSet().getField(fldName);
				return fld;
			case 2:
				return item.getOperation();
			case 3:
				return (item.getValueType() == FilterItem.TYPE_EXP)?
						VALUE_TYPES[1]:VALUE_TYPES[0];
			case 4:
				return item.getValue();
			default:
				break;
			}
			return null;
		}

		public void setValueAt(final Object aValue, final int row,
				final int column) {
			if(aValue == null)
				return;
			DefaultConstEnum obj = null;
			final FilterItem item = getItem(row);
			if (item == null) {
				return;
			}
			switch (column) {
			case 0:
				item.setLink((String) aValue);
				break;
			case 1:
				Field fld = (Field)aValue;
				if (fld != null) {
					item.setFieldInfo(fld);
				}
				break;
			case 2:
				item.setOperation((String) aValue);
				break;
			case 3:
				obj = ((DefaultConstEnum) aValue);
				item.setValueType(new Integer(obj.getValue() + "").intValue());
				break;
			case 4:
				item.setValue((String) aValue);
				break;
			default:
				break;
			}
			fireTableCellUpdated(row, column);
		}

		@Override
		public int getRowCount() {
			return fieldFilterItems.size();
		}

		public void addRow(FilterItem item){
			fieldFilterItems.add(item);
			fireTableDataChanged();
		}
		
		public void reMoveRow(final int row){
			if(row < 0 || row + 1 > fieldFilterItems.size()){
				return ;
			}
			fieldFilterItems.remove(row);
			fireTableDataChanged();
		}
		
		private FilterItem getItem(final int row) {
			if(row < 0 || row + 1 > fieldFilterItems.size()){
				return null;
			}
			return (FilterItem)fieldFilterItems.get(row);
		}		
	}

	public class FieldValueEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private Component comp = null;
		private SortDescriptor sortDesc = null;

		public FieldValueEditor() {
		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			UIComboBox box = new UIComboBox(getFields((Field) value));
			box.setSelectedItem((Field) value);
			comp = box;
			return box;
		}

		private Field[] getFields(Field curValue) {
			Field[] newFlds = getDataSetFields();
			if (sortDesc == null) {
				return newFlds;
			}			
			SortItem[] items = sortDesc.getSorts();
			ArrayList<Field> list = new ArrayList<Field>();
			if (curValue != null) {
				list.add(curValue);
			}
			for (Field fld : newFlds) {
				boolean contain = false;
				for (SortItem item : items) {
					if (fld.getFldname().equals(item.getFieldName())) {
						contain = true;
						break;
					}
				}
				if (!contain) {
					list.add(fld);
				}
			}
			return list.toArray(new Field[0]);
		}

		public Object getCellEditorValue() {
			Field fld = (Field) ((JComboBox) comp).getSelectedItem();
			return fld;
		}
		
	}
	
	private class FilterValueEditor extends AbstractCellEditor implements
	TableCellEditor {
		private static final long serialVersionUID = 1L;

		Component comp = null;

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			FilterItem item = (FilterItem)fieldFilterItems.get(row);
			if(item == null){
				return null;
			}
			
			JTextField fld = null;
			int valuetype = item.getValueType();
			switch (valuetype) {
			case FilterItem.TYPE_FIELD:
				UIComboBox box = new UIComboBox(getDataSet().getMetaData()
						.getFields());
				box.setSelectedItem(getDataSet().getField((String) value));
				comp = box;
				break;

			case FilterItem.TYPE_PARAM:
				UIComboBox box2 = new UIComboBox(getDataSet().getParameters());
				Parameter parameter = getDataSet().getParameter((String) value);
				if(parameter != null){
					box2.setSelectedItem(parameter);
				}
				comp = box2;
				break;

			case FilterItem.TYPE_CONST:
				fld = new JTextField();
				if (value != null) {
					fld.setText(value + "");
				}
				if (item.getDataType() == DataTypeConstant.INT
						|| item.getDataType() == DataTypeConstant.SHORT
						|| item.getDataType() == DataTypeConstant.BYTE) {
					fld.setDocument(new IntegerDocument());
				} else if (item.getDataType() == DataTypeConstant.BIGDECIMAL
						|| item.getDataType() == DataTypeConstant.FLOAT
						|| item.getDataType() == DataTypeConstant.DOUBLE) {
					fld.setDocument(new NumberDocument());
				}
				comp = fld;
				break;
				
			case FilterItem.TYPE_EXP:
				fld = new JTextField();
				if (value != null) {
					fld.setText(value + "");
				}
				fld = new FmlRefTextField(cellsPane,getRowFilterExprFmlExecutor());
				fld.setText(value == null ? "" : value.toString());
				comp = fld;
				break;
			default:
				return null;
			}
			return comp;
		}

		public Object getCellEditorValue() {
			if (comp == null)
				return null;
			if (comp instanceof JComboBox) {
				Object selectedItem = ((JComboBox) comp).getSelectedItem();
				if(selectedItem instanceof Field){
					return ((Field) selectedItem).getFldname();

				} else if(selectedItem instanceof Parameter){
					return ((Parameter) selectedItem).getName();

				}

				return null;
			}
			if (comp instanceof JTextField) {
				return ((JTextField) comp).getText();
			}
			return null;
		}
	}

	/**
	 * @i18n miufoiufoddc012=单位信息
	 * @i18n miufounit00012=单位级次编码
	 */
	private Field[] getDataSetFields(){
		Field[] flds = getDataSet().getMetaData().getFields(true);
		Vector<Field> newFlds = new Vector<Field>(); 
		//过滤掉alone_id, a0, 单位信息, 单位级次编码
		for(Field d:flds){
			if(!d.getFldname().equals("alone_id") && 
					!d.getFldname().equals("a0") && 
					!d.getCaption().equals(StringResource.getStringResource("miufoiufoddc012")) && 
					!d.getCaption().equals(StringResource.getStringResource("miufounit00012"))){
				newFlds.add(d);
			}
		}
		return newFlds.toArray(new Field[0]);
	}
	
	/**
	 * @i18n miufo00616=字段条件
	 * @i18n miufo00617=未正确设置值
	 * @i18n mbiadhoc00003=字段
	 */
	public String check(){
		FilterTableModel dataModel = (FilterTableModel)this.getTM();
		int nRow = dataModel.getRowCount();
		StringBuffer bufRowCord = new StringBuffer();
		Vector<String> errorName = new Vector<String>();
		for(int row = 0; row < nRow; row++){
			FilterItem item = (FilterItem) dataModel.getItem(row);
			Field fld = getDataSet().getField(item.getFieldName());
			if(fld == null){
				errorName.add(StringResource.getStringResource("mbiadhoc00003"));
			}
			String strFldName = fld != null ? fld.getCaption() : "";
			String strExpr = (String)item.getValue();
			if(strExpr == null || strExpr.trim().length() == 0){
				errorName.add(strFldName);
			}
		}
		for (int i = 0; i < errorName.size(); i++) {
			bufRowCord.append(errorName.get(i));
			if(i<errorName.size() -1)
				bufRowCord.append(",");
		}
		
		if(bufRowCord.length() > 0){
			bufRowCord.insert(0, StringResource.getStringResource("miufo00616")+StringResource.getStringResource("miufo00831"));
			bufRowCord.append(StringResource.getStringResource("miufo00617"));
		}
		
		String checkParam = ((LimitRowPanel)getPnNorth()).check();
		if(checkParam != null){
			bufRowCord.append(checkParam);
		}
		
		//modify by wangyga 2008-11-19 去掉数据集参数校验
//		String checkParam = ivjPnSouth.check();
//		if(checkParam != null){
//			bufRowCord.append(checkParam);
//		}
		return bufRowCord.toString();
	}
	
	public String getRowCordFilter(){
		FilterTableModel dataModel = (FilterTableModel)this.getTM();
		int nRow = dataModel.getRowCount();
		StringBuffer bufRowCord = new StringBuffer();
		for(int row = 0; row < nRow; row++){
			FilterItem item = (FilterItem) dataModel.getItem(row);
			String strFieldName = ((Field)dataModel.getValueAt(row, 1)).getCaption();
			String strExpr = (String)item.getValue();
			bufRowCord.append("'");
			bufRowCord.append(strFieldName);
			bufRowCord.append("'");			
			boolean isNumberType = DataTypeConstant.isNumberType(item.getDataType());
			boolean isInSign = item.getOperation().equalsIgnoreCase(FilterItem.FILTER_OPERATOR_IN);
			boolean isConst = (!DataTypeConstant.isNumberType(item.getDataType()) && 
					item.getValueType() == FilterItem.TYPE_CONST);
			if(isInSign){
				bufRowCord.append("IN(");
				StringTokenizer token = new StringTokenizer(strExpr, ",");
				while (token.hasMoreElements()) {
					String strValue = token.nextToken();
					if(!isNumberType){
						bufRowCord.append("\"'");
						bufRowCord.append(strValue);
						bufRowCord.append("'\"");
						bufRowCord.append(",");
					}
				}
				bufRowCord.deleteCharAt(bufRowCord.length()-1);
				bufRowCord.append(")");
			} else{
				bufRowCord.append(item.getOperation());
				if(isConst)	bufRowCord.append(" \'");
				bufRowCord.append(strExpr);
				if(isConst)	bufRowCord.append("\'");
			}
			if(row < nRow-1) bufRowCord.append(" AND ");
		}
		return bufRowCord.toString();
	}
	
	public String getParamCordFilter(){
		return ivjPnSouth.getParamCordFilter();
	}
	
	/**
	 * 根据显示的内容判断是常量还是表达式:表达式必须是系统里面有效的公式,如果是数字或者字符还当作常量
	 * 
	 * @param strDisplay
	 * @return Object
	 */
	private Object getValueType(String strDisplay){
		if(strDisplay == null || strDisplay.trim().length() ==0)
			return VALUE_TYPES[0];
		int startPos = strDisplay.indexOf("(");
		int endPos = strDisplay.indexOf(")");
		if(startPos < 0 || endPos <0)
			return VALUE_TYPES[0];
		String strExpName = strDisplay.substring(0, startPos);
		UfoFmlExecutor rowFilterFmlExecutor = getRowFilterExprFmlExecutor();
		FuncListInst funcList = rowFilterFmlExecutor.getFuncListInst();
		if(funcList == null)
			return VALUE_TYPES[0];
		UfoSimpleObject[] catList = funcList.getCatList();

		if (catList == null || catList.length == 0)
			return VALUE_TYPES[0];

		for (UfoSimpleObject module : catList) {
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				return VALUE_TYPES[0];
			for (UfoSimpleObject funcNameObj : m_FuncNameList) {
				if (funcNameObj.getName().equalsIgnoreCase(strExpName))
					return VALUE_TYPES[1];
			}
		}

		return VALUE_TYPES[0];
	}
	
	/**
	 * @i18n miufo00618=行条件类型=
	 * @i18n miufo00619='最前'
	 * @i18n miufo00620= AND 行号1=
	 * @i18n miufo00621='最后'
	 * @i18n miufo00622='等于'
	 * @i18n miufo00623='介于'
	 * @i18n miufo00624= AND 行号2=
	 */
	public String getLimitRowFilter(){
		LimitRowNumber limitRowNumber = getLimitRowNumber();
		if(limitRowNumber == null || !isAddLimitRowFilter()) return null;
		StringBuffer bufLimitRowCord = new StringBuffer();
		bufLimitRowCord.append(StringResource.getStringResource("miufo00618"));
		switch(limitRowNumber.getLimitType()){
		case LimitRowNumber.TYPE_FIRST: //前N行
			bufLimitRowCord.append(StringResource.getStringResource("miufo00619"));
			bufLimitRowCord.append(StringResource.getStringResource("miufo00620"));
			bufLimitRowCord.append(limitRowNumber.getFirstLimitRowNum());
			break;
		case LimitRowNumber.TYPE_LAST: //后N行
			bufLimitRowCord.append(StringResource.getStringResource("miufo00621"));
			bufLimitRowCord.append(StringResource.getStringResource("miufo00620"));
			bufLimitRowCord.append(limitRowNumber.getFirstLimitRowNum());
			break;
		case LimitRowNumber.TYPE_EQUAL: //等于
			bufLimitRowCord.append(StringResource.getStringResource("miufo00622"));
			bufLimitRowCord.append(StringResource.getStringResource("miufo00620"));
			bufLimitRowCord.append(limitRowNumber.getFirstLimitRowNum());
			break;
		case LimitRowNumber.TYPE_BETWEEN: //N1-N2行之间
			bufLimitRowCord.append(StringResource.getStringResource("miufo00623"));
			bufLimitRowCord.append(StringResource.getStringResource("miufo00620"));
			bufLimitRowCord.append(limitRowNumber.getFirstLimitRowNum());
			bufLimitRowCord.append(StringResource.getStringResource("miufo00624"));
			bufLimitRowCord.append(limitRowNumber.getLastLimitRowNum());
			break;
		}
		if(limitRowNumber.isPriority()){
			bufLimitRowCord.append(" AND isPriority=1");
		}
		return bufLimitRowCord.toString();
	}
	
	private boolean isAddLimitRowFilter(){
		LimitRowNumber limitRowNumber = getLimitRowNumber();
		if(limitRowNumber == null) return false;
		if(limitRowNumber.getLimitType() == LimitRowNumber.TYPE_FIRST || 
				limitRowNumber.getLimitType() == LimitRowNumber.TYPE_LAST ||
				limitRowNumber.getLimitType() == LimitRowNumber.TYPE_EQUAL){
			return limitRowNumber.getFirstLimitRowNum() != 0;
		} else{
			return limitRowNumber.getFirstLimitRowNum() != 0 && 
					limitRowNumber.getLastLimitRowNum() != 0;
		}
	}
	
	private LimitRowNumber getLimitRowNumber(){
		LimitRowPanel pnlLimitRow = (LimitRowPanel)getPnNorth();
		return pnlLimitRow.getLimitRowNumber();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//限制行数
	private class LimitRowPanel extends UIPanel {
		private static final long serialVersionUID = 1L;

		private UIPanel ivjPnNorth = null;

		private UITextField ivjTFSRowNumber = null;

		private UITextField ivjTFERowNumber = null;

		private UIComboBox ivjCbbRowNumber = null;

		private UILabel ivjLabelSRowNumber = null;

		private UILabel ivjLabelERowNumber = null;
		
		private JCheckBox ivjCBPriority = null;
		
		private LimitRowNumber limitRowNumber = null;
		
		/**
		 * 返回 box 特性值。
		 * 
		 * @return UIComboBox
		 */
		private UIComboBox getCbbRowNumber() {
			if (ivjCbbRowNumber == null) {
				try {
					ivjCbbRowNumber = new UIComboBox();
					ivjCbbRowNumber.setName("CbbRowNumber");
					ivjCbbRowNumber.setPreferredSize(new Dimension(110, 1));
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjCbbRowNumber;
		}

		/**
		 * 由限制行数条件初始化条件显示
		 * @param limitRowNumber
		 */
		public void setLimitRow(LimitRowNumber limitRowNumber) {
			this.limitRowNumber = limitRowNumber;
			if(this.limitRowNumber != null){
				switch(this.limitRowNumber.getLimitType()){
				case LimitRowNumber.TYPE_FIRST:
				case LimitRowNumber.TYPE_LAST:
				case LimitRowNumber.TYPE_EQUAL:
					getTFSRowNumber().setText(""+this.limitRowNumber.getFirstLimitRowNum());
					getTFERowNumber().setText("");
					getTFERowNumber().setVisible(false);
					break;
				case LimitRowNumber.TYPE_BETWEEN:
					getTFSRowNumber().setText(""+this.limitRowNumber.getFirstLimitRowNum());
					getTFERowNumber().setText(""+this.limitRowNumber.getLastLimitRowNum());
					getTFERowNumber().setVisible(true);
					break;
				}
				getCbbRowNumber().setSelectedIndex(this.limitRowNumber.getLimitType()-1);
				getCBPriority().setSelected(limitRowNumber.isPriority());
			} else{
				getTFSRowNumber().setText("");
				getTFERowNumber().setText("");
				getTFERowNumber().setVisible(false);
				getCbbRowNumber().setSelectedIndex(0);
				getCBPriority().setSelected(false);
				
			}
		}

		/**
		 * 返回 ivjLabelSRowNumber 特性值。
		 * 
		 * @return UILabel
		 * @i18n miufo1000794=行
		 */
		private UILabel getLabelSRowNumber() {
			if (ivjLabelSRowNumber == null) {
				try {
					ivjLabelSRowNumber = new UILabel();
					ivjLabelSRowNumber.setName("LabelRowNumber");
					ivjLabelSRowNumber.setText(StringResource.getStringResource("miufo1000794"));
					ivjLabelSRowNumber.setForeground(Color.black);
					ivjLabelSRowNumber
							.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
					ivjLabelSRowNumber.setILabelType(0);
					ivjLabelSRowNumber
							.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjLabelSRowNumber;
		}

		/**
		 * 返回 ivjLabelERowNumber 特性值。
		 * 
		 * @return UILabel
		 * @i18n miufo00625=行之间
		 */
		private UILabel getLabelERowNumber() {
			if (ivjLabelERowNumber == null) {
				try {
					ivjLabelERowNumber = new UILabel();
					ivjLabelERowNumber.setName("LabelRowNumber");
					ivjLabelERowNumber.setText(StringResource.getStringResource("miufo00625"));
					ivjLabelERowNumber.setForeground(Color.black);
					ivjLabelERowNumber
							.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
					ivjLabelERowNumber.setILabelType(0);
					ivjLabelERowNumber
							.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjLabelERowNumber;
		}
		
		/**
		 * 返回 ivjCBPriority 特性值。
		 * 
		 * @return UILabel
		 * @i18n miufo00626=优先执行
		 */
		private JCheckBox getCBPriority() {
			if (ivjCBPriority == null) {
				try {
					ivjCBPriority = new JCheckBox();
					ivjCBPriority.setName("cbPriority");
					ivjCBPriority.setText(StringResource.getStringResource("miufo00626"));
					ivjCBPriority
							.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
					ivjCBPriority
							.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjCBPriority;
		}
		
		/**
		 * 返回 PnNorth 特性值。
		 * 
		 * @return UIPanel
		 */
		private UIPanel getPnNorth() {
			if (ivjPnNorth == null) {
				try {
					ivjPnNorth = new UIPanel();
					ivjPnNorth.setName("PnNorth");
					ivjPnNorth.setPreferredSize(new Dimension(10, 33));
					ivjPnNorth.setBorder(new javax.swing.border.EtchedBorder());
					ivjPnNorth.add(getCbbRowNumber(), getCbbRowNumber().getName());
					ivjPnNorth.add(getTFSRowNumber(), getTFSRowNumber().getName());
					ivjPnNorth.add(getLabelSRowNumber(), getLabelSRowNumber()
							.getName());
					ivjPnNorth.add(getTFERowNumber(), getTFERowNumber().getName());
					ivjPnNorth.add(getLabelERowNumber(), getLabelERowNumber()
							.getName());
					doHideTextField(limitRowNumber == null || 
							limitRowNumber.getLimitType() != LimitRowNumber.TYPE_BETWEEN);
					ivjPnNorth.setBorder(new javax.swing.border.EtchedBorder());
					ivjPnNorth.setBorder(new javax.swing.border.EtchedBorder());
					ivjPnNorth.add(getCBPriority(), getCBPriority()
							.getName());
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjPnNorth;
		}

		/**
		 * 返回 TFRowNumber 特性值。
		 * 
		 * @return UITextField
		 */
		private UITextField getTFSRowNumber() {
			if (ivjTFSRowNumber == null) {
				try {
					ivjTFSRowNumber = new UITextField();
					ivjTFSRowNumber.setName("TFSRowNumber");
					ivjTFSRowNumber.setPreferredSize(new Dimension(110, 20));
					ivjTFSRowNumber.setMaxLength(100);
					ivjTFSRowNumber.setDocument(new NumberDocument());
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjTFSRowNumber;
		}

		/**
		 * 返回 TFRowNumber 特性值。
		 * 
		 * @return UITextField
		 */
		private UITextField getTFERowNumber() {
			if (ivjTFERowNumber == null) {
				try {
					ivjTFERowNumber = new UITextField();
					ivjTFERowNumber.setName("TFERowNumber");
					ivjTFERowNumber.setPreferredSize(new Dimension(110, 20));
					ivjTFERowNumber.setMaxLength(100);
					ivjTFERowNumber.setDocument(new NumberDocument());
				} catch (java.lang.Throwable ivjExc) {
					handleException(ivjExc);
				}
			}
			return ivjTFERowNumber;
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

		public LimitRowPanel() {
			super();
			initialize();
			initTextField();
			initComboBox();
		}

		/**
		 * 初始化类
		 */
		protected void initialize() {
			try {
				setName("LimitRowPanel");
				setLayout(new BorderLayout());
				add(getPnNorth(), BorderLayout.CENTER);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		
		private void initTextField() {
			int firstLimitRowNum = 0;
			int lastLimitRowNum = 0;
			if(limitRowNumber != null){
				firstLimitRowNum = limitRowNumber.getFirstLimitRowNum();
				lastLimitRowNum = limitRowNumber.getLastLimitRowNum();
			}
			getTFSRowNumber().setText(firstLimitRowNum!=0?(String.valueOf(firstLimitRowNum)):"");
			getTFERowNumber().setText(lastLimitRowNum!=0?(String.valueOf(lastLimitRowNum)):"");
			getCBPriority().addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					if(limitRowNumber == null){
						limitRowNumber = new LimitRowNumber();
					}
					limitRowNumber.setPriority(getCBPriority().isSelected());					
				}
				
			});
		}

		/**
		 * 设置限制函数条件第一个参数值
		 * @param str
		 */
		private void setFirstLimitRowNum(){
			String str = getTFSRowNumber().getText();
			if (str == null || str.trim().length() < 1) {
				if(limitRowNumber == null){
					limitRowNumber = new LimitRowNumber();
				}
				limitRowNumber.setFirstLimitRowNum(0);
			} else {
				if(limitRowNumber == null){
					limitRowNumber = new LimitRowNumber();
				}
				limitRowNumber.setFirstLimitRowNum(new Integer(str).intValue());
			}
		}
		
		/**
		 * 设置限制函数条件第二个参数值
		 * @param str
		 */
		private void setLastLimitRowNum(){
			String str = getTFERowNumber().getText();
			if (str == null || str.trim().length() < 1) {
				if(limitRowNumber == null){
					limitRowNumber = new LimitRowNumber();
				}
				limitRowNumber.setLastLimitRowNum(0);
			} else {
				if(limitRowNumber == null){
					limitRowNumber = new LimitRowNumber();
				}
				limitRowNumber.setLastLimitRowNum(new Integer(str).intValue());
			}
		}
		
		/**
		 * @i18n mbimulticalc014=最前
		 * @i18n mbimulticalc013=最后
		 * @i18n miufoopersign00001=等于
		 * @i18n miufo00627=介于
		 */
		private void initComboBox() {
			int limitType = LimitRowNumber.TYPE_FIRST;
			if(limitRowNumber != null){
				limitType = limitRowNumber.getLimitType();
			}
			final DefaultConstEnum[] items = new DefaultConstEnum[] {
					new DefaultConstEnum(LimitRowNumber.TYPE_FIRST, StringResource.getStringResource("mbimulticalc014")),
					new DefaultConstEnum(LimitRowNumber.TYPE_LAST, StringResource.getStringResource("mbimulticalc013")), 
					new DefaultConstEnum(LimitRowNumber.TYPE_EQUAL, StringResource.getStringResource("miufoopersign00001")),
					new DefaultConstEnum(LimitRowNumber.TYPE_BETWEEN, StringResource.getStringResource("miufo00627"))};
			getCbbRowNumber().setModel(new DefaultComboBoxModel(items));
			getCbbRowNumber().setSelectedItem(items[limitType - 1]);
			getCbbRowNumber().addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					DefaultConstEnum item = (DefaultConstEnum) getCbbRowNumber().getSelectedItem();
					if (item != null) {
						if(limitRowNumber == null){
							limitRowNumber = new LimitRowNumber();
						}
						limitRowNumber.setLimitType((Integer)item.getValue());
						doHideTextField((Integer)item.getValue() != LimitRowNumber.TYPE_BETWEEN);
					}
				}
			});
		}	

		private void doHideTextField(boolean isHide){
			getTFERowNumber().setVisible(!isHide);
			getLabelERowNumber().setVisible(!isHide);
		}
		
		public LimitRowNumber getLimitRowNumber() {
			setFirstLimitRowNum();
			setLastLimitRowNum();
			return limitRowNumber;
		}

		public void setLimitRowNumber(LimitRowNumber limitRowNumber) {
			this.limitRowNumber = limitRowNumber;
		}
		
		/**
		 * @i18n miufohbbb00082=限制行条件最后的值要大于第一个值
		 */
		public String check(){
			DefaultConstEnum item = (DefaultConstEnum) getCbbRowNumber().getSelectedItem();
			if(item == null || (item != null && (Integer)item.getValue() != LimitRowNumber.TYPE_BETWEEN)){
				return null;
			}
			if(limitRowNumber == null)
				return null;
			getLimitRowNumber();
			int firstRowNum = limitRowNumber.getFirstLimitRowNum();
			int lastRowNum = limitRowNumber.getLastLimitRowNum();
			
			if(lastRowNum < firstRowNum)
				return StringResource.getStringResource("miufohbbb00082");
			return null;
		}
	}

	/**
	 * 避免每次都去实例该类
	 * @return
	 */
	private UfoFmlExecutor getRowFilterExprFmlExecutor(){
		if(rowFilterExprFmlExecutor == null){
             rowFilterExprFmlExecutor = new RowFilterExprFmlExecutor(
					contextVo, cellsPane.getDataModel());
		}
		return rowFilterExprFmlExecutor;
	}
	
	/**
	 * 去多余的引号
	 * @param str
	 * @return
	 */
	private String toDelQuotes(String str){
		if(str == null || str.length() == 0 || str.equals("''")){
			return "";
		}
		if(str.charAt(0) == '\'' && str.charAt(str.length()-1) == '\''){
			return str.substring(1, str.length()-1);
		} else{
			return str;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//控制文本输入
	public class FloatDoc extends PlainDocument {
		private static final long serialVersionUID = 1L;
		public void insertString(int offs, String str, AttributeSet a)
		throws BadLocationException {
			boolean error = false;
			try {
				String name = getText(0, offs) + str + getText(offs, getLength() - offs);
				if(!"-".equals(name)){
					Double.parseDouble(name);
				}
			} catch (Exception e) {
				error = true;
			}
			if (error) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString(offs, str, a);
		}
	}
	
}
 