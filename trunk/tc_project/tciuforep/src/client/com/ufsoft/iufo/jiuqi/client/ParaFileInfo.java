package com.ufsoft.iufo.jiuqi.client;

import com.ufsoft.report.util.MultiLang;

/**
 * <p>Title: �����ⲿ���򣨾���Ӧ�ó��򣩵���ʱ�Ĳ����ļ���Ϣ����</p>
 * <p>Description: ����Ӧ�ó����ڵ��ÿͻ��˳���ʱ����һ���ļ����ƣ������Ƕ��ļ����ݽ����Ľ��</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */

public class ParaFileInfo {
    private String m_strFileName = null; //�ļ�����
    private String m_strStartTime = null;//ָ�����Ͽ�ʼʱ��
    private String m_strEndTime = null;//ָ�����Ͻ���ʱ��
    private String m_strCWZBK_File = null;//����ָ����ļ�����
    private String m_strCWSJK_File = null;//���ɽ������ļ�����
    private String m_strLog_File = null;//������־�ļ�����

    public ParaFileInfo(){
    }

    public String getFileName(){
        return m_strFileName;
    }

    public void setFileName(String strFileName){
        m_strFileName = strFileName;
    }

    public String getStartTime(){
        return m_strStartTime;
    }

    public void setStartTime(String strStartTime){
        m_strStartTime = strStartTime;
    }

    public String getEndTime(){
        return m_strEndTime;
    }

    public void setEndTime(String strEndTime){
        m_strEndTime = strEndTime;
    }

    public String getCWZBK_File(){
        return m_strCWZBK_File;
    }

    public void setCWZBK_File(String strCWZBK_File){
        m_strCWZBK_File = strCWZBK_File;
    }

    public String getCWSJK_File(){
        return m_strCWSJK_File;
    }

    public void setCWSJK_File(String strCWSJK_File){
        m_strCWSJK_File = strCWSJK_File;
    }

    public String getLog_File(){
        return m_strLog_File;
    }

    public void setLog_File(String strLog_File){
        m_strLog_File = strLog_File;
    }

    /**
	 * @i18n uiuforep00056=�����ļ���:
	 * @i18n uiuforep00057=ָ�����Ͽ�ʼʱ��:
	 * @i18n uiuforep00058=ָ�����Ͻ���ʱ��:
	 * @i18n uiuforep00059=����ָ����ļ���:
	 * @i18n uiuforep00060=���������ļ���:
	 * @i18n uiuforep00061=������־�ļ���:
	 */
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(MultiLang.getString("uiuforep00056")+m_strFileName);
        sb.append("\r\n");
        sb.append(MultiLang.getString("uiuforep00057")+m_strStartTime);
        sb.append("\r\n");
        sb.append(MultiLang.getString("uiuforep00058")+m_strEndTime);
        sb.append("\r\n");
        sb.append(MultiLang.getString("uiuforep00059")+m_strCWZBK_File);
        sb.append("\r\n");
        sb.append(MultiLang.getString("uiuforep00060")+m_strCWSJK_File);
        sb.append("\r\n");
        sb.append(MultiLang.getString("uiuforep00061")+m_strLog_File);
        sb.append("\r\n");
        return sb.toString();
    }
}
 