package com.ufsoft.table.demo;

import com.ufsoft.table.re.PriorityMouseHandle;
import java.awt.event.*;
import com.ufsoft.table.*;
import javax.swing.*;

/**
 * <p>Title: 演示一个鼠标事件优先级.客户可以自己定义表格对于鼠标事件的响应</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author not attributable
 * @version 1.0.0.1
 */

public class CustomPriorityMouseEvent implements PriorityMouseHandle {
  private CellPosition[] priority1 = { CellPosition.getInstance(0,0)};//规则1处理
  private CellPosition[] priority2 = { CellPosition.getInstance(1,0)};//规则2处理.
  private String [] inputs = {"亚当","布什","克里"};
  private int type;
  public Object priorityMouseEvent(int row, int col, Object oldValue, MouseEvent mEvent) {
    if((mEvent.getID()&MouseEvent.BUTTON3_MASK)==1) {
      System.out.println("鼠标右键按下3.");
    }
    if((mEvent.getID()&MouseEvent.BUTTON2_MASK)==1) {
      System.out.println("鼠标右键按下2.");
    }

    if(type==1){
      JOptionPane.showConfirmDialog(null,"演示一个处理方式,但是不会影响以前的结果.\r\n鼠标3击");
      return null;
    }else if(type==2){
      return JOptionPane.showInputDialog(null,"为这个单元选择一个新的录入值.","test",JOptionPane.PLAIN_MESSAGE,null,inputs,inputs[0]);
    }
    else return null;
   }
  public boolean hasPriority(int row, int col, MouseEvent mEvent) {
      int count = mEvent.getClickCount();
      if(count<2){
        return false;
      }
    for(int i=0; i<priority1.length;i++){    	
      if(priority1[i].equals(CellPosition.getInstance(row,col))){
        type=1;
        return true;
      }
    }
    for(int i=0; i<priority2.length;i++){
      if(priority2[i].equals(CellPosition.getInstance(row,col))){
        type=2;
        return true;
      }
    }
    return false;
  }

}
