package com.ufsoft.iufo.fmtplugin.measure;


/**
 * 此处插入类型描述。
 * 创建日期：(2002-5-14 13:32:57)
 * @author：王海涛
 */
public class MeasureColumnModel {
public String  title; // ERROR 类和静态属性缺少注释
public int width;
public int alignment;
/**
 * MeasureColumnModel 构造子注解。
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
