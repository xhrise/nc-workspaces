package nc.util.bi.resmng;
import java.io.File;

import nc.vo.bi.base.util.IDMaker;

import com.ufida.web.action.Action;
import com.ufida.web.util.WebGlobalValue;
/**
 * @author zyjun
 *
 * ����������ʱ�ļ�������,�ṩ������ʱ�ļ�Ŀ¼\���Ƶȷ���
 */
public class TempFileUtil {
	public static final String TEMP_PACKAGE = "temp_package";
	public static final int	  TEMP_FILE_LEN = 5;
	
	public 	static final String getDownloadUrl(Action action, String strFileName){
		return getTempFileUrlPath(action)+File.separator+strFileName;
	}
	public static final File getTempFile(Action action, String strSuffix, boolean bRandom){
		StringBuffer	sbFileName = new StringBuffer();
        sbFileName.append( getFileServPath(action) );
        sbFileName.append( File.separator );
        if( bRandom ){
        	sbFileName.append( IDMaker.makeID(TEMP_FILE_LEN) );
        }
        sbFileName.append( strSuffix );    
        String	strFileName = sbFileName.toString();
    
        //�����ļ���¼��TempFileDeleter��
        addFileToDeleter(action, strFileName);
        return new File(strFileName);
	}	
	/**
	 * �õ��ļ��ڷ������˵ı���·����
	 * �������ڣ�(2002-04-19 15:28:45)
	 * @return java.lang.String
	 */
	private static final String getFileServPath(Action action) {
	
//	     String sbufs=System.getProperty("ContextKey");
//	     sbufs=sbufs.substring(0,sbufs.length()-1);
	   
	     //����һ����ʱĿ¼��
	     String strPath = getTempDirName(action, "./webapps/nc_web");
	     //���·���Ƿ����.
	     File file = new File(strPath);
	     if (!file.exists()) {
	     	file.mkdirs();
	     }
	     return strPath;
	}
	/**
	* �õ������ļ������ӵ�ַ��·����
	* ����ʱ�䣺(2002-03-29 13:51:35)
	* @return String���ļ�·����Url����ΪNull
	*/
	private static final String getTempFileUrlPath(Action action) {
	    return getTempDirName(action, File.separator+"nc");
//		return getTempDirName(".");
	}
	
	private static final String getTempDirName(Action action, String strPrefix){
		return strPrefix+File.separator+TEMP_PACKAGE+File.separator+action.getSessionId();
	}
	private static void addFileToDeleter(Action action, String strFileName){		
		TempFileDeleter deleter = (TempFileDeleter)action.getSessionObject(WebGlobalValue.TEMPFILE_DELETER);
		if( deleter == null ){
			deleter = new TempFileDeleter();
			action.addSessionObject(WebGlobalValue.TEMPFILE_DELETER, deleter);		
		}
		deleter.addFile(strFileName);
	}
	
}

