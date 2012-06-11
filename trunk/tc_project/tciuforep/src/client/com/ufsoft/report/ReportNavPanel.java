package com.ufsoft.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import nc.ui.pub.beans.UISplitPane;
import com.ufsoft.report.UICloseableTabbedPane;

import com.ufsoft.report.component.CloseTabbedPaneUI;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.table.TableUtilities;

/**
 * �����ǵ��������м��Ǳ������
 * @author zzl 2005-4-27
 */
public class ReportNavPanel extends nc.ui.pub.beans.UIPanel {
	private static final long serialVersionUID = 1783646262002392605L;

    public static final int WEST_NAV = 0;
    public static final int NORTH_NAV = 1;
    public static final int EAST_NAV = 2;
    public static final int SOUTH_NAV = 3;
    
    private JComponent m_midCom;
    private JSplitPane[] m_splitPanes = new JSplitPane[4];
    private double[] m_splitDividers={0.2,0.1,0.8,0.8};
    private UICloseableTabbedPane[] m_navPanes=new UICloseableTabbedPane[4];
    /** reportNavPanel�Ĵ�С*/
    private Dimension splitScreen;
    /** �ָ�����Ĭ�ϴ�С*/
	private Dimension preSize = new Dimension(200, 30);
    /**
     * ���캯����
     * @param midCom �м�����������
     */
    public ReportNavPanel(JComponent midCom) {
        super();
        m_midCom = midCom;
        setLayout(new BorderLayout());
        init();
    }

