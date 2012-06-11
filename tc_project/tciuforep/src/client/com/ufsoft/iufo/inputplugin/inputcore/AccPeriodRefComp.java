package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.accperiod.IAccountCalendar;
import nc.ui.iufo.web.reference.base.AccPeriodRefAction;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.util.iufo.reportdata.DateInputCheckUtil;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period.AccperiodschemeVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.bd.period3.AccperiodquarterVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UserListComponentPolicy;
import com.ufsoft.table.re.IRefComp;
/**
 * <pre>
 * </pre>
 * 
 * 会计期间季度和月参照
 * 
 * @author 王宇光
 * @version Create on 2008-6-12
 */
public class AccPeriodRefComp extends nc.ui.pub.beans.UIPanel  implements IRefComp{
	private static final long serialVersionUID = 1L;
	private String strAccPreiodPk = null;
	private String strAccPeriodType = null;
	private JSplitPane splitPane = null;
	private JTree ufoTree = null;
	private JTable table = null;
	private UIPanel leftPanel = null;
	private UIPanel rightPanel = null;
	private String strDefaultDate = null;
	private JButton btOk = null;
	private JButton btCancel = null;
	private String strSelectValue = null;
	private String strCurPeroidYear = null;
	private String[] aryTimeValue = null;
	/**** 从KeyVO引用的常量，由于KeyVO不能被report项目访问，无法直接引用 ****/
	public final static String ACC_SEASON_PK = "000000000011";
	public final static String ACC_MONTH_PK = "000000000012";
	/**
	 * @i18n miufo1000589=选择
	 * @i18n miufo00066=月份
	 * @i18n miufo5508000001=会计期间
	 * @i18n miufo1002021=开始日期
	 */
	private final String[] aryMonthColumnNames = { StringResource.getStringResource("miufo1000589"), StringResource.getStringResource("miufo00066"), StringResource.getStringResource("miufo5508000001"), StringResource.getStringResource("miufo1002021") };
	/**
	 * @i18n miufo1000589=选择
	 * @i18n miufo5508000003=会计季度
	 * @i18n miufo00065=开始月份
	 * @i18n miufo1002021=开始日期
	 */
	private final String[] arySeasonColumnNames = { StringResource.getStringResource("miufo1000589"), StringResource.getStringResource("miufo5508000003"), StringResource.getStringResource("miufo00065"), StringResource.getStringResource("miufo1002021") };
	
	public AccPeriodRefComp(String strAccPreiodPk, String strAccPeriodType,String strDefaultDate,String strCurPeroidYear) {
		super();
		this.strAccPreiodPk = strAccPreiodPk;
		this.strAccPeriodType = strAccPeriodType;
		this.strDefaultDate = strDefaultDate;
		this.strSelectValue=strDefaultDate;
		this.strCurPeroidYear = strCurPeroidYear;
		initialize();
	}

	private void initialize() {
		setPreferredSize(new Dimension(500,300));
		setLayout(new BorderLayout());
		splitPane = getSplitPane();		
		splitPane.add(getleftPanel(), JSplitPane.LEFT);
		splitPane.add(getRigthPanel(), JSplitPane.RIGHT);
		add(splitPane);
		initTreeSelected();
		installKeyboardActions();
	}
	
