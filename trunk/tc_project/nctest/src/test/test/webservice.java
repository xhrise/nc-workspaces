package test;

import java.util.ArrayList;


import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


public class webservice {

	public static void main(String[] args) throws Exception {
		// 客户端 Stubs方式 调用
		// HelloWorldServiceLocator hw = new HelloWorldServiceLocator();
		// hw.setEndpointAddress("HelloWorld",
		// "http://127.0.0.1/axis/services/HelloWorld");
		// String sss = hw.getHelloWorld().sayHello();
		// NcwebServiceLocator ncweb = new NcwebServiceLocator();
		// ncweb.setEndpointAddress("ncweb",
		// "http://127.0.0.1/axis/services/ncweb");
		// String[] mss = ncweb.getncweb().getMessageStr("http://localhost",
		// "0001QW10000000000K2C","1001");
		// System.out.println(ncweb.getncweb().sayHello());
		// Dynamic Invocation Interface ( DII)
		// String endpoint = "http://127.0.0.1/axis/services/HelloWorld.jws";
		// 这种方式不稳定，而且不支持包，不建议使用
		// Dynamic Proxy 和 DII不同之处在于endpoint 不一样，其他相同
		// String endpoint = "http://127.0.0.1/axis/services/HelloWorld";
		String endpoint = "http://127.0.0.1/axis/services/ncweb";
		System.out.println("11111111111");
		Service service = new Service();
		System.out.println("2222222222222");
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName("getMessageStr");
		// String res = (String) call.invoke( new Object[] {} ); //无参数
		ArrayList ee = (ArrayList) call.invoke(new Object[] {
				"http://localhost", "0001QW10000000000K2C", "1001" });
	}
}