    /**
     * ��ʼ��������ÿ�����µ�m_navPanes������ʼ����ʱ�򶼻���ã��÷��������°���˳���ʼ����m_splitPanes
     * Ȼ��һ���µİ�����������m_navPanes���µ�JSplitPane��ӵ�ReportNavPanel��ע�����ǰҪ�Ƴ��ɵ������
     */
    private void init() {
    	//main �ǰ������г�ʼ����m_navPanes��m_splitPanes�����ReportNavPanel��Ψһ�����
    	JComponent main = m_midCom;
    	if(m_navPanes[NORTH_NAV] != null){
    		JSplitPane splitPane = new UISplitPane(JSplitPane.VERTICAL_SPLIT);
//    		splitPane.setBorder(null);
    		splitPane.setTopComponent(m_navPanes[NORTH_NAV]);
    		splitPane.setBottomComponent(main);
    		m_splitPanes[NORTH_NAV] = splitPane;
    		main = splitPane;
    		
    	}
    	if(m_navPanes[SOUTH_NAV] != null){
    		JSplitPane splitPane = new UISplitPane(JSplitPane.VERTICAL_SPLIT);
//    		splitPane.setBorder(null);
    		splitPane.setTopComponent(main);
    		splitPane.setBottomComponent(m_navPanes[SOUTH_NAV]);
    		m_navPanes[SOUTH_NAV].setVisible(false);
//    		splitPane.setResizeWeight(0);
    		m_splitPanes[SOUTH_NAV] = splitPane;
    		main = splitPane;
    		
    	}
    	if(m_navPanes[WEST_NAV] != null){
    		JSplitPane splitPane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT);
//    		splitPane.setBorder(null);
    		splitPane.setLeftComponent(m_navPanes[WEST_NAV]);
    		splitPane.setRightComponent(main);
    		m_splitPanes[WEST_NAV] = splitPane;
    		main = splitPane;
    		
    	}
    	if(m_navPanes[EAST_NAV] != null){
    		JSplitPane splitPane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT);
//    		splitPane.setBorder(null);
    		splitPane.setLeftComponent(main);
    		splitPane.setRightComponent(m_navPanes[EAST_NAV]);
    		m_splitPanes[EAST_NAV] = splitPane;
    		main = splitPane;
    	
    	}
        
        //�Ƴ���ǰ��ӵ��������
        removeAll();
        
        add(main);
    }
    public void setMidComp(JComponent newMidComp){
        TableUtilities.replaceCompWith(m_midCom, newMidComp);
        m_midCom = newMidComp;        
        ((JComponent)m_midCom.getParent()).revalidate();
        m_midCom.getParent().repaint();
    }
    public Component getMidComp(){
        return m_midCom;
    }
    public UICloseableTabbedPane getPanelById(int index){
    	return m_navPanes[index];
    }
    public Component getNorthComp(){
    	return m_navPanes[NORTH_NAV];
    }
    public Component getSouthComp(){
    	return m_navPanes[SOUTH_NAV];
    }
    public JSplitPane getNorthPane(){
    	return m_splitPanes[NORTH_NAV];
    }
    public JSplitPane getSouthPane(){
    	return m_splitPanes[SOUTH_NAV];
    }
    public JSplitPane getWestPane(){
    	return m_splitPanes[WEST_NAV];
    }
    public JSplitPane getEastPane(){
    	return m_splitPanes[EAST_NAV];
    }

	/**
     * �����չ
     * @param ext void
     */
    public void addExtent(INavigationExt ext){
    	int navPos = ext.getNavPanelPos();
    	if(m_navPanes[navPos] == null){
    		m_navPanes[navPos] = new UICloseableTabbedPane();
    		//����UI��Ŀ���ǵ�ֻ��һ��Tabʱ������ʾTab����,�ϱߵĳ��⡣
    		if(navPos!=SOUTH_NAV){
    		  m_navPanes[navPos].setUI(new CloseTabbedPaneUI(false));
    		}else{
    		  m_navPanes[navPos].setUI(new CloseTabbedPaneUI(true));
    		}
    		
    		init();
    	}  
    	//�±ߵ��������ͼ���������
    	if(navPos!=SOUTH_NAV){
    	 ext.setShow(true);
    	 m_navPanes[navPos].addTab(ext.getName(),ext.getPanel());
    	}
    }
    /**
     * ��Ҫ�������÷ָ��С
     */
    public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		if ((splitScreen == null||splitScreen.getHeight()*splitScreen.getWidth()<=0) && width > 0 && height > 0) {
			splitScreen = new Dimension(width, height);
			
			if (m_navPanes[NORTH_NAV] != null
					&& m_splitPanes[NORTH_NAV].getDividerLocation() < 0) {
				if (m_navPanes[NORTH_NAV].getPreferredSize().getWidth()
						+ preSize.getWidth() > splitScreen.getWidth()) {
					preSize.height += 20;
				}
				m_splitPanes[NORTH_NAV].setDividerLocation((int) (preSize
						.getHeight()));

				m_navPanes[NORTH_NAV].addShowListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_splitPanes[NORTH_NAV]
								.setDividerLocation((int) (preSize.getHeight()));
					}
				});
			}
			if (m_navPanes[SOUTH_NAV] != null
					&& m_splitPanes[SOUTH_NAV].getDividerLocation() < 0) {
				m_splitPanes[SOUTH_NAV].setDividerLocation((int) (splitScreen
						.getHeight() * m_splitDividers[SOUTH_NAV]));

				m_navPanes[SOUTH_NAV].addShowListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_splitPanes[SOUTH_NAV]
								.setDividerLocation((int) (splitScreen
										.getHeight() * m_splitDividers[SOUTH_NAV]));
					}
				});
			}
			if (m_navPanes[WEST_NAV] != null
					&& m_splitPanes[WEST_NAV].getDividerLocation() < 0) {
				m_splitPanes[WEST_NAV].setDividerLocation((int) (preSize
						.getWidth()));

				m_navPanes[WEST_NAV].addShowListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_splitPanes[WEST_NAV]
								.setDividerLocation((int) (preSize.getWidth()));
					}
				});
			}
			if (m_navPanes[EAST_NAV] != null
					&& m_splitPanes[EAST_NAV].getDividerLocation() < 0) {
				m_splitPanes[EAST_NAV].setDividerLocation((int) (splitScreen
						.getWidth() * m_splitDividers[EAST_NAV]));

				m_navPanes[EAST_NAV].addShowListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_splitPanes[EAST_NAV]
								.setDividerLocation((int) (splitScreen
										.getWidth() * m_splitDividers[EAST_NAV]));

					}
				});
			}
		}else{
			splitScreen = new Dimension(width, height);
		}
		
	}
}
