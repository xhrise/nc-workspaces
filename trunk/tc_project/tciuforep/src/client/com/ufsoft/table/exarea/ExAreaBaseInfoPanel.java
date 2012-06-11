package com.ufsoft.table.exarea;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.IntegerDocument;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import com.ufsoft.report.util.MultiLang;

public class ExAreaBaseInfoPanel extends UIPanel {
    
	private JCheckBox cbFixRowCol;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label_6;
	private JTextField fldName;
	private JLabel label_5;
	private JLabel label_4;
	private JLabel label_1;
	private JComboBox boxFromPoint;
	private JTextField fldFixCol;
	private JLabel lblFixCol;
	private JTextField fldFixRow;
	private JLabel lblFixRow;
	private JPanel paneFix;
	private UIComboBox boxExPoint;
	private UIComboBox boxExRef;
	private JLabel label;
	private JPanel paneExRef;
	private JRadioButton rbExType3;
	private JRadioButton rbExType2;
	private JPanel paneExType;
	private JPanel paneArea2;
	private JPanel paneProp;
	private JTextField fldArea = null;
	private JButton btnFoldUp = null;
	private ButtonGroup groupExType = null;
	
	private ExAreaCell m_exAreaCell = null;

	private ExAreaModel m_exAreaModel = null;
	
	private UfoReport m_report = null;
	private SelectListener m_selectListener = null;
	
	/**
	 * @i18n miufo00089=左上点
	 * @i18n miufo00090=右上点
	 * @i18n miufo00091=左下点
	 * @i18n miufo00092=右下点
	 */
	private final static String[][] Point_Items = new String[][] {
		{ ExAreaCell.EX_POINT_00 + "", MultiLang.getString("miufo00089") },
		{ ExAreaCell.EX_POINT_0X + "", MultiLang.getString("miufo00090") },
		{ ExAreaCell.EX_POINT_0Y + "", MultiLang.getString("miufo00091") },
		{ ExAreaCell.EX_POINT_XY + "", MultiLang.getString("miufo00092") } };
	
	
	public ExAreaBaseInfoPanel(ExAreaModel model, ExAreaCell cell,UfoReport report){
		super();
		this.m_exAreaModel = model;
		this.m_exAreaCell = cell;
		this.m_report=report;
		initialize();
		initData();
	}
	
	private void initialize() {
			setName("UfoDialogContentPane");
			setLayout(null);
			setRequestFocusEnabled(false);
			add(getPaneProp());
			addSelectedListener();
	}
	/**
	 * @return
	 */
	protected JPanel getPaneProp() {
		if (paneProp == null) {
			paneProp = new JPanel();
			paneProp.setLayout(null);
			paneProp.setBounds(0, 0, 486, 357);
			paneProp.add(getPaneArea2());
			paneProp.add(getPaneExType());
			paneProp.add(getPaneExRef());
			paneProp.add(getPaneFix());
		}
		return paneProp;
	}
	/**
	 * @return
	 * @i18n miufo00093=基本信息
	 */
	protected JPanel getPaneArea2() {
		if (paneArea2 == null) {
			paneArea2 = new JPanel();
			paneArea2.setLayout(null);
			paneArea2.setBounds(0, 0, 481, 102);
			paneArea2.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00093"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			paneArea2.add(getFldArea());
			paneArea2.add(getBtnFoldArea());
			paneArea2.add(getLabel_5());
			paneArea2.add(getFldName());
			paneArea2.add(getLabel_6());

		}
		return paneArea2;
	}
	private JTextField getFldArea() {
		if (fldArea == null) {
			try {
				fldArea = new UITextField();
				fldArea.setBounds(132, 63, 272, 20);
				fldArea.setName("JTFArea");
				//      ivjJTFArea.setFont(new java.awt.Font("dialog", 0, 14));
				// user code begin {1}
				//            ivjJTFArea.addActionListener(this);
				//            ivjJTFArea.addFocusListener(this);
				
//				fldArea.setEditable(false);
//				fldArea.addPropertyChangeListener(new PropertyChangeListener(){
//					public void propertyChange(PropertyChangeEvent evt) {
////						if(evt.getSource() == this.fldArea){
////							initExRefs();
////						}
//						
//					}
//					
//				});
			} catch (java.lang.Throwable ivjExc) {
				AppDebug.debug(ivjExc);
			}
		}
		return fldArea;
	}
	/**
	 * 返回 JBFoldArea 特性值。
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	public javax.swing.JButton getBtnFoldArea() {
		if (btnFoldUp == null) {
			try {
				btnFoldUp = new JButton();
				btnFoldUp.setBounds(403, 64, 19, 18);
				btnFoldUp.setName("JBFoldArea");
				btnFoldUp.setText("");
				
//				btnFoldUp.setVisible(false);
				btnFoldUp.setIcon(ResConst
						.getImageIcon("reportcore/up.gif"));
//				btnFoldUp.registerKeyboardAction(this, KeyStroke
//						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
//						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				AppDebug.debug(ivjExc);
			}
		}
		return btnFoldUp;
	}

	public void setParent(final UfoDialog dlg){
		if(dlg!=null)
		getBtnFoldArea().addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Rectangle r = dlg.getBounds();
				if(r.height < 400){
					r.height = 418;
					getBtnFoldArea().setIcon(ResConst
							.getImageIcon("reportcore/up.gif"));
				} else {
					r.height = 185;
					getBtnFoldArea().setIcon(ResConst
							.getImageIcon("reportcore/down.gif"));
				}
				dlg.setBounds(r);
				
			}});
	}
	/**
	 * @return
	 * @i18n miufo00094=扩展方向
	 */
	protected JPanel getPaneExType() {
		if (paneExType == null) {
			paneExType = new JPanel();
			paneExType.setLayout(null);
			paneExType.setBounds(0, 108, 481, 55);
			paneExType.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00094"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			paneExType.add(getRbExType2());
			paneExType.add(getRbExType3());
			
			groupExType = new ButtonGroup();
			//设置按钮组
			groupExType.add(getRbExType2());
			groupExType.add(getRbExType3());

		}
		return paneExType;
	}


