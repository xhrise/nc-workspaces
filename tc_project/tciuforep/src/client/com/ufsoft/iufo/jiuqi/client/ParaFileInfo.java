package com.ufsoft.iufo.jiuqi.client;

import com.ufsoft.report.util.MultiLang;

/**
 * <p>Title: 描述外部程序（久其应用程序）调用时的参数文件信息的类</p>
 * <p>Description: 久其应用程序在调用客户端程序时传递一个文件名称，该类是对文件内容解析的结果</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */

public class ParaFileInfo {
    private String m_strFileName = null; //文件名称
    private String m_strStartTime = null;//指标资料开始时间
    private String m_strEndTime = null;//指标资料结束时间
    private String m_strCWZBK_File = null;//财务指标库文件名称
    private String m_strCWSJK_File = null;//生成结果库的文件名称
    private String m_strLog_File = null;//出错日志文件名称

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
	 * @i18n uiuforep00056=参数文件名:
	 * @i18n uiuforep00057=指标资料开始时间:
	 * @i18n uiuforep00058=指标资料结束时间:
	 * @i18n uiuforep00059=财务指标库文件名:
	 * @i18n uiuforep00060=财务结果库文件名:
	 * @i18n uiuforep00061=错误日志文件名:
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
 