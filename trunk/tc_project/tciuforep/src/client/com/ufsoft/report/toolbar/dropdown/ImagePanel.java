package com.ufsoft.report.toolbar.dropdown;

import java.awt.Dimension;
import java.util.Hashtable;

import com.ufsoft.report.resource.ResConst;

public class ImagePanel extends SwatchPanel {
	/** ͼƬ�����Ӧ�Ĳ���ֵ */
	private Object[] imageProperty;
    private Hashtable<Object, Object> imageTable = null;
	public ImagePanel(Object[] images) {
		super(true);
		this.imageProperty = images;
		imageTable = new Hashtable<Object, Object>();
		super.init();
		
	}

	@Override
	public void initLayout() {
		Dimension swatchSize = new Dimension(16, 16);// ͼƬ��С
		Dimension numSwatches = new Dimension(3, 3);// ͼƬ������
		Dimension gap = new Dimension(5, 5);// ͼƬ֮��ļ��
		setSwatchSize(swatchSize);
		setNumSwatches(numSwatches);
		setGap(gap);
	}

	@Override
	public void initValues() {
		if (imageProperty == null || imageProperty.length == 0) {
			return;
		}
		Object[] swatchs = new Object[imageProperty.length];
		Object[] swatchValues = new Object[imageProperty.length];
		for (int i = 0; i < imageProperty.length; i++) {
			ImagePanelVo imageVo = (ImagePanelVo) imageProperty[i];
			Object operate = imageVo.getOperate();
			Object image = ResConst.getImageIcon(
					imageVo.getImagePath());
			swatchValues[i] = operate;
			swatchs[i] = image;// ���ͼƬ����
			if(operate != null && image != null){
				imageTable.put(operate, image);
			}			
		}
		setSwatchs(swatchs);
		setSwatchValues(swatchValues);

	}

	protected Hashtable<Object, Object> getImageTable() {
		return imageTable;
	}

	protected void setImageTable(Hashtable<Object, Object> imageTable) {
		this.imageTable = imageTable;
	}

}
