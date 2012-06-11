package com.ufsoft.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JRootPane;

public class UIUtilities {

	public static JRootPane getRootPane(UfoReport report) {
		Container container = report;
		while (container != null) {
			if (container instanceof JRootPane) {
				return (JRootPane) container;
			}
			container = container.getParent();
		}

		return null;
	}

	/**
	 * 父面板中寻找UfoReport
	 * 
	 * @param childContainer
	 * @return
	 */
	public static UfoReport getUfoReport4Parent(Container childContainer) {
		Container container = childContainer;
		while (container != null) {
			if (container instanceof UfoReport) {
				return (UfoReport) container;
			}
			container = container.getParent();
		}

		return null;
	}

	/**
	 * 子组件中寻找UfoReport
	 * 
	 * @param parent
	 * @return
	 */
	public static UfoReport getUfoReport4Child(Container parent) {
		final Container container = parent;
		while (container != null) {
			if (container instanceof UfoReport) {
				return (UfoReport) container;
			}
			Component[] comps = container.getComponents();
			for (Component comp : comps) {
				if (comp == container) {
					return null;
				}
				if (comp instanceof Container) {
					UfoReport report = getUfoReport4Child((Container) comp);
					if (report != null) {
						return report;
					}
				}
			}
			return null;
		}

		return null;
	}

	public static void ufoReport2JRootPane(UfoReport report, JRootPane rootPane) {
		if (report == null || rootPane == null) {
			return;
		}

		Container contentPane = rootPane.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.removeAll();
		contentPane.add(report, BorderLayout.CENTER);

		// 重要
		rootPane.setJMenuBar(report.getJMenuBar());

	}

	public static JPanel showPanelBorder(JPanel src) {
		return showPanelBorder(src, 30);
	}

	public static JPanel showPanelBorder(JPanel src, int borderWidth) {

		JPanel pnl = new JPanel();

		// pnl.setBorder(BorderFactory.createEmptyBorder(borderWidth,
		// borderWidth, borderWidth, borderWidth));
		// pnl.add(src, BorderLayout.CENTER);

		//		
		//		
		pnl.setLayout(new BorderLayout());

		pnl.add(src, BorderLayout.CENTER);

		JPanel pnlBorder = new JPanel();
		pnlBorder.setMaximumSize(new Dimension(borderWidth, borderWidth));
		pnl.add(pnlBorder, BorderLayout.SOUTH);

		pnlBorder = new JPanel();
		pnlBorder.setMaximumSize(new Dimension(borderWidth, borderWidth));
		pnl.add(pnlBorder, BorderLayout.EAST);

		pnlBorder = new JPanel();
		pnlBorder.setMaximumSize(new Dimension(borderWidth, borderWidth));
		pnl.add(pnlBorder, BorderLayout.NORTH);

		pnlBorder = new JPanel();
		pnlBorder.setMaximumSize(new Dimension(borderWidth, borderWidth));
		pnl.add(pnlBorder, BorderLayout.WEST);

		return pnl;
	}

}
