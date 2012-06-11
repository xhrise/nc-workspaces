package nc.itf.tc.imp;

import java.io.File;

public interface IQueryList {

	//接收的方法目录名 主键 名字 说明 文件
	public String importXMLRep(String moduleName,String filePk,String fileName,String note,String xmlContent);
	
	public String testText(String string);
}
