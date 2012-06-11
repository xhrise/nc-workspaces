package com.ufsoft.report.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import nc.ui.pub.beans.UICheckBoxMenuItem;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.event.EventService;
import com.ufida.zior.exception.MessageException;
import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.UIStyle;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.SelectListener;

/**
 * 
 * @author zzl 2005-6-28
 */
public class MenuUtil {
	/**
	 * ����ActionUIDes����Ϣ�����������
	 * 
	 * @param uiDes
	 * @return JComponent modify by guogang 2007-9-29
	 *         ֧��ѡ����ȡ��ѡ���Ĳ˵���UICheckBoxMenuItem�Ĵ���
	 */
	public static JComponent createActionComp(IActionExt ext,
			ActionUIDes uiDes, UfoReport report) {
		// ��ʱ��֧�ּ���Menu����MenuItem��������������Ҳ�������򹤾������ʱ���ǡ�
		// modify by guogang
		// AbstractButton actionComp;
		JComponent actionComp;
		if (uiDes.isToolBar()
				&& (uiDes.getPaths() == null || uiDes.getPaths().length == 0)) {
			if (uiDes.isDirectory()) {
				actionComp = new UFMenuButton(ext, uiDes);
			} else if (uiDes.isListCombo()) {
				// modify by guogang 2007-5-31 ���������˵���button�Ĵ���
				actionComp = createComboComp(ext, uiDes, report);
			} else {
				actionComp = new UFButton(ext, uiDes);
				((AbstractButton) actionComp).setBorderPainted(false);
				actionComp.setBorder(new LineBorder(Color.black));
				
				actionComp.setEnabled(ext.isEnabled(report));
			}

		} else {
			if (uiDes.isDirectory()) {
				actionComp = new UFMenu(uiDes.getName(), report);
				if (uiDes.isCheckBoxMenuItem()) {
					actionComp.setVisible(true);
				}
			} else if (uiDes instanceof ToggleMenuUIDes) {
				if (((ToggleMenuUIDes) uiDes).isCheckBox()) {
					actionComp = new UFCheckBoxPopMenuItem(ext, uiDes, report);
					((UFCheckBoxPopMenuItem) actionComp)
							.setSelected(((ToggleMenuUIDes) uiDes).isSelected());
				} else {
					actionComp = new JRadioButtonMenuItem(uiDes.getName(),
							((ToggleMenuUIDes) uiDes).isSelected());
				}
			} else {
				if (uiDes.isPopup()) {
					actionComp = new UFPopMenuItem(ext, uiDes, report);
				} else {
					if (uiDes.isCheckBoxMenuItem()) {
						actionComp = new UFCheckBoxMenuItem(ext, uiDes, report);
						actionComp.setName(uiDes.getName());
					} else
						actionComp = new UFMenuItem(ext, uiDes, report);
				}
			}
			
			if (actionComp instanceof JMenuItem) {
				if (!(actionComp instanceof JMenu)) {
					((JMenuItem) actionComp).setAccelerator(uiDes
							.getAccelerator());
				}
				((JMenuItem) actionComp).setFont(UIStyle.MENUFONT);
				((JMenuItem) actionComp).setMnemonic(uiDes.getMnemonic());
			}
		}
		// ��һ���˵�����Ĭ��ͼ��.
		if (!(actionComp instanceof UFMenu)
				|| (uiDes.getPaths() != null && uiDes.getPaths().length > 0)
				|| uiDes.isPopup()) {
			if (actionComp instanceof AbstractButton) {
				if (((AbstractButton) actionComp).getIcon() == null) {// checkboxMenu��������ͼ�ꡣ
					((AbstractButton) actionComp).setIcon(ResConst
							.getImageIcon(uiDes.getImageFile()));
				}
			}
		}
		actionComp.setToolTipText(uiDes.getTooltip());

		ext.initListenerByComp(actionComp);
		
		EventListener lis = ext.getListener(actionComp);
		
		if (lis != null) {
			if (lis instanceof CellsModelListener) {
				report.getCellsModel().addCellsModelListener(
						(CellsModelListener) lis);
			}
			if (lis instanceof SelectListener) {
				report.getCellsModel().getSelectModel().addSelectModelListener(
						(SelectListener) lis);
			}
		}
 
		createExtListener(ext, report, actionComp, uiDes
				.getAccelerator());
		
		return actionComp;
	}

