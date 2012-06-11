package com.ufsoft.report.fmtplugin.formula;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.comp.KToolBarButton;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportToolBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.table.event.SelectEvent;

/**
 * ����: �������Ͽؼ�����ʽ�༭���.
 * @author chxw
 * 2008-5-29
 */
public class ToolBarFormulaComp extends ReportToolBar {
	static final long serialVersionUID = 5809588378421371182L;
	private static String IMAGEPATH = "reportcore/";
	private static final String EQUALS = "=";
	private JTextField areaTextField = null;
	private JTextField contentTextField = null;
	private JButton cancelButton = null;
	private JButton formulaButton = null;
	private JButton okButton = null;
	private UfoReport _ufoReport;
	
	/**
	 * ToolBarFormulaComp ���캯�� 
	 * @param model 
	 */
	public ToolBarFormulaComp(UfoReport ufoReport) {
		super(ufoReport);
		initialize();
		_ufoReport = ufoReport;
		getAreaTextField().addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if(e.getKeyChar() == KeyEvent.VK_ENTER){
					String strAreaPos = getAreaTextField().getText();
					if(strAreaPos == null || strAreaPos.equals("")){
						return;
					}
					AreaPosition aimArea = TableUtilities.getAreaPosByString(areaTextField.getText());
		            if(aimArea == null) { // ����������Ч������λ�û�ѡ���������Ч���ơ���
		            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0001109"), _ufoReport, null);//"������Ч"
		                return;
		            }

		            _ufoReport.getTable().getCells().getSelectionModel().clear();
		            _ufoReport.getTable().getCells().getSelectionModel().setAnchorCell(aimArea.getStart());
		            _ufoReport.getTable().getCells().getSelectionModel().setSelectedArea(aimArea);
		            _ufoReport.getTable().getCells().requestFocus();
		            
				}
			}			
		});
		getCellsModel().getSelectModel().addSelectModelListener(new SelectListener(){
			public void selectedChanged(SelectEvent e) {
				if(e.getProperty().equals(SelectEvent.ANCHOR_CHANGED)){
					CellPosition cellPos = getCellsModel().getSelectModel().getAnchorCell();
					if(!_ufoReport.getTable().getCells().isCellEditable(cellPos.getRow(),cellPos.getColumn())){
						contentTextField.setEditable(false);
					}else{
						contentTextField.setEditable(true);
					}
					setupFormulaComp();
				}
			}
			
		});
		setupFormulaComp();
		
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setName("toolBarFormulaComp");
        this.add(getAreaTextField());
        this.add(getCancelButton());
        this.add(getOkButton());
        this.add(getFormulaButton());
        Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
        int contextWidth=(int)(dimScreen.getWidth()-getAreaTextField().getPreferredSize().getWidth()-getCancelButton().getPreferredSize().getWidth()-getOkButton().getPreferredSize().getWidth()-getFormulaButton().getPreferredSize().getWidth());
        getContentTextField().setPreferredSize(new java.awt.Dimension(contextWidth,22));
        this.add(getContentTextField());
		
	}

	/**
	 * This method initializes area	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getAreaTextField() {
		if (areaTextField == null) {
			areaTextField = new JTextField();
			areaTextField.setPreferredSize(new java.awt.Dimension(65,22));
			areaTextField.addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent e) {					
				}
				public void focusLost(FocusEvent e) {
					try{
						AreaPosition.getInstance(areaTextField.getText());
					} catch (Exception ex) {
						String area = getCellsModel().getSelectModel().getSelectedArea().toString();
						areaTextField.setText(area);
					}
				}
				
			});
		}
		return areaTextField;
	}

	/**
	 * This method initializes contentTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getContentTextField() {
		if (contentTextField == null) {
			contentTextField = new JTextField();
//			contentTextField.setMaxLength(500);
//			contentTextField.setPreferredSize(new java.awt.Dimension(600,22));
			contentTextField.registerKeyboardAction(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					getOkButton().doClick();
				}				
			},
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),
			WHEN_FOCUSED
			);
			
			contentTextField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e){
					if(e.getKeyCode() != KeyEvent.VK_ENTER){
						getCancelButton().setEnabled(true);
				        getOkButton().setEnabled(true);
					}
			    }
			});
		}
		return contentTextField;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFormulaButton() {
		if (formulaButton == null) {
			formulaButton = new KToolBarButton();
			formulaButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"calculate.gif"));
			formulaButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) { 
					getCancelButton().setEnabled(true);
			        getOkButton().setEnabled(true);
			        executeFormulaDefCmd();	
			        setupFormulaComp();									
				}
			});
		}
		return formulaButton;
	}
	
	protected void executeFormulaDefCmd() {
		new AreaFormulaActionHandler(_ufoReport.getTable().getCells()).execute(null);
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new KToolBarButton();
			cancelButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"cancel.gif"));
			cancelButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					IArea area = getSelectedArea();
					if(isFormatState() && isFormulaCell(area)){
						String fmlContent = getSelectedFormulaText(area);
						getContentTextField().setText(EQUALS + fmlContent);	
					} else{
						CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
						Object value = getCellsModel().getCellValue(anchorPos);
						getContentTextField().setText(value == null ? "": value.toString());	
					}
					getCancelButton().setEnabled(false);
			        getOkButton().setEnabled(false);
				}
			});
		}
		return cancelButton;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new KToolBarButton();
			okButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"ok.gif"));
			okButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {  
					AreaPosition aimArea = AreaPosition.getInstance(areaTextField.getText());
					String strEditCellFormula = contentTextField.getText();
					if(isFormatState() && isInputFormula(strEditCellFormula)){
						setCellFormula(aimArea, strEditCellFormula);
					} else{
						setCellValue(aimArea, strEditCellFormula);
					}
					getCancelButton().setEnabled(false);
			        getOkButton().setEnabled(false);
				}
				
			});
		}
		return okButton;
	}
	
	/**
	 * ����ʽ��ӵ�ָ����Ԫ
	 * 
	 * @param fmlArea
	 * @param strCellFormula
	 */
	private void setCellFormula(AreaPosition fmlArea, String strCellFormula) {
		if(strCellFormula != null){
			getCellsModel().clearArea(UFOTable.CELL_CONTENT,new IArea[]{ fmlArea});
		}				
		
		setCellFormulaImpl(fmlArea, strCellFormula);
	}

	/**
	 * ����ʽ��ӵ�ָ����Ԫ
	 * 
	 * @param fmlArea
	 * @param strCellFormula
	 */
	protected void setCellFormulaImpl(AreaPosition fmlArea, String strCellFormula) {
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(getCellsModel());
		IArea fmlRealArea = formulaModel.getRelatedFmlArea(fmlArea);//ת������ȷ�Ĺ�ʽ����
		if (fmlRealArea == null) {
			fmlRealArea = formulaModel.getRelatedFmlArea(fmlArea);
		}
		if (fmlRealArea != null && !fmlRealArea.isCell()) {
			getCellsModel().getSelectModel().setSelectedArea((AreaPosition) fmlRealArea);
		} else {
			fmlRealArea = fmlArea;
		}
		
		if (strCellFormula == null || strCellFormula.trim().equals("") || strCellFormula.trim().equals(EQUALS)){
			getFormulaHandler().clearFormula(fmlRealArea);
		}else{
			boolean bAddCellFml=false;
			StringBuffer showErrMessage = new StringBuffer();
			try {
				int index = strCellFormula.indexOf(EQUALS);
				strCellFormula = strCellFormula.substring(index + 1);
				if(!checkFormula(strCellFormula))
					return;
				bAddCellFml = getFormulaHandler().addDbDefFormula(showErrMessage, fmlRealArea, strCellFormula, null, true);
			} catch (ParseException ex) {
				AppDebug.debug(ex);
				bAddCellFml=false;
			}
			if (bAddCellFml == false)
				getFormulaHandler().clearFormula(fmlRealArea);
		}
	}
	
	/**
	 * ����Ԫ�ı���ӵ�ָ����Ԫֵ
	 * 
	 * @param areaPos
	 * @param strCellText
	 */
	private void setCellValue(AreaPosition areaPos, String strCellText) {
		Cell c = getCellsModel().getCell(areaPos.getStart());
		if(c != null && c.getValue() instanceof IChartModel){
			return ;
		}
		if(isFormatState()){
			
			if(c == null){
				c = new Cell();
				c.setRow(areaPos.getStart().getRow());
				c.setCol(areaPos.getStart().getColumn());
			}
			if(!_ufoReport.getTable().getCells().isCellEditable(c.getRow(),c.getCol())){//add by wangyga 2008-7-17
				return;
			}
			c.setValue(strCellText);
			getCellsModel().setCell(areaPos.getStart().getRow(), areaPos.getStart().getColumn(), c);
			
		} else{
			UFOTable table = _ufoReport.getTable();
			CellsPane pane = table.getCells();
			//��ϵ�Ԫ�������׵�Ԫ��ת��Ϊ�׵�Ԫ��
			int row = areaPos.getStart().getRow();
			int column = areaPos.getStart().getColumn();
			CombinedCell cc = _ufoReport.getCellsModel().belongToCombinedCell(row, column);
			if (cc != null) {
				row = cc.getArea().getStart().getRow();
				column = cc.getArea().getStart().getColumn();
			}
			//�༭���ǿգ������жϵ�ǰ�༭�ɹ���
			if (!pane.isCellEditable(row, column)) {
				return;
			}
			setCellValue(strCellText, row, column);		
			
		}
		
	}

	/**
	 * ��������̬ʱָ����Ԫ��ֵ
	 * 
	 * @param cellText
	 * @param pane
	 * @param row
	 * @param column
	 */
	protected void setCellValue(String cellText, int row, int column) {		
	}
	
	/**
	 * ����ѡ��������¹�������ʽ�����ʾ״̬
	 */
	private void setupFormulaComp(){
		//ѡ��ģ��ê��ֻ��¼ѡ������ĵ�һ����Ԫ�����Եõ�ѡ������ʱ
		//ֻ�ܸ���ѡ��ê��ͨ��CellsModel��getArea��������
		IArea area = getSelectedArea();
		getAreaTextField().setText(area.toString());
		if(isFormatState() && isFormulaCell(area)){
			String fmlContent = getSelectedFormulaText(area);
			getContentTextField().setText(EQUALS + fmlContent);	
		}else{
			CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
			getContentTextField().setText(getFormatValue(anchorPos));	
		}
		
		getFormulaButton().setEnabled(isFormatState());
		getCancelButton().setEnabled(false);
        getOkButton().setEnabled(false);
	}
	
	protected String getFormatValue(CellPosition cell){
		if(cell == null)
			return null;
		Object value = getCellsModel().getCellValue(cell);
		// add by ����� 2008-5-4 �޸ĵ�Ԫ����Ϊdouble���ͣ���Ϊ8λ����ʱ���������ϲ��ÿ�ѧ������		
		if (value instanceof Double) {
			IufoFormat format = (IufoFormat) getCellsModel().getCellIfNullNew(
					cell.getRow(),cell.getColumn()).getFormat();
			if (format != null) {				
				value = format.getString((Double) value);
			}
		}
		return value != null ? value.toString() : "";
	}
	
	/**
	 * ����ê��ѡ��������(���������ʽ�������Ӧ��Ϊ������������)
	 * @return
	 */
	private IArea getSelectedArea(){
		//ѡ��ģ��ê��ֻ��¼ѡ������ĵ�һ����Ԫ�����Եõ�ѡ������ʱ
		//ֻ�ܸ���ѡ��ê��ͨ��CellsModel��getArea��������
		IArea area = getCellsModel().getSelectModel().getSelectedArea();
		//modify by guogang 2007-11-7 ���������ʽ�Ļ�ѡ��������Ϊ��
		AreaPosition selArea = getCellsModel().getSelectModel().getSelectedArea();
		if(selArea!=null){
			area=selArea;
		}
		//modify end
		return area;

	}
	
	private boolean checkFormula(String strFmlContent){
		if(strFmlContent == null || strFmlContent.trim().length() ==0)
			return true;
		try {
			getFormulaHandler().parseUserDefFormula(null,strFmlContent);
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}		
		return true;
	}
	
	/**
	 * ����ѡ����ʽ����ʾ�ı�
	 * @param area ��ʽ��������(���������ʽ�������Ӧ��Ϊ������������)
	 * @return
	 */
	protected String getSelectedFormulaText(IArea area){
		FormulaVO formulaVO = getSelectedFormula(area);
		return  formulaVO == null ? "" : formulaVO.getContent();

	}

	/**
	 * ����ѡ��������Ĺ�ʽ����
	 * @param area
	 * @return
	 */
	private FormulaVO getSelectedFormula(IArea area){
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(getCellsModel());
		return formulaModel.getDirectFml(area);

	}
	
	/**
	 * ��鹫ʽ�༭��¼��ֵ�Ƿ�Ϊ��ʽ
	 * modify by wangyga
	 * �����һ���ַ���"="��,��ֻ��һ��"="������Ϊ�ǹ�ʽ(���԰�������Ⱥţ���һ���ǵȺ�ʱ���Ǳ༭��ʽ)
	 * 
	 * @param inputFormulaStr ��ʽ�༭����¼���ַ���
	 * @return
	 */
	private boolean isInputFormula(String inputFormulaStr){
		if(inputFormulaStr == null || inputFormulaStr.trim().length() < 2){
			return false;
		}
		return (inputFormulaStr.trim().indexOf('=') == 0)?true:false;
		
	}
	
	protected AreaFmlExecutor getFormulaHandler(){
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(getCellsModel());	
//		AreaFormulaPlugin _formulaDefPlugin = (AreaFormulaPlugin) _ufoReport
//				.getPluginManager().getPlugin(AreaFormulaPlugin.class.getName());
		return formulaModel.getAreaFmlExecutor();
	}
	
	protected boolean isFormulaCell(IArea area){
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(getCellsModel());
		FormulaVO fVO = formulaModel.getDirectFml(area);
		return (fVO != null)?true:false;		
	}
	
	/**
	 * �Ƿ��ʽ���״̬
	 * @return
	 */
	private boolean isFormatState(){
		return _ufoReport.getOperationState() == UfoReport.OPERATION_FORMAT;
	}
	
	private CellsModel getCellsModel(){
		return _ufoReport.getCellsModel();
	}

	protected UfoReport getReport() {
		return _ufoReport;
	}

	protected void setReport(UfoReport report) {
		_ufoReport = report;
	}
	
}
