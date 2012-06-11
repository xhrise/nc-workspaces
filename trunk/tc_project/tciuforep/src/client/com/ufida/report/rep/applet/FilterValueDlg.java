/*
 * Created on 2005-7-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufida.report.rep.model.FilterConditionItem;
import com.ufida.report.rep.model.FilterValueDescriptor;
import com.ufida.report.rep.model.IColumnData;
import com.ufida.report.rep.model.IFilterValueConsts;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 筛选条件对话框
 * 
 * @author caijie
 */
public class FilterValueDlg extends UfoDialog {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class ConditionTableModel extends DefaultTableModel implements
            TableModelListener {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private FilterValueDescriptor filterValueDescriptor = null;

        public ConditionTableModel(FilterValueDescriptor filterValueDescriptor) {
            super(IFilterValueConsts.ColumnNames, filterValueDescriptor.getCondList().size());
            this.filterValueDescriptor = filterValueDescriptor;
            if (this.filterValueDescriptor == null)
                return;
            initConditions();

        }

        /** 加载条件数据到表格 */
        private void initConditions() {
			// 加载新数据
			FilterConditionItem item = null;
			for (int i = 0; i < filterValueDescriptor.getCondList().size(); i++) {
				item = (FilterConditionItem) filterValueDescriptor
						.getCondList().get(i);
				super.setValueAt(FilterValueDescriptor.firstLinkSymbol, 0, 0);
				setValueAt(item.getLink(), i, 0);
				setValueAt(item.getField(), i, 1);
				setValueAt(item.getOperation(), i, 2);
				setValueAt(item.getValue(), i, 3);
				// 如果别名不同以field为准
				if (item.getField() instanceof DefaultReportField) {
					DefaultReportField rf = (DefaultReportField) item
							.getField();
					if (!rf.getFldalias().equals(item.getAlias())) {
						item.setAlias(rf.getFldalias());
					}
				}
				setValueAt(item.getAlias(), i, 4);
			}
			super.addTableModelListener(this);
		}

        /** 首行首列处的单元只能放"where" */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if ((rowIndex == 0) && (columnIndex == 0)) { // 首行首列处的单元只能放"where"
                this.setValueAt(FilterValueDescriptor.firstLinkSymbol, 0, 0);
                return false;
            } else {
                return super.isCellEditable(rowIndex, columnIndex);
            }
        }

        /** 先更新过滤对象,后更新表格模型 */
        public void setValueAt(Object aValue, int row, int column) {
			super.setValueAt(FilterValueDescriptor.firstLinkSymbol, 0, 0);
			FilterConditionItem conditionItem = getConditionItem(row);
			if (conditionItem != null) {
				if (aValue == null) {
					if (column == 0) {
						conditionItem.setLink(null);
					}
					;
					if (column == 1) {
						conditionItem.setField(null);
					}
					;
					if (column == 2) {
						conditionItem.setOperation(null);
					}
					;
					if (column == 3) {
						conditionItem.setValue(null);
					}
					;
					if (column == 4) {
						conditionItem.setAlias(null);
					}
					;
				} else if (aValue instanceof String) { //
					String value = (String) aValue;
					if (column == 0) {
						if (row == 0) {
							super
									.setValueAt(
											FilterValueDescriptor.firstLinkSymbol,
											0, 0);
							return;
						} else {
							conditionItem.setLink(value);
						}
					}
					if (column == 2) {
						conditionItem.setOperation(value);
					}
					;
					if (column == 3) {
						conditionItem.setValue(value);
					}
					;
					if (column == 4) {
						conditionItem.setAlias(value);
					}
					;
				}
				if (aValue instanceof IColumnData) {
					IColumnData vo = (IColumnData) aValue;
					if (column == 1) {
						conditionItem.setField(vo);
						if (conditionItem.getAlias() == null
								|| conditionItem.getAlias().trim().length() == 0) {
							String alias = vo.getID();
							if (vo instanceof IColumnData) {
								alias = ((IColumnData) vo).getDBColumn();
								ArrayList<String> list = new ArrayList<String>();
								filterValueDescriptor.getCondList()
										.trimToSize();
								for (int i = 0; i < filterValueDescriptor
										.getCondList().size(); i++) {
									FilterConditionItem item = (FilterConditionItem) filterValueDescriptor
											.getCondList().get(i);
									if ((item.getAlias() != null) && (i != row))
										list.add(item.getAlias());
								}
								while (list.contains(alias)) {
									alias = alias + row;
								}
							}
							conditionItem.setAlias(alias);

						}
						super.setValueAt(conditionItem.getAlias(), row, 4);
					}
					;
				}
			}
			super.setValueAt(aValue, row, column);
		}

