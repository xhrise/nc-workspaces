package com.ufsoft.table.header;

import javax.swing.JComponent;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.ufsoft.report.resource.ResConst;

import java.awt.event.*;

/**
 * <p>Title:表示行列分组情况的树状的组件 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class HeaderTree
    extends JComponent implements TreeModelListener{
  /**\u00D5\u00DB\u00B5\u00FE\u00B5\u00C4°\u00B4\u00C5\u00A5\u00CD\u00BC±ê*/
   private final Icon iconFold = ResConst.getImageIcon("fold.gif");
   /**\u00D5\u00B9\u00BF\u00AA\u00B5\u00C4°\u00B4\u00C5\u00A5\u00CD\u00BC±ê*/
   private final Icon iconUnfold = ResConst.getImageIcon("unfold.gif");

  /**记录每级节点向右侧偏移的距离   */
  private int LEVEL_STEP = 15;
  /**叶节点的绘制半径   */
  private int LEAF_R = 2;
  /**线条距离绘制边界的长度*/
  private int LINE_OFFSET = 3;
  /**绘制线条的宽度*/
  private  int LINE_WIDTH = 2;
  /**数据模型*/
  private TreeModel model ;
  /**缓存当前的节点的深度*/
  private transient int unfoldDeep = -1;

  /**
   * 构造函数
   * @param type 类型，参考Header
   * @param root 基于根结点构建的Node。
   * @param headerModel 当前树对应行列的数据模型
   */
  public HeaderTree(int type,HeaderNode root,HeaderModel headerModel) {
    this(new TreeModel(root,headerModel,type));
  }
  /**
   * 构造函数
   * @param m 当前树对应行列的数据模型
   */
  public HeaderTree(TreeModel m) {
    super();
    if(m==null) {
      throw new IllegalArgumentException();
    }
    setTreeModel(m);
  }

  /**
   * 得到展开节点的深度，只是一个缓存，避免每次计算。
   * @return int
   */
  private int getUnfoldDeep() {
    if(unfoldDeep==-1) {
      unfoldDeep = getTreeModel().getTreeRoot().getUnfoldDeep();
    }
    return unfoldDeep;
  }
  /**
   * 标志缓存失效
   */
  private void invalidUnfoldDeep(){
    unfoldDeep=-1;
  }

  /**
   * 设置组件对应的数据模型
   * @param model 
   */
  public void setTreeModel(TreeModel model) {
    TreeModel old = this.model;
    if(old!=null) {
      old.removeTreeModelListener(this);
    }
    this.model = model;
    this.model.addTreeModelListener(this);
    this.model.refresh(null);
  }

  /**
   * 得到组件对应的数据模型
   * @return TreeModel
   */
  public TreeModel getTreeModel() {
    return model;
  }

  /**
   * 组件的绘制。绘制规则：首先逐次判断每个子节点的位置，子节点是否已经展开；对于没有展开的子节点，
   * 在相应的位置绘制一个十字表示；对于叶节点，绘制一个点表示；对于展开的父节点，绘制一条包含所有
   * 子节点的直线，并且在父节点的位置绘制一个‘－’号表示
   * @param g Graphics
   */
  protected void paintComponent(Graphics g) {
    removeAll();
    Graphics scratchGraphics = g.create();
    try {
      //得到绘制的区域
      Rectangle rect = getBounds();
      rect.x=0;
      rect.y=0;
      if(rect.width==0||rect.height==0)return;
      HeaderNode root = model.getTreeRoot(); //该节点无需显示
      int offset = isRowHeader() ? rect.y : rect.x;
      if (root != null) {
        ArrayList children = root.getChildren();
        int deep = 1;
        if (children != null) {
          for (int i = 0; i < children.size(); i++) {
            HeaderNode node = (HeaderNode) children.get(i);
            int nodeLen = node.getLength();
            paintNode(g, node, offset, deep, nodeLen);
            offset += nodeLen;
          }
        }
      }
    }
    finally {
      scratchGraphics.dispose();
    }
  }

  /**
   * 绘制一个节点，参见方法paint的说明。
   * @param g Graphics 图象句柄
   * @param node HeaderNode 绘制的节点
   * @param start int 绘制起始的位置
   * @param deep int 当前节点的深度
   * @param nodeLen int 绘制的区间
   */
  private void paintNode(Graphics g, HeaderNode node, int start, int deep,
                         int nodeLen) {
    int x, y, width, heigth;
    if (isRowHeader()) {
        x = (deep - 1) * LEVEL_STEP;
        y = start;
        width = LEVEL_STEP;
        heigth = nodeLen;
      }
      else {
        x = start;
        y = (deep - 1) * LEVEL_STEP;
        width = nodeLen;
        heigth = LEVEL_STEP;
      }

    //是否是一个页节点
    if (node.isLeaf()) { //绘制一个圆点
      if (isRowHeader()) {
        x +=  LEVEL_STEP >> 1;
        y +=  nodeLen >> 1;
      }
      else {
        x +=  nodeLen >> 1;
        y +=  LEVEL_STEP >> 1;
      }
      g.fillArc(x, y, LEAF_R, LEAF_R, 0, 360);
      return;
    }
    //是一个折叠的节点
    if (!node.isUnfold()) {
      JButton btn = new FoldTokenButton(node);
      add(btn);
      btn.setBounds(fitImage(new Rectangle(x, y, width, heigth)));
      return;
    }
    //是打开的节点,需要知道节点打开后的长度,先绘制自身,然后绘制子节点
    else {
      deep++;
      boolean bUp2Down = node.getBranchIndex() == 0; //判断树的打开方向是否自上而下.
      if(isRowHeader()) {
        heigth = node.getHeader().getSize();
      }else {
        width = node.getHeader().getSize();
      }
      paintUnfoldNode(g,node,x,y,width,heigth,nodeLen,bUp2Down);
      ArrayList children = node.getChildren();
      int offset = isRowHeader()?y:x;
      if(bUp2Down){
         offset += node.getHeader().getSize();
      }
      HeaderNode nodeSon;
      for (int i = 0; i < children.size(); i++) {
        nodeSon = (HeaderNode) children.get(i);
        int allLen = nodeSon.getLength();
        paintNode(g, nodeSon, offset, deep, allLen);
        offset+=allLen;
      }
    }
  }

  /**
   * 该方法目的是对于一个展开按钮分配的空间如果过大，将它适应为图标像素的大小
   * @param r Rectangle
   * @return Rectangle
   */
  private Rectangle fitImage(Rectangle r) {
    if (r == null) {
      return r;
    }
    int pixSize = 13+2; //图象的尺寸+按钮边框
    if (r.width > pixSize) {
      r.x += (r.width - pixSize) >> 1;
      r.width = pixSize;
    }
    if (r.height > pixSize) {
      r.y += (r.height - pixSize) >> 1;
      r.height = pixSize;
    }
    return r;
  }

  /**
   * 绘制一个展开的节点
   * @param g 图像句柄
   * @param node
   * @param x 展开点的坐标
   * @param y 展开点的坐标
   * @param width 展开点区域的宽度
   * @param heigth 展开点区域的高度
   * @param Alllen 改节点和所有子节点占据的长度
   * @param bUp2Down 是否向右下展开。
   */
  private void paintUnfoldNode(Graphics g,HeaderNode node, int x, int y, int width, int heigth,
                               int Alllen, boolean bUp2Down) {
    if (bUp2Down) {
      Rectangle buttonR = new Rectangle(x,y,width,heigth);
      fitImage(buttonR);
      JButton btn = new FoldTokenButton(node);
      add(btn);
      btn.setBounds(buttonR);
      if (isRowHeader()) {
        int lineStartX = buttonR.x+LINE_OFFSET;
        int lineStartY = buttonR.y+buttonR.height;
        int lineEndY = y+Alllen-LINE_OFFSET;
        int eLineStartX = lineStartX;
        int eLineStartY  = lineEndY-LINE_WIDTH;
        int eLineEndX = x+width;
        g.fillRect(lineStartX,lineStartY,LINE_WIDTH,lineEndY-lineStartY);
        g.fillRect(eLineStartX,eLineStartY,eLineEndX-eLineStartX,LINE_WIDTH);
      }else {
        int lineStartX = buttonR.x+buttonR.width;
        int lineStartY = buttonR.y+LINE_OFFSET;
        int lineEndX = x+Alllen-LINE_OFFSET;
        int lineEndY = lineStartY+LINE_WIDTH;
        int eLineStartX = lineEndX-LINE_WIDTH;
        int eLineStartY  = lineEndY;
        int eLineEndY = y+heigth;
        g.fillRect(lineStartX,lineStartY,lineEndX-lineStartX,LINE_WIDTH);
        g.fillRect(eLineStartX,eLineStartY,LINE_WIDTH,eLineEndY-eLineStartY);

      }
    }
    else{
      int btnX,btnY;
      if(isRowHeader()) {
        btnX = x;
        btnY = y + Alllen - node.getHeader().getSize();
      }else {
        btnY = y;
        btnX = x + Alllen - node.getHeader().getSize();
      }
      Rectangle buttonR = new Rectangle(btnX,btnY,width,heigth);
      fitImage(buttonR);
      JButton btn = new FoldTokenButton(node);
      add(btn);
      btn.setBounds(buttonR);
      if (isRowHeader()) {
        int lineStartX = buttonR.x+LINE_OFFSET;
        int lineStartY = y+LINE_OFFSET;
        int lineEndY = buttonR.y;
        int eLineStartX = lineStartX;
        int eLineStartY  = lineStartY;
        int eLineEndX = x+width;
        g.fillRect(lineStartX,lineStartY,LINE_WIDTH,lineEndY-lineStartY);
        g.fillRect(eLineStartX,eLineStartY,eLineEndX-eLineStartX,LINE_WIDTH);
      }else {
        int lineStartX = x+LINE_OFFSET;
        int lineStartY = buttonR.y+LINE_OFFSET;
        int lineEndX = buttonR.x;
        int eLineStartX = lineStartX;
        int eLineStartY  = lineStartY;
        int eLineEndY = y+heigth;
        g.fillRect(lineStartX,lineStartY,lineEndX-lineStartX,LINE_WIDTH);
        g.fillRect(eLineStartX,eLineStartY,LINE_WIDTH,eLineEndY-eLineStartY);

      }
    }

  }
  /**
   * 是否行标题模型
   * @return boolean
   */
  private boolean isRowHeader() {
    return getTreeModel().getType() == Header.ROW;
  }
  //涉及到布局管理的几个方法
  /**
 * 得到当前组件的最小的尺寸.
 * @return Dimension
 */
