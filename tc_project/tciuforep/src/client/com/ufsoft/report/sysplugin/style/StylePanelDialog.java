package com.ufsoft.report.sysplugin.style;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.util.MultiLang;
/**
 * ��ʾ���Ի���
 * 
 * @author wupeng
 * @version 3.1
 */
public class StylePanelDialog extends BaseDialog {
	private static final long serialVersionUID = -4397999322753422902L;
	private StylePanel m_Panel;

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            �����
	 */
	public StylePanelDialog(Component parent) {
		super(parent, MultiLang.getString("miufo1001189"), true);//��ʾ���
		setSize(400, 300);
		setResizable(false);
		//	������ʾ
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension frameSize = getSize();
//		setLocation((screenSize.width - frameSize.width) / 2,
//				(screenSize.height - frameSize.height) / 2);

	}
	/**
	 * ���ý�����
	 * 
	 * @param style
	 */
	public void setReportStyle(ReportStyle style) {
		m_Panel.setReportStyle(style);
	}
	/**
	 * �õ�����������
	 * 
	 * @return ReportStyle
	 */
	public ReportStyle getReportStyle() {
		return m_Panel.getReportStyle();
	}

	protected JPanel createDialogPane() {
		m_Panel = new StylePanel();
		return m_Panel;
	}
}