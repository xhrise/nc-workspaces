package com.ufsoft.iufo.fmtplugin.dataprocess.chart;

/**
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-7 16:31:53)
 * 版本：
 */
public class Series{
	private String project;//项目
	private String name;   //显示名称

	public Series(String project,String name){
		this.project=project;
		this.name=name;
		}
	public  String getName(){
	    return name;
	    }
	public String getProject(){
	    return project;
	    }
	public void setName(String name){
	    this.name=name;
	    }
	public void setProject(String project){
	    this.project=project;
	    }
}


