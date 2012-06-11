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
 * ��ά���������������öԻ���ӿ�
 */
public interface IAnalyzerDialog {

	public static final int ADD = 0;
	public static final int EDIT = 1;
	public static final int ENABLE = 2;
	public static final int REMOVE = 3;
	public static final int CHANGE_ORDER = 4;
	
	/**
	 * �����û��趨�ķ�������
	 * @return
	 */
	public IAnalyzerSet getAnalyzerSet();
	
	/**
	 * ����Ҫ�༭�ķ����趨��������ʱΪnull
	 * @param analyzerSet
	 */
	public void setAnalyzer(IAnalyzerSet analyzerSet);
	
	/**
	 * ģ̬��ʾ���öԻ��򣬷������Ͳμ�UIDialog.ID_OK��
	 * @return
	 */
	public int showModel(Container parent);
	
	/**
	 * ��������������ɾ�ĺ͵���˳��Ĵ���
	 */
	public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer);
	public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers);
	
	/**
	 * ά��ѡ����Ϣ�����仯��Ĵ���
	 * @param oldSelDimModel	�޸�ǰ�����ݵ�����Ϣ��ά��ѡ�����ݣ�
	 * @param newSelDimModel	�޸ĺ�����ݵ�����Ϣ��ά��ѡ�����ݣ�
	 */
	public void processMemberChanged(SelDimModel oldSelDimModel,SelDimModel newSelDimModel);
}
