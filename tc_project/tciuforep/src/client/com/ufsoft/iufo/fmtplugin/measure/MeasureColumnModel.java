package com.ufsoft.iufo.fmtplugin.measure;


/**
 * �˴���������������
 * �������ڣ�(2002-5-14 13:32:57)
 * @author��������
 */
public class MeasureColumnModel {
public String  title; // ERROR ��;�̬����ȱ��ע��
public int width;
public int alignment;
/**
 * MeasureColumnModel ������ע�⡣
 * @param title
 * @param width
 * @param aligment
 */
public MeasureColumnModel(String title,int width,int aligment) {
	super();
	this.title = title;
	this.width = width;
	this.alignment = aligment;
}
}
