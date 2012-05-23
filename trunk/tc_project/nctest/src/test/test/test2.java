package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//readBlob("Z4-������估���������");
		writeBlob("128fff","Z1-�ʲ���ծ��");
		System.out.println("����");
	}
	private static Connection getfdhConn()
	{
		   try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		    String sourceURL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		    String user = "iufo";
		    String password = "iufo";
		    Connection cbd = DriverManager.getConnection(
		    	      sourceURL, user, password);
		    return cbd;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		     return null;
	}
	 public static  void readBlob(String reportname) {   
	        try {   
	              
	            Connection conn =  getfdhConn();
	            //Statement st = conn.createStatement();   
	            PreparedStatement ps = conn.prepareStatement("select repinfo,keymeasures from iufo_report where name=?");   
	           ps.setString(1, reportname);
	            ResultSet rs = ps.executeQuery();   
	     rs.next();
	            oracle.sql.BLOB imgBlob = (oracle.sql.BLOB) rs.getBlob("repinfo");
	            oracle.sql.BLOB imgBlob2 = (oracle.sql.BLOB) rs.getBlob("keymeasures");
	         
	            // ������������д��BLOB   
	            try{   
	            FileOutputStream outStream = new FileOutputStream("D:/"+reportname+"_repinfo.png");   
	            InputStream inStream = imgBlob.getBinaryStream();   
	            FileOutputStream outStream2 = new FileOutputStream("D:/"+reportname+"_keymeasures.png");   
	            InputStream inStream2 = imgBlob2.getBinaryStream();   
	            byte[] buf = new byte[10240];   
	            byte[] buf2 = new byte[10240]; 
	            int len;   
	            int len2;  
	                while ((len = inStream.read(buf)) > 0) {   
	                    outStream.write(buf, 0, len);   
	                }   
	                while ((len2 = inStream2.read(buf2)) > 0) {   
	                    outStream2.write(buf2, 0, len2);   
	                }   
	                
	                inStream.close();   
	                outStream.close();   
	                inStream2.close();   
	                outStream2.close();   
	            }catch(Exception e) {   
	                e.printStackTrace();   
	            }   
	               
	  
	        } catch (SQLException e) {   
	            e.printStackTrace();   
	        }   
	    }
	    public static void writeBlob(String newreportname,String oldreportname) {   
	        try {   
	           // Ҫ���µı�"����13"  �Ѿ�����PNG�ı�"Z4-������估���������"
	            Connection conn = getfdhConn();
	           // Statement st = conn.createStatement();   
	            String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	            // ����һ���ն���empty_blob()   
	            // ���������н��и��£�ע�⡰for update�����   
	            PreparedStatement ps = conn.prepareStatement("update iufo_report set repinfo=?,keymeasures=?, modify_time='"+date+"' where name=?");   
	            // ͨ��ORALCE.SQL.BLOB/CLOB.EMPTY_LOB()�����BLOB/CLOB����   
	            ps.setBlob(1, oracle.sql.BLOB.empty_lob());  
	            ps.setBlob(2, oracle.sql.BLOB.empty_lob());   
	            ps.setString(3, newreportname);
	            ps.execute();   
	            ps.close();   
	  
	            // �ٴζԶ���BLOB/CLOB���   
	            ps = conn   
	                    .prepareStatement("select repinfo,keymeasures from iufo_report where name=? for update");   
	            ps.setString(1, newreportname); 
	  
	            ResultSet rs = ps.executeQuery();   
	            rs.next();   
	  
	            oracle.sql.BLOB imgBlob = (oracle.sql.BLOB) rs.getBlob("repinfo");
	            oracle.sql.BLOB imgBlob2 = (oracle.sql.BLOB) rs.getBlob("keymeasures");
	  
	            // ������������д��BLOB   
	            try{   
	            FileInputStream inStream = new FileInputStream("D:/"+oldreportname+"_repinfo.png");   
	            OutputStream outStream = imgBlob.getBinaryOutputStream();   
	            FileInputStream inStream2 = new FileInputStream("D:/"+oldreportname+"_keymeasures.png");   
	            OutputStream outStream2 = imgBlob2.getBinaryOutputStream();   
	            byte[] buf = new byte[10240];   
	            int len;   
	            byte[] buf2 = new byte[10240];   
	            int len2;   
	                while ((len = inStream.read(buf)) > 0) {   
	                    outStream.write(buf, 0, len);   
	                }   
	                while ((len2 = inStream2.read(buf2)) > 0) {   
	                    outStream2.write(buf2, 0, len2);   
	                }   
	                inStream.close();   
	                outStream.close();   
	                inStream2.close();   
	                outStream2.close(); 
	            }catch(Exception e) {   
	                e.printStackTrace();   
	            }   
	  
	            // �ٽ�Blob�ֶθ��µ����ݿ�   
	            ps = conn   
	                    .prepareStatement("update iufo_report set repinfo=?,keymeasures=?,modify_time='"+date+"',pk_key_comb='00000000000000000007' where name='"+newreportname+"'");   
	            ps.setBlob(1, imgBlob);   
	            ps.setBlob(2, imgBlob2);   
	            ps.executeUpdate();   
	  
	        } catch (SQLException e) {   
	            e.printStackTrace();   
	        }   
	    }   
	}  




