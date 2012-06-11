package com.ufsoft.report.sysplugin.dnd;
import java.awt.dnd.DnDConstants;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;

import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;


/**
 * 
 * @author zzl 2005-4-19
 */
public class RunDndTester {

    public static void main(String[] args) {
//        JList c1 = new DndList(new Object[]{"aa","bb"});
//        JList c2 = new DndList(new Object[]{"cc","dd"});
        JTree c1 = new UITree();
//        JTree c2 = new DndTree();
//        JTable c1 = new DndTable(3,3);
        JTable c2 = new nc.ui.pub.beans.UITable(3,3);
//        
//        c1.enableDndDrag(DnDConstants.ACTION_MOVE);
//        c2.enableDndDrop();        
//        c1.enableDndDrop();
//        
        DndHandler.enableDndDrag(c1,new TreeDndAdapter(c1),DnDConstants.ACTION_MOVE);
        DndHandler.enableDndDrop(c2,new TableDndAdapter(c2));
        JSplitPane pane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT,c1,c2);
        
        JFrame f = new UIFrame();
        f.getContentPane().add(pane);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(200,200,500,500);
        f.setVisible(true);
    }
}
