package com.ufsoft.iufo.fmtplugin.dataprocess.chart;

/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-7 16:31:53)
 * �汾��
 */
public class Series{
	private String project;//��Ŀ
	private String name;   //��ʾ����

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


