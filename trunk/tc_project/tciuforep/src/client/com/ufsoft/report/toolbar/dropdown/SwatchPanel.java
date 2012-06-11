package com.ufsoft.report.toolbar.dropdown;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public abstract class SwatchPanel extends JPanel{
	/** 每一个样品的大小*/
	protected Dimension swatchSize;
	/** 样品的行数和列数*/
	protected Dimension numSwatches;
	/** 分隔组件大小*/
	protected Dimension gap=new Dimension(1, 1);
	private boolean isKey;
	/** 样品数组,JComboBox.getSelectedItem()获取*/
	protected Object[] swatchs;
	/** 样品值数组,JComboBox.getSelectedIndex()获取*/
	protected Object[] swatchValues;
	private Object selectedSwatch;

	private boolean isSupportDateChangeListener = false;
	private MouseListener selectBttListener = null;
    
	public SwatchPanel(boolean isKey){
		this.isKey=isKey;
	}

   public void init(){
	   initValues();
	   initLayout();
       setToolTipText("");
       setOpaque(true);
       setBackground(Color.white);
       setRequestFocusEnabled(false);
       selectBttListener=createBttListener();
       this.addMouseListener(selectBttListener);
   }
   /**
    * 初始化swatchSize，numSwatches,其依赖于swatchs的值
    */
   public abstract  void  initLayout();
   /**
    * 初始化swatchs
    */
   public abstract  void  initValues();
   
   public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
/** 是否是可焦点遍历的*/
   public boolean isFocusTraversable()
   {
       return false;
   }
   public void paintComponent(Graphics g)
   {
       g.setColor(getBackground());
       g.fillRect(0, 0, getWidth(), getHeight());
       Object swatch;
       for(int i = 0; i < numSwatches.height; i++)
       {
           for(int j = 0; j < numSwatches.width; j++)
           {
        	   //要绘制的样品
        	   swatch=getSwatchForCell(j, i);
        	   //样品的起始位置
        	   int k = j * (swatchSize.width + gap.width);
               int l = i * (swatchSize.height + gap.height);
        	   if(swatch instanceof Color){
                 g.setColor((Color)swatch);
                 g.fillRect(k, l, swatchSize.width, swatchSize.height);
        	   }else if(swatch instanceof ImageIcon){
        		   ((ImageIcon)swatch).paintIcon(this, g, k, l) ;
        	   }
               //开始位置边线
        	   if (swatch instanceof Color) {//modify by 王宇光 需要画边线的再画
					g.setColor(Color.black);
					g.drawLine((k + swatchSize.width) - 1, l,
							(k + swatchSize.width) - 1,
							(l + swatchSize.height) - 1);
					g.drawLine(k, (l + swatchSize.height) - 1,
							(k + swatchSize.width) - 1,
							(l + swatchSize.height) - 1);
				}
           }

       }

   }
   /**
    * 根据鼠标位置获取该位置的样品对象
    * @param i 
    * @param j
    * @return
    */
   public Object getSwatchForLocation(int x, int y)
   {
       int k = x / (swatchSize.width + gap.width);
       int l = y / (swatchSize.height + gap.height);
       return getSwatchForCell(k, l);
   }
   /**
    * 根据鼠标位置获取该位置的样品对象值
    * @param i 
    * @param j
    * @return
    */
   public Object getSwatchValueForLocation(int x, int y)
   {
       int k = x / (swatchSize.width + gap.width);
       int l = y / (swatchSize.height + gap.height);
       return getSwatchValueForCell(k, l);
   }
   /**
    * 根据样品所在的行、列获取样品对象
    * @param i
    * @param j
    * @return
    */
   private Object getSwatchForCell(int i, int j)
   {
	   if(swatchs==null||j * numSwatches.width + i>=swatchs.length||j * numSwatches.width + i<0)
	    return null;
	   else {
		   return swatchs[j * numSwatches.width + i] ;
	   }
   }
   /**
    * 根据样品所在的行、列获取样品对象
    * @param i
    * @param j
    * @return
    */
   private Object getSwatchValueForCell(int i, int j)
   {
	   if(swatchValues==null||j * numSwatches.width + i>=swatchValues.length||j * numSwatches.width + i<0)
	    return null;
	   else {
		   return swatchValues[j * numSwatches.width + i] ;
	   }
   }
   public Object[] getSwatchs() {
		return swatchs;
	}

	public void setSwatchs(Object[] swatchs) {
		this.swatchs = swatchs;
	}

	public Object[] getSwatchValues() {
		return swatchValues;
	}

	public void setSwatchValues(Object[] swatchValues) {
		this.swatchValues = swatchValues;
	}
public Dimension getPreferredSize()
   {
       int i = numSwatches.width * (swatchSize.width + gap.width) - 1;
       int j = numSwatches.height * (swatchSize.height + gap.height) - 1;
       return new Dimension(i, j);
   }
public Object getSelectedSwatch() {
	return selectedSwatch;
}
public void setSelectedSwatch(Object selectedSwatch) {
	this.selectedSwatch = selectedSwatch;
	if(!isVisible()){
		setVisible(true);
	}
	if(isSupportDateChangeListener){
	 fireSwatchChanged(new ChangeEvent(selectedSwatch));
	}
}
protected EventListenerList listenerList = new EventListenerList();
public void addSwatchChangeListener(ChangeListener l){
    listenerList.add(ChangeListener.class, l);
}

public void removeSwatchChangeListener(ChangeListener l){
    listenerList.remove(ChangeListener.class, l);
}

protected void fireSwatchChanged(ChangeEvent e){
    Object[] listeners = listenerList.getListenerList();
    for(int i = listeners.length - 2; i >= 0; i -= 2){
        if(listeners[i] == ChangeListener.class){
            ((ChangeListener)listeners[i + 1]).stateChanged(e);
        }
    }
}
protected MouseListener createBttListener(){
    return new MouseAdapter(){
        public void mousePressed(MouseEvent e){}
        public void mouseClicked(MouseEvent e){}

        public void mouseReleased(MouseEvent e){
        	 Object selectedObject=getSwatchValueForLocation(e.getX(), e.getY());
        	 if (selectedObject!= null) {
					isSupportDateChangeListener = true;
					setSelectedSwatch(selectedObject);
					isSupportDateChangeListener = false;
				}
        }

        public void mouseEntered(MouseEvent e){}

        public void mouseExited(MouseEvent e){}
    };
}

protected Dimension getSwatchSize() {
	return swatchSize;
}

protected void setSwatchSize(Dimension swatchSize) {
	this.swatchSize = swatchSize;
}

protected Dimension getNumSwatches() {
	return numSwatches;
}

protected void setNumSwatches(Dimension numSwatches) {
	this.numSwatches = numSwatches;
}

protected Dimension getGap() {
	return gap;
}

protected void setGap(Dimension gap) {
	this.gap = gap;
}
  
}
