package com.ufsoft.report.toolbar.dropdown;

import java.io.Serializable;

import com.ufsoft.table.format.Format;

/**
 * ά������panel��ӦС��Ԫ������
 * @author wangyuguang
 * @since 3.1
 */
public class ImagePanelVo implements Serializable{

	/**
     * ͼƬ��Ԫ��Ӧ�Ĳ������ͣ����ݵ����ͼƬ���ж�ִ��ʲô����
     */
	private int operate;
	
	/**
     * ͼƬ��Ӧ��·��
     */
	private String imagePath;
	
	/**
     * ��ʽ
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
