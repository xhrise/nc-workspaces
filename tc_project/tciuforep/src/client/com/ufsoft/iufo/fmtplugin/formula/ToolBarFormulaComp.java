package com.ufsoft.iufo.fmtplugin.formula;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.ui.pub.beans.UIComboBox;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportToolBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.SheetCellEditor;

public class ToolBarFormulaComp extends ReportToolBar implements CellsModelSelectedListener{
	private static final long serialVersionUID = -5470734974520096681L;
	private static String IMAGEPATH = "reportcore/";
	private static final String EQUALS = "=";
	private JTextField areaTextField = null;
	private JTextField contentTextField = null;
	private JButton cancelButton = null;
	private JButton formulaButton = null;
	private JButton okButton = null;
	private JComboBox formulaTypeBox = null;
	private UfoReport _ufoReport;
	
	private static final int PUBLIC_FORMULA = 0;
	private static final int PERSON_FORMULA = 1;
	private static final int TOTAL_FORMULA = 2;
	/**
	 * @i18n miufofunc001=公有公式(fc)
	 * @i18n miufo00661=私有公式(fm)
	 * @i18n miufo1000910=汇总公式(fs)
	 */
	private String[] formulaTypeValue = new String[]{StringResource.getStringResource("miufofunc001"), StringResource.getStringResource("miufo00661"), StringResource.getStringResource("miufo1000910")};
	
	/**
	 * This method initializes 
	 * @param model 
	 * 
	 */
	public ToolBarFormulaComp(UfoReport ufoReport) {
		super(ufoReport);
		_ufoReport = ufoReport;
		initialize();
		// @edit by wangyga at 2009-3-14,下午01:40:50 以下代码先关掉，测试新的框架
		setFormulaComp();
//		
//		getCellsModel().getSelectModel().addSelectModelListener(new SelectListener(){
//			public void selectedChanged(SelectEvent e) {
//				if(e.getProperty().equals(SelectEvent.ANCHOR_CHANGED)){
//					if(!isFormatState())
//						setContentTextDoc();
//					setFormulaComp();
//					if(isFormatState()){
//						setFormulaTypeValue();
//					}					
//				}
//			}
//			
//		});
//		
//		getAreaTextField().addKeyListener(new KeyAdapter(){
//			@Override
//			public void keyTyped(KeyEvent e) {
//				super.keyTyped(e);
//				if(e.getKeyChar() == KeyEvent.VK_ENTER){
//					String strAreaPos = getAreaTextField().getText();
//					if(strAreaPos == null || strAreaPos.equals("")){
//						return;
//					}
//					AreaPosition aimArea = TableUtilities.getAreaPosByString(areaTextField.getText());
//		            if(aimArea == null) { // “请输入有效的引用位置或选定区域的有效名称。”
//		            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0001109"), _ufoReport, null);//"引用无效"
//		                return;
//		            }
//		            if(aimArea.isCell()){ 
//		            	_ufoReport.getTable().getCells().getSelectionModel().clear();
//		            	_ufoReport.getTable().getCells().getSelectionModel().setAnchorCell(aimArea.getStart());
//		            	return;
//		            }
//		            _ufoReport.getTable().getCells().getSelectionModel().clear();
//		            _ufoReport.getTable().getCells().getSelectionModel().setAnchorCell(aimArea.getStart());
//		            _ufoReport.getTable().getCells().getSelectionModel().setSelectedArea(aimArea);
//		            
//		            _ufoReport.getTable().getCells().requestFocus();
//		            
//				}
//			}			
//		});
	}

