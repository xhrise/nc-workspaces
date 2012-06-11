package com.ufida.report.multidimension.applet;

import java.awt.FlowLayout;
import java.awt.dnd.DnDConstants;

import javax.swing.JPanel;
import javax.swing.JTable;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.sysplugin.dnd.DndHandler;
import com.ufsoft.report.sysplugin.dnd.TableDndAdapter;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 
 * @author zzl 2005-4-27
 */
public class PageDimNavExt extends AbstractNavExt {

    /*
     * @see com.ufsoft.report.plugin.INavigationExt#getPanel()
     */
    public JPanel createPanel() {
        JPanel panel = new UIPanel();
        panel.setLayout(new FlowLayout());
        JTable table = new nc.ui.pub.beans.UITable(1,5);
        DndHandler.enableDndDrag(table,new TableDndAdapter(table),DnDConstants.ACTION_MOVE);
        DndHandler.enableDndDrop(table,new TableDndAdapter(table));
        panel.add(table);
        return panel;
    }

    /*
     * @see com.ufsoft.report.plugin.INavigationExt#getNavPanelPos()
     */
    public int getNavPanelPos() {
        return ReportNavPanel.NORTH_NAV;
    }

    /*
     * @see com.ufsoft.report.plugin.IExtension#getName()
     */
    /**
	 * @i18n mbiadhoc00039=页维度
	 */
    public String getName() {
        return StringResource.getStringResource("mbiadhoc00039");
    }

    /*
     * @see com.ufsoft.report.plugin.IExtension#getHint()
     */
    public String getHint() {
        // TODO 自动生成方法存根
        return null;
    }

}
