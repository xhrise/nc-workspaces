package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;

import com.borland.dx.dataset.Variant;
import com.ufida.dataset.DataSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.CrossFieldSetTableModel;
import com.ufida.report.crosstable.CrossTableSet;
import com.ufida.report.crosstable.FixField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.IntegerDocument;
import com.ufsoft.report.util.UfoPublic;


public class AnaFixFieldSetDlg extends UfoDialog implements ActionListener{
	private JPanel jCmdPanel = null;
	private JPanel jFieldSetPane = null;
	private JLabel jTableTitle=null;
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
	private JButton m_btnListToTop = null;
	private JButton m_btnListToBottom = null;
	
	//可移动和排序的tablemodel
	private CrossFieldSetTableModel tableModel;
	//所选择的纬度
	private AnaRepField selectedField;
	AreaDataModel dataModel;
	//该纬度的所有值
	private FixField[] fields=null;
	
	private ArrayList<FixField> oldSet=null;
	private ArrayList<FixField> oldAll=null;
	private int oldSortType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo00298=成员设置
	 */
	public AnaFixFieldSetDlg(Container parent, AreaDataModel areaModel,AnaRepField selectedField) {
		super(parent,StringResource.getStringResource("miufo00298"), true);
		this.dataModel=areaModel;
		this.selectedField=selectedField;
		setSize(400,500);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getFieldSetPanel(), java.awt.BorderLayout.CENTER);
		getContentPane().add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
		initPanel();
	}
	
	
	public AnaRepField getSelectedField() {
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
		   rbNoOrder=createRadioButton(StringResource.getStringResource("miufo1001304"),this);
		   sortgroup.add(rbNoOrder);
		   bottompanel.add(rbNoOrder);
		   rbAsceding=createRadioButton(StringResource.getStringResource("miufo1001305"),this);
		   sortgroup.add(rbAsceding);
		   bottompanel.add(rbAsceding);
		   rbDesceding=createRadioButton(StringResource.getStringResource("miufo1001306"),this);
		   sortgroup.add(rbDesceding);
		   bottompanel.add(rbDesceding);
		   jFieldSetPane.add(bottompanel,BorderLayout.SOUTH);
		   bottompanel.setVisible(false);
	   }
	   return jFieldSetPane;
   }
   
   /**
 * @i18n miufo00308=纬度成员设置列表
 * @i18n miufo00326=纬度固定成员列表
 */
