package com.ufida.report.rep.applet;
import static com.sun.java.swing.SwingUtilities2.DRAG_FIX;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.us.bi.query.manager.QuerySrv;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

public class SelectQueryPanel extends nc.ui.pub.beans.UIPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2081007476009155904L;
	/**
	 * @i18n mbirep00018=当前选择
	 */
	private static String[] Static_Column_Names= new String[]{StringResource.getStringResource("mbirep00018"), StringResource.getStringResource("ubiquery0001"),
		StringResource.getStringResource("ubiquery0002"),
		StringResource.getStringResource("ubiquery0003"),
		StringResource.getStringResource("ubids0002"),
		StringResource.getStringResource("ubids0004")};

	private static String[] getColumnNames(){
		return Static_Column_Names;
		
	}
    private class QueryTableModel extends DefaultTableModel {   	
        /**
		 * 
		 */
		private static final long serialVersionUID = -8484920117395987058L;
		private IResTreeObject[] resTreeObjects = null;
        private String  selectedQueryModelID =  null;
        private String[] rowIndex_qmID = null;
        public QueryTableModel(IResTreeObject[] resTreeObjects) {
            super(getColumnNames(), resTreeObjects.length);
            this.resTreeObjects = resTreeObjects;
            if (this.resTreeObjects == null)
                return;
            loadDatas();
        }
        public String getSelectedQueryID(){
        	return selectedQueryModelID;
        }
        
        public QueryModelVO getSelectedQueryModelVO(){
        	if(selectedQueryModelID == null) return null;
        	
        	QueryModelVO vo = null;
			try {
				vo = QuerySrv.getInstance().getQueryModelVO(selectedQueryModelID);
			} catch (Exception e) {			
				AppDebug.debug(e);				
			}        	
        	return vo;
        }
        /** 加载条件数据到表格 */
        private void loadDatas() {  
            //删除原有数据
            int rowSize = this.getRowCount();
            for (int i = rowSize - 1; i >= 0; i--) {
                this.removeRow(i);
            }
           
            //加载新数据         
            String[] columns = getColumnNames();
            rowIndex_qmID = new String[resTreeObjects.length];
            for (int i = 0; i < resTreeObjects.length; i++) {  
            	Object[] row = new Object[columns.length];
            	Object[] values = null;
            	try {
            		values = QuerySrv.getInstance().getRowDatas(resTreeObjects.length, resTreeObjects[i]);
				} catch (UISrvException e) {					
					AppDebug.debug(e);
				}
				if(values == null) continue;
				
				row[0] = new Boolean(false);				    
            	for(int m = 0; m < columns.length; m++) row[m] = values[m];
            	rowIndex_qmID[i] = ResMngToolKit.getVOIDByTreeObjectID(values[0].toString());
            	if(selectedQueryModelID == null) {
					selectedQueryModelID = rowIndex_qmID[i];					
				}
				if(selectedQueryModelID.equals(rowIndex_qmID[i])){
					row[0] = new Boolean(true);					
				}	
//            	row[1] = vo.getQuerycode();
//            	row[2] = vo.getQueryname();
//            	row[3] = vo.getType();
//            	row[4] = vo.getDscode();
//            	row[5] = vo.getNote();                
                this.addRow(row);     
                
            }          
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex != 0) { //首行首列处的单元只能放"where"               
                return false;
            } else {
                return super.isCellEditable(rowIndex, columnIndex);
            }
        }

        /** 先更新过滤对象,后更新表格模型 */
        public void setValueAt(Object aValue, int row, int column) {         
        	if(column == 0){
        		selectedQueryModelID = rowIndex_qmID[row];
        		loadDatas();
        	}            
        }   
    }
    private class RadioButtonEditor extends AbstractCellEditor 
    implements TableCellEditor, TreeCellEditor{    	
    	    /**
		 * 
		 */
		private static final long serialVersionUID = 9074068747303035759L;
			/** The Swing component being edited. */
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
//    	  Modifying
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
//    	  Override the implementations of the superclass, forwarding all methods 
//    	  from the CellEditor interface to our delegate. 
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
//    	  Implementing the TreeCellEditor Interface
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
//    	  Implementing the CellEditor Interface
    	//
    	    /** Implements the <code>TableCellEditor</code> interface. */
    	    public Component getTableCellEditorComponent(JTable table, Object value,
    							 boolean isSelected,
    							 int row, int column) {
    	        delegate.setValue(value);
    		return editorComponent;
    	    }


    	//
//    	  Protected EditorDelegate class
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
    private class RadioButtonRender extends DefaultTableCellRenderer{
    	 /**
		 * 
		 */
		private static final long serialVersionUID = -6064290944283800834L;

		public Component getTableCellRendererComponent(JTable table, Object value,
                 boolean isSelected, boolean hasFocus, int row, int column) {
    		 boolean  selected = false;    		 
    		 if (value instanceof Boolean) {
    			    selected = ((Boolean)value).booleanValue();
    			}
    			else if (value instanceof String) {
    			    selected = value.equals("true");
    			}    		
    		 return new UIRadioButton("", selected);
		}
    }
    
	private JSplitPane jSplitPane = null;
	private JScrollPane TreeScrollPane = null;
	private JTree queryDirTree = null;
	private JScrollPane queryTableScrollPane = null;
	private String strOwerPK = null;
	private JTable jTable = null;
	/**
	 * This is the default constructor
	 */
	public SelectQueryPanel(String strOwerPK) {
		super();
		this.strOwerPK = strOwerPK;
		initialize();
	}

	public QueryModelVO getSelectedQueryModelVO(){
		QueryTableModel model = (QueryTableModel) getJTable().getModel();
		return model.getSelectedQueryModelVO();
    }
	public QueryModelVO[] getSelQueryModels(){
		QueryTableModel model = (QueryTableModel) getJTable().getModel();
		return new QueryModelVO[]{model.getSelectedQueryModelVO()};
    }
	public String getSelectedQueryID(){
		QueryTableModel model = (QueryTableModel) getJTable().getModel();
		return model.getSelectedQueryID();
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		//this.setSize(300, 200);
		this.add(getJSplitPane(), gridBagConstraints);
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT, true, getTreeScrollPane(), getQueryTableScrollPane());
//			jSplitPane.setLeftComponent(getTreeScrollPane());
//			jSplitPane.setRightComponent(getQueryTableScrollPane());
		//	jSplitPane.setDividerSize(2);
			//jSplitPane.setDividerLocation(0.3);
			jSplitPane.invalidate();
			jSplitPane.validate();
			jSplitPane.repaint();
		}
		return jSplitPane;
	}