	/**
	 * @return
	 * @i18n miufo00095=横向扩展
	 */
	protected JRadioButton getRbExType2() {
		if (rbExType2 == null) {
			rbExType2 = new JRadioButton();
			rbExType2.setText(MultiLang.getString("miufo00095"));
			rbExType2.setSelected(true);
			rbExType2.setBounds(106, 18, 104, 26);
		}
		return rbExType2;
	}

	/**
	 * @return
	 * @i18n miufo00096=纵向扩展
	 */
	protected JRadioButton getRbExType3() {
		if (rbExType3 == null) {
			rbExType3 = new JRadioButton();
			rbExType3.setText(MultiLang.getString("miufo00096"));
			rbExType3.setBounds(280, 18, 104, 26);
		}
		return rbExType3;
	}


	/**
	 * @return
	 * @i18n miufo00097=扩展依赖
	 */
	protected JPanel getPaneExRef() {
		if (paneExRef == null) {
			paneExRef = new JPanel();
			paneExRef.setLayout(null);
			paneExRef.setBounds(0, 244, 481, 67);
			paneExRef.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00097"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			paneExRef.add(getLabel());
			paneExRef.add(getBoxExRef());
			paneExRef.add(getBoxExPoint());
			paneExRef.add(getBoxFromPoint());
			paneExRef.add(getLabel_1());
			paneExRef.add(getLabel_4());
		}
		return paneExRef;
	}

	/**
	 * @return
	 * @i18n miufo00098=依赖区域
	 */
	protected JLabel getLabel() {
		if (label == null) {
			label = new JLabel(MultiLang.getString("miufo00098"));
			label.setBounds(181, 32, 52, 18);
		}
		return label;
	}

