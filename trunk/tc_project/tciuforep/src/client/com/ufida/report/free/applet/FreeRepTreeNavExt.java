package com.ufida.report.free.applet;

import java.awt.BorderLayout;
import java.awt.dnd.DnDConstants;

import javax.swing.JPanel;
import javax.swing.JTree;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITree;

import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.sysplugin.dnd.DndHandler;
import com.ufsoft.report.sysplugin.dnd.TreeDndAdapter;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 
 * @author zzl 2005-4-27
 */
public class FreeRepTreeNavExt extends AbstractNavExt {

    /*
     * @see com.ufsoft.report.plugin.INavigationExt#getPanel()
     */
    public JPanel createPanel() {
        JPanel panel = new UIPanel();
        panel.setLayout(new BorderLayout());
        JTree tree = new UITree();
        DndHandler.enableDndDrag(tree,new TreeDndAdapter(tree),DnDConstants.ACTION_MOVE);
        panel.add(tree,BorderLayout.CENTER);
        return panel;
    }

    /*
     * @see com.ufsoft.report.plugin.INavigationExt#getNavPanelPos()
     */
    public int getNavPanelPos() {
        return ReportNavPanel.WEST_NAV;
    }

    /*
     * @see com.ufsoft.report.plugin.IExtension#getName()
     */
    /**
	 * @i18n ubidim0008=Î¬¶È
	 */
    public String getName() {
        return StringResource.getStringResource("ubidim0008");
    }

    /*
     * @see com.ufsoft.report.plugin.IExtension#getHint()
     */
    public String getHint() {
        return null;
    }

}