        /** 条件List与表格模型中的行一一对应 */
        private FilterConditionItem getConditionItem(int row) {
            if (row>=filterValueDescriptor.getCondList().size()||filterValueDescriptor.getCondList().get(row) == null) {
            	return null;
            }
            return (FilterConditionItem) filterValueDescriptor.getCondList().get(row);
        }

        /***********************************************************************
		 * 模型的任何变化都将导致重新装载数据
		 */
        public void tableChanged(TableModelEvent e) {
			if (e != null) {
				if ((e.getType() == TableModelEvent.UPDATE)
				// && (e.getColumn() == 4)
				) {
					int selectedRow=e.getFirstRow();
					
				} else if (e.getType() == TableModelEvent.INSERT) {
					FilterConditionItem item = new FilterConditionItem();
					int inserRow=e.getFirstRow();
	                super.setValueAt(FilterValueDescriptor.firstLinkSymbol,0, 0);
	                setValueAt(item.getLink(), inserRow, 0);
	                setValueAt(item.getField(), inserRow, 1);
	                setValueAt(item.getOperation(), inserRow, 2);
	                setValueAt(item.getValue(), inserRow, 3);
	                //如果别名不同以field为准
	                if(item.getField() instanceof DefaultReportField){
	                	DefaultReportField rf=(DefaultReportField)item.getField();
	                	if(!rf.getFldalias().equals(item.getAlias())){
	                		item.setAlias(rf.getFldalias());
	                	}
	                }
	                setValueAt(item.getAlias(), inserRow, 4);
	                getFilterConditionDescriptor().getCondList().add(item);
				} else if (e.getType() == TableModelEvent.DELETE) {
                     int selectedRow=e.getFirstRow();
					 getFilterConditionDescriptor().getCondList().remove(
	                            selectedRow);

				}

			}
		}

