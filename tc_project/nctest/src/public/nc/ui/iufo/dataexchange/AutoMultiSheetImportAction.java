/**
 * MultiSheetImportAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-19
 */
package nc.ui.iufo.dataexchange;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.imp.tc.imp.QueryList;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.dataexchange.base.DirectoryMng;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.iufo.input.InputActionUtil;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.util.iufo.pub.UfoException;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.jcom.xml.XMLUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.WebChoice0;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.table.WebTableModel;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.inputplugin.biz.data.AutoImportExcelDataBizUtil;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;
import com.ufsoft.iufo.web.IUFOAction;
import com.ufsoft.iuforeport.tableinput.TableInputOperServlet;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.table.CellsModel;

/**
 * ���ҳExcel���ݵ���
 * CaiJie
 * 2006-01-19
 */
public class AutoMultiSheetImportAction extends DialogAction {
    public static final String CUR_TASK_ID = "strTaskId";
    public static final String FULL_FILE_PATH = "full_path";
    public static final String flag = "@";
    public static final String Field_ID = "dynEndRow";
    public static final String SEL_SHEET_NAME = "selSheet";
    public static final String SEL_REP_CODE = "selRepCode";
    public static final String SEL_DYN_ENDROW = "selEndRow";
    public static final String BACK_URL = "back_url";
    public static final String REQ_MULTIIMPORTUTIL_OBJ="req_multiimportutil_obj";
    public static final String AUTO_CALC="autoCalc";
    
