package com.ufsoft.report.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.sysplugin.file.FileSaveExt;

/**
 * ������֧�������˵��İ�ť��
 * �����ο���http://jroller.com/page/santhosh/20050528#dropdownbutton_for_swing
 * ��������״̬��
 * 1. ���Ӱ�ť�����߽�DropDownButton��
 * 2. ���Ӱ�ť��������ť���ɵ�����
 * @author zzl 2005-6-28
 */
public class UFMenuButton extends UFButton implements ActionListener {
    
    /**����ť�Ƿ���Ե��ִ��*/
    private JPopupMenu popupMenu;

    public UFMenuButton(IActionExt ext,ActionUIDes uiDes) {
        super(ext,uiDes);
//        this.setIcon(ResConst.getImageIcon("reportcore/dropdown.gif"));
        this.addActionListener(this);

    }

    /*---------------[ ActionListener ]---------*/

    public void actionPerformed(ActionEvent ae) {
        JPopupMenu popup = getPopupMenu();
        popup.show(this, 0, getHeight());
    }


    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new UFPopupMenu();
        }
        popupMenu.setBorder(BorderFactory.createLineBorder(new Color(0xB0,0xC1,0xD5)));
        return popupMenu;
    }
    /**
     * ע�ⲻҪ����add��������������뵽toolbar�С�
     * @param toolbar
     * @return JButton
     */
    public JButton addToToolBar(JToolBar toolbar) {
            toolbar.add(this);
        return this;
    }
    public Component[] getComponents() {
        return getPopupMenu().getComponents();
    }

    /*
     * @see java.awt.Container#add(java.awt.Component)
     */
    public Component add(Component comp) {
        if (comp instanceof JMenuItem) {
            return getPopupMenu().add(comp);
        } else {
            throw new IllegalArgumentException();
        }
    }
    public static void main(String[] args) {
        IActionExt ext = new FileSaveExt(null)
        {
        	public UfoCommand getCommand() {
        		return null;
        	}    
        }
        ;
        ActionUIDes uiDes = ext.getUIDesArr()[0];
        uiDes.setDirectory(false);
        UFMenuButton b  = new UFMenuButton(ext,uiDes);
        b.setIcon(ResConst.getImageIcon(uiDes.getImageFile()));
        JToolBar toolbar = new JToolBar();
        b.addToToolBar(toolbar);
        JOptionPane.showMessageDialog(null,toolbar);
        
    }
}
