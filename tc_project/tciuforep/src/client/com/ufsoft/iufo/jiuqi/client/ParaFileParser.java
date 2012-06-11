package com.ufsoft.iufo.jiuqi.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.ufsoft.report.util.MultiLang;
/**
 * <p>Title: 参数文件内容解析器</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */

public class ParaFileParser {
    private String m_strFileName = null;

    public ParaFileParser(String strFileName){
        m_strFileName = strFileName;
    }

    /**
	 * @i18n uiuforep00112=参数文件的内容不正确
	 * @i18n uiuforep00113=指定的文件不存在
	 */
    public ParaFileInfo parse() throws Exception{
        File file = null;
        FileReader in = null;
        try{
            file = new File(m_strFileName);
            if(file.exists()){
                in = new FileReader(file);
                BufferedReader reader = new BufferedReader(in);
                String strLine = null;
                String strStartTime = null;
                String strEndTime = null;
                String strCWZBK_File = null;
                String strCWSJK_File = null;
                String strLog_File = null;
                String startTime = "StartTime";
                String endTime = "EndTime";
                String CWZBK_File = "CWZBK_File";
                String CWSJK_File = "CWSJK_File";
                String Log_File = "Log_File";
                while((strLine = reader.readLine())!=null){
                    if(strLine.indexOf("=")!=-1){
                        int nPos = strLine.indexOf("=");
                        String strKey = strLine.substring(0, nPos);
                        String strValue = strLine.substring(nPos+1, strLine.length());
                        strValue = (strValue == null)?null:strValue.trim();
                        if(strKey.equalsIgnoreCase(startTime)){
                            strStartTime = strValue;
                        }else if(strKey.equalsIgnoreCase(endTime)){
                            strEndTime = strValue;
                        }else if(strKey.equalsIgnoreCase(CWZBK_File)){
                            strCWZBK_File = strValue;
                        }else if(strKey.equalsIgnoreCase(CWSJK_File)){
                            strCWSJK_File = strValue;
                        }else if(strKey.equalsIgnoreCase(Log_File)){
                            strLog_File = strValue;
                        }
                    }
                }
                if(strCWSJK_File == null || strLog_File == null){
                    throw new Exception(MultiLang.getString("uiuforep00112"));
                }
                ParaFileInfo paraFile = new ParaFileInfo();
                paraFile.setFileName(m_strFileName);
                paraFile.setStartTime(strStartTime);
                paraFile.setEndTime(strEndTime);
                paraFile.setCWZBK_File(strCWZBK_File);
                paraFile.setCWSJK_File(strCWSJK_File);
                paraFile.setLog_File(strLog_File);
                return paraFile;
            }else{
                throw new Exception(MultiLang.getString("uiuforep00113"));
            }
        }finally{
            if(in!=null){
                try{
                    in.close();
                }catch(IOException e){
                }
            }
        }
    }
}
 