package com.ufsoft.iufo.inputplugin.formula;

import java.awt.Toolkit;
import java.util.EventObject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaEditAction;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.IufoFormulalUtil;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.fmtplugin.formula.AreaFormulaToolBar;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IArea;
import com.ufsoft.table.event.CellsModelSelectedListener;

/**
 * IUFO 数据录入态的公式条组件,取代了V55之前的ToolBarFormulaComp
 * 
 * @author zhaopq
 * @created at 2009-4-9,上午11:00:17
 * @since v5.6
 */
public class FormulaInputToolBar extends AreaFormulaToolBar implements
		CellsModelSelectedListener {

	private static final long serialVersionUID = 1035757899003882550L;
	
	public FormulaInputToolBar(){
		super();
		
	}

	public FormulaInputToolBar(Mainboard mainBoard) {
		super(mainBoard);
		getContentTextField().setDocument(new LengthCtrlDoc(500));
		
	}
	
	protected void initialize() {
		add(getAreaTextField());
		add(getOkButton());
		add(getCancelButton());
		add(getContentTextField());
	}
	
	@Override
	protected void executeFormulaCmd() {
		// @edit by wangyga at 2009-6-3,上午10:09:05 此处直接调用action,没有通过框架同一生成,缺少容器
		new FormulaEditAction(){
			@Override
			protected CellsPane getCellsPane() {
				return FormulaInputToolBar.this.getCellsPane();
			}
		}.execute(null);
	}

	protected boolean isFormulaCell(IArea area) {
		return IufoFormulalUtil.hasFormula(getCellsModel(), area.getStart());
	}

	protected String getSelectedFormulaText(IArea area) {
		FormulaVO formulaVO = IufoFormulalUtil.getSelectedFormula(
				getCellsModel(), area.getStart());
		return getSelectedFormulaText(area, formulaVO);
	}

	/**
	 * 将单元文本添加到指定单元值
	 * 
	 * @param areaPos
	 * @param strCellText
	 */
	protected void setCellValue(AreaPosition areaPos, String strCellText,
			EventObject e) {
		// 组合单元其他非首单元格转换为首单元格
		int row = areaPos.getStart().getRow();
		int column = areaPos.getStart().getColumn();
		CombinedCell cc = getCellsModel().getCombinedAreaModel()
				.belongToCombinedCell(row, column);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			column = cc.getArea().getStart().getColumn();
		}
		// 编辑器非空，并且中断当前编辑成功。
		if (!getCellsPane().isCellEditable(row, column)) {
			return;
		}

		Object cellEditorValue = null;
		MeasureFmt fmt = (MeasureFmt) getCellsModel().getBsFormat(
				CellPosition.getInstance(row, column),
				MeasureFmt.EXT_FMT_MEASUREINPUT);
		if (fmt != null) {
			if (fmt.getType() == MeasureFmt.TYPE_NUMBER) {
				if (isNumber(strCellText))
					cellEditorValue = new Double(strCellText);
			} else if (fmt.getType() == MeasureFmt.TYPE_CHAR) {
				cellEditorValue = strCellText;
			} else if (fmt.getType() == MeasureFmt.TYPE_REF_CODE) {
				// 暂不支持参照型数据的直接录入
			} else if (fmt.getType() == MeasureFmt.TYPE_REF_DATE) {
				// 暂不支持参照型数据的直接录入
			} else {
				throw new IllegalArgumentException();
			}
		}

		if (cellEditorValue != null) {
			getCellsPane().setValueAt(cellEditorValue, row, column);
			getContentTextField().setToolTipText(cellEditorValue.toString());
		}
	}

	/**
	 * add by wangyga 数据态根据指标的类型，设置contentText的文本类型
	 * 设置contentText的文本类型:字符，数字，日期，表样
	 */
	private void setContentTextDoc() {
		CellsModel cellsModel = getCellsModel();
		if(cellsModel==null){
			return;
		}
		CellPosition pos = cellsModel.getSelectModel().getAnchorCell();
		MeasureFmt fmt = (MeasureFmt) cellsModel.getBsFormat(pos,
				MeasureFmt.EXT_FMT_MEASUREINPUT);
		if (fmt == null)
			return;
		if (fmt.getType() == MeasureFmt.TYPE_CHAR) {
			getContentTextField().setDocument(
					new LengthCtrlDoc(fmt.getCharLength()));
		} else {
			getContentTextField().setDocument(new PlainDocument());
		}
	}

	/**
	 * 检查编辑数据是否是数学
	 * 
	 * @param sample
	 * @return
	 */
	private boolean isNumber(String sample) {
		boolean hasDecimal = false;
		for (int i = 0; i < sample.length(); i++) {
			char ch = sample.charAt(i);
			if (ch == '.' && hasDecimal == true) {
				return false;
			}

			hasDecimal = (ch == '.') ? true : hasDecimal;
			if (Character.isDigit(ch) || ch == '.') {
				continue;
			}
			return false;
		}
		return true;
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
		CellPosition anchorPos = getCellsModel().getSelectModel()
				.getAnchorCell();
		getContentTextField().setText(getFormatValue(anchorPos));

//		getFormulaButton().setEnabled(false);
//		getCancelButton().setEnabled(false);
//		getOkButton().setEnabled(false);
	}

	protected String getFormatValue(CellPosition cell) {
		if (cell == null)
			return null;
		Object value = getCellsModel().getCellValue(cell);
		// add by 王宇光 2008-5-4 修改单元数据为double类型，且为8位以上时，工具栏上不用科学计数法
		if (value instanceof Double) {
			IufoFormat format = (IufoFormat) getCellsModel().getCellIfNullNew(
					cell.getRow(), cell.getColumn()).getFormat();
			if (format != null) {
				value = format.getString((Double) value);
			}
		}
		return value != null ? value.toString() : "";
	}

	/**
	 * 返回选定公式的显示文本
	 * 
	 * @param area
	 *            公式定义区域(如果是区域公式，则参数应该为定义区域区域)
	 * @param formulaVO
	 *            公式对象
	 * @return
	 */
	protected String getSelectedFormulaText(IArea area, FormulaVO formulaVO) {
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
		DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(area
				.getStart());
		String dynAreaPK = dynAreaCell == null ? DynAreaVO.MAINTABLE_DYNAREAPK
				: dynAreaCell.getDynAreaVO().getDynamicAreaPK();
		return (formulaVO == null || getUfoFmlExecutor() == null) ? ""
				: getUfoFmlExecutor().getUserDefFormula(area,formulaVO.getFormulaContent(), dynAreaPK);
	}
	
	protected UfoFmlExecutor getUfoFmlExecutor() {
		return getFormulaModel().getUfoFmlExecutor();
	}

	protected FormulaModel getFormulaModel() {
		return FormulaModel.getInstance(getCellsModel());
	}

	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		setContentTextDoc();
		setFormulaComp();
	}

	private class LengthCtrlDoc extends PlainDocument {
		private static final long serialVersionUID = 5507933420106543610L;

		private final char[] forbidChars = new char[] { '&' };

		private int charLength = 64;

		public LengthCtrlDoc(int charLength) {
			this.charLength = charLength;
		}

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			for (int i = 0; i < forbidChars.length; i++) {
				if (str != null && str.indexOf(forbidChars[i]) != -1) {
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

}