        /**
		 * @return Returns the filterConditionDescriptor.
		 */
        public FilterValueDescriptor getFilterConditionDescriptor() {
            return filterValueDescriptor;
        }

    }

    private class ConditionTable extends nc.ui.pub.beans.UITable {
    	
        private ConditionTable(FilterValueDescriptor filterConditionDescriptor) {
            super(new ConditionTableModel(filterConditionDescriptor));
            init();
        }

        private void init() {
            this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            initLinkColumn();
            initFieldColumn();
            initOperationColumn();
            initValueColumn();
            initAliasColumn();
                       //adjustColumnWidth();
        }
        private JComboBox getFieldCombo(){
        	return new UIComboBox(fields);
        }

		private void initLinkColumn() {
            TableColumn column = this.getColumn(IFilterValueConsts.ColumnNames[0]);
            column.setCellEditor(new DefaultCellEditor(new UIComboBox(
                    FilterConditionItem.Filter_Link)));
        }

        private void initFieldColumn() {
            TableColumn column = this.getColumn(IFilterValueConsts.ColumnNames[1]);
            JComboBox fieldCombo=getFieldCombo();
            
            column.setCellEditor(new DefaultCellEditor(fieldCombo));
        }

        private void initOperationColumn() {
            TableColumn column = this.getColumn(IFilterValueConsts.ColumnNames[2]);
            column.setCellEditor(new DefaultCellEditor(new UIComboBox(
            		FilterConditionItem.Filter_Operation)));
        }
        private void initValueColumn() {
            TableColumn column = this.getColumn(IFilterValueConsts.ColumnNames[3]);
            PageDimRelativeCellEditor cellEditor=new PageDimRelativeCellEditor(1,pageDimFields);
            this.getModel().addTableModelListener(cellEditor);
            column.setCellEditor(cellEditor);
        }
        private void initAliasColumn() {
            TableColumn column = this.getColumn(IFilterValueConsts.ColumnNames[4]);
            column.setCellEditor(new DefaultCellEditor(new JTextField()));
        }
        private void adjustColumnWidth() {
            TableColumn column;
            try {
                column = this.getColumn(IFilterValueConsts.ColumnNames[0]);
                column.setMinWidth(getPreferredWidthForColumn(column));
            } catch (Exception e) {
                // TODO Auto-generated catch block
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
            }

            try {
                column = this.getColumn(IFilterValueConsts.ColumnNames[1]);
                column.setMinWidth(getPreferredWidthForColumn(column));
            } catch (Exception e) {
                // TODO Auto-generated catch block
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
            }

            try {
                column = this.getColumn(IFilterValueConsts.ColumnNames[2]);
                column.setMinWidth(getPreferredWidthForColumn(column));
            } catch (Exception e) {
                // TODO Auto-generated catch block
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
            }

            this.sizeColumnsToFit(0);
        }

        private int getPreferredWidthForColumn(TableColumn col) {
            int columnHeaderWidth = 0;
            TableCellRenderer render = col.getHeaderRenderer();
            if (render != null) {
                Component comp = render.getTableCellRendererComponent(this, col
                        .getHeaderValue(), false, false, 0, 0);
                columnHeaderWidth = comp.getPreferredSize().width;
            }

            int cw = widestCellColumn(col);
            return columnHeaderWidth > cw ? columnHeaderWidth : cw;
        }

        private int widestCellColumn(TableColumn col) {
            int c = col.getModelIndex(), width = 0, maxw = 0;
            TableCellRenderer render;
            Component comp;
            for (int r = 0; r < this.getRowCount(); ++r) {
                render = this.getCellRenderer(r, c);
                comp = render.getTableCellRendererComponent(this, this
                        .getValueAt(r, c), false, false, 0, 0);
                width = comp.getPreferredSize().width;
                maxw = width > maxw ? width : maxw;
            }
            return maxw;
        }
    }

    private javax.swing.JPanel jContentPane = null;

    private JCheckBox filterCondCB = null;

    private JTable conditionTable = null;

    private JScrollPane jScrollPane = null;

    private JButton addCond = null;

    private JButton deleteCond = null;

    private JButton OKBtn = null;

    private JButton cancelBtn = null;

    private FilterValueDescriptor filterValueDescriptor = null;

    private IColumnData[] fields = null;
    
    private AdhocPageDimField[] pageDimFields=null;

    /**
     * This is the default constructor
     */
    public FilterValueDlg(Container parent,AdhocModel adhocModel,
            FilterValueDescriptor filterValueDescriptor, IColumnData[] fields) {
        super(parent);
        if (fields == null || fields.length == 0)
        	throw new IllegalArgumentException();
        this.filterValueDescriptor = filterValueDescriptor;
        this.fields = fields;
        if(fields!=null&&fields.length>0){
        	  pageDimFields=new AdhocPageDimField[fields.length];
        	  AdhocQueryModel query=null;
        	 for(int i=0;i<fields.length;i++){
        		 if(fields[i] instanceof DefaultReportField){
        			 pageDimFields[i]=new AdhocPageDimField(null, (IMetaData)fields[i]);
        			 query=adhocModel.getDataCenter().getQueryByID(((DefaultReportField)fields[i]).getQueryID());
        			 pageDimFields[i].setQueryDef(query);
        		 }
        		
        	 }
        }
       
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     * @i18n miufo1000807=筛选条件
     */
    private void initialize() {
        this.setSize(607, 416);
        this.setTitle(StringResource.getStringResource("miufo1000807"));
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new UIPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getFilterCondCB(), null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getAddCond(), null);
            jContentPane.add(getDeleteCond(), null);
            jContentPane.add(getOKBtn(), null);
            jContentPane.add(getCancelBtn(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes filterCondCB
     * 
     * @return javax.swing.JCheckBox
     * @i18n mbirep00012=启用筛选条件
     */
    public JCheckBox getFilterCondCB() {
        if (filterCondCB == null) {
            filterCondCB = new UICheckBox();
            filterCondCB.setBounds(10, 10, 152, 25);
            filterCondCB.setText(StringResource.getStringResource("mbirep00012"));
            boolean isEnable = (filterValueDescriptor != null && filterValueDescriptor.isEnabled());
            filterCondCB.setSelected(isEnable);
            setTableAreaStatus(filterCondCB.isSelected());
            filterCondCB.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e == null)
                        return;
                    setTableAreaStatus(filterCondCB.isSelected());
                }
            });
        }
        return filterCondCB;
    }

    /**
     * This method initializes conditionTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getConditionTable() {
        if (conditionTable == null) {
            conditionTable = new ConditionTable(this
                    .getFilterConditionDescriptor());
        }
        return conditionTable;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new UIScrollPane();
            jScrollPane.setBounds(10, 36, 507, 305);
            jScrollPane.setViewportView(getConditionTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes addCond
     * 
     * @return javax.swing.JButton
     * @i18n miufo1003165=增加
     */
    private JButton getAddCond() {
        if (addCond == null) {
            addCond = new nc.ui.pub.beans.UIButton();
            addCond.setBounds(524, 89, 75, 22);
            addCond.setText(StringResource.getStringResource("miufo1003165"));
            addCond.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                  终止编辑态
					 if (getConditionTable().getCellEditor() != null)
					     getConditionTable().getCellEditor().stopCellEditing();
                    ConditionTableModel model = (ConditionTableModel) getConditionTable()
                            .getModel();
                    model.addRow((Object[]) null);
                }
            });
        }
        return addCond;
    }

    /**
     * This method initializes deleteCond
     * 
     * @return javax.swing.JButton
     * @i18n ubiquery0110=删除
     */
    private JButton getDeleteCond() {
        if (deleteCond == null) {
            deleteCond = new nc.ui.pub.beans.UIButton();
            deleteCond.setBounds(524, 128, 75, 22);
            deleteCond.setText(StringResource.getStringResource("ubiquery0110"));
            deleteCond.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {                   
					 //终止编辑态
					 if (getConditionTable().getCellEditor() != null)
					     getConditionTable().getCellEditor().stopCellEditing();

                    int selectedRow = getConditionTable().getSelectedRow();
                    if (selectedRow == -1)
                        return;
                    ConditionTableModel model = (ConditionTableModel) getConditionTable()
                            .getModel();
                    model.removeRow(selectedRow);

                }
            });
        }
        return deleteCond;
    }

    /**
     * This method initializes OKBtn
     * 
     * @return javax.swing.JButton
     * @i18n miufo1003314=确定
     */
