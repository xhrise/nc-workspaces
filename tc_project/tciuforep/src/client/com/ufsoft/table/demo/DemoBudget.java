package com.ufsoft.table.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIMenuBar;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.format.DefaultFormat;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.header.HeaderNode;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.PriorityMouseHandle;

/**
 * <p>Title:ΪԤ��ʵ�ֵ�һ����ʾ���� </p>
 * <p>Description:���������Ҫ��ʾ��һ�����ޱ��н���<ol>
 * <li>�������ã�
 * <li>������������
 * <li>����ʽ�ļ����á� </ol></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author not attributable
 * @version 1.0.0.1
 */

public class DemoBudget
    extends JFrame implements ActionListener {
  private UFOTable table ;
  JPanel panel1 = new UIPanel();
  JMenuBar bar = new UIMenuBar();
  /**
 * @i18n miufo00200030=��������
 */
JMenuItem itemRow = new UIMenuItem(MultiLang.getString("miufo00200030"));
  /**
 * 
 * @author wupeng
 * @version 3.1
 */
public static class MyCell extends Cell{
    /**
     * @param f
     * @param v
     */
    public MyCell(Format f,Object v){
      super(f,v);
    }
  }
  

  /**
 * @param frame
 * @param title
 * @param modal
 */
public DemoBudget( String title) {
    super(title);
    try {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      AppDebug.debug(ex);
    }
  }
  /**
   * �÷�����ʾһ������������,����Ĺ��̿��Բο�ÿ��������˵��
   * @return UFOTable
   */
  private UFOTable initTable() {
//����С
    int rowSize = 10, colSize = 7;
    //����һ�����ޱ�
     table = UFOTable.createFiniteTable(rowSize, colSize);
    //��������������Ҫ���Ƶĵ�Ԫ.
    String[][] contents = { //��¼���е�Ԫ������
        {
        null, null, "��Ӫ����", null, null, "��������", null}
        , {
        null, null, "�ز�", "����", "��ҵ", "��˰", "�н�"}
        , {
        "", "1��", "12", "32", "321", "32", "22"}
        , {
        "", "2��", "12", "32", "321", "32", "22"}
        , {
        "", "3��", "12", "32", "321", "32", "22"}
        , {
        "һ����", "", "12", "32", "321", "32", "22"}
        , {
        "", "4��", "12", "32", "321", "32", "22"}
        , {
        "", "5��", "12", "32", "321", "32", "22"}
        , {
        "", "6��", "12", "32", "321", "32", "22"}
        , {
        "������", "", "12", "32", "ֻ����Ԫ", "��Ȩ�鿴�Ҳ൥Ԫ", "22"}
    };
    Color[][] backColor = { //��¼���еı���ɫ
        {
        null, null, null, null, null, null, null}
        , {
        null, null, null, null, null, null, null}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.yellow, Color.yellow, Color.yellow, Color.yellow,
        Color.yellow}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.green, Color.green, Color.green, Color.green,
        Color.green}
        , {
        null, null, Color.yellow, Color.yellow, Color.yellow, Color.yellow,
        Color.yellow}

    };
    try {
      //�������һ��֧�ֶ��ҳ��,���ֻʹ��һ����ҳ,�������·����õ���һҳ������ģ��,���ڸ�ģ����ִ�и��ֲ���
      CellsModel dataModel = table.getCellsModel();
      int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
      //˫������
      for (int row = 0; row < rowSize; row++) {
        for (int col = 0; col < colSize; col++) {
          Color color = backColor[row][col];
          DefaultFormat format = new IufoFormat();
          for (int i = 0; i < linePos.length; i++) {
            format.setLineType(linePos[i],TableConstant.L_SOLID2);
            format.setLineColor(linePos[i],Color.black);
          }
          format.setBackgroundColor(color); //�˴����ñ���ɫ
          String strContent = contents[row][col];
          //������ʾ�ĵ�Ԫ
          Cell c = new Cell(format, strContent == null ? "" : strContent);
          dataModel.setCell(row, col, c);
        }
      }
      //����Ĵ�����������ϵ�Ԫ.
      int row = 0, col = 2;
      AreaPosition aPos = AreaPosition.getInstance(row, col, 1, 3);
      Cell c = dataModel.getCell(row, col);
      CombinedCell cc = new CombinedCell(aPos, c.getFormat(), c.getValue());
//      dataModel.addArea(cc);
      row = 0;
      col = 5;
      aPos = AreaPosition.getInstance(row, col, 1, 2);
      c = dataModel.getCell(row, col);
      cc = new CombinedCell(aPos, c.getFormat(), c.getValue());
//      dataModel.addArea(cc);
      //���÷���
      HeaderModel headerModel = dataModel.getRowHeaderModel(); //�õ��б��������ģ��
      //����ڵ������ĸ����,Ҳ������Ӧ������ģ��
      HeaderNode rowTreeRoot = new HeaderNode(null, true, true);
      int rowNumber = headerModel.getCount();
      //�������е����ڵ�
      HeaderNode[] nodes = new HeaderNode[rowNumber];
      for (int i = 0; i < rowSize; i++) {
        Header h = headerModel.getHeader(i);
        nodes[i] = new HeaderNode(h, false, true);
      }
      /*��ӽڵ�ע�����
      1.���ڵ�Ҫ�������δ����ĸ�Ҷ�ڵ㡣
      2.����ӽڵ�ʱ���谴�մ�С�����˳����ӡ�
      3.һ���ӽڵ������һ�����ڵ㣬��һ���ڵ����һ���ӷ���ʱ��Ҫ����Ӹ�������ɾ����
      4.�ӽڵ���������������򣨰����ӷ��飩��
      5.���ڵ��setBranchIndex()����ֵ��������ʵ�ʸ��ڵ��ڷ����е�λ����ͬһ��
      */
      //����һ���ȶ�Ӧ�����ڵ�
      nodes[5].addChildren(nodes[2]);
      nodes[5].addChildren(nodes[3]);
      nodes[5].addChildren(nodes[4]);
      nodes[5].setBranchIndex(1);
      //ע��Ҫ���Ѿ����ٵ�ǰ���εĽڵ����
      nodes[2] = null;
      nodes[3] = null;
      nodes[4] = null;
      //����ԭ��ͬ��,����2���ȵĽڵ�.
      nodes[6].addChildren(nodes[7]);
      nodes[6].addChildren(nodes[8]);
      nodes[6].addChildren(nodes[9]);
      nodes[7] = null;
      nodes[8] = null;
      nodes[9] = null;
      //�����е�һ���Ľڵ���ӵ�ģ�͵���
      rowTreeRoot.addChildren(nodes);
//    ��ӵ����ؼ���
      table.setHeaderTreeModel(rowTreeRoot, Header.ROW);

      //���������÷�����,������;����ĸ����Excel�е�"���","����"��һ�µ�.
      table.setSeperatePos(2,2);
      table.setFreezing(true);

      
      CellsAuthorization auth = new CellsAuthorization() {
        private CellPosition[] unReadCells, unWriteCells;
        public boolean isReadable(int row, int col) {
          if (unReadCells != null) {
            for (int i = 0; i < unReadCells.length; i++) {
              if (unReadCells[i].equals(CellPosition.getInstance(row,col))) {
                return false;
              }
            }
          }
          return true;
        }

        public boolean isWritable(int row, int col) {
          if (unWriteCells != null) {
            for (int i = 0; i < unWriteCells.length; i++) {
              if (unWriteCells[i].equals(CellPosition.getInstance(row,col))) {
                return false;
              }
            }

          }
          return true;
        }
        public void authorize(int row, int col, int type) {        }
      };
      table.getCells().setCellsAuthorization(auth);
      //�Զ�����ʾ���
      CellRenderAndEditor re = table.getReanderAndEditor();
      regist(re);
      MyCell mycell = new MyCell(null,Boolean.TRUE);
      dataModel.setCell(3,3,mycell);

      //������ʾ��������¼����ص�����.
      PriorityMouseHandle priority = new PriorityMouseHandle(){
            CellPosition[] priority1 = { CellPosition.getInstance(0,0)};//����1����
                CellPosition[] priority2 = { CellPosition.getInstance(1,0)};//����2����.
                    String [] inputs = {"123","222","fff"};
                    int type;
                    //���������ʵ�ִ���Ĵ��룬����ֵ���޸�ԭ����Ԫ�����ݡ�
                    public Object priorityMouseEvent(int row, int col, Object oldValue, MouseEvent mEvent) {
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

      };
      table.getCells().setPriorityMouseEvent(priority);

    }
    catch (Exception e) {
      AppDebug.debug(e);
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
    return table;
  }
  /**
 * @i18n miufo00068=������
 * @i18n miufo00069=ֻ����Ԫ
 * @i18n miufo00070=��Ȩ�鿴�Ҳ൥Ԫ
 */
private UFOTable initTableSimple() {
    //����һ�����ޱ�10��7��
    UFOTable table = UFOTable.createFiniteTable(10, 7);
    //��¼���е�Ԫ������
    String[][] contents = {
        {
        null, null, "��Ӫ����", null, null, "��������", null}
        , {
        null, null, "�ز�", "����", "��ҵ", "��˰", "�н�"}
        , {
        "", "1��", "12", "32", "321", "32", "22"}
        , {
        "", "2��", "12", "32", "321", "32", "22"}
        , {
        "", "3��", "12", "32", "321", "32", "22"}
        , {
        "һ����", "", "12", "32", "321", "32", "22"}
        , {
        "", "4��", "12", "32", "321", "32", "22"}
        , {
        "", "5��", "12", "32", "321", "32", "22"}
        , {
        "", "6��", "12", "32", "321", "32", "22"}
        , {
        MultiLang.getString("miufo00068"), "", "12", "32", MultiLang.getString("miufo00069"), MultiLang.getString("miufo00070"), "22"}
    };
    try {
      //�������һ��֧�ֶ��ҳ��,���ֻʹ��һ����ҳ,�������·����õ���һҳ������ģ��,���ڸ�ģ����ִ�и��ֲ���
      CellsModel dataModel = table.getCellsModel();
      for (int row = 0; row < contents.length; row++) {
        for (int col = 0; col < contents[row].length; col++) {
          String strContent = contents[row][col];
          //������ʾ�ĵ�Ԫ
          Cell c = new Cell(null, strContent);
          dataModel.setCell(row, col, c);
        }
      }

    }
    catch (Exception e) {
      AppDebug.debug(e);
    }
    return table;
  }
  /**
 * @i18n miufo00073=Ԥ��ʾ��
 * @i18n miufo00074=û�и�ʽ
 * @i18n miufo00075=�򵥸�ʽ
 */
private void jbInit() throws Exception {
    panel1.setLayout(new BorderLayout());
    getContentPane().add(panel1);
    String[] selections = new String[] {
        MultiLang.getString("miufo00073"), MultiLang.getString("miufo00074"), MultiLang.getString("miufo00075")};
    String result = (String) JOptionPane.showInputDialog(this, null, "", 0, null,
        selections, selections[0]);
    
    if(result==null){
      System.exit(0);
    }
    else if (result.equals(selections[0])) {
      panel1.add(table=initTable(),BorderLayout.CENTER);
    }
    else if (result.equals(selections[1])) {
      panel1.add(table=initTableSimple(),BorderLayout.CENTER);
    }
    else if (result.equals(selections[2])) {
      panel1.add(table=initTableSimple(),BorderLayout.CENTER);
    }
    
    //��Ӽ��������õĲ˵���
    if(table!=null){
    	panel1.add(bar,BorderLayout.NORTH);
    }
    itemRow.addActionListener(this);
    bar.add(itemRow);


  }
 
  /**
 * @i18n miufo00048=����λ��(��>0,��<0)
 * @i18n miufo00049=��������(����>0,ɾ��<0)
 */
public void actionPerformed(ActionEvent e) {
  	Object pos = JOptionPane.showInputDialog(MultiLang.getString("miufo00048"));
    Object num = JOptionPane.showInputDialog(MultiLang.getString("miufo00049"));
    int nPos = Integer.parseInt("" + pos);
    int nNum = Integer.parseInt("" + num);
    try{
    	CellsModel dataModel = table.getCellsModel();
    	if (nPos > 0) {
            if (nNum > 0) {
              dataModel.getRowHeaderModel().addHeader(nPos, nNum);
            }
            else if (nNum < 0) {
              dataModel.getRowHeaderModel().removeHeader(nPos, -nNum);
            }
          }
          else if (nPos < 0) {
            if (nNum > 0) {
              dataModel.getColumnHeaderModel().addHeader( -nPos, nNum);
            }
            else if (nNum < 0) {
              dataModel.getColumnHeaderModel().removeHeader( -nPos, -nNum);
            }
          }
    }catch(Exception ex){
    	AppDebug.debug(ex);
    }
  }


  /**
 * @param args
 * @i18n miufo00076=��ʾԤ��
 */
public static void main(String[] args) {
    DemoBudget demo = new DemoBudget(MultiLang.getString("miufo00076"));
    demo.setSize(300, 200);
    demo.setVisible(true);  }
  private void regist(CellRenderAndEditor re){
    if(re!=null){
//      re.registEditor(MyCell.class,new BooleanEditor());
//      re.registRender(MyCell.class,new BooleanRenderer());
    }
  }
}
  