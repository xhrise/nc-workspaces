package com.ufsoft.report.sysplugin.dnd;

import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * 
 * @author zzl 2005-4-25
 */
public class ListDndAdapter implements IDndAdapter {

    private JList m_list;

    /**
     * 
     */
    public ListDndAdapter(JList list) {
        m_list = list;
        ListModel dataModel = m_list.getModel();
        //转换模型为DefaultListModel的实例。
        if(!(dataModel instanceof DefaultListModel)){
            DefaultListModel newModel = new DefaultListModel();
            for(int i=0;i<dataModel.getSize();i++){
                newModel.addElement(dataModel.getElementAt(i));
            }
            m_list.setModel(newModel);
        }   
    }
    /*
     * @see dnd.IDndAdapter#getSourceObject()
     */
    public Object getSourceObject() {
        return m_list.getSelectedValue();
    }

    /*
     * @see dnd.IDndAdapter#removeSourceNode()
     */
    public void removeSourceNode() {
        getDefaultListModel().remove(m_list.getSelectedIndex());
    }

    /*
     * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
     */
    public boolean insertObject(Point ap, Object obj) {
        getDefaultListModel().addElement(obj);
        return true;
    }
    private DefaultListModel getDefaultListModel(){
        return (DefaultListModel) m_list.getModel();
    }
}
