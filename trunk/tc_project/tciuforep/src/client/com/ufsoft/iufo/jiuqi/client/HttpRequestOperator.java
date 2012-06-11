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
 * <p>Title: 久其接口客户端与服务器HTTP通讯处理类</p>
 * <p>Description: 处理与服务器进行HTTP通讯的类</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */

public class HttpRequestOperator {
    //Servlet地址
    private String m_strServerlet = null;

    public HttpRequestOperator(String strServlet){
        m_strServerlet = strServlet;
    }

    /**
     * 发送请求给服务器
     * @param request BaseRequest
     * @throws Exception
     * @return BaseResponse
     * @i18n uiuforep00106=开始连接服务器: 
     * @i18n uiuforep00107=无法连接到服务器:
     * @i18n uiuforep00108=向服务器发送请求: 
     * @i18n uiuforep00109=与服务器传输数据时发生错误
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
 