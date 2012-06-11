package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.vo.iufo.authorization.IAuthorizeTypes;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.formula.FormulaInputToolBar;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.SheetCellEditor;

/**
 * IUFO 格式编辑态的公式条组件,取代了V55之前的ToolBarFormulaComp
 * 
 * @author zhaopq
 * @created at 2009-4-9,下午02:26:17
 * @since v5.6
 */
public class FormulaDefToolBar extends FormulaInputToolBar{

	private static final long serialVersionUID = -5470734974520096681L;

	private JComboBox formulaTypeBox;

	private static final int PUBLIC_FORMULA = 0;

	private static final int PERSON_FORMULA = 1;

	private static final int TOTAL_FORMULA = 2;

	/**
	 * @i18n miufofunc001=公有公式(fc)
	 */
	private static final DefaultConstEnum PUBLIC_ENMU = new DefaultConstEnum(PUBLIC_FORMULA,StringResource.getStringResource("miufofunc001"));
	
	/**
	 * @i18n miufo00661=私有公式(fm)
	 */
	private static final DefaultConstEnum PERSON_ENMU = new DefaultConstEnum(PERSON_FORMULA,StringResource.getStringResource("miufo00661"));
	
	/**
	 * @i18n miufo1000910=汇总公式(fs)
	 */
	private static final DefaultConstEnum TOTAL_ENMU = new DefaultConstEnum(TOTAL_FORMULA,StringResource.getStringResource("miufo1000910"));
	
	private final static DefaultConstEnum[] FORMULA_ALL_TYPES = new DefaultConstEnum[] {
			PUBLIC_ENMU, PERSON_ENMU, TOTAL_ENMU };
 
	private final static DefaultConstEnum[] FORMULA_PERSON_TYPES = new DefaultConstEnum[] { PERSON_ENMU, };

	private final static DefaultConstEnum[] FORMULA_NO_CREATE_UNIT_TYPES = new DefaultConstEnum[] {
			PERSON_ENMU, TOTAL_ENMU };
	
	private final static DefaultConstEnum[] FORMULA_ANA_REP_TYPES = new DefaultConstEnum[] {
		PUBLIC_ENMU, PERSON_ENMU};
	
	public FormulaDefToolBar(Mainboard mainBoard) {
		super();
		setMainBoard(mainBoard);
		mainBoard.getEventManager().addListener(this);
		initialize();
		super.doCommonInit();
		mainBoard.getEventManager().addListener(createEventHandler());
	}
	
	protected void initialize() {
		add(getAreaTextField());
		add(getFormulaButton());
		add(getFormulaTypeBox());
		add(getContentTextField());
		add(getOkButton());
		add(getCancelButton());
	}

