package com.ufsoft.table.re;

import java.awt.*;

import com.ufsoft.table.*;

/**
 * <p>
 * Title:描述表页中单元的渲染器
 * </p>
 * <p>
 * Description: 表格控件
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public interface SheetCellRenderer {

	/**
	 * 得到绘制一个单元的组件。可以根据单元的位置来设置不同的渲染器。
	 * 
	 * @param cellsPane
	 *            CellsPane 请求绘制组件的Sheet
	 * @param value
	 *            Object 绘制的内容(无论是控件绘制器，还是业务绘制器，实际类型都为对应的数据。
	 *            在控件绘制器中需要格式对象，可以由参数cellsPane和row、column间接得到。
	 *            对Cell扩展格式，则此值对应扩展格式内容
	 *            )
	 * @param isSelected
	 *            boolean 当前是否被选中
	 * @param hasFocus
	 *            boolean 是否是获得焦点的单元
	 * @param row
	 *            int 所处行位置。
	 * @param column
	 *            int 所处列位置.
	 * @return Component 用于绘制的组件。该组件只是用于绘制，绘制后从父组件中删除，但是父组件不会重绘。
	 * 所以，该组件可以共用。
	 */
	Component getCellRendererComponent(CellsPane cellsPane, Object value,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell);

}