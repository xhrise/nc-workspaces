package com.ufida.report.chart.applet;

import static com.sun.java.swing.SwingUtilities2.DRAG_FIX;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeCellEditor;

import nc.ui.pub.beans.UIRadioButton;

import com.ufida.report.chart.model.DimChartModel;
import com.ufida.report.chart.property.IDataProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;


/**
 * 图表数据选取表格
 * @author caijie
 *
 */
public class DimDataTable extends JTable implements PropertyChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo1000589=选择
	 */
	public static final String FIRST_COLUMN_NAME = StringResource.getStringResource("miufo1000589");
	@SuppressWarnings("unused")
	private static String[] columnNames = {"Col1" , "Col2","Col3","Col4"};
	@SuppressWarnings("unused")
	private static String[] rowNames = {"Row1","Row2","Row3"};
	@SuppressWarnings("unused")
	private static Object[][] rowColdatas = {{"r1","r1","r1","r1"},
			 {"r2","r2","r2","r2"},
			 {"r3","r3","r3","r3"}};
	
	private class DataTableModel extends DefaultTableModel{	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DimChartModel m_dimDataModel = null;
		private boolean[] m_selectedRows = null;
		private boolean[] m_selectedColumns = null;
		
		public DataTableModel(DimChartModel dimDataModel){
			super();
			m_dimDataModel = dimDataModel;
			setDataVector(getDatas(), getColumndNames());			
			resetSelectedFlags();
		}		
		
		public void resetSelectedFlags(){
			m_selectedColumns = new boolean[getColumndNames().length];
			m_selectedRows = new boolean[getDatas().length];
			
			String[] names = getColumndNames();
			ArrayList usedNameList = getDataProperty().getDataProvider().getUsedKeys(IDataProperty.X_AXIS, getDataProperty());
			for(int i = 0; i < m_selectedColumns.length; i++){
				if(usedNameList.contains(names[i])){
					m_selectedColumns[i] = true;
				}else{
					m_selectedColumns[i] = false;
				}
			}
			
			names = getRowNames();
			usedNameList = getDataProperty().getDataProvider().getUsedKeys(IDataProperty.Y_AXIS, getDataProperty());
			for(int i = 0; i < m_selectedRows.length; i++){
				if(usedNameList.contains(names[i])){
					m_selectedRows[i] = true;
				}else{
					m_selectedRows[i] = false;
				}
			}
			
		}
		
		public  Object[][] getDatas(){
			Object[][] datas = new Object[getRowNames().length][getColumndNames().length];
			Object[][] oriDatas = new Object[getRowNames().length][getColumndNames().length - 1];
			if(m_dimDataModel.getOperationState() == ContextVO.OPERATION_INPUT){
				oriDatas = m_dimDataModel.getMultiDimemsionModel().getDataModel().getDatas();
			}
			//Object[][] oriDatas = rowColdatas;
			if(oriDatas != null){			
				NumberFormat numberFormat = NumberFormat.getInstance();
				for(int i = 0; i < datas.length; i++){
					datas[i][0] = new Boolean(true);
					for(int j = 1; j < datas[0].length; j++){
						if(oriDatas[i][j-1] == null){
//							datas[i][j] = 
						}else if(oriDatas[i][j-1] instanceof Double){
							datas[i][j] = numberFormat.format(oriDatas[i][j-1]);
						}else{
							datas[i][j] = String.valueOf(oriDatas[i][j-1]);
						}
						
					}
				}
					
			}
			return datas;
		}
		
		private IDataProperty getDataProperty(){
			return m_dimDataModel.getChartPropertry().getDataProperty();
		}
		@SuppressWarnings("unchecked")
		public  String[] getColumndNames(){
			ArrayList list = new ArrayList();				
			list.add(FIRST_COLUMN_NAME);
			list.addAll(getDataProperty().getDataProvider().getKeys(IDataProperty.X_AXIS, getDataProperty()));			
			//for(int i = 0; i < columnNames.length; i++)list.add(columnNames[i]);
			return (String[]) list.toArray(new String[0]);		
		}
		
		@SuppressWarnings("unchecked")
		public String[] getRowNames(){			
			ArrayList list = getDataProperty().getDataProvider().getKeys(IDataProperty.Y_AXIS, getDataProperty());			
			return (String[]) list.toArray(new String[0]); 
		//	return rowNames;
		}
	    public void setValueAt(Object aValue, int row, int column) {
	    	super.setValueAt(aValue, row, column);
	    	if(column == 0){	    		
	    		selectRow(row);
	    	}
	    	fireTableDataChanged();
	    }
	    public Object getValueAt(int row, int column) {
			  if(column == 0){
				  return m_selectedRows[row];
			  }
		      return super.getValueAt(row, column);
		}
        public boolean isCellEditable(int rowIndex, int columnIndex) {
        	if(columnIndex == 0) return true;
        	 return false;
        }
        
        //选择某行
        public void selectRow(int row){
        	m_selectedRows[row] = !m_selectedRows[row];
        	if(isSingleRowSelection()){
        		for(int i = 0; i < m_selectedRows.length; i++){
        			m_selectedRows[i] = false;
        		}
        		m_selectedRows[row] = true;
        	}
        	
        	
        }
        //选择某列
        public void selectColumn(int col){ 
        	m_selectedColumns[col] = !m_selectedColumns[col];        	
        	if((col == 0) && (isSingleRowSelection() == false)){//如果第一列被选择的话，默认选择最多行
        		boolean selected = m_selectedColumns[0];        		
        		for(int i = 0; i < m_selectedRows.length; i++){
        			m_selectedRows[i] = selected;
        		}
        	}else{
        		if(isSingleColumnSelection()){
            		for(int i = 0; i < m_selectedColumns.length; i++){
            			m_selectedColumns[i] = false;
            		}
            		m_selectedColumns[col] = true;
            	}
        	}
        	
        }
        //已选择行列
        public boolean[] getRowStatus(){
        	return m_selectedRows;
        }
//      已选择行列
        public boolean[] getColumnStatus(){
        	return m_selectedColumns;
        }        
	} 
	private  class JRadioButtonHeaderRenderer implements TableCellRenderer{
		JRadioButton rb;
		DataTableModel model = null;
		public JRadioButtonHeaderRenderer(DataTableModel model, TableColumn column){
			this.model = model; 
			rb = new JRadioButton(column.getHeaderValue().toString());	
			rb.setToolTipText(column.getHeaderValue().toString());
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			rb.setSelected(model.getColumnStatus()[column]);
			return rb;
		}
		
		public JRadioButton getRadioButton(){
			return rb;
		}
	}
	private  class JCheckBoxHeaderRenderer implements TableCellRenderer{
		DataTableModel model = null;
		JCheckBox jcb;		
		public JCheckBoxHeaderRenderer(DataTableModel model, TableColumn column){
			this.model = model; 
			jcb = new JCheckBox(column.getHeaderValue().toString());
			jcb.setToolTipText(column.getHeaderValue().toString());
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			jcb.setSelected(model.getColumnStatus()[column]);
			return jcb;
		}
		
		public JCheckBox getJCheckBox(){
			return jcb;
		}
	}
	private DimChartModel m_dimDataModel = null;
	public DimDataTable(DimChartModel dimDataModel){
		super();
		m_dimDataModel = dimDataModel;
		m_dimDataModel.getChartPropertry().getDataProperty().getDataProvider().getPropertyChangeSupport().removePropertyChangeListener(this);
		m_dimDataModel.getChartPropertry().getDataProperty().getDataProvider().getPropertyChangeSupport().addPropertyChangeListener(this);
		m_dimDataModel.removeChangeListener(this);
		m_dimDataModel.addChangeListener(this);
		setModel(new DataTableModel(dimDataModel));		
		init();
	}
	public String[] getSelectedRowNames(){
		DataTableModel model = (DataTableModel)getModel();
//		Object[][] datas = model.getDatas();		
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < model.getRowStatus().length; i++){
			if(model.getRowStatus()[i]){
				list.add(model.getRowNames()[i]);
			}
		}			
		return (String[])list.toArray(new String[0]);
	}
	public String[] getSelectedColumnNames(){
		DataTableModel model = (DataTableModel)getModel();		
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 1; i < model.getColumnStatus().length; i++){
			if(model.getColumnStatus()[i]){
				list.add(model.getColumndNames()[i]);
			}
		}			
		return (String[])list.toArray(new String[0]);	
	}
	
	public boolean isSingleRowSelection(){
		int num = 1;
		num = getDimDataModel().getChartPropertry().getDataProperty().getDataLimitNum(IDataProperty.Y_AXIS);
		return num == 1;		
	}	
	public boolean isSingleColumnSelection(){
		int num = 2;
		num = getDimDataModel().getChartPropertry().getDataProperty().getDataLimitNum(IDataProperty.X_AXIS);
		return num == 1;		
	}
	
	private DimChartModel getDimDataModel(){
		return m_dimDataModel;
	}
	private void initColumns(){
		Object[] cols = ((DataTableModel)getModel()).getColumndNames();
		if(cols == null) return;
		for(int i = 0; i < cols.length; i++){
			TableColumn column = getColumn(cols[i].toString());
			if(FIRST_COLUMN_NAME.equalsIgnoreCase(column.getHeaderValue().toString()) == true){
				if(isSingleRowSelection() == false){//允许多选
					column.setCellEditor(new CheckBoxEditor());	
					column.setCellRenderer( new CheckBoxRender());
					column.setHeaderRenderer(new JCheckBoxHeaderRenderer((DataTableModel)getModel(), column));
				}else{
					column.setCellEditor(new RadioButtonEditor( new UIRadioButton()));	
					column.setCellRenderer( new RadioButtonRender());
					column.setHeaderRenderer(null);
				}
			}else{
				if(isSingleColumnSelection() == false){
					column.setHeaderRenderer(new JCheckBoxHeaderRenderer((DataTableModel)getModel(), column));
				}else{
					column.setHeaderRenderer(new JRadioButtonHeaderRenderer((DataTableModel)getModel(),column));
				}
				column.setCellRenderer(new RowRender((DataTableModel)getModel()));
			}		
		}	
	}

	private void init(){
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setAutoscrolls(true);
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
		
		initColumns();		
		JTableHeader header = getTableHeader();			
		header.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {				
				TableColumnModel tcm = getColumnModel();
				int vc = tcm.getColumnIndexAtX(e.getX());
				int mc = convertColumnIndexToModel(vc);
				
				((DataTableModel)getModel()).selectColumn(mc);
			//	System.out.println("header mouseClicked "+ mc );
				refresh();						
			}});
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt == null) return;
		DataTableModel model = new DataTableModel(getDimDataModel());		
		setModel(model);	
		model.resetSelectedFlags();
		refresh();
	}
	/**
	 * 刷新
	 */
	public void refresh(){	
		//刷新所有列头
		
		DataTableModel model = (DataTableModel)getModel();
		
		initColumns();
		model.fireTableDataChanged();
		
		boolean[] colStatus = model.getColumnStatus();		
		Object[] cols = ((DataTableModel)getModel()).getColumndNames();
		if(cols == null) return;
		for(int i = 0; i < cols.length; i++){
			//if(FIRST_COLUMN_NAME.equalsIgnoreCase(cols[i].toString())) {continue;
			TableColumn column = getColumn(cols[i]);			
			if(column.getHeaderRenderer() instanceof JRadioButtonHeaderRenderer){
				JRadioButtonHeaderRenderer renderer = (JRadioButtonHeaderRenderer)column.getHeaderRenderer();
				renderer.getRadioButton().setSelected(colStatus[i]);
			}else if(column.getHeaderRenderer() instanceof JCheckBoxHeaderRenderer){
				JCheckBoxHeaderRenderer renderer = (JCheckBoxHeaderRenderer)column.getHeaderRenderer();
				renderer.getJCheckBox().setSelected(colStatus[i]);
			}			
		}				
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		   final DimDataTable table = new DimDataTable(null);	
			JDialog dlg = new JDialog();
			dlg.getContentPane().add(new JScrollPane(table));
			dlg.pack();       
			dlg.setLocationRelativeTo(null);
			dlg.setSize(400,300);
			dlg.setVisible(true);
		//	dlg.getComponent()
	}
	@SuppressWarnings("serial")
	private class CheckBoxEditor extends DefaultCellEditor{	
		JCheckBox m_checkBox = null;
		public CheckBoxEditor(){
			super(new JCheckBox());
			m_checkBox = (JCheckBox)this.getComponent();
			m_checkBox.setToolTipText(m_checkBox.getText());
			m_checkBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {					
				//	System.out.print(m_checkBox.isSelected());
					
					
				}});
		}
		
	}
	
	@SuppressWarnings("serial")
	private class CheckBoxRender extends DefaultTableCellRenderer{ 
			public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {
	   		 boolean  selected = false;    		 
	   		 if (value instanceof Boolean) {
	   			    selected = ((Boolean)value).booleanValue();
	   			}
	   			else if (value instanceof String) {
	   			    selected = value.equals("true");
	   			}    		
	   		DataTableModel model = (DataTableModel)table.getModel();
	   		JCheckBox jcb =  new JCheckBox(model.getRowNames()[row], selected);
	   		jcb.setToolTipText(model.getRowNames()[row]);
	   		 return jcb;
			}
	   }
	
	private class RadioButtonEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected JComponent editorComponent;
	    /**
	     * The delegate class which handles all methods sent from the
	     * <code>CellEditor</code>.
	     */
	    protected EditorDelegate delegate;
	    /**
	     * An integer specifying the number of clicks needed to start editing.
	     * Even if <code>clickCountToStart</code> is defined as zero, it
	     * will not initiate until a click occurs.
	     */
	    protected int clickCountToStart = 1;

    	    /**
	     * Constructs a <code>DefaultCellEditor</code> object that uses a check box.
	     *
	     * @param checkBox  a <code>JCheckBox</code> object
	     */
	    public RadioButtonEditor(final JRadioButton radioButton) {
	        editorComponent = radioButton;
	        delegate = new EditorDelegate() {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 5910814636848052144L;

				public void setValue(Object value) { 
	            	boolean selected = false; 
			if (value instanceof Boolean) {
			    selected = ((Boolean)value).booleanValue();
			}
			else if (value instanceof String) {
			    selected = value.equals("true");
			}
			radioButton.setSelected(selected);
	            }

		    public Object getCellEditorValue() {
			return Boolean.valueOf(radioButton.isSelected());
		    }
	        };
	        radioButton.addActionListener(delegate);

	        if (DRAG_FIX) {
	        	radioButton.setRequestFocusEnabled(false);
	        }
	    }  	  

	    /**
	     * Returns a reference to the editor component.
	     *
	     * @return the editor <code>Component</code>
	     */
	    public Component getComponent() {
		return editorComponent;
	    }

	//