    /**
     * <MethodDescription>
     * CaiJie
     * 2006-01-19
     */
    public ActionForward execute(ActionForm actionForm){
        ActionForward backActionForward = null;
        ActionForward okActionForward = new ActionForward(AutoMultiSheetImportAction.class.getName(), "doImport");
        ActionForward checkResultActionForward = new ActionForward(ImportExcelCheckResultAction.class.getName(), "execute", false);
        nc.ui.iufo.dataexchange.MultiSheetImportForm form = (nc.ui.iufo.dataexchange.MultiSheetImportForm) actionForm;
        try{
            CSomeParam somePar = CSomeParam.getParam(this);
            //�õ�װ�ص�InputNewUIʱ��Ҫ�Ĳ���ֵ
            String repId = somePar.getRepId();
            String aloneId = somePar.getAloneId();
            String strFileName=getRequestParameter(FULL_FILE_PATH);
            //String filePath = getRequestParameter(FULL_FILE_PATH);
            //filePath=DirectoryMng.getTempFileName(this)+File.separator+strFileName;
            String taskid = getRequestParameter(CUR_TASK_ID);
            MeasurePubDataVO pubData=MeasurePubDataBO_Client.findByAloneID(aloneId);
            
            okActionForward.addParameter(CSomeParam.PARAM_ALONEID, aloneId);
            
            if (repId!=null && repId.trim().length()>0 && repId.equalsIgnoreCase("null")==false)
            	okActionForward.addParameter(CSomeParam.PARAM_REPID, repId);
            okActionForward.addParameter(CUR_TASK_ID, taskid);
            okActionForward.addParameter(CSomeParam.PARAM_OPERTYPE, CSomeParam.STR_OPERATION[somePar.getOperType()]);
            if(somePar.getUnitId() != null){
                okActionForward.addParameter(CSomeParam.PARAM_UNITID, somePar.getUnitId());
            }
            okActionForward.addParameter(CSomeParam.PARAM_FROM, CSomeParam.FROM_CHANGEREP);
            okActionForward.addParameter(CSomeParam.PARAM_SHOWKEYPANEL,""+somePar.isShowKeyPanel());
            okActionForward.addParameter(CSomeParam.PARAM_SHOWREPTREE,""+somePar.isShowRepTree());
            
            if(somePar.getImportExcelDataClassPath() != null)
                okActionForward.addParameter(CSomeParam.IMPORT_EXCEL_DATA_CLASS_PATH, somePar.getImportExcelDataClassPath());
            
            if (repId==null || repId.equalsIgnoreCase("null")){
            	String[] repIds=IUFOUICacheManager.getSingleton().getTaskCache().getReportIdsByTaskId(taskid);
            	if (repIds!=null && repIds.length==1)
            		repId=repIds[0];
            }
            
            String back_url = getRequestParameter(BACK_URL);
            if(back_url == null || back_url.equals("")){
            	back_url = nc.ui.iufo.input.InputNewAction.class.getName();
            }
            okActionForward.addParameter(BACK_URL,back_url);
            
            backActionForward=new ActionForward(back_url,"");
            somePar.getLinkString(backActionForward);
            backActionForward.addParameter(CSomeParam.PARAM_FROM,CSomeParam.FROM_IMPORT);
            
            org.w3c.dom.Document newDoc = XMLUtil.getDocumentBuilder().parse("Ufida_IUFO/shangbaojieshou/xmlList.xml");
			org.w3c.dom.NodeList typeList = newDoc.getElementsByTagName("type");
			org.w3c.dom.NodeList fileList = newDoc.getElementsByTagName("file");
			
			Hashtable matchMap = new Hashtable();
			int i = 0;
			int j = 1;
			try{
				QueryList queryList =  new QueryList();
				
//				queryList.alterReportCommit();
				
				for (; i < typeList.getLength(); i++) {
					String fileAllPath = fileList.item(i).getTextContent();
					
					String date = fileAllPath.substring(fileAllPath.lastIndexOf("/") - 7 , fileAllPath.lastIndexOf("/"));
					String time = pubData.getInputDate().toString().substring(0 , 7);
					System.out.println(date + "  :  " + time);
					
					if(date == time || date.equals(time)){
					if ((typeList.item(i).getTextContent().equals("0")
							|| typeList.item(i).getTextContent() == "0") ) {
						// ��ñ�����sheetҳ���Զ�ƥ����Ϣ
						
						String filePath = fileList.item(i).getTextContent();
						Hashtable tempMap = getAutoMatchMap(taskid,fileList.item(i).getTextContent(), null);// getTaskId(action)
						File file = new File(filePath);
						Enumeration keys = tempMap.keys();
						String fileName = null;
						while (keys.hasMoreElements()) {
							String key = (String) keys.nextElement();
							matchMap.put(key, tempMap.get(key));
							try{
								fileName = key.substring(0 , key.indexOf("_"));
							}catch(Exception ex){
								fileName = key;
							}
							
							System.out.println("File name is ��" + fileName);
							String id = queryList.getReportPk(fileName);
							
							System.out.println("File ID is ��" + id);
							
							//queryList.insertReportCommit(id, aloneId , queryList.getUnitID("199"));
							try { 
								queryList.insertReportCommit(id, aloneId); 
							} catch (Exception e ) {
								AppDebug.debug(e);
								e.printStackTrace();
							}
						}
						j++;
						strFileName = file.getPath();
					}
				}
				}
			}catch(Exception e){
				e.printStackTrace();
				j = 0;
			}
			
			if(j == 1)
				System.out.println("����NULL");
			
			if(j == 0){
//				JOptionPane.showMessageDialog(null, "���뱨��ʱ����δ֪�Ĵ���,�򲻴��������ı���", "��������ʾ", JOptionPane.INFORMATION_MESSAGE);
//				return new CloseForward("window.navigate('com.ufida.web.action.ActionServlet?action=nc.ui.iufo.main.MainUIAction');");
				try {
					 QueryList queryList = new QueryList();
					
						String content = "���뱨��ʱ����δ֪�Ĵ���,�򲻴��������ı���";
						String title = "��������ʾ" ;
						String randomVal = queryList.GenPk();
						queryList.insertReleaseinfo(content, title, randomVal);

						java.util.List<String> userList = queryList.getUserId();
						for (String userId : userList) {
							queryList.insertReleasetarget(randomVal, userId);
						}

					
				} catch (Exception ex) {
					AppDebug.debug(ex);
				}
				return new CloseForward(CloseForward.CLOSE);
			}
            
            //��ñ�����sheetҳ���Զ�ƥ����Ϣ
            //Hashtable matchMap = getAutoMatchMap(taskid,filePath,repId);
            //���ƥ����Ϣ
            AutoImportExcelDataBizUtil.checkMatchMap(matchMap);
            
            //�޸�Ϊ�����ϱ������ı����Ǽ��Ŷ������д˲���
            //if(matchMap.size()==1){
            if(false){
                //������̬���ı����������������,���û�ж�̬��,��ֱ�ӵ���,��������ƥ��������
                //? ��֧��¼��ĺ���̬����ı�����δ����أ�//TODO
            	String strRepPK=null;
            	Object objRepCodes=matchMap.values().toArray()[0];
            	if (objRepCodes instanceof String[] && ((String[])objRepCodes).length==2){
            		String strRepCode=((String[])objRepCodes)[1];
            		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
            		ReportVO report=repCache.getByCode(strRepCode,false);
            		if (report!=null)
            			strRepPK=report.getReportPK();
            	}
            	
                if(AutoImportExcelDataBizUtil.isCanImportDirectedRep(strRepPK)){
                    String[] selStrs = new String[4];
                    selStrs[0] = (String)matchMap.keySet().iterator().next();
                    selStrs[1] = ((String[])matchMap.get(selStrs[0]))[1];
                    selStrs[3] = fileList.item(0).getTextContent();
                    
                    if (selStrs[0]!=null && selStrs[0].trim().equalsIgnoreCase("null")==false
                    		&& selStrs[1]!=null && selStrs[1].trim().equalsIgnoreCase("null")==false){
	                    selStrs[2] = "-1";
	                    ArrayList array = new ArrayList();
	                    array.add(selStrs);
	                    AutoMultiSheetImportUtil util = processImport(this,
                                array,
                                false);
	                    if(util.getLog().getResult().length() > 0 && util.getCheckResults().length<=0){
	                    	throw new CommonException(util.getLog().getResult());
	                    }
	                    else{
	                        if (util.getCheckResults().length<=0){
	                        	ActionForward fwd=new ActionForward(back_url,IIUFOConstants.ACTION_METHOD_OPEN);
	                        	somePar.setFrom(CSomeParam.FROM_CHANGEREP);
	                        	somePar.getLinkString(fwd);
	                    		return new CloseForward("window.navigate('"+fwd.genURI()+"');");
	                        }
	                        
	                        checkResultActionForward.addParameter(CSomeParam.PARAM_ALONEID, aloneId);
                            if(repId != null){
                                checkResultActionForward.addParameter(CSomeParam.PARAM_REPID, repId);
                            }
	                        checkResultActionForward.addParameter(CUR_TASK_ID, taskid);
	                        checkResultActionForward.addParameter(CSomeParam.PARAM_OPERTYPE, CSomeParam.STR_OPERATION[somePar.getOperType()]);
	                        if (somePar.getUnitId()!=null)
	                        	checkResultActionForward.addParameter(CSomeParam.PARAM_UNITID, somePar.getUnitId());
	                        checkResultActionForward.addParameter(CSomeParam.PARAM_FROM, CSomeParam.FROM_CHANGEREP);
	                        checkResultActionForward.addParameter(CSomeParam.PARAM_TASKID,somePar.getTaskId());
	                        checkResultActionForward.addParameter(BACK_URL, back_url);
	                        
	                        if(somePar.getImportExcelDataClassPath() != null)
	                            checkResultActionForward.addParameter(CSomeParam.IMPORT_EXCEL_DATA_CLASS_PATH, somePar.getImportExcelDataClassPath());
	                        
                            //����ActionForward.addParameterֻ�ܷ���String�����Դ˴��ô˷�������
                        	this.addRequestObject(REQ_MULTIIMPORTUTIL_OBJ,util);
                        	return checkResultActionForward;
	                    }
                    }
                }
            }
            
            if (getRequestParameter(AUTO_CALC)!=null)
            	okActionForward.addParameter(AUTO_CALC,"true");
            
            okActionForward.addParameter(FULL_FILE_PATH, transPath(strFileName,false));           
            form.setOkActionForward(okActionForward);   
            form.setBackActionForward(backActionForward);

            //String[][] reportItems= getReportItems(getCurUserInfo(), pubData.getUnitPK(), taskid);
            //form.setTableModel(genTableModel(matchMap, reportItems));
        }
        catch(CommonException e){
          	return new ErrorForward(e.getMessage());
        }
        catch(UfoException e){
          	AppDebug.debug(e);//@devTools e.printStackTrace();
          	return new ErrorForward(e.getMessage());
        }catch(Exception e){
        	AppDebug.debug(e);//@devTools             e.printStackTrace();
            return new ErrorForward(StringResource.getStringResource("miufo1002746"));
        }
    
        return new ActionForward(AutoMultiSheetImportAction.class.getName() , "doImport");
    }

    
    public ActionForward doImport(ActionForm actionForm){
    	try{
	    	String inputURL =getRequestParameter(BACK_URL); //��û���forward  = nc.ui.iufo.input.InputNewAction
	    	 
	    	/**
	    	 *  ��õ���EXCEL�Ķ�Ӧ�б� 
	    	 *  ���磺
	    	 *  array	ArrayList<E>  (id=6038)	
			 *	elementData	Object[10]  (id=6039)	
			 *		[0]	String[3]  (id=6040)	
			 *			[0]	"A0-1����_̫�ָ�Э�η������޹�˾_2011-03-31"	  ->  ����EXCEL�е�����
			 *			[1]	"A0-����"	->  ���ݿ��ȡ�������������б��е�һ��
			 *			[2]	"-1"	->  ����̶�ֵ
	    	 */
	    	
	    	// ��ȡXML�����Ϣ
	    	String time = getPubDataVO(this).getTimeCode();
	    	String getTime = getPubDataVO(this).getInputDate().substring(0 , 7);
	    	String taskid = getRequestParameter(CUR_TASK_ID);
            org.w3c.dom.Document newDoc = XMLUtil.getDocumentBuilder().parse("Ufida_IUFO/shangbaojieshou/xmlList.xml");
			org.w3c.dom.NodeList typeList = newDoc.getElementsByTagName("type");
			org.w3c.dom.NodeList fileList = newDoc.getElementsByTagName("file");
			org.w3c.dom.NodeList fileNameList = newDoc.getElementsByTagName("fileName");
			org.w3c.dom.NodeList AllList = newDoc.getElementsByTagName("list");
			Hashtable matchMap = new Hashtable();
			ArrayList array = new ArrayList();
			String[] fileName = new String[typeList.getLength()];
			for (int i = 0; i < typeList.getLength(); i++) {
				String fileAllPath = fileList.item(i).getTextContent();
				String date = fileAllPath.substring(fileAllPath.lastIndexOf("/") - 7 , fileAllPath.lastIndexOf("/"));
				if(date == getTime || date.equals(getTime)){
				if ((typeList.item(i).getTextContent().equals("0")
						|| typeList.item(i).getTextContent() == "0") ) {
					// ��ñ�����sheetҳ���Զ�ƥ����Ϣ
					String filePath = fileList.item(i).getTextContent();
					fileName[i] = fileNameList.item(i).getTextContent();
					Hashtable tempMap = getAutoMatchMap(taskid,fileList.item(i).getTextContent(), null);// getTaskId(action)

					Enumeration keys = tempMap.keys();
					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();
						//matchMap.put(key, tempMap.get(key));
						String[] strs = new String[4];
						try{
	    					strs = new String[]{key , key.substring(0 , key.indexOf('_')) , "-1" , fileList.item(i).getTextContent() };
	    				}catch(Exception ex){
	    					strs = new String[]{key , key , "-1" , fileList.item(i).getTextContent() };
	    				}
						array.add(strs);
					}
					// matchMap = getAutoMatchMap(taskid,filePath,repId);
				}
				}
			}
			
			//ArrayList array = parseRequset();  // ��ȡ�м�����ϵĵ����б���
	        
	        AutoMultiSheetImportUtil util = processImport(this,array,true);
	        
	        if(util.getLog().getResult().length() > 0 && util.getCheckResults().length<=0){
	        	String[] strErrs=util.getLog().getResult().split("\r\n");
	        	return new ErrorForward(strErrs);
	        //	throw new CommonException(util.getLog().getResult());
	        }
	        
	        else if (inputURL==null || inputURL.trim().length()<=0 || inputURL.equalsIgnoreCase("null")){
	        	if(fileName.length > 0){
	        		File[] files = new File("Ufida_IUFO/shangbaojieshou/" + getTime).listFiles();
	    			for(File file : files){
	    				file.delete();
	    			}
	    			
	    			for(int k = 0 ; k < fileList.getLength() ; k++){
	    				String fileAllPath = fileList.item(k).getTextContent();
	    				String date = fileAllPath.substring(fileAllPath.lastIndexOf("/") - 7 , fileAllPath.lastIndexOf("/"));
	    				if(date.equals(getTime) || date == getTime){
	    					Node node = AllList.item(k);
	    					newDoc.getFirstChild().removeChild(node);
	    					this.toSave(newDoc, "Ufida_IUFO/shangbaojieshou/xmlList.xml");
	    					k--;
	    				}
	    			}	
	        	}
	        	
	        	return new CloseForward(CloseForward.CLOSE);
	        }
	        else
	        {
	            taskid =getRequestParameter(CSomeParam.PARAM_TASKID);
	            MeasurePubDataVO pubdataVo = getPubDataVO(this);
                CSomeParam somePar = CSomeParam.getParam(this);
	            somePar.setAloneId(pubdataVo.getAloneID());
	            somePar.setTaskId(taskid);
	            somePar.setFrom(CSomeParam.FROM_CHANGEREP);
	            somePar.setOperate(CSomeParam.OPERATE_NONE);
	            somePar.setRepId(getRequestParameter(CSomeParam.PARAM_REPID));
	            somePar.setOperType(CSomeParam.MODIFY_REP);
	            
	            if(fileName.length > 0)
    	        	this.UpdateXmlListType(fileName);
	            
	            //�õ�װ�ص�InputNewUIʱ��Ҫ�Ĳ���ֵ      
	            if (util.getCheckResults().length<=0 || (util.getCheckResults().length==1 && (CheckResultVO.NOFORUMULA.equalsIgnoreCase(util.getCheckResults()[0].getAllNote())))){
	                ActionForward fwd=new ActionForward(inputURL,"");
	                somePar.getLinkString(fwd);
	                fwd.addParameter(CSomeParam.IMPORT_EXCEL_DATA_CLASS_PATH,somePar.getImportExcelDataClassPath());
	                fwd.setRedirect(true);
	                
	            	return fwd;
	            }
	            else{
	            	addRequestObject(REQ_MULTIIMPORTUTIL_OBJ,util);
	                ActionForward fwd=new ActionForward(ImportExcelCheckResultAction.class.getName(),"");
	                somePar.getLinkString(fwd);
	                fwd.addParameter(BACK_URL,inputURL);
	                
	                
	                
	            	return fwd;
	            }            
	        }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		AppDebug.debug(e);//@devTools e.printStackTrace();
    		return new ErrorForward(e.getMessage());
    	}
    }
    
