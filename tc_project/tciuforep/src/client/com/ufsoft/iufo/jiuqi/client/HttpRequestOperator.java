package com.ufsoft.iufo.jiuqi.client;
//import com.ufida.iufo.pub.tools.AppDebug;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.ufsoft.iufo.jiuqi.pub.BaseRequest;
import com.ufsoft.iufo.jiuqi.pub.BaseResponse;
import com.ufsoft.report.util.MultiLang;
/**
 * <p>Title: ����ӿڿͻ����������HTTPͨѶ������</p>
 * <p>Description: ���������������HTTPͨѶ����</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */

public class HttpRequestOperator {
    //Servlet��ַ
    private String m_strServerlet = null;

    public HttpRequestOperator(String strServlet){
        m_strServerlet = strServlet;
    }

    /**
     * ���������������
     * @param request BaseRequest
     * @throws Exception
     * @return BaseResponse
     * @i18n uiuforep00106=��ʼ���ӷ�����: 
     * @i18n uiuforep00107=�޷����ӵ�������:
     * @i18n uiuforep00108=���������������: 
     * @i18n uiuforep00109=���������������ʱ��������
     */
    public BaseResponse sendRequest(BaseRequest request) throws Exception{
        if(request == null){
            return null;
        }
        URL url = null;
        URLConnection con = null;
        InputStream in = null;
        OutputStream out = null;
        try{
            url = new URL(m_strServerlet);
            Log.getInstance().log(MultiLang.getString("uiuforep00106") + url.toString());
            try{
                con = url.openConnection();
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                out = con.getOutputStream();
            }catch(IOException ioex){
            	Log.getInstance().log(ioex.getMessage());
                throw new Exception(MultiLang.getString("uiuforep00107")+url.toString());
            }
            Log.getInstance().log(MultiLang.getString("uiuforep00108") + request.getRequestType());
            try{
                ObjectOutputStream out1 = new ObjectOutputStream(out);
                out1.writeObject(request);
                out1.flush();
                in = con.getInputStream();
            }catch(IOException ioex){
            	Log.getInstance().log(ioex.getMessage());
                throw new Exception(MultiLang.getString("uiuforep00109"));
            }
            ObjectInputStream in1 = new ObjectInputStream(in);
            BaseResponse response = (BaseResponse)in1.readObject();
            return response;
        }catch(Exception ex){
            throw ex;
        } finally{
            if(out != null){
                try{
                    out.close();
                } catch(IOException e){
                }
            }
            if(in != null){
                try{
                    in.close();
                } catch(IOException e){
                }
            }
        }
    }
}
 