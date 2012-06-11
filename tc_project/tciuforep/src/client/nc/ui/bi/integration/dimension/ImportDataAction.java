/**
 * Action1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nc.bs.logging.Log;
import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.ui.iufo.exproperty.ExPropException;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.us.bi.query.manager.QuerySrv;
import nc.util.iufo.pub.IDMaker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimFldContrastSrv;
import nc.vo.bi.integration.dimension.DimFldcontrastVO;
import nc.vo.bi.integration.dimension.DimMemberMapVO;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.freequery.BIMultiDataSet;
import nc.vo.pub.querymodel.DatasetUtil;

import com.borland.dx.dataset.StorageDataSet;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.Treeable;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * �������������� ll 2006-01-17
 */
public class ImportDataAction extends DialogAction {	
	private class SpliterNode extends DefaultMutableTreeNode{
		private static final long serialVersionUID = -7735572956995320359L;
		private Object pk = null;
		private Object parentPK = null;
		
		public Object getParentPK() {
			return parentPK;
		}
		public void setParentPK(Object parentPK) {
			this.parentPK = parentPK;
		}
		public Object getPk() {
			return pk;
		}
		public void setPk(Object pk) {
			this.pk = pk;
		}
		
	}
	//���ֳ�ͻʱֹͣ
	public static final int CONPFLICT_DEAL_TYPE_STOP = 1;
	//���ֳ�ͻʱֻȡ��һ����¼
	public static final int CONPFLICT_DEAL_TYPE_FIRST_RECORD = 2;
	
	public static final String METHOD_IMPORT_DATA = "importData";

	public static final String SPLITER_RULE_TYPE = "spliterRuleType";
	public static final String METHOD_IMPORT_DATE_SUBMIT = "importDataSubmit";

	public static final String METHOD_IMPORT_DESIGN = "importDesign";
	public static final String METHOD_IMPORT_DESIGN_BACK= "importDesign_back";
	public static final String METHOD_IMPORT_DESIGN_NEXT = "importDesign_next";

	public static final String METHOD_IMPORT_DESIGN_SUBMIT = "importDesignSubmit";

	public static final String KEY_SEL_QUERYMODEL_ID = "sel_querymodel_id";

	private static final String split = "-";

	private static final String ID_IMPORT_FLD = "importfld";

	private static final String ID_SPLIT_RULE = "splitrule";
	
	/**
	 * @i18n mbidim00012=�ϼ��ֶ�
	 */
	private static final String level_parentPKLabel = StringResource.getStringResource("mbidim00012");
	/**
	 * @i18n mbidim00013=�¼��ֶ�
	 */
	private static final String level_PKLabel = StringResource.getStringResource("mbidim00013");
	
	

	/**
	 * <MethodDescription> ll 2006-01-17
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {
		ActionForward actionForward = new ActionForward("");
		return actionForward;
	}

	public ActionForward importData(ActionForm actionForm) throws WebException {
		ActionForward actionForward = DimUIToolKit.checkDimRefer(this);
		if( actionForward == null ){
			AuthUIBizHelper.checkRight(getTableSelectedID(),this);
			String dimID = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());		
			((ImportDataForm) actionForm).setDimID(dimID);		
			actionForward = new ActionForward(ImportDataDlg.class.getName());
		}
		return actionForward;
	}

	/**
	 * @i18n mbidim00014=����ɹ�
	 */
	public ActionForward importDataSubmit(ActionForm actionForm) throws WebException {
		String[] errs = null;		
		ArrayList<String> messageList = new ArrayList<String>();
		try {
			errs = importMemeberVO(messageList, getForm(actionForm));
		} catch (Exception e) {			
			proeceeException(e);
			errs = new String[]{e.getMessage()};
		}
		if(errs != null){
			return new ErrorForward(errs);
		}
		ActionForward actionForward = null;
		if((messageList.isEmpty() == false)&& (getForm(actionForm).getConflictDealType() == CONPFLICT_DEAL_TYPE_FIRST_RECORD)){
			messageList.add(0,StringResource.getStringResource("mbidim00014"));
			actionForward = new MessageForward((String[]) messageList.toArray(new String[0]));
		}else{
			actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		}
		
		return actionForward;

	}
	
	private void proeceeException(Exception e){
		Log.getInstance(this.getClass().getName()).error(e);
		AppDebug.debug(e);
	}
	public ActionForward importDesign_back(ActionForm actionForm) throws WebException {
		ActionForward actionForward = null;		
		ImportDataForm form = getForm(actionForm);
		String dimID = getRequestParameter("btnPrePageDimID");
		DimensionVO dimVO = null;
		try {
			dimVO = (DimensionVO) DimensionSrv.getByID(dimID);				
		} catch (Exception e) {			
		}			
		//form.setQuery_model(loadTreeModel());	
		form.setDimID(dimID);		
		
		DimFldContrastSrv srv = new DimFldContrastSrv(dimVO);
		form.setSpliterRuleType(String.valueOf(srv.getSpliterRuleType()));
//		 ��ù����Ĳ�ѯ		
		
		if(dimVO.getPk_queryid() != null) {
			form.setQueryID(dimVO.getPk_queryid());
			try {
				QueryModelVO vo = QuerySrv.getInstance().getQueryModelVO(form.getQueryID());
				form.setOldQueryName(vo.getName());
			} catch (UISrvException e) {			
				
			}
		}
		actionForward = new ActionForward(ImportDesign1Dlg.class.getName());
		
		return actionForward;
	}
	
