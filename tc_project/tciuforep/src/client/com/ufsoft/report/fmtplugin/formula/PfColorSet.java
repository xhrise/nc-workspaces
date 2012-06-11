package com.ufsoft.report.fmtplugin.formula;

import java.awt.Color;

import nc.vo.pub.beans.editor.DefaultColorSetting;
import nc.vo.pub.beans.editor.IWordType;

/**
 * 代码嵌入编辑器颜色设置类 
 * @author zhaopq
 * @created at 2009-2-18,上午10:28:12
 *
 */
public class PfColorSet extends DefaultColorSetting {
	
	public PfColorSet() {
		super();
		init();
	}

	
	public void init() {
		// 设置字符串为深红
		colors[IWordType.STRING] = Color.magenta;
		colors[IWordType.RESERVED1] = new Color(0, 200, 200);
	}
}