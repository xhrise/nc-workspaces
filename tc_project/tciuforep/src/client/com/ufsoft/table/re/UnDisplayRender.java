package com.ufsoft.table.re;

import java.awt.Component;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
/**
 * “不显示任何信息”的渲染器。
 * @author zzl 2005-8-29
 */
public class UnDisplayRender implements SheetCellRenderer {

    public Component getCellRendererComponent(CellsPane cellsPane, Object obj,
             boolean isSelected, boolean hasFocus, int row,
            int column, Cell cell) {
        return null;
    }

}