	/**
	 * ����һ�����ܵ�ļ�����.�˷����ṩ���˵�,������,״̬��ʹ��
	 * 
	 * @param ext
	 *            ���ܵ�����
	 * @param report
	 *            �����ߵľ��
	 * @param item
	 *            �˵���
	 * @return ActionListener
	 */
	private static void createExtListener(final ICommandExt ext,
			final UfoReport report, final JComponent item, KeyStroke keyStroke) {

		ActionListener listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				try {
					Object[] params = null;
					EventService.saveEventManager(report.getEventManager());					
					AppDebug.debug("ִ�ж�����" + ext.getClass().getName());
					
					params = ext.getParams(report);
					UfoCommand cmd = ext.getCommand();
					if (cmd != null) {
						cmd.run(params, item);
					}
					
				} catch(MessageException e2){
					UfoPublic.sendMessage(e2, report);
					
				} catch (Throwable t) {
					AppDebug.debug(t);
					//modified by biancm 20091028 ��ʾ�����������ϸ��Ϣ
					//UfoPublic.sendErrorMessage(t.getMessage(), report, t);
					UfoPublic.sendMessage(new MessageException(t.getMessage()), report);
				}
				finally {
					EventService.removeEventManager(report.getEventManager());
					
				}
				
			}
			
		};
		
		if (item instanceof AbstractButton) {
			((AbstractButton) item).addActionListener(listener);

		} else if (item instanceof JComboBox) {
			((JComboBox) item).addActionListener(listener);
		}
		
		// item.addActionListener(listener);
		// add by wangyga �˵��͹������İ�ť��ע���ݼ���ֻ�����һ�ߣ����Ԥ��Ѳ˵����óɲ���ʾ����ݼ�ʧȥ���õ�����
		item.registerKeyboardAction(listener, keyStroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	// /**
	// * ����JComboBox����ļ�����
	// *
	// * @param ext
	// * @param con
	// * @param item
	// */
	// private static void createExtListener(final ICommandExt ext,
	// final UfoReport con, final JComponent item, KeyStroke keyStroke) {
	// ActionListener listener = new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// try {
	// Object[] params = null;
	// params = ext.getParams(con);
	// UfoCommand cmd = ext.getCommand();
	// if (cmd != null) {
	// cmd.run(params, item);
	// }
	// // liuyy+ 2008-05-09
	// } catch (Throwable t) {
	// AppDebug.debug(t);
	// UfoPublic.sendErrorMessage(t.getMessage(), con, t);
	// }
	// }
	// };
	// item.addActionListener(listener);
	// // add by wangyga �˵��͹������İ�ť��ע���ݼ���ֻ�����һ�ߣ����Ԥ��Ѳ˵����óɲ���ʾ����ݼ�ʧȥ���õ�����
	// item.registerKeyboardAction(listener, keyStroke,
	// JComponent.WHEN_IN_FOCUSED_WINDOW);
	// }

	/**
	 * �ɵķ���
	 * 
	 * @param paths
	 * @param fromIndex
	 * @param comp
	 * @param report
	 * @return modify by guogang 2007-12-20 �������ĸ�û�д�����Ҫ��֤�����ø������ʱ��ӵ���ȷ�ķ���
	 */
	public static JComponent getCompByPath(String[] paths, int fromIndex,
			JComponent comp, UfoReport report) {
		return getCompByPath(paths, fromIndex, comp, report, null);
	}

	/**
	 * 
	 * @param paths
	 * @param fromIndex
	 * @param comp
	 * @param report
	 * @param group
	 * @return
	 */
	public static JComponent getCompByPath(String[] paths, int fromIndex,
			JComponent comp, UfoReport report, String group) {
		if (paths == null || paths.length == fromIndex) {
			return (JComponent) comp;
		}
		Component[] comps = comp.getComponents();
		for (Component subComp : comps) {
			if (subComp == null || subComp.getName() == null) {
				continue;
			}

			if (subComp instanceof JMenu
					&& subComp.getName().equals(getMenuName(paths[fromIndex]))) {
				return getCompByPath(paths, fromIndex + 1,
						(JComponent) subComp, report, group);
			}

		}
		
		JMenu newMenu = new UFMenu(getMenuName(paths[fromIndex]), report);
		if(fromIndex == 0){//by ll, 1���˵����� ���ټ� ��ע�Ṧ��
			char mnemonic = getMenuMnemonic(paths[fromIndex]);
			if(mnemonic != 0)
				newMenu.setMnemonic(mnemonic);
		}
//		newMenu.setIcon(ResConst.getImageIcon(null));// ���ڲ˵����롣
		if (group == null) {
			group = "";
		}
		if (comp instanceof UFMenu) {
			((UFMenu) comp).add(newMenu, group);
		} else if (comp instanceof UFPopupMenu) {
			((UFPopupMenu) comp).add(newMenu, group);
		} else {
			((Container) comp).add(newMenu, 5);
		}
		return getCompByPath(paths, fromIndex + 1, newMenu, report, group);
	}
	private static String getMenuName(String path){
		int idx = path.indexOf('\t');
		if(idx>=0){
			return path.substring(0, idx);
		}
		return path;
	}
	private static char getMenuMnemonic(String path){
		int idx = path.indexOf('\t');
		if(idx>=0){
			return path.charAt(idx+1);
		}
		return 0;
	}
	public static void addCompToParent(JComponent comp, JComponent parent,
			ActionUIDes uiDes) {
		if (parent instanceof UFMenu) {
			String buttonGroup = null;
			if (uiDes instanceof ToggleMenuUIDes) {
				buttonGroup = ((ToggleMenuUIDes) uiDes).getButtonGroup();
			}
			((UFMenu) parent).add(comp, uiDes.getGroup(), buttonGroup);
		} else if (parent instanceof UFPopupMenu) {
			String buttonGroup = null;
			if (uiDes instanceof ToggleMenuUIDes) {
				buttonGroup = ((ToggleMenuUIDes) uiDes).getButtonGroup();
			}
			((UFPopupMenu) parent).add(comp, uiDes.getGroup(), buttonGroup);
		} else if (parent instanceof ReportMenuBar) {

			((ReportMenuBar) parent).add(comp);

		}
	}

	/**
	 * add by guogang 2007-6-17 �������src����ͼ
	 * 
	 * @param paths
	 *            ������ͼ�����λ��
	 * @param fromIndex
	 * @param report
	 * @param src
	 */
	public static void buildCompView(String[] paths, int fromIndex,
			UfoReport report, final JComponent src) {
		JComponent parent = getCompByPath(paths, fromIndex, report
				.getReportMenuBar(), report, null);
		JCheckBoxMenuItem viewOne = new UICheckBoxMenuItem();
		viewOne.setName(src.getName());
		viewOne.setText(src.getName());
		viewOne.setSelected(true);
		viewOne.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					src.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					src.setVisible(false);
				}

			}
		});
		parent.add(viewOne);
	}

	/**
	 * add by guogang 2007-5-31 ���������˵��Ĵ���
	 */
	public static JComponent createComboComp(IActionExt ext, ActionUIDes uiDes,
			UfoReport con) {
		JComboBox actionComp = null;

		if (uiDes.getComboType() == 0) {
			actionComp = new JComboBox();
			String[] comboItem = uiDes.getListItem();
			for (int i = 0; i < comboItem.length; i++) {
				actionComp.addItem(comboItem[i]);
			}

		} else if (uiDes.getComboType() == 1) {
			if (uiDes.getComboComponent() instanceof SwatchPanel) {
				actionComp = new JPopupPanelButton(ext, uiDes.getName(),
						ResConst.getImageIcon(uiDes.getImageFile()),
						(SwatchPanel) uiDes.getComboComponent());
			}
		}
		// add by ����� 2008-3-21 ���Ӷ������˵���֧��
		else if (uiDes.getComboType() == 2) {
			if (uiDes.getComboComponent() instanceof JPopupMenu) {
				actionComp = new JPopupPanelButton(ext, uiDes.getName(),
						ResConst.getImageIcon(uiDes.getImageFile()),
						(JPopupMenu) uiDes.getComboComponent());
			}
		}
		// end
		if (actionComp != null) {
			actionComp.setName(uiDes.getName());
			actionComp.setSelectedItem(uiDes.getDefaultSelected());
			// actionComp.setBorder(BorderFactory.createTitledBorder(uiDes.getName()));
			// actionComp.setMinimumSize(new Dimension(50,32));
			// Dimension dm=actionComp.getPreferredSize();
			// if(dm.width<50)
			// actionComp.setPreferredSize(new Dimension(50,dm.height));
		}
		return actionComp;
	}

}
