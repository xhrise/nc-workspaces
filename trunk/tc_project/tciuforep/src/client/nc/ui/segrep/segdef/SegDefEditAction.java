/*
 * �������� 2006-7-4
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.segrep.segdef;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.segrep.segdef.IBSegRepExecService;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.query.manager.QuerySrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.us.segrep.segdef.SegDefSrv;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.segrep.segdef.SegDefScopeVO;
import nc.vo.segrep.segdef.SegDefVO;
import nc.vo.segrep.segdef.SegRepException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.WebTree2ListModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.comp.tree.WebTreeNode;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * @author zyjun
 * 
 * �ֲ����ֱ༭Action
 */
public class SegDefEditAction extends DialogAction {
	/**
	 * �½�
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward execute(ActionForm actionForm) {
		if (isCreate()) {
			return create(actionForm);
		} else {
			return update(actionForm);
		}
	}

	public ActionForward create(ActionForm actionForm) {
		// �ж�Ȩ��
		String strSelectedID = getTreeSelectedID();
		if (ResMngToolKit.isVitualRootDir(strSelectedID)) {
			throw new WebException("miuforesmng0104");// ���Ŀ¼ֻ�ܴ���Ŀ¼���ұ�����ϵͳ��ʼ����Ա
		}
		AuthUIBizHelper.checkRight(strSelectedID, this);

		SegDefEditForm form = (SegDefEditForm) actionForm;
		form.setDirPK(ResMngToolKit.getVOIDByTreeObjectID(strSelectedID));
		form.setListModel(new WebTree2ListModel());
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * �޸�
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward update(ActionForm actionForm) {
		try {
			String strSelectedID = getTableSelectedID();
			AuthUIBizHelper.checkRight(strSelectedID, this);

			SegDefEditForm form = (SegDefEditForm) actionForm;
			// �õ�ѡ�еķֲ�����ID
			String strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(strSelectedID);
			// �õ�SegDefVO
			SegDefVO segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if (segDefVO != null) {
				setVOToForm(segDefVO, form);
				return new ActionForward(SegDefEditDlg.class.getName());
			}
			return new ErrorForward(StringResource.getStringResource("msrdef0019"));// �ֲ������Ѿ�ɾ���������޸�
		} catch (Exception e) {
			return new ErrorForward(e.getMessage());
		}
	}

	/**
	 * ����
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward save(ActionForm actionForm) {
		try {
			// �ж����½������޸�
			SegDefEditForm form = (SegDefEditForm) actionForm;
			form.setOrgDimMembers(getOrgMembers());

			if (form.getSegDefPK() == null || form.getSegDefPK().length() == 0) {
				// �½�
				createSegDef(form);
			} else {
				// �޸�
				updateSegDef(form);
			}
			return new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		} catch (Exception e) {
			return new ErrorForward(e.getMessage());
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.ufida.web.action.Action#getFormName()
	 */
	public String getFormName() {
		return SegDefEditForm.class.getName();
	}

