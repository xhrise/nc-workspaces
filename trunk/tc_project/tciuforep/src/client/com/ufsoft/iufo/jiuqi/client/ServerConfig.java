package com.ufsoft.iufo.jiuqi.client;

public class ServerConfig {
    private final static String SERVER_CONTEXT = "/service/~iufo/";
    private final static String SERVLET_NAME = "nc.ui.iufo.jiuqi.ClientRequestDispatch";

    private String serverContext = SERVER_CONTEXT;
    private String servletName = SERVLET_NAME;
    
    public ServerConfig(){
    	
    }
    
    public ServerConfig(String context, String servlet){
    	serverContext = context;
    	servletName = servlet;
    }
    
    
    
    public String toString(){
    	return serverContext + servletName;
    }



	public String getServerContext() {
		return serverContext;
	}



	public void setServerContext(String serverContext) {
		this.serverContext = serverContext;
	}



	public String getServletName() {
		return servletName;
	}



	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
}
