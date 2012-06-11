/**
 * RepDataAuthMngAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-17
 */
package nc.ui.bi.dataauth;
import java.util.ArrayList;

import nc.us.bi.dataauth.RepDataAuthSrv;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.dataauth.IDataAuthConst;
import nc.vo.bi.dataauth.RepDataAuthVO;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.pub.BusinessException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.MultiFrameAction;


/**
 * 报表数据权限管理Action
 * zyjun
 * 2006-04-17
 */
public class RepDataAuthMngAction extends MultiFrameAction {

    /**
     * <MethodDescription>
     * zyjun
     * 2006-04-17
     */
    public ActionForward execute(ActionForm actionForm) {   
    	
    	if( isAuthorizedUser() ){
	    	DataAuthMngForm	form = (DataAuthMngForm)actionForm;
	    	String			strRepPK = getSelectedRepPK();
	    	form.setRepPK(strRepPK);
	    	String[][]		strDimItems = getDimItems(strRepPK);
	    	if( strDimItems != null ){
	    		form.setDimItems(strDimItems);
	    		String      strDimPK = getRequestParameter(IDataAuthConst.DIMPK);
	    		if( strDimPK == null ){
	    			strDimPK = strDimItems[0][0];
	    			addSessionObject(IDataAuthConst.DIMPK, strDimPK);//增加到Session中
	    		}
	    		form.setDimPK(strDimPK);
	    		return new ActionForward(RepDataAuthMngUI.class.getName());
	    	}else{
	    		return new ErrorForward(StringResource.getStringResource("mbiauth0010"));//报表格式中没有关联维度，无法设置数据权限"));
	    	}
    	}else{
    		return new ErrorForward(StringResource.getStringResource("mbiauth0011"));//当前用户不是报表的拥有者，不能进行报表数据权限设置"));
    	}
        
    }

    public ActionForward  delete(ActionForm actionForm){
    	try{
    		String[]	strIDs = getTableSelectedIDs();
    		RepDataAuthSrv.getInstance().removeRepDataAuthes(strIDs);
    		
    		return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
    	}catch(BusinessException e){
    		return new ErrorForward(e.getMessage());
    	}
    }

    /**
     * 树模型加载,与维度数据权限一样
     * zyjun
     * 2006-04-17
     */	    
    public IWebTreeModel getTreeModel(){
    	String		strDimPK = getCurrentDimPK();   	
    	addSessionObject(IDataAuthConst.DIMPK, strDimPK);
    	return DataAuthToolKit.getDimMemberTreeModel(strDimPK);
    }
    
    /**
     * 表模型加载
     * zyjun
     * 2006-04-17
     */	    
    public IWebTableModel getTableModel(){
    	try{
	    	String		strRepPK = getSelectedRepPK();
	    	String		strDimPK = getCurrentDimPK();
	    	String		strDimMemberPK = getTreeSelectedID();
	    	RepDataAuthVO[]		authVOs = RepDataAuthSrv.getInstance().getRepAuthesByMember(strRepPK, strDimPK, strDimMemberPK);
	    	return DataAuthToolKit.getDataAuthTableModel(authVOs);
	    	
    	}catch(BusinessException e){
    		throw new WebException(e.getMessage());
    	}
    }
   

			
    /**
     * 获取Action对应的form名称 
     * zyjun
     * 2006-04-17
     */
	public String getFormName() {
		return DataAuthMngForm.class.getName();
	}
	
	private  String  getSelectedRepPK(){
		return ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());	
	}
	
	private ReportVO	getReportVO(String strRepPK){
		try{
			ReportVO		repVO = BIReportSrv.getInstance().getByID(strRepPK);
			if( repVO == null ){
				throw new WebException("mbiauth0012");//报表已删");
			}
			return repVO;
		}catch(Exception e){
			throw new WebException(e.getMessage());
		}
	}

	/**
	 * 根据报表包含的纬度信息，构建WebChoice需要的列表信息
	 * @param strRepPK
	 * @return
	 */
	private  String[][]  getDimItems(String strRepPK){
		//根据报表ＰＫ得到报表VO, 
		ReportVO		repVO = getReportVO(strRepPK);
		BaseReportModel	repFormat = (BaseReportModel)repVO.getDefinition();
		
		if(repFormat != null ){
			//如果报表有多个查询，则返回每个查询包含的维度，并保证每个维度只出现一次
			String[]		strQueryPKs = repFormat.getQueryIDs();
			//根据报表ＶＯ中的查询ＩＤ得到查询的元数据模型

			try{
				ArrayList<String>		aryList = new ArrayList<String>();
				for( int i=0; i<strQueryPKs.length; i++ ){
					MetaDataVO[]	mDatas = QueryModelSrv.getDimFlds(strQueryPKs[i]);				
					if(mDatas != null ){
						for( int j=0; j<mDatas.length; j++ ){
							String		strDimPK = mDatas[j].getPk_dimdef();
							if( strDimPK != null && strDimPK.length() >0 && aryList.contains(strDimPK)== false){
								aryList.add(mDatas[j].getPk_dimdef());
							}
						}
					}
				}
				String[][]		strItems = null;
				if( aryList.size() >0){
					String[]	strDimIDs = new String[aryList.size()];
					aryList.toArray(strDimIDs);
					DimensionVO[]	dimVOs = DimensionSrv.getInstance().getDimByIDs(strDimIDs);
					if( dimVOs != null && dimVOs.length >0 ){
						strItems = new String[dimVOs.length][];
						for( int i=0; i<strItems.length; i++ ){
							strItems[i] = new String[]{dimVOs[i].getDimID(), dimVOs[i].getDimname()};
						}
					}
				}
				return strItems;
			}catch(Exception e){
				AppDebug.debug(e);
				throw new WebException(e.getMessage());
			}
		}else{
			throw new WebException(StringResource.getStringResource("mbiauth0013"));//报表尚未定义格式，无法设置数据权限"));
		}
	
	}
	
	private boolean	isAuthorizedUser(){
		//判断是否是超级用户
		UserInfoVO		userVO = getCurUserInfo();
		if( userVO.getRole()== UserInfoVO.SUPER_USER ){
			return true;
		}
		
		//是否是报表的创建者
		ReportVO		repVO =getReportVO(getSelectedRepPK());
		if( repVO.getOwnerid().equals(userVO.getID())){
			return true;
		}
		return false;
	}
	
	private String  getCurrentDimPK(){
		String 		strDimPK = getRequestParameter(IDataAuthConst.DIMPK);
		if( strDimPK == null ){
//	    	String			strRepPK = getSelectedRepPK();
//	    	String[][]		strDimItems = getDimItems(strRepPK);
//			strDimPK = strDimItems[0][0];
			strDimPK = (String)getSessionObject(IDataAuthConst.DIMPK);//
		}
		return strDimPK;
	}
  }
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiActionVO name="RepDataAuthMngAction" package="nc.ui.bi.dataauth">
      <MethodsVO execute="">
      </MethodsVO>
    </MultiActionVO>
@WebDeveloper*/