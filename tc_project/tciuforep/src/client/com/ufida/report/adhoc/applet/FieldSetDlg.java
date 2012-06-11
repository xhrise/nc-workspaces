package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class FieldSetDlg extends UfoDialog implements ActionListener{
	private JPanel jCmdPanel = null;
	private JPanel jFieldSetPane = null;
	private JPanel jFieldListPanel;
	private JPanel jFieldOprPanel;
	private JScrollPane jScrollPanel = null;
	private JTable fieldListTable = null;
	private JButton btnOK;
	private JButton btnCancel;
	private ButtonGroup setgroup;
	private JRadioButton rbDefaultField;
	private JRadioButton rbFixedField;
	private ButtonGroup sortgroup;
	private JRadioButton rbAsceding;
	private JRadioButton rbDesceding;
	private JRadioButton rbNoOrder;
	private JButton m_btnLoadList = null;
	private JButton m_btnListAdd = null;
	private JButton m_btnListRemove = null;
	private JButton m_btnListUp = null;
	private JButton m_btnListDown = null;
	
	private AdhocModel m_adhocModel = null;
	//可移动和排序的tablemodel
	private CrossHeaderTableModel tableModel;
	//所选择的纬度
	private DefaultReportField selectedField;
	//该纬度的所有值
	private IMember[] fields=null;
	
	private ArrayList<DimMemberVO> oldSet=null;
	private int oldSortType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo00298=成员设置
	 */
	public FieldSetDlg(Container parent,DefaultReportField selectedField,AdhocModel adhocModel) {
		super(parent,StringResource.getStringResource("miufo00298"), true);
		this.m_adhocModel=adhocModel;
		this.selectedField=selectedField;
		setSize(400,500);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getFieldSetPanel(), java.awt.BorderLayout.CENTER);
		getContentPane().add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
		initPanel();
	}
	
	
   public AdhocModel getAdhocModel() {
		return m_adhocModel;
	}


	public DefaultReportField getSelectedField() {
		return selectedField;
	}


private JPanel getCmdPanel(){
	   if(jCmdPanel==null){
		   jCmdPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
		   btnOK=createOkButton();
		   btnOK.addActionListener(this);
		   jCmdPanel.add(btnOK);
		   btnCancel=createCancleButton();
		   btnCancel.addActionListener(this);
		   jCmdPanel.add(btnCancel);
	   }
	   return jCmdPanel;
   }
   
   /**
 * @i18n miufo00299=默认成员
 * @i18n miufo00239=固定成员
 */
