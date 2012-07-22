package nc.itf.yto.util;

public interface IFilePost {
	public String postFile(String url, String xmlStr);
	public String Encoding(String xmlStr);
	public String GetURI() throws Exception;
}
