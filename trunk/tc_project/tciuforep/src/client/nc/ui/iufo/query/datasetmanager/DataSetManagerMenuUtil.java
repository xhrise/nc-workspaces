package nc.ui.iufo.query.datasetmanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.LineBorder;

import nc.ui.iufo.query.datasetmanager.exts.DSMActionExt;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.UIStyle;
import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.menu.UFCheckBoxMenuItem;
import com.ufsoft.report.menu.UFCheckBoxPopMenuItem;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.menu.UFMenuButton;
import com.ufsoft.report.menu.UFMenuItem;
import com.ufsoft.report.menu.UFPopMenuItem;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;

/**
 * 
 * @author zzl 2005-6-28
 */
public class DataSetManagerMenuUtil {
	/**
	 * ����ActionUIDes����Ϣ�����������
	 * 
	 * @param uiDes
	 * @return JComponent modify by guogang 2007-9-29
	 *         ֧��ѡ����ȡ��ѡ���Ĳ˵���UICheckBoxMenuItem�Ĵ���
	 */
	public static JComponent createActionComp(IActionExt ext,
			ActionUIDes uiDes, DataSetManager datasetManager) {
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
				actionComp = createComboComp(ext, uiDes);
			} else {
				actionComp = new UFButton(ext, uiDes);
				((AbstractButton) actionComp).setBorderPainted(false);
				actionComp.setBorder(new LineBorder(Color.black));
			}
		} else {
			if (uiDes.isDirectory()) {
				actionComp = new UFMenu(uiDes.getName(), null);
				if (uiDes.isCheckBoxMenuItem()) {
					actionComp.setVisible(true);
				}
			} else if (uiDes instanceof ToggleMenuUIDes) {
				if (((ToggleMenuUIDes) uiDes).isCheckBox()) {
					actionComp = new UFCheckBoxPopMenuItem(ext, uiDes, null);
					((UFCheckBoxPopMenuItem) actionComp)
							.setSelected(((ToggleMenuUIDes) uiDes).isSelected());
				} else {
					actionComp = new JRadioButtonMenuItem(uiDes.getName(),
							((ToggleMenuUIDes) uiDes).isSelected());
				}
			} else {
				if (uiDes.isPopup()) {
					actionComp = new UFPopMenuItem(ext, uiDes, null);
				} else {
					if (uiDes.isCheckBoxMenuItem()) {
						actionComp = new UFCheckBoxMenuItem(ext, uiDes, null);
						actionComp.setName(uiDes.getName());
					} else
						actionComp = new UFMenuItem(ext, uiDes, null);
				}
			}
			if (actionComp instanceof AbstractButton) {
				((AbstractButton) actionComp).setMnemonic(uiDes.getMnemonic());
			}
			if (actionComp instanceof JMenuItem) {
				if (!(actionComp instanceof JMenu)) {
					((JMenuItem) actionComp).setAccelerator(uiDes
							.getAccelerator());
				}
				((JMenuItem) actionComp).setFont(UIStyle.MENUFONT);
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

		if ((actionComp instanceof AbstractButton)
				&& (ext instanceof DSMActionExt)) {
			createExtListener((DSMActionExt) ext, datasetManager,
					(AbstractButton) actionComp);

		}
		if ((actionComp instanceof JComboBox) && (ext instanceof DSMActionExt)) {
			createExtListener((DSMActionExt) ext, datasetManager,
					(JComboBox) actionComp);
		}

		return actionComp;
	}

	/**
	 * ����һ�����ܵ�ļ�����.�˷����ṩ���˵�,������,״̬��ʹ��
	 * 
	 * @param ext
	 *            ���ܵ�����
	 * @param con
	 *            �����ߵľ��
	 * @param item
	 *            �˵���
	 * @return ActionListener
	 */
	private static void createExtListener(final DSMActionExt ext,
			final DataSetManager con, final AbstractButton item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] params = null;
				params = ext.getParams(con);
				UfoCommand cmd = ext.getCommand();
				if (cmd != null) {
					cmd.run(params, item);
				}
			}
		});
	}

	/**
	 * ����JComboBox����ļ�����
	 * 
	 * @param ext
	 * @param con
	 * @param item
	 */
	private static void createExtListener(final DSMActionExt ext,
			final DataSetManager con, final JComboBox item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] params = null;
				params = ext.getParams(con);
				UfoCommand cmd = ext.getCommand();
				if (cmd != null) {
					cmd.run(params, item);
				}
			}
		});
	}

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
			JComponent comp, DataSetManager dataSetManager) {
		return getCompByPath(paths, fromIndex, comp, dataSetManager, null);
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
			JComponent comp, DataSetManager dataSetManager, String group) {
		if (paths == null || paths.length == fromIndex) {
			return (JComponent) comp;
		}
		Component[] comps = comp.getComponents();
		for (int i = 0; i < comps.length; i++) {
			// �����try����getName()Ϊ�յ�������ֵ��龰��
			try {
				if (comps[i] instanceof JMenu
						&& comps[i].getName().equals(paths[fromIndex])) {
					return getCompByPath(paths, fromIndex + 1,
							(JComponent) comps[i], dataSetManager, group);
				}
			} catch (NullPointerException e) {
//				System.out.println("Component:" + comps[i]);
				AppDebug.debug(e);
			}
		}
		JMenu newMenu = new UFMenu(getMenuName(paths[fromIndex]), null);
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
		return getCompByPath(paths, fromIndex + 1, newMenu, dataSetManager, group);
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
//	public static void buildCompView(String[] paths, int fromIndex,
//			DataSetManager dataSetManager, final JComponent src) {
//		JComponent parent = getCompByPath(paths, fromIndex, report
//				.getReportMenuBar(), report, null);
//		JCheckBoxMenuItem viewOne = new UICheckBoxMenuItem();
//		viewOne.setName(src.getName());
//		viewOne.setText(src.getName());
//		viewOne.setSelected(true);
//		viewOne.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent e) {
//				if (e.getStateChange() == ItemEvent.SELECTED) {
//					src.setVisible(true);
//				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
//					src.setVisible(false);
//				}
//
//			}
//		});
//		parent.add(viewOne);
//	}

	/**
	 * add by guogang 2007-5-31 ���������˵��Ĵ���
	 */
	public static JComponent createComboComp(IActionExt ext, ActionUIDes uiDes) {
		JComboBox actionComp = null;

		if (uiDes.getComboType() == 0) {
			actionComp = new JComboBox();
			String[] comboItem = uiDes.getListItem();
			for (int i = 0; i < comboItem.length; i++) {
				actionComp.addItem(comboItem[i]);
			}
		}
		if (uiDes.getComboType() == 1) {
			// modify by �����
			if(uiDes.getComboComponent() instanceof SwatchPanel){
			   actionComp = new JPopupPanelButton(ext,uiDes.getName(), ResConst
					.getImageIcon(uiDes.getImageFile()), (SwatchPanel)uiDes.getComboComponent());
			}
		}
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