private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setBounds(226, 358, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufo1003314"));
			OKBtn.addActionListener(new ActionListener() {
			    /**
				 * @i18n uimultical00001=非法的条件
				 */
			    public void actionPerformed(ActionEvent e) {
			        if(getFilterValueDescriptor() != null) {
			            try {
//			              终止编辑态
							 if (getConditionTable().getCellEditor() != null)
							     getConditionTable().getCellEditor().stopCellEditing();
			                String[] errors = getFilterValueDescriptor().checkValid();       
			                if((errors != null) && (errors.length > 0)){
			                	String error = "";
			                	for(int i = 0; i < errors.length; i++){
			                		error += "  " + errors[i];
			                	}
			                	JOptionPane.showMessageDialog(getJContentPane(), error, StringResource.getStringResource("uimultical00001"), JOptionPane.ERROR_MESSAGE);
			                	return;
			                }
			            } catch (Exception e1) {
			                JOptionPane.showMessageDialog(getJContentPane(), e1.getMessage(), StringResource.getStringResource("uimultical00001"), JOptionPane.ERROR_MESSAGE);
			                return;
			            }
			        }
			        
		            setResult(UfoDialog.ID_OK);
		            close();
			        
			    }
			    });
		}
		return OKBtn;
	}
    /**
     * This method initializes cancelBtn
     * 
     * @return javax.swing.JButton
     * @i18n miufo1003315=取消
     */
    private JButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new nc.ui.pub.beans.UIButton();
            cancelBtn.setBounds(307, 358, 75, 22);
            cancelBtn.setText(StringResource.getStringResource("miufo1003315"));
            cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    终止编辑态
					 if (getConditionTable().getCellEditor() != null)
					     getConditionTable().getCellEditor().stopCellEditing();
                    setResult(UfoDialog.ID_CANCEL);
                    close();
                }
            });
        }
        return cancelBtn;
    }

    private FilterValueDescriptor getFilterConditionDescriptor() {
        if (filterValueDescriptor == null) {
            filterValueDescriptor = new FilterValueDescriptor();
        }
        return filterValueDescriptor;
    }

    private void setTableAreaStatus(boolean enable) {
        this.getAddCond().setEnabled(enable);
        this.getDeleteCond().setEnabled(enable);
        this.getJScrollPane().setEnabled(enable);
        this.getConditionTable().setEnabled(enable);
        this.getConditionTable().setCellSelectionEnabled(enable);
        if (enable) {
            this.getConditionTable().setBackground(Color.white);
        } else {
            this.getConditionTable().setBackground(
                    this.getAddCond().getBackground());
        }
    }

    /***************************************************************************
     * 返回最终结果
     * 
     * @return
     */
    public FilterValueDescriptor getFilterValueDescriptor() {
    	if(filterValueDescriptor != null){
    		filterValueDescriptor.setIsEnabled(getFilterCondCB().isSelected());
    	}
        return filterValueDescriptor;

    }

    public static void main(String[] args) {
//        SelectQueryModelDlg dlg = new SelectQueryModelDlg(null);
//        dlg.setLocationRelativeTo(null);
//        dlg.show();
//        if ((dlg.getResult() == UfoDialog.ID_OK)
//                && (dlg.getQueryModel() != null)) {
//            MetaDataVO[] fileds = QueryModelSrv.getDimFlds(dlg.getQueryModel()
//                    .getID());
//            if (fileds != null) {
//                FilterValueDlg fd = new FilterValueDlg(null, null,
//                        BaseReportUtil.convertToDefalutReportField(fileds));
//                fd.setLocationRelativeTo(null);
//                fd.show();
//            }
//
//        }

    }
} //  @jve:decl-index=0:visual-constraint="10,10"
 