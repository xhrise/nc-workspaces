/*
 * 创建日期 2006-6-9
 */
package com.ufida.report.multidimension.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.report.multidimension.model.AnalyzerFilterVO;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.MultiDimFilterCondItem;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * @author ljhua
 */
public class MultiDimFilterCondDlg extends UfoDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final  String[] ColumnNames = { "uibimultical001"/**左操作数*/, "uibimultical002"/**操作符*/, 
    		"uibimultical003"/**右操作符*/, "uibimultical004"/**值*/,"uibimultical005" /**连接*/};
    public static final String STR_FILTER_CONSTRAIN="uibimultical006";//一个方向只能定义一个过滤条件

    private  String[] getColumnNames(){
    	String[] strNames=new String[ColumnNames.length];
    	for(int i=0,size=ColumnNames.length;i<size;i++){
    		strNames[i]=StringResource.getStringResource(ColumnNames[i]);
    	}
    	return strNames;
    }
    
    private class ConditionTableModel extends DefaultTableModel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<MultiDimFilterCondItem>  conditions = null;

        public ConditionTableModel(ArrayList<MultiDimFilterCondItem> listConds) {
            super(getColumnNames(), listConds==null?0:listConds.size());
            this.conditions = listConds;

        }
    	@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
    		if (columnIndex == 3)
    			return String.class;
    		return Object.class;
    	}
        public int getRowCount() {
        	return conditions==null?0:conditions.size();
        }
        public Object getValueAt(int row, int column) {
        	MultiDimFilterCondItem item=conditions==null?null:(MultiDimFilterCondItem) conditions.get(row);
        	if(item==null)
        		return null;
        	
        	Object obj=null;
        	switch(column){
        		case 0:
        			obj=item.getLeftOperand();
        			break;
        		case 1:
        			obj=item.getOperation();
        			break;
        		case 2:
        			 if(item.getRightType()==MultiDimFilterCondItem.TYPE_VALUE){
        			 	obj=StringResource.getStringResource(MultiDimFilterCondItem.STR_CONSTNUMBER);
                    }
                    else if(item.getRightType()==MultiDimFilterCondItem.TYPE_NULL) {
                    	obj=StringResource.getStringResource(MultiDimFilterCondItem.STR_NULL);
                    }
                    else{
                    	obj=item.getRightOperand();
                    }
        			break;
        		case 3:
        			 if(item.getRightType()==MultiDimFilterCondItem.TYPE_VALUE){
        			 	obj=item.getRightOperand();
                    }
                   
        			break;
        		case 4:
        			obj=item.getLink();
        			break;
        	}
        	return obj;
        }


        /** 首行首列处的单元只能放"where" */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
           if(rowIndex==(getRowCount()-1) && columnIndex==4)
           		return false;
           
           if(columnIndex==3){
           		MultiDimFilterCondItem condition=(MultiDimFilterCondItem) conditions.get(rowIndex);
           		if(condition==null || condition.getRightType()!=MultiDimFilterCondItem.TYPE_VALUE)
           			return false;
           }
           return true;
           	
        }

        /** 先更新过滤对象,后更新表格模型 */
        public void setValueAt(Object aValue, int row, int column) {
        	if(conditions==null)
        		return ;
        	
        	MultiDimFilterCondItem condition=(MultiDimFilterCondItem) conditions.get(row);
        	if(condition==null)
        		return;
        	
        	switch(column){
        		case 0:
        			condition.setLeftOperand((DimMemberCombinationVO) aValue);
        			break;
        		case 1:
        			condition.setOperation((String) aValue);
        			break;
        		case 2:
        			if(aValue instanceof DimMemberCombinationVO){
        				condition.setRightOperand(aValue);
        				condition.setRightType(MultiDimFilterCondItem.TYPE_MEMBER);
        				fireTableCellUpdated(row,3);
        			}else if (aValue instanceof String){
        				if(StringResource.getStringResource(MultiDimFilterCondItem.STR_CONSTNUMBER).equals(aValue)){
        					if(condition.getRightType()!=MultiDimFilterCondItem.TYPE_VALUE){
	        					condition.setRightType(MultiDimFilterCondItem.TYPE_VALUE);
	        					condition.setRightOperand(null);
	        					fireTableCellUpdated(row,3);
        					}
        				}
        				else if(StringResource.getStringResource(MultiDimFilterCondItem.STR_NULL).equals(aValue)){
        					
        					condition.setRightType(MultiDimFilterCondItem.TYPE_NULL);
        					condition.setRightOperand(null);
        					fireTableCellUpdated(row,3);
        				}
        			}
        			break;
        		case 3:
        			if(condition.getRightType()==MultiDimFilterCondItem.TYPE_VALUE){
        				condition.setRightOperand(aValue);
        			}

        			break;
        		case 4:
        			condition.setLink((String) aValue);
        	}
        }
        
    	public void addItem( MultiDimFilterCondItem item , int index){
    		
    		if(index<0)
    			conditions.add(item);
    		else if(index+1<conditions.size())
    			conditions.add(index+1,item);
    		else
    			conditions.add(item);
    		
    		fireTableDataChanged();

    	}
    	public void removeItem(int[] indexs){
    		for(int i=0,size=indexs.length;i<size;i++){
    			if(indexs[i]<0)
    				return;
    			conditions.remove(indexs[i]);
    		}
    		fireTableDataChanged();
    	}


    }

    private class ConditionTable extends nc.ui.pub.beans.UITable {
    	
    	
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;



		private ConditionTable(ArrayList<MultiDimFilterCondItem> listConds) {
            super(new ConditionTableModel(listConds));
            init();
        }

        private void init() {
            this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            
            initLeftOprColumn();
            initOperationColumn();
            initRightOprCol();
            initLinkColumn();

        }

     

        private void initLeftOprColumn() {
            TableColumn column = this.getColumn(StringResource.getStringResource(ColumnNames[0]));
            JComboBox cmbLeft=new UIComboBox(leftOpr);
//            cmbLeft.setRenderer(new ListCellRenderer(){
//				
//			    public Component getListCellRendererComponent(
//					          JList list,
//					          Object value,
//					          int index,
//					         boolean isSelected,
//					          boolean cellHasFocus)
//				{
//			    	JLabel lb=new nc.ui.pub.beans.UILabel();
//			    	lb.setBackground(isSelected ? UIManager.getColor("Tree.selectionBackground") : Color.white);
//			    	lb.setOpaque(true);
//			    	if(value!=null && value instanceof DimMemberCombinationVO){
//			    		DimMemberCombinationVO temp=(DimMemberCombinationVO)value;
//			    		if(temp!=null ){
//					    	lb.setText(MultiDimCalcUtil.getUserName(temp));
//			    		}			    			
//			    	}
//			    	return lb;
//
//				}
//			});
            column.setCellEditor(new DefaultCellEditor(cmbLeft));
           
        }
        private void initOperationColumn() {
            TableColumn column = this.getColumn(StringResource.getStringResource(ColumnNames[1]));
            column.setCellEditor(new DefaultCellEditor(new UIComboBox(
            		MultiDimFilterCondItem.Filter_OP)));
           
        }
        private void initRightOprCol(){
        	TableColumn column = this.getColumn(StringResource.getStringResource(ColumnNames[2]));
        	int iNum=leftOpr==null?0:leftOpr.length;
        	Object[] obj=new Object[2+iNum];
        	obj[0]=StringResource.getStringResource(MultiDimFilterCondItem.STR_CONSTNUMBER);
        	obj[1]=StringResource.getStringResource(MultiDimFilterCondItem.STR_NULL);
        	if(iNum>0){
        		System.arraycopy(leftOpr,0,obj,2,iNum);
        	}
        	
            JComboBox cmbLeft=new UIComboBox(obj);
            column.setCellEditor(new DefaultCellEditor(cmbLeft));
        }



        private void initLinkColumn() {
            TableColumn column = this.getColumn(StringResource.getStringResource(ColumnNames[4]));
            column.setCellEditor(new DefaultCellEditor(new UIComboBox(
            		MultiDimFilterCondItem.Filter_Link)));
        }

    }

    private javax.swing.JPanel jContentPane = null;

    private JTable conditionTable = null;

    private JScrollPane jScrollPane = null;

    private JButton addCond = null;

    private JButton deleteCond = null;

    private JButton OKBtn = null;

    private JButton cancelBtn = null;

    private ArrayList<MultiDimFilterCondItem>  listFilterCond = null;

    private DimMemberCombinationVO[] leftOpr = null;
    private SelDimModel m_seldimModel = null;
    private int  	m_iCombinePos;


    /**
     * This is the default constructor
     */
    public MultiDimFilterCondDlg(Container parent,
            ArrayList<MultiDimFilterCondItem> listConds,int iPos, 
            DimMemberCombinationVO[] leftMember,
            SelDimModel seldimModel) {
        super(parent);
        if(seldimModel==null)
			throw new IllegalArgumentException("seldimModel must be not null");
		m_seldimModel=seldimModel;

        if(listConds==null)
        	this.listFilterCond=new ArrayList<MultiDimFilterCondItem>();
        else
        	this.listFilterCond = listConds;
        this.leftOpr = leftMember;
        m_iCombinePos=iPos;
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(597, 326);
        this.setTitle(StringResource.getStringResource("uibimultical007"));//筛选条件
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
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getAddCond(), null);
            jContentPane.add(getDeleteCond(), null);
            jContentPane.add(getOKBtn(), null);
            jContentPane.add(getCancelBtn(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes conditionTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getConditionTable() {
        if (conditionTable == null) {
            conditionTable = new ConditionTable(getFilterCond());
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
            jScrollPane.setBounds(10, 16, 507, 224);
            jScrollPane.setViewportView(getConditionTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes addCond
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddCond() {
        if (addCond == null) {
            addCond = new nc.ui.pub.beans.UIButton();
            addCond.setBounds(523, 18, 75, 22);
            addCond.setText(StringResource.getStringResource("miufo1000080"));//增加
            addCond.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                  终止编辑态
					 if (getConditionTable().getCellEditor() != null)
					     getConditionTable().getCellEditor().stopCellEditing();
                    ConditionTableModel model = (ConditionTableModel) getConditionTable()
                            .getModel();
                    int index = getConditionTable().getSelectedRow();
                    model.addItem(new MultiDimFilterCondItem(),index);
                }
            });
        }
        return addCond;
    }

    /**
     * This method initializes deleteCond
     * 
     * @return javax.swing.JButton
     */
    private JButton getDeleteCond() {
        if (deleteCond == null) {
            deleteCond = new nc.ui.pub.beans.UIButton();
            deleteCond.setBounds(523, 61, 75, 22);
            deleteCond.setText(StringResource.getStringResource("miufo1000930"));//删除
            deleteCond.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {                   
					 //终止编辑态
					 if (getConditionTable().getCellEditor() != null)
					     getConditionTable().getCellEditor().stopCellEditing();

                    int[] selectedRows = getConditionTable().getSelectedRows();
                    if (selectedRows==null || selectedRows.length==0)
                        return;
                    ConditionTableModel model = (ConditionTableModel) getConditionTable()
                            .getModel();

                    model.removeItem(selectedRows);
                }
            });
        }
        return deleteCond;
    }

    /**
     * This method initializes OKBtn
     * 
     * @return javax.swing.JButton
     */
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setBounds(128, 264, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufopublic246"));//确定
			OKBtn.addActionListener(new ActionListener() {
				/**
				 * @i18n uimultical00001=非法的条件
				 */
				public void actionPerformed(ActionEvent e) {

					//终止编辑态
					if (getConditionTable().getCellEditor() != null)
						getConditionTable().getCellEditor().stopCellEditing();

					String strMsg = checkValid();
					if (strMsg != null) {
						JOptionPane.showMessageDialog(getJContentPane(),
								strMsg, StringResource
										.getStringResource("uimultical00001"), JOptionPane.ERROR_MESSAGE);
						return;
					}

					setResult(UfoDialog.ID_OK);
					close();

				}
			});
		}
		return OKBtn;
	}

	private String checkValid() {
		if (getFilterCond() == null)
			return null;

		StringBuffer strBufMsg = new StringBuffer();
		for (int i = 0, size = getFilterCond().size(); i < size; i++) {
			MultiDimFilterCondItem filterItem = (MultiDimFilterCondItem) getFilterCond()
					.get(i);
			StringBuffer strTemp=new StringBuffer();

			if (filterItem.getLeftOperand() == null) {
				strTemp.append(StringResource.getStringResource("uibimultical008"));//左操作数不应为空
				strTemp.append("\r\n");
			}
			if (filterItem.getOperation() == null) {
				strTemp.append(StringResource.getStringResource("uibimultical009"));//操作符不能为空
				strTemp.append("\r\n");
			}
			
			if(filterItem.getRightType()==MultiDimFilterCondItem.TYPE_NULL){
				if(filterItem.getOperation().equals("<>")==false && filterItem.getOperation().equals("=")==false)
					strTemp.append(StringResource.getStringResource("uibimultical011"));//右操作数为空时，操作符只能为=,<>
			}else{
				if(filterItem.getRightOperand()==null){
					strTemp.append(StringResource.getStringResource("uibimultical010"));//右操作数不应为空
					strTemp.append("\r\n");
				}
			}
			if(strTemp.length()>0){
				strBufMsg.append(StringResource.getStringResource("uibimultical012",new String[]{String.valueOf(i + 1)}));//"第" + (i + 1) + "个过滤条件错误"
				strBufMsg.append("\r\n");
				strBufMsg.append(strTemp);
			}
		}
		if(strBufMsg.length()>0)
			return strBufMsg.toString();
	
		String strFilterCond=AnalyzerFilterVO.getCondByList(getFilterCond());
		MultiDimFilterService filterSrv = new MultiDimFilterService(m_seldimModel , null);
		String strErr=filterSrv.checkFilterCond(strFilterCond,m_iCombinePos);
		if(strErr!=null){
			return strErr;
		}
		
		return null;

	}
	
    /**
     * This method initializes cancelBtn
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new nc.ui.pub.beans.UIButton();
            cancelBtn.setBounds(355, 264, 75, 22);
            cancelBtn.setText(StringResource.getStringResource("miufopublic247"));//"取消"
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


    @SuppressWarnings("unused")
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
    public ArrayList<MultiDimFilterCondItem> getFilterCond(){
    	return listFilterCond;
    }


    public static void main(String[] args) {
        
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
 