	public ActionForward importDesign(ActionForm actionForm) throws WebException {
		try{
			(new CodeQueryCreater()).createAllNeedQuerys();
		}catch(Exception ex){
			AppDebug.debug(ex);
			return new MessageForward(ex.getMessage());
		}
		ActionForward actionForward = DimUIToolKit.checkDimRefer(this);
		if( actionForward == null ){
			AuthUIBizHelper.checkRight(getTableSelectedID(),this);
			ImportDataForm form = getForm(actionForm);
			String dimID = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
			DimensionVO dimVO = null;
			try {
				dimVO = (DimensionVO) DimensionSrv.getByID(dimID);			
			} catch (Exception e) {			
			}
			
			form.setQuery_model(loadTreeModel());
			
			form.setDimID(dimID);
			
			DimFldContrastSrv srv = new DimFldContrastSrv(dimVO);
			form.setSpliterRuleType(String.valueOf(srv.getSpliterRuleType()));
			
	//		 ��ù����Ĳ�ѯ
			
			
			if(dimVO.getPk_queryid() != null) {
				form.setQueryID(dimVO.getPk_queryid());
				try {
					QueryModelVO vo = QuerySrv.getInstance().getQueryModelVO(form.getQueryID());
					if(vo != null)
						form.setOldQueryName(vo.getName());
				} catch (UISrvException e) {			
					
				}
			}
			actionForward = new ActionForward(ImportDesign1Dlg.class.getName());
		}
		return actionForward;
	}

	public ActionForward importDesign_next(ActionForm actionForm) throws WebException {
		ImportDataForm form = getForm(actionForm);
		//form.setQueryID(getRequestParameter("queryID"));
		form.setTableModel(loadTableModel(form));		
		ActionForward actionForward = new ActionForward(ImportDesign2Dlg.class.getName());
		return actionForward;
	}

	public ActionForward importDesignSubmit(ActionForm actionForm) throws WebException {
		ImportDataForm form = getForm(actionForm);
		//�����ѯ����
		saveQuery(form);
		
		// �����µ��ֶζ���
		DimFldContrastSrv srvContrast = new DimFldContrastSrv(form.getDimID());
		DimFldcontrastVO[] vos = getSubmitDimFldcontrasts(form);
		srvContrast.create(vos);

		ActionForward actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		return actionForward;
	}

	private ImportDataForm getForm(ActionForm actionForm) {
		ImportDataForm form = (ImportDataForm) actionForm;
		return form;

	}

	private WebTreeModel loadTreeModel() {
		try {
			Treeable[] vos = (Treeable[]) QuerySrv.getInstance().getAllFiles();
			return new WebTreeModel(vos);
		} catch (UISrvException ex) {
			proeceeException(ex);
			throw new DimensionException(ex.getMessage());
		}

	}

