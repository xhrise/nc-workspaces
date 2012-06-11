package com.ufsoft.report.toolbar.dropdown;

import java.io.Serializable;

import com.ufsoft.table.format.Format;

/**
 * 维护下拉panel对应小单元的属性
 * @author wangyuguang
 * @since 3.1
 */
public class ImagePanelVo implements Serializable{

	/**
     * 图片单元对应的操作类型，根据点击的图片，判断执行什么操作
     */
	private int operate;
	
	/**
     * 图片对应的路径
     */
	private String imagePath;
	
	/**
     * 格式
     */
	private Format fomat;

	public int getOperate() {
		return operate;
	}

	public void setOperate(int operate) {
		this.operate = operate;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Format getFomat() {
		return fomat;
	}

	public void setFomat(Format fomat) {
		this.fomat = fomat;
	}
	
}