	/**
	 * @i18n miufo00248=没有默认的会计期间方案，请重新设置
	 * @i18n miufo5508000002=会计年
	 * @i18n miufohbbb00110=该期间方案无数据
	 */
	private DefaultTreeModel getPeriodYearTreeModel(String strAccPreiodPk,String strAccPeriodType){
		if(!checkAccPreiod(strAccPreiodPk)){
			return new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("miufo00248")),false);
		}
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(StringResource.getStringResource("miufo5508000002"));		
		try {
			IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
			AccperiodVO[] periods=calendar.getYearVOsOfCurrentScheme();
			if(periods == null || periods.length ==0){
				return new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("miufohbbb00110")),false);
			}
			addTreeChild(root,periods);
		} catch (Exception e) {
			AppDebug.debug(e);
			return null;
		}		
		DefaultTreeModel treeModel = new DefaultTreeModel(root,false);
		return treeModel;
	}
	
	/**
	 * @i18n miufo00832=参数不允许为空
	 */
	private void addTreeChild(DefaultMutableTreeNode pNode,AccperiodVO[] periods){
		if(pNode == null || periods == null || periods.length == 0){
			throw new IllegalArgumentException(StringResource.getStringResource("miufo00832"));
		}
		String strYear = getCurPeroidYear();
		if(strYear != null && strYear.length() > 4){
			strYear = strYear.substring(0, 4);
		}
		int iLength = periods.length;
		for (int i = 0; i < iLength; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(periods[i].getPeriodyear());
			if(strYear != null && !strYear.equals(periods[i].getPeriodyear()))
				continue;
			pNode.add(node);
		}
	}
	
	private JSplitPane getSplitPane(){
		if(splitPane == null){
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
			splitPane.setDividerLocation(120);
			splitPane.setDividerSize(6);

		}
		return splitPane;
	}
	
	private void initTreeSelected(){
		DefaultTreeModel treeModel = (DefaultTreeModel)ufoTree.getModel();
		if(treeModel == null) return;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		int iChildCount = root.getChildCount();
		DefaultMutableTreeNode defaultNode = null;
		if(strDefaultDate != null && strDefaultDate.length() > 0){
			for(int i = 0;i < iChildCount;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
				String value = (String)node.getUserObject();
				if(value.equals(getDefaultYear())){
					defaultNode = node;
					break;
				}
			}
		}else{
			if(iChildCount > 0){
				defaultNode = (DefaultMutableTreeNode)root.getFirstChild();		
			}				
		}		
		if(defaultNode == null){
			return;
		}
		ufoTree.setSelectionPath(new TreePath(defaultNode.getPath()));
	}
	
	/**
	 * 交验会计期间是否存在
	 * @param strAccPreiodPk
	 * @return
	 */
	private boolean checkAccPreiod(String strAccPreiodPk){
		AccperiodschemeVO schemeVO=AccPeriodSchemeUtil.getInstance().getPeriodSchemeByPK(strAccPreiodPk);
		if(schemeVO != null)
			return true;
		return false;
	}
	
	private String[][] getMonthTimeData(String strAccPreiodPk){
		String[][] rowDatas=null;
		try{
			IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
			String strSelectYear = getSelectYear();
			if (calendar==null||strSelectYear == null || strSelectYear.length() == 0)
				return rowDatas = new String[0][0];
			String strCurYear = getCurPeroidYear();
			int iQuater=-1;
			if (strCurYear!=null && strCurYear.length()>4){
				iQuater=Integer.parseInt(strCurYear.substring(6,7));
				strSelectYear=strCurYear.substring(0,4);
			}
			calendar.set(strSelectYear);
			AccperiodmonthVO[] monthVOs=calendar.getMonthVOsOfCurrentYear();
			if (monthVOs!=null && monthVOs.length>0){
				if (iQuater>0){
					calendar.set(strSelectYear, iQuater);
					monthVOs=calendar.getMonthVOsOfCurrentQuarter();
				}
			}
			if (monthVOs!=null){
				rowDatas=new String[monthVOs.length][4];
				for (int i=0;i<monthVOs.length;i++){					
					rowDatas[i][1]=getFullPeriod(""+monthVOs[i].getMonth());
					rowDatas[i][2]=""+strSelectYear+"-"+getFullPeriod(""+monthVOs[i].getMonth());	
					rowDatas[i][3]=getFullPeriod(""+monthVOs[i].getBegindate());
				}
			}
			setTimeValue(aryTimeValue);
		}catch(Exception e){
    		AppDebug.debug(e);
    	}
		return rowDatas;
	}
	
	private String[][] getSeasonTimeData(String strAccPreiodPk){
		String[][] rowDatas=null;
		try{
			IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
			String strSelectYear = getSelectYear();
			if (strSelectYear == null || strSelectYear.length() == 0)
				return rowDatas = new String[0][0];
			calendar.set(strSelectYear);
			AccperiodquarterVO[] quaterVOs=calendar.getQuarterVOsOfCurrentYear();
			if (quaterVOs!=null){
				rowDatas=new String[quaterVOs.length][4];
				for (int i=0;i<quaterVOs.length;i++){					
					rowDatas[i][1]=""+quaterVOs[i].getQuarter();
					rowDatas[i][2]=getFullPeriod(quaterVOs[i].getBeginmonth());				
					calendar.set(strSelectYear,quaterVOs[i].getQuarter());
					rowDatas[i][3]=getFullPeriod(""+calendar.getMonthVO().getBegindate());
				}
			}
			setTimeValue(aryTimeValue);
		}catch(Exception e){
    		AppDebug.debug(e);
    	}
		return rowDatas;
	}
	
	private void initFirstColumn(){
		TableColumn column = table.getColumnModel().getColumn(0);
//		column.setCellEditor(new RadioButtonEditor( new UIRadioButton()));	
		column.setCellRenderer( new RadioButtonRender());
	}
	
	private void refreshTableModel(){
		DataModel model = null;
		if(ACC_SEASON_PK.equals(strAccPeriodType)){
			model = new DataModel(getSeasonTimeData(strAccPreiodPk),arySeasonColumnNames);
		}else{
			model = new DataModel(getMonthTimeData(strAccPreiodPk),aryMonthColumnNames);
		}
		table.setModel(model);	
		initFirstColumn();
		table.repaint();
	}
	
	private JPanel getleftPanel(){
		if(leftPanel == null){
			leftPanel = new UIPanel();
			leftPanel.setLayout(new BorderLayout());
			UIScrollPane treePanel = new UIScrollPane(getPeriodTree());
			leftPanel.add(treePanel,BorderLayout.CENTER);
		}
		return leftPanel;
	}
	
	private JTree getPeriodTree(){
		if(ufoTree==null){
			ufoTree = new JTree();
			ufoTree.setModel(getPeriodYearTreeModel(strAccPreiodPk,strAccPeriodType));
			ufoTree.addTreeSelectionListener(createTreeSelectionListener());
		}
		return ufoTree;
	}
	private JPanel getRigthPanel(){
		if(rightPanel == null){
			rightPanel = new UIPanel();
			rightPanel.setLayout(new BorderLayout());
			UIScrollPane tablePanel = new UIScrollPane(getTablePane());
			rightPanel.add(tablePanel,BorderLayout.CENTER);
			JPanel bottonPanel = new UIPanel();
			bottonPanel.add(getOkButton());
			bottonPanel.add(getCancelButton());
			rightPanel.add(bottonPanel,BorderLayout.SOUTH);
		}
		return rightPanel;
	}
	
	private TreeSelectionListener createTreeSelectionListener(){
		return new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
					refreshTableModel();	
			}
			
		};
	}
	
	private JTable getTablePane(){
		if(table == null){
			table = new UITable(){
				@Override
				public boolean isCellSelected(int row, int column) {
					 Object cellValue = ACC_SEASON_PK.equals(strAccPeriodType) ? table.getValueAt(row, 1) : table.getValueAt(row, 2);
					return super.isCellSelected(row, column)||checkSelect(cellValue, row);
				}
				
			};
			DataModel model = null;
			if(ACC_SEASON_PK.equals(strAccPeriodType)){
				model = new DataModel(getSeasonTimeData(strAccPreiodPk),arySeasonColumnNames);
			}else{
				model = new DataModel(getMonthTimeData(strAccPreiodPk),aryMonthColumnNames);
			}
			table.setModel(model);	
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {					
					strDefaultDate = null;
					table.repaint();
				}								
			});
			table.addKeyListener(new KeyListener(){

				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()!=KeyEvent.VK_TAB){
						strDefaultDate = null;
						table.repaint();
					}
					
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
					
				}
				
			});
			final KeyStroke keyStroke=KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
			table.registerKeyboardAction(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						beforeClose();
						ActionListener listener=AccPeriodRefComp.this.getActionForKeyStroke(keyStroke);
						ActionEvent event = null;
						if(listener!=null){
							event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
							listener.actionPerformed(event);
						}
					}
				}, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			table.registerKeyboardAction(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					table.nextFocus();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			table.registerKeyboardAction(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
//					table.getParent().requestFocusInWindow();
					table.nextFocus();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.SHIFT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

			initFirstColumn();
		}
		return table;
	}
	
	private String getFullPeriod(String strMonthOrQuater){
		if (strMonthOrQuater.trim().length()==1)
			return "0"+strMonthOrQuater;
		return strMonthOrQuater;
	}
	
	private String getDefaultYear(){
		if(strDefaultDate != null && strDefaultDate.length() > 6){
			return strDefaultDate.substring(0,4);
		}
		return null;
	}

	private String getSelectYear(){
		TreePath treePath = ufoTree.getSelectionPath();
		if(treePath == null){
			return null;
		}
		DefaultMutableTreeNode node = ((DefaultMutableTreeNode)treePath.getLastPathComponent());
		Object selectYear = node.getUserObject();
        return selectYear != null? selectYear.toString():"";
	}
	
	/**
	 * 取消按钮
	 * @param
	 * @return JButton
	 */
	private JButton getCancelButton() {
		if (btCancel == null) {
				btCancel = new UIButton();
				btCancel.setName("btCancel");
				btCancel.setText(MultiLang.getString("uiuforep0000739"));	
				btCancel.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						JTable table = getTablePane();
						table.clearSelection();
					}
     				
     			});
		}
		return btCancel;
	}
		
	
	/**
	 * 确定按钮
	 * @param
	 * @return JButton
	 */
	private JButton getOkButton() {
		if (btOk == null) {
			
				btOk = new UIButton();
				btOk.setName("btOk");
				btOk.setText(MultiLang.getString("uiuforep0000782"));
     			btOk.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						beforeClose();
					}
     				
     			});
     			
			
		}
		return btOk;
	}
	
	private void beforeClose(){

		JTable table = getTablePane();
		int iSelectRow = table.getSelectedRow();
		if(iSelectRow < 0){
			return;
		}
		if(ACC_SEASON_PK.equals(strAccPeriodType)){
			strSelectValue = getSelectYear()+"-0"+table.getValueAt(iSelectRow, 1).toString();
		}else{
			strSelectValue = table.getValueAt(iSelectRow, 2).toString();
		}
	
	}
	public class DataModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] aryColumnNames;
		private String[][] data = null;

		DataModel(String[][] data,String[] aryColumnNames) {
			if(data == null){
				data = new String[0][0];
			}
			this.aryColumnNames = aryColumnNames;
			this.data = data;
		}

		public String getColumnName(int column) {
			if (column < 0 || column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//请输入不小于0的值
			}

			return aryColumnNames[column];
		}

		public int getColumnCount() {
			return aryColumnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public Object getValueAt(int row, int column) {
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//请输入不小于0的值
			}

			return data[row][column];
		}

		public void setValueAt(Object value, int row, int column) {
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//请输入不小于0的值
			}
			data[row][column] = (String) value;
		}
				
	}
			
	 private class RadioButtonRender extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value,
                 boolean isSelected, boolean hasFocus, int row, int column) { 	 
    		 UIRadioButton radioBtn = new UIRadioButton("", isSelected);
    		 setHorizontalAlignment(SwingConstants.CENTER);// 设置居中
    		 return radioBtn;
		}
    }

	private boolean checkSelect(Object value,int row){
		String strDefaultDate = getDefaultDate();
		if(strDefaultDate != null && strDefaultDate.length() > 0){
			String strValue = value != null ? value.toString() : null;
			if(ACC_SEASON_PK.equals(strAccPeriodType)){
				strValue = getSelectYear()+"-0"+strValue;
			}
			if(strDefaultDate.equals(strValue)){
				return true;
			}else{
				return false;
			}
		}
//		if(row == 0){
//			return true;
//		}
		return false;
	}
	
	public Object getSelectValue() {		
		return strSelectValue;
	}

	/**
	 * @i18n miufo00064=会计期间参照
	 */
	public String getTitleValue() {		
		return StringResource.getStringResource("miufo00064");
	}

	public Object getValidateValue(String text) {
		if(DateInputCheckUtil.isValidDate(strAccPeriodType, text)){
			text=DateInputCheckUtil.convert2StandDate(strAccPeriodType, text);
		}
		if(text != null && text.length() > 6 ){
			try{
			if (AccPeriodSchemeUtil.getInstance().isAccPeriodValid(strAccPreiodPk, strAccPeriodType, text)==false)
				return null;
			String strRetPeriod=null;
			IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
			if(ACC_SEASON_PK.equals(strAccPeriodType)){
                String strYear = strCurPeroidYear;	
                if(strYear == null || strYear.length() == 0){
                	strYear = getSelectYear();
                }
                String strInputYear=text.substring(0,4);
				calendar.set(strYear);
				AccperiodquarterVO[] quaters=calendar.getQuarterVOsOfCurrentYear();
				if (quaters!=null && quaters.length>0){
					if (strInputYear.compareTo(strYear)<0)
						strRetPeriod=strYear+"-0"+quaters[0].getQuarter();
					else if (strInputYear.compareTo(strYear)>0)
						strRetPeriod=strYear+"-0"+quaters[quaters.length-1].getQuarter();
					else
						strRetPeriod=text;
				}else
					return null;
				
				return strRetPeriod;
			}else{

				int iMonth=-1;
//				String strYear = strCurPeroidYear;
				String strYear = text;
				if (text != null && text.length()>4){
					iMonth=Integer.parseInt(text.substring(5,7));
					strYear=text.substring(0,4);
				}
				if(strYear == null || strYear.length() == 0){//格式态数据浏览时，防止校验不正确
					strYear = getSelectYear();
				}
				calendar.set(strYear);

				AccperiodmonthVO[] months=calendar.getMonthVOsOfCurrentYear();
				if (months!=null && months.length>0){
					if (iMonth>0){
						calendar.set(strYear, (iMonth<10?"0":"")+iMonth);
						months=calendar.getMonthVOsOfCurrentQuarter();
					}
				}
				
				if (months!=null && months.length>0){
					if (text.compareTo(strYear+"-"+AccPeriodRefAction.getFullPeriod(months[0].getMonth()))<0)
						strRetPeriod=strYear+"-"+AccPeriodRefAction.getFullPeriod(months[0].getMonth());
					else if (text.compareTo(strYear+"-"+AccPeriodRefAction.getFullPeriod(months[months.length-1].getMonth()))>0)
						strRetPeriod=strYear+"-"+AccPeriodRefAction.getFullPeriod(months[months.length-1].getMonth());
					else
						strRetPeriod=text;
				}else
					return null;
				return strRetPeriod;
			}
			}catch(Exception e){
				AppDebug.debug(e);
				return null;
			}
		}
		return null;
	}

	public void setDefaultValue(Object obj) {
		if(obj instanceof String){
			this.strDefaultDate = (String)obj;
			this.strSelectValue=this.strDefaultDate;
		}
		getTablePane().clearSelection();
		initTreeSelected();
	}
	
	/*
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(MouseListener l) {
    	btOk.addMouseListener(l);
    	btCancel.addMouseListener(l);
    }
    

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		
		btOk.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					 beforeClose();	
				}
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {
				
			}
			
		});
		btOk.addKeyListener(l);
		btCancel.addKeyListener(l);
	}

	private String[] getTimeValue() {
		return aryTimeValue;
	}

	private void setTimeValue(String[] aryTimeValue) {
		this.aryTimeValue = aryTimeValue;
	}

	private String getDefaultDate() {
		return strDefaultDate;
	}

	private void setDefaultDate(String strDefaultDate) {
		this.strDefaultDate = strDefaultDate;
	}

	private String getCurPeroidYear() {
		return strCurPeroidYear;
	}
	
	protected void installKeyboardActions(){

		registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getOkButton().dispatchEvent(new KeyEvent(getOkButton(),KeyEvent.KEY_PRESSED,KeyEvent.ACTION_EVENT_MASK,0,KeyEvent.VK_ENTER,(char)KeyEvent.VK_ENTER));
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.ALT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getCancelButton().dispatchEvent(new KeyEvent(getCancelButton(),KeyEvent.KEY_PRESSED,KeyEvent.ACTION_EVENT_MASK,0,KeyEvent.VK_ENTER,(char)KeyEvent.VK_ENTER));
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.ALT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		ArrayList<Component> comList = new ArrayList<Component>();
		comList.add(getPeriodTree());
		comList.add(getTablePane());
		comList.add(getOkButton());
		comList.add(getCancelButton());
		setFocusCycleRoot(true);
		setFocusTraversalPolicy(new UserListComponentPolicy(comList));
		setFocusTraversalPolicyProvider(true);
	}
	
}
  