package com.ufsoft.report.fmtplugin.formula;

import java.awt.Color;

import nc.vo.pub.beans.editor.DefaultColorSetting;
import nc.vo.pub.beans.editor.IWordType;

/**
 * ����Ƕ��༭����ɫ������ 
 * @author zhaopq
 * @created at 2009-2-18,����10:28:12
 *
 */
public class PfColorSet extends DefaultColorSetting {
	
	public PfColorSet() {
		super();
		init();
	}

	
	public void init() {
		// �����ַ���Ϊ���
		colors[IWordType.STRING] = Color.magenta;
		colors[IWordType.RESERVED1] = new Color(0, 200, 200);
	}
}