public Dimension getMinimumSize() {
//  int size = 0;
//  Enumeration enumeration = getTreeModel().getHeaderModel().getHeaders();
//  while (enumeration.hasMoreElements()) {
//    Header h = (Header) enumeration.nextElement();
//    size = size + h.getMinSize();
//  }
	int min = 0;
	Header[] headers = getTreeModel().getHeaderModel().getHeaders();
	for(Header h: headers){
		if (h != null && h.isVisible()) {
			min += h.getMinSize();
		}
	}
	
  int deep = getUnfoldDeep();
  return isRowHeader()?new Dimension(deep*LEVEL_STEP,min):new Dimension(min ,deep*LEVEL_STEP);
  
}

/**
 * 得到当前组件的缺省尺寸
 * @return Dimension
 */
public Dimension getPreferredSize() {
  int deep = getUnfoldDeep();
  HeaderModel headerModel = getTreeModel().getHeaderModel();

	int max = headerModel.getPosition(headerModel.getCount());
	
    return isRowHeader()?new Dimension(deep*LEVEL_STEP,max ):new Dimension(max ,deep*LEVEL_STEP);
}

/**
 * 得到组件的最大尺寸
 * @return Dimension
 */
public Dimension getMaximumSize() {
  int size = getTreeModel().getHeaderModel().getTotalSize();
//  Enumeration enumeration = getTreeModel().getHeaderModel().getHeaders();
//  while (enumeration.hasMoreElements()) {
//    Header h = (Header) enumeration.nextElement();
//    size = size + h.getMaxSize();
//  }
  
  
  int deep = getUnfoldDeep();
  return isRowHeader()?new Dimension(deep*LEVEL_STEP,size):new Dimension(size ,deep*LEVEL_STEP);
}

  /**
   * 折叠按钮的控制
   */
  private class FoldTokenButton
      extends nc.ui.pub.beans.UIButton
      implements ActionListener {
    private boolean bFold;
    private HeaderNode node;
    /**
     * @param node
     */
    public FoldTokenButton(HeaderNode node) {
      super(node.isUnfold() ? iconUnfold : iconFold);
      this.bFold = node.isUnfold();
      this.node = node;
      addActionListener(this);
      setBorder(null);
    }
    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
//      super(e);
      bFold = !bFold;
      setIcon(bFold ? iconFold : iconUnfold);
      node.setUnfold(bFold);
      TreeModel model = getTreeModel();
      model.refresh(node);
    }
  }

//*********************************************************
//**************接口实现**********************************

  /**
   * 树模型发生改变
   * @param e HeaderTreeEvent
   * @see com.ufsoft.table.header.TreeModelListener#nodeChanged(com.ufsoft.table.header.HeaderTreeEvent)
   */ 
  public  void nodeChanged(HeaderTreeEvent e){
     invalidUnfoldDeep();
    revalidate();
    repaint();
  }
    /**
     * 克隆
     * @return HeaderTree
     */
    public HeaderTree cloneSelf() {
      return new HeaderTree(getTreeModel());
    }






}