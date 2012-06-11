/**
 * RepSaveImportAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-02-24
 */
package nc.ui.iuforeport.rep;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import nc.ui.iufo.analysisrep.AnaExportAction;
import nc.ui.iufo.analysisrep.AnaRepMngUI;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.dataexchange.base.DirectoryMng;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.server.module.ReportFormatModule;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iuforeport.rep.ReportVO;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.xml.IUFOXMLImpExpUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.sysplugin.excel.ExcelImpUtil;
import com.ufsoft.table.CellsModel;

/**
 * 导入文件Action
 * zyjun
 * 2006-02-24
 */
public class RepSaveImportAction extends ReportEditAction implements IUfoContextKey{
    
    /**
     * <MethodDescription>
     * zyjun
     * 2006-02-24
     */
    public ActionForward execute(ActionForm actionForm){        
    	 ActionForward errorFwd = doCheckFileParams(this);
         if(errorFwd != null){
             return errorFwd;
         }
        return super.execute(actionForm);
    }

    public String getTreeSelectedID(){
		String selItem = getRequestParameter(AnaRepMngUI.REP_DIR);
		return selItem;
    }
    public static ActionForward doCheckFileParams(Action action){
        if(action == null){
            return null;
        }
        
        String   strFileName = action.getRequestParameter(AnaExportAction.FLIE_NAME);
        String   strFileType = action.getRequestParameter(AnaExportAction.FILETYPE);
        if( strFileName == null || strFileType == null ){
            return new ErrorForward(StringResource.getStringResource(""));//参数中没有设置文件名和类型
        }
        return null;
        
    }
    
    /**
	 * 重写父类方法
     * @param form
     * @param selResTreeObj
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#initFormValue(ActionForm, IResTreeObject)
     */
    protected void initFormValue(ActionForm actionForm, IResTreeObject selResTreeObj) {
    	super.initFormValue(actionForm,selResTreeObj);
    	RepSaveImportForm 	form = (RepSaveImportForm) actionForm;
    	 String				strFileName = getRequestParameter(AnaExportAction.FLIE_NAME);
         String				strFileType = getRequestParameter(AnaExportAction.FILETYPE);
        form.setFilename(strFileName);
        form.setFiletype(strFileType);
        form.setSubmitActionName(RepSaveImportAction.class.getName());
        form.setSubmitActionMethod("saveFile");
    }    
    
	protected String getExecuteUI() {
		return RepSaveImportDlg.class.getName();
	}
    
    /**
     * 保存导入报表的报表格式
     * @param strReportPK
     * @param cModel
     * @param strFileName
     */
	public static void doSaveRepFormat(boolean bXML,String strReportPK,CellsModel cModel,String strFileName,String strOldRepPK,DataSourceVO dataSource){
        //得到保存后的报表VO                
        ReportVO[] repVOs = IUFOUICacheManager.getSingleton().getReportCache().getByPks(new String[]{strReportPK});
        if(repVOs == null || repVOs.length < 0 || repVOs[0] == null){
            return;
        }
        
        ReportVO repVO = repVOs[0];
        //保存报表格式
        if( cModel != null ){
        	if (bXML){
        		ReportFormatModule.importFromXMLFile(cModel, repVO,strOldRepPK,dataSource);
        	}
        	else{
	            UfoContextVO context = new UfoContextVO();
	            context.setAttribute(ReportContextKey.REPORT_PK,repVO.getReportPK());
	            context.setAttribute(CUR_USER_ID, repVO.getUserPK());
	            context.setAttribute(DATA_RIGHT, UfoContextVO.RIGHT_FORMAT_WRITE);
	            CellsModelOperator.saveReportFormat(context, cModel);
        	}
        }
        //删除临时文件
        new File(strFileName).delete();   
    }
    /**
     * <MethodDescription>
     * zyjun
     * 2006-02-24
     */
    public ActionForward saveFile(ActionForm actionForm){     	
    	
    	try{
    		//分类型得到报表格式模型
    		RepSaveImportForm	form = (RepSaveImportForm)actionForm;	
	    	String[] fileNames = parseFileName(this, form.getFilename());
	    	String[] strRepPKs=new String[1];
	    	CellsModel cModel = doImportCellsModel(form.isXMLType(), fileNames[0],strRepPKs);
    		
	    	//保存报表基本信息,调用ReportEditAction的功能
	    	ActionForward	fwd = super.actSave(actionForm);
	    	
	    	if( fwd instanceof CloseForward ){		
                doSaveRepFormat(form.isXMLType(),ResMngToolKit.getVOIDByTreeObjectID(form.getID()),cModel,fileNames[0],strRepPKs[0],(DataSourceVO)getSessionObject(IIUFOConstants.DefaultDSInSession));
	    	}
	    	return fwd;
    	}catch(Exception e){
//    		if( (e instanceof WebException)==false && (e instanceof CommonException)==false){
//    			AppDebug.debug(e);
//    		}
    		AppDebug.debug(e);
//    		if(e.getMessage()!=null){
//    			return new MessageForward(e.getMessage());
//    		}else{
    			return new MessageForward(StringResource.getStringResource("miuforep015"));			//"报表格式有误，无法导入和保存"
//    		}
    	}
    }

    /**
     * @param form
     * @param fileNames
     * @return
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws SAXException
     * @throws IOException
     */
    public static CellsModel doImportCellsModel(boolean bXMLType, String strFileName,String[] strRepPKs) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
        CellsModel	cModel = null;		    
        if(bXMLType){	        		
        	DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        			.newDocumentBuilder();
        	Document doc = builder.parse(strFileName);	
        	String strRepPK=doc.getDocumentElement().getAttribute("repPK");
        	strRepPKs[0]=strRepPK;
        	cModel = IUFOXMLImpExpUtil.getCellsModelByXML(doc);
        //	MeasureModel.getInstance(cModel).clearMeasurePackPKs();
        }else{
        	cModel = ExcelImpUtil.importCellsModel(strFileName);
        }
        if( cModel == null ){
        	throw new WebException("miufo1002428"); //"导入失败"
        }
        return cModel;
    }
       
    /**
	 * 解析hidden中的文件名，主要考虑多文件导入的时候需要区分每一个文件
	 * @param srcFileName String
	 * @return String[]
	 */
	public static String[] parseFileName(IUFOAction action, String srcFileName) {
		if (srcFileName == null || srcFileName.length() == 0) {
			return null;
		}
		StringTokenizer token = new StringTokenizer(srcFileName, ";");
		List<String> listFiles = new ArrayList<String>();
		String strfile;
		while (token.hasMoreTokens()) {
			strfile = token.nextToken();
			strfile=DirectoryMng.getTempFileName(action)+File.separator+strfile;
			listFiles.add(strfile);
		}
		String[] retstrs = new String[listFiles.size()];
		listFiles.toArray(retstrs);
		return retstrs;
	}
 
   /**
    * 关联Form
    *
    */   
    public String getFormName(){
        return nc.ui.iuforeport.rep.RepSaveImportForm.class.getName();
    }
    
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO name="RepSaveImportAction" package="nc.ui.iuforeport.rep" 关联Form="nc.ui.iuforeport.rep.RepSaveImportForm">
      <MethodsVO execute="nc.ui.iuforeport.rep.RepSaveImportDlg" saveFile="CloseForward(CloseForward.CLOSE_REFRESH_MAIN)">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/