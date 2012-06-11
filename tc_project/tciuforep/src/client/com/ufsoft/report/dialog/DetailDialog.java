package com.ufsoft.report.dialog;

/**
 * <p>Title: </p>
 * <p>Description: 报表工具详细对话框</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 北京用友软件股份有限公司</p>
 * @version 1.0
 * @author CaiJie
 */
import java.awt.Component;

/**
 * <br>
 * 细节对话框类</br><br>
 * 继承BasicDialog的OK和cancel按钮，新加了一个显示细节/隐藏细节按钮</br>
 *  
 */
@Deprecated
public class DetailDialog extends com.ufida.zior.dialog.DetailDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造函数
	 */
	public DetailDialog(Component parent) {
		super(parent);

	}

}