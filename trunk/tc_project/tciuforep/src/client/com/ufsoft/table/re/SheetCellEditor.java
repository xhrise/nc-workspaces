package com.ufsoft.table.re;

import java.awt.*;
import javax.swing.*;
import com.ufsoft.table.*;

/**
 * <p>Title: 表格编辑器接口.</p>
 * <p>Description: 表格控件使用的编辑器实现该接口</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public interface SheetCellEditor
    extends CellEditor {
/**
 * 
 * @param table 表格
 * @param value 其类型为数据的实际类型，即Cell中的value值和扩展数据值。
 * @param isSelected 是否被选中
 * @param row 行位置
 * @param column 列位置
 * @return Component
 */
  Component getTableCellEditorComponent(CellsPane table, Object value,
                                        boolean isSelected,
                                        int row, int column);
  /**
   * 得到当前编辑器的优先级，系统预制的编辑器优先级为0，业务编辑器优先级自己定，数值越大优先级越高。
   * @return int
   */
  public int getEditorPRI();
  
  boolean isEnabled(CellsModel cellsModel, CellPosition cellPos);

}