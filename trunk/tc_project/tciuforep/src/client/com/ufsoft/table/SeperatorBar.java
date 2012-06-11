package com.ufsoft.table;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ufsoft.table.header.*;

/**
 * <p>Title: ���ҳ��ķ��������</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ufsoft</p>
 * @author wupeng
 * @version 1.0
 */

public class SeperatorBar
    extends JComponent {
  private static final long serialVersionUID = 1L;
	
  private HeaderModel m_HeaderModel;
  private boolean m_bRowHeader;
  private TablePane m_table;
  /**
 * @param bRow
 * @param table
 * @return SeperatorBar
 */
  public static SeperatorBar createSeperatorBar(boolean bRow, TablePane table) {
    SeperatorBar sep = new SeperatorBar(bRow, table);
    return sep;
  }

  private SeperatorBar(boolean bRow, TablePane table) {
    super();
    if (table == null) {
      throw new IllegalArgumentException();
    }
    if (bRow) {
      m_HeaderModel = table.getCells().getDataModel().getRowHeaderModel();
    }
    else {
      m_HeaderModel = table.getCells().getDataModel().getColumnHeaderModel();
    }

    m_bRowHeader = m_HeaderModel.getType() == Header.ROW;
    m_table = table;
    addMouseMotionListener(new MotionListener());
    //FIXME ��Ҫʵ�ֻ���.
  }

  

   /**
   * @param index
   */
  public void setCurPos(int index) {
    if (index != getCurPos()) {
      if (m_bRowHeader) {
        m_table.setSeperatePos(index,-1);
      }
      else {
        m_table.setSeperatePos(-1,index);
      }
    }
  }

   /**
   * @return int
   */
  public int getCurPos() {
    return m_bRowHeader ? m_table.getSeperateRow() : m_table.getSeperateCol();
  }

  private class MotionListener
      implements MouseMotionListener {
    /**
     * 
     */
    public MotionListener() {
    }

    public void mouseDragged(MouseEvent e) {
      Point p = e.getPoint();
      //�ж��ƶ���λ���Ƿ񵽴��µ����С�
      // @edit by wangyga at 2009-9-8,����10:17:00 �޸��϶�ʱ��Ч��
      int value = m_bRowHeader ? p.y + getSeperateY(): p.x + getSeperateX();
      int index = m_HeaderModel.getIndexByPosition(value);
      setCurPos(index);
    }

    public void mouseMoved(MouseEvent e) {

      //�޸Ĺ��
      setCursor(Cursor.getPredefinedCursor(m_bRowHeader ?
                                           Cursor.N_RESIZE_CURSOR :
                                           Cursor.W_RESIZE_CURSOR));
    }
    
    /**
     * 
     * @create by wangyga at 2009-9-8,����10:16:19
     *
     * @return
     */
    private int getSeperateY(){
    	return m_HeaderModel.getCellsModel().getSeperateLockSet().getSeperateY();
    }
    
    /**
     * 
     * @create by wangyga at 2009-9-8,����10:16:22
     *
     * @return
     */
    private int getSeperateX(){
    	return m_HeaderModel.getCellsModel().getSeperateLockSet().getSeperateX();
    }
  }

}