private void initPanel(){
	    this.oldSet = getSelectedField().getAllFixValues();
		this.oldAll = getSelectedField().getDimInfo().getDimValueAndNames();
		if (getSelectedField().getDimInfo() != null
				&& getSelectedField().getDimInfo().isFix()) {
			rbFixedField.setSelected(true);
			this.getTableTitle().setText(
					StringResource.getStringResource("miufo00326"));
			if (this.oldSet != null) {
				for (int i = 0; i < oldSet.size(); i++) {
					getTableModel().addToTable(oldSet.get(i),
							getFieldListTable());
				}
			}

		} else {
			rbDefaultField.setSelected(true);
			this.getTableTitle().setText(
					StringResource.getStringResource("miufo00308"));
			if (this.oldAll != null) {
				for (int i = 0; i < oldAll.size(); i++) {
					getTableModel().addToTable(oldAll.get(i),
							getFieldListTable());
				}
			}
		}
	   
	    getBtnLoad().setEnabled(rbFixedField.isSelected());
		getBtnUp().setEnabled(rbFixedField.isSelected());
		getBtnDown().setEnabled(rbFixedField.isSelected());
		getBtnTop().setEnabled(rbFixedField.isSelected());
		getBtnBottom().setEnabled(rbFixedField.isSelected());
		
	   int orderType=getSelectedField().getOrderType();
	   oldSortType=orderType;
	   switch(orderType){
	     case CrossTableSet.ORDER_TYPE_NONE:
	    	  rbNoOrder.setSelected(true);
	    	  break;
	     case CrossTableSet.ORDER_TYPE_ASCEDING:
	    	  rbAsceding.setSelected(true);
	    	  break;
	     case CrossTableSet.ORDER_TYPE_DESCENDING:
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
			jFieldListPanel.add(getTableTitle(), BorderLayout.NORTH);
			jFieldListPanel.add(getFieldScrollPanel(), BorderLayout.CENTER);
			jFieldListPanel.add(getFieldOperPanel(), BorderLayout.EAST);
			
		}
		return jFieldListPanel;
	}
   private JPanel getFieldOperPanel() {
		if (jFieldOprPanel == null) {
			jFieldOprPanel = new JPanel();
			jFieldOprPanel.setLayout(null);
			jFieldOprPanel.setSize(70, 300);
			jFieldOprPanel.setPreferredSize(new Dimension(70, 300));
			jFieldOprPanel.add(getBtnLoad());
			jFieldOprPanel.add(getBtnAdd());
			jFieldOprPanel.add(getBtnRemove());
			jFieldOprPanel.add(getBtnUp());
			jFieldOprPanel.add(getBtnDown());
			jFieldOprPanel.add(getBtnTop());
			jFieldOprPanel.add(getBtnBottom());
		}
		return jFieldOprPanel;
	}
   
   private JLabel getTableTitle(){
	   if(jTableTitle==null){
		   jTableTitle=new UILabel();
	   }
	   return jTableTitle;
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
		   //排序
		   SortButtonRenderer renderer = new SortButtonRenderer();
		   TableColumnModel columnModel = fieldListTable.getColumnModel();
		   for(int i=0;i<columnModel.getColumnCount();i++){
			   columnModel.getColumn(i).setHeaderRenderer(renderer);
		   }
		   JTableHeader header=fieldListTable.getTableHeader();
		   header.addMouseListener(new HeaderListener(header,renderer));
		   //编辑
		   TableColumn column=columnModel.getColumn(0);
		   Class columnClass=fieldListTable.getColumnClass(0);
		   if(columnClass.equals(BigDecimal.class)){
			   TableCellEditor cellEditor=fieldListTable.getDefaultEditor(columnClass);
			   if(cellEditor instanceof DefaultCellEditor){
				   ((JTextField)((DefaultCellEditor)cellEditor).getComponent()).setDocument(new IntegerDocument());
			   }
			  
		   }
		}
	   return fieldListTable;
   }
   
   /**
 * @i18n uiufo10004=成员
 * @i18n miufo00300=显示名称
 */
