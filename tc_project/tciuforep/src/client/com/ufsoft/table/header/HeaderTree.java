package com.ufsoft.table.header;

import javax.swing.JComponent;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.ufsoft.report.resource.ResConst;

import java.awt.event.*;

/**
 * <p>Title:��ʾ���з����������״����� </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class HeaderTree
    extends JComponent implements TreeModelListener{
  /**\u00D5\u00DB\u00B5\u00FE\u00B5\u00C4��\u00B4\u00C5\u00A5\u00CD\u00BC����*/
   private final Icon iconFold = ResConst.getImageIcon("fold.gif");
   /**\u00D5\u00B9\u00BF\u00AA\u00B5\u00C4��\u00B4\u00C5\u00A5\u00CD\u00BC����*/
   private final Icon iconUnfold = ResConst.getImageIcon("unfold.gif");

  /**��¼ÿ���ڵ����Ҳ�ƫ�Ƶľ���   */
  private int LEVEL_STEP = 15;
  /**Ҷ�ڵ�Ļ��ư뾶   */
  private int LEAF_R = 2;
  /**����������Ʊ߽�ĳ���*/
  private int LINE_OFFSET = 3;
  /**���������Ŀ��*/
  private  int LINE_WIDTH = 2;
  /**����ģ��*/
  private TreeModel model ;
  /**���浱ǰ�Ľڵ�����*/
  private transient int unfoldDeep = -1;

  /**
   * ���캯��
   * @param type ���ͣ��ο�Header
   * @param root ���ڸ���㹹����Node��
   * @param headerModel ��ǰ����Ӧ���е�����ģ��
   */
  public HeaderTree(int type,HeaderNode root,HeaderModel headerModel) {
    this(new TreeModel(root,headerModel,type));
  }
  /**
   * ���캯��
   * @param m ��ǰ����Ӧ���е�����ģ��
   */
  public HeaderTree(TreeModel m) {
    super();
    if(m==null) {
      throw new IllegalArgumentException();
    }
    setTreeModel(m);
  }

  /**
   * �õ�չ���ڵ����ȣ�ֻ��һ�����棬����ÿ�μ��㡣
   * @return int
   */
  private int getUnfoldDeep() {
    if(unfoldDeep==-1) {
      unfoldDeep = getTreeModel().getTreeRoot().getUnfoldDeep();
    }
    return unfoldDeep;
  }
  /**
   * ��־����ʧЧ
   */
  private void invalidUnfoldDeep(){
    unfoldDeep=-1;
  }

  /**
   * ���������Ӧ������ģ��
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
   * �õ������Ӧ������ģ��
   * @return TreeModel
   */
  public TreeModel getTreeModel() {
    return model;
  }

  /**
   * ����Ļ��ơ����ƹ�����������ж�ÿ���ӽڵ��λ�ã��ӽڵ��Ƿ��Ѿ�չ��������û��չ�����ӽڵ㣬
   * ����Ӧ��λ�û���һ��ʮ�ֱ�ʾ������Ҷ�ڵ㣬����һ�����ʾ������չ���ĸ��ڵ㣬����һ����������
   * �ӽڵ��ֱ�ߣ������ڸ��ڵ��λ�û���һ���������ű�ʾ
   * @param g Graphics
   */
  protected void paintComponent(Graphics g) {
    removeAll();
    Graphics scratchGraphics = g.create();
    try {
      //�õ����Ƶ�����
      Rectangle rect = getBounds();
      rect.x=0;
      rect.y=0;
      if(rect.width==0||rect.height==0)return;
      HeaderNode root = model.getTreeRoot(); //�ýڵ�������ʾ
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
   * ����һ���ڵ㣬�μ�����paint��˵����
   * @param g Graphics ͼ����
   * @param node HeaderNode ���ƵĽڵ�
   * @param start int ������ʼ��λ��
   * @param deep int ��ǰ�ڵ�����
   * @param nodeLen int ���Ƶ�����
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

    //�Ƿ���һ��ҳ�ڵ�
    if (node.isLeaf()) { //����һ��Բ��
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
    //��һ���۵��Ľڵ�
    if (!node.isUnfold()) {
      JButton btn = new FoldTokenButton(node);
      add(btn);
      btn.setBounds(fitImage(new Rectangle(x, y, width, heigth)));
      return;
    }
    //�Ǵ򿪵Ľڵ�,��Ҫ֪���ڵ�򿪺�ĳ���,�Ȼ�������,Ȼ������ӽڵ�
    else {
      deep++;
      boolean bUp2Down = node.getBranchIndex() == 0; //�ж����Ĵ򿪷����Ƿ����϶���.
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
   * �÷���Ŀ���Ƕ���һ��չ����ť����Ŀռ�������󣬽�����ӦΪͼ�����صĴ�С
   * @param r Rectangle
   * @return Rectangle
   */
  private Rectangle fitImage(Rectangle r) {
    if (r == null) {
      return r;
    }
    int pixSize = 13+2; //ͼ��ĳߴ�+��ť�߿�
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
   * ����һ��չ���Ľڵ�
   * @param g ͼ����
   * @param node
   * @param x չ���������
   * @param y չ���������
   * @param width չ��������Ŀ��
   * @param heigth չ��������ĸ߶�
   * @param Alllen �Ľڵ�������ӽڵ�ռ�ݵĳ���
   * @param bUp2Down �Ƿ�������չ����
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
   * �Ƿ��б���ģ��
   * @return boolean
   */
  private boolean isRowHeader() {
    return getTreeModel().getType() == Header.ROW;
  }
  //�漰�����ֹ���ļ�������
  /**
 * �õ���ǰ�������С�ĳߴ�.
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
 * �õ���ǰ�����ȱʡ�ߴ�
 * @return Dimension
 */
public Dimension getPreferredSize() {
  int deep = getUnfoldDeep();
  HeaderModel headerModel = getTreeModel().getHeaderModel();

	int max = headerModel.getPosition(headerModel.getCount());
	
    return isRowHeader()?new Dimension(deep*LEVEL_STEP,max ):new Dimension(max ,deep*LEVEL_STEP);
}

/**
 * �õ���������ߴ�
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
   * �۵���ť�Ŀ���
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
//**************�ӿ�ʵ��**********************************

  /**
   * ��ģ�ͷ����ı�
   * @param e HeaderTreeEvent
   * @see com.ufsoft.table.header.TreeModelListener#nodeChanged(com.ufsoft.table.header.HeaderTreeEvent)
   */ 
  public  void nodeChanged(HeaderTreeEvent e){
     invalidUnfoldDeep();
    revalidate();
    repaint();
  }
    /**
     * ��¡
     * @return HeaderTree
     */
    public HeaderTree cloneSelf() {
      return new HeaderTree(getTreeModel());
    }






}