	/**
	 * This method initializes this
	 * @i18n miufo01178=编辑栏
	 * 
	 */
	private void initialize() {
//        this.setName("toolBarFormulaComp");.
		this.setName(StringResource.getStringResource("miufo01178"));
        this.add(getAreaTextField());
        this.add(getCancelButton());
        this.add(getOkButton());
        this.add(getFormulaButton());
        if(isFormatState()){
        	this.add(getFormulaTypeBox());   
        }         
        this.add(getContentTextField());
        _ufoReport.getEventManager().addListener(this);
		
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
					}catch (Exception ex) {
						String area = getCellsModel().getSelectModel().getSelectedArea().toString();
						areaTextField.setText(area);
					}
				}
				
			});
		}
		return areaTextField;
	}

	/**
	 * This method initializes formulaTypeBox	
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getFormulaTypeBox(){
		if(formulaTypeBox == null){
			formulaTypeBox = new UIComboBox(formulaTypeValue);
			formulaTypeBox.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					//取出对应类型的公式添加到编辑框
					int iFormulaType = formulaTypeBox.getSelectedIndex();
					
					FormulaModel formulaModel = getFormulaModel();
					IArea area = getSelectedArea();
					IArea fmlArea = formulaModel.getRelatedFmlArea(area, true);
					if (fmlArea == null) {
						fmlArea = formulaModel.getRelatedFmlArea(area, false);
					}
					if(fmlArea == null)
						return;

					if(iFormulaType == PUBLIC_FORMULA){
						FormulaVO publicCellFormula = formulaModel.getPublicDirectFml(fmlArea);	
						String fmlContent = getSelectedFormulaText(area, publicCellFormula);
						getContentTextField().setText(EQUALS + fmlContent);	
					}else if(iFormulaType == PERSON_FORMULA){
						FormulaVO personCellFormula = formulaModel.getPersonalDirectFml(fmlArea);	
						String fmlContent = getSelectedFormulaText(area, personCellFormula);
						getContentTextField().setText(EQUALS + fmlContent);	
					}else if(iFormulaType == TOTAL_FORMULA){
						FormulaVO totalFormula = formulaModel.getDirectFml(fmlArea, false);
						String fmlContent = getSelectedFormulaText(area, totalFormula);
						getContentTextField().setText(EQUALS + fmlContent);
					}
				}
				
			});
		}
		return formulaTypeBox;
	}
	
	/**
	 * This method initializes contentTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getContentTextField() {
		if (contentTextField == null) {
			contentTextField = new JTextField();
			contentTextField.setDocument(new LengthCtrlDoc(500));
			contentTextField.setPreferredSize(new java.awt.Dimension(600,22));
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
			formulaButton = new JButton();
			formulaButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"calculate.gif"));
			formulaButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {  
					getCancelButton().setEnabled(true);
			        getOkButton().setEnabled(true);
			        new CalcFmlEditCmd().execute(new Object[]{_ufoReport});
					setFormulaComp();
					
				}
			});
		}
		return formulaButton;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"cancel.gif"));
			cancelButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					IArea area = getSelectedArea();
					if(isFormatState() && IufoFormulalUtil.hasFormula(getCellsModel(), area.getStart())){
						FormulaVO formulaVO = IufoFormulalUtil.getSelectedFormula(getCellsModel(), area.getStart());
						String fmlContent = getSelectedFormulaText(area, formulaVO);
						getContentTextField().setText(EQUALS + fmlContent);	
					}else{
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
			okButton = new JButton();
			okButton.setIcon(ResConst.getImageIcon(IMAGEPATH+"ok.gif"));
			okButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {  
					AreaPosition aimArea = AreaPosition.getInstance(areaTextField.getText());
					String strEditCellFormula = contentTextField.getText();
					if(isFormatState() && isInputFormula(strEditCellFormula)){
						setCellFormula(aimArea, strEditCellFormula);										
					}else{
						setCellValue(aimArea, strEditCellFormula,e);
					}
					getCancelButton().setEnabled(false);
			        getOkButton().setEnabled(false);
				}
				
			});
		}
		return okButton;
	}
	
	/**
	 * 将公式添加到指定单元
	 * @param fmlArea
	 * @param strCellFormula
	 */
	private void setCellFormula(AreaPosition fmlArea, String strCellFormula) {
		IArea fmlRealArea = getFormulaModel().getRelatedFmlArea(fmlArea, true);//转化成正确的公式区域
		if (fmlRealArea == null) {
			fmlRealArea = getFormulaModel().getRelatedFmlArea(fmlArea, false);
		}
		if (fmlRealArea != null && !fmlRealArea.isCell()) {
			getCellsModel().getSelectModel().setSelectedArea((AreaPosition) fmlRealArea);
		} else{
			fmlRealArea = fmlArea;
		}
		
		if(strCellFormula != null){
			getCellsModel().clearArea(UFOTable.CELL_CONTENT,new IArea[]{ fmlRealArea});
		}				
		
		if (strCellFormula == null || strCellFormula.trim().equals("") || strCellFormula.trim().equals(EQUALS)){
			getFormulaHandler().clearFormula(fmlRealArea,true);
		}else{
			boolean bAddCellFml=false;
			StringBuffer showErrMessage = new StringBuffer();
			try {
				int index = strCellFormula.indexOf(EQUALS);
				strCellFormula = strCellFormula.substring(index + 1);
				if(!checkFormula(fmlRealArea,strCellFormula)){
					return;
				}
				int nSelectedFmlType = getFormulaTypeBox().getSelectedIndex();
				if(nSelectedFmlType == PUBLIC_FORMULA){
					bAddCellFml = getFormulaHandler()
					.addUserDefFormula(showErrMessage, fmlRealArea, strCellFormula,true,true,false);
				} else if(nSelectedFmlType == PERSON_FORMULA){
					bAddCellFml = getFormulaHandler()
					.addUserDefFormula(showErrMessage, fmlRealArea, strCellFormula,true,false,false);
				} else if(nSelectedFmlType == TOTAL_FORMULA){
					bAddCellFml = getFormulaHandler()
					.addUserDefFormula(showErrMessage, fmlRealArea, strCellFormula,false,false);
				}
				
			} catch (ParseException ex) {
				AppDebug.debug(ex);
				bAddCellFml=false;
			}
			if (bAddCellFml == false)
				getFormulaHandler().clearFormula(fmlRealArea,true);
		}
	}
	
	/**
	 * 将单元文本添加到指定单元值
	 * @param areaPos
	 * @param strCellText
	 */
	private void setCellValue(AreaPosition areaPos, String strCellText,EventObject e) {
		if(isFormatState()){
			if(!isCellEditable(areaPos.getStart(),e)){//不可编辑
				return;
			}
			Cell c = getCellsModel().getCell(areaPos.getStart());
			if(c == null){
				c = new Cell();
				c.setRow(areaPos.getStart().getRow());
				c.setCol(areaPos.getStart().getColumn());
			}			
			c.setValue(strCellText);
			getCellsModel().setCell(areaPos.getStart().getRow(), areaPos.getStart().getColumn(), c);
			
		} else{
			UFOTable table = _ufoReport.getTable();
			CellsPane pane = table.getCells();
			//组合单元其他非首单元格转换为首单元格
			int row = areaPos.getStart().getRow();
			int column = areaPos.getStart().getColumn();
			CombinedCell cc = _ufoReport.getCellsModel().belongToCombinedCell(row, column);
			if (cc != null) {
				row = cc.getArea().getStart().getRow();
				column = cc.getArea().getStart().getColumn();
			}
			//编辑器非空，并且中断当前编辑成功。
			if (!pane.isCellEditable(row, column)) {
				return;
			}

			Object cellEditorValue = null;
			MeasureFmt fmt = (MeasureFmt)_ufoReport.getCellsModel().getBsFormat(CellPosition.getInstance(row,column),MeasureFmt.EXT_FMT_MEASUREINPUT);
			if(fmt != null){
				if(fmt.getType() == MeasureFmt.TYPE_NUMBER){
					if(isNumber(strCellText))
						cellEditorValue = new Double(strCellText);
				}else if(fmt.getType() == MeasureFmt.TYPE_CHAR){
					cellEditorValue = strCellText;
				}else if(fmt.getType() == MeasureFmt.TYPE_REF_CODE){
					//暂不支持参照型数据的直接录入
				}else if(fmt.getType() == MeasureFmt.TYPE_REF_DATE){
					//暂不支持参照型数据的直接录入
				}else{
					throw new IllegalArgumentException();
				}
			}

			if(cellEditorValue != null){
				pane.setValueAt(cellEditorValue, row, column);
				getContentTextField().setToolTipText(cellEditorValue.toString());
			}
			
		}
		
	}
	
	/** add by wangyga 数据态根据指标的类型，设置contentText的文本类型
	 * 设置contentText的文本类型:字符，数字，日期，表样
	 */
	private void setContentTextDoc(){
		CellsModel cellsModel = getCellsModel();
		CellPosition pos = cellsModel.getSelectModel().getAnchorCell();
		MeasureFmt fmt = (MeasureFmt)cellsModel.getBsFormat(pos, MeasureFmt.EXT_FMT_MEASUREINPUT);		
		if(fmt == null)
			return;
		if(fmt.getType() == MeasureFmt.TYPE_CHAR){
			getContentTextField().setDocument(new LengthCtrlDoc(fmt.getCharLength()));
		}else{
			getContentTextField().setDocument(new PlainDocument());
		}
	}
	
	/**
	 * 检查编辑数据是否是数学
	 * @param sample
	 * @return
	 */
	private boolean isNumber(String sample) {
		boolean hasDecimal = false;
		for(int i=0;i<sample.length();i++){
			char ch = sample.charAt(i);
			if(ch == '.' && hasDecimal == true){
				return false;
			}

			hasDecimal = (ch == '.')?true:hasDecimal;
			if(Character.isDigit(ch) || ch == '.'){
				continue;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 根据选择区域更新工具栏公式组件显示状态
	 */
	private void setFormulaComp(){
		//选择模型锚点只记录选择区域的第一个单元格，所以得到选择区域时
		//只能根据选择锚点通过CellsModel的getArea方法计算
		IArea area = getSelectedArea();
		if (area==null)
			return;
		
		getAreaTextField().setText(area.toString());
		if(isFormatState() && IufoFormulalUtil.hasFormula(getCellsModel(), area.getStart())){
			FormulaVO formulaVO = IufoFormulalUtil.getSelectedFormula(getCellsModel(), area.getStart());
			String fmlContent = getSelectedFormulaText(area, formulaVO);
			getContentTextField().setText(EQUALS + fmlContent);	
		} else{
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
		// add by 王宇光 2008-5-4 修改单元数据为double类型，且为8位以上时，工具栏上不用科学计数法		
		if (value instanceof Double) {
			IufoFormat format = (IufoFormat) getCellsModel().getCellIfNullNew(
					cell.getRow(),cell.getColumn()).getFormat();
			if (format != null) {				
				value = format.getString((Double) value);
			}
		}
		return value != null ? value.toString() : "";
	}
	
	private class LengthCtrlDoc extends PlainDocument {
        private static final long serialVersionUID = 5507933420106543610L;
        private final char[] forbidChars = new char[]{'&'}; 
        private int charLength = 64;
        public LengthCtrlDoc(int charLength) {
            this.charLength = charLength;
        }
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            for (int i = 0; i < forbidChars.length; i++) {
                if(str != null && str.indexOf(forbidChars[i]) != -1){
                    return;
                }
            }
            String name = getText(0, offs) + str
                        + getText(offs, getLength() - offs);            
            if (name.length() > charLength) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offs, str, a);
        }
    }
	
	/**
	 * 返回锚点选定的区域(如果是区域公式，则参数应该为定义区域区域)
	 * @return
	 */
	private IArea getSelectedArea(){
		//选择模型锚点只记录选择区域的第一个单元格，所以得到选择区域时
		//只能根据选择锚点通过CellsModel的getArea方法计算
//		IArea area = getCellsModel().getArea(getCellsModel().getSelectModel().getAnchorCell());
		if(getCellsModel()==null || getCellsModel().getSelectModel()==null)
			return null;
		
		IArea area = getCellsModel().getSelectModel().getSelectedArea();
		//modify by guogang 2007-11-7 如果有区域公式的话选择区域则不为空
		AreaPosition selArea = getCellsModel().getSelectModel().getSelectedArea();
		if(selArea!=null){
			area=selArea;
		}
		//modify end
		return area;

	}
	
	private boolean checkFormula(IArea area,String strFmlContent){
		if(strFmlContent == null || strFmlContent.trim().length() ==0)
			return true;
		try {
			getFormulaHandler().parseUserDefFormula(area, strFmlContent);
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}		
		return true;
	}
	
	/**
	 * 返回选定公式的显示文本
	 * @param area 公式定义区域(如果是区域公式，则参数应该为定义区域区域)
	 * @param formulaVO 公式对象
	 * @return
	 */
	private String getSelectedFormulaText(IArea area, FormulaVO formulaVO){
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
		DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(area.getStart());
		String dynAreaPK = dynAreaCell == null ? DynAreaVO.MAINTABLE_DYNAREAPK : dynAreaCell.getDynAreaVO().getDynamicAreaPK();
		return  (formulaVO == null || getFormulaHandler()==null)? 
				"" : getFormulaHandler().getUserDefFmlContent(formulaVO,area,dynAreaPK);

	}

	private UfoFmlExecutor getFormulaHandler(){
		FormulaModel formulaModel = getFormulaModel();		
		return formulaModel.getUfoFmlExecutor();
	}
	
	/**
	 * 检查公式编辑框录入值是否为公式
	 * modify by wangyga
	 * 如果第一个字符是"="号,且只有一个"="符合认为是公式(可以包含多个等号，第一个是等号时就是编辑公式)
	 * 
	 * @param inputFormulaStr 公式编辑框中录入字符串
	 * @return
	 */
	private boolean isInputFormula(String inputFormulaStr){
		if(inputFormulaStr == null || inputFormulaStr.trim().length() < 2){
			return false;
		}
		return (inputFormulaStr.trim().indexOf('=') == 0)?true:false;
		
	}
	
	/**
	 * 设置下拉列表的公式类型
	 */
	private void setFormulaTypeValue(){
		FormulaModel formulaModel = getFormulaModel();
		IArea area = getSelectedArea();
		IArea fmlArea = formulaModel.getRelatedFmlArea(area, true);
		if (fmlArea == null) {
			fmlArea = formulaModel.getRelatedFmlArea(area, false);
		}
		if(fmlArea == null)
			return;
		
		FormulaVO publicCellFormula = formulaModel.getPublicDirectFml(fmlArea);				
		FormulaVO personCellFormula = formulaModel.getPersonalDirectFml(fmlArea);				
		FormulaVO totalFormula = formulaModel.getDirectFml(fmlArea, false);
				
		if(personCellFormula != null){
			getFormulaTypeBox().setSelectedIndex(PERSON_FORMULA);
			return;
		}
		if(publicCellFormula != null){
			getFormulaTypeBox().setSelectedIndex(PUBLIC_FORMULA);
			return;
		}
		if(totalFormula != null){
			getFormulaTypeBox().setSelectedIndex(TOTAL_FORMULA);
			return;
		}
			
		getFormulaTypeBox().setSelectedIndex(PUBLIC_FORMULA);
	}
	
	/**
	 * 获得某位置的编辑器
	 * @param row
	 * @param column
	 * @return
	 */
	private SheetCellEditor getCellEditor(CellPosition pos) {
			return _ufoReport.getTable().getCells().getCellEditor(pos);
	}
	
	private boolean isCellEditable(CellPosition pos,EventObject anEvent){
		SheetCellEditor editor = getCellEditor(pos);
		return editor.isCellEditable(anEvent);
	}
	
	private FormulaModel getFormulaModel(){		
		return FormulaModel.getInstance(getCellsModel());
	}
	
	/**
	 * 当前是否格式设计状态
	 * @return
	 */
	private boolean isFormatState(){
		return _ufoReport.getOperationState() == UfoReport.OPERATION_FORMAT;
	}
	
	private CellsModel getCellsModel(){
		return _ufoReport.getCellsModel();
	}

	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		if (!isFormatState())
			setContentTextDoc();
		setFormulaComp();
		if (isFormatState()) {
			setFormulaTypeValue();
		}

	}

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {
		// TODO Auto-generated method stub
		
	}
	
}
 