private CrossFieldSetTableModel getTableModel(){
	   if(tableModel==null){
		   Class[] cs=new Class[2];
		   if (selectedField.getFieldDataType() == Variant.INT) {
				cs[0] = BigDecimal.class;
			} else if (selectedField.getFieldDataType() == Variant.BIGDECIMAL) {
				cs[0] = BigDecimal.class;
			} else {
				cs[0] = String.class;
			}
		   cs[1]=String.class;
		   tableModel=new CrossFieldSetTableModel(new String[]{StringResource.getStringResource("uiufo10004"), StringResource.getStringResource("miufo00300")},cs);
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
			m_btnListUp = createButton(StringResource.getStringResource("miufoweb0023"),this);// miufo1001650=向上
			m_btnListUp.setBounds(5, 200, 60, 22);
			
		}
		return m_btnListUp;
	}
    
	private JButton getBtnDown() {
		if (m_btnListDown == null) {
			m_btnListDown = createButton(StringResource.getStringResource("miufoweb0024"),this);// miufo1001648=向下
			m_btnListDown.setBounds(5, 240, 60, 22);
		}
		return m_btnListDown;
	}
	/**
	 * @i18n iufobi00001=置顶
	 */
	private JButton getBtnTop() {
		if (m_btnListToTop == null) {
			m_btnListToTop = createButton(StringResource.getStringResource("iufobi00001"), this);// miufo1001650=向上
			m_btnListToTop.setBounds(5, 280, 60, 22);
			
		}
		return m_btnListToTop;
	}
    
	/**
	 * @i18n iufobi00002=置底
	 */
	private JButton getBtnBottom() {
		if (m_btnListToBottom == null) {
			m_btnListToBottom = createButton(StringResource.getStringResource("iufobi00002"), this);// miufo1001648=向下
			m_btnListToBottom.setBounds(5, 320, 60, 22);
		}
		return m_btnListToBottom;
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
   
/**
 * @i18n miufo00308=纬度成员设置列表
 * @i18n miufo00326=纬度固定成员列表
 * @i18n iufobi00003=固定成员为空,将按默认成员设置
 */
public void actionPerformed(ActionEvent e) {
	 
	if(getFieldListTable().getCellEditor()!=null){
		getFieldListTable().getCellEditor().stopCellEditing();
	}
	
	if(e.getSource()==rbDefaultField||e.getSource()==rbFixedField){
		getBtnLoad().setEnabled(rbFixedField.isSelected());
		getBtnUp().setEnabled(rbFixedField.isSelected());
		getBtnDown().setEnabled(rbFixedField.isSelected());
		getBtnTop().setEnabled(rbFixedField.isSelected());
		getBtnBottom().setEnabled(rbFixedField.isSelected());
		
		getTableModel().getAll().clear();
		if(rbDefaultField.isSelected()){
			if (this.oldAll != null) {
				for (int i = 0; i < oldAll.size(); i++) {
					getTableModel().addToTable(oldAll.get(i),
							getFieldListTable());
				}
			}
			
			this.getTableTitle().setText(StringResource.getStringResource("miufo00308"));
		}
		if(rbFixedField.isSelected()){
			if (this.oldSet != null) {
				for (int i = 0; i < oldSet.size(); i++) {
					getTableModel().addToTable(oldSet.get(i),
							getFieldListTable());
				}
			}
			this.getTableTitle().setText(StringResource.getStringResource("miufo00326"));
		}
	}
	
	if(e.getSource()==rbNoOrder||e.getSource()==rbAsceding||e.getSource()==rbDesceding){
		
			getBtnUp().setEnabled(rbNoOrder.isSelected());
			getBtnDown().setEnabled(rbNoOrder.isSelected());
			if(rbAsceding.isSelected()){
//				getTableModel().sortByColumn(CrossHeaderTableModel.COLUMN_NAME, true);
				getSelectedField().setOrderType(CrossTableSet.ORDER_TYPE_ASCEDING);
			}else if(rbDesceding.isSelected()){
//				getTableModel().sortByColumn(CrossHeaderTableModel.COLUMN_NAME, false);
				getSelectedField().setOrderType(CrossTableSet.ORDER_TYPE_DESCENDING);
			}else{
				getSelectedField().setOrderType(CrossTableSet.ORDER_TYPE_NONE);
			}
	}
	if(e.getSource()==getBtnUp()){
		
		getTableModel().moveUp(getFieldListTable());
	}
	if(e.getSource()==getBtnDown()){
		
		getTableModel().moveDown(getFieldListTable());
	}
    if(e.getSource()==getBtnTop()){
		
		getTableModel().moveTOTop(getFieldListTable());
	}
	if(e.getSource()==getBtnBottom()){
		
		getTableModel().moveTOBottom(getFieldListTable());
	}
	if (e.getSource() == getBtnRemove()) {
			int[] rows = getFieldListTable().getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				int iSelIndex = getFieldListTable().getSelectedRow();
				getTableModel().removeFromTable(iSelIndex, getFieldListTable());
			}

		}
	if(e.getSource()==getBtnAdd()){
		
		getTableModel().addToTable(new FixField("",""), getFieldListTable());
	}
	if (e.getSource() == m_btnLoadList) {
		    Object[][] values=null;
		    Object value=null;
			if (fields == null) {
				//保证每次都是新的实例,避免设置固定成员后只能读取设置后的成员
				DataSet dataset=(DataSet)dataModel.getDSTool().getDSDef().clone();
				dataModel.setDSParametersValue(dataset);
				values=dataModel.getDSTool().getDistinctValue(dataset,new String[]{getSelectedField().getField().getFldname()},null);
				if(values!=null){
					fields=new FixField[values.length];
					for(int j=0;j<fields.length;j++){
						value=values[j][0];
						fields[j]=new FixField(value,getSelectedField().getDimInfo().getDispName(value).toString());
					}
				}
			}
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					
					getTableModel().addToTable(fields[i], getFieldListTable());
				}
			}

		}
	if(e.getSource()==btnOK){
		if(rbDefaultField.isSelected()){
			if(getSelectedField().getDimInfo()!=null){
				getSelectedField().getDimInfo().setDimValueAndNames(getTableModel().getFixedField());
				getSelectedField().getDimInfo().setFix(false);
			}
		}
		if(rbFixedField.isSelected()){
			ArrayList<FixField> fiexs=getTableModel().getFixedField();
			if(fiexs==null||fiexs.size()==0){
				UfoPublic.sendWarningMessage(StringResource.getStringResource("iufobi00003"), this);
			}
			if(getSelectedField().getDimInfo()!=null){
				getSelectedField().getDimInfo().setFixValueAndNames(fiexs);
				getSelectedField().getDimInfo().setFix(true);
			}
			
			
		}
		
	}
	if(e.getSource()==btnCancel){
		getTableModel().getAll().clear();
		
	}
	getFieldListTable().repaint();

	}

	class HeaderListener extends MouseAdapter {
		JTableHeader header;
		SortButtonRenderer renderer;

		HeaderListener(JTableHeader header, SortButtonRenderer renderer) {
			this.header = header;
			this.renderer = renderer;
		}

		public void mousePressed(MouseEvent e) {
			int col = header.columnAtPoint(e.getPoint());
			int sortCol = header.getTable().convertColumnIndexToModel(col);
			renderer.setPressedColumn(col);
			renderer.setSelectedColumn(col);
			header.repaint();

			if (header.getTable().isEditing()) {
				header.getTable().getCellEditor().stopCellEditing();
			}

			boolean isAscent;
			if (SortButtonRenderer.DOWN == renderer.getState(col)) {
				isAscent = true;
			} else {
				isAscent = false;
			}
			((CrossFieldSetTableModel) header.getTable().getModel())
					.sortByColumn(sortCol, isAscent);
		}

		public void mouseReleased(MouseEvent e) {
			int col = header.columnAtPoint(e.getPoint());
			renderer.setPressedColumn(-1); // clear
			header.repaint();
		}
	}

	class BlankIcon implements Icon {
		private Color fillColor;
		private int size;

		public BlankIcon() {
			this(null, 11);
		}

		public BlankIcon(Color color, int size) {
			// UIManager.getColor("control")
			// UIManager.getColor("controlShadow")
			fillColor = color;

			this.size = size;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (fillColor != null) {
				g.setColor(fillColor);
				g.drawRect(x, y, size - 1, size - 1);
			}
		}

		public int getIconWidth() {
			return size;
		}

		public int getIconHeight() {
			return size;
		}
	}

	class BevelArrowIcon implements Icon {
		public static final int UP = 0; // direction
		public static final int DOWN = 1;

		private static final int DEFAULT_SIZE = 11;

		private Color edge1;
		private Color edge2;
		private Color fill;
		private int size;
		private int direction;

		public BevelArrowIcon(int direction, boolean isRaisedView,
				boolean isPressedView) {
			if (isRaisedView) {
				if (isPressedView) {
					init(UIManager.getColor("controlLtHighlight"), UIManager
							.getColor("controlDkShadow"), UIManager
							.getColor("controlShadow"), DEFAULT_SIZE, direction);
				} else {
					init(UIManager.getColor("controlHighlight"), UIManager
							.getColor("controlShadow"), UIManager
							.getColor("control"), DEFAULT_SIZE, direction);
				}
			} else {
				if (isPressedView) {
					init(UIManager.getColor("controlDkShadow"), UIManager
							.getColor("controlLtHighlight"), UIManager
							.getColor("controlShadow"), DEFAULT_SIZE, direction);
				} else {
					init(UIManager.getColor("controlShadow"), UIManager
							.getColor("controlHighlight"), UIManager
							.getColor("control"), DEFAULT_SIZE, direction);
				}
			}
		}

		public BevelArrowIcon(Color edge1, Color edge2, Color fill, int size,
				int direction) {
			init(edge1, edge2, fill, size, direction);
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			switch (direction) {
			case DOWN:
				drawDownArrow(g, x, y);
				break;
			case UP:
				drawUpArrow(g, x, y);
				break;
			}
		}

		public int getIconWidth() {
			return size;
		}

		public int getIconHeight() {
			return size;
		}

		private void init(Color edge1, Color edge2, Color fill, int size,
				int direction) {
			this.edge1 = edge1;
			this.edge2 = edge2;
			this.fill = fill;
			this.size = size;
			this.direction = direction;
		}

		private void drawDownArrow(Graphics g, int xo, int yo) {
			g.setColor(edge1);
			g.drawLine(xo, yo, xo + size - 1, yo);
			g.drawLine(xo, yo + 1, xo + size - 3, yo + 1);
			g.setColor(edge2);
			g.drawLine(xo + size - 2, yo + 1, xo + size - 1, yo + 1);
			int x = xo + 1;
			int y = yo + 2;
			int dx = size - 6;
			while (y + 1 < yo + size) {
				g.setColor(edge1);
				g.drawLine(x, y, x + 1, y);
				g.drawLine(x, y + 1, x + 1, y + 1);
				if (0 < dx) {
					g.setColor(fill);
					g.drawLine(x + 2, y, x + 1 + dx, y);
					g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
				}
				g.setColor(edge2);
				g.drawLine(x + dx + 2, y, x + dx + 3, y);
				g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
				x += 1;
				y += 2;
				dx -= 2;
			}
			g.setColor(edge1);
			g.drawLine(xo + (size / 2), yo + size - 1, xo + (size / 2), yo
					+ size - 1);
		}

		private void drawUpArrow(Graphics g, int xo, int yo) {
			g.setColor(edge1);
			int x = xo + (size / 2);
			g.drawLine(x, yo, x, yo);
			x--;
			int y = yo + 1;
			int dx = 0;
			while (y + 3 < yo + size) {
				g.setColor(edge1);
				g.drawLine(x, y, x + 1, y);
				g.drawLine(x, y + 1, x + 1, y + 1);
				if (0 < dx) {
					g.setColor(fill);
					g.drawLine(x + 2, y, x + 1 + dx, y);
					g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
				}
				g.setColor(edge2);
				g.drawLine(x + dx + 2, y, x + dx + 3, y);
				g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
				x -= 1;
				y += 2;
				dx += 2;
			}
			g.setColor(edge1);
			g.drawLine(xo, yo + size - 3, xo + 1, yo + size - 3);
			g.setColor(edge2);
			g.drawLine(xo + 2, yo + size - 2, xo + size - 1, yo + size - 2);
			g.drawLine(xo, yo + size - 1, xo + size, yo + size - 1);
		}
	}

	class SortButtonRenderer extends JButton implements TableCellRenderer {
		public static final int NONE = 0;
		public static final int DOWN = 1;
		public static final int UP = 2;

		int pushedColumn;
		Hashtable state;
		JButton downButton, upButton;

		public SortButtonRenderer() {
			pushedColumn = -1;
			state = new Hashtable();

			setMargin(new Insets(0, 0, 0, 0));
			setHorizontalTextPosition(LEFT);
			setIcon(new BlankIcon());

			// perplexed
			// ArrowIcon(SwingConstants.SOUTH, true)
			// BevelArrowIcon (int direction, boolean isRaisedView, boolean
			// isPressedView)

			downButton = new JButton();
			downButton.setMargin(new Insets(0, 0, 0, 0));
			downButton.setHorizontalTextPosition(LEFT);
			downButton.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false,
					false));
			downButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN,
					false, true));

			upButton = new JButton();
			upButton.setMargin(new Insets(0, 0, 0, 0));
			upButton.setHorizontalTextPosition(LEFT);
			upButton
					.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
			upButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP,
					false, true));

		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JButton button = this;
			Object obj = state.get(new Integer(column));
			if (obj != null) {
				if (((Integer) obj).intValue() == DOWN) {
					button = downButton;
				} else {
					button = upButton;
				}
			}
			button.setText((value == null) ? "" : value.toString());
			boolean isPressed = (column == pushedColumn);
			button.getModel().setPressed(isPressed);
			button.getModel().setArmed(isPressed);
			return button;
		}

		public void setPressedColumn(int col) {
			pushedColumn = col;
		}

		public void setSelectedColumn(int col) {
			if (col < 0)
				return;
			Integer value = null;
			Object obj = state.get(new Integer(col));
			if (obj == null) {
				value = new Integer(DOWN);
			} else {
				if (((Integer) obj).intValue() == DOWN) {
					value = new Integer(UP);
				} else {
					value = new Integer(DOWN);
				}
			}
			state.clear();
			state.put(new Integer(col), value);
		}

		public int getState(int col) {
			int retValue;
			Object obj = state.get(new Integer(col));
			if (obj == null) {
				retValue = NONE;
			} else {
				if (((Integer) obj).intValue() == DOWN) {
					retValue = DOWN;
				} else {
					retValue = UP;
				}
			}
			return retValue;
		}
	}
   
}

   