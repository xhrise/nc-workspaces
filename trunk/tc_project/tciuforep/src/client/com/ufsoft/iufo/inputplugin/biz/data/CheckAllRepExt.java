/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.util.MultiLang;

/**
 * 全部审核（任务内的所有报表）的IActionExt实现类
 * 
 * @author liulp
 *
 */
public class CheckAllRepExt extends AbsIufoBizMenuExt{
    private boolean m_bHasTaskCheckFormula = false;
    public CheckAllRepExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    
    public void setIsHasTaskCheckFormula(boolean bHasTaskCheckFormula){
        this.m_bHasTaskCheckFormula = bHasTaskCheckFormula;
    }
    private boolean isHasTaskCheckFormula(){
        return this.m_bHasTaskCheckFormula;
    }

    @Override
	protected String getGroup() {
		return "repCheckGroup";
	}

	protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0006");//"表间审核";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new CheckAllRepCmd(ufoReport);
    }

    
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return  isHasTaskCheckFormula();
    }
    /**
	 * 额外的事件处理，用来打开面板
	 */
	public void initListenerByComp(Component stateChangeComp) {
		// TODO Auto-generated method stub
		if(stateChangeComp instanceof AbstractButton){
			((AbstractButton)stateChangeComp).addActionListener(
					new ActionListener() {

						/**
						 * @i18n uiuforep00115=审核结果
						 */
						public void actionPerformed(ActionEvent e) {
                            String name=MultiLang.getString("uiuforep00115");
							UfoReport report=getUfoReport();
							JComponent com=MenuUtil.getCompByPath(new String[]{MultiLang.getString("view"),MultiLang.getString("panelManager")}, 0, report.getJMenuBar(), report);
							JCheckBoxMenuItem windowMenu=null;
							if(com instanceof JMenu){
								JMenu menu=(JMenu)com;
								if(menu.getItemCount()<1){
									ActionListener[] listeners=menu.getActionListeners();
									ActionEvent event = null;
									for(int i=0;i<listeners.length;i++){
										event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
										listeners[i].actionPerformed(event);
									}
								}
									
							JPopupMenu popmenu=menu.getPopupMenu();
							for(int i=0;i<popmenu.getComponentCount();i++){
								if(name.equals(popmenu.getComponent(i).getName())&&popmenu.getComponent(i) instanceof JCheckBoxMenuItem)
									windowMenu=(JCheckBoxMenuItem)popmenu.getComponent(i);
							}
						    
							}
							if(windowMenu!=null&&!windowMenu.isSelected())
								windowMenu.setSelected(true);
								
						}
				
			});
		}
	}
}
 