//	  Modifying
	//

	    /**
	     * Specifies the number of clicks needed to start editing.
	     *
	     * @param count  an int specifying the number of clicks needed to start editing
	     * @see #getClickCountToStart
	     */
	    public void setClickCountToStart(int count) {
		clickCountToStart = count;
	    }

	    /**
	     * Returns the number of clicks needed to start editing.
	     * @return the number of clicks needed to start editing
	     */
	    public int getClickCountToStart() {
		return clickCountToStart;
	    }

	//
//	  Override the implementations of the superclass, forwarding all methods 
//	  from the CellEditor interface to our delegate. 
	//

	    /**
	     * Forwards the message from the <code>CellEditor</code> to
	     * the <code>delegate</code>.
	     * @see EditorDelegate#getCellEditorValue
	     */
	    public Object getCellEditorValue() {
	        return delegate.getCellEditorValue();
	    }

	    /**
	     * Forwards the message from the <code>CellEditor</code> to
	     * the <code>delegate</code>.
	     * @see EditorDelegate#isCellEditable(EventObject)
	     */
	    public boolean isCellEditable(EventObject anEvent) { 
		return delegate.isCellEditable(anEvent); 
	    }
	    
	    /**
	     * Forwards the message from the <code>CellEditor</code> to
	     * the <code>delegate</code>.
	     * @see EditorDelegate#shouldSelectCell(EventObject)
	     */
	    public boolean shouldSelectCell(EventObject anEvent) { 
		return delegate.shouldSelectCell(anEvent); 
	    }

	    /**
	     * Forwards the message from the <code>CellEditor</code> to
	     * the <code>delegate</code>.
	     * @see EditorDelegate#stopCellEditing
	     */
	    public boolean stopCellEditing() {
		return delegate.stopCellEditing();
	    }

	    /**
	     * Forwards the message from the <code>CellEditor</code> to
	     * the <code>delegate</code>.
	     * @see EditorDelegate#cancelCellEditing
	     */
	    public void cancelCellEditing() {
		delegate.cancelCellEditing();
	    }

	//