	/**
	 * �����ѯ
	 * @i18n mbidim00015=ά�ȶ��屣��ʧ��,
	 */
	private void saveQuery(ImportDataForm form) {
		String pkQry = form.getQueryID();

		if (pkQry == null || pkQry.length() == 0)
			throw new DimensionException(DimensionException.ERR_NO_SELECTED_OBJ);
		DimensionVO voDimDef = (DimensionVO) DimensionSrv.getByID(form.getDimID());
		if( voDimDef != null ){
			voDimDef = (DimensionVO)voDimDef.clone();
			voDimDef.setPk_queryid(pkQry);

			try {
				DimensionSrv.update(new DimensionVO[] { voDimDef });
			} catch (Exception ex) {
				proeceeException(ex);
				throw new WebException(StringResource.getStringResource("mbidim00015") + ex.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private DimFldcontrastVO[] getSubmitDimFldcontrasts(ImportDataForm form){
		String[] selectedRows = getTableSelectedIDs();
		if (selectedRows == null || selectedRows.length == 0)
			throw new DimensionException(DimensionException.ERR_NO_SELECTED_OBJ);

		Vector v = new Vector();
		for (int i = 0; i < selectedRows.length; i++) {
			// �к�
			String attrfld = selectedRows[i];
			if(attrfld.indexOf(split, 0)<0)
				throw new DimensionException(StringResource.getStringResource("uiufo20218"));

			int row = Integer.parseInt(attrfld.substring(0, attrfld.indexOf(split, 0)));
			attrfld = attrfld.substring(attrfld.indexOf(split, 0) + 1);
//			String attrfldName = attrfld.substring(attrfld.indexOf(split, 0) + 1);
			attrfld = attrfld.substring(0, attrfld.indexOf(split, 0));

			// �����ֶ�
			String choiceValue = getRequestParameter(ID_IMPORT_FLD + row);
			DimFldcontrastVO vo = new DimFldcontrastVO();
			vo.setAttrfld(attrfld);
		//	vo.setAttrfldname(attrfldName);

			vo.setImportfld(choiceValue);
			// ��ֹ���
			String txtValue = getRequestParameter(ID_SPLIT_RULE + row);
			vo.setSplitrule(txtValue);
			// ����ֶ�
			// txtfld = (WebTextField) getTableModel().getValueAt(row,
			// colSplitFld);
			// m_vosContrast[row-1].setSplitfield(txtfld.getText());

			vo.setDimid(form.getDimID());
			v.add(vo);
		}

		DimFldcontrastVO[] voContrast = new DimFldcontrastVO[v.size()];
		v.copyInto(voContrast);
		return voContrast;
	}
	// �������Ĳ�ѯ�ֶ��б�
	private String[][] getImportFieldItems(String queryID) {
		// ���ݲ�ѯID��ò�ѯ���ֶ�
//		MetaDataVO[] sfs = QueryModelSrv.getDimFlds(queryID);
		MetaDataVO[] sfs = QueryModelSrv.getSelectFlds(queryID);
		int iLen = sfs == null || sfs.length == 0 ? 0 : sfs.length;
		String[][] items = new String[iLen + 1][2];

		items[0][0] = "";
		items[0][1] = "";
		for (int i = 0; i < iLen; i++) {
			items[i + 1][0] = sfs[i].getFldalias();
			items[i + 1][1] = sfs[i].getFldname();
		}

		return items;
	}

	@SuppressWarnings("unchecked")
	private WebTableModel loadTableModel(ImportDataForm form) {	
		Vector v = new Vector();
		DimFldcontrastVO voContrast = null;
		DimFldcontrastVO[] m_vosContrast = null;
		
		IExPropOperator exPropOper =ExPropOperator.getExPropOper(IBIExPropConstants.EXPROP_MODULE_DIMENSION);

		ExPropertyVO[] allExp = null;
		try {
			allExp = exPropOper.loadAllExProp(form.getDimID());
		} catch (ExPropException e) {			
			proeceeException(e);
		}
		int iLen = allExp == null || allExp.length == 0 ? 0 : allExp.length;
		
		
		// �Զ����ֶζ���
		DimFldcontrastVO[] existContrastVOs = new DimFldcontrastVO[0];
		boolean isUsedOldQuery = false; //�Ƿ�ʹ�����²�ѯ
		
		try {
			DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(form.getDimID());
			if(form.getQueryID().equals(dimVO.getPk_queryid())) isUsedOldQuery = true;
			DimFldContrastSrv srvContrast = new DimFldContrastSrv(dimVO);
			existContrastVOs = (DimFldcontrastVO[]) srvContrast.getAll();
		} catch (Exception e) {			
			proeceeException(e);
		}		
		boolean[] selectedFld = new boolean[iLen]; 
		for (int i = 0; i < iLen; i++) {
			voContrast = null;
			if(isUsedOldQuery){
				for(int j = 0; j < existContrastVOs.length; j++){
					if(allExp[i].getDBColumnName().equals(existContrastVOs[j].getAttrfld())){
						voContrast = existContrastVOs[j];						
						selectedFld[i] = true;
						break;
					}
				}
			}			
			if(voContrast == null){
				voContrast = new DimFldcontrastVO();
				voContrast.setAttrfld(allExp[i].getDBColumnName());				
			}			
			voContrast.setAttrfldname(allExp[i].getName());
			v.add(voContrast);
		}

		m_vosContrast = new DimFldcontrastVO[v.size()];
		v.copyInto(m_vosContrast);
		
		boolean isSplilterByLevel = false;
		String str = String.valueOf(DimFldcontrastVO.SPLITER_BY_LEVEL);
		if(str.equalsIgnoreCase(form.getSpliterRuleType())) isSplilterByLevel = true;
		
		WebTableModel m_tableModel = new WebTableModel();
		for (int i = 0; i < m_vosContrast.length; i++) {
			Object[] row = new Object[4];
			row[0] = i + split + m_vosContrast[i].getAttrfld() + split + m_vosContrast[i].getAttrfldname();// �к�+attrfld+attrfldname
			row[1] = m_vosContrast[i].getAttrfldname();
			
			WebChoice importfld = new WebChoice();
			importfld.setID(ID_IMPORT_FLD + i);
			importfld.setName(ID_IMPORT_FLD + i);
			importfld.setItems(getImportFieldItems(form.getQueryID()));
			importfld.setValue(m_vosContrast[i].getImportfld());
			row[2] = importfld;

			if(isSplilterByLevel){
				WebChoice tfSplitRule = new WebChoice();
				tfSplitRule.setID(ID_SPLIT_RULE + i);
				tfSplitRule.setName(ID_SPLIT_RULE + i);
				tfSplitRule.setItems(getSpliterByLevelItems());
				String rule = m_vosContrast[i].getSplitrule();
				if(rule == null) rule = "";
				tfSplitRule.setValue(rule);
				row[3] = tfSplitRule;
			}else{
				WebTextField tfSplitRule = new WebTextField();
				tfSplitRule.setID(ID_SPLIT_RULE + i);
				tfSplitRule.setName(ID_SPLIT_RULE + i);
				tfSplitRule.setVld_esc_chars("+");
				String rule = m_vosContrast[i].getSplitrule();
				if(DimFldcontrastVO.SPLITER_PARENT_PK.equalsIgnoreCase(rule) || DimFldcontrastVO.SPLITER_PK.equalsIgnoreCase(rule)){
					rule ="";
				}
				tfSplitRule.setValue(rule);
				row[3] = tfSplitRule;
			}
			
			m_tableModel.addRow(row);
		}
		m_tableModel.setSelecteds(selectedFld);
		String[] columns = new String[] { DimFldcontrastVO.ATTRFLD, DimFldcontrastVO.IMPTFLD,
				DimFldcontrastVO.SPLITRULE };
		m_tableModel.setColumns(columns);

		return m_tableModel;
	}

	/**
	 * @i18n miufopublic358=��
	 */
	private String[][] getSpliterByLevelItems(){
		String[][] items = new String[3][2];
		items[0][0] = "";
		items[0][1] = StringResource.getStringResource("miufopublic358");		
		
		items[1][0] = DimFldcontrastVO.SPLITER_PARENT_PK;
		items[1][1] = level_parentPKLabel;
		
		items[2][0] = DimFldcontrastVO.SPLITER_PK;
		items[2][1] = level_PKLabel;
		
		return items;
	}
	/**
	 * @i18n mbidim00016=û��ѡ��γ��
	 * @i18n mbidim00017=��ǰά��û�й�����ѯ
	 * @i18n mbidim00018=��ǰά��û�ж��嵼���ֶ�
	 * @i18n mbidim00019=��ȡ��ѯ��¼ʧ��
	 * @i18n mbidim00020=����ά�ȳ�Աʧ�ܣ�
	 */
	@SuppressWarnings("unchecked")
	private String[] importMemeberVO(ArrayList messageList, ImportDataForm form) {

		// ���ѡ�е�ά��
		String pkDimDef = form.getDimID();		
		if (pkDimDef == null || pkDimDef.length() == 0)
			return new String[]{StringResource.getStringResource("mbidim00016")};

		// ά�ȶ���
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(pkDimDef);

		// ���ά�����¼
		DimMemberSrv srvMember = new DimMemberSrv(dimVO);
		DimMemberVO voRoot = srvMember.getRoot();

		// ��ù����Ĳ�ѯ
		String pkQuery = dimVO.getPk_queryid();
		if (pkQuery == null || pkQuery.length() == 0)
			return new String[]{StringResource.getStringResource("mbidim00017")};

		// ��ö����ֶ�
		DimFldContrastSrv srvContrast = new DimFldContrastSrv(dimVO);
		DimFldcontrastVO[] vosContrast = (DimFldcontrastVO[]) srvContrast.getAll();
		if (vosContrast == null || vosContrast.length == 0)
			return new String[]{StringResource.getStringResource("mbidim00018")};

		// ���ݲ�ѯ������ò�ѯ��������ﻯ��
		BIMultiDataSet mds = QueryModelSrv.getMaterQueryResult(pkQuery, null);		
		if (mds == null || mds.getDataSet() == null || mds.getDataSet().getRowCount() == 0)
			return new String[]{StringResource.getStringResource("mbidim00019")};
		
		StorageDataSet sds = mds.getDataSet();
		boolean isReplace = false;
		if (form.getImportType() != null && form.getImportType().equals(ImportDataForm.RB_VALUE_REPLACE)){
			isReplace = true;
		}		
		
		ArrayList<DimMemberVO> memList = new ArrayList<DimMemberVO>();
		String[] errors = null;
	
		
		
		if(srvContrast.getSpliterRuleType() == DimFldcontrastVO.SPLITER_BY_LEVEL){			
			errors = importByLevel(isReplace, form.getConflictDealType(), messageList, memList, sds, vosContrast, dimVO, voRoot, srvMember);			
		}else{//���뵹��
			errors =  importByCode(isReplace, form.getConflictDealType(), messageList, memList, sds, vosContrast, dimVO, voRoot, srvMember);	
		}	
		
		if(errors != null) return errors;
		DimMemberVO[] vosMember = (DimMemberVO[])memList.toArray(new DimMemberVO[0]);		
		for(int i = 0; i < vosMember.length; i++){
			if(vosMember[i].getCalattr() == null){
				vosMember[i].setCalattr(DimRescource.INT_CACLRULE_ADD);
			}
		}
		

//		���ڲ����ֶΣ������Ϊ����룬��Ҫ��ID�滻
		ExPropertyVO[] expVOs = null;
		try {
			expVOs = MemberDesignAction.getAllInputFormalExPropVOs(dimVO.getDimID());
		} catch (Exception e) {			
			AppDebug.debug(e);
		}
		if((expVOs != null) && (expVOs.length > 0)){
			for(int i = 0; i < expVOs.length; i++){
				try {
					if((expVOs[i].getRefTypePK() != null) && (expVOs[i].getRefTypePK().trim().length() > 0)){
						DimMemberSrv memSrv = null;
						DimensionVO refDimVO = (DimensionVO) DimensionSrv.getByID(expVOs[i].getRefTypePK());	
						if(refDimVO != null){
							memSrv = new DimMemberSrv(refDimVO);
						}
						if(memSrv != null){	
							HashMap codeMapID = new HashMap();
							DimMemberVO[] refVOs = memSrv.getAll();						
							for(int k = 0; k < refVOs.length; k++){
								if(refVOs[k] == null) continue;
								codeMapID.put(refVOs[k].getMemcode(), refVOs[k].getMemberID());
							}
							if(codeMapID.isEmpty()) continue;
							for(int k = 0; k < vosMember.length; k++){
								String str = vosMember[k].getPropValue(expVOs[i].getDBColumnName());
								if(str== null || str.trim().length() == 0) continue;							
								vosMember[k].setExPropValue(expVOs[i].getDBColumnName(), (String)codeMapID.get(str));
							}
						}
					}
				} catch (Exception e) {					
					AppDebug.debug(e);
				}
			}
		}
		
		
		
		try {
			// ��õ��뷽ʽ��׷��/�滻
			if(isReplace){
				srvMember.removeAll();
			}	
			// ���ݵ���
			srvMember.create(vosMember);
		} catch (Exception ex) {
			proeceeException(ex);
			return new String[]{StringResource.getStringResource("mbidim00020")};
		}		
		return null;
	}
	
//	��ε���
	/**
	 * @i18n mbidim00021=�Ƿ��Ĳ�ι�ϵ
	 * @i18n mbidim00022=��ȡ�����ֶ���Ϣ����
	 */
	@SuppressWarnings("unchecked")
	private String[] importByLevel(boolean isReplace, int conflictDealType, ArrayList messageList, ArrayList<DimMemberVO> importedList, StorageDataSet sds, DimFldcontrastVO[] vosContrast, DimensionVO dimVO, DimMemberVO voRoot, DimMemberSrv srvMember){
		//��鸸�ӹ�ϵ�Ƿ���ȷ
		int parentPK = 0,pk = 0;		
		for(int i = 0; i < vosContrast.length; i++){
			if(DimFldcontrastVO.SPLITER_PARENT_PK.equals(vosContrast[i].getSplitrule()))parentPK++;
			if(DimFldcontrastVO.SPLITER_PK.equals(vosContrast[i].getSplitrule()))pk++;
		}
		if((parentPK == 1) && (pk == 1)){
			
		}else{
			return new String[]{StringResource.getStringResource("mbidim00021")};
		}
					
		Hashtable map_PK2ID = new Hashtable();
		Hashtable map_ID2Node = new Hashtable();	
		ArrayList allNodeList = new ArrayList();
		//��ʼ��memberVO
		while (sds.inBounds()) {
			DimMemberMapVO memberVO = new DimMemberMapVO();			
			memberVO.setPk_member(IDMaker.makeID(20));			
			SpliterNode node = new SpliterNode();				
			allNodeList.add(node);
			map_ID2Node.put(memberVO.getPk_member(), node);
			
			for (int i = 0; i < vosContrast.length; i++) {
				// �����ֶ�
				String colKey = vosContrast[i].getImportfld();
				if (colKey == null || colKey.trim().length() == 0)continue;
				// �����������
				int iKeyType = sds.getColumn(colKey).getDataType();
				// ���۵�����
				iKeyType = BIModelUtil.variantTypeToSqlType(iKeyType);
				// ���ָ����ֵ
				Object objKey = DatasetUtil.fetchDataRow(sds, colKey, iKeyType);
				// ����ָ����
			//	System.out.println(colKey);
				// ��Ա�������ֶ�
				String attrField = vosContrast[i].getAttrfld();

				// �������������
				Class clsMember = DimMemberMapVO.class;
				Class clsField = null;
				try {					
					clsField = clsMember.getDeclaredField(attrField).getType();
					if (clsField.newInstance() instanceof String){
						objKey = String.valueOf(objKey);
					}else if(clsField.newInstance() instanceof Integer){
						objKey = Integer.valueOf(objKey.toString());
					}
					// ���ó�Ա��ֵ
					String methodName = "set" + attrField.substring(0, 1).toUpperCase() + attrField.substring(1);
					DimMemberMapVO.class.getMethod(methodName, new Class[] { objKey.getClass()}).invoke(memberVO,
							new Object[] { objKey });	
				} catch (Exception e) {
					proeceeException(e);
					return new String[]{StringResource.getStringResource("mbidim00022")};
				}

				// ��ֹ���				
				if (DimFldcontrastVO.SPLITER_PARENT_PK.equals(vosContrast[i].getSplitrule())) {// ���ֶ�	
					node.setParentPK(objKey);
				}
				if (DimFldcontrastVO.SPLITER_PK.equals(vosContrast[i].getSplitrule())) {// ���ֶ�	
					node.setPk(objKey);
					if(objKey != null)map_PK2ID.put(objKey, memberVO.getPk_member());
				}	
			}	
			memberVO.validataFieldLength();//���Ӷ����ֶγ��ȵ���Ч��У�������
			node.setUserObject(memberVO.toMemberVO());
			// �����α�
			sds.next();			
		}		
			
//		������ƺͱ����Ƿ�����ظ�		
		ArrayList validNodeList = new ArrayList();
		checkOverlapNameOrCode(conflictDealType, messageList, validNodeList, (SpliterNode[])allNodeList.toArray(new SpliterNode[0]), isReplace, srvMember, voRoot);		
		if((messageList.isEmpty() == false) && (conflictDealType == CONPFLICT_DEAL_TYPE_STOP)) {
			return (String[]) messageList.toArray(new String[0]);
		}
		
		//����������
		SpliterNode rootNode = new SpliterNode();		
		rootNode.setUserObject(voRoot);
//		DefaultTreeModel tree = new DefaultTreeModel(rootNode);	
		 Iterator it = validNodeList.iterator();		 
		 while(it.hasNext()){
			 SpliterNode node = (SpliterNode)it.next();
			 SpliterNode parentNode = rootNode;			 
			 try {
				Object parentID = map_PK2ID.get(node.getParentPK());
				if(validNodeList.contains(map_ID2Node.get(parentID))){
					parentNode = (SpliterNode) map_ID2Node.get(parentID);
				}		
			} catch (Exception e) {				
				parentNode = rootNode;				
			}
			node.setParent(parentNode);
		 }
		 
		 //��伶����Ϣ		 
		 return setLevelsFromTree(rootNode, validNodeList, importedList, dimVO);
//		 
//		 it = validNodeList.iterator();		 
//		 while(it.hasNext()){
//			 SpliterNode node = (SpliterNode)it.next();
//			 TreeNode[] path = tree.getPathToRoot(node);
//			 DimMemberMapVO memberVO = (DimMemberMapVO)node.getUserObject();
//			 for(int i = 0; i < path.length; i++){				 
//				 if(i  >= DimRescource.INT_MAX_FLDPRE_NUMBER-1){
//					 return  new String[]{"�ڵ�" + memberVO.getMemname() +  "������󼶴���������ʧ��"};
//				 }
//				 SpliterNode nd = (SpliterNode) path[i];
//				 DimMemberMapVO vo = (DimMemberMapVO)nd.getUserObject();				 
//	 			 memberVO.setLvlByDepth(vo.getPk_member(), i);
//			//	 memberVO.setCalattrByDepth(new Integer(1), i);
//			 }					
//			memberVO.setDepth(new Integer(path.length-1));			// ����
//		//	memberVO.setCalattr(new Integer(1));
//			memberVO.setTableName(dimVO.getTablename());			// ���ñ���	
//			importedList.add(memberVO.toMemberVO());
//		 }
//		return null;
	}
	/**
	 * @i18n mbidim00023=�ڵ�
	 * @i18n mbidim00024=������󼶴���������ʧ��
	 */
	private String[] setLevelsFromTree(SpliterNode rootNode, ArrayList validNodeList, ArrayList<DimMemberVO> importedList, DimensionVO dimVO){		
		DefaultTreeModel tree = new DefaultTreeModel(rootNode);	
		 Iterator it = validNodeList.iterator();
		 while(it.hasNext()){
			 SpliterNode node = (SpliterNode)it.next();
			 DimMemberVO memVO = (DimMemberVO)node.getUserObject();
			 TreeNode[] path = tree.getPathToRoot(node);
			 if(path.length  >= DimRescource.INT_MAX_FLDPRE_NUMBER){
				 return  new String[]{StringResource.getStringResource("mbidim00023") + memVO.getMemname() +  StringResource.getStringResource("mbidim00024")};
			 }
			 String[] levels = new String[memVO.getLevels().length];
			 for(int i = 0; i < path.length; i++){
				 SpliterNode nd = (SpliterNode) path[i];
				 levels[i] = ((DimMemberVO)nd.getUserObject()).getMemberID();							
			 }	
			 memVO.setLevels(levels);
			 memVO.setDepth(path.length-1);
			 memVO.setTableName(dimVO.getTablename());			// ���ñ���	
			 importedList.add(memVO);
		 }
		 return null;
	}
	
	/**
	 * ���뵼��
	 * @i18n mbidim00022=��ȡ�����ֶ���Ϣ����
	 */
	@SuppressWarnings("unchecked")
	private String[] importByCode(boolean isReplace, int conflictDealType, ArrayList messageList, ArrayList importedList, StorageDataSet sds, DimFldcontrastVO[] vosContrast, DimensionVO dimVO, DimMemberVO voRoot, DimMemberSrv srvMember){		
		// vo������
		int voCount = 0;		
		HashMap map_value2Node = new HashMap(); //��ֵĽڵ�
		ArrayList allNodeList = new ArrayList();		
		while (sds.inBounds()) {
			DimMemberMapVO memberVO = new DimMemberMapVO();
			memberVO.setPk_member(IDMaker.makeID(20));	
			SpliterNode node = new SpliterNode();	
			allNodeList.add(node);						
			boolean isSplit = false;// �Ƿ񱻲��			
			int iLvlCount = 0;// ���μ�����
			for (int i = 0; i < vosContrast.length; i++) {
				// �����ֶ�
				String colKey = vosContrast[i].getImportfld();
				if (colKey == null || colKey.trim().length() == 0)
					continue;
				// �����������
				int iKeyType = sds.getColumn(colKey).getDataType();
				// ���۵�����
				iKeyType = BIModelUtil.variantTypeToSqlType(iKeyType);
				// ���ָ����ֵ
				Object objKey = DatasetUtil.fetchDataRow(sds, colKey, iKeyType);
				// ����ָ����
			//	System.out.println(colKey);
				// ��Ա�������ֶ�
				String attrField = vosContrast[i].getAttrfld();

				// �������������
				Class clsMember = DimMemberMapVO.class;
				Class clsField = null;
				try {					
					clsField = clsMember.getDeclaredField(attrField).getType();
					if (clsField.newInstance() instanceof String){
						objKey = String.valueOf(objKey);
					}else if(clsField.newInstance() instanceof Integer){
						objKey = Integer.valueOf(objKey.toString());
					}
						
					// ���ó�Ա��ֵ
					String methodName = "set" + attrField.substring(0, 1).toUpperCase() + attrField.substring(1);
					DimMemberMapVO.class.getMethod(methodName, new Class[] { objKey.getClass()}).invoke(memberVO,
							new Object[] { objKey });	
				} catch (Exception e) {
					proeceeException(e);
					return new String[]{StringResource.getStringResource("mbidim00022")};
				}

				// ��ֹ���
				String splitrule = vosContrast[i].getSplitrule();
				if (splitrule != null && splitrule.trim().length() != 0) {	
					objKey = objKey.toString().trim();
					if( map_value2Node.get(objKey) == null ){
						map_value2Node.put(objKey, node);
					}
					StringTokenizer stRule = new StringTokenizer(splitrule, "-");
					String splitlen = null;
					String code = null;					
					int end = 0;					
					while ((stRule.hasMoreTokens())) {
						splitlen = stRule.nextToken();						
						end = end + Integer.parseInt(splitlen);
						if (end < String.valueOf(objKey).length()){
							code = String.valueOf(objKey).substring(0, end);
						}else{
							break;
						}
						isSplit = true;
						iLvlCount++;
						
						memberVO.setLvlByDepth(code,iLvlCount);
				//		memberVO.setCalattrByDepth(new Integer(1),iLvlCount);
					}
				}
			}
			if (!isSplit) {// ���û�в�֣����г�ԱΪһ��//	
				iLvlCount++;
			}
			memberVO.validataFieldLength();//���Ӷ����ֶγ��ȵ���Ч��У�������
			memberVO.setDepth(voRoot.getLevels().length-1);
		//	memberVO.setDepth(new Integer(iLvlCount));	
			node.setUserObject(memberVO.toMemberVO());
			
			// �����α�
			sds.next();
			voCount++;
		}
		
//		������ƺͱ����Ƿ�����ظ�		
		ArrayList validNodeList = new ArrayList();
		checkOverlapNameOrCode(conflictDealType, messageList, validNodeList, (SpliterNode[])allNodeList.toArray(new SpliterNode[0]), isReplace, srvMember, voRoot);		
		if((messageList.isEmpty() == false) && (conflictDealType == CONPFLICT_DEAL_TYPE_STOP)) {
			return (String[]) messageList.toArray(new String[0]);
		}
		 
//		����������		
		SpliterNode rootNode = new SpliterNode();
		DimMemberMapVO root = new DimMemberMapVO();
		root.setPk_member(voRoot.getMemberID());		
		rootNode.setUserObject(voRoot);
		
		Iterator it = validNodeList.iterator();		 
		 while(it.hasNext()){
			 SpliterNode node = (SpliterNode)it.next();
			 node.setParent(rootNode);
			 DimMemberVO memVO = (DimMemberVO)node.getUserObject();
			 String[] levels = memVO.getLevels();			
			 for(int i = levels.length-1; i >=0; i--){
				 if((levels[i] != null) && (map_value2Node.get(levels[i]) != null)){
					 SpliterNode parentNode = (SpliterNode) map_value2Node.get(levels[i]);
					 if(validNodeList.contains(parentNode)){ //ȷ�����ڵ�û����Ϊ��ͻ��������
						 node.setParent(parentNode);
						 break;
					 }					
				 }				 
			 }					 
		 }
//		��伶����Ϣ		 
		 return setLevelsFromTree(rootNode, validNodeList, importedList, dimVO);
	}
	

	/**
	 * ������ƺͱ����Ƿ�����ظ�
	 * @param vosMember
	 * @param isReplace
	 * @param srvMember
	 * @return
	 * @i18n mbidim00025=�³�Ա�����ظ�
	 * @i18n mbidim00026=�³�Ա�����������г�Ա�����ظ�
	 * @i18n mbidim00027=�³�Ա�����ظ�
	 * @i18n mbidim00028=�³�Ա���������г�Ա�����ظ�
	 * @i18n mbidim00029= ��
	 * @i18n mbidim00030=���ظ���¼�������ظ����£�
	 */
	@SuppressWarnings("unchecked")
	private void checkOverlapNameOrCode(int conflictDealType, ArrayList messageList, ArrayList validNodeList, SpliterNode[] nodes, boolean isReplace, DimMemberSrv srvMember, DimMemberVO voRoot){
		ArrayList[] errLists = {new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};	
		String[] errors = new String[]{StringResource.getStringResource("mbidim00025"), StringResource.getStringResource("mbidim00026"), StringResource.getStringResource("mbidim00027"), StringResource.getStringResource("mbidim00028")};
		ArrayList nameList = new ArrayList();
		ArrayList codeList = new ArrayList();		
		nameList.add(voRoot.getMemname());
		codeList.add(voRoot.getMemcode());
		
		for(int i = 0; i <nodes.length; i++){				
			DimMemberVO memVO = (DimMemberVO)nodes[i].getUserObject();
			String name = memVO.getMemname();
			String code = memVO.getMemcode();				
			boolean unique = true;
			if(nameList.contains(name)){
				errLists[0].add(name);
				unique = false;				
			}else if((isReplace == false)&&(srvMember.isMemberNameExist(name))){
				errLists[1].add(name);	
				unique = false;
			}else{
				nameList.add(name);				
			}			
			if(codeList.contains(code)){
				errLists[2].add(code);				
				unique = false;
			}else if((isReplace == false)&&(srvMember.isMemberCodeExist(code))){
				errLists[3].add(code);				
				unique = false;
			}else{
				codeList.add(code);
			}	
			
			if(unique)validNodeList.add(nodes[i]);
		}		
		for(int i = 0; i < errLists.length; i++){
			if(errLists[i].isEmpty() == false){
				StringBuffer sb = new StringBuffer();
				sb.append(errors[i] + StringResource.getStringResource("mbidim00029") + errLists[i].size()+ StringResource.getStringResource("mbidim00030") );
				Iterator it = errLists[i].iterator();
				while((sb.length() < 100) && (it.hasNext())){					
					sb.append((String)it.next());
					sb.append(",");
				}
				sb.append(" ...");				
				messageList.add(sb.toString());
			}
		}			
	}
	/**
	 * FormֵУ��
	 * 
	 * @param actionForm
	 * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
	 * @i18n mbidim00031=��ֹ�������:ȱ��
	 * @i18n miufo1000932=����
	 * @i18n mbidim00032=ֻ����һ����ֹ���
	 * @i18n mbidim00033=��ֹ����зǷ��ַ�:��ֹ���ֻ�������ֺ��ַ�'-'��ɣ���'1-2-2'
	 * @i18n mbidim00034=��ֹ���
	 * @i18n mbidim00035=������󼶴���
	 * @i18n mbidim00036=û��ѡ���ѯ����ѡ��Ĳ�ѯû���κ��ֶ�
	 */
	@SuppressWarnings("unchecked")
	public String[] validate(ActionForm actionForm) {
		ImportDataForm form = (ImportDataForm)actionForm;
		if(METHOD_IMPORT_DESIGN_NEXT.equals(form.getCurrentUIFlag())){			
			DimFldcontrastVO[] vos = null;
			try {
				vos = getSubmitDimFldcontrasts(form);
			} catch (Exception e) {				
			}
			if(vos != null){
				ArrayList errorList = new ArrayList();				
				int codenamecount = 0;
				for(DimFldcontrastVO contrast:vos){//���ά�ȵı���������ֶα�ѡ
					if(contrast.getAttrfld().equals("memcode") || contrast.getAttrfld().equals("memname")){
						if(contrast.getImportfld() != null)
						codenamecount++;
					}
				}
				if(codenamecount < 2)
					errorList.add(StringResource.getStringResource("mbidim00103"));

				if(String.valueOf(DimFldcontrastVO.SPLITER_BY_LEVEL).equalsIgnoreCase(form.getSpliterRuleType())){//��ε���
					int parentPK = 0;
					int pk = 0;
					for(int i = 0; i < vos.length; i++){
						if((vos[i].getSplitrule() != null )&&( vos[i].getSplitrule().trim().length() != 0)){
							if(DimFldcontrastVO.SPLITER_PARENT_PK.equalsIgnoreCase(vos[i].getSplitrule())) parentPK++;
							if(DimFldcontrastVO.SPLITER_PK.equalsIgnoreCase(vos[i].getSplitrule())) pk++;
						}
					}
					if((parentPK == 1) && (pk == 1)){
						
					}else{
						errorList.add(StringResource.getStringResource("mbidim00031") +level_parentPKLabel + StringResource.getStringResource("miufo1000932") + level_PKLabel);
					}
					
				}else{
					String splitrule = null;  
					for(int i = 0; i < vos.length; i++){
						if((vos[i].getSplitrule() != null )&&( vos[i].getSplitrule().trim().length() != 0)){
							if(splitrule == null){
								splitrule = vos[i].getSplitrule();
							}else{
								return new String[]{StringResource.getStringResource("mbidim00032")};								
							}
						}
					}
					if((splitrule != null) && (splitrule.trim().length() > 0)){
						String[] rs = splitrule.split("-");
						for(int i = 0; i < rs.length; i++){						
							try {
								Integer.parseInt(rs[i]);
							} catch (Exception e) {								
								return new String[]{StringResource.getStringResource("mbidim00033")};								
							}
						}
						
						if(rs.length >= DimRescource.INT_MAX_FLDPRE_NUMBER-1){
							return  new String[]{(StringResource.getStringResource("mbidim00034") + splitrule +  StringResource.getStringResource("mbidim00035"))};
						}
						
					}
				}
				
				
				
				if(errorList.isEmpty() == false) return (String[]) errorList.toArray(new String[0]);
			}
		}
		if(METHOD_IMPORT_DESIGN.equals(form.getCurrentUIFlag())){
			if(form.getQueryID() == null || form.getQueryID().trim().length() == 0){
				return new String[]{StringResource.getStringResource("mbidim00036")};
			}
		}
		return null;
	}

	/**
	 * ����Form
	 * 
	 */
	public String getFormName() {
		return ImportDataForm.class.getName();
	}
}
 