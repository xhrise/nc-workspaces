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
 * <p>Title:为预算实现的一个演示程序。 </p>
 * <p>Description:这个程序主要演示在一个有限表中进行<ol>
 * <li>树的设置，
 * <li>分栏和锁定表，
 * <li>表格格式的简单设置。 </ol></p>
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
 * @i18n miufo00200030=插入行列
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
   * 该方法演示一个表格如何设置,具体的过程可以参考每个方法的说明
   * @return UFOTable
   */
  private UFOTable initTable() {
//表格大小
    int rowSize = 10, colSize = 7;
    //构建一个有限表
     table = UFOTable.createFiniteTable(rowSize, colSize);
    //参照样表设置需要绘制的单元.
    String[][] contents = { //记录所有单元的内容
        {
        null, null, "主营收入", null, null, "其他收入", null}
        , {
        null, null, "地产", "服务", "工业", "退税", "中奖"}
        , {
        "", "1月", "12", "32", "321", "32", "22"}
        , {
        "", "2月", "12", "32", "321", "32", "22"}
        , {
        "", "3月", "12", "32", "321", "32", "22"}
        , {
        "一季度", "", "12", "32", "321", "32", "22"}
        , {
        "", "4月", "12", "32", "321", "32", "22"}
        , {
        "", "5月", "12", "32", "321", "32", "22"}
        , {
        "", "6月", "12", "32", "321", "32", "22"}
        , {
        "二季度", "", "12", "32", "只读单元", "无权查看右侧单元", "22"}
    };
    Color[][] backColor = { //记录所有的背景色
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
      //该组件是一个支持多表页的,如果只使用一个表页,按照如下方法得到第一页的数据模型,对于该模型来执行各种操作
      CellsModel dataModel = table.getCellsModel();
      int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
      //双点线条
      for (int row = 0; row < rowSize; row++) {
        for (int col = 0; col < colSize; col++) {
          Color color = backColor[row][col];
          DefaultFormat format = new IufoFormat();
          for (int i = 0; i < linePos.length; i++) {
            format.setLineType(linePos[i],TableConstant.L_SOLID2);
            format.setLineColor(linePos[i],Color.black);
          }
          format.setBackgroundColor(color); //此处设置背景色
          String strContent = contents[row][col];
          //构建显示的单元
          Cell c = new Cell(format, strContent == null ? "" : strContent);
          dataModel.setCell(row, col, c);
        }
      }
      //这里的代码是设置组合单元.
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
      //设置分组
      HeaderModel headerModel = dataModel.getRowHeaderModel(); //得到行标题的数据模型
      //这个节点是树的根结点,也是树对应的数据模型
      HeaderNode rowTreeRoot = new HeaderNode(null, true, true);
      int rowNumber = headerModel.getCount();
      //构建所有的树节点
      HeaderNode[] nodes = new HeaderNode[rowNumber];
      for (int i = 0; i < rowSize; i++) {
        Header h = headerModel.getHeader(i);
        nodes[i] = new HeaderNode(h, false, true);
      }
      /*添加节点注意事项：
      1.根节点要添加所有未分组的根叶节点。
      2.添加子节点时，需按照从小到大的顺序添加。
      3.一个子节点仅能有一个父节点，当一个节点加入一个子分组时，要把其从父分组种删除。
      4.子节点必须是连续的区域（包括子分组）。
      5.父节点的setBranchIndex()设置值，必须与实际父节点在分组中的位置相同一！
      */
      //构建一季度对应的树节点
      nodes[5].addChildren(nodes[2]);
      nodes[5].addChildren(nodes[3]);
      nodes[5].addChildren(nodes[4]);
      nodes[5].setBranchIndex(1);
      //注意要将已经不再当前级次的节点清空
      nodes[2] = null;
      nodes[3] = null;
      nodes[4] = null;
      //操作原理同上,处理2季度的节点.
      nodes[6].addChildren(nodes[7]);
      nodes[6].addChildren(nodes[8]);
      nodes[6].addChildren(nodes[9]);
      nodes[7] = null;
      nodes[8] = null;
      nodes[9] = null;
      //将所有第一级的节点添加的模型当中
      rowTreeRoot.addChildren(nodes);
//    添加到表格控件中
      table.setHeaderTreeModel(rowTreeRoot, Header.ROW);

      //这里是设置分栏表,锁定表;这里的概念和Excel中的"拆分","锁定"是一致的.
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
      //自定义显示风格。
      CellRenderAndEditor re = table.getReanderAndEditor();
      regist(re);
      MyCell mycell = new MyCell(null,Boolean.TRUE);
      dataModel.setCell(3,3,mycell);

      //这里演示对于鼠标事件拦截的设置.
      PriorityMouseHandle priority = new PriorityMouseHandle(){
            CellPosition[] priority1 = { CellPosition.getInstance(0,0)};//规则1处理
                CellPosition[] priority2 = { CellPosition.getInstance(1,0)};//规则2处理.
                    String [] inputs = {"123","222","fff"};
                    int type;
                    //这个方法是实现处理的代码，返回值会修改原来单元的内容。
                    public Object priorityMouseEvent(int row, int col, Object oldValue, MouseEvent mEvent) {
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
 * @i18n miufo00068=二季度
 * @i18n miufo00069=只读单元
 * @i18n miufo00070=无权查看右侧单元
 */
private UFOTable initTableSimple() {
    //构建一个有限表，10行7列
    UFOTable table = UFOTable.createFiniteTable(10, 7);
    //记录所有单元的内容
    String[][] contents = {
        {
        null, null, "主营收入", null, null, "其他收入", null}
        , {
        null, null, "地产", "服务", "工业", "退税", "中奖"}
        , {
        "", "1月", "12", "32", "321", "32", "22"}
        , {
        "", "2月", "12", "32", "321", "32", "22"}
        , {
        "", "3月", "12", "32", "321", "32", "22"}
        , {
        "一季度", "", "12", "32", "321", "32", "22"}
        , {
        "", "4月", "12", "32", "321", "32", "22"}
        , {
        "", "5月", "12", "32", "321", "32", "22"}
        , {
        "", "6月", "12", "32", "321", "32", "22"}
        , {
        MultiLang.getString("miufo00068"), "", "12", "32", MultiLang.getString("miufo00069"), MultiLang.getString("miufo00070"), "22"}
    };
    try {
      //该组件是一个支持多表页的,如果只使用一个表页,按照如下方法得到第一页的数据模型,对于该模型来执行各种操作
      CellsModel dataModel = table.getCellsModel();
      for (int row = 0; row < contents.length; row++) {
        for (int col = 0; col < contents[row].length; col++) {
          String strContent = contents[row][col];
          //构建显示的单元
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
 * @i18n miufo00073=预算示范
 * @i18n miufo00074=没有格式
 * @i18n miufo00075=简单格式
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
    
    //添加几个测试用的菜单。
    if(table!=null){
    	panel1.add(bar,BorderLayout.NORTH);
    }
    itemRow.addActionListener(this);
    bar.add(itemRow);


  }
 
  /**
 * @i18n miufo00048=插入位置(行>0,列<0)
 * @i18n miufo00049=插入数量(增加>0,删除<0)
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
 * @i18n miufo00076=演示预算
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
  