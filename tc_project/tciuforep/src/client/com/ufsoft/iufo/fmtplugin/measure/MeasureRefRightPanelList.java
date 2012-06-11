package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureRefRightPanelList extends MeasureRefRightPanel {
	private static final long serialVersionUID = 3107192508033658239L;
	

	protected MeasureTableModel m_tablemodel = null;
	private JPanel measureFindPanel = null;
	private JLabel measureFindLabel = null;
	private JScrollPane measureListScrollPane = null;	
	private JTextField measureFindTextField = null;
	private JButton measureFindButton = null;
	protected JTable measureListTable = null;

	protected void changeReportImpl(ReportVO repvo, boolean bIncludeRefMeas) {	
		MeasureVO[] vos = getMatchingMeasureVOs(repvo, bIncludeRefMeas);		
		m_tablemodel.resetTable(vos,true);
	}

	public MeasureRefRightPanelList(MeasureRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeMeasuresList, boolean bIncludeRefMeas) {
		super(parentDlg, isContainsCurrentReport, currentKeyGroupVO, excludeMeasuresList, bIncludeRefMeas);
		setLayout(new BorderLayout());
		add(getMeasureFindPanel(), java.awt.BorderLayout.NORTH);
		add(getMeasureListScrollPane(), java.awt.BorderLayout.CENTER);
	}
	/**
	 * This method initializes measureFindPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMeasureFindPanel() {
		if (measureFindPanel == null) {
			measureFindLabel = new nc.ui.pub.beans.UILabel();
			measureFindLabel.setName("MeasureLabel");
			measureFindLabel.setText(StringResource.getStringResource("miufo1000767")); //"指标名称:"
			
			measureFindPanel = new UIPanel();
			measureFindPanel.add(measureFindLabel, null);
			measureFindPanel.add(getMeasureFindTextField(), null);
			measureFindPanel.add(getMeasureFindButton(), null);
		}
		return measureFindPanel;
	}
	/**
	 * This method initializes measureListScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getMeasureListScrollPane() {
		if (measureListScrollPane == null) {
			measureListScrollPane = new UIScrollPane();
			measureListScrollPane.setViewportView(getMeasureListTable());
		}
		return measureListScrollPane;
	}
	/**
	 * This method initializes measureFindTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getMeasureFindTextField() {
		if (measureFindTextField == null) {
			measureFindTextField = new UITextField();
			measureFindTextField.setPreferredSize(new java.awt.Dimension(200,22));
			measureFindTextField.getDocument().addDocumentListener(new DocumentListener(){

				public void changedUpdate(DocumentEvent e) {
					txfSearchUpdate(e);
				}

				public void insertUpdate(DocumentEvent e) {
					txfSearchUpdate(e);
				}

				public void removeUpdate(DocumentEvent e) {
					txfSearchUpdate(e);
				}
				
			});
		}
		return measureFindTextField;
	}
	
	private void txfSearchUpdate(DocumentEvent e){
		try {
		String str = "";
		if (e != null) {
			str = e.getDocument().getText(0, e.getDocument().getLength()).trim();
		} else {
			str = getMeasureFindTextField().getText().trim();
		}
		if (str == null || str.length() == 0)
			return;
		ValueObject[] vos = findValue(str);
		m_tablemodel.resetTable((MeasureVO[]) vos,false);
		}catch (Exception e1) {
			AppDebug.debug(e1);
		}
		
	}
	/**
	 * This method initializes measureFindButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getMeasureFindButton() {
		if (measureFindButton == null) {			
			measureFindButton = new nc.ui.pub.beans.UIButton();
			measureFindButton.setName("BFindByMeasure");
			measureFindButton.setText(StringResource
					.getStringResource("miufo1000765")); //"查 找"
			measureFindButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					txfSearchUpdate(null);
					
				}				
			});
		}
		return measureFindButton;
	}
	/**
	 * This method initializes measureListTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getMeasureListTable() {
		if (measureListTable == null) {
			measureListTable=initTable();
			measureListTable.setAutoCreateColumnsFromModel(false);
			measureListTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			initTableModel();
			measureListTable.setModel(m_tablemodel);
			measureListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//如果是公式定义的指标参照，需要增加自动关闭功能
			if (isAutoClose()){
				measureListTable.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent MEvent) {
						if (MEvent.getSource() == getMeasureListTable() && MEvent.getClickCount() >= 2) {
							MeasureVO vo = getSelectedMeasureVO();
							if(vo != null){
								closeWithOKResult();
							}
						}
					}
				});
			}
			
			m_tablemodel.addMouseListener(measureListTable);
			initColumnsToTable();
			
		}
		return measureListTable;
	}
	
	protected JTable initTable(){
		return new nc.ui.pub.beans.UITable();
	}
	protected void initColumnsToTable(){
		TableColumn column;
		for (int k = 0; k < MeasureTableModel.columnNames.length; k++) {

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			if (k == 0) {//名称列
				renderer.setHorizontalAlignment(JLabel.LEFT);
			} else {
				renderer.setHorizontalAlignment(JLabel.CENTER);
			}

			TableCellEditor editor = null;
			column = new TableColumn(k, MeasureTableModel.columnWidths[k], renderer, editor);
			measureListTable.addColumn(column);
		}

	}
	/**
	 * 初始化tablemodel 创建日期：(2003-7-1 14:19:23)
	 */
	protected void initTableModel() {
		m_tablemodel = new MeasureTableModel();
	}
	/**
	 * 查找指标 如果用户已经选择了某一报表，则从该报表中查找，否则将从所有的用户允许参照的报表中查找 创建日期：(2003-7-9 10:36:00)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 * @param findStr
	 *            java.lang.String
	 */
	private nc.vo.pub.ValueObject[] findValue(String findStr) {
		MeasureVO[] tableVos = null;
		Vector vec = new Vector();
		findStr = findStr.toUpperCase();


		tableVos = m_tablemodel.getAllMeasureVOByTable();
		for (int i = 0; i < tableVos.length; i++) {
			if (tableVos[i].getName().toUpperCase().indexOf(findStr) >= 0)
				vec.addElement(tableVos[i]);
		}
		MeasureVO[] vos = new MeasureVO[vec.size()];
		vec.copyInto(vos);
		return vos;
	}

	protected MeasureVO getSelectedMeasureVO() {
		int row = getMeasureListTable().getSelectedRow();
		if (row < 0)
			row = 0;
		
		MeasureVO vo =  (MeasureVO) m_tablemodel.getVO(row);	
		return filterMeasureVO(vo);	
	}
	public MeasureTableModel getTableModel(){
		return m_tablemodel;
	}
	
	public void setRowSelectFromVO(MeasureVO vo){
		if(vo!=null){
			int anchorRow=getTableModel().getRowIndex(vo);
			getMeasureListTable().getSelectionModel().setSelectionInterval(anchorRow, anchorRow);
			Point newBegin=new Point(0,getMeasureListTable().getRowHeight()*anchorRow);
			if(!getMeasureListScrollPane().getViewport().getViewRect().contains(newBegin)){
				getMeasureListScrollPane().getViewport().setViewPosition(newBegin);
			}
			
		}
		
	}

	@Override
	public MeasureVO[] getSelMeasureVOs() {
		return new MeasureVO[]{getSelectedMeasureVO()};
	}

	@Override
	public void setSelMeasureVOs(MeasureVO[] selMeasures) {
		// TODO Auto-generated method stub
		
	}
}
