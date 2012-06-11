package com.ufida.report.multidimension.applet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.SelDimMemberVO;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.iufo.resource.StringResource;

public class SelMemberListRenderer extends UIPanel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String IMAGE_PATH = "biplugin/";

	private static final String REPORT_IMAGE_PATH = "reportcore/";

	/** 类型图片 */
	protected JLabel m_imageLabel = null;

	protected CheckNodeLabel m_textLabel = null;

	private Hashtable<Integer, Icon> m_table = new Hashtable<Integer, Icon>();

	/**
	 * CheckNodeRenderer 构造子注解。
	 */
	public SelMemberListRenderer() {
		super();
		setLayout(new java.awt.BorderLayout());
		add(getTextLabel(), BorderLayout.CENTER);
		add(getImageLabel(), BorderLayout.WEST);
//		setBackground(javax.swing.UIManager.getColor("List.background"));
//		getTextLabel().setBackground(javax.swing.UIManager.getColor("List.background"));
//		getImageLabel().setBackground(javax.swing.UIManager.getColor("List.background"));
		
	}

	/**
	 * 选择框。 创建日期：(2001-11-7 19:23:19)
	 * 
	 * @since V1.00
	 * @return javax.swing.JCheckBox
	 */
	public CheckNodeLabel getTextLabel() {
		if (m_textLabel == null) {
			m_textLabel = new CheckNodeLabel();
		}
		return m_textLabel;
	}

	/**
	 * 选择标签。 创建日期：(2001-11-7 19:23:19)
	 * 
	 * @since V1.00
	 * @return nc.install.ui.CheckNodeLabel
	 */
	public JLabel getImageLabel() {
		if (m_imageLabel == null) {
			m_imageLabel = new nc.ui.pub.beans.UILabel();
			m_imageLabel.setPreferredSize(new Dimension(20, 22));
		}
		return m_imageLabel;
	}

	/**
	 * 返回首选大小。 创建日期：(2001-11-7 21:48:40)
	 * 
	 * @since V1.00
	 * @return java.awt.Dimension
	 */
	public java.awt.Dimension getPreferredSize() {
		Dimension dTextLabel = getTextLabel().getPreferredSize();
		Dimension dImageLabel = getImageLabel().getPreferredSize();
		return new Dimension(dTextLabel.width + dImageLabel.width,
				(dTextLabel.height < dImageLabel.height ? dImageLabel.height + 1 : dTextLabel.height + 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (value instanceof SelDimMemberVO) {
			SelDimMemberVO selMem = (SelDimMemberVO) value;
			String stringValue = selMem.getMemberVO().getName();
			getTextLabel().setText(stringValue);

			getImageLabel().setIcon(getIcon(selMem.getSelectType()));

			getTextLabel().setFont(list.getFont());
			getTextLabel().setSelected(isSelected);
			getTextLabel().setHasFocus(cellHasFocus);

//			getTextLabel().setBackground(isSelected ? list.getSelectionBackground()
//					: Color.white);
			setBackground(isSelected ? list.getSelectionBackground()
					: list.getBackground());

		}
		return this;
	}

	/**
	 */
	private Icon getIcon(int selType) {
		Icon icon = null;
		if (m_table.containsKey(new Integer(selType)))
			icon = (Icon) m_table.get(new Integer(selType));
		else {
			String filename = null;
			switch (selType) {
			case IMultiDimConst.SELMEMBER_SELF_ONLY:
				filename = REPORT_IMAGE_PATH + "blank.gif";
				break;
			case IMultiDimConst.SELMEMBER_CHILDREN:
				filename = IMAGE_PATH + "children.gif";
				break;
			case IMultiDimConst.SELMEMBER_DESCENDANT:
				filename = IMAGE_PATH + "descendant.gif";
				break;
			case IMultiDimConst.SELMEMBER_PARENT:
				filename = IMAGE_PATH + "parent.gif";
				break;
			case IMultiDimConst.SELMEMBER_ANCESTOR:
				filename = IMAGE_PATH + "ancestor.gif";
				break;
			case IMultiDimConst.SELMEMBER_SAME_GENERATION:
				filename = IMAGE_PATH + "same_generation.gif";
				break;

			default:
				break;
			}
			if (filename != null) {
				try {
					icon = ResConst.getImageIcon(filename);
				} catch (NullPointerException ex) {
					AppDebug.debug("图片加载路径设置有问题。");// @devTools
														// System.out.println("TODO
														// 图片加载路径设置有问题。");
				}
				if (icon != null)
					m_table.put(new Integer(selType), icon);
			}
		}
		return icon;
	}
}  