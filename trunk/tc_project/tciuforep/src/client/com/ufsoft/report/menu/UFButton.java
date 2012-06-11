package com.ufsoft.report.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import com.ufsoft.report.lookandfeel.plaf.Office2003ButtonUI;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;

/**
 * 
 * @author zzl 2005-6-28
 */
public class UFButton extends JButton {
    IActionExt m_ext;
    
    /**
     * @param ext
     */
    public UFButton(IActionExt ext,ActionUIDes uiDes) {
        super();
        setName(uiDes.getName());//按钮没有Text显示.
        m_ext = ext;
    }

    public Point getToolTipLocaion(MouseEvent e) {
        Dimension size = getSize();
        return new Point(0, size.height);
    }
    public void adjustEnabled(Component focusComp){
        if(m_ext != null){
            setEnabled(m_ext.isEnabled(focusComp));
        }
    }

	@Override
	public void updateUI() {
		setUI(new Office2003ButtonUI());
	}
    
}
