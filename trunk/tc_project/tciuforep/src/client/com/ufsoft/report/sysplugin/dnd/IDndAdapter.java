package com.ufsoft.report.sysplugin.dnd;

import java.awt.Point;

/**
 * ֧����ק���ܵ�JComponent��������
 * @author zzl 2005-4-25
 */
public interface IDndAdapter {
    public Object getSourceObject();
    /**
     *  
     * return void
     */
    void removeSourceNode();
    /**
     * @param ap
     * @param obj 
     * return void
     */
    boolean insertObject(Point ap, Object obj);
}
