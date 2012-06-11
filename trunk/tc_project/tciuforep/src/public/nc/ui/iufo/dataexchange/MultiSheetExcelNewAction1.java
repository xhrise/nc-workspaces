/*
 * Created by CaiJie  on  2006-1-18
 *   
 */
package nc.ui.iufo.dataexchange;

import java.io.File;
import java.util.Vector;

import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.dataexchange.base.AbstractExportNewAction;
import nc.ui.iufo.dataexchange.base.AbstractExportDlg;
import nc.ui.iufo.dataexchange.base.ExcelAction;
import nc.ui.iufo.dataexchange.base.FilePackage;
import nc.ui.iufo.dataexchange.base.IDataExchange;
import nc.ui.iufo.input.CSomeParam;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.action.Action;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * ԭUI��MultiSheetExcelUI1_old
 * @version  5.0
 * @author CaiJie 
 */
public class MultiSheetExcelNewAction1 extends AbstractExportNewAction{
    public final static java.lang.String FILETYPE = "filetype";
	public final static String ZIP = "ZIP";
	public final static String XLS = "XLS";
	public final static String FILENAME = "filename";
	
	/**
	 * �������ص��ļ���
	 * ����ʱ�䣺(2002-03-29 17:36:28)
	 * @return java.lang.String��������·�����ļ�������
	 * @param request javax.servlet.http.HttpServletRequest
	 */
	public java.lang.String createFile(IUFOAction action) {
	    try { //�õ��ύ�˵���˵����
	        String sType = (String) action.getRequestParameter(WebGlobalValue.PARAM_UI_TYPE);
	        String sPath = getFileServPath(action);
	        String sFiletype = action.getRequestParameter(FILETYPE);
	        String strZipFile=action.getRequestParameter(ExcelAction.ZIPFILE);
	        boolean bZip=strZipFile!=null && strZipFile.equalsIgnoreCase("true");
	        String sFileName = action.getRequestParameter(FILENAME);
	        String strAccSchemePK=action.getRequestParameter(CSomeParam.PARAM_ACCSCHEME);

	    	if (sType == null || sType.equalsIgnoreCase("null")||sType.trim().length()==0) {
	        	sType = IDataExchange.EXCEL;
	    	}

		    Vector obj = (Vector)action.getRequestObject(sType);
		    
		    if (obj == null) {
		        return null;
		    }


	        if (sFiletype==null)
	            sFiletype=ExcelAction.XLS;
	        
			if (sFiletype.equalsIgnoreCase(ExcelAction.XLS))
				sFileName="excel";
			else
				sFileName="iufo";	        

			String sFullFileName=null;


			if (sFiletype.equalsIgnoreCase(ExcelAction.XLS)){
				sFullFileName=sPath+File.separator+sFileName+".xls";
				Vector<String> sheetnameRule=(Vector<String>)action.getRequestObject(MultiSheetExcelNewAction.SHEETNAMERULE);
				MulitSheetExportUtil.doTranslateMultiSheet((Vector)obj,sheetnameRule,getCurUserInfo().getID(),sFullFileName,getCurLoginDate(),getCurOrgPK(),this,strAccSchemePK);
			}
			else{
				sFullFileName=sPath+File.separator+sFileName;
				bZip=true;

				TableToHtmlUtil.genTableHtml(this,sFullFileName,obj);
			}

		    if (bZip && sFiletype.equalsIgnoreCase(ExcelAction.XLS)) { //��Ҫѹ��

			    File[] files=null;

		        File file = new File(sFullFileName);
	        	files =new File[]{file};

		        FilePackage filePackage = new FilePackage();
		        filePackage.zipFile(files, sPath+File.separator+sFileName + ".zip");
		        //�ļ�ɾ��

		        for (int i=0;i<files.length;i++)
			        files[i].delete();

		        return sFileName + ".zip";
		    }
		    else if (bZip)
		    	return sFileName+".zip";
		    else
		    	return sFileName+".xls";
	    }
	    catch (CommonException ce) {
	        throw ce;
	    }
	    catch (Exception e) {
	    	AppDebug.debug(e);
	        throw new CommonException("miufo1002742");  //"����Excel�ļ�ʧ��"
	    }
	}
	
    protected String getExportUIClass(){
    	return AbstractExportDlg.class.getName();
    }	
		/**
	 * ���ص�������ʾ��Ϣ��
	 * �������ڣ�(2002-06-12 13:41:48)
	 * @return java.lang.String
	 */
	protected String getHint() {
		return StringResource.getStringResource("miufo1002419")+"...";
	}
	/**
	 * ������������⡣
	 * �������ڣ�(2002-04-22 09:59:02)
	 * @return java.lang.String
	 * @param request javax.servlet.http.HttpServletRequest
	 */
	public String getTitle(Action action) {
		return StringResource.getStringResource("miufopublic468");  //"�ļ�����"
	}
}
