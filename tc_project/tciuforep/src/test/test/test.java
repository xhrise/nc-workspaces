//package test;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import org.xml.sax.SAXException;
//
//import nc.imp.tc.imp.QueryList;
//import nc.itf.tc.imp.IQueryList;
//
//public class test {
//
//	/**
//	 * @param args
//	 * @throws IOException 
//	 */
//	public static void main(String[] args) throws IOException {
//		QueryList list=new QueryList();
//		File file=new File("C:/Ufida_IUFO/fasong/test502iufo.xml");
////		String name=file.getName();
////		name=name.substring(0,name.length()-4);
////		String paths="E:/nc56home/webapps/nc_web/iufoserver/iufosrvTemp/"+name;
////		File path=new File(paths);
////		if (!path.exists()) {
////			path.mkdirs(); // ��������Ŀ¼
////		}
////		File newfile=new File(paths+"/"+name+".xml");
////		file.renameTo(newfile);
//          // ����webservice������� 
//       String fileName   =   "test502iufo.xml" ; 
//
//       FileInputStream in;
//	try {
//		in = new   FileInputStream(file);
//	    byte   []bs   =   new   byte [in.available()]; 
//	       
//	       in.read(bs); 
//	       
//	       in.close(); 
//	       System.out.println( " ���ڴ����ļ��� "    +   fileName   +   " �� " );
//	       
//
//		   String st=list.importXMLRep("123", "123", "57575", "123", bs);
//		   System.out.println( " �ļ�������� " ); 
//		   System.out.println(st);
//	} catch (FileNotFoundException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} 
//       
//   
//       
//
////try {
////	list.getAllxmlList();
////} catch (SAXException e) {
////	// TODO Auto-generated catch block
////	e.printStackTrace();
////} catch (IOException e) {
////	// TODO Auto-generated catch block
////	e.printStackTrace();
////}
////	}
//
//}}
