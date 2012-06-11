package com.ufsoft.report.plugin;

/**
 * 菜单UI信息
 * @author zzl 2005-6-28
 */
public class ToggleMenuUIDes extends ActionUIDes {
	private boolean checkBox;
	private boolean selected = false;
	private String buttonGroup;

	/**
	 * @return 返回 buttonGroup。
	 */
	public String getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @param buttonGroup
	 *            要设置的 buttonGroup。
	 */
	public void setButtonGroup(String buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	/**
	 * @return 返回 checkBox。
	 */
	public boolean isCheckBox() {
		return checkBox;
	}

	/**
	 * 设置是checkbox还是radiobutton类型的menu。
	 * 
	 * @param checkBox
	 *            要设置的 checkBox。
	 */
	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

	/**
	 * @return 返回 selected。
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            要设置的 selected。
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
