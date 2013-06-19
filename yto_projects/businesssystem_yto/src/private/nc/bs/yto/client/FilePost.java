package nc.bs.yto.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Properties;

import nc.itf.yto.util.IFilePost;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import sun.misc.BASE64Encoder;

public class FilePost implements Serializable , IFilePost {
	
	private final String KEY = "SHANGHAIEXPRESSYTO";

	public String postFile(String url, String xmlStr) {
		HttpClient client = new HttpClient();

		// try {
		// xmlStr = new String(xmlStr.getBytes("ISO-8859-1") , "UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// e1.printStackTrace();
		// }

		NameValuePair[] paraNames = new NameValuePair[2];
		paraNames[0] = new NameValuePair("logistics_interface", xmlStr);
		paraNames[1] = new NameValuePair("data_digest", this.Encoding(xmlStr + this.KEY));

		PostMethod httppost = new PostMethod(url);
		httppost.setRequestBody(paraNames);
		httppost.setRequestHeader("Content-Type",
				PostMethod.FORM_URL_ENCODED_CONTENT_TYPE + "; charset= UTF-8");

		try {
			client.executeMethod(httppost);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					httppost.getResponseBodyAsStream()));

			char[] buffer = new char[1024 * 10];
			int len = 0;
			StringBuffer retMsg = new StringBuffer();
			while ((len = in.read(buffer)) != -1) {
				retMsg.append(buffer, 0, len);
			}

			httppost.releaseConnection();
			
			System.out.println("\n\n" + retMsg.toString() + "\n\n");

			return retMsg.toString();

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String Encoding(String xmlStr) {

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] data = xmlStr.getBytes("UTF-8"); // UnicodeLittleUnmarked
			byte[] digestBytes = digest.digest(data);
			return new String(new BASE64Encoder().encode(digestBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String GetURI() throws Exception {
		String uri = this.getPropByProperties("uri", "ExtProperties/URI.properties");
		
//		System.out.println(uri);
		
		return uri;	
	}
	
	private String getPropByProperties(String key , String filePath) throws Exception {
		try {
		InputStream is = new FileInputStream(filePath);
		Properties prop = new Properties();
		prop.load(is);
		is.close();

		if(prop.containsKey(key))
			return prop.getProperty(key);
		} catch(Exception e){}
		
		return null;
	}

}
