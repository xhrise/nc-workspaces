/*
 * ToolBarPopupMenu.java
 * 创建日期 2004-11-11
 * Created by CaiJie
 */
package com.ufsoft.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToolBar;

import nc.ui.pub.beans.UICheckBoxMenuItem;

import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
/**
 * 工具栏选择弹出菜单
 * 可由用户选择显示报表工具中的工具条．
 * @author caijie 
 * @since 3.1
 */
public class ToolBarPopupMenu extends nc.ui.pub.beans.UIPopupMenu implements ActionListener{
 
	private static final long serialVersionUID = 1L;

	/**
     * 报表工具
     */
    private UfoReport m_repTool = null;
    
    /**
     * 工具条    
     */
    
    private HashMap m_hmMenuItem = null;
    /**
     * 
     * @param repTool 报表工具
     */
    public ToolBarPopupMenu(UfoReport repTool){
        super();
        if(repTool == null){
            IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000823"));//报表工具的值为空
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000823"));//"报表工具的值为空
        }
        m_repTool = repTool;
        init();
    }
    
    /**
     * 初始化,以复选框菜单项的形式列出m_repTool中登记的所有工具条
     */
    private void init(){
        JToolBar[] bars = this.getUfoReport().getToolBar();
        if ((bars == null) || (bars.length == 0))
            return;
        JCheckBoxMenuItem item = null;
        m_hmMenuItem = new HashMap();
        //将所有的工具条列在菜单中
        for (int i = 0; i < bars.length; i++) {
            if ((bars[i] != null) && (bars[i] instanceof JToolBar)) {
                item = new UICheckBoxMenuItem(bars[i].getName());
                item.addActionListener(this);
                this.add(item);                
                m_hmMenuItem.put(item, bars[i]);
                
                //菜单项初始的选择状态
                if (bars[i].isVisible()) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }

        }
       
    }
    
    /*
     *  
     */
    public void actionPerformed(ActionEvent e) {
        if (e == null) return;
        if(m_hmMenuItem.containsKey(e.getSource())){
            ReportToolBar bar = (ReportToolBar) m_hmMenuItem.get(e.getSource());
            bar.setVisible(!bar.isVisible());
        }     
    }
    
    
    /**
     * @return 返回 UfoReport。
     */
    public UfoReport getUfoReport() {
        return m_repTool;
    }
}
