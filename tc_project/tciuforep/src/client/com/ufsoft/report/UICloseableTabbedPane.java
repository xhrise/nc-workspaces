/**
 * 
 */
package com.ufsoft.report;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;

import nc.ui.pub.beans.UITabbedPane;

/**
 * @author guogang
 * �ɹرյ�tabbedpane
 */

public class UICloseableTabbedPane extends UITabbedPane {
//	public static final String ON_TAB_CLOSE = "ON_TAB_CLOSE";
//    public static final String ON_TAB_DOUBLECLICK = "ON_TAB_DOUBLECLICK";
	//�ر�tab�¼������¼�����ر�tab��������ͼ״̬�Ȳ���
	 protected EventListenerList closeListenerList = new EventListenerList();
	 //���tab�¼������¼��������tab��������ͼ״̬������tab�����ݵȲ���
	 protected EventListenerList showListenerList = new EventListenerList();
	 
    public UICloseableTabbedPane(){
        super();
        init();
    }

    public UICloseableTabbedPane(int tabPlacement){
        super(tabPlacement);
        init();
    }

    public UICloseableTabbedPane(int tabPlacement, int tabLayoutPolicy){
        super(tabPlacement, tabLayoutPolicy);
        init();
    }

	protected void init(){
        addMouseListener(new DefaultMouseAdapter());
        addCloseListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	String tabName=e.getActionCommand();
                if(indexOfTab(tabName)>=0){
                	removeTabAt(indexOfTab(tabName));
                    if(getTabCount()<1)
                    	setVisible(false);
                }
            }
        });
    }
    
    /**
     * �����������������Ҫ�Ĳ�ͬ�ǻᴥ���Լ���ShowListener�¼�
     */
	public void addTab(String title, Component component) {
				
		//Ĭ��Ҫ��ʾ��component���ڻ��Ƶ�ʱ���layoutһ�����ʵĴ�С�������������ʾǰ����JSplitPane.setDividerLocation(),ʹJSplitPane��Ĭ�ϴ�СΪ0
		if (!isVisible()) {
			setVisible(true);
		}
		this.setPreferredSize(component.getPreferredSize());
		super.addTab(title, component);
		
		if (component instanceof JPanel)
			fireShow(new ActionEvent(component, ActionEvent.ACTION_PERFORMED,
					title));
		
	}
    
	public void removeTabAt(String tabName) {
		  fireClosed(new ActionEvent(
                  this,
                  ActionEvent.ACTION_PERFORMED,
                  tabName));
	}

	public void setIconDrawCenter(int index, boolean drawCenter){
        ((CloseIcon)getIconAt(index)).setDrawCenter(drawCenter);
        repaint();
    }
    
    public boolean isDrawCenter(int index){
        return((CloseIcon)getIconAt(index)).isDrawCenter();
    }
    
    public void addCloseListener(ActionListener l){
        closeListenerList.add(ActionListener.class, l);
    }

    public void removeCloseListener(ActionListener l){
        closeListenerList.remove(ActionListener.class, l);
    }
    public void removeAllCloseListener(){
    	if(closeListenerList!=null){
    		closeListenerList=new EventListenerList();
    	}
    }
    /**
     * ��Ҫ�����ڵ���addTab()��������µ��������ʱ�������丸JSplitPane����ʾ����
     * ע�⣺removeAllShowListener�����ĵ���
     * @param l
     */
    public void addShowListener(ActionListener l){
    	showListenerList.add(ActionListener.class, l);
    }

    public void removeShowListener(ActionListener l){
    	showListenerList.remove(ActionListener.class, l);
    }
    /**
     * ReportNavPanel.init()����ø÷���
     */
    public void removeAllShowListener(){
     if(showListenerList!=null){
    	 showListenerList=new EventListenerList();
     }
    }
    protected void fireClosed(ActionEvent e){
        Object[] listeners = closeListenerList.getListenerList();
        for(int i = listeners.length - 2; i >= 0; i -= 2){
            if(listeners[i] == ActionListener.class){
                ((ActionListener)listeners[i + 1]).actionPerformed(e);
            }
        }
    }
    
    protected void fireShow(ActionEvent e){
        Object[] listeners = showListenerList.getListenerList();
        for(int i = listeners.length - 2; i >= 0; i -= 2){
            if(listeners[i] == ActionListener.class){
                ((ActionListener)listeners[i + 1]).actionPerformed(e);
            }
        }
    }
    
    public void close() {
		for (int i = 0; i < getTabCount(); i++) {
			Component c = getComponentAt(i);
			removeTabAt(c.getName());
		}
	}
    
    private class DefaultMouseAdapter extends MouseAdapter{
        CloseIcon icon;
        public void mousePressed(MouseEvent e){
            int index = indexAtLocation(e.getX(), e.getY());
            if(index != -1){
                icon = (CloseIcon)getIconAt(index);
                if(icon.getBounds().contains(e.getPoint())){
                    icon.setPressed(true);
                    fireClosed(new ActionEvent(
                        e.getComponent(),
                        ActionEvent.ACTION_PERFORMED,
                        getTitleAt(index)));
                } 
//                else if(e.getClickCount() == 2){
//                    fireClosed(new ActionEvent(
//                        e.getComponent(),
//                        ActionEvent.ACTION_PERFORMED,
//                        ON_TAB_DOUBLECLICK));
//                }
            }
        }
        public void mouseReleased(MouseEvent e){
            if(icon != null){
                icon.setPressed(false);
                icon = null;
                repaint();
            }
        }
    }
    public Icon getIconAt(int index){
        Icon icon = super.getIconAt(index);
        if(icon == null || !(icon instanceof CloseIcon)){
            super.setIconAt(index, new CloseIcon());
        }
        return super.getIconAt(index);
    }

    private class CloseIcon implements Icon{
        Rectangle rec = new Rectangle(0, 0, 15, 15);
        private boolean pressed = false;
        private boolean drawCenter = true;
        public synchronized void paintIcon(
            Component c, Graphics g, int x1, int y1){
            int x = x1, y = y1;
            if(pressed){
                x++;
                y++;
            }
            rec.x = x;
            rec.y = y;
            Color oldColor = g.getColor();
            g.setColor(UIManager.getColor("TabbedPane.highlight"));
            g.drawLine(x, y, x, y + rec.height);
            g.drawLine(x, y, x + rec.width, y);
            g.setColor(UIManager.getColor("TabbedPane.shadow"));
            g.drawLine(x, y + rec.height, x + rec.width, y + rec.height);
            g.drawLine(x + rec.width, y, x + rec.width, y + rec.height);
            g.setColor(UIManager.getColor("TabbedPane.foreground"));
            //draw X
            //left top
            g.drawRect(x + 4, y + 4, 1, 1);
            g.drawRect(x + 5, y + 5, 1, 1);
            g.drawRect(x + 5, y + 9, 1, 1);
            g.drawRect(x + 4, y + 10, 1, 1);
            //center
            if(drawCenter){
                g.drawRect(x + 6, y + 6, 1, 1);
                g.drawRect(x + 8, y + 6, 1, 1);
                g.drawRect(x + 6, y + 8, 1, 1);
                g.drawRect(x + 8, y + 8, 1, 1);
            }
            //right top
            g.drawRect(x + 10, y + 4, 1, 1);
            g.drawRect(x + 9, y + 5, 1, 1);
            //right bottom
            g.drawRect(x + 9, y + 9, 1, 1);
            g.drawRect(x + 10, y + 10, 1, 1);
            g.setColor(oldColor);
        }

        private void drawRec(Graphics g, int x, int y){
            g.drawRect(x, y, 1, 1);
        }

        public Rectangle getBounds(){
            return rec;
        }

        public void setBounds(Rectangle rec){
            this.rec = rec;
        }

        public int getIconWidth(){
            return rec.width;
        }

        public int getIconHeight(){
            return rec.height;
        }

        public void setPressed(boolean pressed){
            this.pressed = pressed;
        }

        public void setDrawCenter(boolean drawCenter){
            this.drawCenter = drawCenter;
        }

        public boolean isPressed(){
            return pressed;
        }

        public boolean isDrawCenter(){
            return drawCenter;
        }
    }
    
}
