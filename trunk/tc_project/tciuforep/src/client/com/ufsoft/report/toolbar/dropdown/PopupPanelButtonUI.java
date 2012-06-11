package com.ufsoft.report.toolbar.dropdown;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.border.AbstractBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.ComboPopup;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import com.ufida.zior.comp.KToolBarButtonUI;


public class PopupPanelButtonUI extends WindowsComboBoxUI {

    private JButton bttLeft;
    private JButton bttRight;
    
    private boolean isMouseOver = false;
	private boolean isFocused = false;
	protected boolean popupVisible;
    private MouseListener mouseHandler;
    /** 与PopupMenu的显示无关，主要用来绘制边线*/
	private PopupMenuListener popupListener;
	
    public PopupPanelButtonUI(JButton left,JButton right){
    	this.bttLeft=left;
    	this.bttRight=right;   	
    }
	
	public boolean isFocused() {
		return isFocused;
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}

	protected void setFocused(boolean focused) {
		isFocused = focused;
		comboBox.repaint(); // Needed by 1.5.0_02.
	}

	protected void setMouseOver(boolean over) {
		isMouseOver = over;
	}
	
	@Override
	/**
	 * setUI()方法会调用，在这里主要用到个性化的popup
	 */
	public void installUI(JComponent c) {
		comboBox = (JComboBox)c;
		popup = createPopup();
		refreshUI();
	}
    
	@Override
	protected Dimension getDisplaySize() {
		return new Dimension(32, 23);
	}
    
	@Override
	/**
	 * 必须重载的方法，不做任何操作,其父主要做了绘制当前选择值和选择背景的操作
	 */
	public void paint( Graphics g, JComponent c ) {
		if(isMouseOver()){
			int width = c.getWidth();
			int height = c.getHeight();
			Graphics2D g2d = (Graphics2D) g;
			GradientPaint paint = new GradientPaint(0.0f, 0.0f, KToolBarButtonUI.TOOLBAR_BUTTON_ARMED_BEGIN_GRADIENT_COLOR,
						0.0f, height, KToolBarButtonUI.TOOLBAR_BUTTON_ARMED_END_GRADIENT_COLOR);
			g2d.setPaint(paint);
			g2d.fill(new Rectangle(1, 1, width - 1, height - 1));
		}
    }

	private void refreshUI(){
		rebuildComponents();
		rebulidListeners();
	}

	protected FocusListener createFocusHandler() {
		// TODO Auto-generated method stub
		return new FocusHandler();
	}
	/**
	 * Returns the class that will listen to this combo box's mouse events.
	 *
	 * @return The listener.
	 */
	protected MouseListener createMouseHandler() {
		return new MouseHandler();
	}
	
	protected PopupMenuListener createPopupListener() {
		return new SwatchComboPopupListener();
	}
	
	/**
	 * 重新构建组件，先要删除comboBox的子组件
	 */
	private void rebuildComponents() {
		
		    comboBox.removeAll();
		    		    
		    bttLeft.setFocusable(false);
		    bttLeft.setBorder(null);
		    bttRight.setFocusable(false);
		    bttRight.setBorder(null);
		    comboBox.setBorder(null);
		    //重新设置布局管理器
		    comboBox.setLayout(new BorderLayout());
		    
		    comboBox.add(bttLeft, BorderLayout.CENTER);
		    comboBox.add(bttRight, BorderLayout.EAST);
	}
	private void rebulidListeners() {
		mouseHandler=createMouseHandler();
		popupListener=createPopupListener();
		
		if(comboBox!=null){
			comboBox.addPopupMenuListener(popupListener);
			comboBox.addMouseListener(mouseHandler);
		}
		if(bttLeft!=null ){
			bttLeft.addMouseListener(mouseHandler);
		}
		
		if ( bttRight != null) {
        	arrowButton=bttRight;
        	//必须调用该方法，以使得popup正确显示
        	configureArrowButton();
        	bttRight.addMouseListener(mouseHandler);
        }
	}

	@Override
	protected void uninstallListeners() {
		comboBox.removeMouseListener(mouseHandler);
		super.uninstallListeners();
	}
    
	@Override
	/**modify by 2008-3-21 王宇光 根据com的实例，返回不同的ComboPopup ui
	 * 主要的重构方法，新的组合框的弹出部分UI
	 */
	protected ComboPopup createPopup() {
		JComponent com = null;
		if (comboBox instanceof JPopupPanelButton) {
			com = ((JPopupPanelButton) comboBox).getPopupCom();
		}
		if (com == null) {
			return null;
		}
		if (com instanceof SwatchPanel) {
			return new SwatchPopup(comboBox);
		} else {
			return new SwatchPopuMenu(comboBox);
		}
	}
	