private JPanel getFieldSetPanel(){
	   if(jFieldSetPane==null){
		   jFieldSetPane=new UIPanel(new BorderLayout());
		   JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		   setgroup= new ButtonGroup();
		   rbDefaultField=createRadioButton(StringResource.getStringResource("miufo00299"), this);
		   setgroup.add(rbDefaultField);
		   toppanel.add(rbDefaultField);
		   rbFixedField=createRadioButton(StringResource.getStringResource("miufo00239"), this);
		   setgroup.add(rbFixedField);
		   toppanel.add(rbFixedField);
		   
		   
		   jFieldSetPane.add(toppanel,BorderLayout.NORTH);
		   
		   jFieldSetPane.add(getFieldListPanel(),BorderLayout.CENTER);
		   
		   
		   JPanel bottompanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		   sortgroup= new ButtonGroup();
		   rbNoOrder=createRadioButton(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_NONE),this);
		   sortgroup.add(rbNoOrder);
		   bottompanel.add(rbNoOrder);
		   rbAsceding=createRadioButton(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_ASCEDING),this);
		   sortgroup.add(rbAsceding);
		   bottompanel.add(rbAsceding);
		   rbDesceding=createRadioButton(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_DESCENDING),this);
		   sortgroup.add(rbDesceding);
		   bottompanel.add(rbDesceding);
		   jFieldSetPane.add(bottompanel,BorderLayout.SOUTH);
	   }
	   return jFieldSetPane;
   }
   
   private void initPanel(){
	   ArrayList<DimMemberVO> initList=getSelectedField().getFixValues();
	   if(initList==null||initList.size()==0){
		   rbDefaultField.setSelected(true); 
		   
	   }else{
		   rbFixedField.setSelected(true);
		   oldSet=new ArrayList<DimMemberVO>();
		   for (int i = 0; i < initList.size(); i++) {
				getTableModel().addToTable(initList.get(i), getFieldListTable());
				oldSet.add(i, initList.get(i));
			}
	   }
	    getBtnLoad().setEnabled(rbFixedField.isSelected());
		getBtnAdd().setEnabled(rbFixedField.isSelected());
		getBtnRemove().setEnabled(rbFixedField.isSelected());
		getBtnUp().setEnabled(rbFixedField.isSelected());
		getBtnDown().setEnabled(rbFixedField.isSelected());
		
	   int orderType=getSelectedField().getOrderType();
	   oldSortType=orderType;
	   switch(orderType){
	     case IFldCountType.ORDER_TYPE_NONE:
	    	  rbNoOrder.setSelected(true);
	    	  break;
	     case IFldCountType.ORDER_TYPE_ASCEDING:
	    	  rbAsceding.setSelected(true);
	    	  break;
	     case IFldCountType.ORDER_TYPE_DESCENDING:
	    	  rbDesceding.setSelected(true);
	    	  break;
	    default :
	    	 rbNoOrder.setSelected(true);
	   }
   }
   
   private JPanel getFieldListPanel() {
	   
		if (jFieldListPanel == null) {
			jFieldListPanel = new UIPanel();
			jFieldListPanel.setLayout(new BorderLayout());
			jFieldListPanel.add(getFieldScrollPanel(), BorderLayout.CENTER);
			jFieldListPanel.add(getFieldOperPanel(), BorderLayout.EAST);
			
		}
		return jFieldListPanel;
	}
   private JPanel getFieldOperPanel() {
		if (jFieldOprPanel == null) {
			jFieldOprPanel = new JPanel();
			jFieldOprPanel.setLayout(null);
			jFieldOprPanel.setSize(60, 300);
			jFieldOprPanel.setPreferredSize(new Dimension(60, 300));
			jFieldOprPanel.add(getBtnLoad());
			jFieldOprPanel.add(getBtnAdd());
			jFieldOprPanel.add(getBtnRemove());
			jFieldOprPanel.add(getBtnUp());
			jFieldOprPanel.add(getBtnDown());

		}
		return jFieldOprPanel;
	}
   private JScrollPane getFieldScrollPanel(){
	   if(jScrollPanel==null){
		   jScrollPanel = new UIScrollPane();
		   jScrollPanel.setViewportView(getFieldListTable());
	   }
	   return jScrollPanel;
   }
   private JTable getFieldListTable() {
	   if(fieldListTable==null){
		   fieldListTable=new JTable();
		   fieldListTable.setModel(getTableModel());
		   int columns=fieldListTable.getModel().getColumnCount();
		   for(int i=0;i<columns;i++){
			   fieldListTable.getColumn(fieldListTable.getColumnName(i)).setCellEditor(new DefaultCellEditor(new JTextField()));
		   }
	   }
	   return fieldListTable;
   }
   
   /**
 * @i18n uiufo10004=成员
 * @i18n miufo00300=显示名称
 */
private CrossHeaderTableModel getTableModel(){
	   if(tableModel==null){
		   tableModel=new CrossHeaderTableModel(new String[]{StringResource.getStringResource("uiufo10004"), StringResource.getStringResource("miufo00300")});
	   }
	   return tableModel;
   }
   /**
 * @i18n miufo00301=读取数据
 */
