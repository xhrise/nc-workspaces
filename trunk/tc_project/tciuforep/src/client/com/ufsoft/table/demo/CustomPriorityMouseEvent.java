package com.ufsoft.table.demo;

import com.ufsoft.table.re.PriorityMouseHandle;
import java.awt.event.*;
import com.ufsoft.table.*;
import javax.swing.*;

/**
 * <p>Title: ��ʾһ������¼����ȼ�.�ͻ������Լ��������������¼�����Ӧ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author not attributable
 * @version 1.0.0.1
 */

public class CustomPriorityMouseEvent implements PriorityMouseHandle {
  private CellPosition[] priority1 = { CellPosition.getInstance(0,0)};//����1����
  private CellPosition[] priority2 = { CellPosition.getInstance(1,0)};//����2����.
  private String [] inputs = {"�ǵ�","��ʲ","����"};
  private int type;
  public Object priorityMouseEvent(int row, int col, Object oldValue, MouseEvent mEvent) {
    if((mEvent.getID()&MouseEvent.BUTTON3_MASK)==1) {
      System.out.println("����Ҽ�����3.");
    }
    if((mEvent.getID()&MouseEvent.BUTTON2_MASK)==1) {
      System.out.println("����Ҽ�����2.");
    }

    if(type==1){
      JOptionPane.showConfirmDialog(null,"��ʾһ������ʽ,���ǲ���Ӱ����ǰ�Ľ��.\r\n���3��");
      return null;
    }else if(type==2){
      return JOptionPane.showInputDialog(null,"Ϊ�����Ԫѡ��һ���µ�¼��ֵ.","test",JOptionPane.PLAIN_MESSAGE,null,inputs,inputs[0]);
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
