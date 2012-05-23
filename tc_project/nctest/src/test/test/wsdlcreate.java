package test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class wsdlcreate {
    public static void getWsdl(String url,String fileName){
        try {
            URL u = new java.net.URL(url);
            InputStream is = u.openConnection().getInputStream();
            File f = new File(fileName);
            
            BufferedWriter bout = new BufferedWriter(new FileWriter(f,false));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String l;
            while((l=reader.readLine())!=null){
                bout.write(l+"\r\n");
                bout.flush();
            }
            
            bout.close();
            reader.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        getWsdl("http://localhost:89/uapws/service/groupService?wsdl","d:/groupService.wsdl");
    }

}
