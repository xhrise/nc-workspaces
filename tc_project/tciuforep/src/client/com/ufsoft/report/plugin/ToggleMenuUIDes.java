package com.ufsoft.report.plugin;

/**
 * �˵�UI��Ϣ
 * @author zzl 2005-6-28
 */
public class ToggleMenuUIDes extends ActionUIDes {
	private boolean checkBox;
	private boolean selected = false;
	private String buttonGroup;

	/**
	 * @return ���� buttonGroup��
	 */
	public String getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @param buttonGroup
	 *            Ҫ���õ� buttonGroup��
	 */
	public void setButtonGroup(String buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	/**
	 * @return ���� checkBox��
	 */
	public boolean isCheckBox() {
		return checkBox;
	}

	/**
	 * ������checkbox����radiobutton���͵�menu��
	 * 
	 * @param checkBox
	 *            Ҫ���õ� checkBox��
	 */
	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

	/**
	 * @return ���� selected��
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            Ҫ���õ� selected��
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