	/**
	 * Listens for mouse events in this combo box.
	 * 主要用来强化边框的显示效果
	 */
	protected class MouseHandler extends MouseAdapter {
		UpBorder upBorder = new UpBorder();
	    DownBorder downBorder = new DownBorder();
	    
		protected void doMouseEnteredNotFocused() {
			// Disabled combo boxes should not highlight when armed.
			if (comboBox.isEnabled()) {
				
			}
		}

		protected void doMouseExitedNotFocused() {
		
		}
		
		public void mousePressed(MouseEvent e) {
			 if(!comboBox.isEnabled()){
				 return;
			 }
			 if (e.getSource() == bttLeft) {
				bttLeft.setBorder(upBorder);
				bttRight.setBorder(downBorder);

			} else if(e.getSource() == bttRight){
				bttLeft.setBorder(upBorder);
				bttRight.setBorder(downBorder);
				bttRight.setSelected(true);
				//add by 王宇光 2008-3-22
				showPopuMenu();//显示弹出菜单
			}
		 }
		
		public final void mouseEntered(MouseEvent e) {
			if(!comboBox.isEnabled()){
				 return;
			 }
			if(!popup.isVisible()){
				bttRight.setBackground(Color.ORANGE);
           	    bttLeft.setBorder(upBorder);
                bttRight.setBorder(downBorder);
                bttRight.repaint();
                bttLeft.repaint();

           }
			if (!isFocused())
				doMouseEnteredNotFocused();
			setMouseOver(true);
		}

		public final void mouseExited(MouseEvent e) {
			if(!comboBox.isEnabled()){
				 return;
			 }
			if(!popup.isVisible()){
           	 bttLeft.setBorder(null);
             bttRight.setBorder(null);
             bttRight.repaint();
             bttLeft.repaint();
           }
			if (!isFocused())
				doMouseExitedNotFocused();
			setMouseOver(false);
            
		}
    
	}
	
	// add by 2008-3-21王宇光 添加下拉菜单式的效果
	private void showPopuMenu(){			
		JComponent com = null;
		JPopupMenu popupMenu = null;
		if(comboBox == null||popup==null){
			return;
		}
		if (comboBox instanceof JPopupPanelButton) {
			com = ((JPopupPanelButton) comboBox).getPopupCom();
		}
		if (com instanceof JPopupMenu) {
			popupMenu = (JPopupMenu) com;
		}
		if (popup instanceof SwatchPopuMenu) {
			Point point = ((SwatchPopuMenu) popup).getPopupLocation();
			popupMenu.show(comboBox, point.x, point.y);
		}
	}
	
	/**
	 * Listens for the popup menu being displayed/hidden in this combo box.
	 */
	protected class SwatchComboPopupListener implements PopupMenuListener {

		public void popupMenuCanceled(PopupMenuEvent e) {
			popupVisible=false;	
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			popupVisible = false;
			if(!comboBox.isEnabled()){
				 return;
			 }
			bttLeft.setBorder(null);
			bttRight.setBorder(null);
		}

		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			popupVisible = true;
			
		}

	}
	
	 private static class DownBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			g.setColor(KToolBarButtonUI.HIGHT_LIGHT_BORDER_COLOR);
			g.drawLine(0, 0, width - 1, 0);
			g.drawLine(0, 0, 0, height - 1);
			g.drawLine(width - 1, 0, width - 1, height);
			g.drawLine(0, height - 1, width, height - 1);
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(1, 1, 1, 1);
		}
	}

	private static class UpBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			g.setColor(KToolBarButtonUI.HIGHT_LIGHT_BORDER_COLOR);
			g.drawLine(0, 0, width - 1, 0);
			g.drawLine(0, 0, 0, height - 1);
//			g.drawLine(width - 1, 0, width - 1, height);
			g.drawLine(0, height - 1, width, height - 1);
			
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(1, 1, 1, 1);
		}
	}
	
	public static class PopupArrowButton extends JButton{
		private static final long serialVersionUID = 1L;
		
		public void setUI(ButtonUI ui) {
            if(ui instanceof WindowsButtonUI){
               ui = new BasicButtonUI();
            }
            super.setUI(ui);
        }
        public void paint(Graphics g) {
            super.paint(g);
            Polygon p = new Polygon();
            int w = getWidth();
            int y = (getHeight() - 4) / 2;
            int x = (w - 6) / 2;
            if (isSelected()) {
                x += 1;
            }
            p.addPoint(x, y);
            p.addPoint(x + 3, y + 3);
            p.addPoint(x + 6, y);
            g.fillPolygon(p);
            g.drawPolygon(p);
        }
	}
	
	
	 
	
}