	/**
	 * ��֯ά�ȡ��Է���֯ά�ȡ��ֲ����ݡ��Է�febu���ݲ���Ϊ�� ��֯ά�ȡ��Է���֯ά�ȡ�������ͬ ��Ա����Ϊ��
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm) {
		super.validate(errors, actionForm);
		SegDefEditForm form = (SegDefEditForm) actionForm;
		if (form.getSegDefName().getBytes().length > 50) {
			errors.add(StringResource.getStringResource("msrdef0020"));// "�ֲ��������Ƴ��ȳ���50�ֽ�"));
		}
		if (isNull(form.getOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0021"));// ��֯ά�ȱ���ѡ��
		} else if (form.getOrgDimPK().equals(form.getTradeOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0022"));// ��֯ά����Է���֯ά�Ȳ�����ͬ
		}
		if (isNull(form.getTradeOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0024"));// �Է���֯ά�ȱ���ѡ��
		}

		if (isNull(form.getOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0023"));// �ֲ��������ݱ���ѡ��
		} else if (form.getOrgDimField().equals(form.getTradeOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0046"));// ���ӷֲ�����������Է��ֲ������������õ�ά�Ȳ�����ͬ
		}
		if (isNull(form.getTradeOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0025"));// �Է��ֲ��������ݱ���ѡ��
		}
		
		String[] strOrgMembers = getOrgMembers();
		if (strOrgMembers == null) {
			errors.add(StringResource.getStringResource("msrdef0026"));// �ֲ���Χ����ѡ��
		}
		if (isNull(form.getSegQueryDirPK())) {
			errors.add(StringResource.getStringResource("msrdef0027"));// �ֲ���Ӧ�Ĳ�ѯĿ¼����ѡ��
		}
		if (isNull(form.getSegReportDirPK())) {
			errors.add(StringResource.getStringResource("msrdef0028"));// �ֲ�����Ŀ¼����ѡ��
		}
		// �жϲ�ѯ�����ƺͱ����Ƿ��ظ�
		checkQuery(errors, form);
		// �жϱ�������ƺͱ����Ƿ��ظ�
		checkReport(errors, form);
		// �жϷֲ����������Ƿ��ظ�
		String strDirPK = form.getDirPK();
		try {
			SegDefVO oldDefVO = SegDefSrv.getInstance().getSegDefVOByName(strDirPK, form.getSegDefName());
			if (oldDefVO != null && oldDefVO.getSegDefPK().equals(form.getSegDefPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0001"));// "��ͬ���Ƶķֲ������Ѿ�����"
			}
		} catch (SegRepException e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * �жϲ�ѯ�����ƺͱ����Ƿ��ظ�
	 * 
	 * @param errors
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	private void checkQuery(ArrayList errors, SegDefEditForm form) {
		try {
			if (form.getSegQueryDirPK().equals(IResMngConsants.VIRTUAL_ROOT_ID)) {
				errors.add(StringResource.getStringResource("msrdef0041"));// ��ѯĿ¼����ѡ�����Ŀ¼
			}
			QuerySrv qrySrv = QuerySrv.getInstance();
			QueryModelVO qryVO = qrySrv.getQueryModelVOByCode(form.getSegQueryDirPK(), form.getSegQueryName());
			if (qryVO != null && qryVO.getID().equals(form.getSegReportQueryPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0029"));// "�ֲ����ֶ�Ӧ�Ĳ�ѯ�ı����ظ�"
			}
			qryVO = qrySrv.getQueryModelVOByName(form.getSegQueryDirPK(), form.getSegQueryName());
			if (qryVO != null && qryVO.getID().equals(form.getSegReportQueryPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0030"));// "�ֲ����ֶ�Ӧ�Ĳ�ѯ�������ظ�"
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * ��������\���벻���ظ�
	 * 
	 * @param errors
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	private void checkReport(ArrayList errors, SegDefEditForm form) {
		try {
			if (form.getSegReportDirPK().equals(IResMngConsants.VIRTUAL_ROOT_ID)) {
				errors.add(StringResource.getStringResource("msrdef0042"));// ����Ŀ¼����ѡ�����Ŀ¼
			}
			BIReportSrv repSrv = BIReportSrv.getInstance();
			ReportVO repVO = repSrv.getByCode(form.getSegReportDirPK(), form.getSegReportName());
			if (repVO != null && repVO.getID().equals(form.getSegReportPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0031"));// "�ֲ�����ı����ظ�");//""
			}
			repVO = repSrv.getByName(form.getSegReportDirPK(), form.getSegReportName());
			if (repVO != null && repVO.getID().equals(form.getSegReportPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0032"));// ""
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * �Ƿ�Ϊ��
	 * 
	 * @param strItem
	 * @return
	 */
	private boolean isNull(String strItem) {
		if (strItem == null || strItem.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ѯά���޸ĺ���Ҫ����������֯ά�ȵ���Ϣ
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onQueryChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		String strQueryPK = form.getQueryPK();
		try {
			String[][] strDimItems = getDimItems(strQueryPK);
			form.setDimItems(strDimItems);
			// //���ά�Ȳ�Ϊ��,��ȱʡ�����õ�һ��ά��Ϊ��֯ά�ȺͶԷ���֯ά��
			// if( strDimItems != null ){
			//				
			// }else{
			form.setOrgDimPK(null);
			form.setTradeOrgDimPK(null);
			form.setSegItemDimPK(null);
			form.setOrgDimField(null);
			form.setTradeOrgDimField(null);
			form.setOrgDimFieldItems(null);
			form.setOrgDimField(null);
			form.setTradeDimFieldItems(null);
			form.setTradeOrgDimField(null);

			// ��� ��֯��Աѡ��
			form.setListModel(getTree2ListModel(null, null));

			// �ɼ���ѯ\�ֲ���ѯĿ¼\�ֲ�����Ŀ¼���Ʊ��뱣��
			setQueryName(form, strQueryPK);
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}
			return new ActionForward(SegDefEditDlg.class.getName());

		} catch (Exception e) {
			AppDebug.debug(e);
			return new ErrorForward(e.getMessage());
		}
	}

