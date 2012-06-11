/**
 * MultiSheetExcelAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-18
 */
package nc.ui.iufo.dataexchange;
import com.ufida.iufo.pub.tools.AppDebug;


import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.dataexchange.base.ExcelAction;
import nc.ui.iufo.dataexchange.base.IDataExchange;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.iufo.query.tablequery.TableQuerySimpleAction;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * Excel��ʽ���ݴ󵼳�
 * CaiJie
 * 2006-01-18
 */
public class MultiSheetExcelNewAction extends DialogAction {
	public final static java.lang.String FILETYPE = "filetype";
	public final static String ZIP = "ZIP";
	public final static String XLS = "XLS";
	public final static String FILENAME = "filename";

	public final static String ZIPFILE="zipfile";
	
	public final static String MYTABLESELECTID="mytableselectid";
	public final static String SHEETNAMERULE="selectedKeys";
    /**
     * <MethodDescription>
     * CaiJie
     * 2006-01-18
     */
    public ActionForward execute(ActionForm actionForm){
        ActionForward actionForward = null;
        nc.ui.iufo.dataexchange.MultiSheetExcelForm form = (nc.ui.iufo.dataexchange.MultiSheetExcelForm) actionForm;
        String[][] items = getFileTypeItems();
        form.setFileTypes(items);
        form.setSelectedFileType(items[0][0]);
        form.setZipFile(false);
        form.setTableSelectIDs(getTableSelectedIDs());
        form.setSheetNames(getKeyItems());
        actionForward = new ActionForward(getUIClassName());
        form.setAccSchemePK(CSomeParam.getParam(this).getAccSchemePK());
        return actionForward;       
    }
    
    private String[][] getKeyItems(){
    	String[][] sheetNames=null;
    	KeyGroupCache kgc = IUFOUICacheManager.getSingleton().getKeyGroupCache();
    	KeyGroupVO kgvo=null;
    	
    	String strTableID=getTableSelectedID();
    	if (strTableID!=null && strTableID.endsWith("@ddd")){
    		String strRepPK=strTableID.split("@")[1];
    		ReportVO report=IUFOUICacheManager.getSingleton().getReportCache().getByPK(strRepPK);
    		kgvo=kgc.getByPK(report.getKeyCombPK());
    	}else{
	    	String taskId =getCurTaskId();
	    	TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
	    	TaskVO task=taskCache.getTaskVO(taskId);
	    	String keyGroupId=task.getKeyGroupId();
	    	
	    	kgvo = kgc.getByPK(keyGroupId);
    	}
    	KeyVO[] keys = (KeyVO[])kgvo.getKeys();
    	sheetNames=new String[keys.length][2];
    	for( int i = 0; i < keys.length ; i++ )
		{
    		sheetNames[i]=new String[]{keys[i].getKeywordPK(),keys[i].getName()};
		}
    	return sheetNames;
    }
    
    private Vector<String> getSelectedKeyItems(){
    	Vector<String> selectedKeys =new Vector<String>();
    	String[][] keys=getKeyItems();
    	for( int i = 0; i < keys.length ; i++ )
		{
    		if(keys[i]!=null&&keys[i][0]!=null){
    			if("TRUE".equals(getRequestParameter("keyword"+keys[i][0]))){
    				selectedKeys.add(keys[i][0]);
    			}
    		}
		}
    	return selectedKeys;
    }
    private String[][] getFileTypeItems(){
        String[][] items = new String[2][];        
        items[0] = new String[]{ExcelAction.XLS, "EXCEL"+StringResource.getStringResource("miufo1000877")};
        items[1] = new String[]{ExcelAction.HTML,"HTML"+StringResource.getStringResource("miufo1000877")};
        return items;
    }
    
    /**
     * <MethodDescription>
     * CaiJie
     * 2006-01-18
     */
    public ActionForward action(ActionForm actionForm){     
        ActionForward actionForward = new ActionForward(getNextActionClass(), "execute");

    	try {
            Vector selectedReps = null;//����aloneId��reportId��Vec

            //ȡ�ñ���鿴��ʽ��Ĭ��ʱ�ǰ�����鿴
            boolean isViewByRpt = isViewByRpt(this);

            //�õ��û�ѡ�����Ϣ
            String[] vParam=getRequestParameter(MYTABLESELECTID).split(",");
            try{
            	if (isViewByRpt){
            	//���ݱ�����
            		selectedReps = this.getVecForReportView(vParam);
            	}else{
            	//�������񵼳�
            		//�õ���ǰ����
            		  String taskId =getCurTaskId();
            	  	  selectedReps =adjustParamForTask(getCurUserInfo(),getCurOrgPK(),getCurTaskId(), vParam,false);
            	}
            }
            catch(Exception e){
            	throw new CommonException("miufo10000");
            }


            //��ô���Ĳ���ֵ
            String sType = (String) this.getRequestParameter(WebGlobalValue.PARAM_UI_TYPE);
        	if (sType == null || sType.equalsIgnoreCase("null")||sType.trim().length()==0) {
            	sType = IDataExchange.EXCEL;
        	}
            this.addRequestObject(sType,selectedReps);//�ѽ��Hashtable����Session��
            if(ExcelAction.XLS.equalsIgnoreCase(getRequestParameter(MultiSheetExcelNewAction1.FILETYPE))){
            	this.addRequestObject(SHEETNAMERULE, getSelectedKeyItems());
            }
            
            String sFilename = getRequestParameter(FILENAME);
             if (sFilename == null)
                 sFilename = "excel";
                
            actionForward.addParameter(WebGlobalValue.PARAM_UI_TYPE, sType);
            actionForward.addParameter(FILENAME, sFilename);
            String strAccSchemePK=getRequestParameter(CSomeParam.PARAM_ACCSCHEME);
            if (strAccSchemePK==null){
            	TaskVO task=IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(getCurTaskId());
            	if (task!=null)
            		strAccSchemePK=task.getAccPeriodScheme();
            }
            if (strAccSchemePK!=null)
            	actionForward.addParameter(CSomeParam.PARAM_ACCSCHEME, strAccSchemePK);
            	
        } catch (Exception e) {
            return new ErrorForward(e.getMessage());
        }
        
        return actionForward;
    }
    