//  ����type
	public void UpdateXmlListType(String[] fileName) throws SAXException,
			IOException {
		String xmlList = "Ufida_IUFO/shangbaojieshou/xmlList.xml";
		org.w3c.dom.Document document = XMLUtil.getDocumentBuilder().parse(xmlList);
		org.w3c.dom.NodeList lists = document.getElementsByTagName("list");
		// �ж��Ƿ��Ѿ���������ļ�
		for (int s = 0; s < lists.getLength(); s++) {
			String xmlfileName = document.getElementsByTagName("fileName").item(s).getFirstChild().getNodeValue();
			if (xmlfileName.equals(fileName[s]) || xmlfileName == fileName[s]) {

				document.getElementsByTagName("type").item(s).getFirstChild()
						.setNodeValue("1");
				toSave(document, xmlList);
			}
		}
	}

	public void toSave(org.w3c.dom.Document document, String filename) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			mye.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}
    
    /**
     * �����ַ���
     * @param request HttpServletRequest
     * @throws CommonException
     * @return ArrayList
     */
    private ArrayList parseRequset() throws CommonException{
        try{
            String source=getRequestParameter("hid_sel_relation");
            if(source == null)
                return null;

            // ���Ȳ��&
            StringTokenizer token = new StringTokenizer(source, "&");
            int ValColNum = 3;//��ʾҪȡ���е�ֵ
            String[] selValue = new String[ValColNum];//0:SheetName; 1:selRepCode; 2:selDynEndRow
            String strSub;
            int tokenIndex = 0;
            //��ǰĬ��Ϊ0:SheetName; 1:selRepCode; 2:selDynEndRow�Ĺ���
            while(token.hasMoreElements()){
                strSub = token.nextToken();
                if( strSub.indexOf(SEL_SHEET_NAME) >= 0
                   || strSub.indexOf(SEL_DYN_ENDROW) >= 0
                   || strSub.indexOf(SEL_REP_CODE) >= 0 )
                {
                    //���=
                    int eqIndex = strSub.indexOf("=");
                    if(eqIndex > 0){
                        selValue[tokenIndex] = strSub.substring(eqIndex + 1);
                    }
                    tokenIndex += 1;
                }
            }
            //���,
            ArrayList<String[]> valueList = new ArrayList<String[]>();
            HashSet<String> repcodeSet = new HashSet<String>();
            String value;
            for(int i = 0 ; i < ValColNum ; i++)
            {
                int index = 0;
                if(selValue[i] != null)
                {
                    token = new StringTokenizer(selValue[i], ",");
                    while(token.hasMoreElements()){
                        String[] param;
                        if(valueList.size() <= index || valueList.get(index) == null){
                            param = new String[ValColNum];
                            valueList.add(index, param);
                        } else{
                            param = (String[])valueList.get(index);
                        }
                        //У���Ƿ����IUFO�����Ӧһ��sheet
                        value = token.nextToken();
                        if(i == 1){
                            if(repcodeSet.contains(value)){
                                throw new CommonException("miufo1002753");  //"���������sheet��Ӧһ��IUFO����"
                            }
                            repcodeSet.add(value);
                        }
                        param[i] = value;
                        valueList.set(index, param);
                        index += 1;
                    }
                }
            }
            
            if (getRequestParameter(AUTO_CALC)!=null){
	            Object[] objValueList=valueList.toArray();
	            Arrays.sort(objValueList,new Comparator<Object>(){			
	                public int compare(Object o1, Object o2) {
						String[] strVals1=(String[])o1;
						String[] strVals2=(String[])o2;
						
		            	ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		                String[] strRepIDs=IUFOUICacheManager.getSingleton().getTaskCache().getReportIdsByTaskId(getCurTaskId());
		                ArrayList<String> vRepCode=new ArrayList();
		                for (int i=0;strRepIDs!=null && i<strRepIDs.length;i++){
		                	ReportVO report=repCache.getByPK(strRepIDs[i]);
		                	if (report!=null)
		                		vRepCode.add(report.getCode());
		                }
						
						return vRepCode.indexOf(strVals1[1])-vRepCode.indexOf(strVals2[1]);
					}
	            });
	            for (int i=0;i<objValueList.length;i++)
	            	valueList.set(i,(String[])objValueList[i]);
            }
            return valueList;
        }catch(CommonException ce)
        {
            throw ce;
        }catch(Exception e )
        {
AppDebug.debug(e);//@devTools             e.printStackTrace();
            throw new CommonException("miufo1002754");  //"ƥ������������"
        }
    }    

    
   /**
    * FormֵУ��
    * @param actionForm
    * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
    */
    public String[] validate(ActionForm actionForm){
       return null;
       
    }        
     
    
   /**
    * ����Form
    *
    */   
    public String getFormName(){
        return nc.ui.iufo.dataexchange.MultiSheetImportForm.class.getName();
    }
    
    private String transPath(String path,boolean bTransBack){
        if(!bTransBack)
            path = path.replace('\\','^');
        else
            path = path.replace('^','\\');
        return path;
    }
    /**
     * �����������ƺ�����˳���Զ�����ƥ�䣬�����ǰ�����Ѿ��򿪣����ҵ����Excel��Ҳֻ��һ�ű�����ֱ������ƥ��Զ�����
     * @param strTaskPK String ��ǰ����PK
     * @param strfileName String Excel�ļ�ȫ·����
     * @param curRepId String ��ǰ�򿪵ı���pk
     * @throws Exception
     * @return HashMap
     */
    private Hashtable getAutoMatchMap(String strTaskPK,String strfileName,String strCurRepPK) throws Exception{
        if(strTaskPK == null || strfileName == null){
            return null;
        }
        //׼�������еı�����Ϣ
        ChooseRepData[] chooseRepDatas = getRepDatas(strTaskPK);
        //׼����Ҫƥ���workbook����
        HSSFWorkbook workBook = AutoImportExcelDataBizUtil.getImportWorkBook(strfileName);
        
        try{
            return AutoImportExcelDataBizUtil.doGetAutoMatchMap(chooseRepDatas, workBook, strCurRepPK);
        }catch(TableInputException ex){
AppDebug.debug(ex);//@devTools             ex.printStackTrace(System.out);
            throw new CommonException(ex.getMessage());
        }
    }

   

    /**
     * ���������еĿɵ��뱨����Ϣ
     * @param strTaskPK
     * @return
     */
    private ChooseRepData[] getRepDatas(String strTaskPK) {
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        ReportCache reportCache = IUFOUICacheManager.getSingleton().getReportCache();
        String[] repIds = taskCache.getReportIdsByTaskId(strTaskPK);
        ReportVO[] repVos1 = reportCache.getByPks(repIds);
        ChooseRepData[] chooseRepDatas = TableInputOperServlet.geneChooseRepDatas(repVos1);
        return chooseRepDatas;
    }
    
    private AutoMultiSheetImportUtil processImport(IUFOAction action,
            List array,boolean isNeedSave) throws CommonException,IOException{
        //����û�ѡ���˵�����,����е���,����ֱ�ӹر�
    	
    	QueryList queryList = new QueryList();
    	
        if(array != null && array.size() > 0 ){
        	
        	//��ȡ�б� ������ɺ������ʾ�������Ϣ
        	// modify by yh for 2011��3��23��10:56:00
        	List listImportInfos = new ArrayList();
        	for(int i = 0 ; i < array.size() ; i++){
	        	//String strImportFilePath = getImportFilePath(action);     // ��ȡEXCEL�ļ���·��       
	            HSSFWorkbook workBook = AutoImportExcelDataBizUtil.getImportWorkBook(((String[])array.get(i))[3]); //��ȡExcel�е�sheet��Ϣ
	            if(workBook == null){
	                return null;
	            }               
	            List arrays = new ArrayList();
	            arrays.add(array.get(i));
	            Object obj = (AutoImportExcelDataBizUtil.getImportInfos(arrays, workBook)).get(0);  //�õ�����Excel��׼��������Ϣ(CellsModel)
	            listImportInfos.add(obj);
        	}
        	
        	
            //�õ�����Excel�Ĺ�����ʵ��
            String strTaskPK = getTaskId(action); //����ȡ��ǰ��¼�û�ʹ�õ�����
            UserInfoVO curUserInfoVO = action.getCurUserInfo();  //��ȡ��ǰ��¼�û�����Ϣ
            CSomeParam somePar = CSomeParam.getParam(action);
            //String strImportExcelDataClassPath = somePar.getImportExcelDataClassPath(); // ����EXCEL�������ȫ��
            String strImportExcelDataClassPath = "nc.ui.iufo.dataexchange.AutoMultiSheetImportUtil";
            DataSourceVO dataSource = (DataSourceVO)getSessionObject(IIUFOConstants.DefaultDSInSession);  //����ȡ��ǰ�û�������Դ
            MeasurePubDataVO pubdataVo = getPubDataVO(action); //���ؼ��ֵ���Ϣ
            // strTaskPK = ����PK , curUserInfoVO = �û�VO , strImportExcelDataClassPath = ����EXCEL�������ȫ�� , dataSource = ��ȡ��ǰ�û�������Դ , pubdataVo = �ؼ���VO , action.getCurLoginDate() = �û���¼����
            //��importUtil = ��ʼ����ص���EXCEL������
            AutoMultiSheetImportUtil importUtil = AutoImportExcelDataBizUtil.getImportUtilBase(strTaskPK, curUserInfoVO, strImportExcelDataClassPath, dataSource, pubdataVo,getCurOrgPK(),action.getRequestParameter(AUTO_CALC)!=null,action.getCurLoginDate());
            //������Excel׼��������Ϣ(CellsModel),���뵽���ݿ�
            // listImportInfos = ��Ҫ�Ľ�Ҫ�����EXCEL����Ϣ
            // isNeedSave = ����̶�ֵ true
            AutoImportExcelDataBizUtil.processImportData(importUtil, listImportInfos, isNeedSave);
            
            try {
				queryList.updateReportCommit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
            return importUtil;
        }
        else
            throw new CommonException("miufo1002747");  //"��ѡ��Ҫ�����Excel��"
    }

    /**
     * @param action
     * @return
     */
    private String getImportFilePath(IUFOAction action) {
        String filepath = transPath(action.getRequestParameter(FULL_FILE_PATH),true);
        filepath=DirectoryMng.getTempFileName(action)+File.separator+filepath;
        return filepath;
    }

    private String getTaskId(Action action){
        return action.getRequestParameter(CUR_TASK_ID);
    }
    
    
    private MeasurePubDataVO getPubDataVO(Action action){
        CSomeParam somepara = CSomeParam.getParam(action);
        MeasurePubDataVO pubdataVo = null;
        try{
            pubdataVo = MeasurePubDataBO_Client.findByAloneID(somepara.getAloneId());
        }
        catch(Exception e){}
        return pubdataVo;
    }
    
    private WebTableModel genTableModel(Hashtable matchMap,String[][] reportItems){
        
        	WebTableModel model = null;
            try{
                String[] columns = AutoImportExcelDataBizUtil.getTableColumns();               
                
                if(matchMap.size() > 0){
                    //�����б���
                    String[] mapKeys = new String[matchMap.size()];
                    Set keySet = matchMap.keySet();
                    keySet.toArray(mapKeys);
                    Object[][] datas = new Object[mapKeys.length][];
                    for(int i = 0; i < datas.length ; i++ ){
                        datas[i] = new Object[4];
                        datas[i][0] = mapKeys[i];                    
                        datas[i][1] = createWebChoice(reportItems);
                        ((WebChoice0)datas[i][1]).setValue(mapKeys[i]);
                        ((WebChoice0)datas[i][1]).setID(mapKeys[i]);
                        ((WebChoice0)datas[i][1]).setName(mapKeys[i]);
                        datas[i][2] = mapKeys[i];
                        datas[i][3] = createWebTextField(null);   
                        ((WebTextField)datas[i][3]).setID(mapKeys[i]+"dynEndRow");
                        ((WebTextField)datas[i][3]).setName(mapKeys[i]+"dynEndRow");
                        
                    }
                    
                    model = new WebTableModel(datas, columns);    
                }
                else{
                    throw new CommonException("miufo1002748");  //"�����Excel�ļ���û�й�����"
                }
                return model;
            }
            catch(CommonException e){
              throw e;
            }
            catch(Exception e){
AppDebug.debug(e);//@devTools                 e.printStackTrace();
                throw new CommonException(e.getMessage());
            }
        }
    
    public static ReportVO[] getImportExcelReports(UserInfoVO curUserInfoVO,String strUnitID,String strTaskId,String strOrgPK) throws Exception{
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        ReportCache reportCache = IUFOUICacheManager.getSingleton().getReportCache();
        String[] repIds = taskCache.getReportIdsByTaskId(strTaskId); 
        TaskVO task=taskCache.getTaskVO(strTaskId);
        
        if (strUnitID!=null && strUnitID.trim().length()>0)
        	repIds=RepDataRightUtil.loadRepsByRight(curUserInfoVO,task,RepDataRightVO.RIGHT_TYPE_MODIFY,repIds,strUnitID,strOrgPK);
        
        ReportVO[] repVos = reportCache.getByPks(repIds);
        ArrayList<ReportVO> list = new ArrayList<ReportVO>();
        if(repVos != null && repVos.length > 0){
            for(int i = 0 ; i < repVos.length; i++ ){
                CellsModel  cellsModel = IUFOUICacheManager.getSingleton().getRepFormatCache().getUfoTableFormatModel(repVos[i].getReportPK());
                if (cellsModel==null)
                	continue;
                
            	if (InputActionUtil.getReportTypeByRep(repVos[i].getReportPK())==CSomeParam.TYPE_REP_INPUT)
            	    list.add(repVos[i]);            	    
            }
        }
        return list.toArray(new ReportVO[0]);
    }

    private String[][] getReportItems(UserInfoVO curUserInfoVO,String strUnitID,String strTaskId) throws Exception{
    	ReportVO[] reports=getImportExcelReports(curUserInfoVO,strUnitID,strTaskId,getCurOrgPK());
            
        String[][] items = new String[reports.length][];
        ReportVO vo = null;
        for(int i = 0; i < reports.length; i++){
            vo = (ReportVO)reports[i];
            items[i] = new String[]{vo.getCode(), "("+vo.getCode()+")"+vo.getName()};
        }            
        return items;
    } 
        
        private WebTextField createWebTextField(String value){
            WebTextField field = new WebTextField(value);        
            return field;
        }
        
        private WebChoice0 createWebChoice(String[][] items){
            WebChoice0 choice = new WebChoice0();
            choice.setItems(items);
            return choice;
        }        
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO Description="���ҳExcel���ݵ���" name="MultiSheetImportAction" package="nc.ui.iufo.dataexchange" ����Form="nc.ui.iufo.dataexchange.MultiSheetImportForm">
      <MethodsVO execute="">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/