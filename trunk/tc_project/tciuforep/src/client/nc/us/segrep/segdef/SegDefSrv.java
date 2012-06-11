/*
 * �������� 2006-7-4
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.us.segrep.segdef;


import nc.bs.framework.common.NCLocator;
import nc.itf.segrep.segdef.IBSegDefService;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.us.bi.query.manager.QuerySrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.iufo.resmng.AbsFiled4DirSysImpl;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.ResDirectoriedImpl;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.segrep.resmng.ISRResMngConstants;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.pub.ValueObject;
import nc.vo.segrep.segdef.SegDefVO;
import nc.vo.segrep.segdef.SegRepException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * �ֲ����ֹ���ģ�����
 */
public class SegDefSrv extends AbsFiled4DirSysImpl implements IDirectoried {
	private static SegDefSrv		s_segDefSrv = new SegDefSrv();
	private SegDefSrv(){
		super(ISRResMngConstants.MODULE_SEGREP_DEF);
	}
	public static final SegDefSrv getInstance(){
		return s_segDefSrv;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiled4DirSysImpl#doGetAllFileVOs()
	 */
	protected ValueObject[] doGetAllFileVOs() throws UISrvException {
		try{
			return getBSegDefService().getAllSegDefs();
		}catch(SegRepException  e){
			handleException(e);
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiled4DirImpl#doSetParentDirVOPK(nc.vo.pub.ValueObject, java.lang.String)
	 */
	protected void doSetParentDirVOPK(ValueObject fileVO,
			String strParentDirVOPK) throws UISrvException {
		if( fileVO != null ){
			((SegDefVO)fileVO).setDirPK(strParentDirVOPK);
		}

	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#changeToFileTreeObj(nc.vo.pub.ValueObject, java.lang.String)
	 */
	protected IResTreeObject changeToFileTreeObj(ValueObject objVO,
			String strShareFlag) throws UISrvException {
		if( objVO != null ){
		
			SegDefVO	defVO = (SegDefVO)objVO;
				
			IResTreeObject resTreeObject = new ResTreeObject();
	        resTreeObject = new ResTreeObject();
	        int nObjectType = IResTreeObject.OBJECT_TYPE_FILE;
	        if( strShareFlag != null){
	        	resTreeObject.setID(ResMngToolKit.getTreeObjIDAued(defVO.getSegDefPK(), strShareFlag, nObjectType));
	        }else{
	        	resTreeObject.setID(ResMngToolKit.getHashCodeID(defVO.getSegDefPK(), nObjectType));
	        }

	        resTreeObject.setLabel(defVO.getSegDefName());
	        resTreeObject.setParentID(ResMngToolKit.getHashCodeID(defVO.getDirPK(), IResTreeObject.OBJECT_TYPE_DIR));
	        resTreeObject.setType(nObjectType);
	        resTreeObject.setSrcVO(objVO);
		        
	        return resTreeObject;
		}
        return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileDisAttr(java.lang.String, nc.vo.pub.ValueObject)
	 */
	protected Object[] doGetFileDisAttr(String strFileTreeObjID,
			ValueObject fileVO) throws UISrvException {

		if( fileVO != null ){
			SegDefVO		defVO = (SegDefVO)fileVO;
			String[]		strValues = new String[4];
			int				nIndex = 0;
			strValues[nIndex++] = strFileTreeObjID;
			strValues[nIndex++] = defVO.getSegDefName();
			//�ɼ������ƣ���ȫ·����
			QueryModelVO	queryVO = QuerySrv.getInstance().getQueryModelVO(defVO.getQueryPK());
			if( queryVO != null){
				strValues[nIndex] = queryVO.getName();
			}
			nIndex++;
				
			//�ֲ��������ƣ���ȫ·����
			try{
				ReportVO		repVO = BIReportSrv.getInstance().getByID(defVO.getSegReportPK());
				if(repVO != null ){
					strValues[nIndex++] =repVO.getName();
				}
			}catch(Exception e){
				AppDebug.debug(e);
				strValues[nIndex++] = "";
			}
			return strValues;
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doGetFileVO(java.lang.String)
	 */
	protected ValueObject doGetFileVO(String strSrcVOPK) throws UISrvException {
		try{
			return getBSegDefService().getSegDefByPK(strSrcVOPK);
		}catch(SegRepException e){
			handleException(e);
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCreateFileVO(nc.vo.pub.ValueObject)
	 */
	protected ValueObject doCreateFileVO(ValueObject toCreateVO,String strOrgPK)
			throws UISrvException {
		if( toCreateVO != null ){
			try{
				return getBSegDefService().createSegDef((SegDefVO)toCreateVO);
			}catch(SegRepException e){
				handleException(e);
			}
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCheckFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doCheckFileVO(ValueObject fileVO) throws UISrvException {
		// ���ֲ������Ƿ�����
		if( fileVO != null ){
			try{
				SegDefVO	defVO = (SegDefVO)fileVO;
				SegDefVO	oldDefVO = getBSegDefService().getSegDefByName(defVO.getDirPK(), defVO.getSegDefName());
				if( oldDefVO != null && oldDefVO.getSegDefPK().equals(defVO.getSegDefPK())==false){
					throw new UISrvException("msrdef0001");//"��ͬ���Ƶķֲ������Ѿ�����"
				}
			}catch(SegRepException e){
				handleException(e);
			}
		}

	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doUpdateFileVO(nc.vo.pub.ValueObject)
	 */
	protected void doUpdateFileVO(ValueObject fileVO,String strOrgPK) throws UISrvException {
		// �޸ķֲ�����
		if( fileVO != null ){
			try{
				getBSegDefService().updateSegDef((SegDefVO)fileVO);
			}catch(SegRepException e){
				handleException(e);
			}
		}	

	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doRemoveFileVOs(java.lang.String[])
	 */
	protected void doRemoveFileVOs(String[] strSrcVOPKs) throws UISrvException {
		// ɾ���ֲ�����
		if( strSrcVOPKs != null ){
			try{
				getBSegDefService().deleteSegDefs(strSrcVOPKs);
			}catch(SegRepException e){
				handleException(e);
			}
		}

	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.AbsFiledImpl#doCopyFileVO(java.lang.String, java.lang.String, java.lang.String)
	 */
	protected ValueObject doCopyFileVO(String strSrcVOPK,
			String strDstParentDirPK, String strOperUserPK,String strOrgPK)
			throws UISrvException {
		// TODO �Զ����ɷ������
		return null;
	}

	public IResTreeObject getDirByID(String strDirID) throws UISrvException {
		// TODO �Զ����ɷ������
		return getDefaultDirectoriedImpl().getDirByID(strDirID);
	}
	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.IDirectoried#getDirByID(java.lang.String)
	 */
	public IResTreeObject createDir(IResTreeObject dirResTreeObj)
	throws UISrvException {
		return getDefaultDirectoriedImpl().createDir(dirResTreeObj);
	}

	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#updateDir(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	*/
	public void updateDir(IResTreeObject dirResTreeObj) throws UISrvException {
	getDefaultDirectoriedImpl().updateDir(dirResTreeObj);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#removeDir(java.lang.String)
	*/
	public void removeDir(String strDirID) throws UISrvException {
	getDefaultDirectoriedImpl().removeDir(strDirID);
	
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#isDescendant(java.lang.String, java.lang.String)
	*/
	public boolean isDescendant(String strAncestorID, String strDirID)
		throws UISrvException {
	return getDefaultDirectoriedImpl().isDescendant(strAncestorID, strDirID);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#getAllSubDirs(java.lang.String)
	*/
	public IResTreeObject[] getAllSubDirs(String strDirID)
		throws UISrvException {
	return getDefaultDirectoriedImpl().getAllSubDirs(strDirID);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#copyDir(java.lang.String, java.lang.String, java.lang.String)
	*/
	public IResTreeObject copyDir(String strSrcDirID, String strDestDirID,
		String strOperUserPK) throws UISrvException {
	return getDefaultDirectoriedImpl().copyDir(strSrcDirID, strDestDirID, strOperUserPK);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#getFilesByDirID(java.lang.String)
	*/
	public IResTreeObject[] getFilesByDirID(String strDirID)
		throws UISrvException {
	return getDefaultDirectoriedImpl().getFilesByDirID(strDirID);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#copyFiles(java.lang.String[], java.lang.String, java.lang.String)
	*/
	public void copyFiles(String[] strSrcFileIDs, String strDstDirID,
		String strOperUserID,String strOrgPK) throws UISrvException {
	// TODO �Զ����ɷ������
	getDefaultDirectoriedImpl().copyFiles(strSrcFileIDs, strDstDirID, strOperUserID,strOrgPK);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#getDirsByParentID(java.lang.String)
	*/
	public IResTreeObject[] getDirsByParentID(String strParentDirID)
		throws UISrvException {
	return getDefaultDirectoriedImpl().getDirsByParentID(strParentDirID);
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#moveDir(java.lang.String, java.lang.String)
	*/
	public void moveDir(String strSrcDirID, String strDstDirID)
		throws UISrvException {
	getDefaultDirectoriedImpl().moveDir(strSrcDirID, strDstDirID);
	
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#moveFile(java.lang.String, java.lang.String)
	*/
	public void moveFile(String strSrcFileID, String strDstDirID)
		throws UISrvException {
	getDefaultDirectoriedImpl().moveFile(strSrcFileID, strDstDirID);
	
	}
	
	/* ���� Javadoc��
	* @see nc.util.iufo.resmng.IDirectoried#getFullPathName(java.lang.String, java.lang.String)
	*/
	public String getFullPathName(String strResTreeObjID, String strResOwnerPK)
		throws UISrvException {
		return getDefaultDirectoriedImpl().getFullPathName(strResTreeObjID, strResOwnerPK);
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.IFiled4Dir#getSubFileVOs(java.lang.String)
	 */
	public ValueObject[] getSubFileVOs(String strDirVOPK) throws UISrvException {
		//�õ�һ��Ŀ¼�µ����зֲ�����
		if( strDirVOPK != null ){
			try{
				return getBSegDefService().getSegDefsByDirPK(strDirVOPK);
			}catch(SegRepException e){
				handleException(e);
			}
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.IFiled4Dir#moveFileVO(java.lang.String, java.lang.String)
	 */
	public void moveFileVO(String strSrcFileVOPK, String strDstDirVOPK)
			throws UISrvException {
		//�ƶ��ֲ�����
		if(strSrcFileVOPK != null && strDstDirVOPK != null){
			try{
				//�õ�VO
				SegDefVO	defVO = getBSegDefService().getSegDefByPK(strSrcFileVOPK);
				if( defVO != null ){
					//�޸�Ŀ¼
					defVO.setDirPK(strDstDirVOPK);
					getBSegDefService().updateSegDef(defVO);
				}
			}catch(SegRepException e){
				handleException(e);
			}
		}	
	}

	/* ���� Javadoc��
	 * @see nc.util.iufo.resmng.IFiled4Dir#getRootDirDisName(java.lang.String, boolean)
	 */
	public String getRootDirDisName(String strOwerPK, boolean bSelfRoot)
			throws UISrvException {
		// ��Ŀ¼����ʾ����
		return StringResource.getStringResource("usrdef0040");//"�ֲ�����"
	}
	public SegDefVO		getSegDefVOByPK(String strSegDefPK) throws SegRepException{
		return getBSegDefService().getSegDefByPK(strSegDefPK);
	}
	public SegDefVO		createSegDefVO(SegDefVO  segDefVO) throws SegRepException{
		return getBSegDefService().createSegDef(segDefVO);
	}
	public void			updateSegDefVO(SegDefVO segDefVO) throws SegRepException{
		getBSegDefService().updateSegDef(segDefVO);
	}
	public SegDefVO		getSegDefVOByName(String strDirPK, String strName) throws SegRepException{
		if( strName != null && strDirPK != null ){
			return getBSegDefService().getSegDefByName(strDirPK, strName);
		}
		return null;
	}
	private  IBSegDefService  getBSegDefService(){
		return (IBSegDefService)NCLocator.getInstance().lookup(IBSegDefService.class.getName());
	}
	
	private IDirectoried  getDefaultDirectoriedImpl(){
		return ResDirectoriedImpl.getIDirectoried(ISRResMngConstants.MODULE_SEGREP_DEF);
	}
	private void handleException(SegRepException e) throws UISrvException{
		String[]	strParams = e.getParams();
		if( strParams == null ){
			throw new UISrvException(e.getMsgID());
		}else{
			throw new UISrvException(e.getMsgID(), strParams);
		}
	}

}
