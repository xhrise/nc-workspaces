/*
 * Created on 2005-7-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;

import com.ufida.report.multidimension.model.IAnalyzerSet;
import com.ufida.report.multidimension.model.SelDimModel;

/**
 * @author ll
 *
 * 多维分析管理器的设置对话框接口
 */
public interface IAnalyzerDialog {

	public static final int ADD = 0;
	public static final int EDIT = 1;
	public static final int ENABLE = 2;
	public static final int REMOVE = 3;
	public static final int CHANGE_ORDER = 4;
	
	/**
	 * 返回用户设定的分析设置
	 * @return
	 */
	public IAnalyzerSet getAnalyzerSet();
	
	/**
	 * 设置要编辑的分析设定，当新增时为null
	 * @param analyzerSet
	 */
	public void setAnalyzer(IAnalyzerSet analyzerSet);
	
	/**
	 * 模态显示设置对话框，返回类型参见UIDialog.ID_OK等
	 * @return
	 */
	public int showModel(Container parent);
	
	/**
	 * 分析管理器中增删改和调整顺序的处理
	 */
	public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer);
	public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers);
	
	/**
	 * 维度选择信息发生变化后的处理
	 * @param oldSelDimModel	修改前的数据导航信息（维度选择内容）
	 * @param newSelDimModel	修改后的数据导航信息（维度选择内容）
	 */
	public void processMemberChanged(SelDimModel oldSelDimModel,SelDimModel newSelDimModel);
}