private JButton getBtnLoad() {
		if (m_btnLoadList == null) {
			m_btnLoadList =createButton(StringResource.getStringResource("miufo00301"), this);// miufo1000950=添加
			m_btnLoadList.setBounds(5, 40, 60, 22);
		}
		return m_btnLoadList;
	}
   
   private JButton getBtnAdd() {
		if (m_btnListAdd == null) {
			m_btnListAdd =createButton(StringResource.getStringResource("miufo1000950"),this);// miufo1000950=添加
			m_btnListAdd.setBounds(5, 80, 60, 22);
		}
		return m_btnListAdd;
	}

	private JButton getBtnRemove() {
		if (m_btnListRemove == null) {
			m_btnListRemove = createButton(StringResource.getStringResource("miufo1001641"),this);// miufo1001641=删除
			m_btnListRemove.setBounds(5, 120, 60, 22);
		}
		return m_btnListRemove;
	}

	private JButton getBtnUp() {
		if (m_btnListUp == null) {
			m_btnListUp = createButton(StringResource.getStringResource("miufo1001650"),this);// miufo1001650=向上
			m_btnListUp.setBounds(5, 160, 60, 22);
			
		}
		return m_btnListUp;
	}

	private JButton getBtnDown() {
		if (m_btnListDown == null) {
			m_btnListDown = createButton(StringResource.getStringResource("miufo1001648"),this);// miufo1001648=向下
			m_btnListDown.setBounds(5, 200, 60, 22);
		}
		return m_btnListDown;
	}
   private JRadioButton createRadioButton(String s, ActionListener actionlistener)
   {
       JRadioButton jradiobutton = new UIRadioButton(s);
       jradiobutton.addActionListener(actionlistener);
       return jradiobutton;
   }
   
   private JButton createButton(String s, ActionListener actionlistener)
   {
       JButton jbutton = new UIButton(s);
       jbutton.addActionListener(actionlistener);
       return jbutton;
   }
   
public void actionPerformed(ActionEvent e) {
	if(e.getSource()==rbDefaultField||e.getSource()==rbFixedField){
		getBtnLoad().setEnabled(rbFixedField.isSelected());
		getBtnAdd().setEnabled(rbFixedField.isSelected());
		getBtnRemove().setEnabled(rbFixedField.isSelected());
		getBtnUp().setEnabled(rbFixedField.isSelected());
		getBtnDown().setEnabled(rbFixedField.isSelected());
		
		if(rbDefaultField.isSelected()){
		    getTableModel().getAll().clear();
		}
	}
	
	if(e.getSource()==rbNoOrder||e.getSource()==rbAsceding||e.getSource()==rbDesceding){
			getBtnUp().setEnabled(rbNoOrder.isSelected());
			getBtnDown().setEnabled(rbNoOrder.isSelected());
			if(rbAsceding.isSelected()){
//				getTableModel().sortByColumn(CrossHeaderTableModel.COLUMN_NAME, true);
				getSelectedField().setOrderType(IFldCountType.ORDER_TYPE_ASCEDING);
			}else if(rbDesceding.isSelected()){
//				getTableModel().sortByColumn(CrossHeaderTableModel.COLUMN_NAME, false);
				getSelectedField().setOrderType(IFldCountType.ORDER_TYPE_DESCENDING);
			}else{
				getSelectedField().setOrderType(IFldCountType.ORDER_TYPE_NONE);
			}
	}
	if(e.getSource()==getBtnUp()){
		getTableModel().moveUp(getFieldListTable());
	}
	if(e.getSource()==getBtnDown()){
		getTableModel().moveDown(getFieldListTable());
	}
	if(e.getSource()==getBtnRemove()){
		getTableModel().removeFromTable(getFieldListTable().getSelectedRow(), getFieldListTable());
	}
	if(e.getSource()==getBtnAdd()){
		getTableModel().addToTable(new DimMemberVO(), getFieldListTable());
	}
	if (e.getSource() == m_btnLoadList) {

			if (fields == null) {
				AdhocQueryModel query = getAdhocModel().getDataCenter()
						.getCurrQuery();
				fields = query.getExec().getPageDimMembers(getSelectedField());
			}
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					DimMemberVO vo=new DimMemberVO();
					vo.setMemname(fields[i].getName());
					vo.setMemcode(fields[i].getMemcode());
					getTableModel().addToTable(vo, getFieldListTable());
				}
			}

		}
	if(e.getSource()==btnOK){
		getSelectedField().setWrappedValues(getTableModel().getFixedField());
	}
	if(e.getSource()==btnCancel){
		getTableModel().getAll().clear();
		getSelectedField().setWrappedValues(oldSet);
		getSelectedField().setOrderType(oldSortType);
	}
	
}

public static void main(String[] args){
	FieldSetDlg dlg=new FieldSetDlg(null,null,null);
	dlg.show();
}
   
}
 