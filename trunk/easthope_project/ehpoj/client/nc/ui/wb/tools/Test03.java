package nc.ui.wb.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nc.ui.sm.identityverify.ILoginResultMessage;
import nc.vo.framework.rsa.Encode;

public class Test03 implements ILoginResultMessage{
	/**
	 * 更具后台 返回的INT 结果 来做相应的处理 结果直要大于200
	 */
	public String getResultMessage(int intResult) {
		// TODO Auto-generated method stub
		
		System.out.println("%%%%%%%%%%%%%% nc.ui.eh.test.Test03");
		String result=null;
		if(intResult==220){
			result="***********";
		}
		return result;
	}
	
	public static void main(String[] args) {
//		try {
//			Method method = Class.forName("nc.vo.eh.trade.z0206510.LadingbillVO").getDeclaredMethod("getTableName", null);
//			try {
//				String pk = (String)method.invoke(Class.forName("nc.vo.eh.trade.z0206510.LadingbillVO"), null);
//				System.out.println(pk);
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Encode ed = new Encode();
		System.out.println(ed.decode("fjceplggfhckfkio"));
	}

}