	/**
	 * @return
	 */
	protected UIComboBox getBoxExRef() {
		if (boxExRef == null) {
			boxExRef = new UIComboBox();
			boxExRef.setBounds(248, 31, 94, 20);
		
		}
		return boxExRef;
	}
	/**
	 * @return
	 */
	protected UIComboBox getBoxExPoint() {
		if (boxExPoint == null) {
			boxExPoint = new UIComboBox();
			boxExPoint.setBounds(389, 31, 81, 20);
			DefaultConstEnum[] arr = new DefaultConstEnum[Point_Items.length];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = new DefaultConstEnum(Point_Items[i][0], Point_Items[i][1]);
			}
			((UIComboBox) boxExPoint).addItems(arr);
		}
		return boxExPoint;
	}
	/**
	 * @return
	 * @i18n miufo00099=限定区域大小
	 */
	protected JPanel getPaneFix() {
		if (paneFix == null) {
			paneFix = new JPanel();
			paneFix.setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00099"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			paneFix.setLayout(null);
			paneFix.setBounds(-1, 174, 481, 67);
			paneFix.add(getCbFixRowCol());

			paneFix.add(getLblFixRow());
			paneFix.add(getFldFixRow());
			paneFix.add(getLblFixCol());
			paneFix.add(getFldFixCol());
		}
		return paneFix;
	}
	/**
	 * @return
	 * @i18n miufo00100=限定行数：
	 */
	protected JLabel getLblFixRow() {
		if (lblFixRow == null) {
			lblFixRow = new JLabel();
			lblFixRow.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			lblFixRow.setText(MultiLang.getString("miufo00100"));
			lblFixRow.setBounds(128, 28, 82, 18);
		}
		return lblFixRow;
	}
	/**
	 * @return
	 */
	protected JTextField getFldFixRow() {
		if (fldFixRow == null) {
			fldFixRow = new JTextField();
			fldFixRow.setBounds(216, 26, 64, 22);
			fldFixRow.setHorizontalAlignment(SwingConstants.CENTER);
			fldFixRow.setDocument(new IntegerDocument());
		}
		return fldFixRow;
	}
	/**
	 * @return
	 * @i18n miufo00101=限定列数：
	 */
	protected JLabel getLblFixCol() {
		if (lblFixCol == null) {
			lblFixCol = new JLabel();
			lblFixCol.setText(MultiLang.getString("miufo00101"));
			lblFixCol.setBounds(316, 28, 66, 18);
		}
		return lblFixCol;
	}
	/**
	 * @return
	 */
	protected JTextField getFldFixCol() {
		if (fldFixCol == null) {
			fldFixCol = new JTextField();
			fldFixCol.setBounds(388, 26, 66, 22);
			fldFixCol.setHorizontalAlignment(SwingConstants.CENTER);
			fldFixCol.setDocument(new IntegerDocument());
		}
		return fldFixCol;
	}
	/**
	 * @return
	 */
	protected JComboBox getBoxFromPoint() {
		if (boxFromPoint == null) {
			boxFromPoint = new UIComboBox();
			DefaultConstEnum[] arr = new DefaultConstEnum[Point_Items.length];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = new DefaultConstEnum(Point_Items[i][0], Point_Items[i][1]);
			}
			((UIComboBox) boxFromPoint).addItems(arr);
			boxFromPoint.setBounds(79, 31, 81, 27);
		}
		return boxFromPoint;
	}
	/**
	 * @return
	 * @i18n uiuforep00002=的
	 */
	protected JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel();
			label_1.setText(MultiLang.getString("uiuforep00002"));
			label_1.setBounds(360, 32, 23, 18);
		}
		return label_1;
	}
	/**
	 * @return
	 * @i18n miufo00102=本区域的
	 */
	protected JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel();
			label_4.setText(MultiLang.getString("miufo00102"));
			label_4.setBounds(23, 32, 52, 18);
		}
		return label_4;
	}
	/**
	 * @return
	 * @i18n miufo00103=可扩展区名称：
	 */
	protected JLabel getLabel_5() {
		if (label_5 == null) {
			label_5 = new JLabel();
			label_5.setText(MultiLang.getString("miufo00103"));
			label_5.setBounds(28, 30, 98, 18);
		}
		return label_5;
	}
	/**
	 * @return
	 */
	protected JTextField getFldName() {
		if (fldName == null) {
			fldName = new JTextField();
			fldName.setBounds(132, 28, 272, 22);
		}
		return fldName;
	}
	/**
	 * @return
	 * @i18n miufo00104=可扩展区区域：
	 */
	protected JLabel getLabel_6() {
		if (label_6 == null) {
			label_6 = new JLabel();
			label_6.setText(MultiLang.getString("miufo00104"));
			label_6.setBounds(28, 64, 98, 18);
		}
		return label_6;
	}
	
	private void initExRefs() {
		ExAreaCell refCell = null;
		Object obj = getBoxExRef().getSelectedItem();
		 //设置用户已设置项，如果为空，则设置修改前的值。
		if(obj != null && !"".equals(obj)){
			refCell = (ExAreaCell) obj;
		} else {
			if(m_exAreaCell != null){
				refCell = m_exAreaModel.getReferExArea(m_exAreaCell);
			}
		}
		boxExRef.removeAllItems();
//		AreaPosition area = getSettingArea();
//		if(area == null){
//			return;
//		}
		ExAreaCell[] refs = m_exAreaModel.getExAreaCells();//getCanReferCells(area, getExType());
		if (refs == null) {
			return;
		}
		boxExRef.addItem("");
		for (int i = 0; i < refs.length; i++) {
			ExAreaCell cell = refs[i];
			if (cell == m_exAreaCell) {
				continue;
			}
			boxExRef.addItem(cell);
		}
		
		boxExRef.setSelectedItem(refCell);
		
		
	}
	 /**
     * 得到可依赖的区域列表
     * @param area
     * @return
     */
    private ExAreaCell[] getCanReferCells(IArea area, int exMode){
    	if(area == null){
    		return new ExAreaCell[0];
    	}
    	
    	area = m_exAreaModel.genMaxExArea(area, exMode);
    	
    	ArrayList<ExAreaCell> list = new ArrayList<ExAreaCell>();
    	ExAreaCell[] cells = m_exAreaModel.getExAreaCells();
    	for (int i = 0; i < cells.length; i++) {
			ExAreaCell cell = cells[i];
			IArea maxExCell = m_exAreaModel.genMaxExArea(cell.getArea(), cell.getExMode());  //cell.getArea().getExtendArea(10000, Short.MAX_VALUE);//
			//如果与可扩展区域的最大扩展后区域有交叠，即area在此可扩展区域的扩展区中，则可被依赖。
			if(maxExCell.overlap(area)){
				list.add(cell);
			}
		}
    	return list.toArray(new ExAreaCell[list.size()]);
    }
    
    private void addSelectedListener() {
		if (m_selectListener == null) {
			m_selectListener = new SelectListener() {
				public void selectedChanged(SelectEvent e) {
					AreaPosition area = m_report.getCellsModel()
							.getSelectModel().getSelectedArea();
					getFldArea().setText(area.toString());

				}
			};
		}
		m_report.getCellsModel().getSelectModel().addSelectModelListener(
				m_selectListener);
	}
    
    void removeSelectedListener(){
    	if(m_selectListener!=null){
    		m_report.getCellsModel().getSelectModel().removeSelectModelListener(m_selectListener);
    	}
    }
    
    private void initData(){
    	
		initExRefs();
		
		AreaPosition selArea = this.m_report.getCellsModel().getSelectModel().getSelectedArea();
		 
		if(m_exAreaCell == null){
			fldArea.setText(selArea.toString());
			fldName.setText("");//可扩展区" + selArea);
			return;
		}
		//如果区域包含选择区域，或者cell区域与选择区域不重叠，设置原有区域
		if(m_exAreaCell.getArea().contain(selArea) || !m_exAreaCell.getArea().intersection(selArea)){
			fldArea.setText(m_exAreaCell.getArea().toString());
		} else {
			fldArea.setText(selArea.toString());
		}
		
		fldName.setText(m_exAreaCell.getExAreaName());
		fldFixRow.setText(m_exAreaCell.getFixRow() + "");
		fldFixCol.setText(m_exAreaCell.getFixCol() + "");
		cbFixRowCol.setSelected(m_exAreaCell.isFixed());
		cbFixRowCol.getActionListeners()[0].actionPerformed(null);
		
//		if(selArea.overlap(m_exAreaCell.getArea())){
//			fldArea.setText(selArea.toString());
//		} else {
//			fldArea.setText(m_exAreaCell.getArea().toString());
//		}
		
		int exType = m_exAreaCell.getExMode();
		switch (exType) {
		case ExAreaCell.EX_MODE_FIXATION:
			break;
		case ExAreaCell.EX_MODE_X:
			getRbExType2().setSelected(true);
			break;
		case ExAreaCell.EX_MODE_Y:
			getRbExType3().setSelected(true);
			break;
		case ExAreaCell.EX_MODE_XY:
			break;
		default:
			break;
		}
		
		String refPk = m_exAreaCell.getRefAreaPK();
		if(refPk != null){
			ExAreaCell c1 = m_exAreaModel.getExAreaByPK(refPk);
			if(c1 != null){
				getBoxExRef().setSelectedItem(c1);
			}  
		}
		
		int refPoint = m_exAreaCell.getRefPoint();
		int index = refPoint - ExAreaCell.EX_POINT_00;
		if(index >= 0){
			getBoxExPoint().setSelectedIndex(index);
		}
		
		int fromPoint = m_exAreaCell.getFromPoint();
		index = fromPoint - ExAreaCell.EX_POINT_00;
		if(index >= 0){
			getBoxFromPoint().setSelectedIndex(index);
		}
	}
    
	/**
	 * @i18n miufo00105=可扩展区名称重复，请重新输入。
	 * @i18n miufo00106=区域重叠，请重新设置。
	 * @i18n miufo00082=错误提示
	 */
	public boolean saveData(){
		
		String name = fldName.getText();
		if(name == null || name.trim().length() < 1){
//			showmessage("请输入可扩展区名称。");  
////			fldName.requestFocus();
//			return false;
			name = "";
		} else {
			ExAreaCell tmp = m_exAreaModel.getExAreaByName(name);
			if(tmp != null && tmp != m_exAreaCell){
				showmessage(MultiLang.getString("miufo00105"));  
//				fldName.requestFocus();
				return false;
			}
		}
		
		AreaPosition area = getSettingArea();
		if(area == null){
			showmessage(StringResource.getStringResource("miufo1001147")); //"区域名称不合法！"
//			fldArea.requestFocus();
			return false;
		}
		
		//可扩展区域里面可以包括合并区域
		boolean bCheck = m_exAreaModel.check(m_exAreaCell, area);//m_report.getCellsModel().checkArea(m_exAreaCell, area);
		if(!bCheck){
			showmessage(MultiLang.getString("miufo00106"));
//			this.fldArea.requestFocus();
			return false;
		}
		
//		/新建
		if(m_exAreaCell == null){
			m_exAreaCell = m_exAreaModel.addExArea(area);
		} else {
			AreaPosition oldArea = m_exAreaCell.getArea();
			//区域缩小，需要派发区域更改事件。
			if(!oldArea.equals(area)){
				
				String error = m_exAreaCell.fireUIEvent(ExAreaModelListener.CHANGE_AREA, m_exAreaCell, area);
				 if(error != null && error.length() > 0){
					UfoPublic.showErrorDialog(this, error, MultiLang.getString("miufo00082")); 
					return false;
				 }
				 
				 boolean bSuccess = false;

				 if(!area.contain(oldArea)){
					 if(oldArea.contain(area)){
						 bSuccess = reduceArea(area, oldArea);
					 } else {
						 bSuccess = changeArea(area, oldArea);
					 }
					 if(!bSuccess){
						return false;
					 }
				 }
				 m_exAreaCell.setArea(area);
			}
		}
		
		m_exAreaCell.setExAreaName(name);
		
		String text = fldFixRow.getText();
		if(text != null && text.trim().length() > 0){
			m_exAreaCell.setFixRow(new Integer(text).intValue());
		} else {
			m_exAreaCell.setFixRow(0);
		}
		
		text = fldFixCol.getText();
		if(text != null && text.trim().length() > 0){
			m_exAreaCell.setFixCol(new Integer(text).intValue());
		}else {
			m_exAreaCell.setFixCol(0);
		}
		
//		if(getCbFixRowCol().isSelected()){
			m_exAreaCell.setFixed(getCbFixRowCol().isSelected());
//			m_exAreaCell.setFixRow(0);
//			m_exAreaCell.setFixCol(0);
//		} else {
//			
//		}
		 
		m_exAreaCell.setExMode(getExType());
		Object obj = getBoxExRef().getSelectedItem();
		if(obj != null && !"".equals(obj)){
			ExAreaCell refCell = (ExAreaCell) obj;
			m_exAreaCell.setRefAreaPK(refCell.getExAreaPK());
			
			DefaultConstEnum selectedItem = (DefaultConstEnum)getBoxExPoint().getSelectedItem();
			m_exAreaCell.setRefPoint(new Integer(selectedItem.getValue() + "").intValue());
			
			selectedItem = (DefaultConstEnum)getBoxFromPoint().getSelectedItem();
			m_exAreaCell.setFromPoint(new Integer(selectedItem.getValue() + "").intValue());
			
		} else {
			m_exAreaCell.setRefAreaPK(null);
			m_exAreaCell.setRefPoint(ExAreaCell.EX_POINT_00);
			m_exAreaCell.setFromPoint(ExAreaCell.EX_POINT_00);
		}
		
		m_exAreaCell.setValue(m_exAreaCell);
		
		m_report.getCellsModel().setDirty(true);
		
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				ExAreaBaseInfoPanel.this.m_report.updateUI();
			}
		});
		
		return true;
	}
	
	/**
	 * 解析设定的区域字符串
	 * @return
	 */
	private AreaPosition getSettingArea() {
		//判断是否正确
		String strPos = getFldArea().getText();

		if (strPos == null || strPos.equals("")) {
//			showmessage(StringResource.getStringResource("miufo1000787")); //"区域不能输入为空！"
//			fldArea.requestFocus();
			return null;
		}
		AreaPosition area = null;
		try {
			area = AreaPosition.getInstance(strPos);
		} catch (Throwable ex) {
//			showmessage(StringResource.getStringResource("miufo1001147")); //"区域名称不合法！"
//			fldArea.requestFocus();
			return null;
		}
		
		return area;
	}

	private int getExType(){
		if(getRbExType2().isSelected()){
			return ExAreaCell.EX_MODE_X;
		}
		if(getRbExType3().isSelected()){
			return ExAreaCell.EX_MODE_Y;
		}
		
		return ExAreaCell.EX_MODE_FIXATION;
	}
	/**
	 * 错误信息。 创建日期：(00-11-28 14:58:24)
	 */
	private void showmessage(String errs) {
		UfoPublic.sendErrorMessage(errs, this, null);
//		NCOptionPane.showMessageDialog(this, errs, null, NCOptionPane.ERROR_MESSAGE);
	}
	//area是oldArea的子区域
	private boolean reduceArea(AreaPosition area, AreaPosition oldArea) {
		  
		ArrayList<IArea> areas = m_report.getCellsModel().seperateArea(oldArea);
		ArrayList<IArea> clearAreas = new ArrayList<IArea>();
		for(IArea a: areas){
			if(area.contain(a)){
				continue;
			}
			clearAreas.add(a);
		}
		
		m_report.getCellsModel().clearArea(CellsModel.CELL_ALL, clearAreas.toArray(new IArea[0]));
		
		return true;
	}	
	public ExAreaCell getExArea(){
		return m_exAreaCell;
	}
	/**
	 * @i18n miufo00107=将覆盖新区域所有内容，是否继续？
	 */
	private boolean changeArea(AreaPosition area, AreaPosition oldArea) {
		//真实覆盖的区域
		int w = Math.min(oldArea.getWidth(), area.getWidth());
		int h = Math.min(oldArea.getHeigth(), area.getHeigth());
		AreaPosition realNewArea = AreaPosition.getInstance(area.getStart().getRow(), area.getStart().getColumn(), w, h);
		AreaPosition realOldArea = AreaPosition.getInstance(oldArea.getStart().getRow(), oldArea.getStart().getColumn(), w, h);
		
		if(!m_report.getCellsModel().isEmptyArea(realNewArea)){
			if(UfoPublic.showConfirmDialog(m_report, MultiLang.getString("miufo00107"), "", JOptionPane.YES_NO_OPTION) != 0){
				return false;
			}
		}
		
		//移动单元格
		m_report.getCellsModel().moveCells(realOldArea, realNewArea.getStart());
		
		return true;
	}
	/**
	 * @return
	 * @i18n miufo00108=固定大小
	 */
	protected JCheckBox getCbFixRowCol() {
		if (cbFixRowCol == null) {
			cbFixRowCol = new JCheckBox();
			cbFixRowCol.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					boolean selected = cbFixRowCol.isSelected();
					if(selected){
						getFldFixCol().setText(null);
						getFldFixRow().setText(null);
					}  
					getFldFixCol().setEnabled(!selected);
					getFldFixRow().setEnabled(!selected);
					getLblFixCol().setEnabled(!selected);
					getLblFixRow().setEnabled(!selected);
				}
			});
			cbFixRowCol.setText(MultiLang.getString("miufo00108"));
			cbFixRowCol.setBounds(33, 24, 99, 26);
		}
		return cbFixRowCol;
	}
}
 