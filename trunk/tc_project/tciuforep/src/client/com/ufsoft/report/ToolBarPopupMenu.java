/*
 * ToolBarPopupMenu.java
 * �������� 2004-11-11
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
 * ������ѡ�񵯳��˵�
 * �����û�ѡ����ʾ�������еĹ�������
 * @author caijie 
 * @since 3.1
 */
public class ToolBarPopupMenu extends nc.ui.pub.beans.UIPopupMenu implements ActionListener{
 
	private static final long serialVersionUID = 1L;

	/**
     * ������
     */
    private UfoReport m_repTool = null;
    
    /**
     * ������    
     */
    
    private HashMap m_hmMenuItem = null;
    /**
     * 
     * @param repTool ������
     */
    public ToolBarPopupMenu(UfoReport repTool){
        super();
        if(repTool == null){
            IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000823"));//�����ߵ�ֵΪ��
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000823"));//"�����ߵ�ֵΪ��
        }
        m_repTool = repTool;
        init();
    }
    
    /**
     * ��ʼ��,�Ը�ѡ��˵������ʽ�г�m_repTool�еǼǵ����й�����
     */
    private void init(){
        JToolBar[] bars = this.getUfoReport().getToolBar();
        if ((bars == null) || (bars.length == 0))
            return;
        JCheckBoxMenuItem item = null;
        m_hmMenuItem = new HashMap();
        //�����еĹ��������ڲ˵���
        for (int i = 0; i < bars.length; i++) {
            if ((bars[i] != null) && (bars[i] instanceof JToolBar)) {
                item = new UICheckBoxMenuItem(bars[i].getName());
                item.addActionListener(this);
                this.add(item);                
                m_hmMenuItem.put(item, bars[i]);
                
                //�˵����ʼ��ѡ��״̬
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
     * @return ���� UfoReport��
     */
    public UfoReport getUfoReport() {
        return m_repTool;
    }
}
