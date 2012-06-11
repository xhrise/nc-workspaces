package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.ElseField;
import com.ufida.report.anareport.model.FieldCountDef;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.vo.pub.querymodel.QueryConst;
import com.ufsoft.iufo.resource.StringResource;

public class TopNElseDesPanel extends UIPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo1001475=字段名称
	 * @i18n iufobi00089=是否统计
	 * @i18n miufo00300=显示名称
	 */
	private final static String[] COLUMN_NAMES = { StringResource.getStringResource("miufo1001475"), StringResource.getStringResource("iufobi00089"), StringResource.getStringResource("miufo00300") };
    private final static String[] COUNT_TYPES=(new FieldCountDef()).getCountTypeNames();
	private UIButton ivjbtnAdd = null;
	private UIButton ivjbtnDel = null;
	
	private UITablePane ivjTablePn = null;
	private UIPanel ivjPnEast = null;
	
	private TopNMultiEditor cellEditor=null;
	private TopNElseTableModel dataModel=null;
	
	public TopNElseDesPanel(Field[] allFields,ArrayList<ElseField> elseFields){
		cellEditor=new TopNMultiEditor();
		Vector<String> cloumnName=new Vector<String>();
		for(int i=0;i<COLUMN_NAMES.length;i++){
			cloumnName.add(COLUMN_NAMES[i]);
		}
		Vector<ElseField> dataVector=new Vector<ElseField>();
		dataModel=new TopNElseTableModel(dataVector,cloumnName);
		if(elseFields!=null&&elseFields.size()>0){
			dataVector.addAll(elseFields);
		}else{
			initData(allFields);
		}

		initialize();
//		initEvent();
	}
	
	private void initialize() {
			setName("TopNElseDescPanel");
			setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
//			add(getPnEast(), "East");
			add(getTablePn(), "Center");
			getTable().setModel(this.dataModel);
			getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
			getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
			getTable().getColumn(COLUMN_NAMES[0]).setCellEditor(this.cellEditor);
			getTable().getColumn(COLUMN_NAMES[1]).setCellEditor(this.cellEditor);
			getTable().getColumn(COLUMN_NAMES[2]).setCellEditor(this.cellEditor);
	}
	
	private void initData(Field[] allFields) {
		if(allFields!=null){
			ElseField elseField=null;
			for(int i=0;i<allFields.length;i++){
				elseField=new ElseField(allFields[i].getFldname(),allFields[i].getCaption(),null);
				if(allFields[i] instanceof FieldCountDef){
					elseField.setIsCount(true);
				}
				dataModel.getDataVector().add(elseField);
			}
		}
	}
	
	private void initEvent() {
		getbtnAdd().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				stopTableEdit();
				dataModel.getDataVector().add(new ElseField(null,null,null));
				stopTableEdit();
				int curRow =dataModel.getDataVector().size();
				dataModel.fireTableRowsInserted(curRow,curRow+1);
				getTable().getSelectionModel().addSelectionInterval(curRow - 1, curRow - 1);
				getTable().repaint();
			}
		});
		getbtnDel().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				stopTableEdit();
				int row = getTable().getSelectedRow();
				if (row < 0){
					if(getTable().getRowCount()>0){
						row=getTable().getRowCount()-1;
						getTable().getSelectionModel().addSelectionInterval(row, row);
					}else{
						return;
					}
					
				}	
				dataModel.removeRow(row);
				getTable().getSelectionModel().addSelectionInterval(row-1, row-1);
				getTable().repaint();
			}
		});
	}
	public ArrayList<ElseField> getElseFieldList(){
		ArrayList<ElseField> elseList=null;
		ElseField elseField=null;
		if(dataModel.getDataVector().size()>0){
			elseList=new ArrayList<ElseField>();
			for(int i=0;i<dataModel.getDataVector().size();i++){
				elseField=(ElseField)dataModel.getDataVector().get(i);
				if(elseField.getFieldName()!=null&&!elseList.contains(elseField)){
					elseList.add(elseField);
				}
			}
		
		}
		return elseList;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		getTablePn().getTable().setEnabled(enabled);
		getbtnAdd().setEnabled(enabled);
		getbtnDel().setEnabled(enabled);
	}

	public UITable getTable() {
		return getTablePn().getTable();
	}
	
	public void stopTableEdit() {
		if (getTable().getCellEditor() != null) {
			getTable().getCellEditor().stopCellEditing();
		}
	}
	
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			ivjTablePn = new UITablePane();
			ivjTablePn.setPreferredSize(new Dimension(300,200));
			ivjTablePn.setName("TablePn");
		}
		return ivjTablePn;
	}
	
	public UIPanel getPnEast() {
		if (ivjPnEast == null) {
			ivjPnEast = new UIPanel();
			ivjPnEast.setName("PnEast");
//			ivjPnEast.setPreferredSize(new Dimension(160, 0));
			Box box = new Box(BoxLayout.Y_AXIS);

			JPanel addPanel = new UIPanel();
			addPanel.add(getbtnAdd());
			box.add(addPanel);

			JPanel delPanel = new UIPanel();
			delPanel.add(getbtnDel());
			box.add(delPanel);

			ivjPnEast.add(box);

		}
		return ivjPnEast;
	}

	/**
	 * @i18n ubichart00005=增加
	 */
	private UIButton getbtnAdd() {
		if (ivjbtnAdd == null) {
			ivjbtnAdd = new UIButton();
			ivjbtnAdd.setName("btnAdd");
			ivjbtnAdd.setText(StringResource.getStringResource("ubichart00005"));

		}
		return ivjbtnAdd;
	}
	/**
	 * @i18n ubichart00006=删除
	 */
	private UIButton getbtnDel() {
		if (ivjbtnDel == null) {
			ivjbtnDel = new UIButton();
			ivjbtnDel.setName("btnDel");
			ivjbtnDel.setText(StringResource.getStringResource("ubichart00006"));

		}
		return ivjbtnDel;
	}
	
	private class TopNElseTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public TopNElseTableModel(Vector<ElseField> dataVector,Vector<String> cloumNames){
			this.dataVector = dataVector;
	        this.columnIdentifiers = cloumNames; 
	        fireTableStructureChanged();
		}
		/**
		 * @i18n miufo1002256=是
		 * @i18n miufo1002257=否
		 */
		@Override
		public Object getValueAt(int row, int column) {
			ElseField elseField=(ElseField)(getDataVector().get(row));
			if(elseField==null){
				return null;
			}
			switch (column) {
			case 0:
				return elseField.getFieldCaption();
			case 1:
				return elseField.isCount()?StringResource.getStringResource("miufo1002256"):StringResource.getStringResource("miufo1002257");
			case 2:
				if(elseField.isCount()){
					return COUNT_TYPES[elseField.getCountType()];
				}else{
					return elseField.getShowName()==null?"":elseField.getShowName();
				}
			default:
				break;
			}
			return null;
		}

		/**
		 * @i18n miufo1002256=是
		 */
		@Override
		public void setValueAt(Object value, int row, int column) {
			ElseField elseField=(ElseField)getDataVector().get(row);
			if(elseField==null){
				return ;
			}
			switch (column) {
			case 0:
				Field fld = (Field) value;
				if(fld!=null){
					elseField.setFieldName(fld.getFldname());
					elseField.setFieldCaption(fld.getCaption());
				}
				break;
			case 1:
				String isSelected=(String)value;
				if(isSelected!=null){
					if(isSelected.equals(StringResource.getStringResource("miufo1002256"))){
						elseField.setIsCount(true);
					}else{
						elseField.setIsCount(false);
					}
				}
				break;
			case 2:
				if(elseField.isCount()){
					Integer countType=(Integer)value;
					if(countType!=null){
						elseField.setCountType(countType);
					}
					
				}else{
					String showName=(String)value;
					if(showName!=null){
						elseField.setShowName(showName);
					}	
				}
				
				break;
			default:
				break;
			}
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			if(column==0){
				return false;
			}
			return super.isCellEditable(row, column);
		}
		
	}
	
	private class TopNMultiEditor extends AbstractCellEditor implements
			TableCellEditor {
		private static final long serialVersionUID = 1L;
		private final static int COMBO = 0;
		private final static int BOOLEAN = 1;
		private final static int STRING = 2;
		private final static int OTHEREDITOR = 3;
		private DefaultCellEditor[] cellEditors;
		private int flg;

		/**
		 * @i18n miufo1002256=是
		 * @i18n miufo1002257=否
		 */
		public TopNMultiEditor(){
			cellEditors=new DefaultCellEditor[OTHEREDITOR+1];
//			cellEditors[COMBO]=new DefaultCellEditor(new UIComboBox(allFields));
			cellEditors[COMBO]=new DefaultCellEditor(new UITextField());
			cellEditors[BOOLEAN]=new DefaultCellEditor(new UIComboBox(new String[]{StringResource.getStringResource("miufo1002256"), StringResource.getStringResource("miufo1002257")}));
			cellEditors[STRING]  = new DefaultCellEditor(new UITextField());
			cellEditors[OTHEREDITOR]=new DefaultCellEditor(new UIComboBox(COUNT_TYPES));
		}
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			flg=column;
			ElseField elseField=(ElseField)dataModel.getDataVector().get(row);
			if(column==STRING&&elseField!=null&&elseField.isCount()){
				flg=OTHEREDITOR;
				return cellEditors[OTHEREDITOR].getTableCellEditorComponent(table, value, isSelected, row, column);
			}
			return cellEditors[column].getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		public Object getCellEditorValue() {
			switch (flg) {
		      case   COMBO:
                 return cellEditors[COMBO].getCellEditorValue();
		      case BOOLEAN:
		    	 return ((UIComboBox)cellEditors[BOOLEAN].getComponent()).getSelectedItem();
		      case  STRING:
		        return cellEditors[STRING].getCellEditorValue();
		      case OTHEREDITOR:
		    	return ((UIComboBox)cellEditors[OTHEREDITOR].getComponent()).getSelectedIndex();
		      default:         return null;
		    }
		}

	}
}
 