package com.ufsoft.iuforeport;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.ufsoft.iufo.inputplugin.querynavigation.QueryNaviMenu;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.report.applet.UfoApplet;


/**
 * �ۺϲ�ѯ��applet�Ļ���
 * ���ڴ����ѯ�����ȹ���appletӦ�á�
 * @author liuyy
 *
 */
public class UfoQueryNavApplet extends UfoApplet {
 
	private static final long serialVersionUID = 5875819496838932648L;
	

	   /**
	    * applet��Ӧ���ۺϲ�ѯ�����˵�
	    * liuyy
	    */
	
	private QueryNaviMenu m_querymenu = null;
		 
    
		
	public void init(){
 
		super.init();
		  
		QueryNaviMenu menu = new QueryNaviMenu(this, ReportViewType.VIEW_REPORT);
		this.m_querymenu = menu;
		menu.add(getUfoReport(), "");
		QueryNavigation.getSingleton().addMenu(menu);
		
		QueryNavigation.getSingleton().setCurWindow(m_querymenu);
		QueryNavigation.refreshMenu(getUfoReport());
//		getUfoReport().getReportMenuBar().addFocusListener(new FocusListener(){
//			public void focusGained(FocusEvent e) {
//				QueryNavigation.getSingleton().focusMenu(m_querymenu);
//			}
//			
//			public void focusLost(FocusEvent e) {
//			}
//			
//		});
// 	 
		this.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
				QueryNaviMenu curMenu = QueryNavigation.getSingleton().getCurWindow();
				if(curMenu != null && curMenu == m_querymenu){
					return;
				}
				QueryNavigation.getSingleton().focusMenu(m_querymenu);
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
    	
    	//********end. liuyy  *****//
	}
	
	public QueryNaviMenu getMenu(){
		return this.m_querymenu;
	}
	
	 
}
