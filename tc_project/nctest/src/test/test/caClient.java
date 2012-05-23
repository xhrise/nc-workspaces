package test;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.rpc.ServiceException;

//import org.codehaus.xfire.XFireFactory;
//import org.codehaus.xfire.client.XFireProxyFactory;
//import org.codehaus.xfire.service.Service;
//import org.codehaus.xfire.service.binding.ObjectServiceFactory;
//import nc.itf.tc.imp.IQueryList;
//
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

 

public class caClient {
            
       public static void main(String[] args) {
    	   String endpoint = "http://127.0.0.1/uapws/service/tcService";
//    	   Service s=new ObjectServiceFactory().create(IQueryList.class);
//    	   XFireProxyFactory xf=new XFireProxyFactory(XFireFactory.newInstance().getXFire());
//    	 //  String url="http://localhost:8989/HelloWorld/services/HelloWorldService";
//
//    	          try
//    	           {            
//    					File file=new File("C:/Ufida_IUFO/fasong/test502iufo.xml");
//    					FileInputStream in = new   FileInputStream(file);
//    				    byte   []bs   =   new   byte [in.available()]; 
//    	        	  IQueryList hs=(IQueryList) xf.create(s,endpoint);
//    	               String st=hs.importXMLRep("白痴", "bd","abc","bdc",bs);
//    	               System.out.print(st);
//    	           }
//    	           catch(Exception e)
//    	           {
//    	               e.printStackTrace();
//    	           }

			 Service afService =new Service();
			 Call call;
			try {
				File file=new File("C:/Ufida_IUFO/fasong/test502iufo.xml");
				FileInputStream in = new   FileInputStream(file);
			    byte   []bs   =   new   byte [in.available()]; 
				call = (org.apache.axis.client.Call)afService.createCall();
				 call.setTargetEndpointAddress(new java.net.URL(endpoint));
		            call.setOperationName(new  javax.xml.namespace.QName("http://imp.tc.itf.nc/IQueryList",
           "importXMLRep"));
		            call.setOperationName(new  javax.xml.namespace.QName("http://imp.tc.itf.nc/IQueryList",
			           "testText"));
//		            call.addParameter("moduleName",
//                            org.apache.axis.encoding.XMLType.XSD_STRING,
//                            javax.xml.rpc.ParameterMode.IN);
//            call.addParameter("filePk",
//                            org.apache.axis.encoding.XMLType.XSD_STRING,
//                            javax.xml.rpc.ParameterMode.IN);
//            call.addParameter("fileName",
//                            org.apache.axis.encoding.XMLType.XSD_STRING,
//                            javax.xml.rpc.ParameterMode.IN);
//            call.addParameter("note",
//                    org.apache.axis.encoding.XMLType.XSD_STRING,
//                    javax.xml.rpc.ParameterMode.IN);
//
//            call.addParameter("bs",
//                    org.apache.axis.encoding.XMLType.XSD_BASE64,
//                    javax.xml.rpc.ParameterMode.IN);
//            // 方法的返回值类型
//            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//		            String rs = (String) call.invoke(new Object[]{"白痴", "bd",
//                    		"abc","bdc",bs});
		            String rs = (String) call.invoke(new Object[]{"白痴"});
                    System.out.println(rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	   
       } 

}