//	public Object[] getSelQueryModels() {
//	    if(this.getAllQueryList().getSelectedValue() != null) {
//	        return  this.getAllQueryList().getSelectedValues();
//	    }
//	    return null;
//	}
	
	/**
	 * This method initializes TreeScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getTreeScrollPane() {
		if (TreeScrollPane == null) {
			TreeScrollPane = new UIScrollPane();
			TreeScrollPane.setViewportView(getQueryDirTree());
		}
		return TreeScrollPane;
	}

	/**
	 * This method initializes queryDirTree	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private JTree getQueryDirTree() {
		if (queryDirTree == null) {
			queryDirTree = new UITree();				
			queryDirTree.setModel(new DefaultTreeModel(createTreeNode()));			
			initTreeSelectionModel();
		}
		return queryDirTree;
	}

	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode createTreeNode(){
		IResTreeObject rootUserObj = createVitualRootTreeObj();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootUserObj);
		IResTreeObject[] tos = null;	
		try {
			tos = QuerySrv.getInstance().getAllSubDirs(rootUserObj.getID());
		} catch (UISrvException e) {			
			AppDebug.debug(e);
		}
		if(tos == null || tos.length == 0) return root;
		
		Hashtable map = new Hashtable();		
		DefaultMutableTreeNode node = null;		
		DefaultMutableTreeNode parentNode = null;
		
		for(int i = 0; i < tos.length; i++){	
			node = 	new DefaultMutableTreeNode(tos[i]);
			map.put(tos[i].getID(), node);					
		}
		map.put(rootUserObj.getID(), root);
	
		for(int i = 0; i < tos.length; i++){
			node = 	(DefaultMutableTreeNode) map.get(tos[i].getID());	
			if(rootUserObj.getID().equalsIgnoreCase(tos[i].getParentID())){
				root.add(node);
			}else{
				parentNode = (DefaultMutableTreeNode) map.get(tos[i].getParentID());
				parentNode.add(node);	
			}	
		}		
		return root;
	}
	/**
	 * 得到虚根目录
	 * @param strResOwnerPK
	 * @return
	 * @throws ResOperException
	 */
	private IResTreeObject createVitualRootTreeObj(){
		IResTreeObject resTreeObject = new ResTreeObject();
		int nObjectType = IResTreeObject.OBJECT_TYPE_DIR;		
		String strTreeObjID = ResMngToolKit.getHashCodeID(IResMngConsants.VIRTUAL_ROOT_ID,
				nObjectType);
		resTreeObject.setID(strTreeObjID);
		resTreeObject.setParentID("");
		String strRootDirName = "";
		try {
			strRootDirName = QuerySrv.getInstance().getRootDirDisName(strOwerPK, true);
		} catch (UISrvException e) {
			AppDebug.debug(e);
		}
		resTreeObject.setLabel(strRootDirName);
		resTreeObject.setType(nObjectType);
		resTreeObject.setSrcVO(null);//虚根无实际VO
		resTreeObject.setNote(strRootDirName);
		return resTreeObject;
	}
	private void initTreeSelectionModel(){
		TreeSelectionModel model = getQueryDirTree().getSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		model.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				IResTreeObject obj = (IResTreeObject) node.getUserObject();			
				refreshTable(obj.getID());
			}});		
	}
	
	private void refreshTable(String dirID){
		IResTreeObject[] resTreeObjects = null;
		try {
			resTreeObjects = QuerySrv.getInstance().getFilesByDirID(dirID);			
		} catch (Exception e) {		
			AppDebug.debug(e);
		}
		if(resTreeObjects == null)resTreeObjects = new IResTreeObject[0];
		getJTable().setModel(new QueryTableModel(resTreeObjects));
		initFirstColumn();
	}
	
	private void initFirstColumn(){
		TableColumn column = getJTable().getColumnModel().getColumn(0);
		column.setCellEditor(new RadioButtonEditor( new UIRadioButton()));	
		column.setCellRenderer( new RadioButtonRender());
	}
	/**
	 * This method initializes queryTableScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getQueryTableScrollPane() {
		if (queryTableScrollPane == null) {
			JTable table = getJTable();
			queryTableScrollPane = new UIScrollPane(table);
		}
		return queryTableScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getJTable() {
		if (jTable == null) {
			QueryTableModel model = new QueryTableModel(new IResTreeObject[0]);			
			jTable = new nc.ui.pub.beans.UITable(model);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			initFirstColumn();
		//	jTable.doLayout();
			//jTable.getColumn(ColumnNames[2]).sizeWidthToFit();
		}
		return jTable;
	}
}
 