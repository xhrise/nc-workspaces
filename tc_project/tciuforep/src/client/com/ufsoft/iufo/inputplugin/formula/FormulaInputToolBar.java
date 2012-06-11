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
 * IUFO ����¼��̬�Ĺ�ʽ�����,ȡ����V55֮ǰ��ToolBarFormulaComp
 * 
 * @author zhaopq
 * @created at 2009-4-9,����11:00:17
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
		// @edit by wangyga at 2009-6-3,����10:09:05 �˴�ֱ�ӵ���action,û��ͨ�����ͬһ����,ȱ������
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
	 * ����Ԫ�ı���ӵ�ָ����Ԫֵ
	 * 
	 * @param areaPos
	 * @param strCellText
	 */
	protected void setCellValue(AreaPosition areaPos, String strCellText,
			EventObject e) {
		// ��ϵ�Ԫ�������׵�Ԫ��ת��Ϊ�׵�Ԫ��
		int row = areaPos.getStart().getRow();
		int column = areaPos.getStart().getColumn();
		CombinedCell cc = getCellsModel().getCombinedAreaModel()
				.belongToCombinedCell(row, column);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			column = cc.getArea().getStart().getColumn();
		}
		// �༭���ǿգ������жϵ�ǰ�༭�ɹ���
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
				// �ݲ�֧�ֲ��������ݵ�ֱ��¼��
			} else if (fmt.getType() == MeasureFmt.TYPE_REF_DATE) {
				// �ݲ�֧�ֲ��������ݵ�ֱ��¼��
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
	 * add by wangyga ����̬����ָ������ͣ�����contentText���ı�����
	 * ����contentText���ı�����:�ַ������֣����ڣ�����
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
	 * ���༭�����Ƿ�����ѧ
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
	 * ����ѡ��������¹�������ʽ�����ʾ״̬
	 */
	protected void setFormulaComp() {
		// ѡ��ģ��ê��ֻ��¼ѡ������ĵ�һ����Ԫ�����Եõ�ѡ������ʱ
		// ֻ�ܸ���ѡ��ê��ͨ��CellsModel��getArea��������
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
		// add by ����� 2008-5-4 �޸ĵ�Ԫ����Ϊdouble���ͣ���Ϊ8λ����ʱ���������ϲ��ÿ�ѧ������
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
	 * ����ѡ����ʽ����ʾ�ı�
	 * 
	 * @param area
	 *            ��ʽ��������(���������ʽ�������Ӧ��Ϊ������������)
	 * @param formulaVO
	 *            ��ʽ����
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
