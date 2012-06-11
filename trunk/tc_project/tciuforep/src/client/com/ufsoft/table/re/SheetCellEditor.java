package com.ufsoft.table.re;

import java.awt.*;
import javax.swing.*;
import com.ufsoft.table.*;

/**
 * <p>Title: ���༭���ӿ�.</p>
 * <p>Description: ���ؼ�ʹ�õı༭��ʵ�ָýӿ�</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public interface SheetCellEditor
    extends CellEditor {
/**
 * 
 * @param table ���
 * @param value ������Ϊ���ݵ�ʵ�����ͣ���Cell�е�valueֵ����չ����ֵ��
 * @param isSelected �Ƿ�ѡ��
 * @param row ��λ��
 * @param column ��λ��
 * @return Component
 */
  Component getTableCellEditorComponent(CellsPane table, Object value,
                                        boolean isSelected,
                                        int row, int column);
  /**
   * �õ���ǰ�༭�������ȼ���ϵͳԤ�Ƶı༭�����ȼ�Ϊ0��ҵ��༭�����ȼ��Լ�������ֵԽ�����ȼ�Խ�ߡ�
   * @return int
   */
  public int getEditorPRI();
  
  boolean isEnabled(CellsModel cellsModel, CellPosition cellPos);

}