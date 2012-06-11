/*
 * �������� 2004-11-10
 */
package com.ufsoft.report.sysplugin.help;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * ���ڶԻ���Ľڵ�
 * @author wupeng
 * @version 3.1
 */
public class HelpAboutExt extends AbsActionExt{// implements IMainMenuExt {

	private JDialog m_AboutDialog;
/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		// TODO �Զ����ɷ������
		return MultiLang.getString("miufo1000549");//����
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getHint()
	 */
	public String getHint() {
		// TODO �Զ����ɷ������
		return null;
	}

//	/* ���� Javadoc��
//	 * @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
//	 */
//	public int getMenuSlot() {
//		// TODO �Զ����ɷ������
//		return ReportMenuBar.HELP_END;
//	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		// TODO �Զ����ɷ������
		return "ufHeart.gif";
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		// TODO �Զ����ɷ������
		return null;
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		// TODO �Զ����ɷ������
		return null;
	}
	
	/* ���� Javadoc��
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
//		if(m_AboutDialog==null){
			m_AboutDialog = new AboutDialog(container);
			java.awt.Insets insets = m_AboutDialog.getInsets();
			m_AboutDialog.setSize(m_AboutDialog.getWidth() + insets.left + insets.right, m_AboutDialog.getHeight() + insets.top + insets.bottom);
			
//		}
		m_AboutDialog.setVisible(true);
		return null;
	}

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n help=����
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000549"));
        uiDes.setPaths(new String[]{MultiLang.getString("help")});
        //xulm 2009-09-03 ���ð������ܷ���
        uiDes.setGroup("help");
        return new ActionUIDes[]{uiDes};
    }
}
  