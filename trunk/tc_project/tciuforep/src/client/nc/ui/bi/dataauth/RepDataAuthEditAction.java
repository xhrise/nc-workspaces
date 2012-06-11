/*
 * 创建日期 2006-4-21
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.dataauth;

import java.util.ArrayList;

import nc.us.bi.dataauth.RepDataAuthSrv;
import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.dataauth.IDataAuthConst;
import nc.vo.bi.dataauth.IDataAuthVO;
import nc.vo.bi.dataauth.RepDataAuthVO;
import nc.vo.pub.BusinessException;

/**
 * @author zyjun
 * 报表数据权限编辑Action, 继承DimDataAuthEditAction
 * 
 */
public class RepDataAuthEditAction extends DimDataAuthEditAction {

	protected void setCreateForm(DataAuthEditForm form) {
		super.setCreateForm(form);
		form.setDimPK((String)getSessionObject(IDataAuthConst.DIMPK));
		form.setRepPK(getRequestParameter(IDataAuthConst.REPPK));
	}
	protected void setUpdateForm(DataAuthEditForm form, IDataAuthVO dataAuthVO) {
		super.setUpdateForm(form, dataAuthVO);
		form.setDimPK((String)getSessionObject(IDataAuthConst.DIMPK));
		RepDataAuthVO	repDataAuthVO = (RepDataAuthVO)dataAuthVO;
		form.setRepPK(repDataAuthVO.getPk_report());
	}
	protected  IDataAuthVO  getDataAuthVO(String strPK) throws BusinessException{
		return RepDataAuthSrv.getInstance().getRepAuthByPK(strPK);
	}

	
	protected void doCreate(DataAuthEditForm form) throws BusinessException {
    	//新建,需要分清楚哪些是新建,哪些是修改
    	String[]				strUserPKs = DataAuthToolKit.getIDs(form.getAutheePKs());
    	RepDataAuthVO[][]		dataAuthVOs = getCreateAndUpdateVOs(strUserPKs, form);
    	if( dataAuthVOs[0] != null ){
    		RepDataAuthSrv.getInstance().createRepDataAuthes(dataAuthVOs[0]);
    	}
    	if( dataAuthVOs[1] != null ){
    		RepDataAuthSrv.getInstance().updateRepDataAuthes(dataAuthVOs[1]);
    	}
    	
	}
	protected void doUpdate(DataAuthEditForm form) throws BusinessException {
    	//修改
    	RepDataAuthVO		dataAuthVO = new RepDataAuthVO();
    	dataAuthVO.setPk_report(form.getRepPK());
    	setFormToDataAuthVO(form, dataAuthVO);
    	RepDataAuthSrv.getInstance().updateRepDataAuth(dataAuthVO);
	}
	
	/**
	 * 针对选择的用户集合,查询数据库,将需要新建和修改的VO分开
	 * @param strUserPKs
	 * @param form
	 * @return
	 * @throws BusinessException
	 */
	private RepDataAuthVO[][]  getCreateAndUpdateVOs(String[] strUserPKs, DataAuthEditForm form) throws BusinessException{
		String		strDimPK = form.getDimPK();
		String		strMemberPK = form.getDimMemberPK();
		String		strRepPK = form.getRepPK();
    	ArrayList<RepDataAuthVO>	aryCreates = new ArrayList<RepDataAuthVO>();
    	ArrayList<RepDataAuthVO>	aryUpdates = new ArrayList<RepDataAuthVO>();
    	for( int i=0; i<strUserPKs.length; i++ ){
    		RepDataAuthVO	authVO = new RepDataAuthVO();
    		authVO.setPk_report(strRepPK);
    		setFormToDataAuthVO(form, authVO);
    		authVO.setPk_user(strUserPKs[i]);
    		
    		RepDataAuthVO	authOldVO = RepDataAuthSrv.getInstance().getRepAuthesByMemberUser(strRepPK, strDimPK, strMemberPK, strUserPKs[i]);
    		if( authOldVO != null ){
    			authVO.setPk_rep_dataauth(authOldVO.getPk_rep_dataauth());
    			aryUpdates.add(authVO);
    		}else{
    			authVO.setPk_rep_dataauth(IDMaker.makeID(20));
    			aryCreates.add(authVO);
    		}
    	}
    	RepDataAuthVO[][]	results = new RepDataAuthVO[2][];
    	if( aryCreates.size() >0){
    		results[0] = new RepDataAuthVO[aryCreates.size()];
    		aryCreates.toArray(results[0]);
    	}
       	if( aryUpdates.size() >0){
    		results[1] = new RepDataAuthVO[aryUpdates.size()];
    		aryUpdates.toArray(results[1]);
    	} 
       	return results;
	}

}