	private JComboBox getFormulaTypeBox() {
		if (formulaTypeBox == null) {
			formulaTypeBox = new JComboBox(isAnaRep() ? FORMULA_ANA_REP_TYPES : FORMULA_ALL_TYPES);
			formulaTypeBox.setPreferredSize(new Dimension(100,21));
			formulaTypeBox.setMaximumSize(new Dimension(100,21));
			formulaTypeBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					setFormulaContent();
				}
			});
		}
		return formulaTypeBox;
	}

	private void setFormulaContent(){
		// 取出对应类型的公式添加到编辑框
		int iFormulaType = new Integer(((DefaultConstEnum)getFormulaTypeBox().getSelectedItem()).getValue().toString());
		
		FormulaModel formulaModel = getFormulaModel();
		IArea area = getSelectedArea();
		IArea fmlArea = formulaModel.getRelatedFmlArea(area, true);
		if (fmlArea == null) {
			fmlArea = formulaModel.getRelatedFmlArea(area, false);
		}
		if (fmlArea == null)
			return;
		String fmlContent ="";
		if (iFormulaType == PUBLIC_FORMULA) {
			FormulaVO publicCellFormula = formulaModel
					.getPublicDirectFml(fmlArea);
			fmlContent = getSelectedFormulaText(area,
					publicCellFormula);
		} else if (iFormulaType == PERSON_FORMULA) {
			FormulaVO personCellFormula = formulaModel
					.getPersonalDirectFml(fmlArea);
			fmlContent = getSelectedFormulaText(area,
					personCellFormula);
		} else if (iFormulaType == TOTAL_FORMULA) {
			FormulaVO totalFormula = formulaModel.getDirectFml(
					fmlArea, false);
			fmlContent = getSelectedFormulaText(area,
					totalFormula);
		}
		getContentTextField().setText(EQUALS + fmlContent);
	}
	
	/**
	 * 将公式添加到指定单元
	 * 
	 * @param fmlArea
	 * @param strCellFormula
	 */
	protected void setCellFormula(AreaPosition fmlArea, String strCellFormula) {
		IArea fmlRealArea = getFormulaModel().getRelatedFmlArea(fmlArea, true);// 转化成正确的公式区域
		if (fmlRealArea == null) {
			fmlRealArea = getFormulaModel().getRelatedFmlArea(fmlArea, false);
		}
		if (fmlRealArea != null && !fmlRealArea.isCell()) {
			getCellsModel().getSelectModel().setSelectedArea(
					(AreaPosition) fmlRealArea);
		} else {
			fmlRealArea = fmlArea;
		}

		if (strCellFormula != null) {
			getCellsModel().clearArea(UFOTable.CELL_CONTENT,
					new IArea[] { fmlRealArea });
		}

		if (strCellFormula == null || strCellFormula.trim().equals("")
				|| strCellFormula.trim().equals(EQUALS)) {
			getUfoFmlExecutor().clearFormula(fmlRealArea, true);
		} else {
			boolean bAddCellFml = false;
			StringBuffer showErrMessage = new StringBuffer();
			try {
				int index = strCellFormula.indexOf(EQUALS);
				strCellFormula = strCellFormula.substring(index + 1);
				if (!checkFormula(fmlRealArea, strCellFormula)) {
					return;
				}
				int nSelectedFmlType = new Integer(((DefaultConstEnum)getFormulaTypeBox().getSelectedItem()).getValue().toString());
				if (nSelectedFmlType == PUBLIC_FORMULA) {
					bAddCellFml = getUfoFmlExecutor().addUserDefFormula(
							showErrMessage, fmlRealArea, strCellFormula, true,
							true, false);
				} else if (nSelectedFmlType == PERSON_FORMULA) {
					bAddCellFml = getUfoFmlExecutor().addUserDefFormula(
							showErrMessage, fmlRealArea, strCellFormula, true,
							false, false);
				} else if (nSelectedFmlType == TOTAL_FORMULA) {
					bAddCellFml = getUfoFmlExecutor().addUserDefFormula(
							showErrMessage, fmlRealArea, strCellFormula, false,
							false);
				}

			} catch (ParseException ex) {
				AppDebug.debug(ex);
				bAddCellFml = false;
			}
			if (bAddCellFml == false)
				getUfoFmlExecutor().clearFormula(fmlRealArea, true);
		}
	}

	/**
	 * 将单元文本添加到指定单元值
	 * 
	 * @param areaPos
	 * @param strCellText
	 */
	protected void setCellValue(AreaPosition areaPos, String strCellText,
			EventObject e) {
		if (!isCellEditable(areaPos.getStart(), e)) {// 不可编辑
			return;
		}
		Cell c = getCellsModel().getCell(areaPos.getStart());
		if (c == null) {
			c = new Cell();
			c.setRow(areaPos.getStart().getRow());
			c.setCol(areaPos.getStart().getColumn());
		}
		Format format = getCellsModel().getFormatIfNullNew(areaPos.getStart());
		if(format.getCellType() == TableConstant.CELLTYPE_NUMBER){
			try {
				c.setValue(new Double(strCellText));
				return;
			} catch (Exception e2) {
				AppDebug.debug(e2);
			}
		}
		c.setValue(strCellText);
	}

	/**
	 * 根据选择区域更新工具栏公式组件显示状态
	 */
	protected void setFormulaComp() {
		// 选择模型锚点只记录选择区域的第一个单元格，所以得到选择区域时
		// 只能根据选择锚点通过CellsModel的getArea方法计算
		IArea area = getSelectedArea();
		if (area == null)
			return;

		getAreaTextField().setText(area.toString());
		if (IufoFormulalUtil.hasFormula(getCellsModel(), area.getStart())) {
			setFormulaContent();
//			FormulaVO formulaVO = IufoFormulalUtil.getSelectedFormula(
//					getCellsModel(), area.getStart());
//			String fmlContent = getSelectedFormulaText(area, formulaVO);
////			
////			
//			getContentTextField().setText(EQUALS + fmlContent);
		} else {
			CellPosition anchorPos = getCellsModel().getSelectModel()
					.getAnchorCell();
			getContentTextField().setText(getFormatValue(anchorPos));
		}

		getFormulaButton().setEnabled(true);
		getCancelButton().setEnabled(false);
		getOkButton().setEnabled(false);
	}

	private boolean checkFormula(IArea area, String strFmlContent) {
		if (strFmlContent == null || strFmlContent.trim().length() == 0)
			return true;
		try {
			getUfoFmlExecutor().parseUserDefFormula(area, strFmlContent);
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}
		return true;
	}

	/**
	 * 设置下拉列表的公式类型
	 */
	private void setFormulaTypeValue() {
		FormulaModel formulaModel = getFormulaModel();
		IArea area = getSelectedArea();
		IArea fmlArea = formulaModel.getRelatedFmlArea(area, true);
		if (fmlArea == null) {
			fmlArea = formulaModel.getRelatedFmlArea(area, false);
		}
		if (fmlArea == null)
			return;

		FormulaVO publicCellFormula = formulaModel.getPublicDirectFml(fmlArea);
		FormulaVO personCellFormula = formulaModel
				.getPersonalDirectFml(fmlArea);
		FormulaVO totalFormula = formulaModel.getDirectFml(fmlArea, false);

		if (personCellFormula != null) {
			getFormulaTypeBox().setSelectedItem(PERSON_ENMU);
			return;
		}
		if (publicCellFormula != null) {
			getFormulaTypeBox().setSelectedItem(PUBLIC_ENMU);
			return;
		}
		if (totalFormula != null) {
			getFormulaTypeBox().setSelectedItem(TOTAL_ENMU);
			return;
		}

		getFormulaTypeBox().setSelectedItem(PUBLIC_ENMU);
	}

	private boolean isCellEditable(CellPosition pos, EventObject anEvent) {
		SheetCellEditor editor = getCellsPane().getCellEditor(pos);
		if (editor == null) {
			return false;
		}
		return editor.isCellEditable(anEvent);
	}
	
	private boolean isAnaRep(){
		int iRepType = -1;
		Object obj = getMainBoard().getContext().getAttribute(ReportContextKey.REPORT_TYPE);
		if (obj == null) {
			return false;
		}
		try {
			iRepType = Integer.parseInt(obj.toString());
		} catch (Throwable e) {
			AppDebug.debug(e);
			return false;
		}
		
		return iRepType == ReportContextKey.REPORT_TYPE_ANALYSIS ? true : false;
	}
	
	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		setFormulaTypeValue();
		setFormulaComp();

	}
	
	protected EventListener createEventHandler(){
		return new EventHandler();
	}
	
	private class EventHandler extends ViewerListener.Sub{

		@Override
		public void onActive(Viewer currentActiveView, Viewer oldActiveView) {

			DefaultConstEnum[] formulaTypes = getFormulaTypes(currentActiveView);
		
			getFormulaTypeBox().setModel(new DefaultComboBoxModel(formulaTypes));
		}
		
		private DefaultConstEnum[] getFormulaTypes(Viewer viewer){
			IContext context = viewer.getContext();
			if (IufoFormulalUtil.isCreateUnit(context)) {
				return isAnaRep() ? FORMULA_ANA_REP_TYPES : FORMULA_ALL_TYPES;
			}
			if ((Integer) context.getAttribute(IUfoContextKey.FORMAT_RIGHT) >= IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY) {
				return isAnaRep()? FORMULA_PERSON_TYPES : FORMULA_NO_CREATE_UNIT_TYPES;
			} else {
				return FORMULA_PERSON_TYPES;
			}
		}
		
	}
}
