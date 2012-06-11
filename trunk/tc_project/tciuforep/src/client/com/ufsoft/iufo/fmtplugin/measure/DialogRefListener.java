package com.ufsoft.iufo.fmtplugin.measure;

/**
@update
参照方式改变，可以实现不管比对话框多次参照
@end
 * 定义了参照时对话框不关闭的相关接口，包括注册参照组件和响应参照事件
 * 创建日期：(2003-10-30 19:34:21)
 * @author：王海涛
 */
import java.awt.Component;
import java.util.EventListener;
public interface DialogRefListener extends EventListener {
/**
	关闭参照对话框之前，清除注册的组件
	注意：鉴于可能出现同时弹出多个参照窗口的情况，每个窗口关闭时都会调用此方法，需要判断关闭的窗口是否为当前注册的窗口
*/
public void beforeDialogClosed(Component refDialog);
/**
	得到参照对话框组件
*/	
public Component getRefDialog();
/**
	得到参照操作组件
*/		
public Component getRefOper();
/**
	响应参照组件参照
*/	
public void onRef(java.util.EventObject ev);
/**
注册参照界面用来激发设置值操作的组件，比如按钮
Class refDialog 是参照界面类
subComp 是参照界面上激发设置值操作的组件
*/	
public void setRefDialogAndRefOper(Component refDialog,Component subComp) ;
}