    public static Vector<String> adjustParamForTask(UserInfoVO userInfo,String strOrgPK,String taskId,String[] strParams,boolean bAloneIDFirst){
    	TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
    	TaskVO task=taskCache.getTaskVO(taskId);
		String[] repids = taskCache.getReportIdsByTaskId(taskId);
		ReportVO[] reportVOs = IUFOUICacheManager.getSingleton().getReportCache().getByPks(repids);
	  	return getVecForTaskView(userInfo,task,strOrgPK,strParams,reportVOs,bAloneIDFirst);
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
        return nc.ui.iufo.dataexchange.MultiSheetExcelForm.class.getName();
    }
    
    /**
     * ����Ҫ���������Vector,����ס�û�ѡ��ı�������˳��
     * �������ڣ�(2002-11-13 11:12:42)
     * @return java.util.Hashtable
     * @param inputStrs java.lang.String[]
     */
    protected Vector getVecForReportView(String[] inputStrs)
    {
    	if(null==inputStrs)
    	return null;

    	Vector returnVec = new Vector();//���ص�Vector

    	for(int i =0;i<inputStrs.length;i++)
    	{
    		StringTokenizer currentTokenizer = new StringTokenizer(inputStrs[i],"@");
    		String aloneId = currentTokenizer.nextToken();
    		String repId = currentTokenizer.nextToken();

    		// wss edit 2002-11-14
    		//returnTable.put(repId,aloneId);
    		returnVec.add(repId+"@"+aloneId);
    	}

    	//����
    	return returnVec;
    }
    /**
     * ����Ҫ���������Vector,����ס�������±��������˳��
     * �������ڣ�(2002-11-13 11:45:34)
     * @return java.util.Hashtable
     * @param param java.lang.String[]
     * @param repVOs nc.vo.iufo.rep.ReportVO[]
     */
    protected static Vector<String> getVecForTaskView(UserInfoVO userInfo,TaskVO task,String strOrgPK,String[] param, ReportVO[] repVOs,boolean bAloneIDFirst){
    	Vector<String> returnVec = new Vector<String>();

    	if(null==param||param.length==0)
    		return null;
    	if(null==repVOs||repVOs.length==0)
    		return null;

    	//ȡ��aloneId
    	StringTokenizer currentTokenizer = new StringTokenizer(param[0],"@");
    	String aloneId = currentTokenizer.nextToken();

        Vector<String> vRepID=new Vector<String>();
        try{
            String[] strRepIDs=CheckResultBO_Client.loadRepIdsByAloneId(aloneId);
            MeasurePubDataVO pubData=MeasurePubDataBO_Client.findByAloneID(aloneId);
            if (strRepIDs!=null){
                strRepIDs=RepDataRightUtil.loadRepsByRight(userInfo, task, RepDataRightVO.RIGHT_TYPE_VIEW, strRepIDs, pubData.getUnitPK(),strOrgPK);
            }
            
            if (strRepIDs!=null)
            	vRepID.addAll(Arrays.asList(strRepIDs));
        }
        catch(Exception e){
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
        if (vRepID.size()<=0)
            return null;

    	for(int i=0;i<repVOs.length;i++)
    	{
    		String repId = repVOs[i].getReportPK();

            if (vRepID.contains(repId)==false)
                continue;

    		String key =null;
    		if (bAloneIDFirst)
    			key=aloneId+"@"+repId;
    		else
    			key=repId+"@"+aloneId;
    		returnVec.add(key);
    	}

        //���ؽ��
        return returnVec;
    }

//    ȡ�ñ���鿴��ʽ��Ĭ��ʱ�ǰ�����鿴
    protected boolean isViewByRpt(IUFOAction action){
    	return TableQuerySimpleAction.isViewByReport(action);
    }
    
    protected String getNextActionClass(){
    	return MultiSheetExcelNewAction1.class.getName();
    }
    protected String getUIClassName(){
    	return nc.ui.iufo.dataexchange.MultiSheetExcelNewDlg.class.getName();
    }
}

/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <ActionVO Description="Excel��ʽ���ݴ󵼳�" name="MultiSheetExcelAction" package="nc.ui.iufo.dataexchange" ����Form="nc.ui.iufo.dataexchange.MultiSheetExcelForm">
      <MethodsVO action="ActionForward(##)" execute="nc.ui.iufo.dataexchange.MultiSheetExcelDlg">
      </MethodsVO>
    </ActionVO>
@WebDeveloper*/