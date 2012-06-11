package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.Format;

/**
 * 字符串类型的绘制器.
 * 该绘制器在格式设计时使用，在原有DefaultSheetCellRenderer的功能
 * 基础上增加日期类型指标绘制的分支判断。
 * @author chxiaowei 2007-3-26
 */
public class StringRender extends DefaultSheetCellRenderer {

	static final long serialVersionUID = -8208550136822316738L;

	@Override
	public Component getCellRendererComponent(CellsPane table,Object value,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		JComponent editorComp = (JComponent) super.getCellRendererComponent(
				table,value, isSelected, hasFocus, row, column, cell);
		return editorComp;
	}

	/**
	 * @see com.ufsoft.table.re.DefaultSheetCellRenderer#decorateValue(java.lang.Object,
	 *      com.ufsoft.table.format.Format)
	 */
	protected String decorateValue(Component render, Object value, Format format) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

}