//	  Implementing the TreeCellEditor Interface
	//

	    /** Implements the <code>TreeCellEditor</code> interface. */
	    public Component getTreeCellEditorComponent(JTree tree, Object value,
							boolean isSelected,
							boolean expanded,
							boolean leaf, int row) {
		String         stringValue = tree.convertValueToText(value, isSelected,
						    expanded, leaf, row, false);

		delegate.setValue(stringValue);
		return editorComponent;
	    }

	//
//	  Implementing the CellEditor Interface
	//
	    /** Implements the <code>TableCellEditor</code> interface. */
	    public Component getTableCellEditorComponent(JTable table, Object value,
							 boolean isSelected,
							 int row, int column) {
	        delegate.setValue(value);
		return editorComponent;
	    }


	//
//	  Protected EditorDelegate class
	//

	    /**
	     * The protected <code>EditorDelegate</code> class.
	     */
	    private class EditorDelegate implements ActionListener, ItemListener, Serializable {

	        /**
			 * 
			 */
			private static final long serialVersionUID = -2521603422044082116L;
			/**  The value of this cell. */
	        protected Object value;

	       /**
	        * Returns the value of this cell. 
	        * @return the value of this cell
	        */
	        public Object getCellEditorValue() {
	            return value;
	        }

	       /**
	        * Sets the value of this cell. 
	        * @param value the new value of this cell
	        */
	    	public void setValue(Object value) { 
		    this.value = value; 
		}

	       /**
	        * Returns true if <code>anEvent</code> is <b>not</b> a
	        * <code>MouseEvent</code>.  Otherwise, it returns true
	        * if the necessary number of clicks have occurred, and
	        * returns false otherwise.
	        *
	        * @param   anEvent         the event
	        * @return  true  if cell is ready for editing, false otherwise
	        * @see #setClickCountToStart
	        * @see #shouldSelectCell
	        */
	        public boolean isCellEditable(EventObject anEvent) {
		    if (anEvent instanceof MouseEvent) { 
			return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
		    }
		    return true;
		}
	    	
	       /**
	        * Returns true to indicate that the editing cell may
	        * be selected.
	        *
	        * @param   anEvent         the event
	        * @return  true 
	        * @see #isCellEditable
	        */
	        public boolean shouldSelectCell(EventObject anEvent) { 
	            return true; 
	        }

	       /**
	        * Returns true to indicate that editing has begun.
	        *
	        * @param anEvent          the event
	        */
	        public boolean startCellEditing(EventObject anEvent) {
		    return true;
		}

	       /**
	        * Stops editing and
	        * returns true to indicate that editing has stopped.
	        * This method calls <code>fireEditingStopped</code>.
	        *
	        * @return  true 
	        */
	        public boolean stopCellEditing() { 
		    fireEditingStopped(); 
		    return true;
		}

	       /**
	        * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
	        */
	       public void cancelCellEditing() { 
		   fireEditingCanceled(); 
	       }

	       /**
	        * When an action is performed, editing is ended.
	        * @param e the action event
	        * @see #stopCellEditing
	        */
	        public void actionPerformed(ActionEvent e) {
	        	RadioButtonEditor.this.stopCellEditing();
		}

	       /**
	        * When an item's state changes, editing is ended.
	        * @param e the action event
	        * @see #stopCellEditing
	        */
	        public void itemStateChanged(ItemEvent e) {
	        	RadioButtonEditor.this.stopCellEditing();
		}
	    }
	}
	private class RowRender extends DefaultTableCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		DataTableModel m_dataTableModel = null;
		public RowRender(DataTableModel dataTableModel){
			m_dataTableModel = dataTableModel;
		}
		public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
			int realcol = convertColumnIndexToModel(column);			
			if((m_dataTableModel.getRowStatus()[row]) && (m_dataTableModel.getColumnStatus()[realcol])){
				setBackground(Color.YELLOW);
			}else{
				setBackground(Color.WHITE);
			}
   		 return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	   }
	private class RadioButtonRender extends DefaultTableCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
   		 boolean  selected = false;    		 
   		 if (value instanceof Boolean) {
   			    selected = ((Boolean)value).booleanValue();
   			}
   			else if (value instanceof String) {
   			    selected = value.equals("true");
   			}    		
   		DataTableModel model = (DataTableModel)table.getModel();
   		UIRadioButton rb = new UIRadioButton(model.getRowNames()[row], selected);
   		rb.setToolTipText(model.getRowNames()[row]);
   		 return rb ;
		}
   }

}
