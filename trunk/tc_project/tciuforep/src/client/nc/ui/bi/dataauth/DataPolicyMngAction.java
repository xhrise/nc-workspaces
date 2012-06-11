/**
 * DataPolicyMngAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-04-17
 */
package nc.ui.bi.dataauth;
import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.itf.iufo.exproperty.IExPropConstants;
import nc.ui.bi.integration.dimension.DimUIToolKit;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.us.bi.dataauth.DataPolicySrv;
import nc.vo.bi.dataauth.DataPolicyVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.pub.BusinessException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.WebTableColumn;
import com.ufida.web.comp.table.WebTableModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.MultiFrameAction;


/**
 * ��������������
 * zyjun
 * 2006-04-17
 */
public class DataPolicyMngAction extends MultiFrameAction {

    /**
     * <MethodDescription>
     * zyjun
     * 2006-04-17
     */
    public ActionForward execute(ActionForm actionForm) {
    	ActionForward	fwd = DimUIToolKit.checkDimRefer(this);
    	if( fwd == null ){
	    	if( DataAuthToolKit.isAuthorizedDimUser(this) ){
	    	   	DataAuthMngForm		form = (DataAuthMngForm)actionForm;
	        	form.setDimPK(DataAuthToolKit.getSelectedDimPK(this));
	        	return new ActionForward(DataPolicyMngUI.class.getName());  
	    	}else{
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0007"));//ֻ�й���Ա��ά�ȵĴ����߲��ܽ�������Ȩ�޹���"));
	    	}
    	}
    	return fwd;
    }


    /**
     * ��ģ�ͼ���
     * zyjun
     * 2006-04-17
     */	    
    public IWebTableModel getTableModel(){
    	try{
	    	//���ͷ��Ϣ
	    	String[]	strColumnNames ={
	    			"ubiauth0016",//"��ɫ",
					"ubiauth0015",//"�û��ֶ�",
					"ubiauth0014",//"ά���ֶ�",
					"ubiauth0017",//"��������",
					"ubiauth0008",//"Ȩ������",
					"ubiauth0018",//"��Ȩ��"
	    	};
	    	WebTableColumn[]	columns = new WebTableColumn[strColumnNames.length];
	    	for( int i=0; i<strColumnNames.length; i++ ){
	    		columns[i] = new WebTableColumn();
	    		columns[i].setColumnLabel(StringResource.getStringResource(strColumnNames[i]));
	    	}
	    	
	    	//�������
	    	Object[][]		objs = null;
	    	String			strDimPK = DataAuthToolKit.getSelectedDimPK(this);
	    	DataPolicyVO[]	dataPolicyVOs = DataPolicySrv.getInstance().getDataPolicyByDim(strDimPK);
	    	if( dataPolicyVOs != null ){
	    		objs = new String[dataPolicyVOs.length][];
	    		for( int i=0; i<dataPolicyVOs.length; i++){
	    			objs[i] = new String[]{
	    					dataPolicyVOs[i].getPk_datapolicy(),
							DataAuthToolKit.getRoleName(dataPolicyVOs[i].getPk_role()),
							getUserFieldName(dataPolicyVOs[i].getUserfield()),
							getDimFieldName(strDimPK, dataPolicyVOs[i].getDimfield()),
							DataAuthToolKit.getRuleString(dataPolicyVOs[i].getRule()),
							DataAuthToolKit.getTypeString(dataPolicyVOs[i].getType()),
							DataAuthToolKit.getUserCode(dataPolicyVOs[i].getPk_author()),
	    			};
	    		}
	    	}
	    	WebTableModel tableModel = new WebTableModel(objs, columns);   	
	    	return tableModel;
    	}catch(BusinessException e){
    		throw new WebException(e.getMessage());
    	}
    }
	
	public ActionForward delete(ActionForm actionForm) {   
		try{
			String[]	strIDs = getTableSelectedIDs();
			DataPolicySrv.getInstance().removeDataPolicys(strIDs);
			return new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		}catch(BusinessException e){
			return new ErrorForward(e.getMessage());
		}
	}
			
    /**
     * ��ȡAction��Ӧ��form���� 
     * zyjun
     * 2006-04-17
     */
	public String getFormName() {
		return DataAuthMngForm.class.getName();
	}
	private String  getUserFieldName(String strColumnName){
		return getExPropName(IExPropConstants.EXPROP_MODULE_USER, null, strColumnName);
	}
	private String  getDimFieldName(String strDimPK, String strColumnName){
		if( strColumnName.equals("pk_member")){
			return "";
		}
		return getExPropName(IBIExPropConstants.EXPROP_MODULE_DIMENSION, strDimPK, strColumnName);
	}
	private String getExPropName(String strModuleID, String strPropOwnerPK, String strColumnName){
		IExPropOperator  operator = ExPropOperator.getExPropOper(strModuleID);
		try{
			ExPropertyVO	propVO = operator.loadExPropByColumn(strPropOwnerPK, strColumnName);
			if( propVO != null ){
				return propVO.getName();
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
		return null;
	}
		
  }
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiActionVO name="DataPolicyMngAction" package="nc.ui.bi.dataauth">
      <MethodsVO execute="">
      </MethodsVO>
    </MultiActionVO>
@WebDeveloper*/