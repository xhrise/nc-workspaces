package com.ufsoft.iufo.fmtplugin;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import nc.ui.pub.beans.util.NCOptionPane;

import com.ufsoft.iufo.inputplugin.querynavigation.QueryNaviMenu;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.iufo.resource.StringResource;


/**
 * 综合查询弹出框。 
 * @author liuyy
 *
 */
public class ReportQueryFrame  extends JFrame {
	private static final long serialVersionUID = -346050206092350006L;

	private QueryNaviMenu menu = null;
	private boolean needSave;

	public ReportQueryFrame(UfoReport container,boolean needSave) {
		super();
		this.needSave=needSave;
		changeReport(container);

		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				QueryNaviMenu curMenu = QueryNavigation.getSingleton().getCurWindow();
				if(curMenu != null && curMenu == menu){
					return;
				}
				if(menu != null){
					QueryNavigation.getSingleton().focusMenu(menu);
				}
			}
			public void windowLostFocus(WindowEvent e) {
				
			}
		});
		
		this.addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent e) {
			}
			public void windowClosed(WindowEvent e) {
			}
			/**
			 * @i18n miufohbbb00076=是否保存
			 * @i18n miufohbbb00078=请选择
			 */
			public void windowClosing(WindowEvent e) {
				if(menu != null){
					QueryNavigation.getSingleton().removeMenu(menu);
				}
				UfoReport report=UIUtilities.getUfoReport4Child(ReportQueryFrame.this);
				if(report!=null&&report.isDirty()&&isNeedSave()){
					if(UfoPublic.showConfirmDialog(ReportQueryFrame.this, StringResource.getStringResource("miufohbbb00076"), StringResource.getStringResource("miufohbbb00078"), NCOptionPane.YES_NO_OPTION)==NCOptionPane.YES_OPTION){
						report.store();
					}
					
				}
				ReportQueryFrame.this.dispose();
				
			}
			
			
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
			}
		});
	}

	public void changeReport(UfoReport container) {
//		setRootPane(container);
		
		UIUtilities.ufoReport2JRootPane(container, getRootPane());
		
	}

	public QueryNaviMenu getMenu() {
		return menu;
	}

	public void setMenu(QueryNaviMenu menu) {
		this.menu = menu;
	}
    private boolean isNeedSave(){
    	return needSave;
    }
} 