	/**
	 * ��֯ά���޸ĺ��ҳ��ˢ��
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onOrgDimChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		// �õ���֯ά��
		try {
			String strOrgDimPK = form.getOrgDimPK();
			form.setDimItems(getDimItems(form.getQueryPK()));
			// �õ�ά���ֶ��б�
			form.setOrgDimFieldItems(getDimFieldItems(strOrgDimPK));
			form.setOrgDimField(null);
			form.setListModel(getTree2ListModel(strOrgDimPK, null));

			if (form.getTradeOrgDimPK() != null && form.getTradeOrgDimPK().length() > 0) {
				form.setTradeDimFieldItems(getDimFieldItems(form.getTradeOrgDimPK()));
			}

			setQueryName(form, form.getQueryPK());
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}

		} catch (Exception e) {
			AppDebug.debug(e);
			return new ErrorForward(e.getMessage());
		}
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * �Է���֯ά���޸ĺ��ҳ��ˢ��
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onTradeDimChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		try {
			// ��������form�е�һЩ����
			form.setDimItems(getDimItems(form.getQueryPK()));
			// �õ���֯ά��
			String strTradeOrgDimPK = form.getTradeOrgDimPK();
			// ���öԷ���֯ά���ֶ�
			form.setTradeDimFieldItems(getDimFieldItems(strTradeOrgDimPK));
			form.setTradeOrgDimField(null);
			form.setOrgDimFieldItems(getDimFieldItems(form.getOrgDimPK()));
			// ��Ҫ���ύ��
			form.setListModel(getTree2ListModel(form.getOrgDimPK(), getOrgMembers()));

			// ������ѯ��λ������
			setQueryName(form, form.getQueryPK());
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}

		} catch (Exception e) {
			AppDebug.debug(e);
			throw new WebException(e.getMessage());
		}
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * �жϲ���ʱ�½������޸�
	 * 
	 * @return
	 */
	private boolean isCreate() {
		String strOperType = getRequestParameter("operType");
		if (strOperType.equals("new")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �õ��ֲ���Χѡ��ģ��
	 * 
	 * @param strDimPK
	 * @param members
	 * @return
	 * @throws Exception
	 */
	private WebTree2ListModel getTree2ListModel(String strDimPK, String[] members) throws Exception {
		WebTree2ListModel model = new WebTree2ListModel();
		if (strDimPK != null) {
			DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(strDimPK);
			if (dimVO != null) {
				DimMemberSrv memberSrv = new DimMemberSrv(dimVO);
				DimMemberVO[] dimMemberVOs = memberSrv.getAll();
				WebTreeModel treeModel = new WebTreeModel(dimMemberVOs);
				disableNode((WebTreeNode) treeModel.getRoot());
				// model.setSrcTitle();
				model.setTreeModel(treeModel);
				if (members != null) {
					DimMemberVO[] memberVOs = memberSrv.getByID(members);
					String[][] strSelected = new String[memberVOs.length][];
					for (int i = 0; i < memberVOs.length; i++) {
						if (memberVOs[i] != null) {
							strSelected[i] = new String[] { members[i], memberVOs[i].getLabel() };
						}
					}
					model.setDestItems(strSelected);
				}

				// model.setDestTitle(destTitle);
			}
		}
		return model;
	}

	private void disableNode(WebTreeNode node) {
		int nChildren = node.getChildCount();
		if (nChildren > 0) {
			node.setUrl("");
			for (int i = 0; i < nChildren; i++) {
				WebTreeNode child = (WebTreeNode) node.getChildAt(i);
				disableNode(child);
			}
		}
	}

	/**
	 * ά���б�ѡ������
	 * 
	 * @param strQueryPK
	 * @return
	 */
	private String[][] getDimItems(String strQueryPK) {
		// �õ���ѯ��Ӧ��ά����Ϣ
		MetaDataVO[] metaDataVOs = QueryModelSrv.getDimFlds(strQueryPK);
		if (metaDataVOs != null) {
			ArrayList<String[]> aryItems = new ArrayList<String[]>();
			aryItems.add(new String[] { "", "" });
			for (int i = 0; i < metaDataVOs.length; i++) {
				try {
					DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(metaDataVOs[i].getPk_dimdef());
					if (dimVO != null) {
						aryItems.add(new String[] { dimVO.getDimID(), dimVO.getDimname() });
					}
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
			String[][] strDimItems = new String[aryItems.size()][];
			aryItems.toArray(strDimItems);
			return strDimItems;
		}
		return null;
	}

	/**
	 * �õ�ά���е�����ά��
	 * 
	 * @param strDimPK
	 * @return
	 * @throws Exception
	 */
	private String[][] getDimFieldItems(String strDimPK) throws Exception {
		ExPropertyVO[] propVOs = DimensionSrv.getInstance().getAllExPropertyVOs(strDimPK);
		if (propVOs != null) {
			ArrayList<String[]> aryRefDims = new ArrayList<String[]>();
			for (int i = 0; i < propVOs.length; i++) {
				if (propVOs[i].getType() == ExPropertyVO.TYPE_REF) {
					String strRefDimPK = propVOs[i].getRefTypePK();
					if (strRefDimPK != null) {
						aryRefDims.add(new String[] { strRefDimPK, propVOs[i].getName() });
					}
				}
			}
			if (aryRefDims.size() > 0) {
				String[][] strFieldItems = new String[aryRefDims.size()][];
				aryRefDims.toArray(strFieldItems);
				return strFieldItems;
			}
		}
		return null;
	}

	/**
	 * �����޸�ʱ�ĳ�ʼform
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void setVOToForm(SegDefVO segDefVO, SegDefEditForm form) throws Exception {

		form.setSegDefPK(segDefVO.getSegDefPK());
		form.setSegDefName(segDefVO.getSegDefName());
		form.setDirPK(segDefVO.getDirPK());
		form.setQueryPK(segDefVO.getQueryPK());
		// ��ѯ������
		setQueryName(form, segDefVO.getQueryPK());

		String strOrgDimPK = segDefVO.getOrgDimPK();
		form.setDimItems(getDimItems(segDefVO.getQueryPK()));
		form.setOrgDimPK(strOrgDimPK);
		form.setOrgDimFieldItems(getDimFieldItems(strOrgDimPK));

		form.setOrgDimField(segDefVO.getOrgDimField());

		form.setTradeOrgDimPK(segDefVO.getTradeOrgDimPK());
		form.setTradeDimFieldItems(getDimFieldItems(segDefVO.getTradeOrgDimPK()));
		form.setTradeOrgDimField(segDefVO.getTradeOrgDimField());

		// ��Ա��Χ
		form.setListModel(getTree2ListModel(strOrgDimPK, segDefVO.getScopeVO().getOrgMemberPKs()));

		// �洢λ��
		String strSegQueryPK = segDefVO.getSegReportQueryPK();
		// �õ���ѯ��Ŀ¼PK������
		QueryModelVO qryModelVO = QuerySrv.getInstance().getQueryModelVO(strSegQueryPK);
		if (qryModelVO != null) {
			form.setSegReportQueryPK(strSegQueryPK);
			setSegQueryDirName(form, qryModelVO.getPk_folderID());
			form.setSegQueryDirPK(qryModelVO.getPk_folderID());
			form.setSegQueryName(qryModelVO.getName());
		}

		String strSegRepPK = segDefVO.getSegReportPK();
		// �õ������Ŀ¼������
		ReportVO repVO = BIReportSrv.getInstance().getByID(strSegRepPK);
		if (repVO != null) {
			form.setSegReportPK(strSegRepPK);
			setSegReportDirName(form, repVO.getPk_folderID());
			form.setSegReportDirPK(repVO.getPk_folderID());
			form.setSegReportName(repVO.getName());
		}

	}

	/**
	 * ���ֲ����������Ƿ��Ѿ�����
	 * 
	 * @param segDefVO
	 * @return
	 */
	private boolean isSegRepDataExist(SegDefVO segDefVO) throws SegRepException {
		IBSegRepExecService srv = (IBSegRepExecService) NCLocator.getInstance().lookup(
				IBSegRepExecService.class.getName());
		return srv.isSegDataExist(segDefVO);
	}

	/**
	 * ������ѯ�Ͳ�ѯģ��
	 * 
	 * @param form
	 * @return
	 */
	private String createQuery(SegDefEditForm form) throws Exception {

		QueryModelVO queryModelVO = getQueryModelVO(form);
		//У���������ƵĺϷ��ԣ�ͬĿ¼�²����ظ���
		if(QuerySrv.getInstance().getQueryModelVOByCode(queryModelVO.getPk_folderID(), queryModelVO.getQuerycode()) != null)
			throw new WebException("mbiquery0001");// "��ѯ�����ظ�
		if(QuerySrv.getInstance().getQueryModelVOByCode(queryModelVO.getPk_folderID(), queryModelVO.getQuerycode()) != null)
			throw new WebException("mbiquery0002");// "��ѯ�����ظ�

		String strQueryPK = null;
		try {
			strQueryPK = QuerySrv.getInstance().createQueryModelVO(queryModelVO);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new WebException("msrdef0033");// "�޷������ֲ������Ӧ�Ĳ�ѯ�����������Ƿ��ظ�"
		}
		updateQueryQmd(form, queryModelVO);
		return strQueryPK;

	}

	/**
	 * ��������ͱ����ʽ
	 * 
	 * @param form
	 * @return
	 */
	private String createReport(SegDefEditForm form) throws Exception {
		ReportVO repVO = getReportVO(form);
		//У���������ƵĺϷ��ԣ�ͬĿ¼�²����ظ���
		if(BIReportSrv.getInstance().getByCode(repVO.getPk_folderID(), repVO.getReportcode()) != null)
			throw new WebException("mbirep0001");// "��������ظ�";
		if(BIReportSrv.getInstance().getByName(repVO.getPk_folderID(), repVO.getReportname()) != null)
			throw new WebException("mbirep0002");// "���������ظ�";

		repVO.setDefinition(RepModelMaker.getReportModel(form));
		return BIReportSrv.getInstance().createReport(repVO);
	}

	/**
	 * �޸Ĳ�ѯ�Ļ�����Ϣ�Ͳ�ѯ���
	 * 
	 * @param form
	 */
	private void updateQuery(SegDefEditForm form, boolean bUpdateModel) throws Exception {
		// ֻ����ģ�ͻ��߻�����Ϣ����Ҫ�޸ĵ�ʱ��,����Ҫ�޸Ĳ�ѯ
		// ��������
		String strQueryPK = form.getSegReportQueryPK();
		QueryModelVO oldQueryVO = QuerySrv.getInstance().getQueryModelVO(strQueryPK);
		boolean bUpdateBase = false;
		if (oldQueryVO == null) {
			throw new WebException("msrdef0034");// �ֲ����ֶ�Ӧ�Ĳ�ѯ�Ѿ���ɾ��");
		}
		// �Ƚϲ�ѯ���ƣ�Ŀ¼�Ƿ��޸�
		if (oldQueryVO.getPk_folderID().equals(form.getSegQueryDirPK()) == false
				|| oldQueryVO.getName().equals(form.getSegQueryName()) == false) {
			bUpdateBase = true;
		}

		if (bUpdateBase || bUpdateModel) {
			QueryModelVO queryVO = getQueryModelVO(form);
			if (bUpdateBase) {
				QuerySrv.getInstance().updateQueryModelVO(queryVO);
			}
			if (bUpdateModel) {
				BIQueryModelDef def = QueryModelMaker.getQueryModelDef(form, queryVO);
				queryVO.setDefinition(def);
				QuerySrv.getInstance().updateQmd(queryVO.getPrimaryKey(), def);
			}

		}
	}

	/**
	 * �޸Ĳ�ѯģ��
	 * 
	 * @param form
	 * @param modelVO
	 * @throws Exception
	 */
	private void updateQueryQmd(SegDefEditForm form, QueryModelVO modelVO) throws Exception {
		// ��������
		BIQueryModelDef def = QueryModelMaker.getQueryModelDef(form, modelVO);
		// ���²�ѯģ��
		QuerySrv.getInstance().updateQmd(modelVO.getID(), def);
	}

	/**
	 * ����QueryModelVO
	 * 
	 * @param form
	 * @return
	 */
	private QueryModelVO getQueryModelVO(SegDefEditForm form) {
		QueryModelVO queryModelVO = new QueryModelVO();
		queryModelVO.setID(form.getSegReportQueryPK());
		queryModelVO.setPk_folderID(ResMngToolKit.getVOIDByTreeObjectID(form.getSegQueryDirPK()));
		queryModelVO.setName(form.getSegQueryName());
		queryModelVO.setQuerycode(form.getSegQueryName());
		queryModelVO.setOwneruser(getCurUserInfo().getID());

		// ����Դ��Ҫ����,����Ϊ�ɼ�ģ��
		queryModelVO.setType(BIQueryConst.TYPE_INPUTMODEL);
		queryModelVO.setNote(StringResource.getStringResource("usrdef0038", new String[] { form.getSegDefName() }));

		return queryModelVO;
	}

	/**
	 * ����ReportVO
	 * 
	 * @param form
	 * @return
	 */
	private ReportVO getReportVO(SegDefEditForm form) {
		ReportVO repVO = new ReportVO();
		if (form.getSegReportPK() != null && form.getSegReportPK().length() > 0) {
			repVO.setID(form.getSegReportPK());
		}
		repVO.setName(form.getSegReportName());
		repVO.setReportcode(form.getSegReportName());
		repVO.setPk_folderID(ResMngToolKit.getVOIDByTreeObjectID(form.getSegReportDirPK()));
		repVO.setType(new Integer(1));// ��ά����
		repVO.setNote(StringResource.getStringResource("usrdef0039", new String[] { form.getSegDefName() }));
		repVO.setFk_querymodel(form.getSegReportQueryPK());

		return repVO;
	}

	/**
	 * �޸ı���
	 * 
	 * @param form
	 */
	private void updateReport(SegDefEditForm form, boolean bUpdateModel) throws Exception {
		// �жϻ�����Ϣ��û�и���
		String strRepPK = form.getSegReportPK();
		boolean bUpdateBase = false;
		ReportVO oldrepVO = BIReportSrv.getInstance().getByID(strRepPK);
		if (oldrepVO == null) {
			throw new WebException("msrdef0035");// ""
		}
		if (oldrepVO.getName().equals(form.getSegReportName()) == false
				|| oldrepVO.getPk_folderID().equals(form.getSegReportDirPK()) == false) {
			bUpdateBase = true;
		}

		if (bUpdateBase || bUpdateModel) {
			// ��������
			ReportVO repVO = getReportVO(form);
			if (bUpdateModel) {
				repVO.setDefinition(RepModelMaker.getReportModel(form));
			} else {
				repVO.setDefinition(oldrepVO.getDefinition());
			}
			BIReportSrv.getInstance().updateReport(repVO);
		}
	}

	/**
	 * �����ֲ�����
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void createSegDef(SegDefEditForm form) throws Exception {

		SegDefVO segDefVO = new SegDefVO();
		setFormToVO(segDefVO, form);

		// �½�����Ҫ�����½��ֲ������Ӧ�Ĳ�ѯ�ͱ���
		String strSegQueryPK = null;
		String strSegRepPK = null;
		try {
			strSegQueryPK = createQuery(form);
			form.setSegReportQueryPK(strSegQueryPK);
			strSegRepPK = createReport(form);
			segDefVO.setSegReportPK(strSegRepPK);
			segDefVO.setSegReportQueryPK(strSegQueryPK);

			SegDefSrv.getInstance().createSegDefVO(segDefVO);
		} catch (Exception e) {
			// clean up
			if (strSegQueryPK != null) {
				QuerySrv.getInstance().removeQueryModels(new String[] { strSegQueryPK });
			} else {
				QueryModelVO queryVO = QuerySrv.getInstance().getQueryModelVOByName(
						ResMngToolKit.getVOIDByTreeObjectID(form.getSegQueryDirPK()), form.getSegQueryName());
				if (queryVO != null) {
					QuerySrv.getInstance().removeQueryModels(new String[] { queryVO.getPrimaryKey() });
				}
			}
			if (strSegRepPK != null) {
				BIReportSrv.getInstance().removeReports(new String[] { strSegRepPK });
			}
			throw new WebException("msrdef0036", new String[] { e.getMessage() });// �½��ֲ�����ʧ�ܣ�
		}
	}

	/**
	 * �޸ķֲ�����
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void updateSegDef(SegDefEditForm form) throws Exception {
		SegDefVO segDefVO = new SegDefVO();
		setFormToVO(segDefVO, form);

		SegDefVO oldSegDefVO = SegDefSrv.getInstance().getSegDefVOByPK(form.getSegDefPK());
		if (oldSegDefVO != null) {
			// ��Ҫ�жϷֲ���Ӧ�Ĳ�ѯ�Ƿ�������
			if (isSegRepDataExist(oldSegDefVO)) {
				throw new WebException("msrdef0037");// �ֲ������Ѿ����ɣ������޸�
			}

			// ����ֲ��е�ά����Ϣ�����仯������Ҫ�޸Ĳ�ѯ�ͱ���
			boolean bDimChanged = isDimChanged(oldSegDefVO, form);
			updateQuery(form, bDimChanged);
			if (bDimChanged == false) {
				bDimChanged = isOrgMembersChanged(oldSegDefVO, form);
			}
			updateReport(form, bDimChanged);
			segDefVO.setSegReportPK(form.getSegReportPK());
			segDefVO.setSegReportQueryPK(form.getSegReportQueryPK());
			SegDefSrv.getInstance().updateSegDefVO(segDefVO);
		} else {
			throw new WebException("msrdef0019");// �ֲ������Ѿ���ɾ�����޷�����
		}
	}

	/**
	 * ����VO
	 * 
	 * @param segDefVO
	 * @param form
	 * @throws Exception
	 */
	private void setFormToVO(SegDefVO segDefVO, SegDefEditForm form) throws Exception {
		if (form.getSegDefPK() != null && form.getSegDefPK().length() > 0) {
			segDefVO.setSegDefPK(form.getSegDefPK());
		}
		segDefVO.setQueryPK(form.getQueryPK());
		segDefVO.setDirPK(form.getDirPK());
		segDefVO.setSegDefName(form.getSegDefName());
		segDefVO.setOrgDimPK(form.getOrgDimPK());
		// �õ��ֶε�����ά��PK
		segDefVO.setOrgDimField(form.getOrgDimField());
		segDefVO.setTradeOrgDimPK(form.getTradeOrgDimPK());

		segDefVO.setTradeOrgDimField(form.getTradeOrgDimField());
		SegDefScopeVO scopeVO = new SegDefScopeVO();

		scopeVO.setOrgMemberPKs(getOrgMembers());
		segDefVO.setScopeVO(scopeVO);
	}

	/**
	 * �ж�ά�ȵ���Ϣ�Ƿ�ı�
	 * 
	 * @param segDefVO
	 * @param form
	 * @return
	 */
	private boolean isDimChanged(SegDefVO segDefVO, SegDefEditForm form) {
		if (segDefVO.getQueryPK().equals(form.getQueryPK()) == false
				|| segDefVO.getOrgDimPK().equals(form.getOrgDimPK()) == false
				|| segDefVO.getOrgDimField().equals(form.getOrgDimField()) == false
				|| segDefVO.getTradeOrgDimPK().equals(form.getTradeOrgDimPK()) == false
				|| segDefVO.getTradeOrgDimField().equals(form.getTradeOrgDimField()) == false) {
			return true;
		}
		return false;
	}

	/**
	 * �жϷֲ���Χ�Ƿ�ı�
	 * 
	 * @param segDefVO
	 * @param form
	 * @return
	 */
	private boolean isOrgMembersChanged(SegDefVO segDefVO, SegDefEditForm form) {
		SegDefScopeVO scopeVO = segDefVO.getScopeVO();
		if (scopeVO != null) {
			String[] strOldMemberIDs = scopeVO.getOrgMemberPKs();
			String[] strMemberIDs = form.getOrgDimMembers();
			if (strOldMemberIDs == null && strMemberIDs == null) {
				return true;
			}
			if (strOldMemberIDs.length == strMemberIDs.length) {
				Hashtable<String, String> hashIDs = new Hashtable<String, String>();
				for (int i = 0; i < strOldMemberIDs.length; i++) {
					hashIDs.put(strOldMemberIDs[i], strOldMemberIDs[i]);
				}
				for (int i = 0; i < strMemberIDs.length; i++) {
					if (hashIDs.get(strMemberIDs[i]) == null) {
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	/**
	 * �õ��ֲ���Χ
	 * 
	 * @return
	 */
	private String[] getOrgMembers() {
		String selectIDs = getRequestParameter("strOrgDimMembers");
		if (selectIDs != null && selectIDs.length() > 0) {
			return selectIDs.split(WebGlobalValue.FLAG_SPLIT);
		}
		return null;
	}

	/**
	 * ���ò�ѯ����
	 * 
	 * @param form
	 * @param strQueryPK
	 * @throws Exception
	 */
	private void setQueryName(SegDefEditForm form, String strQueryPK) throws Exception {
		QueryModelVO queryVO = QuerySrv.getInstance().getQueryModelVO(strQueryPK);
		if (queryVO != null) {
			form.setQueryName(queryVO.getQuerycode());
		}
	}

	/**
	 * ���ò�ѯĿ¼����
	 * 
	 * @param form
	 * @param strDirPK
	 * @throws Exception
	 */
	private void setSegQueryDirName(SegDefEditForm form, String strDirPK) throws Exception {
		IResTreeObject dirVO = QuerySrv.getInstance().getDirByID(strDirPK);
		if (dirVO != null) {
			form.setSegQueryDirName(dirVO.getLabel());
		}
	}

	/**
	 * ���ñ���Ŀ¼����
	 * 
	 * @param form
	 * @param strDirPK
	 * @throws Exception
	 */
	private void setSegReportDirName(SegDefEditForm form, String strDirPK) throws Exception {
		IResTreeObject dirVO = BIReportSrv.getInstance().getDirByID(strDirPK);
		if (dirVO != null) {
			form.setSegRepDirName(dirVO.getLabel());
		}
	}

}
