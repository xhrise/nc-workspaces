package com.ufsoft.iufo.inputplugin.ref;

import com.ufsoft.table.re.IRefComp;
/**
 * 参照对话框接口
 * @created by wangyga at 2008-12-26,下午12:37:04
 *
 */

public interface IComRefDlg {

	public abstract void setRefComp(IRefComp refComp);
	
	public abstract Object getSelectedValue();
	
	public abstract void showModel();
	
}
