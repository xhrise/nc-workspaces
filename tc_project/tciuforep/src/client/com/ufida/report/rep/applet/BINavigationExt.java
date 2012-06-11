/*
 * Created on 2005-6-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.plugin.AbstractNavExt;

/**
 * 导航扩展
 * @author caijie
 */
public class BINavigationExt extends AbstractNavExt{
     int pos = 0;
    /**    
     * @param pos -导航面板的位置
     * @param panel 导航面板
     */
    public BINavigationExt(int pos, JPanel panel) {
        this.pos = pos;
        setPanel(panel);
    }
    
    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.INavigationExt#getNavPanelPos()
     */
    public int getNavPanelPos() {
        // TODO Auto-generated method stub
        return pos;
    }    
   
    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.IExtension#getName()
     */
    public String getName() {
        return getPanel().getName();
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.IExtension#getHint()
     */
    public String getHint() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	protected JPanel createPanel() {
		// TODO Auto-generated method stub
		return new UIPanel();
	}

   
}
