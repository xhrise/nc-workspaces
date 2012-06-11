//Source file: F:\\workspace\\reportTool\\src\\com\\ufsoft\\report\\ReportMenuBar.java

package nc.ui.iufo.query.datasetmanager;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JMenu;

import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ] �������еĲ˵���
 */
public class DataSetManagerMenuBar extends nc.ui.pub.beans.UIMenuBar {
	// /** �˵�λ�ñ�ʶ���ļ��˵���ʼλ�� */
	// public static int FILE_BEGIN = 1;
	// /** �˵�λ�ñ�ʶ���ļ��˵�����λ�� */
	// public static int FILE_END = 2;
	// /** �˵�λ�ñ�ʶ���༭�˵���ʼλ�� */
	// public static int EDIT_BEGIN = 3;
	// /** �˵�λ�ñ�ʶ���༭�˵���ʼλ�� */
	// public static int EDIT_END = 4;
	// /** �˵�λ�ñ�ʶ����ʽ�˵���ʼλ�� */
	// public static int FORMAT_BEGIN = 5;
	// /** �˵�λ�ñ�ʶ����ʽ�˵���ʼλ�� */
	// public static int FORMAT_END = 6;
	// /** �˵�λ�ñ�ʶ�����ݲ˵� */
	// public static int DATA_BEGIN = 7;
	// /** �˵�λ�ñ�ʶ�����ݲ˵� */
	// public static int DATA_END = 8;
	// /** �˵�λ�ñ�ʶ�����߲˵� */
	// public static int TOOL_BEGIN = 9;
	// /** �˵�λ�ñ�ʶ�����߲˵� */
	// public static int TOOL_END = 10;
	// /** �˵�λ�ñ�ʶ�������˵� */
	// public static int HELP_BEGIN = 11;
	// /** �˵�λ�ñ�ʶ�������˵� */
	// public static int HELP_END = 12;
	/** �˵�λ�ñ�ʶ���û��Զ��塣��ѡ��Ĳ˵�����Help�˵�֮ǰ���ɡ� */
	// public static int CUSTOM = Integer.MAX_VALUE;
	private static final long serialVersionUID = 1244898328654593304L;

	/** Ԥ�Ʋ˵�Ĭ����ʾ����Ϊfalse����������Ӳ˵��󣬲��޸���ʾ״̬Ϊtrue 
	 * @i18n uiufofurl530001=�½�*/
	private final String[] DefaultMenuName = { StringResource.getStringResource("uiufofurl530001"), MultiLang.getString("edit"),// "�༭",
			MultiLang.getString("view"),// "��ͼ",
			MultiLang.getString("format"),// "��ʽ",
			MultiLang.getString("data"),// "����",
			MultiLang.getString("tool"),// "����",
			MultiLang.getString("window"),// "����"
			MultiLang.getString("help"),// "����"
	};

	private final char[] DefaultMnemonic = { 'F', 'E', 'V', 'M', 'D', 'T', 'W',
			'H' };

	/** ��¼������ */
	private DataSetManager datasetManager;

	/**
	 * UfoMenuBar ������ע�⡣
	 * 
	 * @param report
	 *            ������ʵ��
	 */
	public DataSetManagerMenuBar(DataSetManager datasetManager) {
		super();
		this.datasetManager = datasetManager;
		createDefaultMenu();

	}

	/**
	 * ������һ��ε�ȱʡ�˵���
	 */
	private void createDefaultMenu() {
		for (int i = 0; i < DefaultMenuName.length; i++) {
			createMenu(DefaultMenuName[i], DefaultMnemonic[i]);
		}
	}

	private void createMenu(String name, char c) {
		// if (name != null && c > 0) {
		// name += " (" + c + ")";
		// }
		JMenu menu = new UFMenu(name, null);
		menu.setMnemonic(c);
		add(menu);
	}

	/**
	 * ��̬����Ӳ˵������ݡ� �˴���ӵĲ˵�ֻ���������Ԥ�ƵĲ˵�������������Զ���˵��Ĳ˵��顣
	 * 
	 * @param ext
	 *            �������������Ϊdes.getName()==NUll,����ӷָ�����
	 */
	public Component addExtension(IActionExt ext, ActionUIDes uiDes) {
		JComponent parent = DataSetManagerMenuUtil.getCompByPath(uiDes
				.getPaths(), 0, this, datasetManager);
		JComponent comp = DataSetManagerMenuUtil.createActionComp(ext, uiDes,
				datasetManager);
		MenuUtil.addCompToParent(comp, parent, uiDes);
		return comp;

	}
}