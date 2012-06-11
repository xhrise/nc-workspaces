package com.ufsoft.report.toolbar;

import java.awt.Color;
import java.awt.Component;
import java.util.Hashtable;

import javax.swing.JComboBox;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrExt;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;

/**
 * JComboBox�͵Ĺ�����
 * 
 * @author guogang
 * 
 */
public class CellAttrComboBoxExt extends SetCellAttrExt {
	/** Ҫ���õĸ�ʽ���� */
	private int propertyName;
	/** �������������������б� */
	private String[] listItems;
	private SwatchPanel comboPanel;
	private String imageFile;
	/** �����������������Ĭ��ѡ��ֵ���ɹ��췽����̬���� */
	private Object defaultSetItem;
	/** ��������������������ƣ��ɹ��췽����̬���� */
	private String extName;
	/** �����˵���ȡindex����ȡitem:true/index,�ɹ��췽����̬���� */
	private boolean isKey;
	private int comboType;
	/** �������Ӧ�Ĺ����������� */
	private JComboBox item;

	private boolean isFireAction = true;
	
	//�ɵĲ����ʱ����������
	private SelectListener selectListener = null;
	
	//ǰ��ɫ�ͱ���ɫĬ��ֵ
	public final static int DEFAULT_BACK_COLOR = new Color(0,255,102).getRGB();

	public final static int DEFAULT_FORE_COLOR = new Color(255,0,0).getRGB();
	
	public CellAttrComboBoxExt(int propertyname, String[] listitems,
			UfoReport rep) {
		super(rep);
		this.propertyName = propertyname;
		this.listItems = listitems;
		this.comboType = 0;
		setDefaultValue(propertyname);

	}

	public CellAttrComboBoxExt(int propertyname, SwatchPanel panel,
			String imageFile, UfoReport rep) {
		super(rep);
		this.propertyName = propertyname;
		this.comboPanel = panel;
		this.imageFile = imageFile;
		this.comboType = 1;
		setDefaultValue(propertyname);

	}

	/**
	 * ��������Ϣ���ø����������Ĭ��ѡ��
	 * 
	 * @param propertyName
	 */
	public void setDefaultValue(int propertyName) {
		switch (propertyName) {
		case PropertyType.FontIndex:
			extName = MultiLang.getString("uiuforep0000772");
			defaultSetItem = DefaultFormatValue.FONTNAME;
			isKey = true;
			break;
		case PropertyType.FontSize:
			extName = MultiLang.getString("uiuforep0000774");
			defaultSetItem = new Integer(DefaultSetting.DEFAULT_FONTSIZE)
					.toString();
			isKey = false;
			break;
		case PropertyType.BackColor:
			extName = MultiLang.getString("uiuforep0000761");
			defaultSetItem = DEFAULT_BACK_COLOR;
			isKey = false;
			break;
		case PropertyType.ForeColor:
			extName = MultiLang.getString("uiuforep0000775");
			defaultSetItem = DEFAULT_FORE_COLOR;
			isKey = false;
			break;
		case PropertyType.BorderLine:
			extName = MultiLang.getString("uiuforep0000738");
			defaultSetItem = 46;
			isKey = false;
		default:
			defaultSetItem = "";
			isKey = false;
			break;
		}
	}

	/**
	 * ��Ҫ�����������Ƿ������˸ø�ʽ����
	 * 
	 * @param format
	 *            ��Ҫ����������ĸ�ʽ��Ϣ
	 * @return
	 */
	private Object getFormatSetted(Format format) {
		Object selectedItem = null;
		if (format != null) {
			switch (propertyName) {
			case PropertyType.FontIndex:
				selectedItem = format.getFontname();
				break;
			case PropertyType.FontSize:
				if (format.getFontsize() > 0) {
					selectedItem = new Integer(format.getFontsize()).toString();
				}
				break;
			case PropertyType.BackColor:
				selectedItem = format.getBackgroundColor();
				break;
			case PropertyType.ForeColor:
				selectedItem = format.getForegroundColor();
				break;
			default:
				break;
			}
		}

		return selectedItem;
	}

	/**
	 * override
	 * 
	 * @see AbsActionExt.getParams();
	 * @see MenuUtil.createComboxListener() �÷����л���params[]�����ò���������ֵ
	 */
	public Object[] getParams(UfoReport container) {
		Object[] params = new Object[8 + 1];
		params = getCellParams(params);
		int nIndex = 0;
		if (isKey) {
			nIndex = item.getSelectedIndex();
		} else {
			Object selected = item.getSelectedItem();
			if (selected instanceof String) {
				nIndex = Integer.parseInt((String) selected);
			}
			if (selected instanceof Integer) {
				nIndex = ((Integer) (selected)).intValue();
			}
		}
		Hashtable<Integer, Integer> m_propertyCache = new Hashtable<Integer, Integer>();
		m_propertyCache.put(new Integer(propertyName), new Integer(nIndex));
		params[0] = m_propertyCache;
		return params;
	}

	@Override
	public UfoCommand getCommand() {
		if (!isFireAction) {
			isFireAction = true;
			return null;
		}
		return super.getCommand();
	}

	/**
	 * override
	 * 
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(final Component stateChangeComp) {

		setItem((JComboBox) stateChangeComp);
		
		if(selectListener == null){
			selectListener = new SelectListener() {
				public void selectedChanged(SelectEvent e) {
					if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
						CellPosition selectCell = getReport()
								.getCellsModel().getSelectModel()
								.getAnchorCell();
						Format cellFormat = getReport().getCellsModel()
								.getRealFormat(selectCell);
						if (stateChangeComp instanceof JComboBox) {
							
							if (stateChangeComp instanceof JPopupPanelButton) {

							} else {
								if (extName.equals(stateChangeComp
										.getName())) {
									isFireAction = false;
									if (getFormatSetted(cellFormat) == null) {
										((JComboBox) stateChangeComp)
												.setSelectedItem(defaultSetItem);
									} else {
										((JComboBox) stateChangeComp)
												.setSelectedItem(getFormatSetted(cellFormat));
									}
								}
							}
						}
					}
				}
			};
			
			getReport().getCellsModel().getSelectModel().addSelectModelListener(selectListener);
		}
	}

	/**
	 * override
	 * 
	 * @see AbsActionExt.getUIDesArr();
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setPaths(new String[] {});
		uiDes1.setGroup(MultiLang.getString("uiuforep0000870"));
		uiDes1.setToolBar(true);
		uiDes1.setListCombo(true);

		uiDes1.setName(extName);// ��������������
		uiDes1.setKey(isKey);
		uiDes1.setListKey(propertyName);
		uiDes1.setComboType(comboType);
		if (comboType == 0) {
			uiDes1.setListItem(listItems);
		}
		if (comboType == 1) {
			// modify by ����� ֧��ͨ�õĵ������
			uiDes1.setComboComponent(comboPanel);
			uiDes1.setImageFile(imageFile);
		}
		uiDes1.setDefaultSelected(defaultSetItem);
		uiDes1.setTooltip(extName);

		return new ActionUIDes[] { uiDes1 };

	}

	public JComboBox getItem() {
		return item;
	}

	public void setItem(JComboBox item) {
		this.item = item;
	}

	protected boolean isFireAction() {
		return isFireAction;
	}

	protected void setFireAction(boolean isFireAction) {
		this.isFireAction = isFireAction;
	}
}
