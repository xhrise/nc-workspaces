/*
 * �������� 2006-4-12
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.LinkServletUtil;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsModel;

public class InputChangeKeywordsOper extends InputBizOper{
    private Object[] m_oOtherParams = null;
    
    /**
     * 
     * @param ufoReport
     * @param otherParams
     */
    public InputChangeKeywordsOper(UfoReport ufoReport,Object[] otherParams) {
        super(ufoReport);
        m_oOtherParams = otherParams;
    }
    private Object[] getOtherParams(){
        return m_oOtherParams;
    }
    /**
     * 
     * @param transReturnObj
     * @param nMenuType
     * @return ���ظ�html����ʾ��Ϣ
     */
    protected String dealTransReturnObj(Object transReturnObj,int nMenuType) {
        StringBuffer sbErrorRetObjs = new StringBuffer();
        if(checkReturnObjs(transReturnObj,sbErrorRetObjs)){
            return sbErrorRetObjs.toString();
        }
        Object[] results = null;
        if(transReturnObj instanceof Object[]){
            results = (Object[])transReturnObj;
        }else{
            return null;
        }
        Object firstTransReturnObj = transReturnObj;
        if(results.length ==3 ){
            firstTransReturnObj = new Object[]{results[0],results[1]};
            Object[] otherRetResults = (Object[])results[2];
            if(otherRetResults != null && otherRetResults.length > 0){
            	//����AloneID
                String strNewAloneID = (String)otherRetResults[0];
                if(isChangeKeywordsSubmit(nMenuType)){
                    getTransObj().getRepDataParam().setAloneID(strNewAloneID);  
                }else{  
                    getTransObj().getRepDataParam().setImportAloneID(strNewAloneID);  
                }           
                //��������Դ��Ϣ
                DataSourceInfo dataSourceInfo = (DataSourceInfo)otherRetResults[1];
                if(dataSourceInfo != null && getTransObj().getRepDataParam().getDSInfo() != null){
                	getTransObj().getRepDataParam().getDSInfo().setDSUnitPK(dataSourceInfo.getDSUnitPK());
                    getTransObj().getRepDataParam().getDSInfo().setDSUserPK(dataSourceInfo.getDSUserPK());
                    getTransObj().getRepDataParam().getDSInfo().setDSPwd(dataSourceInfo.getDSPwd());	
                }
                
            }  
        }else{
            firstTransReturnObj = transReturnObj;
        }
        
//        if(firstTransReturnObj != null &&  results.length >=2 && results[1] instanceof CellsModel){
        if(results.length >=2 && results[1] instanceof Object[]){
        	return super.dealTransReturnObj(firstTransReturnObj,nMenuType);
        }else {
            return null;
        }
    }
    private boolean isChangeKeywordsSubmit(int nMenuType){
        if(nMenuType == ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT){
            return true;
        }
        return false;
    }
    /**
     * ִ������Servlet�Ĳ���
     * @param nMenuType
     * @return
     */
    protected  Object doLinkServletTask(int nMenuType){
        return linkServletTask(nMenuType,getTransObj(),getOtherParams());
    }
    /**
     * �ϴ����ݣ�����һЩ���������������Ϣ
     * @param nMenuType
     * @param inputTransObj
     * @return
     */
    public static Object linkServletTask(int nMenuType, TableInputTransObj inputTransObj,Object[] oOtherParams) {
        Object returnObj = null;
        switch(nMenuType){
            //����IUFO����
            case ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO: 
            //����IUFO����
            case ITableInputMenuType.MENU_TYPE_IMPORTDATA_EXCEL: 
            //�������Դ����
            case ITableInputMenuType.MENU_TYPE_CHECKDS:
            //�л��ؼ����ύ    
            case ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT:
                //�õ���̨����󷵻�ǰ�˵����ݶ���
                Object[] results = LinkServletUtil.linkTableInputOperServlet(nMenuType,inputTransObj,null,oOtherParams); 
                returnObj = results; 
                break;                
            default:
                break;
        }
            
        return returnObj;
    }
    
}
